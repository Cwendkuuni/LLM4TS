package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JavaTypeTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSimpleJavaType() throws Exception {
        Class<String> rawType = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawType);

        assertEquals(rawType, javaType.getRawClass());
        assertEquals(String.class, javaType.getType(mapper.getFactory()));
        assertEquals("java.lang.String", javaType.getErasedSignature());
        assertEquals("class java.lang.String", javaType.getGenericSignature());

        JavaType narrowType = javaType.narrowBy(String[].class);
        assertEquals(String[].class, narrowType.getRawClass());
        assertEquals(String[].class, narrowType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.String;", narrowType.getErasedSignature());
        assertEquals("class java.lang.String[]", narrowType.getGenericSignature());
        assertNull(narrowType._valueHandler);
        assertNull(narrowType._typeHandler);

        JavaType wideType = javaType.widenBy(Object.class);
        assertEquals(Object.class, wideType.getRawClass());
        assertEquals(Object.class, wideType.getType(mapper.getFactory()));
        assertEquals("class java.lang.Object", wideType.getErasedSignature());
        assertEquals("class java.lang.Object", wideType.getGenericSignature());
        assertNull(wideType._valueHandler);
        assertNull(wideType._typeHandler);

        JavaType emptyType = TypeFactory.defaultInstance().constructType(new Class[0]);
        assertEquals(Class[].class, emptyType.getRawClass());
        assertEquals(Class[].class, emptyType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.Class;", emptyType.getErasedSignature());
        assertEquals("class java.lang.Class[]", emptyType.getGenericSignature());
        assertNull(emptyType._valueHandler);
        assertNull(emptyType._typeHandler);

        TypeReference<String> typeRef = new TypeReference<String>() {};
        JavaType typeRefJavaType = TypeFactory.defaultInstance().constructType(typeRef.getType());
        assertEquals(String.class, typeRefJavaType.getType(mapper.getFactory()));
        assertEquals("class java.lang.String", typeRefJavaType.getErasedSignature());
        assertEquals("class java.lang.String", typeRefJavaType.getGenericSignature());
    }

    @Test
    public void testValueHandlerJavaType() throws Exception {
        Class<String> rawType = String.class;
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(rawType, new StringSerializer(), null);

        assertEquals(rawType, javaType.getRawClass());
        assertEquals(String.class, javaType.getType(mapper.getFactory()));
        assertEquals("java.lang.String", javaType.getErasedSignature());
        assertEquals("class java.lang.String", javaType.getGenericSignature());
        assertEquals(StringSerializer.class, javaType.<StringSerializer>getValueHandler());
        assertNull(javaType._typeHandler);

        JavaType narrowType = javaType.narrowBy(String[].class);
        assertEquals(String[].class, narrowType.getRawClass());
        assertEquals(String[].class, narrowType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.String;", narrowType.getErasedSignature());
        assertEquals("class java.lang.String[]", narrowType.getGenericSignature());
        assertNull(narrowType._valueHandler);
        assertNull(narrowType._typeHandler);

        JavaType wideType = javaType.widenBy(Object.class);
        assertEquals(Object.class, wideType.getRawClass());
        assertEquals(Object.class, wideType.getType(mapper.getFactory()));
        assertEquals("class java.lang.Object", wideType.getErasedSignature());
        assertEquals("class java.lang.Object", wideType.getGenericSignature());
        assertNull(wideType._valueHandler);
        assertNull(wideType._typeHandler);

        JavaType emptyType = TypeFactory.defaultInstance()
                .constructType(new Class[0], null, new EmptyClassSerializer());
        assertEquals(Class[].class, emptyType.getRawClass());
        assertEquals(Class[].class, emptyType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.Class;", emptyType.getErasedSignature());
        assertEquals("class java.lang.Class[]", emptyType.getGenericSignature());
        assertNull(emptyType._valueHandler);
        assertEquals(EmptyClassSerializer.class, emptyType._typeHandler);
    }

    @Test
    public void testTypeHandlerJavaType() throws Exception {
        Class<String> rawType = String.class;
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(rawType, null, new StringDeserializer());

        assertEquals(rawType, javaType.getRawClass());
        assertEquals(String.class, javaType.getType(mapper.getFactory()));
        assertEquals("java.lang.String", javaType.getErasedSignature());
        assertEquals("class java.lang.String", javaType.getGenericSignature());
        assertNull(javaType._valueHandler);
        assertEquals(StringDeserializer.class, javaType._typeHandler);

        JavaType narrowType = javaType.narrowBy(String[].class);
        assertEquals(String[].class, narrowType.getRawClass());
        assertEquals(String[].class, narrowType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.String;", narrowType.getErasedSignature());
        assertEquals("class java.lang.String[]", narrowType.getGenericSignature());
        assertNull(narrowType._valueHandler);
        assertNull(narrowType._typeHandler);

        JavaType wideType = javaType.widenBy(Object.class);
        assertEquals(Object.class, wideType.getRawClass());
        assertEquals(Object.class, wideType.getType(mapper.getFactory()));
        assertEquals("class java.lang.Object", wideType.getErasedSignature());
        assertEquals("class java.lang.Object", wideType.getGenericSignature());
        assertNull(wideType._valueHandler);
        assertNull(wideType._typeHandler);

        JavaType emptyType = TypeFactory.defaultInstance()
                .constructType(new Class[0], null, new EmptyClassDeserializer());
        assertEquals(Class[].class, emptyType.getRawClass());
        assertEquals(Class[].class, emptyType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.Class;", emptyType.getErasedSignature());
        assertEquals("class java.lang.Class[]", emptyType.getGenericSignature());
        assertNull(emptyType._valueHandler);
        assertEquals(EmptyClassDeserializer.class, emptyType._typeHandler);
    }

    @Test
    public void testStaticTypingJavaType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class, null, null, true);
        JavaType staticJavaType = javaType.withStaticTyping();
        JavaType wideStaticJavaType = staticJavaType.widenBy(Number[].class);
        assertEquals(Number[].class, wideStaticJavaType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.Number;", wideStaticJavaType.getErasedSignature());
        assertEquals("class java.lang.Number[]", wideStaticJavaType.getGenericSignature());
        assertNull(wideStaticJavaType._valueHandler);
        assertNull(wideStaticJavaType._typeHandler);
        assertSame(staticJavaType, wideStaticJavaType);

        JavaType narrowStaticJavaType = staticJavaType.narrowBy(int[].class);
        assertEquals(int[].class, narrowStaticJavaType.getType(mapper.getFactory()));
        assertEquals("class [I", narrowStaticJavaType.getErasedSignature());
        assertEquals("class [I", narrowStaticJavaType.getGenericSignature());
        assertNull(narrowStaticJavaType._valueHandler);
        assertNull(narrowStaticJavaType._typeHandler);
        assertSame(staticJavaType, narrowStaticJavaType);

        JavaType emptyStaticJavaType = TypeFactory.defaultInstance()
                .constructType(new Class[0], null, null, true);
        JavaType wideEmptyStaticJavaType = emptyStaticJavaType.widenBy(Object[].class);
        assertEquals(Object[].class, wideEmptyStaticJavaType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.Object;", wideEmptyStaticJavaType.getErasedSignature());
        assertEquals("class java.lang.Object[]", wideEmptyStaticJavaType.getGenericSignature());
        assertNull(wideEmptyStaticJavaType._valueHandler);
        assertNull(wideEmptyStaticJavaType._typeHandler);
        assertSame(emptyStaticJavaType, wideEmptyStaticJavaType);

        JavaType narrowEmptyStaticJavaType = emptyStaticJavaType.narrowBy(Class[].class);
        assertEquals(Class[].class, narrowEmptyStaticJavaType.getType(mapper.getFactory()));
        assertEquals("class [Ljava.lang.Class;", narrowEmptyStaticJavaType.getErasedSignature());
        assertEquals("class java.lang.Class[]", narrowEmptyStaticJavaType.getGenericSignature());
        assertNull(narrowEmptyStaticJavaType._valueHandler);
        assertNull(narrowEmptyStaticJavaType._typeHandler);
        assertSame(emptyStaticJavaType, narrowEmptyStaticJavaType);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JavaType emptyListType = TypeFactory.defaultInstance().constructType(new Class[0], null, null, true);
        JavaType emptyListTypeWithFailOnEmpty = TypeFactory.defaultInstance()
                .constructType(new Class[0], null, null, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        assertEquals(mapper.getTypeFactory().constructCollectionType(List.class, Object.class), emptyListTypeWithFailOnEmpty.getType(mapper));
        assertEquals(mapper.getTypeFactory().constructCollectionType(List.class, Object.class), emptyListType.getType(mapper));
        assertNull(emptyListTypeWithFailOnEmpty._valueHandler);
        assertNull(emptyListTypeWithFailOnEmpty._typeHandler);
        assertNull(emptyListType._valueHandler);
        assertNull(emptyListType._typeHandler);
        assertSame(emptyListType, emptyListTypeWithFailOnEmpty);
    }

    static class EmptyClassDeserializer {}

    static class EmptyClassSerializer {}

    static class EmptyClass {}

    static class StringDeserializer {}

    static class StringSerializer {}
}