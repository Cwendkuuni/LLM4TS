package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class GeneratorBaseTest {

    @Test
    public void testGeneratorBase() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new JsonMapper();
        GeneratorBase generator = mapper.getFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());

        // Test isEnabled() method
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));

        // Test setFeatureMask() method
        generator.overrideStdFeatures(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask(), GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));

        // Test getFeatureMask() method
        assertEquals(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask(), generator.getFeatureMask());

        // Test configure() method (not implemented in GeneratorBase)
        //generator.configure(GeneratorBase.Feature.ESCAPE_NON_ASCII, true); // Unsupported operation

        // Test write methods (not all implemented in GeneratorBase)
        // generator.writeStartArray(); // not implemented
        // generator.writeEndArray(); // not implemented
        // generator.writeStartObject(); // not implemented
        // generator.writeEndObject(); // not implemented

        // Test writeFieldName() method
        generator.writeFieldName("test");

        // Test writeString() method
        generator.writeString("test");

        // Test writeRawValue() method
        generator.writeRawValue("test");
        generator.writeRawValue("test", 0, 3);

        // Test writeNumber() method (not implemented in GeneratorBase)
        //generator.writeNumber(1); // not implemented
        //generator.writeNumber(1L); // not implemented
        //generator.writeNumber(1.0); // not implemented
        //generator.writeNumber(new BigDecimal("1")); // not implemented
        //generator.writeNumber(true); // not implemented

        // Test writeNull() method
        generator.writeNull();

        // Test writeObject() method
        String json = "{\"name\":\"test\"}";
        generator.writeObject(new TestObject("test"));
        assertEquals(json, generator.getOutputContext().toString());

        // Test writeTree() method
        TestTreeNode treeNode = new TestTreeNode();
        generator.writeTree(treeNode);
        assertEquals(json, generator.getOutputContext().toString());

        // Test writeBinary() method (not implemented in GeneratorBase)
        //generator.writeBinary(null, null, 0); // not implemented

        // Test flush() method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        generator.setOutputState(new JsonGenerator.OutputState(outputStream, null, null));
        generator.flush();
        assertTrue(outputStream.size() > 0);

        // Test close() method
        generator.close();
        assertFalse(generator.isClosed());

        // Test isClosed() method
        generator.close();
        assertTrue(generator.isClosed());

        // Test getOutputContext() method
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS, mapper.getFactory());
        assertEquals(jsonGenerator.getOutputContext(), jsonGenerator.getOutputContext());
    }

    static class TestObject {
        private String name;

        public TestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class TestTreeNode extends TreeNode {

        @Override
        public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
            generator.writeFieldName("name");
            generator.writeString("test");
        }
    }
}

class GeneratorBaseTest$TestTreeNode extends TreeNode {

    @Override
    public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeFieldName("name");
        generator.writeString("test");
    }
}