package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;

@RunWith(JUnit4.class)
public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 // Given
 File file = new File("path/to/your/valid/arff/file.arff");
 // When
 Dataset dataset = ARFFHandler.loadARFF(file);
 // Then
 assertNotNull(dataset);
 assertFalse(dataset.isEmpty());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
 // Given
 File file = new File("path/to/your/invalid/file.ext");
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
 assertNotNull(dataset);
 assertFalse(dataset.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadARFFWithInvalidClassIndex() throws IOException {
 // Given
 File file = new File("path/to/your/arff/file.arff");
 // When & Then
 ARFFHandler.loadARFF(file, -1);
 }

 @Test
 public void testLoadARFFWithEmptyFile() throws IOException {
 // Given
 File file = new File("path/to/your/empty/arff/file.arff");
 // When
 Dataset dataset = ARFFHandler.loadARFF(file);
 // Then
 assertNotNull(dataset);
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testLoadARFFWithDataMode() throws IOException {
 // Given
 File file = new File("path/to/your/arff/file.arff");
 // When
 Dataset dataset = ARFFHandler.loadARFF(file);
 // Then
 assertTrue(dataset.stream().anyMatch(instance -> instance.classValue() != null));
 }

 @Test
 public void testParseLine() {
 // Given
 LineIterator it = new LineIterator(new StringReader("@data\n1,2.0,3.0,4.0,5.0\nclass: 0\n"));
 // When
 String line = it.nextLine();
 // Then
 assertEquals("@data", line);
 // Given
 it.setCommentIdentifier("%");
 // When & Then
 assertEquals(null, it.nextLine());
 // Given
 it.setCommentIdentifier("");
 // When
 String[] arr = ARFFHandler.parseLine(it.nextLine());
 // Then
 assertEquals(5, arr.length);
 assertEquals("1", arr[0]);
 // Given
 it.setCommentIdentifier("%");
 // When & Then
 assertEquals(null, it.nextLine());
 // Given
 it.setCommentIdentifier("");
 // When
 double[] values = ARFFHandler.parseLine(it.nextLine(), 2);
 // Then
 assertEquals(3, values.length);
 assertEquals(3.0, values[0], 0.001);
 assertEquals(4.0, values[1], 0.001);
 }

 @Test
 public void testParseLineWithNaN() throws IOException {
 // Given
 LineIterator it = new LineIterator(new StringReader("@data\n1,2.0,3.0,NaN,5.0\n"));
 // When
 double[] values = ARFFHandler.parseLine(it.nextLine(), 3);
 // Then
 assertEquals(4, values.length);
 assertEquals(2.0, values[0], 0.001);
 assertEquals(3.0, values[1], 0.001);
 assertEquals(Double.NaN, values[2]);
 }
}