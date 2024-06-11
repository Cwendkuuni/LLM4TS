package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

import corina.formats.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter mockWriter;
 private Sample sample;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 mockWriter = mock(BufferedWriter.class);
 sample = new Sample();
 sample.setData(new ArrayList<>());
 sample.setCount(new ArrayList<>());
 sample.setMeta(new HashMap<>());
 sample.setMeta().put("id", "testId");
 sample.setRange(new Range(new Year(1990), new Year(1995)));
 }

 @Test
 public void testToString() {
 String expected = I18n.getText("format.tucsonsimple");
 String result = tucsonSimple.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 String result = tucsonSimple.getDefaultExtension();
 assertEquals(expected, result);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 tucsonSimple.load(mock(BufferedReader.class));
 fail("load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Sample sampleWithId = new Sample();
 sampleWithId.setMeta(new HashMap<>());
 sampleWithId.setMeta().put("id", "123456");
 String expected = "123456";
 String result = tucsonSimple.make6digitCode(sampleWithId);
 assertEquals(expected, result);

 Sample sampleWithoutId = new Sample();
 String expectedCode = "000000";
 String result = tucsonSimple.make6digitCode(sampleWithoutId);
 assertEquals(expectedCode, result);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 String code = "testCode";
 int yearWidth = 5;
 Year y = new Year(1992);
 BufferedWriter w = mock(BufferedWriter.class);
 tucsonSimple.saveRowHeader(w, sample, code, yearWidth, y);
 verify(w).write(code + "1992");
 }

 @Test
 public void testSaveData() throws IOException {
 Year start = new Year(1990);
 Year end = new Year(1995);
 String code = "testCode";
 final int dataValue = 123;
 final int countValue = 45;
 final int yearWidth = 6;
 sample.getData().add(dataValue);
 sample.getCount().add(countValue);
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getArgument(0));
 when(mockWriter.newLine()).thenReturn("");
 tucsonSimple.saveData(sample, mockWriter);

 String expected = "testCode1990" + StringUtils.repeat(" ", 4) + dataValue + StringUtils.repeat(" ", 3) + countValue + "\n";
 String result = mockWriter.getCalls().get(0).getArgument(0);
 assertEquals(expected, result);
 }

 @Test
 public void testSaveDataProcessed() throws IOException {
 Year start = new Year(1990);
 Year end = new Year(1995);
 String code = "testCode";
 final int dataValue = 123;
 final int yearWidth = 4;
 sample.getData().add(dataValue);
 sample.setProcessed(true);
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getArgument(0));
 when(mockWriter.newLine()).thenReturn("");
 tucsonSimple.saveData(sample, mockWriter);

 String expected = "testCode" + StringUtils.repeat(" ", yearWidth) + dataValue + "\n";
 String result = mockWriter.getCalls().get(0).getArgument(0);
 assertEquals(expected, result);
 }

 @Test
 public void testSaveDataOutOfRange() throws IOException {
 Year start = new Year(1990);
 Year end = new Year(1995);
 String code = "testCode";
 when(mockWriter.write(anyString())).thenAnswer(i -> i.getArgument(0));
 when(mockWriter.newLine()).thenReturn("");
 tucsonSimple.saveData(sample, mockWriter);
 w.write("999");
 verify(mockWriter, times(2)).write("999");
 }
}