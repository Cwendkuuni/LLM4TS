package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.Shift;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 3;

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
 public void testApplyGrayImageWithNoWrap() {
 GrayImage input = new GrayImage(X, Y);
 input.set(0, 0, (byte) 127);
 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 GrayImage output = shift.apply(input);
 assertNotNull(output);
 assertEquals(X, output.X());
 assertEquals(Y, output.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = input.get(x, y);
 int actual = output.get(x, y);
 assertEquals(expected, actual);
 }
 }
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = input.get(x, y);
 int actual = output.get(x + 1, y + 1);
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageWithWrapX() {
 GrayImage input = new GrayImage(X, Y);
 input.set(0, 0, (byte) 127);
 Shift shift = new Shift(1, 1, Shift.WRAP);
 GrayImage output = shift.apply(input);
 assertNotNull(output);
 assertEquals(X, output.X());
 assertEquals(Y, output.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = input.get(x, y);
 int actual = output.get((x + 1) % X, (y + 1) % Y);
 assertEquals(expected, actual);
 }
 }
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = input.get(x, y);
 int actual = output.get((x + X + 1) % X, y + 1);
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageWithWrapY() {
 GrayImage input = new GrayImage(X, Y);
 input.set(0, 0, (byte) 127);
 Shift shift = new Shift(1, 1, Shift.WRAP);
 GrayImage output = shift.apply(input);
 assertNotNull(output);
 assertEquals(X, output.X());
 assertEquals(Y, output.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = input.get(x, y);
 int actual = output.get(x, (y + 1) % Y);
 assertEquals(expected, actual);
 }
 }
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = input.get(x, y);
 int actual = output.get(x, (y + Y + 1) % Y);
 assertEquals(expected, actual);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeXOffset() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(-1, 1, Shift.NOWRAP);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeYOffset() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(1, -1, Shift.NOWRAP);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeXOffsetAndWrap() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(-1, 1, Shift.WRAP);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeYOffsetAndWrap() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(1, -1, Shift.WRAP);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testApplyRealGrayImageWithNoWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 input.set(0, 0, 0.5f);
 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 RealGrayImage output = shift.apply(input);
 assertNotNull(output);
 assertEquals(X, output.X());
 assertEquals(Y, output.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expected = input.get(x, y);
 float actual = output.get(x, y);
 assertEquals(expected, actual, 0.001f);
 }
 }
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expected = input.get(x, y);
 float actual = output.get(x + 1, y + 1);
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWithWrapX() {
 RealGrayImage input = new RealGrayImage(X, Y);
 input.set(0, 0, 0.5f);
 Shift shift = new Shift(1, 1, Shift.WRAP);
 RealGrayImage output = shift.apply(input);
 assertNotNull(output);
 assertEquals(X, output.X());
 assertEquals(Y, output.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expected = input.get(x, y);
 float actual = output.get((x + 1) % X, (y + 1) % Y);
 assertEquals(expected, actual, 0.001f);
 }
 }
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expected = input.get(x, y);
 float actual = output.get((x + X + 1) % X, y + 1);
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWithWrapY() {
 RealGrayImage input = new RealGrayImage(X, Y);
 input.set(0, 0, 0.5f);
 Shift shift = new Shift(1, 1, Shift.WRAP);
 RealGrayImage output = shift.apply(input);
 assertNotNull(output);
 assertEquals(X, output.X());
 assertEquals(Y, output.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expected = input.get(x, y);
 float actual = output.get(x, (y + 1) % Y);
 assertEquals(expected, actual, 0.001f);
 }
 }
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expected = input.get(x, y);
 float actual = output.get(x, (y + Y + 1) % Y);
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeXOffset() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(-1, 1, Shift.NOWRAP);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeYOffset() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(1, -1, Shift.NOWRAP);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeXOffsetAndWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(-1, 1, Shift.WRAP);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeYOffsetAndWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(1, -1, Shift.WRAP);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testApplyColorImage() {
 ColorImage input = new ColorImage(new GrayImage(X, Y), new GrayImage(X, Y), new GrayImage(X, Y));
 input.setPlane(0, new GrayImage(X, Y).set(0, 0, (byte) 127));
 input.setPlane(1, new GrayImage(X, Y).set(0, 0, (byte) 128));
 input.setPlane(2, new GrayImage(X, Y).set(0, 0, (byte) 129));
 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 ColorImage output = shift.apply(input);
 assertNotNull(output);
 assertEquals(X, output.X());
 assertEquals(Y, output.Y());
 for (int plane = 0; plane < 3; ++plane) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 GrayImage planeInput = input.plane(plane);
 GrayImage planeOutput = output.plane(plane);
 int expected = planeInput.get(x, y);
 int actual = planeOutput.get(x, y);
 assertEquals(expected, actual);
 }
 }
 }
 for (int plane = 0; plane < 3; ++plane) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = planeInput.get(x, y);
 int actual = planeOutput.get(x + 1, y + 1);
 assertEquals(expected, actual);
 }
 }
 }
 }

 @Test
 public void testApplyRealColorImage() {
 RealColorImage input = new RealColorImage(new RealGrayImage(X, Y), new RealGrayImage(X, Y), new RealGrayImage(X, Y));
 input.setPlane(0, new RealGrayImage(X, Y).set(0, 0, 0.5f));
 input.setPlane(1, new RealGrayImage(X, Y).set(0, 0, 0.6f));
 input.setPlane(2, new RealGrayImage(X, Y).set(0, 0, 0.7f));
 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 RealColorImage output = shift.apply(input);
 assertNotNull(output);
 assertEquals(X, output.X());
 assertEquals(Y, output.Y());
 for (int plane = 0; plane < 3; ++plane) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 RealGrayImage planeInput = input.plane(plane);
 RealGrayImage planeOutput = output.plane(plane);
 float expected = planeInput.get(x, y);
 float actual = planeOutput.get(x, y);
 assertEquals(expected, actual, 0.001f);
 }
 }
 }
 for (int plane = 0; plane < 3; ++plane) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 float expected = planeInput.get(x, y);
 float actual = planeOutput.get(x + 1, y + 1);
 assertEquals(expected, actual, 0.001f);
 }
 }
 }
 }
}