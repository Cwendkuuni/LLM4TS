package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.GrayImage;
import jigl.image.types.BinaryImage;

import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsWithFourNeighborsAndBackgroundZero() {
 BinaryImage binaryImage = new BinaryImage(new int[][] {{1, 1, 1, 1, 1}, {1, 0, 1, 0, 1}, {1, 0, 1, 0, 1}, {1, 0, 1, 0, 1}, {1, 1, 1, 1, 1}});
 ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 GrayImage expectedOutput = new GrayImage(5, 5);
 expectedOutput.set(1, 1, Integer.valueOf(1));
 expectedOutput.set(2, 1, Integer.valueOf(1));
 expectedOutput.set(3, 1, Integer.valueOf(1));
 expectedOutput.set(4, 1, Integer.valueOf(1));
 expectedOutput.set(5, 1, Integer.valueOf(1));
 expectedOutput.set(1, 2, Integer.valueOf(2));
 expectedOutput.set(2, 2, Integer.valueOf(2));
 expectedOutput.set(3, 2, Integer.valueOf(2));
 expectedOutput.set(4, 2, Integer.valueOf(2));
 expectedOutput.set(5, 2, Integer.valueOf(2));
 expectedOutput.set(1, 3, Integer.valueOf(3));
 expectedOutput.set(2, 3, Integer.valueOf(3));
 expectedOutput.set(3, 3, Integer.valueOf(3));
 expectedOutput.set(4, 3, Integer.valueOf(3));
 expectedOutput.set(5, 3, Integer.valueOf(3));
 expectedOutput.set(1, 4, Integer.valueOf(4));
 expectedOutput.set(2, 4, Integer.valueOf(4));
 expectedOutput.set(3, 4, Integer.valueOf(4));
 expectedOutput.set(4, 4, Integer.valueOf(4));
 expectedOutput.set(5, 4, Integer.valueOf(4));
 expectedOutput.set(1, 5, Integer.valueOf(5));
 expectedOutput.set(2, 5, Integer.valueOf(5));
 expectedOutput.set(3, 5, Integer.valueOf(5));
 expectedOutput.set(4, 5, Integer.valueOf(5));
 expectedOutput.set(5, 5, Integer.valueOf(5));
 GrayImage output = connectedComponents.apply(binaryImage);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testConnectedComponentsWithFourNeighborsAndBackgroundOne() {
 BinaryImage binaryImage = new BinaryImage(new int[][] {{0, 0, 0, 0, 0}, {0, 1, 1, 1, 0}, {0, 1, 1, 1, 0}, {0, 1, 1, 1, 0}, {0, 0, 0, 0, 0}});
 ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage expectedOutput = new GrayImage(5, 5);
 expectedOutput.set(1, 1, Integer.valueOf(1));
 expectedOutput.set(2, 1, Integer.valueOf(2));
 expectedOutput.set(3, 1, Integer.valueOf(3));
 expectedOutput.set(4, 1, Integer.valueOf(4));
 expectedOutput.set(5, 1, Integer.valueOf(5));
 expectedOutput.set(1, 2, Integer.valueOf(2));
 expectedOutput.set(2, 2, Integer.valueOf(2));
 expectedOutput.set(3, 2, Integer.valueOf(3));
 expectedOutput.set(4, 2, Integer.valueOf(4));
 expectedOutput.set(5, 2, Integer.valueOf(5));
 expectedOutput.set(1, 3, Integer.valueOf(3));
 expectedOutput.set(2, 3, Integer.valueOf(4));
 expectedOutput.set(3, 3, Integer.valueOf(4));
 expectedOutput.set(4, 3, Integer.valueOf(5));
 expectedOutput.set(5, 3, Integer.valueOf(5));
 expectedOutput.set(1, 4, Integer.valueOf(4));
 expectedOutput.set(2, 4, Integer.valueOf(5));
 expectedOutput.set(3, 4, Integer.valueOf(5));
 expectedOutput.set(4, 4, Integer.valueOf(5));
 expectedOutput.set(5, 4, Integer.valueOf(5));
 expectedOutput.set(1, 5, Integer.valueOf(5));
 expectedOutput.set(2, 5, Integer.valueOf(5));
 expectedOutput.set(3, 5, Integer.valueOf(5));
 expectedOutput.set(4, 5, Integer.valueOf(5));
 expectedOutput.set(5, 5, Integer.valueOf(5));
 GrayImage output = connectedComponents.apply(binaryImage);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testConnectedComponentsWithEightNeighborsAndBackgroundZero() {
 BinaryImage binaryImage = new BinaryImage(new int[][] {{1, 1, 1, 1, 1}, {1, 0, 1, 0, 1}, {1, 0, 1, 0, 1}, {1, 0, 1, 0, 1}, {1, 1, 1, 1, 1}});
 ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
 GrayImage expectedOutput = new GrayImage(5, 5);
 expectedOutput.set(1, 1, Integer.valueOf(1));
 expectedOutput.set(1, 2, Integer.valueOf(1));
 expectedOutput.set(2, 1, Integer.valueOf(1));
 expectedOutput.set(2, 2, Integer.valueOf(2));
 expectedOutput.set(3, 1, Integer.valueOf(1));
 expectedOutput.set(3, 2, Integer.valueOf(3));
 expectedOutput.set(4, 1, Integer.valueOf(1));
 expectedOutput.set(4, 2, Integer.valueOf(4));
 expectedOutput.set(5, 1, Integer.valueOf(1));
 expectedOutput.set(5, 2, Integer.valueOf(5));
 expectedOutput.set(1, 3, Integer.valueOf(1));
 expectedOutput.set(1, 4, Integer.valueOf(1));
 expectedOutput.set(2, 3, Integer.valueOf(2));
 expectedOutput.set(2, 4, Integer.valueOf(2));
 expectedOutput.set(3, 3, Integer.valueOf(3));
 expectedOutput.set(3, 4, Integer.valueOf(3));
 expectedOutput.set(4, 3, Integer.valueOf(4));
 expectedOutput.set(4, 4, Integer.valueOf(4));
 expectedOutput.set(5, 3, Integer.valueOf(5));
 expectedOutput.set(5, 4, Integer.valueOf(5));
 expectedOutput.set(1, 5, Integer.valueOf(1));
 expectedOutput.set(2, 5, Integer.valueOf(2));
 expectedOutput.set(3, 5, Integer.valueOf(3));
 expectedOutput.set(4, 5, Integer.valueOf(4));
 expectedOutput.set(5, 5, Integer.valueOf(5));
 GrayImage output = connectedComponents.apply(binaryImage);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testConnectedComponentsWithEightNeighborsAndBackgroundOne() {
 BinaryImage binaryImage = new BinaryImage(new int[][] {{0, 0, 0, 0, 0}, {0, 1, 1, 1, 0}, {0, 1, 1, 1, 0}, {0, 1, 1, 1, 0}, {0, 0, 0, 0, 0}});
 ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
 GrayImage expectedOutput = new GrayImage(5, 5);
 expectedOutput.set(1, 1, Integer.valueOf(1));
 expectedOutput.set(1, 2, Integer.valueOf(1));
 expectedOutput.set(2, 1, Integer.valueOf(2));
 expectedOutput.set(2, 2, Integer.valueOf(2));
 expectedOutput.set(3, 1, Integer.valueOf(2));
 expectedOutput.set(3, 2, Integer.valueOf(3));
 expectedOutput.set(4, 1, Integer.valueOf(2));
 expectedOutput.set(4, 2, Integer.valueOf(4));
 expectedOutput.set(5, 1, Integer.valueOf(2));
 expectedOutput.set(5, 2, Integer.valueOf(5));
 expectedOutput.set(1, 3, Integer.valueOf(2));
 expectedOutput.set(1, 4, Integer.valueOf(2));
 expectedOutput.set(2, 3, Integer.valueOf(3));
 expectedOutput.set(2, 4, Integer.valueOf(3));
 expectedOutput.set(3, 3, Integer.valueOf(4));
 expectedOutput.set(3, 4, Integer.valueOf(4));
 expectedOutput.set(4, 3, Integer.valueOf(4));
 expectedOutput.set(4, 4, Integer.valueOf(4));
 expectedOutput.set(5, 3, Integer.valueOf(5));
 expectedOutput.set(5, 4, Integer.valueOf(5));
 expectedOutput.set(1, 5, Integer.valueOf(2));
 expectedOutput.set(2, 5, Integer.valueOf(3));
 expectedOutput.set(3, 5, Integer.valueOf(3));
 expectedOutput.set(4, 5, Integer.valueOf(4));
 expectedOutput.set(5, 5, Integer.valueOf(5));
 GrayImage output = connectedComponents.apply(binaryImage);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsWithInvalidNeighborsValue() {
 ConnectedComponents connectedComponents = new ConnectedComponents(-1, 0);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsWithInvalidBackgroundValue() {
 ConnectedComponents connectedComponents = new ConnectedComponents(0, 2);
 }
}