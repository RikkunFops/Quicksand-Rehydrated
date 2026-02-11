package net.mokai.quicksandrehydrated.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mokai.quicksandrehydrated.block.quicksands.core.QuicksandBase;
import net.mokai.quicksandrehydrated.entity.data.QuicksandEffectManager;
import net.mokai.quicksandrehydrated.entity.entityQuicksandVar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


import javax.annotation.Nullable;

import static net.mokai.quicksandrehydrated.util.ModTags.Blocks.QUICKSAND_DROWNABLE;

@Mixin(Entity.class)
public abstract class SlowdownMixin implements entityQuicksandVar {

    @Shadow
    protected Vec3 stuckSpeedMultiplier;



    final QuicksandEffectManager quicksandEffectManager = new QuicksandEffectManager();
    public QuicksandEffectManager getQuicksandEffectManager() {return this.quicksandEffectManager;}


    Vec3 quicksandMultiplier = Vec3.ZERO;



    public Vec3 getQuicksandMultiplier() {return this.quicksandMultiplier;}
    public void setQuicksandMultiplier(Vec3 set) {this.quicksandMultiplier = set;}
    public void multiOrSetQuicksandMultiplier(Vec3 set) {
        if (this.quicksandMultiplier.length() > 1.0E-6D) {
            this.quicksandMultiplier = this.quicksandMultiplier.multiply(set);
        }
        else {
            this.quicksandMultiplier = set;
        }
    }

    Vec3 quicksandAdditive = Vec3.ZERO;
    public Vec3 getQuicksandAdditive() {return this.quicksandAdditive;}
    public void setQuicksandAdditive(Vec3 set) {this.quicksandAdditive = set;}
    public void addQuicksandAdditive(Vec3 set) {
        this.quicksandAdditive = this.quicksandAdditive.add(set);
    }
    public void multiplyQuicksandAdditive(Vec3 set) {
        this.quicksandAdditive = this.quicksandAdditive.multiply(set);
    }

    Vec3 tugPosition = Vec3.ZERO;
    public Vec3 getTugPosition() {return this.tugPosition;}
    public void setTugPosition(Vec3 set) {this.tugPosition = set;}

    Vec3 tugMomentum = Vec3.ZERO;
    public Vec3 getTugMomentum() {return this.tugMomentum;}
    public void setTugMomentum(Vec3 set) {this.tugMomentum = set;}


    @Shadow public abstract boolean shouldRender(double p_20296_, double p_20297_, double p_20298_);





    public boolean changed = false;
    public double horizontal = Double.MAX_VALUE;
    public double vertical = Double.MAX_VALUE;


    public boolean inQuicksand = false;
    public boolean getInQuicksand() {
        return this.inQuicksand;
    }
    public void setInQuicksand(boolean set) {
        this.inQuicksand = set;
    }



    public boolean enterQuicksandFlag = false;
    public boolean getEnterQuicksandFlag() {
        return this.enterQuicksandFlag;
    }
    public void setEnterQuicksandFlag(boolean set) {
        this.enterQuicksandFlag = set;
    }




//    @Inject(method = "collide", at = @At("HEAD"))
//    private void collide(Vec3 pVec, CallbackInfoReturnable<Vec3> cir) {
//
//        Entity thisEntity = (Entity)(Object)this;
//        BlockState test = thisEntity.getFeetBlockState();
//
//        this.onGround = this.onGround || test.getBlock() instanceof QuicksandBase;
//
//    }

    public boolean stuckBlockValid(BlockPos pPos, Entity pEntity) {
        // Check if the block is quicksand
        BlockState pState = pEntity.level().getBlockState(pPos);
        
        // Check that the block is not air before considering it as quicksand.
        if (pState.isAir() || !(pState.getBlock() instanceof QuicksandBase quicksand)) {
            return false;
        }
        
        // Verify that the entity is not skipping
        if (pEntity.getDeltaMovement().y > 0.05) {
            return false;
        }

        // Adjust only the surface block to be shorter
        BlockState blockAbove = pEntity.level().getBlockState(pPos.above());
        boolean isSurface = blockAbove.isAir() || !(blockAbove.getBlock() instanceof QuicksandBase);
        double quicksandHeight = isSurface ? 1 - quicksand.getOffset(pState) : 1;

        AABB blockBoundingBox = new AABB(pPos).setMaxY(pPos.getY() + quicksandHeight);
        AABB entityBoundingBox = pEntity.getBoundingBox();

        return entityBoundingBox.intersects(blockBoundingBox);
    }



