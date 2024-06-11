import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;
    private StdDelegatingSerializer<String> stringDelegatingSerializer;
    private StdDelegatingSerializer<Integer> integerDelegatingSerializer;

    @Before
    public void setup() throws JsonProcessingException {
        Converter<Object, String> stringConverter = new SimpleStringConverter();
        Converter<Object, Integer> integerConverter = new SimpleIntegerConverter();
        ObjectMapper mapper = new ObjectMapper();

        stringDelegatingSerializer = new StdDelegatingSerializer<String>(stringConverter) {{
            _delegateSerializer = mapper.findValueSerializer(String.class);
        }};

        integerDelegatingSerializer = new StdDelegatingSerializer<Integer>(Integer.class, integerConverter) {{
            _delegateSerializer = mapper.findValueSerializer(Integer.class);
        }};

        this.objectMapper = mapper;
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, JsonMappingException, IOException {
        String source = "hello";
        Object delegateValue = stringDelegatingSerializer.convertValue(source);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();

        stringDelegatingSerializer.serialize(source, gen, objectMapper.getSerializerProvider());
        stringDelegatingSerializer.serialize(source, gen, objectMapper.getSerializerProvider(), mapper.getTypeSerializer());

        Assert.assertEquals(stringDelegatingSerializer.getDelegateSerializer().serializeValue(delegateValue, gen, objectMapper.getSerializerProvider()),
                stringDelegatingSerializer.getDelegateSerializer().serializeValue(delegateValue, gen, objectMapper.getSerializerProvider(), mapper.getTypeSerializer()));
        String result = gen.getOutputContent().toString();
        JsonNode expectedSchema = mapper.getFactory().createParser("{\"type\":\"string\"}").readValueAsTree();
        JsonNode actualSchema = stringDelegatingSerializer.getSchema(objectMapper.getSerializerProvider(), String.class);

        Assert.assertEquals(result, "\"hello\"");
        Assert.assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException, JsonMappingException, IOException {
        Integer source = 42;
        Object delegateValue = integerDelegatingSerializer.convertValue(source);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();

        integerDelegatingSerializer.serialize(source, gen, objectMapper.getSerializerProvider());
        integerDelegatingSerializer.serialize(source, gen, objectMapper.getSerializerProvider(), mapper.getTypeSerializer());

        Assert.assertEquals(integerDelegatingSerializer.getDelegateSerializer().serializeValue(delegateValue, gen, objectMapper.getSerializerProvider()),
                integerDelegatingSerializer.getDelegateSerializer().serializeValue(delegateValue, gen, objectMapper.getSerializerProvider(), mapper.getTypeSerializer()));
        String result = gen.getOutputContent().toString();
        JsonNode expectedSchema = mapper.getFactory().createParser("{\"type\":\"number\"}").readValueAsTree();
        JsonNode actualSchema = integerDelegatingSerializer.getSchema(objectMapper.getSerializerProvider(), Integer.class);

        Assert.assertEquals(result, "42");
        Assert.assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Class<Integer> intType = Integer.class;
        Class<String> stringType = String.class;

        Converter<Object, Integer> intConverter = new SimpleIntegerConverter();
        Converter<Object, String> stringConverter = new SimpleStringConverter();

        StdDelegatingSerializer<String> stringDelegatingSerializer = new StdDelegatingSerializer<String>(stringConverter) {{
            _delegateSerializer = objectMapper.findValueSerializer(String.class);
        }};

        StdDelegatingSerializer<Integer> intDelegatingSerializer = new StdDelegatingSerializer<Integer>(intType, intConverter) {{
            _delegateSerializer = objectMapper.findValueSerializer(intType);
        }};

        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        StdDelegatingSerializer<Object> objectDelegatingSerializer = new StdDelegatingSerializer<Object>(intType, intConverter) {{
            _delegateSerializer = intDelegatingSerializer;
        }};

        StdDelegatingSerializer<?> resolvedSerializer = intDelegatingSerializer.withDelegate(stringConverter, stringType, stringDelegatingSerializer);
        StdDelegatingSerializer<?> resolvedSerializer2 = stringDelegatingSerializer.withDelegate(intConverter, intType, intDelegatingSerializer);

        Assert.assertEquals(intDelegatingSerializer.getClass(), resolvedSerializer.getClass());
        Assert.assertEquals(stringDelegatingSerializer.getClass(), resolvedSerializer2.getClass());
        Assert.assertEquals(intDelegatingSerializer, resolvedSerializer.getDelegateSerializer());
        Assert.assertEquals(stringDelegatingSerializer, resolvedSerializer2.getDelegateSerializer());
    }

    @Test
    public void testEmpty() throws JsonMappingException {
        String nullString = null;
        Integer nullInteger = null;

        Assert.assertFalse(stringDelegatingSerializer.isEmpty(nullString));
        Assert.assertFalse(integerDelegatingSerializer.isEmpty(nullInteger));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expectedSchemaString = mapper.getFactory().createParser("{\"type\":\"string\"}").readValueAsTree();
        JsonNode expectedSchemaInteger = mapper.getFactory().createParser("{\"type\":\"number\"}").readValueAsTree();

        JsonNode actualSchemaString = stringDelegatingSerializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        JsonNode actualSchemaInteger = integerDelegatingSerializer.getSchema(objectMapper.getSerializerProvider(), Integer.class);

        Assert.assertEquals(expectedSchemaString, actualSchemaString);
        Assert.assertEquals(expectedSchemaInteger, actualSchemaInteger);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(SerializerProvider.StandardSerializers.FIND_FOR_TYPE, String.class);
        JsonFormatVisitorWrapper visitor2 = mapper.schemaFactory().getJsonFormatVisitor(SerializerProvider.StandardSerializers.FIND_FOR_TYPE, Integer.class);

        stringDelegatingSerializer.acceptJsonFormatVisitor(visitor, Object.class);
        integerDelegatingSerializer.acceptJsonFormatVisitor(visitor2, Object.class);

        String schemaString = visitor.getSchema(Object.class);
        String schemaInteger = visitor2.getSchema(Object.class);

        Assert.assertNotNull(schemaString);
        Assert.assertNotNull(schemaInteger);
    }

    private static class SimpleStringConverter implements Converter<Object, String> {
        @Override
        public String convert(Object value) {
            return value.toString();
        }
    }

    private static class SimpleIntegerConverter implements Converter<Object, Integer> {
        @Override
        public Integer convert(Object value) {
            return (Integer) value;
        }
    }
}