package jigl.image.ops.levelOps;

import static org.junit.Assert.*;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

 @org.junit.BeforeClass
 static void setUpBeforeClass() throws Exception {
 // Initialize test data or setup shared resources here
 }

 @org.junit.AfterClass
 static void tearDownAfterClass() throws Exception {
 // Release shared resources or cleanup test data here
 }

 @org.junit.Before
 static void setUp() {
 // Set up test environment or initialize test objects here
 }

 @org.junit.After
 static void tearDown() {
 // Clean up test environment or release test objects here
 }

 @org.junit.Test
 public void testClipConstructors() {
 Clip clip1 = new Clip(1, 10);
 Clip clip2 = new Clip(0.1f, 0.5f);

 assertEquals("int constructor: min should be less than max", 1, clip1.int_min);
 assertEquals("int constructor: max should be greater than min", 10, clip1.int_max);
 assertEquals("float constructor: min should be less than max", 0.1f, clip2.float_min, 0.01f);
 assertEquals("float constructor: max should be greater than min", 0.5f, clip2.float_max, 0.01f);
 }

 @org.junit.Test
 public void testApplyGrayImageWithIntBounds() {
 GrayImage image = new ByteGrayImage(3, 3);
 image.set(0, 0, (byte)10);
 image.set(0, 1, (byte)5);
 image.set(0, 2, (byte)15);
 image.set(1, 0, (byte)8);
 image.set(1, 1, (byte)3);
 image.set(1, 2, (byte)12);
 image.set(2, 0, (byte)14);
 image.set(2, 1, (byte)9);
 image.set(2, 2, (byte)13);

 Clip clip = new Clip(5, 12);
 GrayImage result = (GrayImage)clip.apply(image);

 byte[] expected = {5, 3, 12, 8, 3, 12, 9, 3, 13};
 byte[] actual = new byte[result.X() * result.Y()];
 result.getPixels(0, 0, result.X(), result.Y(), actual);

 assertArrayEquals("apply method with integer bounds should return clipped image", expected, actual);
 }

 @org.junit.Test
 public void testApplyGrayImageWithFloatBounds() {
 GrayImage image = new ByteGrayImage(3, 3);
 image.set(0, 0, (byte)10);
 image.set(0, 1, (byte)5);
 image.set(0, 2, (byte)15);
 image.set(1, 0, (byte)8);
 image.set(1, 1, (byte)3);
 image.set(1, 2, (byte)12);
 image.set(2, 0, (byte)14);
 image.set(2, 1, (byte)9);
 image.set(2, 2, (byte)13);

 Clip clip = new Clip(0.5f, 11.5f);
 GrayImage result = (GrayImage)clip.apply(image);

 byte[] expected = {8, 3, 12, 8, 3, 12, 9, 3, 13};
 byte[] actual = new byte[result.X() * result.Y()];
 result.getPixels(0, 0, result.X(), result.Y(), actual);

 assertArrayEquals("apply method with float bounds should return clipped image", expected, actual);
 }

 @org.junit.Test
 public void testApplyRealGrayImageWithIntBounds() {
 RealGrayImage image = new FloatGrayImage(3, 3);
 image.set(0, 0, 10.0f);
 image.set(0, 1, 5.0f);
 image.set(0, 2, 15.0f);
 image.set(1, 0, 8.0f);
 image.set(1, 1, 3.0f);
 image.set(1, 2, 12.0f);
 image.set(2, 0, 14.0f);
 image.set(2, 1, 9.0f);
 image.set(2, 2, 13.0f);

 Clip clip = new Clip(5.0f, 12.0f);
 RealGrayImage result = (RealGrayImage)clip.apply(image);

 float[] expected = {5.0f, 3.0f, 12.0f, 8.0f, 3.0f, 12.0f, 9.0f, 3.0f, 13.0f};
 float[] actual = new float[result.X() * result.Y()];
 result.getPixels(0, 0, result.X(), result.Y(), actual);

 assertArrayEquals("apply method with integer bounds and RealGrayImage should return clipped image", expected, actual, 0.01f);
 }

 @org.junit.Test
 public void testApplyRealGrayImageWithFloatBounds() {
 RealGrayImage image = new FloatGrayImage(3, 3);
 image.set(0, 0, 10.0f);
 image.set(0, 1, 5.0f);
 image.set(0, 2, 15.0f);
 image.set(1, 0, 8.0f);
 image.set(1, 1, 3.0f);
 image.set(1, 2, 12.0f);
 image.set(2, 0, 14.0f);
 image.set(2, 1, 9.0f);
 image.set(2, 2, 13.0f);

 Clip clip = new Clip(0.5f, 11.5f);
 RealGrayImage result = (RealGrayImage)clip.apply(image);

 float[] expected = {8.0f, 3.0f, 12.0f, 8.0f, 3.0f, 12.0f, 9.0f, 3.0f, 13.0f};
 float[] actual = new float[result.X() * result.Y()];
 result.getPixels(0, 0, result.X(), result.Y(), actual);

 assertArrayEquals("apply method with float bounds and RealGrayImage should return clipped image", expected, actual, 0.01f);
 }

 @org.junit.Test
 public void testApplyColorImageWithIntBounds() {
 ColorImage image = new RGBImage(3, 3, new byte[]{(byte)10, (byte)5, (byte)15}, new byte[]{(byte)8, (byte)3, (byte)12}, new byte[]{(byte)14, (byte)9, (byte)13});

 Clip clip = new Clip(5, 12);
 ColorImage result = (ColorImage)clip.apply(image);

 byte[][] expected = {{5, 3, 12}, {8, 3, 12}, {9, 3, 13}};
 byte[][] actual = new byte[result.X()][result.Y()];
 for (int i = 0; i < result.X(); i++) {
 for (int j = 0; j < result.Y(); j++) {
 actual[i][j] = result.getPixel(i, j).getRed();
 }
 }

 assertArrayEquals("apply method with integer bounds and ColorImage should return clipped image", expected, actual);
 }

 @org.junit.Test
 public void testApplyColorImageWithFloatBounds() {
 ColorImage image = new RGBImage(3, 3, new byte[]{(byte)10, (byte)5, (byte)15}, new byte[]{(byte)8, (byte)3, (byte)12}, new byte[]{(byte)14, (byte)9, (byte)13});

 Clip clip = new Clip(0.5f, 11.5f);
 ColorImage result = (ColorImage)clip.apply(image);

 byte[][] expected = {{8, 3, 12}, {8, 3, 12}, {9, 3, 13}};
 byte[][] actual = new byte[result.X()][result.Y()];
 for (int i = 0; i < result.X(); i++) {
 for (int j = 0; j < result.Y(); j++) {
 actual[i][j] = result.getPixel(i, j).getRed();
 }
 }

 assertArrayEquals("apply method with float bounds and ColorImage should return clipped image", expected, actual);
 }

 @org.junit.Test
 public void testMainMethod() {
 // Prepare input data
 GrayImage image = new ByteGrayImage(3, 3);
 image.set(0, 0, (byte)10);
 image.set(0, 1, (byte)5);
 image.set(0, 2, (byte)15);
 image.set(1, 0, (byte)8);
 image.set(1, 1, (byte)3);
 image.set(1, 2, (byte)12);
 image.set(2, 0, (byte)14);
 image.set(2, 1, (byte)9);
 image.set(2, 2, (byte)13);

 String inputFile = "input.bmp";
 ImageOutputStream os = new FileImageOutputStream(new File(inputFile));
 os.write(image);
 os.close();

 String outputFile = "output.bmp";

 String[] args = {"0.5", "11.5", inputFile, outputFile};

 // Call main method under test
 Clip.main(args);

 // Check output file
 ImageInputStream is = new FileImageInputStream(new File(outputFile));
 GrayImage expectedOutput = (GrayImage)is.read();
 is.close();

 byte[] expected = new byte[expectedOutput.X() * expectedOutput.Y()];
 expectedOutput.getPixels(0, 0, expectedOutput.X(), expectedOutput.Y(), expected);

 byte[] actual = new byte[image.X() * image.Y()];
 image.getPixels(0, 0, image.X(), image.Y(), actual);

 assertArrayEquals("main method should clip the input image according to the specified bounds and save the result to the output file", expected, actual);
 }
}