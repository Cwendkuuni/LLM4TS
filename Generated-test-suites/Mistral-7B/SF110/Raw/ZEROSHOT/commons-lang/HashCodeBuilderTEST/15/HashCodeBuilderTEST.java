import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

 private HashCodeBuilder builder;
 private Object testObject;

 @Before
 public void setup() {
 testObject = new Object() {{}};
 builder = new HashCodeBuilder();
 }

 @Test
 public void testHashCodeBuilderConstruction() {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 HashCodeBuilder hashCodeBuilderWithParams = new HashCodeBuilder(17, 37);

 assertEquals(17, hashCodeBuilder.getHashCode());
 assertEquals(37, hashCodeBuilderWithParams.getHashCode());
 }

 @Test
 public void testAppendBoolean() {
 boolean booleanValue = true;
 builder.append(booleanValue);
 int hashCode = builder.toHashCode();

 Boolean testBoolean = new Boolean(booleanValue);
 int testHashCode = testBoolean.hashCode();
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(booleanValue);
 int hashCodeBuilderHashCode = hashCodeBuilder.toHashCode();

 assertEquals(hashCode, hashCodeBuilderHashCode);
 assertEquals(hashCode, testHashCode);
 }

 @Test
 public void testAppendByte() {
 byte byteValue = 1;
 builder.append(byteValue);
 int hashCode = builder.toHashCode();

 Byte testByte = new Byte(byteValue);
 int testHashCode = testByte.hashCode();
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(byteValue);
 int hashCodeBuilderHashCode = hashCodeBuilder.toHashCode();

 assertEquals(hashCode, hashCodeBuilderHashCode);
 assertEquals(hashCode, testHashCode);
 }

 @Test
 public void testAppendShort() {
 short shortValue = 1;
 builder.append(shortValue);
 int hashCode = builder.toHashCode();

 Short testShort = new Short(shortValue);
 int testHashCode = testShort.hashCode();
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(shortValue);
 int hashCodeBuilderHashCode = hashCodeBuilder.toHashCode();

 assertEquals(hashCode, hashCodeBuilderHashCode);
 assertEquals(hashCode, testHashCode);
 }

 @Test
 public void testAppendInt() {
 int intValue = 1;
 builder.append(intValue);
 int hashCode = builder.toHashCode();

 Integer testInt = new Integer(intValue);
 int testHashCode = testInt.hashCode();
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(intValue);
 int hashCodeBuilderHashCode = hashCodeBuilder.toHashCode();

 assertEquals(hashCode, hashCodeBuilderHashCode);
 assertEquals(hashCode, testHashCode);
 }

 @Test
 public void testAppendLong() {
 long longValue = 1L;
 builder.append(longValue);
 int hashCode = builder.toHashCode();

 Long testLong = new Long(longValue);
 int testHashCode = testLong.hashCode();
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(longValue);
 int hashCodeBuilderHashCode = hashCodeBuilder.toHashCode();

 assertEquals(hashCode, hashCodeBuilderHashCode);
 assertEquals(hashCode, testHashCode);
 }

 @Test
 public void testAppendChar() {
 char charValue = 'a';
 builder.append(charValue);
 int hashCode = builder.toHashCode();

 Character testChar = new Character(charValue);
 int testHashCode = testChar.hashCode();
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(charValue);
 int hashCodeBuilderHashCode = hashCodeBuilder.toHashCode();

 assertEquals(hashCode, hashCodeBuilderHashCode);
 assertEquals(hashCode, testHashCode);
 }

 @Test
 public void testAppendString() {
 String stringValue = "test";
 builder.append(stringValue);
 int hashCode = builder.toHashCode();

 assertEquals(hashCode, stringValue.hashCode());
 }

 @Test
 public void testReflectionHashCode() {
 int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject);
 int builderHashCode = builder.toHashCode();

 assertEquals(reflectionHashCode, builderHashCode);
 }

 @Test
 public void testRegistration() {
 Set<IDKey> registry = HashCodeBuilder.getRegistry();
 Object testObject2 = new Object() {{}};
 HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder();
 hashCodeBuilder2.append(testObject2);

 assertTrue(registry != null);
 assertTrue(registry.contains(new IDKey(testObject)));
 assertTrue(registry.contains(new IDKey(testObject2)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppenderNullObject() {
 builder.append(null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppenderNullArray() {
 builder.append((Object[]) null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppenderArrayWithNullElement() {
 boolean[] array = {true, null};
 builder.append(array);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testAppenderArrayWithElements() {
 boolean[] array = {true, false};
 builder.append(array);
 int hashCode = builder.toHashCode();

 Boolean[] testArray = {new Boolean(true), new Boolean(false)};
 int testHashCode = Arrays.hashCode(testArray);
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(array);
 int hashCodeBuilderHashCode = hashCodeBuilder.toHashCode();

 assertEquals(hashCode, hashCodeBuilderHashCode);
 assertEquals(hashCode, testHashCode);
 }

 @Test(expected = IllegalAccessException.class)
 public void testReflectionAppendIllegalAccessException() {
 try {
 Class<?> clazz = testObject.getClass();
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(testObject, clazz, hashCodeBuilder, true, null, new String[0]);
 fail("Expected IllegalAccessException");
 } catch (IllegalAccessException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionAppendNullPointerException() {
 try {
 HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
 hashCodeBuilder.append(null, null, hashCodeBuilder, true, null, new String[0]);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}