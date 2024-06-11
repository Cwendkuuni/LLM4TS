package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.types.Pixel;
import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsConstructor() {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 ConnectedComponents cc1 = new ConnectedComponents(0, 1);
 ConnectedComponents cc2 = new ConnectedComponents(1, 0);
 ConnectedComponents cc3 = new ConnectedComponents(1, 1);

 assertEquals("Invalid number of neighbors", 0, cc.getNeighbors());
 assertEquals("Invalid number of neighbors", 0, cc1.getNeighbors());
 assertEquals("Invalid number of neighbors", 1, cc2.getNeighbors());
 assertEquals("Invalid number of neighbors", 1, cc3.getNeighbors());

 assertEquals("Invalid background color", 0, cc.getBackground());
 assertEquals("Invalid background color", 1, cc1.getBackground());
 assertEquals("Invalid background color", 0, cc2.getBackground());
 assertEquals("Invalid background color", 1, cc3.getBackground());
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsInvalidConstructor() {
 new ConnectedComponents(2, 0);
 }

 @Test
 public void testConnectedComponentsApply() {
 BinaryImage image = new BinaryImage(2, 2);
 image.set(0, 0, new Pixel(1));
 image.set(0, 1, new Pixel(1));
 image.set(1, 0, new Pixel(1));
 image.set(1, 1, new Pixel(1));
 image.set(1, 2, new Pixel(1));
 image.set(2, 0, new Pixel(0));
 image.set(2, 1, new Pixel(0));
 image.set(2, 2, new Pixel(0));

 ConnectedComponents cc = new ConnectedComponents(0, 0);
 GrayImage expectedImage = new GrayImage(2, 2);
 expectedImage.set(0, 0, new Pixel(1));
 expectedImage.set(0, 1, new Pixel(1));
 expectedImage.set(1, 0, new Pixel(1));
 expectedImage.set(1, 1, new Pixel(1));
 expectedImage.set(1, 2, new Pixel(1));
 expectedImage.set(2, 0, new Pixel(0));
 expectedImage.set(2, 1, new Pixel(0));
 expectedImage.set(2, 2, new Pixel(0));

 GrayImage actualImage = cc.apply(image);

 assertEquals("Image dimensions do not match expected image", 2, actualImage.X());
 assertEquals("Image dimensions do not match expected image", 2, actualImage.Y());

 for (int x = 0; x < actualImage.X(); ++x) {
 for (int y = 0; y < actualImage.Y(); ++y) {
 assertEquals("Pixel values do not match expected image", expectedImage.get(x, y).getValue(), actualImage.get(x, y).getValue());
 }
 }
 }

 @Test
 public void testConnectedComponentsApplyDifferentNeighbors() {
 BinaryImage image = new BinaryImage(2, 2);
 image.set(0, 0, new Pixel(1));
 image.set(0, 1, new Pixel(1));
 image.set(1, 0, new Pixel(1));
 image.set(1, 1, new Pixel(1));
 image.set(1, 2, new Pixel(1));
 image.set(2, 0, new Pixel(0));
 image.set(2, 1, new Pixel(0));
 image.set(2, 2, new Pixel(0));

 ConnectedComponents cc0 = new ConnectedComponents(0, 0);
 ConnectedComponents cc1 = new ConnectedComponents(1, 0);

 GrayImage expectedImage0 = new GrayImage(2, 2);
 expectedImage0.set(0, 0, new Pixel(1));
 expectedImage0.set(0, 1, new Pixel(1));
 expectedImage0.set(1, 0, new Pixel(1));
 expectedImage0.set(1, 1, new Pixel(1));
 expectedImage0.set(1, 2, new Pixel(1));
 expectedImage0.set(2, 0, new Pixel(0));
 expectedImage0.set(2, 1, new Pixel(0));
 expectedImage0.set(2, 2, new Pixel(0));

 GrayImage expectedImage1 = new GrayImage(2, 2);
 expectedImage1.set(0, 0, new Pixel(1));
 expectedImage1.set(0, 1, new Pixel(1));
 expectedImage1.set(1, 0, new Pixel(1));
 expectedImage1.set(1, 1, new Pixel(1));
 expectedImage1.set(1, 2, new Pixel(1));
 expectedImage1.set(2, 0, new Pixel(1));
 expectedImage1.set(2, 1, new Pixel(1));
 expectedImage1.set(2, 2, new Pixel(1));

 GrayImage actualImage0 = cc0.apply(image);
 GrayImage actualImage1 = cc1.apply(image);

 assertEquals("Image dimensions do not match expected image 0", 2, actualImage0.X());
 assertEquals("Image dimensions do not match expected image 0", 2, actualImage0.Y());
 assertEquals("Image dimensions do not match expected image 1", 2, actualImage1.X());
 assertEquals("Image dimensions do not match expected image 1", 2, actualImage1.Y());

 for (int x = 0; x < actualImage0.X(); ++x) {
 for (int y = 0; y < actualImage0.Y(); ++y) {
 assertEquals("Pixel values do not match expected image 0", expectedImage0.get(x, y).getValue(), actualImage0.get(x, y).getValue());
 }
 }

 for (int x = 0; x < actualImage1.X(); ++x) {
 for (int y = 0; y < actualImage1.Y(); ++y) {
 assertEquals("Pixel values do not match expected image 1", expectedImage1.get(x, y).getValue(), actualImage1.get(x, y).getValue());
 }
 }
 }
}