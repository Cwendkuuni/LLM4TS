import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import corina.formats.TucsonSimple;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader reader;
 private BufferedWriter writer;
 private Sample sample;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 reader = new BufferedReader(new StringReader(""));
 writer = new BufferedWriter(new StringWriter());
 sample = new Sample();
 }

 @After
 public void tearDown() throws IOException {
 reader.close();
 writer.close();
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
 public void testLoadThrowsException() throws IOException {
 tucsonSimple.load(reader);
 fail("load method should have thrown WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Sample sampleWithId = new Sample();
 sampleWithId.meta.put("id", "123456");
 String codeWithId = tucsonSimple.make6digitCode(sampleWithId);
 assertEquals("123456", codeWithId);

 Sample sampleWithoutId = new Sample();
 String codeWithoutId = tucsonSimple.make6digitCode(sampleWithoutId);
 assertEquals("000000", codeWithoutId);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(2000);
 Year end = new Year(2002);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 String expected = code + "2000";
 assertEquals(expected, writer.toString());

 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, end);
 String expected2 = code + "2002";
 assertEquals(expected2, writer.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 String data1 = "10";
 String data2 = "20";
 String data3 = "30";
 sample.data.put(new Year(2000), data1);
 sample.data.put(new Year(2001), data2);
 sample.data.put(new Year(2002), data3);

 String count1 = "1";
 String count2 = "2";
 sample.count.put(new Year(2000), count1);
 sample.count.put(new Year(2001), count2);

 tucsonSimple.saveData(sample, writer);
 String expected = "123456 2000   10\n123456 2001   20\n123456 2002   30\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRange() {
 Year start = new Year(1998);
 Range expected = new Range(start, new Year(1998));
 Range computed = tucsonSimple.computeRange(sample);
 assertEquals(expected, computed);

 Year start2 = new Year(1995);
 Range expected2 = new Range(start2, start2.add(8000));
 Range computed2 = tucsonSimple.computeRange(sample);
 assertEquals(expected2, computed2);

 Year start3 = new Year(1990);
 Range expected3 = new Range(start3, new Year(1001));
 Range computed3 = tucsonSimple.computeRange(sample);
 assertEquals(expected3, computed3);
 }

 @Test
 public void testSave() throws IOException {
 String data1 = "10";
 String data2 = "20";
 String data3 = "30";
 sample.data.put(new Year(2000), data1);
 sample.data.put(new Year(2001), data2);
 sample.data.put(new Year(2002), data3);

 String count1 = "1";
 String count2 = "2";
 sample.count.put(new Year(2000), count1);
 sample.count.put(new Year(2001), count2);

 tucsonSimple.save(sample, writer);
 String expected = "123456 2000   10\n123456 2001   20\n123456 2002   30\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }
}