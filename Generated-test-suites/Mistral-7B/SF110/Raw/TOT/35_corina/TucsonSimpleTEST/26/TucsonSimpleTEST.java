package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.WrongFiletypeException;
import corina.util.Sample;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader readerMock;
 private BufferedWriter writerMock;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 readerMock = mock(BufferedReader.class);
 writerMock = mock(BufferedWriter.class);
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
 public void testLoadThrowsException() throws IOException {
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 sample = new Sample();
 String code2 = tucsonSimple.make6digitCode(sample);
 assertEquals("000000", code2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = new Sample();
 Year start = new Year(2000);
 BufferedWriter w = mock(BufferedWriter.class);
 String code = "123456";
 String prefix = "2000";
 String expectedOutput = code + prefix;
 tucsonSimple.saveRowHeader(w, sample, code, 6, start);
 verify(w).write(expectedOutput);

 start = new Year(2001);
 prefix = "2000";
 expectedOutput = code + prefix;
 tucsonSimple.saveRowHeader(w, sample, code, 6, start);
 verify(w, atLeast(2)).write(expectedOutput);

 start = new Year(2000);
 prefix = "2001";
 expectedOutput = code + prefix;
 tucsonSimple.saveRowHeader(w, sample, code, 6, start);
 verify(w, atLeast(3)).write(expectedOutput);
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 sample.data = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 sample.count = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 BufferedWriter w = mock(BufferedWriter.class);
 Year start = new Year(2000);
 Year end = new Year(2003);
 String code = "123456";
 tucsonSimple.saveData(sample, w);

 ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<int[]> dataCaptor = ArgumentCaptor.forClass(int[].class);
 ArgumentCaptor<int[]> countCaptor = ArgumentCaptor.forClass(int[].class);
 ArgumentCaptor<boolean[]> isSummedCaptor = ArgumentCaptor.forClass(boolean[].class);
 ArgumentCaptor<boolean[]> isProcessedCaptor = ArgumentCaptor.forClass(boolean[].class);
 ArgumentCaptor<Year> startCaptor = ArgumentCaptor.forClass(Year.class);
 ArgumentCaptor<Year> endCaptor = ArgumentCaptor.forClass(Year.class);

 verify(w).write(codeCaptor.capture());
 assertEquals(code, codeCaptor.getValue());

 verify(w).write(eq("    "), anyString());
 times(4).verify(w).write(StringUtils.leftPad(anyInt(), 4));

 verify(w).write(eq("    "), anyString());
 times(3).verify(w).write(eq("    "));

 verify(w).write(isA(int[].class));
 verify(w).write(dataCaptor.capture());
 assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toArray(), dataCaptor.getValue());

 verify(w).write(eq("    "), anyString());
 times(10).verify(w).write(StringUtils.leftPad(anyInt(), 3));

 verify(w).write(eq("    "), anyString());
 verify(w).write(isA(boolean[].class));
 verify(w).write(isProcessedCaptor.capture());
 assertFalse(isProcessedCaptor.getValue()[0]);
 assertFalse(isProcessedCaptor.getValue()[1]);

 verify(w).write(eq("    "), anyString());
 times(2).verify(w).write(eq("    "));

 verify(w).write(eq("\n"));
 verify(w).write(eq("\n"));

 verifyNoMoreInteractions(w);
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 sample.range = new YearRange(new Year(1999), new Year(2001));
 Year start = sample.range.getStart();
 Year end = sample.range.getEnd();

 Range range = tucsonSimple.computeRange(sample);
 assertEquals(start, range.getStart());
 assertEquals(end, range.getEnd());
 }

 @Test
 public void testSave() throws IOException {
 Sample sample = new Sample();
 sample.data = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 sample.count = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 sample.range = new YearRange(new Year(2000), new Year(2003));
 File file = new File("output.txt");
 BufferedWriter writer = new BufferedWriter(new FileWriter(file));
 tucsonSimple.save(sample, writer);

 File expectedFile = new File("expectedOutput.txt");
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 BufferedWriter expectedWriter = new BufferedWriter(new OutputStreamWriter(os));
 tucsonSimple.save(sample, expectedWriter);
 String expectedOutput = os.toString();

 assertEquals(expectedOutput, readFileToString(file));
 }

 private static String readFileToString(File file) {
 String content = "";
 try {
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line;
 while ((line = reader.readLine()) != null) {
 content += line;
 }
 reader.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 return content;
 }
}