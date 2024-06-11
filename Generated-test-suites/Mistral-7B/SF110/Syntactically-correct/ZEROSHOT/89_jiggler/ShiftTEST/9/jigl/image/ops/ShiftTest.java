package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import org.junit.Test;

public class ShiftTest {

 @Test
 public void testApplyGrayImagePositiveXOffsetPositiveYOffsetNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(5, 5);
 GrayImage expectedOutput = new GrayImage(5, 5);
 for (int x = 0; x < input.X(); x++) {
 for (int y = 0; y < input.Y(); y++) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y));
 }
 }
 }

 @Test
 public void testApplyGrayImagePositiveXOffsetPositiveYOffsetWrap() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(5, 5);
 GrayImage expectedOutput = new GrayImage(5, 5);
 for (int y = 0; y < input.Y(); y++) {
 for (int x = 0; x < input.X(); x++) {
 expectedOutput.set((x + 2) % 5, (y + 3) % 5, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y));
 }
 }
 }

 @Test
 public void testApplyGrayImageNegativeXOffsetPositiveYOffsetNoWrap() {
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 GrayImage input = new GrayImage(5, 5);
 GrayImage expectedOutput = new GrayImage(5, 5);
 for (int x = 0; x < input.X(); x++) {
 for (int y = 0; y < input.Y(); y++) {
 expectedOutput.set(x, y, input.get(x - 2, y + 3));
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y));
 }
 }
 }

 @Test
 public void testApplyGrayImageNegativeXOffsetPositiveYOffsetWrap() {
 Shift shift = new Shift(-2, 3, Shift.WRAP);
 GrayImage input = new GrayImage(5, 5);
 GrayImage expectedOutput = new GrayImage(5, 5);
 for (int y = 0; y < input.Y(); y++) {
 for (int x = 0; x < input.X(); x++) {
 expectedOutput.set((x + 5 - 2) % 5, (y + 3) % 5, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y));
 }
 }
 }

 @Test
 public void testApplyGrayImagePositiveXOffsetNegativeYOffsetNoWrap() {
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 GrayImage input = new GrayImage(5, 5);
 GrayImage expectedOutput = new GrayImage(5, 5);
 for (int x = 0; x < input.X(); x++) {
 for (int y = 0; y < input.Y(); y++) {
 expectedOutput.set(x, y, input.get(x + 2, y - 3));
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y));
 }
 }
 }

 @Test
 public void testApplyGrayImagePositiveXOffsetNegativeYOffsetWrap() {
 Shift shift = new Shift(2, -3, Shift.WRAP);
 GrayImage input = new GrayImage(5, 5);
 GrayImage expectedOutput = new GrayImage(5, 5);
 for (int y = 0; y < input.Y(); y++) {
 for (int x = 0; x < input.X(); x++) {
 expectedOutput.set((x + 2) % 5, (y + 5 - 3) % 5, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y));
 }
 }
 }

 @Test
 public void testApplyGrayImageNegativeXOffsetNegativeYOffsetNoWrap() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 GrayImage input = new GrayImage(5, 5);
 GrayImage expectedOutput = new GrayImage(5, 5);
 for (int x = 0; x < input.X(); x++) {
 for (int y = 0; y < input.Y(); y++) {
 expectedOutput.set(x, y, input.get(x - 2, y - 3));
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y));
 }
 }
 }

 @Test
 public void testApplyGrayImageNegativeXOffsetNegativeYOffsetWrap() {
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 GrayImage input = new GrayImage(5, 5);
 GrayImage expectedOutput = new GrayImage(5, 5);
 for (int y = 0; y < input.Y(); y++) {
 for (int x = 0; x < input.X(); x++) {
 expectedOutput.set((x + 5 - 2) % 5, (y + 5 - 3) % 5, input.get(x, y));
 }
 }
 GrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y));
 }
 }
 }

 @Test
 public void testApplyRealGrayImagePositiveXOffsetPositiveYOffsetNoWrap() {
 Shift shift = new Shift(2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 RealGrayImage expectedOutput = new RealGrayImage(5, 5);
 for (int x = 0; x < input.X(); x++) {
 for (int y = 0; y < input.Y(); y++) {
 expectedOutput.set(x, y, input.get(x + 2, y + 3));
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImagePositiveXOffsetPositiveYOffsetWrap() {
 Shift shift = new Shift(2, 3, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 RealGrayImage expectedOutput = new RealGrayImage(5, 5);
 for (int y = 0; y < input.Y(); y++) {
 for (int x = 0; x < input.X(); x++) {
 expectedOutput.set((x + 2) % 5, (y + 3) % 5, input.get(x, y));
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageNegativeXOffsetPositiveYOffsetNoWrap() {
 Shift shift = new Shift(-2, 3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 RealGrayImage expectedOutput = new RealGrayImage(5, 5);
 for (int x = 0; x < input.X(); x++) {
 for (int y = 0; y < input.Y(); y++) {
 expectedOutput.set(x, y, input.get(x - 2, y + 3));
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageNegativeXOffsetPositiveYOffsetWrap() {
 Shift shift = new Shift(-2, 3, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 RealGrayImage expectedOutput = new RealGrayImage(5, 5);
 for (int y = 0; y < input.Y(); y++) {
 for (int x = 0; x < input.X(); x++) {
 expectedOutput.set((x + 5 - 2) % 5, (y + 3) % 5, input.get(x, y));
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImagePositiveXOffsetNegativeYOffsetNoWrap() {
 Shift shift = new Shift(2, -3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 RealGrayImage expectedOutput = new RealGrayImage(5, 5);
 for (int x = 0; x < input.X(); x++) {
 for (int y = 0; y < input.Y(); y++) {
 expectedOutput.set(x, y, input.get(x + 2, y - 3));
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImagePositiveXOffsetNegativeYOffsetWrap() {
 Shift shift = new Shift(2, -3, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 RealGrayImage expectedOutput = new RealGrayImage(5, 5);
 for (int y = 0; y < input.Y(); y++) {
 for (int x = 0; x < input.X(); x++) {
 expectedOutput.set((x + 2) % 5, (y + 5 - 3) % 5, input.get(x, y));
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageNegativeXOffsetNegativeYOffsetNoWrap() {
 Shift shift = new Shift(-2, -3, Shift.NOWRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 RealGrayImage expectedOutput = new RealGrayImage(5, 5);
 for (int x = 0; x < input.X(); x++) {
 for (int y = 0; y < input.Y(); y++) {
 expectedOutput.set(x, y, input.get(x - 2, y - 3));
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageNegativeXOffsetNegativeYOffsetWrap() {
 Shift shift = new Shift(-2, -3, Shift.WRAP);
 RealGrayImage input = new RealGrayImage(5, 5);
 RealGrayImage expectedOutput = new RealGrayImage(5, 5);
 for (int y = 0; y < input.Y(); y++) {
 for (int x = 0; x < input.X(); x++) {
 expectedOutput.set((x + 5 - 2) % 5, (y + 5 - 3) % 5, input.get(x, y));
 }
 }
 RealGrayImage output = shift.apply(input);
 for (int x = 0; x < output.X(); x++) {
 for (int y = 0; y < output.Y(); y++) {
 assertEquals(expectedOutput.get(x, y), output.get(x, y), 0.001f);
 }
 }
 }
}