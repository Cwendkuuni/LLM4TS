import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader mockBufferedReader;
 private BufferedWriter mockBufferedWriter;
 private I18n i18n;
 private Sample sample;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 i18n = mock(I18n.class);
 mockBufferedReader = mock(BufferedReader.class);
 mockBufferedWriter = mock(BufferedWriter.class);
 sample = new Sample() {{
 meta = new HashMap<>();
 meta.put("id", "123456");
 data = new ArrayList<>();
 count = new ArrayList<>();
 range = new Range() {{
 start = new Year(1990);
 end = new Year(1991);
 }};
 }};
 }

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
 when(i18n.getText(expected)).thenReturn("Tucson Simple Format");
 assertEquals("Tucson Simple Format", tucsonSimple.toString());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadNullReader() {
 tucsonSimple.load(null);
 }

 @Test(expected = IOException.class)
 public void testLoad() throws IOException {
 String line = "test line";
 when(mockBufferedReader.readLine()).thenReturn(line);
 when(mockBufferedReader.readLine()).thenReturn(null);
 when(mockBufferedReader.readLine()).thenThrow(new IOException());
 tucsonSimple.load(mockBufferedReader);
 verify(mockBufferedReader, times(3)).readLine();
 }

 @Test
 public void testMake6digitCode() {
 String codeWithID = "123456";
 String codeWithoutID = "000000";
 assertEquals(codeWithID, tucsonSimple.make6digitCode(sample));
 assertEquals(codeWithoutID, tucsonSimple.make6digitCode(new Sample()));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 String code = "123456";
 String prefix = "1990";
 int yearWidth = 6;
 BufferedWriter w = Mockito.mock(BufferedWriter.class);
 Year start = new Year(1990);
 Year end = new Year(1991);
 tucsonSimple.saveRowHeader(w, sample, code, yearWidth, start);
 String expected = "1234561990";
 verify(w).write(expected);
 }

 @Test
 public void testSaveData() throws IOException {
 BufferedWriter w = Mockito.mock(BufferedWriter.class);
 String data1 = "12";
 String data2 = "34";
 Year start = new Year(1990);
 Year end = new Year(1991);
 tucsonSimple.saveData(sample, w);
 String expected = "1234561990 12\n9990  0\n3434561991 34\n";
 verify(w).write(expected);
 }

 @Test
 public void testComputeRange() {
 Year expectedStart = new Year(1991);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(expectedStart, range.getStart());
 }

 @Test
 public void testSave() throws IOException {
 BufferedWriter w = Mockito.mock(BufferedWriter.class);
 tucsonSimple.save(sample, w);
 verify(w, times(2)).write("9990   ");
 verify(w, times(2)).write("\n");
 verify(w, times(4)).write(StringUtils.class.getName() + ".leftPad");
 }
}