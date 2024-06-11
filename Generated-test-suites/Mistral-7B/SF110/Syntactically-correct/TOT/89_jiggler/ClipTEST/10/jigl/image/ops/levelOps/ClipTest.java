package jigl.image.ops.levelOps;

import static org.junit.Assert.*;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

import org.junit.Test;

public class ClipTest {

 @Test
 public void testClipIntegerValues() {
 GrayImage inputImage = new GrayImage(5, 5);
 inputImage.set(0, 0, 255);
 inputImage.set(1, 1, 10);
 inputImage.set(2, 2, 150);
 inputImage.set(3, 3, 200);
 inputImage.set(4, 4, 50);

 Clip clip = new Clip(100, 150);
 GrayImage outputImage = (GrayImage) clip.apply(inputImage);

 // Test output image dimensions
 assertEquals(inputImage.X(), outputImage.X());
 assertEquals(inputImage.Y(), outputImage.Y());

 // Test output image pixels values are within the specified range
 for (int y = 0; y < outputImage.Y(); y++) {
 for (int x = 0; x < outputImage.X(); x++) {
 int expectedPixelValue = Math.min(Math.max(inputImage.get(x, y).intValue(), clip.int_min), clip.int_max);
 int actualPixelValue = outputImage.get(x, y).intValue();
 assertEquals(expectedPixelValue, actualPixelValue);
 }
 }
 }

 @Test
 public void testClipFloatValues() {
 RealGrayImage inputImage = new RealGrayImage(5, 5);
 inputImage.set(0, 0, 2.5f);
 inputImage.set(1, 1, 0.6f);
 inputImage.set(2, 2, 1.5f);
 inputImage.set(3, 3, 3.0f);
 inputImage.set(4, 4, 0.1f);

 Clip clip = new Clip(1.0f, 2.5f);
 RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);

 // Test output image dimensions
 assertEquals(inputImage.X(), outputImage.X());
 assertEquals(inputImage.Y(), outputImage.Y());

 // Test output image pixels values are within the specified range
 for (int y = 0; y < outputImage.Y(); y++) {
 for (int x = 0; x < outputImage.X(); x++) {
 float expectedPixelValue = Math.min(Math.max(inputImage.get(x, y).floatValue(), clip.float_min), clip.float_max);
 float actualPixelValue = outputImage.get(x, y).floatValue();
 assertEquals(expectedPixelValue, actualPixelValue, 0.001f);
 }
 }
 }

 @Test
 public void testClipNegativeValues() {
 GrayImage inputImage = new GrayImage(5, 5);
 inputImage.set(0, 0, -50);
 inputImage.set(1, 1, -10);
 inputImage.set(2, 2, -150);
 inputImage.set(3, 3, -200);
 inputImage.set(4, 4, -50);

 Clip clip = new Clip(0, 50);
 GrayImage outputImage = (GrayImage) clip.apply(inputImage);

 // Test output image dimensions
 assertEquals(inputImage.X(), outputImage.X());
 assertEquals(inputImage.Y(), outputImage.Y());

 // Test output image pixels values are not negative
 for (int y = 0; y < outputImage.Y(); y++) {
 for (int x = 0; x < outputImage.X(); x++) {
 int actualPixelValue = outputImage.get(x, y).intValue();
 assertTrue(actualPixelValue >= 0);
 }
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullInputImage() {
 Clip clip = new Clip(0, 0);
 try {
 clip.apply(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMinValue() {
 Clip clip = new Clip(-1, 0);
 try {
 clip.apply(new GrayImage(5, 5));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMaxValue() {
 Clip clip = new Clip(0, -1);
 try {
 clip.apply(new GrayImage(5, 5));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMinMaxValues() {
 Clip clip = new Clip(2.5f, 1.0f);
 try {
 clip.apply(new GrayImage(5, 5));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMinMaxValues2() {
 Clip clip = new Clip(0.5f, 2.5f);
 try {
 clip.apply(new GrayImage(5, 5));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}