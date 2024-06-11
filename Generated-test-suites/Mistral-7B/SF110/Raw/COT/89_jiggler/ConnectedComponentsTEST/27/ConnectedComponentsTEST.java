package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ConnectedComponentsTest {

 private BinaryImage createBinaryImage(int width, int height, int backgroundValue, int... pixelValues) {
 BinaryImage image = new BinaryImage(width, height);
 for (int i = 0; i < width * height; ++i) {
 image.set(i / width, i % width, backgroundValue);
 }
 for (int i = 0; i < pixelValues.length; ++i) {
 image.set(pixelValues[i] / width, pixelValues[i] % width, 1);
 }
 return image;
 }

 @Test
 public void testConnectedComponentsConstructor() {
 try {
 new ConnectedComponents(2, 1);
 fail("Expected InvalidArgumentException");
 } catch (InvalidArgumentException e) {
 // Expected
 }

 try {
 new ConnectedComponents(0, 2);
 fail("Expected InvalidArgumentException");
 } catch (InvalidArgumentException e) {
 // Expected
 }

 try {
 new ConnectedComponents(0, 1);
 ConnectedComponents cc = new ConnectedComponents(0, 1);
 assertEquals(0, cc.neighbors);
 assertEquals(1, cc.background);
 } catch (InvalidArgumentException e) {
 fail("Unexpected exception: " + e.getMessage());
 }

 try {
 new ConnectedComponents(1, 0);
 ConnectedComponents cc = new ConnectedComponents(1, 0);
 assertEquals(1, cc.neighbors);
 assertEquals(0, cc.background);
 } catch (InvalidArgumentException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
}

 @Test
 public void testConnectedComponentsApplyBasic() {
 BinaryImage input = createBinaryImage(3, 3, 0, 1, 1, 1, 1, 1, 1);
 ConnectedComponents cc = new ConnectedComponents(1, 0);
 GrayImage output = cc.apply(input);
 GrayImage expectedOutput = new GrayImage(3, 3);
 expectedOutput.set(0, 0, Integer.valueOf(1));
 expectedOutput.set(1, 0, Integer.valueOf(1));
 expectedOutput.set(2, 0, Integer.valueOf(1));
 expectedOutput.set(0, 1, Integer.valueOf(1));
 expectedOutput.set(1, 1, Integer.valueOf(2));
 expectedOutput.set(2, 1, Integer.valueOf(1));
 expectedOutput.set(0, 2, Integer.valueOf(1));
 expectedOutput.set(1, 2, Integer.valueOf(3));
 expectedOutput.set(2, 2, Integer.valueOf(1));
 assertArrayEquals(expectedOutput.getData(), output.getData());
}

 @Test
 public void testConnectedComponentsApplyEdgeCases() {
 // Test all pixels are background
 BinaryImage input = createBinaryImage(3, 3, 1, 1, 1, 1, 1, 1, 1);
 ConnectedComponents cc = new ConnectedComponents(1, 1);
 GrayImage output = cc.apply(input);
 assertTrue(output.get(0, 0).equals(Integer.valueOf(0)));
 assertTrue(output.get(1, 0).equals(Integer.valueOf(0)));
 assertTrue(output.get(2, 0).equals(Integer.valueOf(0)));
 assertTrue(output.get(0, 1).equals(Integer.valueOf(0)));
 assertTrue(output.get(1, 1).equals(Integer.valueOf(0)));
 assertTrue(output.get(2, 1).equals(Integer.valueOf(0)));
 assertTrue(output.get(0, 2).equals(Integer.valueOf(0)));
 assertTrue(output.get(1, 2).equals(Integer.valueOf(0)));
 assertTrue(output.get(2, 2).equals(Integer.valueOf(0)));

 // Test isolated pixel
 BinaryImage input2 = createBinaryImage(3, 3, 0, 1);
 ConnectedComponents cc2 = new ConnectedComponents(1, 0);
 GrayImage output2 = cc2.apply(input2);
 GrayImage expectedOutput2 = new GrayImage(3, 3);
 expectedOutput2.set(1, 1, Integer.valueOf(1));
 assertArrayEquals(expectedOutput2.getData(), output2.getData());

 // Test single connected component
 BinaryImage input3 = createBinaryImage(3, 3, 0, 1, 1, 1, 1, 1, 1, 1);
 ConnectedComponents cc3 = new ConnectedComponents(1, 0);
 GrayImage output3 = cc3.apply(input3);
 GrayImage expectedOutput3 = new GrayImage(3, 3);
 expectedOutput3.set(0, 0, Integer.valueOf(1));
 expectedOutput3.set(1, 0, Integer.valueOf(1));
 expectedOutput3.set(2, 0, Integer.valueOf(1));
 expectedOutput3.set(0, 1, Integer.valueOf(1));
 expectedOutput3.set(1, 1, Integer.valueOf(2));
 expectedOutput3.set(2, 1, Integer.valueOf(1));
 expectedOutput3.set(0, 2, Integer.valueOf(1));
 expectedOutput3.set(1, 2, Integer.valueOf(3));
 expectedOutput3.set(2, 2, Integer.valueOf(1));
 assertArrayEquals(expectedOutput3.getData(), output3.getData());

 // Test multiple connected components
 BinaryImage input4 = createBinaryImage(3, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
 ConnectedComponents cc4 = new ConnectedComponents(1, 0);
 GrayImage output4 = cc4.apply(input4);
 GrayImage expectedOutput4 = new GrayImage(3, 3);
 expectedOutput4.set(0, 0, Integer.valueOf(1));
 expectedOutput4.set(1, 0, Integer.valueOf(1));
 expectedOutput4.set(2, 0, Integer.valueOf(1));
 expectedOutput4.set(0, 1, Integer.valueOf(2));
 expectedOutput4.set(1, 1, Integer.valueOf(3));
 expectedOutput4.set(2, 1, Integer.valueOf(1));
 expectedOutput4.set(0, 2, Integer.valueOf(4));
 expectedOutput4.set(1, 2, Integer.valueOf(5));
 expectedOutput4.set(2, 2, Integer.valueOf(1));
 assertArrayEquals(expectedOutput4.getData(), output4.getData());
}

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsApplyInvalidArguments() {
 BinaryImage input = createBinaryImage(3, 3, 0);
 ConnectedComponents cc = new ConnectedComponents(2, 1);
 cc.apply(input);
 fail("Expected InvalidArgumentException");
}
}