import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.ops.InvalidArgumentException;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;

 @Test
 public void testConnectedComponentsConstructor() {
 cc = new ConnectedComponents(0, 0);
 assertNotNull(cc);

 cc = new ConnectedComponents(1, 1);
 assertNotNull(cc);

 cc = new ConnectedComponents(4, 0);
 assertNotNull(cc); // Exception not tested in this test case

 cc = new ConnectedComponents(0, 4);
 assertNotNull(cc); // Exception not tested in this test case
 }

 @Test(expected = InvalidArgumentException.class)
 public void testConnectedComponentsInvalidConstructor() {
 cc = new ConnectedComponents(2, 1);
 fail("InvalidArgumentException expected but not thrown.");
 }

 @Test
 public void testApplyMethodBasicCase() {
 BinaryImage image = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 1, 0, 1, 1},
 {1, 0, 0, 0, 1},
 {1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1}
 });
 cc = new ConnectedComponents(0, 0);
 GrayImage result = cc.apply(image);
 GrayImage expected = new GrayImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 1, 2, 1, 1},
 {1, 2, 2, 0, 1},
 {1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1}
 });
 assertEquals(expected, result);
 }

 @Test
 public void testApplyMethodEdgeCase1() {
 BinaryImage image = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 1, 0, 1, 1},
 {1, 0, 0, 0, 1},
 {1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1}
 });
 cc = new ConnectedComponents(1, 0);
 GrayImage result = cc.apply(image);
 GrayImage expected = new GrayImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 1, 2, 1, 1},
 {1, 2, 2, 1, 1},
 {1, 1, 2, 1, 1},
 {1, 1, 1, 1, 1}
 });
 assertEquals(expected, result);
 }

 @Test
 public void testApplyMethodEdgeCase2() {
 BinaryImage image = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 1, 0, 1, 1},
 {1, 0, 0, 0, 1},
 {1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1}
 });
 cc = new ConnectedComponents(1, 1);
 GrayImage result = cc.apply(image);
 GrayImage expected = new GrayImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1},
 {1, 1, 2, 1, 1},
 {1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1}
 });
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testApplyMethodNullImage() {
 BinaryImage nullImage = null;
 cc = new ConnectedComponents(0, 0);
 try {
 cc.apply(nullImage);
 fail("NullPointerException expected but not thrown.");
 } catch (NullPointerException e) {
 // Expected
 }
}

}