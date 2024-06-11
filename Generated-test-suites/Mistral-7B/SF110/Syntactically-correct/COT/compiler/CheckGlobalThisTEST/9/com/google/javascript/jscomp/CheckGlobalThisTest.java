import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.RunWithMockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticContext;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(RunWithMockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @InjectMocks
  private CheckGlobalThis checkGlobalThis;

  private AbstractCompiler compilerMock;
  private DiagnosticContext diagnosticContextMock;
  private NodeTraversal nodeTraversalMock;

  @Before
  public void setup() {
    compilerMock = mock(AbstractCompiler.class);
    diagnosticContextMock = mock(DiagnosticContext.class);
    nodeTraversalMock = mock(NodeTraversal.class);

    when(compilerMock.getContext()).thenReturn(diagnosticContextMock);
    when(compilerMock.getTraverser()).thenReturn(nodeTraversalMock);

    checkGlobalThis = new CheckGlobalThis(compilerMock);
  }

  @Test
  public void shouldNotTraverseNonGlobalContexts() {
    Node nonGlobalContext = Node.newNode(Token.INT_LITERAL, null, null);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversalMock, nonGlobalContext, null));
  }

  @Test
  public void shouldTraverseFunctionPrototypeMethods() {
    Node functionPrototype = Node.newNode(Token.FUNCTION, null, null);
    Node prototype = Node.newNode(Token.OBJECT_LITERAL, null, null);
    prototype.addChildToEnd(functionPrototype);

    Node functionPrototypeCallSite = Node.newNode(Token.GETPROP, null, functionPrototype);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversalMock, functionPrototypeCallSite, prototype));
  }

  @Test
  public void shouldTraverseFunctionsAnnotatedWithAtConstructor() {
    Node constructorFunction = Node.newNode(Token.FUNCTION, null, null);
    constructorFunction.setAttribute(JSDocInfo.CONSTRUCTOR, true);

    Node constructorFunctionCallSite = Node.newNode(Token.CALL, null, constructorFunction);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversalMock, constructorFunctionCallSite, null));
  }

  @Test
  public void shouldTraverseFunctionsAnnotatedWithAtThis() {
    Node thisAnnotatedFunction = Node.newNode(Token.FUNCTION, null, null);
    thisAnnotatedFunction.setAttribute(JSDocInfo.THIS, "MyType");

    Node thisAnnotatedFunctionCallSite = Node.newNode(Token.CALL, null, thisAnnotatedFunction);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversalMock, thisAnnotatedFunctionCallSite, null));
  }

  @Test
  public void shouldTraverseFunctionsWhereThisCouldBeLogicallyPlaced() {
    Node functionWithoutAnnotation = Node.newNode(Token.FUNCTION, null, null);

    Node functionCallSite1 = Node.newNode(Token.CALL, null, functionWithoutAnnotation);
    Node functionCallSite2 = Node.newNode(Token.SCRIPT, null, null);
    functionCallSite2.addChildToEnd(functionCallSite1);

    Node block = Node.newNode(Token.BLOCK, null, null);
    functionCallSite2.addChildToEnd(block);

    Node assignStatement = Node.newNode(Token.ASSIGN, null, null);
    assignStatement.setFirstChild(Node.newNode(Token.NAME, "a", null));
    assignStatement.setLastChild(functionWithoutAnnotation);
    block.addChildToEnd(assignStatement);

    Node objectLiteral = Node.newNode(Token.OBJECT_LITERAL, null, null);
    Node getProp = Node.newNode(Token.GETPROP, null, Node.newName("x"));
    getProp.setLastChild(functionWithoutAnnotation);
    objectLiteral.addChildToEnd(getProp);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversalMock, functionWithoutAnnotation, block));
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversalMock, functionWithoutAnnotation, objectLiteral));
  }

  @Test
  public void shouldNotTraverseFunctionsLentToPrototypeProperties() {
    Node functionLentToPrototype = Node.newNode(Token.FUNCTION, null, null);

    Node objectLiteral = Node.newNode(Token.OBJECT_LITERAL, null, null);
    Node getProp = Node.newNode(Token.GETPROP, null, Node.newName("prototype"));
    getProp.setLastChild(functionLentToPrototype);
    objectLiteral.addChildToEnd(getProp);

    Node functionCallSite = Node.newNode(Token.CALL, null, functionLentToPrototype);

    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversalMock, functionCallSite, objectLiteral));
  }

  @Test
  public void shouldReportGlobalThis() {
    Node globalThis = Node.newNode(Token.THIS, null, null);

    Node functionCallSite = Node.newNode(Token.CALL, null, globalThis);

    when(nodeTraversalMock.makeError(globalThis, GLOBAL_THIS)).thenAnswer(new Answer<Node>() {
      @Override
      public Node answer(InvocationOnMock invocation) throws Throwable {
        Node errorNode = (Node) invocation.getArguments()[0];
        errorNode.setLineNumber(1);
        errorNode.setColumnNumber(1);
        return errorNode;
      }
    });

    Node parent = Node.newNode(Token.SCRIPT, null, null);

    checkGlobalThis.visit(nodeTraversalMock, null, parent);
    checkGlobalThis.visit(nodeTraversalMock, functionCallSite, parent);

    Node errors = diagnosticContextMock.getDiagnostics();

    assertNotNull(errors);
    assertFalse(errors.isEmpty());
    assertFalse(errors.get(0).getSeverity().isError());
  }

  @Test
  public void shouldNotReportThisOnLeftSideOfAssignment() {
    Node assignment = Node.newNode(Token.ASSIGN, null, Node.newName("a"), Node.newNode(Token.THIS, null, null));

    Node parent = Node.newNode(Token.SCRIPT, null, null);

    checkGlobalThis.visit(nodeTraversalMock, null, parent);
    checkGlobalThis.visit(nodeTraversalMock, assignment, parent);

    Node errors = diagnosticContextMock.getDiagnostics();

    assertNotNull(errors);
    assertFalse(errors.isEmpty());
    assertFalse(errors.get(0).getSeverity().isError());
  }

  @Test
  public void shouldNotReportThisOnPropertyAccess() {
    Node propertyAccess = Node.newNode(Token.GETPROP, null, Node.newName("a"), Node.newName("this"));

    Node parent = Node.newNode(Token.SCRIPT, null, null);

    checkGlobalThis.visit(nodeTraversalMock, null, parent);
    checkGlobalThis.visit(nodeTraversalMock, propertyAccess, parent);

    Node errors = diagnosticContextMock.getDiagnostics();

    assertNotNull(errors);
    assertFalse(errors.isEmpty());
    assertFalse(errors.get(0).getSeverity().isError());
  }

  @Test
  public void shouldNotReportThisOnLeftSideOfPropertyAccess() {
    Node propertyAccess = Node.newNode(Token.GETPROP, null, Node.newName("a.this"), null);

    Node parent = Node.newNode(Token.SCRIPT, null, null);

    checkGlobalThis.visit(nodeTraversalMock, null, parent);
    checkGlobalThis.visit(nodeTraversalMock, propertyAccess, parent);

    Node errors = diagnosticContextMock.getDiagnostics();

    assertNotNull(errors);
    assertFalse(errors.isEmpty());
    assertFalse(errors.get(0).getSeverity().isError());
  }

  private boolean assertTrue(boolean condition) {
    if (!condition) {
      fail("Expected true but got false");
    }
    return true;
  }

  private void fail(String message) {
    throw new AssertionError(message);
  }
}