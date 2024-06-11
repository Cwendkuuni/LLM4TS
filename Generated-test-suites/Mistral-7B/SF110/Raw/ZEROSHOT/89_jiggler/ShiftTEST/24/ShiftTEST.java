package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import org.junit.Test;

public class ShiftTest {

 @Test
 public void testShiftGrayImageWithPositiveOffsetAndNoWrap() {
 GrayImage input = new GrayImage(3, 3);
 input.set(0, 0, (byte) 10);
 input.set(1, 1, (byte) 20);
 input.set(2, 2, (byte) 30);

 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 GrayImage output = shift.apply(input);

 byte expectedOutputValue = 10;
 byte actualOutputValue = output.get(0, 0);
 assertEquals("Shift test failed for negative x and y offset and no wrap for GrayImage", expectedOutputValue, actualOutputValue);

 expectedOutputValue = 20;
 actualOutputValue = output.get(1, 1);
 assertEquals("Shift test failed for negative x and y offset and no wrap for GrayImage", expectedOutputValue, actualOutputValue);

 expectedOutputValue = 30;
 actualOutputValue = output.get(2, 2);
 assertEquals("Shift test failed for negative x and y offset and no wrap for GrayImage", expectedOutputValue, actualOutputValue);
 }

 @Test
 public void testShiftGrayImageWithPositiveOffsetAndWrap() {
 GrayImage input = new GrayImage(3, 3);
 input.set(0, 0, (byte) 10);
 input.set(1, 1, (byte) 20);
 input.set(2, 2, (byte) 30);

 Shift shift = new Shift(2, 2, Shift.WRAP);
 GrayImage output = shift.apply(input);

 byte expectedOutputValue = 10;
 byte actualOutputValue = output.get(1, 1);
 assertEquals("Shift test failed for positive x and y offset and wrap for GrayImage", expectedOutputValue, actualOutputValue);

 expectedOutputValue = 20;
 actualOutputValue = output.get(0, 0);
 assertEquals("Shift test failed for positive x and y offset and wrap for GrayImage", expectedOutputValue, actualOutputValue);

 expectedOutputValue = 30;
 actualOutputValue = output.get(2, 2);
 assertEquals("Shift test failed for positive x and y offset and wrap for GrayImage", expectedOutputValue, actualOutputValue);
 }

 @Test(expected = ImageBoundsException.class)
 public void testShiftGrayImageWithNegativeOffsetAndNoWrap() {
 GrayImage input = new GrayImage(3, 3);
 input.set(0, 0, (byte) 10);
 input.set(1, 1, (byte) 20);
 input.set(2, 2, (byte) 30);

 Shift shift = new Shift(-1, -1, Shift.NOWRAP);
 shift.apply(input);
 fail("Expected ImageBoundsException but no exception was thrown.");
 }

 @Test(expected = ImageBoundsException.class)
 public void testShiftGrayImageWithNegativeOffsetAndWrap() {
 GrayImage input = new GrayImage(3, 3);
 input.set(0, 0, (byte) 10);
 input.set(1, 1, (byte) 20);
 input.set(2, 2, (byte) 30);

 Shift shift = new Shift(-1, -1, Shift.WRAP);
 shift.apply(input);
 fail("Expected ImageBoundsException but no exception was thrown.");
 }

 @Test
 public void testShiftRealGrayImageWithPositiveOffsetAndNoWrap() {
 RealGrayImage input = new RealGrayImage(3, 3);
 input.set(0, 0, 10.0f);
 input.set(1, 1, 20.0f);
 input.set(2, 2, 30.0f);

 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 RealGrayImage output = shift.apply(input);

 float expectedOutputValue = 10.0f;
 float actualOutputValue = output.get(0, 0);
 assertEquals("Shift test failed for negative x and y offset and no wrap for RealGrayImage", expectedOutputValue, actualOutputValue, 0.001f);

 expectedOutputValue = 20.0f;
 actualOutputValue = output.get(1, 1);
 assertEquals("Shift test failed for negative x and y offset and no wrap for RealGrayImage", expectedOutputValue, actualOutputValue, 0.001f);

 expectedOutputValue = 30.0f;
 actualOutputValue = output.get(2, 2);
 assertEquals("Shift test failed for negative x and y offset and no wrap for RealGrayImage", expectedOutputValue, actualOutputValue, 0.001f);
 }

 @Test
 public void testShiftRealGrayImageWithPositiveOffsetAndWrap() {
 RealGrayImage input = new RealGrayImage(3, 3);
 input.set(0, 0, 10.0f);
 input.set(1, 1, 20.0f);
 input.set(2, 2, 30.0f);

 Shift shift = new Shift(2, 2, Shift.WRAP);
 RealGrayImage output = shift.apply(input);

 float expectedOutputValue = 10.0f;
 float actualOutputValue = output.get(1, 1);
 assertEquals("Shift test failed for positive x and y offset and wrap for RealGrayImage", expectedOutputValue, actualOutputValue, 0.001f);

 expectedOutputValue = 20.0f;
 actualOutputValue = output.get(0, 0);
 assertEquals("Shift test failed for positive x and y offset and wrap for RealGrayImage", expectedOutputValue, actualOutputValue, 0.001f);

 expectedOutputValue = 30.0f;
 actualOutputValue = output.get(2, 2);
 assertEquals("Shift test failed for positive x and y offset and wrap for RealGrayImage", expectedOutputValue, actualOutputValue, 0.001f);
 }

