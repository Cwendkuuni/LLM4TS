package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import org.junit.Test;

public class ShiftTest {

 private GrayImage createGrayImage(int width, int height) {
  GrayImage image = new GrayImage(width, height);
  for (int y = 0; y < height; y++) {
   for (int x = 0; x < width; x++) {
    image.set(x, y, (byte) 255);
   }
  }
  return image;
 }

 private RealGrayImage createRealGrayImage(int width, int height) {
  RealGrayImage image = new RealGrayImage(width, height);
  for (int y = 0; y < height; y++) {
   for (int x = 0; x < width; x++) {
    image.set(x, y, 1.0f);
   }
  }
  return image;
 }

 @Test
 public void testShiftConstructor() {
  Shift shift = new Shift(2, 3, Shift.WRAP);
  assertEquals(2, shift.getXoffset());
  assertEquals(3, shift.getYoffset());
  assertTrue(shift.isWrap());
 }

 @Test
 public void testShiftApplyWithGrayImageAndNoWrap() {
  Shift shift = new Shift(-1, -1, Shift.NOWRAP);
  GrayImage input = createGrayImage(5, 5);
  GrayImage output = shift.apply(input);
  for (int y = 0; y < 5; y++) {
   for (int x = 0; x < 5; x++) {
    assertEquals(input.get(x, y), output.get(x, y));
   }
  }
 }

