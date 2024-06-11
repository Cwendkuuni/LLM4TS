import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NumberSerializersTest {

    @Test
    public void testNumberSerializers() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization of int, short, byte, long, float, and double
        JsonSerializer<?> intSerializer = mapper.findValueSerializer(Integer.class);
        JsonSerializer<?> shortSerializer = mapper.findValueSerializer(Short.class);
        JsonSerializer<?> byteSerializer = mapper.findValueSerializer(Byte.class);
        JsonSerializer<?> longSerializer = mapper.findValueSerializer(Long.class);
        JsonSerializer<?> floatSerializer = mapper.findValueSerializer(Float.class);
        JsonSerializer<?> doubleSerializer = mapper.findValueSerializer(Double.class);

        Assert.assertNotNull(intSerializer);
        Assert.assertSame(IntegerSerializer.class, intSerializer.getClass());

        Assert.assertNotNull(shortSerializer);
        Assert.assertSame(ShortSerializer.class, shortSerializer.getClass());

        Assert.assertNotNull(byteSerializer);
        Assert.assertSame(IntLikeSerializer.class, byteSerializer.getClass());

        Assert.assertNotNull(longSerializer);
        Assert.assertSame(LongSerializer.class, longSerializer.getClass());

        Assert.assertNotNull(floatSerializer);
        Assert.assertSame(FloatSerializer.class, floatSerializer.getClass());

        Assert.assertNotNull(doubleSerializer);
        Assert.assertSame(DoubleSerializer.class, doubleSerializer.getClass());

        // Test deserialization of int, short, byte, long, float, and double
        JsonParser parser = mapper.getFactory().createParser("123");
        IntSerializer intDeserializer = new IntSerializer();
        Short deserializableShort = intDeserializer.deserialize(parser, Short.class);
        Assert.assertEquals(deserializableShort, (short) 123);

        Byte deserializableByte = intDeserializer.deserialize(parser, Byte.class);
        Assert.assertEquals(deserializableByte, (byte) 123);

        Long deserializableLong = intDeserializer.deserialize(parser, Long.class);
        Assert.assertEquals(deserializableLong, 123L);

        Float deserializableFloat = intDeserializer.deserialize(parser, Float.class);
        Assert.assertEquals(deserializableFloat, 123F);

        Double deserializableDouble = intDeserializer.deserialize(parser, Double.class);
        Assert.assertEquals(deserializableDouble, 123D);

        parser.nextToken(); // consume extra ','
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        AnnotatedMember member = new AnnotatedMember(new StdClassAnnotatedMember(Short.class, "shortField"), mapper.getAnnotationIntrospector());
        JsonParser parserWithFormat = mapper.getFactory().createParser("123");
        ObjectReader reader = mapper.readerForMap().withValueSeparator('&').withRootName("root").readerFor(Map.class);
        Map<String, Object> map = reader.readValue(parserWithFormat);
        JsonSerializer<?> serializerWithFormat = mapper.findValueSerializer(Short.class);
        Object shortValue = serializerWithFormat.deserialize(parserWithFormat, Short.class);
        Assert.assertEquals(shortValue, (short) 123);
        Assert.assertEquals(map.get("shortField"), shortValue);

        // Test serialization and deserialization with custom JsonFormat
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        static class TestClass {
            private Short testShort;

            public Short getTestShort() {
                return testShort;
            }

            public void setTestShort(Short testShort) {
                this.testShort = testShort;
            }
        }

        TestClass testClass = new TestClass();
        testClass.setTestShort((short) 123);

        ObjectWriter writer = mapper.writerFor(TestClass.class);
        String json = writer.writeValueAsString(testClass);
        ObjectMapper testMapper = new ObjectMapper();
        TestClass deserializedTestClass = testMapper.readValue(json, TestClass.class);
        Assert.assertEquals(deserializedTestClass.getTestShort(), (short) 123);
    }
}

class IntSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        integerSerializer.serialize(value, gen, provider);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new IntegerSerializer().deserialize(p, ctxt.constructType(Integer.TYPE));
    }
}