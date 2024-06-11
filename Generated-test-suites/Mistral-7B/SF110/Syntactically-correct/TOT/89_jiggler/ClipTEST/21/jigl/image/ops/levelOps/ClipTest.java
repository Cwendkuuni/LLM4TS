package jigl.image.ops.levelOps;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {

 @Test
 public void testClipIntIntGrayImage() {
 GrayImage inputImage = new ByteGrayFactory().create(3, 3, new byte[][] {
 {(byte)128, (byte)128, (byte)128},
 {(byte)150, (byte)150, (byte)150},
 {(byte)200, (byte)200, (byte)200}
 });
 Clip clip = new Clip(100, 150);
 GrayImage expectedOutputImage = new ByteGrayFactory().create(3, 3, new byte[][] {
 {(byte)128, (byte)128, (byte)128},
 {(byte)150, (byte)150, (byte)150},
 {(byte)150, (byte)150, (byte)150}
 });
 GrayImage outputImage = new Clip().apply(inputImage, new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1));
 assertTrue(ImageUtils.areEqual(expectedOutputImage, outputImage));
 }

 @Test
 public void testClipIntIntRealGrayImage() {
 RealGrayImage inputImage = new FloatGrayFactory().create(3, 3, new float[][] {
 {1.2f, 1.2f, 1.2f},
 {1.5f, 1.5f, 1.5f},
 {2.0f, 2.0f, 2.0f}
 });
 Clip clip = new Clip(1.0f, 1.5f);
 RealGrayImage expectedOutputImage = new FloatGrayFactory().create(3, 3, new float[][] {
 {1.2f, 1.2f, 1.2f},
 {1.5f, 1.5f, 1.5f},
 {1.5f, 1.5f, 1.5f}
 });
 RealGrayImage outputImage = new Clip().apply(inputImage, new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1));
 assertTrue(ImageUtils.areEqual(expectedOutputImage, outputImage));
 }

 @Test
 public void testClipFloatFloatGrayImage() {
 GrayImage inputImage = new ByteGrayFactory().create(3, 3, new byte[][] {
 {(byte)128, (byte)128, (byte)128},
 {(byte)150, (byte)150, (byte)150},
 {(byte)200, (byte)200, (byte)200}
 });
 Clip clip = new Clip(1.5f, 2.0f);
 GrayImage expectedOutputImage = new ByteGrayFactory().create(3, 3, new byte[][] {
 {(byte)150, (byte)150, (byte)150},
 {(byte)150, (byte)150, (byte)150},
 {(byte)150, (byte)150, (byte)150}
 });
 GrayImage outputImage = new Clip().apply(inputImage, new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1));
 assertTrue(ImageUtils.areEqual(expectedOutputImage, outputImage));
 }

 @Test
 public void testClipIntIntColorImage() {
 ColorImage inputImage = new RGBImageFactory().create(3, 3, new int[][] {
 {0xFF0000, 0xFF0000, 0xFF0000},
 {0xFF5555, 0xFF5555, 0xFF5555},
 {0xFFFF00, 0xFFFF00, 0xFFFF00}
 });
 Clip clip = new Clip(100, 150);
 ColorImage expectedOutputImage = new RGBImageFactory().create(3, 3, new int[][] {
 {0xFF0000, 0xFF0000, 0xFF0000},
 {0xFF5555, 0xFF5555, 0xFF5555},
 {0xFF5555, 0xFF5555, 0xFF5555}
 });
 ColorImage outputImage = new Clip().apply(inputImage);
 assertTrue(ImageUtils.areEqual(expectedOutputImage, outputImage));
 }

 @Test
 public void testClipFloatFloatColorImage() {
 ColorImage inputImage = new RGBImageFactory().create(3, 3, new int[][] {
 {0xFF0000, 0xFF0000, 0xFF0000},
 {0xFF5555, 0xFF5555, 0xFF5555},
 {0xFFFF00, 0xFFFF00, 0xFFFF00}
 });
 Clip clip = new Clip(1.5f, 2.0f);
 ColorImage expectedOutputImage = new RGBImageFactory().create(3, 3, new int[][] {
 {0xFF0000, 0xFF0000, 0xFF0000},
 {0xFF5555, 0xFF5555, 0xFF5555},
 {0xFF5555, 0xFF5555, 0xFF5555}
 });
 ColorImage outputImage = new Clip().apply(inputImage);
 assertTrue(ImageUtils.areEqual(expectedOutputImage, outputImage));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipNullImage() {
 Clip clip = new Clip(1.0f, 2.0f);
 clip.apply(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipNullROI() {
 GrayImage inputImage = new ByteGrayFactory().create(3, 3, new byte[][] {
 {(byte)128, (byte)128, (byte)128},
 {(byte)150, (byte)150, (byte)150},
 {(byte)200, (byte)200, (byte)200}
 });
 Clip clip = new Clip(1.0f, 2.0f);
 clip.apply(inputImage, null);
 }
}