package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws IOException {
  File file = new File("testData.arff"); // Replace with the path to your ARFF file
  Dataset expectedDataset = createExpectedDataset();
  Dataset actualDataset = ARFFHandler.loadARFF(file);
  assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
  assertEquals(expectedDataset.attributeNames(), actualDataset.attributeNames());
  for (int i = 0; i < expectedDataset.numInstances(); i++) {
   Instance expectedInstance = expectedDataset.instance(i);
   Instance actualInstance = actualDataset.instance(i);
   assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray());
  }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
  File file = new File("invalidFile.arff"); // Replace with the path to an invalid file
  ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
  File file = new File("testData.arff"); // Replace with the path to your ARFF file
  Dataset expectedDataset = createExpectedDataset();
  Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
  assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
  assertEquals(expectedDataset.attributeNames(), actualDataset.attributeNames());
  for (int i = 0; i < expectedDataset.numInstances(); i++) {
   Instance expectedInstance = expectedDataset.instance(i);
   Instance actualInstance = actualDataset.instance(i);
   assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray());
   assertNull(actualInstance.classValue());
   assertArraySubarrayEquals(expectedInstance.toArray(1, expectedInstance.numAttributes() - 1), actualInstance.toArray(1, actualInstance.numAttributes() - 1));
  }
 }

 private Dataset createExpectedDataset() throws IOException {
  // Replace this with the creation of the expected Dataset using the ARFFHandler
  // Add instances and attributes as needed
  Dataset expectedDataset = new DefaultDataset();
  expectedDataset.add(new DenseInstance(new double[] {1.0, 2.0, "class1"}));
  expectedDataset.add(new DenseInstance(new double[] {2.0, 3.0, "class2"}));
  return expectedDataset;
 }

}