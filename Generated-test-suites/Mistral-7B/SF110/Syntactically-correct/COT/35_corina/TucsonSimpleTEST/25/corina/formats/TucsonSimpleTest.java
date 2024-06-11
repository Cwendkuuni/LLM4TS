package corina.formats.test;

import static org.junit.Assert.*;

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

import org.junit.Before;
import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.formats.Filetype;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private static final String TEST_DATA = "testData.rwl";
 private TucsonSimple tucsonSimple;
 private Filetype filetype;
 private Sample sample;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 filetype = new Filetype() {
 @Override
 public String toString() {
 return "TucsonSimple";
 }
 };
 sample = new Sample() {
 @Override
 public Map<String, Object> meta() {
 Map<String, Object> meta = new HashMap<>();
 meta.put("id", "123456");
 return meta;
 }
 @Override
 public Range range() {
 Year start = new Year(1990);
 Year end = new Year(1992);
 return new Range(start, end);
 }
 @Override
 public String[] data() {
 String[] data = new String[] { "10", "20", "30" };
 return data;
 }
 @Override
 public int[] count() {
 int[] count = new int[] { 1, 2, 3 };
 return count;
 }
 };
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
 public void testLoadException() throws IOException {
 BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(TEST_DATA))));
 tucsonSimple.load(reader);
 fail("Expected WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 code = tucsonSimple.make6digitCode(new Sample() {
 @Override
 public Map<String, Object> meta() {
 return null;
 }
 });
 assertEquals("000000", code);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter(new File("temp.rwl")));
 tucsonSimple.saveRowHeader(writer, sample, "123456", 4, new Year(1990));
 String expected = "1234561990";
 String result = readFile("temp.rwl", 0);
 assertEquals(expected, result);
 }

 @Test
 public void testSaveData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.saveData(sample, writer);
 String expected = "10 1990\n" +
 "20 1991\n" +
 "30 1992\n" +
 "   999\n";
 String result = new String(outputStream.toByteArray());
 assertEquals(expected, result);
 }

 @Test
 public void testComputeRange() {
 Year start = new Year(1995);
 Range expected = new Range(start, start.add(2));
 Range result = tucsonSimple.computeRange(sample);
 assertEquals(expected, result);
 }

 @Test
 public void testSave() throws IOException {
 File file = new File(TEST_DATA);
 file.createNewFile();
 BufferedWriter writer = new BufferedWriter(new FileWriter(file));
 tucsonSimple.save(sample, writer);
 writer.close();
 String expected = "10 1990\n" +
 "20 1991\n" +
 "30 1992\n" +
 "   999\n";
 String result = readFile(TEST_DATA, 0);
 assertEquals(expected, result);
 }

 private String readFile(String filePath, int startIndex) throws IOException {
 File file = new File(filePath);
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 int index = 0;
 while (line != null && index < startIndex) {
 line = reader.readLine();
 index++;
 }
 if (line == null) {
 fail("File does not contain expected data");
 }
 return line;
 }

}