package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.ARFFHandler;

import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 File file = new File("path/to/your/valid/arff/file.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0, "Class1"}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{3.0, 4.0, "Class2"}, "Class2"));
 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset, actualDataset);
 }

 @Test
 public void testLoadARFFWithInvalidFile() {
 File file = new File("path/to/your/invalid/file.txt"); // Replace with an invalid file path
 try {
 ARFFHandler.loadARFF(file);
 fail("Should have thrown FileNotFoundException");
 } catch (FileNotFoundException e) {
 // Expected
 }
 }

 @Test
 public void testLoadARFFWithNoClassIndex() throws IOException {
 File file = new File("path/to/your/arff/file.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0, "Class1"}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{3.0, 4.0, "Class2"}, "Class2"));
 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset, actualDataset);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 File file = new File("path/to/your/arff/file.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{3.0, 4.0}, "Class2"));
 Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
 assertEquals(expectedDataset, actualDataset);
 }
}