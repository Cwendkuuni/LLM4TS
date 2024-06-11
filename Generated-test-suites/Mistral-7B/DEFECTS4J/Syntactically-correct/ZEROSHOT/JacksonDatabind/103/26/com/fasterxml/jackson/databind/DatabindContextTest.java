package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdResolverType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.Builder;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorImpl;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;
import com.fasterxml.jackson.databind.util.TypeFactory;

public class DatabindContextTest {

    private MapperConfig<?> config;
    private DatabindContext context;

    @Before
    public void setup() {
        Builder builder = new Builder();
        config = builder.build();
        context = mock(DatabindContext.class);
    }

    @Test
    public void testGetConfig() throws JsonMappingException {
        when(context.getConfig()).thenReturn(config);
        MapperConfig<?> result = context.getConfig();
        assertSame(config, result);
    }

    @Test
    public void testGetAnnotationIntrospector() throws JsonMappingException {
        AnnotationIntrospector introspector = new AnnotationIntrospectorImpl();
        when(context.getAnnotationIntrospector()).thenReturn(introspector);
        AnnotationIntrospector result = context.getAnnotationIntrospector();
        assertSame(introspector, result);
    }

    @Test
    public void testIsEnabled() {
        when(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)).thenReturn(true);
        boolean result = context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS);
        assertFalse(result);
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        when(context.canOverrideAccessModifiers()).thenReturn(true);
        boolean result = context.canOverrideAccessModifiers();
        assertFalse(result);
    }

    @Test
    public void testGetActiveView() {
        when(context.getActiveView()).thenReturn(null);
        Class<?> result = context.getActiveView();
        assertNull(result);
    }

    @Test
    public void testGetLocale() {
        when(context.getLocale()).thenReturn(Locale.ENGLISH);
        Locale result = context.getLocale();
        assertSame(Locale.ENGLISH, result);
    }

    @Test
    public void testGetTimeZone() {
        when(context.getTimeZone()).thenReturn(TimeZone.getDefault());
        TimeZone result = context.getTimeZone();
        assertSame(TimeZone.getDefault(), result);
    }

    @Test
    public void testGetDefaultPropertyFormat() throws JsonMappingException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        when(context.getDefaultPropertyFormat(baseType)).thenReturn(null);
        Object result = context.getDefaultPropertyFormat(baseType);
        assertNull(result);
    }

    @Test
    public void testGetAttribute() {
        when(context.getAttribute(null)).thenReturn("Attribute Value");
        String result = context.getAttribute(null);
        assertEquals("Attribute Value", result);
    }

    @Test
    public void testSetAttribute() {
        context.setAttribute(null, "Attribute Value");
        when(context.getAttribute(null)).thenReturn("Attribute Value");
        String result = context.getAttribute(null);
        assertEquals("Attribute Value", result);
    }

    @Test
    public void testConstructType() throws JsonMappingException {
        when(context.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());
        JavaType result = context.constructType(Type.getType(SomeClass.class));
        JavaType expected = TypeFactory.defaultInstance().constructType(SomeClass.class);
        assertSame(expected, result);
    }

    @Test
    public void testConstructSpecializedType() throws JsonMappingException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(SomeClass.class);
        Class<?> subclass = SomeSubClass.class;
        JavaType result = context.constructSpecializedType(baseType, subclass);
        JavaType expected = TypeFactory.defaultInstance().constructSpecializedType(baseType, subclass);
        assertSame(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithInvalidTypeId() throws JsonMappingException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(SomeClass.class);
        String subClass = "SomeInvalidType";
        when(context.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());
        context.resolveSubType(baseType, subClass);
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(SomeClass.class);
        String subClass = SomeSubClass.class.getName();
        JavaType result = context.resolveSubType(baseType, subClass);
        JavaType expected = TypeFactory.defaultInstance().constructSpecializedType(baseType, SomeSubClass.class);
        assertSame(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = TypeFactory.defaultInstance().constructType(SomeClass.class);
        String invalidTypeId = "InvalidTypeId";
        when(context.getTypeFactory()).thenThrow(new JsonMappingException("Error"));
        context.invalidTypeIdException(baseType, invalidTypeId, null);
    }

    @Test
    public void testGetTypeFactory() throws JsonMappingException {
        TypeFactory result = context.getTypeFactory();
        TypeFactory expected = TypeFactory.defaultInstance();
        assertSame(expected, result);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        when(objectIdInfo.getGeneratorType()).thenReturn(ObjectIdGenerator.class);
        when(generator.forScope(objectIdInfo.getScope())).thenReturn(generator);
        when(context.objectIdGeneratorInstance(annotatedClass, objectIdInfo)).thenReturn(generator);
        ObjectIdGenerator<?> result = context.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertSame(generator, result);
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        when(objectIdInfo.getResolverType()).thenReturn(IdResolverType.STRING);
        when(resolver.forScope(objectIdInfo.getScope())).thenReturn(resolver);
        when(context.objectIdResolverInstance(annotatedClass, objectIdInfo)).thenReturn(resolver);
        ObjectIdResolver result = context.objectIdResolverInstance(annotatedClass, objectIdInfo);
        assertSame(resolver, result);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        Object converterDef = new StdConverter.StringToInt();
        Converter<Object, Object> result = context.converterInstance(annotatedClass, converterDef);
        Converter<Object, Object> expected = new StdConverter.StringToInt();
        assertSame(expected, result);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        String msg = "Bad definition message";
        when(context.reportBadDefinition(type, msg)).thenThrow(new JsonProcessingException("Error"));
        try {
            context.reportBadDefinition(type, msg);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Error", e.getMessage());
        }
    }
}

class SomeClass {
    @JsonTypeInfo(use = Id.CLASS, include = As.EXISTING_PROPERTY, property = "type")
    public static class SomeSubClass {
        private String name;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }
    }
}