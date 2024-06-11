package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.*;
import org.junit.*;

import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter defaultPrettyPrinter;

    @Before
    public void setUp() {
        defaultPrettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testDefaultPrettyPrinterEmptyObject() throws IOException {
        String json = defaultPrettyPrinter.writeValueAsString(new Object());
        assertEquals("{}", json);
    }

    @Test
    public void testDefaultPrettyPrinterEmptyArray() throws IOException {
        String json = defaultPrettyPrinter.writeValueAsString(new Object[0]);
        assertEquals("[]", json);
    }

    @Test
    public void testDefaultPrettyPrinterSimpleObject() throws IOException {
        String json = defaultPrettyPrinter.writeValueAsString(new SimpleObject());
        assertEquals("{\"name\":\"John\",\"age\":30}", json);
    }

    @Test
    public void testDefaultPrettyPrinterNestedObjects() throws IOException {
        String json = defaultPrettyPrinter.writeValueAsString(new NestedObject());
        assertEquals("{\"outer\":{\"inner\":{\"name\":\"John\",\"age\":30}}}", json);
    }

    @Test
    public void testDefaultPrettyPrinterNestedArrays() throws IOException {
        String json = defaultPrettyPrinter.writeValueAsString(new NestedArray());
        assertEquals("[[1,2],[3,4],[5,6]]", json);
    }

    @Test
    public void testDefaultPrettyPrinterSpacesInObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();
        String json = prettyPrinter.writeValueAsString(new SimpleObjectWithSpaces());
        assertEquals("{\"name\": \"John\",\"age\": 30}", json);
    }

    @Test(expected = IOException.class)
    public void testDefaultPrettyPrinterNullObject() throws IOException {
        defaultPrettyPrinter.writeValueAsString(null);
    }

    @Test(expected = IOException.class)
    public void testDefaultPrettyPrinterNullString() throws IOException {
        defaultPrettyPrinter.writeValueAsString((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPrettyPrinterNullRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(null);
    }
}

class SimpleObject {
    String name = "John";
    int age = 30;
}

class NestedObject {
    SimpleObject obj = new SimpleObject();
}

class SimpleObjectWithSpaces {
    String name = "John";
    int age = 30;
}

class NestedArray {
    int[][] array = {{1, 2}, {3, 4}, {5, 6}};
}