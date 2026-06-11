package net.mokai.quicksandrehydrated.worldgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

/**
 * Placement modifier that determines whether a quicksand pool should be generated at a specific location.
 * It uses a combination of criteria to ensure that puddles are generated in realistic locations.
 */
public class QuicksandPitPlacement extends PlacementFilter {
    // Singleton instance
    public static final QuicksandPitPlacement INSTANCE = new QuicksandPitPlacement();
    // Codec for serialization
    public static final Codec<QuicksandPitPlacement> CODEC = Codec.unit(() -> INSTANCE);

    private QuicksandPitPlacement() {
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        WorldGenLevel level = context.getLevel();
        
        // 1. Find the surface area
        int surfaceY = findSurfaceY(level, pos);
        if (surfaceY == -1) return false;
        
        // Verify that the surface is above sea level (y=62)
        if (surfaceY < 62) {
            return false;
        }
        
        BlockPos surfacePos = new BlockPos(pos.getX(), surfaceY, pos.getZ());
        
        // 2. Check that the ground is sufficiently level.
        // Let's simplify this check to increase the chances of generation.
        if (!isTerrainFlat(level, surfacePos, 6, random)) return false;
        
        // 3. Verify that we are at an appropriate altitude
        double heightProbability = getQuicksandProbabilityByHeight(surfaceY);
        if (random.nextDouble() > heightProbability) return false;
        
        // 4. Use a noise map for natural distribution
        // Let's simplify this check to increase the chances of generation.
        if (!shouldGenerateQuicksandPatch(pos.getX(), pos.getZ(), level.getSeed())) return false;
        
        // 5. Probability bonus if we are in a natural depression
        boolean inDepression = isNaturalDepression(level, surfacePos, 6);
        
        // 6. Probability bonus if we are on a drainage path
        // We are temporarily disabling this verification to increase the chances of generation.
        // boolean inDrainagePath = isWaterDrainagePath(level, surfacePos);
        boolean inDrainagePath = false;
        
        // Final decision with bonus for ideal positions
        // We maximize the base probability to ensure generation.
        double baseProbability = 1.0; // 100% di probabilità base
        
        // Debug printing
        System.out.println("[QuicksandPitPlacement] Checking placement at " + pos + ", surfaceY=" + surfaceY + 
                           ", heightProb=" + heightProbability + ", inDepression=" + inDepression + 
                           ", finalProb=" + baseProbability);
        
        // We force the generation by always returning true
        return true;
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModPlacementModifierTypes.QUICKSAND_PIT_PLACEMENT.get();
    }
    
