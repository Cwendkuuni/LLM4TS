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
 public void testLoadARFF() throws IOException {
  File file = new File("test.arff"); // Replace with the path to your ARFF file
  Dataset expectedDataset = createExpectedDataset();
  Dataset actualDataset = ARFFHandler.loadARFF(file);
  assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
  assertEquals(expectedDataset.attributeNames(), actualDataset.attributeNames());
  for (int i = 0; i < expectedDataset.numInstances(); i++) {
   Instance expectedInstance = expectedDataset.instance(i);
   Instance actualInstance = actualDataset.instance(i);
   assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray());
  }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
  File file = new File("nonexistentfile.arff");
  ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
  File file = new File("test.arff"); // Replace with the path to your ARFF file
  Dataset expectedDataset = createExpectedDataset();
  int classIndex = 2;
  Dataset actualDataset = ARFFHandler.loadARFF(file, classIndex);
  assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
  assertEquals(expectedDataset.attributeNames().size(), actualDataset.attributeNames().size());
  Attribute classAttribute = expectedDataset.attribute(classIndex);
  assertEquals(classAttribute.isNominal(), actualDataset.attribute(classIndex).isNominal());
  assertEquals(classAttribute.isNumeric(), actualDataset.attribute(classIndex).isNumeric());
  assertEquals(classAttribute.name(), actualDataset.attribute(classIndex).name());
  for (int i = 0; i < expectedDataset.numInstances(); i++) {
   Instance expectedInstance = expectedDataset.instance(i);
   Instance actualInstance = actualDataset.instance(i);
   assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray());
  }
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithInvalidNumericValue() throws IOException {
  File file = new File("test.arff"); // Replace with the path to your ARFF file
  ARFFHandler.loadARFF(file, 0);
 }

 private Dataset createExpectedDataset() throws IOException {
  Dataset dataset = new DefaultDataset();
  Attribute attribute1 = new Attribute("attribute1");
  attribute1.setType(Attribute.NUMERIC);
  dataset.addAttribute(attribute1);
  Attribute attribute2 = new Attribute("attribute2");
  attribute2.setType(Attribute.NOMINAL);
  attribute2.addValue("value1");
  attribute2.addValue("value2");
  dataset.addAttribute(attribute2);
  Attribute classAttribute = new Attribute("class");
  classAttribute.setType(Attribute.NOMINAL);
  dataset.addAttribute(classAttribute);
  Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "value1");
  dataset.add(instance1);
  Instance instance2 = new DenseInstance(new double[]{3.0, Double.NaN}, "value2");
  dataset.add(instance2);
  return dataset;
 }

}