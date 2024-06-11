package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonPOJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonNode node;

    @Before
    public void setup() throws JsonProcessingException {
        mapper = new ObjectMapper();
        node = mapper.readTree("{\"name\":\"John\",\"age\":30,\"address\":{\"street\":\"123 Main St\",\"city\":\"Anytown\"},\"hobbies\":[\"reading\",\"swimming\",\"hiking\"]}");
    }

    @Test
    public void testNameAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node);
        Assert.assertEquals("name", parser.nextToken().getName());
        Assert.assertEquals("John", parser.nextText());
        parser.close();
    }

    @Test
    public void testArrayAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("hobbies"));
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals("reading", parser.nextText());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("swimming", parser.nextText());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("hiking", parser.nextText());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testObjectAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("address"));
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("street", parser.nextToken().getName());
        Assert.assertEquals("123 Main St", parser.nextText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("city", parser.nextToken().getName());
        Assert.assertEquals("Anytown", parser.nextText());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testEmptyArrayAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("emptyArray"));
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testEmptyObjectAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("emptyObject"));
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidTokenAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // consume name token
        parser.nextText(); // consume "John"
        parser.nextToken(); // consume "age"
        parser.nextToken(); // consume number token
        parser.nextText(); // consume "30"
        parser.nextToken(); // consume "address"
        parser.nextToken(); // consume "{"
        parser.nextToken(); // consume "street"
        parser.nextToken(); // consume "123 Main St"
        parser.nextToken(); // consume "}"
        parser.nextToken(); // consume "hobbies"
        parser.nextToken(); // consume "["
        parser.nextText(); // should fail here
    }

    @Test
    public void testNullAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("nullValue"));
        Assert.assertEquals(JsonToken.NULL, parser.nextToken());
        parser.close();
    }

    @Test
    public void testBigIntegerAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("bigIntegerValue"));
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(new BigInteger("12345678901234567890"), parser.nextBigIntegerValue());
        parser.close();
    }

    @Test
    public void testBigDecimalAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("bigDecimalValue"));
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        Assert.assertEquals(new BigDecimal("123.456.789.123"), parser.nextBigDecimalValue());
        parser.close();
    }

    @Test
    public void testPOJONodeAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("pojoNode"));
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        JsonNode pojoNode = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, pojoNode.nextToken().getTokenType());
        Assert.assertEquals("John", ((JsonPOJONode) pojoNode).getPojo().getName());
        Assert.assertEquals(30, ((JsonPOJONode) pojoNode).getPojo().getAge());
        parser.close();
    }

    @Test
    public void testBinaryAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node.get("binaryNode"));
        byte[] binaryData = parser.nextBinaryValue(JsonEncoding.Base64Standard);

        String expectedBase64 = "VG8gZXJyIGlzIGh1bWFuLCBzdHlsZT0nZm9udCwKCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHN1YnR5cGUgZ29pbmcgZnVsbH1z";
        Assert.assertArrayEquals(expectedBase64.getBytes(), binaryData);

        parser.close();
    }

    @Test
    public void testClosedParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken();
        parser.nextText();
        parser.nextToken();
        parser.nextText();
        parser.nextToken();
        parser.nextText();
        parser.nextToken();
        parser.nextText();
        parser.nextToken();
        parser.close();

        try {
            parser.nextToken();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }
}