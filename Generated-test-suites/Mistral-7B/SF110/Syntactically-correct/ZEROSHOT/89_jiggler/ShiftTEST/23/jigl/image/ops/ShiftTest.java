package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.types.*;
import org.junit.*;

import jigl.image.*;
import jigl.image.ops.Shift;

public class ShiftTest {

 @BeforeClass
 public static void setupClass() {
 // Set up any class-level resources or initializations here
 }

 @AfterClass
 public static void tearDownClass() {
 // Clean up any class-level resources or finalizations here
 }

 @Before
 public void setup() {
 // Set up any test-level resources or initializations here
 }

 @After
 public void tearDown() {
 // Clean up any test-level resources or finalizations here
 }

 @Test
 public void testShiftConstructor() {
 Shift shift = new Shift(3, 4, Shift.WRAP);
 assertEquals(3, shift.getXoffset());
 assertEquals(4, shift.getYoffset());
 assertTrue(shift.isWrap());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShiftConstructorInvalidWrap() {
 new Shift(3, 4, -1);
 }

 @Test
 public void testApplyGrayImagePositiveShift() {
 final int X = 5;
 final int Y = 5;
 final GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 image.set(i % X, i % Y, (byte) i);
 }

 final Shift shift = new Shift(2, 2, Shift.NOWRAP);
 final GrayImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = i + 2;
 int actual = result.get(x, y);
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageNegativeShift() {
 final int X = 5;
 final int Y = 5;
 final GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 image.set(i % X, i % Y, (byte) i);
 }

 final Shift shift = new Shift(-2, -2, Shift.NOWRAP);
 final GrayImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int expected = i - 2;
 int actual = result.get(x, y);
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyGrayImageWrap() {
 final int X = 5;
 final int Y = 5;
 final GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 image.set(i % X, i % Y, (byte) i);
 }

 final Shift shift = new Shift(2, 2, Shift.WRAP);
 final GrayImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int xNew = (x + 2) % X;
 int yNew = (y + 2) % Y;
 int expected = image.get(xNew, yNew);
 int actual = result.get(x, y);
 assertEquals(expected, actual);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageOutOfBounds() {
 final int X = 5;
 final int Y = 5;
 final GrayImage image = new GrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 image.set(i % X, i % Y, (byte) i);
 }

 final Shift shift = new Shift(X + 2, Y + 2, Shift.NOWRAP);
 final GrayImage result = shift.apply(image);
 assertNull(result);
 }

 @Test
 public void testApplyRealGrayImagePositiveShift() {
 final int X = 5;
 final int Y = 5;
 final RealGrayImage image = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 image.set(i % X, i % Y, (double) i / 10.0);
 }

 final Shift shift = new Shift(2, 2, Shift.NOWRAP);
 final RealGrayImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 double expected = i + 2.0 / 10.0;
 double actual = result.get(x, y);
 assertEquals(expected, actual, 0.01);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageNegativeShift() {
 final int X = 5;
 final int Y = 5;
 final RealGrayImage image = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 image.set(i % X, i % Y, (double) i / 10.0);
 }

 final Shift shift = new Shift(-2, -2, Shift.NOWRAP);
 final RealGrayImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 double expected = i - 2.0 / 10.0;
 double actual = result.get(x, y);
 assertEquals(expected, actual, 0.01);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageWrap() {
 final int X = 5;
 final int Y = 5;
 final RealGrayImage image = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 image.set(i % X, i % Y, (double) i / 10.0);
 }

 final Shift shift = new Shift(2, 2, Shift.WRAP);
 final RealGrayImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int xNew = (x + 2) % X;
 int yNew = (y + 2) % Y;
 double expected = image.get(xNew, yNew);
 double actual = result.get(x, y);
 assertEquals(expected, actual, 0.01);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageOutOfBounds() {
 final int X = 5;
 final int Y = 5;
 final RealGrayImage image = new RealGrayImage(X, Y);
 for (int i = 0; i < X * Y; ++i) {
 image.set(i % X, i % Y, (double) i / 10.0);
 }

 final Shift shift = new Shift(X + 2, Y + 2, Shift.NOWRAP);
 final RealGrayImage result = shift.apply(image);
 assertNull(result);
 }

