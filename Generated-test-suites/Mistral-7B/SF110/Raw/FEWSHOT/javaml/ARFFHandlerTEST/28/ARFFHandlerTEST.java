package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import be.abeel.util.*;
import net.sf.javaml.core.*;
import java.io.*;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ARFFHandlerTest {

 @Mock
 private LineIterator lineIterator;

 @Test
 public void testLoadARFFWithNullFile() {
     assertThrows(NullPointerException.class, () -> ARFFHandler.loadARFF(null));
 }

 @Test
 public void testLoadARFFWithoutClassIndex() throws FileNotFoundException {
     when(lineIterator.hasNext()).thenReturn(true).thenReturn(false);
     final File file = mock(File.class);
     when(lineIterator.next()).thenAnswer(i -> "@data\nfoo,bar1,bar2,bar3\n1.0,1.1,2.2,3.3\n2.0,2.1,3.3,4.4");

     final Dataset expected = new DefaultDataset();
     expected.add(new DenseInstance(new double[]{1.0, 1.1, 2.2, 3.3}, "1.0"));
     expected.add(new DenseInstance(new double[]{2.0, 2.1, 3.3, 4.4}, "2.0"));

     final Dataset result = ARFFHandler.loadARFF(file);
     assertEquals(expected, result);
     verify(lineIterator).hasNext();
     verify(lineIterator, times(3)).next();
     verify(lineIterator).close();
 }

 @Test
 public void testLoadARFFWithClassIndex() throws FileNotFoundException {
     when(lineIterator.hasNext()).thenReturn(true).thenReturn(false);
     final File file = mock(File.class);
     when(lineIterator.next()).thenAnswer(i -> "@data\nfoo,bar1,bar2,class\n1.0,1.1,2.2,1.5\n2.0,2.1,3.3,2.0");

     final Dataset expected = new DefaultDataset();
     expected.add(new DenseInstance(new double[]{1.0, 1.1, 2.2}, "1.5"));
     expected.add(new DenseInstance(new double[]{2.0, 2.1, 3.3}, "2.0"));

     final Dataset result = ARFFHandler.loadARFF(file, 2);
     assertEquals(expected, result);
     verify(lineIterator).hasNext();
     verify(lineIterator, times(3)).next();
     verify(lineIterator).close();
 }

 @Test
 public void testLoadARFFWithEmptyFile() throws FileNotFoundException {
     when(lineIterator.hasNext()).thenReturn(false);
     final File file = mock(File.class);

     final Dataset result = ARFFHandler.loadARFF(file);
     assertTrue(result.isEmpty());
     verify(lineIterator).hasNext();
     verify(lineIterator).close();
 }

 @Test
 public void testLoadARFFWithCommentLines() throws FileNotFoundException {
     when(lineIterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
     when(lineIterator.next()).thenAnswer(i -> "% This is a comment line\n@data\nfoo,bar1,bar2,bar3\n1.0,1.1,2.2,3.3");

     final Dataset result = ARFFHandler.loadARFF(new File("file.arff"), 0);
     final Dataset expected = new DefaultDataset();
     expected.add(new DenseInstance(new double[]{1.0, 1.1, 2.2, 3.3}, null));

     assertEquals(expected, result);
     verify(lineIterator, atLeast(5)).next();
     verifyNoMoreInteractions(lineIterator);
 }
}