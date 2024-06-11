package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testComputeRange() {
        Sample sample = createSample(1995, "sampleData");
        Year start = new Year(1995);
        Range expected = new Range(start, start.add(1));

        Range actual = new TucsonSimple().computeRange(sample);
        assertEquals(expected, actual);

        // Test range redating
        sample.setRange(new Range(new Year(2001), new Year(2002)));
        expected = new Range(new Year(2001), new Year(2002).add(8000));
        actual = new TucsonSimple().computeRange(sample);
        assertEquals(expected, actual);

        // Test range start redating
        sample.setRange(new Range(new Year(-5), new Year(1000)));
        expected = new Range(new Year(1001), new Year(1001));
        actual = new TucsonSimple().computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = createSample(2000, "sampleData");
        BufferedWriter writer = mock(BufferedWriter.class);

        // Test null writer
        tucsonSimple.saveData(sample, null);

        // Test empty data
        when(writer.write(anyString())).thenAnswer(i -> i.getArgument(0));
        tucsonSimple.saveData(sample, writer);
        verify(writer, never()).newLine();

        // Test valid data
        String expected = "000001 2000\n";
        when(writer.write(anyString())).thenAnswer(i -> i.getArgument(0));
        when(writer.newLine()).thenReturn(true);
        tucsonSimple.saveData(sample, writer);
        verify(writer).write(expected);
        verify(writer, times(2)).newLine();

        // Test invalid data
        sample.setData(new int[]{-1, 2, 3, 4, 5, 6});
        expected = "000001 2000\n999 999999\n5 4 3 2 1 6\n";
        tucsonSimple.saveData(sample, writer);
        verify(writer).write(expected);
        verify(writer, times(3)).newLine();
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("sampleData", new TucsonSimple().stripLeadingAndTrailingQuotes("\"sampleData\""));
        assertEquals("foo", new TucsonSimple().stripLeadingAndTrailingQuotes("foo"));
        assertNull(new TucsonSimple().stripLeadingAndTrailingQuotes(null));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = createSample(2000, "sampleData");
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("testFile.txt")));
        String code = "000001";

        // Test null writer
        tucsonSimple.saveRowHeader(null, sample, code, 6, new Year(2000));

        // Test invalid year width
        tucsonSimple.saveRowHeader(writer, sample, code, -1, new Year(2000));

        // Test valid data
        tucsonSimple.saveRowHeader(writer, sample, code, 6, new Year(2000));
        assertEquals("000001 2000\n", readFile("testFile.txt"));
        writer.close();
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = createSample(null, "sampleData");
        assertEquals("000000", new TucsonSimple().make6digitCode(sample));

        sample = createSample(new Year(1995), "sampleData");
        assertEquals("000001", new TucsonSimple().make6digitCode(sample));

        sample = createSample(new Year(1995), "sampleData");
        sample.setMeta(new HashMap<>());
        sample.getMeta().put("id", "testID");
        assertEquals("testID", new TucsonSimple().make6digitCode(sample));
    }

    @Test(expected = WrongFiletypeException.class)
    public void testload() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        tucsonSimple.load(reader);
    }

    private Sample createSample(Year year, String data) {
        Sample sample = mock(Sample.class);
        when(sample.getData()).thenReturn(new int[]{1, 2, 3, 4, 5, 6});
        when(sample.getRange()).thenReturn(new Range(new Year(year), new Year(year).add(1)));
        when(sample.count).thenReturn(new int[7]);
        return sample;
    }

    private String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }
}