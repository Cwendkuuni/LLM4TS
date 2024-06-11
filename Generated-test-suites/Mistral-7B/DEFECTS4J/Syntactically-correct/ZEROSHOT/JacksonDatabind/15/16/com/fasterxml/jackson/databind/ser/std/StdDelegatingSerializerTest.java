import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.NodeBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ConvertedType;
import java.util.Converter;
import java.util.Converter.SuperConverter;
import java.util.function.Function;

public class StdDelegatingSerializerTest {

    private static final Class<Object> OBJECT_CLASS = Object.class;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Function<Object, Integer> INT_CONVERTER = Object::hashCode;
    private static final Converter<Object, Integer> INT_CONVERTER_IMPL = new Converter.Std(INT_CONVERTER) {
        @Override
        public Integer convert(Object src) {
            return INT_CONVERTER.apply(src);
        }
    };

    @SuppressWarnings("unchecked")
    private static final StdDelegatingSerializer<Integer> INT_DELEGATOR = new StdDelegatingSerializer<>(INT_CONVERTER_IMPL,
            ObjectTypeFactory.intType(), new IntSerializer());

    private static final Class<Double> DOUBLE_CLASS = Double.class;
    private static final ObjectTypeFactory typeFactory = MAPPER.getTypeFactory();

    @BeforeClass
    public void setup() throws IOException {
        // Set up a custom serializer for the delegate type
        ObjectNode intSerializerConfig = NodeBuilder.objectNode();
        intSerializerConfig.put("numberType", "INT");
        MAPPER.registerSerializer(Integer.class, new IntSerializer());
        MAPPER.registerSerializer(typeFactory.constructType(IntSerializer.class), INT_DELEGATOR);
        MAPPER.registerSerializer(JavaType.constructType(StdDelegatingSerializer.class), INT_DELEGATOR);
    }

    @Test
    public void testSimpleConversion() throws JsonGenerationException, JsonMappingException, IOException {
        SerializerProvider serializerProvider = MAPPER.getSerializerProvider();
        Object value = new Object();
        String expectedJson = "{\"delegate\":315624113}";
        ObjectNode result = MAPPER.writeValueAsTree(value, serializerProvider);
        Assert.assertEquals(result.toString(), expectedJson);
    }

    @Test
    public void testConversionWithTypeHint() throws JsonGenerationException, JsonMappingException, IOException {
        SerializerProvider serializerProvider = MAPPER.getSerializerProvider();
        Object value = new Object();
        Type intType = typeFactory.constructType(Integer.class);
        ObjectNode result = MAPPER.writeValueAsTree(value, serializerProvider, intType);
        Assert.assertEquals(result.toString(), "{\"delegate\":315624113,\"numberType\":\"INT\"}");
    }

    @Test
    public void testContextualization() throws JsonGenerationException, JsonMappingException, IOException {
        SerializerProvider serializerProvider = MAPPER.getSerializerProvider();
        Object objectWithIntValue = new Object() {{
            int intValue = 42;
        }};
        ObjectNode result = MAPPER.writeValueAsTree(objectWithIntValue, serializerProvider);
        Assert.assertEquals(result.toString(), "{\"delegate\":42,\"numberType\":\"INT\"}");
    }

    @Test
    public void testContextualizationWithCustomDelegateSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        SerializerProvider serializerProvider = MAPPER.getSerializerProvider();
        Object objectWithDoubleValue = new Object() {{
            double doubleValue = 3.14;
        }};
        ObjectNode result = MAPPER.writeValueAsTree(objectWithDoubleValue, serializerProvider);
        Assert.assertEquals(result.toString(), "{\"delegate\":3.14}");
    }

    @Test
    public void testContextualizationWithSuperConverter() throws JsonGenerationException, JsonMappingException, IOException {
        // Test a super-converter which converts both Integer and Double to String
        Converter<Object, String> stringConverter = Object::toString;
        SuperConverter<Object, String> stringSuperConverter = new SuperConverter<Object, String>() {
            @Override
            public String convert(Object src) {
                return ((Converter<Object, ?>) stringConverter).convert(src);
            }

            @Override
            public String convertAndSet(Object src, String target) {
                return ((Converter<Object, String>) stringConverter).convertAndSet(src, target);
            }
        };
        StdDelegatingSerializer<String> stringDelegator = new StdDelegatingSerializer<>(stringSuperConverter,
                ObjectTypeFactory.stringType(), new StringSerializer());

        SerializerProvider serializerProvider = MAPPER.getSerializerProvider();
        Object objectWithIntValue = new Object() {{
            int intValue = 42;
        }};
        ObjectNode result = MAPPER.writeValueAsTree(objectWithIntValue, serializerProvider);
        Assert.assertEquals(result.toString(), "{\"delegate\":\"42\"}");

        Object objectWithDoubleValue = new Object() {{
            double doubleValue = 3.14;
        }};
        result = MAPPER.writeValueAsTree(objectWithDoubleValue, serializerProvider);
        Assert.assertEquals(result.toString(), "{\"delegate\":\"3.14\"}");
    }

    @Test
    public void testNegativeCases() throws JsonGenerationException, JsonMappingException, IOException {
        // Test for null delegate and delegate and source type being the same
        Object objectWithNullValue = null;
        SerializerProvider serializerProvider = MAPPER.getSerializerProvider();
        try {
            MAPPER.writeValueAsTree(objectWithNullValue, serializerProvider);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Object objectWithSameValue = new Object() {{
            int intValue = 42;
        }};
        try {
            StdDelegatingSerializer<Integer> sameValueDelegator = new StdDelegatingSerializer<>(INT_CONVERTER_IMPL,
                    ObjectTypeFactory.intType(), new IntSerializer());
            MAPPER.registerSerializer(JavaType.constructType(StdDelegatingSerializer.class), sameValueDelegator);
            ObjectNode result = MAPPER.writeValueAsTree(objectWithSameValue, serializerProvider);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        } finally {
            MAPPER.getSerializerProvider().removeSerializer(JavaType.constructType(StdDelegatingSerializer.class));
        }
    }
}

class IntSerializer extends StdSerializer<Integer> {
    public IntSerializer() {
        this(null);
    }

    public IntSerializer(Class<Integer> t) {
        super(t);
    }

    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.intValue());
    }
}

class StringSerializer extends StdSerializer<String> {
    public StringSerializer() {
        this(null);
    }

    public StringSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value);
    }
}