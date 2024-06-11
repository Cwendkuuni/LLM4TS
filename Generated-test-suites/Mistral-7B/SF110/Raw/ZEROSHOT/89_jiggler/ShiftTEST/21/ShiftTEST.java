package jigl.image.ops.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.ops.Shift;

public class ShiftTest {

 @Test
 public void testShiftWithPositiveXOffsetAndYOffsetAndNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutputImage = new GrayImage(8, 8);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 inputImage.set(i, j, (byte) 128);
 }
 }

 GrayImage outputImage = shift.apply(inputImage);

 for (int i = 0; i < 8; i++) {
 for (int j = 0; j < 8; j++) {
 expectedOutputImage.set(i, j, inputImage.get(i + 2, j + 3));
 }
 }

 assertEquals(expectedOutputImage, outputImage);
 }

 @Test
 public void testShiftWithPositiveXOffsetAndNoYOffsetAndNoWrap() {
 Shift shift = new Shift(2, 0, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutputImage = new GrayImage(10, 8);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 inputImage.set(i, j, (byte) 128);
 }
 }

 GrayImage outputImage = shift.apply(inputImage);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 8; j++) {
 expectedOutputImage.set(i, j, inputImage.get(i + 2, j));
 }
 }

 assertEquals(expectedOutputImage, outputImage);
 }

 @Test
 public void testShiftWithPositiveYOffsetAndNoXOffsetAndNoWrap() {
 Shift shift = new Shift(0, 2, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutputImage = new GrayImage(10, 8);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 inputImage.set(i, j, (byte) 128);
 }
 }

 GrayImage outputImage = shift.apply(inputImage);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 8; j++) {
 expectedOutputImage.set(i, j, inputImage.get(i, j + 2));
 }
 }

 assertEquals(expectedOutputImage, outputImage);
 }

 @Test
 public void testShiftWithPositiveXOffsetAndPositiveYOffsetAndNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutputImage = new GrayImage(6, 6);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 inputImage.set(i, j, (byte) 128);
 }
 }

 GrayImage outputImage = shift.apply(inputImage);

 for (int i = 0; i < 6; i++) {
 for (int j = 0; j < 6; j++) {
 expectedOutputImage.set(i, j, inputImage.get(i + 2, j + 3));
 }
 }

 assertEquals(expectedOutputImage, outputImage);
 }

 @Test
 public void testShiftWithNegativeXOffsetAndPositiveYOffsetAndWrap() {
 Shift shift = new Shift(-2, 3, Shift.WRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutputImage = new GrayImage(12, 8);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 inputImage.set(i, j, (byte) 128);
 }
 }

 GrayImage outputImage = shift.apply(inputImage);

 for (int i = 0; i < 12; i++) {
 for (int j = 0; j < 8; j++) {
 expectedOutputImage.set(i, j, inputImage.get(i + 2, j + 3));
 if (i == 10 || i == 11) {
 expectedOutputImage.set(i, j, inputImage.get(i % 10, j));
 }
 }
 }

 assertEquals(expectedOutputImage, outputImage);
 }

 @Test
 public void testShiftWithPositiveXOffsetAndNegativeYOffsetAndWrap() {
 Shift shift = new Shift(2, -3, Shift.WRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutputImage = new GrayImage(10, 13);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 inputImage.set(i, j, (byte) 128);
 }
 }

 GrayImage outputImage = shift.apply(inputImage);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 13; j++) {
 expectedOutputImage.set(i, j, inputImage.get(i + 2, j - 3));
 if (j == 7 || j == 8) {
 expectedOutputImage.set(i, j, inputImage.get(i, j));
 }
 }
 }

 assertEquals(expectedOutputImage, outputImage);
 }

 @Test
 public void testShiftWithNegativeXOffsetAndNegativeYOffsetAndWrap() {
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutputImage = new GrayImage(12, 13);

 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 inputImage.set(i, j, (byte) 128);
 }
 }

 GrayImage outputImage = shift.apply(inputImage);

 for (int i = 0; i < 12; i++) {
 for (int j = 0; j < 13; j++) {
 expectedOutputImage.set(i, j, inputImage.get(i + 2, j - 3));
 if (i == 10 || i == 11) {
 expectedOutputImage.set(i, j, inputImage.get(i % 10, j));
 }
 if (j == 10 || j == 11) {
 expectedOutputImage.set(i, j, inputImage.get(i, j % 10));
 }
 }
 }

 assertEquals(expectedOutputImage, outputImage);
 }

 @Test
 public void testShiftWithInvalidXOffsetAndYOffset() {
 Shift shift = new Shift(-5, -5);
 assertNotNull(shift);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftWithNegativeXOffsetAndInvalidYOffset() {
 Shift shift = new Shift(-5, -50);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftWithPositiveXOffsetAndInvalidYOffset() {
 Shift shift = new Shift(5, 50);
 }
}