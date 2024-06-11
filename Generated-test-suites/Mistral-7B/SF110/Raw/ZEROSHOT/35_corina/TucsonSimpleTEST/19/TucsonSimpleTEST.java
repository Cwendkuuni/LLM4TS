import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import corina.formats.I18n;
import corina.formats.StringUtils;
import corina.ui.Sample;
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
 when(writerMock.write(Mockito.anyString())).then(invocation -> outputStream.write(invocation.getArgument(0).toString().getBytes()));
 }

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
 String actual = tucsonSimple.toString();
 assert (expected.equals(actual));
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 assert (expected.equals(actual));
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadMethodThrowsException() throws IOException {
 BufferedReader readerMock = mock(BufferedReader.class);
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 String expected = "123456";
 String code = tucsonSimple.make6digitCode(sample);
 assert (expected.equals(code));

 Sample sample2 = new Sample();
 String code2 = tucsonSimple.make6digitCode(sample2);
 assert (code2.equals("000000"));
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = new Sample();
 sample.range = new Range(new Year(2000), new Year(2002));
 sample.data = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 sample.count = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 String expected = "000001 2000 1\n" +
 "000002 2000 2\n" +
 "000003 2000 3\n" +
 "000004 2000 4\n" +
 "000005 2000 5\n" +
 "000006 2000 6\n" +
 "000007 2001 1\n" +
 "000008 2001 2\n" +
 "000009 2001 3\n" +
 "000010 2001 4\n" +
 "000011 2001 5\n" +
 "000012 2001 6\n" +
 "000013 2001 7\n" +
 "000014 2001 8\n" +
 "000015 2002 1\n" +
 "000016 2002 2\n" +
 "000017 2002 3\n" +
 "9990   ";

 tucsonSimple.saveData(sample, writerMock);
 String actual = new String(outputStream.toByteArray());
 assert (expected.equals(actual));
 }

 @Test
 public void testSaveDataWhenProcessed() throws IOException {
 Sample sample = new Sample();
 sample.range = new Range(new Year(2000), new Year(2002));
 sample.data = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 sample.count = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 sample.meta.put("processed", true);
 String expected = "000001 2000 1\n" +
 "000004 1001 1\n" +
 "9990  0\n";

 tucsonSimple.saveData(sample, writerMock);
 String actual = new String(outputStream.toByteArray());
 assert (expected.equals(actual));
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 sample.range = new Range(new Year(-1), new Year(2002));
 Range expected = new Range(new Year(1001), new Year(2002));
 Range actual = tucsonSimple.computeRange(sample);
 assert (expected.equals(actual));
 }

 @Test
 public void testSaveMethod() throws IOException {
 Sample sample = new Sample();
 sample.range = new Range(new Year(2000), new Year(2002));
 sample.data = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 sample.count = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
 tucsonSimple.save(sample, writer);
 BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("output.txt")));
 String expected = "000001 2000 1\n" +
 "000002 2000 2\n" +
 "000003 2000 3\n" +
 "000004 2000 4\n" +
 "000005 2000 5\n" +
 "000006 2000 6\n" +
 "000007 2001 1\n" +
 "000008 2001 2\n" +
 "000009 2001 3\n" +
 "000010 2001 4\n" +
 "000011 2001 5\n" +
 "000012 2001 6\n" +
 "000013 2001 7\n" +
 "000014 2001 8\n" +
 "000015 2002 1\n" +
 "000016 2002 2\n" +
 "000017 2002 3\n";
 String actual = "";
 String line;
 while ((line = reader.readLine()) != null) {
 actual += line;
 }
 assert (expected.equals(actual));
 }

}