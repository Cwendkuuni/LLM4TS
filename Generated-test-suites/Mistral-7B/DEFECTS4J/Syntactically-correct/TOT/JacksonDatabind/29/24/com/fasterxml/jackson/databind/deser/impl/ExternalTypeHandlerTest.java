import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.PropertyName;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name"), new PropertyName("typeId") });
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\": \"John Doe\", \"typeId\": \"Person\"}");
 Object bean = new Object();
 handler.handleTypePropertyValue(jsonParser, new StandardDeserializationContext(), "name", bean);
 assertEquals("Person", handler.getLastTypeId());
 handler.handleTypePropertyValue(jsonParser, new StandardDeserializationContext(), "typeId", bean);
 assertNull(handler.getLastTypeId());
 }

 @Test
 public void testHandlePropertyValue_NullValue() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name") });
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\": null}");
 Object bean = new Object();
 handler.handlePropertyValue(jsonParser, new StandardDeserializationContext(), "name", bean);
 assertNull(bean);
 }

 @Test
 public void testHandlePropertyValue_StringValue() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name") });
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\": \"John Doe\"}");
 Object bean = new Object();
 handler.handlePropertyValue(jsonParser, new StandardDeserializationContext(), "name", bean);
 assertEquals("John Doe", handler.getLastPropertyValue());
 }

 @Test
 public void testHandlePropertyValue_NestedObject() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("person") });
 JsonParser jsonParser = mapper.getFactory().createParser("{\"person\": {\"name\": \"John Doe\"}}");
 Object bean = new Object();
 handler.handlePropertyValue(jsonParser, new StandardDeserializationContext(), "person", bean);
 assertTrue(bean instanceof Map);
 Map map = (Map) bean;
 assertEquals("John Doe", map.get("name"));
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValue_MissingProperty() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name") });
 JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId\": \"Person\"}");
 Object bean = new Object();
 handler.handlePropertyValue(jsonParser, new StandardDeserializationContext(), "name", bean);
 fail("Expected IOException for missing property");
 }

 @Test
 public void testComplete() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name"), new PropertyName("typeId") });
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\": \"John Doe\", \"typeId\": \"Person\"}");
 Object bean = new Object();
 handler.complete(jsonParser, new StandardDeserializationContext(), bean);
 assertEquals("Person", bean.getClass().getName());
 Map map = (Map) bean;
 assertEquals("John Doe", map.get("name"));
 }

 @Test
 public void testComplete_NullBean() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name"), new PropertyName("typeId") });
 Object bean = null;
 handler.complete(mapper.getFactory().createParser("{\"name\": \"John Doe\", \"typeId\": \"Person\"}"), new StandardDeserializationContext(), bean);
 assertNull(bean);
 }

 @Test
 public void testComplete_MissingTypeId() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name"), new PropertyName("typeId") });
 JsonParser jsonParser = mapper.getFactory().createParser("{\"name\": \"John Doe\"}");
 Object bean = new Object();
 try {
 handler.complete(jsonParser, new StandardDeserializationContext(), bean);
 fail("Expected IOException for missing typeId");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testComplete_MissingProperty() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name"), new PropertyName("typeId") });
 JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId\": \"Person\"}");
 SettableBeanProperty prop = new AnnotatedClassSelectorProperty("name", Person.class);
 Object bean = new Object();
 try {
 handler.complete(jsonParser, new StandardDeserializationContext(), bean, prop);
 fail("Expected IOException for missing property");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testComplete_PropertyValueBuffer() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name"), new PropertyName("typeId") });
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object createUsingDefaults() throws IOException {
 return new Person();
 }

 @Override
 public Object createUsingBeanProperties(DeserializationContext ctxt, Map<String, Object> properties) throws IOException {
 Person person = new Person();
 person.setName((String) properties.get("name"));
 return person;
 }
 };
 Object bean = new Object();
 JsonParser jsonParser = mapper.getFactory().createParser("{{\"name\": \"John Doe\", \"typeId\": \"Person\"}}");
 PropertyValueBuffer buffer = new PropertyValueBuffer();
 handler.complete(jsonParser, new StandardDeserializationContext(), buffer, creator);
 assertEquals(2, buffer.size());
 assertEquals("John Doe", buffer.get("name"));
 assertEquals(Person.class, buffer.get("typeId"));
 }

 @Test
 public void testComplete_PropertyBasedCreator() throws IOException {
 ExternalTypeHandler handler = createHandlerWithProperties(new PropertyName[] { new PropertyName("name"), new PropertyName("typeId") });
 PropertyBasedCreator creator = new PropertyBasedCreator() {
 @Override
 public Object createUsingDefaults() throws IOException {
 return new Person();
 }

 @Override
 public Object createUsingBeanProperties(DeserializationContext ctxt, Map<String, Object> properties) throws IOException {
 Person person = new Person();
 person.setName((String) properties.get("name"));
 person.setTypeId((String) properties.get("typeId"));
 return person;
 }
 };
 Object bean = new Object();
 JsonParser jsonParser = mapper.getFactory().createParser("{{\"name\": \"John Doe\", \"typeId\": \"Person\"}}");
 handler.complete(jsonParser, new StandardDeserializationContext(), bean, creator);
 assertEquals("John Doe", bean.getClass().getMethod("getName").invoke(bean));
 assertEquals("Person", bean.getClass().getMethod("getTypeId").invoke(bean));
 }

 private ExternalTypeHandler createHandlerWithProperties(PropertyName... names) {
 PropertyName[] properties = new PropertyName[names.length];
 System.arraycopy(names, 0, properties, 0, names.length);
 return new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new AnnotatedClassSelectorProperty("name", String.class), mapper.getTypeFactory().constructValueType(String.class))
 .addExternal(new AnnotatedClassSelectorProperty("typeId", Person.class), mapper.getTypeFactory().constructValueType(Person.class))
 .build());
 }

 private static class AnnotatedClassSelectorProperty extends SettableBeanProperty {
 private final Class<?> _clazz;

 public AnnotatedClassSelectorProperty(String propertyName, Class<?> clazz) {
 super(propertyName, clazz);
 _clazz = clazz;
 }

 @Override
 public void set(Object bean, Object value) throws IllegalAccessException {
 super.set(bean, value);
 }

 @Override
 public Object getValue(Object bean) {
 return super.get(bean);
 }

 @Override
 public void setValue(Object bean, Object value) throws IllegalAccessException {
 super.setValue(bean, value);
 }

 @Override
 public Object get(Object bean) throws IllegalAccessException {
 return super.get(bean);
 }

 @Override
 public Type getType(Object bean) throws IllegalAccessException {
 return _clazz;
 }
 }

 private static class Person {
 private String name;
 private String typeId;

 public void setName(String name) {
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setTypeId(String typeId) {
 this.typeId = typeId;
 }

 public String getTypeId() {
 return typeId;
 }
 }
}