    /**
     * Find the Y coordinate of the surface at a given position
     */
    private int findSurfaceY(WorldGenLevel level, BlockPos pos) {
        // Get the height of the surface
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
        
        // Check that there is a solid block
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), surfaceY, pos.getZ());
        
        // Find the first non-air block
        while (mutablePos.getY() > level.getMinBuildHeight()) {
            BlockState state = level.getBlockState(mutablePos);
            if (!state.isAir()) {
                // Check that there is a solid block underneath.
                BlockPos belowPos = mutablePos.below();
                BlockState belowState = level.getBlockState(belowPos);
                
                if (!belowState.isAir() && belowState.isSolid()) {
                    return mutablePos.getY();
                }
            }
            mutablePos.move(0, -1, 0);
        }
        
        return -1; // No valid surface found
    }
    
    /**
     * Check whether the ground is sufficiently level.
     */
    private boolean isTerrainFlat(WorldGenLevel level, BlockPos pos, int radius, RandomSource random) {
        int centerY = pos.getY();
        
        // Calculate the maximum slope in random directions
        double maxSlope = 0.0;
        int numChecks = 8; // Checks 8 random directions for efficiency
        
        for (int i = 0; i < numChecks; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            int dx = (int) Math.round(Math.cos(angle) * radius);
            int dz = (int) Math.round(Math.sin(angle) * radius);
            
            BlockPos checkPos = new BlockPos(pos.getX() + dx, 0, pos.getZ() + dz);
            int checkY = findSurfaceY(level, checkPos);
            
            if (checkY == -1) continue;
            
            // Calculate the slope in this direction
            double distance = Math.sqrt(dx*dx + dz*dz);
            double heightDiff = Math.abs(checkY - centerY);
            double slope = heightDiff / distance;
            
            maxSlope = Math.max(maxSlope, slope);
        }
        
        // Generate on land with acceptable slope
        return maxSlope < 0.35; // Less than 35% slope (increased from 20%)
    }
    
    /**
     * Calculate the probability of generation based on altitude
     */
    private double getQuicksandProbabilityByHeight(int y) {
        // Define an ideal altitude range for quicksand
        int minIdealHeight = 62; // Sea level
        int maxIdealHeight = 320; // The ideal range has increased dramatically
        
        // Maximum probability in the ideal range
        if (y >= minIdealHeight && y <= maxIdealHeight) {
            return 1.0; // 100% probability in the ideal range
        }
        
        // High probability even outside the ideal range
        return 0.9;
    }
    
    /**
     * Use a noise function to determine where to generate quicksand patches.
     */
    private boolean shouldGenerateQuicksandPatch(int x, int z, long seed) {
        // We force the generation by always returning true
        // This will ensure that quicksand pools are generated everywhere.
        return true;
    }
    
    /**
     * Check whether the location is in a natural depression.
     */
    private boolean isNaturalDepression(WorldGenLevel level, BlockPos pos, int radius) {
        // Check whether the location is in a natural depression.
        int centerY = pos.getY();
        int surroundingAvgY = 0;
        int count = 0;
        
        // Samples surrounding points at a distance greater than the radius of the pool
        for (int x = -radius*2; x <= radius*2; x += 2) {
            for (int z = -radius*2; z <= radius*2; z += 2) {
                // Skip points within the radius of the pool
                if (Math.sqrt(x*x + z*z) <= radius) continue;
                
                BlockPos checkPos = pos.offset(x, 0, z);
                int surfaceY = findSurfaceY(level, checkPos);
                
                if (surfaceY != -1) {
                    surroundingAvgY += surfaceY;
                    count++;
                }
            }
        }
        
        if (count == 0) return false;
        
        // Calculate the average height of the surrounding terrain
        surroundingAvgY /= count;
        
        // Check whether the center is lower than the surrounding ground.
        return centerY < surroundingAvgY - 1; // At least 1 block lower
    }
    
    /**
     * Check whether the location is in a water drainage path.
     */
    private boolean isWaterDrainagePath(WorldGenLevel level, BlockPos pos) {
        // Check if there are any water blocks nearby.
        boolean hasWaterNearby = false;
        int waterCheckRadius = 15;
        
        for (int x = -waterCheckRadius; x <= waterCheckRadius; x++) {
            for (int z = -waterCheckRadius; z <= waterCheckRadius; z++) {
                BlockPos checkPos = pos.offset(x, 0, z);
                int checkY = findSurfaceY(level, checkPos);
                if (checkY == -1) continue;
                
                checkPos = new BlockPos(checkPos.getX(), checkY, checkPos.getZ());
                BlockState state = level.getBlockState(checkPos);
                
                if (state.getBlock() == Blocks.WATER) {
                    hasWaterNearby = true;
                    break;
                }
            }
            if (hasWaterNearby) break;
        }
        
        if (!hasWaterNearby) return false;
        
        // Check whether the location is at a lower elevation than the surrounding area.
        // (potential drainage path)
        int lowerPointsCount = 0;
        int higherPointsCount = 0;
        int checkRadius = 8;
        
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int z = -checkRadius; z <= checkRadius; z++) {
                if (x == 0 && z == 0) continue;
                
                BlockPos checkPos = pos.offset(x, 0, z);
                int checkY = findSurfaceY(level, checkPos);
                if (checkY == -1) continue;
                
                if (checkY > pos.getY()) {
                    higherPointsCount++;
                } else if (checkY < pos.getY()) {
                    lowerPointsCount++;
                }
            }
        }
        
        // If there are more high points than low points, we are on a potential drainage path.
        return higherPointsCount > lowerPointsCount * 1.5;
    }
}