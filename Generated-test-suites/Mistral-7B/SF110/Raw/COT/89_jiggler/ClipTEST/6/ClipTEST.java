import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.ROI;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

 private Clip clip;
 private GrayImage image;
 private RealGrayImage imageReal;
 private ColorImage colorImage;
 private RealColorImage realColorImage;

 @Before
 public void setUp() {
 image = new GrayImage(5, 5, 0.0f);
 imageReal = new RealGrayImage(5, 5, new float[25]);
 colorImage = new ColorImage(5, 5, 3, new int[15 * 5], new float[15 * 5 * 3]);
 realColorImage = new RealColorImage(5, 5, 3, new float[15 * 5 * 3]);
 }

 @After
 public void tearDown() {
 clip = null;
 image = null;
 imageReal = null;
 colorImage = null;
 realColorImage = null;
 }

 @Test
 public void testClipConstructorIntInt() {
 clip = new Clip(1, 2);
 assertNotNull(clip);
 }

 @Test
 public void testClipConstructorFloatFloat() {
 clip = new Clip(1.5f, 2.5f);
 assertNotNull(clip);
 }

 @Test
 public void testApplyGrayImage() {
 clip = new Clip(0, 100);
 assertNotNull(clip.apply(image));
 }

 @Test(expected = NullPointerException.class)
 public void testApplyGrayImageNullImage() {
 clip = new Clip(0, 100);
 clip.apply(null);
 }

 @Test
 public void testApplyGrayImageROI() {
 clip = new Clip(0, 100);
 ROI roi = new ROI(0, 0, 3, 3);
 GrayImage expected = new GrayImage(3, 3, 0.0f);
 assertArrayEquals(expected.getPixels(), clip.apply(image, roi).getPixels());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNegativeValues() {
 clip = new Clip(-1, 1);
 }

 @Test
 public void testApplyRealGrayImage() {
 clip = new Clip(0.5f, 1.5f);
 assertNotNull(clip.apply(imageReal));
 }

 @Test
 public void testApplyRealGrayImageROI() {
 clip = new Clip(0.5f, 1.5f);
 ROI roi = new ROI(0, 0, 3, 3);
 RealGrayImage expected = new RealGrayImage(3, 3, new float[9]);
 assertArrayEquals(expected.getPixels(), clip.apply(imageReal, roi).getPixels(), 0.01f);
 }

 @Test
 public void testApplyColorImage() {
 clip = new Clip(50, 150);
 assertNotNull(clip.apply(colorImage));
 }

 @Test
 public void testApplyColorImageROI() {
 clip = new Clip(50, 150);
 ROI roi = new ROI(0, 0, 3, 3);
 ColorImage expected = new ColorImage(3, 3, 3, new int[9 * 3], new float[9 * 3 * 3]);
 assertArrayEquals(expected.getPixel(0, 0), clip.apply(colorImage, roi).getPixel(0, 0), 0.001f);
 }

 @Test
 public void testApplyRealColorImage() {
 clip = new Clip(50.5f, 150.5f);
 assertNotNull(clip.apply(realColorImage));
 }

 @Test
 public void testApplyRealColorImageROI() {
 clip = new Clip(50.5f, 150.5f);
 ROI roi = new ROI(0, 0, 3, 3);
 RealColorImage expected = new RealColorImage(3, 3, 3, new float[9 * 3 * 3]);
 assertArrayEquals(expected.getPixels(), clip.apply(realColorImage, roi).getPixels(), 0.01f);
 }

 @Test
 public void testMain() {
 String inputFile = "test.jpg";
 String outputFile = "output.jpg";
 String arg0 = "1.0";
 String arg1 = "2.0";
 String arg2 = inputFile;
 String arg3 = outputFile;
 try {
 Image image = new ImageInputStream(arg2).read();
 Clip clip = new Clip(Float.parseFloat(arg0), Float.parseFloat(arg1));
 Image image2 = clip.apply(image);
 new ImageOutputStream(arg3).write(image2);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
}