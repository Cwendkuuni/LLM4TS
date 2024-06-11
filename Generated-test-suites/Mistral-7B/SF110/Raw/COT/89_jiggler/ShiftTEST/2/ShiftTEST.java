import junit.framework.*;
import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import org.junit.Assert;

public class ShiftTest {

 private Shift shift;
 private GrayImage inputImage;
 private RealGrayImage realInputImage;
 private GrayImage expectedOutput;
 private RealGrayImage realExpectedOutput;

 @Before
 public void setUp() {
 GrayImage image = new GrayImage(5, 5);
 image.set(0, 0, (byte) 50);
 image.set(0, 1, (byte) 100);
 image.set(0, 2, (byte) 150);
 image.set(0, 3, (byte) 200);
 image.set(0, 4, (byte) 250);
 image.set(1, 0, (byte) 250);
 image.set(1, 1, (byte) 200);
 image.set(1, 2, (byte) 150);
 image.set(1, 3, (byte) 100);
 image.set(1, 4, (byte) 50);
 image.set(2, 0, (byte) 50);
 image.set(2, 1, (byte) 100);
 image.set(2, 2, (byte) 150);
 image.set(2, 3, (byte) 200);
 image.set(2, 4, (byte) 250);
 image.set(3, 0, (byte) 250);
 image.set(3, 1, (byte) 200);
 image.set(3, 2, (byte) 150);
 image.set(3, 3, (byte) 100);
 image.set(3, 4, (byte) 50);
 image.set(4, 0, (byte) 50);
 image.set(4, 1, (byte) 100);
 image.set(4, 2, (byte) 150);
 image.set(4, 3, (byte) 200);
 image.set(4, 4, (byte) 250);
 inputImage = image;
 realInputImage = new RealGrayImage(inputImage);

 shift = new Shift(1, 1, Shift.WRAP);
 }

