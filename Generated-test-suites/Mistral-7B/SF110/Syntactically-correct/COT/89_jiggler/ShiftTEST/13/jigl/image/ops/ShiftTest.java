package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 5;

 @Test
 public void testShiftConstructor() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 assertEquals(2, shift.getXoffset());
 assertEquals(3, shift.getYoffset());
 assertTrue(shift.isWrap());
 }

 @Test
 public void testShiftApplyGrayImagePositiveOffsets() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x + 2, y + 3));
 }
 }
 GrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData());
 }

 @Test
 public void testShiftApplyGrayImageNegativeXOffset() {
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x - 2, y + 3));
 }
 }
 GrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData());
 }

 @Test
 public void testShiftApplyGrayImageNegativeYOffset() {
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x + 2, y - 3));
 }
 }
 GrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testShiftApplyGrayImageInvalidOffsets() {
 Shift shift = new Shift(5, 5, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage output = shift.apply(input);
 fail("Shift should have thrown IndexOutOfBoundsException");
 }

 @Test
 public void testShiftApplyRealGrayImagePositiveOffsets() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 RealGrayImage expected = new RealGrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x + 2, y + 3));
 }
 }
 RealGrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData(), 0.01f);
 }

 @Test
 public void testShiftApplyRealGrayImageNegativeXOffset() {
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 RealGrayImage expected = new RealGrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x - 2, y + 3));
 }
 }
 RealGrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData(), 0.01f);
 }

 @Test
 public void testShiftApplyRealGrayImageNegativeYOffset() {
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 RealGrayImage expected = new RealGrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x + 2, y - 3));
 }
 }
 RealGrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData(), 0.01f);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testShiftApplyRealGrayImageInvalidOffsets() {
 Shift shift = new Shift(5, 5, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 RealGrayImage output = shift.apply(input);
 fail("Shift should have thrown IndexOutOfBoundsException");
 }

 @Test
 public void testShiftApplyColorImagePositiveOffsets() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[]{}, new byte[]{}, new byte[]{});
 ColorImage expected = new ColorImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.getPixel(x + 2, y + 3));
 }
 }
 ColorImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test
 public void testShiftApplyColorImageNegativeXOffset() {
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[]{}, new byte[]{}, new byte[]{});
 ColorImage expected = new ColorImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.getPixel(x - 2, y + 3));
 }
 }
 ColorImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test
 public void testShiftApplyColorImageNegativeYOffset() {
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[]{}, new byte[]{}, new byte[]{});
 ColorImage expected = new ColorImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.getPixel(x + 2, y - 3));
 }
 }
 ColorImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testShiftApplyColorImageInvalidOffsets() {
 Shift shift = new Shift(5, 5, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[]{}, new byte[]{}, new byte[]{});
 ShiftedImage output = shift.apply(input);
 fail("Shift should have thrown IndexOutOfBoundsException");
 }

 @Test
 public void testShiftApplyRealColorImagePositiveOffsets() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealColorImage input = new RealColorImage(X, Y, new float[]{}, new float[]{}, new float[]{}, new float[]{});
 RealColorImage expected = new RealColorImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x + 2, y + 3));
 }
 }
 RealColorImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData(), 0.01f);
 }

 @Test
 public void testShiftApplyRealColorImageNegativeXOffset() {
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 RealColorImage input = new RealColorImage(X, Y, new float[]{}, new float[]{}, new float[]{}, new float[]{});
 RealColorImage expected = new RealColorImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x - 2, y + 3));
 }
 }
 RealColorImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData(), 0.01f);
 }

 @Test
 public void testShiftApplyRealColorImageNegativeYOffset() {
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 RealColorImage input = new RealColorImage(X, Y, new float[]{}, new float[]{}, new float[]{}, new float[]{});
 RealColorImage expected = new RealColorImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x + 2, y - 3));
 }
 }
 RealColorImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData(), 0.01f);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testShiftApplyRealColorImageInvalidOffsets() {
 Shift shift = new Shift(5, 5, Shift.NOWRAP);
 RealColorImage input = new RealColorImage(X, Y, new float[]{}, new float[]{}, new float[]{}, new float[]{});
 ShiftedImage output = shift.apply(input);
 fail("Shift should have thrown IndexOutOfBoundsException");
 }

 @Test
 public void testShiftWrap() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get((x + 2) % X, (y + 3) % Y));
 }
 }
 GrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData());
 }

 @Test
 public void testShiftApplyGrayImageWrapXPositive() {
 Shift shift = new Shift(X + 2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get((x + 2) % X, y + 3));
 }
 }
 GrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData());
 }

 @Test
 public void testShiftApplyGrayImageWrapYPositive() {
 Shift shift = new Shift(2, Y + 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x + 2, (y + 3) % Y));
 }
 }
 GrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData());
 }

 @Test
 public void testShiftApplyGrayImageWrapXNegative() {
 Shift shift = new Shift(-X - 2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get((x - 2 + X) % X, y + 3));
 }
 }
 GrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData());
 }

 @Test
 public void testShiftApplyGrayImageWrapYNegative() {
 Shift shift = new Shift(2, -Y - 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 expected.set(x, y, input.get(x + 2, (y - 3 + Y) % Y));
 }
 }
 GrayImage output = shift.apply(input);
 assertArrayEquals(expected.getData(), output.getData());
 }
}