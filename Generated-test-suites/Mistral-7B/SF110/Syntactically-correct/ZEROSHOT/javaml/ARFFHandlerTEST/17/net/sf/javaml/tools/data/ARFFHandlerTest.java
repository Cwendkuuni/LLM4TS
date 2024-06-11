package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.util.LineIterator;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 // Given
 File file = new File("path/to/your/valid/arff/file.arff");
 // When
 Dataset dataset = ARFFHandler.loadARFF(file);
 // Then
 assertNotNull(dataset);
 assertEquals(10, dataset.numInstances());
 assertEquals(5, dataset.numAttributes());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
 // Given
 File file = new File("path/to/your/invalid/file.txt");
 // When & Then
 ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 // Given
 File file = new File("path/to/your/arff/file.arff");
 // When
 Dataset dataset = ARFFHandler.loadARFF(file, 2);
 // Then
 assertEquals(10, dataset.numInstances());
 assertEquals(5, dataset.numAttributes());
 assertNotNull(dataset.attribute(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadARFFWithNegativeClassIndex() throws IOException {
 // Given
 File file = new File("path/to/your/arff/file.arff");
 // When & Then
 ARFFHandler.loadARFF(file, -1);
 }

 @Test
 public void testLoadARFFWithEmptyFile() throws IOException {
 // Given
 File file = new File("path/to/your/empty/file.arff");
 // When
 Dataset dataset = ARFFHandler.loadARFF(file);
 // Then
 assertNotNull(dataset);
 assertEquals(0, dataset.numInstances());
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithInvalidValue() throws IOException {
 // Given
 String invalidValue = "invalidValue";
 File file = new File("path/to/your/arff/file.arff");
 // When & Then
 ARFFHandler.loadARFF(file, new File("path/to/your/output/dataset.arff"), new PrintStream(System.out), new InstanceMaker() {
 @Override
 public Instance newInstance(double[] values, String classValue) {
 return new DenseInstance(values, classValue);
 }
 }, new Attribute[]{}, new Attribute("name", new BasicAttributeType(AttributeType.NOMINAL), "name", new String[]{invalidValue}));
 }
}