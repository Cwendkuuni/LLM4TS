package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter stringWriter = new StringWriter();

        JsonGenerator jsonGenerator = prettyPrinter.getFactory().createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, new Object());

        String actual = stringWriter.toString();
        String expected = "{\n" +
                "}\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        StringWriter stringWriter = new StringWriter();

        JsonGenerator jsonGenerator = prettyPrinter.getFactory().createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, new Object());

        String actual = stringWriter.toString();
        String expected = "--{\n" +
                "}\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testIndentArraysWith() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indentArraysWith = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentArraysWith(indentArraysWith);

        StringWriter stringWriter = new StringWriter();

        JsonGenerator jsonGenerator = prettyPrinter.getFactory().createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, new int[] {1, 2, 3});

        String actual = stringWriter.toString();
        String expected = "[ \n" +
                " 0,\n" +
                " 1,\n" +
                " 2,\n" +
                " 3\n" +
                "]\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testIndentObjectsWith() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indentObjectsWith = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentObjectsWith(indentObjectsWith);

        StringWriter stringWriter = new StringWriter();

        JsonGenerator jsonGenerator = prettyPrinter.getFactory().createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, new Person("John", "Doe"));

        String actual = stringWriter.toString();
        String expected = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\"\n" +
                "}\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();

        StringWriter stringWriter = new StringWriter();

        JsonGenerator jsonGenerator = prettyPrinter.getFactory().createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, new Person("John", "Doe"));

        String actual = stringWriter.toString();
        String expected = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\"\n" +
                "}\n";

        assertEquals(expected, actual);

        String actualWithColon = stringWriter.getBuffer().toString();
        String expectedWithColon = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\"\n" +
                "}\n";

        assertFalse(actualWithColon.contains(":"));
    }

    @Test
    public void testWriteObjectEntrySeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();

        StringWriter stringWriter = new StringWriter();

        JsonGenerator jsonGenerator = prettyPrinter.getFactory().createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, new Person("John", "Doe")).putField("age", 30);

        String actual = stringWriter.toString();
        String expected = "{\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"age\": 30\n" +
                "}\n";

        assertEquals(expected, actual);
    }

    private static class Person {
        private final String firstName;
        private final String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public int getAge() {
            return 0;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    private static class Indenter implements PrettyPrinter.Indenter {
        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            g.writeRaw("  ");
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }
}

class PrettyPrinterFactory {
    public JsonGenerator createGenerator(StringWriter stringWriter) throws IOException {
        throw new JsonGenerationException("Not supported.");
    }
}