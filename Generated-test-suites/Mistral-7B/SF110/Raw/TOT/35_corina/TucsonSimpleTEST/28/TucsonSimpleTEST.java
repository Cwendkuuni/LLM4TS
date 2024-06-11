package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.WrongFiletypeException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private I18n i18n;
 private Sample sample;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 tucsonSimple = new TucsonSimple();
 i18n = mock(I18n.class);
 sample = new Sample();
 sample.setMeta(new HashMap<String, Object>());
 }

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
 when(i18n.getText(expected)).thenReturn("Tucson Simple");
 assertEquals("Tucson Simple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadInvalidFile() throws IOException {
 BufferedReader reader = mock(BufferedReader.class);
 when(reader.readLine()).thenReturn("Invalid data");
 tucsonSimple.load(reader);
 fail("load should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCodeForSampleWithId() {
 sample.getMeta().put("id", "123456");
 String expected = "123456";
 assertEquals(expected, tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testMake6digitCodeForSampleWithoutId() {
 String expected = "000000";
 assertEquals(expected, tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testSaveRowHeaderForStartYear() throws IOException {
 BufferedWriter writer = mock(BufferedWriter.class);
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(2000);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 verify(writer).write(code + "2000");
 }

 @Test
 public void testSaveRowHeaderForEndYear() throws IOException {
 BufferedWriter writer = mock(BufferedWriter.class);
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, end);
 verify(writer).write(code + "2001");
 }

 @Test
 public void testSaveDataForSampleWithDataAndNoSumOrProcess() throws IOException {
 BufferedWriter writer = mock(BufferedWriter.class);
 Sample sample = new Sample();
 sample.setData(new Object[]{10.5d, 11.3d, 12.2d, 13.0d, 14.2d, 15.1d});
 String expected = "10.5\n11.3\n12.2\n13.0\n14.2\n15.1\n";
 when(writer.write(anyString())).thenAnswer(invocation -> {
 String str = invocation.getArgument(0);
 System.out.print(str);
 return str.length();
 });
 tucsonSimple.saveData(sample, writer);
 verify(writer).write("10.5\n11.3\n11.3\n12.2\n12.2\n13.0\n13.0\n14.2\n14.2\n15.1\n15.1\n");
 }

 @Test
 public void testSaveDataForSampleWithDataAndSum() throws IOException {
 BufferedWriter writer = mock(BufferedWriter.class);
 Sample sample = new Sample();
 sample.setData(new Object[]{10.5d, 11.3d, 12.2d, 13.0d, 14.2d, 15.1d});
 Map<Integer, Double> count = new HashMap<>();
 count.put(0, 1.0);
 count.put(1, 2.0);
 sample.setCount(count);
 String expected = "10.5\n11.3\n11.3\n12.2\n12.2\n13.0\n13.0\n14.2\n14.2\n15.1\n15.1\n1.0 2.0\n";
 when(writer.write(anyString())).thenAnswer(invocation -> {
 String str = invocation.getArgument(0);
 System.out.print(str);
 return str.length();
 });
 tucsonSimple.saveData(sample, writer);
 verify(writer).write("10.5\n11.3\n11.3\n12.2\n12.2\n13.0\n13.0\n14.2\n14.2\n15.1\n15.1\n1.0 2.0\n");
 }

 @Test
 public void testSaveDataForSampleWithDataAndProcess() throws IOException {
 BufferedWriter writer = mock(BufferedWriter.class);
 Sample sample = new Sample();
 sample.setData(new Object[]{10.5d, 11.3d, 12.2d, 13.0d, 14.2d, 15.1d});
 String expected = "   999\n9990  0\n9990  0\n9990  0\n9990  0\n9990  0\n";
 when(writer.write(anyString())).thenAnswer(invocation -> {
 String str = invocation.getArgument(0);
 System.out.print(str);
 return str.length();
 });
 tucsonSimple.saveData(sample, writer);
 verify(writer).write("   999\n9990  0\n9990  0\n9990  0\n9990  0\n9990  0\n");
 }

 @Test
 public void testComputeRangeForSampleWithStartYearLessThanOne() {
 Range expected = new Range(new Year(1), new Year(1001));
 assertEquals(expected, tucsonSimple.computeRange(sample));
 }

 @Test
 public void testComputeRangeForSampleWithStartYearBetweenOneAnd8000() {
 Year start = new Year(3000);
 Range expected = new Range(start, start.add(8000));
 assertEquals(expected, tucsonSimple.computeRange(sample));
 }

 @Test
 public void testComputeRangeForSampleWithStartYearGreaterThan8000() {
 Year start = new Year(8001);
 Range expected = new Range(start, start.add(8000));
 assertEquals(expected, tucsonSimple.computeRange(sample));
 }

 @Test
 public void testSave() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 Sample sample = new Sample();
 sample.setData(new Object[]{10.5d, 11.3d, 12.2d, 13.0d, 14.2d, 15.1d});
 String expected = "10.5\n11.3\n11.3\n12.2\n12.2\n13.0\n13.0\n14.2\n14.2\n15.1\n15.1\n";
 when(writer.write(anyString())).thenAnswer(invocation -> {
 String str = invocation.getArgument(0);
 outputStream.write(str.getBytes());
 return str.length();
 });
 tucsonSimple.save(sample, writer);
 String result = new String(outputStream.toByteArray());
 assertEquals(expected, result);
 }

 @Test
 public void testSaveWithInvalidSampleData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 Sample sample = new Sample();
 sample.setData(new Object[]{null, 11.3d, 12.2d, 13.0d, 14.2d, 15.1d});
 String expected = "";
 when(writer.write(anyString())).thenAnswer(invocation -> {
 String str = invocation.getArgument(0);
 outputStream.write(str.getBytes());
 return str.length();
 });
 tucsonSimple.save(sample, writer);
 String result = new String(outputStream.toByteArray());
 assertEquals(expected, result);
 }
}