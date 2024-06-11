import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper.JsonFormatVisitors;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdKeySerializerTest {

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.UTF8);

        String testValue = "test_key";
        serializer.serialize(testValue, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.close();

        ObjectMapper reader = new ObjectMapper();
        JsonNode jsonNode = reader.readTree(new File("output.json"));

        Assert.assertEquals(testValue, jsonNode.asText());
    }

    @Test
    public void testSerialize_NullValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.UTF8);

        Object testValue = null;
        serializer.serialize(testValue, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.close();

        ObjectMapper reader = new ObjectMapper();
        JsonNode jsonNode = reader.readTree(new File("output.json"));
        Assert.assertNull(jsonNode);
    }

    @Test
    public void testSerialize_EmptyString() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.UTF8);

        String testValue = "";
        serializer.serialize(testValue, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.close();

        ObjectMapper reader = new ObjectMapper();
        JsonNode jsonNode = reader.readTree(new File("output.json"));
        Assert.assertEquals("", jsonNode.asText());
    }

    @Test
    public void testSerialize_LargeString() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.UTF8);

        String testValue = new String(new char[1024 * 16]); // 16KB string
        serializer.serialize(testValue, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.close();

        ObjectMapper reader = new ObjectMapper();
        JsonNode jsonNode = reader.readTree(new File("output.json"));
        Assert.assertEquals(testValue, jsonNode.asText());
    }

    @Test
    public void testSerialize_DifferentDataTypes() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.UTF8);

        // Test with Date
        Date date = new Date();
        serializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.close();

        ObjectMapper reader = new ObjectMapper();
        JsonNode jsonNode = reader.readTree(new File("output.json"));
        Assert.assertTrue(jsonNode instanceof JsonNode);
        Assert.assertFalse(jsonNode.isTextual());

        // Test with Integer
        int intValue = 123;
        serializer.serialize(intValue, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.close();

        ObjectMapper intReader = new ObjectMapper();
        JsonNode intJsonNode = intReader.readTree(new File("output.json"));
        Assert.assertEquals(Integer.toString(intValue), intJsonNode.asText());

        // Test with Long
        long longValue = 123456789;
        serializer.serialize(longValue, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.close();

        ObjectMapper longReader = new ObjectMapper();
        JsonNode longJsonNode = longReader.readTree(new File("output.json"));
        Assert.assertEquals(Long.toString(longValue), longJsonNode.asText());
    }

    @Test(expected = IOException.class)
    public void testSerialize_IOException() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new File("output.json"), JsonEncoding.UTF8);

        String testValue = "test_key";
        // Simulate IOException by providing a null File
        serializer.serialize(testValue, jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JavaType javaType = TypeFactory.defaultInstance().constructType(Object.class);
        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), javaType);

        Assert.assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getJsonFormatVisitors();

        StdKeySerializer serializer = new StdKeySerializer();
        serializer.acceptJsonFormatVisitor(visitor, JavaType.constructType(Object.class));

        JsonFormatVisitors jsonFormatVisitors = visitor.getFormatVisitors();
        Assert.assertTrue(jsonFormatVisitors instanceof JsonStringFormatVisitor);
    }
}