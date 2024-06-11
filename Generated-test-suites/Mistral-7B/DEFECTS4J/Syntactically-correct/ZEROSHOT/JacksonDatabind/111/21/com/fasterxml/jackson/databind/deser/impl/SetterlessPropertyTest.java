package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testSetterlessProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SetterlessProperty property = mapper.findProperty("testProperty", SetterlessProperty.class);

        // Test getter
        assertNotNull(property.getGetter());

        // Test setting value using deserializer
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        Object testObject = new Object();
        property.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject);
        assertNotNull(testObject);
        assertEquals("[1, 2, 3]", testObject.toString());

        // Test setting null value
        parser = mapper.getFactory().createParser("null");
        property.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject);
        assertNull(testObject);

        // Test deserializing with type deserializer
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializers().findValueDeserializer(String.class, JsonString.class);
        JsonParser parserWithType = mapper.getFactory().createParser("[\"hello\", \"world\"]");
        parserWithType.nextToken(); // consume [
        parserWithType.nextToken(); // consume "hello"
        parserWithType.nextToken(); // consume ,
        parserWithType.nextToken(); // consume "world"
        parserWithType.nextToken(); // consume ]

        testObject = new Object();
        property.deserializeAndSet(parserWithType, mapper.getDeserializationContext(), testObject, typeDeserializer);
        assertNotNull(testObject);
        assertEquals("hello, world", testObject.toString());
    }
}