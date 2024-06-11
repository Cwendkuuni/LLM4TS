import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void init() throws JsonProcessingException {
 mapper = new ObjectMapper();
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is not provided.
 */
 @Test
 public void testGetStdKeySerializerWithNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is String.
 */
 @Test
 public void testGetStdKeySerializerWithStringRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is Number.
 */
 @Test
 public void testGetStdKeySerializerWithNumberRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, int.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is Class.
 */
 @Test
 public void testGetStdKeySerializerWithClassRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Class.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is Date.
 */
 @Test
 public void testGetStdKeySerializerWithDateRawKeyType() throws JsonGenerationException {
 Date date = new Date();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Date.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, generator, mapper.getSerializerProvider());
 assertNotNull(generator.getCurrentOutputValue());
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is Enum.
 */
 @Test
 public void testGetStdKeySerializerWithEnumRawKeyType() {
 enum TestEnum { TEST }
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, TestEnum.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is UUID.
 */
 @Test
 public void testGetStdKeySerializerWithUUIDRawKeyType() {
 java.util.UUID uuid = java.util.UUID.randomUUID();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, uuid.getClass(), true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 /**
 * Test for getStdKeySerializer method when rawKeyType is not found.
 */
 @Test(expected = NullPointerException.class)
 public void testGetStdKeySerializerWithUnknownRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, MyUnknownClass.class, true);
 fail("Should have returned null");
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is not provided.
 */
 @Test
 public void testGetFallbackKeySerializerWithNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is String.
 */
 @Test
 public void testGetFallbackKeySerializerWithStringRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, String.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is Number.
 */
 @Test
 public void testGetFallbackKeySerializerWithNumberRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, int.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is Class.
 */
 @Test
 public void testGetFallbackKeySerializerWithClassRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Class.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is Date.
 */
 @Test
 public void testGetFallbackKeySerializerWithDateRawKeyType() throws JsonGenerationException {
 Date date = new Date();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Date.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, generator, mapper.getSerializerProvider());
 assertNotNull(generator.getCurrentOutputValue());
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is Enum.
 */
 @Test
 public void testGetFallbackKeySerializerWithEnumRawKeyType() {
 enum TestEnum { TEST }
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, TestEnum.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is UUID.
 */
 @Test
 public void testGetFallbackKeySerializerWithUUIDRawKeyType() {
 java.util.UUID uuid = java.util.UUID.randomUUID();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, uuid.getClass());
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 /**
 * Test for getFallbackKeySerializer method when rawKeyType is not found.
 */
 @Test(expected = NullPointerException.class)
 public void testGetFallbackKeySerializerWithUnknownRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, MyUnknownClass.class);
 fail("Should have returned null");
 }

 /**
 * Test for Dynamic key serializer.
 */
 @Test
 public void testDynamicKeySerializer() throws JsonGenerationException {
 Object object = new Object() {
 @Override
 public String toString() {
 return "DynamicKeySerializerTest";
 }
 };
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(object, generator, mapper.getSerializerProvider());

 assertEquals("DynamicKeySerializerTest", generator.getCurrentOutputValue());
 }
}