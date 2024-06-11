import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 // Test case for getStdKeySerializer method with valid arguments
 @Test
 public void testGetStdKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Class<Integer> intType = Integer.class;
 Class<String> stringType = String.class;

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), intType, true);
 assertNotNull(serializer);
 assertEquals(Default.TYPE_NUMBER, serializer.getClass().getField("_typeId").getInt(serializer));

 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), stringType, true);
 assertNotNull(serializer);
 assertEquals(StringKeySerializer.class, serializer.getClass());
 }

 // Test case for getStdKeySerializer method with null arguments
 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithNullArguments() {
 StdKeySerializers.getStdKeySerializer(null, null, true);
 }

 // Test case for getStdKeySerializer method with null rawKeyType
 @Test(expected = IllegalArgumentException.class)
 public void testGetStdKeySerializerWithNullRawKeyType() {
 StdKeySerializers.getStdKeySerializer(new ObjectMapper().getSerializationConfig(), null, true);
 }

 // Test case for getStdKeySerializer method with Object.class as rawKeyType
 @Test
 public void testGetStdKeySerializerWithObjectRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Object.class, true);
 assertNotNull(serializer);
 assertEquals(Dynamic.class, serializer.getClass());
 }

 // Test case for getStdKeySerializer method with String.class as rawKeyType
 @Test
 public void testGetStdKeySerializerWithStringRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, true);
 assertNotNull(serializer);
 assertEquals(StringKeySerializer.class, serializer.getClass());
 }

 // Test case for getStdKeySerializer method with Number.class as rawKeyType
 @Test
 public void testGetStdKeySerializerWithNumberRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Number.class, true);
 assertNotNull(serializer);
 assertEquals(Default.TYPE_NUMBER, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 // Test case for getStdKeySerializer method with Class.class as rawKeyType
 @Test
 public void testGetStdKeySerializerWithClassRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, true);
 assertNotNull(serializer);
 assertEquals(Default.TYPE_CLASS, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 // Test case for getStdKeySerializer method with Date.class as rawKeyType
 @Test
 public void testGetStdKeySerializerWithDateRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, true);
 assertNotNull(serializer);
 assertEquals(Default.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 // Test case for getStdKeySerializer method with Calendar.class as rawKeyType
 @Test
 public void testGetStdKeySerializerWithCalendarRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, true);
 assertNotNull(serializer);
 assertEquals(Default.TYPE_CALENDAR, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 // Test case for getStdKeySerializer method with UUID.class as rawKeyType
 @Test
 public void testGetStdKeySerializerWithUUIDRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), UUID.class, true);
 assertNotNull(serializer);
 assertEquals(Default.TYPE_TO_STRING, serializer.getClass().getField("_typeId").getInt(serializer));
 }

 // Test case for getFallbackKeySerializer method with valid arguments
 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Class<Integer> intType = Integer.class;

 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), intType);
 assertNotNull(serializer);
 assertEquals(Dynamic.class, serializer.getClass());
 }

 // Test case for getFallbackKeySerializer method with null rawKeyType
 @Test(expected = IllegalArgumentException.class)
 public void testGetFallbackKeySerializerWithNullRawKeyType() {
 StdKeySerializers.getFallbackKeySerializer(new ObjectMapper().getSerializationConfig(), null);
 }

 // Test case for getFallbackKeySerializer method with Enum.class as rawKeyType
 @Test
 public void testGetFallbackKeySerializerWithEnumRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Enum.class);
 assertNotNull(serializer);
 assertEquals(Dynamic.class, serializer.getClass());
 }

 // Test case for getFallbackKeySerializer method with non-Enum class as rawKeyType
 @Test
 public void testGetFallbackKeySerializerWithNonEnumRawKeyType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Class<String> stringType = String.class;

 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), stringType);
 assertNotNull(serializer);
 assertEquals(Default.class, serializer.getClass());
 }

 // Test case for getFallbackKeySerializer method with null serializationConfig
 @Test(expected = IllegalArgumentException.class)
 public void testGetFallbackKeySerializerWithNullSerializationConfig() {
 StdKeySerializers.getFallbackKeySerializer(null, Number.class);
 }

 // Test case for getDefault method (deprecated)
 @Test
 public void testGetDefault() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertNotNull(serializer);
 assertEquals(Default.class, serializer.getClass());
 }

 // Test case for Dynamic key serializer
 @Test
 public void testDynamicKeySerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object obj = new Object() {
 @Override
 public String toString() {
 return "dynamic";
 }
 };

 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 serializer.serialize(obj, generator, mapper.getSerializationConfig());
 String result = generator.getOutputBuffer().toString();
 assertEquals("dynamic", result);
 }

 // Test case for Dynamic key serializer with custom serializer
 @Test
 public void testDynamicKeySerializerWithCustomSerializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 PropertySerializerMap dynamicSerializers = PropertySerializerMap.emptyForProperties();
 JsonSerializer<Object> customSerializer = new StdSerializer<Object>(String.class) {
 @Override
 public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
 g.writeFieldName("custom");
 }
 };
 dynamicSerializers.put("customType", customSerializer);
 JsonSerializer<Object> serializer = new Dynamic() {
 protected PropertySerializerMap _dynamicSerializers = dynamicSerializers;
 };
 Object obj = new Object() {
 @Override
 public String toString() {
 return "custom";
 }
 };

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
 serializer.serialize(obj, generator, mapper.getSerializationConfig());
 String result = generator.getOutputBuffer().toString();
 assertEquals("custom", result);
 }
}