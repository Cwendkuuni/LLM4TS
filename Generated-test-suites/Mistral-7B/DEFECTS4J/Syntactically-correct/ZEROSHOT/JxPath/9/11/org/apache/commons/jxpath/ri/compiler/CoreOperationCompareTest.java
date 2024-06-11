import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 @Test
 public void testCoreOperationCompare_equal_sameObjects() {
 CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression(new Pointer("1")), new ConstantExpression(new Pointer("1")));
 EvalContext context = new EvalContext();
 assertTrue(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_equal_sameValues() {
 CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression("1"), new ConstantExpression("1"));
 EvalContext context = new EvalContext();
 assertTrue(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_equal_differentObjects_differentValues() {
 CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression("1"), new ConstantExpression("2"));
 EvalContext context = new EvalContext();
 assertFalse(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_equal_differentObjects_sameValues() {
 CoreOperationCompare compare = new CoreOperationCompare(new ConstantExpression("1"), new ConstantExpression(new Pointer("1")));
 EvalContext context = new EvalContext();
 assertFalse(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_equal_sameCollection_sameElements() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2]").getChildNodes())),
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2]").getChildNodes())));
 EvalContext context = new EvalContext();
 assertTrue(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_equal_sameCollection_differentElements() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2]").getChildNodes())),
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,3]").getChildNodes())));
 EvalContext context = new EvalContext();
 assertFalse(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_equal_differentCollections_sameElements() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2]").getChildNodes())),
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[2,1]").getChildNodes())));
 EvalContext context = new EvalContext();
 assertTrue(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_equal_differentCollections_differentElements() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2]").getChildNodes())),
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,3]").getChildNodes())));
 EvalContext context = new EvalContext();
 assertFalse(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_contains_iterator_containedValue() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2,3]").getChildNodes())),
 new ConstantExpression("1"));
 EvalContext context = new EvalContext();
 assertTrue(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_contains_iterator_notContainedValue() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2,3]").getChildNodes())),
 new ConstantExpression("4"));
 EvalContext context = new EvalContext();
 assertFalse(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_contains_iterator_notIterator() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2,3]").getChildNodes())),
 new ConstantExpression("5"));
 EvalContext context = new EvalContext();
 assertFalse(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_findMatch_iteratorsWithMatchingElements() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2]").getChildNodes())),
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2]").getChildNodes())));
 EvalContext context = new EvalContext();
 assertTrue(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }

 @Test
 public void testCoreOperationCompare_findMatch_iteratorsWithoutMatchingElements() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,2]").getChildNodes())),
 new ConstantExpression(new IteratorExpression(new ConstantExpression("[1,3]").getChildNodes())));
 EvalContext context = new EvalContext();
 assertFalse(compare.equals(context, compare.getArguments()[0], compare.getArguments()[1]));
 }
}