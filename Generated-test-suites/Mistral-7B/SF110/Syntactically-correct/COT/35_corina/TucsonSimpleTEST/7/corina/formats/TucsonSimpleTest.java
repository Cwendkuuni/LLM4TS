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

 private static final String TEST_FILE_CONTENTS = "id:1,data:1,data:2,data:3,data:4,data:5,data:6,data:7,data:8,data:9,data:10,count:1,count:2,count:3,count:4,count:5,count:6,count:7,count:8,count:9,count:10\n";

 @Test
 public void testToString() {
     TucsonSimple tucsonSimple = new TucsonSimple();
     assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
     TucsonSimple tucsonSimple = new TucsonSimple();
     assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
     TucsonSimple tucsonSimple = new TucsonSimple();
     BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_FILE_CONTENTS.getBytes())));
     tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
     Sample s = new Sample();
     s.meta.put("id", "123456");
     TucsonSimple tucsonSimple = new TucsonSimple();
     String expected = "123456";
     String result = tucsonSimple.make6digitCode(s);
     assertEquals(expected, result);

     s = new Sample();
     s.meta.clear();
     expected = "000000";
     result = tucsonSimple.make6digitCode(s);
     assertEquals(expected, result);

     s.meta.put("id", "111111");
     expected = "111111";
     result = tucsonSimple.make6digitCode(s);
     assertEquals(expected, result);

     s.meta.put("id", "1111111111");
     expected = "111111";
     result = tucsonSimple.make6digitCode(s);
     assertEquals(expected, result);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
     TucsonSimple tucsonSimple = new TucsonSimple();
     Sample s = new Sample();
     String code = "123456";
     int yearWidth = 6;
     Year year = new Year(2001);
     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));

     tucsonSimple.saveRowHeader(writer, s, code, yearWidth, year);
     String expected = "1234562001";
     String result = writer.toString();
     assertEquals(expected, result);

     year = new Year(1999);
     expected = "1234561999";
     result = writer.toString();
     assertEquals(expected, result);
 }

 @Test
 public void testSaveData() throws IOException {
     TucsonSimple tucsonSimple = new TucsonSimple();
     Sample s = new Sample();
     s.data.put(new Year(2001), "1");
     s.data.put(new Year(2002), "2");
     s.data.put(new Year(2003), "3");
     s.count.put(new Year(2001), "1");
     s.count.put(new Year(2002), "2");
     s.count.put(new Year(2003), "3");
     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));

     tucsonSimple.saveData(s, writer);
     String expected = "123456 2001 1\n" +
                       "123456 2002 2\n" +
                       "123456 2003 3\n";
     String result = writer.toString();
     assertEquals(expected, result);
 }

 @Test
 public void testComputeRange() {
     TucsonSimple tucsonSimple = new TucsonSimple();
     Sample s = new Sample();
     s.range = new YearRange(new Year(1), new Year(10));

     YearRange expected = s.range;
     YearRange result = tucsonSimple.computeRange(s);
     assertEquals(expected, result);

     s.range.setStart(new Year(8001));
     expected = s.range.redateBy(8000);
     result = tucsonSimple.computeRange(s);
     assertEquals(expected, result);

     s.range.setStart(new Year(9999));
     expected = s.range.redateStartTo(new Year(1001));
     result = tucsonSimple.computeRange(s);
     assertEquals(expected, result);
 }

 @Test
 public void testSave() throws IOException {
     TucsonSimple tucsonSimple = new TucsonSimple();
     Sample s = new Sample();
     s.data.putAll(SampleData.SAMPLE_DATA);
     s.count.putAll(SampleData.SAMPLE_COUNT);
     BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_FILE_CONTENTS.getBytes())));
     BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));

     tucsonSimple.save(s, writer);
     String input = reader.readLine();
     String expected = input;
     String result = "";
     while ((input = reader.readLine()) != null) {
         result += input + "\n";
     }
     assertEquals(expected, result);

     tucsonSimple.save(s, outputWriter);
     expected = TEST_FILE_CONTENTS;
     result = outputWriter.toString();
     assertEquals(expected, result);
 }

}