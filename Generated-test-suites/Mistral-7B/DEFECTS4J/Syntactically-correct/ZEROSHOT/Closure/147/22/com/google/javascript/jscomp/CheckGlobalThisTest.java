import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;
  private NodeTraversal traversal;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = NodeFactory.getInstance();
    traversal = new NodeTraversal(nodeFactory, compiler) {
      @Override
      public boolean shouldTraverse(Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };
    CheckLevel level = CheckLevel. WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node functionNode = nodeFactory.createNode(Token.FUNCTION);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node functionNode = nodeFactory.createNode(Token.FUNCTION, Token.FUNCTION, 0, null, null, null);
    Node parentNode = nodeFactory.createNode(Token.NAME, "MyConstructor", null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = nodeFactory.createNode(Token.FUNCTION, Token.FUNCTION, 0, null, null, null);
    Node thisAnnotationNode = nodeFactory.createNode(Token.IDENT, "@this", null);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    functionNode.addChildToEnd(thisAnnotationNode);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = nodeFactory.createNode(Token.FUNCTION, Token.FUNCTION, 0, null, null, null);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = nodeFactory.createNode(Token.ASSIGN, null, null);
    Node leftSideNode = nodeFactory.createNode(Token.THIS, null, null);
    Node rightSideNode = nodeFactory.createNode(Token.NUMBER, "42", null);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    assignNode.addChildToEnd(leftSideNode);
    assignNode.addChildToEnd(rightSideNode);
    parentNode.addChildToEnd(assignNode);

    checkGlobalThis.visit(traversal, leftSideNode, parentNode);
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node assignNode = nodeFactory.createNode(Token.ASSIGN, null, null);
    Node leftSideNode = nodeFactory.createNode(Token.IDENT, "obj", null);
    Node getNode = nodeFactory.createNode(Token.GETPROP, null, leftSideNode);
    Node prototypeNode = nodeFactory.createNode(Token.STRING, "prototype", null);
    Node rightSideNode = nodeFactory.createNode(Token.NUMBER, "42", null);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    getNode.addChildToEnd(prototypeNode);
    assignNode.addChildToEnd(leftSideNode);
    assignNode.addChildToEnd(getNode);
    assignNode.addChildToEnd(rightSideNode);
    parentNode.addChildToEnd(assignNode);

    checkGlobalThis.visit(traversal, rightSideNode, parentNode);
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubProperty() {
    Node assignNode = nodeFactory.createNode(Token.ASSIGN, null, null);
    Node leftSideNode = nodeFactory.createNode(Token.IDENT, "obj", null);
    Node getNode1 = nodeFactory.createNode(Token.GETPROP, null, leftSideNode);
    Node getNode2 = nodeFactory.createNode(Token.GETPROP, null, getNode1);
    Node prototypeNode = nodeFactory.createNode(Token.STRING, "prototype", null);
    Node rightSideNode = nodeFactory.createNode(Token.NUMBER, "42", null);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    getNode1.addChildToEnd(prototypeNode);
    getNode2.addChildToEnd(getNode1);
    assignNode.addChildToEnd(leftSideNode);
    assignNode.addChildToEnd(getNode2);
    assignNode.addChildToEnd(rightSideNode);
    parentNode.addChildToEnd(assignNode);

    checkGlobalThis.visit(traversal, rightSideNode, parentNode);
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignNode = nodeFactory.createNode(Token.ASSIGN, null, null);
    Node leftSideNode = nodeFactory.createNode(Token.THIS, null, null);
    Node rightSideNode = nodeFactory.createNode(Token.NUMBER, "42", null);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    assignNode.addChildToEnd(leftSideNode);
    assignNode.addChildToEnd(rightSideNode);
    parentNode.addChildToEnd(assignNode);

    checkGlobalThis.visit(traversal, leftSideNode, parentNode);
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfPropertyAccess() {
    Node getNode = nodeFactory.createNode(Token.GETPROP, null, nodeFactory.createNode(Token.THIS, null, null));
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    parentNode.addChildToEnd(getNode);
    checkGlobalThis.visit(traversal, getNode, parentNode);
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignNode = nodeFactory.createNode(Token.ASSIGN, null, null);
    Node leftSideNode = nodeFactory.createNode(Token.IDENT, "a", null);
    Node thisNode = nodeFactory.createNode(Token.THIS, null, null);
    Node rightSideNode = nodeFactory.createNode(Token.ASSIGN, null, thisNode);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    assignNode.addChildToEnd(leftSideNode);
    assignNode.addChildToEnd(rightSideNode);
    parentNode.addChildToEnd(assignNode);

    checkGlobalThis.visit(traversal, thisNode, parentNode);
  }

  @Test
  public void testShouldNotReportThisKeywordOnFunctionDeclaration() {
    Node functionNode = nodeFactory.createNode(Token.FUNCTION, Token.FUNCTION, 0, null, null, null);
    Node parentNode = nodeFactory.createNode(Token.SCRIPT, null, null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    parentNode.addChildToEnd(functionNode);
    checkGlobalThis.visit(traversal, functionNode, parentNode);
  }

  @Test
  public void testShouldNotReportThisKeywordOnMethodDeclaration() {
    Node methodNode = nodeFactory.createNode(Token.FUNCTION, Token.METHOD, 0, null, null, null);
    Node parentNode = nodeFactory.createNode(Token.NAME, "MyObject", null);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    parentNode.addChildToEnd(methodNode);
    checkGlobalThis.visit(traversal, methodNode, parentNode);
  }
}