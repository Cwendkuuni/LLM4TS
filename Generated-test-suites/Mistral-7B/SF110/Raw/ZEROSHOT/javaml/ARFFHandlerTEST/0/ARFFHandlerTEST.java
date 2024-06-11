package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws IOException {
 // Given: A valid ARFF file
 File arffFile = new File("testData/validARFF.arff");
 // When: Load ARFF file using ARFFHandler.loadARFF() method
 Dataset expectedDataset = ARFFHandler.loadARFF(arffFile);
 // Then: Assert that the loaded dataset is not null
 assertNotNull(expectedDataset);
 // And: Assert that the dataset contains data
 assertTrue(expectedDataset.size() > 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
 // Given: An invalid file path
 File arffFile = new File("testData/invalidFile.txt");
 // When: Load ARFF file using ARFFHandler.loadARFF() method
 ARFFHandler.loadARFF(arffFile);
 // Then: Assert that FileNotFoundException is thrown
 fail("FileNotFoundException expected but not thrown");
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 // Given: A valid ARFF file with a class index
 File arffFile = new File("testData/validARFFWithClassIndex.arff");
 // When: Load ARFF file using ARFFHandler.loadARFF() method with class index
 Dataset expectedDataset = ARFFHandler.loadARFF(arffFile, 2);
 // Then: Assert that the loaded dataset is not null
 assertNotNull(expectedDataset);
 // And: Assert that the dataset contains data
 assertTrue(expectedDataset.size() > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithInvalidNumberFormat() throws IOException, NumberFormatException {
 // Given: A valid ARFF file with an invalid number format
 File arffFile = new File("testData/invalidNumberFormatARFF.arff");
 // When: Load ARFF file using ARFFHandler.loadARFF() method
 ARFFHandler.loadARFF(arffFile);
 // Then: Assert that NumberFormatException is thrown
 fail("NumberFormatException expected but not thrown");
 }

 @Test
 public void testLoadARFFWithMissingDataMode() throws IOException {
 // Given: A valid ARFF file without the @data identifier
 File arffFile = new File("testData/missingDataIdentifierARFF.arff");
 // When: Load ARFF file using ARFFHandler.loadARFF() method
 Dataset dataset = ARFFHandler.loadARFF(arffFile);
 // Then: Assert that the dataset is empty
 assertTrue(dataset.size() == 0);
 }
}