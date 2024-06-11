package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.GrayImage;
import jigl.image.types.BinaryImage;

import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;
 private BinaryImage binaryImage;
 private GrayImage expectedResult;

 @Before
 public void setup() {
 // Initialize ConnectedComponents object with 4-connected neighbors and background color 0
 cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);

 // Initialize binary image (sample image of size 3x3 with pixels: 1 1 1, 1 1 0, 1 0 1)
 binaryImage = new BinaryImage(new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}});

 // Initialize expected result image (size 3x3 with pixels: 1 1 2, 1 1 2, 1 2 1)
 expectedResult = new GrayImage(3, 3);
 expectedResult.set(0, 0, Integer.valueOf(1));
 expectedResult.set(0, 1, Integer.valueOf(1));
 expectedResult.set(0, 2, Integer.valueOf(2));
 expectedResult.set(1, 0, Integer.valueOf(1));
 expectedResult.set(1, 1, Integer.valueOf(1));
 expectedResult.set(1, 2, Integer.valueOf(2));
 expectedResult.set(2, 0, Integer.valueOf(1));
 expectedResult.set(2, 1, Integer.valueOf(2));
 expectedResult.set(2, 2, Integer.valueOf(1));
 }

 @Test
 public void testConnectedComponents() {
 // Test ConnectedComponents method with 4-connected neighbors
 GrayImage result = cc.apply(binaryImage);
 assertEquals(expectedResult, result);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testInvalidArguments() {
 // Test case with invalid number of neighbors (2-connected)
 ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED + 1, 0);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testInvalidBackgroundColor() {
 // Test case with invalid background color (-1)
 ConnectedComponents ccInvalidBackground = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, -1);
 }
}