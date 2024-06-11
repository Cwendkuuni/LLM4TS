package jigl.image.ops.test;

import jigl.image.types.GrayImage;
import jigl.image.ops.ConnectedComponents;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsWithFourConnectedAndValidBackground() {
 BinaryImage binaryImage = new BinaryImage(new int[][]{
 { 1, 1, 1, 1, 1 },
 { 1, 0, 0, 0, 1 },
 { 1, 0, 1, 0, 1 },
 { 1, 0, 1, 0, 1 },
 { 1, 1, 1, 1, 1 }
 });

 ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 GrayImage result = cc.apply(binaryImage);

 GrayImage expected = new GrayImage(binaryImage.X(), binaryImage.Y());
 expected.set(0, 0, Integer.valueOf(1));
 expected.set(1, 0, Integer.valueOf(1));
 expected.set(1, 1, Integer.valueOf(1));
 expected.set(2, 1, Integer.valueOf(1));
 expected.set(3, 1, Integer.valueOf(1));
 expected.set(4, 1, Integer.valueOf(1));
 expected.set(5, 1, Integer.valueOf(1));
 expected.set(0, 2, Integer.valueOf(1));
 expected.set(1, 2, Integer.valueOf(1));
 expected.set(2, 2, Integer.valueOf(1));
 expected.set(3, 2, Integer.valueOf(1));
 expected.set(4, 2, Integer.valueOf(1));
 expected.set(5, 2, Integer.valueOf(1));
 expected.set(1, 3, Integer.valueOf(2));
 expected.set(2, 3, Integer.valueOf(2));
 expected.set(3, 3, Integer.valueOf(2));
 expected.set(4, 3, Integer.valueOf(2));
 expected.set(5, 3, Integer.valueOf(2));
 expected.set(0, 4, Integer.valueOf(1));
 expected.set(1, 4, Integer.valueOf(1));
 expected.set(2, 4, Integer.valueOf(1));
 expected.set(3, 4, Integer.valueOf(1));
 expected.set(4, 4, Integer.valueOf(1));
 expected.set(5, 4, Integer.valueOf(1));

 for (int x = 0; x < result.X(); x++) {
 for (int y = 0; y < result.Y(); y++) {
 assertEquals(expected.get(x, y), result.get(x, y));
 }
 }
 }

 @Test
 public void testConnectedComponentsWithFourConnectedAndInvalidBackground() {
 BinaryImage binaryImage = new BinaryImage(new int[][]{
 { 1, 1, 1, 1, 1 },
 { 1, 0, 0, 0, 1 },
 { 1, 0, 1, 0, 1 },
 { 1, 0, 1, 0, 1 },
 { 1, 1, 1, 1, 1 }
 });

 try {
 ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
 fail("Expected InvalidArgumentException");
 } catch (InvalidArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testConnectedComponentsWithEightConnectedAndValidBackground() {
 BinaryImage binaryImage = new BinaryImage(new int[][]{
 { 1, 1, 1, 1, 1 },
 { 1, 0, 0, 0, 1 },
 { 1, 0, 1, 0, 1 },
 { 1, 0, 1, 0, 1 },
 { 1, 1, 1, 1, 1 }
 });

 ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
 GrayImage result = cc.apply(binaryImage);

 GrayImage expected = new GrayImage(binaryImage.X(), binaryImage.Y());
 expected.set(0, 0, Integer.valueOf(1));
 expected.set(1, 0, Integer.valueOf(1));
 expected.set(1, 1, Integer.valueOf(1));
 expected.set(2, 1, Integer.valueOf(1));
 expected.set(3, 1, Integer.valueOf(1));
 expected.set(4, 1, Integer.valueOf(1));
 expected.set(5, 1, Integer.valueOf(1));
 expected.set(6, 1, Integer.valueOf(1));
 expected.set(7, 1, Integer.valueOf(1));
 expected.set(0, 2, Integer.valueOf(1));
 expected.set(1, 2, Integer.valueOf(1));
 expected.set(2, 2, Integer.valueOf(1));
 expected.set(3, 2, Integer.valueOf(1));
 expected.set(4, 2, Integer.valueOf(1));
 expected.set(5, 2, Integer.valueOf(1));
 expected.set(6, 2, Integer.valueOf(1));
 expected.set(7, 2, Integer.valueOf(1));
 expected.set(1, 3, Integer.valueOf(2));
 expected.set(2, 3, Integer.valueOf(2));
 expected.set(3, 3, Integer.valueOf(2));
 expected.set(4, 3, Integer.valueOf(2));
 expected.set(5, 3, Integer.valueOf(2));
 expected.set(6, 3, Integer.valueOf(2));
 expected.set(7, 3, Integer.valueOf(2));
 expected.set(0, 4, Integer.valueOf(1));
 expected.set(1, 4, Integer.valueOf(1));
 expected.set(2, 4, Integer.valueOf(1));
 expected.set(3, 4, Integer.valueOf(1));
 expected.set(4, 4, Integer.valueOf(1));
 expected.set(5, 4, Integer.valueOf(1));
 expected.set(6, 4, Integer.valueOf(1));
 expected.set(7, 4, Integer.valueOf(1));
 expected.set(1, 5, Integer.valueOf(2));
 expected.set(2, 5, Integer.valueOf(2));
 expected.set(3, 5, Integer.valueOf(2));
 expected.set(4, 5, Integer.valueOf(2));
 expected.set(5, 5, Integer.valueOf(2));
 expected.set(6, 5, Integer.valueOf(2));
 expected.set(7, 5, Integer.valueOf(2));
 expected.set(0, 6, Integer.valueOf(1));
 expected.set(1, 6, Integer.valueOf(1));
 expected.set(2, 6, Integer.valueOf(1));
 expected.set(3, 6, Integer.valueOf(1));
 expected.set(4, 6, Integer.valueOf(1));
 expected.set(5, 6, Integer.valueOf(1));
 expected.set(6, 6, Integer.valueOf(1));
 expected.set(7, 6, Integer.valueOf(1));
 expected.set(1, 7, Integer.valueOf(2));
 expected.set(2, 7, Integer.valueOf(2));
 expected.set(3, 7, Integer.valueOf(2));
 expected.set(4, 7, Integer.valueOf(2));
 expected.set(5, 7, Integer.valueOf(2));
 expected.set(6, 7, Integer.valueOf(2));
 expected.set(7, 7, Integer.valueOf(2));

 for (int x = 0; x < result.X(); x++) {
 for (int y = 0; y < result.Y(); y++) {
 assertEquals(expected.get(x, y), result.get(x, y));
 }
 }
 }

 @Test
 public void testConnectedComponentsWithEightConnectedAndInvalidBackground() {
 BinaryImage binaryImage = new BinaryImage(new int[][]{
 { 1, 1, 1, 1, 1 },
 { 1, 0, 0, 0, 1 },
 { 1, 0, 1, 0, 1 },
 { 1, 0, 1, 0, 1 },
 { 1, 1, 1, 1, 1 }
 });

 try {
 ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 2);
 fail("Expected InvalidArgumentException");
 } catch (InvalidArgumentException e) {
 // Expected
 }
 }
}