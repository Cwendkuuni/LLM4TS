import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.junit.Assert;
import org.junit.Test;

public class StdKeySerializersTest {

 private static final ObjectMapper mapper = new JsonMapper();

 @Test
 public void testDefaultKeySerializer() throws Exception {
 int value = 123;
 ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
 StdKeySerializers serializers = new StdKeySerializers();
 JsonSerializer<Object> serializer = serializers.getDefault();
 ObjectNode node = writer.writeValueAsObject(new Object[] { value }).get("0");
 JsonNode keyNode = node.get("0");
 JsonSerializer<Object> keySerializer = serializer.getSerializerProvider().findValueSerializer(keyNode.getClass());
 Assert.assertEquals(serializer, keySerializer);
 }

 @Test
 public void testGetStringSerializer() throws Exception {
 String key = "test";
 ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
 ObjectNode node = writer.writeValueAsObject(new Object[] { key }).get("0");
 JsonNode keyNode = node.get("0");
 JsonSerializer<Object> keySerializer = StdKeySerializers.getDefaultStringSerializer().serializeValue(null, new JsonGenerator(), new SerializerProvider());
 Assert.assertEquals(StdKeySerializers.getDefaultStringSerializer(), keySerializer);
 }

 @Test
 public void testGetStdKeySerializer() throws Exception {
 int value = 123;
 Class<Integer> integerClass = Integer.class;
 ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
 ObjectNode node = writer.writeValueAsObject(new Object[] { value, integerClass }).get("1");
 JsonNode keyNode = node.get("1");
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), keyNode.getClass(), true);
 Assert.assertNotNull(keySerializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 Integer value = 123;
 ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
 ObjectNode node = writer.writeValueAsObject(new Object[] { value }).get("0");
 JsonNode keyNode = node.get("0");
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), keyNode.getClass());
 Assert.assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, keySerializer);
 }

 @Test
 public void testDynamicKeySerializer() throws Exception {
 ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
 ObjectNode node = writer.writeValueAsObject(new Object[] { new Object() {}}).get("0");
 JsonNode keyNode = node.get("0");
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializers().getDynamic().serializeValue(null, new JsonGenerator(), new SerializerProvider());
 StdKeySerializers.Dynamic dynamicSerializer = (StdKeySerializers.Dynamic) keySerializer;
 Assert.assertNotNull(dynamicSerializer._dynamicSerializers);
 }
}