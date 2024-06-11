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
import net.sf.javaml.core.DefaultInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.util.Pair;

@RunWith(MockitoJUnitRunner.class)
public class ARFFHandlerTest {

 private File file;
 private LineIterator lineIterator;

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 file = new File("src/test/resources/ARFFFiles/test_ valid.arff");
 Dataset expectedData = new DefaultDataset();
 expectedData.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1"));
 expectedData.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "Class2"));
 Dataset actualData = ARFFHandler.loadARFF(file);
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 file = new File("src/test/resources/ARFFFiles/test_ class_index.arff");
 Dataset expectedData = new DefaultDataset();
 expectedData.add(new DenseInstance(new double[]{1.0, 2.0}, "Class1"));
 expectedData.add(new DenseInstance(new double[]{3.0, 4.0}, "Class2"));
 Dataset actualData = ARFFHandler.loadARFF(file, 1);
 assertEquals(expectedData, actualData);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
 file = new File("src/test/resources/ARFFFiles/test_invalidFile.arff");
 ARFFHandler.loadARFF(file);
 fail("FileNotFoundException not thrown");
 }

 @Test
 public void testLoadARFFWithEmptyFile() throws IOException {
 file = new File("src/test/resources/ARFFFiles/test_emptyFile.arff");
 LineIterator iteratorMock = Mockito.mock(LineIterator.class);
 Mockito.when(iteratorMock.hasNext()).thenReturn(false);
 ARFFHandler.loadARFF(file, 0, iteratorMock);
 assertEquals(new DefaultDataset(), ARFFHandler.loadARFF(file));
 }

 @Test
 public void testLoadARFFWithCommentedData() throws IOException {
 file = new File("src/test/resources/ARFFFiles/test_commentedData.arff");
 LineIterator iteratorMock = Mockito.mock(LineIterator.class);
 Mockito.when(iteratorMock.hasNext()).thenReturn(true).thenReturn("@data").thenReturn("1.0, 2.0, %Class1, 3.0, 4.0").thenReturn("5.0, 6.0, Class2, 7.0, 8.0").thenReturn("").thenReturn(null);
 Dataset expectedData = new DefaultDataset();
 expectedData.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1"));
 expectedData.add(new DenseInstance(new double[]{5.0, 6.0}, "Class2"));
 ARFFHandler.loadARFF(file, 2, iteratorMock);
 assertEquals(expectedData, ARFFHandler.loadARFF(file));
 }

 @Test
 public void testLoadARFFWithMissingClassIndex() throws IOException {
 file = new File("src/test/resources/ARFFFiles/test_missingClassIndex.arff");
 Dataset expectedData = new DefaultDataset();
 expectedData.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1"));
 expectedData.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "Class2"));
 expectedData.add(new DenseInstance(new double[]{7.0, 8.0}, null));
 Dataset actualData = ARFFHandler.loadARFF(file);
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testLoadARFFWithInvalidFormat() throws IOException {
 file = new File("src/test/resources/ARFFFiles/test_invalidFormat.arff");
 LineIterator iteratorMock = Mockito.mock(LineIterator.class);
 Mockito.when(iteratorMock.hasNext()).thenReturn(true).thenReturn("@data").thenReturn("1.0, 2.0, 3.0").thenReturn("Class1, 3.0, 4.0").thenReturn("5.0, 6.0, Class2").thenReturn("7.0, 8.0, 9.0").thenReturn("").thenReturn(null);
 Dataset expectedData = new DefaultDataset();
 expectedData.add(new DenseInstance(new double[]{1.0, 2.0}, "Class1"));
 expectedData.add(new DenseInstance(new double[]{5.0, 6.0}, "Class2"));
 Dataset actualData = ARFFHandler.loadARFF(file, 2, iteratorMock);
 assertEquals(expectedData, actualData);
 }
}