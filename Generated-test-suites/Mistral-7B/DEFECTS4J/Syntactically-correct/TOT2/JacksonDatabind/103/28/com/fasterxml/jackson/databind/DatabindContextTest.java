package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link DatabindContext} abstract class.
 *
 * @author <a href="mailto:werner.dobberpuhl@gmail.com">Werner Dobberpuhl</a>
 */
public class DatabindContextTest {

    private DatabindContext context;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        MapperConfig<?> config = new MapperFactory().getConfig();
        context = config.getDeserializationConfig().getInstance(DatabindContext.class);
        mapper = config.getSerializerProvider().getObjectMapper();
    }

    /**
     * Test for {@link DatabindContext#getConfig()}.
     */
    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    /**
     * Test for {@link DatabindContext#isEnabled(MapperFeature)}.
     */
    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS.with(false)));
    }

    /**
     * Test for {@link DatabindContext#getDefaultPropertyFormat(Class)}.
     */
    @Test
    public void testDefaultPropertyFormat() {
        JsonFormat.Value format = context.getDefaultPropertyFormat(String.class);
        assertNotNull(format);
    }

    /**
     * Test for {@link DatabindContext#getAttribute(Object)}.
     */
    @Test
    public void testGetAttribute() {
        Object attr = context.getAttribute("testKey");
        assertNull(attr);

        context.setAttribute("testKey", "testValue");
        Object getAttr = context.getAttribute("testKey");
        assertEquals("testValue", getAttr);
    }

    /**
     * Test for {@link DatabindContext#getTypeFactory()}.
     */
    @Test
    public void testTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);

        Class<?> clazz = JavaType.class;
        JavaType javaType = typeFactory.constructType(clazz);
        assertEquals(clazz, javaType.getRawClass());
    }

    /**
     * Test for {@link DatabindContext#constructType(Type)}.
     */
    @Test
    public void testConstructType() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = context.constructType(intClass.getTypeParameters()[0]);
        assertEquals(intClass, javaType.getRawClass());
    }

    /**
     * Test for {@link DatabindContext#resolveSubType(JavaType, String)}.
     * @throws JsonMappingException
     */
    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.String");
        assertEquals(String.class, subType.getRawClass());
    }

    /**
     * Test for {@link DatabindContext#invalidTypeIdException(JavaType, String, String)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "java.lang.Integer", "Not a subtype");
    }

    /**
     * Test for {@link DatabindContext#objectIdGeneratorInstance(Annotated, ObjectIdInfo)}.
     */
    @Test
    public void testObjectIdGeneratorInstance() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonTypeInfo.Id.NAME, null, null);
        ObjectIdGenerator<?> generator = context.objectIdGeneratorInstance(null, objectIdInfo);
        assertNotNull(generator);
    }

    /**
     * Test for {@link DatabindContext#objectIdResolverInstance(Annotated, ObjectIdInfo)}.
     */
    @Test
    public void testObjectIdResolverInstance() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonTypeInfo.Id.NAME, null, null);
        ObjectIdResolver resolver = context.objectIdResolverInstance(null, objectIdInfo);
        assertNotNull(resolver);
    }

    /**
     * Test for {@link DatabindContext#converterInstance(Annotated, Object)}.
     */
    @Test
    public void testConverterInstance() {
        Annotated annotated = new AnnotatedClass(String.class);
        Converter<String, Integer> converterDef = new StringToIntConverter();

        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
        assertTrue(converter instanceof StringToIntConverter);
    }

    /**
     * Test for {@link DatabindContext#reportBadDefinition(JavaType, String)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType javaType = context.getTypeFactory().constructType(String[].class);
        context.reportBadDefinition(javaType, "Invalid definition: array of String[]");
    }

    /**
     * Test for {@link DatabindContext#reportBadDefinition(Class, String)}.
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionClass() throws JsonMappingException {
        context.reportBadDefinition(Object.class, "Invalid definition: Object");
    }
}

class AnnotatedClass implements Annotated {

    private Class<?> clazz;

    public AnnotatedClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<?> getRawClass() {
        return clazz;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getAnnotations(Class<? extends Annotation> annotationType) {
        return new Annotation[0];
    }

    @Override
    public String getClassName() {
        return clazz.getName();
    }

    @Override
    public String getPropertyName() {
        return "";
    }
}

class StringToIntConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String value, SerializerProvider provider) throws IllegalAccessException, InstantiationException {
        return Integer.valueOf(value);
    }

    @Override
    public String convert(Integer value, DeserializationContext ctxt) throws IOException {
        return value.toString();
    }
}