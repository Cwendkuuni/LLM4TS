package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 private HashCodeBuilder builder;

 @Test
 public void testHashCodeBuilderConstructor() {
     builder = new HashCodeBuilder();
     assertEquals(17, builder.iTotal);
     assertEquals(37, builder.iConstant);
 }

 @Test
 public void testHashCodeBuilderWithInitialAndMultiplier() {
     builder = new HashCodeBuilder(11, 31);
     assertEquals(11, builder.iTotal);
     assertEquals(31, builder.iConstant);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithEvenInitialValue() {
     try {
         new HashCodeBuilder(2, 3);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeBuilderWithEvenMultiplierValue() {
     try {
         new HashCodeBuilder(1, 2);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testHashCodeBuilderAppendBoolean() {
     builder = new HashCodeBuilder();
     builder.append(true);
     assertEquals(34, builder.iTotal);
     builder.append(false);
     assertEquals(35, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendBooleanArray() {
     builder = new HashCodeBuilder();
     boolean[] booleanArray = {true, false, true};
     builder.append(booleanArray);
     assertEquals(115, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendByte() {
     builder = new HashCodeBuilder();
     builder.append((byte)5);
     assertEquals(51, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendByteArray() {
     builder = new HashCodeBuilder();
     byte[] byteArray = {1, 2, 3};
     builder.append(byteArray);
     assertEquals(163, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendChar() {
     builder = new HashCodeBuilder();
     builder.append('a');
     assertEquals(1158675181, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendCharArray() {
     builder = new HashCodeBuilder();
     char[] charArray = {'a', 'b', 'c'};
     builder.append(charArray);
     assertEquals(477262346, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendDouble() {
     builder = new HashCodeBuilder();
     builder.append(3.14);
     assertEquals(5552331255187110511, builder.iTotal);
 }

 @Test(expected = InternalError.class)
 public void testHashCodeBuilderAppendDoubleThrowsInternalError() {
     try {
         builder.append(Double.NaN);
         fail("InternalError expected");
     } catch (InternalError e) {
         // expected
     }
 }

 @Test
 public void testHashCodeBuilderAppendDoubleArray() {
     builder = new HashCodeBuilder();
     double[] doubleArray = {1.1, 2.2, 3.3};
     builder.append(doubleArray);
     assertEquals(10988226656111780107, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendFloat() {
     builder = new HashCodeBuilder();
     builder.append((float)3.14);
     assertEquals(1118820486, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendFloatArray() {
     builder = new HashCodeBuilder();
     float[] floatArray = {1.1f, 2.2f, 3.3f};
     builder.append(floatArray);
     assertEquals(1105716537, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendInt() {
     builder = new HashCodeBuilder();
     builder.append(42);
     assertEquals(634548299, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendIntArray() {
     builder = new HashCodeBuilder();
     int[] intArray = {1, 2, 3};
     builder.append(intArray);
     assertEquals(436567332, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendLong() {
     builder = new HashCodeBuilder();
     builder.append(123L);
     assertEquals(146528811756437L, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendLongArray() {
     builder = new HashCodeBuilder();
     long[] longArray = {1, 2, 3};
     builder.append(longArray);
     assertEquals(19085679268257190394L, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendObject() {
     Object obj = new Object();
     builder = new HashCodeBuilder();
     builder.append(obj);
     assertEquals(obj.hashCode(), builder.iTotal);
 }

 @Test(expected = InternalError.class)
 public void testHashCodeBuilderAppendNullThrowsInternalError() {
     try {
         builder.append(null);
         fail("InternalError expected");
     } catch (InternalError e) {
         // expected
     }
 }

 @Test
 public void testHashCodeBuilderAppendObjectArray() {
     Object obj1 = new Object();
     Object obj2 = new Object();
     Object[] objectArray = {obj1, obj2};
     builder = new HashCodeBuilder();
     builder.append(objectArray);
     int expectedHashCode = obj1.hashCode() ^ obj2.hashCode();
     assertEquals(expectedHashCode, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendShort() {
     builder = new HashCodeBuilder();
     builder.append((short)5);
     assertEquals(51, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendShortArray() {
     builder = new HashCodeBuilder();
     short[] shortArray = {1, 2, 3};
     builder.append(shortArray);
     assertEquals(163, builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderAppendSuperHashCode() {
     Object parent = new Object();
     builder = new HashCodeBuilder();
     builder.appendSuper(parent.hashCode());
     assertEquals(parent.hashCode(), builder.iTotal);
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeBoolean() {
     Object obj = new Object();
     boolean booleanValue = true;
     assertEquals(336313459, HashCodeBuilder.reflectionHashCode(obj, booleanValue));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeBooleanArray() {
     Object obj = new Object();
     boolean[] booleanArray = {true, false, true};
     assertEquals(115, HashCodeBuilder.reflectionHashCode(obj, booleanArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeByte() {
     Object obj = new Object();
     byte byteValue = 5;
     assertEquals(51, HashCodeCodeBuilder.reflectionHashCode(obj, byteValue));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeByteArray() {
     Object obj = new Object();
     byte[] byteArray = {1, 2, 3};
     assertEquals(163, HashCodeBuilder.reflectionHashCode(obj, byteArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeChar() {
     Object obj = new Object();
     char charValue = 'a';
     assertEquals(1158675181, HashCodeBuilder.reflectionHashCode(obj, charValue));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeCharArray() {
     Object obj = new Object();
     char[] charArray = {'a', 'b', 'c'};
     assertEquals(477262346, HashCodeBuilder.reflectionHashCode(obj, charArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeDouble() {
     Object obj = new Object();
     double doubleValue = 3.14;
     assertEquals(5552331255187110511, HashCodeBuilder.reflectionHashCode(obj, doubleValue));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeDoubleArray() {
     Object obj = new Object();
     double[] doubleArray = {1.1, 2.2, 3.3};
     assertEquals(10988226656111780107, HashCodeBuilder.reflectionHashCode(obj, doubleArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeFloat() {
     Object obj = new Object();
     float floatValue = 3.14f;
     assertEquals(1118820486, HashCodeBuilder.reflectionHashCode(obj, floatValue));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeFloatArray() {
     Object obj = new Object();
     float[] floatArray = {1.1f, 2.2f, 3.3f};
     assertEquals(1105716537, HashCodeBuilder.reflectionHashCode(obj, floatArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeInt() {
     Object obj = new Object();
     int intValue = 42;
     assertEquals(634548299, HashCodeBuilder.reflectionHashCode(obj, intValue));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeIntArray() {
     Object obj = new Object();
     int[] intArray = {1, 2, 3};
     assertEquals(436567332, HashCodeBuilder.reflectionHashCode(obj, intArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeLong() {
     Object obj = new Object();
     long longValue = 123L;
     assertEquals(146528811756437L, HashCodeBuilder.reflectionHashCode(obj, longValue));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeLongArray() {
     Object obj = new Object();
     long[] longArray = {1, 2, 3};
     assertEquals(19085679268257190394L, HashCodeBuilder.reflectionHashCode(obj, longArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeObject() {
     Object obj1 = new Object();
     Object obj2 = new Object();
     Object[] objectArray = {obj1, obj2};
     assertEquals(obj1.hashCode() ^ obj2.hashCode(), HashCodeBuilder.reflectionHashCode(obj1, obj2));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeObjectArray() {
     Object obj1 = new Object();
     Object obj2 = new Object();
     Object[] objectArray = {obj1, obj2};
     assertEquals(obj1.hashCode() ^ obj2.hashCode(), HashCodeBuilder.reflectionHashCode(objectArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeShort() {
     Object obj = new Object();
     short shortValue = 5;
     assertEquals(51, HashCodeBuilder.reflectionHashCode(obj, shortValue));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeShortArray() {
     Object obj = new Object();
     short[] shortArray = {1, 2, 3};
     assertEquals(163, HashCodeBuilder.reflectionHashCode(obj, shortArray));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeWithTestTransientsFalse() {
     Object obj = new Object() {
         private boolean testTransient = true;
         public boolean getTestTransient() {
             return testTransient;
         }
     };
     assertEquals(42, HashCodeBuilder.reflectionHashCode(obj, false));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeWithTestTransientsTrue() {
     Object obj = new Object() {
         private boolean testTransient = true;
         public boolean getTestTransient() {
             return testTransient;
         }
     };
     assertEquals(43, HashCodeBuilder.reflectionHashCode(obj, true));
 }

 @Test(expected = InternalError.class)
 public void testHashCodeBuilderReflectionHashCodeWithNullThrowsInternalError() {
     try {
         HashCodeBuilder.reflectionHashCode(null, null);
         fail("InternalError expected");
     } catch (InternalError e) {
         // expected
     }
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeWithNullValue() {
     Object obj = new Object();
     assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj, null));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeWithReflectUpToClass() {
     Object obj = new MyClass();
     assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj, MyClass.class));
 }

 @Test
 public void testHashCodeBuilderReflectionHashCodeWithExcludeFields() {
     Object obj = new MyClass();
     String[] excludeFields = {"field1", "field2"};
     assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj, excludeFields));
 }

 @Test
 public void testHashCodeBuilderWithRegisteredObjects() {
     Object obj1 = new Object();
     Object obj2 = new Object();
     HashCodeBuilder builder = new HashCodeBuilder();
     builder.append(obj1);
     builder.append(obj2);
     Set<IDKey> registry = HashCodeBuilder.getRegistry();
     assertTrue(registry.contains(new IDKey(obj1)));
     assertTrue(registry.contains(new IDKey(obj2)));
 }

 @Test
 public void testHashCodeBuilderWithRegisteredObjectsAndUnregisteredObjects() {
     Object obj1 = new Object();
     Object obj2 = new Object();
     HashCodeBuilder builder = new HashCodeBuilder();
     builder.append(obj1);
     builder.append(obj2);
     Set<IDKey> registry = HashCodeBuilder.getRegistry();
     registry.remove(new IDKey(obj1));
     assertFalse(registry.contains(new IDKey(obj1)));
     assertTrue(registry.contains(new IDKey(obj2)));
 }
}