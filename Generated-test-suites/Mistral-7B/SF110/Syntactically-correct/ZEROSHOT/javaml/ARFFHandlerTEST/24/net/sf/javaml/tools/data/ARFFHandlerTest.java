package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DefaultInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SimpleDataset;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws IOException {
  File file = new File("test.arff"); // Replace with the path to your ARFF file
  Dataset expectedDataset = new DefaultDataset();
  expectedDataset.add((Instance) new DefaultInstance(new Object[]{"feature1", "feature2", "feature3"}, new double[]{1.0, 2.0, 3.0}));
  expectedDataset.add((Instance) new DefaultInstance(new Object[]{"feature1", "feature2", "feature3"}, new double[]{4.0, 5.0, 6.0}));
  expectedDataset.setClassIndex(2);

  Dataset actualDataset = ARFFHandler.loadARFF(file);

  assertEquals(expectedDataset.size(), actualDataset.size());
  for (int i = 0; i < expectedDataset.size(); i++) {
   Instance expectedInstance = expectedDataset.instance(i);
   Instance actualInstance = actualDataset.instance(i);

   assertEquals(expectedInstance.classValue(), actualInstance.classValue());
   assertArrayEquals(expectedInstance.toDoubleValues(), actualInstance.toDoubleValues(), 0.001);
  }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
  File file = new File("non_existing_file.arff");
  ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
  File file = new File("test.arff"); // Replace with the path to your ARFF file

  Dataset expectedDataset = new DefaultDataset();
  expectedDataset.add((Instance) new DefaultInstance(new Object[]{"feature1", "feature2"}, new double[]{1.0, 2.0}));
  expectedDataset.add((Instance) new DefaultInstance(new Object[]{"feature1", "feature2"}, new double[]{3.0, 4.0}));
  expectedDataset.setClassAttribute(new Attribute("class"));

  Dataset actualDataset = ARFFHandler.loadARFF(file, 1);

  assertEquals(expectedDataset.size(), actualDataset.size());
  for (int i = 0; i < expectedDataset.size(); i++) {
   Instance expectedInstance = expectedDataset.instance(i);
   Instance actualInstance = actualDataset.instance(i);

   assertEquals(expectedInstance.classAttribute().value(actualInstance), expectedInstance.classValue());
   assertArrayEquals(expectedInstance.toDoubleValues(), actualInstance.toDoubleValues(), 0.001);
  }

  // Test for no class index
  try {
   ARFFHandler.loadARFF(file, -2);
   fail("Expected FileNotFoundException");
  } catch (FileNotFoundException e) {
   // Expected
  }

  try {
   ARFFHandler.loadARFF(file, 3); // Test for out of bounds class index
   fail("Expected ArrayIndexOutOfBoundsException");
  } catch (ArrayIndexOutOfBoundsException e) {
   // Expected
  }
 }

 @Test
 public void testLoadARFFWithEmptyFile() throws IOException {
  File file = new File("empty.arff"); // Replace with the path to your empty ARFF file

  Dataset expectedDataset = new DefaultDataset();

  Dataset actualDataset = ARFFHandler.loadARFF(file);

  assertEquals("Expected size: 0", expectedDataset.size(), actualDataset.size());
  assertEquals("Expected empty dataset", expectedDataset, actualDataset);
 }
}