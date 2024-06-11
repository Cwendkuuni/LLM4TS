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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.Sample;
import corina.WrongFiletypeException;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedReader readerMock;
    private BufferedWriter writerMock;
    private Sample sample;

    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
        readerMock = mock(BufferedReader.class);
        writerMock = mock(BufferedWriter.class);
        sample = new Sample("testData", new HashMap<>(), new Year(2000), new Range(new Year(1999), new Year(2001)));
    }

    @Test
    public void testToString() {
        String expected = "format.tucsonsimple";
        String result = tucsonSimple.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testgetDefaultExtension() {
        String expected = ".RWL";
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(expected, result);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadUnsupportedFile() throws IOException {
        when(readerMock.readLine()).thenReturn("testLine1");
        when(readerMock.readLine()).thenReturn("testLine2");

        tucsonSimple.load(readerMock);
    }

    @Test
    public void testMake6digitCodeSampleWithId() {
        String expected = "123456";
        Map<String, String> meta = new HashMap<>();
        meta.put("id", "123456");

        String result = tucsonSimple.make6digitCode(new Sample("testData", meta, new Year(2000), new Range(new Year(1999), new Year(2001))));
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCodeSampleWithoutId() {
        String expected = "000000";
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream));

        Year start = new Year(1998);
        Year end = new Year(2001);
        String code = "123456";
        int yearWidth = 6;

        tucsonSimple.saveRowHeader(w, sample, code, yearWidth, start);
        String expected = "1234561998";
        String result = outputStream.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testSaveData() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream));

        when(reader.readLine()).thenReturn("testData1");
        when(reader.readLine()).thenReturn("testData2");
        when(reader.readLine()).thenReturn(null);

        tucsonSimple.save(sample, w);

        String expected = "1234561999   testData1\n" +
                          "1234562000   testData2\n";
        String result = outputStream.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRange() {
        Year expected = new Year(2002);
        Year result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testSave() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream));

        String inputData = "1,2,3,4,5\n";
        String expectedData = "12345\n" +
                               "12346\n" +
                               "12347\n" +
                               "12348\n" +
                               "12349\n";

        when(readerMock.readLine()).thenReturn(inputData);
        when(readerMock.readLine()).thenReturn(null);

        tucsonSimple.save(sample, writerMock);

        verify(writerMock, times(5)).write(eq("12345"));
        verify(writerMock, times(5)).write(eq(" "));
        verify(writerMock, times(5)).newLine();

        String result = outputStream.toString();
        assertEquals(expectedData, result);
    }
}