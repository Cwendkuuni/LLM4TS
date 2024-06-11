package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.WrongFiletypeException;
import corina.ui.Sample;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedReader readerMock;
    private BufferedWriter writerMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tucsonSimple = new TucsonSimple();
        readerMock = mock(BufferedReader.class);
        writerMock = mock(BufferedWriter.class);
    }

    @Test
    public void testToString() {
        String expected = "format.tucsonsimple";
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
    public void testLoadWithWrongFiletype() throws IOException {
        when(readerMock.readLine()).thenReturn("not a TucsonSimple file");
        Sample sample = new Sample(new HashMap<>(), new Year(1), new Year(1), new byte[0]);
        tucsonSimple.load(readerMock, sample);
    }

    @Test
    public void testMake6digitCodeWithSampleId() {
        Sample sample = new Sample(new HashMap<>(), new Year(1), new Year(1), new byte[0]);
        sample.meta.put("id", "123456");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeWithoutSampleId() {
        Sample sample = new Sample(new HashMap<>(), new Year(1), new Year(1), new byte[0]);
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeaderWithStartYearBeforeSampleYear() throws IOException {
        Sample sample = new Sample(new HashMap<>(), new Year(2000), new Year(2001), new byte[0]);
        String code = "123456";
        int yearWidth = 6;
        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, new Year(1999));

        String expected = "123456 1999";
        verify(writer).write(expected);
    }

    @Test
    public void testSaveRowHeaderWithStartYearEqualSampleYear() throws IOException {
        Sample sample = new Sample(new HashMap<>(), new Year(2000), new Year(2000), new byte[0]);
        String code = "123456";
        int yearWidth = 6;
        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, new Year(2000));

        String expected = "123456 2000";
        verify(writer).write(expected);
    }

    @Test
    public void testSaveRowHeaderWithStartYearAfterSampleYear() throws IOException {
        Sample sample = new Sample(new HashMap<>(), new Year(2001), new Year(2000), new byte[0]);
        String code = "123456";
        int yearWidth = 6;
        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, new Year(2002));

        String expected = "123456   ";
        verify(writer).write(expected);
    }

    @Test
    public void testSaveDataWithSampleDataAndNoSum() throws IOException {
        Sample sample = new Sample(new HashMap<>(), new Year(2001), new Year(2002), new byte[0]);
        sample.data.put(new Year(2001), "10");
        sample.data.put(new Year(2002), "20");
        String code = "123456";
        int yearWidth = 6;
        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveData(sample, writer);

        String expected = "123456 2001 10\n123456 2002 20\n";
        verify(writer).write(expected);
    }

    @Test
    public void testSaveDataWithSampleDataAndSum() throws IOException {
        Sample sample = new Sample(new HashMap<>(), new Year(2001), new Year(2002), new byte[0]);
        sample.data.put(new Year(2001), "10");
        sample.data.put(new Year(2002), "20");
        String code = "123456";
        int yearWidth = 6;
        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveData(sample, writer, true);

        String expected = "123456 2001 10\n123456 2002 20\n123456 2001 10 20\n";
        verify(writer).write(expected);
    }

    @Test
    public void testSaveDataWithSampleDataAndProcessed() throws IOException {
        Sample sample = new Sample(new HashMap<>(), new Year(2001), new Year(2002), new byte[0]);
        sample.data.put(new Year(2001), "10");
        sample.data.put(new Year(2002), "20");
        String code = "123456";
        int yearWidth = 4;
        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveData(sample, writer, true, true);

        String expected = "123456 2001 10\n123456 2002 20\n123456   \n";
        verify(writer).write(expected);
    }

    @Test
    public void testComputeRangeWithStartYearBefore1() throws Exception {
        Sample sample = new Sample(new HashMap<>(), new Year(1999), new Year(2000), new byte[0]);
        Range expected = new Range(new Year(1001), new Year(1001));
        Range actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWithStartYearBetween8000And1() throws Exception {
        Sample sample = new Sample(new HashMap<>(), new Year(7999), new Year(8000), new byte[0]);
        Range expected = new Range(new Year(8001), new Year(8001));
        Range actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWithStartYearGreaterThan8000() throws Exception {
        Sample sample = new Sample(new HashMap<>(), new Year(8001), new Year(8002), new byte[0]);
        Range expected = new Range(new Year(8001), new Year(8002));
        Range actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveWithSampleDataAndNoSum() throws IOException {
        Sample sample = new Sample(new HashMap<>(), new Year(2001), new Year(2002), new byte[0]);
        sample.data.put(new Year(2001), "10");
        sample.data.put(new Year(2002), "20");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        tucsonSimple.save(sample, writer);

        String expected = "123456 2001 10\n123456 2002 20\n";
        String actual = outputStream.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveWithSampleDataAndSum() throws IOException {
        Sample sample = new Sample(new HashMap<>(), new Year(2001), new Year(2002), new byte[0]);
        sample.data.put(new Year(2001), "10");
        sample.data.put(new Year(2002), "20");
        sample.count.put(new Year(2001), 2);
        sample.count.put(new Year(2002), 3);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        tucsonSimple.save(sample, writer, true);

        String expected = "123456 2001 10\n123456 2002 20\n123456 2001 10 2 20 3\n";
        String actual = outputStream.toString();

        assertEquals(expected, actual);
    }
}