package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.Converter;

public class DatabindContextTest {

    private DeserializationContext ctx;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        ctx = mapper.getDeserializationContext();
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = ctx.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector ai = ctx.getAnnotationIntrospector();
        assertNotNull(ai);
    }

    @Test
    public void testIsEnabled_MapperFeature() {
        assertTrue(ctx.isEnabled(MapperFeature.DEFAULT_USE_ANNOTATIONS));
        assertTrue(ctx.isEnabled(MapperFeature.DEFAULT_USE_STATIC_TYPES));
        assertFalse(ctx.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
        assertFalse(ctx.isEnabled(MapperFeature.AUTO_DETECT_SETTERS));
        assertFalse(ctx.isEnabled(MapperFeature.AUTO_DETECT_FIELDS));
        assertFalse(ctx.isEnabled(MapperFeature.AUTO_DETECT_METHODS));
        assertFalse(ctx.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        assertFalse(ctx.isEnabled(MapperFeature.CAN_EMPTY_BEAN_CONSTRUCTOR));
        assertFalse(ctx.isEnabled(MapperFeature.CAN_USE_DEFAULT_VALUES));
        assertFalse(ctx.isEnabled(MapperFeature.DEFAULT_LOCALE));
        assertFalse(ctx.isEnabled(MapperFeature.DEFAULT_TIMEZONE));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertFalse(ctx.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(ctx.getActiveView());
    }

    @Test
    public void testGetLocale() {
        Locale locale = ctx.getLocale();
        assertNotNull(locale);
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = ctx.getTimeZone();
        assertNotNull(timeZone);
        assertEquals("America/Los_Angeles", timeZone.getID());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        assertEquals(null, ctx.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testGetAttribute() {
        Object attribute = ctx.getAttribute("test");
        assertNull(attribute);
    }

    @Test
    public void testSetAttribute() {
        ctx.setAttribute("test", "testValue");
        Object attribute = ctx.getAttribute("test");
        assertEquals("testValue", attribute);
    }

    @Test
    public void testConstructType() throws Exception {
        Type type = Integer[].class;
        JavaType javaType = ctx.constructType(type);
        assertEquals(Integer[].class, javaType.getRawClass());
        assertEquals("class [Ljava.lang.Integer;", javaType.getClassName());
    }

    @Test(expected = JsonMappingException.class)
    public void testConstructType_InvalidType() throws Exception {
        Type type = new TypeToken<Object>() {}.getType();
        JavaType javaType = ctx.constructType(type);
    }

    @Test
    public void testConstructSpecializedType() throws Exception {
        JavaType baseType = ctx.getTypeFactory().constructType(Integer[].class);
        JavaType subType = ctx.constructSpecializedType(baseType, String[].class);
        assertFalse(baseType.isTypeOrSubTypeOf(subType.getRawClass()));
        assertEquals("class [Ljava.lang.String;", subType.getClassName());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_InvalidBaseType() throws Exception {
        JavaType baseType = ctx.getTypeFactory().constructType(Object.class);
        String subClass = "com.test.InvalidClass";
        ctx.resolveSubType(baseType, subClass);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_InvalidSubClass() throws Exception {
        JavaType baseType = ctx.getTypeFactory().constructType(Integer[].class);
        String subClass = "java.lang.Integer";
        ctx.resolveSubType(baseType, subClass);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_InvalidTypeId() throws Exception {
        JavaType baseType = ctx.getTypeFactory().constructType(Integer[].class);
        String typeId = "com.test.InvalidTypeId";
        ctx.invalidTypeIdException(baseType, typeId, null);
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = ctx.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        Class<?> idGeneratorClass = ObjectIdGenerator.Identity.class;
        ObjectIdGenerator<?> objectIdGenerator = ctx.objectIdGeneratorInstance(null, null);
        assertTrue(objectIdGenerator instanceof ObjectIdGenerator.Identity);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdGeneratorInstance_InvalidClass() throws Exception {
        Class<?> idGeneratorClass = Object.class;
        ObjectIdGenerator<?> objectIdGenerator = ctx.objectIdGeneratorInstance(null, null);
    }

    @Test
    public void testObjectIdResolverInstance() throws Exception {
        Class<? extends ObjectIdResolver> idResolverClass = ObjectIdResolver.Std.class;
        ObjectIdResolver objectIdResolver = ctx.objectIdResolverInstance(null, null);
        assertTrue(objectIdResolver instanceof ObjectIdResolver.Std);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdResolverInstance_InvalidClass() throws Exception {
        Class<? extends ObjectIdResolver> idResolverClass = Object.class;
        ObjectIdResolver objectIdResolver = ctx.objectIdResolverInstance(null, null);
    }

    @Test
    public void testConverterInstance() throws Exception {
        Annotated annotated = mapper.getSerializationConfig().getDefaultIntrospector().findProperties(TestClass.class);
        Converter<String, Integer> converter = ctx.converterInstance(annotated, Converter.stringConverter(Integer.class));
        assertTrue(converter instanceof StringToInteger);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstance_InvalidConverterDef() throws Exception {
        Annotated annotated = mapper.getSerializationConfig().getDefaultIntrospector().findProperties(TestClass.class);
        Object converterDef = "not a valid converter";
        Converter<String, Integer> converter = ctx.converterInstance(annotated, converterDef);
    }

    @Test
    public void testReportBadDefinition() throws Exception {
        JavaType javaType = ctx.getTypeFactory().constructType(TestClass.class);
        String msg = "Bad definition: " + _desc(javaType.getClassName());
        String exceptionMessage = (String) ctx.reportBadDefinition(javaType, msg);
        assertEquals(msg, exceptionMessage);
    }

    @Test
    public void testReportBadDefinition_Class() throws Exception {
        Class<?> clazz = TestClass.class;
        String msg = "Bad definition: " + _desc(clazz.getName());
        String exceptionMessage = (String) ctx.reportBadDefinition(clazz, msg);
        assertEquals(msg, exceptionMessage);
    }

    @Test
    public void testFormat() {
        String msg = "Test: %s has property %s with value: %s";
        String format = ctx._format(msg, "testObj", " propName", "testValue");
        assertEquals("Test: testObj has property propName with value: testValue", format);
    }

    @Test
    public void testTruncate() {
        String test = "This is a really long test string";
        String truncated = ctx._truncate(test);
        assertTrue(truncated.length() < test.length());
    }

    @Test
    public void testQuotedString() {
        String test = "This is a test string";
        String quoted = ctx._quotedString(test);
        assertEquals("\"This is a test string\"", quoted);
    }

    @Test
    public void testColonConcat() {
        String base = "Test base message: ";
        String extra = "Test extra message";
        String colonConcat = ctx._colonConcat(base, extra);
        assertEquals("Test base message: Test extra message", colonConcat);
    }

    @Test
    public void testDesc() {
        String test = "This is a test string";
        String desc = ctx._desc(test);
        assertEquals("This is a test string", desc);
    }

    static class TestClass {
        @JsonProperty("testProperty")
        private int testProp;
        private String testString;

        public int getTestProp() {
            return testProp;
        }

        public void setTestProp(int testProp) {
            this.testProp = testProp;
        }

        public String getTestString() {
            return testString;
        }

        public void setTestString(String testString) {
            this.testString = testString;
        }

        public static class InnerClass {
            private int innerTestProp;

            public int getInnerTestProp() {
                return innerTestProp;
            }

            public void setInnerTestProp(int innerTestProp) {
                this.innerTestProp = innerTestProp;
            }
        }
    }

    static class StringToInteger implements Converter<String, Integer> {
        @Override
        public Integer convert(String text, DeserializationContext ctxt) throws JsonMappingException {
            return Integer.valueOf(text);
        }

        @Override
        public String reverseConvert(Integer value, DeserializationContext ctxt) throws JsonMappingException {
            return value.toString();
        }
    }
}