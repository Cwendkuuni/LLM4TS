package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.TypeResolver;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector.FindAnnotation;
import com.fasterxml.jackson.databind.HandlerInstantiator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.cfg.MapperConfigImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;
import com.fasterxml.jackson.databind.util.ConverterProperties;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;
import com.fasterxml.jackson.databind.util.TypeFactory;
import com.fasterxml.jackson.databind.util.TypeResolverBuilder;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private DatabindContext context;
    private MapperConfig<?> config;
    private HandlerInstantiator hi;
    private AnnotationIntrospector ai;

    @Before
    public void setUp() {
        config = new MapperConfigImpl();
        hi = mock(HandlerInstantiator.class);
        ai = mock(AnnotationIntrospector.class);
        context = new DatabindContextBase() {
            @Override
            public MapperConfig<?> getConfig() {
                return config;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return ai;
            }

            @Override
            public HandlerInstantiator getHandlerInstantiator() {
                return hi;
            }
        };
    }

    @Test
    public void testGetConfig() {
        assertEquals(config, context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertEquals(ai, context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled_MapperFeature() {
        when(config.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)).thenReturn(true);
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testIsEnabled_MapperFeature_False() {
        when(config.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)).thenReturn(false);
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        when(config.canOverrideAccessModifiers()).thenReturn(true);
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testCanOverrideAccessModifiers_False() {
        when(config.canOverrideAccessModifiers()).thenReturn(false);
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        when(config.getLocale()).thenReturn(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        when(config.getTimeZone()).thenReturn(TimeZone.getDefault());
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        when(config.getDefaultPropertyFormat(Object.class)).thenReturn(new JsonFormat.Value(""));
        assertEquals(new JsonFormat.Value(""), context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testGetAttribute() {
        Object attr = new Object();
        when(context.getAttribute("test")).thenReturn(attr);
        assertEquals(attr, context.getAttribute("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAttribute_NullKey() {
        context.setAttribute("test", null);
        context.getAttribute(null);
    }

    @Test
    public void testSetAttribute() {
        Object attr = new Object();
        context.setAttribute("test", attr);
        assertEquals(attr, context.getAttribute("test"));
    }

    @Test
    public void testConstructType() {
        Type type = null;
        JavaType javaType = context.constructType(type);
        assertNull(javaType);

        type = String.class;
        JavaType javaTypeString = context.constructType(type);
        assertNotNull(javaTypeString);

        Class<?> rawType = javaTypeString.getRawClass();
        assertEquals(String.class, rawType);
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "StringBuffer");
        assertNotNull(subType);
        assertEquals(StringBuffer.class, subType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "foo");
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertEquals(TypeFactory.defaultInstance(), typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = mock(ObjectIdGenerator.class);
        when(ai.findAnnotation(annotatedClass, ObjectIdGenerator.class)).thenReturn(objectIdGenerator);
        ObjectIdGenerator<?> generatedObjectIdGenerator = context.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertSame(objectIdGenerator, generatedObjectIdGenerator);

        when(ai.findAnnotation(annotatedClass, ObjectIdGenerator.class)).thenReturn(null);
        assertNull(context.objectIdGeneratorInstance(annotatedClass, objectIdInfo));
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        when(ai.findAnnotation(annotatedClass, ObjectIdResolver.class)).thenReturn(objectIdResolver);
        ObjectIdResolver generatedObjectIdResolver = context.objectIdResolverInstance(annotatedClass, objectIdInfo);
        assertSame(objectIdResolver, generatedObjectIdResolver);

        when(ai.findAnnotation(annotatedClass, ObjectIdResolver.class)).thenReturn(null);
        assertNull(context.objectIdResolverInstance(annotatedClass, objectIdInfo));
    }

    @Test
    public void testConversionInstance() throws JsonMappingException {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        Object converterDef = new StdConverter<Object, Object>() {};
        Converter<Object, Object> converter = context.converterInstance(annotatedMember, converterDef);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType(Object.class);
        when(context.reportBadDefinition(type, "error message"))
                .thenReturn(new InvalidDefinitionException("error message"));
        InvalidDefinitionException ex = context.reportBadDefinition(type, "error message");
        assertEquals("error message", ex.getMessage());
    }

    @Test
    public void testFormat() {
        String msg = "Message: {0} Size: {1}";
        String actual = context._format(msg, "test", 5);
        String expected = String.format(msg, "test", 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testTruncate() {
        String msg = "Message: {0} Size: {1} [ {2} ]";
        String actual = context._truncate(msg);
        String expected = "Message: {0} Size: {1} [" + msg.substring(msg.length() - 10) + "]";
        assertEquals(expected, actual);
    }

    @Test
    public void testQuotedString() {
        String msg = "Message: {0} Size: {1}";
        String actual = context._quotedString(msg);
        String expected = "\"Message: {0} Size: {1}\"";
        assertEquals(expected, actual);
    }

    @Test
    public void testColonConcat() {
        String msgBase = "Message: ";
        String msgExtra = "test";
        String actual = context._colonConcat(msgBase, msgExtra);
        String expected = msgBase + ": test";
        assertEquals(expected, actual);
    }

    @Test
    public void testDesc() {
        String desc = "Message: {0} Size: {1}";
        String actual = context._desc(desc);
        String expected = "[Message: {0} Size: {1}]";
        assertEquals(expected, actual);
    }
}

class DatabindContextBase extends DatabindContext {
    @Override
    protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
        throw new JsonMappingException("Invalid type id " + _quotedString(typeId)
                + " for " + _desc(baseType) + " " + extraDesc);
    }
}