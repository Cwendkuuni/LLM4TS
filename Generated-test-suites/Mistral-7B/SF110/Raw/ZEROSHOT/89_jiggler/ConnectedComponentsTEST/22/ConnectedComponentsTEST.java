package jigl.image.ops.test;

import jigl.image.types.GrayImage;
import jigl.image.ops.ConnectedComponents;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsConstructor() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 ConnectedComponents cc1 = new ConnectedComponents(1, 1);
 ConnectedComponents exception = null;
 try {
 ConnectedComponents cc2 = new ConnectedComponents(2, 1);
 } catch (ConnectedComponents.InvalidArgumentException e) {
 exception = e;
 }

 assertTrue(cc != null);
 assertTrue(cc1 != null);
 assertNotNull(exception);
 }

 @Test
 public void testConnectedComponentsApplyWith0Neighbors() {
 BinaryImage input = new BinaryImage(3, 3);
 input.set(1, 1, 1);
 input.set(1, 2, 1);
 input.set(2, 1, 1);
 input.set(2, 2, 1);
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 GrayImage output = cc.apply(input);

 assertEquals(2, output.X());
 assertEquals(2, output.Y());
 assertEquals(Integer.valueOf(1), output.get(1, 1));
 assertEquals(Integer.valueOf(1), output.get(1, 2));
 assertEquals(Integer.valueOf(1), output.get(2, 1));
 assertEquals(Integer.valueOf(1), output.get(2, 2));
 }

 @Test
 public void testConnectedComponentsApplyWith1Neighbors() {
 BinaryImage input = new BinaryImage(3, 3);
 input.set(1, 1, 1);
 input.set(1, 2, 1);
 input.set(2, 1, 1);
 input.set(2, 2, 1);
 input.set(2, 3, 1);
 input.set(3, 1, 1);
 input.set(3, 2, 1);
 input.set(3, 3, 1);
 ConnectedComponents cc = new ConnectedComponents(1, 0);
 GrayImage output = cc.apply(input);

 assertEquals(2, output.X());
 assertEquals(2, output.Y());
 assertEquals(Integer.valueOf(1), output.get(1, 1));
 assertEquals(Integer.valueOf(1), output.get(1, 2));
 assertEquals(Integer.valueOf(1), output.get(2, 1));
 assertEquals(Integer.valueOf(1), output.get(2, 2));
 }

 @Test
 public void testConnectedComponentsApplyWithInvalidArguments() {
 BinaryImage input = new BinaryImage(3, 3);
 input.set(1, 1, 1);
 input.set(1, 2, 1);
 input.set(2, 1, 1);
 input.set(2, 2, 1);
 ConnectedComponents exception = null;
 try {
 ConnectedComponents cc = new ConnectedComponents(3, 0);
 } catch (ConnectedComponents.InvalidArgumentException e) {
 exception = e;
 }

 assertNotNull(exception);
 }
}