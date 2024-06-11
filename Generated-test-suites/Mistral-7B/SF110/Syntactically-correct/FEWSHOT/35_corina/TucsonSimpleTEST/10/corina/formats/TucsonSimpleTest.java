package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.Sample;
import corina.formats.I18n;
import corina.formats.StringUtils;
import corina.ui.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

    @Test
    public void testComputeRange() throws IOException {
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123");
        Year start = new Year(1990);
        sample.range = new Range(start, start.add(1));

        TucsonSimple tucson = new TucsonSimple();
        Range expectedRange = new Range(start, start.add(1));
        Range resultRange = tucson.computeRange(sample);

        assertEquals(expectedRange.getStart(), resultRange.getStart());
        assertEquals(expectedRange.getEnd(), resultRange.getEnd());

        sample.range = new Range(new Year(2022), new Year(2022));
        expectedRange = new Range(new Year(2022).add(8000), new Year(2022).add(8000));
        resultRange = tucson.computeRange(sample);

        assertEquals(expectedRange.getStart(), resultRange.getStart());
        assertEquals(expectedRange.getEnd(), resultRange.getEnd());

        sample.range = new Range(new Year(1975), new Year(1975));
        expectedRange = new Range(new Year(1001), new Year(1001));
        resultRange = tucson.computeRange(sample);

        assertEquals(expectedRange.getStart(), resultRange.getStart());
        assertEquals(expectedRange.getEnd(), resultRange.getEnd());

        sample.meta.put("id", null);
        assertThrows(WrongFiletypeException.class, () -> tucson.computeRange(sample));
    }

    @Test
    public void testSaveData() throws IOException {
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.data = Arrays.asList(1.0, 2.0, 3.0);
        sample.count = Arrays.asList(1, 2, 3);
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123");
        Year start = new Year(1990);
        sample.range = new Range(start, start.add(1));

        I18n i18n = mock(I18n.class);
        when(i18n.getText(String.class, String.class)).thenReturn("format.tucsonsimple");
        when(i18n.getText(String.class, String.class, String.class)).thenReturn("format.tucsonsimple.range");

        StringUtils stringUtils = mock(StringUtils.class);
        when(stringUtils.leftPad(String.class, int.class, String.class)).thenAnswer(a -> a.getArgument(0));

        TucsonSimple tucson = new TucsonSimple();
        tucson.i18n = i18n;
        tucson.stringUtils = stringUtils;

        tucson.saveData(sample, writer);

        String expectedFileContent = "format.tucsonsimple\n"
                + "123\n"
                + "1990 1990\n"
                + "1.0 1\n"
                + "2.0 2\n"
                + "3.0 3\n"
                + "1 3\n"
                + "\n";

        verify(writer).write(tucson.toString());
        verify(writer).write(sample.meta.get("id").toString());
        verify(writer).write("\n");
        verify(writer).write("format.tucsonsimple.range\n");
        verify(writer).write("1990 ");
        verify(writer).write("1990\n");
        verify(writer).write("1.0 1\n");
        verify(writer).write("2.0 2\n");
        verify(writer).write("3.0 3\n");
        verify(writer).write("1 3\n");
        verify(writer).write("\n");
    }

    @Test
    public void testSaveDataWithProcessedData() throws IOException {
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        sample.data = Arrays.asList(1.0, 2.0, 3.0);
        sample.count = Arrays.asList(1, 2, 3);
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123");
        Year start = new Year(1990);
        sample.range = new Range(start, start.add(1));

        I18n i18n = mock(I18n.class);
        when(i18n.getText(String.class, String.class)).thenReturn("format.tucsonsimple");
        when(i18n.getText(String.class, String.class, String.class)).thenReturn("format.tucsonsimple.range");

        StringUtils stringUtils = mock(StringUtils.class);
        when(stringUtils.leftPad(String.class, int.class, String.class)).thenAnswer(a -> a.getArgument(0));

        TucsonSimple tucson = new TucsonSimple();
        tucson.i18n = i18n;
        tucson.stringUtils = stringUtils;

        tucson.saveData(sample, writer, true);

        String expectedFileContent = "format.tucsonsimple\n"
                + "123\n"
                + "1990 1990\n"
                + "   \n"
                + "   \n"
                + "   \n"
                + "1 3\n"
                + "\n";

        verify(writer).write(tucson.toString());
        verify(writer).write(sample.meta.get("id").toString());
        verify(writer).write("\n");
        verify(writer).write("format.tucsonsimple.range\n");
        verify(writer).write("1990 ");
        verify(writer).write("1990\n");
        verify(writer, never()).write("1.0 1\n");
        verify(writer, never()).write("2.0 2\n");
        verify(writer, never()).write("3.0 3\n");
    }

    @Test
    public void testLoad() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(
                "format.tucsonsimple\n"
                        + "123\n"
                        + "1990 1990\n"
                        + "1.0 1\n"
                        + "2.0 2\n"
                        + "3.0 3\n"
                        + "1 3\n"));
        Sample expectedSample = new Sample();
        expectedSample.data = Arrays.asList(1.0, 2.0, 3.0);
        expectedSample.count = Arrays.asList(1, 2, 3);
        expectedSample.meta = new HashMap<>();
        expectedSample.meta.put("id", "123");
        Year start = new Year(1990);
        expectedSample.range = new Range(start, start.add(1));

        TucsonSimple tucson = new TucsonSimple();
        Sample resultSample = tucson.load(reader);

        assertEquals(expectedSample.getData(), resultSample.getData());
        assertEquals(expectedSample.getCount(), resultSample.getCount());
        assertEquals(expectedSample.getMeta(), resultSample.getMeta());
        assertEquals(expectedSample.getRange(), resultSample.getRange());
    }

    @Test
    public void testSave() throws IOException {
        BufferedWriter writer = new FileWriter("output.txt");
        Sample sample = new Sample();
        sample.data = Arrays.asList(1.0, 2.0, 3.0);
        sample.count = Arrays.asList(1, 2, 3);
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123");
        Year start = new Year(1990);
        sample.range = new Range(start, start.add(1));

        TucsonSimple tucson = new TucsonSimple();
        tucson.save(sample, writer);

        Sample loadedSample = new Sample();
        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        loadedSample = tucson.load(reader);

        assertEquals(sample.getData(), loadedSample.getData());
        assertEquals(sample.getCount(), loadedSample.getCount());
        assertEquals(sample.getMeta(), loadedSample.getMeta());
        assertEquals(sample.getRange(), loadedSample.getRange());
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123");

        TucsonSimple tucson = new TucsonSimple();
        String code = tucson.make6digitCode(sample);

        assertEquals("123", code);

        sample.meta.put("id", null);
        assertNull(tucson.make6digitCode(sample));
    }
}