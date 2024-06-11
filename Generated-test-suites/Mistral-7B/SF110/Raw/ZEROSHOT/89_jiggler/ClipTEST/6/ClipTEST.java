package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import org.junit.Test;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

 @Test
 public void testApplyGrayImageIntInt() {
 GrayImage inputImage = new ByteGrayImage(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 4, 4);
 Clip clip = new Clip(2, 6);
 Image expectedOutputImage = new ByteGrayImage(new int[][]{{2, 2, 2, 2}, {5, 5, 5, 5}, {9, 9, 9, 9}, {13, 13, 13, 13}}, 4, 4);
 Image outputImage = clip.apply(inputImage);
 assertEquals("Failed to apply Clip operator with int min and max values to GrayImage", expectedOutputImage, outputImage);
 }

 @Test
 public void testApplyGrayImageFloatFloat() {
 GrayImage inputImage = new ByteGrayImage(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 4, 4);
 Clip clip = new Clip(2.5f, 6.5f);
 Image expectedOutputImage = new ByteGrayImage(new int[][]{{2, 2, 2, 2}, {5, 5, 5, 5}, {9, 9, 9, 9}, {13, 13, 13, 13}}, 4, 4);
 Image outputImage = clip.apply(inputImage);
 assertEquals("Failed to apply Clip operator with float min and max values to GrayImage", expectedOutputImage, outputImage);
 }

 @Test
 public void testApplyGrayImageWithDefaultMinMax() {
 GrayImage inputImage = new ByteGrayImage(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 4, 4);
 Clip clip = new Clip();
 Image outputImage = clip.apply(inputImage);
 assertEquals("Failed to apply Clip operator with default min and max values to GrayImage", inputImage, outputImage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageWithInvalidMinValue() {
 GrayImage inputImage = new ByteGrayImage(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 4, 4);
 Clip clip = new Clip(-1);
 try {
 clip.apply(inputImage);
 fail("Expected IllegalArgumentException with invalid min value");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageWithInvalidMaxValue() {
 GrayImage inputImage = new ByteGrayImage(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 4, 4);
 Clip clip = new Clip(16);
 try {
 clip.apply(inputImage);
 fail("Expected IllegalArgumentException with invalid max value");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testApplyRealGrayImageIntInt() {
 RealGrayImage inputImage = new FloatGrayImage(new float[][]{{1.0f, 2.0f, 3.0f, 4.0f}, {5.0f, 6.0f, 7.0f, 8.0f}, {9.0f, 10.0f, 11.0f, 12.0f}, {13.0f, 14.0f, 15.0f, 16.0f}}, 4, 4);
 Clip clip = new Clip(2.0f, 6.0f);
 RealGrayImage expectedOutputImage = new FloatGrayImage(new float[][]{{2.0f, 2.0f, 2.0f, 2.0f}, {5.0f, 5.0f, 5.0f, 5.0f}, {9.0f, 9.0f, 9.0f, 9.0f}, {13.0f, 13.0f, 13.0f, 13.0f}}, 4, 4);
 RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);
 assertEquals("Failed to apply Clip operator with float min and max values to RealGrayImage", expectedOutputImage, outputImage);
 }

 @Test
 public void testApplyRealGrayImageFloatFloat() {
 RealGrayImage inputImage = new FloatGrayImage(new float[][]{{1.0f, 2.0f, 3.0f, 4.0f}, {5.0f, 6.0f, 7.0f, 8.0f}, {9.0f, 10.0f, 11.0f, 12.0f}, {13.0f, 14.0f, 15.0f, 16.0f}}, 4, 4);
 Clip clip = new Clip(2.5f, 6.5f);
 RealGrayImage expectedOutputImage = new FloatGrayImage(new float[][]{{2.0f, 2.0f, 2.0f, 2.0f}, {5.0f, 5.0f, 5.0f, 5.0f}, {9.0f, 9.0f, 9.0f, 9.0f}, {13.0f, 13.0f, 13.0f, 13.0f}}, 4, 4);
 RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);
 assertEquals("Failed to apply Clip operator with int min and max values to RealGrayImage", expectedOutputImage, outputImage);
 }

 @Test
 public void testApplyRealGrayImageWithDefaultMinMax() {
 RealGrayImage inputImage = new FloatGrayImage(new float[][]{{1.0f, 2.0f, 3.0f, 4.0f}, {5.0f, 6.0f, 7.0f, 8.0f}, {9.0f, 10.0f, 11.0f, 12.0f}, {13.0f, 14.0f, 15.0f, 16.0f}}, 4, 4);
 Clip clip = new Clip();
 RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);
 assertEquals("Failed to apply Clip operator with default min and max values to RealGrayImage", inputImage, outputImage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageWithInvalidMinValue() {
 RealGrayImage inputImage = new FloatGrayImage(new float[][]{{1.0f, 2.0f, 3.0f, 4.0f}, {5.0f, 6.0f, 7.0f, 8.0f}, {9.0f, 10.0f, 11.0f, 12.0f}, {13.0f, 14.0f, 15.0f, 16.0f}}, 4, 4);
 Clip clip = new Clip(-1.5f);
 try {
 clip.apply(inputImage);
 fail("Expected IllegalArgumentException with invalid min value");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageWithInvalidMaxValue() {
 RealGrayImage inputImage = new FloatGrayImage(new float[][]{{1.0f, 2.0f, 3.0f, 4.0f}, {5.0f, 6.0f, 7.0f, 8.0f}, {9.0f, 10.0f, 11.0f, 12.0f}, {13.0f, 14.0f, 15.0f, 16.0f}}, 4, 4);
 Clip clip = new Clip(17.5f);
 try {
 clip.apply(inputImage);
 fail("Expected IllegalArgumentException with invalid max value");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testApplyColorImage() {
 ColorImage inputImage = new RGBImage(new byte[][]{{(byte) 1, (byte) 2, (byte) 3, (byte) 4}, {(byte) 5, (byte) 6, (byte) 7, (byte) 8}, {(byte) 9, (byte) 10, (byte) 11, (byte) 12}, {(byte) 13, (byte) 14, (byte) 15, (byte) 16}}, 4, 4, 3);
 Clip clip = new Clip(new int[]{2, 6}, new int[]{9, 13});
 ColorImage expectedOutputImage = new RGBImage(new byte[][]{{2, 2, 2, 2}, {5, 5, 5, 5}, {9, 9, 9, 9}, {13, 13, 13, 13}}, 4, 4, 3);
 Image outputImage = clip.apply(inputImage);
 assertEquals("Failed to apply Clip operator with int min and max values to ColorImage", expectedOutputImage, outputImage);
 }

 @Test
 public void testApplyColorImageWithDefaultMinMax() {
 ColorImage inputImage = new RGBImage(new byte[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 4, 4, 3);
 Clip clip = new Clip();
 ColorImage outputImage = clip.apply(inputImage);
 assertEquals("Failed to apply Clip operator with default min and max values to ColorImage", inputImage, outputImage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyColorImageWithInvalidMinValue() {
 ColorImage inputImage = new RGBImage(new byte[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 4, 4, 3);
 Clip clip = new Clip(-1, -1);
 try {
 clip.apply(inputImage);
 fail("Expected IllegalArgumentException with invalid min value");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyColorImageWithInvalidMaxValue() {
 ColorImage inputImage = new RGBImage(new byte[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, 4, 4, 3);
 Clip clip = new Clip(17, 17);
 try {
 clip.apply(inputImage);
 fail("Expected IllegalArgumentException with invalid max value");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}