package jigl.image.ops.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jigl.image.*;
import jigl.image.ops.Shift;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 3;
 private static final int X_OFFSET = 2;
 private static final int Y_OFFSET = 1;
 private static final boolean WRAP = true;
 private static final boolean NO_WRAP = false;

 @Test
 public void testShiftConstructor() {
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, WRAP);
 Shift shiftNoWrap = new Shift(X_OFFSET, Y_OFFSET, NO_WRAP);
 Shift shiftNullX = new Shift(0, Y_OFFSET, WRAP);
 Shift shiftNullY = new Shift(X_OFFSET, 0, WRAP);

 assertTrue(shift instanceof Shift);
 assertTrue(shiftNoWrap instanceof Shift);
 assertNull(shiftNullX);
 assertNull(shiftNullY);
 }

 @Test
 public void testApplyGrayImageWithWrap() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, WRAP);
 GrayImage output = shift.apply(input);

 int[] expectedOutput = new int[X * Y];
 int index = 0;
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + X_OFFSET >= 0 && x + X_OFFSET < X && y + Y_OFFSET >= 0 && y + Y_OFFSET < Y) {
 expectedOutput[index++] = input.get(x + X_OFFSET, y + Y_OFFSET);
 } else {
 expectedOutput[index++] = input.get((x + X_OFFSET) % X, (y + Y_OFFSET) % Y);
 }
 }
 }
 assertArrayEquals(expectedOutput, output.getData());
 }

 @Test
 public void testApplyGrayImageWithoutWrap() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, NO_WRAP);
 GrayImage output = shift.apply(input);

 int[] expectedOutput = new int[X * Y];
 int index = 0;
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + X_OFFSET >= 0 && x + X_OFFSET < X && y + Y_OFFSET >= 0 && y + Y_OFFSET < Y) {
 expectedOutput[index++] = input.get(x + X_OFFSET, y + Y_OFFSET);
 } else {
 expectedOutput[index++] = 0;
 }
 }
 }
 assertArrayEquals(expectedOutput, output.getData());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeXOffset() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(-X_OFFSET, Y_OFFSET, WRAP);
 shift.apply(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeYOffset() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(X_OFFSET, -Y_OFFSET, WRAP);
 shift.apply(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftWithInvalidWrap() {
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, 2);
 }

 @Test
 public void testApplyRealGrayImageWithWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, WRAP);
 RealGrayImage output = shift.apply(input);

 float[] expectedOutput = new float[X * Y];
 int index = 0;
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + X_OFFSET >= 0 && x + X_OFFSET < X && y + Y_OFFSET >= 0 && y + Y_OFFSET < Y) {
 expectedOutput[index++] = input.get(x + X_OFFSET, y + Y_OFFSET);
 } else {
 expectedOutput[index++] = input.get((x + X_OFFSET) % X, (y + Y_OFFSET) % Y);
 }
 }
 }
 assertArrayEquals(expectedOutput, output.getData(), 0.001f);
 }

 @Test
 public void testApplyRealGrayImageWithoutWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, NO_WRAP);
 RealGrayImage output = shift.apply(input);

 float[] expectedOutput = new float[X * Y];
 int index = 0;
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + X_OFFSET >= 0 && x + X_OFFSET < X && y + Y_OFFSET >= 0 && y + Y_OFFSET < Y) {
 expectedOutput[index++] = input.get(x + X_OFFSET, y + Y_OFFSET);
 } else {
 expectedOutput[index++] = 0.0f;
 }
 }
 }
 assertArrayEquals(expectedOutput, output.getData(), 0.001f);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeXOffset() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(-X_OFFSET, Y_OFFSET, WRAP);
 shift.apply(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeYOffset() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(X_OFFSET, -Y_OFFSET, WRAP);
 shift.apply(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftWithInvalidWrap() {
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, 2);
 }

 @Test
 public void testApplyColorImageWithWrap() {
 ColorImage input = new ColorImage(X, Y, new byte[]{}, new byte[]{}, new byte[]{});
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, WRAP);
 ColorImage output = shift.apply(input);

 byte[][] expectedOutput = new byte[3][X * Y];
 int index = 0;
 for (int plane = 0; plane < 3; ++plane) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + X_OFFSET >= 0 && x + X_OFFSET < X && y + Y_OFFSET >= 0 && y + Y_OFFSET < Y) {
 expectedOutput[plane][index++] = input.plane(plane)[x + X_OFFSET][y + Y_OFFSET];
 } else {
 expectedOutput[plane][index++] = input.plane(plane)[((x + X_OFFSET) % X)][(y + Y_OFFSET) % Y];
 }
 }
 }
 }
 assertArrayEquals(expectedOutput[0], output.plane(0), 0.1f);
 assertArrayEquals(expectedOutput[1], output.plane(1), 0.1f);
 assertArrayEquals(expectedOutput[2], output.plane(2), 0.1f);
 }

 @Test
 public void testApplyColorImageWithoutWrap() {
 ColorImage input = new ColorImage(X, Y, new byte[]{}, new byte[]{}, new byte[]{});
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, NO_WRAP);
 ColorImage output = shift.apply(input);

 byte[][] expectedOutput = new byte[3][X * Y];
 int index = 0;
 for (int plane = 0; plane < 3; ++plane) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + X_OFFSET >= 0 && x + X_OFFSET < X && y + Y_OFFSET >= 0 && y + Y_OFFSET < Y) {
 expectedOutput[plane][index++] = input.plane(plane)[x + X_OFFSET][y + Y_OFFSET];
 } else {
 expectedOutput[plane][index++] = 0;
 }
 }
 }
 }
 assertArrayEquals(expectedOutput[0], output.plane(0), 0.1f);
 assertArrayEquals(expectedOutput[1], output.plane(1), 0.1f);
 assertArrayEquals(expectedOutput[2], output.plane(2), 0.1f);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeXOffset() {
 ColorImage input = new ColorImage(X, Y, new byte[]{}, new byte[]{}, new byte[]{});
 Shift shift = new Shift(-X_OFFSET, Y_OFFSET, WRAP);
 shift.apply(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeYOffset() {
 ColorImage input = new ColorImage(X, Y, new byte[]{}, new byte[]{}, new byte[]{});
 Shift shift = new Shift(X_OFFSET, -Y_OFFSET, WRAP);
 shift.apply(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftWithInvalidWrap() {
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, 2);
 }

 @Test
 public void testApplyRealColorImageWithWrap() {
 RealColorImage input = new RealColorImage(X, Y, new float[]{}, new float[]{}, new float[]{});
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, WRAP);
 RealColorImage output = shift.apply(input);

 float[][] expectedOutput = new float[3][X * Y];
 int index = 0;
 for (int plane = 0; plane < 3; ++plane) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + X_OFFSET >= 0 && x + X_OFFSET < X && y + Y_OFFSET >= 0 && y + Y_OFFSET < Y) {
 expectedOutput[plane][index++] = input.getPlane(plane)[x + X_OFFSET][y + Y_OFFSET];
 } else {
 expectedOutput[plane][index++] = input.getPlane(plane)[((x + X_OFFSET) % X)][(y + Y_OFFSET) % Y];
 }
 }
 }
 }
 assertArrayEquals(expectedOutput[0], output.getPlane(0), 0.001f);
 assertArrayEquals(expectedOutput[1], output.getPlane(1), 0.001f);
 assertArrayEquals(expectedOutput[2], output.getPlane(2), 0.001f);
 }

 @Test
 public void testApplyRealColorImageWithoutWrap() {
 RealColorImage input = new RealColorImage(X, Y, new float[]{}, new float[]{}, new float[]{});
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, NO_WRAP);
 RealColorImage output = shift.apply(input);

 float[][] expectedOutput = new float[3][X * Y];
 int index = 0;
 for (int plane = 0; plane < 3; ++plane) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + X_OFFSET >= 0 && x + X_OFFSET < X && y + Y_OFFSET >= 0 && y + Y_OFFSET < Y) {
 expectedOutput[plane][index++] = input.getPlane(plane)[x + X_OFFSET][y + Y_OFFSET];
 } else {
 expectedOutput[plane][index++] = 0.0f;
 }
 }
 }
 }
 assertArrayEquals(expectedOutput[0], output.getPlane(0), 0.001f);
 assertArrayEquals(expectedOutput[1], output.getPlane(1), 0.001f);
 assertArrayEquals(expectedOutput[2], output.getPlane(2), 0.001f);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealColorImageWithNegativeXOffset() {
 RealColorImage input = new RealColorImage(X, Y, new float[]{}, new float[]{}, new float[]{});
 Shift shift = new Shift(-X_OFFSET, Y_OFFSET, WRAP);
 shift.apply(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealColorImageWithNegativeYOffset() {
 RealColorImage input = new RealColorImage(X, Y, new float[]{}, new float[]{}, new float[]{});
 Shift shift = new Shift(X_OFFSET, -Y_OFFSET, WRAP);
 shift.apply(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftWithInvalidWrap() {
 Shift shift = new Shift(X_OFFSET, Y_OFFSET, 2);
 }
}