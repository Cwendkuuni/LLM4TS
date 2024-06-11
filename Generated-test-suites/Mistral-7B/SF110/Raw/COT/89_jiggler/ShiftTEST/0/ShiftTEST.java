package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 3;

 @Test
 public void testShiftConstructorWithValidArguments() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 assertEquals(2, shift.getXoffset());
 assertEquals(3, shift.getYoffset());
 assertTrue(shift.isWrap());
 }

 @Test
 public void testShiftConstructorWithInvalidWrapArgument() {
 assertThrows(IllegalArgumentException.class, () -> new Shift(2, 3, -1));
 }

 @Test
 public void testApplyGrayImageWithValidArguments() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x < 2 || x >= (X - 2) || y < 2 || y >= (Y - 2)) {
 assertEquals(0, output.get(x, y).getGrayValue());
 } else {
 assertEquals(input.get(x - 2, y - 2).getGrayValue(), output.get(x, y).getGrayValue());
 }
 }
 }
 }

 @Test
 public void testApplyGrayImageWithoutWrap() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + 2 >= X || y + 3 >= Y) {
 assertEquals(0, output.get(x, y).getGrayValue());
 } else {
 assertEquals(input.get(x + 2, y + 3).getGrayValue(), output.get(x, y).getGrayValue());
 }
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeOffsets() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 shift.apply(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeOffsetsNoWrap() {
 GrayImage input = new GrayImage(X, Y);
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 shift.apply(input);
 }

 @Test
 public void testApplyRealGrayImageWithValidArguments() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealGrayImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x < 2 || x >= (X - 2) || y < 2 || y >= (Y - 2)) {
 assertEquals(0.0f, output.get(x, y), 0.01f);
 } else {
 assertEquals(input.get(x - 2, y - 2).getGrayValue(), output.get(x, y).getGrayValue(), 0.01f);
 }
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWithoutWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + 2 >= X || y + 3 >= Y) {
 assertEquals(0.0f, output.get(x, y), 0.01f);
 } else {
 assertEquals(input.get(x + 2, y + 3).getGrayValue(), output.get(x, y).getGrayValue(), 0.01f);
 }
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeOffsets() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 shift.apply(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeOffsetsNoWrap() {
 RealGrayImage input = new RealGrayImage(X, Y);
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 shift.apply(input);
 }

 @Test
 public void testApplyColorImageWithValidArguments() {
 ColorImage input = new ColorImage(new ByteImage(X, Y), new ByteImage(X, Y), new ByteImage(X, Y));
 Shift shift = new Shift(2, 3, Shift.WRAP);
 ColorImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x < 2 || x >= (X - 2) || y < 2 || y >= (Y - 2)) {
 assertEquals(new GrayValue(0), output.getPixel(x, y));
 } else {
 assertEquals(input.getPixel(x - 2, y - 2), output.getPixel(x, y));
 }
 }
 }
 }

 @Test
 public void testApplyColorImageWithoutWrap() {
 ColorImage input = new ColorImage(new ByteImage(X, Y), new ByteImage(X, Y), new ByteImage(X, Y));
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 ColorImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + 2 >= X || y + 3 >= Y) {
 assertEquals(new GrayValue(0), output.getPixel(x, y));
 } else {
 assertEquals(input.getPixel(x + 2, y + 3), output.getPixel(x, y));
 }
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeOffsets() {
 ColorImage input = new ColorImage(new ByteImage(X, Y), new ByteImage(X, Y), new ByteImage(X, Y));
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 shift.apply(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeOffsetsNoWrap() {
 ColorImage input = new ColorImage(new ByteImage(X, Y), new ByteImage(X, Y), new ByteImage(X, Y));
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 shift.apply(input);
 }

 @Test
 public void testApplyRealColorImageWithValidArguments() {
 RealColorImage input = new RealColorImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealColorImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x < 2 || x >= (X - 2) || y < 2 || y >= (Y - 2)) {
 assertEquals(new RealGrayValue(0.0f), output.getPixel(x, y), 0.01f);
 } else {
 assertEquals(input.getPixel(x - 2, y - 2), output.getPixel(x, y), 0.01f);
 }
 }
 }
 }

 @Test
 public void testApplyRealColorImageWithoutWrap() {
 RealColorImage input = new RealColorImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealColorImage output = shift.apply(input);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + 2 >= X || y + 3 >= Y) {
 assertEquals(new RealGrayValue(0.0f), output.getPixel(x, y), 0.01f);
 } else {
 assertEquals(input.getPixel(x + 2, y + 3), output.getPixel(x, y), 0.01f);
 }
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealColorImageWithNegativeOffsets() {
 RealColorImage input = new RealColorImage(X, Y);
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 shift.apply(input);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealColorImageWithNegativeOffsetsNoWrap() {
 RealColorImage input = new RealColorImage(X, Y);
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 shift.apply(input);
 }
}