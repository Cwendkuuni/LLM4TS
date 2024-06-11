package jigl.image.ops.test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import org.junit.Assert;
import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsConstructorWithValidArguments() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 Assert.assertEquals(0, cc.neighbors);
 Assert.assertEquals(0, cc.background);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsConstructorWithInvalidNeighborCount() {
 ConnectedComponents cc = new ConnectedComponents(2, 0);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsConstructorWithInvalidBackgroundColor() {
 ConnectedComponents cc = new ConnectedComponents(0, 2);
 }

 @Test
 public void testApplyMethodWithEmptyImage() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 BinaryImage binaryImage = new BinaryImage(0, 0);
 GrayImage expectedResult = new GrayImage(0, 0);
 GrayImage result = cc.apply(binaryImage);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyMethodWithSinglePixelImage() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 BinaryImage binaryImage = new BinaryImage(1, 1);
 binaryImage.set(0, 0, 1);
 GrayImage expectedResult = new GrayImage(1, 1);
 expectedResult.set(0, 0, 1);
 GrayImage result = cc.apply(binaryImage);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyMethodWithComplexImage() {
 ConnectedComponents cc = new ConnectedComponents(1, 1);
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(1, 1, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(4, 2, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(2, 4, 1);
 binaryImage.set(3, 4, 1);
 GrayImage expectedResult = new GrayImage(5, 5);
 expectedResult.set(1, 1, 1);
 expectedResult.set(2, 1, 1);
 expectedResult.set(2, 2, 2);
 expectedResult.set(3, 1, 2);
 expectedResult.set(3, 2, 2);
 expectedResult.set(4, 1, 2);
 expectedResult.set(4, 2, 2);
 expectedResult.set(2, 3, 2);
 expectedResult.set(3, 3, 3);
 expectedResult.set(2, 4, 3);
 expectedResult.set(3, 4, 3);
 GrayImage result = cc.apply(binaryImage);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyMethodWithFourConnectedNeighbors() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(1, 1, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(4, 2, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(2, 4, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(2, 2, 1);
 GrayImage expectedResult = new GrayImage(5, 5);
 expectedResult.set(1, 1, 1);
 expectedResult.set(2, 1, 1);
 expectedResult.set(2, 2, 2);
 expectedResult.set(3, 1, 2);
 expectedResult.set(3, 2, 2);
 expectedResult.set(4, 1, 2);
 expectedResult.set(4, 2, 2);
 expectedResult.set(2, 3, 2);
 expectedResult.set(3, 3, 3);
 expectedResult.set(2, 4, 3);
 expectedResult.set(3, 4, 3);
 expectedResult.set(2, 2, 1);
 GrayImage result = cc.apply(binaryImage);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyMethodWithEightConnectedNeighbors() {
 ConnectedComponents cc = new ConnectedComponents(1, 0);
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(1, 1, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(4, 2, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(2, 4, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(1, 3, 1);
 binaryImage.set(1, 4, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(2, 4, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(3, 4, 1);
 GrayImage expectedResult = new GrayImage(5, 5);
 expectedResult.set(1, 1, 1);
 expectedResult.set(2, 1, 1);
 expectedResult.set(2, 2, 2);
 expectedResult.set(3, 1, 2);
 expectedResult.set(3, 2, 2);
 expectedResult.set(4, 1, 2);
 expectedResult.set(4, 2, 2);
 expectedResult.set(2, 3, 2);
 expectedResult.set(3, 3, 3);
 expectedResult.set(2, 4, 3);
 expectedResult.set(3, 4, 3);
 expectedResult.set(1, 2, 2);
 expectedResult.set(1, 3, 2);
 expectedResult.set(1, 4, 2);
 expectedResult.set(2, 3, 2);
 expectedResult.set(2, 4, 2);
 expectedResult.set(3, 3, 3);
 expectedResult.set(3, 4, 3);
 GrayImage result = cc.apply(binaryImage);
 Assert.assertEquals(expectedResult, result);
 }
}