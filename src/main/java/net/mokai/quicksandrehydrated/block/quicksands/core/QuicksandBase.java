package net.mokai.quicksandrehydrated.block.quicksands.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

import net.neoforged.neoforge.registries.DeferredRegister;

import static net.mokai.quicksandrehydrated.util.ModTags.Blocks.QUICKSAND_DROWNABLE;
import static net.mokai.quicksandrehydrated.util.ModTags.Fluids.QUICKSAND_DROWNABLE_FLUID;
import static org.joml.Math.abs;
import static org.joml.Math.clamp;

public class QuicksandBase extends Block implements QuicksandInterface {

	private final Random rng = new Random();
	
	public QuicksandBehavior QSBehavior;
	public QuicksandBehavior getQuicksandBehavior() {return QSBehavior;}
	public QuicksandBase(Properties properties, QuicksandBehavior QuicksandBehavior) {
		super(properties);
		this.QSBehavior = QuicksandBehavior;
	}
	
    public String getSecretDeathMessage() {return QSBehavior.getSecretDeathMessage();}
    public double getSecretDeathMessageChance() {return QSBehavior.getSecretDeathMessageChance();}
    
    /**
     * The sinking speed, depending on the depth.
     * normalized against the vertSpeed, so this value will remain effective - even
     * if the quicksand is very thick.
     * @param depth The depth in blocks. <code>0</code> is exactly on surface level.
     * @return The sinking value. Lower value means slower sinking.
     */
    public double getSinkSpeed(double depth) {return QSBehavior.getSinkSpeed(depth);}
	
    /**
     * Horizontal movement speed depending on the depth.
     * Thickness - but inverse.
     * @param depth The depth of the object. <code>0</code> is exactly on surface level.
     * @return The inverse resistance when walking. <code>0</code> = very thick; <code>1</code> = very thin.
     */
    public double getWalkSpeed(double depth) {return QSBehavior.getWalkSpeed(depth);}
    
    /**
     * Vertical movement speed depending on the depth.
     * Same as <code>getWalk()</code>
     * @param depth The depth of the object.
     * @return The inverse resistance when moving up/down. <code>0</code> = very thick; <code>1</code> = very thin.
     */
    public double getVertSpeed(double depth) {return QSBehavior.getVertSpeed(depth);} //TODO: invert this back
    
    /** Used by position based wobble.
     * How strongly the quicksand pulls the player horizontally towards the wobble point. 1 is full strength.
     * @return Horizontal wobble strength. [0, 1]
     */
    public double getWobbleTugHorizontal(double depth) {return QSBehavior.getWobbleTugHorizontal(depth);}

    /** Used by position based wobble.
     * How strongly the quicksand pulls the player vertically towards the wobble point. 1 is full strength.
     * If not set, will default to equal the horizontal wobble strength.
     * @param depth
     * @return Vertical wobble strength. [0, 1]
     */
    public double getWobbleTugVertical(double depth) {return QSBehavior.getWobbleTugVertical(depth);}

    /** Used by both Wobble types.
     * In position based wobble, How quickly the wobble point approaches the player, as a percentage of the distance per tick.
     * In momentum based wobble, how much of the momentum is applied to the player, as a percentage of the total per tick.
     * You can think of this as how "sticky" the quicksand is.
     * 1.0 = no effect on player's movement
     * 0.0 = player effectively cannot move unless they manage to stop touching the QS.
     * @param depth
     * @return Vertical wobble strength. [0, 1]
     **/
    public double getWobbleMove(double depth) {return QSBehavior.getWobbleMove(depth);}

    /** Used by momentum based wobble.
     * How much the wobble momentum decays, percentage per tick.
     * 1.0 = momentum never decays
     * 0.0 = momentum completely decays every tick
     * @param depth
     * @return Vertical tug strength. [0, 1]
     **/
    public double getWobbleDecay(double depth) {return QSBehavior.getWobbleDecay(depth);}

    /** Used by momentum based wobble.
     * How much the wobble actually wobbles back and forth.
     * Mathematically, how much of the actual wobble momentum is added to the wobble momentum's *momentum* as a percentage per tick.
     * 1.0 = flips back and forth every single tick.
     * 0.0 = wobble does not change. just drifts the player in some random direction.
     * @param depth
     * @return Vertical tug strength. [0, 1]
     **/
    public double getWobbleRebound(double depth) {return QSBehavior.getWobbleRebound(depth);}

    /** Used by momentum based wobble.
     * How much of the entity's momentum is added to the wobble momentum.
     * You could think of it as how thick the quicksand is- separate from the Vert and Walk speeds.
     * Mathematically, what percent of the entity's momentum is added to the wobble momentum per tick.
     * 1.0 = 100% of the entity's momentum is added
     * 0.0 = 0% added, no effect.
     * @param depth
     * @return Vertical tug strength. [0, 1]
     **/
    public double getWobbleApply(double depth) {return QSBehavior.getWobbleApply(depth);}
    
    /** The lowest point the TugPoint will sink to.
     * @return The buoyancy depth.
     */
    public double getBuoyancyPoint() {return QSBehavior.getBuoyancyPoint();}

    /** If the player is above this height, they can simply jump out of the quicksand.
     * @param depth
     * @return true if the player should be allowed to jump out.
     */
    public boolean canStepOut(double depth) {return QSBehavior.canStepOut(depth);}

    /**
     * How far from the top of a block the surface actually is.
     * For example, Mud and Soulsand would use 0.125 (or 1/8th of a block) before sinking begins.
     * @param blockstate The BlockState.
     * @return distance from the top of the block.
     */
    public double getOffset(BlockState blockstate) {return QSBehavior.getOffset();}
    
}