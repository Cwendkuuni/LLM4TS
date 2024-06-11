package corina.formats;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.formats.TucsonSimple;
import corina.formats.WrongFiletypeException;

public class TucsonSimpleTest {

    private BufferedReader reader;
    private BufferedWriter writer;
    private TucsonSimple tucsonSimple;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private Map<String, String> meta;

    @Before
    public void setUp() {
        I18n i18n = mock(I18n.class);
        when(i18n.getText(String.class, String.class)).thenReturn("format.tucsonsimple");

        tucsonSimple = new TucsonSimple();
        tucsonSimple.i18n = i18n;

        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        outputStream = new ByteArrayOutputStream();
        writer.writeTo(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
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
    public void testLoadThrowsException() throws IOException {
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = mock(Sample.class);
        meta = new HashMap<>();
        meta.put("id", "123456");
        when(sample.meta).thenReturn(meta);

        assertEquals("123456", tucsonSimple.make6digitCode(sample));

        Sample sample2 = mock(Sample.class);
        when(sample2.meta).thenReturn(null);

        assertEquals("000000", tucsonSimple.make6digitCode(sample2));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        Sample sample = mock(Sample.class);
        String code = "000001";
        int yearWidth = 4;
        Year year = mock(Year.class);

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);

        assertEquals("000001" + "1999", writer.getBuffer().toString());
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = mock(Sample.class);
        Range range = mock(Range.class);
        Year start = mock(Year.class);
        Year end = mock(Year.class);
        String code = "000001";
        boolean isSummed = true;
        boolean isProcessed = true;

        when(sample.range).thenReturn(range);
        when(range.getStart()).thenReturn(start);
        when(range.getEnd()).thenReturn(end);
        when(start.column()).thenReturn(1);

        tucsonSimple.saveData(sample, writer);

        assertEquals("0000011999\n", writer.getBuffer().toString());

        when(start.column()).thenReturn(2);

        tucsonSimple.saveData(sample, writer);

        assertEquals("0000011999\n9990  0\n", writer.getBuffer().toString());
    }

    @Test
    public void testComputeRange() {
        Sample sample = mock(Sample.class);
        Year start = mock(Year.class);
        when(sample.range.getStart()).thenReturn(start);

        Year year = new Year(1);
        assertEquals(sample.range, tucsonSimple.computeRange(sample));

        when(start.compareTo(new Year(1))).thenReturn(1);

        assertEquals(sample.range.redateBy(8000), tucsonSimple.computeRange(sample));

        when(start.add(8000).compareTo(new Year(1))).thenReturn(1);

        assertEquals(sample.range.redateStartTo(new Year(1001)), tucsonSimple.computeRange(sample));
    }

    @Test
    public void testSave() throws IOException {
        Sample sample = mock(Sample.class);
        Range range = new Range(new Year(1999), new Year(2000));
        when(sample.range).thenReturn(range);

        String[] data = {"1", "2", "3"};
        when(sample.data).thenReturn(data);

        tucsonSimple.save(sample, writer);

        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        BufferedWriter expectedWriter = new BufferedWriter(new OutputStreamWriter(expectedOutput));
        tucsonSimple.saveData(sample, expectedWriter);
        expectedWriter.close();

        assertEquals(expectedOutput.toString(), writer.getBuffer().toString());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testSaveThrowsException() throws IOException {
        tucsonSimple.save(mock(Sample.class), writer);
    }
}