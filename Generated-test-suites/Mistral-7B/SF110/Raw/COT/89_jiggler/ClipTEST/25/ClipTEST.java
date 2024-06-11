import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.types.GrayImage;
import jigl.image.ops.types.ROI;
import jigl.image.ops.types.RealGrayImage;
import jigl.image.types.Image;
import jigl.image.types.Pixel;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

 private Image image;
 private GrayImage grayImage;
 private RealGrayImage realGrayImage;
 private ROI roi;
 private Clip clip;

 @Before
 public void setUp() {
 // Initialize test data
 image = new Image(new int[1][1], new int[]{}, new int[]{}, 1, 1);
 grayImage = new GrayImage(image.width(), image.height(), new Pixel[image.width() * image.height()]);
 realGrayImage = new RealGrayImage(image.width(), image.height(), new float[image.width() * image.height()]);
 roi = new ROI(0, 0, image.width() - 1, image.height() - 1);
 clip = new Clip(0, 100);
 }

 @After
 public void tearDown() {
 // Release test data
 image = null;
 grayImage = null;
 realGrayImage = null;
 roi = null;
 clip = null;
 }

 @Test
 public void testClipConstructorWithIntMinAndIntMax() {
 Clip clip = new Clip(0, 100);
 assertTrue(clip instanceof Clip);
 assertSame(clip.getClass(), Clip.class);
 }

 @Test
 public void testClipConstructorWithFloatMinAndFloatMax() {
 Clip clip = new Clip(0.0f, 100.0f);
 assertTrue(clip instanceof Clip);
 assertSame(clip.getClass(), Clip.class);
 }

 @Test
 public void testApplyGrayImageWithIntMinAndIntMax() {
 clip = new Clip(0, 100);
 grayImage.set(0, 0, (Integer) 50);
 grayImage.set(0, 1, (Integer) 150);
 Image expected = new Image(new int[1][1], new int[]{}, new int[]{}, 1, 1);
 expected.set(0, 0, Integer.valueOf(100));
 clip.apply(grayImage, roi);
 assertArrayEquals(expected.getPixels(), grayImage.getPixels());
 }

 @Test
 public void testApplyGrayImageWithFloatMinAndFloatMax() {
 clip = new Clip(0.0f, 100.0f);
 grayImage.set(0, 0, (Integer) 50);
 grayImage.set(0, 1, (Integer) 150);
 Image expected = new Image(new int[1][1], new int[]{}, new int[]{}, 1, 1);
 expected.set(0, 0, Integer.valueOf(100.0f));
 clip.apply(grayImage, roi);
 assertArrayEquals(expected.getPixels(), grayImage.getPixels());
 }

 @Test
 public void testApplyRealGrayImageWithIntMinAndIntMax() {
 clip = new Clip(0, 100);
 realGrayImage.set(0, 0, 50.0f);
 realGrayImage.set(0, 1, 150.0f);
 Image expected = new Image(new float[1][1], new int[]{}, new int[]{}, 1, 1);
 expected.set(0, 0, Float.valueOf(100.0f));
 clip.apply(realGrayImage, roi);
 assertArrayEquals(expected.getPixels(), realGrayImage.getPixels(), 0.001f);
 }

 @Test
 public void testApplyRealGrayImageWithFloatMinAndFloatMax() {
 clip = new Clip(0.0f, 100.0f);
 realGrayImage.set(0, 0, 50.0f);
 realGrayImage.set(0, 1, 150.0f);
 Image expected = new Image(new float[1][1], new int[]{}, new int[]{}, 1, 1);
 expected.set(0, 0, Float.valueOf(100.0f));
 clip.apply(realGrayImage, roi);
 assertArrayEquals(expected.getPixels(), realGrayImage.getPixels(), 0.001f);
 }

 @Test
 public void testApplyColorImageWithIntMinAndIntMax() {
 clip = new Clip(0, 100);
 ColorImage colorImage = new ColorImage(image.width(), image.height(), new Pixel[image.width() * image.height() * 3]);
 Image expected = new Image(new int[1][1], new int[]{}, new int[]{}, 1, 1);
 expected.setPlane(0, grayImage);
 expected.setPlane(1, grayImage);
 expected.setPlane(2, grayImage);
 clip.apply(colorImage, roi);
 assertSame(expected, colorImage);
 }

 @Test
 public void testApplyColorImageWithFloatMinAndFloatMax() {
 clip = new Clip(0.0f, 100.0f);
 ColorImage colorImage = new ColorImage(image.width(), image.height(), new Pixel[image.width() * image.height() * 3]);
 Image expected = new Image(new int[1][1], new int[]{}, new int[]{}, 1, 1);
 expected.setPlane(0, realGrayImage);
 expected.setPlane(1, realGrayImage);
 expected.setPlane(2, realGrayImage);
 clip.apply(colorImage, roi);
 assertSame(expected, colorImage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyNullImage() {
 clip.apply(null, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyNullROI() {
 clip.apply(grayImage, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithNegativeROIWidthOrHeight() {
 clip.apply(grayImage, new ROI(-1, 0, image.width() - 1, image.height() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithNegativeROIXOrY() {
 clip.apply(grayImage, new ROI(0, -1, image.width() - 1, image.height() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithROIWidthOrHeightGreaterThanImageWidthOrHeight() {
 clip.apply(grayImage, new ROI(0, 0, image.width() + 1, image.height() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithROIXOrYOutOfImageBounds() {
 clip.apply(grayImage, new ROI(-1, 1, image.width() - 1, image.height() - 1));
 }
}