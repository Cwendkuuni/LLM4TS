package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {

private ObjectMapper objectMapper;
private InnerClassProperty innerClassProperty;

@Before
public void setup() throws Exception {
objectMapper = new ObjectMapper();
ObjectNode rootNode = objectMapper.createObjectNode();
AnnotatedClass ac = objectMapper.getTypeFactory().constructType(InnerClass.class);
AnnotatedConstructor acConstructor = ac.getConstructors()[0];
PropertyName propertyName = PropertyName.construct("innerClassProperty");
innerClassProperty = new InnerClassProperty(new SettableBeanProperty(propertyName, acConstructor), null);
}

@Test
public void testInnerClassProperty() throws Exception {
// Creating an instance of the inner class using the constructor
ObjectNode constructorInput = objectMapper.createObjectNode();
constructorInput.put("name", "John Doe");
ObjectNode constructorOutput = objectMapper.readTree(objectMapper.writerWithValueTypeSerializer(true).writeValueAsString(constructorInput));
Object innerClassInstance = innerClassProperty.deserializeAndSet(objectMapper.getFactory().createParser(constructorOutput.traverse()), objectMapper.getDeserializationContext(), new Object());

// Testing the property value
assertEquals("John Doe", innerClassInstance.getClass().getDeclaredField("name").get(innerClassInstance));

// Testing setting the property value
ObjectNode setInput = objectMapper.createObjectNode();
setInput.put("name", "Jane Doe");
ObjectNode setOutput = objectMapper.readTree(objectMapper.writerWithValueTypeSerializer(true).writeValueAsString(setInput));
innerClassProperty.set(innerClassInstance, setInput.get("name"));
assertEquals("Jane Doe", innerClassInstance.getClass().getDeclaredField("name").get(innerClassInstance));
}

@Test(expected = IllegalArgumentException.class)
public void testMissingConstructor() throws Exception {
AnnotatedClass ac = objectMapper.getTypeFactory().constructType(MissingConstructorInnerClass.class);
PropertyName propertyName = PropertyName.construct("innerClassProperty");
innerClassProperty = new InnerClassProperty(new SettableBeanProperty(propertyName, null), null);
innerClassProperty.deserializeAndSet(objectMapper.getFactory().createParser(objectMapper.writeValueAsString(new ObjectNode())), objectMapper.getDeserializationContext(), new Object());
}

@Test(expected = IllegalArgumentException.class)
public void testBrokenDeSerialization() throws Exception {
AnnotatedClass ac = objectMapper.getTypeFactory().constructType(BrokenDeSerializationInnerClass.class);
PropertyName propertyName = PropertyName.construct("innerClassProperty");
innerClassProperty = new InnerClassProperty(new SettableBeanProperty(propertyName, ac.getConstructors()[0]), null);
JsonParser jp = objectMapper.getFactory().createParser(objectMapper.writeValueAsString(new ObjectNode()));
innerClassProperty.deserializeAndSet(jp, objectMapper.getDeserializationContext(), new Object());
}

@Test
public void testJsonDeserializer() throws Exception {
AnnotatedClass ac = objectMapper.getTypeFactory().constructType(JsonDeserializerInnerClass.class);
PropertyName propertyName = PropertyName.construct("innerClassProperty");
JsonDeserializer<JsonNode> jsonDeserializer = new JsonDeserializer<JsonNode>() {
@Override
public JsonNode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
return jp.getValueAsTree();
}
};
innerClassProperty = new InnerClassProperty(new SettableBeanProperty(propertyName, ac.getConstructors()[0]), jsonDeserializer);
ObjectNode input = objectMapper.createObjectNode();
input.put("name", "Test");
ObjectNode output = objectMapper.readTree(objectMapper.writerWithValueTypeSerializer(true).writeValueAsString(input));
innerClassProperty.set(new Object(), output);
assertEquals(input, innerClassProperty.get(new Object()));
}

@Test
public void testWithValueDeserializer() throws Exception {
AnnotatedClass ac = objectMapper.getTypeFactory().constructType(WithValueDeserializerInnerClass.class);
PropertyName propertyName = PropertyName.construct("innerClassProperty");
innerClassProperty = new InnerClassProperty(new SettableBeanProperty(propertyName, ac.getConstructors()[0]), null);
innerClassProperty = new InnerClassProperty(innerClassProperty, new JsonDeserializer<String>() {
@Override
public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
return jp.getValueAsString();
}
});
ObjectNode input = objectMapper.createObjectNode();
input.put("name", "Test");
ObjectNode output = objectMapper.readTree(objectMapper.writerWithValueTypeSerializer(true).writeValueAsString(input));
innerClassProperty.set(new Object(), output);
assertEquals("Test", innerClassProperty.get(new Object()).getClass().getDeclaredField("name").get(innerClassProperty.get(new Object())));
}

@Test
public void testToStringSerialization() throws Exception {
AnnotatedClass ac = objectMapper.getTypeFactory().constructType(ToStringSerializationInnerClass.class);
PropertyName propertyName = PropertyName.construct("innerClassProperty");
innerClassProperty = new InnerClassProperty(new SettableBeanProperty(propertyName, ac.getConstructors()[0]), null);
ObjectNode input = objectMapper.createObjectNode();
input.put("name", "Test");
innerClassProperty.set(new Object(), input);
StringWriter sw = new StringWriter();
JsonGenerator jg = objectMapper.getFactory().createGenerator(sw);
objectMapper.writeValue(jg, innerClassProperty.get(new Object()));
assertEquals("InnerClassProperty(innerClassProperty=JsonNode(\"name\":\"Test\"))", sw.toString());
}

@Test
public void testNameChange() throws Exception {
AnnotatedClass ac = objectMapper.getTypeFactory().constructType(NameChangeInnerClass.class);
PropertyName oldPropertyName = PropertyName.construct("oldPropertyName");
PropertyName newPropertyName = PropertyName.construct("newPropertyName");
innerClassProperty = new InnerClassProperty(new SettableBeanProperty(oldPropertyName, ac.getConstructors()[0]), null);
innerClassProperty = new InnerClassProperty(innerClassProperty, newPropertyName);
ObjectNode input = objectMapper.createObjectNode();
input.put("oldPropertyName", "Test");
innerClassProperty.set(new Object(), input);
assertEquals("Test", innerClassProperty.get(new Object()).getClass().getDeclaredField("newPropertyName").get(innerClassProperty.get(new Object())));
}
}