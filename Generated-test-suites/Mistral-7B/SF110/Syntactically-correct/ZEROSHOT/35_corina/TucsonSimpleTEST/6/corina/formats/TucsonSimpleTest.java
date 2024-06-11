import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.CorinaException;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader readerMock;
 private BufferedWriter writerMock;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 readerMock = mock(BufferedReader.class);
 writerMock = mock(BufferedWriter.class);
 }

 @Test
 public void testToString() throws CorinaException {
 String expected = "format.tucsonsimple";
 I18n i18nMock = mock(I18n.class);
 when(i18nMock.getText(expected)).thenReturn("Tucson Simple");
 tucsonSimple.i18n = i18nMock;

 String actual = tucsonSimple.toString();

 assert (actual.equals("Tucson Simple"));
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();

 assert (expected.equals(actual));
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 BufferedReader reader = new BufferedReader(new StringReader(""));
 when(readerMock.readLine()).thenReturn("");
 when(readerMock.readLine()).thenThrow(IOException.class);

 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() throws CorinaException {
 Sample sample = new Sample();
 sample.meta = new HashMap<String, Object>();
 sample.meta.put("id", 123456);

 String expected = "123456";
 String code = tucsonSimple.make6digitCode(sample);

 assert (expected.equals(code));

 Sample sample2 = new Sample();
 String code2 = tucsonSimple.make6digitCode(sample2);

 assert (code2.equals("000000"));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 StringWriter writer = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(writer);
 writerMock = mock(BufferedWriter.class);
 when(writerMock.write(Mockito.anyString())).then(i -> writer.write(i.arg(0)));

 Year year = new Year(2000);
 Sample sample = new Sample();
 String code = "ABCDEF";
 int yearWidth = 6;

 tucsonSimple.saveRowHeader(writerMock, sample, code, yearWidth, year);

 String expected = "ABCDEF2000";
 String actual = writer.toString();

 assert (expected.equals(actual));
 }

 @Test
 public void testSaveData() throws IOException, CorinaException {
 StringWriter writer = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(writer);
 writerMock = mock(BufferedWriter.class);
 when(writerMock.write(Mockito.anyString())).then(i -> writer.write(i.arg(0)));
 when(writerMock.newLine()).then(i -> writer.write("\n"));

 Sample sample = new Sample();
 Range range = new Range(new Year(2000), new Year(2001));
 sample.range = range;
 Year start = range.getStart();
 Year end = range.getEnd();
 String code = "ABCDEF";
 boolean isSummed = false;
 boolean isProcessed = false;
 Year y = start;
 Year yEnd = end;
 String yearPrefix = "";

 tucsonSimple.saveData(sample, writerMock);

 String expected = "ABCDEF2000\n" +
                  "000001  " +
                  "0.5\n" +
                  "ABCDEF2001\n" +
                  "000002  " +
                  "0.5\n" +
                  "ABCDEF   999\n" +
                  "\n";
 String actual = writer.toString();

 assert (expected.equals(actual));

 // Test with processed data
 sample.processData();
 y = yEnd.add(-start.column());
 yearPrefix = "000004 ";

 tucsonSimple.saveData(sample, writerMock);

 expected = "ABCDEF2000\n" +
                  "000001  " +
                  "0.5\n" +
                  "ABCDEF2001\n" +
                  "000002  " +
                  "0.5\n" +
                  "ABCDEF   999\n" +
                  "9990   0\n" +
                  "\n";
 String actual2 = writer.toString();

 assert (expected.equals(actual2));
 }

 @Test
 public void testComputeRange() throws CorinaException {
 Sample sample = new Sample();
 Year start = new Year(1999);
 Range expectedRange = new Range(new Year(1999), new Year(1999));

 Range actualRange = tucsonSimple.computeRange(sample);

 assert (expectedRange.equals(actualRange));

 Sample sample2 = new Sample();
 Year start2 = new Year(7001);
 Year end = new Year(7010);
 Range expectedRange2 = new Range(new Year(7001), new Year(7010));

 Range actualRange2 = tucsonSimple.computeRange(sample2);

 assert (expectedRange2.equals(actualRange2));
 }

 @Test
 public void testSave() throws IOException, CorinaException {
 StringWriter writer = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(writer);
 writerMock = mock(BufferedWriter.class);
 when(writerMock.write(Mockito.anyString())).then(i -> writer.write(i.arg(0)));
 when(writerMock.newLine()).then(i -> writer.write("\n"));

 Sample sample = new Sample();
 Range range = new Range(new Year(2000), new Year(2001));
 sample.range = range;
 Year start = range.getStart();
 Year end = range.getEnd();
 String code = "ABCDEF";
 boolean isProcessed = false;

 tucsonSimple.save(sample, writerMock);

 String expected = "ABCDEF2000\n" +
                  "000001  " +
                  "0.5\n" +
                  "ABCDEF2001\n" +
                  "000002  " +
                  "0.5\n" +
                  "ABCDEF   999\n" +
                  "\n";
 String actual = writer.toString();

 assert (expected.equals(actual));
 }
}