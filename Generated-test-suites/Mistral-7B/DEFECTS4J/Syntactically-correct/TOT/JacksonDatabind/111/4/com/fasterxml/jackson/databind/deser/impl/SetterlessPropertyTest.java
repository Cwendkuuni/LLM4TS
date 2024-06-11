package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.NodeProcessor;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SetterlessPropertyTest extends TestBase {

 private SettableBeanProperty<Object, Object> property;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

 Class<?> testClass = Mockito.mock(Class.class);
 BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
 JavaType type = Mockito.mock(JavaType.class);
 TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
 Annotations contextAnnotations = Mockito.mock(Annotations.class);
 AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

 property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
 }

 @Test
 public void testDeserializeAndSetNullValue() throws JsonProcessingException {
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object instance = Mockito.mock(Object.class);

 when(p.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

 property.deserializeAndSet(p, ctxt, instance);

 verify(ctxt).reportBadDefinition(type, "Problem deserializing 'setterless' property (null): get method returned null");
 }

 @Test
 public void testDeserializeAndSetValidValue() throws JsonProcessingException {
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object instance = Mockito.mock(Object.class);
 JsonNode expectedNode = createNode("expectedValue");
 Object expectedValue = mapper.readValue(expectedNode, getTypeOf(Object.class));

 when(p.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
 when(p.nextValue()).thenReturn(expectedNode);

 when(type.getType()).thenReturn(getCollectionType(List.class, String.class));
 when(method.getAnnotated().getMethod()).thenReturn(getGetterMethod(instance, "getList"));

 property.deserializeAndSet(p, ctxt, instance);

 verify(instance).getClass();
 verify(instance).getClass().getMethod("getList")
         .invoke(instance, (Object[]) anyArray());
 verify(ctxt.getBeanDeserializerProvider()).getDeserializerFactory()
         .findDeserializerForProperty(type, _getter, contextAnnotations, mapper);
 verify(ctx.getValueDeserializer()).deserialize(p, ctxt, any());
 verify(instance).getClass().getMethod("setList")
         .invoke(instance, eq(expectedValue), (Object[]) anyArray());
 }

 @Test
 public void testDeserializeAndSetInvalidType() throws JsonProcessingException {
 JsonParser p = mock(JsonParser.class);
 DeserializationContext ctxt = mock(DeserializationContext.class);
 Object instance = Mockito.mock(Object.class);

 when(p.getValueAsToken()).thenReturn(JsonToken.START_OBJECT);
 when(p.nextValue()).thenReturn(createNode("anInvalidType"));

 when(type.getType()).thenReturn(getCollectionType(List.class, String.class));
 when(method.getAnnotated().getMethod()).thenReturn(getGetterMethod(instance, "getList"));

 property.deserializeAndSet(p, ctxt, instance);

 verify(ctxt).reportBadDefinition(type,
         "Problem deserializing 'setterless' property (anInvalidType): "
         + "no suitable deserializer found for this type");
 }

 @Test
 public void testValidateAnnotations() throws Exception {
 SettableBeanProperty<Object, Object> property = Mockito.spy(this.property);

 property.fixAccess(MapperFactory.getDefaultInstance());

 verify(method.getAnnotated()).fixAccess(MapperFactory.getDefaultInstance()
         .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 private static Method getGetterMethod(Object instance, String propertyName) throws NoSuchMethodException {
 return instance.getClass().getMethod("get" + Character.toUpperCase(propertyName.charAt(0))
         + propertyName.substring(1));
 }

 private static Class<?> getTypeOf(Class<?> clazz) {
 return clazz;
 }

 private static TypeDescriptor getCollectionType(Class<?> collectionClass, Class<?> elementType) {
 return mapper.getTypeFactory()
         .constructCollectionType(collectionClass, elementType);
 }

 private static JsonDeserializer<?> getJsonDeserializer() {
 return new StdDeserializer<Object>(Object.class) {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
         TypeDeserializer typeDeserializer) throws IOException {
 return null;
 }
 };
 }

 private static JsonDeserializer<?> _valueDeserializer = getJsonDeserializer();
 private static NullValueProvider _nullProvider = mock(NullValueProvider.class);
 private static TypeDeserializer _valueTypeDeserializer = mock(TypeDeserializer.class);

}