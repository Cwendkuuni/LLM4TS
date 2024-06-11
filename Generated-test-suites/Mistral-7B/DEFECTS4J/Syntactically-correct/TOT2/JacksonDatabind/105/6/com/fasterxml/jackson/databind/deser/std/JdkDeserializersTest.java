
package com.example.junit; // replace with your package name

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JdkDeserializersTest {

    @Test
    public void testStaticInitializerBlock() {
        Set<String> hashSet = new HashSet<>();
        JdkDeserializers.staticInitializer();
        assertEquals(JdkDeserializers._classNames, hashSet);
    }

    @Test
    public void testFind() {
        Class<?> rawType = String.class;
        String clsName = "com.fasterxml.jackson.databind.deser.std.UUIDDeserializer";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindWithWrongClassName() {
        Class<?> rawType = UUID.class;
        String clsName = "com.fasterxml.jackson.databind.deser.std.InvalidClassName";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFindWithInvalidClass() {
        Class<?> rawType = new Object() {}.getClass();
        String clsName = "com.fasterxml.jackson.databind.deser.std.UUIDDeserializer";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test
    public void testFindWithValidClass() {
        Class<?> rawType = UUID.class;
        String clsName = "com.fasterxml.jackson.databind.deser.std.UUIDDeserializer";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

}
