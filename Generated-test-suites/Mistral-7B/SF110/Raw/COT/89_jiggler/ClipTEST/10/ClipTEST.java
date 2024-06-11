import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.GrayImage;
import jigl.image.ops.RealGrayImage;
import jigl.image.ops.ColorImage;
import jigl.image.ops.RealColorImage;
import jigl.image.types.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import static org.junit.Assert.assertEquals;

public class ClipTest {

 private Clip clip;
 private GrayImage image;
 private RealGrayImage imageReal;
 private ColorImage imageColor;
 private RealColorImage imageRealColor;
 private ROI roi;

 @Before
 public void setUp() {
 image = new GrayImage(5, 5, null);
 imageReal = new RealGrayImage(image);
 imageColor = new ColorImage(5, 5, null, null, null);
 imageRealColor = new RealColorImage(imageColor);
 roi = new ROI(1, 1, 3, 3);
 clip = new Clip(0, 100);
 }

 @After
 public void tearDown() {
 image = null;
 imageReal = null;
 imageColor = null;
 imageRealColor = null;
 roi = null;
 clip = null;
 }

 @Test
 public void testConstructorWithIntMinIntMax() {
 clip = new Clip(0, 100);
 assertEquals("Expected constructor with int min and int max", 0, clip.int_min);
 assertEquals("Expected constructor with int min and int max", 100, clip.int_max);
 }

 @Test
 public void testConstructorWithFloatMinFloatMax() {
 clip = new Clip(0.5f, 1.5f);
 assertEquals("Expected constructor with float min and float max", 0, clip.int_min);
 assertEquals("Expected constructor with float min and float max", (int)1.5f, clip.int_max);
 }

 @Test
 public void testApplyWithGrayImageAndRoi() {
 clip = new Clip(0, 100);
 image.set(1, 1, (byte) 150);
 image.set(2, 2, (byte) 200);
 image.set(3, 3, (byte) 50);
 image.set(4, 4, (byte) 255);
 image.set(5, 5, (byte) 100);

 clip.apply(image, roi);

 assertEquals("Expected value 150 after clipping, but was", 100, image.get(1, 1).intValue());
 assertEquals("Expected value 200 after clipping, but was", 100, image.get(2, 2).intValue());
 assertEquals("Expected value 50 after clipping, but was", 50, image.get(3, 3).intValue());
 assertEquals("Expected value 255 after clipping, but was", 100, image.get(4, 4).intValue());
 assertEquals("Expected value 100 after clipping, but was", 100, image.get(5, 5).intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithGrayImageAndNullRoi() {
 clip = new Clip(0, 100);
 clip.apply(image, null);
 }

 @Test
 public void testApplyWithRealGrayImageAndRoi() {
 clip = new Clip(0.5f, 1.5f);
 imageReal.set(1, 1, 150.0f);
 imageReal.set(2, 2, 200.0f);
 imageReal.set(3, 3, 50.0f);
 imageReal.set(4, 4, 255.0f);
 imageReal.set(5, 5, 100.0f);

 clip.apply(imageReal, roi);

 assertEquals("Expected value 150.0 after clipping, but was", 100.0f, imageReal.get(1, 1), 0.01f);
 assertEquals("Expected value 200.0 after clipping, but was", 100.0f, imageReal.get(2, 2), 0.01f);
 assertEquals("Expected value 50.0 after clipping, but was", 50.0f, imageReal.get(3, 3), 0.01f);
 assertEquals("Expected value 255.0 after clipping, but was", 100.0f, imageReal.get(4, 4), 0.01f);
 assertEquals("Expected value 100.0 after clipping, but was", 100.0f, imageReal.get(5, 5), 0.01f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithRealGrayImageAndNullRoi() {
 clip = new Clip(0.5f, 1.5f);
 clip.apply(imageReal, null);
 }

 @Test
 public void testApplyWithColorImageAndRoi() {
 clip = new Clip(new float[]{0.5f, 0.5f, 0.5f}, new float[]{1.5f, 1.5f, 1.5f});
 imageColor.setPlane(0, image.clone());
 imageColor.setPlane(1, image.clone());
 imageColor.setPlane(2, image.clone());
 imageColor.setPixel(1, 1, 150, 150, 150);
 imageColor.setPixel(2, 2, 200, 200, 200);
 imageColor.setPixel(3, 3, 50, 50, 50);
 imageColor.setPixel(4, 4, 255, 255, 255);
 imageColor.setPixel(5, 5, 100, 100, 100);

 clip.apply(imageColor, roi);

 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(0).get(1, 1, 0).intValue());
 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(0).get(1, 1, 1).intValue());
 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(0).get(1, 1, 2).intValue());

 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(1).get(2, 2, 0).intValue());
 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(1).get(2, 2, 1).intValue());
 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(1).get(2, 2, 2).intValue());

 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(2).get(3, 3, 0).intValue());
 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(2).get(3, 3, 1).intValue());
 assertEquals("Expected RGB value (100, 100, 100) after clipping, but was", 100, imageColor.getPlane(2).get(3, 3, 2).intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithColorImageAndNullRoi() {
 clip = new Clip(new float[]{0.5f, 0.5f, 0.5f}, new float[]{1.5f, 1.5f, 1.5f});
 clip.apply(imageColor, null);
 }

 @Test
 public void testApplyWithRealColorImageAndRoi() {
 clip = new Clip(new float[]{0.5f, 0.5f, 0.5f}, new float[]{1.5f, 1.5f, 1.5f});
 imageRealColor.setPlane(0, imageReal.clone());
 imageRealColor.setPlane(1, imageReal.clone());
 imageRealColor.setPlane(2, imageReal.clone());
 imageRealColor.setPixel(1, 1, 150.0f, 150.0f, 150.0f);
 imageRealColor.setPixel(2, 2, 200.0f, 200.0f, 200.0f);
 imageRealColor.setPixel(3, 3, 50.0f, 50.0f, 50.0f);
 imageRealColor.setPixel(4, 4, 255.0f, 255.0f, 255.0f);
 imageRealColor.setPixel(5, 5, 100.0f, 100.0f, 100.0f);

 clip.apply(imageRealColor, roi);

 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(0).get(1, 1, 0), 0.01f);
 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(0).get(1, 1, 1), 0.01f);
 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(0).get(1, 1, 2), 0.01f);

 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(1).get(2, 2, 0), 0.01f);
 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(1).get(2, 2, 1), 0.01f);
 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(1).get(2, 2, 2), 0.01f);

 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(2).get(3, 3, 0), 0.01f);
 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(2).get(3, 3, 1), 0.01f);
 assertEquals("Expected RGB value (100.0, 100.0, 100.0) after clipping, but was", 100.0f, imageRealColor.getPlane(2).get(3, 3, 2), 0.01f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithRealColorImageAndNullRoi() {
 clip = new Clip(new float[]{0.5f, 0.5f, 0.5f}, new float[]{1.5f, 1.5f, 1.5f});
 clip.apply(imageRealColor, null);
 }
}