package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectMapper;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);

        String input = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        JsonGenerator generator = mapper.createGenerator(new StringWriter());
        generator.setPrettyPrinter(new DefaultPrettyPrinter());
        mapper.readTree(input).writeValue(generator);
        generator.flush();

        String output = generator.getOutputContent();
        String expected = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\"\n" +
                "}";

        assertEquals(expected, output);
    }

    @Test
    public void testRootSeparator() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);

        String input = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(": ");
        JsonGenerator generator = mapper.createGenerator(new StringWriter());
        generator.setPrettyPrinter(prettyPrinter);
        mapper.readTree(input).writeValue(generator);
        generator.flush();

        String output = generator.getOutputContent();
        String expected = ":\n" +
                "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"city\": \"New York\"\n" +
                "}";

        assertEquals(expected, output);
    }

    @Test
    public void testObjectIndenter() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);

        String input = "{\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new DefaultIndenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw(String.format("%s%s", new String(new char[level * 2]).replace("\0", " "), "\t"));
            }
        });
        JsonGenerator generator = mapper.createGenerator(new StringWriter());
        generator.setPrettyPrinter(prettyPrinter);
        mapper.readTree(input).writeValue(generator);
        generator.flush();

        String output = generator.getOutputContent();
        String expected = "{\n" +
                "  \"name\": {\n" +
                "    \"first\": \"John\",\n" +
                "    \"last\": \"Doe\"\n" +
                "  }\n" +
                "}";

        assertEquals(expected, output);
    }

    @Test
    public void testArrayIndenter() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);

        String input = "[1,2,3]";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new FixedSpaceIndenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("\n");
            }
        });
        JsonGenerator generator = mapper.createGenerator(new StringWriter());
        generator.setPrettyPrinter(prettyPrinter);
        mapper.readTree(input).writeValue(generator);
        generator.flush();

        String output = generator.getOutputContent();
        String expected = "[1\n" +
                " ,2\n" +
                " ,3]";

        assertEquals(expected, output);
    }
}