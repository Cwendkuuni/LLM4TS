package jigl.image.ops.test;

import static org.junit.Assert.*;

import org.junit.Test;

import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;

public class ShiftTest {

 @Test
 public void testShiftGrayImageWithNoWrap() {
 Shift shift = new Shift(2, 2, Shift.NOWRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 GrayImage shiftedImage = new GrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (byte) (x + y));
 shiftedImage.set(x, y, (byte) (x + y));
 }
 }

 GrayImage outputImage = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 assertEquals(outputImage.get(x, y), shiftedImage.get(x + 2, y + 2));
 }
 }
 }

 @Test
 public void testShiftGrayImageWithWrapXPositive() {
 Shift shift = new Shift(2, 2, Shift.WRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 GrayImage shiftedImage = new GrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (byte) (x + y));
 shiftedImage.set(x, y, (byte) (x + y));
 }
 }

 GrayImage outputImage = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if (x + 2 >= 5) {
 x = x % 5;
 }
 if (y + 2 >= 5) {
 y = y % 5;
 }
 assertEquals(outputImage.get(x, y), shiftedImage.get(x + 2, y + 2));
 }
 }
 }

 @Test
 public void testShiftGrayImageWithWrapYPositive() {
 Shift shift = new Shift(2, 2, Shift.WRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 GrayImage shiftedImage = new GrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (byte) (x + y));
 shiftedImage.set(x, y, (byte) (x + y));
 }
 }

 GrayImage outputImage = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if (x >= 5) {
 x = x % 5;
 }
 if (y + 2 >= 5) {
 y = y % 5;
 }
 assertEquals(outputImage.get(x, y), shiftedImage.get(x, y + 2));
 }
 }
 }

 @Test
 public void testShiftGrayImageWithWrapXY() {
 Shift shift = new Shift(2, 2, Shift.WRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 GrayImage shiftedImage = new GrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, (byte) (x + y));
 shiftedImage.set(x, y, (byte) (x + y));
 }
 }

 GrayImage outputImage = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if (x + 2 >= 5) {
 x = x % 5;
 }
 if (y + 2 >= 5) {
 y = y % 5;
 }
 assertEquals(outputImage.get(x, y), shiftedImage.get(x + 2, y + 2));
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftGrayImageWithNegativeXOffset() {
 Shift shift = new Shift(-2, 2, Shift.NOWRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 assertNull(shift.apply(originalImage));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftGrayImageWithNegativeYOffset() {
 Shift shift = new Shift(2, -2, Shift.NOWRAP);
 GrayImage originalImage = new GrayImage(5, 5);
 assertNull(shift.apply(originalImage));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftGrayImageWithNegativeWrap() {
 Shift shift = new Shift(2, 2, -1);
 assertNull(shift.apply(new GrayImage(5, 5)));
 }

 @Test
 public void testShiftRealGrayImageWithNoWrap() {
 Shift shift = new Shift(2.0f, 2.0f, Shift.NOWRAP);
 RealGrayImage originalImage = new RealGrayImage(5, 5);
 RealGrayImage shiftedImage = new RealGrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, x + y);
 shiftedImage.set(x, y, x + y);
 }
 }

 RealGrayImage outputImage = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 assertEquals(outputImage.get(x, y), shiftedImage.get(x + 2, y + 2), 0.001f);
 }
 }
 }

 @Test
 public void testShiftRealGrayImageWithWrapXPositive() {
 Shift shift = new Shift(2.0f, 2.0f, Shift.WRAP);
 RealGrayImage originalImage = new RealGrayImage(5, 5);
 RealGrayImage shiftedImage = new RealGrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, x + y);
 shiftedImage.set(x, y, x + y);
 }
 }

 RealGrayImage outputImage = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if (x + 2 >= 5) {
 x = x % 5.0f;
 }
 if (y + 2 >= 5) {
 y = y % 5.0f;
 }
 assertEquals(outputImage.get(x, y), shiftedImage.get(x + 2, y + 2), 0.001f);
 }
 }
 }

 @Test
 public void testShiftRealGrayImageWithWrapYPositive() {
 Shift shift = new Shift(2.0f, 2.0f, Shift.WRAP);
 RealGrayImage originalImage = new RealGrayImage(5, 5);
 RealGrayImage shiftedImage = new RealGrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, x + y);
 shiftedImage.set(x, y, x + y);
 }
 }

 RealGrayImage outputImage = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if (x >= 5) {
 x = x % 5.0f;
 }
 if (y + 2 >= 5) {
 y = y % 5.0f;
 }
 assertEquals(outputImage.get(x, y), shiftedImage.get(x, y + 2), 0.001f);
 }
 }
 }

 @Test
 public void testShiftRealGrayImageWithWrapXY() {
 Shift shift = new Shift(2.0f, 2.0f, Shift.WRAP);
 RealGrayImage originalImage = new RealGrayImage(5, 5);
 RealGrayImage shiftedImage = new RealGrayImage(5, 5);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 originalImage.set(x, y, x + y);
 shiftedImage.set(x, y, x + y);
 }
 }

 RealGrayImage outputImage = shift.apply(originalImage);

 for (int x = 0; x < 5; x++) {
 for (int y = 0; y < 5; y++) {
 if (x + 2 >= 5) {
 x = x % 5.0f;
 }
 if (y + 2 >= 5) {
 y = y % 5.0f;
 }
 assertEquals(outputImage.get(x, y), shiftedImage.get(x + 2, y + 2), 0.001f);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftRealGrayImageWithNegativeXOffset() {
 Shift shift = new Shift(-2.0f, 2.0f, Shift.NOWRAP);
 assertNull(shift.apply(new RealGrayImage(5, 5)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftRealGrayImageWithNegativeYOffset() {
 Shift shift = new Shift(2.0f, -2.0f, Shift.NOWRAP);
 assertNull(shift.apply(new RealGrayImage(5, 5)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftRealGrayImageWithNegativeWrap() {
 Shift shift = new Shift(2.0f, 2.0f, -1.0f);
 assertNull(shift.apply(new RealGrayImage(5, 5)));
 }
}