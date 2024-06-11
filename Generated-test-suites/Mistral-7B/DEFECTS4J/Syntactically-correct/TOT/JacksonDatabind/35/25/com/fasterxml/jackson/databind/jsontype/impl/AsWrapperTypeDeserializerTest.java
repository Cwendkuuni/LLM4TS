package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.As;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;

public class AsWrapperTypeDeserializerTest extends BaseMapTest {

    static class DummyClass {
        private String name;

        public DummyClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static class DummySerializer extends StdSerializer<DummyClass> {
            public DummySerializer() {
                this(null);
            }

            protected DummySerializer(Class<DummyClass> t) {
                this(null);
            }

            public DummySerializer(Class<DummyClass> t, java.lang.reflect.Constructor<?> c, java.lang.Class<?> w) {
                super(t, c, w);
            }

            @Override
            public void serialize(DummyClass value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeStartObject();
                jgen.writeFieldName("typeId");
                jgen.writeString(As.DEFAULT_TYPE_ID_STRING);
                jgen.writeFieldName("value");
                jgen.writeValue(value.getName());
                jgen.writeEndObject();
            }
        }
    }

    @BeforeClass
    public static void setup() throws Exception {
        SimpleModule module = new SimpleModule();
        module.addSerializer(DummyClass.class, new DummySerializer());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @Test
    public void testDeserializeFromObject() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("{\"typeId\":\"DummyClass\", \"value\":\"test\"}");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                objectMapper.getTypeFactory().constructType(DummyClass.class),
                objectMapper.getTypeIdResolver(), "typeId", true, DummyClass.class);
        Object result = deserializer.deserializeTypedFromObject(jp, context);
        assertEquals("test", result.getClass().getMethod("getName").invoke(result));
    }

    @Test
    public void testDeserializeFromArray() throws IOException {
        String json = "[{\"typeId\":\"DummyClass\", \"value\":\"test1\"}, {\"typeId\":\"DummyClass\", \"value\":\"test2\"}]";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        Object[] result = deserializer.deserializeTypedFromArray(jp, context);
        assertEquals(2, result.length);
        for (Object obj : result) {
            assertEquals("test1", ((DummyClass) obj).getName());
            assertEquals("test2", ((DummyClass) (result[1])).getName());
        }
    }

    @Test
    public void testDeserializeFromScalar() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("{\"typeId\":\"DummyClass\", \"value\":\"test\"}");
        jp.nextToken(); // consume "{"
        jp.nextToken(); // consume "typeId"
        jp.nextToken(); // consume "DummyClass"
        jp.nextToken(); // consume ","
        jp.nextToken(); // consume "value"
        jp.nextToken(); // consume "test"
        jp.nextToken(); // consume "}"
        Object result = deserializer.deserializeTypedFromScalar(jp, context);
        assertEquals("test", result.getClass().getMethod("getName").invoke(result));
    }

    @Test
    public void testDeserializeFromAny() throws IOException {
        String json = "[1, 2.0, null, {\"typeId\":\"DummyClass\", \"value\":\"test\"}]";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        jp.nextToken(); // consume "[
        jp.nextToken(); // consume 1
        jp.nextToken(); // consume 2.0
        jp.nextToken(); // consume null
        Object result = deserializer.deserializeTypedFromAny(jp, context);
        assertNull(result);
        jp.nextToken(); // consume ","
        Object wrappedResult = deserializer.deserializeTypedFromAny(jp, context);
        assertEquals("test", wrappedResult.getClass().getMethod("getName").invoke(wrappedResult));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        String json = "{\"typeId\":\"DummyClass\", \"value\":{\"name\":\"test\"}}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        Object result = deserializer.deserializeWithNativeTypeId(jp, context);
        assertEquals("test", result.getClass().getMethod("getName").invoke(result));
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidTypeId() throws IOException {
        String json = "{\"typeId\":\"InvalidTypeId\", \"value\":{\"name\":\"test\"}}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        deserializer.deserializeWithNativeTypeId(jp, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithMissingTypeId() throws IOException {
        String json = "{\"value\":{\"name\":\"test\"}}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        deserializer.deserializeWithNativeTypeId(jp, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeIdThatDoesNotExist() throws IOException {
        String json = "{\"typeId\":\"MissingTypeId\", \"value\":{\"name\":\"test\"}}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        deserializer.deserializeWithNativeTypeId(jp, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJsonStructure() throws IOException {
        String json = "[1, 2.0]";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        deserializer.deserializeWithNativeTypeId(jp, context);
    }

    @Test
    public void testDeserializeWithInvisibleTypeId() throws IOException {
        String json = "{\"value\":{\"name\":\"test\"}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper contextMapper = new ObjectMapper();
        contextMapper.disable(SerializationFeature.WRITE_TYPE_ID_FOR_CLASS_ANNOTATIONS);
        contextMapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeWithTypeId(jp, contextMapper);
        assertEquals("test", result.getClass().getMethod("getName").invoke(result));
    }

    @After
    public void tearDown() throws Exception {
        objectMapper = null;
    }

    private static ObjectMapper objectMapper;
    private static DeserializationContext context;
}