import junit.framework.Assert;
import junit.framework.TestCase;
import jigl.image.ops.Shift;
import jigl. image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.exceptions.ImageProcessingException;

public class ShiftTest extends TestCase {

 private Shift shift;
 private GrayImage inputImage;
 private RealGrayImage realInputImage;
 private RealGrayImage expectedOutput;

 @Before
 public void setUp() {
 // Initialize the shift object with specific values
 shift = new Shift(2, 2, Shift.WRAP);
 // Initialize input image with some values for testing
 inputImage = new GrayImage(5, 5);
 for (int i = 0; i < inputImage.X(); i++) {
 for (int j = 0; j < inputImage.Y(); j++) {
 inputImage.set(i, j, (byte) i);
 }
 }
 // Initialize real input image with some values for testing
 realInputImage = new RealGrayImage(5, 5);
 for (int i = 0; i < realInputImage.X(); i++) {
 for (int j = 0; j < realInputImage.Y(); j++) {
 realInputImage.set(i, j, i / 10.0f);
 }
 }
 }

 // Test constructor
 public void testShiftConstructor() {
 Shift shiftObj = new Shift(3, 4, Shift.NOWRAP);
 assertEquals(3, shiftObj.getXoffset());
 assertEquals(4, shiftObj.getYoffset());
 assertEquals(false, shiftObj.isWrap());
 }

 // Test apply method for GrayImage with positive offsets and no wrapping
 public void testApplyGrayImagePositiveNoWrap() {
 GrayImage output = shift.apply(inputImage);
 for (int i = 0; i < output.X(); i++) {
 for (int j = 0; j < output.Y(); j++) {
 int expected = inputImage.get(i, j).getValue() + 2;
 int actual = output.get(i, j).getValue();
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
 }
 }
 }

 // Test apply method for GrayImage with positive offsets and wrapping
 public void testApplyGrayImagePositiveWrap() {
 GrayImage output = shift.apply(inputImage);
 shift = new Shift(2, 2, Shift.WRAP);
 GrayImage outputWrapped = shift.apply(output);
 for (int i = 0; i < outputWrapped.X(); i++) {
 for (int j = 0; j < outputWrapped.Y(); j++) {
 int expected = inputImage.get((i + 2) % inputImage.X(), (j + 2) % inputImage.Y()).getValue();
 int actual = outputWrapped.get(i, j).getValue();
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
 }
 }
 }

