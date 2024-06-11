package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link DatabindContext} abstract class.
 */
public class DatabindContextTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    /**
     * Test for {@link DatabindContext#getConfig()}.
     */
    @Test
    public void testGetConfig() {
        DatabindContext context = mapper.getDeserializationContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);

        // Test with null config
        context = mapper.getSerializerProvider();
        config = context.getConfig();
        assertNull(config);
    }

    /**
     * Test for {@link DatabindContext#isEnabled(MapperFeature)}.
     */
    @Test
    public void testIsEnabled() {
        DatabindContext context = mapper.getDeserializationContext();
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_SERIALIZERS));
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        assertFalse(context.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));

        // Test with null context
        context = mapper.getSerializerProvider();
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_SERIALIZERS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        assertFalse(context.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
    }

    /**
     * Test for {@link DatabindContext#getDefaultPropertyFormat(Class)}.
     */
    @Test
    public void testDefaultPropertyFormat() {
        DatabindContext context = mapper.getDeserializationContext();
        JsonFormat.Value format = context.getDefaultPropertyFormat(String.class);
        assertNotNull(format);

        // Test with null context
        context = mapper.getSerializerProvider();
        assertNull(context.getDefaultPropertyFormat(String.class));
    }

    /**
     * Test for {@link DatabindContext#getAttribute(Object)}.
     */
    @Test
    public void testGetAttribute() {
        DatabindContext context = mapper.getDeserializationContext();
        Object attribute = context.getAttribute("test");
        assertNull(attribute);

        context.setAttribute("test", "testValue");
        attribute = context.getAttribute("test");
        assertEquals("testValue", attribute);

        // Test with null context
        context = mapper.getSerializerProvider();
        assertNull(context.getAttribute("test"));
    }

    /**
     * Test for {@link DatabindContext#constructType(Type)}.
     */
    @Test
    public void testConstructType() {
        DatabindContext context = mapper.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();

        Type intType = new TypeToken<Integer>() {}.getType();
        JavaType intJavaType = context.constructType(intType);
        assertEquals(Integer.class, intJavaType.getRawClass());

        Type stringListType = new TypeToken<List<String>>() {}.getType();
        JavaType stringListJavaType = context.constructType(stringListType);
        assertEquals(List.class, stringListJavaType.getRawClass());
        assertEquals(String.class, stringListJavaType.getComponentType().getRawClass());

        // Test with null context
        context = mapper.getSerializerProvider();
        assertNull(context.constructType(intType));
    }

    /**
     * Test for {@link DatabindContext#resolveSubType(JavaType, String)}.
     */
    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = mapper.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();

        JavaType javaIntType = typeFactory.constructType(Integer.class);
        JavaType javaStringType = typeFactory.constructType(String.class);

        JavaType javaIntSubType = context.resolveSubType(javaIntType, "java.util.Random");
        assertEquals(java.util.Random.class, javaIntSubType.getRawClass());

        JavaType javaStringSubType = context.resolveSubType(javaStringType, "java.lang.String[]");
        assertEquals(String[].class, javaStringSubType.getRawClass());

        // Test with null context
        context = mapper.getSerializerProvider();
        assertNull(context.resolveSubType(javaIntType, "java.util.Random"));
    }

    /**
     * Test for {@link DatabindContext#invalidTypeIdException(JavaType, String, String)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaIntType = context.getTypeFactory().constructType(Integer.class);
        JavaType javaStringType = context.getTypeFactory().constructType(String.class);

        context.invalidTypeIdException(javaIntType, "java.lang.String", "Invalid type id");
        context.invalidTypeIdException(javaStringType, "java.util.Integer", "Not a subtype");
    }

    /**
     * Test for {@link DatabindContext#getTypeFactory()}.
     */
    @Test
    public void testTypeFactory() {
        DatabindContext context = mapper.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();

        Type intType = new TypeToken<Integer>() {}.getType();
        JavaType intJavaType = typeFactory.constructType(intType);
        assertEquals(Integer.class, intJavaType.getRawClass());

        // Test with null context
        context = mapper.getSerializerProvider();
        assertNull(context.getTypeFactory());
    }

    /**
     * Test for {@link DatabindContext#objectIdGeneratorInstance(Annotated, ObjectIdInfo)}.
     */
    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = mapper.getDeserializationContext();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new StdSerializer<>(String.class), null, null);

        ObjectIdGenerator<?> generator = context.objectIdGeneratorInstance(ObjectMappings.any(), objectIdInfo);
        assertNotNull(generator);
        assertTrue(generator instanceof ObjectIdGenerator);

        // Test with null context
        context = mapper.getSerializerProvider();
        assertNull(context.objectIdGeneratorInstance(ObjectMappings.any(), objectIdInfo));
    }

    /**
     * Test for {@link DatabindContext#objectIdResolverInstance(Annotated, ObjectIdInfo)}.
     */
    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = mapper.getDeserializationContext();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new StdSerializer<>(String.class), null, null);

        ObjectIdResolver resolver = context.objectIdResolverInstance(ObjectMappings.any(), objectIdInfo);
        assertNotNull(resolver);
        assertTrue(resolver instanceof ObjectIdResolver);

        // Test with null context
        context = mapper.getSerializerProvider();
        assertNull(context.objectIdResolverInstance(ObjectMappings.any(), objectIdInfo));
    }

    /**
     * Test for {@link DatabindContext#converterInstance(Annotated, Object)}.
     */
    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = mapper.getDeserializationContext();

        Annotated annotated = ObjectMappings.any();
        ObjectConverter converterDef = new StringDeserializer();

        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
        assertTrue(converter instanceof Converter);

        // Test with null context
        context = mapper.getSerializerProvider();
        assertNull(context.converterInstance(annotated, converterDef));
    }

    /**
     * Test for {@link DatabindContext#reportBadDefinition(JavaType, String)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = mapper.getDeserializationContext();
        JavaType javaIntType = context.getTypeFactory().constructType(Integer.class);

        context.reportBadDefinition(javaIntType, "Invalid definition for Integer type");
    }
}