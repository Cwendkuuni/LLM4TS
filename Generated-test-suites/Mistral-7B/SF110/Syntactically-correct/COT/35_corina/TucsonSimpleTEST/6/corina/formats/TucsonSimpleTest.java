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
import corina.formats.TucsonSimple.Year;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.YearCompare;

public class TucsonSimpleTest {

 private static final String TEST_DATA = "id:1,data:[1,2,3,4,5],count:[1,2,3,4,5],range:start:1,end:5";

 @Test
 public void testToString() {
  TucsonSimple tucsonSimple = new TucsonSimple();
  String actual = tucsonSimple.toString();
  String expected = I18n.getText("format.tucsonsimple");
  assertEquals(expected, actual);
 }

 @Test
 public void testgetDefaultExtension() {
  TucsonSimple tucsonSimple = new TucsonSimple();
  String actual = tucsonSimple.getDefaultExtension();
  String expected = ".RWL";
  assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
  TucsonSimple tucsonSimple = new TucsonSimple();
  BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("Invalid data".getBytes())));
  tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
  TucsonSimple tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.meta = new HashMap<>();
  sample.meta.put("id", "123456");
  String actual = tucsonSimple.make6digitCode(sample);
  String expected = "123456";
  assertEquals(expected, actual);

  Sample sample2 = new Sample();
  String actual2 = tucsonSimple.make6digitCode(sample2);
  String expected2 = "000000";
  assertEquals(expected2, actual2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
  TucsonSimple tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  String code = "123456";
  int yearWidth = 6;
  Year start = new Year(2000);
  tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
  String actual = writer.toString();
  String expected = "1234562000";
  assertEquals(expected, actual);

  Year end = new Year(1999);
  tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, end);
  String actual2 = writer.toString();
  String expected2 = "1234561999";
  assertEquals(expected2, actual2);
 }

 @Test
 public void testSaveData() throws IOException {
  TucsonSimple tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.meta = new HashMap<>();
  sample.meta.put("id", "123456");
  sample.data = new int[10];
  sample.data[0] = 1;
  sample.data[1] = 2;
  sample.data[2] = 3;
  sample.data[3] = 4;
  sample.data[4] = 5;
  sample.count = new int[10];
  sample.count[0] = 1;
  sample.count[1] = 2;
  sample.count[2] = 3;
  sample.count[3] = 4;
  sample.count[4] = 5;
  sample.range = new Range(new Year(1), new Year(5));
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  String code = "123456";
  Year start = new Year(1);
  Year end = new Year(5);
  tucsonSimple.saveData(sample, writer);
  String actual = writer.toString();
  String expected = "1234561 1\n1234561 2\n1234561 3\n1234561 4\n1234561 5\n"
          + "9990  0\n9990  0\n9990  0\n9990  0\n9990  0\n";
  assertEquals(expected, actual);
 }

 @Test
 public void testComputeRange() {
  TucsonSimple tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.range = new Range(new Year(1000), new Year(2000));
  Range actual = tucsonSimple.computeRange(sample);
  Range expected = sample.range;
  assertEquals(expected, actual);

  sample.range = new Range(new Year(-1), new Year(1));
  actual = tucsonSimple.computeRange(sample);
  expected = new Range(new Year(1001), new Year(1001));
  assertEquals(expected, actual);

  sample.range = new Range(new Year(8001), new Year(9000));
  actual = tucsonSimple.computeRange(sample);
  expected = new Range(new Year(8010), new Year(9000));
  assertEquals(expected, actual);
 }

 @Test
 public void testSave() throws IOException {
  TucsonSimple tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.meta = new HashMap<>();
  sample.meta.put("id", "123456");
  sample.data = new int[10];
  sample.data[0] = 1;
  sample.data[1] = 2;
  sample.data[2] = 3;
  sample.data[3] = 4;
  sample.data[4] = 5;
  sample.count = new int[10];
  sample.count[0] = 1;
  sample.count[1] = 2;
  sample.count[2] = 3;
  sample.count[3] = 4;
  sample.count[4] = 5;
  sample.range = new Range(new Year(1), new Year(5));
  BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
  BufferedWriter testWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  tucsonSimple.save(sample, testWriter);
  String actual = testWriter.toString();
  String expected = "id:1,data:[1,2,3,4,5],count:[1,2,3,4,5],range:start:1,end:5\n"
          + "1234561 1\n1234561 2\n1234561 3\n1234561 4\n1234561 5\n"
          + "9990  0\n9990  0\n9990  0\n9990  0\n9990  0\n";
  assertEquals(expected, actual);
 }

}