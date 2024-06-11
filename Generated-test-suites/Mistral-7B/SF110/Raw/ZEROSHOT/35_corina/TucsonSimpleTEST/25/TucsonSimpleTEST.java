import org.junit.Test;
import org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

 private static final String TEST_DATA = "id:1 name:TestSample data:1,2,3,4,5 count:3,2,1,1,1";

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
 public void testLoadThrowsException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("Test data".getBytes())));
 tucsonSimple.load(reader);
 fail("Expected WrongFiletypeException to be thrown");
 }

 @Test
 public void testMake6digitCode() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 Sample sample2 = new Sample();
 String code2 = tucsonSimple.make6digitCode(sample2);
 assertEquals("000000", code2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year year = new Year(2000);
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveRowHeader(writer, sample, "abcdef", 6, year);
 String expected = "abcdef2000";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.data = new int[10];
 sample.count = new int[10];
 sample.data[0] = 1;
 sample.count[0] = 3;
 sample.range = new Range(new Year(1999), new Year(2001));
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveData(sample, writer);
 String expected = "000001  3\n" +
                  "000002  2\n" +
                  "000003  1\n" +
                  "000004  1\n" +
                  "000005  1\n" +
                  "9990  0\n";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testComputeRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(1998), new Year(2005));
 Year expected = new Year(1001);
 Year result = tucsonSimple.computeRange(sample);
 assertEquals(expected, result);
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.data = new int[10];
 sample.count = new int[10];
 sample.data[0] = 1;
 sample.count[0] = 3;
 sample.range = new Range(new Year(1999), new Year(2001));
 BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes()))){};
 BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.save(sample, writer);
 tucsonSimple.save(sample, outputWriter);
 String expected = "id:1\n" +
                 "name:TestSample\n" +
                 "data:\n" +
                 "1     3\n" +
                 "2     2\n" +
                 "3     1\n" +
                 "4     1\n" +
                 "5     1\n" +
                 "9990  0\n";
 String result = outputWriter.toString();
 assertEquals(expected, result);
 }
}