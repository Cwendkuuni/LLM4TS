package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsWithValidArguments() {
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(2, 2, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(4, 3, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(4, 4, 1);
 binaryImage.set(2, 4, 1);
 binaryImage.set(3, 4, 1);

 ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
 GrayImage resultImage = connectedComponents.apply(binaryImage);

 GrayImage expectedImage = new GrayImage(5, 5);
 expectedImage.set(2, 2, (byte) 1);
 expectedImage.set(3, 2, (byte) 1);
 expectedImage.set(3, 3, (byte) 1);
 expectedImage.set(4, 3, (byte) 1);
 expectedImage.set(2, 3, (byte) 1);
 expectedImage.set(3, 3, (byte) 1);
 expectedImage.set(3, 4, (byte) 1);
 expectedImage.set(4, 4, (byte) 1);
 expectedImage.set(2, 4, (byte) 1);
 expectedImage.set(3, 4, (byte) 1);
 expectedImage.set(x, y, (byte) 0, x < 2 || x > 4 || y < 2 || y > 4);

 assertEquals("Connected components test failed with 0 neighbors", expectedImage, resultImage);
 }

 @Test
 public void testConnectedComponentsWithValidArgumentsEightConnected() {
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(2, 2, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(4, 3, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(4, 4, 1);
 binaryImage.set(2, 4, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(1, 3, 1);
 binaryImage.set(1, 4, 1);
 binaryImage.set(1, 3, 1);

 ConnectedComponents connectedComponents = new ConnectedComponents(1, 0);
 GrayImage resultImage = connectedComponents.apply(binaryImage);

 GrayImage expectedImage = new GrayImage(5, 5);
 expectedImage.set(x, y, (byte) 1, x != 2 && x != 3 && y != 2 && y != 3);
 expectedImage.set(2, 2, (byte) 1);
 expectedImage.set(3, 2, (byte) 1);
 expectedImage.set(3, 3, (byte) 1);
 expectedImage.set(4, 3, (byte) 1);
 expectedImage.set(2, 3, (byte) 1);
 expectedImage.set(3, 3, (byte) 1);
 expectedImage.set(3, 4, (byte) 1);
 expectedImage.set(4, 4, (byte) 1);
 expectedImage.set(2, 4, (byte) 1);
 expectedImage.set(3, 4, (byte) 1);
 expectedImage.set(3, 1, (byte) 1);
 expectedImage.set(4, 1, (byte) 1);
 expectedImage.set(2, 1, (byte) 1);
 expectedImage.set(3, 1, (byte) 1);
 expectedImage.set(4, 1, (byte) 1);
 expectedImage.set(1, 2, (byte) 1);
 expectedImage.set(1, 3, (byte) 1);
 expectedImage.set(1, 4, (byte) 1);
 expectedImage.set(1, 3, (byte) 1);

 assertEquals("Connected components test with 8 neighbors failed", expectedImage, resultImage);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsWithInvalidArguments() {
 ConnectedComponents connectedComponents = new ConnectedComponents(2, 1);
 }
}