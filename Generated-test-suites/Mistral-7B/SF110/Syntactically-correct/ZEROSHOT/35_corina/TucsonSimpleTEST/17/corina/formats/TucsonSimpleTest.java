import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import corina.formats.Sample;
import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.Year;
import corina.util.StringUtils;
import corina.util.Util;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private Sample sample;
 private BufferedWriter writer;
 private BufferedReader reader;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 tucsonSimple = new TucsonSimple();
 sample = new Sample();
 sample.setMeta(new HashMap<String, Object>() {{
 put("id", "123456");
 }});
 writer = mock(BufferedWriter.class);
 reader = mock(BufferedReader.class);
 }

 @After
 public void tearDown() throws IOException {
 if (writer != null) {
 writer.close();
 }
 if (reader != null) {
 reader.close();
 }
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
 public void testLoadNullReader() throws IOException {
 tucsonSimple.load(null);
 }

 @Test(expected = IOException.class)
 public void testLoadEmptyReader() throws IOException {
 when(reader.readLine()).thenReturn(null);
 tucsonSimple.load(reader);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadInvalidFormat() throws IOException {
 String input = "InvalidFormat";
 when(reader.readLine()).thenReturn(input);
 tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCodeNullSample() {
 assertEquals("000000", tucsonSimple.make6digitCode(null));
 }

 @Test
 public void testMake6digitCodeSampleWithId() {
 assertEquals("123456", tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testMake6digitCodeSampleWithoutId() {
 assertEquals("000000", tucsonSimple.make6digitCode(new Sample()));
 }

 @Test
 public void testSaveRowHeaderNullWriter() {
 tucsonSimple.saveRowHeader(null, sample, "000000", 6, new Year(2000));
 }

 @Test
 public void testSaveRowHeaderNormalCase() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
 BufferedWriter bufferedWriter = mock(BufferedWriter.class);
 when(bufferedWriter.write(anyString())).thenWriteTo(writer);
 tucsonSimple.saveRowHeader(bufferedWriter, sample, "000123", 6, new Year(2000));
 String expected = "0001232000";
 assertEquals(expected, writer.getBuffer().toString());
 }

 @Test
 public void testSaveDataNullSample() throws IOException {
 tucsonSimple.saveData(null, writer);
 }

 @Test
 public void testSaveDataNullWriter() throws IOException {
 tucsonSimple.saveData(sample, null);
 }

 @Test
 public void testSaveDataNormalCase() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
 BufferedWriter bufferedWriter = mock(BufferedWriter.class);
 when(bufferedWriter.write(anyString())).thenWriteTo(writer);
 when(reader.readLine()).thenReturn("Year Data Data");
 tucsonSimple.saveData(sample, bufferedWriter);
 String expected = "0000002001 Data Data\n" +
                 "0000002002 Data Data\n" +
                 "0000002003 Data Data\n";
 assertEquals(expected, writer.getBuffer().toString());
 }

 @Test
 public void testComputeRangeNullSample() {
 assertEquals(sample.getRange(), tucsonSimple.computeRange(null));
 }

 @Test
 public void testComputeRangeSampleWithYearBefore1() {
 Sample sample = new Sample();
 sample.setRange(new Range(new Year(1999), new Year(2000)));
 Range expected = new Range(new Year(1999), new Year(2000));
 assertEquals(expected, tucsonSimple.computeRange(sample));
 }

 @Test
 public void testComputeRangeSampleWithYearAfter8000() {
 Sample sample = new Sample();
 sample.setRange(new Range(new Year(8001), new Year(8002)));
 Range expected = new Range(new Year(8001), new Year(8002)).redateStartTo(new Year(1001));
 assertEquals(expected, tucsonSimple.computeRange(sample));
 }

 @Test
 public void testComputeRangeSampleWithYearBefore1001() {
 Sample sample = new Sample();
 sample.setRange(new Range(new Year(899), new Year(900)));
 Range expected = new Range(new Year(1001), new Year(1001)).redateBy(8999);
 assertEquals(expected, tucsonSimple.computeRange(sample));
 }

 @Test
 public void testSaveSample() throws IOException {
 File tempFile = File.createTempFile("test", null);
 tucsonSimple.save(sample, new BufferedWriter(new FileWriter(tempFile)));
 InputStream inputStream = new FileInputStream(tempFile);
 InputStreamReader inputReader = new InputStreamReader(inputStream);
 reader = new BufferedReader(inputReader);
 String expected = "0001232001 Data Data\n" +
                 "0001232002 Data Data\n" +
                 "0001232003 Data Data\n" +
                 "   999\n";
 String actual = "";
 String line;
 while ((line = reader.readLine()) != null) {
 actual += line;
 }
 assertEquals(expected, actual);
 tempFile.delete();
 }
}