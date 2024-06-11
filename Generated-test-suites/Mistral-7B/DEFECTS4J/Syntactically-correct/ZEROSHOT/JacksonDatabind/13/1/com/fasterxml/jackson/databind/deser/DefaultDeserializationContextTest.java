package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.DeserializerFinder;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdGeneratorDelegate;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.SimpleBeanPropertyMap;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private ObjectMapper mapper;
    private HandlerInstantiator handlerInstanciator;
    private DeserializerFactory deserializerFactory;
    private DeserializationConfig config;
    private JsonParser parser;
    private InjectableValues injectableValues;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        handlerInstanciator = mapper.getDeserializationConfig().getDefaultInstance()
                .getHandlerInstantiator();
        deserializerFactory = mapper.getDeserializationFactory();
        config = mapper.getDeserializationConfig();
        parser = mock(JsonParser.class);
        injectableValues = new InjectableValues();
        context = new DefaultDeserializationContext(deserializerFactory, null);
    }

    @Test
    public void testConstruction() {
        // Constructor that will pass specified deserializer factory and cache: cache may be null (in which case default implementation
        // will be used), factory can not be null
        DefaultDeserializationContext context1 = new DefaultDeserializationContext(deserializerFactory, null);
        assertEquals(deserializerFactory, context1.getDeserializationFactory());
        assertNull(context1._objectIds);

        // Constructor that will copy settings, but not state, from a source context
        DefaultDeserializationContext contextSrc = new DefaultDeserializationContext(context);
        assertEquals(deserializerFactory, contextSrc.getDeserializationFactory());
        assertEquals(config, contextSrc.getDeserializationConfig());
        assertEquals(parser, contextSrc.getCurrentLocation());
        assertEquals(injectableValues, contextSrc.getInjectableValues());
        assertNotSame(context._objectIds, contextSrc._objectIds);

        // Constructor that will copy all settings, both state and settings, from a source context
        DefaultDeserializationContext contextSrc2 = new DefaultDeserializationContext(context);
        assertEquals(deserializerFactory, contextSrc2.getDeserializationFactory());
        assertEquals(config, contextSrc2.getDeserializationConfig());
        assertEquals(parser, contextSrc2.getCurrentLocation());
        assertEquals(injectableValues, contextSrc2.getInjectableValues());
        assertSame(context._objectIds, contextSrc2._objectIds);

        // Constructor that will create an empty context instance
        DefaultDeserializationContext contextEmpty = new DefaultDeserializationContext();
        assertNull(contextEmpty._objectIds);
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);

        when(resolverType.canUseFor(eq(ObjectIdResolver.class))).thenReturn(true);
        when(resolverType.newForDeserialization(context)).thenReturn(mock(SimpleObjectIdResolver.class));

        context._objectIdResolvers = Arrays.asList(resolverType);

        ReadableObjectId result = context.findObjectId(id, gen);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void testFindObjectId_nullId() {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolverType = mock(ObjectIdResolver.class);

        context._objectIdResolvers = Arrays.asList(resolverType);

        ReadableObjectId result = context.findObjectId(null, gen);
        assertNull(result);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        ObjectIdGeneratorDelegate generator = new ObjectIdGeneratorDelegate();
        generator.setIdKey(IdKey.NAME);
        ObjectNode node = mapper.readValue(
                "{ \"id\": {" +
                        "\"@json\":{" +
                        "\"$id\": \"id\"," +
                        "\"@type\": \"com.example.MyClass\"" +
                        "}," +
                        "\"name\": \"test\"" +
                        "}, " +
                        "\"referenced\": {" +
                        "\"@json\":{" +
                        "\"$ref\": \"#/id\"" +
                        "}" +
                        "}" +
                        "}", ObjectNode.class);

        ReadableObjectId entry1 = new ReadableObjectId(IdKey.NAME, node.path("id").asText());
        ReadableObjectId entry2 = new ReadableObjectId(IdKey.NAME, node.path("referenced").asText());
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        when(resolver.newForDeserialization(context)).thenReturn(resolver);

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(IdKey.NAME, entry1);

        context.checkUnresolvedObjectId();

        context._objectIds.put(IdKey.NAME, entry2);

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        when(deserializer.getClass()).thenReturn(StringDeserializer.class);

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);

        Object deserDef = deserializer;

        JsonDeserializer<String> result = context.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, result);

        when(deserializer.getClass()).thenReturn(JsonDeserializer.None.class);
        result = context.deserializerInstance(ann, deserDef);
        assertNull(result);

        when(deserializer.getClass()).thenReturn(String[].class);
        result = context.deserializerInstance(ann, deserDef);
        assertNull(result);

        when(deserializer.getClass()).thenReturn(JsonDeserializer.class);
        when(deserializer.hasEmptyConstructor()).thenReturn(true);

        result = context.deserializerInstance(ann, deserializer.getClass());
        assertEquals(deserializer, result);

        when(deserializer.hasEmptyConstructor()).thenReturn(false);
        HandlerInstantiator handlerInstantiator = mapper.getDeserializationConfig().getDefaultInstance()
                .getHandlerInstantiator();
        result = handlerInstantiator.deserializerInstance(config, ann, deserializer.getClass());
        assertEquals(deserializer, result);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        KeyDeserializer deserializer = mock(KeyDeserializer.class);
        when(deserializer.getClass()).thenReturn(KeyDeserializer.class);

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);

        Object deserDef = deserializer;

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(deserializer, result);

        when(deserializer.getClass()).thenReturn(JsonDeserializer.None.class);
        result = context.keyDeserializerInstance(ann, deserDef);
        assertNull(result);

        when(deserializer.getClass()).thenReturn(String[].class);
        result = context.keyDeserializerInstance(ann, deserDef);
        assertNull(result);

        when(deserializer.getClass()).thenReturn(KeyDeserializer.class);
        when(deserializer.hasEmptyConstructor()).thenReturn(true);

        result = context.keyDeserializerInstance(ann, deserializer.getClass());
        assertEquals(deserializer, result);

        when(deserializer.hasEmptyConstructor()).thenReturn(false);
        HandlerInstantiator handlerInstantiator = mapper.getDeserializationConfig().getDefaultInstance()
                .getHandlerInstantiator();
        result = handlerInstantiator.keyDeserializerInstance(config, ann, deserializer.getClass());
        assertEquals(deserializer, result);
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = mock(DeserializerFactory.class);
        DefaultDeserializationContext result = context.with(factory);
        assertEquals(factory, result.getDeserializationFactory());
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = mapper.getDeserializationConfig();
        JsonParser jp = mock(JsonParser.class);
        InjectableValues values = new InjectableValues();

        DefaultDeserializationContext result = context.createInstance(config, jp, values);
        assertEquals(deserializerFactory, result.getDeserializationFactory());
        assertEquals(config, result.getDeserializationConfig());
        assertEquals(jp, result.getCurrentLocation());
        assertEquals(values, result.getInjectableValues());
        assertNull(result._objectIds);
        assertNull(result._objectIdResolvers);
    }

    static class StringDeserializer extends JsonDeserializer<String> {

        @JsonDeserialize(using = StringConverter.class)
        static class StringConverter implements Converter<String, String> {

            @Override
            public String convert(String value, DeserializationContext ctxt) {
                return value;
            }

            @Override
            public String readTree(JsonParser p) throws IOException {
                return p.getValueAsString();
            }
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return ctxt.readValueAs(String.class);
        }

        @Override
        public void initialize(DeserializationConfig config, DeserializerProvider provider) throws IOException {
            super.initialize(config, provider);
            provider.findValueDeserializer(SimpleBeanPropertyMap.class);
        }
    }
}