package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;

 @org.junit.Before
 public void setUp() {
 cc = new ConnectedComponents(0, 0);
 }

 // Test constructor with valid arguments
 @org.junit.Test
 public void testConnectedComponentsValidArgs() throws InvalidArgumentException {
 GrayImage image = new GrayImage(5, 5);
 claimEquals(new ConnectedComponents(2, 0), new ConnectedComponents(2, 0));
 claimEquals(new ConnectedComponents(0, 1), new ConnectedComponents(0, 1));
 claimThrowsException(new InvalidArgumentException(), () -> new ConnectedComponents(-1, 1));
 claimThrowsException(new InvalidArgumentException(), () -> new ConnectedComponents(1, -1));
 claimThrowsException(new InvalidArgumentException(), () -> new ConnectedComponents(-1, -1));
 claimThrowsException(new InvalidArgumentException(), () -> new ConnectedComponents(3, 3));

 GrayImage result = cc.apply(image);
 assertEquals(result.X(), image.X());
 assertEquals(result.Y(), image.Y());
 }

 // Test constructor with invalid arguments
 @org.junit.Test
 public void testConnectedComponentsInvalidArgs() {
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(-1, 1));
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(1, -1));
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(-1, -1));
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(3, 3));
 }

 // Test apply method with empty image
 @org.junit.Test
 public void testApplyEmptyImage() {
 GrayImage image = new GrayImage(0, 0);
 GrayImage result = cc.apply(image);
 assertEquals(result.X(), 0);
 assertEquals(result.Y(), 0);
 }

 // Test apply method with single connected component
 @org.junit.Test
 public void testApplySingleComponent() {
 GrayImage image = new GrayImage(1, 1);
 image.set(0, 0, 1);
 GrayImage result = cc.apply(image);
 assertEquals(result.get(0, 0), 1);
 }

 // Test apply method with multiple connected components (4-connected)
 @org.junit.Test
 public void testApplyMultipleComponents4Connected() {
 GrayImage image = new GrayImage(4, 3);
 image.set(0, 0, 1);
 image.set(1, 1, 1);
 image.set(2, 0, 1);
 image.set(2, 1, 1);
 image.set(2, 2, 1);
 image.set(3, 1, 1);
 image.set(3, 2, 1);

 GrayImage result = cc.apply(image);
 int[] expected = {1, 2, 2, 2, 2, 2, 3, 3, 3, 3};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 assertEquals(expected[x * result.Y() + y], result.get(x, y).intValue());
 }
 }
 }

 // Test apply method with multiple connected components (8-connected)
 @org.junit.Test
 public void testApplyMultipleComponents8Connected() {
 GrayImage image = new GrayImage(4, 3);
 image.set(0, 0, 1);
 image.set(0, 1, 1);
 image.set(0, 2, 1);
 image.set(1, 0, 1);
 image.set(1, 1, 1);
 image.set(1, 2, 1);
 image.set(2, 0, 1);
 image.set(2, 1, 1);
 image.set(2, 2, 1);
 image.set(3, 0, 1);
 image.set(3, 1, 1);
 image.set(3, 2, 1);

 GrayImage result = cc.apply(image, 1);
 int[] expected = {1, 1, 1, 1, 1, 1, 2, 2, 2, 2};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 assertEquals(expected[x * result.Y() + y], result.get(x, y).intValue());
 }
 }
 }
}