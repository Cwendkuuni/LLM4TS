package jigl.image.ops.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.types.GrayImage;
import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.InvalidArgumentException;
import jigl.image.BinaryImage;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;
 private GrayImage resultImage;
 private BinaryImage inputImage;

 @Before
 public void setup() {
 inputImage = new BinaryImage(new int[][]{
 {0, 0, 1, 1, 0, 0},
 {0, 0, 1, 1, 0, 0},
 {1, 1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1, 1},
 {0, 0, 1, 1, 0, 0},
 {0, 0, 1, 1, 0, 0}
 });
 cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 resultImage = new GrayImage(inputImage.X(), inputImage.Y());
 }

 @Test
 public void testValidConstructor() {
 ConnectedComponents cc1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
 }

 @Test(expected = InvalidArgumentException.class)
 public void testInvalidConstructor() {
 new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
 }

 @Test
 public void testApplyWithFourConnected() {
 cc.apply(inputImage, resultImage);
 // Assuming the output is [1, 2, 3, 3, 2, 2] for the given input image
 assertEquals("[1, 2, 3, 3, 2, 2]", resultImage.toRowsString());
 }

 @Test
 public void testApplyWithEightConnected() {
 cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
 resultImage = new GrayImage(inputImage.X(), inputImage.Y());
 cc.apply(inputImage, resultImage);
 // Assuming the output is [1, 2, 3, 3, 2, 2] for the given input image
 assertEquals("[1, 2, 3, 3, 2, 2]", resultImage.toRowsString());
 }

 @Test
 public void testBackground() {
 cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 cc.apply(inputImage, resultImage);
 // Assuming the output is [1, 1, 2, 2, 1, 1] for the given input image with a background color of 1
 assertEquals("[1, 1, 2, 2, 1, 1]", resultImage.toRowsString());
 }
}