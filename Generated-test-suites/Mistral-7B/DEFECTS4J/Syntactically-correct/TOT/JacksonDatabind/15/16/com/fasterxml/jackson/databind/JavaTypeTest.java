package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JavaTypeTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJavaTypeCreation() throws Exception {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(JavaType.class);

        assertEquals(String.class, stringType.getRawClass());
        assertEquals(int.class, intType.getRawClass());
        assertEquals(JavaType.class, javaType.getRawClass());

        assertNotNull(stringType.getValueHandler());
        assertNull(stringType.getTypeHandler());
        assertEquals(false, stringType.useStaticType());

        assertNull(intType.getValueHandler());
        assertNull(intType.getTypeHandler());
        assertEquals(false, intType.useStaticType());

        assertNotNull(javaType.getValueHandler());
        assertNotNull(javaType.getTypeHandler());
        assertEquals(false, javaType.useStaticType());
    }

    @Test
    public void testJavaTypeWithHandlers() throws Exception {
        JavaType stringType = TypeFactory.defaultInstance()
                .constructType(String.class)
                .withValueHandler("valueHandler")
                .withTypeHandler("typeHandler");

        assertEquals("valueHandler", stringType.getValueHandler());
        assertEquals("typeHandler", stringType.getTypeHandler());
        assertEquals(false, stringType.useStaticType());
    }

    @Test
    public void testJavaTypeStaticTyping() throws Exception {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class).withStaticTyping();
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class).withStaticTyping();

        assertEquals(String.class, stringType.getRawClass());
        assertEquals(int.class, intType.getRawClass());
        assertEquals(true, stringType.useStaticType());
        assertEquals(true, intType.useStaticType());
    }

    @Test
    public void testJavaTypeNarrowing() throws Exception {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType stringArrayType = stringType.narrowBy(String[].class);

        assertEquals(String[].class, stringArrayType.getRawClass());
        assertEquals(stringArrayType, stringType.narrowBy(String[].class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingInvalid() throws Exception {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType stringArrayType = stringType.narrowBy(int[].class);
    }

    @Test
    public void testJavaTypeWidening() throws Exception {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringArrayType = stringType.widenBy(String[].class);

        assertEquals(String[].class, stringArrayType.getRawClass());
        assertEquals(stringType, stringArrayType.widenBy(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningInvalid() throws Exception {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringArrayType = stringType.widenBy(int.class);
    }

    @Test
    public void testJavaTypeWithTypeParameters() throws Exception {
        TypeReference<Map<String, List<Integer>>> typeRef = new TypeReference<Map<String, List<Integer>>>() {};
        JavaType mapType = TypeFactory.defaultInstance().constructType(typeRef.getType());

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.containedTypeOrUnknown(0).getRawClass());
        assertEquals(List.class, mapType.containedTypeOrUnknown(1).getRawClass());
        assertEquals(Integer.class, mapType.containedTypeOrUnknown(1).containedTypeOrUnknown(0).getRawClass());
    }
}