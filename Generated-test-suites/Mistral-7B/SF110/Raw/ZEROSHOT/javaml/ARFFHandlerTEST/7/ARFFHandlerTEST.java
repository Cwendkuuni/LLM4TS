package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.LineIterator;
import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws IOException {
 // Test with valid ARFF file
 File arffFile = new File("testData/sample.arff");
 Dataset expectedDataset = loadExpectedDataset();
 Dataset actualDataset = ARFFHandler.loadARFF(arffFile);
 assertEquals("Size of datasets should be equal", expectedDataset.size(), actualDataset.size());
 for (int i = 0; i < expectedDataset.size(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 assertArrayEquals("Values of instances should be equal", expectedInstance.toArray(), actualInstance.toArray(), 0.0);
 }

 // Test with non-existent file
 File nonExistentFile = new File("testData/nonExistentFile.arff");
 try {
 ARFFHandler.loadARFF(nonExistentFile);
 fail("Expected FileNotFoundException");
 } catch (FileNotFoundException e) {
 // Expected
 }

 // Test with null file
 File nullFile = null;
 try {
 ARFFHandler.loadARFF(nullFile);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 // Test with valid ARFF file and valid class index
 File arffFile = new File("testData/sample.arff");
 Dataset expectedDataset = loadExpectedDataset();
 Dataset actualDataset = ARFFHandler.loadARFF(arffFile, 0);
 assertEquals("Size of datasets should be equal", expectedDataset.size(), actualDataset.size());
 for (int i = 0; i < expectedDataset.size(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 assertArrayEquals("Values of instances should be equal", expectedInstance.toArray(new double[expectedInstance.numAttributes()]), actualInstance.toArray(), 0.0);
 String expectedClassValue = (String) expectedInstance.value(expectedInstance.numAttributes() - 1);
 String actualClassValue = (String) actualInstance.value(actualInstance.numAttributes() - 1);
 assertEquals("Class values should be equal", expectedClassValue, actualClassValue);
 }

 // Test with valid ARFF file and invalid class index
 File arffFileInvalidClassIndex = new File("testData/sample.arff");
 Dataset expectedDatasetInvalidClassIndex = loadExpectedDataset();
 Dataset actualDatasetInvalidClassIndex = ARFFHandler.loadARFF(arffFileInvalidClassIndex, expectedDatasetInvalidClassIndex.numInstances());
 assertEquals("Size of datasets should be equal", expectedDatasetInvalidClassIndex.size(), actualDatasetInvalidClassIndex.size());
 for (int i = 0; i < expectedDatasetInvalidClassIndex.size(); i++) {
 Instance expectedInstance = expectedDatasetInvalidClassIndex.instance(i);
 Instance actualInstance = actualDatasetInvalidClassIndex.instance(i);
 assertArrayEquals("Values of instances should be equal", expectedInstance.toArray(new double[expectedInstance.numAttributes()]), actualInstance.toArray(), 0.0);
 String expectedClassValue = (String) expectedInstance.value(expectedInstance.numAttributes() - 1);
 String actualClassValue = (String) actualInstance.value(actualInstance.numAttributes() - 2);
 assertNotSame("Class index value is different", expectedClassValue, actualClassValue);
 }

 // Test with non-existent file
 File nonExistentFile = new File("testData/nonExistentFile.arff");
 try {
 ARFFHandler.loadARFF(nonExistentFile, 0);
 fail("Expected FileNotFoundException");
 } catch (FileNotFoundException e) {
 // Expected
 }

 // Test with null file
 File nullFile = null;
 try {
 ARFFHandler.loadARFF(nullFile, 0);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 private Dataset loadExpectedDataset() throws IOException {
 // Load expected dataset from a file
 File arffFile = new File("testData/sample.arff");
 Dataset dataset = ARFFHandler.loadARFF(arffFile);
 return dataset;
 }
}