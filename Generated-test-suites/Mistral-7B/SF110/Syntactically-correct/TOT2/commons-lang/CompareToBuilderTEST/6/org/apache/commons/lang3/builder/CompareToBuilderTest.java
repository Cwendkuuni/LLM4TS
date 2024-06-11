import org.apache.commons.lang3.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CompareToBuilderTest {

 private CompareToBuilder builder;
 private Object lhs;
 private Object rhs;

 @BeforeEach
 public void setup() {
 lhs = new Integer(5);
 rhs = new Integer(10);
 builder = new CompareToBuilder();
 }

 @Test
 public void testCompareIntObjects() {
 builder.append(lhs, rhs);
 int comparison = builder.toComparison();
 assertEquals(-5, comparison);
 }

 @Test
 public void testCompareNullObjects() {
 lhs = null;
 comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testCompareSameObjects() {
 lhs = rhs;
 comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testCompareArraysOfInts() {
 int[] lhsArray = {1, 2, 3};
 int[] rhsArray = {1, 2, 4};
 builder.append(lhsArray, rhsArray);
 comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testCompareArraysWithSameLengthButDifferentValues() {
 int[] lhsArray = {1, 2, 3};
 int[] rhsArray = {1, 2, 3, 4};
 builder.append(lhsArray, rhsArray);
 comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testCompareArraysWithDifferentLengths() {
 int[] lhsArray = {1, 2, 3};
 int[] rhsArray = {1, 2, 3, 4, 5};
 builder.append(lhsArray, rhsArray);
 comparison = builder.toComparison();
 assertEquals(-2, comparison);
 }

 @Test
 public void testCompareArraysWithNullValues() {
 int[] lhsArray = {1, null};
 int[] rhsArray = {1, 2};
 builder.append(lhsArray, rhsArray);
 comparison = builder.toComparison();
 assertNotNull(comparison); // comparison should be non-zero
 }

 @Test
 public void testCompareArraysWithCustomComparator() {
 int[] lhsArray = {1, 2, 3};
 int[] rhsArray = {3, 2, 1};
 Comparator<int[]> comparator = new Comparator<int[]>() {
 @Override
 public int compare(int[] lhs, int[] rhs) {
 int lengthDifference = lhs.length - rhs.length;
 if (lengthDifference != 0) {
 return lengthDifference;
 }
 for (int i = 0; i < lhs.length && i < rhs.length; i++) {
 int comparison = Integer.compare(lhs[i], rhs[i]);
 if (comparison != 0) {
 return comparison;
 }
 }
 return 0;
 }
 };
 builder.append(lhsArray, rhsArray, comparator);
 comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testCompareObjectsWithCustomComparator() {
 Object lhs = new Integer(5);
 Object rhs = new Integer(10);
 Comparator<Object> comparator = new Comparator<Object>() {
 @Override
 public int compare(Object lhs, Object rhs) {
 return ((Integer)lhs).compareTo((Integer)rhs);
 }
 };
 builder.append(lhs, rhs, comparator);
 comparison = builder.toComparison();
 assertEquals(-5, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testNullLhs() {
 lhs = null;
 builder.append(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testDifferentClassTypes() {
 lhs = new String("hello");
 rhs = new Integer(10);
 builder.append(lhs, rhs);
 }

 @Test
 public void testAppendSuper() {
 CompareToBuilder builder2 = new CompareToBuilder();
 builder2.appendSuper(1);
 CompareToBuilder builder3 = new CompareToBuilder();
 builder3.appendSuper(2);
 builder3.append(lhs, rhs);
 builder3.appendSuper(builder2.toComparison());
 int comparison = builder3.toComparison();
 assertEquals(2, comparison);
 }
}