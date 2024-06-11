package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.Shift;
import jigl.image.*;
import jigl.image.types.*;

import org.junit.Test;

public class ShiftTest {

 @Test
 public void testShiftGrayImage() {
 GrayImage input = new GrayImage(5, 5);
 Shift shift = new Shift(1, 1, Shift.WRAP);
 GrayImage output = shift.apply(input);

 // Testing with positive offset and wrap mode
 int expectedX = 2;
 int expectedY = 2;
 for (int i = 0; i < input.X(); i++) {
 for (int j = 0; j < input.Y(); j++) {
 int actualX = output.getX() + i;
 int actualY = output.getY() + j;
 assertEquals("Expected pixel at (" + i + ", " + j + ") to be at (" + expectedX + ", " + expectedY + ")", expectedX, actualX);
 expectedX = (expectedX + 1) % input.X();
 expectedY = (expectedY + 1) % input.Y();
 }
 }

 input = new GrayImage(5, 5);
 shift = new Shift(1, 1, Shift.NOWRAP);
 output = shift.apply(input);

 // Testing with positive offset and no wrap mode
 expectedX = 1;
 expectedY = 1;
 for (int i = 0; i < input.X(); i++) {
 for (int j = 0; j < input.Y(); j++) {
 int actualX = output.getX() + i;
 int actualY = output.getY() + j;
 if (actualX >= 0 && actualX < input.X() && actualY >= 0 && actualY < input.Y()) {
 assertEquals("Expected pixel at (" + i + ", " + j + ") to be at (" + expectedX + ", " + expectedY + ")", expectedX, actualX);
 } else {
 fail("Expected pixel at (" + i + ", " + j + ") to be within the input image boundaries");
 }
 expectedX++;
 expectedY++;
 }
 }

 input = new GrayImage(5, 5);
 shift = new Shift(-1, 1, Shift.WRAP);
 output = shift.apply(input);

 // Testing with negative offset and wrap mode
 expectedX = 3;
 expectedY = 1;
 for (int i = 0; i < input.X(); i++) {
 for (int j = 0; j < input.Y(); j++) {
 int actualX = output.getX() + i;
 int actualY = output.getY() + j;
 assertEquals("Expected pixel at (" + i + ", " + j + ") to be at (" + expectedX + ", " + expectedY + ")", expectedX, actualX);
 expectedX = (expectedX - 1 + input.X()) % input.X();
 expectedY = actualY;
 }
 }

 input = new GrayImage(5, 5);
 shift = new Shift(-1, 1, Shift.NOWRAP);
 output = shift.apply(input);

 // Testing with negative offset and no wrap mode
 expectedX = input.X() - 2;
 expectedY = 1;
 for (int i = 0; i < input.X(); i++) {
 for (int j = 0; j < input.Y(); j++) {
 int actualX = output.getX() + i;
 int actualY = output.getY() + j;
 if (actualX >= 0 && actualX < input.X() && actualY >= 0 && actualY < input.Y()) {
 fail("Expected pixel at (" + i + ", " + j + ") to be outside the input image boundaries");
 }
 }
 }
 }

 @Test
 public void testShiftRealGrayImage() {
 RealGrayImage input = new RealGrayImage(5, 5, new float[5 * 5]);
 Shift shift = new Shift(1, 1, Shift.WRAP);
 RealGrayImage output = shift.apply(input);

 // Testing with positive offset and wrap mode
 float expectedValue = 0.5f;
 for (int i = 0; i < input.X(); i++) {
 for (int j = 0; j < input.Y(); j++) {
 float actualX = output.getX() + i;
 float actualY = output.getY() + j;
 assertEquals("Expected value at (" + i + ", " + j + ") to be " + expectedValue, expectedValue, output.getSample(i, j), 0.001f);
 expectedValue += 0.1f;
 }
 }

 input = new RealGrayImage(5, 5, new float[5 * 5]);
 shift = new Shift(1, 1, Shift.NOWRAP);
 output = shift.apply(input);

 // Testing with positive offset and no wrap mode
 expectedValue = 1.0f;
 for (int i = 0; i < input.X(); i++) {
 for (int j = 0; j < input.Y(); j++) {
 float actualX = output.getX() + i;
 float actualY = output.getY() + j;
 if (actualX >= 0 && actualX < input.X() && actualY >= 0 && actualY < input.Y()) {
 assertEquals("Expected value at (" + i + ", " + j + ") to be " + expectedValue, expectedValue, output.getSample(i, j), 0.001f);
 } else {
 fail("Expected value at (" + i + ", " + j + ") to be within the input image boundaries");
 }
 expectedValue++;
 }
 }

 input = new RealGrayImage(5, 5, new float[5 * 5]);
 shift = new Shift(-1, 1, Shift.WRAP);
 output = shift.apply(input);

 // Testing with negative offset and wrap mode
 expectedValue = 4.5f;
 for (int i = 0; i < input.X(); i++) {
 for (int j = 0; j < input.Y(); j++) {
 float actualX = output.getX() + i;
 float actualY = output.getY() + j;
 assertEquals("Expected value at (" + i + ", " + j + ") to be " + expectedValue, expectedValue, output.getSample(i, j), 0.001f);
 expectedValue -= 0.1f;
 }
 }

 input = new RealGrayImage(5, 5, new float[5 * 5]);
 shift = new Shift(-1, 1, Shift.NOWRAP);
 output = shift.apply(input);

 // Testing with negative offset and no wrap mode
 expectedValue = 0.0f;
 for (int i = 0; i < input.X(); i++) {
 for (int j = 0; j < input.Y(); j++) {
 float actualX = output.getX() + i;
 float actualY = output.getY() + j;
 if (actualX >= 0 && actualX < input.X() && actualY >= 0 && actualY < input.Y()) {
 fail("Expected value at (" + i + ", " + j + ") to be outside the input image boundaries");
 }
 }
 }
 }

 @Test(expected = ImageOutOfBoundsException.class)
 public void testShiftGrayImageOutOfBounds() {
 GrayImage input = new GrayImage(5, 5);
 Shift shift = new Shift(5, 5, Shift.WRAP);
 shift.apply(input);
 }

 @Test(expected = ImageOutOfBoundsException.class)
 public void testShiftRealGrayImageOutOfBounds() {
 RealGrayImage input = new RealGrayImage(5, 5, new float[5 * 5]);
 Shift shift = new Shift(5, 5, Shift.WRAP);
 shift.apply(input);
 }
}