 @Test
 public void testApplyColorImagePositiveShift() {
 final int X = 5;
 final int Y = 5;
 final ColorImage image = new ColorImage(X, Y);
 for (int channel = 0; channel < 3; ++channel) {
 for (int i = 0; i < X * Y; ++i) {
 image.setPixel(i % X, i % Y, channel, (byte) i);
 }
 }

 final Shift shift = new Shift(2, 2, Shift.NOWRAP);
 final ColorImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int channel = 0; channel < 3; ++channel) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int rExpected = i + 2;
 int gExpected = i + 2;
 int bExpected = i + 2;
 int rActual = result.getRed(x, y);
 int gActual = result.getGreen(x, y);
 int bActual = result.getBlue(x, y);
 assertEquals(rExpected, rActual);
 assertEquals(gExpected, gActual);
 assertEquals(bExpected, bActual);
 }
 }
 }
 }

 @Test
 public void testApplyColorImageNegativeShift() {
 final int X = 5;
 final int Y = 5;
 final ColorImage image = new ColorImage(X, Y);
 for (int channel = 0; channel < 3; ++channel) {
 for (int i = 0; i < X * Y; ++i) {
 image.setPixel(i % X, i % Y, channel, (byte) i);
 }
 }

 final Shift shift = new Shift(-2, -2, Shift.NOWRAP);
 final ColorImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int channel = 0; channel < 3; ++channel) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int rExpected = i - 2;
 int gExpected = i - 2;
 int bExpected = i - 2;
 int rActual = result.getRed(x, y);
 int gActual = result.getGreen(x, y);
 int bActual = result.getBlue(x, y);
 assertEquals(rExpected, rActual);
 assertEquals(gExpected, gActual);
 assertEquals(bExpected, bActual);
 }
 }
 }
 }

 @Test
 public void testApplyColorImageWrap() {
 final int X = 5;
 final int Y = 5;
 final ColorImage image = new ColorImage(X, Y);
 for (int channel = 0; channel < 3; ++channel) {
 for (int i = 0; i < X * Y; ++i) {
 image.setPixel(i % X, i % Y, channel, (byte) i);
 }
 }

 final Shift shift = new Shift(2, 2, Shift.WRAP);
 final ColorImage result = shift.apply(image);
 assertNotNull(result);
 assertEquals(X, result.X());
 assertEquals(Y, result.Y());
 for (int channel = 0; channel < 3; ++channel) {
 for (int y = 0; y < Y; ++y) {
 for (int x = 0; x < X; ++x) {
 int xNew = (x + 2) % X;
 int yNew = (y + 2) % Y;
 int rExpected = image.getRed(xNew, yNew);
 int gExpected = image.getGreen(xNew, yNew);
 int bExpected = image.getBlue(xNew, yNew);
 int rActual = result.getRed(x, y);
 int gActual = result.getGreen(x, y);
 int bActual = result.getBlue(x, y);
 assertEquals(rExpected, rActual);
 assertEquals(gExpected, gActual);
 assertEquals(bExpected, bActual);
 }
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageOutOfBounds() {
 final int X = 5;
 final int Y = 5;
 final ColorImage image = new ColorImage(X, Y);
 for (int channel = 0; channel < 3; ++channel) {
 for (int i = 0; i < X * Y; ++i) {
 image.setPixel(i % X, i % Y, channel, (byte) i);
 }
 }

 final Shift shift = new Shift(X + 2, Y + 2, Shift.NOWRAP);
 final ColorImage result = shift.apply(image);
 assertNull(result);
 }

}