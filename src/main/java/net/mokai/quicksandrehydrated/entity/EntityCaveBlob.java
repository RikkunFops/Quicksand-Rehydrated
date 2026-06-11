package net.mokai.quicksandrehydrated.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;          // NOTE: world.entity.*, not client.animation
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class EntityCaveBlob extends Slime {

    public final AnimationState idleAnimationState  = new AnimationState();
    public final AnimationState jumpAnimationState  = new AnimationState();

    private static final int JUMP_ANIM_TICKS = 45;
    private int  jumpAnimEndTick = 0;
    private boolean wasOnGround  = true;

    // size/split control
    private static final int TARGET_SIZE = 4;
    private boolean suppressSplitCheck = false;

    public EntityCaveBlob(EntityType<? extends Slime> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2)
                .add(Attributes.MOVEMENT_SPEED, .25)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f)
                .add(Attributes.ATTACK_DAMAGE, 2f)
                .build();
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level,
                                                  DifficultyInstance difficulty,
                                                  MobSpawnType spawnType,
                                                  @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData d = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (this.getSize() != TARGET_SIZE) this.setSize(TARGET_SIZE, true);
        return d;
    }

    @Override
    public void tick() {
        super.tick();

        // keep the blob at size 4
        if (!level().isClientSide && this.getSize() != TARGET_SIZE) {
            this.setSize(TARGET_SIZE, false);
        }

        final boolean onGroundNow = this.onGround();

        /* -------- start/stop the jump clip (server + client) -------- */
        if (wasOnGround && !onGroundNow && this.getDeltaMovement().y > 0.08D && this.tickCount >= jumpAnimEndTick) {
            jumpAnimEndTick = this.tickCount + JUMP_ANIM_TICKS;
            if (level().isClientSide) {
                jumpAnimationState.start(this.tickCount);
                // stop idle while in air so animations don't fight each other
                if (idleAnimationState.isStarted()) idleAnimationState.stop();
            }
        }

        // drive idle while grounded and not in jump
        if (level().isClientSide) {
            if (onGroundNow && this.tickCount >= jumpAnimEndTick) {
                idleAnimationState.startIfStopped(this.tickCount);
            }
            if (jumpAnimationState.isStarted() && this.tickCount >= jumpAnimEndTick) {
                jumpAnimationState.stop();
            }
        }

        // hard gate jumping while clip is active
        if (!level().isClientSide) {
            boolean clipActive = this.tickCount < jumpAnimEndTick;
            // disable jumping goals during the clip; re-enable after
            this.goalSelector.setControlFlag(Goal.Flag.JUMP, !clipActive);
        }

        wasOnGround = onGroundNow;
    }

    /* -------- No splitting & locked size -------- */

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide) suppressSplitCheck = true;
        try { super.die(source); }
        finally { suppressSplitCheck = false; }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        boolean guard = !this.level().isClientSide && reason == Entity.RemovalReason.KILLED;
        if (guard) suppressSplitCheck = true;
        try { super.remove(reason); }
        finally { suppressSplitCheck = false; }
    }

    @Override
    public int getSize() {
        return suppressSplitCheck ? 1 : super.getSize();
    }

    /* -------- Sounds -------- */

    @Override
    protected SoundEvent getJumpSound() {
        return SoundEvents.MAGMA_CUBE_JUMP;
    }

    @Override
    public float getVoicePitch() {
        return 0.5F;
    }
}
