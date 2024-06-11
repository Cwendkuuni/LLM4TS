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

 private static final String VALID_DATA = "id: 1\nname: Test\ndata: 1 2 3 4 5 6 7 8 9 10\ncount: 1 2 3 4 5 6 7 8 9 10";

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
 public void testLoad() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(VALID_DATA.getBytes())));
  tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
  Sample sample = new Sample();
  sample.meta = new HashMap<String, String>();
  sample.meta.put("id", "123456");
  Filetype tucsonSimple = new TucsonSimple();
  String code = tucsonSimple.make6digitCode(sample);
  assertEquals("123456", code);

  sample.meta = new HashMap<String, String>();
  sample.meta.put("id", "abcdef");
  code = tucsonSimple.make6digitCode(sample);
  assertEquals("000001", code);

  sample.meta = new HashMap<String, String>();
  sample.meta.put("id", "1234567");
  code = tucsonSimple.make6digitCode(sample);
  assertEquals("123456", code);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  Sample sample = new Sample();
  String code = "123456";
  int yearWidth = 6;
  Year year = new Year(2000);

  tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
  assertEquals("1234562000", writer.toString());

  year = new Year(1999);
  tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
  assertEquals("1234561999", writer.toString());
 }

 @Test
 public void testSaveData() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.data = new int[11];
  sample.count = new int[11];

  BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(VALID_DATA.getBytes()))) {
   @Override
   public BufferedWriter wrap(OutputStream outputStream) {
    return new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
   }
  };
  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
  writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));

  tucsonSimple.saveData(sample, writer);
  String expectedData = "123456 2001\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2002\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2003\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2004\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2005\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2006\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2007\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2008\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2009\n1 2 3 4 5 6 7 8 9 10\n9990  0\n123456 2010\n1 2 3 4 5 6 7 8 9 10\n";
  assertEquals(expectedData, byteArrayOutputStream.toString());
 }

 @Test
 public void testComputeRange() {
  Sample sample = new Sample();
  sample.range = new YearRange(new Year(1), new Year(2));
  Filetype tucsonSimple = new TucsonSimple();
  Range range = tucsonSimple.computeRange(sample);
  assertEquals(sample.range, range);

  sample.range = new YearRange(new Year(8001), new Year(8002));
  range = tucsonSimple.computeRange(sample);
  assertEquals(sample.range.redateBy(8000), range);

  sample.range = new YearRange(new Year(1000), new Year(1001));
  range = tucsonSimple.computeRange(sample);
  assertEquals(new YearRange(new Year(1001), new Year(1002)), range);
 }

 @Test
 public void testSave() throws IOException {
  Filetype tucsonSimple = new TucsonSimple();
  Sample sample = new Sample();
  sample.data = new int[11];
  sample.count = new int[11];

  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
  tucsonSimple.save(sample, writer);
 }
}