import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Integer integer = 123;
        String json = mapper.writeValueAsString(integer);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, Integer.class);

        JsonSerializer<Integer> serializer = NumberSerializers.IntegerSerializer.instance;
        JsonNode serializationNode = serializer.serialize(integer, new JsonGenerator(new StringWriter()), new SerializerProvider());
        JsonNode deserializationNode = mapper.readTree(serializationNode.getValueAsString());

        Assert.assertEquals(integer, readValue);
        Assert.assertEquals(integer.intValue(), deserializationNode.asInt());
    }

    @Test
    public void testShortSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Short shortValue = Short.parseShort("127");
        String json = mapper.writeValueAsString(shortValue);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, Short.class);

        JsonSerializer<Short> serializer = NumberSerializers.ShortSerializer.instance;
        JsonNode serializationNode = serializer.serialize(shortValue, new JsonGenerator(new StringWriter()), new SerializerProvider());
        JsonNode deserializationNode = mapper.readTree(serializationNode.getValueAsString());

        Assert.assertEquals(shortValue, readValue);
        Assert.assertEquals(shortValue.shortValue(), deserializationNode.asInt());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Long longValue = 123L;
        String json = mapper.writeValueAsString(longValue);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, Number.class);

        JsonSerializer<Number> serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonNode serializationNode = serializer.serialize(longValue, new JsonGenerator(new StringWriter()), new SerializerProvider());
        JsonNode deserializationNode = mapper.readTree(serializationNode.getValueAsString());

        Assert.assertEquals(longValue.longValue(), readValue.longValue());
        Assert.assertEquals(readValue.longValue(), deserializationNode.asLong());
    }

    @Test
    public void testLongSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Long longValue = 12345L;
        String json = mapper.writeValueAsString(longValue);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, Long.class);

        JsonSerializer<Long> serializer = NumberSerializers.LongSerializer.instance;
        JsonNode serializationNode = serializer.serialize(longValue, new JsonGenerator(new StringWriter()), new SerializerProvider());
        JsonNode deserializationNode = mapper.readTree(serializationNode.getValueAsString());

        Assert.assertEquals(longValue, readValue);
        Assert.assertEquals(longValue.longValue(), deserializationNode.asLong());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatValue = 123.45f;
        String json = mapper.writeValueAsString(floatValue);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, Float.class);

        JsonSerializer<Float> serializer = NumberSerializers.FloatSerializer.instance;
        JsonNode serializationNode = serializer.serialize(floatValue, new JsonGenerator(new StringWriter()), new SerializerProvider());
        JsonNode deserializationNode = mapper.readTree(serializationNode.getValueAsString());

        Assert.assertEquals(floatValue, readValue);
        Assert.assertEquals(floatValue.floatValue(), deserializationNode.asFloat(), 0.001f);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Double doubleValue = 123.45;
        String json = mapper.writeValueAsString(doubleValue);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, Double.class);

        JsonSerializer<Double> serializer = NumberSerializers.DoubleSerializer.instance;
        JsonNode serializationNode = serializer.serialize(doubleValue, new JsonGenerator(new StringWriter()), new SerializerProvider());
        JsonNode deserializationNode = mapper.readTree(serializationNode.getValueAsString());

        Assert.assertEquals(doubleValue, readValue);
        Assert.assertEquals(doubleValue.doubleValue(), deserializationNode.asDouble(), 0.001);
    }

    @Test
    public void testJsonFormatAnnotation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        int intValue = 123;
        String json = mapper.writeValueAsString(intValue);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, JsonDeserializer.None.class);
        int deserializedValue = mapper.readerForMap().readValueAs(JsonNode.class, JsonNode.class).asInt();

        Assert.assertEquals(intValue, readValue);
        Assert.assertEquals(Integer.toString(intValue), deserializedValue);
    }

    @Test
    public void testBigDecimalSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BigDecimal bigDecimal = new BigDecimal("123.45");
        String json = mapper.writeValueAsString(bigDecimal);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, BigDecimal.class);

        JsonSerializer<BigDecimal> serializer = new BaseSerializer<>(BigDecimal.class, JsonParser.NumberType.NUMBER, "number");
        JsonNode serializationNode = serializer.serialize(bigDecimal, new JsonGenerator(new StringWriter()), new SerializerProvider());
        JsonNode deserializationNode = mapper.readTree(serializationNode.getValueAsString());

        Assert.assertEquals(bigDecimal, readValue);
        Assert.assertEquals(bigDecimal.toPlainString(), deserializationNode.asText());
    }

    @Test
    public void testBigIntegerSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        String json = mapper.writeValueAsString(bigInteger);
        JsonNode node = mapper.readTree(json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object readValue = mapper.readValue(parser, BigInteger.class);

        JsonSerializer<BigInteger> serializer = new BaseSerializer<>(BigInteger.class, JsonParser.NumberType.NUMBER, "number");
        JsonNode serializationNode = serializer.serialize(bigInteger, new JsonGenerator(new StringWriter()), new SerializerProvider());
        JsonNode deserializationNode = mapper.readTree(serializationNode.getValueAsString());

        Assert.assertEquals(bigInteger, readValue);
        Assert.assertEquals(bigInteger.toString(), deserializationNode.asText());
    }

    private static class BaseSerializer<T> extends StdScalarSerializer<T>
            implements ContextualSerializer {
        public BaseSerializer(Class<T> cls, JsonParser.NumberType numberType, String schemaType) {
            super(cls, false);
            this._numberType = numberType;
            this._schemaType = schemaType;
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return null;
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            return this;
        }

        private JsonParser.NumberType _numberType;
        private String _schemaType;
    }
}