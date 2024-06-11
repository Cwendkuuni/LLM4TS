import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.AxisNames;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 private JXPathContext context;
 private Document document;

 @Before
 public void setup() throws Exception {
 document = JXPathConstants.newDocument();
 Element root = document.createElement("root");
 document.appendChild(root);
 context = new JXPathContext(document);
 }

 @Test
 public void testEqualOperation() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(1), new ConstantExpression(1));
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualOperation() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(1), new ConstantExpression(2));
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithNull() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(null), new ConstantExpression(null));
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualWithNull() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(null), new ConstantExpression(""));
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithStrings() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression("test"), new ConstantExpression("test"));
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualWithStrings() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression("test"), new ConstantExpression("other"));
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithNumbers() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(1.0), new ConstantExpression(1.0));
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualWithNumbers() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(1.0), new ConstantExpression(2.0));
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test
 public void testEqualWithIterators() throws Exception {
 Element e1 = document.createElement("e1");
 Element e2 = document.createElement("e2");
 root.appendChild(e1);
 root.appendChild(e2);
 CoreOperationCompare compare = new CoreOperationCompare(
 new AxisIterator(context, AxisNames.CHILD, e1),
 new AxisIterator(context, AxisNames.CHILD, e1));
 assertTrue(compare.evaluateToBoolean(context));
 }

 @Test
 public void testNotEqualWithIterators() throws Exception {
 Element e1 = document.createElement("e1");
 Element e2 = document.createElement("e2");
 root.appendChild(e1);
 root.appendChild(e2);
 CoreOperationCompare compare = new CoreOperationCompare(
 new AxisIterator(context, AxisNames.CHILD, e1),
 new AxisIterator(context, AxisNames.CHILD, e2));
 assertFalse(compare.evaluateToBoolean(context));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidComparison() throws Exception {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(1), null);
 }
}