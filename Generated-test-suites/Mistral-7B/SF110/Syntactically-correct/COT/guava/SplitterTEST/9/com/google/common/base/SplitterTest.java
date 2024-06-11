package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SplitterTest {
    

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('-');
        CharSequence sequence = "item1-item2-item3";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("item1", "item2", "item3");
        assertEquals(expected, result);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is('-'));
        CharSequence sequence = "item1-item2-item3";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("item1", "item2", "item3");
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("-");
        CharSequence sequence = "item1-item2-item3";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("item1", "item2", "item3");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on("[\\-]");
        CharSequence sequence = "item1--item2-item3";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("item1", "", "item2", "", "item3");
        assertEquals(expected, result);
    }

    @Test
    public void testOnFixedLengthSeparator() {
        Splitter splitter = Splitter.fixedLength(3);
        CharSequence sequence = "item1abc item2def item3ghi";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("item1abc", "item2def", "item3ghi");
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        CharSequence sequence = "item1:item2:item3:";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("item2");
        assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(2);
        CharSequence sequence = "item1:item2:item3:item4:item5";
        List<String> result = splitter.split(sequence);
        List<String> expected = Arrays.asList("item1:item2", "item3:item4");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSequence() {
        Splitter splitter = Splitter.on("-");
        splitter.split(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySequence() {
        Splitter splitter = Splitter.on("-");
        splitter.split(Character.toString(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        Splitter splitter = Splitter.on("-");
        CharSequence sequence = "item1-item2";
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next();
        splitter.splittingIterator(sequence).next(); // index out of bounds
    }

    @Test
    public void testMapSplitter() {
        Splitter keySplitter = Splitter.on(":");
        Splitter valueSplitter = Splitter.on("=");
        MapSplitter mapSplitter = new Splitter.MapSplitter(keySplitter, valueSplitter);
        CharSequence sequence = "key1:value1=item1,key2:value2=item2,key3:value3=item3";
        Map<String, String> result = mapSplitter.split(sequence);
        Map<String, String> expected = new LinkedHashMap<>() {{
            put("key1", "item1");
            put("key2", "item2");
            put("key3", "item3");
        }};
        assertEquals(expected, result);
    }

    
}