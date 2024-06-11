package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TreeTypeId;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class GeneratorBaseTest {
    private ObjectMapper mapper;
    private ObjectCodec objectCodec;
    private JsonGenerator generator;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() throws JsonProcessingException {
        mapper = new ObjectMapper();
        objectCodec = mapper.getFactory().getEncoder();
        outputStream = new ByteArrayOutputStream();
        generator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), objectCodec, null) {
            @Override
            public void writeRaw(byte[] bytes, int offset, int length) throws IOException {
                outputStream.write(bytes, offset, length);
            }

            @Override
            public void close() throws IOException {
                super.close();
                outputStream.close();
            }
        };
    }

    @Test
    public void testVersion() throws JsonGenerationException {
        assertEquals(GeneratorBase.class.getPackage().getImplementationVersion(), generator.version().toString());
    }

    @Test
    public void testIsEnabled() throws JsonGenerationException {
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.WRITE_ESCAPED_NUMBERS));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_ESCAPED_NUMBERS);
        assertFalse(generator.isEnabled(Feature.WRITE_ESCAPED_NUMBERS));

        generator.enable(Feature.WRITE_ESCAPED_NUMBERS);
        assertTrue(generator.isEnabled(Feature.WRITE_ESCAPED_NUMBERS));
    }

    @Test
    public void testGetFeatureMask() throws JsonGenerationException {
        assertEquals(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testConfigure() throws JsonGenerationException, IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode rootNode = mapper.readTree(inputStream);
        TreeTypeId id = rootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_OBJECT, id.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_OBJECT, id.getSubTypeId());

        JsonParser parser = mapper.getFactory().createParser(inputStream);
        JsonGenerator newGenerator = parser.getCodec().getGeneratorForDefaultPrettyPrinter();
        newGenerator.configure(Feature.ESCAPE_NON_ASCII, false);

        generator.writeString("foo");
        generator.writeRaw("\u00A0");
        generator.close();

        parser.nextValue();
        String result = parser.getText();
        assertEquals("foo\\u00A0", result);
    }

    @Test
    public void testOverrideStdFeatures() throws JsonGenerationException, IOException {
        ByteArrayOutputStream newOutput = new ByteArrayOutputStream();
        JsonGenerator newGenerator = new GeneratorBase(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.WRITE_ESCAPED_NUMBERS.getMask(), objectCodec, null) {
            @Override
            public void writeRaw(byte[] bytes, int offset, int length) throws IOException {
                newOutput.write(bytes, offset, length);
            }

            @Override
            public void close() throws IOException {
                super.close();
                newOutput.close();
            }
        };

        newGenerator.overrideStdFeatures(Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_ESCAPED_NUMBERS.getMask());
        newGenerator.writeString("foo");
        newGenerator.writeRaw("\u00A0");
        newGenerator.close();

        ByteArrayInputStream newInput = new ByteArrayInputStream(newOutput.toByteArray());
        ObjectNode newRootNode = mapper.readTree(newInput);
        TreeTypeId newId = newRootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_OBJECT, newId.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_OBJECT, newId.getSubTypeId());

        JsonParser newParser = mapper.getFactory().createParser(newInput);
        JsonGenerator newNewGenerator = newParser.getCodec().getGeneratorForDefaultPrettyPrinter();
        newNewGenerator.configure(Feature.ESCAPE_NON_ASCII, false);

        String newResult = newParser.nextTextValue();
        assertEquals("foo\\u00A0", newResult);
    }

    @Test
    public void testWriteString() throws JsonGenerationException, IOException {
        generator.writeString("foo");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode rootNode = mapper.readTree(inputStream);
        TreeTypeId id = rootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_STRING, id.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_STRING, id.getSubTypeId());
        assertEquals("foo", rootNode.asText());
    }

    @Test
    public void testWriteRawValue() throws JsonGenerationException, IOException {
        generator.writeRawValue("foo");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode rootNode = mapper.readTree(inputStream);
        TreeTypeId id = rootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_STRING, id.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_STRING, id.getSubTypeId());
        assertEquals("foo", rootNode.asText());
    }

    @Test
    public void testWriteRawValueWithOffset() throws JsonGenerationException, IOException {
        generator.writeRawValue("foo", 0, 3);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode rootNode = mapper.readTree(inputStream);
        TreeTypeId id = rootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_STRING, id.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_STRING, id.getSubTypeId());
        assertEquals("foo", rootNode.asText());
    }

    @Test
    public void testWriteRawValueWithCharArray() throws JsonGenerationException, IOException {
        generator.writeRawValue("foo".toCharArray(), 0, 3);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode rootNode = mapper.readTree(inputStream);
        TreeTypeId id = rootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_STRING, id.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_STRING, id.getSubTypeId());
        assertEquals("foo", rootNode.asText());
    }

    @Test
    public void testWriteRawValueWithSerializableString() throws JsonGenerationException, IOException {
        generator.writeRawValue(new TextNode("foo"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode rootNode = mapper.readTree(inputStream);
        TreeTypeId id = rootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_STRING, id.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_STRING, id.getSubTypeId());
        assertEquals("foo", rootNode.asText());
    }

    @Test
    public void testWriteObject() throws JsonGenerationException, IOException {
        ObjectNode rootNode = ObjectNode.class.cast(mapper.createObjectNode());
        rootNode.put("name", new TextNode("John Doe"));
        generator.writeObject(rootNode);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode parsedNode = mapper.readTree(inputStream);
        TreeTypeId nodeId = parsedNode.getClassId();

        assertEquals(TreeTypeId.TYPE_OBJECT, nodeId.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_OBJECT, nodeId.getSubTypeId());

        ObjectNode parsedRootNode = ObjectNode.class.cast(parsedNode);
        assertEquals("name", parsedRootNode.get("name").asText());
    }

    @Test
    public void testWriteNull() throws JsonGenerationException, IOException {
        generator.writeNull();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode rootNode = mapper.readTree(inputStream);
        TreeTypeId id = rootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_null, id.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_NULL, id.getSubTypeId());
    }

    @Test(expected = IOException.class)
    public void testWriteNumber() throws JsonGenerationException, IOException {
        generator.writeNumber(42);
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteBoolean() throws JsonGenerationException, IOException {
        generator.writeBoolean(true);
    }

    @Test
    public void testClose() throws JsonGenerationException, IOException {
        generator.writeString("foo");
        generator.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectNode rootNode = mapper.readTree(inputStream);
        TreeTypeId id = rootNode.getClassId();

        assertEquals(TreeTypeId.TYPE_STRING, id.getBaseTypeId());
        assertEquals(TreeTypeId.STANDARD_STRING, id.getSubTypeId());
        assertEquals("foo", rootNode.asText());

        assertFalse(generator.isOpen());
    }

    @Test(expected = IOException.class)
    public void testWriteRawAfterClose() throws JsonGenerationException, IOException {
        generator.writeString("foo");
        generator.close();
        generator.writeRawValue("bar");
    }
}