package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsWithValidArguments() throws InvalidArgumentException {
 BinaryImage binaryImage = new BinaryImage(5, 5);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(1, 3, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(4, 2, 1);
 binaryImage.set(4, 3, 1);
 binaryImage.set(4, 4, 1);
 binaryImage.set(5, 1, 1);
 binaryImage.set(5, 2, 1);
 binaryImage.set(5, 3, 1);
 binaryImage.set(5, 4, 1);
 binaryImage.set(5, 5, 1);

 ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
 GrayImage result = connectedComponents.apply(binaryImage);

 assertEquals(6, result.X());
 assertEquals(6, result.Y());

 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int expectedValue = x * 6 + y + 1;
 int actualValue = result.get(x, y).intValue();
 assertEquals(expectedValue, actualValue);
 }
 }

 ConnectedComponents connectedComponents1 = new ConnectedComponents(1, 0);
 GrayImage result1 = connectedComponents1.apply(binaryImage);

 assertEquals(6, result1.X());
 assertEquals(6, result1.Y());

 for (int x = 0; x < result1.X(); ++x) {
 for (int y = 0; y < result1.Y(); ++y) {
 int expectedValue = ConnectedComponents.findComponentLabel(binaryImage, x, y, 1);
 int actualValue = result1.get(x, y).intValue();
 assertEquals(expectedValue, actualValue);
 }
 }
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsWithInvalidBackgroundColor() throws InvalidArgumentException {
 ConnectedComponents connectedComponents = new ConnectedComponents(0, 2);
 binaryImage.setBackground(2);
 try {
 connectedComponents.apply(binaryImage);
 fail();
 } catch (InvalidArgumentException e) {
 // expected
 }
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsWithInvalidNumberOfNeighbors() throws InvalidArgumentException {
 ConnectedComponents connectedComponents = new ConnectedComponents(2, 0);
 try {
 connectedComponents.apply(binaryImage);
 fail();
 } catch (InvalidArgumentException e) {
 // expected
 }
 }

 @Test
 public void testConnectedComponentsWithEmptyImage() throws InvalidArgumentException {
 BinaryImage emptyImage = new BinaryImage(0, 0);
 ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
 GrayImage result = connectedComponents.apply(emptyImage);
 assertEquals(0, result.X());
 assertEquals(0, result.Y());
 }

 @Test
 public void testConnectedComponentsWithSinglePixelImage() throws InvalidArgumentException {
 BinaryImage singlePixelImage = new BinaryImage(1, 1);
 singlePixelImage.set(0, 0, 1);
 ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
 GrayImage result = connectedComponents.apply(singlePixelImage);
 assertEquals(1, result.X());
 assertEquals(1, result.Y());
 assertEquals(1, result.get(0, 0).intValue());
 }
}