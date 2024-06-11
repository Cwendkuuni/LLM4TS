package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        assertEquals("format.tucsonsimple", result);
    }

    @Test
    public void testgetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(".RWL", result);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadThrowsWrongFiletypeException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("Invalid data".getBytes())));
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCodeWithId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", result);
    }

    @Test
    public void testMake6digitCodeWithoutId() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String result = tucsonSimple.make6digitCode(sample);
        assertEquals("000000", result);
    }

    @Test
    public void testSaveRowHeaderWithStartBeforeYear() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        String code = "123456";
        int yearWidth = 6;
        Year start = new Year(-1);
        Year end = new Year(2022);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
        String expected = "123456-";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testSaveRowHeaderWithStartAfterYear() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        String code = "123456";
        int yearWidth = 6;
        Year start = new Year(2022);
        Year end = new Year(2022);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
        String expected = "1234562022";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testSaveDataWithIsSummedFalseAndIsProcessedFalse() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        Range range = new Range(new Year(2022), new Year(2022));
        Year start = range.getStart();
        Year end = range.getEnd();
        String code = "123456";
        tucsonSimple.saveData(sample, writer);
        String expected = "1234562022\n";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testSaveDataWithIsSummedFalseAndIsProcessedTrue() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        Range range = new Range(new Year(2022), new Year(2022));
        Year start = range.getStart();
        Year end = range.getEnd();
        String code = "123456";
        Year startYear = new Year(2021);
        sample.range = new Range(startYear, start);
        tucsonSimple.saveData(sample, writer);
        String expected = "123456   \n";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testSaveDataWithIsSummedTrue() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        Range range = new Range(new Year(2022), new Year(2022));
        Year start = range.getStart();
        Year end = range.getEnd();
        String code = "123456";
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        sample.data = data;
        tucsonSimple.saveData(sample, writer);
        String expected = "1234562022\n9990  1.0\n";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testComputeRangeWithStartBeforeYear1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = tucsonSimple.computeRange(sample);
        Year expectedStart = new Year(1001);
        Year expectedEnd = new Year(1001);
        assertEquals(expectedStart, range.getStart());
        assertEquals(expectedEnd, range.getEnd());
    }

    @Test
    public void testComputeRangeWithStartAfterYear8000() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = tucsonSimple.computeRange(sample);
        Year expectedStart = new Year(9001);
        Year expectedEnd = new Year(9001);
        assertEquals(expectedStart, range.getStart());
        assertEquals(expectedEnd, range.getEnd());
    }

    @Test
    public void testComputeRangeWithStartBetweenYear1AndYear8000() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = tucsonSimple.computeRange(sample);
        Year expectedStart = new Year(1001);
        Year expectedEnd = new Year(1002);
        assertEquals(expectedStart, range.getStart());
        assertEquals(expectedEnd, range.getEnd());
    }

    @Test
    public void testSaveThrowsWrongFiletypeException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        tucsonSimple.save(sample, writer);
        fail("save method should throw WrongFiletypeException");
    }

    @Test
    public void testSaveWithValidData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
        Range range = new Range(new Year(2022), new Year(2022));
        Year start = range.getStart();
        Year end = range.getEnd();
        String code = "123456";
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        sample.data = data;
        tucsonSimple.save(sample, writer);
        String expected = "1234562022\n9990  1.0\n";
        assertEquals(expected, writer.toString());
    }
}