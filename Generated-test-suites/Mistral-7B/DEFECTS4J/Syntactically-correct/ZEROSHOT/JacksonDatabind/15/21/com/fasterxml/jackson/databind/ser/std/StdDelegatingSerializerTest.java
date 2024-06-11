import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper mapper;
 private StdDelegatingSerializer delegateSerializer;
 private Converter<Integer, String> converter;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 converter = mapper.getTypeFactory().constructConverter(Integer.class, String.class);
 }

 @Test
 public void testStdDelegatingSerializer_DefaultConstructor() throws JsonGenerationException, IOException {
 Object source = 123;
 delegateSerializer = new StdDelegatingSerializer(converter);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 delegateSerializer.serialize(source, gen, mapper.getSerializationConfig());
 String expected = "'123'";
 assertEquals(expected, gen.getOutputContentAsString());
 }

 @Test
 public void testStdDelegatingSerializer_ConstructorWithClass() throws JsonGenerationException, IOException {
 Class<String> targetClass = String.class;
 Object source = "Test";
 delegateSerializer = new StdDelegatingSerializer(String.class, converter);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 delegateSerializer.serialize(source, gen, mapper.getSerializationConfig());
 String expected = "'Test'";
 assertEquals(expected, gen.getOutputContentAsString());
 }

 @Test
 public void testStdDelegatingSerializer_ConstructorWithConverter() throws JsonGenerationException, IOException {
 Object source = new Integer(123);
 delegateSerializer = new StdDelegatingSerializer(converter);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 delegateSerializer.serialize(source, gen, mapper.getSerializationConfig());
 String expected = "'123'";
 assertEquals(expected, gen.getOutputContentAsString());
 }

 @Test
 public void testStdDelegatingSerializer_ConstructorWithConverterType() throws JsonGenerationException, IOException {
 Object source = new Integer(123);
 JavaType targetType = mapper.getTypeFactory().constructType(String.class);
 delegateSerializer = new StdDelegatingSerializer(converter, targetType, mapper.findValueSerializer(targetType));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 delegateSerializer.serialize(source, gen, mapper.getSerializationConfig());
 String expected = "'123'";
 assertEquals(expected, gen.getOutputContentAsString());
 }

 @Test
 public void testStdDelegatingSerializer_resolve() throws JsonGenerationException, IOException {
 delegateSerializer = new StdDelegatingSerializer(converter);
 delegateSerializer.resolve(mapper.getSerializationConfig());
 JsonSerializer<String> delegateSerializerResolved = delegateSerializer.getDelegatee();
 assertNotNull(delegateSerializerResolved);
 }

 @Test
 public void testStdDelegatingSerializer_createContextual() throws JsonGenerationException, IOException {
 JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
 JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
 delegateSerializer = new StdDelegatingSerializer(converter);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 Object source = new Integer(123);
 JsonSerializer<Object> serializedSerializer = delegateSerializer.createContextual(mapper.getSerializationConfig(), null);
 serializedSerializer.serialize(source, gen, mapper.getSerializationConfig());
 String expected = "'123'";
 assertEquals(expected, gen.getOutputContentAsString());

 delegateSerializer = new StdDelegatingSerializer(converter);
 serializedSerializer = delegateSerializer.createContextual(mapper.getSerializationConfig(), new BeanProperty("testProperty"));
 serializedSerializer.serialize(source, gen, mapper.getSerializationConfig());
 expected = "'123'";
 assertEquals(expected, gen.getOutputContentAsString());

 delegateSerializer = new StdDelegatingSerializer(converter);
 serializedSerializer = delegateSerializer.createContextual(mapper.getSerializationConfig(), null);
 assertEquals(intSerializer, serializedSerializer);
 }

 @Test
 public void testStdDelegatingSerializer_serialize() throws JsonGenerationException, IOException {
 Object source = new Integer(123);
 delegateSerializer = new StdDelegatingSerializer(converter);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 delegateSerializer.serialize(source, gen, mapper.getSerializationConfig());
 String expected = "'123'";
 assertEquals(expected, gen.getOutputContentAsString());
 }

 @Test
 public void testStdDelegatingSerializer_serializeWithType() throws JsonGenerationException, IOException {
 Object source = new Integer(123);
 JavaType targetType = mapper.getTypeFactory().constructType(String.class);
 delegateSerializer = new StdDelegatingSerializer(converter, targetType, mapper.findValueSerializer(targetType));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 delegateSerializer.serializeWithType(source, gen, mapper.getSerializationConfig(), typeSerializer);
 String expected = "'123'";
 assertEquals(expected, gen.getOutputContentAsString());
 }

 @Test
 public void testStdDelegatingSerializer_isEmpty() throws JsonGenerationException, IOException {
 Object source = null;
 delegateSerializer = new StdDelegatingSerializer(converter);
 assertTrue(delegateSerializer.isEmpty(null));
 assertTrue(delegateSerializer.isEmpty(mapper.getSerializationConfig(), null));
 }

 @Test
 public void testStdDelegatingSerializer_getSchema() throws JsonMappingException {
 JsonNode expected = mapper.readTree("{@type: \"string\"}");
 delegateSerializer = new StdDelegatingSerializer(converter);
 assertEquals(expected, delegateSerializer.getSchema(mapper.getSerializationConfig(), Object.class));
 }

 @Test
 public void testStdDelegatingSerializer_acceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getDefaultFormatVisitor(MapperFeature.AUTO_DETECT_TYPES, Object.class);
 delegateSerializer = new StdDelegatingSerializer(converter);
 delegateSerializer.acceptJsonFormatVisitor(visitor, null);
 visitor.expectCustomSerializableModel(Object.class, StdDelegatingSerializer.class);
 }
}