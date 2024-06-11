package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DefaultInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {

 private static final String TEST_FILE = "testARFFFile.arff";

 @Test
 public void testLoadARFF() throws IOException {
 File file = new File(TEST_FILE);
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertNotNull(dataset);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() {
 File file = new File("nonExistentFile.arff");
 ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 File file = new File(TEST_FILE);
 Dataset dataset = ARFFHandler.loadARFF(file, 0);
 assertNotNull(dataset);
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithInvalidNumberFormat() throws IOException {
 File file = new File(TEST_FILE);
 ARFFHandler.loadARFF(file); // Load the dataset first to set up line iterator

 LineIterator it = new LineIterator(file);
 it.next(); // "@data" line
 String line = it.next(); // A line with invalid number format
 String[] arr = line.split(",");
 assertFalse(arr.length > 0); // The line should be empty after parsing
 }

 @Test
 public void testLoadARFFWithMultipleClasses() throws IOException {
 File file = new File(TEST_FILE);
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertEquals(dataset.numClasses(), 2);
 }

 @Test
 public void testLoadARFFWithMissingClassIndex() throws IOException {
 File file = new File(TEST_FILE);
 Dataset dataset = ARFFHandler.loadARFF(file, 3);
 assertNotNull(dataset);
 }

 @Test
 public void testLoadARFFWithEmptyDataset() throws IOException {
 File file = new File("emptyARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertTrue(dataset.numInstances() == 0);
 }

 @Test
 public void testLoadARFFWithEmptyLine() throws IOException {
 File file = new File(TEST_FILE);
 LineIterator it = new LineIterator(file);
 it.next(); // "@data" line
 it.next(); // Skip empty line

 String line = it.next(); // A line with valid data
 String[] arr = line.split(",");
 Attribute attribute = new Attribute("testAttribute", arr[0].getClass());
 DefaultInstance instance = new DenseInstance(new double[arr.length - 1], arr[arr.length - 1]);
 instance.setDataset(dataset);
 instance.setAttribute(attribute);

 Dataset dataset = new DefaultDataset();
 dataset.add(instance);

 Dataset loadedDataset = ARFFHandler.loadARFF(file, 0);
 assertEquals(dataset, loadedDataset);
 }
}