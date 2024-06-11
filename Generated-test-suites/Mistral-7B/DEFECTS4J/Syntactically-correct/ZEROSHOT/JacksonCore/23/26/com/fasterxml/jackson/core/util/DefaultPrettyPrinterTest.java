package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private JsonGenerator jsonGenerator;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        prettyPrinter = jsonFactory.getCodec().getDefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);
        stringWriter = new StringWriter();
    }

    @Test
    public void testDefaultRootValueSeparator() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new int[] { 1, 2, 3 });
        JsonParser parser = mapper.getFactory().getJsonParser(new StringReader(json));

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(1, parser.getValueAsInt());

        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(2, parser.getValueAsInt());

        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(3, parser.getValueAsInt());

        parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        mapper.writeValue(jsonGenerator, new int[] { 1, 2, 3 });
        json = stringWriter.toString();

        parser = mapper.getFactory().getJsonParser(new StringReader(json));

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(1, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(2, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(3, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        String defaultRootValueSeparator = prettyPrinter.getRootValueSeparatorAsString();
        assertTrue(defaultRootValueSeparator.isEmpty());
    }

    @Test
    public void testCustomRootValueSeparator() throws JsonProcessingException {
        prettyPrinter = new DefaultPrettyPrinter(":");
        jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writer().setRootValueSeparator(":");
        String json = mapper.writeValueAsString(new int[] { 1, 2, 3 });

        parser = mapper.getFactory().getJsonParser(new StringReader(json));

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals(":", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(1, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(2, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(3, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        json = stringWriter.toString();
        parser = mapper.getFactory().getJsonParser(new StringReader(json));
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals(":", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(1, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(2, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(3, parser.getValueAsInt());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());
    }

    @Test
    public void testDefaultIndenter() throws IOException, JsonProcessingException {
        prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writer().withDefaultPrettyPrinter();
        String json = mapper.writeValueAsString(new String[] { "foo", "bar" });

        parser = mapper.getFactory().getJsonParser(new StringReader(json));

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("foo", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("bar", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        json = stringWriter.toString();
        parser = mapper.getFactory().getJsonParser(new StringReader(json));
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("foo", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("bar", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        mapper = new ObjectMapper();
        mapper.writer().withDefaultPrettyPrinter();
        json = mapper.writeValueAsString(new String[] { "foo", "bar" });

        parser = mapper.getFactory().getJsonParser(new StringReader(json));

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("foo", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("bar", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        json = stringWriter.toString();
        parser = mapper.getFactory().getJsonParser(new StringReader(json));
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("foo", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("bar", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());
    }

    @Test
    public void testCustomIndenter() throws IOException, JsonProcessingException {
        prettyPrinter = new DefaultPrettyPrinter();
        jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.setPrettyPrinter(prettyPrinter);

        Indenter customIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("\t");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        prettyPrinter.indentObjectsWith(customIndenter);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writer().withDefaultPrettyPrinter();
        String json = mapper.writeValueAsString(new String[] { "foo", "bar" });

        parser = mapper.getFactory().getJsonParser(new StringReader(json));

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("\tfoo", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("\tbar", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        json = stringWriter.toString();
        parser = mapper.getFactory().getJsonParser(new StringReader(json));
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("\tfoo", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("\tbar", parser.getValueAsString());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());
    }
}