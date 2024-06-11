package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.LineIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Dataset;

@RunWith(MockitoJUnitRunner.class)
public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
  File file = new File("testData.arff"); // Replace with the path to your valid ARFF file
  Dataset expectedDataset = createExpectedDataset(); // Replace with the creation of your expected Dataset
  Dataset actualDataset = ARFFHandler.loadARFF(file);
  assertEquals(expectedDataset, actualDataset);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
  File file = new File("invalidFile.arff"); // Replace with the path to your invalid file
  ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
  File file = new File("testData.arff"); // Replace with the path to your ARFF file
  int classIndex = 2;
  Dataset expectedDataset = createExpectedDataset(); // Replace with the creation of your expected Dataset
  Dataset actualDataset = ARFFHandler.loadARFF(file, classIndex);
  assertEquals(expectedDataset, actualDataset);
 }

 @Test
 public void testLoadARFFWithEmptyFile() throws IOException {
  File file = new File("emptyFile.arff"); // Replace with the path to your empty ARFF file
  Dataset emptyDataset = new DefaultDataset();
  Dataset actualDataset = ARFFHandler.loadARFF(file);
  assertEquals(emptyDataset, actualDataset);
 }

 @Test
 public void testLoadARFFWithSkippedComments() throws IOException {
  LineIterator lineIterator = Mockito.mock(LineIterator.class);
  when(lineIterator.hasNext()).thenReturn(true).thenReturn(false);
  when(lineIterator.next()).thenReturn("@data\n% This is a comment\n1.0, 2.0, 3.0\n4.0, 5.0, 6.0");

  File file = new File("testData.arff"); // Replace with the path to your ARFF file
  Dataset expectedDataset = createExpectedDataset(); // Replace with the creation of your expected Dataset
  Dataset actualDataset = ARFFHandler.loadARFF(file);
  assertEquals(expectedDataset, actualDataset);
 }

 @Test
 public void testLoadARFFWithSkippedBlanks() throws IOException {
  LineIterator lineIterator = Mockito.mock(LineIterator.class);
  when(lineIterator.hasNext()).thenReturn(true).thenReturn(false);
  when(lineIterator.next()).thenReturn("   @data\n1.0, 2.0, 3.0\n  4.0, 5.0, 6.0");

  File file = new File("testData.arff"); // Replace with the path to your ARFF file
  Dataset expectedDataset = createExpectedDataset(); // Replace with the creation of your expected Dataset
  Dataset actualDataset = ARFFHandler.loadARFF(file);
  assertEquals(expectedDataset, actualDataset);
 }

 private Dataset createExpectedDataset() {
  double[][] data = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
  Instance[] instances = new Instance[2];
  for (int i = 0; i < 2; ++i) {
   instances[i] = new DenseInstance(data[i], "ClassLabel" + (i + 1));
  }
  return new DefaultDataset(instances);
 }
}