import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;
    private Converter<String, Integer> converter;
    private StdDelegatingSerializer<Object> delegatingSerializer;
    private StdSerializer<Integer> intSerializer;

    @BeforeMethod
    public void setup() {
        objectMapper = new ObjectMapper();
        converter = objectMapper.getTypeFactory().constructConverter(String.class, Integer.class);
        intSerializer = objectMapper.findValueSerializer(Integer.class);
        delegatingSerializer = new StdDelegatingSerializer<Object>(converter);
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        Object input = null;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new byte[0]);
        delegatingSerializer.serialize(input, gen, objectMapper.getSerializerProvider());
        Assert.assertTrue(gen.isClosed());
    }

    @Test
    public void testSerializeStringToInteger() throws JsonProcessingException {
        Object input = "42";
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new byte[0]);
        delegatingSerializer.serialize(input, gen, objectMapper.getSerializerProvider());
        Assert.assertEquals(gen.getOutputValue(), "42");
        gen.close();

        Object deserialized = objectMapper.readValue(gen.getOutputStream(), Object.class);
        Assert.assertEquals(deserialized, 42);
    }

    @Test
    public void testSerializeCustomClassToInteger() throws JsonProcessingException {
        Object input = new Object() {};
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new byte[0]);

        delegatingSerializer = delegatingSerializer.withDelegate(converter, JavaType.constructType(Integer.class), intSerializer);
        delegatingSerializer.serialize(input, gen, objectMapper.getSerializerProvider());
        Assert.assertEquals(gen.getOutputValue(), "null");

        gen.writeStartObject();
        gen.writeFieldName("number");
        gen.writeNumber(42);
        gen.writeEndObject();

        Object deserialized = objectMapper.readTree(gen.getOutputStream()).path("number").asInt();
        Assert.assertEquals(deserialized, 42);
        gen.close();
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Assert.assertNull(delegatingSerializer.getDelegateSerializer());
        delegatingSerializer.resolve(objectMapper.getSerializerProvider());
        Assert.assertNotNull(delegatingSerializer.getDelegateSerializer());
        Assert.assertEquals(delegatingSerializer.getDelegateSerializer().getClass(), intSerializer.getClass());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        Assert.assertNull(delegatingSerializer.getDelegateSerializer());
        delegatingSerializer = delegatingSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        Assert.assertNotNull(delegatingSerializer.getDelegateSerializer());
        Assert.assertEquals(delegatingSerializer.getDelegateSerializer().getClass(), intSerializer.getClass());
    }

    @Test
    public void testSerializeWithType() throws JsonProcessingException {
        Object input = "42";
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new byte[0]);
        TypeSerializer typeSerializer = objectMapper.getSerializerProvider().getTypeSerializer();

        delegatingSerializer = delegatingSerializer.withDelegate(converter, JavaType.constructType(Integer.class), intSerializer);
        delegatingSerializer.serializeWithType(input, gen, objectMapper.getSerializerProvider(), typeSerializer);

        Assert.assertEquals(gen.getOutputValue(), "42");
        gen.close();

        Object deserialized = objectMapper.readValue(gen.getOutputStream(), Object.class);
        Assert.assertEquals(deserialized, 42);
    }

    @Test
    public void testGetOutputType() throws JsonMappingException {
        Assert.assertNull(delegatingSerializer.getDelegateType());
        delegatingSerializer = new StdDelegatingSerializer<>(converter, Integer.class);
        Assert.assertEquals(delegatingSerializer.getDelegateType().getRawClass(), Integer.class);
    }
}