import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.SingleNodeIterator;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 private CoreOperationCompare comparisonOperation;
 private EvalContext context;
 private InfoSet infoSet;
 private JXPathContext jxPathContext;
 private Expression leftExpression;
 private Expression rightExpression;
 private Element root;

 @Before
 public void setUp() {
 leftExpression = new ConstantExpression(new Integer(5));
 rightExpression = new ConstantExpression(new Integer(6));
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 context = new EvalContext();
 infoSet = new org.apache.commons.jxpath.SimpleNamespaceContext();
 jxPathContext = new JXPathContext(infoSet);
 root = createRootElement();
 }

 @Test
 public void testEqual_twoSameNumbers() {
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqual_twoDifferentNumbers() {
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testEqual_twoSameStrings() {
 leftExpression = new ConstantExpression("hello");
 rightExpression = new ConstantExpression("hello");
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqual_twoDifferentStrings() {
 leftExpression = new ConstantExpression("hello");
 rightExpression = new ConstantExpression("world");
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testEqual_twoNullValues() {
 leftExpression = new ConstantExpression(null);
 rightExpression = new ConstantExpression(null);
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqual_oneNullValue() {
 leftExpression = new ConstantExpression(null);
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testEqual_oneBooleanValue_and_oneNumberValue() {
 leftExpression = new ConstantExpression(Boolean.TRUE);
 rightExpression = new ConstantExpression(new Integer(5));
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testEqual_oneBooleanValue_and_oneStringValue() {
 leftExpression = new ConstantExpression(Boolean.TRUE);
 rightExpression = new ConstantExpression("true");
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqual_oneBooleanValue_and_oneBooleanValue_with_different_values() {
 leftExpression = new ConstantExpression(Boolean.TRUE);
 rightExpression = new ConstantExpression(Boolean.FALSE);
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testEqual_oneBooleanValue_and_oneStringValue() {
 leftExpression = new ConstantExpression(Boolean.TRUE);
 rightExpression = new ConstantExpression("true");
 comparisonOperation = new EqualOperatior(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqual_twoIterators() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
 leftExpression = new ConstantExpression(list1.iterator());
 rightExpression = new ConstantExpression(list2.iterator());
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testEqual_twoIterators_with_common_elements() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = new ArrayList<>(list1);
 list2.add(4);
 leftExpression = new ConstantExpression(list1.iterator());
 rightExpression = new ConstantExpression(list2.iterator());
 comparisonOperation = new EqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotEqual_twoSameNumbers() {
 leftExpression = new ConstantExpression(5);
 rightExpression = new ConstantExpression(5);
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqual_twoDifferentNumbers() {
 leftExpression = new ConstantExpression(5);
 rightExpression = new ConstantExpression(6);
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotEqual_twoSameStrings() {
 leftExpression = new ConstantExpression("hello");
 rightExpression = new ConstantExpression("hello");
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqual_twoDifferentStrings() {
 leftExpression = new ConstantExpression("hello");
 rightExpression = new ConstantExpression("world");
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotEqual_twoNullValues() {
 leftExpression = new ConstantExpression(null);
 rightExpression = new ConstantExpression(null);
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotEqual_oneNullValue() {
 leftExpression = new ConstantExpression(null);
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqual_oneBooleanValue_and_oneNumberValue() {
 leftExpression = new ConstantExpression(Boolean.TRUE);
 rightExpression = new ConstantExpression(new Integer(5));
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqual_oneBooleanValue_and_oneStringValue() {
 leftExpression = new ConstantExpression(Boolean.TRUE);
 rightExpression = new ConstantExpression("true");
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqual_oneBooleanValue_and_oneBooleanValue_with_different_values() {
 leftExpression = new ConstantExpression(Boolean.TRUE);
 rightExpression = new ConstantExpression(Boolean.FALSE);
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testNotEqual_oneBooleanValue_and_oneStringValue() {
 leftExpression = new ConstantExpression(Boolean.TRUE);
 rightExpression = new ConstantExpression("true");
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqual_twoIterators() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4);
 leftExpression = new ConstantExpression(list1.iterator());
 rightExpression = new ConstantExpression(list2.iterator());
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testNotEqual_twoIterators_with_common_elements() {
 List<Integer> list1 = Arrays.asList(1, 2, 3);
 List<Integer> list2 = new ArrayList<>(list1);
 list2.add(4);
 leftExpression = new ConstantExpression(list1.iterator());
 rightExpression = new ConstantExpression(list2.iterator());
 comparisonOperation = new NotEqualOperation(leftExpression, rightExpression);
 jxPathContext.setContextNode(root);
 Object result = comparisonOperation.compute(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 private Element createRootElement() {
 Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 Element root = doc.createElement("root");
 doc.appendChild(root);
 return root;
 }

}