package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext rootContext;
    private JsonWriteContext arrayContext;
    private JsonWriteContext objectContext;
    private JsonWriteContext objectContextWithName;
    private JsonWriteContext objectContextWithValue;
    private DupDetector noDupDetector;
    private DupDetector withDupDetector;

    @Before
    public void setUp() {
        noDupDetector = new DupDetector();
        withDupDetector = new DupDetector();
        rootContext = JsonWriteContext.createRootContext(noDupDetector);
        arrayContext = rootContext.createChildArrayContext();
        objectContext = rootContext.createChildObjectContext();
        objectContextWithName = objectContext.reset(JsonWriteContext.TYPE_OBJECT).writeFieldName("name");
        objectContextWithValue = objectContextWithName.setCurrentValue("value");
    }

    @Test
    public void testWriteFieldName() {
        String name = "fieldName";
        int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_COMMA;

        JsonWriteContext jsonWriteContext = objectContext.reset(JsonWriteContext.TYPE_OBJECT);
        int status = jsonWriteContext.writeFieldName(name);

        assertEquals(expectedStatus, status);
        assertEquals(name, jsonWriteContext.getCurrentName());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDuplicateName() {
        String name = "duplicateFieldName";
        JsonWriteContext jsonWriteContext = objectContextWithName.reset(JsonWriteContext.TYPE_OBJECT);
        jsonWriteContext.writeFieldName(name);

        withDupDetector.addFieldName(name);
        JsonWriteContext jsonWriteContextWithDupDetector = jsonWriteContext.withDupDetector(withDupDetector);
        jsonWriteContextWithDupDetector.writeFieldName(name);
    }

    @Test
    public void testWriteValue_RootContext() {
        int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_SPACE;
        JsonWriteContext jsonWriteContext = rootContext;

        int status = jsonWriteContext.writeValue();

        assertEquals(expectedStatus, status);
    }

    @Test
    public void testWriteValue_ArrayContext() {
        int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_COMMA;
        JsonWriteContext jsonWriteContext = arrayContext;

        int status = jsonWriteContext.writeValue();

        assertEquals(expectedStatus, status);
    }

    @Test
    public void testWriteValue_ObjectContextWithName() {
        int expectedStatus = JsonWriteContext.STATUS_EXPECT_VALUE;
        JsonWriteContext jsonWriteContext = objectContextWithName;

        int status = jsonWriteContext.writeValue();

        assertEquals(expectedStatus, status);
    }

    @Test
    public void testWriteValue_ObjectContextWithValue() {
        int expectedStatus = JsonWriteContext.STATUS_OK_AFTER_COLON;
        JsonWriteContext jsonWriteContext = objectContextWithValue;

        int status = jsonWriteContext.writeValue();

        assertEquals(expectedStatus, status);
        assertNotNull(jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testToString() {
        String expectedRootContext = "/";
        assertEquals(expectedRootContext, rootContext.toString());

        String expectedArrayContext = "[0]";
        assertEquals(expectedArrayContext, arrayContext.toString());

        String expectedObjectContext = "{name}";
        assertEquals(expectedObjectContext, objectContextWithName.toString());

        String expectedObjectContextWithValue = "{name:value}";
        assertEquals(expectedObjectContextWithValue, objectContextWithValue.toString());
    }
}