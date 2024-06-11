package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    private static final String JSON_WITH_TYPE_ID = "{\"typeId\":\"MyType\",\"value\":42}";
    private static final String JSON_WITHOUT_TYPE_ID = "{\"value\":42}";

    private DeserializationContext ctxt;
    private ObjectMapper mapper;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() throws Exception {
        ctxt = mock(DeserializationContext.class);
        mapper = new ObjectMapper();
        typeDeserializer = new TestTypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mock(TypeIdResolver.class),
                "typePropertyName",
                false,
                mapper.getTypeFactory().constructType(String.class)
        );
    }

    @Test
    public void testDeserializeWithTypeId() throws Exception {
        when(ctxt.findContextualValueDeserializer(mapper.getTypeFactory().constructType(String.class), null)).thenReturn(new StringDeserializer());
        JsonParser jp = mapper.getFactory().createParser(JSON_WITH_TYPE_ID);
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);
        assertEquals("MyType", result);
    }

    @Test
    public void testDeserializeWithoutTypeId() throws Exception {
        JsonParser jp = mapper.getFactory().createParser(JSON_WITHOUT_TYPE_ID);
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, null);
        assertNull(result);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        when(ctxt.findContextualValueDeserializer(mapper.getTypeFactory().constructType("com.example.MyType"), null)).thenReturn(new MyTypeDeserializer());
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, "MyType");
        assertEquals(MyTypeDeserializer.class, deserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownType() throws Exception {
        when(ctxt.getTypeIdResolver().typeFromId(ctxt, "UnknownType")).thenReturn(null);
        typeDeserializer._findDeserializer(ctxt, "UnknownType");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(ctxt.findContextualValueDeserializer(mapper.getTypeFactory().constructType(Void.TYPE), null)).thenReturn(NullifyingDeserializer.instance);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertEquals(NullifyingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializerWithFailOnInvalidSubtype() throws Exception {
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() { this(null); }
        public StringDeserializer(Class<String> vc) { super(vc); }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class MyTypeDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {
        public TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            // no implementation needed for test
            return null;
        }
    }
}