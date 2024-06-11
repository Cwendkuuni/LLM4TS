import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

 @Test
 public void testContextDependentExpression() throws Exception {
 Expression contextDependentExpression = new ContextDependentExpression();
 EvalContext context = new EvalContext(createDocument(), new NodePointer(createDocument().getDocumentElement()));

 // Typical use case: context-dependent expression evaluates to different values in different contexts
 Assertions.assertEquals("1", contextDependentExpression.computeValue(context).toString());
 context = new EvalContext(createDocument(), new NodePointer(createDocument().getDocumentElement().getElementsByTagName("child")).get(0));
 Assertions.assertEquals("2", contextDependentExpression.computeValue(context).toString());

 // Edge case: context-dependent expression evaluates to the same value in identical contexts
 context = new EvalContext(createDocument(), new NodePointer(createDocument().getDocumentElement()));
 context = new EvalContext(createDocument(), new NodePointer(createDocument().getDocumentElement()));
 Assertions.assertEquals("1", contextDependentExpression.computeValue(context).toString());

 // Error scenario: context-dependent expression evaluates to an error in an invalid context
 context = new EvalContext(createDocument(), new NodePointer(createDocument().getDocumentElement().getElementsByTagName("non-existent-tag").get(0)));
 Assertions.assertThrows(RuntimeException.class, () -> contextDependentExpression.computeValue(context));
 }

 @Test
 public void testContextIndependentExpression() throws Exception {
 Expression contextIndependentExpression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(createDocument(), new NodePointer(createDocument().getDocumentElement()));
 Object value = contextIndependentExpression.computeValue(context);

 // Typical use case: context-independent expression evaluates to the same value in different contexts
 EvalContext context2 = new EvalContext(createDocument(), new NodePointer(createDocument().getDocumentElement().getElementsByTagName("child").get(0)));
 Object value2 = contextIndependentExpression.computeValue(context2);
 Assertions.assertEquals(value, value2);

 // Edge case: context-independent expression evaluates to the same value in an empty context
 EvalContext emptyContext = new EvalContext(createDocument(), null);
 Object valueEmptyContext = contextIndependentExpression.computeValue(emptyContext);
 Assertions.assertEquals(value, valueEmptyContext);

 // Error scenario: context-independent expression evaluates to an error in an invalid context
 EvalContext invalidContext = new EvalContext(createDocument(), new NodePointer(createDocument().getDocumentElement().getElementsByTagName("non-existent-tag").get(0)));
 Assertions.assertThrows(RuntimeException.class, () -> contextIndependentExpression.computeValue(invalidContext));
 }

 // Helper method for creating sample XML document
 private Document createDocument() {
 Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 Element root = document.createElement("root");
 document.appendChild(root);

 Element child1 = document.createElement("child");
 child1.setTextContent("1");
 root.appendChild(child1);

 Element child2 = document.createElement("child");
 child2.setTextContent("2");
 root.appendChild(child2);

 return document;
 }

 // Helper class for context-dependent expression
 static class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getCurrentPosition().getNumber();
 }
 }

 // Helper class for context-independent expression
 static class ContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return new Double(1);
 }
 }
}