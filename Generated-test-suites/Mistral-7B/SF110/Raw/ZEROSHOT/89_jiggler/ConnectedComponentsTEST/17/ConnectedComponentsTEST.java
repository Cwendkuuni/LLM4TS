package jigl.image.ops.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

 private BinaryImage createBinaryImage(int width, int height) {
 return new BinaryImage(width, height, new int[width][height]);
 }

 @Test
 public void testConnectedComponentsValidConstruction() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 }

 @Test
 public void testConnectedComponentsInvalidNeighborArgument() {
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 0));
 }

 @Test
 public void testConnectedComponentsInvalidBackgroundArgument() {
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, -1));
 }

 @Test
 public void testConnectedComponentsApplyZeroNeigborhood() {
 BinaryImage input = createBinaryImage(3, 3);
 input.set(1, 1, 1);
 input.set(1, 2, 1);
 input.set(2, 1, 1);
 input.set(2, 2, 1);

 ConnectedComponents cc = new ConnectedComponents(0, 0);
 GrayImage output = cc.apply(input);

 assertEquals(Integer.valueOf(1), output.get(1, 1));
 assertEquals(Integer.valueOf(1), output.get(1, 2));
 assertEquals(Integer.valueOf(1), output.get(2, 1));
 assertEquals(Integer.valueOf(1), output.get(2, 2));
 }

 @Test
 public void testConnectedComponentsApplyFourNeigborhood() {
 BinaryImage input = createBinaryImage(3, 3);
 input.set(1, 1, 1);
 input.set(1, 2, 1);
 input.set(2, 1, 1);
 input.set(2, 2, 1);
 input.set(2, 3, 1);
 input.set(3, 1, 1);
 input.set(3, 2, 1);
 input.set(3, 3, 1);

 ConnectedComponents cc = new ConnectedComponents(4, 0);
 GrayImage output = cc.apply(input);

 assertEquals(Integer.valueOf(1), output.get(1, 1));
 assertEquals(Integer.valueOf(1), output.get(1, 2));
 assertEquals(Integer.valueOf(1), output.get(2, 1));
 assertEquals(Integer.valueOf(1), output.get(2, 2));
 assertEquals(Integer.valueOf(1), output.get(2, 3));
 assertEquals(Integer.valueOf(1), output.get(3, 1));
 assertEquals(Integer.valueOf(1), output.get(3, 2));
 assertEquals(Integer.valueOf(1), output.get(3, 3));
 }

 @Test
 public void testConnectedComponentsApplyEightNeigborhood() {
 BinaryImage input = createBinaryImage(3, 3);
 input.set(1, 1, 1);
 input.set(1, 2, 1);
 input.set(2, 1, 1);
 input.set(2, 2, 1);
 input.set(2, 3, 1);
 input.set(3, 1, 1);
 input.set(3, 2, 1);
 input.set(3, 3, 1);
 input.set(4, 1, 1);
 input.set(4, 2, 1);
 input.set(4, 3, 1);
 input.set(5, 1, 1);
 input.set(5, 2, 1);
 input.set(5, 3, 1);

 ConnectedComponents cc = new ConnectedComponents(8, 0);
 GrayImage output = cc.apply(input);

 assertEquals(Integer.valueOf(1), output.get(1, 1));
 assertEquals(Integer.valueOf(1), output.get(1, 2));
 assertEquals(Integer.valueOf(1), output.get(2, 1));
 assertEquals(Integer.valueOf(1), output.get(2, 2));
 assertEquals(Integer.valueOf(1), output.get(2, 3));
 assertEquals(Integer.valueOf(1), output.get(3, 1));
 assertEquals(Integer.valueOf(1), output.get(3, 2));
 assertEquals(Integer.valueOf(1), output.get(3, 3));
 assertEquals(Integer.valueOf(1), output.get(4, 1));
 assertEquals(Integer.valueOf(1), output.get(4, 2));
 assertEquals(Integer.valueOf(1), output.get(4, 3));
 assertEquals(Integer.valueOf(1), output.get(5, 1));
 assertEquals(Integer.valueOf(1), output.get(5, 2));
 assertEquals(Integer.valueOf(1), output.get(5, 3));
 }

}