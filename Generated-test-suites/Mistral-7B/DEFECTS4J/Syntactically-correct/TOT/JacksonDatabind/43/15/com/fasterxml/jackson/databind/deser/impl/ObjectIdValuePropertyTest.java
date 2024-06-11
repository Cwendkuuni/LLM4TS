package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.JsonSubTypes;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws Exception {
 ObjectIdReader objectIdReader = new SimpleObjectIdReader(JsonTestUtils.getSimpleClassInfo(TestPojo.class, "id"));
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ_ID_PROPERTY_METADATA);
 this.objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 TestPojo testPojo = new TestPojo();
 ObjectNode node = ObjectMapperTestUtils.createObjectNode("{\"id\":1}");
 objectMapper.readValue(node, TestPojo.class); // deserialize to populate _objectIdReader.idProperty

 ObjectIdReader objectIdReaderSpy = ObjectMapperTestUtils.mockObjectIdReader(1, TestPojo.class, "id");
 ObjectIdValueProperty objectIdValuePropertySpy = new ObjectIdValueProperty(objectIdReaderSpy, PropertyMetadata.STD_OBJ_ID_PROPERTY_METADATA);
 ObjectIdValueProperty originalObjectIdValueProperty = this.objectMapper.findSpecialValueDeserializer(TestPojo.class, "id");
 ObjectIdValueProperty.DeserializationContext context = ObjectMapperTestUtils.mockDeserializationContext();

 Object result = originalObjectIdValueProperty.deserializeAndSet(ObjectMapperTestUtils.createJsonParser(node), context, testPojo);
 assertEquals(1, testPojo.getId()); // verify that id was set

 // test spy to verify that deserializeAndSet calls deserializeSetAndReturn
 ObjectIdValuePropertyTestUtils.verifyDeserializeSetAndReturnCalls(objectIdValuePropertySpy, 1, testPojo);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNullSet() throws Exception {
 TestPojo testPojo = new TestPojo();
 this.objectMapper.findSpecialValueDeserializer(TestPojo.class, "id").set(testPojo, null);
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 @Test
 public void testSetAndReturn() throws Exception {
 TestPojo testPojo = new TestPojo();
 int initialId = testPojo.getId();
 ObjectIdReader objectIdReaderSpy = ObjectMapperTestUtils.mockObjectIdReader(2, TestPojo.class, "id");
 ObjectIdValueProperty objectIdValuePropertySpy = new ObjectIdValueProperty(objectIdReaderSpy, PropertyMetadata.STD_OBJ_ID_PROPERTY_METADATA);
 ObjectIdValueProperty originalObjectIdValueProperty = this.objectMapper.findSpecialValueDeserializer(TestPojo.class, "id");
 ObjectIdValueProperty.DeserializationContext context = ObjectMapperTestUtils.mockDeserializationContext();

 Object result = originalObjectIdValueProperty.setAndReturn(testPojo, 2);
 assertEquals(2, testPojo.getId()); // verify that id was set
 assertEquals(testPojo, result); // verify that TestPojo instance was returned

 // test spy to verify that setAndReturn calls setAndReturn
 ObjectIdValuePropertyTestUtils.verifySetAndReturnCalls(objectIdValuePropertySpy, testPojo, 2);
 }

 @Test
 public void testInvalidId() throws Exception {
 TestPojo testPojo = new TestPojo();
 ObjectIdReader objectIdReaderSpy = ObjectMapperTestUtils.mockObjectIdReader("invalidId", TestPojo.class, "id");
 ObjectIdValueProperty objectIdValuePropertySpy = new ObjectIdValueProperty(objectIdReaderSpy, PropertyMetadata.STD_OBJ_ID_PROPERTY_METADATA);
 ObjectIdValueProperty originalObjectIdValueProperty = this.objectMapper.findSpecialValueDeserializer(TestPojo.class, "id");
 ObjectIdValueProperty.DeserializationContext context = ObjectMapperTestUtils.mockDeserializationContext();

 try {
 originalObjectIdValueProperty.deserializeAndSet(ObjectMapperTestUtils.createJsonParser("{\"id\":\"invalidId\"}"), context, testPojo);
 fail("Should throw an IOException for invalid id");
 } catch (IOException e) {
 // expected
 }

 // test spy to verify that deserializeSetAndReturn does not call bindItem for invalid id
 ObjectIdValuePropertyTestUtils.verifyBindItemCalls(objectIdValuePropertySpy, 0);
 }

 @Test
 public void testMissingId() throws Exception {
 TestPojo testPojo = new TestPojo();
 ObjectIdReader objectIdReaderSpy = ObjectMapperTestUtils.mockObjectIdReader(null, TestPojo.class, "id");
 ObjectIdValueProperty objectIdValuePropertySpy = new ObjectIdValueProperty(objectIdReaderSpy, PropertyMetadata.STD_OBJ_ID_PROPERTY_METADATA);
 ObjectIdValueProperty originalObjectIdValueProperty = this.objectMapper.findSpecialValueDeserializer(TestPojo.class, "id");
 ObjectIdValueProperty.DeserializationContext context = ObjectMapperTestUtils.mockDeserializationContext();

 Object result = originalObjectIdValueProperty.deserializeAndSet(ObjectMapperTestUtils.createJsonParser("{}"), context, testPojo);
 assertNull(testPojo.getId()); // verify that id was not set
 assertEquals(testPojo, result); // verify that TestPojo instance was returned

 // test spy to verify that deserializeSetAndReturn does not call bindItem for missing id
 ObjectIdValuePropertyTestUtils.verifyBindItemCalls(objectIdValuePropertySpy, 0);
 }
}

class TestPojo {
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 private int id;

 // getters and setters
}

class SimpleObjectIdReader extends ObjectIdReader {
 // implementation
}

class TestPojo_AnnotatedTest {
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 @JsonSubTypes({ @JsonSubTypes.Type(value = TestPojoWithAnnotations.class, name = "annotated") })
 private int id;

 // getters and setters
}

class TestPojoWithAnnotations {
 @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
 @JsonIdentityInfo.UseGeneratedProperties(value = true)
 private int id;

 // getters and setters
}

class ObjectIdValuePropertyTestUtils {
 // test utilities for verifying call counts of deserializeSetAndReturn and setAndReturn
}