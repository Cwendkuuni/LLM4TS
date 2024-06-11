package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.BeanDeserializerProvider.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectIdReader = mock(ObjectIdReader.class);
 when(objectIdReader.propertyName).thenReturn("id");
 when(objectIdReader.getIdType()).thenReturn(Integer.class);
 when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
 when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
 when(objectIdReader.idProperty).thenReturn(new StubSettableBeanProperty("idProp"));
 BeanDeserializerFactory factory = new BeanDeserializerFactory(objectMapper.getConfig(), objectMapper.getTypeFactory());
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata() {
 @Override public BeanPropertyDefinition mbeanPropertyDefinition(AnnotationIntrospector valueIntrospector) { return null; }
 @Override public BeanPropertyDefinition findPropertyDefinition(BeanPropertyDefinition bpd, String name, TypeIdResolver typeIdResolver) { return null; }
 @Override public Object getRawSetter() { return null; }
 @Override public Object getRawGetter() { return null; }
 @Override public String getName() { return "testProperty"; }
 @Override public ObjectIdReader getObjectIdReader() { return objectIdReader; }
 @Override public JsonDeserializer<?> getValueDeserializer() { return new None(); }
 @Override public BeanDeserializerFactory.BeanDeserializerBase createBeansDeserializer(BeanPropertyDefinition propertyDefinition, JsonDeserializer<?> valueDeserializer) { return null; }
 @Override public BeanPropertyDefinition findPropertyDefinition(String name, TypeIdResolver typeIdResolver) { return null; }
 @Override public Object getValue(Object bean) { return null; }
 @Override public void setValue(Object bean, Object value) { }
 @Override public void setValue(Object bean, Object value, Object parent) { }
 @Override public PropertyMetadata getMetadata() { return null; }
 @Override public void setValueWithThrown(Object bean, Object value, Throwable t) { }
 @Override public void setValueWithThrown(Object bean, Object value, Object parent, Throwable t) { }
 @Override public String getNameAsText() { return "testProperty"; }
 @Override public String getNameWithPrefix(String prefix) { return "testProperty"; }
 @Override public String getShortName() { return null; }
 @Override public String getNameWithTransformer(NameTransformer nameTransformer) { return "testProperty"; }
 @Override public boolean isEmpty() { return false; }
 @Override public Class<?> getRawType() { return Object.class; }
 @Override public Class<?> getComponentType() { return null; }
 @Override public Annotation[] getAnnotations() { return new Annotation[0]; }
 @Override public String toString() { return "testProperty"; }
 });
 }

 @Test
 public void testGetAnnotation() throws IOException {
 assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
 }

 @Test
 public void testGetMember() throws IOException {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = 1;
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": " + id + "}");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.findObjectId(eq(id), eq(objectIdReader), any())).thenReturn(new ReadableObjectId() {
 @Override public void bindItem(Object obj) { }
 });
 when(context.findBeanProperties()).thenReturn(new BeanPropertyCollection() {
 @Override public int size() { return 0; }
 @Override public BeanPropertyDefinition findPropertyDefinition(String name) { return null; }
 @Override public BeanPropertyDefinition findPropertyDefinition(Class<?> clazz) { return null; }
 @Override public BeanPropertyDefinition findPropertyDefinition(String name, Class<?> type) { return null; }
 @Override public Iterator<BeanPropertyDefinition> iterator() { return null; }
 });
 when(objectMapper.readValueAs(JsonParser.class, new TypeReference<Object>() {})).thenReturn(jsonParser);
 objectIdValueProperty.deserializeAndSet(jsonParser, context, new Object());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = 1;
 Object result = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().createParser("{\"testProperty\": " + id + "}"), mock(DeserializationContext.class), new Object());
 assertNotNull(result);
 }

 @Test
 public void testSet() throws IOException {
 Object id = 1;
 Object instance = new Object();
 objectIdValueProperty.set(instance, id);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object id = 1;
 Object result = objectIdValueProperty.setAndReturn(new Object(), id);
 assertNotNull(result);
 }
}

 class ObjectIdGenerator {
 // empty implementation
 }

 class ObjectIdResolver {
 // empty implementation
 }

 class StubSettableBeanProperty extends SettableBeanProperty {
 private String name;

 public StubSettableBeanProperty(String name) {
 super(null, null, null, null, null);
 this.name = name;
 }

 @Override public Object getValue(Object bean) { return null; }

 @Override public void setValue(Object bean, Object value) { }

 @Override public Object setAndReturn(Object bean, Object value) throws IOException { return bean; }

 @Override public void setWithThrown(Object bean, Object value, Throwable t) { }

 @Override public void setWithThrown(Object bean, Object value, Object parent, Throwable t) { }

 @Override public String getName() { return name; }

 @Override public String getNameAsText() { return name; }

 @Override public String getNameWithPrefix(String prefix) { return name; }

 @Override public String getShortName() { return null; }

 @Override public String getNameWithTransformer(NameTransformer nameTransformer) { return name; }

 @Override public boolean isEmpty() { return false; }

 @Override public Class<?> getRawType() { return Object.class; }

 @Override public Class<?> getComponentType() { return null; }

 @Override public Annotation[] getAnnotations() { return new Annotation[0]; }

 @Override public String toString() { return name; }
 }

 class ReadableObjectId {
 // empty implementation
 }