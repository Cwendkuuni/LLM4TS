package jigl.image.ops.test;

import jigl.image.types.GrayImage;
import jigl.image.ops.ConnectedComponents;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ConnectedComponentsTest {

 private ConnectedComponents component;

 @Test
 public void testConnectedComponentsConstructorValidArguments() {
 component = new ConnectedComponents(0, 0);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsConstructorInvalidArguments() {
 component = new ConnectedComponents(2, 5);
 }

 @Test
 public void testApplyMethodValidImage() throws Exception {
 BinaryImage image = new BinaryImage(3, 3);
 image.set(0, 0, 1);
 image.set(1, 1, 1);
 image.set(2, 2, 1);
 component = new ConnectedComponents(0, 0);
 GrayImage result = component.apply(image);
 int[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals("Pixel value does not match expected", expected[i % result.X()], result.get(i / result.X(), i % result.X()));
 }
 }

 @Test
 public void testApplyMethodEmptyImage() {
 BinaryImage emptyImage = new BinaryImage(0, 0);
 component = new ConnectedComponents(0, 0);
 GrayImage result = component.apply(emptyImage);
 assertEquals("Expected empty image result, but got " + result.X() + " x " + result.Y() + " pixels", 0, result.X());
 assertEquals("Expected empty image result, but got " + result.X() + " x " + result.Y() + " pixels", 0, result.Y());
 }

 @Test
 public void testApplyMethodBackgroundValue() throws Exception {
 BinaryImage image = new BinaryImage(3, 3);
 image.setBackground(0);
 component = new ConnectedComponents(0, 0);
 GrayImage result = component.apply(image);
 int[] expected = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals("Pixel value does not match expected", expected[i % result.X()], result.get(i / result.X(), i % result.X()));
 }
 }

 @Test
 public void testApplyMethodSinglePixel() throws Exception {
 BinaryImage image = new BinaryImage(1, 1);
 image.set(0, 0, 1);
 component = new ConnectedComponents(0, 0);
 GrayImage result = component.apply(image);
 int[] expected = {1};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals("Pixel value does not match expected", expected[i % result.X()], result.get(i / result.X(), i % result.X()));
 }
 }

 @Test
 public void testApplyMethodMultipleConnectedComponents() throws Exception {
 BinaryImage image = new BinaryImage(4, 4);
 image.set(0, 0, 1);
 image.set(1, 0, 1);
 image.set(0, 1, 1);
 image.set(1, 1, 1);
 image.set(2, 1, 1);
 image.set(3, 1, 1);
 image.set(0, 2, 1);
 image.set(1, 2, 1);
 image.set(2, 2, 1);
 image.set(3, 2, 1);
 image.set(0, 3, 1);
 image.set(1, 3, 1);
 image.set(2, 3, 1);
 image.set(3, 3, 1);
 component = new ConnectedComponents(0, 0);
 GrayImage result = component.apply(image);
 int[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals("Pixel value does not match expected", expected[i % result.X()], result.get(i / result.X(), i % result.X()));
 }
 }

 @Test
 public void testApplyMethodDisconnectedComponents() throws Exception {
 BinaryImage image = new BinaryImage(4, 4);
 image.set(0, 0, 1);
 image.set(1, 1, 1);
 image.set(2, 1, 1);
 image.set(3, 1, 1);
 image.set(0, 2, 1);
 image.set(1, 2, 1);
 image.set(2, 2, 1);
 image.set(3, 2, 1);
 image.set(0, 3, 1);
 image.set(1, 3, 1);
 image.set(2, 3, 1);
 image.set(3, 3, 0);
 component = new ConnectedComponents(0, 0);
 GrayImage result = component.apply(image);
 int[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals("Pixel value does not match expected", expected[i % result.X()], result.get(i / result.X(), i % result.X()));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyMethodNullImage() {
 component = new ConnectedComponents(0, 0);
 assertThrows("Exception should be thrown when applying method to null image", IllegalArgumentException.class, () -> component.apply(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyMethodNegativeSizeImage() {
 component = new ConnectedComponents(0, 0);
 BinaryImage negativeSizeImage = new BinaryImage(-1, -1);
 assertThrows("Exception should be thrown when applying method to negative size image", IllegalArgumentException.class, () -> component.apply(negativeSizeImage));
 }
}