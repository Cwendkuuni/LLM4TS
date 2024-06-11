package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.base.Splitter.SplittingIterator;

public class SplitterTest {

	@Test
	public void testOnChar() {
		Splitter splitter = Splitter.on('-');
		List<String> result = new ArrayList<String>(Arrays.asList("one-two-three".split("-")));
		List<String> expected = Arrays.asList("one", "two", "three");
		assertEquals(expected, result);
	}

	@Test
	public void testOnCharMatcher() {
		Splitter splitter = Splitter.on(CharMatcher.is('-'));
		List<String> result = new ArrayList<String>(Arrays.asList("one--two--three".split(CharMatcher.is('-'))));
		List<String> expected = Arrays.asList("one", "", "two", "", "three");
		assertEquals(expected, result);
	}

	@Test
	public void testOnString() {
		Splitter splitter = Splitter.on("-");
		List<String> result = new ArrayList<String>(Arrays.asList("one-two-three".split("-")));
		List<String> expected = Arrays.asList("one", "two", "three");
		assertEquals(expected, result);
	}

	@Test
	public void testOnPattern() {
		Splitter splitter = Splitter.on(Pattern.compile("-"));
		List<String> result = new ArrayList<String>(Arrays.asList("one--two--three".split(Pattern.compile("-"))));
		List<String> expected = Arrays.asList("one", "", "two", "", "three");
		assertEquals(expected, result);
	}

	@Test
	public void testOnPatternWithEmptyString() {
		Splitter splitter = Splitter.onPattern("[-]");
		List<String> result = new ArrayList<String>(Arrays.asList("one--two--three".split(Pattern.compile("[-]"))));
		List<String> expected = Arrays.asList("one", "", "two", "", "three");
		assertEquals(expected, result);
	}

	@Test
	public void testOnPatternWithEmptyStringAtBeginning() {
		Splitter splitter = Splitter.onPattern("[-]");
		List<String> result = new ArrayList<String>(Arrays.asList("--one--two--three".split(Pattern.compile("[-]"))));
		List<String> expected = Arrays.asList("", "one", "", "two", "", "three");
		assertEquals(expected, result);
	}

	@Test
	public void testOnPatternWithEmptyStringAtEnd() {
		Splitter splitter = Splitter.onPattern("[-]");
		List<String> result = new ArrayList<String>(Arrays.asList("one--two--"));
		List<String> expected = Arrays.asList("one", "two", "", "");
		assertEquals(expected, result);
	}

	@Test
	public void testOnPatternWithEmptyStringBetween() {
		Splitter splitter = Splitter.onPattern("[-]");
		List<String> result = new ArrayList<String>(Arrays.asList("one----two--three".split(Pattern.compile("[-]"))));
		List<String> expected = Arrays.asList("one", "", "two", "", "three");
		assertEquals(expected, result);
	}

	@Test
	public void testOnPatternWithEmptyStringBetweenAndAtEnd() {
		Splitter splitter = Splitter.onPattern("[-]");
		List<String> result = new ArrayList<String>(Arrays.asList("one----two--three--".split(Pattern.compile("[-]"))));
		List<String> expected = Arrays.asList("one", "", "two", "", "three", "", "");
		assertEquals(expected, result);
	}

