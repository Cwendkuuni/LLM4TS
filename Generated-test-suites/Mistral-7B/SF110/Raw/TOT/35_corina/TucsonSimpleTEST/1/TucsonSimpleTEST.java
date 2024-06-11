package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.ui.*;
import corina.util.*;
import corina.*;
import static corina.util.StringUtils.*;

public class TucsonSimpleTest {

 private static final String I18N_TEXT = "format.tucsonsimple";
 private static final String DEFAULT_EXTENSION = ".RWL";

 private TucsonSimple tucsonSimple;
 private BufferedReader mockBufferedReader;
 private BufferedWriter mockBufferedWriter;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 mockBufferedReader = mock(BufferedReader.class);
 mockBufferedWriter = mock(BufferedWriter.class);
 outputStream = new ByteArrayOutputStream();
 when(mockBufferedWriter.write(anyString())).then(invocation -> outputStream.write(invocation.getArgument(0).toString().getBytes()));
 when(mockBufferedWriter.newLine()).then(() -> outputStream.write("\n".getBytes()));
 }

 @Test
 public void testToString() {
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadMethodThrowsException() throws IOException {
 tucsonSimple.load(mockBufferedReader);
 fail("load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 String expectedCode = "123456";
 assertEquals(expectedCode, tucsonSimple.make6digitCode(sample));

 Sample sampleWithoutId = new Sample();
 String expectedCodeWithoutId = "000000";
 assertEquals(expectedCodeWithoutId, tucsonSimple.make6digitCode(sampleWithoutId));
 }

 @Test
 public void testSaveDataWritesCorrectDataToBuffer() throws IOException {
 Sample sample = new Sample();
 Year start = new Year(2000);
 Year end = new Year(2002);
 sample.range = new Range(start, end);
 String expectedData = "123 2000\n456 2001\n789 2002\n";
 String expectedSummedData = "123 2000\n456 2001\n789 2002\n9990  0\n999";
Map<Integer, Double> data = new HashMap<>();
 data.put(-2, 123.0);
 data.put(-1, 456.0);
 data.put(0, 789.0);
 sample.data = data;

 tucsonSimple.saveData(sample, mockBufferedWriter);
 assertEquals(expectedData, outputStream.toString());

 tucsonSimple.saveData(sample, mockBufferedWriter, true);
 assertEquals(expectedSummedData, outputStream.toString());
 }
}