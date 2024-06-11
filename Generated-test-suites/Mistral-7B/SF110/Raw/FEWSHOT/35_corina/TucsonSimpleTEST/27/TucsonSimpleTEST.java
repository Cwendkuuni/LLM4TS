package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.Year;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {
    private TucsonSimple tucsonSimple;
    private BufferedWriter bufferedWriterMock;
    private ByteArrayOutputStream byteArrayOutputStream;
    private I18n i18nMock;
    private Sample sampleMock;
    private Year yearMock1, yearMock2;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        bufferedWriterMock = mock(BufferedWriter.class);
        byteArrayOutputStream = new ByteArrayOutputStream();
        when(bufferedWriterMock.write(anyString())).thenWriteTo(byteArrayOutputStream);
        when(bufferedWriterMock.newLine()).thenCallRealMethod();
        yearMock1 = new Year(2000);
        yearMock2 = new Year(2010);
        i18nMock = mock(I18n.class);
        sampleMock = mock(Sample.class);
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        tucsonSimple.load(mock(BufferedReader.class));
    }

    @Test
    public void testMake6digitCode() {
        String id = "sampleId";
        when(sampleMock.meta).thenReturn(new HashMap<>());
        when(sampleMock.meta.get("id")).thenReturn(id);

        String code = tucsonSimple.make6digitCode(sampleMock);
        assertEquals(id, code);

        when(sampleMock.meta.get("id")).thenReturn(null);

        code = tucsonSimple.make6digitCode(sampleMock);
        assertEquals("000000", code);

        id = "123456";
        when(sampleMock.meta.get("id")).thenReturn(id);

        code = tucsonSimple.make6digitCode(sampleMock);
        assertEquals("123456", code);

        id = "1234567";
        when(sampleMock.meta.get("id")).thenReturn(id);

        code = tucsonSimple.make6digitCode(sampleMock);
        assertEquals("123456", code);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        String code = "sampleCode";
        int yearWidth = 6;
        String prefix = "prefix";

        tucsonSimple.saveRowHeader(bufferedWriterMock, sampleMock, code, yearWidth, yearMock1);
        assertEquals(code + prefix, byteArrayOutputStream.toString());

        prefix = "0001";
        tucsonSimple.saveRowHeader(bufferedWriterMock, sampleMock, code, yearWidth, yearMock2);
        assertEquals(code + prefix, byteArrayOutputStream.toString());

        yearWidth = 4;
        tucsonSimple.saveRowHeader(bufferedWriterMock, sampleMock, code, yearWidth, yearMock1);
        assertEquals(code + "  0001", byteArrayOutputStream.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        String data = "data";
        double sum = 123.45;
        int count = 5;
        Year start = yearMock1;
        Year end = yearMock2;
        String code = "sampleCode";
        boolean isSummed = true;
        boolean isProcessed = true;

        when(sampleMock.data).thenReturn(new HashMap<>());
        when(sampleMock.count).thenReturn(new HashMap<>());
        when(sampleMock.data.get(yearMock1.diff(start))).thenReturn(data);
        when(sampleMock.count.get(yearMock1.diff(start))).thenReturn(count);
        when(sampleMock.range.getStart()).thenReturn(start);
        when(sampleMock.range.getEnd()).thenReturn(end);

        tucsonSimple.saveData(sampleMock, bufferedWriterMock);

        String expectedOutput = code + start.toString() + "    " + StringUtils.leftPad(data, isProcessed ? 4 : 6) + "    "
                + StringUtils.leftPad(Double.toString(sum), isSummed ? 3 : 0) + System.lineSeparator();

        assertEquals(expectedOutput, byteArrayOutputStream.toString());
    }

    @Test
    public void testComputeRange() {
        Year start = new Year(1999);
        Sample sample = mock(Sample.class);
        when(sample.range.getStart()).thenReturn(start);

        Range range = tucsonSimple.computeRange(sample);

        assertEquals(start, range.getStart());

        start = new Year(1950);
        when(sample.range.getStart()).thenReturn(start);

        range = tucsonSimple.computeRange(sample);

        assertEquals(new Year(1001), range.getStart());

        start = new Year(2012);
        when(sample.range.getStart()).thenReturn(start);

        range = tucsonSimple.computeRange(sample);

        assertEquals(start.redateBy(8000), range.getStart());
    }

    @Test
    public void testSave() throws IOException {
        String data = "data";
        double sum = 123.45;
        int count = 5;
        Year start = yearMock1;
        Year end = yearMock2;
        String code = "sampleCode";
        boolean isSummed = true;
        boolean isProcessed = true;

        when(sampleMock.data).thenReturn(new HashMap<>());
        when(sampleMock.count).thenReturn(new HashMap<>());
        when(sampleMock.data.get(yearMock1.diff(start))).thenReturn(data);
        when(sampleMock.count.get(yearMock1.diff(start))).thenReturn(count);
        when(sampleMock.range.getStart()).thenReturn(start);
        when(sampleMock.range.getEnd()).thenReturn(end);

        tucsonSimple.save(sampleMock, bufferedWriterMock);

        String expectedOutput = code + start.toString() + "    " + StringUtils.leftPad(data, isProcessed ? 4 : 6) + "    "
                + StringUtils.leftPad(Double.toString(sum), isSummed ? 3 : 0) + System.lineSeparator();

        assertEquals(expectedOutput, byteArrayOutputStream.toString());
    }
}