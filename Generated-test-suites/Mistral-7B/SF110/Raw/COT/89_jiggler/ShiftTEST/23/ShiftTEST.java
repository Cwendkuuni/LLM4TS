package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import org.junit.Test;

public class ShiftTest {

 // Test constructor
 @Test
 public void testShiftConstructor() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 assertEquals(2, shift.getXoffset());
 assertEquals(3, shift.getYoffset());
 assertTrue(shift.isWrap());
 }

 // Test apply method for GrayImage with positive offsets and no wrap
 @Test
 public void testApplyGrayImagePositiveOffsetsNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(5, 5);
 GrayImage expectedOutputImage = new GrayImage(5, 5);
 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 expectedOutputImage.set(x, y, inputImage.get(x + 2, y + 3));
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method for GrayImage with negative offsets and no wrap
 @Test
 public void testApplyGrayImageNegativeOffsetsNoWrap() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(5, 5);
 GrayImage expectedOutputImage = new GrayImage(5, 5);
 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if (x - 2 >= 0 && x - 2 < 5 && y - 3 >= 0 && y - 3 < 5) {
 expectedOutputImage.set(x, y, inputImage.get(x - 2, y - 3));
 } else {
 expectedOutputImage.set(x, y, Byte.MIN_VALUE);
 }
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method for GrayImage with positive offsets and wrap
 @Test
 public void testApplyGrayImagePositiveOffsetsWrap() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage inputImage = new GrayImage(5, 5);
 GrayImage expectedOutputImage = new GrayImage(5, 5);
 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if ((x + 2) % 5 >= 0 && (y + 3) % 5 >= 0) {
 expectedOutputImage.set((x + 2) % 5, (y + 3) % 5, inputImage.get(x, y));
 } else {
 expectedOutputImage.set((x + 2) % 5, (y + 3) % 5, Byte.MIN_VALUE);
 }
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method for GrayImage with negative offsets and wrap
 @Test
 public void testApplyGrayImageNegativeOffsetsWrap() {
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 GrayImage inputImage = new GrayImage(5, 5);
 GrayImage expectedOutputImage = new GrayImage(5, 5);
 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if ((x + 2) % 5 < 0 || (y + 3) % 5 < 0) {
 expectedOutputImage.set((x + 2) % 5, (y + 3) % 5, Byte.MIN_VALUE);
 } else {
 expectedOutputImage.set((x + 2) % 5, (y + 3) % 5, inputImage.get(x, y));
 }
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method for RealGrayImage with positive offsets and no wrap
 @Test
 public void testApplyRealGrayImagePositiveOffsetsNoWrap() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.NOWRAP);
 RealGrayImage inputImage = new RealGrayImage(5, 5);
 RealGrayImage expectedOutputImage = new RealGrayImage(5, 5);
 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 expectedOutputImage.set(x, y, inputImage.get(x + 2.0f, y + 3.0f));
 }
 }
 RealGrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method for RealGrayImage with negative offsets and no wrap
 @Test
 public void testApplyRealGrayImageNegativeOffsetsNoWrap() {
 Shift shift = new Shift(-2.0f, -3.0f, Shift.NOWRAP);
 RealGrayImage inputImage = new RealGrayImage(5, 5);
 RealGrayImage expectedOutputImage = new RealGrayImage(5, 5);
 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if (x - 2.0f >= 0 && x - 2.0f < 5 && y - 3.0f >= 0 && y - 3.0f < 5) {
 expectedOutputImage.set(x, y, inputImage.get(x - 2.0f, y - 3.0f));
 } else {
 expectedOutputImage.set(x, y, Float.valueOf(0.0f));
 }
 }
 }
 RealGrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method for RealGrayImage with positive offsets and wrap
 @Test
 public void testApplyRealGrayImagePositiveOffsetsWrap() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
 RealGrayImage inputImage = new RealGrayImage(5, 5);
 RealGrayImage expectedOutputImage = new RealGrayImage(5, 5);
 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if ((x + 2.0f) % 5 >= 0 && (y + 3.0f) % 5 >= 0) {
 expectedOutputImage.set((x + 2.0f) % 5, (y + 3.0f) % 5, inputImage.get(x, y));
 } else {
 expectedOutputImage.set((x + 2.0f) % 5, (y + 3.0f) % 5, Float.valueOf(0.0f));
 }
 }
 }
 RealGrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method for RealGrayImage with negative offsets and wrap
 @Test
 public void testApplyRealGrayImageNegativeOffsetsWrap() {
 Shift shift = new Shift(-2.0f, -3.0f, Shift.WRAP);
 RealGrayImage inputImage = new RealGrayImage(5, 5);
 RealGrayImage expectedOutputImage = new RealGrayImage(5, 5);
 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if ((x + 2.0f) % 5 < 0 || (y + 3.0f) % 5 < 0) {
 expectedOutputImage.set((x + 2.0f) % 5, (y + 3.0f) % 5, Float.valueOf(0.0f));
 } else {
 expectedOutputImage.set((x + 2.0f) % 5, (y + 3.0f) % 5, inputImage.get(x, y));
 }
 }
 }
 RealGrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test exception handling: invalid input image
 @Test(expected = IllegalArgumentException.class)
 public void testApplyInvalidInputImage() {
 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 // Input image with invalid dimensions
 GrayImage inputImage = new GrayImage(3, 3);
 shift.apply(inputImage);
 }

 // Test exception handling: negative offsets and no wrap
 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyNegativeOffsetsNoWrap() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(5, 5);
 shift.apply(inputImage);
 }

 // Test exception handling: positive offsets and image dimensions not enough to wrap
 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyPositiveOffsetsNotEnoughToWrap() {
 Shift shift = new Shift(5, 5, Shift.WRAP);
 GrayImage inputImage = new GrayImage(3, 3);
 shift.apply(inputImage);
 }

 // Test exception handling: negative wrap offset
 @Test(expected = IllegalArgumentException.class)
 public void testApplyNegativeWrapOffset() {
 Shift shift = new Shift(-2, 3, Shift.WRAP);
 GrayImage inputImage = new GrayImage(5, 5);
 shift.apply(inputImage);
 }
}