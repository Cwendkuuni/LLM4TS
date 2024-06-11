package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.types.*;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 3;

 @Test
 public void testShiftWithNoWrapAndZeroOffsets() {
 Shift shift = new Shift(0, 0, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 expected.set(x, y, (byte) x);
 }
 }
 GrayImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test
 public void testShiftWithNoWrapAndPositiveOffsets() {
 Shift shift = new Shift(2, 1, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 expected.set(x - 2, y - 1, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test
 public void testShiftWithNoWrapAndNegativeOffsets() {
 Shift shift = new Shift(-2, -1, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 expected.set(x + 2, y + 1, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test
 public void testShiftWithWrapAndZeroOffsets() {
 Shift shift = new Shift(0, 0, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 expected.set((x + this.xoffset) % X, (y + this.yoffset) % Y, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test
 public void testShiftWithWrapAndPositiveOffsets() {
 Shift shift = new Shift(2, 1, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int newX = (x + this.xoffset) % X;
 int newY = (y + this.yoffset) % Y;
 expected.set(newX, newY, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test
 public void testShiftWithWrapAndNegativeOffsets() {
 Shift shift = new Shift(-2, -1, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expected = new GrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int newX = (x + this.xoffset) % X;
 int newY = (y + this.yoffset) % Y;
 expected.set(newX, newY, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 assertEquals(expected, output);
 }

 @Test(expected = ImageOperationException.class)
 public void testShiftWithNegativeXOffsetAndWrapButImageWidthIsNotEven() {
 Shift shift = new Shift(-2, 0, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 try {
 shift.apply(input);
 fail();
 } catch (ImageOperationException e) {
 // expected
 }
 }

 @Test(expected = ImageOperationException.class)
 public void testShiftWithNegativeYOffsetAndWrapButImageHeightIsNotEven() {
 Shift shift = new Shift(0, -2, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 try {
 shift.apply(input);
 fail();
 } catch (ImageOperationException e) {
 // expected
 }
 }

 @Test(expected = ImageOperationException.class)
 public void testShiftWithNegativeXOffsetAndNoWrapButImageWidthIsNotEven() {
 Shift shift = new Shift(-2, 0, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 try {
 shift.apply(input);
 fail();
 } catch (ImageOperationException e) {
 // expected
 }
 }

 @Test(expected = ImageOperationException.class)
 public void testShiftWithNegativeYOffsetAndNoWrapButImageHeightIsNotEven() {
 Shift shift = new Shift(0, -2, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 try {
 shift.apply(input);
 fail();
 } catch (ImageOperationException e) {
 // expected
 }
 }
}