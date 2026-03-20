package net.mokai.quicksandrehydrated.entity;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.mokai.quicksandrehydrated.registry.ModEntityTypes;
import net.mokai.quicksandrehydrated.util.EasingHandler;

import javax.annotation.Nullable;

public class EntityBubble extends Entity {

    public long startLife;
    public long endLife;
    public float scale;
    public float rotAngle;
    @Nullable
    public CompoundTag blockData;

    public static final EntityDataAccessor<BlockState> BLOCK_STATE =
            SynchedEntityData.defineId(EntityBubble.class, EntityDataSerializers.BLOCK_STATE);

    public EntityBubble(EntityType<? extends EntityBubble> type, Level world) {
        super(type, world);
    }

    public EntityBubble(EntityType<? extends EntityBubble> type, Level world, Vec3 pos, BlockState bs) {
        super(type, world);
        startPrep(world);
        this.setPos(pos);
        setBlockState(bs);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK_STATE, Blocks.DIRT.defaultBlockState()); }

    public void startPrep(Level world) {
        startLife = world.getGameTime();
        endLife = startLife + 60; // lifespan in ticks
        scale = 0.4f;
        rotAngle = (float) (Math.random() * 360);
    }

    public static EntityBubble spawn(Level level, Vec3 pos, BlockState blockState) {
        EntityBubble bubble = new EntityBubble(ModEntityTypes.BUBBLE.get(), level, pos, blockState);
        level.addFreshEntity(bubble);
        return bubble;
    }

    public void setBlockState(BlockState state) {
        this.entityData.set(BLOCK_STATE, state);
    }

    public BlockState getBlockState() {
        return this.entityData.get(BLOCK_STATE);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("BlockState", NbtUtils.writeBlockState(getBlockState()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK),
                tag.getCompound("BlockState")));
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    public double getSize() {
        long ct = this.level().getGameTime();
        return Math.sqrt(EasingHandler.reverse_interp(startLife, endLife, ct)) * scale;
    }



    @Override
    public void tick() {
        super.tick();

        if (this.level().getGameTime() > endLife) {
            this.kill();
        }

        // Spawn particles one tick before death
        if (this.level().getGameTime() + 1 > endLife) {
            spawnParticles();
        }
    }

    @Override
    public void kill() {
        this.playSound(SoundEvents.LAVA_POP);
        this.playSound(SoundEvents.LAVA_POP, 0.5f + (float) Math.random() * 0.25f, 0.6f);
        this.playSound(SoundEvents.SLIME_ATTACK, 0.15f, 0.6f);
        super.kill();
    }

    protected void spawnParticles() {
        BlockState bs = getBlockState();
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK, bs).setPos(this.getOnPos()),
                    this.getX(),
                    this.getY() + 0.5,
                    this.getZ(),
                    (Math.random() - 0.5) * 2.0,
                    0.25,
                    (Math.random() - 0.5) * 2.0
            );
        }
    }

}