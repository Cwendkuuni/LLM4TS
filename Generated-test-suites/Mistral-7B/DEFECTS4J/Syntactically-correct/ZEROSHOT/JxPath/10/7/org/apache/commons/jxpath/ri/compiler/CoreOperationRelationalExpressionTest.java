import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testCoreOperationRelationalExpression() {
 CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(new CoreOperation[] { new CoreNumberTestConstantExpression(1L), new CoreNumberTestConstantExpression(2L) });
 assertEquals(3, operation.getPrecedence());
 assertFalse(operation.isSymmetric());
 }

 @Test
 public void testComputeValue() {
 CoreOperationRelationalExpression operation = new CoreOperationGreaterThanExpression(new CoreOperation[] { new CoreNumberTestConstantExpression(1L), new CoreNumberTestConstantExpression(2L) });
 EvalContext context = new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext());
 Object left = context.getSingleNodePointer();
 Object right = new CoreNumberTestConstantExpression(2L).computeValue(context);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 left = new CoreNumberTestConstantExpression(2L).computeValue(context);
 right = new CoreNumberTestConstantExpression(2L).computeValue(context);
 result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEvaluateCompare() {
 CoreOperationRelationalExpression operation = new CoreOperationGreaterThanExpression(new CoreOperation[] { new CoreNumberTestConstantExpression(1L), new CoreNumberTestConstantExpression(2L) });
 assertTrue(operation.evaluateCompare(-1));
 assertFalse(operation.evaluateCompare(0));
 assertFalse(operation.evaluateCompare(1));
 }

 @Test
 public void testCompute() {
 CoreOperationRelationalExpression operation = new CoreOperationGreaterThanExpression(new CoreOperation[] { new CoreNumberTestConstantExpression(1L), new CoreNumberTestConstantExpression(2L) });
 Object left = new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 Object right = new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 assertFalse(operation.compute(left, right));

 left = new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 right = new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 assertFalse(operation.compute(left, right));

 left = new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 right = new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 assertTrue(operation.compute(left, right));

 Iterator leftIter = Collections.singletonList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))).iterator();
 Iterator rightIter = Collections.singletonList(new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))).iterator();
 assertFalse(operation.compute(leftIter, rightIter));

 leftIter = Collections.singletonList(new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))).iterator();
 rightIter = Collections.singletonList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))).iterator();
 assertFalse(operation.compute(leftIter, rightIter));

 leftIter = Iterators.emptyIterator();
 rightIter = Collections.singletonList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))).iterator();
 assertFalse(operation.compute(leftIter, rightIter));

 leftIter = Iterators.emptyIterator();
 rightIter = Iterators.emptyIterator();
 assertFalse(operation.compute(leftIter, rightIter));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 rightIter = Collections.singletonList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))).iterator();
 assertFalse(operation.compute(leftIter, rightIter));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 rightIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 assertFalse(operation.compute(leftIter, rightIter));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 rightIter = Collections.singletonList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))).iterator();
 assertFalse(operation.compute(leftIter, rightIter));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 rightIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 assertTrue(operation.compute(leftIter, rightIter));
 }

 @Test
 public void testReduce() {
 CoreOperationRelationalExpression operation = new CoreOperationGreaterThanExpression(new CoreOperation[] { new CoreNumberTestConstantExpression(1L), new CoreNumberTestConstantExpression(2L) });
 Object left = new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 Object right = new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 Object reducedLeft = operation.reduce(left);
 Object reducedRight = operation.reduce(right);
 assertEquals(left, reducedLeft);
 assertEquals(right, reducedRight);
 }

 @Test
 public void testContainsMatch() {
 CoreOperationRelationalExpression operation = new CoreOperationGreaterThanExpression(new CoreOperation[] { new CoreNumberTestConstantExpression(1L), new CoreNumberTestConstantExpression(2L) });
 Iterator leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 Object value = new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 assertFalse(operation.containsMatch(leftIter, value));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 assertFalse(operation.containsMatch(leftIter, new CoreNumberTestConstantExpression(3L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 assertTrue(operation.containsMatch(leftIter, value));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 assertFalse(operation.containsMatch(leftIter, value));
 }

 @Test
 public void testFindMatch() {
 CoreOperationRelationalExpression operation = new CoreOperationGreaterThanExpression(new CoreOperation[] { new CoreNumberTestConstantExpression(1L), new CoreNumberTestConstantExpression(2L) });
 Iterator leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 Iterator rightIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 assertFalse(operation.findMatch(leftIter, rightIter));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 Iterator valueIter = Collections.singletonList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()))).iterator();
 assertFalse(operation.findMatch(leftIter, valueIter));

 leftIter = new ArrayList<Object>(Arrays.asList(new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())),
 new CoreNumberTestConstantExpression(2L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext())))).iterator();
 Object value = new CoreNumberTestConstantExpression(1L).computeValue(new EvalContext(Iterators.emptyIterator(), new InitialContext(), new SelfContext()));
 assertTrue(operation.findMatch(leftIter, Collections.singletonList(value).iterator()));
 }
}