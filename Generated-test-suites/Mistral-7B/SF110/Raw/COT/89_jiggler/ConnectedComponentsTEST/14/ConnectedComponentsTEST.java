import junit.framework.TestCase;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;
 private BinaryImage binaryImage;

 @ org.junit.Before
 public void setUp() {
 // Create a new ConnectedComponents object with 4-connected neighbors and a background of 0
 try {
 cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 } catch (InvalidArgumentException e) {
 e.printStackTrace();
 fail("Failed to initialize ConnectedComponents object with valid arguments.");
 }

 // Create a sample BinaryImage for testing
 binaryImage = new BinaryImage(5, 5);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(4, 1, 1);
 binaryImage.set(4, 2, 1);
 binaryImage.set(4, 3, 1);
 binaryImage.set(4, 4, 1);
 binaryImage.set(4, 5, 1);
 }

 @ org.junit.Test
 public void testConnectedComponentsConstructor() {
 // Test the constructor with valid arguments
 try {
 cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
 } catch (InvalidArgumentException e) {
 fail("Expected no exception when initializing ConnectedComponents object with valid arguments.");
 }

 // Test the constructor with invalid arguments
 try {
 cc = new ConnectedComponents(2, 0);
 fail("Expected InvalidArgumentException when initializing ConnectedComponents object with invalid arguments.");
 } catch (InvalidArgumentException e) {
 // Expected exception
 }

 try {
 cc = new ConnectedComponents(0, 2);
 fail("Expected InvalidArgumentException when initializing ConnectedComponents object with invalid arguments.");
 } catch (InvalidArgumentException e) {
 // Expected exception
 }
 }

 @ org.junit.Test
 public void testApply() {
 // Test the apply method with a 4-connected ConnectedComponents object
 GrayImage expected = new GrayImage(5, 5);
 expected.set(1, 1, 1);
 expected.set(1, 2, 1);
 expected.set(2, 1, 1);
 expected.set(2, 2, 1);
 expected.set(2, 3, 1);
 expected.set(3, 1, 1);
 expected.set(3, 2, 1);
 expected.set(3, 3, 1);
 expected.set(3, 4, 1);
 expected.set(4, 1, 1);
 expected.set(4, 2, 1);
 expected.set(4, 3, 1);
 expected.set(4, 4, 1);
 expected.set(4, 5, 1);

 GrayImage actual = cc.apply(binaryImage);

 // Check that the expected and actual GrayImages are equal
 TestCase.assertEquals(expected, actual);

 // Test the apply method with a 8-connected ConnectedComponents object
 ConnectedComponents eightConnected = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
 GrayImage eightConnectedExpected = new GrayImage(5, 5);
 eightConnectedExpected.set(1, 1, 1);
 eightConnectedExpected.set(1, 2, 1);
 eightConnectedExpected.set(2, 1, 1);
 eightConnectedExpected.set(2, 2, 1);
 eightConnectedExpected.set(2, 3, 1);
 eightConnectedExpected.set(2, 4, 1);
 eightConnectedExpected.set(2, 5, 1);
 eightConnectedExpected.set(3, 1, 1);
 eightConnectedExpected.set(3, 2, 1);
 eightConnectedExpected.set(3, 3, 1);
 eightConnectedExpected.set(3, 4, 1);
 eightConnectedExpected.set(3, 5, 1);
 eightConnectedExpected.set(3, 6, 1);
 eightConnectedExpected.set(4, 1, 1);
 eightConnectedExpected.set(4, 2, 1);
 eightConnectedExpected.set(4, 3, 1);
 eightConnectedExpected.set(4, 4, 1);
 eightConnectedExpected.set(4, 5, 1);
 eightConnectedExpected.set(4, 6, 1);
 eightConnectedExpected.set(4, 7, 1);
 eightConnectedExpected.set(5, 1, 1);
 eightConnectedExpected.set(5, 2, 1);
 eightConnectedExpected.set(5, 3, 1);
 eightConnectedExpected.set(5, 4, 1);
 eightConnectedExpected.set(5, 5, 1);
 eightConnectedExpected.set(5, 6, 1);
 eightConnectedExpected.set(5, 7, 1);
 eightConnectedExpected.set(5, 8, 1);

 GrayImage eightConnectedActual = eightConnected.apply(binaryImage);

 // Check that the expected and actual GrayImages are equal
 TestCase.assertEquals(eightConnectedExpected, eightConnectedActual);
 }

 @ org.junit.Test(expected = InvalidArgumentException.class)
 public void testApplyInvalidArgumentException() {
 // Test the apply method with a ConnectedComponents object with invalid arguments
 ConnectedComponents invalidArguments = new ConnectedComponents(1, 2);
 try {
 invalidArguments.apply(binaryImage);
 fail("Expected InvalidArgumentException when calling apply method with invalid ConnectedComponents object.");
 } catch (InvalidArgumentException e) {
 // Expected exception
 }
 }
}