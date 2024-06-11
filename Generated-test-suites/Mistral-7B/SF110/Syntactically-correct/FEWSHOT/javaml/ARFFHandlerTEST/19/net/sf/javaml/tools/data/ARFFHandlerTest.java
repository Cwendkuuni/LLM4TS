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
 private LineIterator mockLineIterator;

 @Test
 public void testLoadARFFWithNullFile() {
     assertThrows(NullPointerException.class, () -> ARFFHandler.loadARFF(null));
 }

 @Test
 public void testLoadARFFWithValidFile() throws Exception {
     when(mockLineIterator.hasNext()).thenReturn(true, true, false);
     when(mockLineIterator.next()).thenReturn("@relation foobar", "@attribute a numeric", "@attribute b string", "@data", "1,1.0,hello,world");

     final File file = mock(File.class);
     when(file.exists()).thenReturn(true);
     when(file.toURI()).thenReturn(new URI("file:test.arff"));
     when(mockLineIterator.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{"@relation foobar".getBytes()}));

     final Dataset result = ARFFHandler.loadARFF(file);

     final Instance expectedInstance = new DenseInstance(new double[]{1.0}, "world");
     verify(mockLineIterator, times(4)).next();
     verify(mockLineIterator).hasNext();
     verify(mockLineIterator).hasNext();
     verify(mockLineIterator).hasNext();
     verifyNoMoreInteractions(mockLineIterator);
     assertEquals(1, result.size());
     assertEquals(expectedInstance, result.instance(0));
 }

 @Test
 public void testLoadARFFWithClassIndex() throws Exception {
     when(mockLineIterator.hasNext()).thenReturn(true, true, false);
     when(mockLineIterator.next()).thenReturn("@relation foobar", "@attribute a numeric", "@attribute b string", "@data", "1,1.0,hello,world", "2,2.0,foo,bar");

     final File file = mock(File.class);
     when(file.exists()).thenReturn(true);
     when(file.toURI()).thenReturn(new URI("file:test.arff"));
     when(mockLineIterator.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{"@relation foobar".getBytes()}));

     final Dataset result = ARFFHandler.loadARFF(file, 1);

     final Instance expectedInstance1 = new DenseInstance(new double[]{1.0}, "world");
     final Instance expectedInstance2 = new DenseInstance(new double[]{2.0, 2.0}, "bar");
     verify(mockLineIterator, times(6)).next();
     verify(mockLineIterator).hasNext();
     verify(mockLineIterator).hasNext();
     verify(mockLineIterator).hasNext();
     verify(mockLineIterator).hasNext();
     verify(mockLineIterator).hasNext();
     verifyNoMoreInteractions(mockLineIterator);
     assertEquals(2, result.size());
     assertEquals(expectedInstance1, result.instance(0));
     assertEquals(expectedInstance2, result.instance(1));
 }

 @Test
 public void testLoadARFFWithInvalidFile() {
     when(mockLineIterator.hasNext()).thenReturn(true);
     when(mockLineIterator.next()).thenReturn("@relation foobar", "@attribute a numeric", "@attribute b string", "@data", "1,1.0,hello,world", null);

     final File file = mock(File.class);
     when(file.exists()).thenReturn(true);
     when(file.toURI()).thenReturn(new URI("file:test.arff"));
     when(mockLineIterator.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{"@relation foobar".getBytes()}));

     assertThrows(IOException.class, () -> ARFFHandler.loadARFF(file));
 }
}