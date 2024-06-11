package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import be.abeel.util.LineIterator;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ARFFHandlerTest {

 private File file;
 private LineIterator lineIteratorMock;

 @Before
 public void setUp() {
 file = new File("testARFFFile.arff");
 lineIteratorMock = mock(LineIterator.class);
 }

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 Dataset expectedResult = createTestDataset();
 when(lineIteratorMock.hasNext()).thenReturn(true).thenReturn(false);
 when(lineIteratorMock.next()).thenAnswer(i -> "testLine1").thenAnswer(i -> "testLine2").thenAnswer(i -> "@data").thenReturn(null);

 Dataset result = ARFFHandler.loadARFF(file);
 assertEquals(expectedResult, result);

 verify(lineIteratorMock, times(4)).next();
 verifyNoMoreInteractions(lineIteratorMock);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
 File invalidFile = new File("nonExistingFile.arff");
 ARFFHandler.loadARFF(invalidFile);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 Dataset expectedResult = createTestDatasetWithClassIndex();
 when(lineIteratorMock.hasNext()).thenReturn(true).thenReturn(false);
 when(lineIteratorMock.next()).thenAnswer(i -> "testLine1").thenAnswer(i -> "@attribute label string").thenAnswer(i -> "testLine3").thenAnswer(i -> "@data").thenReturn(null);

 Dataset result = ARFFHandler.loadARFF(file, 1);
 assertEquals(expectedResult, result);

 verify(lineIteratorMock, times(5)).next();
 verifyNoMoreInteractions(lineIteratorMock);
 }

 @Test
 public void testLoadARFFWithEmptyFile() throws IOException {
 Dataset expectedResult = new DefaultDataset();
 when(lineIteratorMock.hasNext()).thenReturn(false);

 Dataset result = ARFFHandler.loadARFF(file);
 assertEquals(expectedResult, result);

 verifyNoInteractions(lineIteratorMock);
 }

 private Dataset createTestDataset() {
 String[] attributes = new String[]{"attribute1", "attribute2"};
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "classValue1");
 Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, "classValue2");
 Dataset result = new DefaultDataset();
 result.add(instance1);
 result.add(instance2);
 return result;
 }

 private Dataset createTestDatasetWithClassIndex() {
 String[] attributes = new String[]{"attribute1", "attribute2", "label"};
 Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
 Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, "classValue2");
 Dataset result = new DefaultDataset();
 result.add(instance1);
 result.add(instance2);
 return result;
 }
}