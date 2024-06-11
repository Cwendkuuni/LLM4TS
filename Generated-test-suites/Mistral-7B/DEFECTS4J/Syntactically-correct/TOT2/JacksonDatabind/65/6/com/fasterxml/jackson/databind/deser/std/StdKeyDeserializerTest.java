import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 // Test for the constructor with int kind and Class<?> cls
 @Test
 public void testStdKeyDeserializerConstruction() {
 StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
 assertEquals("Checking kind and keyClass", 1, keyDeserializer.getKind());
 assertEquals("Checking keyClass", String.class, keyDeserializer.getKeyClass());
 }

 // Test for deserializeKey method with valid boolean key
 @Test
 public void testDeserializeKeyWithValidBooleanKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey("true", null);
 assertTrue("Checking result is Boolean.TRUE", result instanceof Boolean);
 assertEquals("Checking result value", true, ((Boolean) result).booleanValue());
 }

 // Test for deserializeKey method with valid byte key
 @Test
 public void testDeserializeKeyWithValidByteKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey("127", null);
 assertTrue("Checking result is Byte", result instanceof Byte);
 assertEquals("Checking result value", (byte) 127, ((Byte) result).byteValue());
 }

 // Test for deserializeKey method with valid char key
 @Test
 public void testDeserializeKeyWithValidCharKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey("A", null);
 assertTrue("Checking result is Character", result instanceof Character);
 assertEquals("Checking result value", 'A', ((Character) result).charValue());
 }

 // Test for deserializeKey method with valid int key
 @Test
 public void testDeserializeKeyWithValidIntKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class).deserializeKey("42", null);
 assertTrue("Checking result is Integer", result instanceof Integer);
 assertEquals("Checking result value", 42, ((Integer) result).intValue());
 }

 // Test for deserializeKey method with valid long key
 @Test
 public void testDeserializeKeyWithValidLongKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class).deserializeKey("9223372036854775807", null);
 assertTrue("Checking result is Long", result instanceof Long);
 assertEquals("Checking result value", 9223372036854775807L, ((Long) result).longValue());
 }

 // Test for deserializeKey method with valid float key
 @Test
 public void testDeserializeKeyWithValidFloatKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class).deserializeKey("3.14159265", null);
 assertTrue("Checking result is Float", result instanceof Float);
 assertEquals("Checking result value", 3.14159265f, ((Float) result).floatValue(), 0.0001);
 }

 // Test for deserializeKey method with valid double key
 @Test
 public void testDeserializeKeyWithValidDoubleKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class).deserializeKey("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596423377930041312217131494142247171352960280055111212902193037614211881609768443765675814233791484007149288267333432471332311825593600112128651414145274487139217171676059142231110000000000001D", null);
 assertTrue("Checking result is Double", result instanceof Double);
 assertEquals("Checking result value", 3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596423377930041312217131494142247171352960280055111212902193037614211881609768443765675814233791484007149288267333432471332311825593600112128651414145274487139217171676059142231110000000000001d, ((Double) result).doubleValue(), 0.000001);
 }

 // Test for deserializeKey method with valid URI key
 @Test
 public void testDeserializeKeyWithValidURIKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class).deserializeKey("https://www.example.com", null);
 assertTrue("Checking result is URI", result instanceof URI);
 assertEquals("Checking result value", "https://www.example.com", result.toString());
 }

 // Test for deserializeKey method with valid URL key
 @Test
 public void testDeserializeKeyWithValidURLKey() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class).deserializeKey("http://www.example.com", null);
 assertTrue("Checking result is URL", result instanceof URL);
 assertEquals("Checking result value", "http://www.example.com", result.toString());
 }

 // Test for deserializeKey method with valid Class key
 @Test
 public void testDeserializeKeyWithValidClassKey() throws IOException, ClassNotFoundException {
 Class<?> result = (Class<?>) new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class).deserializeKey("java.lang.String", null);
 assertEquals("Checking result value", String.class, result);
 }

 // Test for deserializeKey method with invalid key
 @Test(expected = IOException.class)
 public void testDeserializeKeyWithInvalidKey() throws IOException {
 new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class).deserializeKey("invalid_key", null);
 }

 // Test for deserializeKey method with null key
 @Test(expected = IOException.class)
 public void testDeserializeKeyWithNullKey() throws IOException {
 new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class).deserializeKey(null, null);
 }

 // Test for deserializeKey method with key that cannot be parsed as any supported type
 @Test(expected = IOException.class)
 public void testDeserializeKeyWithUnsupportedKey() throws IOException {
 new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class).deserializeKey("[1, 2, 3]", null);
 }

 // Test for deserializeKey method with key that is nullable Enum and is deserialized as null
 @Test
 public void testDeserializeKeyWithNullableEnumKeyAsNull() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey(null, null);
 assertNull("Checking result is null for nullable Enum", result);
 }

 // Test for deserializeKey method with key that is nullable Enum and is deserialized as non-null value
 @Test
 public void testDeserializeKeyWithNullableEnumKeyAsValue() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey("en_US", null);
 assertEquals("Checking result value", Locale.US, result);
 }

 // Test for deserializeKey method with key that is non-nullable Enum and is deserialized as null
 @Test(expected = IOException.class)
 public void testDeserializeKeyWithNonNullableEnumKeyAsNull() throws IOException {
 new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey(null, null);
 }

 // Test for deserializeKey method with key that is non-nullable Enum and is deserialized as non-null value
 @Test
 public void testDeserializeKeyWithNonNullableEnumKeyAsValue() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class).deserializeKey("en_US", null);
 assertEquals("Checking result value", Locale.US, result);
 }

 // Test for deserializeKey method with key that is not an Enum and is deserialized as non-null value
 @Test
 public void testDeserializeKeyWithNonEnumKeyAsValue() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Object.class).deserializeKey("ExampleKey", null);
 assertNotNull("Checking result is not null", result);
 }

 // Test for deserializeKey method with key that is not an Enum and is deserialized as null
 @Test
 public void testDeserializeKeyWithNonEnumKeyAsNull() throws IOException {
 Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Object.class).deserializeKey(null, null);
 assertNull("Checking result is null", result);
 }
}