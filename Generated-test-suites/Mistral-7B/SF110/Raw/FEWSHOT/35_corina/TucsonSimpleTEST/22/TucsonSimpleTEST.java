package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Year;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.WrongFiletypeException;
import corina.util.files.Sample;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private Sample sample;
    private BufferedWriter writer;

    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
        sample = new Sample();
        sample.setMeta(Map.of("id", "sampleId"));
        writer = Mockito.mock(BufferedWriter.class);
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
        tucsonSimple.load(mock(BufferedReader.class));
    }

    @Test
    public void testMake6digitCode() {
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("sampleId", code);

        String emptyCode = tucsonSimple.make6digitCode(new Sample());
        assertEquals("000000", emptyCode);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(stringWriter.toStringOutput()));

        tucsonSimple.saveRowHeader(bufferedWriter, sample, "code", 4, new Year(2020));
        assertEquals("code2020", stringWriter.toString());

        stringWriter = new StringWriter();
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(stringWriter.toStringOutput()));

        tucsonSimple.saveRowHeader(bufferedWriter, sample, "code", 6, new Year(2021));
        assertEquals("code  2021", stringWriter.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(stringWriter.toStringOutput()));

        Year start = new Year(2020);
        Year end = new Year(2022);

        when(writer.write(anyString())).thenAnswer(i -> i.getArgument(0));

        tucsonSimple.saveData(sample, bufferedWriter);

        String expectedOutput = "0000002020\n" +
                "0000002021   999\n" +
                "0000002022   999\n";

        assertEquals(expectedOutput, stringWriter.toString());

        verify(writer, times(3)).write(anyString());
    }

    @Test
    public void testComputeRange() {
        Year start = new Year(1995);
        Sample sample = new Sample();
        sample.setRange(new Range(start, start.add(1)));

        Range range = tucsonSimple.computeRange(sample);

        assertEquals(start, range.getStart());
        assertEquals(start.add(1), range.getEnd());
    }

    @Test
    public void testSave() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(stringWriter.toStringOutput()));

        Year start = new Year(2020);
        Year end = new Year(2022);

        when(writer.write(anyString())).thenAnswer(i -> i.getArgument(0));

        tucsonSimple.save(sample, bufferedWriter);

        String expectedOutput = "0000002020\n" +
                "0000002021   999\n" +
                "0000002022   999\n";

        assertEquals(expectedOutput, stringWriter.toString());

        verify(writer, times(3)).write(anyString());
    }
}