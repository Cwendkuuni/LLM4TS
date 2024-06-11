package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.SettableAnyProperty;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

private static class TestPojo {
private Map<String, Object> anyProperties;

// getter and setter for testing
public Map<String, Object> getAnyProperties() { return anyProperties; }
public void setAnyProperties(Map<String, Object> properties) { this.anyProperties = properties; }
}

@Test
public void testSettableAnyPropertyDeserializeAndSet() throws Exception {
// prepare test data
ObjectMapper mapper = new ObjectMapper();
JsonParser parser = mapper.getFactory().createParser("{\"prop1\":1,\"prop2\":[3,4],\"prop3\":null}");
TestPojo pojo = new TestPojo();
SettableAnyProperty anyProperty = mapper.findSettableProperty("TestPojo", "anyProperties", JsonType.MAP_VALUE, null, null, null, null);

// deserialize and set
anyProperty.deserializeAndSet(parser, new TestDeserializationContext(), pojo, "anyProperties");

// check result
Map<String, Object> actualProperties = pojo.getAnyProperties();
assertEquals(3, actualProperties.size());
assertEquals(1, actualProperties.get("prop1"));
assertEquals(Arrays.asList(3, 4), actualProperties.get("prop2"));
assertNull(actualProperties.get("prop3"));
}

@Test(expected = IOException.class)
public void testSettableAnyPropertyDeserializeAndSetNullKey() throws Exception {
// prepare test data
ObjectMapper mapper = new ObjectMapper();
JsonParser parser = mapper.getFactory().createParser("{\"nullKey\":null}");
TestPojo pojo = new TestPojo();
SettableAnyProperty anyProperty = mapper.findSettableProperty("TestPojo", "anyProperties", JsonType.MAP_VALUE, null, null, null, null);

// deserialize and set
anyProperty.deserializeAndSet(parser, new TestDeserializationContext(), pojo, "nullKey");
fail("Should throw an IOException for null keys");
}

@Test(expected = IOException.class)
public void testSettableAnyPropertyDeserializeAndSetUnknownType() throws Exception {
// prepare test data
ObjectMapper mapper = new ObjectMapper();
JsonParser parser = mapper.getFactory().createParser("{\"prop1\":1,\"prop2\":[3,4],\"prop3\":true}");
TestPojo pojo = new TestPojo();
SettableAnyProperty anyProperty = mapper.findSettableProperty("TestPojo", "anyProperties", JsonType.BASE_TYPE, null, null, null, null);

// deserialize and set
try {
anyProperty.deserializeAndSet(parser, new TestDeserializationContext(), pojo, "anyProperties");
fail("Should throw an IOException for unknown types");
} catch (JsonMappingException e) {
// expected exception
assertTrue(e.getCauses().size() > 0);
JsonMappingException cause = (JsonMappingException) e.getCauses().get(0);
assertEquals("Unknown JSON type: BOOLEAN", cause.getMessage());
}
}

@Test
public void testSettableAnyPropertyDeserializeAndSetEmptyObject() throws Exception {
// prepare test data
ObjectMapper mapper = new ObjectMapper();
JsonParser parser = mapper.getFactory().createParser("{}}");
TestPojo pojo = new TestPojo();
SettableAnyProperty anyProperty = mapper.findSettableProperty("TestPojo", "anyProperties", JsonType.MAP_VALUE, null, null, null, null);

// deserialize and set
anyProperty.deserializeAndSet(parser, new TestDeserializationContext(), pojo, "anyProperties");

// check result
assertEquals(0, pojo.getAnyProperties().size());
}
}