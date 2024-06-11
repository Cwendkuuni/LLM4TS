import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StdDelegatingSerializerTest {

 private static final Object SOME_VALUE = new Object() {
 };
 private static final Class<String> STRING_CLASS = String.class;
 private static final Class<Integer> INTEGER_CLASS = Integer.class;
 private static final Converter<Object, String> STRING_CONVERTER = new Converter.Std() {
 @Override
 public String convert(Object t) {
 return t.toString();
 }
 };
 private static final Converter<Object, Integer> INTEGER_CONVERTER = new Converter.Std() {
 @Override
 public Integer convert(Object t) {
 return (Integer) t;
 }
 };
 private static final JavaType STRING_TYPE = TypeFactory.defaultInstance().constructType(String.class);
 private static final JavaType INTEGER_TYPE = TypeFactory.defaultInstance().constructType(Integer.class);
 private static final ObjectMapper MAPPER = new ObjectMapper();
 private static final JsonSerializer<Object> STRING_SERIALIZER = MAPPER.findValueSerializer(STRING_TYPE);
 private static final JsonSerializer<Object> INTEGER_SERIALIZER = MAPPER.findValueSerializer(INTEGER_TYPE);
 private StdDelegatingSerializer<Object> stringDelegatingSerializer;
 private StdDelegatingSerializer<Object> integerDelegatingSerializer;

 @BeforeAll
 static void setup() throws Exception {
 stringDelegatingSerializer = new StdDelegatingSerializer<>(STRING_CONVERTER);
 integerDelegatingSerializer = new StdDelegatingSerializer<>(INTEGER_CONVERTER, INTEGER_TYPE, INTEGER_SERIALIZER);
 }

 @Test
 void testSerializationOfObjectWithStringConverter() throws JsonGenerationException, JsonMappingException {
 Object value = SOME_VALUE;
 String expectedResult = "some_value";
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 stringDelegatingSerializer.serialize(value, gen, new SerializerProvider());
 String actualResult = gen.getOutputContent();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 void testSerializationOfObjectWithStringConverterWithTypeSerialization() throws JsonGenerationException, JsonMappingException {
 Object value = SOME_VALUE;
 String expectedResult = "\"some_value\"";
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 TypeSerializer typeSerializer = MAPPER.getTypeSerializer();
 stringDelegatingSerializer.serializeWithType(value, gen, new SerializerProvider(), typeSerializer);
 String actualResult = gen.getOutputContent();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 void testSerializationOfObjectWithIntegerConverter() throws JsonGenerationException, JsonMappingException {
 Object value = SOME_VALUE;
 Integer expectedResult = 42;
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 integerDelegatingSerializer.serialize(value, gen, new SerializerProvider());
 int actualResult = gen.getOutputContentAsInt();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 void testSerializationOfObjectWithIntegerConverterWithTypeSerialization() throws JsonGenerationException, JsonMappingException {
 Object value = SOME_VALUE;
 Integer expectedResult = 42;
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 TypeSerializer typeSerializer = MAPPER.getTypeSerializer();
 integerDelegatingSerializer.serializeWithType(value, gen, new SerializerProvider(), typeSerializer);
 int actualResult = gen.getOutputContentAsInt();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 void testSerializationOfNullValueWithStringConverter() throws JsonGenerationException, JsonMappingException {
 Object value = null;
 String expectedResult = "null";
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 stringDelegatingSerializer.serialize(value, gen, new SerializerProvider());
 String actualResult = gen.getOutputContent();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 void testSerializationOfNullValueWithIntegerConverter() throws JsonGenerationException, JsonMappingException {
 Object value = null;
 Integer expectedResult = null;
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 integerDelegatingSerializer.serialize(value, gen, new SerializerProvider());
 int actualResult = gen.getOutputContentAsInt();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 void testSerializationOfObjectWithWrongConverter() throws JsonGenerationException, JsonMappingException {
 Object value = SOME_VALUE;
 String expectedResult = "some_value";
 JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
 StdDelegatingSerializer<Object> wrongDelegatingSerializer = new StdDelegatingSerializer<>(STRING_CONVERTER, INTEGER_TYPE, INTEGER_SERIALIZER);
 wrongDelegatingSerializer.serialize(value, gen, new SerializerProvider());
 String actualResult = gen.getOutputContent();
 assertNotEquals(expectedResult, actualResult);
 }
}