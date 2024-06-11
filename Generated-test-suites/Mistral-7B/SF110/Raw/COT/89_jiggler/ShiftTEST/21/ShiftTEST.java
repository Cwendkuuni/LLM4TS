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
 public void testShiftConstructor() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 assertEquals(2, shift.getXoffset());
 assertEquals(3, shift.getYoffset());
 assertFalse(shift.isWrap());
 }

 @Test
 public void testShiftConstructorWithWrap() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 assertEquals(2, shift.getXoffset());
 assertEquals(3, shift.getYoffset());
 assertTrue(shift.isWrap());
 }

 @Test
 public void testApplyGrayImagePositiveOffsets() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (byte) x);
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int expected = x + 2;
 int actual = output.get(x, y).getValue().intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageNegativeXOffset() {
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (byte) x);
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int expected = X + x - 2;
 int actual = output.get(x, y).getValue().intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageNegativeYOffset() {
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (byte) y);
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int expected = Y + y - 3;
 int actual = output.get(x, y).getValue().intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageNegativeOffsets() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (byte) x);
 }
 }
 GrayImage output = shift.apply(input);
 }

 @Test
 public void testApplyRealGrayImagePositiveOffsets() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (float) x * 0.1f);
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 float expected = x * 0.1f + 2.0f;
 float actual = output.get(x, y).getValue();
 assertEquals(expected, actual, 0.01f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageNegativeXOffset() {
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (float) x * 0.1f);
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 float expected = (X - x) * 0.1f - 2.0f;
 float actual = output.get(x, y).getValue();
 assertEquals(expected, actual, 0.01f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageNegativeYOffset() {
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (float) y * 0.1f);
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 float expected = (Y - y) * 0.1f - 3.0f;
 float actual = output.get(x, y).getValue();
 assertEquals(expected, actual, 0.01f);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageNegativeOffsets() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (float) x * 0.1f);
 }
 }
 RealGrayImage output = shift.apply(input);
 }

 @Test
 public void testApplyColorImage() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 ColorImage input = new ColorImage(new GrayImage(X, Y), new GrayImage(X, Y), new GrayImage(X, Y));
 ColorImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 ColorPixel expected = new ColorPixel((byte) (x + 2), (byte) (y + 3), (byte) 0);
 ColorPixel actual = output.getPixel(x, y);
 assertEquals(expected.getRed(), actual.getRed());
 assertEquals(expected.getGreen(), actual.getGreen());
 assertEquals(expected.getBlue(), actual.getBlue());
 }
 }
 }

 @Test
 public void testApplyRealColorImage() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealColorImage input = new RealColorImage(new RealGrayImage(X, Y), new RealGrayImage(X, Y), new RealGrayImage(X, Y));
 RealColorImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 RealColorPixel expected = new RealColorPixel(new RealGrayPixel((float) (x + 2), 0.0f),
 new RealGrayPixel((float) (y + 3), 0.0f), new RealGrayPixel((float) 0.0f, 0.0f));
 RealColorPixel actual = output.getPixel(x, y);
 assertEquals(expected.getRed().getValue(), actual.getRed().getValue(), 0.01f);
 assertEquals(expected.getGreen().getValue(), actual.getGreen().getValue(), 0.01f);
 assertEquals(expected.getBlue().getValue(), actual.getBlue().getValue(), 0.01f);
 }
 }
 }

 @Test
 public void testApplyWithWrap() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (byte) x);
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int expected = (x + 2) % X;
 int actual = output.get(x, y).getValue().intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyWrapNegativeX() {
 Shift shift = new Shift(-2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (byte) x);
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int expected = (X + x - 2 + X) % X;
 int actual = output.get(x, y).getValue().intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyWrapNegativeY() {
 Shift shift = new Shift(2, -3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 input.set(x, y, (byte) y);
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int expected = (Y + y - 3 + Y) % Y;
 int actual = output.get(x, y).getValue().intValue();
 assertEquals(expected, actual);
 }
 }
 }

}