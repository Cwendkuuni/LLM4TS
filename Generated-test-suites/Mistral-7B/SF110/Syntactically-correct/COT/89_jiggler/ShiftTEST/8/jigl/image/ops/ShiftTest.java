package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 3;

 @Test
 public void testShiftConstructorWithValidArguments() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 assertEquals(2, shift.getXoffset());
 assertEquals(3, shift.getYoffset());
 assertTrue(shift.isWrap());
 }

 @Test
 public void testShiftConstructorWithInvalidWrapArgument() {
 assertThrows(IllegalArgumentException.class, () -> new Shift(2, 3, 3));
 }

 @Test
 public void testApplyGrayImageWithValidArguments() {
 GrayImage image = new GrayImage(X, Y);
 image.set(0, 0, (byte) 50);
 image.set(1, 1, (byte) 100);
 image.set(X - 1, Y - 1, (byte) 150);
 image.set(X - 1, 0, (byte) 200);
 image.set(0, Y - 1, (byte) 250);

 Shift shift = new Shift(1, 1, Shift.WRAP);
 GrayImage result = shift.apply(image);

 byte expected = (byte) 50;
 byte resultPixel = result.get(X - 2, Y - 2);
 assertEquals(expected, resultPixel);

 expected = (byte) 100;
 resultPixel = result.get(1, 1);
 assertEquals(expected, resultPixel);

 expected = (byte) 150;
 resultPixel = result.get(X - 1, Y - 1);
 assertEquals(expected, resultPixel);

 expected = (byte) 200;
 resultPixel = result.get(X - 1, 0);
 assertEquals(expected, resultPixel);

 expected = (byte) 250;
 resultPixel = result.get(0, Y - 1);
 assertEquals(expected, resultPixel);
 }

 @Test
 public void testApplyGrayImageWithInvalidArguments() {
 GrayImage image = new GrayImage(X, Y);
 image.set(0, 0, (byte) 50);
 image.set(1, 1, (byte) 100);
 image.set(X - 1, Y - 1, (byte) 150);
 image.set(X - 1, 0, (byte) 200);
 image.set(0, Y - 1, (byte) 250);

 Shift shift = new Shift(X + 1, Y + 1, Shift.WRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(image));
 }

 @Test
 public void testApplyGrayImageWithOutOfBoundsCoordinates() {
 GrayImage image = new GrayImage(X, Y);
 image.set(0, 0, (byte) 50);
 image.set(1, 1, (byte) 100);
 image.set(X - 1, Y - 1, (byte) 150);
 image.set(X - 1, 0, (byte) 200);
 image.set(0, Y - 1, (byte) 250);

 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 GrayImage result = shift.apply(image);

 byte expected = (byte) 0;
 byte resultPixel = result.get(X - 2, Y - 2);
 assertEquals(expected, resultPixel);

 expected = (byte) 0;
 resultPixel = result.get(1, 1);
 assertEquals(expected, resultPixel);

 expected = (byte) 0;
 resultPixel = result.get(X - 1, Y - 1);
 assertEquals(expected, resultPixel);

 expected = (byte) 0;
 resultPixel = result.get(X - 1, 0);
 assertEquals(expected, resultPixel);

 expected = (byte) 0;
 resultPixel = result.get(0, Y - 1);
 assertEquals(expected, resultPixel);
 }

 @Test
 public void testApplyRealGrayImageWithValidArguments() {
 RealGrayImage image = new RealGrayImage(X, Y);
 image.set(0, 0, 50.0f);
 image.set(1, 1, 100.0f);
 image.set(X - 1, Y - 1, 150.0f);
 image.set(X - 1, 0, 200.0f);
 image.set(0, Y - 1, 250.0f);

 Shift shift = new Shift(1, 1, Shift.WRAP);
 RealGrayImage result = shift.apply(image);

 float expected = 50.0f;
 float resultPixel = result.get(X - 2, Y - 2);
 assertEquals(expected, resultPixel, 0.01f);

 expected = 100.0f;
 resultPixel = result.get(1, 1);
 assertEquals(expected, resultPixel, 0.01f);

 expected = 150.0f;
 resultPixel = result.get(X - 1, Y - 1);
 assertEquals(expected, resultPixel, 0.01f);

 expected = 200.0f;
 resultPixel = result.get(X - 1, 0);
 assertEquals(expected, resultPixel, 0.01f);

 expected = 250.0f;
 resultPixel = result.get(0, Y - 1);
 assertEquals(expected, resultPixel, 0.01f);
 }

 @Test
 public void testApplyRealGrayImageWithInvalidArguments() {
 RealGrayImage image = new RealGrayImage(X, Y);
 image.set(0, 0, 50.0f);
 image.set(1, 1, 100.0f);
 image.set(X - 1, Y - 1, 150.0f);
 image.set(X - 1, 0, 200.0f);
 image.set(0, Y - 1, 250.0f);

 Shift shift = new Shift(X + 1, Y + 1, Shift.WRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(image));
 }

 @Test
 public void testApplyRealGrayImageWithOutOfBoundsCoordinates() {
 RealGrayImage image = new RealGrayImage(X, Y);
 image.set(0, 0, 50.0f);
 image.set(1, 1, 100.0f);
 image.set(X - 1, Y - 1, 150.0f);
 image.set(X - 1, 0, 200.0f);
 image.set(0, Y - 1, 250.0f);

 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 RealGrayImage result = shift.apply(image);

 float expected = 0.0f;
 float resultPixel = result.get(X - 2, Y - 2);
 assertEquals(expected, resultPixel, 0.01f);

 expected = 0.0f;
 resultPixel = result.get(1, 1);
 assertEquals(expected, resultPixel, 0.01f);

 expected = 0.0f;
 resultPixel = result.get(X - 1, Y - 1);
 assertEquals(expected, resultPixel, 0.01f);

 expected = 0.0f;
 resultPixel = result.get(X - 1, 0);
 assertEquals(expected, resultPixel, 0.01f);

 expected = 0.0f;
 resultPixel = result.get(0, Y - 1);
 assertEquals(expected, resultPixel, 0.01f);
 }

 @Test
 public void testApplyColorImageWithValidArguments() {
 ColorImage image = new ColorImage(new PlaneBuffer(new RGBImageData(3 * X, 3 * Y, new byte[3 * X * Y][], 'BGR')));
 image.setPlane(0, new PlaneBuffer(new ByteInterleavedRaster(new BufferedImage(X, Y, BufferedImage.TYPE_3BYTE_BGR), null, 0, 0, X, Y, null)));
 image.setPlane(1, new PlaneBuffer(new ByteInterleavedRaster(new BufferedImage(X, Y, BufferedImage.TYPE_3BYTE_BGR), null, 0, Y * 3, X, Y, null)));
 image.setPlane(2, new PlaneBuffer(new ByteInterleavedRaster(new BufferedImage(X, Y, BufferedImage.TYPE_3BYTE_BGR), null, 0, 2 * Y * 3, X, Y, null)));

 Shift shift = new Shift(1, 1, Shift.WRAP);
 ColorImage result = shift.apply(image);

 byte[] expectedRGB = {255, 0, 0, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 0, 255, 0, 0, 0};
 byte[] resultRGB = result.plane(0).getData();
 assertArrayEquals(expectedRGB, resultRGB);
 }

 @Test
 public void testApplyRealColorImageWithValidArguments() {
 RealColorImage image = new RealColorImage(new PlaneBuffer(new ColorModel(ColorModel.TYPE_RGB, "RGB", new short[] {8, 8, 8}, null)));
 image.setPlane(0, new PlaneBuffer(new DataBufferByte(X * Y * 3), new Raster(new BufferedImage(X, Y, BufferedImage.TYPE_3BYTE_FLOAT), null, 0, 0, X, Y, null)));

 Shift shift = new Shift(1, 1, Shift.WRAP);
 RealColorImage result = shift.apply(image);

 float[] expectedRGB = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f};
 float[] resultRGB = result.plane(0).getData();
 assertArrayEquals(expectedRGB, resultRGB, 0.001f);
 }

 @Test
 public void testApplyColorImageWithInvalidArguments() {
 ColorImage image = new ColorImage(new PlaneBuffer(new RGBImageData(3 * X, 3 * Y, new byte[3 * X * Y][], 'BGR')));
 image.setPlane(0, new PlaneBuffer(new ByteInterleavedRaster(new BufferedImage(X, Y, BufferedImage.TYPE_3BYTE_BGR), null, 0, 0, X, Y, null)));
 image.setPlane(1, new PlaneBuffer(new ByteInterleavedRaster(new BufferedImage(X, Y, BufferedImage.TYPE_3BYTE_BGR), null, 0, Y * 3, X, Y, null)));
 image.setPlane(2, new PlaneBuffer(new ByteInterleavedRaster(new BufferedImage(X, Y, BufferedImage.TYPE_3BYTE_BGR), null, 0, 2 * Y * 3, X, Y, null)));

 Shift shift = new Shift(X + 1, Y + 1, Shift.WRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(image));
 }

 @Test
 public void testApplyRealColorImageWithInvalidArguments() {
 RealColorImage image = new RealColorImage(new PlaneBuffer(new ColorModel(ColorModel.TYPE_RGB, "RGB", new short[] {8, 8, 8}, null)));
 image.setPlane(0, new PlaneBuffer(new DataBufferByte(X * Y * 3), new Raster(new BufferedImage(X, Y, BufferedImage.TYPE_3BYTE_FLOAT), null, 0, 0, X, Y, null)));

 Shift shift = new Shift(X + 1, Y + 1, Shift.WRAP);
 assertThrows(IndexOutOfBoundsException.class, () -> shift.apply(image));
 }
}