    @Nullable
    public BlockPos getStuckBlock(Entity pEntity) {
        // Find the quicksand block the entity is in
        Level level = pEntity.level();
        
        // Get the entity's position and dimensions
        double entityX = pEntity.getX();
        double entityY = pEntity.getY();
        double entityZ = pEntity.getZ();
        double entityHeight = pEntity.getBbHeight();
        double entityWidth = pEntity.getBbWidth() / 2.0;
        
        // Define the area to check for quicksand blocks
        int minX = (int) Math.floor(entityX - entityWidth);
        int maxX = (int) Math.floor(entityX + entityWidth);
        int minY = (int) Math.floor(entityY - 0.1); // Check slightly below feet
        int maxY = (int) Math.floor(entityY + entityHeight); // Check the entire entity's height
        int minZ = (int) Math.floor(entityZ - entityWidth);
        int maxZ = (int) Math.floor(entityZ + entityWidth);
        
        // First, check the block at the entity's feet (most common case)
        BlockPos feetPos = new BlockPos(
            (int) Math.floor(entityX),
            (int) Math.floor(entityY - 0.1),
            (int) Math.floor(entityZ)
        );
        
        // Check if the block at the entity's feet is quicksand
        BlockState feetState = level.getBlockState(feetPos);
        // Check that the block is not air before considering it as quicksand.
        if (!feetState.isAir() && feetState.getBlock() instanceof QuicksandBase && stuckBlockValid(feetPos, pEntity)) {
            return feetPos;
        }
        
        // Then check the block the entity is in (second most common case)
        BlockPos entityPos = new BlockPos(
            (int) Math.floor(entityX),
            (int) Math.floor(entityY),
            (int) Math.floor(entityZ)
        );
        
        // Check if the block the entity is in is quicksand
        BlockState entityState = level.getBlockState(entityPos);
        // Check that the block is not air before considering it as quicksand.
        if (!entityState.isAir() && entityState.getBlock() instanceof QuicksandBase && stuckBlockValid(entityPos, pEntity)) {
            return entityPos;
        }
        
        // If the common cases did not work, check the upper body first.
        // This is important to ensure that the quicksand effect also applies to the upper part.
        
        // First, check the upper part of your body (from the middle up).
        int midY = (int) Math.floor(entityY + entityHeight * 0.5);
        for (int y = midY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    
                    // Skip positions already checked
                    if (pos.equals(feetPos) || pos.equals(entityPos)) {
                        continue;
                    }
                    
                    BlockState state = level.getBlockState(pos);
                    if (state.getBlock() instanceof QuicksandBase && stuckBlockValid(pos, pEntity)) {
                        return pos;
                    }
                }
            }
        }
        
        // Then check the lower part of the body (from the middle down)
        for (int y = minY; y < midY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    
                    // Skip positions already checked
                    if (pos.equals(feetPos) || pos.equals(entityPos)) {
                        continue;
                    }
                    
                    BlockState state = level.getBlockState(pos);
                    if (state.getBlock() instanceof QuicksandBase && stuckBlockValid(pos, pEntity)) {
                        return pos;
                    }
                }
            }
        }
        
        return null;
    }



    @Inject(method = "tick", at = @At("HEAD"))
    private void tickEnd(CallbackInfo ci) {
        Entity thisEntity = (Entity)(Object)this;
        entityQuicksandVar qsE = (entityQuicksandVar)(Object)this;

        // Do not apply quicksand effects if the entity is jumping
        if (thisEntity.getDeltaMovement().y > 0.05) {
            return;
        }

        // Reset the inQuicksand flag at the start of each tick
        boolean wasInQuicksand = qsE.getInQuicksand();
        qsE.setInQuicksand(false);

        // Check if the entity is in quicksand
        BlockPos bp = getStuckBlock(thisEntity);
        
        if (bp != null && stuckBlockValid(bp, thisEntity)) {
            // Entity is in quicksand
            qsE.setInQuicksand(true);
            
            Level eLevel = thisEntity.level();
            BlockState bs = eLevel.getBlockState(bp);
            
            // Check that the block is not air
            if (!bs.isAir() && bs.getBlock() instanceof QuicksandBase qs) {
                // Only trigger firstTouch when entering quicksand for the first time
                if (!qsE.getEnterQuicksandFlag()) {
                    qsE.setEnterQuicksandFlag(true);
                    qs.firstTouch(bp, thisEntity, eLevel);
                }
                
                // Apply quicksand effects
                qs.applyQuicksandEffects(bs, eLevel, bp, thisEntity);
            }
        } else {
            // Not in quicksand
            if (qsE.getEnterQuicksandFlag()) {
                // Reset the enter flag when leaving quicksand
                qsE.setEnterQuicksandFlag(false);
            }
        }
    }



    /**
     *
     * @param pState
     * @param spd
     * @author Mokai
     * @reason makeStuckInBlock simply overwrote stuckSpeedMultiplier, which lead to instances of directional priority; now, it only accepts the strongest slowdown applied that tick.
     */

    @Overwrite
    public void makeStuckInBlock(BlockState pState, Vec3 spd) {

        // It's not that easy.

        if (horizontal > spd.x() || horizontal > spd.y() || !changed) {

            if (!changed) { horizontal = spd.x(); vertical = spd.y(); }
            if (horizontal > spd.x()) { horizontal = spd.x(); }
            if (vertical > spd.y()) { vertical = spd.y(); }
            changed = true;
            stuckSpeedMultiplier = new Vec3(horizontal, vertical, horizontal);
        }
        Entity e = (Entity)(Object) this;
        e.resetFallDistance();

        // It's that easy.
    }


    @Inject(method = "collide", at = @At("HEAD"))
    private void collide(Vec3 pVec, CallbackInfoReturnable<Vec3> cir) {
        Entity thisEntity = (Entity)(Object)this;
        
        // Do not modify onGround if the entity is jumping or falling
        if (thisEntity.getDeltaMovement().y > 0.05) {
            // If the entity is jumping, do nothing.
            return;
        }
        
        // Check whether the entity is on a block of quicksand
        BlockState test = thisEntity.getFeetBlockState();
        if (test.getTags().toList().contains(QUICKSAND_DROWNABLE)) {
            // Set onGround to true only if the entity is actually in contact with the block
            // and it's not jumping
            if (thisEntity.getDeltaMovement().y <= 0.0) {
                thisEntity.setOnGround(true);
            }
        }
    }

    @Inject(method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", at = @At("TAIL"))
    public void emperorsNewMove(MoverType mv, Vec3 spd, final CallbackInfo ci)
    {
        changed = false;
    }

    @Inject(
            method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;maybeBackOffFromEdge(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/MoverType;)Lnet/minecraft/world/phys/Vec3;",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void quicksandModifier(MoverType p_19973_, Vec3 p_19974_, CallbackInfo ci, @Local LocalRef<MoverType> moverTypeLocalRef, @Local LocalRef<Vec3> Vec3localRef) {
        Entity thisEntity = (Entity)(Object)this;
        entityQuicksandVar qsEntity = (entityQuicksandVar)(Object)this;
        
        // Check if the entity is in quicksand - early return if it is not
        if (!qsEntity.getInQuicksand()) {
            return;
        }
        
        // Obtain the quicksand block where the entity is located.
        BlockPos stuckBlockPos = qsEntity.getStuckBlock(thisEntity);
        if (stuckBlockPos == null) {
            return;
        }
        
        // Get the block and block status
        Level level = thisEntity.level();
        BlockState blockState = level.getBlockState(stuckBlockPos);
        Block block = blockState.getBlock();
        
        // Check that it is a block of quicksand.
        if (!(block instanceof QuicksandBase quicksandBlock)) {
            return;
        }
        
        // Calculate the depth of the entity in quicksand
        double depth = quicksandBlock.getDepth(level, stuckBlockPos, thisEntity);
        
        // Apply the motion effects of quicksand
        quicksandBlock.quicksandMomentum(blockState, level, stuckBlockPos, thisEntity, depth);
        
        // Change the movement vector
        Vec3 currentVec = Vec3localRef.get();
        
        // Optimization: Combine all changes into a single operation
        double xMod = currentVec.x;
        double yMod = currentVec.y;
        double zMod = currentVec.z;
        boolean modified = false;
        
        // Apply vertical resistance to simulate sinking
        if (yMod < 0) {
            // Sinking factor
            yMod *= 0.7;
            modified = true;
        }
        
        // Apply horizontal resistance only if there is significant movement.
        boolean hasHorizontalMovement = Math.abs(xMod) > 0.01 || Math.abs(zMod) > 0.01;
        if (hasHorizontalMovement) {
            // Calculate a resistance factor based on depth
            double resistanceFactor = Math.max(0.1, 1.0 - (depth * 0.5));
            xMod *= resistanceFactor;
            zMod *= resistanceFactor;
            modified = true;
        }
        
        // If the entity is jumping, reduce the jump height.
        if (yMod > 0.1) {
            // Reduce the height of the jump based on the depth
            double jumpFactor = Math.max(0.1, 1.0 - (depth * 0.7));
            yMod *= jumpFactor;
            modified = true;
        }
        
        // Apply changes only if necessary
        if (modified) {
            Vec3localRef.set(new Vec3(xMod, yMod, zMod));
        }
    }

    @ModifyVariable(
            method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(value = "HEAD"),
            index = 2,
            argsOnly = true
    )
    private Vec3 injected(Vec3 momentum) {
        // Optimization: Avoid unnecessary casting and operations
        entityQuicksandVar QuicksandVarEntity = (entityQuicksandVar)(Object)this;

        // Optimization: Check if there are any changes to apply before proceeding
        Vec3 quicksandMod = QuicksandVarEntity.getQuicksandMultiplier();
        Vec3 quicksandAdd = QuicksandVarEntity.getQuicksandAdditive();
        
        boolean hasMultiplier = quicksandMod.length() > 1.0E-6D;
        boolean hasAdditive = quicksandAdd.length() > 1.0E-6D;
        
        // If there are no changes to apply, return the original momentum.
        if (!hasMultiplier && !hasAdditive) {
            return momentum;
        }
        
        // Apply the multiplier if present
        if (hasMultiplier) {
            momentum = momentum.multiply(quicksandMod);
            QuicksandVarEntity.setQuicksandMultiplier(Vec3.ZERO);
        }

        // Apply the additive if present
        if (hasAdditive) {
            momentum = momentum.add(quicksandAdd);
            QuicksandVarEntity.setQuicksandAdditive(Vec3.ZERO);
        }

        // Update deltaMovement only if necessary
        Entity thisEntity = (Entity)(Object)this;
        thisEntity.setDeltaMovement(momentum);
        
        return momentum;
    }



    public boolean entityCanStepOut(Entity pEntity) {
        // Use the past entity as a parameter, not thisEntity
        entityQuicksandVar QuicksandVarEntity = (entityQuicksandVar)(Object)pEntity;

        BlockPos stuckBlockPos = QuicksandVarEntity.getStuckBlock(pEntity);
        if (stuckBlockPos != null) {
            Block stuckBlock = pEntity.level().getBlockState(stuckBlockPos).getBlock();

            if (stuckBlock instanceof QuicksandBase) {
                QuicksandBase qsBlock = (QuicksandBase) stuckBlock;
                double depth = qsBlock.getDepth(pEntity.level(), stuckBlockPos, pEntity);
                return qsBlock.canStepOut(depth);
            }
        }
        return false;
    }



    @Inject(method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", at = @At("TAIL"))
    private void onMoveAfterSetOnGround(MoverType pType, Vec3 pPos, CallbackInfo ci) {
        Entity thisEntity = (Entity)(Object)this;
        
        // Do not modify onGround if the entity is jumping
        if (thisEntity.getDeltaMovement().y > 0.05) {
            return;
        }
        
        if (entityCanStepOut(thisEntity)) {
            boolean notFlyingPlayer = true;

            if (thisEntity instanceof Player) {
                Player p = (Player) thisEntity;
                notFlyingPlayer = !p.getAbilities().flying;
            }

            // Commented to avoid problems with jumping
            // if (notFlyingPlayer) {
            //     thisEntity.setOnGround(true);
            // }
        }
    }

}
