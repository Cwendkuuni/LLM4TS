package com.google.common.base; // replace with your package if necessary

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SplitterTest {
    

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        String input = "test-1-test-2-test-3";
        List<String> expected = new ArrayList<String>() {{
            add("test");
            add("1");
            add("test");
            add("2");
            add("test");
            add("3");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testOnCharNullInput() {
        Splitter splitter = Splitter.on('-');
        CharSequence input = null;
        splitter.split(input);
    }

    @Test
    public void testOnCharEmptyString() {
        Splitter splitter = Splitter.on('-');
        String input = "";
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(new ArrayList<String>(), result);
    }

    @Test
    public void testOnCharLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        String input = "test-1-test-2-test-3-test-4";
        List<String> expected = new ArrayList<String>() {{
            add("test");
            add("1");
            add("test-2");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOnPattern() {
        String separatorPattern = "\\s+";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        String input = "test 1 test 2 test 3";
        List<String> expected = new ArrayList<String>() {{
            add("test");
            add("1");
            add("test");
            add("2");
            add("test");
            add("3");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnPatternEmptyPattern() {
        String emptyPattern = "";
        Splitter splitter = Splitter.onPattern(emptyPattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnPatternEmptyInput() {
        String separatorPattern = "\\s+";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        CharSequence input = "";
        splitter.split(input);
    }

    @Test
    public void testOnPatternEmptyStringInSequence() {
        String separatorPattern = "\\s+";
        Splitter splitter = Splitter.onPattern(separatorPattern);
        String input = "test 1 test 2 test ";
        List<String> expected = new ArrayList<String>() {{
            add("test");
            add("1");
            add("test");
            add("2");
            add("");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOnPatternWithLimit() {
        String separatorPattern = "\\s+";
        Splitter splitter = Splitter.onPattern(separatorPattern).limit(2);
        String input = "test 1 test 2 test 3 test 4 test 5";
        List<String> expected = new ArrayList<String>() {{
            add("test");
            add("1");
            add("test-2");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("/");
        String input = "test/1/test/2/test/3";
        List<String> expected = new ArrayList<String>() {{
            add("test");
            add("1");
            add("test");
            add("2");
            add("test");
            add("3");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOnStringEmptyString() {
        Splitter splitter = Splitter.on("/");
        String input = "";
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(new ArrayList<String>(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnStringEmptyPattern() {
        Splitter splitter = Splitter.on("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnStringNullInput() {
        Splitter splitter = Splitter.on("/");
        CharSequence input = null;
        splitter.split(input);
    }

    @Test
    public void testOnStringWithLimit() {
        Splitter splitter = Splitter.on("/").limit(2);
        String input = "test/1/test/2/test/3/test/4";
        List<String> expected = new ArrayList<String>() {{
            add("test");
            add("1");
            add("test/2");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        String input = "test123test456test789test123";
        List<String> expected = new ArrayList<String>() {{
            add("test12");
            add("test45");
            add("test78");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFixedLengthWithEmptyString() {
        Splitter splitter = Splitter.fixedLength(3);
        String input = "test123test456test";
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(new ArrayList<String>() {{
            add("test123");
            add("test456");
            add("");
        }}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixedLengthNegativeLength() {
        Splitter splitter = Splitter.fixedLength(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixedLengthZeroLength() {
        Splitter splitter = Splitter.fixedLength(0);
    }

    @Test
    public void testOmmitEmptyStrings() {
        Splitter splitter = Splitter.on("/").omitEmptyStrings();
        String input = "test/1/test/2//test/3//test/4/test/5";
        List<String> expected = new ArrayList<String>() {{
            add("test/1");
            add("test/2");
            add("test/3");
            add("test/4");
            add("test/5");
        }};
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOmmitEmptyStringsEmptyString() {
        Splitter splitter = Splitter.on("/").omitEmptyStrings();
        String input = "";
        List<String> result = new ArrayList<>(splitter.split(input));
        Assert.assertEquals(new ArrayList<String>(), result);
    }

    @Test
    public void testMapSplitter() {
        String keyValueSeparator = ":";
        Splitter outerSplitter = Splitter.on(",");
        Splitter keyValueSplitter = Splitter.on(keyValueSeparator);
        Map<String, String> expected = new LinkedHashMap<String, String>() {{
            put("test1", "value1");
            put("test2", "value2");
            put("test3", "value3");
        }};
        CharSequence input = "test1,value1:test2,value2:test3,value3";
        Map<String, String> result = outerSplitter.withKeyValueSeparator(keyValueSplitter).split(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMapSplitterEmptyString() {
        String keyValueSeparator = ":";
        Splitter outerSplitter = Splitter.on(",");
        Splitter keyValueSplitter = Splitter.on(keyValueSeparator);
        Map<String, String> expected = new LinkedHashMap<String, String>();
        CharSequence input = "";
        Map<String, String> result = outerSplitter.withKeyValueSeparator(keyValueSplitter).split(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitterNullInput() {
        String keyValueSeparator = ":";
        Splitter outerSplitter = Splitter.on(",");
        Splitter keyValueSplitter = Splitter.on(keyValueSeparator);
        MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(keyValueSplitter);
        CharSequence input = null;
        mapSplitter.split(input);
    }

    @Test
    public void testMapSplitterEmptyKeyValueSeparator() {
        String keyValueSeparator = "";
        Splitter outerSplitter = Splitter.on(",");
        Splitter keyValueSplitter = Splitter.on(keyValueSeparator);
        Map<String, String> expected = new LinkedHashMap<String, String>();
        CharSequence input = "test1,,test2,,test3";
        Map<String, String> result = outerSplitter.withKeyValueSeparator(keyValueSplitter).split(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitterEmptyKey() {
        String keyValueSeparator = ":";
        Splitter outerSplitter = Splitter.on(",");
        Splitter keyValueSplitter = Splitter.on(keyValueSeparator);
        Map<String, String> expected = new LinkedHashMap<String, String>();
        CharSequence input = "test1,:test2,test3";
        Map<String, String> result = outerSplitter.withKeyValueSeparator(keyValueSplitter).split(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitterEmptyValue() {
        String keyValueSeparator = ":";
        Splitter outerSplitter = Splitter.on(",");
        Splitter keyValueSplitter = Splitter.on(keyValueSeparator);
        Map<String, String> expected = new LinkedHashMap<String, String>();
        CharSequence input = "test1,test2,";
        Map<String, String> result = outerSplitter.withKeyValueSeparator(keyValueSplitter).split(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitterDuplicateKey() {
        String keyValueSeparator = ":";
        Splitter outerSplitter = Splitter.on(",");
        Splitter keyValueSplitter = Splitter.on(keyValueSeparator);
        Map<String, String> expected = new LinkedHashMap<String, String>() {{
            put("test1", "value1");
            put("test2", "value2");
        }};
        CharSequence input = "test1,value1:test1,value2";
        Map<String, String> result = outerSplitter.withKeyValueSeparator(keyValueSplitter).split(input);
        Assert.assertEquals(expected, result);
    }

    
}