 // Test apply method for RealGrayImage with positive offsets and no wrapping
 public void testApplyRealGrayImagePositiveNoWrap() {
 RealGrayImage output = shift.apply(realInputImage);
 for (int i = 0; i < output.X(); i++) {
 for (int j = 0; j < output.Y(); j++) {
 float expected = realInputImage.get(i, j).getValue() + 2.0f / 10.0f;
 float actual = output.get(i, j).getValue();
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual, 0.001f);
 }
 }
 }

 // Test apply method for RealGrayImage with positive offsets and wrapping
 public void testApplyRealGrayImagePositiveWrap() {
 RealGrayImage output = shift.apply(realInputImage);
 shift = new Shift(2, 2, Shift.WRAP);
 RealGrayImage outputWrapped = shift.apply(output);
 for (int i = 0; i < outputWrapped.X(); i++) {
 for (int j = 0; j < outputWrapped.Y(); j++) {
 float expected = realInputImage.get((i + 2) % realInputImage.X(), (j + 2) % realInputImage.Y()).getValue();
 float actual = outputWrapped.get(i, j).getValue();
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual, 0.001f);
 }
 }
 }

 // Test apply method for GrayImage with negative offsets and no wrapping
 public void testApplyGrayImageNegativeNoWrap() {
 // Initialize a new shift object with negative offsets
 shift = new Shift(-2, -2, Shift.NOWRAP);
 GrayImage output = shift.apply(inputImage);
 for (int i = 0; i < output.X(); i++) {
 for (int j = 0; j < output.Y(); j++) {
 int expected = inputImage.get(i, j).getValue() - 2;
 int actual = output.get(i, j).getValue();
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
 }
 }
 }

 // Test apply method for GrayImage with negative offsets and wrapping
 public void testApplyGrayImageNegativeWrap() {
 // Initialize a new shift object with negative offsets
 shift = new Shift(-2, -2, Shift.WRAP);
 GrayImage output = shift.apply(inputImage);
 shift = new Shift(2, 2, Shift.WRAP);
 GrayImage outputWrapped = shift.apply(output);
 for (int i = 0; i < outputWrapped.X(); i++) {
 for (int j = 0; j < outputWrapped.Y(); j++) {
 int expected = inputImage.get((i + 4) % inputImage.X(), (j + 4) % inputImage.Y()).getValue();
 int actual = outputWrapped.get(i, j).getValue();
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual);
 }
 }
 }

 // Test apply method for RealGrayImage with negative offsets and no wrapping
 public void testApplyRealGrayImageNegativeNoWrap() {
 // Initialize a new shift object with negative offsets
 shift = new Shift(-2, -2, Shift.NOWRAP);
 RealGrayImage output = shift.apply(realInputImage);
 for (int i = 0; i < output.X(); i++) {
 for (int j = 0; j < output.Y(); j++) {
 float expected = realInputImage.get(i, j).getValue() - 2.0f / 10.0f;
 float actual = output.get(i, j).getValue();
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual, 0.001f);
 }
 }
 }

 // Test apply method for RealGrayImage with negative offsets and wrapping
 public void testApplyRealGrayImageNegativeWrap() {
 // Initialize a new shift object with negative offsets
 shift = new Shift(-2, -2, Shift.WRAP);
 RealGrayImage output = shift.apply(realInputImage);
 shift = new Shift(2, 2, Shift.WRAP);
 RealGrayImage outputWrapped = shift.apply(output);
 for (int i = 0; i < outputWrapped.X(); i++) {
 for (int j = 0; j < outputWrapped.Y(); j++) {
 float expected = realInputImage.get((i + 4) % realInputImage.X(), (j + 4) % realInputImage.Y()).getValue();
 float actual = outputWrapped.get(i, j).getValue();
 assertEquals("Expected: " + expected + ", but was: " + actual, expected, actual, 0.001f);
 }
 }
 }

 // Test apply method for GrayImage with offsets outside the image bounds (x)
 public void testApplyGrayImageXOutOfBounds() {
 shift = new Shift(inputImage.X() + 1, 0, Shift.NOWRAP);
 GrayImage output = shift.apply(inputImage);
 assertNull("Expected null, but was: " + output, output);
 }

 // Test apply method for GrayImage with offsets outside the image bounds (y)
 public void testApplyGrayImageYOutOfBounds() {
 shift = new Shift(0, inputImage.Y() + 1, Shift.NOWRAP);
 GrayImage output = shift.apply(inputImage);
 assertNull("Expected null, but was: " + output, output);
 }

 // Test apply method for RealGrayImage with offsets outside the image bounds (x)
 public void testApplyRealGrayImageXOutOfBounds() {
 shift = new Shift(realInputImage.X() + 1, 0, Shift.NOWRAP);
 RealGrayImage output = shift.apply(realInputImage);
 assertNull("Expected null, but was: " + output, output);
 }

 // Test apply method for RealGrayImage with offsets outside the image bounds (y)
 public void testApplyRealGrayImageYOutOfBounds() {
 shift = new Shift(0, realInputImage.Y() + 1, Shift.NOWRAP);
 RealGrayImage output = shift.apply(realInputImage);
 assertNull("Expected null, but was: " + output, output);
 }

 // Test apply method for GrayImage with invalid wrap value
 public void testApplyGrayImageInvalidWrapValue() {
 shift = new Shift(2, 2, 3);
 try {
 shift.apply(inputImage);
 fail("Expected ImageProcessingException, but no exception was thrown");
 } catch (ImageProcessingException e) {
 // Expected exception
 }
 }

 // Test apply method for RealGrayImage with invalid wrap value
 public void testApplyRealGrayImageInvalidWrapValue() {
 shift = new Shift(2, 2, 3);
 try {
 shift.apply(realInputImage);
 fail("Expected ImageProcessingException, but no exception was thrown");
 } catch (ImageProcessingException e) {
 // Expected exception
 }
 }
}