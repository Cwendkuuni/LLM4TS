package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    @Test
    public void testDefaultFeatures() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        // Test default features
        assertFalse(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        jsonGenerator.close();
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.writeString("test string");

        JsonParser jsonParser = objectCodec.getFactory().createParser(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes()));
        assertEquals("test string", jsonParser.getValueAsString());

        jsonGenerator.close();
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.writeBoolean(true);

        JsonParser jsonParser = objectCodec.getFactory().createParser(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes()));
        assertTrue(jsonParser.getValueAsBoolean());

        jsonGenerator.close();
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.writeNull();

        JsonParser jsonParser = objectCodec.getFactory().createParser(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes()));
        assertNull(jsonParser.getValueAsString());

        jsonGenerator.close();
    }

    @Test(expected = IOException.class)
    public void testWriteInvalidValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.writeRawValue("invalid value");

        jsonGenerator.close();
    }

    @Test
    public void testDisableFeatures() throws IOException {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        // Test disabled features
        assertFalse(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        jsonGenerator.close();
    }

    @Test
    public void testEnableFeatures() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        jsonGenerator.enable(Feature.ESCAPE_NON_ASCII);
        jsonGenerator.enable(Feature.STRICT_DUPLICATE_DETECTION);

        // Test enabled features
        assertTrue(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        jsonGenerator.close();
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.useDefaultPrettyPrinter();

        String jsonString = jsonGenerator.getOutputBuffer().toString();
        TreeNode rootNode = new ObjectMapper().readTree(jsonString);

        assertEquals(1, rootNode.size());
        assertSame("root", rootNode.get(0).asText());

        jsonGenerator.close();
    }

    @Test
    public void testSetCodec() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();

        ObjectCodec newObjectCodec = new ObjectMapper().getFactory().getCodec();
        generator.setCodec(newObjectCodec);

        JsonGenerator jsonGenerator = generator.getCodec().getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.writeString("test string");

        JsonParser jsonParser = generator.getCodec().getFactory().createParser(new ByteArrayInputStream(jsonGenerator.getOutputBuffer().getBytes()));
        assertEquals("test string", jsonParser.getValueAsString());

        jsonGenerator.close();
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.close();

        assertFalse(jsonGenerator.isClosed());

        jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));
        jsonGenerator.close();

        assertTrue(jsonGenerator.isClosed());
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        String jsonString = "{\"name\":\"test\",\"age\":30,\"isStudent\":false}";
        TreeNode rootNode = new ObjectMapper().readTree(jsonString);

        jsonGenerator.writeObject(rootNode);

        String generatedJsonString = jsonGenerator.getOutputBuffer().toString();
        TreeNode generatedRootNode = new ObjectMapper().readTree(generatedJsonString);

        assertEquals(rootNode, generatedRootNode);

        jsonGenerator.close();
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, null));

        jsonGenerator.writeRawValue("{\"name\":\"test\",\"age\":30,\"isStudent\":false}");

        String generatedJsonString = jsonGenerator.getOutputBuffer().toString();
        TreeNode generatedRootNode = new ObjectMapper().readTree(generatedJsonString);

        assertEquals(new ObjectMapper().readTree("{\"name\":\"test\",\"age\":30,\"isStudent\":false}"), generatedRootNode);

        jsonGenerator.close();
    }

    @Test
    public void testWriteObjectWithPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = generator.getCodec();
        JsonGenerator jsonGenerator = objectCodec.getJsonGenerator(new JsonGenerationSchema(3.0, new DefaultPrettyPrinter()));

        String jsonString = "{\"name\":\"test\",\"age\":30,\"isStudent\":false}";
        TreeNode rootNode = new ObjectMapper().readTree(jsonString);

        jsonGenerator.writeObject(rootNode);

        String generatedJsonString = jsonGenerator.getOutputBuffer().toString();
        TreeNode generatedRootNode = new ObjectMapper().readTree(generatedJsonString);

        assertEquals(rootNode, generatedRootNode);

        jsonGenerator.close();
    }
}