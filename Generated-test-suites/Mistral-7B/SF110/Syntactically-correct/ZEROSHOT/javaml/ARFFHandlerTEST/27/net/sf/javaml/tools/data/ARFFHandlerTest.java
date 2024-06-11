package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

 private static final String TEST_FILE = "testData.arff";

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
  File file = new File(TEST_FILE);
  Dataset dataset = ARFFHandler.loadARFF(file);
  assertNotNull(dataset);
  assertEquals(10, dataset.numInstances());
  assertEquals(4, dataset.numAttributes());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
  File file = new File("nonExistentFile.arff");
  ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
  File file = new File(TEST_FILE);
  Dataset dataset = ARFFHandler.loadARFF(file, 2);
  assertNotNull(dataset);
  assertEquals(10, dataset.numInstances());
  assertEquals(4, dataset.numAttributes());
 }

 @Test
 public void testLoadARFFWithInvalidClassIndex() throws IOException {
  File file = new File(TEST_FILE);
  Dataset dataset = ARFFHandler.loadARFF(file, -1);
  assertNotNull(dataset);
  assertEquals(10, dataset.numInstances());
  assertEquals(4, dataset.numAttributes());

  Instance instance = dataset.instance(0);
  assertEquals("class1", instance.stringValue(3));
 }

 @Test
 public void testLoadARFFWithMixedDataTypes() throws IOException {
  File file = new File(TEST_FILE);
  Dataset dataset = ARFFHandler.loadARFF(file);
  assertNotNull(dataset);
  assertEquals(10, dataset.numInstances());
  assertEquals(4, dataset.numAttributes());

  Instance instance = dataset.instance(0);
  assertEquals(1.0, instance.value(0), 0.001);
  assertEquals("string1", instance.stringValue(1));
  assertEquals(3.0, instance.value(2), 0.001);
  assertEquals("class1", instance.stringValue(3));
 }

 @Test
 public void testLoadARFFWithEmptyLine() throws IOException {
  String testData = "// empty line\n@data\n";
  LineIterator it = new LineIterator(new StringReader(testData));
  it.setSkipBlanks(true);
  it.setCommentIdentifier("%");
  it.setSkipComments(true);
  Dataset dataset = new DefaultDataset();
  boolean dataMode = false;

  for (String line : it) {
   if (dataMode) {
    String[] arr = line.split(",");
    double[] values = new double[arr.length];
    for (int i = 0; i < arr.length; ++i) {
     double val;
     try {
      val = Double.parseDouble(arr[i]);
     } catch (NumberFormatException e) {
      val = Double.NaN;
     }
     values[i] = val;
    }
    dataset.add((Instance) new DenseInstance(values, null));
   }
   if (line.equalsIgnoreCase("@data")) {
    dataMode = true;
   }
  }

  assertEquals(1, dataset.numInstances());
  assertEquals(4, dataset.numAttributes());
 }
}