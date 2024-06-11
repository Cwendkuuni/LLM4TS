package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

public class DatabindContextTest {

    private DatabindContext context;

    @Before
    public void setup() {
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        context = config.getContextInstance();
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        Assert.assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector ai = context.getAnnotationIntrospector();
        Assert.assertNotNull(ai);
    }

    @Test
    public void testIsEnabled() {
        Assert.assertTrue(context.isEnabled(MapperFeature.DEFAULT_PROPERTY_INCLUDE));
        Assert.assertTrue(context.isEnabled(MapperFeature.DEFAULT_PROPERTY_INCLUDE_ANNOTATIONS));
        Assert.assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        Assert.assertFalse(context.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        Assert.assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        Assert.assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        Assert.assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        Assert.assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        Class<?> baseType = String.class;
        JsonFormat.Value format = context.getDefaultPropertyFormat(baseType);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetAttribute() {
        Object key = MapperFeature.PROPERTY_NAME_CASE_INSENSITIVE;
        Object value = context.getAttribute(key);
        Assert.assertEquals(true, value);
    }

    @Test
    public void testSetAttribute() {
        Object key = "test";
        Object value = "testValue";
        context.setAttribute(key, value);
        Object result = context.getAttribute(key);
        Assert.assertEquals(value, result);
    }

    @Test
    public void testConstructType() {
        Type type = String[].class;
        JavaType javaType = context.constructType(type);
        Assert.assertNotNull(javaType);
        Assert.assertEquals(javaType.getClass(), ContextType.ArrayType.class);
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = ContextType.ClassType.STRING_CLASS;
        Class<?> subclass = StringBuffer.class;
        JavaType javaType = context.constructSpecializedType(baseType, subclass);
        Assert.assertNotNull(javaType);
        Assert.assertEquals(javaType.getRawClass(), StringBuffer.class);
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = ContextType.ClassType.STRING_CLASS;
        String subClassName = "java.lang.StringBuffer";
        JavaType javaType = context.resolveSubType(baseType, subClassName);
        Assert.assertNotNull(javaType);
        Assert.assertEquals(javaType.getRawClass(), StringBuffer.class);
    }

    @JsonSerialize(using = MySerializer.class)
    static class MyClass {
        private int value;
    }

    static class MySerializer extends StdSerializer<MyClass> {
        public MySerializer() {
            this(null);
        }

        public MySerializer(Class<MyClass> t) {
            super(t);
        }

        @Override
        public void serialize(MyClass myClass, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            jsonGenerator.writeNumber(myClass.value);
        }
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(MyClass.class);
        Object converterDef = MySerializer.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        Assert.assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = ContextType.ClassType.STRING_CLASS;
        String typeId = "java.util.Date";
        context.invalidTypeIdException(baseType, typeId, null);
    }

    @Test
    public void testTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        Assert.assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(MyClass.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, null, null, null);
        ObjectIdGenerator<?> generator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        Assert.assertNotNull(generator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = new AnnotatedClass(MyClass.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, null, null, null);
        ObjectIdResolver resolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        Assert.assertNotNull(resolver);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = ContextType.ClassType.STRING_CLASS;
        context.reportBadDefinition(type, "Invalid type definition");
    }
}