	@Test
	public void testOnPatternWithEmptyStringAtBeginningAndBetween() {		
		Splitter splitter = Splitter.onPattern("[-]");
		List<String> result = new ArrayList<String>(Arrays.asList("--one--two--three--".split(Pattern.compile("[-]"))));
		List<String> expected = Arrays.asList("", "one", "", "two", "", "three", "", "");
		assertEquals(expected, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOnPatternWithEmptyPattern() {
		Splitter.onPattern("");
	}

	@Test
	public void testFixedLength() {
		Splitter splitter = Splitter.fixedLength(2);
		List<String> result = new ArrayList<String>(Arrays.asList("abcdefghijk".split(Pattern.compile("(.{2})"))));
		List<String> expected = Arrays.asList("ab", "cd", "ef", "gh", "ik");
		assertEquals(expected, result);
	}

	@Test
	public void testFixedLengthWithLimit() {
		List<String> result = new ArrayList<String>(Splitter.fixedLength(2).split("abcdefghijklmnopqrstuvwxyz".substring(0, 25)).limit(5));
		List<String> expected = Arrays.asList("ab", "cd", "ef", "gh", "ij");
		assertEquals(expected, result);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testFixedLengthWithLimitTooSmall() {
		List<String> result = new ArrayList<String>(Splitter.fixedLength(2).split("abcdefghijklmnopqrstuvwxyz".substring(0, 25)).limit(3));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testFixedLengthWithLimitLargerThanString() {
		List<String> result = new ArrayList<String>(Splitter.fixedLength(2).split("abcdefghijklmnopqrstuvwxyz".substring(0, 25)).limit(27));
	}

	@Test
	public void testOmitEmptyStrings() {
		Splitter splitter = Splitter.on("-").omitEmptyStrings();
		List<String> result = new ArrayList<String>(Arrays.asList("one--two--three--".split("-")));
		List<String> expected = Arrays.asList("one", "two", "three");
		assertEquals(expected, result);
	}

	@Test
	public void testLimit() {
		List<String> result = new ArrayList<String>(Splitter.on("-").split("one--two--three--four--five--six--seven--eight--nine--ten--eleven--twelve--thirteen--fourteen--fifteen--sixteen--seventeen--eighteen--nineteen--twenty".split("-")).limit(5));
		List<String> expected = Arrays.asList("one", "two", "three", "four", "five");
		assertEquals(expected, result);
	}

	@Test
	public void testMapSplitter() {
		MapSplitter mapSplitter = Splitter.on("-").withKeyValueSeparator(":");
		Map<String, String> result = mapSplitter.split("key1:value1:key2:value2:key3:value3:key4:value4:key5:value5");
		Map<String, String> expected = new LinkedHashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "value2");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};
		assertEquals(expected, result);
	}

	@Test
	public void testMapSplitterWithEmptyString() {
		MapSplitter mapSplitter = Splitter.on("-").withKeyValueSeparator(":");
		Map<String, String> result = mapSplitter.split("key1:value1:key2::key3:value3:key4:value4:key5:value5:");
		Map<String, String> expected = new LinkedHashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};
		assertEquals(expected, result);
	}

	@Test
	public void testMapSplitterWithEmptyStringAtBeginning() {
		MapSplitter mapSplitter = Splitter.on("-").withKeyValueSeparator(":");
		Map<String, String> result = mapSplitter.split("::key1:value1:key2:value2:key3:value3:key4:value4:key5:value5:");
		Map<String, String> expected = new LinkedHashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "value2");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};
		assertEquals(expected, result);
	}

	@Test
	public void testMapSplitterWithEmptyStringAtEnd() {
		MapSplitter mapSplitter = Splitter.on("-").withKeyValueSeparator(":");
		Map<String, String> result = mapSplitter.split("key1:value1:key2:value2:key3:value3:key4:value4:key5:value5:");
		Map<String, String> expected = new LinkedHashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "value2");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};
		assertEquals(expected, result);
	}

	@Test
	public void testMapSplitterWithEmptyStringBetween() {
		MapSplitter mapSplitter = Splitter.on("-").withKeyValueSeparator(":");
		Map<String, String> result = mapSplitter.split("key1:value1::key2:value2:key3:value3:key4:value4:key5:value5:");
		Map<String, String> expected = new LinkedHashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "value2");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};
		assertEquals(expected, result);
	}

	@Test
	public void testMapSplitterWithEmptyStringBetweenAndAtEnd() {
		MapSplitter mapSplitter = Splitter.on("-").withKeyValueSeparator(":");
		Map<String, String> result = mapSplitter.split("::key1:value1:key2:value2:key3:value3::key4:value4:key5:value5:");
		Map<String, String> expected = new LinkedHashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "value2");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};
		assertEquals(expected, result);
	}

	@Test
	public void testMapSplitterWithEmptyStringAtBeginningAndBetween() {
		MapSplitter mapSplitter = Splitter.on("-").withKeyValueSeparator(":");
		Map<String, String> result = mapSplitter.split("::key1:value1::key2:value2:key3:value3::key4:value4:key5:value5:");
		Map<String, String> expected = new LinkedHashMap<String, String>() {{
			put("key1", "value1");
			put("key2", "value2");
			put("key3", "value3");
			put("key4", "value4");
			put("key5", "value5");
		}};
		assertEquals(expected, result);
	}
}