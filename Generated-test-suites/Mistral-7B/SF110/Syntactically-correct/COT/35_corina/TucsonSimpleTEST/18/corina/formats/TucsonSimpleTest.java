package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

	@Test
	public void testToString() {
		TucsonSimple tucsonSimple = new TucsonSimple();
		String expected = "format.tucsonsimple";
		String actual = tucsonSimple.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetDefaultExtension() {
		TucsonSimple tucsonSimple = new TucsonSimple();
		String expected = ".RWL";
		String actual = tucsonSimple.getDefaultExtension();
		assertEquals(expected, actual);
	}

	@Test(expected = WrongFiletypeException.class)
	public void testLoadMethodThrowsWrongFiletypeException() throws IOException {
		TucsonSimple tucsonSimple = new TucsonSimple();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
		tucsonSimple.load(reader);
	}

	@Test
	public void testMake6digitCodeWithSampleId() {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		sample.meta.put("id", "123456");
		String expected = "123456";
		String actual = tucsonSimple.make6digitCode(sample);
		assertEquals(expected, actual);
	}

	@Test
	public void testMake6digitCodeWithoutSampleId() {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		String expected = "000000";
		String actual = tucsonSimple.make6digitCode(sample);
		assertEquals(expected, actual);
	}

	@Test
	public void testSaveRowHeaderWithStartYearBeforeSampleYear() throws IOException {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
		Year start = new Year(1990);
		String code = "123456";
		int yearWidth = 4;
		tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, new Year(1989));
		String expected = "1234561989";
		String actual = writer.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testSaveRowHeaderWithStartYearAfterSampleYear() throws IOException {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
		Year start = new Year(1992);
		String code = "123456";
		int yearWidth = 6;
		tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, new Year(1991));
		String expected = "1234561991";
		String actual = writer.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testSaveDataWithIsProcessedFalse() throws IOException {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
		Year start = new Year(1995);
		Year end = new Year(1997);
		String code = "123456";
		int yearWidth = 6;
		Map<Integer, String> data = new HashMap<>();
		data.put(3, "10.5");
		sample.setData(data);
		tucsonSimple.saveData(sample, writer);
		String expected = "1234561995  10.5" + System.lineSeparator();
		String actual = writer.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testSaveDataWithIsProcessedTrue() throws IOException {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
		Range range = new Range(new Year(1985), new Year(1988));
		sample.setRange(range);
		String code = "123456";
		int yearWidth = 4;
		Map<Integer, String> data = new HashMap<>();
		Map<Integer, Double> count = new HashMap<>();
		data.put(0, "5.2");
		count.put(0, 1.5);
		sample.setData(data);
		sample.setCount(count);
		tucsonSimple.saveData(sample, writer, true);
		String expected = "123456   9990  0" + System.lineSeparator() +
				"123456   9990   " + System.lineSeparator() +
				"123456   5.2   1.5" + System.lineSeparator() +
				"123456   ";
		String actual = writer.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testComputeRangeWithValidStartYear() {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		Year start = new Year(2000);
		Range expected = new Range(start, start);
		Range actual = tucsonSimple.computeRange(sample);
		assertEquals(expected, actual);
	}

	@Test
	public void testComputeRangeWithStartYearBeforeYear1() {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		Year start = new Year(-1000);
		Range expected = new Range(new Year(1001), new Year(1001));
		Range actual = tucsonSimple.computeRange(sample);
		assertEquals(expected, actual);
	}

	@Test
	public void testComputeRangeWithStartYearAfterYear8000() {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		Year start = new Year(8001);
		Range expected = new Range(new Year(8001), new Year(8001));
		Range actual = tucsonSimple.computeRange(sample);
		assertEquals(expected, actual);
	}

	@Test
	public void testSaveMethod() throws IOException {
		TucsonSimple tucsonSimple = new TucsonSimple();
		Sample sample = new Sample();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
		Year start = new Year(1990);
		Year end = new Year(1992);
		String code = "123456";
		int yearWidth = 6;
		Map<Integer, String> data = new HashMap<>();
		data.put(0, "10.5");
		sample.setData(data);
		tucsonSimple.save(sample, writer);
		String expected = "1234561990  10.5" + System.lineSeparator() +
				"1234561991   " + System.lineSeparator() +
				"1234561992   ";
		String actual = writer.toString();
		assertEquals(expected, actual);
	}

}