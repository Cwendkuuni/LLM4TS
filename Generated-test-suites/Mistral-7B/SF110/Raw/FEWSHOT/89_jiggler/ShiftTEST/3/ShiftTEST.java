package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.types.*;
import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftGrayImagePositiveXAndY() {
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 10);
        inputImage.set(1, 1, (byte) 20);
        inputImage.set(2, 2, (byte) 30);
        inputImage.set(3, 3, (byte) 40);
        inputImage.set(4, 4, (byte) 50);
        inputImage.set(0, 1, (byte) 60);
        inputImage.set(0, 2, (byte) 70);
        inputImage.set(0, 3, (byte) 80);
        inputImage.set(0, 4, (byte) 90);
        inputImage.set(1, 0, (byte) 110);
        inputImage.set(1, 2, (byte) 120);
        inputImage.set(1, 3, (byte) 130);
        inputImage.set(1, 4, (byte) 140);
        inputImage.set(2, 0, (byte) 150);
        inputImage.set(2, 1, (byte) 160);
        inputImage.set(2, 2, (byte) 170);
        inputImage.set(2, 3, (byte) 180);
        inputImage.set(2, 4, (byte) 190);
        inputImage.set(3, 0, (byte) 210);
        inputImage.set(3, 1, (byte) 220);
        inputImage.set(3, 2, (byte) 230);
        inputImage.set(3, 3, (byte) 240);
        inputImage.set(3, 4, (byte) 250);
        inputImage.set(4, 0, (byte) 260);
        inputImage.set(4, 1, (byte) 270);
        inputImage.set(4, 2, (byte) 280);
        inputImage.set(4, 3, (byte) 290);
        inputImage.set(4, 4, (byte) 300);

        GrayImage outputImage = shift.apply(inputImage);
        byte expectedPixel = 12; // (x + xOffset) % X, (y + yOffset) % Y
        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                assertEquals(outputImage.get(x, y), inputImage.get(x + xOffset, y + yOffset));
                if (x == xOffset && y == yOffset) {
                    assertEquals(outputImage.get(x, y), (byte) expectedPixel);
                    expectedPixel++;
                }
            }
        }
    }

    @Test
    public void testShiftGrayImageNegativeXAndY() {
        int xOffset = -2;
        int yOffset = -3;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 10);
        inputImage.set(1, 1, (byte) 20);
        inputImage.set(2, 2, (byte) 30);
        inputImage.set(3, 3, (byte) 40);
        inputImage.set(4, 4, (byte) 50);
        inputImage.set(0, 1, (byte) 60);
        inputImage.set(0, 2, (byte) 70);
        inputImage.set(0, 3, (byte) 80);
        inputImage.set(0, 4, (byte) 90);
        inputImage.set(1, 0, (byte) 110);
        inputImage.set(1, 2, (byte) 120);
        inputImage.set(1, 3, (byte) 130);
        inputImage.set(1, 4, (byte) 140);
        inputImage.set(2, 0, (byte) 150);
        inputImage.set(2, 1, (byte) 160);
        inputImage.set(2, 2, (byte) 170);
        inputImage.set(2, 3, (byte) 180);
        inputImage.set(2, 4, (byte) 190);
        inputImage.set(3, 0, (byte) 210);
        inputImage.set(3, 1, (byte) 220);
        inputImage.set(3, 2, (byte) 230);
        inputImage.set(3, 3, (byte) 240);
        inputImage.set(3, 4, (byte) 250);
        inputImage.set(4, 0, (byte) 260);
        inputImage.set(4, 1, (byte) 270);
        inputImage.set(4, 2, (byte) 280);
        inputImage.set(4, 3, (byte) 290);
        inputImage.set(4, 4, (byte) 300);

        GrayImage outputImage = shift.apply(inputImage);
        byte expectedPixel = 238; // (X + xOffset + x) % X, (Y + yOffset + y) % Y
        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                assertEquals(outputImage.get(x, y), inputImage.get(x + xOffset, y + yOffset));
                if (x == -xOffset && y == -yOffset) {
                    assertEquals(outputImage.get(x, y), (byte) expectedPixel);
                    expectedPixel--;
                }
            }
        }
    }

    @Test
    public void testShiftGrayImageWrapXAndY() {
        int xOffset = 2;
        int yOffset = 3;
        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 10);
        inputImage.set(1, 1, (byte) 20);
        inputImage.set(2, 2, (byte) 30);
        inputImage.set(3, 3, (byte) 40);
        inputImage.set(4, 4, (byte) 50);
        inputImage.set(0, 1, (byte) 60);
        inputImage.set(0, 2, (byte) 70);
        inputImage.set(0, 3, (byte) 80);
        inputImage.set(0, 4, (byte) 90);
        inputImage.set(1, 0, (byte) 110);
        inputImage.set(1, 2, (byte) 120);
        inputImage.set(1, 3, (byte) 130);
        inputImage.set(1, 4, (byte) 140);
        inputImage.set(2, 0, (byte) 150);
        inputImage.set(2, 1, (byte) 160);
        inputImage.set(2, 2, (byte) 170);
        inputImage.set(2, 3, (byte) 180);
        inputImage.set(2, 4, (byte) 190);
        inputImage.set(3, 0, (byte) 210);
        inputImage.set(3, 1, (byte) 220);
        inputImage.set(3, 2, (byte) 230);
        inputImage.set(3, 3, (byte) 240);
        inputImage.set(3, 4, (byte) 250);
        inputImage.set(4, 0, (byte) 260);
        inputImage.set(4, 1, (byte) 270);
        inputImage.set(4, 2, (byte) 280);
        inputImage.set(4, 3, (byte) 290);
        inputImage.set(4, 4, (byte) 300);

        GrayImage outputImage = shift.apply(inputImage);
        byte expectedPixel = 39; // (x + xOffset) % X, (y + yOffset) % Y
        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                assertEquals(outputImage.get(x, y), inputImage.get(x + xOffset, y + yOffset));
                if (x == xOffset && y == yOffset) {
                    assertEquals(outputImage.get(x, y), (byte) expectedPixel);
                    expectedPixel++;
                }
                if (x + xOffset >= outputImage.X() && x + xOffset < 0) {
                    assertEquals(outputImage.get(x, y), inputImage.get((x + xOffset) % outputImage.X(), y + yOffset));
                }
                if (y + yOffset >= outputImage.Y() && y + yOffset < 0) {
                    assertEquals(outputImage.get(x, y), inputImage.get(x + xOffset, (y + yOffset) % outputImage.Y()));
                }
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageNegativeXAndYOutOfBounds() {
        int xOffset = -5;
        int yOffset = -5;
        Shift shift = new Shift(xOffset, yOffset, Shift.NOWRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 10);
        inputImage.set(1, 1, (byte) 20);
        inputImage.set(2, 2, (byte) 30);
        inputImage.set(3, 3, (byte) 40);
        inputImage.set(4, 4, (byte) 50);
        shift.apply(inputImage);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShiftGrayImageNegativeXAndYWrapOutOfBounds() {
        int xOffset = -5;
        int yOffset = -5;
        Shift shift = new Shift(xOffset, yOffset, Shift.WRAP);
        GrayImage inputImage = new GrayImage(5, 5);
        inputImage.set(0, 0, (byte) 10);
        inputImage.set(1, 1, (byte) 20);
        inputImage.set(2, 2, (byte) 30);
        inputImage.set(3, 3, (byte) 40);
        inputImage.set(4, 4, (byte) 50);
        shift.apply(inputImage);
    }
}