package com.example.junit; // replace with your package name

import com.google.common.base.Joiner;
import com.google.common.base.MapJoiner;
import com.google.common.collect.Iterators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JoinerTest {

    @Test
    public void testJoinerConstruction() {
        Joiner joiner = Joiner.on(",");
        assertEquals(",", joiner.separator());
    }

    @Test
    public void testAppendTo_Iterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);

        Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");
        joiner.appendTo(appendable, iterable);

        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendTo_ObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);

        String[] array = new String[]{"apple", "banana", "cherry"};
        joiner.appendTo(appendable, array);

        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testAppendTo_Iterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);

        Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(appendable, iterator);

        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testJoin_Iterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new String[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoin_Iterator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry").iterator());
        assertEquals("apple,banana,cherry", result);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_NullIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);

        Iterable<?> nullIterable = null;
        joiner.appendTo(appendable, nullIterable);
    }

    @Test(expected = NullPointerException.class)
    public void testJoin_NullIterable_UseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        String result = joiner.join(Arrays.asList(null, "banana", "cherry"));
        fail("Expected a NullPointerException");
    }

    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);

        Iterator<Object> iterator = Arrays.asList("apple", null, "banana", "cherry").iterator();
        joiner.appendTo(appendable, iterator, Joiner.SkipNulls.INSTANCE);

        assertEquals("apple,banana,cherry", outputStream.toString());
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");

        Map<String, String> map = new HashMap<>();
        map.put("apple", "fruit");
        map.put("banana", "yellow");
        map.put("cherry", "red");

        StringBuilder result = mapJoiner.appendTo(new StringBuilder(), map.entrySet());
        assertEquals("apple:fruit,banana:yellow,cherry:red", result.toString());

        String joined = mapJoiner.join(map.entrySet());
        assertEquals("apple:fruit,banana:yellow,cherry:red", joined);
    }

    @Test
    public void testMapJoiner_UseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");

        Map<String, String> map = new HashMap<>();
        map.put(null, "cherry");
        map.put("banana", "yellow");

        StringBuilder result = mapJoiner.appendTo(new StringBuilder(), map.entrySet());
        assertEquals("null:cherry,banana:yellow", result.toString());

        String joined = mapJoiner.join(map.entrySet());
        assertEquals("null:cherry,banana:yellow", joined);
    }
}

class JoinerTest_Mockito {

    @Test
    public void testAppendTo_MockIterable() throws IOException {
        Joiner joiner = Mockito.spy(Joiner.on(","));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);

        Iterator<String> mockIterator = Mockito.mock(Iterator.class);
        Mockito.when(mockIterator.hasNext()).thenReturn(true, false);
        Mockito.when(mockIterator.next()).thenReturn("apple", "banana", "cherry");

        Joiner.AppendableFunction<Appendable, Iterator<?>> appendToFunction = joiner::appendTo;
        appendToFunction.apply(appendable, mockIterator);

        assertEquals("apple,banana,cherry", outputStream.toString());
        Mockito.verify(joiner, Mockito.times(3)).toString(Mockito.eq("apple"));
        Mockito.verify(joiner, Mockito.times(2)).toString(Mockito.eq("banana"));
        Mockito.verify(joiner, Mockito.times(1)).toString(Mockito.eq("cherry"));
    }
}