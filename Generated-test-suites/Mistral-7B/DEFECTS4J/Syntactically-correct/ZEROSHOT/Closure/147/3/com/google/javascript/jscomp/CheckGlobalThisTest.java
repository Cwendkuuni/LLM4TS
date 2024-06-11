import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckLevel level;
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setup() {
    level = new CheckLevel(Message.Warning, CheckLevel.Warning);
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo("constructor");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo("this");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo("override");
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypePropertyAccess() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeAccessNode = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    Node prototypePropertyAccessNode = new Node(Token.GETPROP, prototypeAccessNode, new Node(Token.STRING, "property"));
    functionNode.addChildToEnd(prototypePropertyAccessNode);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeSubPropertyAccess() {
    Node functionNode = new Node(Token.FUNCTION);
    Node prototypeAccessNode = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    Node prototypePropertyAccessNode = new Node(Token.GETPROP, prototypeAccessNode, new Node(Token.STRING, "subProperty"));
    functionNode.addChildToEnd(prototypePropertyAccessNode);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverseFunctionWithNoPrototypeAccess() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverseAssignmentToLeftSide() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.THIS);
    assignNode.addChildToEnd(leftSideNode);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), assignNode, parentNode);

    assertTrue(result);
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    Node prototypePropertyNode = new Node(Token.GETPROP, leftSideNode, new Node(Token.STRING, "property"));
    assignNode.addChildToEnd(prototypePropertyNode);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), assignNode, parentNode);

    assertFalse(result);
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubProperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    Node prototypePropertyNode = new Node(Token.GETPROP, leftSideNode, new Node(Token.STRING, "subProperty"));
    assignNode.addChildToEnd(prototypePropertyNode);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), assignNode, parentNode);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverseAssignmentToNonPrototypeProperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.THIS);
    Node propertyNode = new Node(Token.GETPROP, leftSideNode, new Node(Token.STRING, "property"));
    assignNode.addChildToEnd(propertyNode);

    Node parentNode = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), assignNode, parentNode);

    assertTrue(result);
  }
}