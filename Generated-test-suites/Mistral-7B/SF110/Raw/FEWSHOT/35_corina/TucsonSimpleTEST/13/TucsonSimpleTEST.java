package corina.formats.test;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

import corina.formats.Filetype;
import corina.formats.Sample;
import corina.formats.TucsonSimple;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.setMeta(Arrays.asList("id", "2010"));
        Year start = new Year(2005);
        Year end = new Year(2007);
        Range expected = new Range(start, end);
        Range actual = new TucsonSimple().computeRange(sample);
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());

        sample.setMeta(Arrays.asList("id", "50000"));
        start = new Year(50005);
        end = new Year(50010);
        expected = new Range(start, end);
        actual = new TucsonSimple().computeRange(sample);
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());

        sample.setMeta(Arrays.asList("id", "123456"));
        start = new Year(1999);
        expected = new Range(start, start.add(7999));
        actual = new TucsonSimple().computeRange(sample);
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        String expected = "000000";
        String actual = new TucsonSimple().make6digitCode(sample);
        assertEquals(expected, actual);

        sample.setMeta(Arrays.asList("id", "abc123"));
        expected = "abc123";
        actual = new TucsonSimple().make6digitCode(sample);
        assertEquals(expected, actual);

        sample.setMeta(Arrays.asList("id", "12345"));
        expected = "12345";
        actual = new TucsonSimple().make6digitCode(sample);
        assertEquals(expected, actual);

        sample.setMeta(Arrays.asList("id", "1234567"));
        expected = "123456";
        actual = new TucsonSimple().make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        sample.setData(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
        sample.setCount(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L));
        sample.setMeta(Arrays.asList("id", "year1"));

        StringWriter sw = new StringWriter();
        BufferedWriter w = new BufferedWriter(sw);
        new TucsonSimple().saveData(sample, w);
        String expected = "1  1\n" +
                          "2  2\n" +
                          "3  3\n" +
                          "4  4\n" +
                          "5  5\n" +
                          "6  6\n" +
                          "7  7\n" +
                          "8  8\n" +
                          "9  9\n";
        String actual = sw.toString();
        assertEquals(expected, actual);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        w = new BufferedWriter(baos);
        new TucsonSimple().saveData(sample, w);
        byte[] expectedBytes = Arrays.copyOfRange(expected.getBytes(), 0, expected.length());
        byte[] actualBytes = baos.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }
}