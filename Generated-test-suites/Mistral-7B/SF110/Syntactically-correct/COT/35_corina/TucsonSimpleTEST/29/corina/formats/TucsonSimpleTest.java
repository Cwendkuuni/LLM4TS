package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.formats.Filetype;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private static final String TEST_DATA = "id:1\nname:Test Data\nrange:1995-2000\ndata:1 2 3 4 5\ncount:1 2 3 4 5\n";

 @Test
 public void testToString() {
  Filetype tucsonSimple = new TucsonSimple();
  assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
  Filetype tucsonSimple = new TucsonSimple();
  assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadException() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
  tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
  Sample sample = new Sample();
  sample.meta.put("id", "123456");
  Filetype tucsonSimple = new TucsonSimple();
  assertEquals("123456", tucsonSimple.make6digitCode(sample));
  sample.meta.put("id", null);
  assertEquals("000000", tucsonSimple.make6digitCode(sample));
  sample.meta.put("id", "1");
  assertEquals("000001", tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
  Sample sample = new Sample();
  Filetype tucsonSimple = new TucsonSimple();
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Year year = new Year(1995);
  tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
  assertEquals("1234561995", writer.toString());
  year = new Year(1996);
  tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
  assertEquals("1234561996", writer.toString());
  year = new Year(1995);
  year = year.add(-1);
  tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
  assertEquals("1994123456", writer.toString());
 }

 @Test
 public void testSaveData() throws IOException {
  Sample sample = new Sample();
  sample.meta.put("id", "123456");
  sample.range = new Range(new Year(1995), new Year(2000));
  sample.data = new double[]{1, 2, 3, 4, 5};
  sample.count = new int[]{1, 2, 3, 4, 5};
  Filetype tucsonSimple = new TucsonSimple();
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  tucsonSimple.saveData(sample, writer);
  String expected = "1234561995   1\n" +
                    "1234561996   2\n" +
                    "1234561997   3\n" +
                    "1234561998   4\n" +
                    "1234561999   5\n" +
                    "9990   ";
  assertEquals(expected, writer.toString());
 }

 @Test
 public void testComputeRange() {
  Sample sample = new Sample();
  sample.range = new Range(new Year(1995), new Year(2000));
  Filetype tucsonSimple = new TucsonSimple();
  Range range = tucsonSimple.computeRange(sample);
  assertEquals(sample.range, range);
  sample.range = new Range(new Year(-500), new Year(1995));
  range = tucsonSimple.computeRange(sample);
  assertEquals(sample.range.redateStartTo(new Year(1001)), range);
  sample.range = new Range(new Year(1002), new Year(-500));
  range = tucsonSimple.computeRange(sample);
  assertEquals(sample.range.redateBy(8000), range);
 }

 @Test
 public void testSave() throws IOException {
  Sample sample = new Sample();
  sample.meta.put("id", "123456");
  sample.range = new Range(new Year(1995), new Year(2000));
  sample.data = new double[]{1, 2, 3, 4, 5};
  sample.count = new int[]{1, 2, 3, 4, 5};
  Filetype tucsonSimple = new TucsonSimple();
  BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
  tucsonSimple.save(sample, writer);
  String inputData = writer.toString();
  String expectedOutput = "1234561995   1\n" +
                         "1234561996   2\n" +
                         "1234561997   3\n" +
                         "1234561998   4\n" +
                         "1234561999   5\n" +
                         "9990   ";
  tucsonSimple.save(sample, outputWriter);
  assertEquals(expectedOutput, outputStream.toString());
 }

}