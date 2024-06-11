package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

 private static final String DEFAULT_ENCODING = "UTF-8";

 @Test
 public void testToString() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testGetDefaultExtension() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadMethodThrowsException() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader inputStreamMock = mock(BufferedReader.class);
 when(inputStreamMock.readLine()).thenReturn("some text");
 TucsonSimple tucson = new TucsonSimple();
 tucson.load(inputStreamMock);
 fail("Exception expected but not thrown");
 }

 @Test
 public void testMake6digitCode_SampleWithID() throws Exception {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("123456", tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testMake6digitCode_SampleWithoutID() throws Exception {
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("000000", tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testMake6digitCode_SampleWithShortID() throws Exception {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123");
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("123   ", tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testSaveRowHeader_YearsBeforeStart() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writerMock = mock(BufferedWriter.class);
 Sample sample = new Sample();
 Year start = new Year(1970);
 Year year = new Year(1969);
 String code = "123456";
 int yearWidth = 6;
 tucsonSimple.saveRowHeader(writerMock, sample, code, yearWidth, year);
 verify(writerMock).write(eq("1969123456"));
 }

 @Test
 public void testSaveRowHeader_YearsAfterEnd() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writerMock = mock(BufferedWriter.class);
 Sample sample = new Sample();
 Year start = new Year(1970);
 Year end = new Year(1971);
 Year year = new Year(1972);
 String code = "123456";
 int yearWidth = 6;
 tucsonSimple.saveRowHeader(writerMock, sample, code, yearWidth, year);
 assertTrue(writerMock.writeCalls().isEmpty());
 }

 @Test
 public void testSaveData_ProcessedAndEndYearReached() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writerMock = mock(BufferedWriter.class);
 Sample sample = new Sample();
 Year start = new Year(1970);
 Year end = new Year(1972);
 String code = "123456";
 tucsonSimple.saveData(sample, writerMock);
 verify(writerMock).write(eq("1970123456"));
 verify(writerMock).write(eq("1971123456"));
 verify(writerMock).write(eq("1972123456"));
 verify(writerMock).write(eq("9990   "));
 verify(writerMock).write(eq("9990   "));
 verify(writerMock).write(eq("999"));
 }

 @Test
 public void testSaveData_ProcessedAndEndColumnReached() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writerMock = mock(BufferedWriter.class);
 Sample sample = new Sample();
 Year start = new Year(1970);
 Year end = new Year(1972);
 String code = "123456";
 tucsonSimple.saveData(sample, writerMock);
 verify(writerMock).write(eq("1970123456"));
 verify(writerMock).write(eq("1971123456"));
 verify(writerMock).write(eq("1972123456"));
 verify(writerMock).write(eq("9990   "));
 verify(writerMock).write(eq("9990   "));
 verify(writerMock).write(eq("999"));
 verify(writerMock).write(eq("\n"));
 }

 @Test
 public void testComputeRange_StartYearValid() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(1970);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(start, range.getStart());
 }

 @Test
 public void testComputeRange_StartYearBefore1() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(-1);
 Year expectedStart = new Year(1001);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(expectedStart, range.getStart());
 }

 @Test
 public void testComputeRange_StartYearAfter8000() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(8001);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(start.redateStartTo(new Year(1001)), range.getStart());
 }

 @Test
 public void testSave() throws Exception {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writerMock = mock(BufferedWriter.class);
 Sample sample = new Sample();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 InputStreamReader reader = new InputStreamReader(inputStream, DEFAULT_ENCODING);
 InputStreamReader inputStreamReaderMock = mock(InputStreamReader.class);
 when(inputStreamReaderMock.readLine()).thenAnswer(i -> null);
 when(inputStreamReaderMock.readAllLines()).thenReturn(new String[]{"some text"});
 when(reader.readLine()).thenAnswer(i -> null);
 when(reader.readAllLines()).thenReturn(new String[]{"1970 data"});
 when(writerMock.write(anyString())).thenAnswer(i -> "");
 when(writerMock.newLine()).thenAnswer(i -> System.lineSeparator());
 I18n i18nMock = mock(I18n.class);
 when(i18nMock.getText(eq("format.tucsonsimple"))).thenReturn("format.tucsonsimple");
 when(i18nMock.getText(eq("error.wrongfiletype"))).thenReturn("error.wrongfiletype");
 TucsonSimple tucson = new TucsonSimple();
 tucson.save(sample, writerMock);
 verify(writerMock, times(2)).write(anyString());
 verify(writerMock, times(2)).newLine();
 verify(i18nMock).getText(eq("format.tucsonsimple"));
 verify(i18nMock).getText(eq("error.wrongfiletype"));
 }
}