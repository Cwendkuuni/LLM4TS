import jigl.image.*;
import jigl.image.ops.Shift;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

 @Test
 public void testShiftGrayImageWithPositiveOffsetsAndNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
 inputImage.set(i, (byte) i % 256);
 }
 GrayImage expectedOutputImage = new GrayImage(8, 8);
 for (int x = 0; x < expectedOutputImage.X(); x++) {
 for (int y = 0; y < expectedOutputImage.Y(); y++) {
 int index = (x + 2) % 10 * 10 + (y + 3);
 expectedOutputImage.set(x, y, inputImage.get(index));
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals("Shift with positive offsets and no wrap", expectedOutputImage, outputImage);
 }

 @Test
 public void testShiftGrayImageWithPositiveOffsetsAndWrap() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
 inputImage.set(i, (byte) i % 256);
 }
 GrayImage expectedOutputImage = new GrayImage(10, 10);
 for (int x = 0; x < expectedOutputImage.X(); x++) {
 for (int y = 0; y < expectedOutputImage.Y(); y++) {
 int index = ((x + 2) % 10 + 10) % 10 * 10 + ((y + 3) % 10 + 10) % 10;
 expectedOutputImage.set(x, y, inputImage.get(index));
 }
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertEquals("Shift with positive offsets and wrap", expectedOutputImage, outputImage);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testShiftGrayImageWithNegativeOffsets() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
 inputImage.set(i, (byte) i % 256);
 }
 shift.apply(inputImage);
 fail("Expected IndexOutOfBoundsException but it didn't occur.");
 }

 @Test
 public void testShiftGrayImageWithZeroOffsets() {
 Shift shift = new Shift(0, 0, Shift.NOWRAP);
 GrayImage inputImage = new GrayImage(10, 10);
 for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
 inputImage.set(i, (byte) i % 256);
 }
 GrayImage outputImage = shift.apply(inputImage);
 assertSame("Shift with zero offsets", inputImage, outputImage);
 }

 // Add test cases for the other methods in the Shift class

}