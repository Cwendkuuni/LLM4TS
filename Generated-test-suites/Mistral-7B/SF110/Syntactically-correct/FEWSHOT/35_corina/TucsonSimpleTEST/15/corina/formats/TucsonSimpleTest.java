package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedWriter writerMock;
    private Sample sampleMock;

    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
        writerMock = mock(BufferedWriter.class);
        sampleMock = mock(Sample.class);
    }

    @Test
    public void testMake6digitCode() {
        when(sampleMock.meta).thenReturn(new HashMap<String, String>() {{
            put("id", "123456");
        }});

        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sampleMock);

        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeWhenNoId() {
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sampleMock);

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        Year start = new Year(2000);
        when(sampleMock.range).thenReturn(new Range(start, start.add(1)));

        String code = "123";
        int yearWidth = 6;

        tucsonSimple.saveRowHeader(writerMock, sampleMock, code, yearWidth, start);

        String expected = "1232000";
        String actual = "";
        try (BufferedReader reader = new BufferedReader(new StringReader(new StringReader(writerMock.toString()).toString()))) {
            actual = reader.readLine();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeaderWhenStartYearIsBefore() throws IOException {
        Year start = new Year(1999);
        when(sampleMock.range).thenReturn(new Range(start, start.add(1)));

        String code = "123";
        int yearWidth = 6;

        tucsonSimple.saveRowHeader(writerMock, sampleMock, code, yearWidth, start);

        String expected = "1999123";
        String actual = "";
        try (BufferedReader reader = new BufferedReader(new StringReader(new StringReader(writerMock.toString()).toString()))) {
            actual = reader.readLine();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException {
        when(sampleMock.data.get(0)).thenReturn("10.5");
        when(sampleMock.count.get(0)).thenReturn(1);

        Year start = new Year(2000);
        Range range = new Range(start, start.add(1));
        when(sampleMock.range).thenReturn(range);

        tucsonSimple.saveData(sampleMock, writerMock);

        String expected = "10.5   1";
        String actual = "";
        try (BufferedReader reader = new BufferedReader(new StringReader(new StringReader(writerMock.toString()).toString()))) {
            actual = reader.readLine();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveDataWhenIsSummedIsTrue() throws IOException {
        when(sampleMock.data.get(0)).thenReturn("10.5");
        when(sampleMock.count.get(0)).thenReturn(1);

        Year start = new Year(2000);
        Range range = new Range(start, start.add(1));
        when(sampleMock.range).thenReturn(range);

        tucsonSimple.saveData(sampleMock, writerMock, true);

        String expected = "10.5   10.5";
        String actual = "";
        try (BufferedReader reader = new BufferedReader(new StringReader(new StringReader(writerMock.toString()).toString()))) {
            actual = reader.readLine();
        }

        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadMethodThrowsException() throws IOException {
        BufferedReader readerMock = mock(BufferedReader.class);
        tucsonSimple.load(readerMock);
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(1995), new Year(1996)));

        Range expected = new Range(new Year(1995), new Year(1995));
        Range actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWhenStartYearIsAfter8000() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(10001), new Year(10002)));

        Range expected = new Range(new Year(28001), new Year(28002));
        Range actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWhenStartYearIsBefore1001() {
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(1), new Year(2)));

        Range expected = new Range(new Year(1001), new Year(1001));
        Range actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);
    }
}