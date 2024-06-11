import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.Year;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader readerMock;
 private BufferedWriter writerMock;
 private I18n i18nMock;
 private StringUtils stringUtilsMock;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 i18nMock = Mockito.mock(I18n.class);
 stringUtilsMock = Mockito.mock(StringUtils.class);
 readerMock = Mockito.mock(BufferedReader.class);
 writerMock = Mockito.mock(BufferedWriter.class);

 Mockito.when(i18nMock.getText(ArgumentMatchers.eq("format.tucsonsimple"))).thenReturn("TucsonSimple format");
 }

 @Test
 public void testToString() {
 assertEquals("TucsonSimple format", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsException() throws IOException {
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 Sample sample2 = new Sample();
 String code2 = tucsonSimple.make6digitCode(sample2);
 assertEquals("000000", code2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = new Sample();
 Year year = new Year(1990);
 StringWriter sw = new StringWriter();
 writerMock = Mockito.mock(BufferedWriter.class);
 Mockito.when(writerMock.write(ArgumentMatchers.anyString())).then(invocation -> sw.write(invocation.getArgument(0)));

 tucsonSimple.saveRowHeader(writerMock, sample, "000001", 6, year);
 String expected = "0000011990";
 assertEquals(expected, sw.toString());
}

 @Test
 public void testSaveData() throws IOException {
 StringWriter sw = new StringWriter();
 writerMock = Mockito.mock(BufferedWriter.class);
 Mockito.when(writerMock.write(ArgumentMatchers.anyString())).then(invocation -> sw.write(invocation.getArgument(0)));

 Sample sample = new Sample();
 sample.data = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9 };
 sample.count = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9 };
 Year start = new Year(2000);
 Year end = new Year(2010);

 tucsonSimple.saveData(sample, writerMock);
 String expected = "0000012000  1 2  \n" +
                 "0000012001  3 4  \n" +
                 "0000012002  5 6  \n" +
                 "0000012003  7 8  \n" +
                 "0000012004  9 9  \n" +
                 "0000012005  9 9  \n" +
                 "0000012006  9 9  \n" +
                 "0000012007  9 9  \n" +
                 "0000012008  9 9  \n" +
                 "0000012009  9 9  \n" +
                 "9990  0  \n" +
                 "9990   \n";
 assertEquals(expected, sw.toString());
}

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 Year start = new Year(1999);
 Range range = new Range(start, start.add(1));
 Range expectedRange = new Range(start, start.add(1));
 Range computedRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, computedRange);
}

 @Test
 public void testSave() throws IOException {
 Sample sample = new Sample();
 StringWriter sw = new StringWriter();
 writerMock = Mockito.mock(BufferedWriter.class);
 Mockito.when(writerMock.write(ArgumentMatchers.anyString())).then(invocation -> sw.write(invocation.getArgument(0)));

 tucsonSimple.save(sample, writerMock);
 assertNull(sw.toString());
}
}