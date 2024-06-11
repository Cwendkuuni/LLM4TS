import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualWithDifferentTypes() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(1), new Constant(2.0));
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithEqualValues() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(1), new Constant(1));
 assertEquals(Boolean.FALSE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithSameExpression() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(1), new Constant(1));
 CoreOperationCompare compare = new CoreOperationCompare(new Constant(1), new Constant(1));
 assertTrue(notEqual.getClass().isInstance(compare));
 assertEquals(Boolean.FALSE, notEqual.computeValue(new EvalContext()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithNull() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(1), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithNullArg1() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(null, new Constant(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithNullArg2() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(1), null);
 }

 @Test
 public void testNotEqualWithBooleanValues() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(Boolean.TRUE), new Constant(Boolean.FALSE));
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithBooleanValuesSame() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(Boolean.TRUE), new Constant(Boolean.TRUE));
 assertEquals(Boolean.FALSE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithEmptyString() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(""), new Constant(""));
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithDifferentStrings() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant("apple"), new Constant("banana"));
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithSameStrings() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant("apple"), new Constant("apple"));
 assertEquals(Boolean.FALSE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithEmptyList() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(new ArrayList<>()), new Constant(new ArrayList<>()));
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithListsWithSameElements() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(Arrays.asList(1, 2)), new Constant(Arrays.asList(1, 2)));
 assertEquals(Boolean.FALSE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithListsWithDifferentElements() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant(Arrays.asList(1, 2)), new Constant(Arrays.asList(2, 3)));
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));
 }
}