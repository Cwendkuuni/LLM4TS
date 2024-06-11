package corina.formats.test;

import static org.junit.Assert.*;

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

import corina.formats.TucsonSimple;
import corina.formats.Filetype;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private static final String TEST_DATA = "id:1\nname:John Doe\ndata:10 20 30 40 50\ncount:1 1 1 1 1\nrange:1999-2000";

 @Test
 public void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testGetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("Incorrect data".getBytes())));
 tucsonSimple.load(reader);
 fail("Should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta = new HashMap<String, String>();
 sample.meta.put("id", "123456");
 String result = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", result);

 Sample sample2 = new Sample();
 String result2 = tucsonSimple.make6digitCode(sample2);
 assertEquals("000000", result2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.meta = new HashMap<String, String>();
 sample.meta.put("id", "123456");
 String code = "123456";
 int yearWidth = 4;
 Year year = new Year(2000);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) writer.getOutputStream();
 assertEquals("1234562000", outputStream.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = createSample();
 String data = "10 20 30 40 50";
 String countData = "1 1 1 1 1";
 tucsonSimple.saveData(sample, writer);
 String expectedOutput = "123456 1999  10\n123456 2000  20\n123456 2001  30\n123456 2002  40\n123456 2003  50\n9990   ";
 assertEquals(expectedOutput, writer.getOutputStream().toString());
 }

 @Test
 public void testComputeRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = createSample();
 Year start = new Year(1999);
 Range result = tucsonSimple.computeRange(sample);
 assertEquals(start, result.getStart());
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = createSample();
 tucsonSimple.save(sample, writer);
 String expectedOutput = "123456 1999  10\n123456 2000  20\n123456 2001  30\n123456 2002  40\n123456 2003  50\n9990   ";
 assertEquals(expectedOutput, writer.getOutputStream().toString());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testSave_WrongFiletypeException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = createSample();
 tucsonSimple.save(sample, writer);
 String data = "10 20 30 40 50\n1 1 1 1 1";
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data.getBytes())));
 tucsonSimple.load(reader);
 fail("Should throw WrongFiletypeException");
 }

 @Test
 public void testSave_InvalidDataFormat() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = createSample();
 String data = "10 20 30 id:1 name:John Doe\n12 21 32 43 54";
 tucsonSimple.save(sample, writer);
 String output = writer.getOutputStream().toString();
 String expectedOutput = "123456 1999  10\n123456 2000  21\n123456 2001  32\n123456 2002  43\n123456 2003  54\n9990   ";
 assertEquals(expectedOutput, output);
 }

 private Sample createSample() {
 Sample sample = new Sample();
 sample.data = new int[1000];
 sample.count = new int[1000];
 sample.meta = new HashMap<String, String>();
 sample.meta.put("id", "123456");
 sample.range = new Range(new Year(1999), new Year(2003));
 return sample;
 }

}