package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    private BufferedReader mockReader;
    private BufferedWriter mockWriter;
    private TucsonSimple tucsonSimple;
    private Sample mockSample;

    @Before
    public void setup() {
        mockReader = mock(BufferedReader.class);
        mockWriter = mock(BufferedWriter.class);
        tucsonSimple = new TucsonSimple();
        mockSample = mock(Sample.class);
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
    public void testLoadThrowsWrongFiletypeException() throws IOException {
        when(mockReader.readLine()).thenReturn("test");
        tucsonSimple.load(mockReader);
    }

    @Test
    public void testMake6digitCode() {
        when(mockSample.meta).thenReturn(new HashMap<String, Object>() {{
            put("id", "123456");
        }});
        assertEquals("123456", tucsonSimple.make6digitCode(mockSample));

        when(mockSample.meta).thenReturn(new HashMap<String, Object>() {{
            put("id", "1234567890");
        }});
        assertEquals("123456", tucsonSimple.make6digitCode(mockSample));

        when(mockSample.meta).thenReturn(new HashMap<String, Object>() {{
            put("id", "123456");
        }});
        assertEquals("000000", tucsonSimple.make6digitCode(mockSample));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date startDate = dateFormat.parse("2010");
        Year start = new Year(startDate);
        when(mockSample.range).thenReturn(new Range(start, start.add(1)));

        when(mockWriter.write(anyString())).thenAnswer(i -> i.toString());

        tucsonSimple.saveRowHeader(mockWriter, mockSample, "123456", 4, start);
        assertEquals("1234562010", mockWriter.toString());

        when(mockSample.range).thenReturn(new Range(start.add(-1), start));

        tucsonSimple.saveRowHeader(mockWriter, mockSample, "123456", 6, start);
        assertEquals("123456  2010", mockWriter.toString());
    }

    @Test
    public void testSaveData() throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date startDate = dateFormat.parse("2010");
        Year start = new Year(startDate);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yy");
        Date endDate = dateFormat2.parse("12");
        Year end = new Year(endDate);
        when(mockSample.data).thenReturn(new HashMap<Year, String>() {{
            put(start, "1");
            put(start.add(1), "2");
            put(end.add(-1), "3");
        }});
        when(mockSample.count).thenReturn(new HashMap<Year, Integer>() {{
            put(start, 1);
            put(start.add(1), 2);
        }});
        when(mockSample.range).thenReturn(new Range(start, end));

        when(mockWriter.write(anyString())).thenAnswer(i -> i.toString());

        tucsonSimple.saveData(mockSample, mockWriter);
        assertEquals("123456 2010\n1 1\n2 2\n3   \n", mockWriter.toString());
    }

    @Test
    public void testComputeRange() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date startDate = dateFormat.parse("1980");
        Year start = new Year(startDate.getTime());
        assertEquals(new Range(start, start), tucsonSimple.computeRange(mockSample));

        Date endDate = dateFormat.parse("1999");
        Year end = new Year(endDate.getTime());
        when(mockSample.range).thenReturn(new Range(start, end));
        assertEquals(new Range(start, end.add(8000)), tucsonSimple.computeRange(mockSample));

        Date date = dateFormat.parse("2000");
        Year dateYear = new Year(date.getTime());
        assertEquals(new Range(dateYear, dateYear.add(1)), tucsonSimple.computeRange(mockSample));
    }

    @Test
    public void testSave() throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date startDate = dateFormat.parse("2010");
        Year start = new Year(startDate);
        when(mockSample.data).thenReturn(new HashMap<Year, String>() {{
            put(start, "1");
            put(start.add(1), "2");
        }});
        when(mockSample.count).thenReturn(new HashMap<Year, Integer>() {{
            put(start, 1);
            put(start.add(1), 2);
        }});
        when(mockSample.range).thenReturn(new Range(start, start.add(1)));

        when(mockWriter.write(anyString())).thenAnswer(i -> i.toString());

        tucsonSimple.save(mockSample, mockWriter);
        assertEquals("123456 2010\n1 1\n2 2\n", mockWriter.toString());
    }
}