package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import corina.formats.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

 private Sample sample;
 private BufferedReader mockReader;
 private BufferedWriter mockWriter;
 private TucsonSimple tucsonSimple;

 @Before
 public void setUp() {
 sample = new Sample();
 sample.setData(new ArrayList<Double>());
 sample.setMeta(new HashMap<String, Object>());
 sample.setCount(new HashMap<Year, Integer>());
 mockReader = mock(BufferedReader.class);
 mockWriter = mock(BufferedWriter.class);
 tucsonSimple = new TucsonSimple();
 }

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
 assertEquals(expected, tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 assertEquals(expected, tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadWithInvalidReader() throws IOException {
 when(mockReader.readLine()).thenReturn("");
 tucsonSimple.load(mockReader);
 fail("load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCodeWithSampleId() {
 String expected = "123456";
 sample.getMeta().put("id", "123456");
 String actual = tucsonSimple.make6digitCode(sample);
 assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithoutSampleId() {
 String expected = "000000";
 String actual = tucsonSimple.make6digitCode(sample);
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderWithValidStartYearAndProcessedFlagFalse() throws IOException {
 Year start = new Year(2001);
 String code = "123456";
 int yearWidth = 6;
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getCallerMethod().invokeSuper());
 ArgumentCaptor<Sample> sampleArgumentCaptor = ArgumentCaptor.forClass(Sample.class);
 tucsonSimple.saveRowHeader(mockWriter, sample, code, yearWidth, start);
 verify(mockWriter).write(code + start.toString());
 }

 @Test
 public void testSaveRowHeaderWithValidStartYearAndProcessedFlagTrue() throws IOException {
 Year start = new Year(2001);
 Year startProcessed = new Year(2000);
 String code = "123456";
 int yearWidth = 4;
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getCallerMethod().invokeSuper());
 tucsonSimple.saveRowHeader(mockWriter, sample, code, yearWidth, start, true);
 verify(mockWriter).write(code + startProcessed.toString().substring(0, 2));
 }

 @Test(expected = WrongFiletypeException.class)
 public void testSaveDataWithNullSample() throws IOException {
 tucsonSimple.saveData(null, mockWriter);
 fail("saveData method should throw WrongFiletypeException when Sample is null");
 }

 @Test
 public void testSaveDataWithValidSampleAndWriter() throws IOException {
 Year start = new Year(2001);
 Year end = new Year(2002);
 String code = "123456";
 double sampleData = 12.3;
 sample.getData().add(sampleData);
 sample.setRange(new Range(start, end));
 Year y = start;
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getCallerMethod().invokeSuper());
 ArgumentCaptor<Sample> sampleArgumentCaptor = ArgumentCaptor.forClass(Sample.class);
 tucsonSimple.saveData(sample, mockWriter);
 verify(mockWriter).write("1234562001");
 verify(mockWriter).write("12.3");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(" ");
 verify(mockWriter).write(StringUtils.leftPad("12.3", 4));
 verify(mockWriter).write(" ");
 verify(mockWriter).newLine();
 verify(mockWriter).newLine();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSaveDataWithInvalidStartYear() throws IOException {
 Year start = new Year(-1);
 Year end = new Year(2002);
 String code = "123456";
 double sampleData = 12.3;
 sample.getData().add(sampleData);
 sample.setRange(new Range(start, end));
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getCallerMethod().invokeSuper());
 ArgumentCaptor<Sample> sampleArgumentCaptor = ArgumentCaptor.forClass(Sample.class);
 tucsonSimple.saveData(sample, mockWriter);
 fail("saveData method should throw IndexOutOfBoundsException when start year is invalid");
 }

 @Test
 public void testSaveDataWithValidStartYearButEndYearBeforeStartYear() throws IOException {
 Year start = new Year(2001);
 Year end = new Year(2000);
 String code = "123456";
 double sampleData = 12.3;
 sample.getData().add(sampleData);
 sample.setRange(new Range(start, end));
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getCallerMethod().invokeSuper());
 ArgumentCaptor<Sample> sampleArgumentCaptor = ArgumentCaptor.forClass(Sample.class);
 tucsonSimple.saveData(sample, mockWriter);
 verify(mockWriter).write("1234562000");
 verify(mockWriter).write("999");
 verify(mockWriter).newLine();
 }

 @Test
 public void testSaveDataWithProcessedFlagTrue() throws IOException {
 Year start = new Year(2001);
 Year end = new Year(2003);
 String code = "123456";
 double sampleData = 12.3;
 sample.getData().add(sampleData);
 sample.setRange(new Range(start, end));
 sample.setProcessed(true);
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getCallerMethod().invokeSuper());
 ArgumentCaptor<Sample> sampleArgumentCaptor = ArgumentCaptor.forClass(Sample.class);
 tucsonSimple.saveData(sample, mockWriter);
 verify(mockWriter).write("1234562001");
 verify(mockWriter).write("12.3");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write(" ");
 verify(mockWriter).newLine();
 verify(mockWriter).newLine();
 }

 @Test(expected = WrongFiletypeException.class)
 public void testSaveWithoutSample() throws IOException {
 tucsonSimple.save(null, mockWriter);
 fail("save method should throw WrongFiletypeException when Sample is null");
 }

 @Test
 public void testSaveWithValidSampleAndWriter() throws IOException {
 Year start = new Year(2001);
 Year end = new Year(2002);
 String code = "123456";
 double sampleData = 12.3;
 Sample sample = new Sample();
 sample.setData(new ArrayList<Double>());
 sample.setMeta(new HashMap<String, Object>());
 sample.setCount(new HashMap<Year, Integer>());
 sample.setProcessed(false);
 sample.setRange(new Range(start, end));
 sample.getData().add(sampleData);
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getCallerMethod().invokeSuper());
 ArgumentCaptor<Sample> sampleArgumentCaptor = ArgumentCaptor.forClass(Sample.class);
 tucsonSimple.save(sample, mockWriter);
 verify(mockWriter).write("1234562001");
 verify(mockWriter).write("12.3");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write("   ");
 verify(mockWriter).write(" ");
 verify(mockWriter).newLine();
 verify(mockWriter).newLine();
 }

 @Rule
 public ExpectedException expectedException = ExpectedException.none();

 @Test
 public void testSaveWithNullWriter() {
 expectedException.expect(NullPointerException.class);
 tucsonSimple.save(sample, null);
 }
}