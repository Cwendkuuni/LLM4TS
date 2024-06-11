package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.formats.Filetype;
import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.Year;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedWriter mockWriter;
    private BufferedReader mockReader;
    private Sample sample;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        mockWriter = mock(BufferedWriter.class);
        mockReader = mock(BufferedReader.class);
        sample = new Sample() {
            private Map<String, Serializable> meta = new HashMap<>();
            private Year range = new Year(1999);
            private int[] data = new int[] { 1, 2, 3, 4 };
            private int[] count = new int[] { 1, 2, 3, 4 };

            public Year getRange() {
                return range;
            }

            public void setRange(Year range) {
                this.range = range;
            }

            public Map<String, Serializable> getMeta() {
                return meta;
            }

            public void setMeta(Map<String, Serializable> meta) {
                this.meta = meta;
            }

            public int[] getData() {
                return data;
            }

            public void setData(int[] data) {
                this.data = data;
            }

            public int[] getCount() {
                return count;
            }

            public void setCount(int[] count) {
                this.count = count;
            }
        };
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadEmpty() throws IOException {
        tucsonSimple.load(mockReader);
    }

    @Test
    public void testMake6digitCodeSampleWithId() throws IOException {
        sample.getMeta().put("id", "123456");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);
    }

    @Test
    public void testMake6digitCodeSampleWithoutId() throws IOException {
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testSaveRowHeaderYearBeforeStart() throws IOException {
        Year year = new Year(1998);
        when(mockWriter.write(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        tucsonSimple.saveRowHeader(mockWriter, sample, "123456", 6, year);

        verify(mockWriter).write("1234561998");
    }

    @Test
    public void testSaveRowHeaderYearAfterEnd() throws IOException {
        Year year = new Year(2001);
        when(mockWriter.write(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        tucsonSimple.saveRowHeader(mockWriter, sample, "123456", 6, year);

        verify(mockWriter, never()).write("1234562001");
    }

    @Test
    public void testSaveDataNoProcessed() throws IOException {
        when(mockWriter.write(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockWriter.newLine()).thenAnswer(invocation -> {});

        tucsonSimple.saveData(sample, mockWriter);

        String expected = "1234561999 1\n" +
                          "1234562000 2\n" +
                          "1234562001 3\n" +
                          "1234562002 4\n";
        verify(mockWriter).write(eq(expected));
    }

    @Test
    public void testSaveDataProcessed() throws IOException {
        when(mockWriter.write(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockWriter.newLine()).thenAnswer(invocation -> {});

        sample.setProcessed(true);

        tucsonSimple.saveData(sample, mockWriter);

        String expected = "1234561999    \n" +
                          "1234562000    \n" +
                          "1234562001    \n" +
                          "1234562002    \n" +
                          "9990   0\n";
        verify(mockWriter).write(eq(expected));
    }

    @Test
    public void testSaveDataProcessedEndOfFile() throws IOException {
        when(mockWriter.write(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockWriter.newLine()).thenAnswer(invocation -> {});

        sample.setProcessed(true);

        tucsonSimple.saveData(sample, mockWriter);

        String expected = "1234561999    \n" +
                          "1234562000    \n" +
                          "1234562001    \n" +
                          "1234562002    \n" +
                          "9990   0\n" +
                          "\n";
        verify(mockWriter).write(eq(expected));
    }
}