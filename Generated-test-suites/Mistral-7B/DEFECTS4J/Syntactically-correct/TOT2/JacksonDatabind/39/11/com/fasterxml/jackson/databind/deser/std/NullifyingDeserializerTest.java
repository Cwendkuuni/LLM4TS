package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        JsonDeserializer underTest = new NullifyingDeserializer();

        Object result = underTest.deserialize(parser, new DeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonDeserializer<Object> underTest = new NullifyingDeserializer();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(Null.class);

        Object result = underTest.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
        assertNull(result);
    }
}