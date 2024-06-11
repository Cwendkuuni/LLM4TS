import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node node = Node.newNode(Token.FUNCTION);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = Node.newNode(Token.BLOCK);

    when(compiler.report(Mockito.any(NodeTraversal.class), Mockito.eq(CheckLevel.WARNING),
        Mockito.eq(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "")))).thenReturn(null);

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotations() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node parent = Node.newNode(Token.SCRIPT);

    assertFalse(checker.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldTraversePrototypeMethod() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node prototypeNode = Node.newNode(Token.GETPROP, null, Node.newNode(Token.STRING, "prototype"));
    Node parent = Node.newNode(Token.NAME, "Object");

    Node functionPrototype = Node.newNode(Token.GETPROP, null, Node.newNode(Token.STRING, "prototype"));
    Node functionPrototypePrototype = Node.newNode(Token.NAME, "Object");

    parent.setFirstChild(Node.newNode(Token.ASSIGN, null, functionPrototypePrototype));
    functionPrototypePrototype.setFirstChild(prototypeNode);
    functionPrototype.setFirstChild(functionNode);

    assertFalse(checker.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node constructorAnnotation = Node.newNode(Token.ANNOTATION, null, Node.newNode(Token.STRING, "@constructor"));
    Node parent = Node.newNode(Token.SCRIPT);

    functionNode.setFirstChild(constructorAnnotation);

    assertFalse(checker.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node thisAnnotation = Node.newNode(Token.ANNOTATION, null, Node.newNode(Token.STRING, "@this"));
    Node parent = Node.newNode(Token.SCRIPT);

    functionNode.setFirstChild(thisAnnotation);

    assertFalse(checker.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotationOrParent() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node parent = null;

    assertFalse(checker.shouldTraverse(null, functionNode, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN, null, Node.newNode(Token.THIS), Node.newNode(Token.IDENT, "x"));
    Node parent = Node.newNode(Token.SCRIPT);

    checker.visit(null, assignNode, parent);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfGetProperty() {
    Node getPropertyNode = Node.newNode(Token.GETPROP, null, Node.newNode(Token.THIS), Node.newNode(Token.STRING, "x"));
    Node parent = Node.newNode(Token.SCRIPT);

    checker.visit(null, getPropertyNode, parent);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.THIS));
    Node parent = Node.newNode(Token.SCRIPT);

    checker.visit(null, assignNode, parent);
  }

  @Test
  public void testShouldReportThisWhenParentIsGetProperty() {
    Node getPropertyNode = Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "obj"), Node.newNode(Token.THIS));
    Node parent = Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "obj"), Node.newNode(Token.STRING, "x"));

    checker.visit(null, getPropertyNode, parent);
  }

  @Test
  public void testShouldReportThisWhenParentIsGetPropertyWithPrototype() {
    Node getPropertyNode = Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "obj"), Node.newNode(Token.STRING, "prototype"));
    Node getPrototypeNode = Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "prototype"), Node.newNode(Token.STRING, "x"));
    Node parent = Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "obj"), getPrototypeNode);

    checker.visit(null, getPropertyNode, parent);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node parent = Node.newNode(Token.ASSIGN, null, Node.newNode(Token.IDENT, "x"), functionNode);

    JSDocInfo jsDoc = checker.getFunctionJsDocInfo(functionNode);
    assertNotNull(jsDoc);

    functionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.ANNOTATION, null, Node.newNode(Token.STRING, "@constructor")));
    jsDoc = checker.getFunctionJsDocInfo(functionNode);
    assertNotNull(jsDoc);

    functionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.ANNOTATION, null, Node.newNode(Token.STRING, "@this")));
    jsDoc = checker.getFunctionJsDocInfo(functionNode);
    assertNotNull(jsDoc);

    functionNode = Node.newNode(Token.FUNCTION);
    jsDoc = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc);

    functionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.VAR, null, Node.newNode(Token.IDENT, "x"), Node.newNode(Token.FUNCTION)));
    jsDoc = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc);

    functionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.VAR, null, Node.newNode(Token.IDENT, "x")));
    jsDoc = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDoc);
  }
}