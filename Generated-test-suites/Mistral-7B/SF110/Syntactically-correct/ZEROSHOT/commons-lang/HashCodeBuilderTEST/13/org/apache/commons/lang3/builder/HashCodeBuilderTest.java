import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import java.lang.reflect.*;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class HashCodeBuilderTest {

 private HashCodeBuilder builder;
 private Object testObject;

 @Before
 public void setup() {
 testObject = new Object() {};
 MockitoAnnotations.initMocks(this);
 PowerMockito.mockStatic(HashCodeBuilder.class);
 builder = new HashCodeBuilder();
 }

 @Test
 public void testConstructor() {
 final HashCodeBuilder instance = new HashCodeBuilder();
 assertNotNull(instance);
 }

 @Test
 public void testHashCodeWithNullObject() {
 final int result = builder.reflectionHashCode(null, 17, 37);
 assertEquals(-1, result);
 }

 @Test
 public void testHashCodeWithObject() {
 final int result = builder.reflectionHashCode(testObject, 17, 37);
 final HashCodeBuilder mockBuilder = mock(HashCodeBuilder.class);
 when(mockBuilder.toHashCode()).thenReturn(42);
 PowerMockito.whenNew(HashCodeBuilder.class).withArguments(17, 37).thenReturn(mockBuilder);
 final int expected = 42;
 PowerMockito.doAnswer(invocation -> {
 final Object object = invocation.getArgument(2);
 builder.append(object);
 return null;
 }).when(mockBuilder).append(anyObject());
 final int actual = builder.reflectionHashCode(testObject, 17, 37);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfBoolean() {
 final boolean[] booleanArray = new boolean[] { true, false };
 final int expected = 31 * (37 + 1) + 1 + 31 * (37 + 1) + 0;
 final int actual = builder.reflectionHashCode(booleanArray, 17, 37, false, Boolean[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfByte() {
 final byte[] byteArray = new byte[] { 1, 2 };
 final int expected = 31 * (37 + 1) + 1 + 31 * (37 + 1) + 2;
 final int actual = builder.reflectionHashCode(byteArray, 17, 37, false, Byte[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfChar() {
 final char[] charArray = new char[] { 'a', 'b' };
 final int expected = 31 * (37 + 1) + 'a' + 31 * (37 + 1) + 'b';
 final int actual = builder.reflectionHashCode(charArray, 17, 37, false, Char[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfDouble() {
 final double[] doubleArray = new double[] { 1.0, 2.0 };
 final int expected = 31 * (37 + 1) + Double.doubleToIntBits(1.0) + 31 * (37 + 1) + Double.doubleToIntBits(2.0);
 final int actual = builder.reflectionHashCode(doubleArray, 17, 37, false, Double[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfFloat() {
 final float[] floatArray = new float[] { 1.0f, 2.0f };
 final int expected = 31 * (37 + 1) + Float.floatToIntBits(1.0f) + 31 * (37 + 1) + Float.floatToIntBits(2.0f);
 final int actual = builder.reflectionHashCode(floatArray, 17, 37, false, Float[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfInt() {
 final int[] intArray = new int[] { 1, 2 };
 final int expected = 31 * (37 + 1) + 1 + 31 * (37 + 1) + 2;
 final int actual = builder.reflectionHashCode(intArray, 17, 37, false, Int[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfLong() {
 final long[] longArray = new long[] { 1L, 2L };
 final int expected = 31 * (37 + 1) + (int)(1L ^ (1L >> 32)) + 31 * (37 + 1) + (int)(2L ^ (2L >> 32));
 final int actual = builder.reflectionHashCode(longArray, 17, 37, false, Long[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfObject() {
 final Object[] objectArray = new Object[] { new Object() {}, new Object() {} };
 final int expected = 31 * (37 + 1) + objectArray[0].hashCode() + 31 * (37 + 1) + objectArray[1].hashCode();
 final int actual = builder.reflectionHashCode(objectArray, 17, 37, false, Object[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfShort() {
 final short[] shortArray = new short[] { 1, 2 };
 final int expected = 31 * (37 + 1) + 1 + 31 * (37 + 1) + 2;
 final int actual = builder.reflectionHashCode(shortArray, 17, 37, false, Short[].class, new String[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithArrayOfBooleanExcludingField() {
 final boolean[] booleanArray = new boolean[] { true, false };
 final String[] excludedFields = new String[]{"fieldName"};
 final int expected = 31 * (37 + 1) + 1;
 final int actual = builder.reflectionHashCode(booleanArray, 17, 37, false, Boolean[].class, excludedFields);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithObjectExcludingField() {
 final Object testObjectWithField = new Object() {
 @SuppressWarnings("unused")
 private String fieldName;
 };
 final int expected = 31 * (37 + 1) + testObjectWithField.hashCode();
 final int actual = builder.reflectionHashCode(testObjectWithField, false, new String[]{"fieldName"});
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCodeWithObjectSuperHashCode() {
 final Object superClassObject = new Object() {};
 final Object subClassObject = new Object() {
 @Override
 public int hashCode() {
 return super.hashCode();
 }
 };
 final int superHashCode = 42;
 final int expected = 42 * 37 + superHashCode;
 final int actual = builder.reflectionHashCode(subClassObject, 37, 37, false, null, new String[0]);
 assertEquals(expected, actual);
 }
}