package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreatorPropertyTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testCreatorProperty() throws Exception {
 // Test constructor
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.construct("java.lang.String"),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(
 new AnnotatedElement("testClass", "testConstructor"),
 "testParam",
 new JackssonCreatorAnnotation(),
 new int[0],
 ClassUtil.classOf(String.class)
 ),
 0,
 null,
 null
 );
 assertEquals("testProperty", property.getName().asString());
 assertEquals(JavaType.construct("java.lang.String"), property.getType());
 assertNull(property._annotated);
 assertNull(property._injectableValueId);
 assertNull(property._fallbackSetter);
 assertNull(property._creatorIndex);
 assertFalse(property.isIgnorable());

 // Test copy constructor
 CreatorProperty copy = new CreatorProperty(property, "newName");
 assertEquals("newName", copy.getName().asString());
 assertEquals(property.getType(), copy.getType());
 assertEquals(property._annotated, copy._annotated);
 assertEquals(property._injectableValueId, copy._injectableValueId);
 assertNull(copy._fallbackSetter);
 assertEquals(property._creatorIndex, copy._creatorIndex);
 assertFalse(copy.isIgnorable());

 // Test setter
 property.setFallbackSetter(new CreatorProperty(
 PropertyName.construct("testProperty2"),
 JavaType.construct("java.lang.Integer"),
 PropertyName.construct("wrapperName2"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(
 new AnnotatedElement("testClass2", "testConstructor2"),
 "testParam2",
 new JackssonCreatorAnnotation(),
 new int[0],
 ClassUtil.classOf(Integer.class)
 ),
 0,
 null,
 null
 ));
 assertEquals(new CreatorProperty(
 PropertyName.construct("testProperty2"),
 JavaType.construct("java.lang.Integer"),
 PropertyName.construct("wrapperName2"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(
 new AnnotatedElement("testClass2", "testConstructor2"),
 "testParam2",
 new JackssonCreatorAnnotation(),
 new int[0],
 ClassUtil.classOf(Integer.class)
 ),
 0,
 null,
 null
 ), property._fallbackSetter);

 // Test markAsIgnorable
 property.markAsIgnorable();
 assertTrue(property.isIgnorable());

 // Test injection
 ObjectMapper objectMapper = new ObjectMapper();
 JsonDeserializer<String> deserializer = new FromStringDeserializer();
 property = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.construct("java.lang.String"),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(
 new AnnotatedElement("TestClass", "testConstructor"),
 "testParam",
 new JsonDeserialize(using = deserializer),
 new int[0],
 ClassUtil.classOf(String.class)
 ),
 0,
 "testId",
 null
 );
 Object testInstance = objectMapper.readValue(
 "[{\"id\":\"testId\", \"value\":\"testValue\"}]", TestClass.class);
 Object injectedValue = property.findInjectableValue(objectMapper.getContext(), testInstance);
 assertEquals("testValue", injectedValue);
 property.inject(objectMapper.getContext(), testInstance);
 assertEquals("testValue", testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test(expected = InvalidDefinitionException.class)
 public void testMissingSetter() throws Exception {
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("testProperty"),
 JavaType.construct("java.lang.String"),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(
 new AnnotatedElement("TestClass", "testConstructor"),
 "testParam",
 null,
 new int[0],
 ClassUtil.classOf(String.class)
 ),
 0,
 null,
 null
 );
 property.setFallbackSetter(null);
 property.deserializeAndSet(null, objectMapper.getContext(), null);
 }
}

class TestClass {
 @JsonDeserialize
 private String testProperty;

 public String getTestProperty() {
 return testProperty;
 }

 public void setTestProperty(String testProperty) {
 this.testProperty = testProperty;
 }
}

class JackssonCreatorAnnotation implements Annotation {
}