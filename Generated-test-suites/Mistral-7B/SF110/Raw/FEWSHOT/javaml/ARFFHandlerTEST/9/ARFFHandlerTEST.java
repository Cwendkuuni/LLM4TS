package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.javaml.core.*;
import org.junit.Before;
import org.junit.Test;

public class ARFFHandlerTest {

 private LineIterator mockLineIterator;
 private File mockFile;
 private Dataset expectedDataset;

 @Before
 public void setup() {
 mockLineIterator = mock(LineIterator.class);
 mockFile = mock(File.class);

 when(mockFile.exists()).thenReturn(true);
 when(mockFile.isFile()).thenReturn(true);
 when(mockFile.length()).thenReturn(100L); // Replace with actual file size if known
 when(mockFile.getAbsolutePath()).thenReturn("/path/to/file.arff");

 try {
 FileInputStream mockFileInputStream = mock(FileInputStream.class);
 when(mockFile.getInputStream()).thenReturn(mockFileInputStream);
 when(mockLineIterator.hasNext()).thenReturn(true, true, true, true, true, false); // Return true for all lines except last
 when(mockLineIterator.next()).thenReturn("@relation testRel", "@attribute attr1 num", "@attribute attr2 num", "@attribute attr3 {value1, value2}", "@data", "1.0,2.0,3.0,value1", "4.0,5.0,6.0,value2");

 List<String> lines = Arrays.asList("@relation testRel", "@attribute attr1 num", "@attribute attr2 num", "@attribute attr3 {value1, value2}", "@data", "1.0,2.0,3.0,value1", "4.0,5.0,6.0,value2");
 doAnswer(invocation -> {
 String line = (String) invocation.getArguments()[0];
 return lines.remove(lines.indexOf(line));
 }).when(mockLineIterator).remove();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }

 expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "value1"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "value2"));
 }

 @Test
 public void testLoadARFF() throws IOException {
 Dataset actualDataset = ARFFHandler.loadARFF(mockFile);
 verify(mockFile).getInputStream();
 verify(mockLineIterator).hasNext(); // First call
 verifyZeroInteractions(mockLineIterator); // No interactions between tests
 assertEquals(expectedDataset, actualDataset);
 }

 @Test
 public void testLoadARFFClassIndex() throws IOException {
 Dataset actualDataset = ARFFHandler.loadARFF(mockFile, 1);
 verify(mockFile).getInputStream();
 verify(mockLineIterator).hasNext(); // First call
 verifyZeroInteractions(mockLineIterator); // No interactions between tests
 assertEquals(expectedDataset, actualDataset);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFFileNotExists() throws IOException {
 ARFFHandler.loadARFF(mockFile);
 verifyZeroInteractions(mockFile);
 verifyZeroInteractions(mockLineIterator);
 }
}