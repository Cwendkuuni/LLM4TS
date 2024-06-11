import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader readerMock;
 private BufferedWriter writerMock;
 private I18n i18nMock;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 i18nMock = mock(I18n.class);
 readerMock = mock(BufferedReader.class);
 writerMock = mock(BufferedWriter.class);
 when(i18nMock.getText(Mockito.eq("format.tucsonsimple"))).thenReturn("TucsonSimple Format");
 when(readerMock.readLine()).thenReturn("Sample data"); // Replace with actual data from a file
 }

 @Test
 public void testToString() {
 String expected = "TucsonSimple Format";
 String actual = tucsonSimple.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() {
 Map<String, String> meta = new HashMap<>();
 meta.put("id", "123456");
 Sample sample = new Sample(meta, new String[]{"Sample data"});
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 assertEquals(expected, actual);

 meta = new HashMap<>();
 Sample sample2 = new Sample(meta, new String[]{"Sample data"});
 String actual2 = tucsonSimple.make6digitCode(sample2);
 String expected2 = "000000";
 assertEquals(expected2, actual2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Year year = new Year(2000);
 Sample sample = new Sample(new HashMap<>(), new String[]{"Sample data"});
 String code = "123456";
 int yearWidth = 6;
 when(writerMock.write(Mockito.anyString())).thenAnswer(invocation -> {
 return invocation.callRealMethod();
 });
 tucsonSimple.saveRowHeader(writerMock, sample, code, yearWidth, year);
 String expected = "1234562000";
 String actual = readFileContent(writerMock);
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveData() throws IOException {
 String yearWidth = "6";
 String isSummed = "false";
 String isProcessed = "false";
 Year start = new Year(2000);
 Year end = new Year(2002);
 Range range = new Range(start, end);
 Sample sample = new Sample(new HashMap<>(), new String[]{"Sample data"});
 sample.range = range;
 String code = "123456";
 when(writerMock.write(Mockito.anyString())).thenAnswer(invocation -> {
 return invocation.callRealMethod();
 });
 when(writerMock.newLine()).thenAnswer(invocation -> {
 return invocation.callRealMethod();
 });
 when(writerMock.write(Mockito.anyString(), Mockito.anyInt())).thenAnswer(invocation -> {
 return invocation.callRealMethod();
 });
 when(writerMock.write(Mockito.eq("   999"))).thenReturn(3);
 when(writerMock.write(Mockito.anyString(), Mockito.eq(4))).thenReturn(4);
 tucsonSimple.saveData(sample, writerMock);
 String expected = "1234562000   999\n" +
 "1234562001 Sample data   \n" +
 "1234562002 Sample data\n" +
 "1234562003   999\n";
 String actual = readFileContent(writerMock);
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRange() {
 Year expectedStart = new Year(1001);
 Year expectedEnd = new Year(1002);
 Sample sample = new Sample(new HashMap<>(), new String[]{"Sample data"});
 sample.range = new Range(new Year(999), new Year(999));
 Range actualRange = tucsonSimple.computeRange(sample);
 Year actualStart = actualRange.getStart();
 Year actualEnd = actualRange.getEnd();
 assertEquals(expectedStart, actualStart);
 assertEquals(expectedEnd, actualEnd);
 }

 @Test
 public void testSave() throws IOException {
 String expected = "1234561001 Sample data\n" +
 "1234561002 Sample data\n" +
 "1234561003 Sample data\n" +
 "1234561004 Sample data\n" +
 "1234561005 Sample data\n" +
 "1234561006 Sample data\n" +
 "1234561007 Sample data\n" +
 "1234561008 Sample data\n" +
 "1234561009 Sample data\n" +
 "1234561010   999\n";
 Sample sample = new Sample(new HashMap<>(), new String[]{"Sample data"});
 when(writerMock.write(Mockito.anyString())).thenAnswer(invocation -> {
 return invocation.callRealMethod();
 });
 when(writerMock.newLine()).thenAnswer(invocation -> {
 return invocation.callRealMethod();
 });
 when(writerMock.write(Mockito.anyString(), Mockito.anyInt())).thenAnswer(invocation -> {
 return invocation.callRealMethod();
 });
 when(writerMock.write(Mockito.eq("   999"))).thenReturn(3);
 when(writerMock.write(Mockito.anyString(), Mockito.eq(4))).thenReturn(4);
 tucsonSimple.save(sample, writerMock);
 String actual = readFileContent(writerMock);
 assertEquals(expected, actual);
 }

 private String readFileContent(BufferedWriter writerMock) throws IOException {
 StringReader stringReader = new StringReader(writerMock.toString());
 StringWriter stringWriter = new StringWriter();
 String s = null;
 while ((s = stringReader.readLine()) != null) {
 stringWriter.append(s);
 stringWriter.append("\n");
 }
 return stringWriter.toString();
 }

}