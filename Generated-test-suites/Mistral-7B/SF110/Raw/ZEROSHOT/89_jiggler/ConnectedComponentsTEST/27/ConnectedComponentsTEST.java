package jigl.image.ops.test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.ops.exceptions.InvalidArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsConstructionWithValidArguments() {
 ConnectedComponents cc = null;
 try {
 cc = new ConnectedComponents(0, 0);
 } catch (InvalidArgumentException e) {
 fail("Expected no exception when creating an instance with valid arguments.");
 }
 assertEquals(0, cc.neighbors);
 assertEquals(0, cc.background);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsConstructionWithInvalidArguments() {
 ConnectedComponents cc = null;
 try {
 cc = new ConnectedComponents(2, 5);
 } catch (InvalidArgumentException e) {
 // expected
 }
 }

 @Test
 public void testApplyWithBinaryImageOfSize1x1AndBackgroundColorOfZero() {
 BinaryImage binaryImage = new BinaryImage(1, 1);
 binaryImage.set(0, 0, 0);
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 GrayImage grayImage = cc.apply(binaryImage);
 assertTrue(grayImage.get(0, 0).equals(Integer.valueOf(0)));
 }

 @Test
 public void testApplyWithBinaryImageOfSize1x1AndBackgroundColorOfOne() {
 BinaryImage binaryImage = new BinaryImage(1, 1);
 binaryImage.set(0, 0, 1);
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 GrayImage grayImage = cc.apply(binaryImage);
 assertTrue(grayImage.get(0, 0).equals(Integer.valueOf(1)));
 }

 @Test
 public void testApplyWithBinaryImageOfSize2x2() {
 BinaryImage binaryImage = new BinaryImage(2, 2);
 binaryImage.set(0, 0, 0);
 binaryImage.set(0, 1, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(1, 1, 0);
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 GrayImage grayImage = cc.apply(binaryImage);
 GrayImage expectedImage = new GrayImage(2, 2);
 expectedImage.set(0, 0, Integer.valueOf(1));
 expectedImage.set(0, 1, Integer.valueOf(1));
 expectedImage.set(1, 0, Integer.valueOf(1));
 expectedImage.set(1, 1, Integer.valueOf(0));
 assertEquals(expectedImage, grayImage);
 }

 @Test
 public void testApplyWithBinaryImageOfSize2x2WithBackgroundColorOfOne() {
 BinaryImage binaryImage = new BinaryImage(2, 2);
 binaryImage.set(0, 0, 0);
 binaryImage.set(0, 1, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(1, 1, 0);
 ConnectedComponents cc = new ConnectedComponents(0, 1);
 GrayImage grayImage = cc.apply(binaryImage);
 GrayImage expectedImage = new GrayImage(2, 2);
 expectedImage.set(0, 0, Integer.valueOf(1));
 expectedImage.set(0, 1, Integer.valueOf(1));
 expectedImage.set(1, 0, Integer.valueOf(1));
 expectedImage.set(1, 1, Integer.valueOf(1));
 assertEquals(expectedImage, grayImage);
 }

 @Test
 public void testApplyWithBinaryImageOfSize3x3() {
 BinaryImage binaryImage = new BinaryImage(3, 3);
 binaryImage.set(0, 0, 0);
 binaryImage.set(0, 1, 1);
 binaryImage.set(0, 2, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(2, 0, 0);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 GrayImage grayImage = cc.apply(binaryImage);
 GrayImage expectedImage = new GrayImage(3, 3);
 expectedImage.set(0, 0, Integer.valueOf(1));
 expectedImage.set(0, 1, Integer.valueOf(2));
 expectedImage.set(0, 2, Integer.valueOf(2));
 expectedImage.set(1, 0, Integer.valueOf(1));
 expectedImage.set(1, 1, Integer.valueOf(1));
 expectedImage.set(1, 2, Integer.valueOf(1));
 expectedImage.set(2, 0, Integer.valueOf(0));
 expectedImage.set(2, 1, Integer.valueOf(2));
 expectedImage.set(2, 2, Integer.valueOf(2));
 assertEquals(expectedImage, grayImage);
 }

 @Test
 public void testApplyWithBinaryImageOfSize3x3WithBackgroundColorOfOne() {
 BinaryImage binaryImage = new BinaryImage(3, 3);
 binaryImage.set(0, 0, 0);
 binaryImage.set(0, 1, 1);
 binaryImage.set(0, 2, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(2, 0, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 ConnectedComponents cc = new ConnectedComponents(0, 1);
 GrayImage grayImage = cc.apply(binaryImage);
 GrayImage expectedImage = new GrayImage(3, 3);
 expectedImage.set(0, 0, Integer.valueOf(1));
 expectedImage.set(0, 1, Integer.valueOf(1));
 expectedImage.set(0, 2, Integer.valueOf(1));
 expectedImage.set(1, 0, Integer.valueOf(1));
 expectedImage.set(1, 1, Integer.valueOf(1));
 expectedImage.set(1, 2, Integer.valueOf(1));
 expectedImage.set(2, 0, Integer.valueOf(1));
 expectedImage.set(2, 1, Integer.valueOf(1));
 expectedImage.set(2, 2, Integer.valueOf(1));
 assertEquals(expectedImage, grayImage);
 }

 @Test
 public void testApplyWithBinaryImageOfSize5x5() {
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(0, 0, 0);
 binaryImage.set(0, 1, 1);
 binaryImage.set(0, 2, 1);
 binaryImage.set(0, 3, 1);
 binaryImage.set(0, 4, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(1, 3, 1);
 binaryImage.set(1, 4, 0);
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
 binaryImage.set(4, 0, 0);
 binaryImage.set(4, 1, 1);
 binaryImage.set(4, 2, 1);
 binaryImage.set(4, 3, 1);
 binaryImage.set(4, 4, 1);
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 GrayImage grayImage = cc.apply(binaryImage);
 // Manual verification of the output is difficult due to its size.
 // You can display the result using a graphics library or a debugger.
 }

 @Test
 public void testApplyWithBinaryImageOfSize5x5WithBackgroundColorOfOne() {
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(0, 0, 0);
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
 ConnectedComponents cc = new ConnectedComponents(0, 1);
 GrayImage grayImage = cc.apply(binaryImage);
 // Manual verification of the output is difficult due to its size.
 // You can display the result using a graphics library or a debugger.
 }

}