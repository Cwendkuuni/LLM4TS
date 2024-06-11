package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 5;

 @Test
 public void testShiftConstructorWithValidArguments() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 assertEquals(2, shift.xoffset);
 assertEquals(3, shift.yoffset);
 assertTrue(shift.wrap);
 }

 @Test
 public void testShiftConstructorWithInvalidWrapArgument() {
 assertThrows(IllegalArgumentException.class, () -> new Shift(2, 3, -1));
 }

 @Test
 public void testApplyGrayImageWithNoWrap() {
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, (byte) i);
 }
 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 GrayImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = (x + 1) % X;
 int actual = output.get(x, y);
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageWithWrap() {
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, (byte) i);
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expectedX = (x + 2) % X;
 int expectedY = (y + 3) % Y;
 int actual = output.get(x, y);
 assertEquals(expectedX, actual % X);
 assertEquals(expectedY, actual % Y);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWithNoWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, i / (float) 10.0);
 }
 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 RealGrayImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expected = (x + 1) / (float) 10.0;
 float actual = output.get(x, y);
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWithWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, i / (float) 10.0);
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealGrayImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expectedX = (x + 2) / (float) 10.0;
 float expectedY = (y + 3) / (float) 10.0;
 float actual = output.get(x, y);
 assertEquals(expectedX, actual % X, 0.001f);
 assertEquals(expectedY, actual % Y, 0.001f);
 }
 }
 }

 @Test
 public void testApplyColorImageWithNoWrap() {
 ColorImage input = new ColorImage(X, Y, new GrayImage(X, Y), new GrayImage(X, Y), new GrayImage(X, Y));
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 input.plane(plane).set(x, y, (byte) (x + y * X + plane));
 }
 }
 }
 Shift shift = new Shift(1, 2, Shift.NOWRAP);
 ColorImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 int expected = (x + 1) % X + (y + 2) * X + plane;
 int actual = output.plane(plane).get(x, y);
 assertEquals(expected, actual);
 }
 }
 }
 }

 @Test
 public void testApplyColorImageWithWrap() {
 ColorImage input = new ColorImage(X, Y, new GrayImage(X, Y), new GrayImage(X, Y), new GrayImage(X, Y));
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 input.plane(plane).set(x, y, (byte) (x + y * X + plane));
 }
 }
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 ColorImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 int expectedX = (x + 2) % X;
 int expectedY = (y + 3) % Y;
 int actual = output.plane(plane).get(x, y);
 assertEquals(expectedX, actual % X);
 assertEquals(expectedY, actual % Y);
 }
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeXOffset() {
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, (byte) i);
 }
 Shift shift = new Shift(-1, 1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeYOffset() {
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, (byte) i);
 }
 Shift shift = new Shift(1, -1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeXAndYOffsets() {
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, (byte) i);
 }
 Shift shift = new Shift(-1, -1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeXOffset() {
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, i / (float) 10.0);
 }
 Shift shift = new Shift(-1, 1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeYOffset() {
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, i / (float) 10.0);
 }
 Shift shift = new Shift(1, -1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeXAndYOffsets() {
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 input.set(i % X, i % Y, i / (float) 10.0);
 }
 Shift shift = new Shift(-1, -1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeXOffset() {
 ColorImage input = new ColorImage(X, Y, new GrayImage(X, Y), new GrayImage(X, Y), new GrayImage(X, Y));
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 input.plane(plane).set(x, y, (byte) (x + y * X + plane));
 }
 }
 }
 Shift shift = new Shift(-1, 1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeYOffset() {
 ColorImage input = new ColorImage(X, Y, new GrayImage(X, Y), new GrayImage(X, Y), new GrayImage(X, Y));
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 input.plane(plane).set(x, y, (byte) (x + y * X + plane));
 }
 }
 }
 Shift shift = new Shift(1, -1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeXAndYOffsets() {
 ColorImage input = new ColorImage(X, Y, new GrayImage(X, Y), new GrayImage(X, Y), new GrayImage(X, Y));
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 input.plane(plane).set(x, y, (byte) (x + y * X + plane));
 }
 }
 }
 Shift shift = new Shift(-1, -1, Shift.NOWRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(input));
 }
}