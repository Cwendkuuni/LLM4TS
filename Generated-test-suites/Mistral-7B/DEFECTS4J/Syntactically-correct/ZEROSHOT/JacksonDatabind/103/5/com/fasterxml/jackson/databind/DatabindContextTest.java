package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector.FindAnnotation;
import com.fasterxml.jackson.databind.AnnotationIntrospector.ValueProvider;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.MemberNameWrapper;
import com.fasterxml.jackson.databind.introspect.MethodNameWrapper;
import com.fasterxml.jackson.databind.introspect.ValuePropertyDefinition;
import com.fasterxml.jackson.databind.type.ClassIdHandler;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ConverterUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;
import com.fasterxml.jackson.databind.util.StdConverter.StdConverterDeserializer;

public class DatabindContextTest {

    @SuppressWarnings("unchecked")
    private static DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }

    @org.junit.Test
    public void testGetConfig() {
        DeserializationContext ctx = createContext();
        MapperConfig<?> config = ctx.getConfig();
        assertNotNull(config);
    }

    @org.junit.Test
    public void testGetAnnotationIntrospector() {
        DeserializationContext ctx = createContext();
        AnnotationIntrospector ai = ctx.getAnnotationIntrospector();
        assertNotNull(ai);
    }

    @org.junit.Test
    public void testIsEnabled() {
        DeserializationContext ctx = createContext();
        assertTrue(ctx.isEnabled(MapperFeature.AUTO_DETECT_CREATOR_FROM_ANNOTATIONS));
        assertTrue(ctx.isEnabled(MapperFeature.AUTO_DETECT_CREATOR_FROM_ANNOTATIONS));
        assertTrue(ctx.isEnabled(MapperFeature.AUTO_DETECT_CREATOR_FROM_ANNOTATIONS));
        assertFalse(ctx.isEnabled(MapperFeature.CAN_OVERLOAD_ACCESS_MODIFIERS));
        assertFalse(ctx.isEnabled(MapperFeature.CAN_OVERLOAD_ACCESS_MODIFIERS));
        assertFalse(ctx.isEnabled(MapperFeature.IGNORE_UNKNOWN_KEYS));
    }

    @org.junit.Test
    public void testCanOverrideAccessModifiers() {
        DeserializationContext ctx = createContext();
        assertFalse(ctx.canOverrideAccessModifiers());
        assertFalse(ctx.canOverrideAccessModifiers());
    }

    @org.junit.Test
    public void testGetActiveView() {
        DeserializationContext ctx = createContext();
        assertNull(ctx.getActiveView());
    }

    @org.junit.Test
    public void testGetLocale() {
        DeserializationContext ctx = createContext();
        assertEquals(Locale.getDefault(), ctx.getLocale());
    }

    @org.junit.Test
    public void testGetTimeZone() {
        DeserializationContext ctx = createContext();
        assertEquals(TimeZone.getDefault(), ctx.getTimeZone());
    }

    @org.junit.Test
    public void testGetDefaultPropertyFormat() {
        DeserializationContext ctx = createContext();
        JavaType javaType = ctx.getTypeFactory().constructType(String.class);
        JsonFormat.Value format = ctx.getDefaultPropertyFormat(javaType);
        assertNotNull(format);
    }

    @org.junit.Test
    public void testGetAttribute() {
        DeserializationContext ctx = createContext();
        ctx.setAttribute("test", "testValue");
        Object attr = ctx.getAttribute("test");
        assertEquals("testValue", attr);
        ctx.setAttribute("test2", "testValue2");
        Object attr2 = ctx.getAttribute("test");
        assertEquals("testValue", attr);
        assertEquals("testValue2", ctx.getAttribute("test2"));
    }

    @org.junit.Test
    public void testConstructType() {
        DeserializationContext ctx = createContext();
        Type type = Integer.TYPE;
        JavaType javaType = ctx.constructType(type);
        assertNotNull(javaType);
        assertEquals(Integer.class, javaType.getRawClass());

        JavaType javaType2 = ctx.getTypeFactory().constructType(String.class);
        assertEquals(String.class, javaType2.getRawClass());
    }

    @org.junit.Test
    public void testResolveSubType() throws JsonMappingException {
        DeserializationContext ctx = createContext();
        JavaType baseType = ctx.getTypeFactory().constructType(String.class);
        String subClass = "java.util.ArrayList";
        JavaType javaType = ctx.resolveSubType(baseType, subClass);
        assertNotNull(javaType);
        assertFalse(javaType.getRawClass().isInstance(baseType.getRawClass()));

        JavaType javaType2 = ctx.resolveSubType(baseType, "java.util.ArrayList<String>");
        assertNotNull(javaType2);
        assertTrue(javaType2.getRawClass().isInstance(baseType.getRawClass()));

        JavaType javaType3 = ctx.resolveSubType(baseType, "java.util.ArrayList<Integer>");
        assertNull(javaType3);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DeserializationContext ctx = createContext();
        JavaType baseType = ctx.getTypeFactory().constructType(String.class);
        String typeId = "java.util.ArrayList";
        ctx.invalidTypeIdException(baseType, typeId, "Not a subtype");
    }

    @org.junit.Test
    public void testGetTypeFactory() {
        DeserializationContext ctx = createContext();
        TypeFactory typeFactory = ctx.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @org.junit.Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DeserializationContext ctx = createContext();
        AnnotatedClass annotatedClass = new AnnotatedClass(ObjectIdGenerator.class, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerator.class, "generator");
        ObjectIdGenerator<?> gen = ctx.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(gen);
    }

    @org.junit.Test
    public void testObjectIdResolverInstance() {
        DeserializationContext ctx = createContext();
        AnnotatedClass annotatedClass = new AnnotatedClass(ObjectIdResolver.class, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdResolver.class, "resolver");
        ObjectIdResolver resolver = ctx.objectIdResolverInstance(annotatedClass, objectIdInfo);
        assertNotNull(resolver);
    }

    @org.junit.Test
    public void testConverterInstance() throws JsonMappingException {
        DeserializationContext ctx = createContext();
        AnnotatedClass annotatedClass = new AnnotatedClass(StdConverter.class, null);
        Object converterDef = new JsonTypeName(String.class);
        Converter<Object, Object> converter = ctx.converterInstance(annotatedClass, converterDef);
        assertNotNull(converter);

        AnnotatedMember annotatedMember = new AnnotatedMember(Integer.class.getMethod("intValue"), null);
        Object converterDef2 = new JsonTypeName(String.class);
        Converter<Object, Object> converter2 = ctx.converterInstance(annotatedMember, converterDef2);
        assertNotNull(converter2);

        JavaType javaType = ctx.getTypeFactory().constructType(Integer.class);
        Converter<Object, Integer> converter3 = ctx.converterInstance(javaType, StdConverter.class);
        assertNull(converter3);
    }

    @org.junit.Test
    public void testReportBadDefinition() throws JsonMappingException {
        DeserializationContext ctx = createContext();
        JavaType javaType = ctx.getTypeFactory().constructType(Integer.class);
        String msg = "testMessage";
        JavaType javaType2 = ctx.reportBadDefinition(javaType, msg);
        assertNull(javaType2);

        Class<?> clazz = Integer.class;
        String msg2 = "testMessage2";
        JavaType javaType3 = ctx.reportBadDefinition(clazz, msg2);
        assertNull(javaType3);
    }
}

class TestConverterDeserializer extends StdConverterDeserializer {

    @Override
    public Converter<?> createConverter(DeserializationContext ctxt, BeanPropertyDefinition prop, AnnotatedMember m, Class<?> valueType)
            throws JsonMappingException {
        return new TestConverter();
    }

}

class TestConverter extends StdConverter<Object, String> {

    @Override
    public String convert(Object value) {
        return value.toString();
    }

}

class TestBean {

    @JsonTypeName("testBean")
    public String testProperty;

}