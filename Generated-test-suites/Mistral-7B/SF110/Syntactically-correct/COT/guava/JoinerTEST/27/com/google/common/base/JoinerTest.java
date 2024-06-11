package com.example.junit; // replace with your package name

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JoinerTest {

    private Joiner joinerWithComma;
    private Joiner joinerWithDash;
    private Joiner joinerWithEmptySeparator;
    private Joiner joinerWithNullText;
    private Joiner joinerWithSkipNulls;
    private MapJoiner mapJoinerWithComma;

    @Before
    public void setUp() {
        joinerWithComma = Joiner.on(",");
        joinerWithDash = Joiner.on("-");
        joinerWithEmptySeparator = Joiner.on("");
        joinerWithNullText = joinerWithComma.useForNull("NA");
        joinerWithSkipNulls = joinerWithComma.skipNulls();
        mapJoinerWithComma = joinerWithComma.withKeyValueSeparator(",=");
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        StringWriter stringWriter = new StringWriter();
        Joiner joiner = joinerWithComma;

        Iterable<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        joiner.appendTo(stringWriter, integers);
        String expected = "1,2,3,4,5";
        String actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        StringWriter stringWriter = new StringWriter();
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Joiner joiner = joinerWithComma;

        joiner.appendTo(stringWriter, iterator);
        String expected = "1,2,3,4,5";
        String actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendToWithArray() throws IOException {
        StringWriter stringWriter = new StringWriter();
        Joiner joiner = joinerWithComma;

        Object[] array = new Object[]{1, 2, 3, 4, 5};
        joiner.appendTo(stringWriter, array);
        String expected = "1,2,3,4,5";
        String actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithIterable() {
        String expected = "apple,banana,cherry,date,elderberry";
        String actual = joinerWithComma.join(Arrays.asList("apple", "banana", "cherry", "date", "elderberry"));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithArray() {
        String expected = "apple,banana,cherry,date,elderberry";
        String actual = joinerWithComma.join(new String[]{"apple", "banana", "cherry", "date", "elderberry"});
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNullFirstAndSecondAndRest() {
        String expected = "NA,NA,NA,apple,banana,cherry,date,elderberry";
        String actual = joinerWithNullText.join(Arrays.asList(null, null, null, "apple", "banana", "cherry", "date", "elderberry"));
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithNullFirstAndSecondAndRestWithSkipNulls() {
        String expected = "apple,banana,cherry,date,elderberry";
        String actual = joinerWithSkipNulls.join(Arrays.asList("apple", null, "cherry", null, "date", "elderberry"));
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinWithNullIterable() {
        joinerWithComma.join((Iterable<Object>)null);
    }

    @Test
    public void testAppendToWithMapEntrySet() throws IOException {
        StringWriter stringWriter = new StringWriter();
        MapJoiner mapJoiner = mapJoinerWithComma;

        Map<String,Integer> map = Maps.newLinkedHashMap();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);
        map.put("date", 4);
        map.put("elderberry", 5);

        mapJoiner.appendTo(stringWriter, map.entrySet());
        String expected = "apple=1,banana=2,cherry=3,date=4,elderberry=5";
        String actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testJoinWithMapEntrySet() {
        String expected = "apple=1,banana=2,cherry=3,date=4,elderberry=5";
        String actual = mapJoinerWithComma.join(map.entrySet());
        assertEquals(expected, actual);
    }
}