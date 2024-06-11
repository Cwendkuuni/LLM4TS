import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;
 private JsonValueSerializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializationOfSimpleValueMethod() throws JsonProcessingException {
 Object testObject = new TestClass();
 TestClassField testField = testObject.getTestField();

 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 SerializerProvider prov = mapper.getSerializerProvider();

 serializer = new JsonValueSerializer(testField.getAccessMethod(testObject), null);
 serializer = serializer.withResolved(testField, null, false);
 serializer.serialize(testObject, gen, prov);
 gen.flush();
 String json = gen.getOutputContent();
 assertEquals("{\"value\":\"testStringValue\"}", json);
 }

 @Test
 public void testSerializationOfNullValueMethod() throws JsonProcessingException {
 Object testObject = new TestClass();
 TestClassField testField = testObject.getTestField();

 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 SerializerProvider prov = mapper.getSerializerProvider();

 serializer = new JsonValueSerializer(testField.getAccessMethod(testObject), null);
 Object value = testField.getValue(testObject);
 if (value == null) {
 prov.defaultSerializeNull(gen);
 return;
 }
 serializer = serializer.withResolved(testField, null, false);
 serializer.serialize(testObject, gen, prov);
 gen.flush();
 String json = gen.getOutputContent();
 assertEquals("null", json);
 }

 @Test
 public void testSerializationOfComplexValueMethod() throws JsonProcessingException {
 Object testObject = new TestClass();
 TestComplexField testField = testObject.getTestComplexField();

 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 SerializerProvider prov = mapper.getSerializerProvider();

 serializer = new JsonValueSerializer(testField.getAccessMethod(testObject), null);
 serializer = serializer.withResolved(testField, null, false);
 serializer.serialize(testObject, gen, prov);
 gen.flush();
 String json = gen.getOutputContent();
 assertEquals("{\"complexValue\":{\"complexField\":\"testComplexValueString\"}}", json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationOfExceptionThrownMethod() throws JsonProcessingException {
 Object testObject = new TestClass();
 TestExceptionMethod testField = testObject.getTestExceptionMethod();

 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 SerializerProvider prov = mapper.getSerializerProvider();

 serializer = new JsonValueSerializer(testField.getAccessMethod(testObject), null);
 serializer = serializer.withResolved(testField, null, false);
 try {
 serializer.serialize(testObject, gen, prov);
 } catch (IOException ioe) {
 // expected, as this method throws an IOException
 }
 testField.throwException();
 serializer.serialize(testObject, gen, prov);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationOfNestedExceptionThrownMethod() throws JsonProcessingException {
 Object testObject = new TestClass();
 TestNestedExceptionMethod testField = testObject.getTestNestedExceptionMethod();

 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 SerializerProvider prov = mapper.getSerializerProvider();

 serializer = new JsonValueSerializer(testField.getAccessMethod(testObject), null);
 serializer = serializer.withResolved(testField, null, false);
 try {
 serializer.serialize(testObject, gen, prov);
 } catch (IOException ioe) {
 // expected, as this method throws an IOException
 }
 testField.throwNestedException();
 }
}

class TestClass {
 private TestClassField testField;
 private TestComplexField testComplexField;
 private TestExceptionMethod testExceptionMethod;
 private TestNestedExceptionMethod testNestedExceptionMethod;

 public TestClass() {
 this.testField = new TestClassField(this, "testStringValue");
 this.testComplexField = new TestComplexField(this, new TestComplex());
 this.testExceptionMethod = new TestExceptionMethod(this);
 this.testNestedExceptionMethod = new TestNestedExceptionMethod(this);
 }

 public TestClassField getTestField() {
 return testField;
 }

 public TestComplexField getTestComplexField() {
 return testComplexField;
 }

 public TestExceptionMethod getTestExceptionMethod() {
 return testExceptionMethod;
 }

 public TestNestedExceptionMethod getTestNestedExceptionMethod() {
 return testNestedExceptionMethod;
 }
}

class TestClassField {
 private JsonValueSerializer serializer;
 private AnnotatedMethod accessMethod;

 public TestClassField(Object obj, String value) {
 this.accessMethod = new AnnotatedMethod(TestClassSerializer.class, "getValue", TestClass.class);
 this.accessMethod.setValue(obj);
 this.accessMethod.setParameterTypes(new Class[]{Object.class});
 this.accessMethod.setName("getTestField");
 this.accessMethod.setAnnotation(new JsonValue());
 }

 public AnnotatedMethod getAccessMethod(Object obj) {
 return accessMethod;
 }

 public String getValue(Object obj) {
 return value;
 }
}

class TestComplexField {
 private JsonValueSerializer serializer;
 private AnnotatedMethod accessMethod;
 private TestComplex testComplex;

 public TestComplexField(Object obj, TestComplex testComplex) {
 this.accessMethod = new AnnotatedMethod(TestClassSerializer.class, "getValue", TestClass.class);
 this.accessMethod.setValue(obj);
 this.accessMethod.setParameterTypes(new Class[]{Object.class});
 this.accessMethod.setName("getTestComplexField");
 this.accessMethod.setAnnotation(new JsonValue());
 this.testComplex = testComplex;
 }

 public AnnotatedMethod getAccessMethod(Object obj) {
 return accessMethod;
 }

 public TestComplex getValue(Object obj) {
 return testComplex;
 }
}

class TestExceptionMethod {
 private JsonValueSerializer serializer;
 private AnnotatedMethod accessMethod;

 public TestExceptionMethod(Object obj) {
 this.accessMethod = new AnnotatedMethod(TestClassSerializer.class, "getValue", TestClass.class);
 this.accessMethod.setValue(obj);
 this.accessMethod.setParameterTypes(new Class[]{Object.class});
 this.accessMethod.setName("getTestExceptionMethod");
 this.accessMethod.setAnnotation(new JsonValue());
 }

 public AnnotatedMethod getAccessMethod(Object obj) {
 return accessMethod;
 }

 public void throwException() {
 throw new RuntimeException("");
 }
}

class TestNestedExceptionMethod {
 private JsonValueSerializer serializer;
 private AnnotatedMethod accessMethod;

 public TestNestedExceptionMethod(Object obj) {
 this.accessMethod = new AnnotatedMethod(TestClassSerializer.class, "getValue", TestClass.class);
 this.accessMethod.setValue(obj);
 this.accessMethod.setParameterTypes(new Class[]{Object.class});
 this.accessMethod.setName("getTestNestedExceptionMethod");
 this.accessMethod.setAnnotation(new JsonValue());
 }

 public AnnotatedMethod getAccessMethod(Object obj) {
 return accessMethod;
 }

 public void throwNestedException() {
 throw new NestedRuntimeException("");
 }
}

class NestedRuntimeException extends RuntimeException {
 public NestedRuntimeException(String message) {
 super(message);
 }
}