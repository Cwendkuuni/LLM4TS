import org.apache.commons.lang3.*;
import org.junit.Assert;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;
 private Comparator<?> comparator;
 private Collection<String> excludeFields;

 @Test
 public void testReflectionCompare() {
 lhs = new Object() {
 };
 rhs = new Object() {
 };
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 Assert.assertEquals(0, comparison);

 lhs = new Integer(1);
 rhs = new Integer(2);
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 Assert.assertEquals(-1, comparison);

 lhs = new Integer(2);
 rhs = new Integer(1);
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 Assert.assertEquals(1, comparison);
 }

 @Test
 public void testReflectionCompareWithExcludeFields() {
 lhs = new ClassWithExcludedFields() {
 };
 rhs = new ClassWithExcludedFields() {
 };
 excludeFields = Arrays.asList("field1", "field2");
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, excludeFields);
 Assert.assertEquals(0, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompareWithNull() {
 lhs = null;
 rhs = new Object() {
 };
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompareWithDifferentClasses() {
 lhs = new Integer(1);
 rhs = new String("1");
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testReflectionCompareWithSuperClass() {
 ClassWithSuperclass superclass = new ClassWithSuperclass();
 lhs = new ClassWithSuperclass() {
 };
 rhs = new ClassWithSuperclass() {
 };
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, superclass.getClass());
 Assert.assertEquals(0, comparison);
 }

 @Test
 public void testAppend() {
 lhs = new Object() {
 int field1 = 1;
 int field2 = 2;
 };
 rhs = new Object() {
 int field1 = 2;
 int field2 = 1;
 };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 lhs = new Object() {
 int field1 = 1;
 int field2 = 1;
 };
 rhs = new Object() {
 int field1 = 1;
 int field2 = 1;
 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 lhs = new Object() {
 int field1 = 1;
 int field2 = 2;
 };
 rhs = new Object() {
 int field1 = 2;
 int field2 = 3;
 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithComparator() {
 lhs = new Integer(1);
 rhs = new Integer(2);
 comparator = new Comparator<Object>() {
 @Override
 public int compare(Object o1, Object o2) {
 return ((Integer)o1).compareTo((Integer)o2) * -1;
 }
 };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs, comparator);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(1, comparison);
 }

 @Test
 public void testAppendWithArrays() {
 int[] lhs = { 1, 2, 3 };
 int[] rhs = { 1, 2, 3 };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 int[] lhs2 = { 1, 2, 3 };
 int[] rhs2 = { 2, 2, 3 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 int[] lhs3 = { 1, 2, 3 };
 int[] rhs3 = { 1, 2, 4 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithLongArrays() {
 long[] lhs = { 1L, 2L, 3L };
 long[] rhs = { 1L, 2L, 3L };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 long[] lhs2 = { 1L, 2L, 3L };
 long[] rhs2 = { 2L, 2L, 3L };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 long[] lhs3 = { 1L, 2L, 3L };
 long[] rhs3 = { 1L, 2L, 4L };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithIntArrays() {
 int[] lhs = { 1, 2, 3 };
 int[] rhs = { 1, 2, 3 };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 int[] lhs2 = { 1, 2, 3 };
 int[] rhs2 = { 2, 2, 3 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 int[] lhs3 = { 1, 2, 3 };
 int[] rhs3 = { 1, 2, 4 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithShortArrays() {
 short[] lhs = { (short)1, (short)2, (short)3 };
 short[] rhs = { (short)1, (short)2, (short)3 };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 short[] lhs2 = { (short)1, (short)2, (short)3 };
 short[] rhs2 = { (short)2, (short)2, (short)3 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 short[] lhs3 = { (short)1, (short)2, (short)3 };
 short[] rhs3 = { (short)1, (short)2, (short)4 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithCharArrays() {
 char[] lhs = { 'a', 'b', 'c' };
 char[] rhs = { 'a', 'b', 'c' };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 char[] lhs2 = { 'a', 'b', 'c' };
 char[] rhs2 = { 'b', 'b', 'c' };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 char[] lhs3 = { 'a', 'b', 'c' };
 char[] rhs3 = { 'a', 'b', 'd' };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithByteArrays() {
 byte[] lhs = { (byte)1, (byte)2, (byte)3 };
 byte[] rhs = { (byte)1, (byte)2, (byte)3 };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 byte[] lhs2 = { (byte)1, (byte)2, (byte)3 };
 byte[] rhs2 = { (byte)2, (byte)2, (byte)3 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 byte[] lhs3 = { (byte)1, (byte)2, (byte)3 };
 byte[] rhs3 = { (byte)1, (byte)2, (byte)4 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithDoubleArrays() {
 double[] lhs = { 1.0D, 2.0D, 3.0D };
 double[] rhs = { 1.0D, 2.0D, 3.0D };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 double[] lhs2 = { 1.0D, 2.0D, 3.0D };
 double[] rhs2 = { 2.0D, 2.0D, 3.0D };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 double[] lhs3 = { 1.0D, 2.0D, 3.0D };
 double[] rhs3 = { 1.0D, 2.0D, 4.0D };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithFloatArrays() {
 float[] lhs = { 1.0F, 2.0F, 3.0F };
 float[] rhs = { 1.0F, 2.0F, 3.0F };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 float[] lhs2 = { 1.0F, 2.0F, 3.0F };
 float[] rhs2 = { 2.0F, 2.0F, 3.0F };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 float[] lhs3 = { 1.0F, 2.0F, 3.0F };
 float[] rhs3 = { 1.0F, 2.0F, 4.0F };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }

 @Test
 public void testAppendWithBooleanArrays() {
 boolean[] lhs = { true, false, true };
 boolean[] rhs = { true, false, true };
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 int comparison = compareToBuilder.toComparison();
 Assert.assertEquals(0, comparison);

 boolean[] lhs2 = { true, false, true };
 boolean[] rhs2 = { false, false, true };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs2, rhs2);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);

 boolean[] lhs3 = { true, false, true };
 boolean[] rhs3 = { true, false, false };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs3, rhs3);
 comparison = compareToBuilder.toComparison();
 Assert.assertEquals(-1, comparison);
 }
}

class ClassWithExcludedFields {
 private int field1;
 private int field2;
 private int field3;

 private String privateField = "privateField";
}

class ClassWithSuperclass {
 private int field1;
 private int field2;
}