 @Test(expected = ImageBoundsException.class)
 public void testShiftRealGrayImageWithNegativeOffsetAndNoWrap() {
 RealGrayImage input = new RealGrayImage(3, 3);
 input.set(0, 0, 10.0f);
 input.set(1, 1, 20.0f);
 input.set(2, 2, 30.0f);

 Shift shift = new Shift(-1, -1, Shift.NOWRAP);
 shift.apply(input);
 fail("Expected ImageBoundsException but no exception was thrown.");
 }

 @Test(expected = ImageBoundsException.class)
 public void testShiftRealGrayImageWithNegativeOffsetAndWrap() {
 RealGrayImage input = new RealGrayImage(3, 3);
 input.set(0, 0, 10.0f);
 input.set(1, 1, 20.0f);
 input.set(2, 2, 30.0f);

 Shift shift = new Shift(-1, -1, Shift.WRAP);
 shift.apply(input);
 fail("Expected ImageBoundsException but no exception was thrown.");
 }

 @Test
 public void testShiftColorImageWithPositiveOffsetAndNoWrap() {
 ColorImage input = new ColorImage(3, 3, new byte[3 * 3], new byte[3 * 3], new byte[3 * 3]);
 input.setPlane(0, 0, 0, 0, 10, 5, 20);
 input.setPlane(1, 1, 255, 0, 20, 20, 255);
 input.setPlane(2, 2, 0, 255, 30, 5, 0);

 Shift shift = new Shift(1, 1, Shift.NOWRAP);
 ColorImage output = shift.apply(input);

 byte[] expectedOutputValues = {5, 20, 10};
 byte[] actualOutputValues = output.plane(0);
 assertArrayEquals("Shift test failed for negative x and y offset and no wrap for ColorImage", expectedOutputValues, actualOutputValues);

 byte[] expectedOutputValues1 = {0, 20, 255};
 byte[] actualOutputValues1 = output.plane(1);
 assertArrayEquals("Shift test failed for negative x and y offset and no wrap for ColorImage", expectedOutputValues1, actualOutputValues1);

 byte[] expectedOutputValues2 = {255, 5, 0};
 byte[] actualOutputValues2 = output.plane(2);
 assertArrayEquals("Shift test failed for negative x and y offset and no wrap for ColorImage", expectedOutputValues2, actualOutputValues2);
 }

 @Test
 public void testShiftColorImageWithPositiveOffsetAndWrap() {
 ColorImage input = new ColorImage(3, 3, new byte[3 * 3], new byte[3 * 3], new byte[3 * 3]);
 input.setPlane(0, 0, 0, 0, 10, 5, 20);
 input.setPlane(1, 1, 255, 0, 20, 20, 255);
 input.setPlane(2, 2, 0, 255, 30, 5, 0);

 Shift shift = new Shift(2, 2, Shift.WRAP);
 ColorImage output = shift.apply(input);

 byte[] expectedOutputValues = {255, 5, 10};
 byte[] actualOutputValues = output.plane(0);
 assertArrayEquals("Shift test failed for positive x and y offset and wrap for ColorImage", expectedOutputValues, actualOutputValues);

 byte[] expectedOutputValues1 = {0, 10, 255};
 byte[] actualOutputValues1 = output.plane(1);
 assertArrayEquals("Shift test failed for positive x and y offset and wrap for ColorImage", expectedOutputValues1, actualOutputValues1);

 byte[] expectedOutputValues2 = {10, 255, 0};
 byte[] actualOutputValues2 = output.plane(2);
 assertArrayEquals("Shift test failed for positive x and y offset and wrap for ColorImage", expectedOutputValues2, actualOutputValues2);
 }

 @Test(expected = ImageBoundsException.class)
 public void testShiftColorImageWithNegativeOffsetAndNoWrap() {
 ColorImage input = new ColorImage(3, 3, new byte[3 * 3], new byte[3 * 3], new byte[3 * 3]);
 input.setPlane(0, 0, 0, 0, 10, 5, 20);
 input.setPlane(1, 1, 255, 0, 20, 20, 255);
 input.setPlane(2, 2, 0, 255, 30, 5, 0);

 Shift shift = new Shift(-1, -1, Shift.NOWRAP);
 shift.apply(input);
 fail("Expected ImageBoundsException but no exception was thrown.");
 }

 @Test(expected = ImageBoundsException.class)
 public void testShiftColorImageWithNegativeOffsetAndWrap() {
 ColorImage input = new ColorImage(3, 3, new byte[3 * 3], new byte[3 * 3], new byte[3 * 3]);
 input.setPlane(0, 0, 0, 0, 10, 5, 20);
 input.setPlane(1, 1, 255, 0, 20, 20, 255);
 input.setPlane(2, 2, 0, 255, 30, 5, 0);

 Shift shift = new Shift(-1, -1, Shift.WRAP);
 shift.apply(input);
 fail("Expected ImageBoundsException but no exception was thrown.");
 }
}