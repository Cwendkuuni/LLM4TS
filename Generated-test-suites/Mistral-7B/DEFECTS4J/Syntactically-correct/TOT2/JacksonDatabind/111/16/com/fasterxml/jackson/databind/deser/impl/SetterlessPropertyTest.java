package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testGetterlessProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");

        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object testInstance = new Object();

        property.deserializeAndSet(parser, null, testInstance);
        assertNotNull(testInstance);

        parser.nextToken(); // consume first "["

        parser.nextToken(); // consume Array type descriptor

        property.deserializeAndSet(parser, null, testInstance);
        assertNotNull(testInstance);

        parser.nextToken(); // consume "end array marker"

        parser.nextToken(); // consume "null"

        property.deserializeAndSet(parser, null, testInstance);
        assertNull(testInstance);

        parser.nextToken(); // consume "null"

        // Test exception handling when getter method throws an Exception
        parser.nextToken(); // consume "NaN"
        try {
            property.deserializeAndSet(parser, null, testInstance);
        } catch (IOException e) {
            assertEquals("Unexpected token (NAN) @ [Source: (String)null, line: -1, column: -1]", e.getMessage());
        }

        // Test exception handling when getter method throws a RuntimeException
        parser.nextToken(); // consume "String"
        try {
            property.deserializeAndSet(parser, null, testInstance);
        } catch (IOException e) {
            assertTrue(e.getCause() instanceof RuntimeException);
        }
    }
}