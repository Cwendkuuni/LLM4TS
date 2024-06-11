package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.formats.TucsonSimple.Year;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.YearUtils;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testgetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad_ThrowsWrongFiletypeException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123456");
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.clear();
        expected = "000000";
        actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.meta.put("id", "1234567890");
        expected = "123456";
        actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.meta.put("id", "12345678");
        expected = "123456";
        actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.meta.put("id", "123456");
        expected = "123456";
        actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new ByteArrayOutputStream());
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123456");
        String code = "123456";
        int yearWidth = 6;
        Year year = YearUtils.fromString("2000");
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        String expected = "1234562000";
        String actual = writer.toString();
        assertEquals(expected, actual);

        year = YearUtils.fromString("1999");
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        expected = "1234561999";
        actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new ByteArrayOutputStream());
        Sample sample = new Sample();
        sample.range = new Range(YearUtils.fromString("1995"), YearUtils.fromString("1996"));
        sample.data = new double[sample.range.getSize()];
        sample.count = new int[sample.range.getSize()];
        String code = "123456";
        boolean isSummed = false;
        boolean isProcessed = false;
        Year start = YearUtils.fromString("1995");
        Year end = YearUtils.fromString("1996");
        writer.write("1234561995");
        writer.newLine();
        tucsonSimple.saveData(sample, writer);
        String expected = "1234561995\n";
        String actual = writer.toString();
        assertEquals(expected, actual);

        writer.reset();
        writer.write("1234561995");
        writer.newLine();
        tucsonSimple.saveData(sample, writer);
        writer.write("9990  0");
        writer.newLine();
        expected = "1234561995\n9990  0";
        actual = writer.toString();
        assertEquals(expected, actual);

        writer.reset();
        sample.data[0] = 1.2;
        sample.count[0] = 2;
        tucsonSimple.saveData(sample, writer);
        writer.write("1.2  2");
        writer.newLine();
        expected = "1234561995\n9990  0\n1.2  2";
        actual = writer.toString();
        assertEquals(expected, actual);

        writer.reset();
        isSummed = true;
        tucsonSimple.saveData(sample, writer);
        writer.write("1.2  2\n1.2");
        writer.newLine();
        expected = "1234561995\n9990  0\n1.2  2\n1.2";
        actual = writer.toString();
        assertEquals(expected, actual);

        writer.reset();
        isProcessed = true;
        tucsonSimple.saveData(sample, writer);
        writer.write("     ");
        writer.newLine();
        expected = "1234561995\n9990    \n";
        actual = writer.toString();
        assertEquals(expected, actual);

        writer.reset();
        sample.range = new Range(YearUtils.fromString("1995"), YearUtils.fromString("1997"));
        tucsonSimple.saveData(sample, writer);
        writer.write("1234561995\n9990    \n999\n");
        expected = "1234561995\n9990    \n999\n";
        actual = writer.toString();
        assertEquals(expected, actual);

        writer.reset();
        writer.write("1234561995\n9990    \n999\n");
        tucsonSimple.saveData(sample, writer);
        writer.write("9990   ");
        writer.newLine();
        expected = "1234561995\n9990    \n999\n9990   ";
        actual = writer.toString();
        assertEquals(expected, actual);

        writer.reset();
        isProcessed = true;
        writer.write("1234561995\n9990    \n999\n9990   ");
        tucsonSimple.saveData(sample, writer);
        writer.write("    ");
        writer.newLine();
        expected = "1234561995\n9990    \n999\n9990   \n    ";
        actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(YearUtils.fromString("1995"), YearUtils.fromString("1996"));
        Range expected = sample.range;
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.range = new Range(YearUtils.fromString("1895"), YearUtils.fromString("1896"));
        expected = new Range(YearUtils.fromString("1001"), YearUtils.fromString("1001"));
        actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.range = new Range(YearUtils.fromString("1995"), YearUtils.fromString("1895"));
        expected = new Range(YearUtils.fromString("1001"), YearUtils.fromString("1001"));
        actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.range = new Range(YearUtils.fromString("1995"), YearUtils.fromString("1995"));
        expected = new Range(YearUtils.fromString("1001"), YearUtils.fromString("1001"));
        actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.range = new Range(YearUtils.fromString("1995"), YearUtils.fromString("1996"));
        expected = new Range(YearUtils.fromString("1"), YearUtils.fromString("1"));
        actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);

        sample = new Sample();
        sample.range = new Range(YearUtils.fromString("1995"), YearUtils.fromString("1996"));
        expected = new Range(YearUtils.fromString("8001"), YearUtils.fromString("8001"));
        actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new ByteArrayOutputStream());
        Sample sample = new Sample();
        sample.range = new Range(YearUtils.fromString("1995"), YearUtils.fromString("1996"));
        sample.data = new double[sample.range.getSize()];
        sample.data[0] = 1.2;
        sample.count = new int[sample.range.getSize()];
        sample.count[0] = 2;
        String code = "123456";
        tucsonSimple.save(sample, writer);
        String expected = "1234561995\n9990  0\n1.2  2\n";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

}