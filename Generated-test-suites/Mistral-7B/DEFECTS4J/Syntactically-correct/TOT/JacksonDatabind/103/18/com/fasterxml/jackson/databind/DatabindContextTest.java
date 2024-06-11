package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatabindContextTest {

    private DatabindContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        context = new BaseDatabindContext();
        objectMapper = new ObjectMapper(new JsonFactory(context));
    }

    @Test
    public void testGenericConfigAccess() {
        MapperConfig<?> config = context.getConfig();
        assertEquals(MapperFeature.AUTO_DETECT_TYPES, config.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        config.disable(MapperFeature.AUTO_DETECT_TYPES);
        assertEquals(false, context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));

        config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        assertEquals(JsonInclude.Include.NON_NULL, context.getDefaultPropertyInclusion());
    }

    @Test
    public void testAccessToSpecificConfigSettings() {
        assertEquals(MapperFeature.DEFAULT_INTERNAL_VALUE_INJECTOR, context.isEnabled(MapperFeature.DEFAULT_INTERNAL_VALUE_INJECTOR));
        context.disable(MapperFeature.DEFAULT_INTERNAL_VALUE_INJECTOR);
        assertEquals(false, context.isEnabled(MapperFeature.DEFAULT_INTERNAL_VALUE_INJECTOR));

        assertEquals(SerializationFeature.FAIL_ON_EMPTY_BEANS, context.canOverrideAccessModifiers() && context.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
        context.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        assertEquals(false, context.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
    }

    @Test
    public void testLocaleAndTimeZone() {
        context.setLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, context.getLocale());

        context.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), context.getTimeZone());
    }

    @Test
    public void testDefaultPropertyFormat() {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        assertEquals(JsonFormat.Value.DATE_TIME, context.getDefaultPropertyFormat(javaType));
    }

    @Test
    public void testGenericAttributes() {
        context.setAttribute("testKey", "testValue");
        assertEquals("testValue", context.getAttribute("testKey"));

        context.setAttribute("testKey", null);
        assertEquals(null, context.getAttribute("testKey"));
    }

    @Test
    public void testTypeInstantiationResolution() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.util.List");
        assertEquals(JavaType.constructRawType(ArrayList.class), subType);

        JavaType baseType2 = context.getTypeFactory().constructType(Integer.class);
        try {
            JavaType subType2 = context.resolveSubType(baseType2, "com.example.MyIntClass");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}