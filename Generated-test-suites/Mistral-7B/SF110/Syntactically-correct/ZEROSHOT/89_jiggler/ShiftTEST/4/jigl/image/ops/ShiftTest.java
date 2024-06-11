package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 5;

 @Test
 public void testShiftGrayImageWithNoWrapAndZeroOffset() {
 Shift shift = new Shift(0, 0, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, (byte) i);
 }
 GrayImage output = shift.apply(input);
 for (int i = 0; i < X * Y; i++) {
 assertEquals(input.get(i % X, i % Y), output.get(i % X, i % Y));
 }
 }

 @Test
 public void testShiftGrayImageWithNoWrapAndPositiveOffset() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, (byte) i);
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int outputIndex = (x + 2) % X * Y + (y + 3) % Y;
 int inputIndex = x * Y + y;
 assertEquals(input.get(inputIndex), output.get(outputIndex));
 }
 }
 }

 @Test
 public void testShiftGrayImageWithNoWrapAndNegativeOffset() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, (byte) i);
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int outputIndex = (x - 2 + X) % X * Y + (y - 3 + Y) % Y;
 int inputIndex = x * Y + y;
 assertEquals(input.get(inputIndex), output.get(outputIndex));
 }
 }
 }

 @Test
 public void testShiftGrayImageWithWrapAndZeroOffset() {
 Shift shift = new Shift(0, 0, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, (byte) i);
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int outputIndex = (x + this.xoffset) % X * Y + (y + this.yoffset) % Y;
 int inputIndex = x * Y + y;
 assertEquals(input.get(inputIndex), output.get(outputIndex));
 }
 }
 }

 @Test
 public void testShiftGrayImageWithWrapAndPositiveOffset() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, (byte) i);
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 int outputIndex = (x + this.xoffset) % X * Y + (y + this.yoffset) % Y;
 int inputIndex = (x + this.xoffset) % X * Y + (y + this.yoffset) % Y;
 assertEquals(input.get(inputIndex), output.get(outputIndex));
 }
 }
 }

 @Test(expected = ImageSizeMismatchException.class)
 public void testShiftGrayImageWithInvalidOffset() {
 Shift shift = new Shift(X * 2, Y * 2, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, (byte) i);
 }
 try {
 shift.apply(input);
 fail("Expected ImageSizeMismatchException");
 } catch (ImageSizeMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testShiftRealGrayImageWithNoWrapAndZeroOffset() {
 Shift shift = new Shift(0, 0, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, i / (float) 255.0);
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 double outputValue = output.get(x, y);
 double inputValue = input.get(x, y);
 assertEquals(inputValue, outputValue, 0.001);
 }
 }
 }

 @Test
 public void testShiftRealGrayImageWithNoWrapAndPositiveOffset() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, i / (float) 255.0);
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 double outputValue = output.get(x, y);
 double inputValue = input.get(x + 2, y + 3);
 assertEquals(inputValue, outputValue, 0.001);
 }
 }
 }

 @Test
 public void testShiftRealGrayImageWithNoWrapAndNegativeOffset() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, i / (float) 255.0);
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 double outputValue = output.get(x, y);
 double inputValue = input.get(x - 2, y - 3);
 assertEquals(inputValue, outputValue, 0.001);
 }
 }
 }

 @Test
 public void testShiftRealGrayImageWithWrapAndZeroOffset() {
 Shift shift = new Shift(0, 0, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, i / (float) 255.0);
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 double outputValue = output.get(x, y);
 double inputValue = input.get((x + this.xoffset) % X, (y + this.yoffset) % Y);
 assertEquals(inputValue, outputValue, 0.001);
 }
 }
 }

 @Test
 public void testShiftRealGrayImageWithWrapAndPositiveOffset() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, i / (float) 255.0);
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 double outputValue = output.get(x, y);
 double inputValue = input.get((x + this.xoffset) % X, (y + this.yoffset) % Y);
 assertEquals(inputValue, outputValue, 0.001);
 }
 }
 }

 @Test(expected = ImageSizeMismatchException.class)
 public void testShiftRealGrayImageWithInvalidOffset() {
 Shift shift = new Shift(X * 2, Y * 2, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 input.set(i % X, i % Y, i / (float) 255.0);
 }
 try {
 shift.apply(input);
 fail("Expected ImageSizeMismatchException");
 } catch (ImageSizeMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testShiftColorImageWithNoWrapAndZeroOffset() {
 Shift shift = new Shift(0, 0, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[3 * X * Y], new byte[3 * X * Y], new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 input.setPixel(i / 3, i / 3, i / 255.0f, i / 510.0f, i / 765.0f);
 }
 ColorImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 float[] outputPixel = output.getPixel(x, y);
 float[] inputPixel = input.getPixel(x, y);
 for (int i = 0; i < 3; i++) {
 assertEquals(inputPixel[i], outputPixel[i], 0.001f);
 }
 }
 }
 }

 @Test
 public void testShiftColorImageWithNoWrapAndPositiveOffset() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[3 * X * Y], new byte[3 * X * Y], new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 input.setPixel(i / 3, i / 3, i / 255.0f, i / 510.0f, i / 765.0f);
 }
 ColorImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 float[] outputPixel = output.getPixel(x, y);
 float[] inputPixel = input.getPixel(x + 2, y + 3);
 for (int i = 0; i < 3; i++) {
 assertEquals(inputPixel[i], outputPixel[i], 0.001f);
 }
 }
 }
 }

 @Test
 public void testShiftColorImageWithNoWrapAndNegativeOffset() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[3 * X * Y], new byte[3 * X * Y], new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 input.setPixel(i / 3, i / 3, i / 255.0f, i / 510.0f, i / 765.0f);
 }
 ColorImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 float[] outputPixel = output.getPixel(x, y);
 float[] inputPixel = input.getPixel(x - 2, y - 3);
 for (int i = 0; i < 3; i++) {
 assertEquals(inputPixel[i], outputPixel[i], 0.001f);
 }
 }
 }
 }

 @Test
 public void testShiftColorImageWithWrapAndZeroOffset() {
 Shift shift = new Shift(0, 0, Shift.WRAP);
 ColorImage input = new ColorImage(X, Y, new byte[3 * X * Y], new byte[3 * X * Y], new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 input.setPixel(i / 3, i / 3, i / 255.0f, i / 510.0f, i / 765.0f);
 }
 ColorImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 float[] outputPixel = output.getPixel(x, y);
 float[] inputPixel = input.getPixel((x + this.xoffset) % X, (y + this.yoffset) % Y);
 for (int i = 0; i < 3; i++) {
 assertEquals(inputPixel[i], outputPixel[i], 0.001f);
 }
 }
 }
 }

 @Test
 public void testShiftColorImageWithWrapAndPositiveOffset() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 ColorImage input = new ColorImage(X, Y, new byte[3 * X * Y], new byte[3 * X * Y], new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 input.setPixel(i / 3, i / 3, i / 255.0f, i / 510.0f, i / 765.0f);
 }
 ColorImage output = shift.apply(input);
 for (int x = 0; x < X; x++) {
 for (int y = 0; y < Y; y++) {
 float[] outputPixel = output.getPixel(x, y);
 float[] inputPixel = input.getPixel((x + this.xoffset) % X, (y + this.yoffset) % Y);
 for (int i = 0; i < 3; i++) {
 assertEquals(inputPixel[i], outputPixel[i], 0.001f);
 }
 }
 }
 }

 @Test(expected = ImageSizeMismatchException.class)
 public void testShiftColorImageWithInvalidOffset() {
 Shift shift = new Shift(X * 2, Y * 2, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[3 * X * Y], new byte[3 * X * Y], new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 input.setPixel(i / 3, i / 3, i / 255.0f, i / 510.0f, i / 765.0f);
 }
 try {
 shift.apply(input);
 fail("Expected ImageSizeMismatchException");
 } catch (ImageSizeMismatchException e) {
 // Expected exception
 }
 }
}