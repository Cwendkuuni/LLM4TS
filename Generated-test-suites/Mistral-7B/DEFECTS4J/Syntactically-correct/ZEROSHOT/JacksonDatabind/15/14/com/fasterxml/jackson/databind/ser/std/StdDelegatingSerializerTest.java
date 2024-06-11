import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Converter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StdDelegatingSerializerTest {
    private ObjectMapper objectMapper;
    private StdDelegatingSerializer<String, Integer> stringToIntegerDelegateSerializer;
    private StdDelegatingSerializer<Integer, String> integerToStringDelegateSerializer;

    @BeforeMethod
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        Converter<String, Integer> stringToIntegerConverter = new StringToIntegerConverter();
        Converter<Integer, String> integerToStringConverter = new IntegerToStringConverter();
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerJavaType = TypeFactory.defaultInstance().constructType(Integer.class);
        stringToIntegerDelegateSerializer = new StdDelegatingSerializer<>(stringJavaType, stringToIntegerConverter);
        integerToStringDelegateSerializer = new StdDelegatingSerializer<>(integerJavaType, integerToStringConverter);
    }

    @Test
    public void testSerializeStringToInteger() throws JsonMappingException, JsonGenerationException {
        Object input = "123";
        JsonSerializer<Object> delegateSerializer = stringToIntegerDelegateSerializer;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();

        delegateSerializer.serialize(input, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) throws JsonMappingException {
                return new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                        gen.writeNumber(value.toString().charAt(0) - '0');
                    }
                };
            }
        });

        JsonNode actual = jsonGenerator.getOutputTree();
        JsonNode expected = Json.createNumber(1);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSerializeIntegerToString() throws JsonMappingException, JsonGenerationException {
        Object input = 123;
        JsonSerializer<Object> delegateSerializer = integerToStringDelegateSerializer;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();

        delegateSerializer.serialize(input, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) throws JsonMappingException {
                return new StringSerializer();
            }
        });

        JsonNode actual = jsonGenerator.getOutputTree();
        JsonNode expected = Json.createValue("123");
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        JsonSerializer<?> delegateSerializer = stringToIntegerDelegateSerializer;
        SerializerProvider serializerProvider = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type) throws JsonMappingException {
                return new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                        gen.writeNumber(value.toString().charAt(0) - '0');
                    }
                };
            }
        };

        delegateSerializer.resolve(serializerProvider);
        JsonSerializer<String> stringSerializer = (JsonSerializer<String>) delegateSerializer;
        Assert.assertNotNull(stringSerializer);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testInvalidDelegateSerializerType() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = stringToIntegerDelegateSerializer;
        JavaType javaType = TypeFactory.defaultInstance().constructType(Double.class);

        delegateSerializer = delegateSerializer.withDelegate(new Converter<Object, Double>() {}, javaType, null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = stringToIntegerDelegateSerializer;
        JsonFormatVisitorWrapper jsonFormatVisitor = objectMapper.getFactory().getJsonFormatVisitor(null, null);

        delegateSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, null);
        Assert.assertNull(jsonFormatVisitor.getFormatValue());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = stringToIntegerDelegateSerializer;
        SchemaSerializerMap schemaSerializerMap = objectMapper.getSerializationConfig().getSchemaSerializer();

        JsonNode actual = delegateSerializer.getSchema(new SerializerProvider() {}, String.class);
        JsonNode expected = schemaSerializerMap.findSchema(String.class);
        Assert.assertEquals(actual, expected);
    }

    private static class StringToIntegerConverter implements Converter<String, Integer> {
        @Override
        public Integer convert(String value) {
            return Integer.parseInt(value);
        }
    }

    private static class IntegerToStringConverter implements Converter<Integer, String> {
        @Override
        public String convert(Integer value) {
            return value.toString();
        }
    }

    private static class StringSerializer extends StdSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value);
        }
    }
}