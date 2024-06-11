package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.LineIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;

@RunWith(MockitoJUnitRunner.class)
public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
  File file = new File("testData/validARFFFile.arff");
  LineIterator it = Mockito.mock(LineIterator.class);
  when(it.hasNextLine()).thenReturn(true).thenReturn(false);
  when(it.nextLine()).thenAnswer(i -> "line1,value1,value2,label1\n@data\nvalue3,value4,value5,label2");

  Dataset dataset = ARFFHandler.loadARFF(file);
  Instance instance1 = (Instance) dataset.instance(0);
  Instance instance2 = (Instance) dataset.instance(1);

  assertEquals(2, dataset.numInstances());
  assertEquals(5, dataset.numAttributes());

  assertArrayEquals(new double[] {1.0, 2.0, Double.NaN, Double.NaN, Double.NaN}, instance1.toArray());
  assertEquals("label1", instance1.classValue().toString());

  assertArrayEquals(new double[] {Double.NaN, 3.0, 4.0, 5.0, Double.NaN}, instance2.toArray());
  assertEquals("label2", instance2.classValue().toString());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
  File file = new File("testData/invalidFile.txt");
  ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
  File file = new File("testData/validARFFFileWithClassIndex.arff");
  LineIterator it = Mockito.mock(LineIterator.class);
  when(it.hasNextLine()).thenReturn(true).thenReturn(false);
  when(it.nextLine()).thenAnswer(i -> "line1,value1,value2,label1\n@data\nvalue3,value4,label2");

  Dataset dataset = ARFFHandler.loadARFF(file, 2);
  Instance instance1 = (Instance) dataset.instance(0);
  Instance instance2 = (Instance) dataset.instance(1);

  assertEquals(2, dataset.numInstances());
  assertEquals(3, dataset.numAttributes());

  assertArrayEquals(new double[] {1.0, 2.0}, instance1.toArray(new double[2]));
  assertNull(instance1.classValue());

  assertArrayEquals(new double[] {Double.NaN, 3.0}, instance2.toArray(new double[2]));
  assertEquals("label2", instance2.classValue().toString());
 }
}