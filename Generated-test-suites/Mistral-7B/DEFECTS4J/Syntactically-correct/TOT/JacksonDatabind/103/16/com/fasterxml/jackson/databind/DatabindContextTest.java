package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabindContextTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetConfig() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        AnnotationIntrospector ai = ctx.getAnnotationIntrospector();
        MapperConfig<?> config = ctx.getConfig();
        assertNotNull(config);
        assertEquals(ai, config.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled_BooleanFeature() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        assertTrue(ctx.isEnabled(MapperFeature.DEFAULT_INTENTS_RESOLVER));
        assertFalse(ctx.isEnabled(MapperFeature.USE_STATIC_TYPES));
        mapper.enable(MapperFeature.USE_STATIC_TYPES);
        assertTrue(ctx.isEnabled(MapperFeature.USE_STATIC_TYPES));
    }

    @Test
    public void testGetActiveView() {
        JavaType jsonView = mapper.getTypeFactory().constructType(Object.class);
        DeserializationContext ctx = mapper.getDeserializationContext();
        ctx.setActiveView(jsonView);
        assertEquals(jsonView, ctx.getActiveView());
        ctx.setActiveView(null);
        assertNull(ctx.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        assertEquals(Locale.getDefault(), ctx.getLocale());
        mapper.setLocale(Locale.US);
        assertEquals(Locale.US, ctx.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        assertEquals(TimeZone.getDefault(), ctx.getTimeZone());
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(TimeZone.getTimeZone("UTC"), ctx.getTimeZone());
    }

    @JsonSerialize(using = MyNumberSerializer.class)
    static class MyNumber {
        private int value;

        public MyNumber() {}

        public MyNumber(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    static class MyNumberSerializer extends StdSerializer<MyNumber> {

        public MyNumberSerializer() {
            this(null);
        }

        public MyNumberSerializer(Class<MyNumber> t) {
            this(null);
        }

        public MyNumberSerializer(Class<MyNumber> t, AnnotationIntrospector ai) {
            this(t);
        }

        @Override
        public void serialize(MyNumber myNumber, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            jsonGenerator.writeNumber(myNumber.getValue());
        }
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        TypeFactory typeFactory = ctx.getTypeFactory();
        JavaType myNumberType = typeFactory.constructType(MyNumber.class);
        JsonSerializer<MyNumber> serializer = mapper.findValueSerializer(MyNumber.class);
        MyNumberSerializer myNumberSerializer = (MyNumberSerializer)serializer;
        AnnotationIntrospector ai = ctx.getAnnotationIntrospector();
        assertNotNull(ai);
        assertEquals(MyNumberSerializer.class, ai.findSerializationConfig(MyNumber.class, null));
        ObjectIdInfo objectIdInfo = ai.findIdInfo(myNumberType, null);
        assertNotNull(objectIdInfo);
        ObjectIdGenerator<?> idGenerator = ctx.objectIdGeneratorInstance(null, objectIdInfo);
        assertNotNull(idGenerator);
    }

    @Test
    public void testGetTypeFactory() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        TypeFactory typeFactory = ctx.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        assertEquals(String.class, javaType.getRawClass());
        JavaType nestedType = typeFactory.constructType(new NestedClass());
        assertEquals(NestedClass.class, nestedType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        DeserializationContext ctx = mapper.getDeserializationContext();
        JavaType baseType = ctx.getTypeFactory().constructType(String.class);
        JavaType subType1 = ctx.resolveSubType(baseType, "java.lang.String");
        assertEquals(java.lang.String.class, subType1.getRawClass());
        JavaType subType2 = ctx.resolveSubType(baseType, "java.lang.Object");
        assertNull(subType2);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DeserializationContext ctx = mapper.getDeserializationContext();
        JavaType baseType = ctx.getTypeFactory().constructType(String.class);
        ctx.invalidTypeIdException(baseType, "InvalidClass");
    }

    @Test
    public void testConstructType() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        TypeFactory typeFactory = ctx.getTypeFactory();
        Type type = String[].class;
        JavaType javaType = ctx.constructType(type);
        assertEquals(type, javaType.getType());
    }

    @Test
    public void testConstructSpecializedType() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        JavaType baseType = ctx.getTypeFactory().constructType(Object.class);
        JavaType subType = ctx.constructSpecializedType(baseType, String[].class);
        assertEquals(String[].class, subType.getRawClass());
    }

    static class NestedClass {
        private int id;

        public NestedClass() {}

        public NestedClass(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    @Test
    public void testSetAttribute() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        ctx.setAttribute("test", "attribute");
        Object test = ctx.getAttribute("test");
        assertEquals("attribute", test);
    }

    @Test
    public void testGetAttribute() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        ctx.setAttribute("test", "attribute");
        Object test = ctx.getAttribute("test");
        assertEquals("attribute", test);
        ctx.setAttribute("test", null);
        assertNull(ctx.getAttribute("test"));
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DeserializationContext ctx = mapper.getDeserializationContext();
        Class<?> baseType = String[].class;
        JsonFormat.Value defaultPropertyFormat = ctx.getDefaultPropertyFormat(baseType);
        assertNull(defaultPropertyFormat);
    }

    @Test
    public void testGetEnableDefaultTyping() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctx = mapper.getDeserializationContext();
        assertTrue(ctx.canOverrideAccessModifiers());
        assertFalse(ctx.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        assertTrue(ctx.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
    }
}