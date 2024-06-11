package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.WrongFiletypeException;
import corina.util.Sample;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedWriter mockBufferedWriter;
    private BufferedReader mockBufferedReader;

    @Before
    public void setup() {
        mockBufferedWriter = mock(BufferedWriter.class);
        mockBufferedReader = mock(BufferedReader.class);
        tucsonSimple = new TucsonSimple() {
            @Override
            public Sample load(BufferedReader reader) throws IOException {
                Sample sample = new Sample();
                sample.meta = new HashMap<>();
                sample.meta.put("id", "sampleId");
                sample.data = Arrays.asList(1, 2, 3, 4, 5);
                sample.count = Arrays.asList(1, 2, 3, 4, 5);
                sample.range = new YearRange(new Year(2000), new Year(2004));
                return sample;
            }
        };
        I18n.setInstance(mock(I18n.class));
    }

    @Test
    public void testToString() {
        String result = tucsonSimple.toString();
        String expected = "format.tucsonsimple";
        assertEquals(expected, result);
        verify(I18n, times(1)).getText(ArgumentMatchers.eq("format.tucsonsimple"));
    }

    @Test
    public void testgetDefaultExtension() {
        String result = tucsonSimple.getDefaultExtension();
        String expected = ".RWL";
        assertEquals(expected, result);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadThrowsException() throws IOException {
        tucsonSimple.load(mockBufferedReader);
    }

    @Test
    public void testMake6digitCodeWithId() {
        String result = tucsonSimple.make6digitCode(new Sample());
        String expected = "sampleId";
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        String result = tucsonSimple.make6digitCode(new Sample());
        String expected = "000000";
        assertEquals(expected, result);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        when(mockBufferedWriter.write(ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            System.out.println("Written: " + invocation.getArgument(0));
            return invocation.getArgument(0);
        });

        tucsonSimple.saveRowHeader(mockBufferedWriter, new Sample(), "sampleCode", 6, new Year(2000));
        String expected = "sampleCode2000";
        verify(mockBufferedWriter, times(1)).write(expected);

        tucsonSimple.saveRowHeader(mockBufferedWriter, new Sample(), "sampleCode", 4, new Year(2000));
        expected = "sampleCode   ";
        verify(mockBufferedWriter, times(1)).write(expected);
    }

    @Test
    public void testSaveData() throws IOException {
        when(mockBufferedWriter.write(ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            System.out.println("Written: " + invocation.getArgument(0));
            return invocation.getArgument(0);
        });

        Year start = new Year(2001);
        Year end = new Year(2004);
        String code = "sampleCode";

        tucsonSimple.saveData(new Sample(), mockBufferedWriter);

        // First row
        String expectedFirstRow = "sampleCode2001    ";
        verify(mockBufferedWriter, times(1)).write(expectedFirstRow);

        // Data rows
        for (int i = 0; i < 5; i++) {
            Year year = new Year(i + 2001);
            String data = String.valueOf(i + 1);
            String expectedDataRow = " " + data + "   ";
            verify(mockBufferedWriter, times(1)).write(expectedDataRow);
        }

        // Last row
        verify(mockBufferedWriter, times(1)).write("   999");
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.range = new YearRange(new Year(1999), new Year(2000));

        YearRange result = tucsonSimple.computeRange(sample);
        Year start = new Year(1001);
        Year end = new Year(1001);
        YearRange expected = new YearRange(start, end);

        assertEquals(expected.getStart(), result.getStart());
        assertEquals(expected.getEnd(), result.getEnd());
    }

    @Test
    public void testSave() throws IOException {
        when(mockBufferedWriter.write(ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            System.out.println("Written: " + invocation.getArgument(0));
            return invocation.getArgument(0);
        });
        when(mockBufferedReader.readLine()).thenReturn("Sample data");

        String input = "Sample data\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        String expectedOutput = "sampleCode2001  1\n"
                + "sampleCode2002  2\n"
                + "sampleCode2003  3\n"
                + "sampleCode2004  4\n"
                + "   999";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileWriter fileWriter = new FileWriter(new OutputStreamWriter(outputStream));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        tucsonSimple.save(new Sample(), bufferedWriter);

        String actualOutput = outputStream.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}