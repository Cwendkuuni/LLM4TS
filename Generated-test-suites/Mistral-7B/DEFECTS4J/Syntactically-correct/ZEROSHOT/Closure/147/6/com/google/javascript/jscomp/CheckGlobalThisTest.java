import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private CheckGlobalThis checkerUnderTest;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = mock(Checker.class);
    CheckerFactory checkerFactory = mock(CheckerFactory.class);
    when(checkerFactory.getCheck(CheckLevel.WARNING, DiagnosticType.warning)).thenReturn(checkerUnderTest);
    when(checker.getFactory()).thenReturn(checkerFactory);
    Parser parser = new Parser(new DiagnosticReporter());
    Node node = parser.parse("function evil() { this.useful = undefined; }", null, null).getTree();
    traversal = new NodeTraversal(node, checker, compiler);
  }

  @Test
  public void shouldNotTraverseNonGlobalContexts() {
    Node nonGlobalNode = new Node(Token.FUNCTION, 0, null);
    assertFalse(checkerUnderTest.shouldTraverse(traversal, nonGlobalNode, null));
  }

  @Test
  public void shouldTraverseFunctionsThatAreConstructors() {
    Node constructorNode = new Node(Token.FUNCTION, 0, null);
    JSDocInfo jsDoc = new JSDocInfo("constructor", null, null, null, null, null);
    constructorNode.setJSDocInfo(jsDoc);
    assertTrue(checkerUnderTest.shouldTraverse(traversal, constructorNode, null));
  }

  @Test
  public void shouldTraverseFunctionsWithThisAnnotation() {
    Node thisAnnotatedNode = new Node(Token.FUNCTION, 0, null);
    JSDocInfo jsDoc = new JSDocInfo(null, null, "@this", null, null, null);
    thisAnnotatedNode.setJSDocInfo(jsDoc);
    assertTrue(checkerUnderTest.shouldTraverse(traversal, thisAnnotatedNode, null));
  }

  @Test
  public void shouldTraverseFunctionsWithNoLogicalPlaceForThisAnnotation() {
    Node noLogicalPlaceNode = new Node(Token.FUNCTION, 0, null);
    Node parent = new Node(Token.SCRIPT, 0, null);
    Node child = new Node(Token.ASSIGN, 0, parent);
    Node lhs = new Node(Token.NAME, 0, child);
    Node rhs = new Node(Token.FUNCTION, 0, null);
    Node grandParent = new Node(Token.SCRIPT, 0, null);
    Node grandChild = new Node(Token.ASSIGN, 0, grandParent);
    Node grandLhs = new Node(Token.NAME, 0, grandChild);

    lhs.setParent(child);
    rhs.setParent(child);
    grandLhs.setParent(grandChild);
    grandChild.setParent(grandParent);
    parent.addChild(child);
    grandParent.addChild(grandChild);

    noLogicalPlaceNode.setParent(grandParent);

    assertTrue(checkerUnderTest.shouldTraverse(traversal, noLogicalPlaceNode, grandParent));
  }

  @Test
  public void shouldTraverseLeftSideOfAssignment() {
    Node assignmentNode = new Node(Token.ASSIGN, 0, null);
    Node lhs = new Node(Token.THIS, 0, assignmentNode);
    Node rhs = new Node(Token.NUMBER, 0, assignmentNode);

    lhs.setParent(assignmentNode);
    rhs.setParent(assignmentNode);

    assertTrue(checkerUnderTest.shouldReportThis(lhs, assignmentNode));
  }

  @Test
  public void shouldTraverseRightSideOfAssignment() {
    Node assignmentNode = new Node(Token.ASSIGN, 0, null);
    Node lhs = new Node(Token.NAME, 0, assignmentNode);
    Node rhs = new Node(Token.THIS, 0, assignmentNode);

    lhs.setParent(assignmentNode);
    rhs.setParent(assignmentNode);

    assertFalse(checkerUnderTest.shouldReportThis(rhs, assignmentNode));
  }

  @Test
  public void shouldNotTraversePropertyAccessToPrototype() {
    Node prototypePropertyAccessNode = new Node(Token.GETPROP, 0, null);
    prototypePropertyAccessNode.setLastChild(new Node(Token.STRING, 0, prototypePropertyAccessNode, "prototype"));

    Node parent = new Node(Token.ASSIGN, 0, null);
    Node lhs = new Node(Token.THIS, 0, parent);
    Node rhs = new Node(Token.GETPROP, 0, parent);
    rhs.setLastChild(prototypePropertyAccessNode);

    lhs.setParent(parent);
    rhs.setParent(parent);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, prototypePropertyAccessNode, parent));
  }

  @Test
  public void shouldNotTraverseNestedPropertyAccessToPrototype() {
    Node prototypePropertyAccessNode = new Node(Token.GETPROP, 0, null);
    prototypePropertyAccessNode.setLastChild(new Node(Token.STRING, 0, prototypePropertyAccessNode, "prototype"));

    Node parent = new Node(Token.ASSIGN, 0, null);
    Node lhs = new Node(Token.THIS, 0, parent);
    Node grandParent = new Node(Token.ASSIGN, 0, null);
    Node rhs = new Node(Token.GETPROP, 0, grandParent);
    rhs.setLastChild(prototypePropertyAccessNode);

    Node grandChild = new Node(Token.GETPROP, 0, parent);
    Node childLhs = new Node(Token.THIS, 0, grandParent);
    Node childRhs = new Node(Token.GETPROP, 0, grandParent);
    childRhs.setLastChild(prototypePropertyAccessNode);

    lhs.setParent(parent);
    grandParent.setParent(parent);
    rhs.setParent(grandParent);
    grandChild.setParent(parent);
    childLhs.setParent(grandParent);
    childRhs.setParent(grandParent);

    assertFalse(checkerUnderTest.shouldTraverse(traversal, prototypePropertyAccessNode, parent));
  }

  @Test
  public void shouldReportDangerousUseOfGlobalThis() {
    Node node = new Node(Token.THIS, 0, null);
    Node parent = new Node(Token.SCRIPT, 0, null);

    node.setParent(parent);

    checkerUnderTest.visit(traversal, node, parent);
    assertTrue(compiler.reportedErrorWithType(GLOBAL_THIS));
  }

  @Test
  public void shouldNotReportSafeUseOfThis() {
    Node node = new Node(Token.THIS, 0, null);
    Node parent = new Node(Token.FUNCTION, 0, null);
    JSDocInfo jsDoc = new JSDocInfo("constructor", null, null, null, null, null);
    parent.setJSDocInfo(jsDoc);

    node.setParent(parent);

    checkerUnderTest.visit(traversal, node, parent);
    assertFalse(compiler.reportedErrorWithType(GLOBAL_THIS));
  }

  @Test
  public void shouldNotReportThisInsidePrototypeMethod() {
    Node node = new Node(Token.THIS, 0, null);
    Node parent = new Node(Token.GETPROP, 0, null);
    Node grandParent = new Node(Token.NAME, 0, parent);
    Node prototype = new Node(Token.STRING, 0, null, "prototype");
    Node prototypeProperty = new Node(Token.GETPROP, 0, null);
    prototypeProperty.setLastChild(prototype);

    grandParent.setLastChild(prototypeProperty);
    parent.setLastChild(grandParent);
    node.setParent(parent);

    checkerUnderTest.visit(traversal, node, parent);
    assertFalse(compiler.reportedErrorWithType(GLOBAL_THIS));
  }

  @Test
  public void shouldNotReportThisInsideFunctionAnnotatedWithAtConstructor() {
    Node node = new Node(Token.THIS, 0, null);
    Node parent = new Node(Token.FUNCTION, 0, null);
    JSDocInfo jsDoc = new JSDocInfo("constructor", null, null, null, null, null);
    parent.setJSDocInfo(jsDoc);

    node.setParent(parent);

    checkerUnderTest.visit(traversal, node, parent);
    assertFalse(compiler.reportedErrorWithType(GLOBAL_THIS));
  }

  @Test
  public void shouldNotReportThisInsideFunctionAnnotatedWithAtThis() {
    Node node = new Node(Token.THIS, 0, null);
    Node parent = new Node(Token.FUNCTION, 0, null);
    JSDocInfo jsDoc = new JSDocInfo(null, null, "@this", null, null, null);
    parent.setJSDocInfo(jsDoc);

    node.setParent(parent);

    checkerUnderTest.visit(traversal, node, parent);
    assertFalse(compiler.reportedErrorWithType(GLOBAL_THIS));
  }

  @Test
  public void shouldNotReportThisInFunctionWhereThereIsNoLogicalPlaceToPutThisAnnotation() {
    Node node = new Node(Token.THIS, 0, null);
    Node parent = new Node(Token.SCRIPT, 0, null);

    Node child = new Node(Token.ASSIGN, 0, null);
    Node lhs = new Node(Token.NAME, 0, child);
    Node rhs = new Node(Token.FUNCTION, 0, null);
    Node childParent = new Node(Token.ASSIGN, 0, null);
    Node childLhs = new Node(Token.NAME, 0, childParent);
    Node childRhs = new Node(Token.NAME, 0, childParent);
    childParent.setParent(parent);
    lhs.setParent(childParent);
    rhs.setParent(child);
    childLhs.setParent(childParent);
    childRhs.setParent(childParent);

    node.setParent(parent);

    checkerUnderTest.visit(traversal, node, parent);
    assertFalse(compiler.reportedErrorWithType(GLOBAL_THIS));
  }
}