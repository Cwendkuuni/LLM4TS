package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser. standard.CreatorProperty;
import com.fasterxml.jackson.databind.deser.standard.ObjectIdResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.testng.Assert.*;

public class CreatorPropertyTest {

    private static final ObjectIdResolver idResolver = new ObjectIdResolver();

    @Test
    public void testCreatorPropertyDeserialization() throws IOException, JsonProcessingException {
        AnnotatedClass ac = new AnnotatedClass(CreatorPropertyTest.class, null);
        ObjectReader reader = new JsonMapper().readerFor(CreatorProperty.class);
        CreatorProperty property = reader.readValue(getClass().getResource("testData.json"), CreatorProperty.class, ac);
        assertEquals(property.getName(), "testProperty");
        assertEquals(property.getType(), String.class);
        assertEquals(property.getWrapperName(), "wrapper");
        assertEquals(property.getTypeDeser(), StringDeserializer.instance);
        assertEquals(property.getContextAnnotations(), ac.annotations());
        assertEquals(property.getAnnotated(), ac.getMembers()[0]);
        assertEquals(property.getCreatorIndex(), 0);
        assertEquals(property.getInjectableValueId(), "injectId");
        assertNull(property._fallbackSetter);
        assertEquals(property._ignorable, false);
    }

    @Test
    public void testCreatorPropertyDeserializationWithInjectValue() throws IOException, JsonProcessingException {
        AnnotatedClass ac = new AnnotatedClass(CreatorPropertyTest.class, null);
        ObjectReader reader = new JsonMapper().readerFor(CreatorProperty.class);
        CreatorProperty property = reader.readValue(getClass().getResource("testDataWithInjectValue.json"), CreatorProperty.class, ac);
        assertEquals(property.getName(), "testProperty");
        assertEquals(property.getType(), String.class);
        assertEquals(property.getWrapperName(), "wrapper");
        assertEquals(property.getTypeDeser(), StringDeserializer.instance);
        assertEquals(property.getContextAnnotations(), ac.annotations());
        assertEquals(property.getAnnotated(), ac.getMembers()[0]);
        assertEquals(property.getCreatorIndex(), 0);
        assertEquals(property.getInjectableValueId(), "injectId");
        assertNotNull(property._fallbackSetter);
        assertEquals(property._ignorable, false);
    }

    @Test
    public void testCreatorPropertyDeserializationWithFallbackSetter() throws IOException, JsonProcessingException, InstantiationException, IllegalAccessException, InvocationTargetException {
        AnnotatedClass ac = new AnnotatedClass(CreatorPropertyTest.class, null);
        ObjectReader reader = new JsonMapper().readerFor(CreatorProperty.class);
        CreatorProperty property = reader.readValue(getClass().getResource("testDataWithFallbackSetter.json"), CreatorProperty.class, ac);
        assertEquals(property.getName(), "testProperty");
        assertEquals(property.getType(), String.class);
        assertEquals(property.getWrapperName(), "wrapper");
        assertEquals(property.getTypeDeser(), StringDeserializer.instance);
        assertEquals(property.getContextAnnotations(), ac.annotations());
        assertEquals(property.getAnnotated(), ac.getMembers()[0]);
        assertEquals(property.getCreatorIndex(), 0);
        assertEquals(property.getInjectableValueId(), null);
        assertNotNull(property._fallbackSetter);
        assertEquals(property._ignorable, false);
    }

    @Test
    public void testCreatorPropertyDeserializationWithInjectAndFallbackSetter() throws IOException, JsonProcessingException, InstantiationException, IllegalAccessException, InvocationTargetException {
        AnnotatedClass ac = new AnnotatedClass(CreatorPropertyTest.class, null);
        ObjectReader reader = new JsonMapper().readerFor(CreatorProperty.class);
        CreatorProperty property = reader.readValue(getClass().getResource("testDataWithInjectAndFallbackSetter.json"), CreatorProperty.class, ac);
        assertEquals(property.getName(), "testProperty");
        assertEquals(property.getType(), String.class);
        assertEquals(property.getWrapperName(), "wrapper");
        assertEquals(property.getTypeDeser(), StringDeserializer.instance);
        assertEquals(property.getContextAnnotations(), ac.annotations());
        assertEquals(property.getAnnotated(), ac.getMembers()[0]);
        assertEquals(property.getCreatorIndex(), 0);
        assertEquals(property.getInjectableValueId(), "injectId");
        assertNotNull(property._fallbackSetter);
        assertEquals(property._ignorable, false);
    }

    @Test
    public void testCreatorPropertyDeserializationWithIgnore() throws IOException, JsonProcessingException {
        AnnotatedClass ac = new AnnotatedClass(CreatorPropertyTest.class, null);
        ObjectReader reader = new JsonMapper().readerFor(CreatorProperty.class);
        CreatorProperty property = reader.readValue(getClass().getResource("testDataWithIgnore.json"), CreatorProperty.class, ac);
        assertEquals(property.getName(), "testProperty");
        assertEquals(property.getType(), String.class);
        assertEquals(property.getWrapperName(), "wrapper");
        assertEquals(property.getTypeDeser(), StringDeserializer.instance);
        assertEquals(property.getContextAnnotations(), ac.annotations());
        assertEquals(property.getAnnotated(), ac.getMembers()[0]);
        assertEquals(property.getCreatorIndex(), 0);
        assertEquals(property.getInjectableValueId(), null);
        assertNull(property._fallbackSetter);
        assertEquals(property._ignorable, true);
    }

    @Test(expectedExceptions = InvalidDefinitionException.class)
    public void testCreatorPropertyMissingSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new StandardAnnotationIntrospector());
        ObjectReader reader = mapper.readerFor(CreatorProperty.class);
        reader.readValue(getClass().getResource("testDataMissingSetter.json"));
    }
}

class StringDeserializer extends StdDeserializer<String> {
    static final Long serialVersionUID = 1L;

    public StringDeserializer() {
        this(null);
    }

    public StringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }

    @Override
    public String deserializeValue(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }

    @Override
    public void acceptAnnotation(Annotation ann) throws Validated {
        if (ann instanceof JsonDeserialize) {
            JsonDeserialize jsonDeserialize = (JsonDeserialize) ann;
            if (jsonDeserialize.contentAsStream() != null) {
                throw new RuntimeException("contentAsStream is not supported for StringDeserializer");
            }
        }
    }
}