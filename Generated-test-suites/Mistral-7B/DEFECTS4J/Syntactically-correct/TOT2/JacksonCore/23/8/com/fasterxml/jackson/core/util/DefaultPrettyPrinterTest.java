package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory factory = new JsonFactory(prettyPrinter);
        ObjectMapper mapper = new ObjectMapper(factory);

        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
        byte[] jsonBytes = json.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        mapper.writeValue(generator, parser.readValueAsTree());
        String prettyPrintedJson = generator.getOutputStream().toString();

        Assert.assertEquals(json, prettyPrintedJson);
    }

    @Test
    public void testConstructorWithRootSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        JsonFactory factory = new JsonFactory(prettyPrinter);
        ObjectMapper mapper = new ObjectMapper(factory);

        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
        byte[] jsonBytes = json.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        mapper.writeValue(generator, parser.readValueAsTree());
        String prettyPrintedJson = generator.getOutputStream().toString();

        Assert.assertEquals("--{" + System.lineSeparator() + " \"name\": \"John\"" + System.lineSeparator() + ",\"age\": 30," + System.lineSeparator() + " \"cars\": [" + System.lineSeparator() + " \"Ford\"" + System.lineSeparator() + ",\"BMW\"" + System.lineSeparator() + ",\"Fiat\"" + System.lineSeparator() + " ]" + System.lineSeparator() + "}", prettyPrintedJson);
    }

    @Test
    public void testWithRootSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withRootSeparator(new SerializedString("--"));
        JsonFactory factory = new JsonFactory(prettyPrinter);
        ObjectMapper mapper = new ObjectMapper(factory);

        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
        byte[] jsonBytes = json.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        mapper.writeValue(generator, parser.readValueAsTree());
        String prettyPrintedJson = generator.getOutputStream().toString();

        Assert.assertEquals("--{" + System.lineSeparator() + " \"name\": \"John\"" + System.lineSeparator() + ",\"age\": 30," + System.lineSeparator() + " \"cars\": [" + System.lineSeparator() + " \"Ford\"" + System.lineSeparator() + ",\"BMW\"" + System.lineSeparator() + ",\"Fiat\"" + System.lineSeparator() + " ]" + System.lineSeparator() + "}", prettyPrintedJson);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullRootSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withRootSeparator(null);
    }

    @Test
    public void testWithIndenters() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter arrayIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("| ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        Indenter objectIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("| ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.indentObjectsWith(objectIndenter);

        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
        byte[] jsonBytes = json.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        mapper.writeValue(generator, parser.readValueAsTree());
        String prettyPrintedJson = generator.getOutputStream().toString();

        Assert.assertEquals("| |--{|  |-- \"name\": \"John\"" + System.lineSeparator() + "|  |,\"age\": 30," + System.lineSeparator() + "|  |-- \"cars\": [" + System.lineSeparator() + "| |  | |--\"Ford\"" + System.lineSeparator() + "| |  |," + System.lineSeparator() + "| |  |--\"BMW\"" + System.lineSeparator() + "| |  |," + System.lineSeparator() + "| |  |--\"Fiat\"" + System.lineSeparator() + "]" + System.lineSeparator() + "}", prettyPrintedJson);
    }

    @Test
    public void testWithSpacesInObjectEntries() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withSpacesInObjectEntries();

        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
        byte[] jsonBytes = json.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        mapper.writeValue(generator, parser.readValueAsTree());
        String prettyPrintedJson = generator.getOutputStream().toString();

        Assert.assertEquals(json.replaceAll(":", " : "), prettyPrintedJson);
    }

    @Test
    public void testWithSeparators() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators() {{
            setObjectEntrySeparator(">");
            setObjectFieldValueSeparator(" ");
            setArrayValueSeparator("*");
        }};
        prettyPrinter.withSeparators(separators);

        String json = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
        byte[] jsonBytes = json.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonBytes);
        JsonParser parser = mapper.getFactory().createParser(inputStream);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);

        mapper.writeValue(generator, parser.readValueAsTree());
        String prettyPrintedJson = generator.getOutputStream().toString();

        Assert.assertEquals("{ >\"name\": \"John\" " + System.lineSeparator() + " >,\"age\": 30," + System.lineSeparator() + " >\"cars\": [" + System.lineSeparator() + " *\"Ford\"" + System.lineSeparator() + ",*\"BMW\"" + System.lineSeparator() + ",*\"Fiat\"" + System.lineSeparator() + " ] }", prettyPrintedJson);
    }
}