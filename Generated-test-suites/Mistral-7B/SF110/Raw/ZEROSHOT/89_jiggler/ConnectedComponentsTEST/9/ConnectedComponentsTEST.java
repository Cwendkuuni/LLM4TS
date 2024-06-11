package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testValidConstructor() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 assertNotNull(cc);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testInvalidConstructorWithInvalidNeighbors() {
 ConnectedComponents cc = null;
 try {
 cc = new ConnectedComponents(2, 0);
 } catch (InvalidArgumentException e) {
 // Expected exception
 }
 assertNull(cc);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testInvalidConstructorWithInvalidBackground() {
 ConnectedComponents cc = null;
 try {
 cc = new ConnectedComponents(0, 2);
 } catch (InvalidArgumentException e) {
 // Expected exception
 }
 assertNull(cc);
 }

 @Test
 public void testApplyWithEmptyImage() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 BinaryImage binaryImage = new BinaryImage(0, 0);
 GrayImage expectedResult = new GrayImage(0, 0);
 GrayImage actualResult = cc.apply(binaryImage);
 assertSame(expectedResult, actualResult);
 }

 @Test
 public void testApplyWithSinglePixelImage() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 BinaryImage binaryImage = new BinaryImage(1, 1);
 binaryImage.set(0, 0, 1);
 GrayImage expectedResult = new GrayImage(1, 1);
 expectedResult.set(0, 0, 1);
 GrayImage actualResult = cc.apply(binaryImage);
 assertSame(expectedResult, actualResult);
 }

 @Test
 public void testApplyWithSimpleImage() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 BinaryImage binaryImage = new BinaryImage(3, 3);
 binaryImage.set(0, 0, 1);
 binaryImage.set(0, 1, 1);
 binaryImage.set(0, 2, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(2, 0, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 GrayImage expectedResult = new GrayImage(3, 3);
 expectedResult.set(0, 0, 1);
 expectedResult.set(0, 1, 1);
 expectedResult.set(0, 2, 1);
 expectedResult.set(1, 0, 2);
 expectedResult.set(1, 1, 2);
 expectedResult.set(1, 2, 2);
 expectedResult.set(2, 0, 3);
 expectedResult.set(2, 1, 3);
 expectedResult.set(2, 2, 3);
 GrayImage actualResult = cc.apply(binaryImage);
 assertSame(expectedResult, actualResult);
 }

 @Test
 public void testApplyWithComplexImage() {
 ConnectedComponents cc = new ConnectedComponents(1, 0);
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(0, 0, 1);
 binaryImage.set(0, 1, 1);
 binaryImage.set(0, 2, 1);
 binaryImage.set(0, 3, 1);
 binaryImage.set(0, 4, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(1, 3, 1);
 binaryImage.set(1, 4, 1);
 binaryImage.set(2, 0, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(2, 4, 1);
 binaryImage.set(3, 0, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(4, 0, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(4, 2, 1);
 binaryImage.set(4, 3, 1);
 binaryImage.set(4, 4, 1);
 GrayImage expectedResult = new GrayImage(5, 5);
 expectedResult.set(0, 0, 1);
 expectedResult.set(0, 1, 2);
 expectedResult.set(0, 2, 3);
 expectedResult.set(0, 3, 4);
 expectedResult.set(0, 4, 5);
 expectedResult.set(1, 0, 1);
 expectedResult.set(1, 1, 2);
 expectedResult.set(1, 2, 2);
 expectedResult.set(1, 3, 3);
 expectedResult.set(1, 4, 4);
 expectedResult.set(2, 0, 5);
 expectedResult.set(2, 1, 5);
 expectedResult.set(2, 2, 5);
 expectedResult.set(2, 3, 5);
 expectedResult.set(2, 4, 5);
 expectedResult.set(3, 0, 5);
 expectedResult.set(3, 1, 5);
 expectedResult.set(3, 2, 5);
 expectedResult.set(3, 3, 5);
 expectedResult.set(3, 4, 5);
 expectedResult.set(4, 0, 5);
 expectedResult.set(4, 1, 5);
 expectedResult.set(4, 2, 5);
 expectedResult.set(4, 3, 5);
 expectedResult.set(4, 4, 5);
 GrayImage actualResult = cc.apply(binaryImage);
 assertSame(expectedResult, actualResult);
 }

 @Test
 public void testApplyWithComplexImageEightConnected() {
 ConnectedComponents cc = new ConnectedComponents(1, 0);
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(0, 0, 1);
 binaryImage.set(0, 1, 1);
 binaryImage.set(0, 2, 1);
 binaryImage.set(0, 3, 1);
 binaryImage.set(0, 4, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(1, 3, 1);
 binaryImage.set(1, 4, 1);
 binaryImage.set(2, 0, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(2, 4, 1);
 binaryImage.set(3, 0, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(4, 0, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(4, 2, 1);
 binaryImage.set(4, 3, 1);
 binaryImage.set(4, 4, 1);
 GrayImage expectedResult = new GrayImage(5, 5);
 expectedResult.set(0, 0, 1);
 expectedResult.set(0, 1, 2);
 expectedResult.set(0, 2, 3);
 expectedResult.set(0, 3, 4);
 expectedResult.set(0, 4, 5);
 expectedResult.set(1, 0, 1);
 expectedResult.set(1, 1, 2);
 expectedResult.set(1, 2, 3);
 expectedResult.set(1, 3, 4);
 expectedResult.set(1, 4, 5);
 expectedResult.set(2, 0, 5);
 expectedResult.set(2, 1, 5);
 expectedResult.set(2, 2, 6);
 expectedResult.set(2, 3, 6);
 expectedResult.set(2, 4, 6);
 expectedResult.set(3, 0, 7);
 expectedResult.set(3, 1, 7);
 expectedResult.set(3, 2, 8);
 expectedResult.set(3, 3, 8);
 expectedResult.set(3, 4, 8);
 expectedResult.set(4, 0, 9);
 expectedResult.set(4, 1, 9);
 expectedResult.set(4, 2, 9);
 expectedResult.set(4, 3, 9);
 expectedResult.set(4, 4, 9);
 GrayImage actualResult = cc.apply(binaryImage);
 assertSame(expectedResult, actualResult);
 }

}