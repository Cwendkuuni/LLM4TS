package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        // Test constructor with a simple class
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        assertInstanceOf(SimpleType.class, simpleType);

        // Test constructor with bindings, superclass and superinterfaces
        JavaType customSimpleType = TypeFactory.defaultInstance()
                .construct(String.class, TypeBindings.emptyBindings(),
                        TypeFactory.defaultInstance().construct(List.class),
                        new JavaType[]{TypeFactory.defaultInstance().construct(Integer.class)});
        assertInstanceOf(SimpleType.class, customSimpleType);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        // Test copy constructor
        JavaType originalSimpleType = TypeFactory.defaultInstance().construct(String.class);
        JavaType copiedSimpleType = new SimpleType((TypeBase) originalSimpleType);
        assertEquals(originalSimpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getTypeBindings(), copiedSimpleType.getTypeBindings());
        assertArrayEquals(originalSimpleType.getSuperClasses(), copiedSimpleType.getSuperClasses());
        assertArrayEquals(originalSimpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(originalSimpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertEquals(originalSimpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
        assertEquals(originalSimpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        // Test narrowing to a subclass
        JavaType superclassType = TypeFactory.defaultInstance().construct(Object.class);
        JavaType subclassType = TypeFactory.defaultInstance().construct(String.class);
        JavaType narrowedType = superclassType.narrow(String.class);
        assertSame(subclassType, narrowedType);

        // Test narrowing to a different class
        JavaType differentClassType = TypeFactory.defaultInstance().construct(Integer.class);
        JavaType narrowedDifferentType = superclassType.narrow(Integer.class);
        assertNull(narrowedDifferentType);
    }

    @Test
    public void testSimpleTypeMethods() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);

        // Test withContentType
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(TypeFactory.defaultInstance().construct(Integer.class)));

        // Test withTypeHandler
        JavaType handlerType = TypeFactory.defaultInstance().construct(StringHandler.class);
        JavaType simpleTypeWithHandler = simpleType.withTypeHandler(handlerType);
        assertEquals(handlerType, simpleTypeWithHandler.getTypeHandler());

        // Test withContentTypeHandler
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(TypeFactory.defaultInstance().construct(IntegerHandler.class)));

        // Test withValueHandler
        JavaType valueHandlerType = TypeFactory.defaultInstance().construct(ValueHandler.class);
        JavaType simpleTypeWithValueHandler = simpleType.withValueHandler(valueHandlerType);
        assertEquals(valueHandlerType, simpleTypeWithValueHandler.getValueHandler());

        // Test withContentValueHandler
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentValueHandler(TypeFactory.defaultInstance().construct(ValueHandler.class)));

        // Test withStaticTyping
        JavaType staticSimpleType = simpleType.withStaticTyping();
        assertTrue(staticSimpleType.isStatic());

        // Test refine
        JavaType refinedType = simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testConstructUnsafe() {
        // Test constructUnsafe method
        JavaType simpleType = TypeFactory.defaultInstance().constructUnsafe(String.class);
        assertInstanceOf(SimpleType.class, simpleType);
    }

    @Test
    public void testConstructDeprecated() {
        // Test construct method deprecated since 2.7
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        assertInstanceOf(SimpleType.class, simpleType);

        try {
            JavaType simpleTypeDeprecated = TypeFactory.defaultInstance().construct(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private static class StringHandler implements TypeSerializer {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStringValue(value.toString());
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public JsonSerializer<Object> createSerializer(SerializerProvider p) {
            return this;
        }

        @Override
        public JavaType handleTypeReference(TypeReference<Object> valueReference) throws JsonProcessingException {
            return TypeFactory.defaultInstance().construct(String.class);
        }
    }

    private static class StringDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vh, JavaType t) throws IOException, JsonProcessingException {
            return p.getValueAsString();
        }
    }

    private static class StringValueHandler implements ValueHandler<Object> {
        @Override
        public Object getValue(Object bean, String propertyName) throws IOException {
            return bean.toString();
        }

        @Override
        public void setValue(Object bean, String propertyName, Object value) throws IOException {
            bean.toString();
        }

        @Override
        public void serializeValue(Object value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
            jgen.writeStringValue(value.toString());
        }

        @Override
        public void setValueHandler(ValueHandler<?> newHandler) {
            // empty
        }
    }

    private static class ValueHandler<T> implements ValueDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }

        @Override
        public JsonDeserializer<T> createContextual(DeserializationContext ctxt) throws JsonMappingException {
            return new StringDeserializer();
        }
    }

    private static class IntegerHandler implements TypeSerializer {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return p.nextIntValue();
        }

        @Override
        public JsonSerializer<Object> createSerializer(SerializerProvider p) {
            return this;
        }

        @Override
        public JavaType handleTypeReference(TypeReference<Object> valueReference) throws JsonProcessingException {
            return TypeFactory.defaultInstance().construct(Integer.class);
        }
    }

    private static class IntegerDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext context, TypeDeserializer vh, JavaType t) throws IOException, JsonProcessingException {
            return p.nextIntValue();
        }
    }

    private static class IntegerValueHandler implements ValueHandler<Object> {
        @Override
        public Object getValue(Object bean, String propertyName) throws IOException {
            return 0;
        }

        @Override
        public void setValue(Object bean, String propertyName, Object value) throws IOException {
            // empty
        }

        @Override
        public void serializeValue(Object value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
            jgen.writeNumber((int) value);
        }

        @Override
        public void setValueHandler(ValueHandler<?> newHandler) {
            // empty
        }
    }
}