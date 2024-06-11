package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeDeserializerBase typeDeserializerBase = new SomeTypeDeserializerBase(null, null, null, false, null);
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer actual = typeDeserializerBase.forProperty(property);
        Assert.assertNotNull(actual);
        Assert.assertEquals(typeDeserializerBase.getClass(), actual.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        Assert.assertNull(typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeDeserializerBase typeDeserializerBase = mapper.getDeserializerFactory()
                .findValueDeserializer(String.class);
        Assert.assertEquals(String.class.getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        Assert.assertNull(typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        Assert.assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testDefaultImpl() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeDeserializerBase typeDeserializerBase = mapper.getDeserializerFactory()
                .findValueDeserializer(String.class);
        Assert.assertNotNull(typeDeserializerBase.getDefaultImpl());
        Assert.assertEquals(String.class, typeDeserializerBase.getDefaultImpl().getRawClass());
    }

    @Test
    public void testToString() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeDeserializerBase typeDeserializerBase = mapper.getDeserializerFactory()
                .findValueDeserializer(String.class);
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class java.lang.String; id-resolver:com.fasterxml.jackson.databind.JsonDeserializer$NullSafe$1@1f6d6a1a]";
        Assert.assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        typeDeserializerBase.findDeserializer(null, "test");
    }

    @Test
    public void testFindDeserializerEmptyDeserializers() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        typeDeserializerBase._deserializers.put("test", deserializer);
        JsonDeserializer<Object> actual = typeDeserializerBase.findDeserializer(null, "test");
        Assert.assertEquals(deserializer, actual);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        typeDeserializerBase.findDeserializer(null, "unknown");
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        JsonDeserializer<Object> actual = typeDeserializerBase.findDefaultImplDeserializer(null);
        Assert.assertNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerHasDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, JsonDeserializer.class);
        JsonDeserializer<Object> actual = typeDeserializerBase.findDefaultImplDeserializer(null);
        Assert.assertNotNull(actual);
        Assert.assertEquals(JsonDeserializer.class, actual.getClass());
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        Object actual = typeDeserializerBase.deserializeWithNativeTypeId(null, null);
        Assert.assertNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeIdEmptyTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        JsonParser parser = mapper.getFactory()
                .createParser("[{\"type\":\"null\"}]");
        Object actual = typeDeserializerBase.deserializeWithNativeTypeId(parser, null);
        Assert.assertNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeIdValidTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        JsonParser parser = mapper.getFactory()
                .createParser("[{\"type\":\"com.test.MyClass\"}]");
        Class<?> expected = MyClass.class;
        Object actual = typeDeserializerBase.deserializeWithNativeTypeId(parser, null);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        typeDeserializerBase.handleUnknownTypeId(null, "test");
    }

    @Test
    public void testHandleMissingTypeIdNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        typeDeserializerBase.handleMissingTypeId(null, "test");
    }
}

class SomeTypeDeserializerBase extends TypeDeserializerBase {}

class StubJsonDeserializer extends JsonDeserializer<Object> {}

class MyClass {}