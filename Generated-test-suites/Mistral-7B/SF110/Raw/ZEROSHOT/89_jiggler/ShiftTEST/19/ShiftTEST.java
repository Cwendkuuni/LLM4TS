package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ShiftTest {

 private static final int X = 10;
 private static final int Y = 5;

 @Test
 public void testShiftGrayImageWithNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expectedOutput = new GrayImage(X, Y);

 for (int x = 0; x < X; ++x) {
 for (int y = 0; y < Y; ++y) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 }
 }

 GrayImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test
 public void testShiftGrayImageWithWrapX() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expectedOutput = new GrayImage(X, Y);

 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + 2 >= 0 && x + 2 < X && y + 3 >= 0 && y + 3 < Y) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 }
 else {
 int newX = (x + 2) % X;
 int newY = (y + 3) % Y;
 expectedOutput.set(newX, newY, input.get(x, y));
 }
 }
 }

 GrayImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test
 public void testShiftGrayImageWithWrapY() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(X, Y);
 GrayImage expectedOutput = new GrayImage(X, Y);

 for (int x = 0; x < X; ++x) {
 for (int y = 0; y < Y; ++y) {
 if (x + 2 >= 0 && x + 2 < X && y + 3 >= 0 && y + 3 < Y) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 }
 else {
 int newX = (x + 2) % X;
 int newY = (y + 3 + Y) % Y;
 expectedOutput.set(newX, newY, input.get(x, y));
 }
 }
 }

 GrayImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftGrayImageWithInvalidOffsets() {
 Shift shift = new Shift(X + 5, Y + 5, Shift.NOWRAP);
 GrayImage input = new GrayImage(X, Y);
 assertFalse("Shift should have thrown an exception", shift.apply(input) instanceof GrayImage);
 }

 @Test
 public void testShiftRealGrayImageWithNoWrap() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 RealGrayImage expectedOutput = new RealGrayImage(X, Y);

 for (int x = 0; x < X; ++x) {
 for (int y = 0; y < Y; ++y) {
 expectedOutput.set(x, y, input.get(x + 2.0f, y + 3.0f));
 }
 }

 RealGrayImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test
 public void testShiftRealGrayImageWithWrapX() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 RealGrayImage expectedOutput = new RealGrayImage(X, Y);

 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + 2.0f >= 0 && x + 2.0f < X && y + 3.0f >= 0 && y + 3.0f < Y) {
 expectedOutput.set(x, y, input.get(x + 2.0f, y + 3.0f));
 }
 else {
 double newX = (x + 2.0f) % X;
 double newY = y + 3.0f;
 expectedOutput.set(newX, newY, input.get(x, y));
 }
 }
 }

 RealGrayImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test
 public void testShiftRealGrayImageWithWrapY() {
 Shift shift = new Shift(2.0f, 3.0f, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 RealGrayImage expectedOutput = new RealGrayImage(X, Y);

 for (int x = 0; x < X; ++x) {
 for (int y = 0; y < Y; ++y) {
 if (x + 2.0f >= 0 && x + 2.0f < X && y + 3.0f >= 0 && y + 3.0f < Y) {
 expectedOutput.set(x, y, input.get(x + 2.0f, y + 3.0f));
 }
 else {
 double newX = x + 2.0f;
 double newY = (y + 3.0f + Y) % Y;
 expectedOutput.set(newX, newY, input.get(x, y));
 }
 }
 }

 RealGrayImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftRealGrayImageWithInvalidOffsets() {
 Shift shift = new Shift(X + 5.0f, Y + 5.0f, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(X, Y);
 assertFalse("Shift should have thrown an exception", shift.apply(input) instanceof RealGrayImage);
 }

 @Test
 public void testShiftColorImageWithNoWrap() {
 Shift shift = new Shift(new int[]{2, 3}, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[]{0, 1, 2}, new byte[]{3, 4, 5}, new byte[]{6, 7, 8});
 ColorImage expectedOutput = new ColorImage(X, Y);

 for (int channel = 0; channel < 3; ++channel) {
 for (int x = 0; x < X; ++x) {
 for (int y = 0; y < Y; ++y) {
 expectedOutput.plane(channel).set(x, y, input.plane(channel).get(x + 2, y + 3));
 }
 }
 }

 ColorImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test
 public void testShiftColorImageWithWrapX() {
 Shift shift = new Shift(new int[]{2, 3}, Shift.WRAP);
 ColorImage input = new ColorImage(X, Y, new byte[]{0, 1, 2}, new byte[]{3, 4, 5}, new byte[]{6, 7, 8});
 ColorImage expectedOutput = new ColorImage(X, Y);

 for (int channel = 0; channel < 3; ++channel) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + 2 >= 0 && x + 2 < X && y + 3 >= 0 && y + 3 < Y) {
 expectedOutput.plane(channel).set(x, y, input.plane(channel).get(x + 2, y + 3));
 }
 else {
 int newX = (x + 2) % X;
 int newY = y + 3;
 expectedOutput.plane(channel).set(newX, newY, input.plane(channel).get(x, y));
 }
 }
 }
 }

 ColorImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test
 public void testShiftColorImageWithWrapY() {
 Shift shift = new Shift(new int[]{2, 3}, Shift.WRAP);
 ColorImage input = new ColorImage(X, Y, new byte[]{0, 1, 2}, new byte[]{3, 4, 5}, new byte[]{6, 7, 8});
 ColorImage expectedOutput = new ColorImage(X, Y);

 for (int channel = 0; channel < 3; ++channel) {
 for (int x = 0; x < X; ++x) {
 for (int y = 0; y < Y; ++y) {
 if (x + 2 >= 0 && x + 2 < X && y + 3 >= 0 && y + 3 < Y) {
 expectedOutput.plane(channel).set(x, y, input.plane(channel).get(x + 2, y + 3));
 }
 else {
 int newX = x + 2;
 int newY = (y + 3 + Y) % Y;
 expectedOutput.plane(channel).set(newX, newY, input.plane(channel).get(x, y));
 }
 }
 }
 }

 ColorImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftColorImageWithInvalidOffsets() {
 Shift shift = new Shift(new int[]{X + 5, Y + 5}, Shift.NOWRAP);
 ColorImage input = new ColorImage(X, Y, new byte[]{0, 1, 2}, new byte[]{3, 4, 5}, new byte[]{6, 7, 8});
 assertFalse("Shift should have thrown an exception", shift.apply(input) instanceof ColorImage);
 }

 @Test
 public void testShiftRealColorImageWithNoWrap() {
 Shift shift = new Shift(new float[]{2.0f, 3.0f}, Shift.NOWRAP);
 RealColorImage input = new RealColorImage(X, Y, new float[]{0.0f, 1.0f, 2.0f}, new float[]{3.0f, 4.0f, 5.0f}, new float[]{6.0f, 7.0f, 8.0f});
 RealColorImage expectedOutput = new RealColorImage(X, Y);

 for (int channel = 0; channel < 3; ++channel) {
 for (int x = 0; x < X; ++x) {
 for (int y = 0; y < Y; ++y) {
 expectedOutput.plane(channel).set(x, y, input.plane(channel).get(x + 2.0f, y + 3.0f));
 }
 }
 }

 RealColorImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test
 public void testShiftRealColorImageWithWrapX() {
 Shift shift = new Shift(new float[]{2.0f, 3.0f}, Shift.WRAP);
 RealColorImage input = new RealColorImage(X, Y, new float[]{0.0f, 1.0f, 2.0f}, new float[]{3.0f, 4.0f, 5.0f}, new float[]{6.0f, 7.0f, 8.0f});
 RealColorImage expectedOutput = new RealColorImage(X, Y);

 for (int channel = 0; channel < 3; ++channel) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x + 2.0f >= 0 && x + 2.0f < X && y + 3.0f >= 0 && y + 3.0f < Y) {
 expectedOutput.plane(channel).set(x, y, input.plane(channel).get(x + 2.0f, y + 3.0f));
 }
 else {
 double newX = (x + 2.0f) % X;
 double newY = y + 3.0f;
 expectedOutput.plane(channel).set(newX, newY, input.plane(channel).get(x, y));
 }
 }
 }
 }

 RealColorImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test
 public void testShiftRealColorImageWithWrapY() {
 Shift shift = new Shift(new float[]{2.0f, 3.0f}, Shift.WRAP);
 RealColorImage input = new RealColorImage(X, Y, new float[]{0.0f, 1.0f, 2.0f}, new float[]{3.0f, 4.0f, 5.0f}, new float[]{6.0f, 7.0f, 8.0f});
 RealColorImage expectedOutput = new RealColorImage(X, Y);

 for (int channel = 0; channel < 3; ++channel) {
 for (int x = 0; x < X; ++x) {
 for (int y = 0; y < Y; ++y) {
 if (x + 2.0f >= 0 && x + 2.0f < X && y + 3.0f >= 0 && y + 3.0f < Y) {
 expectedOutput.plane(channel).set(x, y, input.plane(channel).get(x + 2.0f, y + 3.0f));
 }
 else {
 double newX = x + 2.0f;
 double newY = (y + 3.0f + Y) % Y;
 expectedOutput.plane(channel).set(newX, newY, input.plane(channel).get(x, y));
 }
 }
 }
 }

 RealColorImage output = shift.apply(input);
 assertImageEquals(expectedOutput, output);
 }

 @Test(expected = ImageDimensionMismatchException.class)
 public void testShiftRealColorImageWithInvalidOffsets() {
 Shift shift = new Shift(new float[]{X + 5.0f, Y + 5.0f}, Shift.NOWRAP);
 RealColorImage input = new RealColorImage(X, Y, new float[]{0.0f, 1.0f, 2.0f}, new float[]{3.0f, 4.0f, 5.0f}, new float[]{6.0f, 7.0f, 8.0f});
 assertFalse("Shift should have thrown an exception", shift.apply(input) instanceof RealColorImage);
 }
}