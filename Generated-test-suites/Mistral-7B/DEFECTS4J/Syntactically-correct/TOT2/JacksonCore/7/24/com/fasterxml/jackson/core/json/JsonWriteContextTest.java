package com.example.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = mock(JsonWriteContext.class);
        when(context.writeValue()).thenReturn(JsonWriteContext.STATUS_OK_AS_IS);

        JsonWriteContext expectedContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        int actualResult = expectedContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, actualResult);
    }

    // Test case 1: Test writeValue() in object context
    @Test
    public void testWriteValueObjectContext() throws JsonProcessingException {
        JsonWriteContext context = mock(JsonWriteContext.class);
        when(context._type).thenReturn(JsonWriteContext.TYPE_OBJECT);
        when(context.writeValue()).thenReturn(JsonWriteContext.STATUS_OK_AFTER_COLON);

        JsonWriteContext expectedContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        int actualResult = expectedContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, actualResult);
    }

    // Test case 2: Test writeValue() in array context
    @Test
    public void testWriteValueArrayContext() throws JsonProcessingException {
        JsonWriteContext context = mock(JsonWriteContext.class);
        when(context._type).thenReturn(JsonWriteContext.TYPE_ARRAY);
        when(context.getCurrentIndex()).thenReturn(1);

        JsonWriteContext expectedContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        int actualResult = expectedContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, actualResult);
    }

    // Test case 3: Test writeValue() in root context
    @Test
    public void testWriteValueRootContext() throws JsonProcessingException {
        JsonWriteContext context = mock(JsonWriteContext.class);
        when(context._type).thenReturn(JsonWriteContext.TYPE_ROOT);

        JsonWriteContext expectedContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        int actualResult = expectedContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, actualResult);
    }

    // Test case 4: Test writeValue() with null current value
    @Test(expected = JsonProcessingException.class)
    public void testWriteValueNullCurrentValue() throws JsonProcessingException {
        JsonWriteContext context = mock(JsonWriteContext.class);
        when(context.getCurrentValue()).thenReturn(null);

        JsonWriteContext expectedContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        expectedContext.setCurrentValue(null);
        int actualResult = expectedContext.writeValue();

        // Throws JsonProcessingException
    }

    // Test case 5: Test writeValue() with non-null current value
    @Test
    public void testWriteValueNonnullCurrentValue() throws JsonProcessingException {
        JsonWriteContext context = mock(JsonWriteContext.class);
        Object currentValue = new Object();
        when(context.getCurrentValue()).thenReturn(currentValue);
        when(context.writeValue()).thenReturn(JsonWriteContext.STATUS_OK_AS_IS);

        JsonWriteContext expectedContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        expectedContext.setCurrentValue(currentValue);
        int actualResult = expectedContext.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, actualResult);
    }
}