package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.Shift;

import org.junit.Test;

public class ShiftTest {

	@Test
	public void testShiftGrayImageWithNoWrap() {
		int xOffset = 2;
		int yOffset = 3;
		GrayImage input = new GrayImage(10, 10);
		GrayImage expectedOutput = new GrayImage(10, 10);

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				expectedOutput.set(i, j, (byte) (i + xOffset + j + yOffset));
			}
		}

		Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
		GrayImage output = shift.apply(input);

		assertTrue(output.equals(expectedOutput));
	}

	@Test
	public void testShiftGrayImageWithWrapX() {
		int xOffset = 2;
		int yOffset = 3;
		GrayImage input = new GrayImage(10, 10);
		GrayImage expectedOutput = new GrayImage(10, 10);

		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				if (x + xOffset >= 0 && x + xOffset < 10 && y + yOffset >= 0 && y + yOffset < 10) {
					expectedOutput.set(x, y, input.get(x + xOffset, y + yOffset));
				} else {
					expectedOutput.set(x, y, input.get((x + xOffset) % 10, (y + yOffset) % 10));
				}
			}
		}

		Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
		GrayImage output = shift.apply(input);

		assertTrue(output.equals(expectedOutput));
	}

	@Test
	public void testShiftGrayImageWithWrapY() {
		int xOffset = 2;
		int yOffset = 2;
		GrayImage input = new GrayImage(10, 10);
		GrayImage expectedOutput = new GrayImage(10, 10);

		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				if (x + xOffset >= 0 && x + xOffset < 10 && y + yOffset >= 0 && y + yOffset < 10) {
					expectedOutput.set(x, y, input.get(x + xOffset, y + yOffset));
				} else {
					expectedOutput.set(x, y, input.get(x + xOffset, (y + yOffset) % 10));
				}
			}
		}

		Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
		GrayImage output = shift.apply(input);

		assertTrue(output.equals(expectedOutput));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testShiftGrayImageWithInvalidWrap() {
		int xOffset = 2;
		int yOffset = 3;
		Shift shift = new Shift(xOffset, yOffset, 2);
	}

	// Repeat the same tests for RealGrayImage, ColorImage, and RealColorImage

	// Test constructor with valid and invalid arguments

	// Test getters and setters

	// Test equals and hashCode methods
}