package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws Exception {
 // given
 File file = new File("testData.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.1, 3.2}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.1, 6.2}, "Class2"));

 // when
 Dataset actualDataset = ARFFHandler.loadARFF(file);

 // then
 assertEquals("Unexpected dataset size", expectedDataset.size(), actualDataset.size());
 for (int i = 0; i < expectedDataset.size(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 assertArrayEquals("Unexpected instance values", expectedInstance.toArray(), actualInstance.toArray());
 assertEquals("Unexpected class labels", expectedInstance.classValue(), actualInstance.classValue());
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws Exception {
 // given
 File invalidFile = new File("invalidFile.arff");

 // when, then
 ARFFHandler.loadARFF(invalidFile);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws Exception {
 // given
 File file = new File("testData.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.1}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.1}, "Class2"));

 // when
 Dataset actualDataset = ARFFHandler.loadARFF(file, 1);

 // then
 assertEquals("Unexpected dataset size", expectedDataset.size(), actualDataset.size());
 for (int i = 0; i < expectedDataset.size(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 assertArrayEquals("Unexpected instance values", expectedInstance.toArray(new double[0]), Arrays.copyOfRange(actualInstance.toArray(), 0, actualInstance.numAttributes() - 1));
 assertEquals("Unexpected class labels", expectedInstance.classValue(), actualInstance.attribute(actualInstance.numAttributes() - 1).value().toString());
 }
 }
}