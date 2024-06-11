package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 // Given a valid ARFF file
 File arffFile = new File("testdata/validARFFfile.arff");

 // When loading the ARFF file
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.1}, "A"));
 expectedDataset.add(new DenseInstance(new double[]{3.0, 4.1, "B"}, "B"));
 expectedDataset.add(new DenseInstance(new double[]{5.0, 6.1, 7.0, "C"}, "C"));
 expectedDataset.add(new DenseInstance(new double[]{8.0, 9.1, 10.0, "D"}, "D"));
 Dataset actualDataset = ARFFHandler.loadARFF(arffFile);

 // Then the loaded dataset should be equal to the expected dataset
 assertEquals(expectedDataset, actualDataset);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
 // Given an invalid ARFF file that doesn't exist
 File invalidArffFile = new File("testdata/nonexistentfile.arff");

 // When trying to load the ARFF file
 ARFFHandler.loadARFF(invalidArffFile);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 // Given a valid ARFF file
 File arffFileWithClassIndex = new File("testdata/ARFFfileWithClassIndex.arff");

 // When loading the ARFF file with class index
 Dataset expectedDatasetWithClassIndex = new DefaultDataset();
 expectedDatasetWithClassIndex.add(new DenseInstance(new double[]{1.0}, "A"));
 expectedDatasetWithClassIndex.add(new DenseInstance(new double[]{3.0, 4.1}, "B"));
 expectedDatasetWithClassIndex.add(new DenseInstance(new double[]{5.0, 6.1, 7.0}, "C"));
 expectedDatasetWithClassIndex.add(new DenseInstance(new double[]{8.0, 9.1, 10.0}, "D"));
 Dataset actualDatasetWithClassIndex = ARFFHandler.loadARFF(arffFileWithClassIndex, 0);

 // Then the loaded dataset with class index should be equal to the expected dataset
 assertEquals(expectedDatasetWithClassIndex, actualDatasetWithClassIndex);
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithInvalidNumberFormat() throws IOException {
 // Given a valid ARFF file with an instance containing an invalid number format
 File arffFileWithInvalidNumberFormat = new File("testdata/ARFFfileWithInvalidNumberFormat.arff");

 // When loading the ARFF file
 Dataset actualDatasetWithInvalidNumberFormat = ARFFHandler.loadARFF(arffFileWithInvalidNumberFormat);

 // Then an exception should be thrown due to the invalid number format
 // in the instance
 fail("Expected NumberFormatException not thrown");
 }

 @Test
 public void testLoadARFFWithEmptyLine() throws IOException {
 // Given a valid ARFF file with an empty line
 File arffFileWithEmptyLine = new File("testdata/ARFFfileWithEmptyLine.arff");

 // When loading the ARFF file
 Dataset actualDatasetWithEmptyLine = ARFFHandler.loadARFF(arffFileWithEmptyLine);

 // Then the loaded dataset should be empty as the empty line is ignored
 assertEquals(new DefaultDataset(), actualDatasetWithEmptyLine);
 }
}