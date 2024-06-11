import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import corina.formats.I18n;
import corina.formats.StringUtils;
import corina.ui.Sample;
import corina.util.Column;
import corina.util.Year;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter writerMock;
 private ByteArrayOutputStream outputStream;
 private BufferedReader readerMock;
 private Sample sampleMock;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 writerMock = mock(BufferedWriter.class);
 outputStream = new ByteArrayOutputStream();
 writerMock.writeTo(outputStream);
 readerMock = mock(BufferedReader.class);
 sampleMock = mock(Sample.class);
 }

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
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
 public void testLoadWithWrongFiletypeException() throws IOException {
 when(readerMock.readLine()).thenReturn("not a valid file");
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() throws Exception {
 String id = "123456";
 Map<String, String> meta = new HashMap<>();
 meta.put("id", id);
 Sample sample = new Sample(meta, null, null, null);
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 assert(expected.equals(actual));

 String noId = null;
 Sample sampleNoId = new Sample(null, null, null, null);
 String expectedNoId = "000000";
 String actualNoId = tucsonSimple.make6digitCode(sampleNoId);
 assert(expectedNoId.equals(actualNoId));
 }

 @Test
 public void testSaveRowHeader() throws Exception {
 Year start = new Year(2022);
 String code = "123456";
 int yearWidth = 6;
 Year y = new Year(2021);
 String expected = "1234562022";
 tucsonSimple.saveRowHeader(writerMock, sampleMock, code, yearWidth, y);

 String prefix = "2021";
 assert(prefix.equals(expected.substring(0, 6)));
 assert(y.toString().equals(prefix.substring(2)));
 }

 @Test
 public void testSaveData() throws Exception {
 Column data = new Column(new ArrayList<>());
 Column count = new Column(new ArrayList<>());
 Year start = new Year(2022);
 Year end = new Year(2023);
 Map<String, Object> meta = new HashMap<>();
 meta.put("id", "123456");
 Sample sample = new Sample(meta, data, count, start, end);
 int yearWidth = 6;
 StringWriter stringWriter = new StringWriter();
 BufferedWriter writer = new BufferedWriter(stringWriter);
 tucsonSimple.saveData(sample, writer);
 String expected = "123456" + start.toString() + "\n" +
 "5\n" +
 "999\n" +
 "9990  0\n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n";
 String actual = stringWriter.toString();
 assert(expected.equals(actual));
 }

 @Test
 public void testComputeRange() throws Exception {
 Year start = new Year(2022);
 Sample sample = new Sample(null, null, null, start);
 Range expected = new Range(start, start.add(1));
 Range actual = tucsonSimple.computeRange(sample);
 assert(expected.equals(actual));

 Year start2 = new Year(1985);
 Sample sample2 = new Sample(null, null, null, start2);
 Range expected2 = new Range(start2.redateStartTo(new Year(1001)), start2.redateBy(8000));
 Range actual2 = tucsonSimple.computeRange(sample2);
 assert(expected2.equals(actual2));

 Year start3 = new Year(1950);
 Sample sample3 = new Sample(null, null, null, start3);
 Range expected3 = new Range(new Year(1001), start3.add(8001));
 Range actual3 = tucsonSimple.computeRange(sample3);
 assert(expected3.equals(actual3));
 }

 @Test
 public void testSave() throws Exception {
 Column data = new Column(new ArrayList<>());
 Column count = new Column(new ArrayList<>());
 Year start = new Year(2022);
 Year end = new Year(2023);
 Map<String, Object> meta = new HashMap<>();
 meta.put("id", "123456");
 Sample sample = new Sample(meta, data, count, start, end);
 StringWriter stringWriter = new StringWriter();
 BufferedWriter writer = new BufferedWriter(stringWriter);
 tucsonSimple.save(sample, writer);
 String expected = "123456" + start.toString() + "\n" +
 "5\n" +
 "999\n" +
 "9990  0\n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n" +
 "5\n" +
 "999\n" +
 "9990   \n";
 String actual = stringWriter.toString();
 assert(expected.equals(actual));
 }
}