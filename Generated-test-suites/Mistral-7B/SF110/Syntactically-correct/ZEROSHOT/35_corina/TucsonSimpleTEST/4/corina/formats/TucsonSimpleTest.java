import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter writerMock;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 writerMock = mock(BufferedWriter.class);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() {
 writerMock = null;
 outputStream = null;
 }

 @Test
 public void testToString() {
 String expected = I18n.getText("format.tucsonsimple");
 String actual = tucsonSimple.toString();
 assert(expected.equals(actual));
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 assert(expected.equals(actual));
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadException() throws IOException {
 BufferedReader readerMock = mock(BufferedReader.class);
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() throws Exception {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 assert(expected.equals(actual));

 Sample sample2 = new Sample();
 String actual2 = tucsonSimple.make6digitCode(sample2);
 assert(actual2.equals("000000"));
 }

 @Test
 public void testSaveRowHeader() throws Exception {
 Sample sample = new Sample();
 BufferedWriter writer = Mockito.spy(writerMock);
 Year year = new Year(2005);
 tucsonSimple.saveRowHeader(writer, sample, "000123", 4, year);
 String expected = "0001232005";
 String actual = outputStream.toString();
 outputStream.reset();
 writer.write(code);
 writer.flush();
 verify(writer).write(expected);
 }

 @Test
 public void testSaveData() throws Exception {
 Sample sample = new Sample();
 BufferedWriter writer = Mockito.spy(writerMock);
 Year start = new Year(2001);
 Year end = new Year(2003);
 String code = "000123";
 Year y = start;
 String data = "1";
 String summedData = "10";
 String expectedOutput = "0001232001  1\n" +
                         "0001232002  1\n" +
                         "0001232003  1\n" +
                         "0001232004   ";
 String actualOutput = "";
 tucsonSimple.saveData(sample, writer);
 outputStream.reset();
 writer.write(outputStream.toString());
 writer.flush();
 verify(writer).write(expectedOutput);

 Sample sampleSummed = new Sample();
 sampleSummed.data = new int[1000];
 sampleSummed.count = new int[1000];
 sampleSummed.data[0] = 1;
 sampleSummed.count[0] = 10;
 year = new Year(2001);
 tucsonSimple.saveData(sampleSummed, writer);
 expectedOutput = "0001232001  10\n";
 actualOutput = outputStream.toString();
 outputStream.reset();
 writer.write(outputStream.toString());
 writer.flush();
 verify(writer).write(expectedOutput);
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 Year expected = new Year(1001);
 Year actual = tucsonSimple.computeRange(sample).getStart();
 assert(expected.equals(actual));

 Sample sample2 = new Sample();
 Year start = new Year(2005);
 Year end = new Year(2007);
 sample2.range = new Range(start, end);
 Year expected2 = new Year(2005);
 Year actual2 = tucsonSimple.computeRange(sample2).getStart();
 assert(expected2.equals(actual2));
 }

 @Test
 public void testSave() throws Exception {
 Sample sample = new Sample();
 BufferedWriter writer = Mockito.spy(writerMock);
 tucsonSimple.save(sample, writer);
 String expectedOutput = "";
 String actualOutput = outputStream.toString();
 assert(expectedOutput.equals(actualOutput));
 }
}