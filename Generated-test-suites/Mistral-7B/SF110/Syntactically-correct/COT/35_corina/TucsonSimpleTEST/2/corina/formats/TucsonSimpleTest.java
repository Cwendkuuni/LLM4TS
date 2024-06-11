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

public class TucsonSimpleTest {

 private static final String TEST_DATA = "id:1\n" +
         "data:[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n" +
         "count:[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n" +
         "range:{'start': 1995, 'end': 2005}";

 @Test
 public void testToString() {
  Filetype tucsonSimple = new TucsonSimple();
  String expected = "format.tucsonsimple";
  String actual = tucsonSimple.toString();
  assertEquals(expected, actual);
 }

 @Test
 public void testgetDefaultExtension() {
  Filetype tucsonSimple = new TucsonSimple();
  String expected = ".RWL";
  String actual = tucsonSimple.getDefaultExtension();
  assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
  Sample sample = tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
  Filetype tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.meta = new HashMap<>();
  sample.meta.put("id", "123456");
  String expected = "123456";
  String actual = tucsonSimple.make6digitCode(sample);
  assertEquals(expected, actual);

  Sample sample2 = new Sample();
  String actual2 = tucsonSimple.make6digitCode(sample2);
  assertEquals("000000", actual2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  String code = "123456";
  int yearWidth = 6;
  Year year = new Year(1995);

  tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
  String expected = "1234561995";
  String actual = writer.toString();
  assertEquals(expected, actual);

  Year year2 = new Year(1994);
  tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year2);
  String expected2 = "1234561994";
  String actual2 = writer.toString();
  assertEquals(expected2, actual2);
}

 @Test
 public void testSaveData() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  sample.data = new int[11];
  sample.data[0] = 1;
  sample.count = new int[11];
  sample.count[0] = 1;
  sample.meta = new HashMap<>();
  sample.meta.put("id", 1);
  sample.range = new Range(new Year(1995), new Year(2005));

  tucsonSimple.saveData(sample, writer);

  String expected = "1 1\n" +
          "2 2\n" +
          "3 3\n" +
          "4 4\n" +
          "5 5\n" +
          "6 6\n" +
          "7 7\n" +
          "8 8\n" +
          "9 9\n" +
          "10 10\n" +
          "   999\n";
  String actual = writer.toString();
  assertEquals(expected, actual);
 }

 @Test
 public void testComputeRange() {
  Filetype tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.range = new Range(new Year(1), new Year(1000));
  Year expected = new Year(1001);
  Year actual = tucsonSimple.computeRange(sample).getStart();
  assertEquals(expected, actual);
 }

 @Test
 public void testSave() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
  BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  sample.data = new int[11];
  sample.data[0] = 1;
  sample.count = new int[11];
  sample.count[0] = 1;
  sample.meta = new HashMap<>();
  sample.meta.put("id", 1);
  sample.range = new Range(new Year(1995), new Year(2005));

  tucsonSimple.save(sample, writer);
  String inputData = writer.toString();
  tucsonSimple.save(sample, outputWriter);
  String expected = "1 1\n" +
          "2 2\n" +
          "3 3\n" +
          "4 4\n" +
          "5 5\n" +
          "6 6\n" +
          "7 7\n" +
          "8 8\n" +
          "9 9\n" +
          "10 10\n" +
          "   999\n";
  String actual = outputWriter.toString();
  assertEquals(expected, actual);
 }

}