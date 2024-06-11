package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 // Given: A valid ARFF file
 File file = new File("testData/validARFFFile.arff");

 // When: Loading the ARFF file using ARFFHandler
 Dataset dataset = ARFFHandler.loadARFF(file);

 // Then: The loaded dataset should not be null and have at least one instance
 assertNotNull(dataset);
 assertTrue(dataset.size() > 0);
 }

 @Test
 public void testLoadARFFWithInvalidFile() {
 // Given: An invalid file (does not exist)
 File file = new File("testData/invalidARFFFile.txt");

 // When: Trying to load the ARFF file using ARFFHandler
 // Then: An IOException should be thrown
 expectThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(file));
 }

 @Test
 public void testLoadARFFWithClassIndexValid() throws IOException {
 // Given: A valid ARFF file with a class index
 File file = new File("testData/validARFFFileWithClassIndex.arff");

 // When: Loading the ARFF file using ARFFHandler with class index
 Dataset dataset = ARFFHandler.loadARFF(file, 2);

 // Then: The loaded dataset should not be null and have at least one instance
 assertNotNull(dataset);
 assertTrue(dataset.size() > 0);

 // Verify that the class values are correctly extracted
 for (Object instance : dataset) {
 DenseInstance denseInstance = (DenseInstance) instance;
 assertEquals(denseInstance.classValue(), "class3");
 }
 }

 @Test
 public void testLoadARFFWithClassIndexInvalid() throws IOException {
 // Given: A valid ARFF file with an invalid class index
 File file = new File("testData/validARFFFile.arff");

 // When: Loading the ARFF file using ARFFHandler with an invalid class index
 Dataset dataset = ARFFHandler.loadARFF(file, 5);

 // Then: The loaded dataset should not be null and have at least one instance
 assertNotNull(dataset);
 assertTrue(dataset.size() > 0);

 // Verify that the class values are correctly extracted (ignoring the last one)
 for (int i = 0; i < dataset.size(); ++i) {
 DenseInstance denseInstance = (DenseInstance) dataset.instance(i);
 assertEquals(denseInstance.classValue(), i < dataset.size() - 1 ? "class1" : null);
 }
 }

 @Test
 public void testLoadARFFWithNaNs() throws IOException {
 // Given: A valid ARFF file with NaNs
 File file = new File("testData/validARFFFileWithNaNs.arff");

 // When: Loading the ARFF file using ARFFHandler
 Dataset dataset = ARFFHandler.loadARFF(file);

 // Then: The loaded dataset should not be null and have at least one instance
 assertNotNull(dataset);
 assertTrue(dataset.size() > 0);

 // Verify that the NaNs are correctly extracted
 for (int i = 0; i < dataset.size(); ++i) {
 DenseInstance denseInstance = (DenseInstance) dataset.instance(i);
 assertEquals(denseInstance.value(2), Double.NaN);
 }
 }
}