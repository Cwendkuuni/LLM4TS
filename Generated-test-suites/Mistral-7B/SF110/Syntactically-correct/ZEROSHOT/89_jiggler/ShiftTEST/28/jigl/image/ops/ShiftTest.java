package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.Shift;

import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 5;

 @Test
 public void testApplyGrayImageNOWrap() {
 GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (byte) i);
 }
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 int expected = image.get(x + 2, y + 3).get();
 int actual = result.get(x, y).get();
 assertEquals("X and Y coordinates not equal", expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageWrapHorizontally() {
 GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (byte) i);
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 int expected = image.get((x + 2) % X, (y + 3) % Y).get();
 int actual = result.get(x, y).get();
 assertEquals("X and Y coordinates not equal", expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageWrapVertically() {
 GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (byte) i);
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 int expected = image.get((X + x + 2) % X, (y + 3) % Y).get();
 int actual = result.get(x, y).get();
 assertEquals("X and Y coordinates not equal", expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageWrapBoth() {
 GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (byte) i);
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 int expected = image.get((X + x + 2) % X, (Y + y + 3) % Y).get();
 int actual = result.get(x, y).get();
 assertEquals("X and Y coordinates not equal", expected, actual);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageNegativeXOffset() {
 GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (byte) i);
 }
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 try {
 shift.apply(image);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageNegativeYOffset() {
 GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (byte) i);
 }
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 try {
 shift.apply(image);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testApplyRealGrayImageNOWrap() {
 RealGrayImage image = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (float) i / 10);
 }
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 float expected = image.get(x + 2, y + 3);
 float actual = result.get(x, y);
 assertEquals("X and Y coordinates not equal", expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWrapHorizontally() {
 RealGrayImage image = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (float) i / 10);
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealGrayImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 float expected = image.get((x + 2) % X, (y + 3) % Y);
 float actual = result.get(x, y);
 assertEquals("X and Y coordinates not equal", expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWrapVertically() {
 RealGrayImage image = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (float) i / 10);
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealGrayImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 float expected = image.get((X + x + 2) % X, (y + 3) % Y);
 float actual = result.get(x, y);
 assertEquals("X and Y coordinates not equal", expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWrapBoth() {
 RealGrayImage image = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; i++) {
 image.set(i / X, i % Y, (float) i / 10);
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealGrayImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 float expected = image.get((X + x + 2) % X, (Y + y + 3) % Y);
 float actual = result.get(x, y);
 assertEquals("X and Y coordinates not equal", expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testApplyColorImageNOWrap() {
 ColorImage image = new ColorImage(X, Y, new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 image.setPixel(i / 3 / X, i % 3 / Y, new RGB(i / 3, i / 3 + 1, i / 3 + 2));
 }
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 ColorImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 RGB expected = image.getPixel(x + 2, y + 3);
 RGB actual = result.getPixel(x, y);
 assertEquals("RGB values not equal", expected.getRed(), actual.getRed());
 assertEquals("RGB values not equal", expected.getGreen(), actual.getGreen());
 assertEquals("RGB values not equal", expected.getBlue(), actual.getBlue());
 }
 }
 }

 @Test
 public void testApplyColorImageWrapHorizontally() {
 ColorImage image = new ColorImage(X, Y, new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 image.setPixel(i / 3 / X, i % 3 / Y, new RGB(i / 3, i / 3 + 1, i / 3 + 2));
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 ColorImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 RGB expected = image.getPixel((x + 2) % X, (y + 3) % Y);
 RGB actual = result.getPixel(x, y);
 assertEquals("RGB values not equal", expected.getRed(), actual.getRed());
 assertEquals("RGB values not equal", expected.getGreen(), actual.getGreen());
 assertEquals("RGB values not equal", expected.getBlue(), actual.getBlue());
 }
 }
 }

 @Test
 public void testApplyColorImageWrapVertically() {
 ColorImage image = new ColorImage(X, Y, new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 image.setPixel(i / 3 / X, i % 3 / Y, new RGB(i / 3, i / 3 + 1, i / 3 + 2));
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 ColorImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 RGB expected = image.getPixel((X + x + 2) % X, (y + 3) % Y);
 RGB actual = result.getPixel(x, y);
 assertEquals("RGB values not equal", expected.getRed(), actual.getRed());
 assertEquals("RGB values not equal", expected.getGreen(), actual.getGreen());
 assertEquals("RGB values not equal", expected.getBlue(), actual.getBlue());
 }
 }
 }

 @Test
 public void testApplyColorImageWrapBoth() {
 ColorImage image = new ColorImage(X, Y, new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 image.setPixel(i / 3 / X, i % 3 / Y, new RGB(i / 3, i / 3 + 1, i / 3 + 2));
 }
 Shift shift = new Shift(2, 3, Shift.WRAP);
 ColorImage result = shift.apply(image);
 for (int y = 0; y < Y; y++) {
 for (int x = 0; x < X; x++) {
 RGB expected = image.getPixel((X + x + 2) % X, (Y + y + 3) % Y);
 RGB actual = result.getPixel(x, y);
 assertEquals("RGB values not equal", expected.getRed(), actual.getRed());
 assertEquals("RGB values not equal", expected.getGreen(), actual.getGreen());
 assertEquals("RGB values not equal", expected.getBlue(), actual.getBlue());
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageNegativeXOffset() {
 ColorImage image = new ColorImage(X, Y, new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 image.setPixel(i / 3 / X, i % 3 / Y, new RGB(i / 3, i / 3 + 1, i / 3 + 2));
 }
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 try {
 shift.apply(image);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageNegativeYOffset() {
 ColorImage image = new ColorImage(X, Y, new byte[3 * X * Y]);
 for (int i = 0; i < 3 * X * Y; i += 3) {
 image.setPixel(i / 3 / X, i % 3 / Y, new RGB(i / 3, i / 3 + 1, i / 3 + 2));
 }
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 try {
 shift.apply(image);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }
}