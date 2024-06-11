import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectMapper objectMapper;
 private JsonFactory jsonFactory;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private Foo foo;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 jsonFactory = objectMapper.getFactory();
 objectIdReader = new SimpleObjectIdReader(JsonType.LONG);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 foo = new Foo();
 }

 @Test
 public void testDefaultBehavior() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", 1L);
 ObjectReader reader = objectMapper.readerForMap(Foo.class);
 Foo foo = reader.readValue(node);
 assertNotNull(foo.getId());
 assertEquals(1, foo.getId().longValue());
 }

 @Test
 public void testNullId() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 ObjectReader reader = objectMapper.readerForMap(Foo.class);
 Foo foo = reader.readValue(node);
 assertNull(foo.getId());
 }

 @Test
 public void testCustomIdDeserializer() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", "abc123");
 ObjectReader reader = objectMapper.readerForMap(Foo.class)
 .withValueDeserializer(new StringToLongDeserializer());
 Foo foo = reader.readValue(node);
 assertNotNull(foo.getId());
 assertEquals("abc123", foo.getId().toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullId() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 ObjectReader reader = objectMapper.readerForMap(Foo.class);
 Object id = null;
 foo.setId(id);
 fail("Should throw UnsupportedOperationException when trying to set null id");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetIdWithoutIdProperty() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 ObjectReader reader = objectMapper.readerForMap(NoIdPropertyFoo.class);
 Object id = 1L;
 NoIdPropertyFoo foo = reader.readValue(node);
 foo.setId(id);
 fail("Should throw UnsupportedOperationException when trying to set id on a Foo without an idProperty");
 }

 @Test
 public void testSetId() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", 2L);
 ObjectReader reader = objectMapper.readerForMap(Foo.class);
 Foo foo = reader.readValue(node);
 assertEquals(2, foo.getId().longValue());
 foo.setId(3L);
 assertEquals(3, foo.getId().longValue());
 }

 @Test
 public void testCustomIdPropertyName() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("objectId", 4L);
 ObjectReader reader = objectMapper.readerForMap(CustomIdPropertyFoo.class);
 CustomIdPropertyFoo foo = reader.readValue(node);
 assertEquals(4, foo.getId().longValue());
 }

 @Test
 public void testCustomIdPropertyNameAndIdDeserializer() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("objectId", "def456");
 ObjectReader reader = objectMapper.readerForMap(CustomIdPropertyFoo.class)
 .withValueDeserializer(new StringToLongDeserializer());
 CustomIdPropertyFoo foo = reader.readValue(node);
 assertEquals("def456", foo.getId().toString());
 }

 @Test
 public void testCustomIdPropertyNameAndIdProperty() throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("objectId", 7L);
 ObjectReader reader = objectMapper.readerForMap(CustomIdPropertyFoo.class);
 CustomIdPropertyFoo foo = reader.readValue(node);
 assertEquals(7, foo.getId().longValue());
 foo.setObjectId(8L);
 assertEquals(8, foo.getId().longValue());
 }

 @Test
 public void testCustomIdPropertyNameAndIdPropertyAndCustomIdDeserializer()
 throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("objectId", "hij789");
 ObjectReader reader = objectMapper.readerForMap(CustomIdPropertyFoo.class)
 .withValueDeserializer(new StringToLongDeserializer());
 CustomIdPropertyFoo foo = reader.readValue(node);
 assertEquals("hij789", foo.getId().toString());
 foo.setObjectId(10L);
 assertEquals(10, foo.getId().longValue());
 }

 @Test
 public void testCustomIdPropertyNameAndIdPropertyAndIdPropertyDeserializer()
 throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("objectId", 11L);
 ObjectReader reader = objectMapper.readerForMap(CustomIdPropertyFoo.class)
 .withValueDeserializer(new CustomIdDeserializer());
 CustomIdPropertyFoo foo = reader.readValue(node);
 assertEquals(11, foo.getId().longValue());
 foo.setObjectId(12L);
 assertEquals(12, foo.getId().longValue());
 }

 @Test
 public void testCustomIdPropertyNameAndIdPropertyAndIdDeserializerAndCustomName()
 throws Exception {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("objectId", 13L);
 ObjectReader reader = objectMapper.readerForMap(CustomIdPropertyFoo.class)
 .withValueDeserializer(new CustomIdDeserializer())
 .withPropertyName("_id");
 CustomIdPropertyFoo foo = reader.readValue(node);
 assertEquals(13, foo.getId().longValue());
 foo.setObjectId(14L);
 assertEquals(14, foo.getId().longValue());
 }

 private static class Foo {
 @JsonDeserialize(using = ObjectIdValueProperty.class)
 private Long id;
 }

 private static class NoIdPropertyFoo {
 // no idProperty defined
 }

 private static class CustomIdPropertyFoo {
 @JsonDeserialize(using = ObjectIdValueProperty.class)
 @JsonProperty("objectId")
 private Long id;
 }

 private static class CustomIdDeserializer extends JsonDeserializer<Long> {
 @Override
 public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return ctxt.readValueAs(Long.class, p);
 }
 }

}