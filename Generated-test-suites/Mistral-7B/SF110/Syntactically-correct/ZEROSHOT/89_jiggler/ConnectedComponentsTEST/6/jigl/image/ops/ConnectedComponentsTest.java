package jigl.image.ops.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.types.GrayPixel;
import jigl.image.ops.ConnectedComponents;

public class ConnectedComponentsTest {

 private static final int NUM_NEIGHBORS = 8;
 private static final int BACKGROUND = 0;
 private static final int WIDTH = 5;
 private static final int HEIGHT = 5;
 private static final int[][] TEST_IMAGE_DATA = { { 0, 0, 0, 1, 1 },
 { 0, 1, 1, 1, 0 },
 { 0, 1, 1, 1, 0 },
 { 1, 1, 1, 1, 1 },
 { 1, 0, 0, 1, 1 } };

 @Test
 public void testConnectedComponentsConstructor() {
 ConnectedComponents cc = new ConnectedComponents(NUM_NEIGHBORS, BACKGROUND);
 assertNotNull(cc);
 }

 @Test(expected = org.jigl.image.exceptions.InvalidArgumentException.class)
 public void testConnectedComponentsConstructor_InvalidNumberOfNeighbors() {
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, BACKGROUND));
 }

 @Test(expected = org.jigl.image.exceptions.InvalidArgumentException.class)
 public void testConnectedComponentsConstructor_InvalidBackgroundColor() {
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(NUM_NEIGHBORS, 2));
 }

 @Test
 public void testConnectedComponentsApply_EmptyImage() {
 BinaryImage binaryImage = new BinaryImage(WIDTH, HEIGHT);
 GrayImage expectedGrayImage = new GrayImage(WIDTH, HEIGHT);
 ConnectedComponents cc = new ConnectedComponents(NUM_NEIGHBORS, BACKGROUND);
 GrayImage resultGrayImage = cc.apply(binaryImage);
 assertNotNull(resultGrayImage);
 for (int x = 0; x < WIDTH; ++x) {
 for (int y = 0; y < HEIGHT; ++y) {
 assertEquals(0, resultGrayImage.get(x, y).intValue());
 }
 }
 }

 @Test
 public void testConnectedComponentsApply_SimpleImage() {
 BinaryImage binaryImage = new BinaryImage(TEST_IMAGE_DATA);
 GrayImage expectedGrayImage = new GrayImage(TEST_IMAGE_DATA);
 ConnectedComponents cc = new ConnectedComponents(NUM_NEIGHBORS, BACKGROUND);
 GrayImage resultGrayImage = cc.apply(binaryImage);
 assertNotNull(resultGrayImage);
 for (int x = 0; x < WIDTH; ++x) {
 for (int y = 0; y < HEIGHT; ++y) {
 assertEquals(expectedGrayImage.get(x, y).intValue(), resultGrayImage.get(x, y).intValue());
 }
 }
 }

 @Test
 public void testConnectedComponentsApply_ComplexImage() {
 BinaryImage binaryImage = new BinaryImage(new int[][] { { 0, 0, 0, 1, 1 },
 { 0, 1, 1, 1, 0 },
 { 0, 1, 1, 1, 0 },
 { 1, 1, 1, 1, 1 },
 { 1, 0, 0, 1, 1 },
 { 1, 0, 0, 1, 1 },
 { 1, 0, 0, 1, 1 },
 { 1, 0, 0, 1, 1 },
 { 1, 0, 0, 1, 1 } });
 GrayImage expectedGrayImage = new GrayImage(new int[][] { { 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1 },
 { 1, 1, 1, 1, 1 } });
 ConnectedComponents cc = new ConnectedComponents(NUM_NEIGHBORS, BACKGROUND);
 GrayImage resultGrayImage = cc.apply(binaryImage);
 assertNotNull(resultGrayImage);
 for (int x = 0; x < WIDTH; ++x) {
 for (int y = 0; y < HEIGHT; ++y) {
 assertEquals(expectedGrayImage.get(x, y).intValue(), resultGrayImage.get(x, y).intValue());
 }
 }
 }

 @Test
 public void testConnectedComponentsApply_BackgroundHandling() {
 BinaryImage binaryImage = new BinaryImage(new int[][] { { 0, 1, 1 },
 { 1, 0, 1 },
 { 1, 1, 1 } });
 GrayImage expectedGrayImage = new GrayImage(new int[][] { { 0, 1, 1 },
 { 1, 0, 1 },
 { 1, 1, 1 } });
 ConnectedComponents cc = new ConnectedComponents(NUM_NEIGHBORS, BACKGROUND);
 GrayImage resultGrayImage = cc.apply(binaryImage);
 assertNotNull(resultGrayImage);
 for (int x = 0; x < WIDTH; ++x) {
 for (int y = 0; y < HEIGHT; ++y) {
 assertEquals(expectedGrayImage.get(x, y).intValue(), resultGrayImage.get(x, y).intValue());
 }
 }
 }
}