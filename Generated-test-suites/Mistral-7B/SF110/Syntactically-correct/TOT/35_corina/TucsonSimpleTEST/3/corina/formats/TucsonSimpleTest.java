package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private final Sample sample = new Sample() {{
 meta = new HashMap<String, Object>() {{
 put("id", 12345);
}};
 data = new int[100];
 count = new int[100];
 range = new Range(new Year(2000), new Year(2002));
 }};

 @Test
 public void testToString() throws Exception {
 TucsonSimple t = new TucsonSimple();
 String result = t.toString();
 String expected = I18n.getText("format.tucsonsimple");
 assertEquals(expected, result);
 }

 @Test
 public void testgetDefaultExtension() throws Exception {
 TucsonSimple t = new TucsonSimple();
 String result = t.getDefaultExtension();
 String expected = ".RWL";
 assertEquals(expected, result);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadFailed() throws Exception {
 TucsonSimple t = new TucsonSimple();
 BufferedReader mockReader = mock(BufferedReader.class);
 when(mockReader.readLine()).thenReturn("TestData");
 t.load(mockReader);
 fail("load method should have thrown WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() throws Exception {
 TucsonSimple t = new TucsonSimple();
 String result = t.make6digitCode(sample);
 String expected = "123450";
 assertEquals(expected, result);
 }

 @Test
 public void testSaveRowHeader() throws Exception {
 TucsonSimple t = new TucsonSimple();
 BufferedWriter mockWriter = mock(BufferedWriter.class);
 Year start = new Year(2001);
 int yearWidth = 6;
 Year y = new Year(2002);
 String code = "123450";
 t.saveRowHeader(mockWriter, sample, code, yearWidth, start);
 String expected = "1234502001";
 verify(mockWriter).write(expected);
 }

 @Test
 public void testSaveData() throws Exception {
 TucsonSimple t = new TucsonSimple();
 BufferedWriter mockWriter = mock(BufferedWriter.class);
 StringWriter output = new StringWriter();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 writer.init(output);
 mockWriter = mockWriter;
 Year start = new Year(2001);
 Year end = new Year(2002);
 String code = "123450";
 t.saveData(sample, writer);
 String expected = "1234502001  1\n" +
 "1234502002  2\n" +
 "1234502003  3\n";
 String result = output.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testComputeRange() throws Exception {
 TucsonSimple t = new TucsonSimple();
 Year result = t.computeRange(sample);
 Year expected = new Year(1001);
 assertEquals(expected, result);
 }

 @Test
 public void testSave() throws Exception {
 TucsonSimple t = new TucsonSimple();
 BufferedWriter mockWriter = mock(BufferedWriter.class);
 StringReader reader = new StringReader("TestData");
 BufferedReader input = new BufferedReader(reader);
 t.save(sample, mockWriter);
 verify(mockWriter, times(1)).write("123450");
 verify(mockWriter, times(1)).write("2001");
 verify(mockWriter, times(1)).write("9990  0");
 verify(mockWriter, times(2)).write("1");
 verify(mockWriter, times(2)).write("2");
 verify(mockWriter, times(2)).write("3");
 }

 @Test
 public void testSaveWithProcessedData() throws Exception {
 TucsonSimple t = new TucsonSimple();
 BufferedWriter mockWriter = mock(BufferedWriter.class);
 StringReader reader = new StringReader("TestData");
 BufferedReader input = new BufferedReader(reader);
 t.save(sample, mockWriter);
 sample.setProcessed(true);
 t.save(sample, mockWriter);
 verify(mockWriter, times(3)).write("123450");
 verify(mockWriter, times(1)).write("2001");
 verify(mockWriter, times(1)).write("2001   ");
 verify(mockWriter, times(1)).write("9990  0");
 verify(mockWriter, times(1)).write("9990  0");
 verify(mockWriter, times(1)).write("999");
 }

 @Test(expected = IOException.class)
 public void testSaveWithInvalidData() throws Exception {
 TucsonSimple t = new TucsonSimple();
 BufferedWriter mockWriter = mock(BufferedWriter.class);
 StringReader reader = new StringReader("TestData");
 BufferedReader input = new BufferedReader(reader);
 sample.setData(new int[101]);
 t.save(sample, mockWriter);
 fail("save method should have thrown IOException");
 }
}