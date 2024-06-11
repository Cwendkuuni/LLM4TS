package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestTypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DatabindContextTest extends TestBase {

    @Test
    public void testGenericConfigAccess() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        DatabindContext context = config.newDefaultContext();
        MapperConfig<?> config2 = context.getConfig();
        assertTrue(config2 instanceof MapperConfig.StandardConfig);
        assertEquals(MapperFeature.DEFAULT_INTENTS.length, config2.getDefaultIntents().length);
        assertSame(config, config2);

        AnnotationIntrospector ai = context.getAnnotationIntrospector();
        assertSame(ai, context.getConfig().getAnnotationIntrospector());
    }

    @Test
    public void testAccessToSpecificConfigSettings() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig().with(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        DatabindContext context = config.newDefaultContext();
        assertTrue(context.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS_AND_SETTERS));
        assertNull(context.getActiveView());
        assertEquals(Locale.getDefault(), context.getLocale());
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
        JsonFormat.Value format = context.getDefaultPropertyFormat(String.class);
        assertTrue(format.getShape() == JsonFormat.Shape.STRING);
        assertEquals(Include.NON_NULL, format.getSerializationInclusion());
    }

    @Test
    public void testGenericAttributes() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        DatabindContext context = config.newDefaultContext();
        assertEquals(null, context.getAttribute(null));
        assertEquals("foo", context.setAttribute(null, "foo").getAttribute(null));

        config.setPropertyHandler(String.class, new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromObject(Object jc, Object c, Object value) {
                return value.toString();
            }
        });
        context = config.newDefaultContext();
        assertEquals("bar", context.setAttribute(null, "bar").getAttribute("key"));

        config.setPropertyHandler(String.class, null);
        context = config.newDefaultContext();
        assertNull(context.getAttribute("key"));
    }

    @Test
    public void testTypeInstantiationResolution() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        DatabindContext context = config.newDefaultContext();
        JavaType baseType = context.getTypeFactory().constructType(int[].class);
        JavaType subType = context.resolveSubType(baseType, "java.util.List");
        assertTrue(subType.isContainerType());
        assertEquals(java.util.List.class, subType.getRawClass());

        JavaType type = context.getTypeFactory().constructType(String.class);
        JavaType classType = context.constructType(String.class);
        assertEquals(type, classType);

        Class<?> cls = Class.forName("java.util.ArrayList");
        JavaType subClassType = context.resolveSubType(baseType, cls.getName());
        JavaType subClass = context.constructSpecializedType(baseType, cls);
        assertEquals(subClass, subClassType);
    }

    @Test
    public void testTypeInstantiationErrorHandling() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        DatabindContext context = config.newDefaultContext();
        TypeFactory tf = context.getTypeFactory();
        try {
            tf.constructType("FooBar");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            tf.constructType((Type) null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            tf.constructType(new Object() {}.getClass());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testObjectIdGeneration() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        DatabindContext context = config.newDefaultContext();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo( new AnnotatedClass(String.class), null, null );
        ObjectIdGenerator<?> gen = context.objectIdGeneratorInstance(objectIdInfo);
        assertNotNull(gen);

        ObjectIdResolver resolver = context.objectIdResolverInstance(objectIdInfo);
        assertNotNull(resolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        DatabindContext context = config.newDefaultContext();
        Annotated annotated = new AnnotatedClass(String.class);
        Converter<Object,Object> converter = context.converterInstance(annotated, Converter.stringConverter());
        assertNotNull(converter);
    }

    @Test
    public void testErrorReporting() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        DatabindContext context = config.newDefaultContext();
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        TypeSerializer ts = new TestTypeSerializer();
        JavaType type = context.getTypeFactory().constructType(new TypeReference<TypeDeserializer>() {}.getType());
        JsonMappingException e = (JsonMappingException) context.reportBadDefinition(baseType, "_message: bad type def [%s]", ts);
        assertEquals("bad type def [TestTypeSerializer]", e.getMessage());

        JavaType classType = context.getTypeFactory().constructType(String.class);
        e = (JsonMappingException) context.reportBadDefinition(classType, "_message: bad type def [%s]", "invalid type definition");
        assertEquals("bad type def [invalid type definition]", e.getMessage());
    }

    @Test
    public void testHelperMethods() throws Exception {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        DatabindContext context = config.newDefaultContext();
        assertEquals("foo", context._format("%s", "foo"));
        assertEquals("foo: bar", context._colonConcat("%s: %s", "foo", "bar"));
        assertEquals("\"foo\"", context._quotedString("foo"));
        assertEquals("foo", context._desc("foo"));
    }
}