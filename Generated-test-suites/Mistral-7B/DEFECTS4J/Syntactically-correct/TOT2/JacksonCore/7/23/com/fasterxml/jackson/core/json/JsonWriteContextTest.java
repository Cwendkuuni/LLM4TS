package com.example;

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import org.junit.Test;

public class JsonWriteContextTest {

  @Test
  public void testWriteValue() throws JsonProcessingException {
    JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    Object value = new Object();
    context.setCurrentValue(value);

    int result = context.writeValue();
    assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
  }

  // Tests for writeFieldName method

  @Test(expected = JsonProcessingException.class)
  public void testWriteFieldNameNullName() throws JsonProcessingException {
    JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    context.writeFieldName(null);
  }

  @Test(expected = JsonProcessingException.class)
  public void testWriteFieldNameEmptyName() throws JsonProcessingException {
    JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    context.writeFieldName("");
  }

  @Test
  public void testWriteFieldNameLongName() throws JsonProcessingException {
    JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    String longName = "longFieldName";
    context.writeFieldName(longName);
    assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
  }

  @Test(expected = JsonProcessingException.class)
  public void testWriteFieldNameInvalidName() throws JsonProcessingException {
    JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    String invalidName = "field\"name";
    context.writeFieldName(invalidName);
  }

  @Test(expected = JsonProcessingException.class)
  public void testWriteFieldNameDuplicateName() throws JsonProcessingException {
    DupDetector dupDetector = new DupDetector();
    JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
    String duplicateName = "duplicateFieldName";
    context.writeFieldName(duplicateName);
    context.writeFieldName(duplicateName);
  }
}