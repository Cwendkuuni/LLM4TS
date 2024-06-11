package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"skills\":[\"Java\",\"Python\"]}";
        JsonNode node = JsonFactory.instance.readTree(json);
        ObjectCodec objectCodec = JsonFactory.instance.getObjectMapper().getFactory();
        TreeTraversingParser parser = new TreeTraversingParser(node, objectCodec);

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("skills", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        // Test nextToken() with recursive call to nextToken()
        token = parser.nextToken();
        assertTrue(token.isStartStructureToken());
        JsonToken nextToken = parser.nextToken();
        assertTrue(nextToken.isTextual());
        assertEquals("Java", nextToken.asString());
        token = parser.nextToken();
        assertTrue(token.isValueSeparatorToken());
        token = parser.nextToken();
        assertTrue(token.isTextual());
        assertEquals("Python", token.asString());
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test skipChildren()
        parser.skipChildren();
        assertNull(parser.nextToken());

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test getText()
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        assertEquals(null, parser.getText());

        // Test getTextCharacters() and getTextLength()
        String json2 = "{\"name\":\"John\",\"age\":30,\"skills\":[\"Java\",\"Python\"]}";
        node = JsonFactory.instance.readTree(json2);
        parser = new TreeTraversingParser(node, objectCodec);
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        char[] textCharacters = parser.getTextCharacters();
        assertEquals("name", parser.getCurrentName());
        assertEquals(5, parser.getTextLength());
        assertEquals("name", new String(textCharacters, 0, 5));

        // Test getNumberType()
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals(JsonType.NUMBER, parser.getNumberType());

        // Test getBigIntegerValue() and getDecimalValue()
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30L, parser.getBigIntegerValue().longValue());
        assertEquals(new BigDecimal("30"), parser.getDecimalValue());

        // Test getDoubleValue() and getFloatValue()
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(30.0, parser.getDoubleValue(), 0.0);
        assertEquals(30.0f, parser.getFloatValue(), 0.0f);

        // Test getLongValue() and getIntValue()
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());
        assertEquals(30L, parser.getLongValue());

        // Test getNumberValue()
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(new Integer(30), parser.getNumberValue());

        // Test getEmbeddedObject()
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("skills", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        JsonNode embeddedObject = parser.getEmbeddedObject();
        assertArrayEquals(new String[]{"Java", "Python"}, ((JsonArray) embeddedObject).toTypedArray(String[].class));

        // Test isNaN()
        token = parser.nextToken();
        assertFalse(parser.isNaN());

        // Test getBinaryValue() with base64 variant
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        byte[] binaryValue = parser.getBinaryValue(Base64Variants.BASE64);
        assertArrayEquals(new byte[]{0x41, 0x52, 0x53}, binaryValue);

        // Test readBinaryValue() with base64 variant
        OutputStream outputStream = new ByteArrayOutputStream();
        parser.readBinaryValue(Base64Variants.BASE64, outputStream);
        byte[] binaryValueRead = outputStream.toByteArray();
        assertArrayEquals(new byte[]{0x41, 0x52, 0x53}, binaryValueRead);
    }

    // Helper classes
    static class ByteArrayOutputStream extends java.io.OutputStream {
        byte[] buffer;

        public ByteArrayOutputStream() {
            this.buffer = new byte[1024];
        }

        @Override
        public void write(int b) throws IOException {
            write(new byte[]{ (byte) b });
        }

        @Override
        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (off < 0 || len < 0 || (off + len) > buffer.length) {
                throw new IndexOutOfBoundsException();
            }
            System.arraycopy(b, off, buffer, off, len);
        }

        byte[] toByteArray() {
            return java.util.Arrays.copyOf(buffer, buffer.length);
        }
    }
}