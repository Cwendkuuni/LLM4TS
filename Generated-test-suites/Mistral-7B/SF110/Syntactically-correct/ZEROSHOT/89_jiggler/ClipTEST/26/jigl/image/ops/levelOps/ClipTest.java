package jigl.image.ops.levelOps;

import static org.junit.Assert.*;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Test;

public class ClipTest {

 @Test
 public void testApplyGrayImageIntMinIntMax() {
 GrayImage originalImage = new BufferedImageGray(5, 5, DataTypes.BYTE);
 originalImage.set(1, 1, (byte) 100);
 originalImage.set(2, 2, (byte) 50);
 originalImage.set(3, 3, (byte) 150);
 originalImage.set(4, 4, (byte) 200);

 ROI roi = new ROI(1, 1, 3, 3);
 int min = 50;
 int max = 150;
 Clip clip = new Clip(min, max);
 GrayImage resultImage = (GrayImage)clip.apply(originalImage, roi);

 byte[] expectedResult = {(byte) 50, (byte) 50, (byte) 50, (byte) 150, (byte) 150, (byte) 150};
 byte[] result = new byte[resultImage.height() * resultImage.width()];
 resultImage.getPixels(result, 0, 0, 0, 0, resultImage.width(), resultImage.height());

 assertArrayEquals("Unexpected result after applying clip filter with integers", expectedResult, result);
 }

 @Test
 public void testApplyGrayImageFloatMinFloatMax() {
 GrayImage originalImage = new BufferedImageGray(5, 5, DataTypes.BYTE);
 originalImage.set(1, 1, (byte) 100);
 originalImage.set(2, 2, (byte) 50);
 originalImage.set(3, 3, (byte) 150);
 originalImage.set(4, 4, (byte) 200);

 ROI roi = new ROI(1, 1, 3, 3);
 float min = 50.0f;
 float max = 150.0f;
 Clip clip = new Clip(min, max);
 GrayImage resultImage = (GrayImage)clip.apply(originalImage, roi);

 byte[] expectedResult = {(byte) 50, (byte) 50, (byte) 50, (byte) 150, (byte) 150, (byte) 150};
 byte[] result = new byte[resultImage.height() * resultImage.width()];
 resultImage.getPixels(result, 0, 0, 0, 0, resultImage.width(), resultImage.height());

 assertArrayEquals("Unexpected result after applying clip filter with floats", expectedResult, result);
 }

 @Test
 public void testApplyGrayImageZeroMinZeroMax() {
 GrayImage originalImage = new BufferedImageGray(5, 5, DataTypes.BYTE);
 originalImage.set(1, 1, (byte) 100);
 originalImage.set(2, 2, (byte) 50);
 originalImage.set(3, 3, (byte) 150);
 originalImage.set(4, 4, (byte) 200);

 ROI roi = new ROI(1, 1, 3, 3);
 Clip clip = new Clip();
 GrayImage resultImage = (GrayImage)clip.apply(originalImage, roi);

 byte[] expectedResult = {(byte) 50, (byte) 50, (byte) 50, (byte) 150, (byte) 150, (byte) 150};
 byte[] result = new byte[resultImage.height() * resultImage.width()];
 resultImage.getPixels(result, 0, 0, 0, 0, resultImage.width(), resultImage.height());

 assertArrayEquals("Unexpected result after applying clip filter with default min and max", expectedResult, result);
 }

 @Test
 public void testApplyRealGrayImageIntMinIntMax() {
 RealGrayImage originalImage = new BufferedImageRealGray(5, 5, DataTypes.FLOAT);
 originalImage.set(1, 1, 100.0f);
 originalImage.set(2, 2, 50.0f);
 originalImage.set(3, 3, 150.0f);
 originalImage.set(4, 4, 200.0f);

 ROI roi = new ROI(1, 1, 3, 3);
 int min = 50;
 int max = 150;
 Clip clip = new Clip(min, max);
 RealGrayImage resultImage = (RealGrayImage)clip.apply(originalImage, roi);

 float[] expectedResult = {50.0f, 50.0f, 50.0f, 150.0f, 150.0f, 150.0f};
 float[] result = new float[resultImage.height() * resultImage.width()];
 resultImage.getPixels(result, 0, 0, 0, 0, resultImage.width(), resultImage.height());

 assertArrayEquals("Unexpected result after applying clip filter with integers", expectedResult, result, 0.001f);
 }

