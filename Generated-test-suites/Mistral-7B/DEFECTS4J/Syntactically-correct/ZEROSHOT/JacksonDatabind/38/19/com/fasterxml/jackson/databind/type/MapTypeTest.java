package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        MapType actualMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object valueHandler = new Object() { /* empty */ };
        MapType actualMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType, valueHandler);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testConstructWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object contentTypeHandler = new Object() { /* empty */ };
        MapType actualMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType, null, contentTypeHandler, false);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, contentTypeHandler, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testConstructWithValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object valueHandler = new Object() { /* empty */ };
        MapType actualMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testConstructWithContentValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object contentValueHandler = new Object() { /* empty */ };
        MapType actualMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType, null, null, false, contentValueHandler);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false, contentValueHandler);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object valueHandler = new Object() { /* empty */ };
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = actualMapType.withTypeHandler(valueHandler);
        assertEquals(expectedMapType, actualMapType);
        JavaType actualKeyType = expectedMapType.getKeyType();
        JavaType expectedKeyType = keyType;
        assertEquals(expectedKeyType, actualKeyType);
        JavaType actualValueType = expectedMapType.getValueType();
        JavaType expectedValueType = valueType;
        assertEquals(expectedValueType, actualValueType);
        Object actualTypeHandler = expectedMapType.getTypeHandler();
        Object expectedTypeHandler = valueHandler;
        assertEquals(expectedTypeHandler, actualTypeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        JavaType contentType = JavaType.constructUsing(Boolean.class, boolean.class);
        Object typeHandler = new Object() { /* empty */ };
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType
                = actualMapType.withContentTypeHandler(contentType);
        assertEquals(expectedMapType, actualMapType);
        JavaType actualKeyType = expectedMapType.getKeyType();
        JavaType expectedKeyType = keyType;
        assertEquals(expectedKeyType, actualKeyType);
        JavaType actualValueType = expectedMapType.getValueType();
        JavaType expectedValueType = valueType;
        assertEquals(expectedValueType, actualValueType);
        JavaType actualContentType = expectedMapType.getContentType();
        JavaType expectedContentType = contentType;
        assertEquals(expectedContentType, actualContentType);
        Object actualTypeHandler = expectedMapType.getTypeHandler();
        Object expectedTypeHandler = typeHandler;
        assertEquals(expectedTypeHandler, actualTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object valueHandler = new Object() { /* empty */ };
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = actualMapType.withValueHandler(valueHandler);
        assertEquals(expectedMapType, actualMapType);
        JavaType actualKeyType = expectedMapType.getKeyType();
        JavaType expectedKeyType = keyType;
        assertEquals(expectedKeyType, actualKeyType);
        JavaType actualValueType = expectedMapType.getValueType();
        JavaType expectedValueType = valueType;
        assertEquals(expectedValueType, actualValueType);
        Object actualValueHandler = expectedMapType.getValueHandler();
        Object expectedValueHandler = valueHandler;
        assertEquals(expectedValueHandler, actualValueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object contentValueHandler = new Object() { /* empty */ };
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = actualMapType.withContentValueHandler(contentValueHandler);
        assertEquals(expectedMapType, actualMapType);
        JavaType actualKeyType = expectedMapType.getKeyType();
        JavaType expectedKeyType = keyType;
        assertEquals(expectedKeyType, actualKeyType);
        JavaType actualValueType = expectedMapType.getValueType();
        JavaType expectedValueType = valueType;
        assertEquals(expectedValueType, actualValueType);
        JavaType actualContentValueType = expectedMapType.getContentValueType();
        JavaType expectedContentValueType = valueType;
        assertEquals(expectedContentValueType, actualContentValueType);
        Object actualContentValueHandler = expectedMapType.getContentValueHandler();
        Object expectedContentValueHandler = contentValueHandler;
        assertEquals(expectedContentValueHandler, actualContentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = actualMapType.withStaticTyping();
        assertTrue(expectedMapType._asStatic);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        JavaType contentType = JavaType.constructUsing(Boolean.class, boolean.class);
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = actualMapType.withContentType(contentType);
        assertEquals(expectedMapType, actualMapType);
        JavaType actualKeyType = expectedMapType.getKeyType();
        JavaType expectedKeyType = keyType;
        assertEquals(expectedKeyType, actualKeyType);
        JavaType actualValueType = expectedMapType.getValueType();
        JavaType expectedValueType = valueType;
        assertEquals(expectedValueType, actualValueType);
        JavaType actualContentType = expectedMapType.getContentType();
        JavaType expectedContentType = contentType;
        assertEquals(expectedContentType, actualContentType);
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        JavaType newKeyType = JavaType.constructUsing(Double.class, double.class);
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = actualMapType.withKeyType(newKeyType);
        assertEquals(expectedMapType, actualMapType);
        JavaType actualKeyType = expectedMapType.getKeyType();
        JavaType expectedKeyType = newKeyType;
        assertEquals(expectedKeyType, actualKeyType);
        JavaType actualValueType = expectedMapType.getValueType();
        JavaType expectedValueType = valueType;
        assertEquals(expectedValueType, actualValueType);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superclass = JavaType.constructUsing(Map.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.constructUsing(Serializable.class) };
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        MapType expectedMapType = new MapType(rawType, bindings, superclass, superInterfaces, keyType, valueType);
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType).refine(rawType, bindings, superclass, superInterfaces);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testToString() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        String expectedString = "[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]";
        String actualString = actualMapType.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object keyTypeHandler = new Object() { /* empty */ };
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = actualMapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(expectedMapType, actualMapType);
        JavaType actualKeyType = expectedMapType.getKeyType();
        JavaType expectedKeyType = keyType;
        assertEquals(expectedKeyType, actualKeyType);
        Object actualKeyTypeHandler = expectedMapType.getKeyTypeHandler();
        Object expectedKeyTypeHandler = keyTypeHandler;
        assertEquals(expectedKeyTypeHandler, actualKeyTypeHandler);
        JavaType actualValueType = expectedMapType.getValueType();
        JavaType expectedValueType = valueType;
        assertEquals(expectedValueType, actualValueType);
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructUsing(Integer.class, int.class);
        JavaType valueType = JavaType.constructUsing(String.class, String.class);
        Object keyValueHandler = new Object() { /* empty */ };
        MapType actualMapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType expectedMapType = actualMapType.withKeyValueHandler(keyValueHandler);
        assertEquals(expectedMapType, actualMapType);
        JavaType actualKeyType = expectedMapType.getKeyType();
        JavaType expectedKeyType = keyType;
        assertEquals(expectedKeyType, actualKeyType);
        Object actualKeyValueHandler = expectedMapType.getKeyValueHandler();
        Object expectedKeyValueHandler = keyValueHandler;
        assertEquals(expectedKeyValueHandler, actualKeyValueHandler);
        JavaType actualValueType = expectedMapType.getValueType();
        JavaType expectedValueType = valueType;
        assertEquals(expectedValueType, actualValueType);
    }
}