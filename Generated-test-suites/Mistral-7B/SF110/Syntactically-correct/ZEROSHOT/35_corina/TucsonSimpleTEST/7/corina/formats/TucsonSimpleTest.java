import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.formats.TucsonSimple;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader readerMock;
 private BufferedWriter writerMock;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 readerMock = mock(BufferedReader.class);
 writerMock = mock(BufferedWriter.class);
 }

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
 String actual = tucsonSimple.toString();
 assert (expected.equals(actual));
 }

 @Test
 public void testGetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 assert (expected.equals(actual));
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsException() throws IOException {
 when(readerMock.readLine()).thenReturn("test line");
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.setMeta(new HashMap<String, Object>() {{
 put("id", "123456");
 }});
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 assert (expected.equals(actual));

 Sample sample2 = new Sample();
 String actual2 = tucsonSimple.make6digitCode(sample2);
 String expected2 = "000000";
 assert (expected2.equals(actual2));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = new Sample();
 Year year = new Year(2001);
 String code = "ABCDEF";
 int yearWidth = 6;
 String expected = "ABCDEF2001";
 tucsonSimple.saveRowHeader(writerMock, sample, code, yearWidth, year);
 String actual = "";
 writerMock.write(Mockito.argThat(arg -> {
 actual = arg.toString();
 return true;
 }));
 assert (expected.equals(actual));
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(2001), new Year(2002));
 sample.setRange(range);
 String code = "ABCDEF";
 boolean isSummed = false;
 boolean isProcessed = false;
 Year start = new Year(2001);
 Year end = new Year(2002);
 Year y = new Year(2001);
 String data = "1";
 String expected = "ABCDEF2001  1\n";
 tucsonSimple.saveData(sample, writerMock);
 String actual = "";
 writerMock.write(Mockito.argThat(arg -> {
 actual = arg.toString();
 return arg.toString().startsWith(expected.substring(0, expected.length()-1));
 }));
 assert (expected.equals(actual.substring(0, expected.length()-1)));
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 Year start = new Year(1999);
 Range expected = new Range(new Year(1999), new Year(1999));
 Range actual = tucsonSimple.computeRange(sample);
 assert (expected.equals(actual));

 Sample sample2 = new Sample();
 Year start2 = new Year(1995);
 Range expected2 = new Range(new Year(1995), new Year(1995));
 Range actual2 = tucsonSimple.computeRange(sample2);
 assert (expected2.equals(actual2));

 Sample sample3 = new Sample();
 Year start3 = new Year(1990);
 Range expected3 = new Range(new Year(1991), new Year(1990));
 Range actual3 = tucsonSimple.computeRange(sample3);
 assert (expected3.equals(actual3));

 Sample sample4 = new Sample();
 Year start4 = new Year(1899);
 Range expected4 = new Range(new Year(1901), new Year(1899));
 Range actual4 = tucsonSimple.computeRange(sample4);
 assert (expected4.equals(actual4));
}

 @Test
 public void testSave() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(2001), new Year(2002));
 sample.setRange(range);
 String code = "ABCDEF";
 boolean isSummed = false;
 boolean isProcessed = false;
 Year start = new Year(2001);
 Year end = new Year(2002);
 Year y = new Year(2001);
 String data = "1";
 String expected = "ABCDEF2001  1\n";
 tucsonSimple.save(sample, writerMock);
 String actual = "";
 writerMock.write(Mockito.argThat(arg -> {
 actual = arg.toString();
 return arg.toString().startsWith(expected.substring(0, expected.length()-1));
 }));
 assert (expected.equals(actual.substring(0, expected.length()-1)));
 }

}