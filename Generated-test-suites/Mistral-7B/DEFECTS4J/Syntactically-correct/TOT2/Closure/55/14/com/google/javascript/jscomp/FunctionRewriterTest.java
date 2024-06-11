import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {

 private FunctionRewriter functionRewriter;
 private Node rootNode;

 @Before
 public void setup() {
 functionRewriter = new FunctionRewriter(null);
 rootNode = createTestNode();
 }

 @Test
 public void testEmptyFunctionReducer() {
 Node emptyFunctionNode = createEmptyFunctionNode();
 Node expectedHelperNode = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
 Node result = functionRewriter.process(null, rootNode);
 Node actualHelperNode = getHelperNode(result, EmptyFunctionReducer.class);
 assertEquals("Unexpected helper code.", expectedHelperNode, actualHelperNode);
 }

 @Test
 public void testIdentityReducer() {
 Node identityFunctionNode = createIdentityFunctionNode();
 Node expectedHelperNode = functionRewriter.parseHelperCode(IdentityReducer.class);
 Node result = functionRewriter.process(null, rootNode);
 Node actualHelperNode = getHelperNode(result, IdentityReducer.class);
 assertEquals("Unexpected helper code.", expectedHelperNode, actualHelperNode);
 }

 @Test
 public void testGetterReducer() {
 Node getterFunctionNode = createGetterFunctionNode();
 Node expectedHelperNode = functionRewriter.parseHelperCode(GetterReducer.class);
 Node result = functionRewriter.process(null, rootNode);
 Node actualHelperNode = getHelperNode(result, GetterReducer.class);
 assertEquals("Unexpected helper code.", expectedHelperNode, actualHelperNode);
 }

 @Test
 public void testSetterReducer() {
 Node setterFunctionNode = createSetterFunctionNode();
 Node expectedHelperNode = functionRewriter.parseHelperCode(SetterReducer.class);
 Node result = functionRewriter.process(null, rootNode);
 Node actualHelperNode = getHelperNode(result, SetterReducer.class);
 assertEquals("Unexpected helper code.", expectedHelperNode, actualHelperNode);
 }

 @Test
 public void testReducerWithArguments() {
 Node reducerFunctionNode = createReducerWithArgumentsNode();
 Node expectedHelperNode = functionRewriter.parseHelperCode(ReducerWithArguments.class);
 Node result = functionRewriter.process(null, rootNode);
 Node actualHelperNode = getHelperNode(result, ReducerWithArguments.class);
 assertEquals("Unexpected helper code.", expectedHelperNode, actualHelperNode);
 }

 @Test(expected = IllegalStateException.class)
 public void testFunctionWithoutReturnStatement() {
 Node functionNodeWithoutReturn = createFunctionWithoutReturnStatementNode();
 functionRewriter.process(null, rootNode);
 fail("Function without return statement should not be reduced.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeSavingsThreshold() {
 functionRewriter = new FunctionRewriter(null);
 functionRewriter.SAVINGS_THRESHOLD = -16;
 Node functionNodeToReduce = createFunctionNodeToReduce();
 Node result = functionRewriter.process(null, rootNode);
 fail("Negative savings threshold should cause no reductions.");
 }

 @Test
 public void testNoReductionWhenSavingsBelowThreshold() {
 functionRewriter = new FunctionRewriter(null);
 Node functionNodeToReduce = createFunctionNodeToReduce();
 Node result = functionRewriter.process(null, rootNode);
 Node functionNodeAfterReduction = functionRewriter.getFunctionNode(functionNodeToReduce);
 assertNotSame("Function should not be reduced.", functionNodeToReduce, functionNodeAfterReduction);
 }

 @Test
 public void testFunctionReduction() {
 functionRewriter = new FunctionRewriter(null);
 Node functionNodeToReduce = createFunctionNodeToReduce();
 Node expectedResult = createExpectedReducedNode();
 Node result = functionRewriter.process(null, rootNode);
 Node actualResult = getFunctionNode(result, functionNodeToReduce);
 assertEquals("Unexpected reduction result.", expectedResult, actualResult);
 }

 private Node createTestNode() {
 // A simple test node with no functions to be reduced.
 return new Node(Token.BLOCK, 1, 1);
 }

 private Node createEmptyFunctionNode() {
 // A function with no body.
 Node functionNode = new Node(Token.FUNCTION, 1, 1);
 functionNode.setProperty("name", "emptyFunction");
 functionNode.setProperty("arguments", new Node(Token.EMPTY, 1, 1));
 functionNode.setProperty("body", null);
 return functionNode;
 }

 private Node createIdentityFunctionNode() {
 // A function that returns its argument.
 Node functionNode = new Node(Token.FUNCTION, 1, 1);
 functionNode.setProperty("name", "identityFunction");
 functionNode.setProperty("arguments", new Node(Token.LIST, 1, 1));
 functionNode.setProperty("body", new Node(Token.RETURN, 1, 1));
 functionNode.setProperty("body.arguments", new Node(Token.NAME, 1, 1));
 return functionNode;
 }

 private Node createGetterFunctionNode() {
 // A getter function.
 Node functionNode = new Node(Token.FUNCTION, 1, 1);
 functionNode.setProperty("name", "getterFunction");
 functionNode.setProperty("arguments", new Node(Token.LIST, 1, 1));
 functionNode.setProperty("body", new Node(Token.RETURN, 1, 1));
 functionNode.setProperty("body.arguments", new Node(Token.THIS, 1, 1));
 Node getterPropertyNode = new Node(Token.GETPROP, 1, 1);
 getterPropertyNode.setProperty("object", functionNode);
 getterPropertyNode.setProperty("property", new Node(Token.NAME, 1, 1));
 functionNode.setProperty("body.arguments", getterPropertyNode);
 return functionNode;
 }

 private Node createSetterFunctionNode() {
 // A setter function.
 Node functionNode = new Node(Token.FUNCTION, 1, 1);
 functionNode.setProperty("name", "setterFunction");
 functionNode.setProperty("arguments", new Node(Token.LIST, 1, 1));
 functionNode.setProperty("body", new Node(Token.ASSIGN, 1, 1));
 Node assignNode = functionNode.getLastChild();
 Node thisNode = new Node(Token.THIS, 1, 1);
 Node propertyNode = new Node(Token.GETPROP, 1, 1);
 propertyNode.setProperty("object", thisNode);
 propertyNode.setProperty("property", new Node(Token.NAME, 1, 1));
 assignNode.setProperty("left", propertyNode);
 assignNode.setProperty("right", new Node(Token.NAME, 1, 1));
 return functionNode;
 }

 private Node createReducerWithArgumentsNode() {
 // A reducer function with arguments.
 Node functionNode = new Node(Token.FUNCTION, 1, 1);
 functionNode.setProperty("name", "reducerWithArguments");
 functionNode.setProperty("arguments", new Node(Token.LIST, 1, 1));
 functionNode.setProperty("body", new Node(Token.RETURN, 1, 1));
 Node returnNode = functionNode.getLastChild();
 Node argNode = new Node(Token.NAME, 1, 1);
 returnNode.setProperty("arguments", argNode);
 return functionNode;
 }

 private Node createFunctionNodeToReduce() {
 // A function that can be reduced.
 Node functionNodeToReduce = new Node(Token.FUNCTION, 1, 1);
 functionNodeToReduce.setProperty("name", "functionToReduce");
 functionNodeToReduce.setProperty("arguments", new Node(Token.LIST, 1, 1));
 functionNodeToReduce.setProperty("body", new Node(Token.RETURN, 1, 1));
 Node returnNode = functionNodeToReduce.getLastChild();
 Node argNode = new Node(Token.NAME, 1, 1);
 returnNode.setProperty("arguments", argNode);
 return functionNodeToReduce;
 }

 private Node createExpectedReducedNode() {
 // The expected node after reduction.
 Node expectedResult = new Node(Token.CALL, 1, 1);
 expectedResult.setProperty("callee", new Node(Token.NAME, 1, 1));
 return expectedResult;
 }

 private Node getHelperNode(Node node, Class<? extends Reducer> reducerClass) {
 Node helperNode = null;
 for (Node childNode : node.getChildren()) {
 if (childNode.getType() == Token.BLOCK) {
 for (Node grandChildNode : childNode.getChildren()) {
 if (helperNode == null && grandChildNode.isHelperNode(reducerClass)) {
 helperNode = grandChildNode;
 break;
 }
 }
 if (helperNode != null) {
 break;
 }
 }
 }
 return helperNode;
 }

 private Node getFunctionNode(Node node, Node functionNodeToReduce) {
 Node functionNode = null;
 for (Node childNode : node.getChildren()) {
 if (childNode.getType() == Token.BLOCK) {
 for (Node grandChildNode : childNode.getChildren()) {
 if (grandChildNode.isFunctionNode(functionNodeToReduce)) {
 functionNode = grandChildNode;
 break;
 }
 }
 if (functionNode != null) {
 break;
 }
 }
 }
 return functionNode;
 }
}