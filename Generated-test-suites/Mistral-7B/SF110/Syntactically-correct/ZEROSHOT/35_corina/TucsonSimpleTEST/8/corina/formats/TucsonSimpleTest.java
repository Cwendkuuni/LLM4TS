import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private Sample sample;
 private TucsonSimple tucsonSimple;
 private String expectedCode;
 private String expectedData;
 private String expectedYearWidth;
 private String expectedFileContent;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 sample = new Sample();
 expectedCode = "000001";
 expectedData = "123.5";
 expectedYearWidth = "6";
 expectedFileContent = expectedCode + " 1995\n" +
 "00045 123.5\n" +
 "00046 456.7\n" +
 "999 999.0\n";
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
 public void testLoad() throws IOException {
 tucsonSimple.load(new BufferedReader(new InputStreamReader(new ByteArrayInputStream("Test data".getBytes()))));
 fail("Should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 sample.meta.put("id", "1");
 assertEquals("000001", tucsonSimple.make6digitCode(sample));

 sample.meta.remove("id");
 assertEquals("000000", tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new FileWriter("temp.rwl")));
 writer.init(outputStream);
 tucsonSimple.saveRowHeader(writer, sample, expectedCode, expectedYearWidth, new Year(1995));
 writer.close();
 assertEquals(expectedCode + " 1995", outputStream.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new FileWriter("temp.rwl")));
 writer.init(outputStream);
 tucsonSimple.saveData(sample, writer);
 writer.close();

 assertEquals(expectedFileContent, new String(outputStream.toByteArray()));
 }

 @Test
 public void testComputeRange() {
 Year start = new Year(1994);
 Range range = new Range(start, new Year(1995));
 Year computedRangeStart = tucsonSimple.computeRange(sample).getStart();
 assertEquals(start, computedRangeStart);

 Year computedRangeEnd = tucsonSimple.computeRange(sample).getEnd();
 Year end = new Year(1995);
 assertEquals(end, computedRangeEnd);
 }

 @Test
 public void testSave() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new FileWriter(new FileWriter("temp.rwl")));
 writer.init(outputStream);
 Map<Integer, Double> data = new HashMap<>();
 data.put(1, new Double(123.5));
 data.put(2, new Double(456.7));
 sample.data = data;
 sample.range = new Range(new Year(1995), new Year(1996));
 tucsonSimple.save(sample, writer);
 writer.close();

 assertEquals(expectedFileContent, new String(outputStream.toByteArray()));
 }

 @After
 public void tearDown() {
 new File("temp.rwl").delete();
 }

}