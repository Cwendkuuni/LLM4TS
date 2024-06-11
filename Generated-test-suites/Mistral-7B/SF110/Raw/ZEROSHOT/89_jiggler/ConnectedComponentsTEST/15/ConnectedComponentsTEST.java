package jigl.image.ops.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.RGBImage;
import jigl.image.types.types.GrayPixel;
import org.junit.Test;

public class ConnectedComponentsTest {

 private GrayImage createTestImage() {
 int width = 5;
 int height = 5;
 GrayImage image = new GrayImage(width, height);
 image.set(1, 1, GrayPixel.fromInt(1));
 image.set(1, 2, GrayPixel.fromInt(1));
 image.set(1, 3, GrayPixel.fromInt(1));
 image.set(2, 1, GrayPixel.fromInt(1));
 image.set(2, 2, GrayPixel.fromInt(1));
 image.set(2, 3, GrayPixel.fromInt(1));
 image.set(3, 1, GrayPixel.fromInt(1));
 image.set(3, 2, GrayPixel.fromInt(1));
 image.set(3, 3, GrayPixel.fromInt(1));
 image.set(4, 1, GrayPixel.fromInt(0));
 image.set(4, 2, GrayPixel.fromInt(0));
 image.set(4, 3, GrayPixel.fromInt(0));
 image.set(0, 0, GrayPixel.fromInt(0));
 image.set(0, 1, GrayPixel.fromInt(0));
 image.set(0, 2, GrayPixel.fromInt(0));
 image.set(0, 3, GrayPixel.fromInt(0));
 image.set(0, 4, GrayPixel.fromInt(0));
 image.set(1, 0, GrayPixel.fromInt(0));
 image.set(1, 4, GrayPixel.fromInt(0));
 image.set(2, 0, GrayPixel.fromInt(0));
 image.set(2, 4, GrayPixel.fromInt(0));
 image.set(3, 0, GrayPixel.fromInt(0));
 image.set(3, 4, GrayPixel.fromInt(0));
 image.set(4, 0, GrayPixel.fromInt(0));
 image.set(4, 4, GrayPixel.fromInt(0));
 return image;
 }

 @Test
 public void testValidConstruction() {
 ConnectedComponents component = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 assertNotNull(component);
 }

 @Test
 public void testInvalidConstructionWithInvalidNumberOfNeighbors() {
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(-1, 0));
 }

 @Test
 public void testInvalidConstructionWithInvalidBackgroundColor() {
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, -1));
 }

 @Test
 public void testApplyWithFourConnected() {
 ConnectedComponents component = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 GrayImage input = createTestImage();
 GrayImage output = component.apply(input);
 GrayImage expectedOutput = new GrayImage(input.X(), input.Y());
 expectedOutput.set(1, 1, GrayPixel.fromInt(1));
 expectedOutput.set(2, 1, GrayPixel.fromInt(1));
 expectedOutput.set(3, 1, GrayPixel.fromInt(1));
 expectedOutput.set(1, 2, GrayPixel.fromInt(1));
 expectedOutput.set(2, 2, GrayPixel.fromInt(1));
 expectedOutput.set(3, 2, GrayPixel.fromInt(1));
 expectedOutput.set(1, 3, GrayPixel.fromInt(1));
 expectedOutput.set(2, 3, GrayPixel.fromInt(1));
 expectedOutput.set(3, 3, GrayPixel.fromInt(1));
 expectedOutput.set(1, 4, GrayPixel.fromInt(0));
 expectedOutput.set(2, 4, GrayPixel.fromInt(0));
 expectedOutput.set(3, 4, GrayPixel.fromInt(0));
 expectedOutput.set(4, 4, GrayPixel.fromInt(0));
 expectedOutput.set(4, 3, GrayPixel.fromInt(1));
 expectedOutput.set(4, 2, GrayPixel.fromInt(1));
 expectedOutput.set(4, 1, GrayPixel.fromInt(1));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testApplyWithEightConnected() {
 ConnectedComponents component = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
 GrayImage input = createTestImage();
 GrayImage output = component.apply(input);
 GrayImage expectedOutput = new GrayImage(input.X(), input.Y());
 expectedOutput.set(1, 1, GrayPixel.fromInt(1));
 expectedOutput.set(2, 1, GrayPixel.fromInt(1));
 expectedOutput.set(3, 1, GrayPixel.fromInt(1));
 expectedOutput.set(1, 2, GrayPixel.fromInt(1));
 expectedOutput.set(2, 2, GrayPixel.fromInt(1));
 expectedOutput.set(3, 2, GrayPixel.fromInt(1));
 expectedOutput.set(1, 3, GrayPixel.fromInt(1));
 expectedOutput.set(2, 3, GrayPixel.fromInt(1));
 expectedOutput.set(3, 3, GrayPixel.fromInt(1));
 expectedOutput.set(1, 4, GrayPixel.fromInt(0));
 expectedOutput.set(2, 4, GrayPixel.fromInt(0));
 expectedOutput.set(3, 4, GrayPixel.fromInt(0));
 expectedOutput.set(4, 4, GrayPixel.fromInt(0));
 expectedOutput.set(4, 3, GrayPixel.fromInt(1));
 expectedOutput.set(4, 2, GrayPixel.fromInt(1));
 expectedOutput.set(4, 1, GrayPixel.fromInt(1));
 expectedOutput.set(3, 4, GrayPixel.fromInt(1));
 expectedOutput.set(2, 4, GrayPixel.fromInt(1));
 expectedOutput.set(3, 3, GrayPixel.fromInt(1));
 expectedOutput.set(2, 3, GrayPixel.fromInt(1));
 expectedOutput.set(3, 2, GrayPixel.fromInt(1));
 expectedOutput.set(2, 2, GrayPixel.fromInt(1));
 expectedOutput.set(3, 1, GrayPixel.fromInt(1));
 expectedOutput.set(2, 1, GrayPixel.fromInt(1));
 expectedOutput.set(3, 0, GrayPixel.fromInt(1));
 expectedOutput.set(2, 0, GrayPixel.fromInt(1));
 expectedOutput.set(1, 0, GrayPixel.fromInt(1));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEmptyImage() {
 ConnectedComponents component = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 GrayImage input = new GrayImage(0, 0);
 GrayImage output = component.apply(input);
 assertTrue(output.isEmpty());
 }

 @Test
 public void testApplyWithBackgroundImage() {
 ConnectedComponents component = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 GrayImage input = new GrayImage(5, 5);
 for (int x = 0; x < input.X(); ++x) {
 for (int y = 0; y < input.Y(); ++y) {
 input.set(x, y, GrayPixel.fromInt(0));
 }
 }
 GrayImage output = component.apply(input);
 assertFalse(output.isEmpty());
 for (int x = 0; x < output.X(); ++x) {
 for (int y = 0; y < output.Y(); ++y) {
 assertTrue(output.get(x, y).getGrayValue() > 0);
 }
 }
 }

 @Test
 public void testApplyWithEmptyComponent() {
 ConnectedComponents component = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 BinaryImage input = new BinaryImage(new RGBImage(5, 5, new GrayPixel[25]));
 GrayImage output = component.apply(new SimpleOperator() {
 @Override
 public GrayImage apply(BinaryImage image) {
 return image;
 }
 }.apply(input));
 assertTrue(output.isEmpty());
 }

}