import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import corina.formats.I18n;
import corina.formats.Sample;
import corina.formats.StringUtils;
import corina.formats.TucsonSimple;
import corina.formats.Year;
import corina.ui.UIComponent;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

    private File tempFile;
    private BufferedWriter writer;
    private TucsonSimple tucsonSimple;

    @Before
    public void setUp() throws Exception {
        tucsonSimple = new TucsonSimple();
        I18n i18nMock = mock(I18n.class);
        when(i18nMock.getText(eq("format.tucsonsimple"))).thenReturn("TucsonSimple format");
        tucsonSimple.setI18n(i18nMock);

        tempFile = File.createTempFile("TucsonSimpleTest", ".RWL");
        writer = new BufferedWriter(new FileWriter(tempFile));
    }

    @After
    public void tearDown() throws Exception {
        if (tempFile.exists()) {
            tempFile.delete();
        }
        if (writer != null) {
            writer.close();
        }
    }

    @Test
    public void testToString() {
        String expected = "TucsonSimple format";
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
    public void testLoad() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("Invalid data");
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        Map<String, String> meta = new HashMap<>();
        meta.put("id", "ABC123");
        sample.meta = meta;

        String expected = "ABC123";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);

        sample.meta = new HashMap<>();
        expected = "000000";
        actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        BufferedWriter wMock = mock(BufferedWriter.class);
        Sample sample = new Sample();
        Map<String, String> meta = new HashMap<>();
        meta.put("id", "ABC123");
        sample.meta = meta;
        Year start = new Year(1995);
        Year end = new Year(1996);
        int yearWidth = 6;

        tucsonSimple.saveRowHeader(wMock, sample, "ABC123", yearWidth, start);
        String expected = "ABC1231995";
        String actual = readLineFromFile(writer, "Utf-8");
        assertEquals(expected, actual);

        start = new Year(1995).subtract(1);
        tucsonSimple.saveRowHeader(wMock, sample, "ABC123", yearWidth, start);
        expected = "ABC1231994";
        actual = readLineFromFile(writer, "Utf-8");
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        Map<String, String> meta = new HashMap<>();
        meta.put("id", "ABC123");
        sample.meta = meta;
        Range range = new Range(new Year(1995), new Year(1997));
        sample.range = range;
        Year start = new Year(1995);
        Year end = new Year(1996);
        String code = "123456";
        boolean isSummed = false;
        boolean isProcessed = false;
        Year y = start;

        tucsonSimple.saveData(sample, writer);

        String expectedCode1 = "1234561995";
        String expectedCode2 = "1234561996";
        String expectedData1 = "0";
        String expectedData2 = "123";
        String expectedData3 = "456";

        String actualCode1 = readLineFromFile(writer, "Utf-8");
        String actualCode2 = readLineFromFile(writer, "Utf-8");
        String actualData1 = readLineFromFile(writer, "Utf-8");
        String actualData2 = readLineFromFile(writer, "Utf-8");

        assertEquals(expectedCode1, actualCode1);
        assertEquals(expectedCode2, actualCode2);
        assertEquals(expectedData1, actualData1);
        assertEquals(expectedData2, actualData2);

        y = new Year(1997);
        tucsonSimple.saveData(sample, writer);

        String actualData3 = readLineFromFile(writer, "Utf-8");
        assertEquals(expectedData3, actualData3);
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        Range expected = new Range(new Year(1), new Year(1001));
        Range actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);

        sample.range = new Range(new Year(2000), new Year(2005));
        expected = new Range(new Year(2000), new Year(2005)).redateStartTo(new Year(1001));
        actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);

        sample.range = new Range(new Year(1999), new Year(2000));
        expected = new Range(new Year(1001), new Year(1001));
        actual = tucsonSimple.computeRange(sample);

        assertEquals(expected, actual);
    }

    @Test
    public void testSave() throws IOException {
        Sample sample = new Sample();
        Map<String, String> meta = new HashMap<>();
        meta.put("id", "ABC123");
        sample.meta = meta;
        Range range = new Range(new Year(1995), new Year(1997));
        sample.range = range;

        tucsonSimple.save(sample, writer);

        String expectedCode1 = "ABC1231995";
        String expectedCode2 = "ABC1231996";
        String expectedData1 = "0";
        String expectedData2 = "123";
        String expectedData3 = "456";

        String actualCode1 = readLineFromFile(writer, "Utf-8");
        String actualCode2 = readLineFromFile(writer, "Utf-8");
        String actualData1 = readLineFromFile(writer, "Utf-8");
        String actualData2 = readLineFromFile(writer, "Utf-8");

        assertEquals(expectedCode1, actualCode1);
        assertEquals(expectedCode2, actualCode2);
        assertEquals(expectedData1, actualData1);
        assertEquals(expectedData2, actualData2);
    }

    private String readLineFromFile(BufferedWriter writer, String encoding) throws IOException {
        writer.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile), encoding));
        String line = reader.readLine();
        reader.close();
        return line;
    }
}