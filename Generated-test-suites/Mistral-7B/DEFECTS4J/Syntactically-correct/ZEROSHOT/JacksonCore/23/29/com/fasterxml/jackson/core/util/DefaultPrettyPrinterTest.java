package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for {@link DefaultPrettyPrinter} class.
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        String json = mapper.writeValueAsString(new Person("Alice", 25), prettyPrinter);
        System.out.println(json);
        assertEquals("{\"name\":\"Alice\",\"age\":25}", json);

        json = mapper.writeValueAsString(new Person("Bob", 30), prettyPrinter);
        System.out.println(json);
        assertEquals("{\"name\":\"Bob\",\"age\":30}", json);

        json = mapper.writeValueAsString(new Person[] { new Person("Charlie", 35), new Person("David", 40) },
                prettyPrinter);
        System.out.println(json);
        assertEquals("[{\"name\":\"Charlie\",\"age\":35},{\"name\":\"David\",\"age\":40}]", json);
    }

    @Test
    public void testCustomRootSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("-----"));

        String json = mapper.writeValueAsString(new Person("Eve", 45), prettyPrinter);
        System.out.println(json);
        assertEquals("-----{\"name\":\"Eve\",\"age\":45}", json);
    }

    @Test
    public void testCustomObjectFieldValueSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSeparators(new Separators(new SerializedString(":"), new SerializedString(", ")));

        String json = mapper.writeValueAsString(new Person("Frank", 50), prettyPrinter);
        System.out.println(json);
        assertEquals("{\"name\":Frank,\"age\":50}", json);
    }

    @Test
    public void testSpacesInObjectEntries() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();

        String json = mapper.writeValueAsString(new Person("Grace", 55), prettyPrinter);
        System.out.println(json);
        assertEquals("{\"name\": Grace,\"age\": 55}", json);

        json = mapper.writeValueAsString(new Person("Harry", 60), prettyPrinter);
        System.out.println(json);
        assertEquals("{\"name\": Harry,\"age\": 60}", json);
    }

    @Test
    public void testCustomArrayIndenter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        String json = mapper.writeValueAsString(new Person[] { new Person("Ivy", 65), new Person("Jack", 70) },
                prettyPrinter);
        System.out.println(json);
        assertEquals("[  {\"name\":\"Ivy\",\"age\":65},\n  {\"name\":\"Jack\",\"age\":70}]", json);
    }

    @Test
    public void testCustomObjectIndenter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("    ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        String json = mapper.writeValueAsString(new Person("Kate", 75), prettyPrinter);
        System.out.println(json);
        assertEquals("{\n    \"name\":\"Kate\",\n    \"age\":75\n}", json);
    }

    private static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("{\"name\":\"%s\",\"age\":%d}", name, age);
        }
    }

    private static ObjectMapper mapper = new ObjectMapper();
}