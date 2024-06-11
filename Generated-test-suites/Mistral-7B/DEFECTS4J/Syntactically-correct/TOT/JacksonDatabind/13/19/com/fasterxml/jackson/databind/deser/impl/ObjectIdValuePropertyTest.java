import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader = new ObjectIdReader() {
 @Override
 public BeanPropertyDefinition findPropertyDefinition(AnnotationIntrospector introspector, BeanDescription beanDesc, String name) {
 return introspector.findBeanPropertyDefinition(beanDesc.getAnnotatedClass(), name);
 }

 @Override
 public PropertyName propertyName() {
 return new PropertyName("id");
 }

 @Override
 public Class<?> idType() {
 return Long.class;
 }

 @Override
 public JsonDeserializer<?> idDeserializer() {
 return new FromStringDeserializer();
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null; // not used in this test
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null; // not used in this test
 }

 @Override
 public SettableBeanProperty idProperty() {
 return null; // not used in this test
 }
 };

 @Test
 public void testObjectIdValuePropertyWithValidId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode input = mapper.createObjectNode();
 input.put("id", "123");
 ObjectNode result = mapper.readValue(input.traverse(), Object.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 assertEquals(Long.valueOf(123), property.get(result, Long.class));
 }

 @Test
 public void testObjectIdValuePropertyWithNullId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode input = mapper.createObjectNode();
 ObjectNode result = mapper.readValue(input.traverse(), Object.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 assertNull(property.get(result, Long.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetObjectIdValuePropertyWithNullId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode input = mapper.createObjectNode();
 ObjectNode result = mapper.readValue(input.traverse(), Object.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 property.set(result, null);
 fail("Should not be able to set null id");
 }

 @Test
 public void testObjectIdValuePropertyWithEmptyStringId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode input = mapper.createObjectNode();
 input.put("id", "");
 ObjectNode result = mapper.readValue(input.traverse(), Object.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 assertNull(property.get(result, Long.class));
 }

 @Test(expected = IOException.class)
 public void testObjectIdValuePropertyWithInvalidId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode input = mapper.createObjectNode();
 input.put("id", "invalid");
 ObjectNode result = mapper.readValue(input.traverse(), Object.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 property.deserializeAndSet(null,null, result);
 fail("Should not be able to deserialize and set invalid id");
 }

 @Test
 public void testObjectIdValuePropertyWithSettableBeanPropertyId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode input = mapper.createObjectNode();
 input.put("id", "123");
 ObjectNode result = mapper.readValue(input.traverse(), new TypeReference<Object>() {});
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
 ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, "idProperty");
 BeanDescription beanDesc = mapper.findBeanDescription(result.getClass(), null);
 assertEquals(Long.valueOf(123), property.get(result, Long.class));
 assertEquals(Long.valueOf(123), idProperty.get(result, Long.class));
 }
}