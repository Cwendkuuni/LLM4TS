package jigl.image.ops.test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.BinaryImage;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsWithFourNeighborsAndBackgroundZero() {
 BinaryImage binaryImage = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1, 1},
 {1, 0, 0, 0, 0, 1},
 {1, 0, 1, 1, 1, 1},
 {1, 0, 1, 0, 1, 1},
 {1, 0, 1, 0, 0, 1},
 {1, 1, 1, 1, 1, 1}
 });
 ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 GrayImage expectedResult = new GrayImage(5, 6);
 expectedResult.set(1, 1, 1);
 expectedResult.set(1, 2, 1);
 expectedResult.set(1, 3, 2);
 expectedResult.set(1, 4, 2);
 expectedResult.set(1, 5, 2);
 expectedResult.set(2, 1, 1);
 expectedResult.set(2, 2, 3);
 expectedResult.set(2, 3, 3);
 expectedResult.set(2, 4, 3);
 expectedResult.set(2, 5, 3);
 expectedResult.set(3, 1, 1);
 expectedResult.set(3, 2, 3);
 expectedResult.set(3, 3, 3);
 expectedResult.set(3, 4, 3);
 expectedResult.set(3, 5, 3);
 expectedResult.set(4, 1, 1);
 expectedResult.set(4, 2, 4);
 expectedResult.set(4, 3, 4);
 expectedResult.set(4, 4, 4);
 expectedResult.set(4, 5, 4);
 expectedResult.set(5, 1, 1);
 expectedResult.set(5, 2, 4);
 expectedResult.set(5, 3, 4);
 expectedResult.set(5, 4, 4);
 expectedResult.set(5, 5, 4);
 GrayImage result = cc.apply(binaryImage);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testConnectedComponentsWithFourNeighborsAndBackgroundOne() {
 BinaryImage binaryImage = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1, 1},
 {1, 0, 0, 0, 0, 1},
 {1, 0, 1, 1, 1, 1},
 {1, 0, 1, 0, 1, 1},
 {1, 0, 1, 0, 0, 1},
 {1, 1, 1, 1, 1, 1}
 });
 ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage expectedResult = new GrayImage(5, 6);
 expectedResult.set(1, 1, 1);
 expectedResult.set(1, 2, 1);
 expectedResult.set(1, 3, 2);
 expectedResult.set(1, 4, 2);
 expectedResult.set(1, 5, 2);
 expectedResult.set(2, 1, 1);
 expectedResult.set(2, 2, 3);
 expectedResult.set(2, 3, 3);
 expectedResult.set(2, 4, 3);
 expectedResult.set(2, 5, 3);
 expectedResult.set(3, 1, 1);
 expectedResult.set(3, 2, 3);
 expectedResult.set(3, 3, 3);
 expectedResult.set(3, 4, 3);
 expectedResult.set(3, 5, 3);
 expectedResult.set(4, 1, 4);
 expectedResult.set(4, 2, 4);
 expectedResult.set(4, 3, 4);
 expectedResult.set(4, 4, 5);
 expectedResult.set(4, 5, 5);
 expectedResult.set(5, 1, 4);
 expectedResult.set(5, 2, 4);
 expectedResult.set(5, 3, 4);
 expectedResult.set(5, 4, 5);
 expectedResult.set(5, 5, 5);
 GrayImage result = cc.apply(binaryImage);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testConnectedComponentsWithEightNeighborsAndBackgroundZero() {
 BinaryImage binaryImage = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1, 1},
 {1, 0, 0, 0, 0, 1},
 {1, 0, 1, 1, 1, 1},
 {1, 0, 1, 0, 1, 1},
 {1, 0, 1, 0, 0, 1},
 {1, 1, 1, 1, 1, 1}
 });
 ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
 GrayImage expectedResult = new GrayImage(5, 6);
 expectedResult.set(1, 1, 1);
 expectedResult.set(1, 2, 1);
 expectedResult.set(1, 3, 2);
 expectedResult.set(1, 4, 2);
 expectedResult.set(1, 5, 2);
 expectedResult.set(2, 1, 1);
 expectedResult.set(2, 2, 3);
 expectedResult.set(2, 3, 3);
 expectedResult.set(2, 4, 3);
 expectedResult.set(2, 5, 3);
 expectedResult.set(3, 1, 1);
 expectedResult.set(3, 2, 3);
 expectedResult.set(3, 3, 3);
 expectedResult.set(3, 4, 3);
 expectedResult.set(3, 5, 3);
 expectedResult.set(4, 1, 4);
 expectedResult.set(4, 2, 4);
 expectedResult.set(4, 3, 4);
 expectedResult.set(4, 4, 4);
 expectedResult.set(4, 5, 4);
 expectedResult.set(5, 1, 4);
 expectedResult.set(5, 2, 4);
 expectedResult.set(5, 3, 4);
 expectedResult.set(5, 4, 4);
 expectedResult.set(5, 5, 4);
 GrayImage result = cc.apply(binaryImage);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testConnectedComponentsWithEightNeighborsAndBackgroundOne() {
 BinaryImage binaryImage = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1, 1},
 {1, 0, 0, 0, 0, 1},
 {1, 0, 1, 1, 1, 1},
 {1, 0, 1, 0, 1, 1},
 {1, 0, 1, 0, 0, 1},
 {1, 1, 1, 1, 1, 1}
 });
 ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
 GrayImage expectedResult = new GrayImage(5, 6);
 expectedResult.set(1, 1, 1);
 expectedResult.set(1, 2, 1);
 expectedResult.set(1, 3, 2);
 expectedResult.set(1, 4, 2);
 expectedResult.set(1, 5, 2);
 expectedResult.set(2, 1, 1);
 expectedResult.set(2, 2, 3);
 expectedResult.set(2, 3, 3);
 expectedResult.set(2, 4, 3);
 expectedResult.set(2, 5, 3);
 expectedResult.set(3, 1, 1);
 expectedResult.set(3, 2, 3);
 expectedResult.set(3, 3, 3);
 expectedResult.set(3, 4, 3);
 expectedResult.set(3, 5, 3);
 expectedResult.set(4, 1, 5);
 expectedResult.set(4, 2, 5);
 expectedResult.set(4, 3, 5);
 expectedResult.set(4, 4, 5);
 expectedResult.set(4, 5, 5);
 expectedResult.set(5, 1, 5);
 expectedResult.set(5, 2, 5);
 expectedResult.set(5, 3, 5);
 expectedResult.set(5, 4, 5);
 expectedResult.set(5, 5, 5);
 GrayImage result = cc.apply(binaryImage);
 assertEquals(expectedResult, result);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsWithInvalidArgumentsThrowsException() {
 ConnectedComponents cc = new ConnectedComponents(2, 2);
 }

}