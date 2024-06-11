package jigl.image.ops.test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.BinaryImage;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponentsConstructor() throws InvalidArgumentException {
 ConnectedComponents cc = new ConnectedComponents(0, 0);
 assertNotNull(cc);

 ConnectedComponents cc1 = new ConnectedComponents(4, 1);
 assertNotNull(cc1);

 ConnectedComponents cc2 = new ConnectedComponents(1, 1);
 assertNotNull(cc2);

 ConnectedComponents cc3 = new ConnectedComponents(2, 1);
 assertNotNull(cc3);

 ConnectedComponents cc4 = new ConnectedComponents(-1, 1);
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(-1, 1));

 ConnectedComponents cc5 = new ConnectedComponents(0, -1);
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, -1));

 ConnectedComponents cc6 = new ConnectedComponents(4, -1);
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(4, -1));
 }

 @Test
 public void testConnectedComponentsApplyBinaryImage() {
 BinaryImage binaryImage = new BinaryImage( new int[][] {
 { 0, 1, 1, 1, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 0, 0, 0, 0, 0, 1 },
 { 1, 1, 1, 0, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 0, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 0, 0, 0, 0, 0, 1 },
 { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1, 1, 1, 1, 1 }
 }, 10, 10 );

 ConnectedComponents cc = new ConnectedComponents(4, 1);
 GrayImage result = cc.apply(binaryImage);

 GrayImage expectedResult = new GrayImage(result.X(), result.Y());
 expectedResult.set(0, 0, Integer.valueOf(1));
 expectedResult.set(1, 0, Integer.valueOf(2));
 expectedResult.set(2, 0, Integer.valueOf(2));
 expectedResult.set(3, 0, Integer.valueOf(2));
 expectedResult.set(4, 0, Integer.valueOf(2));
 expectedResult.set(5, 0, Integer.valueOf(2));
 expectedResult.set(6, 0, Integer.valueOf(2));
 expectedResult.set(7, 0, Integer.valueOf(2));
 expectedResult.set(8, 0, Integer.valueOf(2));
 expectedResult.set(9, 0, Integer.valueOf(3));
 expectedResult.set(0, 1, Integer.valueOf(2));
 expectedResult.set(1, 1, Integer.valueOf(3));
 expectedResult.set(2, 1, Integer.valueOf(2));
 expectedResult.set(3, 1, Integer.valueOf(2));
 expectedResult.set(4, 1, Integer.valueOf(2));
 expectedResult.set(5, 1, Integer.valueOf(2));
 expectedResult.set(6, 1, Integer.valueOf(2));
 expectedResult.set(7, 1, Integer.valueOf(2));
 expectedResult.set(8, 1, Integer.valueOf(2));
 expectedResult.set(9, 1, Integer.valueOf(3));
 expectedResult.set(0, 2, Integer.valueOf(2));
 expectedResult.set(1, 2, Integer.valueOf(3));
 expectedResult.set(2, 2, Integer.valueOf(2));
 expectedResult.set(3, 2, Integer.valueOf(3));
 expectedResult.set(4, 2, Integer.valueOf(3));
 expectedResult.set(5, 2, Integer.valueOf(3));
 expectedResult.set(6, 2, Integer.valueOf(3));
 expectedResult.set(7, 2, Integer.valueOf(3));
 expectedResult.set(8, 2, Integer.valueOf(3));
 expectedResult.set(9, 2, Integer.valueOf(3));
 expectedResult.set(0, 3, Integer.valueOf(2));
 // ... and so on

 for (int i = 0; i < result.X(); ++i) {
 for (int j = 0; j < result.Y(); ++j) {
 int expected = expectedResult.get(i, j);
 int actual = result.get(i, j);
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testConnectedComponentsApplyBinaryImageWithBackground() {
 BinaryImage binaryImage = new BinaryImage( new int[][] {
 { 0, 1, 1, 1, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 0, 0, 0, 0, 1, 1 },
 { 1, 1, 1, 0, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 0, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 0, 0, 0, 0, 1, 1 },
 { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1, 1, 1, 1, 1 }
 }, 10, 10 );

 ConnectedComponents cc = new ConnectedComponents(4, 0);
 GrayImage result = cc.apply(binaryImage);

 // We expect all pixels to be set to 0 in the result since the background is set to 0
 for (int i = 0; i < result.X(); ++i) {
 for (int j = 0; j < result.Y(); ++j) {
 int actual = result.get(i, j);
 assertEquals(0, actual);
 }
 }
 }
}