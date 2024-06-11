import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

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
 BufferedReader reader = new BufferedReader(new FileReader(new File("testFile.rwl")));
 tucsonSimple.load(reader);
 fail("Load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 TucsonSimple tucsonSimple = new TucsonSimple();

 Sample sample = new Sample();
 Map<String, Object> meta = new HashMap<>();
 meta.put("id", "123456");
 sample.meta = meta;
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 Sample sample2 = new Sample();
 String code2 = tucsonSimple.make6digitCode(sample2);
 assertEquals("000000", code2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output.txt")));

 Sample sample = new Sample();
 Map<String, Object> meta = new HashMap<>();
 meta.put("id", "123456");
 sample.meta = meta;
 Year year = new Year(2000);

 tucsonSimple.saveRowHeader(writer, sample, "000000", 6, year);
 assertEquals("0000002000", writer.getBuffer().toString());

 tucsonSimple.saveRowHeader(writer, sample, "123456", 4, year);
 assertEquals("123456", writer.getBuffer().toString());
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output.txt")));

 Sample sample = new Sample();
 Map<String, Object> meta = new HashMap<>();
 meta.put("id", "123456");
 sample.meta = meta;
 Year start = new Year(2000);
 Year end = new Year(2001);
 Year currentYear = new Year(2000);
 String code = "123456";

 tucsonSimple.saveData(sample, writer);
 assertEquals("0000002000 123456", reader.readLine());
 assertEquals("9990    ", reader.readLine());

 reader.close();
 writer.close();

 sample.data.put(currentYear.diff(start), "10");
 sample.count.put(currentYear.diff(start), "5");
 tucsonSimple.saveData(sample, writer);

 assertEquals("0000002000 123456", reader.readLine());
 assertEquals("9990  5", reader.readLine());
 }

 @Test
 public void testComputeRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();

 Sample sample = new Sample();
 Year start = new Year(1999);
 Year end = new Year(2000);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(start, range.getStart());
 assertEquals(end, range.getEnd());
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output.txt")));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{"0'.\u0003TUCSONSIMPLE\u001A\u0003".getBytes("UTF-8")});
 BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));

 Sample sample = new Sample();
 Map<String, Object> meta = new HashMap<>();
 meta.put("id", "123456");
 sample.meta = meta;
 Year start = new Year(2000);
 Year end = new Year(2001);
 String code = "123456";

 tucsonSimple.save(sample, outputWriter);
 outputWriter.flush();
 String output = outputStream.toString();
 String expectedOutput = "0000002000 123456\n9990    \n";
 assertEquals(expectedOutput, output);

 tucsonSimple.save(sample, writer);
 String actualOutput = reader.readLine();
 assertEquals(expectedOutput, actualOutput);

 sample.data.put(start.diff(new Year(1000)), "10");
 sample.count.put(start.diff(new Year(1000)), "5");
 tucsonSimple.save(sample, writer);
 String actualOutput2 = reader.readLine();
 String expectedOutput2 = "0000002000 123456\n9990    \n9990  5\n";
 assertEquals(expectedOutput2, actualOutput2);

 reader.close();
 writer.close();
 inputReader.close();
 inputStream.close();
 outputStream.close();
 }

 @Test
 public void testWrongFiletypeException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new FileReader(new File("testFile.txt")));
 try {
 tucsonSimple.load(reader);
 fail("Load method should throw WrongFiletypeException");
 } catch (WrongFiletypeException e) {
 //Expected
 }
 }
}