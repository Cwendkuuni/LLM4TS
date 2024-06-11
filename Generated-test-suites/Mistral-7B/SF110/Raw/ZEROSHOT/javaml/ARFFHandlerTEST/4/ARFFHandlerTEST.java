package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.LineIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

@RunWith(JUnit4.class)
public class ARFFHandlerTest {

 @Test
 public void testLoadARFFFileWithValidData() throws IOException {
  File file = new File("testData/validARFFfile.arff");
  Dataset dataset = ARFFHandler.loadARFF(file);
  assertEquals(10, dataset.numInstances());
  assertEquals(5, dataset.numAttributes());

  Instance inst = dataset.instance(0);
  assertNotNull(inst);
  assertEquals("attribute1_0", inst.value(0));
  assertEquals("attribute2_0", inst.value(1));
  assertEquals("attribute3_0", inst.value(2));
  assertEquals("attribute4_0", inst.value(3));
  assertEquals("attribute5_0", inst.value(4));
  assertEquals("ClassLabel_0", inst.classValue());

  inst = dataset.instance(1);
  assertEquals("attribute1_1", inst.value(0));
  assertEquals("attribute2_1", inst.value(1));
  assertEquals("attribute3_1", inst.value(2));
  assertEquals("attribute4_1", inst.value(3));
  assertEquals("attribute5_1", inst.value(4));
  assertEquals("ClassLabel_1", inst.classValue());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFFileWithInvalidFile() throws IOException {
  File file = new File("testData/invalidFile.txt");
  ARFFHandler.loadARFF(file);
  fail("Expected FileNotFoundException to be thrown");
 }

 @Test
 public void testLoadARFFFileWithClassIndex() throws IOException {
  File file = new File("testData/validARFFfile.arff");
  Dataset dataset = ARFFHandler.loadARFF(file, 1);
  assertEquals(10, dataset.numInstances());
  assertEquals(5, dataset.numAttributes());

  Instance inst = dataset.instance(0);
  assertNotNull(inst);
  assertEquals("attribute1_0", inst.value(0));
  assertEquals("attribute2_0", inst.value(1));
  assertEquals("attribute3_0", inst.value(2));
  assertEquals("attribute4_0", inst.value(3));
  assertNull(inst.classValue());

  inst = dataset.instance(1);
  assertEquals("attribute1_1", inst.value(0));
  assertEquals("attribute2_1", inst.value(1));
  assertEquals("attribute3_1", inst.value(2));
  assertEquals("attribute4_1", inst.value(3));
  assertEquals("ClassLabel_1", inst.value(4));
 }

 @Test(expected = IOException.class)
 public void testLoadARFFFileWithMalformedData() throws IOException {
  File file = new File("testData/malformedARFFfile.arff");
  LineIterator it = new LineIterator(file);
  it.setSkipBlanks(true);
  it.setCommentIdentifier("%");
  it.setSkipComments(true);

  // Consume the header
  String line = it.nextLine();
  assertEquals("@relation testRelation\n", line);

  // Consume the data description
  line = it.nextLine();
  assertEquals("@attribute attribute1 {A, B, C}\n", line);
  line = it.nextLine();
  assertEquals("@attribute attribute2 numeric\n", line);
  line = it.nextLine();
  assertEquals("@attribute attribute3 {D, E}\n", line);
  line = it.nextLine();
  assertEquals("@attribute attribute4 numeric\n", line);
  line = it.nextLine();
  assertEquals("@attribute attribute5 {F, G}\n", line);
  line = it.nextLine();
  assertEquals("@attribute ClassLabel {A, B}\n", line);
  line = it.nextLine();
  assertEquals("@data\n", line);

  // Consume the first instance with missing value
  line = it.nextLine();
  String[] arr = line.split(",");
  assertEquals(5, arr.length);
  fail("Expected IOException to be thrown");
 }

 @Test
 public void testCloseLineIterator() throws IOException {
  File file = new File("testData/validARFFfile.arff");
  LineIterator it = new LineIterator(file);
  it.setSkipBlanks(true);
  it.setCommentIdentifier("%");
  it.setSkipComments(true);

  // Consume the header
  String line = it.nextLine();
  assertEquals("@relation testRelation\n", line);

  // Consume the data description
  line = it.nextLine();
  assertEquals("@attribute attribute1 {A, B, C}\n", line);
  line = it.nextLine();
  assertEquals("@attribute attribute2 numeric\n", line);
  line = it.nextLine();
  assertEquals("@attribute attribute3 {D, E}\n", line);
  line = it.nextLine();
  assertEquals("@attribute attribute4 numeric\n", line);
  line = it.nextLine();
  assertEquals("@attribute attribute5 {F, G}\n", line);
  line = it.nextLine();
  assertEquals("@attribute ClassLabel {A, B}\n", line);
  line = it.nextLine();
  assertEquals("@data\n", line);

  // Consume all data
  int instancesCount = 0;
  while (it.hasNextLine()) {
   String lineData = it.nextLine();
   if (lineData.equalsIgnoreCase("@data")) {
    break;
   }
   instancesCount++;
  }

  assertEquals(10, instancesCount);
  it.close();
 }

}