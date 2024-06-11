package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.*;

public class ShiftTest {

 private Shift shift;
 private GrayImage inputImage;
 private RealGrayImage realInputImage;
 private ColorImage inputColorImage;
 private RealColorImage realInputColorImage;

 @Before
 public void setUp() {
 GrayPixel grayPixel = new GrayPixel(128);
 inputImage = new GrayImage(3, 3);
 inputImage.set(0, 0, grayPixel);
 inputImage.set(0, 1, grayPixel);
 inputImage.set(0, 2, grayPixel);
 inputImage.set(1, 0, grayPixel);
 inputImage.set(1, 1, grayPixel);
 inputImage.set(1, 2, grayPixel);
 inputImage.set(2, 0, grayPixel);
 inputImage.set(2, 1, grayPixel);
 inputImage.set(2, 2, grayPixel);

 realInputImage = new RealGrayImage(3, 3);
 realInputImage.set(0, 0, 128.0);
 realInputImage.set(0, 1, 128.0);
 realInputImage.set(0, 2, 128.0);
 realInputImage.set(1, 0, 128.0);
 realInputImage.set(1, 1, 128.0);
 realInputImage.set(1, 2, 128.0);
 realInputImage.set(2, 0, 128.0);
 realInputImage.set(2, 1, 128.0);
 realInputImage.set(2, 2, 128.0);

 inputColorImage = new ColorImage(3, 3);
 ColorPixel colorPixel = new ColorPixel(128, 128, 128);
 inputColorImage.set(0, 0, colorPixel);
 inputColorImage.set(0, 1, colorPixel);
 inputColorImage.set(0, 2, colorPixel);
 inputColorImage.set(1, 0, colorPixel);
 inputColorImage.set(1, 1, colorPixel);
 inputColorImage.set(1, 2, colorPixel);
 inputColorImage.set(2, 0, colorPixel);
 inputColorImage.set(2, 1, colorPixel);
 inputColorImage.set(2, 2, colorPixel);

 realInputColorImage = new RealColorImage(3, 3);
 realInputColorImage.setPlane(0, new RealGrayImage(3, 3).set(0, 0, 128.0).set(0, 1, 128.0).set(0, 2, 128.0)
         .set(1, 0, 128.0).set(1, 1, 128.0).set(1, 2, 128.0).set(2, 0, 128.0).set(2, 1, 128.0).set(2, 2, 128.0));
 realInputColorImage.setPlane(1, new RealGrayImage(3, 3).set(0, 0, 128.0).set(0, 1, 128.0).set(0, 2, 128.0)
         .set(1, 0, 128.0).set(1, 1, 128.0).set(1, 2, 128.0).set(2, 0, 128.0).set(2, 1, 128.0).set(2, 2, 128.0));
 realInputColorImage.setPlane(2, new RealGrayImage(3, 3).set(0, 0, 128.0).set(0, 1, 128.0).set(0, 2, 128.0)
         .set(1, 0, 128.0).set(1, 1, 128.0).set(1, 2, 128.0).set(2, 0, 128.0).set(2, 1, 128.0).set(2, 2, 128.0));

 shift = new Shift(1, 1, Shift.WRAP);
 }

 @Test
 public void testApplyGrayImageWithWrapXAndY() {
 GrayImage outputImage = shift.apply(inputImage);
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 int expectedX = (x + shift.xoffset) % outputImage.X();
 int expectedY = (y + shift.yoffset) % outputImage.Y();
 GrayPixel expectedPixel = inputImage.get(expectedX, expectedY);
 assertEquals(expectedPixel.getGrayValue(), outputImage.get(x, y).getGrayValue());
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeXAndY() {
 shift = new Shift(-1, -1, Shift.WRAP);
 try {
 shift.apply(inputImage);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeX() {
 shift = new Shift(1, 1, Shift.NOWRAP);
 try {
 shift.apply(inputImage).get(-1, 0);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageWithNegativeY() {
 shift = new Shift(1, 1, Shift.NOWRAP);
 try {
 shift.apply(inputImage).get(0, -1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test
 public void testApplyRealGrayImageWithWrapXAndY() {
 RealGrayImage outputImage = (RealGrayImage) shift.apply(realInputImage);
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 float expectedX = ((float) (x + shift.xoffset)) % ((float) outputImage.X());
 float expectedY = ((float) (y + shift.yoffset)) % ((float) outputImage.Y());
 assertEquals(realInputImage.get(expectedX, expectedY), outputImage.get(x, y), 0.01);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeXAndY() {
 shift = new Shift(-1, -1, Shift.WRAP);
 try {
 shift.apply(realInputImage);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeX() {
 shift = new Shift(1, 1, Shift.NOWRAP);
 try {
 shift.apply(realInputImage).get(-1, 0);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageWithNegativeY() {
 shift = new Shift(1, 1, Shift.NOWRAP);
 try {
 shift.apply(realInputImage).get(0, -1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test
 public void testApplyColorImageWithWrapXAndY() {
 ColorImage outputImage = shift.apply(inputColorImage);
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 int expectedX = (x + shift.xoffset) % outputImage.X();
 int expectedY = (y + shift.yoffset) % outputImage.Y();
 ColorPixel expectedPixel = inputColorImage.get(expectedX, expectedY);
 assertEquals(expectedPixel, outputImage.get(x, y));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeXAndY() {
 shift = new Shift(-1, -1, Shift.WRAP);
 try {
 shift.apply(inputColorImage);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeX() {
 shift = new Shift(1, 1, Shift.NOWRAP);
 try {
 shift.apply(inputColorImage).get(-1, 0).getRed();
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageWithNegativeY() {
 shift = new Shift(1, 1, Shift.NOWRAP);
 try {
 shift.apply(inputColorImage).get(0, -1).getRed();
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test
 public void testApplyRealColorImageWithWrapXAndY() {
 RealColorImage outputImage = (RealColorImage) shift.apply(realInputColorImage);
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 float expectedX = ((float) (x + shift.xoffset)) % ((float) outputImage.X());
 float expectedY = ((float) (y + shift.yoffset)) % ((float) outputImage.Y());
 assertEquals(realInputColorImage.getPlane(0).get(expectedX, expectedY), outputImage.getPlane(0).get(x, y), 0.01);
 assertEquals(realInputColorImage.getPlane(1).get(expectedX, expectedY), outputImage.getPlane(1).get(x, y), 0.01);
 assertEquals(realInputColorImage.getPlane(2).get(expectedX, expectedY), outputImage.getPlane(2).get(x, y), 0.01);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealColorImageWithNegativeXAndY() {
 shift = new Shift(-1, -1, Shift.WRAP);
 try {
 shift.apply(realInputColorImage);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealColorImageWithNegativeX() {
 shift = new Shift(1, 1, Shift.NOWRAP);
 try {
 shift.apply(realInputColorImage).getPlane(0).get(-1, 0).getRed();
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealColorImageWithNegativeY() {
 shift = new Shift(1, 1, Shift.NOWRAP);
 try {
 shift.apply(realInputColorImage).getPlane(0).get(0, -1).getRed();
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 }
}