import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter writerMock;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 writerMock = mock(BufferedWriter.class);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws IOException {
 Mockito.flushAllMocks();
 writerMock.close();
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
 public void testLoad() throws IOException {
 BufferedReader readerMock = mock(BufferedReader.class);
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = Mockito.mock(Sample.class);
 Mockito.when(sample.meta.get("id")).thenReturn("123456");
 String expectedCode = "123456";
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals(expectedCode, code);

 Mockito.when(sample.meta.get("id")).thenReturn(null);
 expectedCode = "000000";
 code = tucsonSimple.make6digitCode(sample);
 assertEquals(expectedCode, code);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = Mockito.mock(Sample.class);
 Year year = Mockito.mock(Year.class);
 Mockito.when(sample.range.getStart()).thenReturn(new Year(2022));
 Mockito.when(year.toString()).thenReturn("2022");
 Mockito.when(year.compareTo(new Year(2022))).thenReturn(0);
 BufferedWriter writer = Mockito.mock(BufferedWriter.class);
 String code = "ABCDEF";
 int yearWidth = 6;

 tucsonSimple.saveRowHeader(writerMock, sample, code, yearWidth, year);
 String expectedCodeAndYear = code + year.toString();
 String actualCodeAndYear = outputStream.toString();
 assertEquals(expectedCodeAndYear, actualCodeAndYear);
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = Mockito.mock(Sample.class);
 Range range = Mockito.mock(Range.class);
 Year start = Mockito.mock(Year.class);
 Year end = Mockito.mock(Year.class);
 Mockito.when(sample.data.get(Mockito.anyInt())).thenReturn("1.2");
 Mockito.when(sample.count.get(Mockito.anyInt())).thenReturn(1);
 Mockito.when(sample.range).thenReturn(range);
 Mockito.when(range.getStart()).thenReturn(start);
 Mockito.when(range.getEnd()).thenReturn(end);
 Mockito.when(start.compareTo(end)).thenReturn(-1);
 Mockito.when(end.column()).thenReturn(1);
 BufferedWriter writer = Mockito.mock(BufferedWriter.class);
 String code = "123456";
 int yearWidth = 6;
 String expectedData = "1.2";
 String expectedYear = "2022";

 Map<Integer, Integer> data = new HashMap<>();
 data.put(0, 1);
 tucsonSimple.saveData(sample, writerMock);
 String actualData = outputStream.toString();
 assertEquals(expectedData, actualData.substring(0, actualData.lastIndexOf("\n")));
 assertEquals(expectedCode + expectedYear, actualData.substring(0, actualData.lastIndexOf("\n")).split("\n")[0]);
 }

 @Test
 public void testComputeRange() {
 Sample sample = Mockito.mock(Sample.class);
 Year start = Mockito.mock(Year.class);
 Mockito.when(sample.range.getStart()).thenReturn(start);
 Year expectedStart = new Year(1001);
 Year end = Mockito.mock(Year.class);
 Mockito.when(start.compareTo(new Year(1))).thenReturn(-1);
 Mockito.when(start.add(8000).compareTo(new Year(1))).thenReturn(-1);
 Mockito.when(start.redateStartTo(new Year(1001))).thenReturn(expectedStart);
 Mockito.when(sample.range.getEnd()).thenReturn(end);
 Range expectedRange = new Range(expectedStart, end);
 Range actualRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, actualRange);
 }

 @Test
 public void testSave() throws IOException {
 Sample sample = Mockito.mock(Sample.class);
 BufferedWriter writer = Mockito.mock(BufferedWriter.class);
 tucsonSimple.save(sample, writerMock);
 verify(writerMock).write(Mockito.anyString());
 }
}