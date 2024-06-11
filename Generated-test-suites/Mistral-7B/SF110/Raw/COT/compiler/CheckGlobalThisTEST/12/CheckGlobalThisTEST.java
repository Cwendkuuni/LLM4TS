import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_Function() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node functionNode = new Node(TOKEN_FUNCTION);
    Node functionJsDocInfo = new Node(TOKEN_COMMENT, "function foo() { }");
    functionNode.setJSDocInfo(functionJsDocInfo);

    Node functionParentNode = new Node(TOKEN_SCRIPT);

    NodeTraversal traversal = new NodeTraversal(functionParentNode, checkGlobalThis);
    traversal.traverse(functionNode, null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParentNode));
  }

  @Test
  public void testShouldTraverse_Constructor() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node functionNode = new Node(TOKEN_FUNCTION);
    functionNode.setIsConstructor(true);

    Node functionParentNode = new Node(TOKEN_SCRIPT);

    NodeTraversal traversal = new NodeTraversal(functionParentNode, checkGlobalThis);
    traversal.traverse(functionNode, null);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParentNode));
  }

  @Test
  public void testShouldTraverse_ThisAnnotatedFunction() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node functionNode = new Node(TOKEN_FUNCTION);
    Node thisAnnotationNode = new Node(TOKEN_COMMENT, "@this");
    functionNode.addChildToEnd(thisAnnotationNode);

    Node functionParentNode = new Node(TOKEN_SCRIPT);

    NodeTraversal traversal = new NodeTraversal(functionParentNode, checkGlobalThis);
    traversal.traverse(functionNode, null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParentNode));
  }

  @Test
  public void testShouldTraverse_FunctionWithNoLogicalPlaceForThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node functionNode = new Node(TOKEN_FUNCTION);
    Node varAssignmentNode = new Node(ASSIGN, new Node(TOKEN_VAR, "a"), new Node(TOKEN_FUNCTION));
    functionNode.addChildToEnd(varAssignmentNode);

    Node functionParentNode = new Node(TOKEN_SCRIPT);

    NodeTraversal traversal = new NodeTraversal(functionParentNode, checkGlobalThis);
    traversal.traverse(functionNode, null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, functionParentNode));
  }

  @Test
  public void testShouldTraverse_FunctionGettingLentToPrototype() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node prototypeNode = new Node(TOKEN_OBJECT_LITERAL);
    Node prototypeLendsNameNode = new Node(TOKEN_STRING, "prototype");
    prototypeNode.addChildToEnd(new Node(TOKEN_PROPERTY, prototypeLendsNameNode, null, null, null, null));

    Node functionParentNode = new Node(TOKEN_SCRIPT);
    Node functionNode = new Node(TOKEN_FUNCTION);
    Node functionJsDocInfo = new Node(TOKEN_COMMENT, "@lends this.prototype");
    functionNode.setJSDocInfo(functionJsDocInfo);
    functionNode.addChildToEnd(new Node(Token.NAME, "getFoo"));

    functionNode.addChildToEnd(prototypeNode);

    Node functionTraversalParentNode = new Node(TOKEN_SCRIPT);
    NodeTraversal functionTraversal = new NodeTraversal(functionTraversalParentNode, checkGlobalThis);
    functionTraversal.traverse(functionNode, null);

    Node functionGetFooNode = functionNode.getChild(functionNode.getChildCount() - 1);
    Node getFooFunctionParentNode = functionGetFooNode.getParent();

    Node getFooFunctionParentParentNode = getFooFunctionParentNode.getParent();

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionGetFooNode, getFooFunctionParentParentNode));
  }

  @Test
  public void testShouldReportThis_Assignment() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node leftSideNode = new Node(TOKEN_NAME, "a");
    Node rightSideNode = new Node(TOKEN_THIS);

    Node assignmentNode = new Node(ASSIGN, leftSideNode, rightSideNode);

    Node parentNode = new Node(TOKEN_SCRIPT);

    NodeTraversal traversal = new NodeTraversal(parentNode, checkGlobalThis);
    traversal.traverse(assignmentNode, null);

    assertTrue(checkGlobalThis.shouldReportThis(assignmentNode));
  }

  @Test
  public void testShouldReportThis_GetProperty() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node parentNode = new Node(TOKEN_SCRIPT);
    Node thisNode = new Node(TOKEN_THIS);
    Node propertyAccessNode = new Node(TOKEN_GET_PROP, thisNode, new Node(TOKEN_STRING, "property"));

    NodeTraversal traversal = new NodeTraversal(parentNode, checkGlobalThis);
    traversal.traverse(propertyAccessNode, null);

    assertTrue(checkGlobalThis.shouldReportThis(propertyAccessNode));
  }

  @Test
  public void testShouldNotReportThis_PrototypePropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node parentNode = new Node(TOKEN_SCRIPT);
    Node thisNode = new Node(TOKEN_THIS);
    Node prototypeNode = new Node(TOKEN_OBJECT_LITERAL);
    Node prototypePropertyNode = new Node(TOKEN_GET_PROP, thisNode, new Node(TOKEN_STRING, "prototype"));
    prototypeNode.addChildToEnd(new Node(TOKEN_PROPERTY, prototypePropertyNode, null, null, null, null));

    NodeTraversal traversal = new NodeTraversal(parentNode, checkGlobalThis);
    traversal.traverse(prototypeNode, null);

    Node prototypeProperty = prototypeNode.getChild(prototypeNode.getChildCount() - 1);

    assertFalse(checkGlobalThis.shouldReportThis(prototypeProperty));
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThis_ThisAssignmentToVariable() {
    AbstractCompiler compiler = new AbstractCompiler(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node leftSideNode = new Node(TOKEN_NAME, "a");
    Node thisNode = new Node(TOKEN_THIS);

    Node assignmentNode = new Node(ASSIGN, leftSideNode, thisNode);

    Node parentNode = new Node(TOKEN_SCRIPT);

    NodeTraversal traversal = new NodeTraversal(parentNode, checkGlobalThis);
    traversal.traverse(assignmentNode, null);

    checkGlobalThis.visit(traversal, assignmentNode, parentNode);

    fail("Expected an error to be reported.");
  }
}