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

 private final String I18N_TEXT = "format.tucsonsimple";
 private final String DEFAULT_EXTENSION = ".RWL";

 @Test
 public void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 String result = tucsonSimple.toString();
 String expected = I18n.getText(I18N_TEXT);
 assertEquals(expected, result);
 }

 @Test
 public void testgetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 String result = tucsonSimple.getDefaultExtension();
 String expected = DEFAULT_EXTENSION;
 assertEquals(expected, result);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("test data".getBytes())));
 tucsonSimple.load(reader);
 fail("Load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 String result = tucsonSimple.make6digitCode(sample);
 String expected = "123456";
 assertEquals(expected, result);

 Sample sampleNoId = new Sample();
 String resultNoId = tucsonSimple.make6digitCode(sampleNoId);
 String expectedNoId = "000000";
 assertEquals(expectedNoId, resultNoId);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 String code = "123456";
 int yearWidth = 6;
 Year year = new Year(2022);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
 String expected = "1234562022";
 String result = writer.toString();
 assertEquals(expected, result);

 Year start = new Year(2021);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 String expectedStart = "1234562021";
 String resultStart = writer.toString();
 assertEquals(expectedStart, resultStart);
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 String code = "123456";
 Year start = new Year(2021);
 Year end = new Year(2023);
 int yearWidth = 6;
 tucsonSimple.saveData(sample, writer);

 String expected = "1234562021   " + StringUtils.leftPad("1", 6) + StringUtils.leftPad("1", 4) + "\n" +
                  "1234562022   " + StringUtils.leftPad("2", 6) + StringUtils.leftPad("2", 4) + "\n" +
                  "1234562023   " + StringUtils.leftPad("3", 6) + StringUtils.leftPad("3", 4) + "\n";
 String result = writer.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testComputeRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(1980);
 Year end = new Year(1982);
 Range result = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(1981);
 Year expectedEnd = new Year(1983);
 assertEquals(expectedStart, result.getStart());
 assertEquals(expectedEnd, result.getEnd());
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("test data".getBytes())));
 BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.save(sample, writer);
 String inputData = writer.toString();
 tucsonSimple.save(sample, outputWriter);
 String expectedOutput = "1234562021  1  \n" +
                        "1234562022  2  \n" +
                        "1234562023  3  \n";
 String resultOutput = outputWriter.toString();
 assertEquals(expectedOutput, resultOutput);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testSaveWithWrongFiletype() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.save(new Filetype() {
    @Override
    public String toString() {
        return "wrongFiletype";
    }

    @Override
    public String getDefaultExtension() {
        return "";
    }
 }, sample, writer);
 fail("Save method should throw WrongFiletypeException");
 }
}