package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.types.*;
import org.junit.Test;

public class ShiftTest {

 @Test
 public void testShiftGrayImageWithNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(10, 10);
 GrayImage expectedOutput = new GrayImage(10, 10);
 GrayImage output = shift.apply(input);

 for (int x = 0; x < 10; x++) {
 for (int y = 0; y < 10; y++) {
 if (x >= 2 && x < 10 && y >= 3 && y < 10) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 } else {
 expectedOutput.set(x, y, input.get(x, y));
 }
 }
 }

 assertEquals(expectedOutput, output);
 }

 @Test
 public void testShiftGrayImageWithWrapX() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(10, 10);
 GrayImage expectedOutput = new GrayImage(10, 10);
 GrayImage output = shift.apply(input);

 for (int y = 0; y < 10; y++) {
 for (int x = 0; x < 10; x++) {
 if (x + 2 >= 0 && x + 2 < 10 && y + 3 >= 0 && y + 3 < 10) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 } else if (x + 2 < 0) {
 expectedOutput.set((x + 2 + 10) % 10, y, input.get(x, y));
 } else if (x + 2 >= 10) {
 expectedOutput.set((x - (2 % 10)) % 10, y, input.get(x, y));
 } else {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 }
 }
 }

 assertEquals(expectedOutput, output);
 }

 @Test
 public void testShiftGrayImageWithWrapY() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(10, 10);
 GrayImage expectedOutput = new GrayImage(10, 10);
 GrayImage output = shift.apply(input);

 for (int x = 0; x < 10; x++) {
 for (int y = 0; y < 10; y++) {
 if (x >= 0 && x < 10 && y + 3 >= 0 && y + 3 < 10) {
 expectedOutput.set(x, y, input.get(x, y + 3));
 } else if (y + 3 < 0) {
 expectedOutput.set(x, (y + 3 + 10) % 10, input.get(x, y));
 } else if (y + 3 >= 10) {
 expectedOutput.set(x, (y - (3 % 10)) % 10, input.get(x, y));
 } else {
 expectedOutput.set(x, y, input.get(x, y + 3));
 }
 }
 }

 assertEquals(expectedOutput, output);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testShiftGrayImageWithInvalidWrap() {
 Shift shift = new Shift(2, 3, 2);
 GrayImage input = new GrayImage(10, 10);
 try {
 shift.apply(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testShiftRealGrayImageWithNoWrap() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(10, 10);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 RealGrayImage output = shift.apply(input);

 for (int x = 0; x < 10; x++) {
 for (int y = 0; y < 10; y++) {
 if (x >= 2 && x < 10 && y >= 3 && y < 10) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 } else {
 expectedOutput.set(x, y, input.get(x, y));
 }
 }
 }

 assertTrue(output.equals(expectedOutput));
 }

 @Test
 public void testShiftRealGrayImageWithWrapX() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(10, 10);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 RealGrayImage output = shift.apply(input);

 for (int y = 0; y < 10; y++) {
 for (int x = 0; x < 10; x++) {
 if (x + 2 >= 0 && x + 2 < 10 && y + 3 >= 0 && y + 3 < 10) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 } else if (x + 2 < 0) {
 expectedOutput.set((x + 2 + 10) % 10, y, input.get(x, y));
 } else if (x + 2 >= 10) {
 expectedOutput.set((x - (2 % 10)) % 10, y, input.get(x, y));
 } else {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 }
 }
 }

 assertTrue(output.equals(expectedOutput));
 }

 @Test
 public void testShiftRealGrayImageWithWrapY() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(10, 10);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 RealGrayImage output = shift.apply(input);

 for (int x = 0; x < 10; x++) {
 for (int y = 0; y < 10; y++) {
 if (x >= 0 && x < 10 && y + 3 >= 0 && y + 3 < 10) {
 expectedOutput.set(x, y, input.get(x, y + 3));
 } else if (y + 3 < 0) {
 expectedOutput.set(x, (y + 3 + 10) % 10, input.get(x, y));
 } else if (y + 3 >= 10) {
 expectedOutput.set(x, (y - (3 % 10)) % 10, input.get(x, y));
 } else {
 expectedOutput.set(x, y, input.get(x, y + 3));
 }
 }
 }

 assertTrue(output.equals(expectedOutput));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftRealGrayImageWithInvalidWrap() {
 Shift shift = new Shift(2.0f, 3.0f, 2.0f);
 RealGrayImage input = new RealGrayImage(10, 10);
 try {
 shift.apply(input);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}