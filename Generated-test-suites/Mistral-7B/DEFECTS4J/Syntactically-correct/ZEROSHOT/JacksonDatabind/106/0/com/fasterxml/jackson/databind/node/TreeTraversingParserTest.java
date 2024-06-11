package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);

        String json = "{\"a\": 1, \"b\": [2, 3], \"c\": \"abc\"}";
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertSame(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals("a", parser.getCurrentName());

        parser.skipChildren();
        assertSame(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertSame(JsonToken.START_ARRAY, parser.nextToken());
        assertTrue(parser.isStartArray());

        parser.nextToken(); // skip first element of array
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertFalse(parser.isStartArray());

        parser.skipChildren();
        assertEquals("c", parser.getCurrentName());
        assertSame(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("abc", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());

        parser.close();

        // Test binary data
        ObjectNode binaryNode = mapper.createObjectNode();
        binaryNode.put("bin", mapper.valueFromBytes("test".getBytes()));
        parser = new TreeTraversingParser(binaryNode);
        assertSame(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("bin", parser.getCurrentName());
        assertSame(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        byte[] binaryData = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
        assertEquals("test", new String(binaryData, "UTF-8"));
        parser.close();

        // Test reading binary data
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ObjectNode binaryNode2 = mapper.readTree(inputStream);
        parser = new TreeTraversingParser(binaryNode2);
        byte[] data = new byte[5];
        parser.readBinaryValue(Base64Variant.BASE64_STANDARD, new OutputStream() {
            @Override
            public void write(int b) {
                data[0] = (byte) b;
            }
        });
        assertEquals("test", new String(data, 0, 5));
        parser.close();

        // Test empty JSON
        parser = new TreeTraversingParser(JsonNodeFactory.instance.nullValue());
        assertNull(parser.nextToken());
        parser.close();
    }
}