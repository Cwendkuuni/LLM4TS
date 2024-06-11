package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;

import org.junit.Test;

public class ShiftTest {

 @Test
 public void testShiftGrayImageWithNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 input.set(i, j, (byte) i);
 }
 }
 GrayImage output = shift.apply(input);
 GrayImage expectedOutput = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 if ((i + 2) >= 0 && (i + 2) < 10 && (j + 3) >= 0 && (j + 3) < 10) {
 expectedOutput.set(i, j, input.get(i + 2, j + 3));
 } else {
 expectedOutput.set(i, j, Byte.valueOf((byte) 0));
 }
 }
 }
 assertArrayEquals(expectedOutput.getPixels(), output.getPixels());
 }

 @Test
 public void testShiftGrayImageWithWrapX() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 input.set(i, j, (byte) i);
 }
 }
 GrayImage output = shift.apply(input);
 GrayImage expectedOutput = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 if (i + 2 >= 0) {
 if (i + this.xoffset + j >= 0) {
 expectedOutput.set(i, j, input.get(i + 2, j));
 } else {
 expectedOutput.set(i, j, input.get((i + 2) % 10, j));
 }
 } else {
 expectedOutput.set(i, j, Byte.valueOf((byte) 0));
 }
 }
 }
 assertArrayEquals(expectedOutput.getPixels(), output.getPixels());
 }

 @Test
 public void testShiftGrayImageWithWrapY() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 input.set(i, j, (byte) i);
 }
 }
 GrayImage output = shift.apply(input);
 GrayImage expectedOutput = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 if (j + 3 >= 0) {
 if (i + this.xoffset + j >= 0) {
 expectedOutput.set(i, j, input.get(i, j + 3));
 } else {
 expectedOutput.set(i, j, input.get(i, (j + 3) % 10));
 }
 } else {
 expectedOutput.set(i, j, Byte.valueOf((byte) 0));
 }
 }
 }
 assertArrayEquals(expectedOutput.getPixels(), output.getPixels());
 }

 @Test
 public void testShiftGrayImageWithNegativeOffsets() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 GrayImage input = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 input.set(i, j, (byte) i);
 }
 }
 GrayImage output = shift.apply(input);
 GrayImage expectedOutput = new GrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 if ((i - 2) >= 0 && (i - 2) < 10 && (j - 3) >= 0 && (j - 3) < 10) {
 expectedOutput.set(i, j, input.get(i - 2, j - 3));
 } else {
 expectedOutput.set(i, j, Byte.valueOf((byte) 0));
 }
 }
 }
 assertArrayEquals(expectedOutput.getPixels(), output.getPixels());
 }

 @Test
 public void testShiftRealGrayImageWithNoWrap() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 input.set(i, j, i / 10.0f);
 }
 }
 RealGrayImage output = shift.apply(input);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 if ((i + 2) >= 0 && (i + 2) < 10 && (j + 3) >= 0 && (j + 3) < 10) {
 expectedOutput.set(i, j, input.get(i + 2, j + 3) / 10.0f);
 } else {
 expectedOutput.set(i, j, Double.valueOf(0.0));
 }
 }
 }
 assertTrue(output.isEqual(expectedOutput, 0.01));
 }

 @Test
 public void testShiftRealGrayImageWithWrapX() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 input.set(i, j, i / 10.0f);
 }
 }
 RealGrayImage output = shift.apply(input);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 if (i + 2 >= 0) {
 if (i + this.xoffset + j >= 0) {
 expectedOutput.set(i, j, input.get(i + 2, j) / 10.0f);
 } else {
 expectedOutput.set(i, j, input.get((i + 2) % 10, j) / 10.0f);
 }
 } else {
 expectedOutput.set(i, j, Double.valueOf(0.0));
 }
 }
 }
 assertTrue(output.isEqual(expectedOutput, 0.01));
 }

 @Test
 public void testShiftRealGrayImageWithWrapY() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 input.set(i, j, i / 10.0f);
 }
 }
 RealGrayImage output = shift.apply(input);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 if (j + 3 >= 0) {
 if (i + this.xoffset + j >= 0) {
 expectedOutput.set(i, j, input.get(i, j + 3) / 10.0f);
 } else {
 expectedOutput.set(i, j, input.get(i, (j + 3) % 10) / 10.0f);
 }
 } else {
 expectedOutput.set(i, j, Double.valueOf(0.0));
 }
 }
 }
 assertTrue(output.isEqual(expectedOutput, 0.01));
 }

 @Test
 public void testShiftRealGrayImageWithNegativeOffsets() {
 Shift shift = new Shift(-2.0f, -3.0f, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 input.set(i, j, i / 10.0f);
 }
 }
 RealGrayImage output = shift.apply(input);
 RealGrayImage expectedOutput = new RealGrayImage(10, 10);
 for (int i = 0; i < 10; i++) {
 for (int j = 0; j < 10; j++) {
 if ((i - 2) >= 0 && (i - 2) < 10 && (j - 3) >= 0 && (j - 3) < 10) {
 expectedOutput.set(i, j, input.get(i - 2, j - 3) / 10.0f);
 } else {
 expectedOutput.set(i, j, Double.valueOf(0.0));
 }
 }
 }
 assertTrue(output.isEqual(expectedOutput, 0.01));
 }

 @Test(expected = ImageException.class)
 public void testShiftNegativeInput() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage input = null;
 assertNull(input);
 try {
 shift.apply(input);
 } catch (ImageException e) {
 // Expected
 }
 }

 @Test(expected = ImageException.class)
 public void testShiftNullInput() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(0, 0);
 assertNull(input.getWidth());
 assertNull(input.getHeight());
 try {
 shift.apply(input);
 } catch (ImageException e) {
 // Expected
 }
 }

 @Test(expected = ImageException.class)
 public void testShiftInvalidImage() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 try {
 shift.apply(input);
 fail("Should have thrown an ImageException");
 } catch (ImageException e) {
 // Expected
 }
 }
}