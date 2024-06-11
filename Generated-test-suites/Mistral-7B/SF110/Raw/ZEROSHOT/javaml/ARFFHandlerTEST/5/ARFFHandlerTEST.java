package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

 private static final String TEST_FILE = "test.arff";

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
  File file = new File(TEST_FILE);
  Dataset dataset = ARFFHandler.loadARFF(file);
  assertNotNull(dataset);
  assertEquals(10, dataset.numInstances());
 }

 @Test
 public void testLoadARFFWithEmptyFile() {
  assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(new File("empty.arff")));
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
  File file = new File(TEST_FILE);
  Dataset dataset = ARFFHandler.loadARFF(file, 2);
  assertNotNull(dataset);
  assertEquals(10, dataset.numInstances());
  for (int i = 0; i < dataset.numAttributes(); i++) {
   if (i != 2) {
    assertTrue(dataset.instance(0).value(i) != Double.NaN);
   }
  }
 }

 @Test
 public void testLoadARFFWithInvalidClassIndex() throws IOException {
  File file = new File(TEST_FILE);
  assertThrows(IllegalArgumentException.class, () -> ARFFHandler.loadARFF(file, 13));
 }

 @Test
 public void testLoadARFFWithCommentedLine() throws IOException {
  File file = new File("test_commented.arff");
  Dataset dataset = ARFFHandler.loadARFF(file);
  assertNotNull(dataset);
  assertEquals(5, dataset.numInstances());
 }

 @Test
 public void testLoadARFFWithMixedDataAndComment() throws IOException {
  File file = new File("test_mixed.arff");
  Dataset dataset = ARFFHandler.loadARFF(file);
  assertNotNull(dataset);
  assertEquals(10, dataset.numInstances());
 }

 @Test
 public void testLoadARFFWithMissingData() throws IOException {
  File file = new File("test_missing_data.arff");
  Dataset dataset = ARFFHandler.loadARFF(file);
  assertNotNull(dataset);
  assertEquals(10, dataset.numInstances());
  for (int i = 0; i < dataset.numInstances(); i++) {
   Instance instance = dataset.instance(i);
   for (int j = 0; j < instance.numValues(); j++) {
    if (Double.isNaN(instance.value(j))) {
     assertTrue(instance.value(j) == Double.NaN);
    }
   }
  }
 }

 @Test
 public void testLoadARFFWithInvalidFormat() throws IOException {
  File file = new File("test_invalid_format.arff");
  assertThrows(IllegalArgumentException.class, () -> ARFFHandler.loadARFF(file));
 }

 @Test
 public void testLoadARFFWithInvalidCharset() throws IOException {
  File file = new File("test_invalid_charset.arff");
  InputStream inputStream = new FileInputStream(file);
  InputStreamReader reader = new InputStreamReader(inputStream, "UTF-16BE");
  LineIterator it = new LineIterator(reader);
  it.setSkipBlanks(true);
  it.setCommentIdentifier("%");
  it.setSkipComments(true);
  assertThrows(IOException.class, () -> ARFFHandler.loadARFF(new File(TEST_FILE), it));
 }

 @Test
 public void testLoadARFFWithNullFile() {
  assertThrows(NullPointerException.class, () -> ARFFHandler.loadARFF(null));
 }

 @Test
 public void testLoadARFFWithNullIt() {
  File file = new File(TEST_FILE);
  assertThrows(NullPointerException.class, () -> ARFFHandler.loadARFF(file, null));
 }

 @AfterClass
 public static void cleanUp() {
  File file = new File(TEST_FILE);
  file.delete();
 }

}