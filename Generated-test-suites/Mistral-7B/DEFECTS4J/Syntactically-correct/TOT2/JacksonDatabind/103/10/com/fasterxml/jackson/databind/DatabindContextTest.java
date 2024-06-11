package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DatabindContextTest {

    private JsonMapper mapper;
    private DatabindContext context;

    @Before
    public void setup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mapper = objectMapper.getDeserializerProvider().getDeserializerFactory().findValueDeserializer(
                Object.class, null, null, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        context = mapper.getDeserializationContext();
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        Locale locale = ContextTestUtils.getDefaultLocale();
        context.setLocale(locale);
        assertEquals(locale, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = ContextTestUtils.getDefaultTimeZone();
        context.setTimeZone(timeZone);
        assertEquals(timeZone, context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        assertNotNull(context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testGetAttribute() {
        Object attribute = context.setAttribute("testKey", "testValue").getAttribute("testKey");
        assertEquals("testValue", attribute);
    }

    @Test
    public void testConstructType() {
        Type type = new TypeReference<List<String>>() {}.getType();
        JavaType javaType = context.getTypeFactory().constructType(type);
        assertEquals(javaType.getRawClass(), java.util.List.class);
        assertEquals(javaType.getComponentType().getRawClass(), java.lang.String.class);
    }

    @Test
    public void testResolveSubType() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subClass = "java.util.ArrayList";
        JavaType subType = context.resolveSubType(baseType, subClass);
        assertNotNull(subType);
        assertEquals(subType.getRawClass(), java.util.ArrayList.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "com.non.existent.package.MyClass";
        context.invalidTypeIdException(baseType, typeId, null);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        ObjectIdGenerator<?> generator = context.objectIdGeneratorInstance(ContextTestUtils.createAnnotated(MyClass.class));
        assertNotNull(generator);
    }

    @Test
    public void testObjectIdResolverInstance() throws Exception {
        ObjectIdResolver resolver = context.objectIdResolverInstance(ContextTestUtils.createAnnotated(MyClass.class));
        assertNotNull(resolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        Annotated annotated = ContextTestUtils.createAnnotated(MyStringConverter.class);
        Converter<Object, Object> converter = context.converterInstance(annotated, null);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() {
        JavaType type = context.getTypeFactory().constructType(String.class);
        assertNull(context.reportBadDefinition(type, "Test error"));
    }

    @Test
    public void testFormat() {
        String msg = "Test message: {0} {1} {2}";
        String formattedMsg = context._format(msg, "test1", "test2", "test3");
        assertEquals("Test message: test1 test2 test3", formattedMsg);
    }

    @Test
    public void testTruncate() {
        String desc = "This is a very long description that exceeds the maximum error message length";
        String truncatedDesc = context._truncate(desc);
        assertTrue(truncatedDesc.contains("This is a very long description that exceeds the maximum error message length]..."));
    }

    @Test
    public void testQuotedString() {
        String desc = "This is a description with a double quote: \"some text\"";
        String quotedDesc = context._quotedString(desc);
        assertEquals("\"This is a description with a double quote: \"some text\"\"", quotedDesc);
    }

    @Test
    public void testColonConcat() {
        String msgBase = "Test base message: ";
        String extra = "some extra data";
        String concatMsg = context._colonConcat(msgBase, extra);
        assertEquals("Test base message: some extra data", concatMsg);
    }

    @Test
    public void testDesc() {
        String desc = "Test description";
        String truncatedDesc = context._desc(desc);
        assertEquals("Test description", truncatedDesc);
    }

    private static class MyClass {
        @JsonAutoDetect
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String name;

        @JsonProperty("myProperty")
        private int myInt;

        @JsonDeserialize(using = FromStringDeserializer.class)
        @JsonProperty("myEnum")
        private MyEnum myEnum;
    }

    private static class MyEnum {
        public static final MyEnum VALUE1 = new MyEnum("VALUE1");
        public static final MyEnum VALUE2 = new MyEnum("VALUE2");

        private final String value;

        MyEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class MyStringConverter {
        @JsonDeserialize(using = FromStringDeserializer.class)
        public static class MyStringConverterInner {
            @JsonProperty("value")
            private String value;

            public String getValue() {
                return value;
            }
        }

        @JsonProperty("myConverter")
        public MyStringConverterInner myConverter;
    }
}