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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

 private static final String I18N_TEXT = "format.tucsonsimple";
 private static final String DEFAULT_EXTENSION = ".RWL";

 @Test
 public void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals(DEFAULT_Extensions, tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad_WrongFiletypeException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new FileReader(new File("non_existent_file.txt")));
 tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode_SampleWithID() {
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 TucsonSimple tucsonSimple = new TucsonSimple();
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 public void testMake6digitCode_SampleWithoutID() {
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("000000", code);
 }

 @Test
 public void testMake6digitCode_IDWithLengthMoreThanSix() {
 Sample sample = new Sample();
 sample.meta.put("id", "1234567890");
 TucsonSimple tucsonSimple = new TucsonSimple();
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 public void testSaveRowHeader_StartYearLessThanCodeYear() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test_saveRowHeader.rwl")));
 tucsonSimple.saveRowHeader(writer, sample, "000001", 6, new Year(1));
 writer.close();
 File file = new File("test_saveRowHeader.rwl");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 String expected = "00000101";
 assertEquals(expected, line);
 }

 @Test
 public void testSaveRowHeader_StartYearGreaterThanCodeYear() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test_saveRowHeader.rwl")));
 tucsonSimple.saveRowHeader(writer, sample, "000001", 6, new Year(2));
 writer.close();
 File file = new File("test_saveRowHeader.rwl");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 String expected = "00000102";
 assertEquals(expected, line);
 }

 @Test
 public void testSaveData_IsSummedFalse_IsProcessedFalse() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test_saveData.rwl")));
 tucsonSimple.saveData(sample, writer);
 writer.close();
 File file = new File("test_saveData.rwl");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 String firstExpected = "   999";
 String secondExpected = "123456   ";
 assertEquals(firstExpected, line);
 line = reader.readLine();
 assertEquals(secondExpected, line);
 reader.close();
 file.delete();
 }

 @Test
 public void testSaveData_IsSummedFalse_IsProcessedTrue() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.processed = true;
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test_saveData.rwl")));
 tucsonSimple.saveData(sample, writer);
 writer.close();
 File file = new File("test_saveData.rwl");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 String firstExpected = "9990   ";
 String secondExpected = "123456   ";
 assertEquals(firstExpected, line);
 line = reader.readLine();
 assertEquals(secondExpected, line);
 reader.close();
 file.delete();
 }

 @Test
 public void testSaveData_IsSummedTrue() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.data = new HashMap<>();
 sample.data.put(new Year(1), "1");
 sample.data.put(new Year(2), "2");
 sample.count = new HashMap<>();
 sample.count.put(new Year(1), "1");
 sample.count.put(new Year(2), "2");
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test_saveData.rwl")));
 tucsonSimple.saveData(sample, writer);
 writer.close();
 File file = new File("test_saveData.rwl");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 String firstExpected = "   999";
 String secondExpected = "1  1";
 String thirdExpected = "2  2";
 assertEquals(firstExpected, line);
 line = reader.readLine();
 assertEquals(secondExpected, line);
 line = reader.readLine();
 assertEquals(thirdExpected, line);
 reader.close();
 file.delete();
 }

 @Test
 public void testComputeRange_SampleWithStartYearLessThanYear1() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(-1), new Year(1));
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(new Range(new Year(1001), new Year(1001)), range);
 }

 @Test
 public void testComputeRange_SampleWithStartYearGreaterThanYear8000() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(8001), new Year(9999));
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(new Range(new Year(8001), new Year(8001)).redateBy(8000), range);
 }

 @Test
 public void testSave_SampleWithStartYearLessThanYear1() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(-1), new Year(1));
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test_save.rwl")));
 tucsonSimple.save(sample, writer);
 writer.close();
 File file = new File("test_save.rwl");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 String expected = "format.tucsonsimple";
 assertEquals(expected, line);
 reader.close();
 file.delete();
 }

 @Test
 public void testSave_SampleWithStartYearGreaterThanYear8000() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(8001), new Year(9999));
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test_save.rwl")));
 tucsonSimple.save(sample, writer);
 writer.close();
 File file = new File("test_save.rwl");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 String expected = "format.tucsonsimple";
 assertEquals(expected, line);
 reader.close();
 file.delete();
 }

 @Test
 public void testSave_SampleWithStartYearEqualToYear1() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(1), new Year(1));
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test_save.rwl")));
 tucsonSimple.save(sample, writer);
 writer.close();
 File file = new File("test_save.rwl");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 String expected = "format.tucsonsimple";
 assertEquals(expected, line);
 reader.close();
 file.delete();
 }
}