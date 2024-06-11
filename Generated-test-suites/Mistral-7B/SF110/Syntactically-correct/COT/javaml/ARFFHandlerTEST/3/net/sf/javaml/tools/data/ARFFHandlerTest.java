package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceList;
import net.sf.javaml.core.relation.Relation;
import net.sf.javaml.tools.data.ARFFHandler;

@RunWith(Parameterized.class)
public class ARFFHandlerTest {

 private File file;
 private int classIndex;
 private Dataset expectedResult;

 public ARFFHandlerTest(String filePath, int classIndex, Dataset expectedResult) {
 this.file = new File(filePath);
 this.classIndex = classIndex;
 this.expectedResult = expectedResult;
 }

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 new Object[] { "data1.arff", -1, new DefaultDataset() },
 new Object[] { "data2.arff", 0, new DefaultDataset() },
 new Object[] { "data3.arff", 2, new DefaultDataset() },
 new Object[] { "data4.arff", 3, new DefaultDataset() },
 new Object[] { "data5.arff", 4, new DefaultDataset() }
 });
 }

 @Test
 public void testLoadARFF() throws IOException {
 Dataset actualResult = ARFFHandler.loadARFF(file);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws Exception {
 ARFFHandler.loadARFF(new File("nonExistentFile.arff"));
 failing(new AssertionError("Expected FileNotFoundException but none was thrown."));
 }

 @Test
 public void testLoadARFFWithClassIndexGreaterThanDataSize() {
 try {
 ARFFHandler.loadARFF(new File("data1.arff"), 5);
 failing(new AssertionError("Expected IllegalArgumentException but none was thrown."));
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLoadARFFWithEmptyLine() {
 String line = "";
 LineIterator it = new LineIterator(new File("data1.arff"));
 it.setSkipBlanks(true);
 it.setCommentIdentifier("%");
 it.setSkipComments(true);
 it.next();
 assertFalse(it.hasNext());
 }

 @Test
 public void testLoadARFFWithCommentLine() {
 String line = "% This is a comment line";
 LineIterator it = new LineIterator(new File("data1.arff"));
 it.setSkipBlanks(true);
 it.setCommentIdentifier("%");
 it.setSkipComments(true);
 it.next();
 assertFalse(it.hasNext());
 }

 @Test
 public void testLoadARFFWithMissingDataModeSwitch() {
 String line = "@data";
 LineIterator it = new LineIterator(new File("data1.arff"));
 it.setSkipBlanks(true);
 it.setCommentIdentifier("%");
 it.setSkipComments(true);
 it.next();
 it.next();
 assertFalse(it.hasNext());
 }

 @Test
 public void testLoadARFFWithInvalidAttributeDataType() {
 String[] arr = new String[] { "Age", "Color:Blue", "Weight" };
 Instance instance = new DenseInstance(new double[arr.length], null);
 DefaultDataset dataset = new DefaultDataset();
 dataset.add(instance);
 LineIterator it = new LineIterator(new File("data1.arff"));
 it.setSkipBlanks(true);
 it.setCommentIdentifier("%");
 it.setSkipComments(true);
 it.next(); // @data
 it.next(); // empty line
 it.next(); // @attribute
 Arrays.stream(arr).forEach(attributeName -> {
 try {
 it.next();
 String attributeLine = it.next();
 String[] attributeParts = attributeLine.split(",");
 Attribute attribute = new Attribute(attributeName);
 attribute.setType(Attribute.NOMINAL);
 dataset.addAttribute(attribute);
 } catch (Exception e) {
 failing(new AssertionError("Expected to read valid attribute line for name: " + attributeName
 + ", but encountered: " + e.getMessage()));
 }
 });
 it.next(); // @data
 it.next(); // empty line
 String instanceLine = "1,20,Male,185.5,70.0";
 try {
 double[] values = new double[5];
 String[] arr2 = instanceLine.split(",");
 for (int i = 0; i < arr2.length; ++i) {
 double val;
 if (i > 0) {
 val = Double.parseDouble(arr2[i]);
 values[i - 1] = val;
 } else {
 val = Double.parseDouble(arr2[i]);
 values[i] = val;
 }
 }
 Instance expectedInstance = new DenseInstance(values, "Male");
 Instance actualInstance = new DenseInstance(values, null);
 dataset.add(actualInstance);
 assertEquals(expectedInstance, dataset.getInstance(0));
 } catch (Exception e) {
 failing(new AssertionError("Expected to read valid instance line, but encountered: " + e.getMessage()));
 }
 }
}