import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 // Test for getStdKeySerializer method with null config, null rawKeyType, and useDefault set to true
 @Test
 public void testGetStdKeySerializerNullConfigNullRawKeyTypeTrue() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, true);
 assertNotNull(serializer);
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 // Test for getStdKeySerializer method with non-null config, null rawKeyType, and useDefault set to false
 @Test(expected = NullPointerException.class)
 public void testGetStdKeySerializerNullRawKeyTypeFalse() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), null, false);
 }

 // Test for getStdKeySerializer method with non-null config, String rawKeyType, and useDefault set to false
 @Test
 public void testGetStdKeySerializerStringRawKeyTypeFalse() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), String.class, false);
 assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
 }

 // Test for getStdKeySerializer method with non-null config, primitive type rawKeyType, and useDefault set to false
 @Test
 public void testGetStdKeySerializerPrimitiveTypeRawKeyTypeFalse() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), int.class, false);
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 // Test for getStdKeySerializer method with non-null config, Class rawKeyType, and useDefault set to false
 @Test
 public void testGetStdKeySerializerClassRawKeyTypeFalse() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), String[].class, false);
 assertNull(serializer);
 }

 // Test for getStdKeySerializer method with non-null config, Date rawKeyType, and useDefault set to false
 @Test
 public void testGetStdKeySerializerDateRawKeyTypeFalse() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Date.class, false);
 assertSame(StdKeySerializers.getDefault(Default.TYPE_DATE, Date.class), serializer);
 }

 // Test for getStdKeySerializer method with non-null config, Calendar rawKeyType, and useDefault set to false
 @Test
 public void testGetKeySerializerCalendarRawKeyTypeFalse() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), Calendar.class, false);
 assertSame(StdKeySerializers.getDefault(Default.TYPE_CALENDAR, Calendar.class), serializer);
 }

 // Test for getStdKeySerializer method with non-null config, UUID rawKeyType, and useDefault set to false
 @Test
 public void testGetStdKeySerializerUuidRawKeyTypeFalse() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(new SerializationConfig(), UUID.class, false);
 assertSame(StdKeySerializers.getDefault(Default.TYPE_TO_STRING, UUID.class), serializer);
 }

 // Test for getFallbackKeySerializer method with null config and null rawKeyType
 @Test
 public void testGetFallbackKeySerializerNullConfigNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 // Test for getFallbackKeySerializer method with null config and non-null rawKeyType
 @Test(expected = NullPointerException.class)
 public void testGetFallbackKeySerializerNullConfigNonNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, String.class);
 }

 // Test for getFallbackKeySerializer method with non-null config and null rawKeyType
 @Test(expected = NullPointerException.class)
 public void testGetFallbackKeySerializerNonNullConfigNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), null);
 }

 // Test for getFallbackKeySerializer method with non-null config and non-enum rawKeyType
 @Test
 public void testGetFallbackKeySerializerNonNullConfigNonEnumRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), String.class);
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 // Test for getFallbackKeySerializer method with non-null config and enum rawKeyType
 @Test
 public void testGetFallbackKeySerializerNonNullConfigEnumRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(new SerializationConfig(), Enum.class);
 assertSame(StdKeySerializers.getDefault(Default.TYPE_ENUM, Enum.class), serializer);
 }

 // Test for getDefault method
 @Test
 public void testGetDefault() {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
 }

 // Test for Default class with TYPE_DATE
 @Test
 public void testDefaultTypeDate() throws JsonGenerationException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault(Default.TYPE_DATE, Date.class);
 ObjectWriter writer = mapper.writerWithView(new SimpleObjectMapper.SerializerProvider());
 String json = writer.writeValueAsString(new Date());
 JsonParser parser = mapper.parser();
 parser.readValueAs(Date.class);
 JsonGenerator generator = writer.getGenerator();
 serializer.serialize(new Date(), generator, mapper.getSerializerProvider());
 assertEquals(json, generator.getOutputValueAsString());
 }

 // Test for Default class with TYPE_CALENDAR
 @Test
 public void testDefaultTypeCalendar() throws JsonGenerationException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault(Default.TYPE_CALENDAR, Calendar.class);
 ObjectWriter writer = mapper.writerWithView(new SimpleObjectMapper.SerializerProvider());
 String json = writer.writeValueAsString(new GregorianCalendar(2022, Calendar.JANUARY, 1).getTimeInMillis());
 JsonParser parser = mapper.parser();
 parser.readValueAs(Calendar.class);
 JsonGenerator generator = writer.getGenerator();
 serializer.serialize(new GregorianCalendar(2022, Calendar.JANUARY, 1), generator, mapper.getSerializerProvider());
 assertEquals(json, generator.getOutputValueAsString());
 }

 // Test for Dynamic class
 @Test
 public void testDynamicClass() throws JsonGenerationException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> serializer = StdKeySerializers.DEFAULT_KEY_SERIALIZER;
 ObjectWriter writer = mapper.writerWithView(new SimpleObjectMapper.SerializerProvider());
 String json = writer.writeValueAsString("test");
 JsonParser parser = mapper.parser();
 parser.readValueAs(String.class);
 JsonGenerator generator = writer.getGenerator();
 serializer.serialize("test", generator, mapper.getSerializerProvider());
 assertEquals(json, generator.getOutputValueAsString());
 Class<?> cls = Integer.TYPE;
 PropertySerializerMap map = new PropertySerializerMap();
 JsonSerializer<Object> dynamicSerializer = mapper.findValueSerializer(cls);
 serializer = mapper.findValueSerializer(cls);
 JsonSerializer<Object> dynamicSerializerAfter = StdKeySerializers.Dynamic._findAndAddDynamic(map, cls, mapper.getSerializerProvider());
 assertEquals(dynamicSerializerAfter, serializer);
 }
}