import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StdKeySerializersTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testGetStdKeySerializer() throws JsonProcessingException {
 ObjectSerializer serializer = StdKeySerializers.getStdKeySerializer(
 mapper.getSerializationConfig(), String.class, false);
 String key = "test";
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(key, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 Assert.assertEquals("\"test\"", generator.getOutputBuffer().toString());
 generator.close();
 }

 @Test
 public void testGetStdKeySerializer_Date() throws JsonProcessingException {
 ObjectSerializer serializer = StdKeySerializers.getStdKeySerializer(
 mapper.getSerializationConfig(), Date.class, false);
 Date date = new Date();
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(date, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 Assert.assertEquals("\"Fri Aug 12 13:24:56 ICT 2023\"", generator.getOutputBuffer().toString());
 generator.close();
 }

 @Test
 public void testGetStdKeySerializer_Calendar() throws JsonProcessingException {
 ObjectSerializer serializer = StdKeySerializers.getStdKeySerializer(
 mapper.getSerializationConfig(), Calendar.class, false);
 Calendar calendar = Calendar.getInstance();
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(calendar, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 Assert.assertEquals("\"Fri Aug 12 13:24:56 ICT 2023\"", generator.getOutputBuffer().toString());
 generator.close();
 }

 @Test
 public void testGetStdKeySerializer_Class() throws JsonProcessingException {
 ObjectSerializer serializer = StdKeySerializers.getStdKeySerializer(
 mapper.getSerializationConfig(), Class.class, false);
 Class<?> clazz = String.class;
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(clazz, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 Assert.assertEquals("\"class java.lang.String\"", generator.getOutputBuffer().toString());
 generator.close();
 }

 @Test
 public void testGetStdKeySerializer_Enum() throws JsonProcessingException {
 enum TestEnum {
 ONE, TWO, THREE
 }
 ObjectSerializer serializer = StdKeySerializers.getStdKeySerializer(
 mapper.getSerializationConfig(), TestEnum.class, false);
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(TestEnum.ONE, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 Assert.assertEquals("\"ONE\"", generator.getOutputBuffer().toString());
 generator.close();
 }

 @Test
 public void testGetStdKeySerializer_UnknownType() throws JsonProcessingException {
 ObjectSerializer serializer = StdKeySerializers.getStdKeySerializer(
 mapper.getSerializationConfig(), Integer.TYPE, false);
 Assert.assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonProcessingException {
 ObjectSerializer serializer = StdKeySerializers.getFallbackKeySerializer(
 mapper.getSerializationConfig(), Integer.TYPE);
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(Integer.valueOf(1), generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 Assert.assertEquals("null", generator.getOutputBuffer().toString());
 generator.close();
 }

 @Test
 public void testGetDefault() throws JsonProcessingException {
 ObjectSerializer serializer = StdKeySerializers.getDefault();
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(null, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 Assert.assertEquals("null", generator.getOutputBuffer().toString());
 generator.close();
 }

 @Test
 public void testDynamicKeySerializer() throws JsonProcessingException {
 Object obj = new Object() {
 @Override
 public String toString() {
 return "dynamicKey";
 }
 };
 PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
 ObjectSerializer serializer = map.getSerializerProvider().findValueSerializer(Object.class, map);
 JsonGenerator generator = mapper.getFactory().createGenerator();
 serializer.serialize(obj, generator, mapper.getSerializationConfig().getDefaultSerializerProvider());
 Assert.assertEquals("\"dynamicKey\"", generator.getOutputBuffer().toString());
 generator.close();
 }
}