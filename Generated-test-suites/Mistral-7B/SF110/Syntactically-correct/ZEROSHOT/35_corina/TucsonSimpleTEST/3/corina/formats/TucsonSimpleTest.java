package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.Filetype;
import corina.formats.TucsonSimple;
import corina.formats.util.StringUtils;
import corina.ui.I18n;
import corina.util.Sample;
import corina.util.Util;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        Filetype filetype = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String actual = filetype.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultExtension() {
        Filetype filetype = new TucsonSimple();
        String expected = ".RWL";
        String actual = filetype.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        Filetype filetype = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("Invalid Data".getBytes())));
        Sample sample = filetype.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        Filetype filetype = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123456");
        String expected = "123456";
        String actual = filetype.make6digitCode(sample);
        assertEquals(expected, actual);

        Sample sample2 = new Sample();
        sample2.meta = new HashMap<>();
        String code = filetype.make6digitCode(sample2);
        assertEquals("000000", code);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        Filetype filetype = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123456");
        String code = "123456";
        int yearWidth = 4;
        Year year = new Year(1990);

        filetype.saveRowHeader(writer, sample, code, yearWidth, year);

        String expected = "1234561990";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException, ParseException {
        Filetype filetype = new TucsonSimple();
        Sample sample = new Sample();
        sample.data = new HashMap<>();
        sample.count = new HashMap<>();
        sample.meta = new HashMap<>();
        sample.range = new YearRange(new Year(1990), new Year(1991));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));

        Year start = new Year(1990);
        Year end = new Year(1991);
        String code = "123456";
        boolean isSummed = false;
        boolean isProcessed = false;

        filetype.saveData(sample, writer);

        String expected = "1234561990\n" +
                "   999\n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   \n" +
                "123456   999\n";

        String actual = writer.toString();
        assertEquals(expected, actual);

        // Test with isProcessed = true
        sample.range = new YearRange(new Year(1991), new Year(1992));
        filetype.saveData(sample, writer);

        expected = "1234561991\n" +
                "   9990  0\n" +
                "   999\n";

        actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRange() throws ParseException {
        Filetype filetype = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new YearRange(new Year(1001), new Year(1002));

        YearRange expected = new YearRange(new Year(1001), new Year(1001));
        YearRange actual = filetype.computeRange(sample);
        assertEquals(expected, actual);

        sample.range = new YearRange(new Year(-1), new Year(8000));
        expected = new YearRange(new Year(1), new Year(8001));
        actual = filetype.computeRange(sample);
        assertEquals(expected, actual);

        sample.range = new YearRange(new Year(1990), new Year(1990));
        expected = new YearRange(new Year(1991), new Year(1991));
        actual = filetype.computeRange(sample);
        assertEquals(expected, actual);

        sample.range = new YearRange(new Year(1990), new Year(1989));
        expected = new YearRange(new Year(1991), new Year(1990));
        actual = filetype.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSave() throws IOException {
        Filetype filetype = new TucsonSimple();
        Sample sample = new Sample();
        sample.data = new HashMap<>();
        sample.count = new HashMap<>();
        sample.meta = new HashMap<>();
        sample.range = new YearRange(new Year(1990), new Year(1991));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        filetype.save(sample, writer);

        // Test with isProcessed = true
        sample.range = new YearRange(new Year(1991), new Year(1992));
        filetype.save(sample, writer);
    }
}