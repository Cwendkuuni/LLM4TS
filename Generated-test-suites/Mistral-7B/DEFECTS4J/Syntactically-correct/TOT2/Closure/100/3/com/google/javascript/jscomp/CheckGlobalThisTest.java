import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private CheckLevel level;
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    level = mock(CheckLevel.class);
    compiler = mock(AbstractCompiler.class);
    when(compiler.report(any(ErrorReporter.class), eq(level), eq(DiagnosticType.class), anyInt(), anyString(), anyString()))
        .thenReturn(true);
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorNode = new Node(Token.FUNCTION);
    JSDocInfo constructorJsDoc = new JSDocInfo();
    constructorJsDoc.setConstructor(true);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.setFirstChild(constructorNode);

    assertFalse(checkGlobalThis.shouldTraverse(null, null, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo functionJsDoc = new JSDocInfo();
    functionJsDoc.setThisType(true);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.setFirstChild(functionNode);

    assertFalse(checkGlobalThis.shouldTraverse(null, null, parentNode));
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    assertNotNull(checkGlobalThis.shouldTraverse(null, null, functionNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.IDENTIFIER);
    Node rightSideNode = new Node(Token.NUMBER);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.setFirstChild(assignNode);
    assignNode.setFirstChild(leftSideNode);
    assignNode.setLastChild(rightSideNode);

    assertNotNull(checkGlobalThis.shouldTraverse(null, null, parentNode));
  }

  @Test
  public void testShouldNotTraversePrototypeProperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.GETPROP);
    leftSideNode.setLastChild(new Node(Token.STRING, "prototype"));
    Node rightSideNode = new Node(Token.NUMBER);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.setFirstChild(assignNode);
    assignNode.setFirstChild(leftSideNode);
    assignNode.setLastChild(rightSideNode);

    assertFalse(checkGlobalThis.shouldTraverse(null, null, parentNode));
  }

  @Test
  public void testShouldNotTraversePrototypeSubproperty() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSideNode = new Node(Token.GETPROP);
    leftSideNode.setLastChild(new Node(Token.STRING, "prototype"));
    leftSideNode.setFirstChild(new Node(Token.IDENTIFIER, "a"));
    Node rightSideNode = new Node(Token.NUMBER);

    Node parentNode = new Node(Token.BLOCK);
    parentNode.setFirstChild(assignNode);
    assignNode.setFirstChild(leftSideNode);
    assignNode.setLastChild(rightSideNode);

    assertFalse(checkGlobalThis.shouldTraverse(null, null, parentNode));
  }
}