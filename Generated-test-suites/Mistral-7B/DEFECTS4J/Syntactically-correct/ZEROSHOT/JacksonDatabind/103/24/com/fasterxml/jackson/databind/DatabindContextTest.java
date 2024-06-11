package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.StaticSubType;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Test class for com.fasterxml.jackson.databind.DatabindContext
 */
public class DatabindContextTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void before() {
        mapper = new ObjectMapper();
    }

    /**
     * Test method for {@link DatabindContext#getConfig()}
     */
    @Test
    public void testGetConfig() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    /**
     * Test method for {@link DatabindContext#getAnnotationIntrospector()}
     */
    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        AnnotationIntrospector introspector = context.getAnnotationIntrospector();
        assertNotNull(introspector);
    }

    /**
     * Test method for {@link DatabindContext#isEnabled(MapperFeature)}
     */
    @Test
    public void testIsEnabled() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_SERIALIZERS));
        assertFalse(context.isEnabled(MapperFeature.CAN_OVERLOAD_ACCESS_MODIFIERS));
    }

    /**
     * Test method for {@link DatabindContext#canOverrideAccessModifiers()}
     */
    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        assertFalse(context.canOverrideAccessModifiers());
    }

    /**
     * Test method for {@link DatabindContext#getActiveView()}
     */
    @Test
    public void testGetActiveView() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        assertNull(context.getActiveView());
    }

    /**
     * Test method for {@link DatabindContext#getLocale()}
     */
    @Test
    public void testGetLocale() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        assertEquals("default locale", Locale.getDefault(), context.getLocale());
    }

    /**
     * Test method for {@link DatabindContext#getTimeZone()}
     */
    @Test
    public void testGetTimeZone() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        assertEquals("default timezone", TimeZone.getDefault(), context.getTimeZone());
    }

    /**
     * Test method for {@link DatabindContext#getDefaultPropertyFormat(Class)}
     */
    @Test
    public void testgetDefaultPropertyFormat() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        assertEquals(JsonFormat.Value.class, context.getDefaultPropertyFormat(String.class).getClass());
        assertEquals(JsonFormat.Value.class, context.getDefaultPropertyFormat(Integer.class).getClass());
        assertNull(context.getDefaultPropertyFormat(Object.class));
    }

    /**
     * Test method for {@link DatabindContext#getAttribute(Object)}
     */
    @Test
    public void testGetAttribute() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        context.setAttribute("testKey", "testValue");
        assertEquals("testValue", context.getAttribute("testKey"));
        assertNull(context.getAttribute("unknownKey"));
    }

    /**
     * Test method for {@link DatabindContext#setAttribute(Object, Object)}
     */
    @Test
    public void testSetAttribute() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        context.setAttribute("testKey", null);
        context.setAttribute("testKey", "testValue");
        assertEquals("testValue", context.getAttribute("testKey"));
        assertNull(context.getAttribute("unknownKey"));
    }

    /**
     * Test method for {@link DatabindContext#constructType(Type)}
     */
    @Test
    public void testConstructType() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        Type type = String[].class;
        JavaType javaType = context.constructType(type);
        assertEquals(String[].class, javaType.getRawClass());
    }

    /**
     * Test method for {@link DatabindContext#constructSpecializedType(JavaType, Class)}
     */
    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.constructSpecializedType(baseType, String[].class);
        assertEquals(String[].class, subType.getRawClass());
    }

    /**
     * Test method for {@link DatabindContext#resolveSubType(JavaType, String)}
     */
    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.String[]");
        assertEquals(String[].class, subType.getRawClass());

        JavaType failSubType = context.resolveSubType(baseType, "java.lang.Integer");
        fail(failSubType + " should be null");
    }

    /**
     * Test method for {@link DatabindContext#invalidTypeIdException(JavaType, String, String)}
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "java.lang.Object", "Should not reach here");
    }

    /**
     * Test method for {@link DatabindContext#getTypeFactory()}
     */
    @Test
    public void testgetTypeFactory() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertEquals(TypeFactory.class, typeFactory.getClass());
    }

    /**
     * Test method for {@link DatabindContext#objectIdGeneratorInstance(Annotated, ObjectIdInfo)}
     */
    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdInfo objectIdInfo = annotated.getIdInfo();
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    /**
     * Test method for {@link DatabindContext#objectIdResolverInstance(Annotated, ObjectIdInfo)}
     */
    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdInfo objectIdInfo = annotated.getIdInfo();
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    /**
     * Test method for {@link DatabindContext#converterInstance(Annotated, Object)}
     */
    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        Annotated annotated = new AnnotatedClass(String[].class);
        Object converterDef = annotated.getAnnotation(JsonSubTypes.With.class).value()[0];
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    /**
     * Test method for {@link DatabindContext#reportBadDefinition(JavaType, String)}
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        context.reportBadDefinition(javaType, "Invalid definition");
    }

    /**
     * Test method for {@link DatabindContext#reportBadDefinition(Class, String)}
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionClass() throws JsonMappingException {
        DatabindContext context = mapper.getSerializationConfig().getContext();
        context.reportBadDefinition(String.class, "Invalid definition");
    }
}