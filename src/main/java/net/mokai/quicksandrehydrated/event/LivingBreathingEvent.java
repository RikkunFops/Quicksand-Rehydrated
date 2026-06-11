package net.mokai.quicksandrehydrated.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.bus.api.Event;

import javax.annotation.Nullable;

public abstract class LivingBreathingEvent
{
    public static class LivingCanBreatheFluidEvent extends LivingEvent
    {
        private final FluidState state;

        public LivingCanBreatheFluidEvent(LivingEntity living, @Nullable FluidState stateIn)
        {
            super(living);
            this.state = stateIn;
        }

        @Nullable
        public FluidState state() { return this.state; }
    }
}