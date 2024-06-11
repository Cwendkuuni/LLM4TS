
package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

public class JoinerTest {
    private Joiner joiner;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        joiner = Joiner.on("|");
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Joiner.Appendable appendable = outputStream;

        Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(appendable, parts);

        assertEquals("apple|banana|cherry", outputStream.toString());
    }

    @Test
    public void testAppendToWithEmptyIterable() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Joiner.Appendable appendable = outputStream;

        Iterator<String> parts = Collections.emptyIterator();
        joiner.appendTo(appendable, parts);

        assertEquals("", outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToWithNullIterable() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Joiner.Appendable appendable = outputStream;

        Iterator<String> parts = null;
        joiner.appendTo(appendable, parts);
    }

    @Test
    public void testAppendToWithObjectArray() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Joiner.Appendable appendable = outputStream;
        String separator = joiner.separator;

        Object[] parts = new Object[] {"apple", "banana", "cherry"};
        joiner.appendTo(appendable, parts);

        String expected = String.join(separator, parts);
        assertEquals(expected, outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToWithNullObjectArray() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Joiner.Appendable appendable = outputStream;
        String separator = joiner.separator;

        Object[] parts = null;
        joiner.appendTo(appendable, parts);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendToWithNullSeparator() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Joiner.Appendable appendable = outputStream;
        Joiner joinerWithNullSeparator = Joiner.on(null);

        Object[] parts = new Object[] {"apple", "banana", "cherry"};
        joinerWithNullSeparator.appendTo(appendable, parts);
    }

    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on("|");
        String expected = String.join(joiner.separator, Arrays.asList("apple", "banana", "cherry"));
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry").toArray());

        assertEquals(expected, result);
    }

    @Test
    public void testJoinWithEmptyObjectArray() {
        Joiner joiner = Joiner.on("|");
        String expected = "";
        String result = joiner.join(new Object[0]);

        assertEquals(expected, result);
    }

    @Test
    public void testJoinWithNullObjectArray() {
        Joiner joiner = Joiner.on("|");
        String expected = "";
        String result = joiner.join(new Object[0]);

        assertEquals(expected, result);
    }

    @Test
    public void testJoinWithNullFirstObject() {
        Joiner joiner = Joiner.on("|");
        Object[] parts = new Object[] {null, "apple", "banana"};
        String expected = "";
        String result = joiner.join(parts);

        assertEquals(expected, result);
    }

    @Test
    public void testAppendToWithIterator_String() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Joiner.Appendable appendable = outputStream;

        Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(appendable, parts);

        assertEquals("apple|banana|cherry", outputStream.toString());
    }

    @Test
    public void testAppendToWithIterator_Object() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Joiner.Appendable appendable = outputStream;

        Iterator<Object> parts = Arrays.asList("apple", "banana", "cherry").iterator();
        joiner.appendTo(appendable, parts);

        assertEquals("apple|banana|cherry", outputStream.toString());
    }

    @Test
    public void testJoinWithIterator_String() {
        Joiner joiner = Joiner.on("|");
        Iterator<String> parts = Arrays.asList("apple", "banana", "cherry").iterator();
        String expected = joiner.join(parts);
        String result = joiner.appendTo(new StringBuilder(), parts).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testJoinWithIterator_Object() {
        Joiner joiner = Joiner.on("|");
        Iterator<Object> parts = Arrays.asList("apple", "banana", "cherry").iterator();
        String expected = joiner.join(parts);
        String result = joiner.appendTo(new StringBuilder(), parts).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testUseForNullWithObjectArray() {
        Joiner joiner = Joiner.on("|");
        Joiner joinerWithNullText = joiner.useForNull("null");
        String expected = String.join(joiner.separator, Arrays.asList("apple", null, "cherry"));
        String result = joinerWithNullText.join(new Object[] {"apple", null, "cherry"});

        assertEquals(expected, result);
    }

    @Test
    public void testUseForNullWithIterator_String() {
        Joiner joiner = Joiner.on("|");
        Joiner joinerWithNullText = joiner.useForNull("null");
        Iterator<String> parts = Arrays.asList("apple", null, "cherry").iterator();
        String expected = joiner.separator + "null" + joiner.separator + "cherry";
        String result = joinerWithNullText.join(parts);

        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullWithIterator_Object() {
        Joiner joiner = Joiner.on("|");
        Joiner joinerWithNullText = joiner.useForNull("null");
        Iterator<Object> parts = Arrays.asList("apple", null, "cherry").iterator();

        joinerWithNullText.appendTo(new StringBuilder(), parts);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithObjectArray() {
        Joiner joiner = Joiner.on("|");
        Joiner joinerWithSkipNulls = joiner.skipNulls();
        String[] parts = new String[] {"apple", null, "cherry"};
        String expected = "apple|cherry";
        String result = joinerWithSkipNulls.join(parts);

        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithIterator_String() {
        Joiner joiner = Joiner.on("|");
        Joiner joinerWithSkipNulls = joiner.skipNulls();
        Iterator<String> parts = Arrays.asList("apple", null, "cherry").iterator();

        joinerWithSkipNulls.appendTo(new StringBuilder(), parts);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullWithJoiner() {
        Joiner joiner = Joiner.on("|");
        Joiner joinerWithNullText = joiner.useForNull("null").useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithJoiner() {
        Joiner joiner = Joiner.on("|");
        Joiner joinerWithSkipNulls = joiner.skipNulls().skipNulls();
    }

    @Test
    public void testWithKeyValueSeparatorWithMap() {
        Joiner joiner = Joiner.on(":");
        Joiner mapJoiner = joiner.withKeyValueSeparator("=");

        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", null);

        String expected = "key1=value1:" + joiner.separator + "key2=value2";
        String result = mapJoiner.join(map);

        assertEquals(expected, result);
    }
}