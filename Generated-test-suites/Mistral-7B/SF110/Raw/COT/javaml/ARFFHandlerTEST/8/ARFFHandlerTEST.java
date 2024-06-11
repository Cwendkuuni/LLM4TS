package net.sf.javaml.tools.data.test;

 import static org.junit.Assert.*;
 import net.sf.javaml.core.*;
 import java.io.*;

 public class ARFFHandlerTest {

 private final String testFilePath = "src/test/resources/ARFFFiles/test.arff";

 @Test
 public void testLoadARFFFileWithValidData() throws FileNotFoundException {
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0, "Class1"}, new String[]{"Attribute1", "Attribute2", "Attribute3", "Class"}));
 expectedDataset.add(new DenseInstance(new double[]{2.0, 3.0, 4.0, "Class2"}, new String[]{"Attribute1", "Attribute2", "Attribute3", "Class"}));

 Dataset actualDataset = ARFFHandler.loadARFF(new File(testFilePath));

 assertEquals(expectedDataset.size(), actualDataset.size());
 for (int i = 0; i < expectedDataset.size(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 assertEquals(expectedInstance.classValue(), actualInstance.classValue());
 for (int j = 0; j < expectedInstance.numAttributes(); j++) {
 assertEquals(expectedInstance.value(j), actualInstance.value(j), 0.001);
 }
 }
 }

 @Test
 public void testLoadARFFFileWithClassIndex() throws FileNotFoundException {
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0}, new String[]{"Attribute1", "Attribute2"}));
 expectedDataset.add(new DenseInstance(new double[]{2.0, 3.0}, new String[]{"Attribute1", "Attribute2"}));
 expectedDataset.add(new DenseInstance(new String[]{"Class1", "Class2"}, new String[]{"Class"}));

 Dataset actualDataset = ARFFHandler.loadARFF(new File(testFilePath), 1);

 assertEquals(expectedDataset.size(), actualDataset.size());
 for (int i = 0; i < expectedDataset.size(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 if (i < 2) { // First two instances do not have class values
 assertEquals(expectedInstance.numValues(), actualInstance.numValues());
 for (int j = 0; j < expectedInstance.numValues(); j++) {
 assertEquals(expectedInstance.value(j), actualInstance.value(j), 0.001);
 }
 } else { // Third instance has a class value
 assertEquals(expectedInstance.classValue(), actualInstance.classValue());
 }
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFFileWithInvalidFile() {
 ARFFHandler.loadARFF(new File("src/test/resources/ARFFFiles/invalidFile.arff"));
 fail("FileNotFoundException was expected but not thrown.");
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFFileWithInvalidNumericValue() throws FileNotFoundException {
 File file = new File(testFilePath);
 LineIterator it = new LineIterator(file);
 it.setSkipBlanks(true);
 it.setCommentIdentifier("%");
 it.setSkipComments(true);
 String invalidLine = it.next();
 it.close();
 String[] invalidData = invalidLine.split(",");
 Dataset dataset = new DefaultDataset();
 try {
 dataset = ARFFHandler.loadARFF(file, 0);
 fail("NumberFormatException was expected but not thrown.");
 } catch (NumberFormatException e) {
 assertEquals("Expected NumberFormatException but got " + e.getMessage(), "For input string: \"" + invalidData[1] + "\"", e.getMessage());
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testLoadARFFFileWithMissingClassAttribute() throws FileNotFoundException {
 File file = new File(testFilePath);
 LineIterator it = new LineIterator(file);
 it.setSkipBlanks(true);
 it.setCommentIdentifier("%");
 it.setSkipComments(true);
 it.next(); // Consume @data line
 it.next(); // Consume empty line
 it.close();
 String[] missingClassData = it.next().split(",");
 Dataset dataset = new DefaultDataset();
 try {
 dataset = ARFFHandler.loadARFF(new File(testFilePath), missingClassData.length);
 fail("ArrayIndexOutOfBoundsException was expected but not thrown.");
 } catch (ArrayIndexOutOfBoundsException e) {
 assertEquals("Expected ArrayIndexOutOfBoundsException but got " + e.getMessage(), "Index: " + (missingClassData.length + 1), e.getMessage());
 }
 }
 }