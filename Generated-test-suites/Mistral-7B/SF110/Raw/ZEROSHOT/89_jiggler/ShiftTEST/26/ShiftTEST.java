package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ShiftTest {

 @Test
 public void testShiftGrayImage() {
 Shift shift = new Shift(2, 2, Shift.WRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 GrayImage shiftedImage = new GrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (byte) (x + y));
 shiftedImage.set(x, y, (byte) 0);
 }
 }

 GrayImage result = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 assertEquals("Shifted image does not match expected image at position (" + x + ", " + y + ")",
 (byte) (x + y + 2), shiftedImage.get(x, y));
 assertEquals("Applied shift operation does not match expected result at position (" + x + ", " + y + ")",
 result.get(x, y), shiftedImage.get(x, y));
 }
 }
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftWithNegativeOffsets() {
 Shift shift = new Shift(-2, -2, Shift.NOWRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 try {
 shift.apply(originalImage);
 fail("Expected ImageDimensionMismatchException but no exception was thrown.");
 } catch (ImageDimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testShiftGrayImageNoWrap() {
 Shift shift = new Shift(2, 2, Shift.NOWRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 GrayImage shiftedImage = new GrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (byte) (x + y));
 shiftedImage.set(x, y, (byte) 0);
 }
 }

 GrayImage result = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if ((x + 2 < 0) || (x + 2 >= 5) || (y + 2 < 0) || (y + 2 >= 5)) {
 shiftedImage.set(x, y, (byte) 0);
 } else {
 shiftedImage.set(x, y, originalImage.get(x + 2, y + 2));
 }
 assertEquals("Shifted image does not match expected image at position (" + x + ", " + y + ")",
 shiftedImage.get(x, y), result.get(x, y));
 }
 }
 }

 @Test
 public void testShiftRealGrayImage() {
 Shift shift = new Shift(2.5f, 2.5f, Shift.WRAP);
 RealGrayImage originalImage = new RealGrayImage(5, 5);
 RealGrayImage shiftedImage = new RealGrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (float) (x + y));
 shiftedImage.set(x, y, 0.0f);
 }
 }

 RealGrayImage result = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 assertEquals("Shifted image does not match expected image at position (" + x + ", " + y + ")",
 (float) (x + y + 2), shiftedImage.get(x, y), 0.01f);
 assertEquals("Applied shift operation does not match expected result at position (" + x + ", " + y + ")",
 result.get(x, y), shiftedImage.get(x, y), 0.01f);
 }
 }
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftRealGrayImageNegativeOffsets() {
 Shift shift = new Shift(-2.5f, -2.5f, Shift.NOWRAP);
 RealGrayImage originalImage = new RealGrayImage(5, 5);
 try {
 shift.apply(originalImage);
 fail("Expected ImageDimensionMismatchException but no exception was thrown.");
 } catch (ImageDimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testShiftRealGrayImageNoWrap() {
 Shift shift = new Shift(2.5f, 2.5f, Shift.NOWRAP);
 RealGrayImage originalImage = new RealGrayImage(5, 5);
 RealGrayImage shiftedImage = new RealGrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (float) (x + y));
 shiftedImage.set(x, y, 0.0f);
 }
 }

 RealGrayImage result = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if ((x + 2 < 0) || (x + 2 >= 5) || (y + 2 < 0) || (y + 2 >= 5)) {
 shiftedImage.set(x, y, 0.0f);
 } else {
 shiftedImage.set(x, y, originalImage.get(x + 2, y + 2));
 }
 assertEquals("Shifted image does not match expected image at position (" + x + ", " + y + ")",
 shiftedImage.get(x, y), result.get(x, y), 0.01f);
 }
 }
 }

 @Test
 public void testShiftColorImage() {
 Shift shift = new Shift(2, 2, Shift.WRAP);
 ColorImage originalImage = new ColorImage(5, 5, new byte[15 * 5]);
 ColorImage shiftedImage = new ColorImage(5, 5, new byte[15 * 5]);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.setPixel(new RGBColor(x + y, x + y, x + y), x, y);
 shiftedImage.setPixel(new RGBColor(0, 0, 0), x, y);
 }
 }

 ColorImage result = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 assertEquals("Shifted image does not match expected image at position (" + x + ", " + y + ")",
 originalImage.getPixel(x + 2, y + 2), result.getPixel(x, y));
 }
 }
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftColorImageNegativeOffsets() {
 Shift shift = new Shift(-2, -2, Shift.NOWRAP);
 ColorImage originalImage = new ColorImage(5, 5, new byte[15 * 5]);
 try {
 shift.apply(originalImage);
 fail("Expected ImageDimensionMismatchException but no exception was thrown.");
 } catch (ImageDimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testShiftColorImageNoWrap() {
 Shift shift = new Shift(2, 2, Shift.NOWRAP);
 ColorImage originalImage = new ColorImage(5, 5, new byte[15 * 5]);
 ColorImage shiftedImage = new ColorImage(5, 5, new byte[15 * 5]);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.setPixel(new RGBColor(x + y, x + y, x + y), x, y);
 shiftedImage.setPixel(new RGBColor(0, 0, 0), x, y);
 }
 }

 ColorImage result = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if ((x + 2 < 0) || (x + 2 >= 5) || (y + 2 < 0) || (y + 2 >= 5)) {
 shiftedImage.setPixel(new RGBColor(0, 0, 0), x, y);
 } else {
 shiftedImage.setPixel(originalImage.getPixel(x + 2, y + 2), x, y);
 }
 assertEquals("Shifted image does not match expected image at position (" + x + ", " + y + ")",
 shiftedImage.getPixel(x, y), result.getPixel(x, y));
 }
 }
 }

 @Test
 public void testShiftRealColorImage() {
 Shift shift = new Shift(2.5f, 2.5f, Shift.WRAP);
 RealColorImage originalImage = new RealColorImage(5, 5, new float[15 * 5]);
 RealColorImage shiftedImage = new RealColorImage(5, 5, new float[15 * 5]);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (x + y) / 255.0f);
 shiftedImage.set(x, y, 0.0f);
 }
 }

 RealColorImage result = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 assertEquals("Shifted image does not match expected image at position (" + x + ", " + y + ")",
 (originalImage.get(x + 2, y + 2) / 255.0f), shiftedImage.get(x, y), 0.01f);
 assertEquals("Applied shift operation does not match expected result at position (" + x + ", " + y + ")",
 result.get(x, y), shiftedImage.get(x, y), 0.01f);
 }
 }
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftRealColorImageNegativeOffsets() {
 Shift shift = new Shift(-2.5f, -2.5f, Shift.NOWRAP);
 RealColorImage originalImage = new RealColorImage(5, 5, new float[15 * 5]);
 try {
 shift.apply(originalImage);
 fail("Expected ImageDimensionMismatchException but no exception was thrown.");
 } catch (ImageDimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testShiftRealColorImageNoWrap() {
 Shift shift = new Shift(2.5f, 2.5f, Shift.NOWRAP);
 RealColorImage originalImage = new RealColorImage(5, 5, new float[15 * 5]);
 RealColorImage shiftedImage = new RealColorImage(5, 5, new float[15 * 5]);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (x + y) / 255.0f);
 shiftedImage.set(x, y, 0.0f);
 }
 }

 RealColorImage result = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if ((x + 2 < 0) || (x + 2 >= 5) || (y + 2 < 0) || (y + 2 >= 5)) {
 shiftedImage.set(x, y, 0.0f);
 } else {
 shiftedImage.set(originalImage.get(x + 2, y + 2), x, y);
 }
 assertEquals("Shifted image does not match expected image at position (" + x + ", " + y + ")",
 shiftedImage.get(x, y), result.get(x, y), 0.01f);
 }
 }
 }
}