 @Test
 public void testShiftApplyWithGrayImageAndWrap() {
  Shift shift = new Shift(-1, -1, Shift.WRAP);
  GrayImage input = createGrayImage(5, 5);
  GrayImage output = shift.apply(input);
  for (int y = 0; y < 5; y++) {
   for (int x = 0; x < 5; x++) {
    int xValue = x + shift.getXoffset();
    int yValue = y + shift.getYoffset();
    if (xValue >= 0 && xValue < 5 && yValue >= 0 && yValue < 5) {
     assertEquals(input.get(xValue, yValue), output.get(x, y));
    } else {
     int xWrapped = (xValue + 5) % 5;
     int yWrapped = (yValue + 5) % 5;
     assertEquals(input.get(xWrapped, yWrapped), output.get(x, y));
    }
   }
  }
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftApplyWithGrayImageAndInvalidShift() {
  Shift shift = new Shift(-1, 5, Shift.NOWRAP);
  GrayImage input = createGrayImage(5, 5);
  assertNull(shift.apply(input));
 }

 @Test
 public void testShiftApplyWithRealGrayImageAndNoWrap() {
  Shift shift = new Shift(-1, -1, Shift.NOWRAP);
  RealGrayImage input = createRealGrayImage(5, 5);
  RealGrayImage output = shift.apply(input);
  for (int y = 0; y < 5; y++) {
   for (int x = 0; x < 5; x++) {
    assertEquals(input.get(x, y), output.get(x, y), 0.001);
   }
  }
 }

 @Test
 public void testShiftApplyWithRealGrayImageAndWrap() {
  Shift shift = new Shift(-1, -1, Shift.WRAP);
  RealGrayImage input = createRealGrayImage(5, 5);
  RealGrayImage output = shift.apply(input);
  for (int y = 0; y < 5; y++) {
   for (int x = 0; x < 5; x++) {
    float xValue = x + shift.getXoffset();
    float yValue = y + shift.getYoffset();
    if (xValue >= 0 && xValue < 5 && yValue >= 0 && yValue < 5) {
     assertEquals(input.get(xValue, yValue), output.get(x, y), 0.001);
    } else {
     float xWrapped = (xValue + 5.0f) % 5.0f;
     float yWrapped = (yValue + 5.0f) % 5.0f;
     assertEquals(input.get(xWrapped, yWrapped), output.get(x, y), 0.001);
    }
   }
  }
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftApplyWithRealGrayImageAndInvalidShift() {
  Shift shift = new Shift(-1, 5, Shift.NOWRAP);
  RealGrayImage input = createRealGrayImage(5, 5);
  assertNull(shift.apply(input));
 }

 @Test
 public void testShiftApplyWithColorImageAndNoWrap() {
  Shift shift = new Shift(-1, -1, Shift.NOWRAP);
  ColorImage input = new ColorImage(5, 5);
  ColorImage output = shift.apply(input);
  for (int y = 0; y < 5; y++) {
   for (int x = 0; x < 5; x++) {
    assertEquals(input.plane(0).get(x, y), output.plane(0).get(x, y), 0.001);
    assertEquals(input.plane(1).get(x, y), output.plane(1).get(x, y), 0.001);
    assertEquals(input.plane(2).get(x, y), output.plane(2).get(x, y), 0.001);
   }
  }
 }

 @Test
 public void testShiftApplyWithColorImageAndWrap() {
  Shift shift = new Shift(-1, -1, Shift.WRAP);
  ColorImage input = new ColorImage(5, 5);
  ColorImage output = shift.apply(input);
  for (int y = 0; y < 5; y++) {
   for (int x = 0; x < 5; x++) {
    int xValue = x + shift.getXoffset();
    int yValue = y + shift.getYoffset();
    if (xValue >= 0 && xValue < 5 && yValue >= 0 && yValue < 5) {
     assertEquals(input.plane(0).get(xValue, yValue), output.plane(0).get(x, y), 0.001);
     assertEquals(input.plane(1).get(xValue, yValue), output.plane(1).get(x, y), 0.001);
     assertEquals(input.plane(2).get(xValue, yValue), output.plane(2).get(x, y), 0.001);
    } else {
     int xWrapped = (xValue + 5) % 5;
     int yWrapped = (yValue + 5) % 5;
     assertEquals(input.plane(0).get(xWrapped, yWrapped), output.plane(0).get(x, y), 0.001);
     assertEquals(input.plane(1).get(xWrapped, yWrapped), output.plane(1).get(x, y), 0.001);
     assertEquals(input.plane(2).get(xWrapped, yWrapped), output.plane(2).get(x, y), 0.001);
    }
   }
  }
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftApplyWithColorImageAndInvalidShift() {
  Shift shift = new Shift(-1, 5, Shift.NOWRAP);
  ColorImage input = new ColorImage(5, 5);
  assertNull(shift.apply(input));
 }

 @Test
 public void testShiftApplyWithRealColorImageAndNoWrap() {
  Shift shift = new Shift(-1, -1, Shift.NOWRAP);
  RealColorImage input = new RealColorImage(5, 5);
  RealColorImage output = shift.apply(input);
  for (int y = 0; y < 5; y++) {
   for (int x = 0; x < 5; x++) {
    assertEquals(input.plane(0).get(x, y), output.plane(0).get(x, y), 0.001);
    assertEquals(input.plane(1).get(x, y), output.plane(1).get(x, y), 0.001);
    assertEquals(input.plane(2).get(x, y), output.plane(2).get(x, y), 0.001);
   }
  }
 }

 @Test
 public void testShiftApplyWithRealColorImageAndWrap() {
  Shift shift = new Shift(-1, -1, Shift.WRAP);
  RealColorImage input = new RealColorImage(5, 5);
  RealColorImage output = shift.apply(input);
  for (int y = 0; y < 5; y++) {
   for (int x = 0; x < 5; x++) {
    float xValue = x + shift.getXoffset();
    float yValue = y + shift.getYoffset();
    if (xValue >= 0 && xValue < 5 && yValue >= 0 && yValue < 5) {
     assertEquals(input.plane(0).get(xValue, yValue), output.plane(0).get(x, y), 0.001);
     assertEquals(input.plane(1).get(xValue, yValue), output.plane(1).get(x, y), 0.001);
     assertEquals(input.plane(2).get(xValue, yValue), output.plane(2).get(x, y), 0.001);
    } else {
     float xWrapped = (xValue + 5.0f) % 5.0f;
     float yWrapped = (yValue + 5.0f) % 5.0f;
     assertEquals(input.plane(0).get(xWrapped, yWrapped), output.plane(0).get(x, y), 0.001);
     assertEquals(input.plane(1).get(xWrapped, yWrapped), output.plane(1).get(x, y), 0.001);
     assertEquals(input.plane(2).get(xWrapped, yWrapped), output.plane(2).get(x, y), 0.001);
    }
   }
  }
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftApplyWithRealColorImageAndInvalidShift() {
  Shift shift = new Shift(-1, 5, Shift.NOWRAP);
  RealColorImage input = new RealColorImage(5, 5);
  assertNull(shift.apply(input));
 }

}