package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

 private static final TypeIdResolver idResolver = new StandardTypeIdResolver();
 private static final JavaType baseType = Object.class;

 @Test
 public void testTypeDeserializerBaseConstruction() {
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
 assertEquals(baseType, typeDeserializerBase.baseType());
 assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
 assertNull(typeDeserializerBase.getPropertyName());
 assertNull(typeDeserializerBase.getDefaultImpl());
 assertNull(typeDeserializerBase.toString());
 }

 @Test
 public void testTypeDeserializerBaseForProperty() {
 final BeanProperty property = new BeanProperty(TestClass.class, "testProperty");
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "testProperty", false, null);
 final TypeDeserializerBase newTypeDeserializerBase = typeDeserializerBase.forProperty(property);
 assertEquals(baseType, newTypeDeserializerBase.baseType());
 assertEquals(idResolver, newTypeDeserializerBase.getTypeIdResolver());
 assertEquals("testProperty", newTypeDeserializerBase.getPropertyName());
 assertNull(newTypeDeserializerBase.getDefaultImpl());
 assertNull(newTypeDeserializerBase.toString());
 }

 @Test
 public void testGetTypeInclusion() {
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
 assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() {
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
 assertEquals(baseType.getRawClass().getName(), typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testGetPropertyName() {
 final BeanProperty property = new BeanProperty(TestClass.class, "testProperty");
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "testProperty", false, null);
 assertEquals("testProperty", typeDeserializerBase.getPropertyName());
 }

 @Test
 public void testFindDeserializer() throws IOException {
 final DeserializationContext ctxt = new TestDeserializationContext();
 final String typeId = "com.test.TestClass";
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
 @Override
 public JsonDeserializer<Object> forProperty(BeanProperty prop) {
 return new StubJsonDeserializer<Object>(prop.getType());
 }
 };
 final JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
 assertEquals(StubJsonDeserializer.class, deserializer.getClass());
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerNoDeserializer() throws IOException {
 final DeserializationContext ctxt = new TestDeserializationContext();
 final String typeId = "com.test.NonExistentClass";
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
 typeDeserializerBase._findDeserializer(ctxt, typeId);
 fail("Expected IOException but did not receive one");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws IOException {
 final DeserializationContext ctxt = new TestDeserializationContext();
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
 @Override
 public JavaType baseType() {
 return JavaType.constructType(String.class);
 }
 };
 final JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
 assertNull(deserializer);
 }

 @Test
 public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
 final DeserializationContext ctxt = new TestDeserializationContext();
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, String.class.getTypeName());
 final JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
 assertNotNull(deserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerWithNullTypeId() throws IOException {
 final DeserializationContext ctxt = new TestDeserializationContext();
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
 typeDeserializerBase._findDeserializer(ctxt, null);
 fail("Expected IOException but did not receive one");
 }

 @Test
 public void testToString() {
 final TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
 assertEquals("[TypeDeserializerBase; base-type:class java.lang.Object; id-resolver:com.fasterxml.jackson.core.type.StandardTypeIdResolver@3f5417e2]", typeDeserializerBase.toString());
 }
}

 class StubJsonDeserializer<T> extends JsonDeserializer<T> {
 private final Class<T> type;

 public StubJsonDeserializer(Class<T> type) {
 this.type = type;
 }

 @Override
 public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
}

 class TestDeserializationContext extends DeserializationContext {
 @Override
 public ConfigView getConfig() {
 return new ConfigView();
 }

 @Override
 public ObjectFactory getFactory() {
 return new TestObjectFactory();
 }

 @Override
 public ObjectReader getRootValueFactory() {
 return null;
 }

 @Override
 public JsonFactory getFactory() {
 return new TestJsonFactory();
 }

 @Override
 public DeserializationFeature[] getFeatures() {
 return new DeserializationFeature[0];
 }

 @Override
 public JavaType handleUnknownTypeId(JavaType baseType, String id, TypeIdResolver typeIdResolver) throws IOException {
 return null;
 }

 @Override
 public JavaType handleMissingTypeId(JavaType baseType, TypeIdResolver typeIdResolver) throws IOException {
 return null;
 }
}

 class TestJsonFactory extends JsonFactory {
 @Override
 public JsonParser createParser(InputStream stream, Locale locale) throws IOException {
 return null;
 }

 @Override
 public JsonParser createParser(File file) throws IOException {
 return null;
 }

 @Override
 public JsonParser createParser(String contentInUtf8) throws IOException {
 return null;
 }

 @Override
 public JsonParser createParser(char[] bytes, int offset, int length) throws IOException {
 return null;
 }

 @Override
 public ObjectCodec createObjectCodec() {
 return null;
 }
}

 class TestObjectFactory extends ObjectFactory {
 @Override
 public SimpleValueInstantiator _valueInstantiator() {
 return null;
 }

 @Override
 public ValueInstantiator findValueInstantiator(JavaType type) {
 return null;
 }

 @Override
 public SimpleType _valueType(Class<?> rawClass) {
 return null;
 }

 @Override
 public JavaType constructType(Class<?> rawClass) {
 return null;
 }

 @Override
 public JavaType constructType(JavaType baseType, Class<?>... interfaces) {
 return null;
 }

 @Override
 public JavaType constructType(JavaType baseType, Class<?> cls, JavaType... interfaces) {
 return null;
 }

 @Override
 public JavaType constructType(JavaType baseType, JavaType[] interfaces) {
 return null;
 }

 @Override
 public JavaType constructType(JavaType baseType, JavaType[] interfaces, Class<?>... types) {
 return null;
 }

 @Override
 public JavaType constructSpecializedType(JavaType baseType, JavaType... typeArguments) {
 return null;
 }

 @Override
 public JavaType constructType(String name) throws ClassNotFoundException {
 return null;
 }

 @Override
 public JavaType constructTypeFromAnnotations(Class<?> rawClass, AnnotatedClass ac) {
 return null;
 }
}

 class TestClass {
 private String testProperty;
}