import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 @Test
 public void testSerializingSimpleValue() throws JsonProcessingException {
 ObjectNode input = JsonNodeFactory.instance.objectNode();
 input.put("inputValue", 123);
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer serializer = mapper.findValueSerializer(JsonTypeFactory.instance.constructType(Integer.class));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(input.get("inputValue"), gen, mapper.getSerializerProvider());
 gen.flush();
 }

 @Test
 public void testSerializingNullValue() throws JsonProcessingException {
 ObjectNode input = JsonNodeFactory.instance.objectNode();
 StdDelegatingSerializer serializer = mapper.findValueSerializer(JsonTypeFactory.instance.constructType(String.class));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(null, gen, mapper.getSerializerProvider());
 gen.flush();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializingDifferentTypes() throws JsonProcessingException {
 ObjectNode input = JsonNodeFactory.instance.objectNode();
 input.put("inputValue1", 123);
 input.put("inputValue2", "testString");
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer serializer = mapper.findValueSerializer(JsonTypeFactory.instance.constructType(Integer.class));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(input, gen, mapper.getSerializerProvider());
 }

 @Test
 public void testContextualization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ContextualSerializers contextualSerializers = mapper.getSerializers();
 StdDelegatingSerializer serializer1 = contextualSerializers.findValueSerializer(JsonTypeFactory.instance.constructType(Integer.class));
 StdDelegatingSerializer serializer2 = contextualSerializers.findValueSerializer(JsonTypeFactory.instance.constructType(String.class));
 ObjectNode input = JsonNodeFactory.instance.objectNode();
 input.put("inputValue", 123);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer1.serialize(input.get("inputValue"), gen, mapper.getSerializerProvider());
 gen.flush();
 serializer2.serialize(input.get("inputValue"), gen, mapper.getSerializerProvider());
 gen.flush();
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDelegation() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer serializer = mapper.findValueSerializer(JsonTypeFactory.instance.constructType(Object.class));
 ObjectNode input = JsonNodeFactory.instance.objectNode();
 input.put("inputValue", new Object());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(input.get("inputValue"), gen, mapper.getSerializerProvider());
 }

 @Test
 public void testValidDelegation() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer serializer = mapper.findValueSerializer(JsonTypeFactory.instance.constructType(Integer.class));
 ObjectNode input = JsonNodeFactory.instance.objectNode();
 input.put("inputValue", new Integer(123));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(input.get("inputValue"), gen, mapper.getSerializerProvider());
 gen.flush();
 }

 @Test
 public void testInvalidDelegateType() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer serializer = mapper.findValueSerializer(JsonTypeFactory.instance.constructType(Integer.class));
 ObjectNode input = JsonNodeFactory.instance.objectNode();
 input.put("inputValue", "testString");
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 try {
 serializer.serialize(input.get("inputValue"), gen, mapper.getSerializerProvider());
 fail();
 } catch (JsonMappingException e) {
 }
 gen.flush();
 }

 @Test
 public void testSchemaAware() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdDelegatingSerializer serializer = mapper.findValueSerializer(JsonTypeFactory.instance.constructType(Integer.class));
 ObjectNode input = JsonNodeFactory.instance.objectNode();
 JsonSchema schema = mapper.getFactory().getSchemaProvider().findSchemaForSerializer(serializer);
 assertEquals(JsonPOJOClassMapper.Type.VALUE_TYPE, schema.getSchemaType());
 }
}