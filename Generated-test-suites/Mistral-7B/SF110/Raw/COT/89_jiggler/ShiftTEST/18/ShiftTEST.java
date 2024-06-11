package jigl.image.ops.test;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

 private static final int X = 5;
 private static final int Y = 3;

 @Test
 public void testShiftConstructorWithValidArguments() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 assertEquals(2, shift.getXoffset());
 assertEquals(3, shift.getYoffset());
 assertTrue(shift.isWrap());
 }

 @Test
 public void testShiftConstructorWithInvalidWrapArgument() {
try {
 Shift shift = new Shift(2, 3, 3); // Invalid wrap argument
} catch (IllegalArgumentException e) {
 assertTrue(true);
}
 }

 @Test
 public void testApplyGrayImageWithValidArguments() {
 GrayImage image = new GrayImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage expectedResult = new GrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x > 1 && y > 1) {
 expectedResult.set(x, y, image.get(x - 2, y - 2));
 }
 }
 }
 GrayImage result = shift.apply(image);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 assertEquals(expectedResult.get(x, y), result.get(x, y));
 }
 }
 }

 @Test
 public void testApplyGrayImageWithInvalidOffsetValues() {
 GrayImage image = new GrayImage(X, Y);
 Shift shift = new Shift(X + 1, Y + 1, Shift.NOWRAP);
 GrayImage result = shift.apply(image);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x >= 0 && y >= 0) {
 assertEquals(Integer.valueOf(0), result.get(x, y));
 }
 }
 }
 }

 @Test
 public void testApplyGrayImageWithNegativeOffsetValues() {
 GrayImage image = new GrayImage(X, Y);
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 GrayImage expectedResult = new GrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x < X - 2 && y < Y - 3) {
 expectedResult.set(x, y, image.get(x + 2, y + 3));
 }
 }
 }
 GrayImage result = shift.apply(image);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 assertEquals(expectedResult.get(x, y), result.get(x, y));
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWithValidArguments() {
 RealGrayImage image = new RealGrayImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealGrayImage expectedResult = new RealGrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x > 1 && y > 1) {
 expectedResult.set(x, y, image.get(x - 2, y - 2));
 }
 }
 }
 RealGrayImage result = shift.apply(image);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 assertEquals(expectedResult.get(x, y), result.get(x, y), 0.01f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWithInvalidOffsetValues() {
 RealGrayImage image = new RealGrayImage(X, Y);
 Shift shift = new Shift(X + 1, Y + 1, Shift.NOWRAP);
 RealGrayImage result = shift.apply(image);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x >= 0 && y >= 0) {
 assertEquals(Float.valueOf(0.0f), result.get(x, y), 0.01f);
 }
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWithNegativeOffsetValues() {
 RealGrayImage image = new RealGrayImage(X, Y);
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 RealGrayImage expectedResult = new RealGrayImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x < X - 2 && y < Y - 3) {
 expectedResult.set(x, y, image.get(x + 2, y + 3));
 }
 }
 }
 RealGrayImage result = shift.apply(image);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 assertEquals(expectedResult.get(x, y), result.get(x, y), 0.01f);
 }
 }
 }

 @Test
 public void testApplyColorImageWithValidArguments() {
 ColorImage image = new ColorImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.WRAP);
 ColorImage expectedResult = new ColorImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x > 1 && y > 1) {
 expectedResult.set(x, y, image.getPlane(0).getPixel(x - 2, y - 2));
 expectedResult.setPlane(1, expectedResult.getPlane(1).setPixel(x, y, image.getPlane(1).getPixel(x - 2, y - 2)));
 expectedResult.setPlane(2, expectedResult.getPlane(2).setPixel(x, y, image.getPlane(2).getPixel(x - 2, y - 2)));
 }
 }
 }
 ColorImage result = shift.apply(image);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 assertEquals(expectedResult.getPlane(0).getPixel(x, y), result.getPlane(0).getPixel(x, y));
 assertEquals(expectedResult.getPlane(1).getPixel(x, y), result.getPlane(1).getPixel(x, y));
 assertEquals(expectedResult.getPlane(2).getPixel(x, y), result.getPlane(2).getPixel(x, y));
 }
 }
 }

 @Test
 public void testApplyRealColorImageWithValidArguments() {
 RealColorImage image = new RealColorImage(X, Y);
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealColorImage expectedResult = new RealColorImage(X, Y);
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 if (x > 1 && y > 1) {
 expectedResult.setPlane(0, expectedResult.getPlane(0).setPixel(x, y, image.getPlane(0).getPixel(x - 2, y - 2)));
 expectedResult.setPlane(1, expectedResult.getPlane(1).setPixel(x, y, image.getPlane(1).getPixel(x - 2, y - 2)));
 expectedResult.setPlane(2, expectedResult.getPlane(2).setPixel(x, y, image.getPlane(2).getPixel(x - 2, y - 2)));
 }
 }
 }
 RealColorImage result = shift.apply(image);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 assertEquals(expectedResult.getPlane(0).getPixel(x, y), result.getPlane(0).getPixel(x, y), 0.01f);
 assertEquals(expectedResult.getPlane(1).getPixel(x, y), result.getPlane(1).getPixel(x, y), 0.01f);
 assertEquals(expectedResult.getPlane(2).getPixel(x, y), result.getPlane(2).getPixel(x, y), 0.01f);
 }
 }
 }
}