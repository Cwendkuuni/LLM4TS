package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.Pixel;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponents() {
 BinaryImage binaryImage = new BinaryImage(2, 2);
 binaryImage.setPixel(new Pixel(0, 0, 1));
 binaryImage.setPixel(new Pixel(0, 1, 1));
 binaryImage.setPixel(new Pixel(1, 0, 1));
 binaryImage.setPixel(new Pixel(1, 1, 1));

 ConnectedComponents cc = null;
 try {
 cc = new ConnectedComponents(4, 0);
 GrayImage expected = new GrayImage(2, 2);
 expected.setPixel(new Pixel(0, 0, 1));
 expected.setPixel(new Pixel(0, 1, 1));
 expected.setPixel(new Pixel(1, 0, 1));
 expected.setPixel(new Pixel(1, 1, 2));
 assertEquals(expected, cc.apply(binaryImage));
 } catch (InvalidArgumentException e) {
 fail("InvalidArgumentException expected but not thrown.");
 }

 try {
 cc = new ConnectedComponents(4, 1);
 GrayImage expected = new GrayImage(2, 2);
 expected.setPixel(new Pixel(0, 0, 1));
 expected.setPixel(new Pixel(0, 1, 1));
 expected.setPixel(new Pixel(1, 0, 1));
 expected.setPixel(new Pixel(1, 1, 1));
 assertEquals(expected, cc.apply(binaryImage));
 fail("Invalid argument should not produce the same output.");
 } catch (InvalidArgumentException e) {
 // expected
 }

 try {
 cc = new ConnectedComponents(0, 0);
 fail("InvalidArgumentException expected but not thrown.");
 } catch (InvalidArgumentException e) {
 // expected
 }

 try {
 cc = new ConnectedComponents(0, 1);
 fail("InvalidArgumentException expected but not thrown.");
 } catch (InvalidArgumentException e) {
 // expected
 }

 try {
 cc = new ConnectedComponents(1, 1);
 fail("InvalidArgumentException expected but not thrown.");
 } catch (InvalidArgumentException e) {
 // expected
 }

 try {
 cc = new ConnectedComponents(4, 2);
 fail("InvalidArgumentException expected but not thrown.");
 } catch (InvalidArgumentException e) {
 // expected
 }

 try {
 cc = new ConnectedComponents(2, 1);
 GrayImage expected = new GrayImage(2, 2);
 expected.setPixel(new Pixel(0, 0, 1));
 expected.setPixel(new Pixel(0, 1, 2));
 expected.setPixel(new Pixel(1, 0, 1));
 expected.setPixel(new Pixel(1, 1, 1));
 assertEquals(expected, cc.apply(binaryImage));
 } catch (InvalidArgumentException e) {
 fail("InvalidArgumentException not expected.");
 }
}

 @Test
 public void testConnectedComponentsWithBackground() {
 BinaryImage binaryImage = new BinaryImage(3, 3);
 binaryImage.setPixel(new Pixel(1, 1, 1));
 binaryImage.setPixel(new Pixel(2, 1, 1));
 binaryImage.setPixel(new Pixel(1, 2, 0));
 binaryImage.setPixel(new Pixel(2, 2, 0));
 binaryImage.setPixel(new Pixel(1, 3, 1));
 binaryImage.setPixel(new Pixel(2, 3, 1));

 ConnectedComponents cc = null;
 try {
 cc = new ConnectedComponents(4, 1);
 GrayImage expected = new GrayImage(3, 3);
 expected.setPixel(new Pixel(1, 1, 1));
 expected.setPixel(new Pixel(2, 1, 1));
 expected.setPixel(new Pixel(1, 3, 2));
 expected.setPixel(new Pixel(2, 3, 2));
 expected.setPixel(new Pixel(1, 2, 0));
 expected.setPixel(new Pixel(2, 2, 0));
 assertEquals(expected, cc.apply(binaryImage));
 } catch (InvalidArgumentException e) {
 fail("InvalidArgumentException expected but not thrown.");
 }
}
}