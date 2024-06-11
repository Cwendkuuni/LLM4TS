import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        JsonToken token = parser.nextToken();
        Assert.assertNull(token);
        parser.close();
    }

    @Test
    public void testSimpleParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonObject());
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        String name = parser.getCurrentName();
        Assert.assertEquals("root", name);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        name = parser.getCurrentName();
        Assert.assertEquals("name", name);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        String value = parser.getText();
        Assert.assertEquals("John Doe", value);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        name = parser.getCurrentName();
        Assert.assertEquals("age", name);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        int valueInt = parser.getIntValue();
        Assert.assertEquals(30, valueInt);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testArrayParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonArray());
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        int valueInt = parser.getIntValue();
        Assert.assertEquals(1, valueInt);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        String valueString = parser.getText();
        Assert.assertEquals("Hello", valueString);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        float valueFloat = parser.getFloatValue();
        Assert.assertEquals(3.14f, valueFloat, 0.01f);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test
    public void testNullParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonNull());
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.NULL, token);
        parser.close();
    }

    @Test
    public void testEmptyObjectParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonEmptyObject());
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testEmptyArrayParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonEmptyArray());
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test
    public void testClosedParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonObject());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.close();
        JsonToken token = parser.nextToken();
        Assert.assertNull(token);
    }

    @Test(expected = IOException.class)
    public void testBadParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser("{bad:json}");
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testUnexpectedTokenParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonObject());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken(); // unexpected token
        parser.nextToken(); // should throw an exception
    }

    @Test
    public void testBinaryParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonBinary());
        byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int readBytes = parser.readBinaryValue(Base64Variant.STANDARD, outputStream);
        Assert.assertArrayEquals(binaryValue, outputStream.toByteArray());
        Assert.assertEquals(binaryValue.length, readBytes);
        parser.close();
    }

    @Test
    public void testNumericParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonNumeric());
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        int valueInt = parser.getIntValue();
        Assert.assertEquals(42, valueInt);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        float valueFloat = parser.getFloatValue();
        Assert.assertEquals(3.14f, valueFloat, 0.01f);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    private ObjectNode createTestJsonObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (ObjectNode) mapper.readTree("{\"name\":\"John Doe\", \"age\":30}");
    }

    private ArrayNode createTestJsonArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (ArrayNode) mapper.readTree("[1,\"Hello\",3.14]");
    }

    private TextNode createTestJsonNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (TextNode) mapper.readTree("null");
    }

    private ObjectNode createTestJsonEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (ObjectNode) mapper.readTree("{}");
    }

    private ArrayNode createTestJsonEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (ArrayNode) mapper.readTree("[]");
    }

    private ObjectNode createTestJsonBinary() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("binary", mapper.valueToTree(new byte[]{1, 2, 3}));
        return root;
    }

    private ObjectNode createTestJsonNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("int", mapper.valueToTree(42));
        root.set("float", mapper.valueToTree(3.14));
        return root;
    }

    @Test
    public void testSkipChildren() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestJsonObject());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }
}