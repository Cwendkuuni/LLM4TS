import junit.framework.TestCase;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;
 private BinaryImage binaryImage;
 private GrayImage expectedOutput, actualOutput;

 @Before
 public void setUp() {
 // Initialize ConnectedComponents object with valid arguments
 cc = new ConnectedComponents(0, 0);
 // Initialize input BinaryImage
 binaryImage = new BinaryImage(3, 3);
 binaryImage.set(0, 0, 1);
 binaryImage.set(1, 1, 1);
 binaryImage.set(2, 0, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 }

 @Test
 public void testConnectedComponentsConstructor() {
 // Test constructor
 try {
 cc = new ConnectedComponents(-1, 0);
 fail("InvalidArgumentException expected");
 } catch (InvalidArgumentException e) {
 // Expected
 }
 try {
 cc = new ConnectedComponents(0, -1);
 fail("InvalidArgumentException expected");
 } catch (InvalidArgumentException e) {
 // Expected
 }
 try {
 cc = new ConnectedComponents(2, 0);
 fail("InvalidArgumentException expected");
 } catch (InvalidArgumentException e) {
 // Expected
 }
 try {
 cc = new ConnectedComponents(0, 2);
 fail("InvalidArgumentException expected");
 } catch (InvalidArgumentException e) {
 // Expected
 }
 try {
 cc = new ConnectedComponents(0, 0);
 // Success
 } catch (InvalidArgumentException e) {
 fail("Unexpected InvalidArgumentException");
 }
 }

 @Test
 public void testApply() {
 // Test apply method with 4-connected neighbor hood
 expectedOutput = new GrayImage(3, 3);
 expectedOutput.set(0, 0, 1);
 expectedOutput.set(1, 1, 2);
 expectedOutput.set(2, 0, 2);
 expectedOutput.set(2, 1, 2);
 expectedOutput.set(2, 2, 2);
 // Perform ConnectedComponents operation
 actualOutput = cc.apply(binaryImage);
 // Compare expected and actual output
 TestCase.assertEquals(expectedOutput, actualOutput);
 // Test apply method with 8-connected neighbor hood
 cc = new ConnectedComponents(1, 0);
 expectedOutput = new GrayImage(3, 3);
 expectedOutput.set(0, 0, 1);
 expectedOutput.set(1, 1, 2);
 expectedOutput.set(1, 2, 2);
 expectedOutput.set(2, 1, 2);
 expectedOutput.set(2, 2, 2);
 // Perform ConnectedComponents operation
 actualOutput = cc.apply(binaryImage);
 // Compare expected and actual output
 TestCase.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testApplyWithBorderPixels() {
 // Test apply method with border pixels
 binaryImage = new BinaryImage(4, 4);
 binaryImage.set(0, 0, 1);
 binaryImage.set(1, 0, 1);
 binaryImage.set(2, 0, 1);
 binaryImage.set(3, 0, 1);
 binaryImage.set(3, 1, 1);
 binaryImage.set(3, 2, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(3, 4, 1);
 binaryImage.set(2, 3, 1);
 binaryImage.set(1, 3, 1);
 binaryImage.set(0, 3, 1);
 // Perform ConnectedComponents operation
 actualOutput = cc.apply(binaryImage);
 // Check if all border pixels have the same label
 int label = actualOutput.get(0, 0).intValue();
 for (int x = 0; x < binaryImage.X(); ++x) {
 for (int y = 0; y < binaryImage.Y(); ++y) {
 if (binaryImage.get(x, y) != 0 && binaryImage.get(x, y) != -1) {
 int actualLabel = actualOutput.get(x, y).intValue();
 if (actualLabel != label) {
 fail("All border pixels should have the same label");
 }
 }
 }
 }
 }

 @Test
 public void testApplyWithDisconnectedComponents() {
 // Test apply method with disconnected components
 binaryImage = new BinaryImage(4, 4);
 binaryImage.set(1, 1, 1);
 binaryImage.set(1, 2, 1);
 binaryImage.set(2, 1, 1);
 binaryImage.set(2, 2, 1);
 binaryImage.set(3, 3, 1);
 binaryImage.set(3, 4, 1);
 // Perform ConnectedComponents operation
 actualOutput = cc.apply(binaryImage);
 // Check if there are 3 connected components in the output
 int componentsCount = 0;
 for (int x = 0; x < actualOutput.X(); ++x) {
 for (int y = 0; y < actualOutput.Y(); ++y) {
 int label = actualOutput.get(x, y).intValue();
 if (label > 0) {
 ++componentsCount;
 }
 }
 }
 TestCase.assertEquals(3, componentsCount);
 }

 @Test
 public void testApplyWithSinglePixelComponent() {
 // Test apply method with single pixel component
 binaryImage = new BinaryImage(1, 1);
 binaryImage.set(0, 0, 1);
 // Perform ConnectedComponents operation
 actualOutput = cc.apply(binaryImage);
 // Check if the output has only 1 pixel with label 1
 TestCase.assertEquals(1, actualOutput.X());
 TestCase.assertEquals(1, actualOutput.Y());
 TestCase.assertEquals(1, actualOutput.get(0, 0).intValue());
 }

 @Test
 public void testApplyWithEmptyImage() {
 // Test apply method with empty image
 binaryImage = new BinaryImage(0, 0);
 // Perform ConnectedComponents operation
 actualOutput = cc.apply(binaryImage);
 // Check if the output is empty
 TestCase.assertEquals(0, actualOutput.X());
 TestCase.assertEquals(0, actualOutput.Y());
 }

}