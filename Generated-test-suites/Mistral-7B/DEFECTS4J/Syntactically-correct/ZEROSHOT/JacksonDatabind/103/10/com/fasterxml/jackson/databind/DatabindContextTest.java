package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector.ResultHolder;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test class for {@link DatabindContext}.
 */
public class DatabindContextTest {

    private DeserializationContext context;
    private HandlerInstantiator handlerInstantiator;
    private MapperConfig mapperConfig;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        mapperConfig = new MapperConfig();
        handlerInstantiator = mock(HandlerInstantiator.class);
        when(mapperConfig.getHandlerInstantiator()).thenReturn(handlerInstantiator);
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.AUTO_DETECT_TYPES, false);
        objectMapper.setConfig(mapperConfig);
        context = objectMapper.getDeserializationContext();
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        assertSame(mapperConfig, config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertSame(objectMapper.getAnnotationIntrospector(), annotationIntrospector);
    }

    @Test
    public void testIsEnabled() {
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        Locale locale = context.getLocale();
        assertNull(locale);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = context.getTimeZone();
        assertNull(timeZone);
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        Class<?> baseType = JsonFormat.NumberFormat.class;
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(baseType);
        assertNull(defaultPropertyFormat);
    }

    @Test
    public void testGetAttribute() {
        context.setAttribute("key", "value");
        Object attribute = context.getAttribute("key");
        assertSame("value", attribute);
    }

    @Test
    public void testSetAttribute() {
        context.setAttribute("key", "value");
        DatabindContext newContext = mock(DatabindContext.class);
        Object newAttribute = "newValue";
        when(newContext.getAttribute("key")).thenReturn(null);
        DatabindContext result = context.setAttribute("key", newContext).setAttribute("key", newAttribute);
        assertSame(newContext, result);
        assertSame(newAttribute, result.getAttribute("key"));
    }

    @Test
    public void testConstructType() throws JsonMappingException {
        Type type = Object.class;
        JavaType javaType = context.constructType(type);
        assertSame(TypeFactory.defaultInstance.constructType(type), javaType);
    }

    @Test
    public void testConstructSpecializedType() throws JsonMappingException {
        JavaType baseType = TypeFactory.defaultInstance.constructType(String.class);
        Class<?> subClass = Integer.class;
        JavaType javaType = context.constructSpecializedType(baseType, subClass);
        JavaType expected = TypeFactory.defaultInstance.constructSpecializedType(baseType, subClass);
        assertSame(expected, javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithInvalidTypeId() throws JsonMappingException {
        JavaType baseType = TypeFactory.defaultInstance.constructType(String.class);
        String typeId = "InvalidTypeId";
        context.invalidTypeIdException(baseType, typeId, null);
    }

    @Test
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = TypeFactory.defaultInstance.constructType(String.class);
        String typeId = "String";
        String extraDesc = "subtype";
        when(handlerInstantiator.objectIdGeneratorInstance(any(MapperConfig.class), any(Annotated.class), any(Class.class)))
                .thenReturn(mock(ObjectIdGenerator.class));
        when(handlerInstantiator.resolverIdGeneratorInstance(any(MapperConfig.class), any(Annotated.class), any(Class.class)))
                .thenReturn(mock(ObjectIdResolver.class));
        try {
            context.invalidTypeIdException(baseType, typeId, extraDesc);
        } catch (JsonMappingException e) {
            String expectedMsg = String.format("Not a subtype of %s: %s", baseType.getName(), extraDesc);
            assertSame(expectedMsg, e.getMessage());
        }
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertSame(TypeFactory.defaultInstance, typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String.class);
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String.class);
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String.class);
        Annotated annotated = mock(Annotated.class);
        Object converterDef = StringToIntConverter.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String.class);
        String msg = "Bad definition for type %s";
        Object result = context.reportBadDefinition(javaType, msg);
        assertNull(result);
    }

    @Test
    public void testReportBadDefinitionWithClass() throws JsonMappingException {
        Class<?> clazz = String.class;
        String msg = "Bad definition for type %s";
        Object result = context.reportBadDefinition(clazz, msg);
        assertNull(result);
    }

    static class StringToIntConverter {
        @JsonProperty
        @JsonIgnore
        public int value;

        @JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
        @JsonTypeName("StringToInt")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = MySubClass.class, name = "MySubClass")
        })
        static class MySubClass {
            @JsonProperty("myVal")
            public int myValue;
        }
    }
}