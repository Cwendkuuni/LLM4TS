package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectNode node = jsonFactory.createObjectNode();
        node.putPOJO("name", new Person("John Doe", 30));
        node.put("age", 30);
        node.put("city", "New York");

        jsonGenerator.writeObject(node);
        jsonGenerator.close();

        String actual = stringWriter.toString();
        String expected = "{\n" +
                " \"name\": {\n" +
                " \"first\": \"John\",\n" +
                " \"last\": \"Doe\"\n" +
                " },\n" +
                " \"age\": 30,\n" +
                " \"city\": \"New York\"\n" +
                "}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectNode node = jsonFactory.createObjectNode();
        node.putPOJO("name", new Person("John Doe", 30));
        node.put("age", 30);
        node.put("city", "New York");

        jsonGenerator.writeObject(node);
        jsonGenerator.close();

        String actual = stringWriter.toString();
        String expected = "--\n" +
                "{\n" +
                " \"name\": {\n" +
                " \"first\": \"John\",\n" +
                " \"last\": \"Doe\"\n" +
                " },\n" +
                " \"age\": 30,\n" +
                " \"city\": \"New York\"\n" +
                "}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorAndCustomIndentation() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = new IndenterWithLineFeedAndTwoSpaces();
        DefaultPrettyPrinter customPrettyPrinter = new DefaultPrettyPrinter(prettyPrinter, objectIndenter);
        StringWriter stringWriter = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(customPrettyPrinter);

        ObjectNode node = jsonFactory.createObjectNode();
        node.putPOJO("name", new Person("John Doe", 30));
        node.put("age", 30);
        node.put("city", "New York");

        jsonGenerator.writeObject(node);
        jsonGenerator.close();

        String actual = stringWriter.toString();
        String expected = "\n---\n" +
                "{\n" +
                " \t\t\"name\": {\n" +
                " \t\t\t\"first\": \"John\",\n" +
                " \t\t\t\"last\": \"Doe\"\n" +
                " \t\t},\n" +
                " \t\"age\": 30,\n" +
                " \t\"city\": \"New York\"\n" +
                "}\n";

        assertEquals(expected, actual);
    }

    private static class Person {
        private final String first;
        private final String last;
        private final int age;

        public Person(String first, int age) {
            this.first = first;
            this.last = last;
            this.age = age;
        }

        public String getFirst() {
            return first;
        }

        public String getLast() {
            return last;
        }

        public int getAge() {
            return age;
        }
    }

    private static class IndenterWithLineFeedAndTwoSpaces implements Indenter {

        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            for (int i = 0; i < level; i++) {
                g.writeRaw("\t");
                g.writeRaw("\n");
            }
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }
}