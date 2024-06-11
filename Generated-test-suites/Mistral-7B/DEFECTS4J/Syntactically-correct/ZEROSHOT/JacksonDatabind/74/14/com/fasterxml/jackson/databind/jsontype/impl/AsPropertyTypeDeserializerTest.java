package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AsPropertyTypeDeserializerTest extends BaseMapTest {

    private static final AsPropertyTypeDeserializer TESTEE;

    static {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(HashMap.class);
        TypeIdResolver idResolver = mapper.getPolymorphicTypeIdResolver();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, "type", true, null, As.PROPERTY);
        TESTEE = (AsPropertyTypeDeserializer) deserializer;
    }

    @org.junit.Test
    public void testDeserializeTypedFromObject() throws Exception {
        String json = "{\"type\":\"StringType\",\"value\":\"test\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        assertEquals("test", TESTEE.deserializeTypedFromObject(jsonParser, context).getValue());

        json = "{\"value\":42}";
        JsonParser jsonParser2 = mapper.getFactory().createParser(json);
        assertNull(TESTEE.deserializeTypedFromObject(jsonParser2, context));

        json = "[]";
        JsonParser jsonParser3 = mapper.getFactory().createParser(json);
        assertNull(TESTEE.deserializeTypedFromObject(jsonParser3, context));
    }

    @org.junit.Test
    public void testDeserializeTypedForId() throws Exception {
        String json = "{\"type\":\"StringType\",\"value\":\"test\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(jsonParser, context);

        assertEquals("test", TESTEE.deserializeTypedForId(jsonParser, context, tokenBuffer).getValue());

        json = "{\"value\":42}";
        jsonParser = mapper.getFactory().createParser(json);
        tokenBuffer = new TokenBuffer(jsonParser, context);
        assertNull(TESTEE.deserializeTypedForId(jsonParser, context, tokenBuffer));
    }

    @org.junit.Test
    public void testDeserializeTypedUsingDefaultImpl() throws Exception {
        String json = "42";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        assertEquals(42, TESTEE.deserializeTypedUsingDefaultImpl(jsonParser, context, null).getValue());

        json = "[42]";
        jsonParser = mapper.getFactory().createParser(json);
        assertEquals(42, TESTEE.deserializeTypedUsingDefaultImpl(jsonParser, context, null).getValue());
    }

    @org.junit.Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[\"StringType\",\"test\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        assertEquals("test", TESTEE.deserializeTypedFromArray(jsonParser, context).getValue());

        json = "[42]";
        jsonParser = mapper.getFactory().createParser(json);
        assertEquals(42, TESTEE.deserializeTypedFromArray(jsonParser, context).getValue());
    }

    @org.junit.Test
    public void testDeserializeTypedFromScalar() throws Exception {
        String json = "42";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        assertNull(TESTEE.deserializeTypedFromScalar(jsonParser, context));

        json = "\"test\"";
        jsonParser = mapper.getFactory().createParser(json);
        assertEquals("test", TESTEE.deserializeTypedFromScalar(jsonParser, context).getValue());
    }

    @org.junit.Test
    public void testDeserializeTypedFromAny() throws Exception {
        String json = "[\"StringType\",\"test\"]";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        assertEquals("test", TESTEE.deserializeTypedFromAny(jsonParser, context).getValue());

        json = "[42]";
        jsonParser = mapper.getFactory().createParser(json);
        assertEquals(42, TESTEE.deserializeTypedFromAny(jsonParser, context).getValue());

        json = "42";
        jsonParser = mapper.getFactory().createParser(json);
        assertNull(TESTEE.deserializeTypedFromAny(jsonParser, context));
    }
}