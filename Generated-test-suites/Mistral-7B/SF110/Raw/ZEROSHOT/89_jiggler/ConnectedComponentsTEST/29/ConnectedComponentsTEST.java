package jigl.image.ops.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.IntegerPixel;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;
 private BinaryImage inputImage;
 private GrayImage expectedOutput;

 @Before
 public void setup() {
 inputImage = new BinaryImage(new int[][]{{1, 1, 1, 1, 1}, {1, 1, 1, 0, 1}, {1, 1, 0, 0, 1}, {1, 0, 0, 0, 1}, {1, 1, 1, 1, 1}});
 expectedOutput = new GrayImage(new int[][]{{1, 1, 1, 1, 1}, {1, 2, 2, 0, 1}, {1, 2, 3, 0, 1}, {1, 0, 0, 0, 1}, {1, 1, 1, 1, 1}});
 try {
 cc = new ConnectedComponents(0, 1);
 } catch (InvalidArgumentException e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
 }
 }

 @Test
 public void testApply() {
 final GrayImage output = cc.apply(inputImage);
 assertNotNull(output);
 assertArrayEquals(expectedOutput.getData(), output.getData());
 }

 @Test(expected = InvalidArgumentException.class)
 public void testInvalidArgumentException() {
 try {
 cc = new ConnectedComponents(2, 1);
 } catch (InvalidArgumentException e) {
 // expected
 }
 }

 @Test(expected = InvalidArgumentException.class)
 public void testInvalidArgumentException2() {
 try {
 cc = new ConnectedComponents(1, 2);
 } catch (InvalidArgumentException e) {
 // expected
 }
 }

 @Test
 public void testConnectedComponentsWithFourConnectedNeighbors() {
 final GrayImage output = cc.apply(inputImage);
 final int[][] data = output.getData();
 final IntegerPixel p1 = new IntegerPixel(0, 0);
 final IntegerPixel p2 = new IntegerPixel(1, 1);
 final IntegerPixel p3 = new IntegerPixel(2, 1);
 final IntegerPixel p4 = new IntegerPixel(3, 1);
 final IntegerPixel p5 = new IntegerPixel(4, 1);
 final IntegerPixel p6 = new IntegerPixel(2, 2);
 final IntegerPixel p7 = new IntegerPixel(3, 2);
 final IntegerPixel p8 = new IntegerPixel(3, 3);
 final IntegerPixel p9 = new IntegerPixel(4, 3);
 final IntegerPixel p10 = new IntegerPixel(4, 4);

 assertTrue(data[0][p1.getX()][p1.getY()] == 1);
 assertTrue(data[1][p2.getX()][p2.getY()] == 2);
 assertTrue(data[1][p3.getX()][p3.getY()] == 2);
 assertTrue(data[2][p4.getX()][p4.getY()] == 3);
 assertTrue(data[2][p6.getX()][p6.getY()] == 2);
 assertTrue(data[2][p7.getX()][p7.getY()] == 2);
 assertTrue(data[3][p8.getX()][p8.getY()] == 0);
 assertTrue(data[4][p9.getX()][p9.getY()] == 0);
 assertTrue(data[4][p10.getX()][p10.getY()] == 0);
 }

 @Test
 public void testConnectedComponentsWithEightConnectedNeighbors() {
 try {
 cc = new ConnectedComponents(1, 1);
 } catch (InvalidArgumentException e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
 }

 final GrayImage output = cc.apply(inputImage);
 final int[][] data = output.getData();
 final IntegerPixel p1 = new IntegerPixel(0, 0);
 final IntegerPixel p2 = new IntegerPixel(1, 1);
 final IntegerPixel p3 = new IntegerPixel(2, 1);
 final IntegerPixel p4 = new IntegerPixel(3, 1);
 final IntegerPixel p5 = new IntegerPixel(4, 1);
 final IntegerPixel p6 = new IntegerPixel(2, 2);
 final IntegerPixel p7 = new IntegerPixel(3, 2);
 final IntegerPixel p8 = new IntegerPixel(3, 3);
 final IntegerPixel p9 = new IntegerPixel(4, 3);
 final IntegerPixel p10 = new IntegerPixel(4, 4);

 assertTrue(data[0][p1.getX()][p1.getY()] == 1);
 assertFalse(data[1][p1.getX()][p1.getY()] == 1);
 assertFalse(data[1][p2.getX()][p2.getY()] == 1);
 assertFalse(data[2][p1.getX()][p1.getY()] == 1);
 assertFalse(data[3][p1.getX()][p1.getY()] == 1);
 assertFalse(data[4][p1.getX()][p1.getY()] == 1);
 assertTrue(data[1][p2.getX()][p2.getY()] == 2);
 assertTrue(data[1][p3.getX()][p3.getY()] == 2);
 assertTrue(data[2][p4.getX()][p4.getY()] == 3);
 assertTrue(data[2][p6.getX()][p6.getY()] == 2);
 assertTrue(data[2][p7.getX()][p7.getY()] == 2);
 assertTrue(data[3][p8.getX()][p8.getY()] == 0);
 assertTrue(data[4][p9.getX()][p9.getY()] == 0);
 assertTrue(data[4][p10.getX()][p10.getY()] == 0);
 }
}