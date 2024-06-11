package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.*;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Sample sample;
    private BufferedReader reader;
    private BufferedWriter writer;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        sample = new Sample();
        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("TestData".getBytes())));
        writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
    }

    @Test
    public void toStringTest() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void getDefaultExtensionTest() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void loadExceptionHandlingTest() {
        when(reader.readLine()).thenThrow(new IOException());
        assertThrows(WrongFiletypeException.class, () -> tucsonSimple.load(reader));
    }

    @Test
    public void make6digitCodeTest() {
        sample.meta.put("id", "123456");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));

        sample.meta.remove("id");
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void make6digitCodeEdgeCasesTest() {
        sample.meta.put("id", new String(new char[7]));
        assertEquals("0000001", tucsonSimple.make6digitCode(sample));

        sample.meta.put("id", new String(new char[8]));
        assertEquals("0000006", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void saveRowHeaderTest() throws IOException {
        Year start = new Year(2001);
        when(reader.readLine()).thenReturn("SampleData");

        tucsonSimple.saveRowHeader(writer, sample, "000001", 6, start);
        assertEquals("0000012001", writer.toString());

        Year end = new Year(2002);
        tucsonSimple.saveRowHeader(writer, sample, "000001", 6, end);
        assertEquals("0000012001\n9990  0", writer.toString());
    }

    @Test
    public void saveRowHeaderEdgeCasesTest() throws IOException {
        Year start = new Year(-1);
        tucsonSimple.saveRowHeader(writer, sample, "000001", 6, start);
        assertEquals("000001-1\n9990  0", writer.toString());

        Year end = new Year(2000);
        tucsonSimple.saveRowHeader(writer, sample, "000001", 6, end);
        assertEquals("0000012000\n9990   ", writer.toString());
    }

    @Test
    public void saveDataTest() throws IOException {
        sample.data.put(new Year(2001), "1");
        sample.count.put(new Year(2001), "1");

        tucsonSimple.saveData(sample, writer);
        assertEquals("000001 1 1\n9990  1", writer.toString());
    }

    @Test
    public void saveDataEdgeCasesTest() throws IOException {
        sample.data.put(new Year(2001), "12345678901234567890");
        sample.count.put(new Year(2001), "123456789012345680");

        tucsonSimple.saveData(sample, writer);
        assertEquals("000001 12345678901234567890\n9990  123456789012345680", writer.toString());
    }

    @Test
    public void computeRangeTest() {
        sample.range = new Range(new Year(1995), new Year(2000));
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(sample.range, range);

        sample.range = new Range(new Year(1), new Year(-1000));
        range = tucsonSimple.computeRange(sample);
        assertEquals(sample.range.redateStartTo(new Year(1001)), range);

        sample.range = new Range(new Year(-8001), new Year(-8000));
        range = tucsonSimple.computeRange(sample);
        assertEquals(sample.range.redateBy(8000), range);
    }

    @Test(expected = WrongFiletypeException.class)
    public void loadTest() throws IOException {
        when(reader.readLine()).thenReturn("TestData");
        tucsonSimple.load(reader);
        assertThrows(WrongFiletypeException.class, () -> tucsonSimple.load(reader));
    }

    @Test(expected = IOException.class)
    public void saveDataExceptionHandlingTest() throws IOException {
        when(writer.write(StringUtils.leftPad("Test", 4))).thenThrow(new IOException());
        tucsonSimple.saveData(sample, writer);
        assertThrows(IOException.class, () -> tucsonSimple.saveData(sample, writer));
    }
}