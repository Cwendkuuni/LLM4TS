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

public class TucsonSimpleTest {

 private static final String TEST_DATA = "id:1 name:Test Sample range:1995-2000";

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
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
 tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
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
 public void testMake6digitCodeLongID() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "1234567890");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123");
 Year year = new Year(1995);
 tucsonSimple.saveRowHeader(writer, sample, "123", 6, year);
 String expected = "1231995";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderNegativeYear() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 Year year = new Year(1994);
 tucsonSimple.saveRowHeader(writer, sample, "123", 6, year);
 String expected = "1995123";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123");
 sample.range = new Range(new Year(1995), new Year(2000));
 sample.data = new int[]{-1, 1, 2, 3, 4, 5, 6, 7, 8, 9};
 sample.count = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
 tucsonSimple.saveData(sample, writer);
 String expected = "1231995   1\n" +
 "1231996   1\n" +
 "1231997   1\n" +
 "1231998   1\n" +
 "1231999   1\n" +
 "1232000   1\n" +
 "   999\n" +
 "9990   0\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataEmptyData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123");
 sample.range = new Range(new Year(1995), new Year(2000));
 sample.data = new int[0];
 tucsonSimple.saveData(sample, writer);
 String expected = "1231995   999\n" +
 "1231996   9990  0\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataNullData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123");
 sample.range = new Range(new Year(1995), new Year(2000));
 tucsonSimple.saveData(sample, writer);
 String expected = "1231995   999\n" +
 "1231996   9990  0\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRange() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(1994), new Year(1995));
 Range range = tucsonSimple.computeRange(sample);
 Year start = range.getStart();
 Year end = range.getEnd();
 Year expectedStart = new Year(1001);
 Year expectedEnd = new Year(1006);
 assertEquals(expectedStart, start);
 assertEquals(expectedEnd, end);

 sample.range = new Range(new Year(1995), new Year(1995));
 range = tucsonSimple.computeRange(sample);
 start = range.getStart();
 end = range.getEnd();
 expectedStart = new Year(1995);
 expectedEnd = new Year(1995);
 assertEquals(expectedStart, start);
 assertEquals(expectedEnd, end);

 sample.range = new Range(new Year(2010), new Year(2010));
 range = tucsonSimple.computeRange(sample);
 start = range.getStart();
 end = range.getEnd();
 expectedStart = new Year(2010);
 expectedEnd = new Year(2010);
 assertEquals(expectedStart, start);
 assertEquals(expectedEnd, end);

 sample.range = new Range(new Year(1994), new Year(1994));
 range = tucsonSimple.computeRange(sample);
 start = range.getStart();
 end = range.getEnd();
 expectedStart = new Year(1995);
 expectedEnd = new Year(1995);
 assertEquals(expectedStart, start);
 assertEquals(expectedEnd, end);
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes()))) instanceof BufferedReader ? (BufferedWriter) new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream())) : (BufferedWriter) new BufferedWriter(new OutputStreamWriter(new FileOutputStream("temp.rwl"), "UTF-8"));
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123");
 sample.range = new Range(new Year(1995), new Year(2000));
 sample.data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
 sample.count = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
 tucsonSimple.save(sample, writer);
 String expected = "1231995   1\n" +
 "1231996   1\n" +
 "1231997   1\n" +
 "1231998   1\n" +
 "1231999   1\n" +
 "1232000   1\n" +
 "   999\n" +
 "9990   0\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 writer.close();
 }

 @Test(expected = FileNotFoundException.class)
 public void testSaveFileNotFound() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/nonexistentfile.rwl"), "UTF-8"));
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123");
 sample.range = new Range(new Year(1995), new Year(2000));
 sample.data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
 sample.count = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
 tucsonSimple.save(sample, writer);
 }

 @Test
 public void testSaveNullFile() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = null;
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123");
 sample.range = new Range(new Year(1995), new Year(2000));
 sample.data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
 sample.count = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
 try {
 tucsonSimple.save(sample, writer);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }
}