 @Test
 public void testApplyGrayImageWithPositiveOffsetAndWrap() {
 expectedOutput = new GrayImage(5, 5);
 expectedOutput.set(0, 0, (byte) 101);
 expectedOutput.set(0, 1, (byte) 151);
 expectedOutput.set(0, 2, (byte) 201);
 expectedOutput.set(0, 3, (byte) 251);
 expectedOutput.set(0, 4, (byte) 301);
 expectedOutput.set(1, 0, (byte) 250);
 expectedOutput.set(1, 1, (byte) 200);
 expectedOutput.set(1, 2, (byte) 150);
 expectedOutput.set(1, 3, (byte) 100);
 expectedOutput.set(1, 4, (byte) 50);
 expectedOutput.set(2, 0, (byte) 51);
 expectedOutput.set(2, 1, (byte) 101);
 expectedOutput.set(2, 2, (byte) 151);
 expectedOutput.set(2, 3, (byte) 201);
 expectedOutput.set(2, 4, (byte) 251);
 expectedOutput.set(3, 0, (byte) 100);
 expectedOutput.set(3, 1, (byte) 50);
 expectedOutput.set(3, 2, (byte) 0);
 expectedOutput.set(3, 3, (byte) -50);
 expectedOutput.set(3, 4, (byte) -100);
 expectedOutput.set(4, 0, (byte) -100);
 expectedOutput.set(4, 1, (byte) -50);
 expectedOutput.set(4, 2, (byte) 0);
 expectedOutput.set(4, 3, (byte) 50);
 expectedOutput.set(4, 4, (byte) 100);
 GrayImage output = shift.apply(inputImage);
 Assert.assertArrayEquals(expectedOutput.getPixels(), output.getPixels());
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testApplyGrayImageWithNegativeOffsetAndDifferentImageSize() {
 shift = new Shift(-1, 1, Shift.WRAP);
 shift.apply(inputImage);
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testApplyGrayImageWithNegativeOffsetAndSameImageSize() {
 shift = new Shift(-1, 1, Shift.WRAP);
 shift.apply(new GrayImage(5, 5));
 }

 @Test
 public void testApplyGrayImageWithNegativeOffsetAndWrap() {
 expectedOutput = new GrayImage(5, 5);
 expectedOutput.set(0, 4, (byte) 50);
 expectedOutput.set(0, 3, (byte) 100);
 expectedOutput.set(0, 2, (byte) 150);
 expectedOutput.set(0, 1, (byte) 200);
 expectedOutput.set(0, 0, (byte) 250);
 expectedOutput.set(1, 4, (byte) 150);
 expectedOutput.set(1, 3, (byte) 200);
 expectedOutput.set(1, 2, (byte) 250);
 expectedOutput.set(1, 1, (byte) 300);
 expectedOutput.set(1, 0, (byte) 350);
 expectedOutput.set(2, 4, (byte) 250);
 expectedOutput.set(2, 3, (byte) 300);
 expectedOutput.set(2, 2, (byte) 350);
 expectedOutput.set(2, 1, (byte) 400);
 expectedOutput.set(2, 0, (byte) 450);
 expectedOutput.set(3, 4, (byte) 350);
 expectedOutput.set(3, 3, (byte) 400);
 expectedOutput.set(3, 2, (byte) 450);
 expectedOutput.set(3, 1, (byte) 500);
 expectedOutput.set(3, 0, (byte) 550);
 expectedOutput.set(4, 4, (byte) 450);
 expectedOutput.set(4, 3, (byte) 500);
 expectedOutput.set(4, 2, (byte) 550);
 expectedOutput.set(4, 1, (byte) 600);
 expectedOutput.set(4, 0, (byte) 650);
 expectedOutput.set(4, 4, (byte) 650);
 expectedOutput.set(4, 3, (byte) 600);
 expectedOutput.set(4, 2, (byte) 550);
 expectedOutput.set(4, 1, (byte) 500);
 expectedOutput.set(4, 0, (byte) 450);
 GrayImage output = shift.apply(inputImage);
 Assert.assertArrayEquals(expectedOutput.getPixels(), output.getPixels());
 }

 @Test
 public void testApplyRealGrayImageWithPositiveOffsetAndWrap() {
 realExpectedOutput = new RealGrayImage(5, 5);
 realExpectedOutput.set(0, 0, 101.0);
 realExpectedOutput.set(0, 1, 151.0);
 realExpectedOutput.set(0, 2, 201.0);
 realExpectedOutput.set(0, 3, 251.0);
 realExpectedOutput.set(0, 4, 301.0);
 realExpectedOutput.set(1, 0, 250.0);
 realExpectedOutput.set(1, 1, 200.0);
 realExpectedOutput.set(1, 2, 150.0);
 realExpectedOutput.set(1, 3, 100.0);
 realExpectedOutput.set(1, 4, 50.0);
 realExpectedOutput.set(2, 0, 51.0);
 realExpectedOutput.set(2, 1, 101.0);
 realExpectedOutput.set(2, 2, 151.0);
 realExpectedOutput.set(2, 3, 201.0);
 realExpectedOutput.set(2, 4, 251.0);
 realExpectedOutput.set(3, 0, 100.0);
 realExpectedOutput.set(3, 1, 50.0);
 realExpectedOutput.set(3, 2, 0.0);
 realExpectedOutput.set(3, 3, -50.0);
 realExpectedOutput.set(3, 4, -100.0);
 realExpectedOutput.set(4, 0, -100.0);
 realExpectedOutput.set(4, 1, -50.0);
 realExpectedOutput.set(4, 2, 0.0);
 realExpectedOutput.set(4, 3, 50.0);
 realExpectedOutput.set(4, 4, 100.0);
 RealGrayImage output = shift.apply(realInputImage);
 Assert.assertArrayEquals(realExpectedOutput.getPixels(), output.getPixels(), 0.001f);
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testApplyRealGrayImageWithNegativeOffsetAndDifferentImageSize() {
 shift = new Shift(-1, 1, Shift.WRAP);
 shift.apply(realInputImage);
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testApplyRealGrayImageWithNegativeOffsetAndSameImageSize() {
 shift = new Shift(-1, 1, Shift.WRAP);
 shift.apply(new RealGrayImage(5, 5));
 }

 @Test
 public void testApplyRealGrayImageWithNegativeOffsetAndWrap() {
 realExpectedOutput = new RealGrayImage(5, 5);
 realExpectedOutput.set(0, 4, 50.0);
 realExpectedOutput.set(0, 3, 100.0);
 realExpectedOutput.set(0, 2, 150.0);
 realExpectedOutput.set(0, 1, 200.0);
 realExpectedOutput.set(0, 0, 250.0);
 realExpectedOutput.set(1, 4, 150.0);
 realExpectedOutput.set(1, 3, 200.0);
 realExpectedOutput.set(1, 2, 250.0);
 realExpectedOutput.set(1, 1, 300.0);
 realExpectedOutput.set(1, 0, 350.0);
 realExpectedOutput.set(2, 4, 250.0);
 realExpectedOutput.set(2, 3, 300.0);
 realExpectedOutput.set(2, 2, 350.0);
 realExpectedOutput.set(2, 1, 400.0);
 realExpectedOutput.set(2, 0, 450.0);
 realExpectedOutput.set(3, 4, 350.0);
 realExpectedOutput.set(3, 3, 400.0);
 realExpectedOutput.set(3, 2, 450.0);
 realExpectedOutput.set(3, 1, 500.0);
 realExpectedOutput.set(3, 0, 550.0);
 realExpectedOutput.set(4, 4, 450.0);
 realExpectedOutput.set(4, 3, 500.0);
 realExpectedOutput.set(4, 2, 550.0);
 realExpectedOutput.set(4, 1, 600.0);
 realExpectedOutput.set(4, 0, 650.0);
 realExpectedOutput.set(4, 4, 650.0);
 realExpectedOutput.set(4, 3, 600.0);
 realExpectedOutput.set(4, 2, 550.0);
 realExpectedOutput.set(4, 1, 500.0);
 realExpectedOutput.set(4, 0, 450.0);
 RealGrayImage output = shift.apply(realInputImage);
 Assert.assertArrayEquals(realExpectedOutput.getPixels(), output.getPixels(), 0.001f);
 }

 @Test
 public void testApplyColorImage() {
 ColorImage input = new ColorImage(5, 5);
 for (int i = 0; i < 5; i++) {
 for (int j = 0; j < 5; j++) {
 int r = i * 10 + j;
 int g = i + j;
 int b = j * 10 + i;
 input.set(i, j, new RGBColor(r, g, b));
 }
 }
 RealColorImage expectedOutput = new RealColorImage(5, 5);
 for (int i = 0; i < 5; i++) {
 for (int j = 0; j < 5; j++) {
 int r = i * 10 + j + 1;
 int g = i + j + 1;
 int b = j * 10 + i + 1;
 expectedOutput.set(i, j, new RGBColor(r, g, b));
 }
 }
 ColorImage output = shift.apply(input);
 for (int i = 0; i < 5; i++) {
 for (int j = 0; j < 5; j++) {
 RGBColor color1 = output.get(i, j);
 RGBColor color2 = expectedOutput.get(i, j);
 Assert.assertEquals(color1.getRed(), color2.getRed());
 Assert.assertEquals(color1.getGreen(), color2.getGreen());
 Assert.assertEquals(color1.getBlue(), color2.getBlue());
 }
 }
 }

 @Test
 public void testApplyRealColorImage() {
 RealColorImage input = new RealColorImage(5, 5);
 for (int i = 0; i < 5; i++) {
 for (int j = 0; j < 5; j++) {
 float r = i * 10.0f + j;
 float g = i + j;
 float b = j * 10.0f + i;
 input.set(i, j, new RGBColor(r, g, b));
 }
 }
 RealColorImage expectedOutput = new RealColorImage(5, 5);
 for (int i = 0; i < 5; i++) {
 for (int j = 0; j < 5; j++) {
 float r = i * 10.0f + j + 1;
 float g = i + j + 1;
 float b = j * 10.0f + i + 1;
 expectedOutput.set(i, j, new RGBColor(r, g, b));
 }
 }
 RealColorImage output = shift.apply(expectedOutput);
 for (int i = 0; i < 5; i++) {
 for (int j = 0; j < 5; j++) {
 RGBColor color1 = output.get(i, j);
 RGBColor color2 = expectedOutput.get(i, j);
 Assert.assertEquals(color1.getRed(), color2.getRed(), 0.001f);
 Assert.assertEquals(color1.getGreen(), color2.getGreen(), 0.001f);
 Assert.assertEquals(color1.getBlue(), color2.getBlue(), 0.001f);
 }
 }
 }
}