import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThanExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(1), new CoreNumberExpression(2)) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), 1, 2, new SelfContext() });

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(1), new CoreNumberExpression(2)) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), 1, 2, new SelfContext() });

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(2), new CoreNumberExpression(1)) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare < 0;
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), 1, 2, new SelfContext() });

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(2), new CoreNumberExpression(1)) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare <= 0;
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), 1, 2, new SelfContext() });

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanExpressionWithNaN() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(Double.NaN), new CoreNumberExpression(1)) {
 @Override
 protected boolean evaluateCompare(int compare) {
 throw new UnsupportedOperationException("Not used");
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), Double.NaN, 1, new SelfContext() });

 Object result = expression.computeValue(context);

 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testLessThanExpressionWithNaN() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumberExpression(1), new CoreNumberExpression(Double.NaN)) {
 @Override
 protected boolean evaluateCompare(int compare) {
 throw new UnsupportedOperationException("Not used");
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), 1, Double.NaN, new SelfContext() });

 Object result = expression.computeValue(context);

 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testGreaterThanExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreIteratorExpression(new Object[] { 1, 2, 3, 4 }), new CoreIteratorExpression(new Object[] { 3, 4, 5, 6 })) {
 @Override
 protected boolean evaluateCompare(int compare) {
 throw new UnsupportedOperationException("Not used");
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), new SelfContext(), new IteratorContext(new Object[] { 1, 2, 3, 4 }), new IteratorContext(new Object[] { 3, 4, 5, 6 }) });

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGreaterThanOrEqualExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreIteratorExpression(new Object[] { 1, 2, 3, 4 }), new CoreIteratorExpression(new Object[] { 3, 4, 5, 6 })) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), new SelfContext(), new IteratorContext(new Object[] { 1, 2, 3, 4 }), new IteratorContext(new Object[] { 3, 4, 5, 6 }) });

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreIteratorExpression(new Object[] { 4, 5, 6, 7 }), new CoreIteratorExpression(new Object[] { 3, 4, 5, 6 })) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare < 0;
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), new SelfContext(), new IteratorContext(new Object[] { 4, 5, 6, 7 }), new IteratorContext(new Object[] { 3, 4, 5, 6 }) });

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanOrEqualExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreIteratorExpression(new Object[] { 4, 5, 6, 7 }), new CoreIteratorExpression(new Object[] { 3, 4, 5, 6 })) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare <= 0;
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), new SelfContext(), new IteratorContext(new Object[] { 4, 5, 6, 7 }), new IteratorContext(new Object[] { 3, 4, 5, 6 }) });

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanExpressionWithNaNAndIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreIteratorExpression(new Object[] { Double.NaN }), new CoreIteratorExpression(new Object[] { 1, 2, 3, 4 })) {
 @Override
 protected boolean evaluateCompare(int compare) {
 throw new UnsupportedOperationException("Not used");
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), new SelfContext(), new IteratorContext(new Object[] { Double.NaN }), new IteratorContext(new Object[] { 1, 2, 3, 4 }) });

 Object result = expression.computeValue(context);

 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testLessThanExpressionWithNaNAndIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreIteratorExpression(new Object[] { 1, 2, 3, 4 }), new CoreIteratorExpression(new Object[] { Double.NaN })) {
 @Override
 protected boolean evaluateCompare(int compare) {
 throw new UnsupportedOperationException("Not used");
 }
 };

 EvalContext context = new EvalContext();
 context.setNodes(new Object[] { new InitialContext(), new SelfContext(), new IteratorContext(new Object[] { 1, 2, 3, 4 }), new IteratorContext(new Object[] { Double.NaN }) });

 Object result = expression.computeValue(context);

 assertFalse(result instanceof Boolean);
 }
}