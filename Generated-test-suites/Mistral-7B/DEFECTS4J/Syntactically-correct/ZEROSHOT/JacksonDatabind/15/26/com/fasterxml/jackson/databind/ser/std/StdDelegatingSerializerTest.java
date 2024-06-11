import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing StdDelegatingSerializer class")
public class StdDelegatingSerializerTest {

 private ObjectMapper objectMapper;
 private StdDelegatingSerializer stdDelegatingSerializer;

 @BeforeAll
 public static void setup() {
 objectMapper = new ObjectMapper();
 }

 @Nested
 @DisplayName("Constructor tests")
 class ConstructorTests {

 @Test
 void testConstructorWithConverter() {
 Converter<Object, ?> converter = new MapToIntConverter();
 stdDelegatingSerializer = new StdDelegatingSerializer(converter);
 assertNull(stdDelegatingSerializer._delegateType);
 assertNull(stdDelegatingSerializer._delegateSerializer);
 }

 @Test
 void testConstructorWithClassAndConverter() {
 Class<Integer> intClass = Integer.class;
 Converter<Integer, ?> converter = new MapToIntConverter();
 stdDelegatingSerializer = new StdDelegatingSerializer(intClass, converter);
 assertEquals(intClass, stdDelegatingSerializer.getClass());
 assertNull(stdDelegatingSerializer._delegateType);
 assertNull(stdDelegatingSerializer._delegateSerializer);
 }

 @Test
 void testConstructorWithConverterDelegateTypeAndDelegateSerializer() {
 Converter<Object, ?> converter = new MapToIntConverter();
 JavaType delegateType = objectMapper.typeFactory().constructType(int[].class);
 JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(int[].class);
 stdDelegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
 assertEquals(delegateType, stdDelegatingSerializer._delegateType);
 assertEquals(delegateSerializer, stdDelegatingSerializer._delegateSerializer);
 }
 }

 @Nested
 @DisplayName("Serialization tests")
 class SerializationTests {

 @Test
 void testSerializeNull() throws JsonProcessingException {
 Object nullValue = null;
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 stdDelegatingSerializer.serialize(nullValue, generator, objectMapper.getSerializerProvider());
 assertNull(generator.getOutputBuffer().toString());
 }

 @Test
 void testSerializeIntToIntArray() throws JsonProcessingException {
 int intValue = 5;
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 stdDelegatingSerializer.serialize(intValue, generator, objectMapper.getSerializerProvider());
 assertEquals("[]", generator.getOutputBuffer().toString());
 int[] intArray = new int[]{5};
 stdDelegatingSerializer = new StdDelegatingSerializer(new MapToIntArrayConverter(), int[].class, objectMapper.findValueSerializer(int[].class));
 stdDelegatingSerializer.serialize(intValue, generator, objectMapper.getSerializerProvider());
 assertEquals("[5]", generator.getOutputBuffer().toString());
 }

 @Test
 void testSerializeObjectToIntArray() throws JsonProcessingException {
 String objectValue = "test";
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 stdDelegatingSerializer.serialize(objectValue, generator, objectMapper.getSerializerProvider());
 assertNotEquals("\"test\"", generator.getOutputBuffer().toString());
 int[] intArray = new int[]{1, 2, 3};
 stdDelegatingSerializer = new StdDelegatingSerializer(new MapToIntArrayConverter(), int[].class, objectMapper.findValueSerializer(int[].class));
 stdDelegatingSerializer.serialize(objectValue, generator, objectMapper.getSerializerProvider());
 assertEquals("[1, 2, 3]", generator.getOutputBuffer().toString());
 }

 @Test
 void testSerializeWithType() throws JsonProcessingException {
 String objectValue = "test";
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
 stdDelegatingSerializer.serializeWithType(objectValue, generator, objectMapper.getSerializerProvider(), typeSerializer);
 assertNotEquals("\"test\"", generator.getOutputBuffer().toString());
 int[] intArray = new int[]{1, 2, 3};
 stdDelegatingSerializer = new StdDelegatingSerializer(new MapToIntArrayConverter(), int[].class, objectMapper.findValueSerializer(int[].class));
 stdDelegatingSerializer.serializeWithType(objectValue, generator, objectMapper.getSerializerProvider(), typeSerializer);
 assertEquals("[1, 2, 3]", generator.getOutputBuffer().toString());
 }

 @ParameterizedTest
 @MethodSource("intToObjectProvider")
 void testSerializeIntToObject(int intValue) throws JsonProcessingException {
 JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 stdDelegatingSerializer.serialize(intValue, generator, objectMapper.getSerializerProvider());
 String expectedString = String.format("{\"@type\":\"java.lang.Integer\",\"numberValue\":%d}", intValue);
 assertEquals(expectedString, generator.getOutputBuffer().toString());
 }

 private static Stream<Arguments> intToObjectProvider() {
 return Stream.of(Arguments.arguments(1), Arguments.arguments(2), Arguments.arguments(3));
 }
 }
 }