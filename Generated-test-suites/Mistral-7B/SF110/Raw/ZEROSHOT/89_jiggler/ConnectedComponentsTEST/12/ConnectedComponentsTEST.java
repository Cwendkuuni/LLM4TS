package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsValidConstruction() {
 ConnectedComponents component = new ConnectedComponents(0, 0);
 assertNull(component.getBackground());
 assertFalse(component.isNeighborsEightConnected());

 component = new ConnectedComponents(1, 0);
 assertNull(component.getBackground());
 assertTrue(component.isNeighborsEightConnected());

 component = new ConnectedComponents(0, 1);
 assertEqualInt(1, component.getBackground());
 assertFalse(component.isNeighborsEightConnected());

 component = new ConnectedComponents(1, 1);
 assertEqualInt(1, component.getBackground());
 assertTrue(component.isNeighborsEightConnected());
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsInvalidConstruction() {
 new ConnectedComponents(-1, 0);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsInvalidConstruction2() {
 new ConnectedComponents(0, -1);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsInvalidConstruction3() {
 new ConnectedComponents(2, 0);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsInvalidConstruction4() {
 new ConnectedComponents(0, 2);
 }

 @Test
 public void testConnectedComponentsApply() {
 BinaryImage binaryImage = createBinaryImage(3, 3, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
 GrayImage expectedOutput = new GrayImage(3, 3, new int[]{1, 1, 1, 2, 2, 2, 2, 2, 2});
 ConnectedComponents component = new ConnectedComponents(0, 0);
 GrayImage outputImage = component.apply(binaryImage);
 assertSame(expectedOutput, outputImage);
 }

 @Test
 public void testConnectedComponentsApplyEightConnected() {
 BinaryImage binaryImage = createBinaryImage(3, 3, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
 ConnectedComponents component = new ConnectedComponents(1, 0);
 GrayImage outputImage = component.apply(binaryImage);
 assertSame(expectedOutputEightConnected(3, 3), outputImage);
 }

 @Test
 public void testConnectedComponentsApplyEdgeCases() {
 BinaryImage binaryImage1 = createBinaryImage(3, 2, new int[]{1, 1, 0, 1, 1});
 BinaryImage binaryImage2 = createBinaryImage(3, 2, new int[]{0, 0, 0, 1, 1});
 BinaryImage binaryImage3 = createBinaryImage(3, 2, new int[]{1, 1, 1, 0, 0});

 GrayImage expectedOutput1 = new GrayImage(3, 2, new int[]{1, 1, 1, 2, 2});
 GrayImage expectedOutput2 = new GrayImage(3, 2, new int[]{2, 2, 2, 2, 2});
 GrayImage expectedOutput3 = new GrayImage(3, 2, new int[]{1, 1, 1, 2, 2});

 ConnectedComponents component1 = new ConnectedComponents(0, 0);
 GrayImage outputImage1 = component1.apply(binaryImage1);
 assertSame(expectedOutput1, outputImage1);

 ConnectedComponents component2 = new ConnectedComponents(1, 0);
 GrayImage outputImage2 = component2.apply(binaryImage2);
 assertSame(expectedOutput2, outputImage2);

 ConnectedComponents component3 = new ConnectedComponents(0, 0);
 GrayImage outputImage3 = component3.apply(binaryImage3);
 assertSame(expectedOutput3, outputImage3);
 }

 private void assertEqualInt(int expected, int actual) {
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
 }

 private void assertSame(Object expected, Object actual) {
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
 }

 private BinaryImage createBinaryImage(int width, int height, int[] values) {
 GrayImage inputImage = new GrayImage(width, height, values);
 BinaryImage binaryImage = new BinaryImage(inputImage);
 return binaryImage;
 }

 private GrayImage expectedOutputEightConnected(int width, int height) {
 int[] values = new int[width * height];
 for (int i = 0; i < values.length; i++) {
 values[i] = 1;
 }
 GrayImage expectedOutput = new GrayImage(width, height, values);
 return expectedOutput;
 }
}