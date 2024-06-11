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

 private static final String VALID_SAMPLE = "sample data here";

 @Test
 public void testToString() {
  TucsonSimple tucson = new TucsonSimple();
  assertEquals("format.tucsonsimple", tucson.toString());
 }

 @Test
 public void testgetDefaultExtension() {
  TucsonSimple tucson = new TucsonSimple();
  assertEquals(".RWL", tucson.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
  TucsonSimple tucson = new TucsonSimple();
  BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("invalid data".getBytes())));
  tucson.load(reader);
 }

 @Test
 public void testMake6digitCode() {
  Sample sample = new Sample();
  sample.meta = new HashMap<>();
  sample.meta.put("id", "123456");
  TucsonSimple tucson = new TucsonSimple();
  assertEquals("123456", tucson.make6digitCode(sample));

  sample.meta.clear();
  assertEquals("000000", tucson.make6digitCode(sample));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  TucsonSimple tucson = new TucsonSimple();
  Year year = new Year(1995);
  tucson.saveRowHeader(writer, sample, "123456", 4, year);
  assertEquals("1234561995", writer.toString());
 }

 @Test
 public void testSaveDataHappyPath() throws IOException {
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  TucsonSimple tucson = new TucsonSimple();

  // Test empty range
  tucson.saveData(sample, writer);
  assertEquals("", writer.toString());

  // Test valid range with valid data
  Year start = new Year(1995);
  Year end = new Year(1997);
  sample.range = new Range(start, end);
  sample.data = new int[end.diff(start) + 1];
  for (int i = 0; i < sample.data.length; i++) {
   sample.data[i] = i + 1;
  }
  tucson.saveData(sample, writer);
  String expected = "000006 1995\n" +
                    "000001\n" +
                    "000002\n" +
                    "000003\n" +
                    "000004\n" +
                    "000005\n" +
                    "999\n" +
                    "9990  0\n";
  assertEquals(expected, writer.toString());
 }

 @Test
 public void testSaveDataInvalidRangeStart() throws IOException {
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  TucsonSimple tucson = new TucsonSimple();

  // Test negative year
  sample.range = new Range(new Year(-1), new Year(1997));
  tucson.saveData(sample, writer);
  String expected = "000000 1997\n" +
                    "999\n" +
                    "9990  0\n";
  assertEquals(expected, writer.toString());

  // Test year before minimum valid year
  sample.range.setStart(new Year(999));
  tucson.saveData(sample, writer);
  expected = "000000 1001\n" +
             "999\n" +
             "9990  0\n";
  assertEquals(expected, writer.toString());
 }

 @Test
 public void testSaveDataInvalidRangeEnd() throws IOException {
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  TucsonSimple tucson = new TucsonSimple();

  // Test year before start year
  sample.range = new Range(new Year(1995), new Year(999));
  tucson.saveData(sample, writer);
  String expected = "000000 1995\n" +
                    "999\n" +
                    "9990  0\n";
  assertEquals(expected, writer.toString());

  // Test year after maximum valid year
  Year end = new Year(99999);
  sample.range.setEnd(end);
  tucson.saveData(sample, writer);
  expected = "000000 1001\n" +
             "999\n" +
             "9990  0\n";
  assertEquals(expected, writer.toString());
 }

 @Test
 public void testSaveDataProcessed() throws IOException {
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  TucsonSimple tucson = new TucsonSimple();

  // Test no processed flag set
  sample.range = new Range(new Year(1995), new Year(1997));
  tucson.saveData(sample, writer, true, true);
  String expected = "000006 1995\n" +
                    "000001\n" +
                    "000002\n" +
                    "000003\n" +
                    "000004\n" +
                    "000005\n" +
                    "999\n" +
                    "9990  0\n";
  assertEquals(expected, writer.toString());

  // Test processed flag set
  sample.processed = true;
  tucson.saveData(sample, writer, true, true);
  expected = "000000 1001\n" +
             "999\n" +
             "9990  0\n";
  assertEquals(expected, writer.toString());
 }

 @Test
 public void testComputeRange() throws IOException {
  Sample sample = new Sample();
  TucsonSimple tucson = new TucsonSimple();

  // Test valid start year
  Year start = new Year(1995);
  Range range = tucson.computeRange(sample);
  assertEquals(start, range.getStart());

  // Test start year before minimum valid year
  start = new Year(999);
  range = tucson.computeRange(sample);
  assertEquals(new Year(1001), range.getStart());

  // Test start year after maximum valid year
  start = new Year(10001);
  range = tucson.computeRange(sample);
  assertEquals(new Year(10001), range.getStart());

  // Test start year >= maximum valid year
  Year maxValidYear = new Year(99999);
  sample.range.setStart(maxValidYear.add(1));
  range = tucson.computeRange(sample);
  assertEquals(maxValidYear, range.getStart());
 }

 @Test
 public void testSave() throws IOException {
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  TucsonSimple tucson = new TucsonSimple();

  // Test null sample
  tucson.save(null, writer);
  assertEquals("", writer.toString());

  // Test empty range
  sample.range = new Range();
  tucson.save(sample, writer);
  assertEquals("", writer.toString());

  // Test valid save
  Year start = new Year(1995);
  Year end = new Year(1997);
  sample.range = new Range(start, end);
  tucson.save(sample, writer);
  String expected = "000006 1995\n" +
                    "000001\n" +
                    "000002\n" +
                    "000003\n" +
                    "000004\n" +
                    "000005\n" +
                    "999\n" +
                    "9990  0\n";
  assertEquals(expected, writer.toString());
 }
}