package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import org.junit.Test;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;

 @Test
 public void testConnectedComponentsConstructor() {
 cc = new ConnectedComponents(0, 0);
 assertNotNull(cc);
 cc = new ConnectedComponents(1, 1);
 assertNotNull(cc);
 cc = new ConnectedComponents(2, 1);
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 1));
 cc = new ConnectedComponents(1, 2);
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(1, 2));
 }

 @Test
 public void testApply_BinaryImage_GrayImage_FOUR_CONNECTED() {
 BinaryImage binaryImage = new BinaryImage(5, 5, new byte[25]);
 binaryImage.set(2, 2, (byte) 1);
 binaryImage.set(2, 3, (byte) 1);
 binaryImage.set(3, 2, (byte) 1);
 binaryImage.set(3, 3, (byte) 1);
 cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 GrayImage result = cc.apply(binaryImage);
 int[] expected = {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals(expected[i], result.get(i / result.X(), i % result.X()));
 }
 }

 @Test
 public void testApply_BinaryImage_GrayImage_EIGHT_CONNECTED() {
 BinaryImage binaryImage = new BinaryImage(5, 5, new byte[25]);
 binaryImage.set(2, 2, (byte) 1);
 binaryImage.set(2, 3, (byte) 1);
 binaryImage.set(3, 2, (byte) 1);
 binaryImage.set(3, 3, (byte) 1);
 binaryImage.set(3, 4, (byte) 1);
 binaryImage.set(4, 3, (byte) 1);
 binaryImage.set(4, 4, (byte) 1);
 cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
 GrayImage result = cc.apply(binaryImage);
 int[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals(expected[i], result.get(i / result.X(), i % result.X()));
 }
 }

 @Test
 public void testApply_BinaryImage_GrayImage_InvalidBackground() {
 BinaryImage binaryImage = new BinaryImage(5, 5, new byte[25]);
 cc = new ConnectedComponents(-1, 0);
 assertThrows(InvalidArgumentException.class, () -> cc.apply(binaryImage));
 }

 @Test
 public void testApply_BinaryImage_GrayImage_InvalidNumberNeighbors() {
 BinaryImage binaryImage = new BinaryImage(5, 5, new byte[25]);
 cc = new ConnectedComponents(2, 0);
 assertThrows(InvalidArgumentException.class, () -> cc.apply(binaryImage));
 }

 @Test
 public void testApply_NullBinaryImage() {
 assertThrows(NullPointerException.class, () -> cc.apply(null));
 }

 @Test
 public void testApply_BinaryImage_GrayImage_EmptyImage() {
 BinaryImage binaryImage = new BinaryImage(0, 0);
 GrayImage result = cc.apply(binaryImage);
 assertEquals(0, result.X());
 assertEquals(0, result.Y());
 }

 @Test
 public void testApply_BinaryImage_GrayImage_BorderCases() {
 BinaryImage binaryImage = new BinaryImage(5, 5, new byte[25]);
 binaryImage.set(0, 0, (byte) 1);
 binaryImage.set(0, 1, (byte) 1);
 binaryImage.set(0, 4, (byte) 1);
 binaryImage.set(0, 5, (byte) 1);
 binaryImage.set(1, 0, (byte) 1);
 binaryImage.set(1, 4, (byte) 1);
 binaryImage.set(1, 5, (byte) 1);
 binaryImage.set(4, 0, (byte) 1);
 binaryImage.set(4, 1, (byte) 1);
 binaryImage.set(4, 4, (byte) 1);
 binaryImage.set(4, 5, (byte) 1);
 binaryImage.set(5, 0, (byte) 1);
 binaryImage.set(5, 1, (byte) 1);
 binaryImage.set(5, 4, (byte) 1);
 binaryImage.set(5, 5, (byte) 1);
 cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 GrayImage result = cc.apply(binaryImage);
 int[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals(expected[i], result.get(i / result.X(), i % result.X()));
 }
 }

 @Test
 public void testApply_BinaryImage_GrayImage_MultipleComponents() {
 BinaryImage binaryImage = new BinaryImage(7, 7, new byte[49]);
 binaryImage.set(1, 1, (byte) 1);
 binaryImage.set(1, 2, (byte) 1);
 binaryImage.set(1, 3, (byte) 1);
 binaryImage.set(1, 4, (byte) 1);
 binaryImage.set(1, 5, (byte) 1);
 binaryImage.set(1, 6, (byte) 1);
 binaryImage.set(1, 7, (byte) 1);
 binaryImage.set(2, 1, (byte) 1);
 binaryImage.set(2, 2, (byte) 1);
 binaryImage.set(2, 3, (byte) 1);
 binaryImage.set(2, 4, (byte) 1);
 binaryImage.set(2, 5, (byte) 1);
 binaryImage.set(2, 6, (byte) 1);
 binaryImage.set(2, 7, (byte) 1);
 binaryImage.set(3, 1, (byte) 1);
 binaryImage.set(3, 2, (byte) 1);
 binaryImage.set(3, 3, (byte) 1);
 binaryImage.set(3, 4, (byte) 1);
 binaryImage.set(3, 5, (byte) 1);
 binaryImage.set(3, 6, (byte) 1);
 binaryImage.set(3, 7, (byte) 1);
 binaryImage.set(4, 1, (byte) 1);
 binaryImage.set(4, 2, (byte) 1);
 binaryImage.set(4, 3, (byte) 1);
 binaryImage.set(4, 4, (byte) 1);
 binaryImage.set(4, 5, (byte) 1);
 binaryImage.set(4, 6, (byte) 1);
 binaryImage.set(4, 7, (byte) 1);
 binaryImage.set(5, 1, (byte) 1);
 binaryImage.set(5, 2, (byte) 1);
 binaryImage.set(5, 3, (byte) 1);
 binaryImage.set(5, 4, (byte) 1);
 binaryImage.set(5, 5, (byte) 1);
 binaryImage.set(5, 6, (byte) 1);
 binaryImage.set(5, 7, (byte) 1);
 binaryImage.set(6, 1, (byte) 1);
 binaryImage.set(6, 2, (byte) 1);
 binaryImage.set(6, 3, (byte) 1);
 binaryImage.set(6, 4, (byte) 1);
 binaryImage.set(6, 5, (byte) 1);
 binaryImage.set(6, 6, (byte) 1);
 binaryImage.set(6, 7, (byte) 1);
 binaryImage.set(7, 1, (byte) 1);
 binaryImage.set(7, 2, (byte) 1);
 binaryImage.set(7, 3, (byte) 1);
 binaryImage.set(7, 4, (byte) 1);
 binaryImage.set(7, 5, (byte) 1);
 binaryImage.set(7, 6, (byte) 1);
 binaryImage.set(7, 7, (byte) 1);
 cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
 GrayImage result = cc.apply(binaryImage);
 int[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
 for (int i = 0; i < result.X() * result.Y(); ++i) {
 assertEquals(expected[i], result.get(i / result.X(), i % result.X()));
 }
 }

}