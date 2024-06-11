package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext context;

 @Before
 public void setUp() {
 context = JsonWriteContext.createRootContext();
 }

 @After
 public void tearDown() {
 context = null;
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertEquals("[]", rootContext.toString());
 }

 @Test
 public void testCreateChildArrayContext() {
 context = JsonWriteContext.createRootContext();
 JsonWriteContext arrayContext = context.createChildArrayContext();
 assertEquals("[]", arrayContext.toString());
 }

 @Test
 public void testCreateChildObjectContext() {
 context = JsonWriteContext.createRootContext();
 JsonWriteContext objectContext = context.createChildObjectContext();
 assertEquals("{}", objectContext.toString());
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 context = JsonWriteContext.createRootContext();
 context.writeFieldName("testField");
 assertEquals("{\"testField\":\"\"}", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameDuplicate() throws JsonProcessingException {
 context = JsonWriteContext.createRootContext();
 DupDetector dupDetector = new DupDetector();
 context.withDupDetector(dupDetector);
 context.writeFieldName("testField");
 context.writeFieldName("testField");
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 context = JsonWriteContext.createRootContext();
 context.writeValue();
 assertEquals("[null]", context.toString());
 }

 @Test
 public void testWriteValueArray() throws JsonProcessingException {
 context = JsonWriteContext.createRootContext();
 context = context.createChildArrayContext();
 context.writeValue();
 context.writeValue("test1");
 context.writeValue(1);
 context.writeValue(3.14);
 assertEquals("[null,\"test1\",1,3.14]", context.toString());
 }

 @Test
 public void testWriteValueObject() throws JsonProcessingException {
 context = JsonWriteContext.createRootContext();
 context = context.createChildObjectContext();
 context.writeFieldName("testField1");
 context.writeValue("test1");
 context.writeFieldName("testField2");
 context.writeValue(1);
 context.writeFieldName("testField3");
 context.writeValue(3.14);
 assertEquals("{\"testField1\":\"test1\",\"testField2\":1,\"testField3\":3.14}", context.toString());
 }
}