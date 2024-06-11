package jigl.image.ops.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;

public class ShiftTest {

 @Test
 public void testShiftGrayImage() {
 Shift shift = new Shift(2, 2, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutput = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 expectedOutput.set(i, j, inputImage.get(i + 2, j + 2));
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutput, outputImage);
 }

 @Test
 public void testShiftGrayImageWithWrapX() {
 Shift shift = new Shift(2, 2, Shift.WRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutput = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 int newX = i + 2;
 if (newX >= 10) {
 newX -= 10;
 }
 expectedOutput.set(i, j, inputImage.get(newX, j));
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutput, outputImage);
 }

 @Test
 public void testShiftGrayImageWithWrapY() {
 Shift shift = new Shift(2, 2, Shift.WRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 GrayImage expectedOutput = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 int newY = j + 2;
 if (newY >= 10) {
 newY -= 10;
 }
 expectedOutput.set(i, j, inputImage.get(i, newY));
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutput, outputImage);
 }

 @Test
 public void testShiftGrayImageWithOutOfBounds() {
 Shift shift = new Shift(10, 10, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(5, 5);
 GrayImage outputImage = shift.apply(inputImage);
 assertNotNull(outputImage);
 }

 @Test
 public void testShiftRealGrayImage() {
 Shift shift = new Shift(2.0f, 2.0f, Shift.NOWRAP);
 RealGrayImage inputImage = new RealGrayImage(10, 10);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 expectedOutput.set(i, j, inputImage.get(i + 2.0f, j + 2.0f));
 }
 }
 RealGrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutput, outputImage);
 }

 @Test
 public void testShiftRealGrayImageWithWrapX() {
 Shift shift = new Shift(2.0f, 2.0f, Shift.WRAP);
 RealGrayImage inputImage = new RealGrayImage(10, 10);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 double newX = i + 2.0f;
 if (newX >= 10.0f) {
 newX -= 10.0f;
 }
 expectedOutput.set(i, j, inputImage.get(newX, j));
 }
 }
 RealGrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutput, outputImage);
 }

 @Test
 public void testShiftRealGrayImageWithWrapY() {
 Shift shift = new Shift(2.0f, 2.0f, Shift.WRAP);
 RealGrayImage inputImage = new RealGrayImage(10, 10);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 double newY = j + 2.0f;
 if (newY >= 10.0f) {
 newY -= 10.0f;
 }
 expectedOutput.set(i, j, inputImage.get(i, newY));
 }
 }
 RealGrayImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutput, outputImage);
 }

 @Test
 public void testShiftColorImage() {
 Shift shift = new Shift(2, 2, Shift.NOWRAP);
 ColorImage inputImage = new ColorImage(10, 10, new byte[10 * 10 * 3], 10, 10);
 ColorImage expectedOutput = new ColorImage(10, 10, new byte[10 * 10 * 3], 10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 for (int k = 0; k < 3; k++) {
 expectedOutput.set(i, j, k, inputImage.get(i + 2, j + 2, k));
 }
 }
 }
 ColorImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutput, outputImage);
 }

 @Test
 public void testShiftRealColorImage() {
 Shift shift = new Shift(2.0f, 2.0f, Shift.NOWRAP);
 RealColorImage inputImage = new RealColorImage(10, 10, new float[10 * 10 * 3], 10, 10);
 RealColorImage expectedOutput = new RealColorImage(10, 10, new float[10 * 10 * 3], 10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 for (int k = 0; k < 3; k++) {
 expectedOutput.set(i, j, k, inputImage.get(i + 2.0f, j + 2.0f, k));
 }
 }
 }
 RealColorImage outputImage = shift.apply(inputImage);
 assertEquals(expectedOutput, outputImage);
 }
}