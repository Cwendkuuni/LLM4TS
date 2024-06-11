import org.apache.commons.jxpath.EvaluationException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathFactory;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Literal;
import org.apache.commons.jxpath.ri.model.Node;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.Iterator;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Node root;
 private EvalContext context;

 @Before
 public void setUp() throws Exception {
 root = NodeFactory.newDocument("test.xml");
 context = new EvalContext(root);
 }

 @Test
 public void testLiteralExpressionIsContextIndependent() {
 Expression expr = new Literal(1);
 assertTrue(expr.isContextDependent());
 assertEquals(1.0, expr.computeValue(context), 0.0);
 }

 @Test
 public void testLiteralExpressionIteration() {
 Expression expr = new Literal(new NodePointer("test.xml", "/"));
 Iterator<Object> iterator = expr.iterate(context);
 assertTrue(iterator.hasNext());
 assertEquals(root, iterator.next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testLiteralExpressionIteratorRemove() {
 Expression expr = new Literal(new NodePointer("test.xml", "/"));
 Iterator<Object> iterator = expr.iterate(context);
 iterator.remove();
 }

 @Test
 public void testContextDependentExpression() {
 Expression expr = new ContextDependentExpression();
 assertFalse(expr.isContextDependent());
 assertEquals(1.0, expr.computeValue(context), 0.0);
 }

 @Test
 public void testContextDependentExpressionIteration() {
 Expression expr = new ContextDependentExpression();
 Iterator<Object> iterator = expr.iterate(context);
 iterator.next();
 assertEquals(root, iterator.next());
 iterator.next();
 assertNull(iterator.next());
 }

 @Test
 public void testContextDependentExpressionError() {
 Expression expr = new ContextDependentExpression();
 try {
 expr.computeValue(null);
 fail();
 } catch (EvaluationException e) {
 //expected
 }
 }

 @Test
 public void testContextDependentExpressionIterationWithEmptyContext() {
 Expression expr = new ContextDependentExpression();
 Iterator<Object> iterator = expr.iterate(new EvalContext(null));
 assertFalse(iterator.hasNext());
 }
}

public class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getCurrentNodePointer().getNodeValue();
 }
}