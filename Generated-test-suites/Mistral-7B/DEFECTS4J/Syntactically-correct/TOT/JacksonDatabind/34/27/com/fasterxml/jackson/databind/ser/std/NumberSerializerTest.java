import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class NumberSerializerTest {

    private static final ObjectMapper objectMapper = JsonMapperFactory.getInstance();

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        BigDecimal bigDecimal = new BigDecimal("123.456");
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();
        String expectedJson = "123.456";
        ObjectNode actualJson = objectMapper.readTree(System.in);
        Assert.assertEquals(expectedJson, actualJson.asText());
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();
        String expectedJson = "123456789012345678901234567890";
        ObjectNode actualJson = objectMapper.readTree(System.in);
        Assert.assertEquals(expectedJson, actualJson.asText());
    }

    @Test
    public void testSerializeInvalidNumber() throws JsonProcessingException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = new NumberSerializer(String.class);
        String invalidNumber = "abc";
        try {
            numberSerializer.serialize(invalidNumber, objectMapper.getGenerator(), objectMapper.getSerializerProvider());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonProcessingException, JsonMappingException {
        String json = "\"123.456\"";
        ObjectNode jsonNode = objectMapper.readTree(json);
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);
        ObjectMapper deserializer = JsonMapperFactory.getInstance();
        Number deserializedNumber = deserializer.readValue(jsonParser, Number.class);
        Assert.assertEquals(new BigDecimal("123.456"), deserializedNumber);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonProcessingException, JsonMappingException {
        String json = "\"123456789012345678901234567890\"";
        ObjectNode jsonNode = objectMapper.readTree(json);
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);
        ObjectMapper deserializer = JsonMapperFactory.getInstance();
        Number deserializedNumber = deserializer.readValue(jsonParser, Number.class);
        Assert.assertEquals(new BigInteger("123456789012345678901234567890"), deserializedNumber);
    }

    @Test
    public void testDeserializeInvalidJson() throws JsonProcessingException, JsonMappingException {
        String json = "\"abc\"";
        ObjectNode jsonNode = objectMapper.readTree(json);
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonNode);
        ObjectMapper deserializer = JsonMapperFactory.getInstance();
        try {
            deserializer.readValue(jsonParser, Number.class);
            Assert.fail();
        } catch (JsonProcessingException e) {
            // Expected exception
        }
    }
}