import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompareToBuilderTest {

 @BeforeAll
 static void init() {
 System.out.println("Test initialization");
 }

 @AfterAll
 static void tearDown() {
 System.out.println("Test tear down");
 }

 @Test
 void testCompareToBuilderInstance() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 assertTrue(compareToBuilder instanceof CompareToBuilder);
 }

 @Test
 void testReflectionCompare_NullLhs() {
 assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(null, new Object()));
 }

 @Test
 void testReflectionCompare_NullRhs() {
 assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(new Object(), null));
 }

 @Test
 void testReflectionCompare_SameObjects() {
 int comparison = CompareToBuilder.reflectionCompare(new Object(), new Object());
 assertEquals(0, comparison);
 }

 @Test
 void testReflectionCompare_DifferentClasses() {
 assertThrows(ClassCastException.class, () -> CompareToBuilder.reflectionCompare(new Object(), new String()));
 }

 @Test
 void testReflectionCompare_ExcludedFields() {
 String[] excludeFields = new String[]{"field1", "field2"};
 int comparison = CompareToBuilder.reflectionCompare(new Object(), new Object(), Arrays.asList(excludeFields.clone()), excludeFields);
 // Add assertions based on the expected comparison result
 }

 @Test
 void testAppend_NullObject() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(null, new Object());
 assertEquals(-1, compareToBuilder.comparison);
 }

 @Test
 void testAppend_SameObjects() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(new Object(), new Object());
 assertEquals(0, compareToBuilder.comparison);
 }

 @Test
 void testAppend_DifferentObjectsWithComparator() {
 Comparator<Object> comparator = (lhs, rhs) -> lhs.toString().compareTo(rhs.toString());
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(new Object(), new Object(), comparator);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"int[]", "int[]"})
 void testAppend_Arrays(String arrayType) {
 Object[] lhs = new Object[] {1, 2, 3};
 Object[] rhs = new Object[] {1, 2, 3};
 if (arrayType.equals("int[]")) {
 int[] lhsArr = (int[]) lhs;
 int[] rhsArr = (int[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"int[]", new String[] {"int[]", "int[]"}})
 void testAppend_ArraysWithExcludedFields(String arrayType, String[] excludeFields) {
 Object[] lhs = new Object[] {1, 2, 3};
 Object[] rhs = new Object[] {1, 2, 3};
 if (arrayType.equals("int[]")) {
 int[] lhsArr = (int[]) lhs;
 int[] rhsArr = (int[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs, null);
 // Add assertions based on the expected comparison result
 // Add tests for excluded fields if necessary
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"long[]", "long[]"})
 void testAppend_LongArrays(String arrayType) {
 Object[] lhs = new Object[] {1L, 2L, 3L};
 Object[] rhs = new Object[] {1L, 2L, 3L};
 if (arrayType.equals("long[]")) {
 long[] lhsArr = (long[]) lhs;
 long[] rhsArr = (long[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"int[]", "int[]"})
 void testAppend_IntegerArrays(String arrayType) {
 Object[] lhs = new Object[] {1, 2, 3};
 Object[] rhs = new Object[] {1, 2, 3};
 if (arrayType.equals("int[]")) {
 int[] lhsArr = (int[]) lhs;
 int[] rhsArr = (int[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs, null);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"short[]", "short[]"})
 void testAppend_ShortArrays(String arrayType) {
 Object[] lhs = new Object[] {(short)1, (short)2, (short)3};
 Object[] rhs = new Object[] {(short)1, (short)2, (short)3};
 if (arrayType.equals("short[]")) {
 short[] lhsArr = (short[]) lhs;
 short[] rhsArr = (short[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"char[]", "char[]"})
 void testAppend_CharacterArrays(String arrayType) {
 Object[] lhs = new Object[] {'a', 'b', 'c'};
 Object[] rhs = new Object[] {'a', 'b', 'c'};
 if (arrayType.equals("char[]")) {
 char[] lhsArr = (char[]) lhs;
 char[] rhsArr = (char[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"byte[]", "byte[]"})
 void testAppend_ByteArrays(String arrayType) {
 Object[] lhs = new Object[] {(byte)1, (byte)2, (byte)3};
 Object[] rhs = new Object[] {(byte)1, (byte)2, (byte)3};
 if (arrayType.equals("byte[]")) {
 byte[] lhsArr = (byte[]) lhs;
 byte[] rhsArr = (byte[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"double[]", "double[]"})
 void testAppend_DoubleArrays(String arrayType) {
 Object[] lhs = new Object[] {1.0, 2.0, 3.0};
 Object[] rhs = new Object[] {1.0, 2.0, 3.0};
 if (arrayType.equals("double[]")) {
 double[] lhsArr = (double[]) lhs;
 double[] rhsArr = (double[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"float[]", "float[]"})
 void testAppend_FloatArrays(String arrayType) {
 Object[] lhs = new Object[] {1.0f, 2.0f, 3.0f};
 Object[] rhs = new Object[] {1.0f, 2.0f, 3.0f};
 if (arrayType.equals("float[]")) {
 float[] lhsArr = (float[]) lhs;
 float[] rhsArr = (float[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 // Add assertions based on the expected comparison result
 }

 @Test
 @ParameterizedTest(name = "testAppend_{0}vs{1}")
 @ValueSource(strings = {"boolean[]", "boolean[]"})
 void testAppend_BooleanArrays(String arrayType) {
 Object[] lhs = new Object[] {true, false, true};
 Object[] rhs = new Object[] {true, false, true};
 if (arrayType.equals("boolean[]")) {
 boolean[] lhsArr = (boolean[]) lhs;
 boolean[] rhsArr = (boolean[]) rhs;
 Arrays.sort(lhsArr);
 Arrays.sort(rhsArr);
 }
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 // Add assertions based on the expected comparison result
 }

 @Test
 void testAppendSuper() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.appendSuper(10);
 assertEquals(10, compareToBuilder.comparison);
 }
}