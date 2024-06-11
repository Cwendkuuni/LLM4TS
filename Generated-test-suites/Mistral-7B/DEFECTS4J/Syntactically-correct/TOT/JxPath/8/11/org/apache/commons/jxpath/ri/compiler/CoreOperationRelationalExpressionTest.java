import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new VariableExpression("x"), new NumberExpression(5.0));
 JXPathContext context = new JXPathContext(createTestDocument());
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new VariableExpression("x"), new NumberExpression(5.0));
 JXPathContext context = new JXPathContext(createTestDocument());
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThan(new VariableExpression("x"), new NumberExpression(5.0));
 JXPathContext context = new JXPathContext(createTestDocument());
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new NumberExpression(5.0));
 JXPathContext context = new JXPathContext(createTestDocument());
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testStringComparison() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new VariableExpression("x"), new StringExpression("apple"));
 JXPathContext context = new JXPathContext(createTestDocumentWithStrings());
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new StringExpression("apple"));
 JXPathContext context2 = new JXPathContext(createTestDocumentWithStrings());
 Object result2 = expression.computeValue(context2);
 Assert.assertEquals(Boolean.TRUE, result2);
 }

 @Test
 public void testIterableComparison() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new VariableExpression("x"), new CollectionExpression(new Integer[]{1, 2, 3, 4, 5}));
 JXPathContext context = new JXPathContext(createTestDocumentWithIterable());
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new CollectionExpression(new Integer[]{1, 2, 3, 4, 5}));
 JXPathContext context2 = new JXPathContext(createTestDocumentWithIterable());
 Object result2 = expression.computeValue(context2);
 Assert.assertEquals(Boolean.TRUE, result2);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidComparison() {
 CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
 JXPathContext context = new JXPathContext(createTestDocument());
 try {
 Object result = expression.computeValue(context);
 } catch (RuntimeException e) {
 Assert.assertEquals("Invalid number of arguments for relational operation", e.getMessage());
 throw e;
 }
 }

 private Node createTestDocument() {
 // Initialize an empty Document object
 Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 // Set document element
 Node root = doc.createElement("root");
 doc.appendChild(root);
 return doc.getDocumentElement();
 }

 private Node createTestDocumentWithStrings() {
 // Initialize an empty Document object
 Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 // Set document element
 Node root = doc.createElement("root");
 root.setTextContent("apple");
 doc.appendChild(root);
 return doc.getDocumentElement();
 }

 private Node createTestDocumentWithIterable() {
 // Initialize an empty Document object
 Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 // Set document element
 Node root = doc.createElement("root");
 root.appendChild(doc.createAttribute("class", "java.util.ArrayList"));
 root.appendChild(doc.createElement("item"));
 root.appendChild(doc.createElement("item"));
 root.appendChild(doc.createElement("item"));
 root.appendChild(doc.createElement("item"));
 root.appendChild(doc.createElement("item"));
 doc.appendChild(root);
 return doc.getDocumentElement();
 }

}