import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node parentNonGlobal = nodeFactory.createNode(Token.NUMBER, null, null, null);
    Node functionNonGlobal = nodeFactory.createNode(Token.FUNCTION, null, null, null);

    when(nodeFactory.createNode(Token.FUNCTION, null, null, null)).thenReturn(functionNonGlobal);

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNonGlobal, parentNonGlobal));
  }

  @Test
  public void testShouldTraversePrototypeMethods() {
    Node parentPrototype = nodeFactory.createNode(Token.GETPROP, null, null, null);
    Node prototypeMethod = nodeFactory.createNode(Token.FUNCTION, null, null, null);

    parentPrototype.setLastChild(prototypeMethod);
    when(nodeFactory.createNode(Token.GETPROP, null, null, null)).thenReturn(parentPrototype);

    Node functionNonPrototype = nodeFactory.createNode(Token.FUNCTION, null, null, null);

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, prototypeMethod, functionNonPrototype));
    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNonPrototype, parentPrototype));
  }

  @Test
  public void testShouldTraverseConstructors() {
    Node parentConstructor = nodeFactory.createNode(Token.FUNCTION, null, null, new JSDocInfo("constructor", null, null));
    Node constructor = nodeFactory.createNode(Token.FUNCTION, null, null, null);

    when(nodeFactory.createNode(Token.FUNCTION, null, null, null)).thenReturn(constructor);

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, constructor, parentConstructor));
  }

  @Test
  public void testShouldTraverseFunctionsAnnotatedWithThis() {
    Node parentThis = nodeFactory.createNode(Token.FUNCTION, null, null, new JSDocInfo("", null, new String[]{"@this"}));
    Node functionThis = nodeFactory.createNode(Token.FUNCTION, null, null, null);

    when(nodeFactory.createNode(Token.FUNCTION, null, null, null)).thenReturn(functionThis);

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionThis, parentThis));
  }

  @Test
  public void testShouldTraverseFunctionsWithNoLogicalPlaceForThisAnnotation() {
    Node parentFunction = nodeFactory.createNode(Token.FUNCTION, null, null, null);
    Node function = nodeFactory.createNode(Token.FUNCTION, null, null, null);

    when(nodeFactory.createNode(Token.FUNCTION, null, null, null)).thenReturn(function);

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, function, parentFunction));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node parentAssignment = nodeFactory.createNode(Token.ASSIGN, null, null, null);
    Node leftSide = nodeFactory.createNode(Token.THIS, null, null, null);
    Node rightSide = nodeFactory.createNode(Token.NUMBER, null, null, null);

    parentAssignment.setFirstChild(leftSide);
    parentAssignment.setLastChild(rightSide);

    ArgumentCaptor<DiagnosticPosition> positionCaptor = ArgumentCaptor.forClass(DiagnosticPosition.class);
    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);

    checker.visit(NodeTraversal.EMPTY, leftSide, parentAssignment);

    when(compiler.report(nodeCaptor.capture(), eq(CheckLevel.WARNING), eq(GLOBAL_THIS)))
        .thenReturn(true);

    assertTrue(checker.visit(NodeTraversal.EMPTY, leftSide, parentAssignment));

    DiagnosticPosition position = positionCaptor.getValue();
    Node reportedNode = nodeCaptor.getValue();

    assertEquals("Expected reported node to be the same as the one passed to visit", leftSide, reportedNode);
    assertEquals("Expected the reported position to be the position of the THIS keyword", position.getStartPosition(), leftSide.getStartPosition());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node parentAssignment = nodeFactory.createNode(Token.ASSIGN, null, null, null);
    Node leftSide = nodeFactory.createNode(Token.NUMBER, null, null, null);
    Node rightSide = nodeFactory.createNode(Token.THIS, null, null, null);

    parentAssignment.setFirstChild(leftSide);
    parentAssignment.setLastChild(rightSide);

    assertFalse(checker.visit(NodeTraversal.EMPTY, rightSide, parentAssignment));
  }

  @Test
  public void testShouldNotReportThisOnPrototypePropertyAccess() {
    Node parentPrototypeProperty = nodeFactory.createNode(Token.GETPROP, null, null, null);
    Node prototype = nodeFactory.createNode(Token.GETPROP, null, null, null);
    Node thisKeyword = nodeFactory.createNode(Token.THIS, null, null, null);

    prototype.setLastChild(thisKeyword);
    parentPrototypeProperty.setLastChild(prototype);

    assertFalse(checker.visit(NodeTraversal.EMPTY, thisKeyword, parentPrototypeProperty));
  }

  @Test
  public void testShouldNotReportThisOnSubPropertyAccess() {
    Node parentPrototypeProperty = nodeFactory.createNode(Token.GETPROP, null, null, null);
    Node prototype = nodeFactory.createNode(Token.OBJLIT, null, null, null);
    Node thisKeyword = nodeFactory.createNode(Token.THIS, null, null, null);
    Node property = nodeFactory.createNode(Token.IDENT, null, null, "property");

    prototype.setLastChild(thisKeyword);
    parentPrototypeProperty.setLastChild(NodeUtil.createGetPropNode(prototype, property));

    assertFalse(checker.visit(NodeTraversal.EMPTY, thisKeyword, parentPrototypeProperty));
  }
}