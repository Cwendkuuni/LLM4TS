package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new JsonFactory(prettyPrinter);
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);

        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        String jsonString = mapper.writeValueAsString(Arrays.asList(1, 2, 3, 4));

        jsonGenerator.writeRaw("[" + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "]");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeRaw("[" + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "]");
        jsonGenerator.writeNumber(1);
        jsonGenerator.writeArrayValueSeparator();
        jsonGenerator.writeRaw("[" + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "]");
        jsonGenerator.writeNumber(2);
        jsonGenerator.writeArrayValueSeparator();
        jsonGenerator.writeRaw("[" + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "]");
        jsonGenerator.writeNumber(3);
        jsonGenerator.writeArrayValueSeparator();
        jsonGenerator.writeRaw("[" + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "]");
        jsonGenerator.writeNumber(4);
        jsonGenerator.writeEndArray();

        assertEquals(jsonString, jsonGenerator.getOutputContent());
        jsonGenerator.close();
    }

    @Test
    public void testWithRootSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new JsonFactory(prettyPrinter);
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);

        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        String jsonString = mapper.writeValueAsString(Arrays.asList(1, 2, 3, 4));

        String rootSeparator = " separator ";
        prettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        jsonGenerator = jsonFactory.createGenerator(System.out);

        jsonGenerator.writeRaw("[" + rootSeparator + "]");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeRaw("[" + rootSeparator + "]");
        jsonGenerator.writeNumber(1);
        jsonGenerator.writeArrayValueSeparator();
        jsonGenerator.writeRaw("[" + rootSeparator + "]");
        jsonGenerator.writeNumber(2);
        jsonGenerator.writeArrayValueSeparator();
        jsonGenerator.writeRaw("[" + rootSeparator + "]");
        jsonGenerator.writeNumber(3);
        jsonGenerator.writeArrayValueSeparator();
        jsonGenerator.writeRaw("[" + rootSeparator + "]");
        jsonGenerator.writeNumber(4);
        jsonGenerator.writeEndArray();

        assertEquals("[" + rootSeparator + "][" + rootSeparator + "]1" + JsonGenerationException.COMMATO_SEPARATOR, jsonGenerator.getOutputContent());
        jsonGenerator.close();
    }

    @Test
    public void testWithObjectIndenter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new JsonFactory(prettyPrinter);
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);

        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        String jsonString = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            int age = 30;
        }});

        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new DefaultIndenter.SystemLineFeedInstance());
        jsonGenerator = jsonFactory.createGenerator(System.out);

        jsonGenerator.writeRaw("{\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "name" + "\n");
        jsonGenerator.writeRaw("  :\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "\"John Doe\"\n");
        jsonGenerator.writeRaw(",\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "age" + "\n");
        jsonGenerator.writeRaw("  :\n");
        jsonGenerator.writeNumber(30);
        jsonGenerator.writeEndObject();

        assertEquals(jsonString, jsonGenerator.getOutputContent());
        jsonGenerator.close();
    }

    @Test
    public void testWithSpacesInObjectEntries() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new JsonFactory(prettyPrinter);
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);

        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        String jsonString = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            int age = 30;
        }});

        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSpacesInObjectEntries();
        jsonGenerator = jsonFactory.createGenerator(System.out);

        jsonGenerator.writeRaw("{\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "name" + " :\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "\"John Doe\"\n");
        jsonGenerator.writeRaw(",\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "age" + " :\n");
        jsonGenerator.writeNumber(30);
        jsonGenerator.writeEndObject();

        assertEquals(jsonString, jsonGenerator.getOutputContent());
        jsonGenerator.close();
    }

    @Test
    public void testWithObjectFieldValueSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonFactory jsonFactory = new JsonFactory(prettyPrinter);
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);

        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        String jsonString = mapper.writeValueAsString(new Object() {{
            String name = "John Doe";
            int age = 30;
        }});

        prettyPrinter = new DefaultPrettyPrinter();
        String objectFieldValueSeparator = " = ";
        prettyPrinter.withSeparators(new Separators(null, null, objectFieldValueSeparator, null, null, null, null));
        jsonGenerator = jsonFactory.createGenerator(System.out);

        jsonGenerator.writeRaw("{\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "name" + " :\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "\"John Doe\"" + objectFieldValueSeparator);
        jsonGenerator.writeRaw(",\n");
        jsonGenerator.writeRaw("  " + DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "age" + " :\n");
        jsonGenerator.writeNumber(30);
        jsonGenerator.writeEndObject();

        assertEquals(jsonString.replace(":\n", " :\n" + objectFieldValueSeparator), jsonGenerator.getOutputContent());
        jsonGenerator.close();
    }
}

class DefaultIndenter implements PrettyPrinter.Indenter {
    public static class SystemLineFeedInstance implements DefaultIndenter {
        @Override
        public void writeIndentation(JsonGenerator generator, int indentationLevel) throws IOException {
            generator.writeRaw(System.lineSeparator());
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }

    @Override
    public void writeIndentation(JsonGenerator generator, int indentationLevel) throws IOException {
        for (int i = 0; i < indentationLevel * 2; i++) {
            generator.writeRaw(' ');
        }
    }

    @Override
    public boolean isInline() {
        return true;
    }
}

class Separators {
    private final String objectEntrySeparator;
    private final String arrayValueSeparator;
    private final String objectFieldValueSeparator;

    public Separators(String objectEntrySeparator, String arrayValueSeparator, String objectFieldValueSeparator,
            String nullValueSeparator, String notQuotedKeySeparator, String notQuotedValueSeparator, String doubleQuotedKeySeparator) {
        this.objectEntrySeparator = objectEntrySeparator;
        this.arrayValueSeparator = arrayValueSeparator;
        this.objectFieldValueSeparator = objectFieldValueSeparator;
    }

    public String getObjectEntrySeparator() {
        return objectEntrySeparator;
    }

    public String getArrayValueSeparator() {
        return arrayValueSeparator;
    }

    public String getObjectFieldValueSeparator() {
        return objectFieldValueSeparator;
    }
}