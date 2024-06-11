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

 @Test
 public void testLoadARFF() throws IOException {
 File file = new File("testData.arff"); // Replace with the path to your ARFF file
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertNotNull(dataset);
 assertEquals(10, dataset.numInstances()); // Replace with the number of instances in your file
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 File file = new File("testData.arff"); // Replace with the path to your ARFF file
 Dataset dataset = ARFFHandler.loadARFF(file, 2); // Replace with the class index
 assertNotNull(dataset);
 assertEquals(10, dataset.numInstances()); // Replace with the number of instances in your file

 Attribute classAttribute = dataset.attribute("class");
 assertEquals("class", classAttribute.getName());
 assertEquals(Dataset.NO_VALIDATION, dataset.classIndex());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundException() throws FileNotFoundException {
 File file = new File("nonExistentFile.arff");
 ARFFHandler.loadARFF(file);
 fail("FileNotFoundException should be thrown.");
 }

 @Test(expected = NumberFormatException.class)
 public void testNumberFormatException() throws IOException {
 File file = new File("testDataWithInvalidValues.arff"); // Replace with the path to your ARFF file with invalid values
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertNotNull(dataset);
 assertEquals(10, dataset.numInstances()); // Replace with the number of instances in your file

 Attribute attribute = dataset.attribute(0);
 assertEquals(attribute.isNominal(), false);
 assertEquals(attribute.isNumeric(), true);

 Instance instance = dataset.instance(0);
 Double invalidValue = (Double) instance.value(2); // Replace with the index of the invalid value
 assertEquals(Double.NaN, invalidValue);
 }
}