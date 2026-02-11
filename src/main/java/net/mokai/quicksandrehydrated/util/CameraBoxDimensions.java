package net.mokai.quicksandrehydrated.util;

// Dimensions of a small rectangle around the player's viewport
// We use this for both screen cover effects and drowning
// Putting the constants in this file since both of those systems need to be in sync
public class CameraBoxDimensions {
	public static double HALF_WIDTH = 0.09;
	public static double HALF_HEIGHT = 0.05;

	public static double FULL_WIDTH = HALF_WIDTH * 2;
	public static double FULL_HEIGHT = HALF_HEIGHT * 2;
}
