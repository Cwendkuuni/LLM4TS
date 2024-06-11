import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NumberSerializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws Exception {
 BigDecimal number = new BigDecimal("123.45");
 NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("123.45", json);
 }

 @Test
 public void testSerializeBigInteger() throws Exception {
 BigInteger number = BigInteger.valueOf(1234567890123L);
 NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("1234567890123", json);
 }

 @Test
 public void testSerializeInteger() throws Exception {
 Integer number = 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("123", json);
 }

 @Test
 public void testSerializeLong() throws Exception {
 Long number = 1234567890123L;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("1234567890123", json);
 }

 @Test
 public void testSerializeDouble() throws Exception {
 Double number = 123.45;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("123.45", json);
 }

 @Test
 public void testSerializeFloat() throws Exception {
 Float number = 123.45F;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("123.45", json);
 }

 @Test
 public void testSerializeByte() throws Exception {
 Byte number = (byte) 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("123", json);
 }

 @Test
 public void testSerializeShort() throws Exception {
 Short number = (short) 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("123", json);
 }

 @Test
 public void testSerializeString() throws Exception {
 String number = "invalid";
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String json = mapper.enabledApi(SerializationFeature.FAIL_ON_EMPTY_BEANS)
 .writeValueAsString(numberSerializer, number);
 assertNotNull(json);
 assertEquals("\"invalid\"", json);
 }
}