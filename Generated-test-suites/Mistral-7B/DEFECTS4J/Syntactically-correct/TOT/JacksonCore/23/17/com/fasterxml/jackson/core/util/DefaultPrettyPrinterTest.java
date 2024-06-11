package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
    }

    @After
    public void teardown() throws IOException {
        jsonFactory.close();
    }

    @Test
    public void testDefaultPrettyPrinterBasic() throws IOException {
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        generator.useDefaultPrettyPrinter();

        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeValue("John Doe");
        generator.writeEndObject();

        generator.close();

        String json = readTestFile("default_pretty_printer_basic.json");
        JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        parser.nextToken();
        Object readValue = parser.readValueAs(Object.class);
        assertEquals("{\"name\":\"John Doe\"}", readValue.toString());
    }

    @Test
    public void testDefaultPrettyPrinterRootSeparator() throws IOException {
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        generator.useDefaultPrettyPrinter();
        generator.withRootSeparator(":");

        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeValue("John Doe");
        generator.writeEndObject();

        generator.close();

        String json = readTestFile("default_pretty_printer_root_separator.json");
        JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        parser.nextToken();
        Object readValue = parser.readValueAs(Object.class);
        assertEquals("{\":name\":\"John Doe\"}", readValue.toString());
    }

    @Test
    public void testDefaultPrettyPrinterIndentObjects() throws IOException {
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        generator.useDefaultPrettyPrinter();
        generator.indentObjectsWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeValue("John Doe");
        generator.writeFieldName("age");
        generator.writeValue(25);
        generator.writeEndObject();

        generator.close();

        String json = readTestFile("default_pretty_printer_indent_objects.json");
        JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        parser.nextToken();
        Object readValue = parser.readValueAs(Object.class);
        assertEquals("{\"name\":\"John Doe\",\"age\":25}", readValue.toString());
    }

    @Test
    public void testDefaultPrettyPrinterIndentArrays() throws IOException {
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        generator.useDefaultPrettyPrinter();
        generator.indentArraysWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        generator.writeStartArray();
        generator.writeValue("element1");
        generator.writeValue("element2");
        generator.writeValue("element3");
        generator.writeEndArray();

        generator.close();

        String json = readTestFile("default_pretty_printer_indent_arrays.json");
        JsonParser parser = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        parser.nextToken();
        Object readValue = parser.readValueAs(Object.class);
        assertEquals("[\"element1\",\"element2\",\"element3\"]", readValue.toString());
    }
}