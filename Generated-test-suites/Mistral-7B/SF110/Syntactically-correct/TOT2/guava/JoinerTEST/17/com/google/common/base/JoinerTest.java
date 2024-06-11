package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JoinerTest {

    @Test
    public void testNewJoiner() {
        Joiner joiner = Joiner.on(" ");
        assertEquals(" ", joiner.separator);
    }

    @Test
    public void testNewJoinerWithCharSeparator() {
        Joiner joiner = Joiner.on(' ');
        assertEquals(" ", joiner.separator);
    }

    @Test
    public void testAppendToWithIterableAndAppendable() throws IOException {
        Joiner joiner = Joiner.on(" ");
        StringWriter stringWriter = new StringWriter();
        Iterable<String> list = Arrays.asList("Hello", "World");

        joiner.appendTo(stringWriter, list);
        String result = stringWriter.toString();
        assertEquals("Hello World", result);

        // Edge cases and error scenarios
        Joiner joinerNull = Joiner.on(null);
        assertNull(joinerNull);

        StringWriter stringWriterNull = null;
        Joiner joinerAppendableNull = Joiner.on(" ");
        assertThrows(NullPointerException.class, () -> joinerAppendableNull.appendTo(stringWriterNull, Collections.emptyList()));

        Iterable<Object> iterableNull = null;
        Joiner joinerIterableNull = Joiner.on(" ");
        assertThrows(NullPointerException.class, () -> joinerIterableNull.appendTo(new StringWriter(), iterableNull));

        Iterator<Object> iteratorThrowException = () -> {
            throw new RuntimeException("Test exception");
        };
        Joiner joinerIteratorThrowException = Joiner.on(" ");
        StringWriter stringWriterIterator = new StringWriter();
        assertThrows(RuntimeException.class, () -> joinerIteratorThrowException.appendTo(stringWriterIterator, iteratorThrowException));
    }

    // Test other methods (appendTo, join, useForNull, skipNulls, withKeyValueSeparator) similarly

    // ...
}