package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 final File file = new File("testData/validARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertNotNull(dataset);
 assertEquals(5, dataset.numInstances());
 assertEquals(3, dataset.numAttributes());

 Instance instance = dataset.instance(0);
 assertEquals("1.0,2.0,3.0", instance.toString());
 instance = dataset.instance(1);
 assertEquals("2.0,3.0,4.0", instance.toString());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
 File file = new File("testData/invalidFile.txt");
 try {
 ARFFHandler.loadARFF(file);
 fail("Should have thrown FileNotFoundException");
 } catch (FileNotFoundException e) {
 // expected
 }
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 final File file = new File("testData/classIndexARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file, 2);
 assertNotNull(dataset);
 assertEquals(5, dataset.numInstances());
 assertEquals(3, dataset.numAttributes());

 Instance instance = dataset.instance(0);
 assertEquals("[1.0, 2.0, 3.0]", instance.toString(2));
 instance = dataset.instance(1);
 assertEquals("[2.0, 3.0, 4.0]", instance.toString(2));
 }

 @Test
 public void testLoadARFFWithSkippedComments() throws IOException {
 final File file = new File("testData/commentsARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertNotNull(dataset);
 assertEquals(5, dataset.numInstances());
 assertEquals(3, dataset.numAttributes());
 }

 @Test
 public void testLoadARFFWithNaNValues() throws IOException {
 final File file = new File("testData/nanValuesARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertNotNull(dataset);
 assertEquals(5, dataset.numInstances());
 assertEquals(3, dataset.numAttributes());

 Instance instance = dataset.instance(0);
 assertEquals("1.0,2.0,NaN", instance.toString());
 instance = dataset.instance(1);
 assertEquals("2.0,NaN,4.0", instance.toString());
 }

 @Test
 public void testLoadARFFWithMixedDataTypes() throws IOException {
 final File file = new File("testData/mixedDataTypesARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertNotNull(dataset);
 assertEquals(5, dataset.numInstances());
 assertEquals(3, dataset.numAttributes());

 Instance instance = dataset.instance(0);
 assertEquals("1.0,2.0,3.0", instance.toString());
 instance = dataset.instance(1);
 assertEquals("2.0,NaN,4.0", instance.toString());
 }
}