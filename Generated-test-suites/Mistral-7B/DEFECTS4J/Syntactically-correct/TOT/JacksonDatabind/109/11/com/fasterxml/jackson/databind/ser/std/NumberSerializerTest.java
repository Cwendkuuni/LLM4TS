import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(NumberSerializable.class, mapper.getFactory(), null);
 numberSerializer.serialize(bigDecimal, generator, mapper.getSerializerProvider());
 String expected = "123.456";
 assertEquals(expected, generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(NumberSerializable.class, mapper.getFactory(), null);
 numberSerializer.serialize(bigInteger, generator, mapper.getSerializerProvider());
 String expected = "12345678901234567890";
 assertEquals(expected, generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 int intValue = 123;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(NumberSerializable.class, mapper.getFactory(), null);
 numberSerializer.serialize(intValue, generator, mapper.getSerializerProvider());
 String expected = "123";
 assertEquals(expected, generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 float floatValue = 123.45f;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(NumberSerializable.class, mapper.getFactory(), null);
 numberSerializer.serialize(floatValue, generator, mapper.getSerializerProvider());
 String expected = "123.45";
 assertEquals(expected, generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 double doubleValue = 123.45;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(NumberSerializable.class, mapper.getFactory(), null);
 numberSerializer.serialize(doubleValue, generator, mapper.getSerializerProvider());
 String expected = "123.45";
 assertEquals(expected, generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 byte byteValue = 127;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(NumberSerializable.class, mapper.getFactory(), null);
 numberSerializer.serialize(byteValue, generator, mapper.getSerializerProvider());
 String expected = "127";
 assertEquals(expected, generator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 short shortValue = 123;
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(NumberSerializable.class, mapper.getFactory(), null);
 numberSerializer.serialize(shortValue, generator, mapper.getSerializerProvider());
 String expected = "123";
 assertEquals(expected, generator.getOutputBuffer().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializeString() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 String stringValue = "123";
 JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class)
 .getSerializerProvider().getGeneratorFactory()
 .createGenerator(NumberSerializable.class, mapper.getFactory(), null);
 numberSerializer.serialize(stringValue, generator, mapper.getSerializerProvider());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 SchemaFactory schemaFactory = new JsonSchemaFactory();
 JsonSchema schema = schemaFactory.getSchema(new ObjectMapper().configure(SerializerFeatures.FAIL_ON_EMPTY_BEANS, false)
 .getValueHandler(null).getValueSerializer()
 .getSerializerProvider()
 .getSerializers());
 JsonNode expected = schema.getSchemaProvider().getRootNode();
 JsonNode actual = numberSerializer.getSchema(null, Number.class);
 assertEquals(expected, actual);
 }
}