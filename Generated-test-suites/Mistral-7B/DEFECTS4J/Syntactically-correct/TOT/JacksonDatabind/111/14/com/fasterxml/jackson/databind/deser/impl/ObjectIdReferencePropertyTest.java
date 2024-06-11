package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.*;
import org.junit.runner.*;
import static org.junit.Assert.*;

@RunWith(JUnitRunner.class)
public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private ObjectIdReferenceProperty objIdProp;
 private JsonParser parser;
 private DeserializationContext ctxt;

 @Before
 public void setup() throws JsonProcessingException {
 objectIdInfo = new ObjectIdInfo(MyPojo.class, "id");
 objIdProp = new ObjectIdReferenceProperty(new StdScalarProperty.Std(new TextNode("name")), objectIdInfo);
 parser = new MapperFactory().getFactory().createParser("{\"name\": 1}");
 ctxt = new DefaultDeserializationContext();
 }

 @Test
 public void testBasicDeserialization() throws IOException {
 Object pojo = new ObjectMapper().readValue(parser, MyPojo.class);
 assertEquals("Name from deserialized object", pojo.getName(), "Name from input");
 }

 @Test
 public void testDeserializationWithForwardReference() throws IOException {
 parser = new MapperFactory().getFactory().createParser("{\"name\": null, \"id\": 1}");
 ctxt.setActiveContext(new MinimalContext());
 ctxt.setInputFilter(new FilterProvider().emptyFilterProvider());
 JsonDeserializer<String> deserializer = new StringDeserializer();
 ObjectIdReferenceProperty objIdPropWithDeserializer = new ObjectIdReferenceProperty(objIdProp, deserializer, null);
 ctxt.findDeserializer(MyPojo.class, "name");
 ctxt.findDeserializer(MyPojo.class, "id");
 Object pojo = objIdPropWithDeserializer.deserializeAndSet(parser, ctxt, new MyPojo());
 assertEquals("Name from deserialized object", pojo.getName(), "Name from input");
 }

 @Test
 public void testDeserializationWithNullProvider() throws IOException {
 NullValueProvider nva = new StandardNullValueProvider();
 ObjectIdReferenceProperty objIdPropWithNullProvider = new ObjectIdReferenceProperty(objIdProp, null, nva);
 Object pojo = objIdPropWithNullProvider.deserializeAndSet(parser, ctxt, new MyPojo());
 assertNull("Name should be null", pojo.getName());
 }

 @Test
 public void testDeserializationWithUnresolvedForwardReference() throws IOException {
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 ctxt.setInputFilter(new FilterProvider().filter("{\"name\": null, \"id\": }", 0, 2));
 ObjectIdReferenceProperty objIdPropWithUnresolvedForwardReference = new ObjectIdReferenceProperty(objIdProp, null, null);
 ctxt.findDeserializer(MyPojo.class, "name");
 ctxt.findDeserializer(MyPojo.class, "id");
 Object pojo = objIdPropWithUnresolvedForwardReference.deserializeAndSet(parser, ctxt, new MyPojo());
 assertNotNull("Object should not be null", pojo);
 // Check that the forward reference was stored correctly
 PropertyReferring referencing = (PropertyReferring) ctxt.getInputContext().getRootValue().getClass().getDeclaredField("_referring").get(ctxt.getInputContext().getRootValue());
 assertEquals("Parent property", objIdProp, referencing._parent);
 }

 @Test
 public void testSerialization() throws IOException {
 MyPojo pojo = new MyPojo("Name from ObjectIdReferencePropertyTest", 42);
 ObjectWriter writer = new ObjectMapper().writer();
 String json = writer.writeValueAsString(pojo);
 JsonParser parser = new MapperFactory().getFactory().createParser(json);
 Object pojoDeserialized = parser.readValue(MyPojo.class);
 assertEquals("Name from deserialized object", pojoDeserialized.getName(), pojo.getName());
 }

 @Test
 public void testSerializationWithForwardReference() throws IOException {
 MyPojo pojo = new MyPojo(null, 42);
 ObjectWriter writer = new ObjectMapper().writer();
 writer.setSerializationConfig(new SerializationConfig().withUnwrappingVisitor(new SimpleTypeSerializer.Std()));
 String json = writer.writeValueAsString(pojo);
 JsonParser parser = new MapperFactory().getFactory().createParser(json);
 Object pojoDeserialized = parser.readValue(MyPojo.class);
 assertNull("Name should be null", pojoDeserialized.getName());
 }
}

class MyPojo {
 private String name;
 private Integer id;

 public MyPojo(String name, Integer id) {
 this.name = name;
 this.id = id;
 }

 public String getName() {
 return name;
 }
}