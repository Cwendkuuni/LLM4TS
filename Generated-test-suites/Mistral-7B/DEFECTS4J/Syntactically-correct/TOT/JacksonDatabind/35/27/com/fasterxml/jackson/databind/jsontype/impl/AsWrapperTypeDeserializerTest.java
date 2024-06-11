package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class AsWrapperTypeDeserializerTest extends TestBase {

 private static final JavaType WRAPPER_TYPE = TypeFactory.defaultInstance().constructType(AsWrapperTypeDeserializer.class);
 private static final TypeIdResolver idResolver = new StandardTypeIdResolver();
 private static final ObjectMapper mapper = new ObjectMapper();
 private static final AsWrapperTypeDeserializer deserializer;

 @BeforeClass
 public static void setUpClass() throws Exception {
 Object typePropertyName = "typeProperty";
 deserializer = new AsWrapperTypeDeserializer(
 ClassUtil.constructType(Object.class), idResolver, typePropertyName, true, Object.class);
 }

 @Test
 public void testDeserializeFromObjectWithValidTypeId() throws Exception {
 Object subTypeInstance = new MySubType("subTypeValue");
 String jsonString = mapper.writerWithTypeValidator(false).writeValueAsString(subTypeInstance);
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 Object result = deserializer.deserializeTypedFromObject(jp, context());
 assertEquals(subTypeInstance, result);
 }

 @Test
 public void testDeserializeFromObjectWithInvalidTypeId() throws Exception {
 String jsonString = "{\"typeId\":\"InvalidTypeId\",\"value\":{\"name\":\"InvalidValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromObject(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromObjectWithEmptyTypeId() throws Exception {
 String jsonString = "{\"\":{\"value\":{\"name\":\"EmptyValue\"}}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromObject(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromObjectWithNullValue() throws Exception {
 String jsonString = "{\"typeId\":\"MyType\",\"value\":null}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 Object result = deserializer.deserializeTypedFromObject(jp, context());
 assertNull(result);
 }

 @Test
 public void testDeserializeFromObjectWithNullTypeId() throws Exception {
 String jsonString = "{\"value\":{\"name\":\"NullTypeValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromObject(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromObjectWithMissingTypeId() throws Exception {
 String jsonString = "{\"value\":{\"name\":\"MissingTypeValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromObject(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromArrayWithValidTypeId() throws Exception {
 Object[] subTypeInstances = new Object[]{new MySubType1("subTypeValue1"), new MySubType2("subTypeValue2")};
 Object[] jsonObjects = new Object[]{mapper.writerWithTypeValidator(false).writeValueAsString(subTypeInstances[0]), mapper.writerWithTypeValidator(false).writeValueAsString(subTypeInstances[1])};
 Object[] results = new Object[2];
 for (int i = 0; i < jsonObjects.length; i++) {
 JsonParser jp = mapper.getFactory().createParser(jsonObjects[i]);
 results[i] = deserializer.deserializeTypedFromArray(jp, context());
 }
 assertArrayEquals(subTypeInstances, results);
 }

 @Test
 public void testDeserializeFromArrayWithInvalidTypeId() throws Exception {
 Object[] jsonObjects = new Object[]{"{\"typeId\":\"InvalidTypeId\",\"value\":{\"name\":\"InvalidValue\"}}", "{\"typeId\":\"MyType\",\"value\":{\"name\":\"ValidValue\"}}"};
 Object[] results = new Object[2];
 for (int i = 0; i < jsonObjects.length; i++) {
 JsonParser jp = mapper.getFactory().createParser(jsonObjects[i]);
 try {
 results[i] = deserializer.deserializeTypedFromArray(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }
 }

 @Test
 public void testDeserializeFromArrayWithEmptyTypeId() throws Exception {
 Object[] jsonObjects = new Object[]{"{\"\":{\"value\":{\"name\":\"EmptyValue\"}}}", "{\"typeId\":\"MyType\",\"value\":{\"name\":\"ValidValue\"}}"};
 Object[] results = new Object[2];
 for (int i = 0; i < jsonObjects.length; i++) {
 JsonParser jp = mapper.getFactory().createParser(jsonObjects[i]);
 try {
 results[i] = deserializer.deserializeTypedFromArray(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }
 }

 @Test
 public void testDeserializeFromArrayWithNullValue() throws Exception {
 Object[] jsonObjects = new Object[]{"{\"typeId\":\"MyType\",\"value\":null}", "{\"typeId\":\"MyType\",\"value\":{\"name\":\"ValidValue\"}}"};
 Object[] results = new Object[2];
 for (int i = 0; i < jsonObjects.length; i++) {
 JsonParser jp = mapper.getFactory().createParser(jsonObjects[i]);
 Object result = deserializer.deserializeTypedFromArray(jp, context());
 if (i == 0) {
 assertNull(result);
 } else {
 assertEquals(results[i - 1], result);
 }
 }
 }

 @Test
 public void testDeserializeFromArrayWithMissingTypeId() throws Exception {
 Object[] jsonObjects = new Object[]{"{\"value\":{\"name\":\"MissingTypeValue\"}}", "{\"typeId\":\"MyType\",\"value\":{\"name\":\"ValidValue\"}}"};
 Object[] results = new Object[2];
 for (int i = 0; i < jsonObjects.length; i++) {
 JsonParser jp = mapper.getFactory().createParser(jsonObjects[i]);
 try {
 results[i] = deserializer.deserializeTypedFromArray(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }
 }

 @Test
 public void testDeserializeFromScalarWithValidTypeId() throws Exception {
 String jsonString = "\"MyType\":{\"value\":{\"name\":\"ValidValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 Object result = deserializer.deserializeTypedFromScalar(jp, context());
 assertEquals("MyType", result.getClass().getName());
 }

 @Test
 public void testDeserializeFromScalarWithInvalidTypeId() throws Exception {
 String jsonString = "\"InvalidTypeId\":{\"value\":{\"name\":\"ValidValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromScalar(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromScalarWithEmptyTypeId() throws Exception {
 String jsonString = "\"\":{\"value\":{\"name\":\"ValidValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromScalar(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromScalarWithNullValue() throws Exception {
 String jsonString = "\"MyType\":null";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 Object result = deserializer.deserializeTypedFromScalar(jp, context());
 assertNull(result);
 }

 @Test
 public void testDeserializeFromScalarWithMissingTypeId() throws Exception {
 String jsonString = "{\"value\":{\"name\":\"ValidValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromScalar(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromAnyWithValidTypeId() throws Exception {
 Object jsonObject = mapper.writerWithTypeValidator(false).writeValueAsString(new MyType("typeValue"));
 JsonParser jp = mapper.getFactory().createParser(jsonObject);
 Object result = deserializer.deserializeTypedFromAny(jp, context());
 assertEquals("MyType", result.getClass().getName());
 }

 @Test
 public void testDeserializeFromAnyWithInvalidTypeId() throws Exception {
 String jsonString = "{\"typeId\":\"InvalidTypeId\",\"value\":{\"name\":\"ValidValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromAny(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromAnyWithEmptyTypeId() throws Exception {
 String jsonString = "{\"\":{\"value\":{\"name\":\"ValidValue\"}}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromAny(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeFromAnyWithNullValue() throws Exception {
 String jsonString = "\"MyType\":null";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 Object result = deserializer.deserializeTypedFromAny(jp, context());
 assertNull(result);
 }

 @Test
 public void testDeserializeFromAnyWithMissingTypeId() throws Exception {
 String jsonString = "{\"value\":{\"name\":\"ValidValue\"}}";
 JsonParser jp = mapper.getFactory().createParser(jsonString);
 try {
 Object result = deserializer.deserializeTypedFromAny(jp, context());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 private static DeserializationContext context() {
 return mapper.getDeserializationContext().withTypeResolver(new StandardTypeResolver());
 }
}

class MySubType {
 private final String name;

 public MySubType(String name) {
 this.name = name;
 }

 @Override
 public String toString() {
 return "MySubType{" +
 "name='" + name + '\'' +
 '}';
 }
}

class MySubType1 extends MySubType {
 MySubType1(String name) {
 super(name);
 }
}

class MySubType2 extends MySubType {
 MySubType2(String name) {
 super(name);
 }
}

class MyType {
 private final String typeValue;

 public MyType(String typeValue) {
 this.typeValue = typeValue;
 }

 @Override
 public String toString() {
 return "MyType{" +
 "typeValue='" + typeValue + '\'' +
 '}';
 }
}