 @Test
 public void testApplyRealGrayImageFloatMinFloatMax() {
 RealGrayImage originalImage = new BufferedImageRealGray(5, 5, DataTypes.FLOAT);
 originalImage.set(1, 1, 100.0f);
 originalImage.set(2, 2, 50.0f);
 originalImage.set(3, 3, 150.0f);
 originalImage.set(4, 4, 200.0f);

 ROI roi = new ROI(1, 1, 3, 3);
 float min = 50.0f;
 float max = 150.0f;
 Clip clip = new Clip(min, max);
 RealGrayImage resultImage = (RealGrayImage)clip.apply(originalImage, roi);

 float[] expectedResult = {50.0f, 50.0f, 50.0f, 150.0f, 150.0f, 150.0f};
 float[] result = new float[resultImage.height() * resultImage.width()];
 resultImage.getPixels(result, 0, 0, 0, 0, resultImage.width(), resultImage.height());

 assertArrayEquals("Unexpected result after applying clip filter with floats", expectedResult, result, 0.001f);
 }

 @Test
 public void testApplyRealGrayImageZeroMinZeroMax() {
 RealGrayImage originalImage = new BufferedImageRealGray(5, 5, DataTypes.FLOAT);
 originalImage.set(1, 1, 100.0f);
 originalImage.set(2, 2, 50.0f);
 originalImage.set(3, 3, 150.0f);
 originalImage.set(4, 4, 200.0f);

 ROI roi = new ROI(1, 1, 3, 3);
 Clip clip = new Clip();
 RealGrayImage resultImage = (RealGrayImage)clip.apply(originalImage, roi);

 float[] expectedResult = {50.0f, 50.0f, 50.0f, 150.0f, 150.0f, 150.0f};
 float[] result = new float[resultImage.height() * resultImage.width()];
 resultImage.getPixels(result, 0, 0, 0, 0, resultImage.width(), resultImage.height());

 assertArrayEquals("Unexpected result after applying clip filter with default min and max", expectedResult, result, 0.001f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullImage() {
 GrayImage originalImage = null;
 ROI roi = new ROI(0, 0, 5, 5);
 Clip clip = new Clip(50, 150);
 clip.apply(originalImage, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullRoi() {
 GrayImage originalImage = new BufferedImageGray(5, 5, DataTypes.BYTE);
 Clip clip = new Clip(50, 150);
 clip.apply(originalImage, null);
 }

 @Test
 public void testApplyColorImageIntMinIntMax() {
 ColorImage originalImage = new BufferedImageRGB(5, 5, DataTypes.BYTE);
 originalImage.setPlane(0, new BufferedImageGray(5, 5, DataTypes.BYTE) {{ set(1, 1, (byte) 100); set(2, 2, (byte) 50); set(3, 3, (byte) 150); set(4, 4, (byte) 200); }});
 originalImage.setPlane(1, new BufferedImageGray(5, 5, DataTypes.BYTE) {{ set(1, 1, (byte) 255); set(2, 2, (byte) 0); set(3, 3, (byte) 255); set(4, 4, (byte) 0); }});
 originalImage.setPlane(2, new BufferedImageGray(5, 5, DataTypes.BYTE) {{ set(1, 1, (byte) 255); set(2, 2, (byte) 0); set(3, 3, (byte) 255); set(4, 4, (byte) 0); }});

 ROI roi = new ROI(1, 1, 3, 3);
 int min = 50;
 int max = 150;
 Clip clip = new Clip(min, max);
 ColorImage resultImage = (ColorImage)clip.apply(originalImage, roi);

 assertEquals("Unexpected number of planes in the result image after applying clip filter", 3, resultImage.numPlanes());
 for (int plane = 0; plane < resultImage.numPlanes(); plane++) {
 GrayImage planeImage = resultImage.plane(plane);

 byte[] expectedResult = {(byte) 50, (byte) 50, (byte) 50, (byte) 150, (byte) 150, (byte) 150};
 byte[] result = new byte[planeImage.height() * planeImage.width()];
 planeImage.getPixels(result, 0, 0, 0, 0, planeImage.width(), planeImage.height());

 assertArrayEquals("Unexpected result after applying clip filter to plane " + plane, expectedResult, result);
 }
 }

 @Test
 public void testApplyColorImageFloatMinFloatMax() {
 ColorImage originalImage = new BufferedImageRGB(5, 5, DataTypes.FLOAT);
 originalImage.setPlane(0, new BufferedImageRealGray(5, 5, DataTypes.FLOAT) {{ set(1, 1, 100.0f); set(2, 2, 50.0f); set(3, 3, 150.0f); set(4, 4, 200.0f); }});
 originalImage.setPlane(1, new BufferedImageRealGray(5, 5, DataTypes.FLOAT) {{ set(1, 1, 255.0f); set(2, 2, 0.0f); set(3, 3, 255.0f); set(4, 4, 0.0f); }});
 originalImage.setPlane(2, new BufferedImageRealGray(5, 5, DataTypes.FLOAT) {{ set(1, 1, 255.0f); set(2, 2, 0.0f); set(3, 3, 255.0f); set(4, 4, 0.0f); }});

 ROI roi = new ROI(1, 1, 3, 3);
 float min = 50.0f;
 float max = 150.0f;
 Clip clip = new Clip(min, max);
 ColorImage resultImage = (ColorImage)clip.apply(originalImage, roi);

 assertEquals("Unexpected number of planes in the result image after applying clip filter", 3, resultImage.numPlanes());
 for (int plane = 0; plane < resultImage.numPlanes(); plane++) {
 RealGrayImage planeImage = (RealGrayImage)resultImage.plane(plane);

 float[] expectedResult = {50.0f, 50.0f, 50.0f, 150.0f, 150.0f, 150.0f};
 float[] result = new float[planeImage.height() * planeImage.width()];
 planeImage.getPixels(result, 0, 0, 0, 0, planeImage.width(), planeImage.height());

 assertArrayEquals("Unexpected result after applying clip filter to plane " + plane, expectedResult, result, 0.001f);
 }
 }

 @Test
 public void testApplyColorImageZeroMinZeroMax() {
 ColorImage originalImage = new BufferedImageRGB(5, 5, DataTypes.FLOAT);
 originalImage.setPlane(0, new BufferedImageRealGray(5, 5, DataTypes.FLOAT) {{ set(1, 1, 100.0f); set(2, 2, 50.0f); set(3, 3, 150.0f); set(4, 4, 200.0f); }});
 originalImage.setPlane(1, new BufferedImageRealGray(5, 5, DataTypes.FLOAT) {{ set(1, 1, 255.0f); set(2, 2, 0.0f); set(3, 3, 255.0f); set(4, 4, 0.0f); }});
 originalImage.setPlane(2, new BufferedImageRealGray(5, 5, DataTypes.FLOAT) {{ set(1, 1, 255.0f); set(2, 2, 0.0f); set(3, 3, 255.0f); set(4, 4, 0.0f); }});

 ROI roi = new ROI(1, 1, 3, 3);
 Clip clip = new Clip();
 ColorImage resultImage = (ColorImage)clip.apply(originalImage, roi);

 assertEquals("Unexpected number of planes in the result image after applying clip filter with default min and max", 3, resultImage.numPlanes());
 for (int plane = 0; plane < resultImage.numPlanes(); plane++) {
 RealGrayImage planeImage = (RealGrayImage)resultImage.plane(plane);

 float[] expectedResult = {50.0f, 50.0f, 50.0f, 150.0f, 150.0f, 150.0f};
 float[] result = new float[planeImage.height() * planeImage.width()];
 planeImage.getPixels(result, 0, 0, 0, 0, planeImage.width(), planeImage.height());

 assertArrayEquals("Unexpected result after applying clip filter to plane " + plane, expectedResult, result, 0.001f);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyColorImageNullImage() {
 ColorImage originalImage = null;
 ROI roi = new ROI(0, 0, 5, 5);
 Clip clip = new Clip(50, 150);
 clip.apply(originalImage, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyColorImageNullRoi() {
 ColorImage originalImage = new BufferedImageRGB(5, 5, DataTypes.BYTE);
 Clip clip = new Clip(50, 150);
 clip.apply(originalImage, null);
 }
}