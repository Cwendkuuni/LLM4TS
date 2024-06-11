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
import corina.formats.Filetype;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

 private static final String TEST_DATA = "id:1 name:Test Sample start:1999 column:1 data:123 end:2000 column:2 data:321";

 @Test
 public void testToString() {
  Filetype tucsonSimple = new TucsonSimple();
  String result = tucsonSimple.toString();
  assertEquals("format.tucsonsimple", result);
 }

 @Test
 public void testgetDefaultExtension() {
  Filetype tucsonSimple = new TucsonSimple();
  String result = tucsonSimple.getDefaultExtension();
  assertEquals(".RWL", result);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
  tucsonSimple.load(reader);
  fail("Load method should have thrown WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
  Sample sample = new Sample();
  sample.meta = new HashMap<>();
  sample.meta.put("id", "123456");
  Filetype tucsonSimple = new TucsonSimple();
  String result = tucsonSimple.make6digitCode(sample);
  assertEquals("123456", result);

  sample.meta.clear();
  result = tucsonSimple.make6digitCode(sample);
  assertEquals("000000", result);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Filetype tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  Year year = new Year(1999);
  String code = "123456";
  int yearWidth = 6;
  tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
  String expected = "1234561999";
  assertEquals(expected, writer.toString());
 }

 @Test
 public void testSaveData() throws IOException {
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Filetype tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.meta = new HashMap<>();
  sample.meta.put("id", "123456");
  Year start = new Year(1999);
  Year end = new Year(2000);
  Year currentYear = new Year(1999);
  String code = "123456";
  tucsonSimple.saveData(sample, writer);

  String expected = "1234561999   123\n" +
                    "1234561999   321\n" +
                    "9990     ";
  assertEquals(expected, writer.toString());
 }

 @Test
 public void testComputeRange() {
  Filetype tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  Year start = new Year(1995);
  Year end = new Year(2000);
  Range result = tucsonSimple.computeRange(sample);
  Year expectedStart = new Year(1996);
  Year expectedEnd = new Year(2000);
  assertEquals(expectedStart, result.getStart());
  assertEquals(expectedEnd, result.getEnd());
 }

 @Test
 public void testSave() throws IOException {
  BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes()))) {
      BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
      @Override
      public void write(int c) throws IOException {
          outputWriter.write(c);
      }
      @Override
      public void newLine() throws IOException {
          outputWriter.newLine();
      }
      @Override
      public void close() throws IOException {
          outputWriter.close();
      }
  };
  Filetype tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.meta = new HashMap<>();
  sample.meta.put("id", "123456");
  tucsonSimple.save(sample, writer);

  String expected = "1234561999   123\n" +
                    "1234561999   321\n" +
                    "9990     ";
  String result = new String(new ByteArrayOutputStream(writer.size()).toByteArray());
  try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new ByteArrayOutputStream(result.getBytes()).toByteArray())))) {
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
          stringBuilder.append(line);
          stringBuilder.append("\n");
      }
      assertEquals(expected, stringBuilder.toString());
  }
 }

}