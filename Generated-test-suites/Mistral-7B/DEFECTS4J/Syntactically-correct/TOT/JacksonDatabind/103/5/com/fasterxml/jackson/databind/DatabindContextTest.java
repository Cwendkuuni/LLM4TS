package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabindContextTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetConfig() throws Exception {
        DatabindContext context = mapper.getDeserializationContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testIsEnabled() throws Exception {
        DatabindContext context = mapper.getDeserializationContext();
        assertTrue(context.isEnabled(MapperFeature.VERIFY_UNKNOWN_PROPERTIES));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_PROPERTIES));
    }

    @Test
    public void testGetActiveView() throws Exception {
        DatabindContext context = mapper.getDeserializationContext();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() throws Exception {
        DatabindContext context = mapper.getDeserializationContext();
        assertEquals("en_US", context.getLocale().toString());
    }

    @Test
    public void testGetTimeZone() throws Exception {
        DatabindContext context = mapper.getDeserializationContext();
        assertEquals("America/Los_Angeles", context.getTimeZone().getID());
    }

    @Test
    public void testGetDefaultPropertyFormat() throws Exception {
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        JsonFormat.Value format = context.getDefaultPropertyFormat(javaType);
        assertEquals(JsonFormat.Feature.WRITE_DATES_AS_TIMESTAMPS, format.getFeature("WRITE_DATES_AS_TIMESTAMPS"));
    }

    @Test
    public void testGetAttribute() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        context.setAttribute("foo", "bar");
        assertEquals("bar", context.getAttribute("foo"));
        assertNull(context.getAttribute("baz"));
    }

    @Test
    public void testGetTypeFactory() throws Exception {
        DatabindContext context = mapper.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subclass = "com.example.InvalidTypeId";
        context.invalidTypeIdException(baseType, subclass, "Invalid type id");
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getAnnotationIntrospector().findPropertyType("foo", Foo.class);
        ObjectIdInfo objectIdInfo = annotatedClass.findAnnotation(JsonTypeInfo.Id.class);
        DatabindContext context = mapper.getDeserializationContext();
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedMember annotatedMember = mapper.getAnnotationIntrospector().findMemberProperty("bar", Foo.class, "id");
        ObjectIdInfo objectIdInfo = annotatedMember.findAnnotation(JsonTypeInfo.Id.class);
        DatabindContext context = mapper.getDeserializationContext();
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotatedMember, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getAnnotationIntrospector().findPropertyType("foo", Foo.class);
        Object converterDef = annotatedClass.findAnnotation(JsonAdapter.class);
        DatabindContext context = mapper.getDeserializationContext();
        Converter<Object, Object> converter = context.converterInstance(annotatedClass, converterDef);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaType = context.getTypeFactory().constructType(new TypeReference<>() {}.getType());
        String msg = "Invalid definition: " + _quotedString(_desc(javaType.getRawClass().getName())) + _desc(javaType.getName());
        String report = (String) context.reportBadDefinition(javaType, msg);
        assertEquals(_format("Invalid definition: %s %s", _quotedString(javaType.getRawClass().getName()), _quotedString(javaType.getName())), report);
    }
}