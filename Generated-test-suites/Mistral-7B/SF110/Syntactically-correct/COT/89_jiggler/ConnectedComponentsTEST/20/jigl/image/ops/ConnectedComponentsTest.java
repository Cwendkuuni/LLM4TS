package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

 private BinaryImage createImageWithShape(int shapeX, int shapeY, int shapeValue, int backgroundValue) {
  BinaryImage image = new BinaryImage(shapeX, shapeY);
  for (int x = 0; x < shapeX; ++x) {
   for (int y = 0; y < shapeY; ++y) {
    image.set(x, y, shapeValue);
   }
  }
  for (int x = shapeX; x < image.X(); ++x) {
   for (int y = 0; y < image.Y(); ++y) {
    image.set(x, y, backgroundValue);
   }
  }
  for (int x = 0; x < shapeX; ++x) {
   for (int y = 0; y < shapeY; ++y) {
    if (x < shapeX / 2 || y < shapeY / 2 || x > shapeX - shapeX / 2 || y > shapeY - shapeY / 2) {
     image.set(x, y, backgroundValue);
    }
   }
  }
  return image;
 }

 @Test
 public void testConnectedComponentsConstructorInvalidArgumentException() {
  try {
   new ConnectedComponents(-1, 1);
   fail("Expected InvalidArgumentException");
  } catch (InvalidArgumentException e) {
   // expected
  }
  
  try {
   new ConnectedComponents(1, -1);
   fail("Expected InvalidArgumentException");
  } catch (InvalidArgumentException e) {
   // expected
  }
 }

 @Test
 public void testConnectedComponentsApplyBasicFunctionality() {
  BinaryImage image = createImageWithShape(5, 5, 1, 0);
  ConnectedComponents op = new ConnectedComponents(0, 1);
  GrayImage result = op.apply(image);
  assertEquals(5, result.X());
  assertEquals(5, result.Y());
  for (int x = 0; x < result.X(); ++x) {
   for (int y = 0; y < result.Y(); ++y) {
    int expected = (x / 2 + y / 2) % 2 + 1;
    int actual = result.get(x, y).intValue();
    assertEquals(Integer.valueOf(expected), result.get(x, y));
   }
  }
 }

 @Test
 public void testConnectedComponentsApplyEightConnectedFunctionality() {
  BinaryImage image = createImageWithShape(6, 6, 1, 0);
  ConnectedComponents op = new ConnectedComponents(1, 1);
  GrayImage result = op.apply(image);
  assertEquals(6, result.X());
  assertEquals(6, result.Y());
  for (int x = 0; x < result.X(); ++x) {
   for (int y = 0; y < result.Y(); ++y) {
    int expected = Math.abs(x / 2 - y / 2) % 2 + 1;
    int actual = result.get(x, y).intValue();
    assertEquals(Integer.valueOf(expected), result.get(x, y));
   }
  }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testConnectedComponentsApplyOutOfBounds() {
  BinaryImage image = new BinaryImage(1, 1);
  ConnectedComponents op = new ConnectedComponents(0, 1);
  op.apply(image);
  fail("Expected ArrayIndexOutOfBoundsException");
 }

 @Test(expected = NullPointerException.class)
 public void testConnectedComponentsApplyNullImage() {
  BinaryImage image = null;
  ConnectedComponents op = new ConnectedComponents(0, 1);
  op.apply(image);
  fail("Expected NullPointerException");
 }

 @Test
 public void testConnectedComponentsApplyInputImageNullPixel() {
  BinaryImage image = createImageWithShape(5, 5, 1, 0);
  ConnectedComponents op = new ConnectedComponents(0, 1);
  GrayImage result = op.apply(image);
  for (int x = 0; x < image.X(); ++x) {
   for (int y = 0; y < image.Y(); ++y) {
    if (image.get(x, y) == null) {
     assertEquals(Integer.valueOf(0), result.get(x, y));
    }
   }
  }
 }

 @Test
 public void testConnectedComponentsApplyInputImageZeroPixel() {
  BinaryImage image = createImageWithShape(5, 5, 0, 1);
  ConnectedComponents op = new ConnectedComponents(0, 1);
  GrayImage result = op.apply(image);
  for (int x = 0; x < result.X(); ++x) {
   for (int y = 0; y < result.Y(); ++y) {
    int expected = 0;
    int actual = result.get(x, y).intValue();
    assertEquals(Integer.valueOf(expected), result.get(x, y));
   }
  }
 }

 @Test
 public void testConnectedComponentsApplyInputImageBackgroundPixel() {
  BinaryImage image = createImageWithShape(5, 5, 1, 1);
  ConnectedComponents op = new ConnectedComponents(0, 1);
  GrayImage result = op.apply(image);
  for (int x = 0; x < result.X(); ++x) {
   for (int y = 0; y < result.Y(); ++y) {
    if (image.get(x, y).intValue() == 1) {
     fail("Expected background pixel to remain unchanged");
    }
   }
  }
 }

 @Test
 public void testConnectedComponentsApplyInputImageMixedPixels() {
  BinaryImage image = createImageWithShape(6, 6, new int[] { 1, 1, 0, 1, 1, 1 }, 0);
  ConnectedComponents op = new ConnectedComponents(0, 1);
  GrayImage result = op.apply(image);
  assertEquals(6, result.X());
  assertEquals(6, result.Y());
  for (int x = 0; x < result.X(); ++x) {
   for (int y = 0; y < result.Y(); ++y) {
    int expected = (x / 2 + y / 2) % 2 + 1;
    int actual = result.get(x, y).intValue();
    assertEquals(Integer.valueOf(expected), result.get(x, y));
   }
  }
 }

 @Test
 public void testConnectedComponentsApplyInputImageMixedPixelsTwoComponents() {
  BinaryImage image = createImageWithShape(7, 7, new int[] { 1, 1, 0, 1, 1, 1, 1 }, 0);
  ConnectedComponents op = new ConnectedComponents(0, 1);
  GrayImage result = op.apply(image);
  assertEquals(7, result.X());
  assertEquals(7, result.Y());
  for (int x = 0; x < result.X(); ++x) {
   for (int y = 0; y < result.Y(); ++y) {
    int expected = (x / 2 + y / 2) % 2 + 1;
    int actual = result.get(x, y).intValue();
    if ((x / 2 + y / 2) % 2 == 0) {
     assertEquals(Integer.valueOf(1), result.get(x, y));
    } else {
     assertEquals(Integer.valueOf(expected), result.get(x, y));
    }
   }
  }
 }

 @Test
 public void testConnectedComponentsApplyInputImageMixedPixelsThreeComponents() {
  BinaryImage image = createImageWithShape(7, 7, new int[] { 1, 1, 1, 0, 1, 1, 1 }, 0);
  ConnectedComponents op = new ConnectedComponents(0, 1);
  GrayImage result = op.apply(image);
  assertEquals(7, result.X());
  assertEquals(7, result.Y());
  for (int x = 0; x < result.X(); ++x) {
   for (int y = 0; y < result.Y(); ++y) {
    int expected = Math.abs(x / 2 - y / 2) % 2 + 1;
    int actual = result.get(x, y).intValue();
    assertEquals(Integer.valueOf(expected), result.get(x, y));
   }
  }
 }

 @Test
 public void testConnectedComponentsApplyInputImageMixedPixelsFourComponents() {
  BinaryImage image = createImageWithShape(7, 7, new int[] { 1, 1, 1, 1, 0, 1, 1 }, 0);
  ConnectedComponents op = new ConnectedComponents(1, 1);
  GrayImage result = op.apply(image);
  assertEquals(7, result.X());
  assertEquals(7, result.Y());
  for (int x = 0; x < result.X(); ++x) {
   for (int y = 0; y < result.Y(); ++y) {
    int expected = Math.abs(x / 2 - y / 2) % 2 + 1;
    int actual = result.get(x, y).intValue();
    assertEquals(Integer.valueOf(expected), result.get(x, y));
   }
  }
 }

}