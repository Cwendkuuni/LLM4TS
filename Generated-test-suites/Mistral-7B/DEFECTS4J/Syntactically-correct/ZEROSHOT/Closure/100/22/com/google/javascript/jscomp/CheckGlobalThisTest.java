import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compilerMock;

  @org.junit.Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
  }

  @org.junit.Test
  public void shouldNotTraverseNonGlobalContexts() {
    NodeTraversal traversalMock = mock(NodeTraversal.class);
    Node nonGlobalNode = Node.newInstance(Token.STRING, "nonGlobal");

    when(compilerMock.getTraversal()).thenReturn(traversalMock);
    when(compilerMock.getSourceName()).thenReturn("test.js");

    Node parentNode = Node.newInstance(Token.NAME, "parentNode");
    Node nodeToTest = Node.newInstance(Token.FUNCTION, null, null, null, null, null, null, null);

    boolean result = checker.shouldTraverse(traversalMock, nodeToTest, parentNode);
    assertEquals(false, result);
  }

  @org.junit.Test
  public void shouldNotTraverseConstructorsOrFunctionsWithThisAnnotation() {
    NodeTraversal traversalMock = mock(NodeTraversal.class);
    Node constructorNode = Node.newInstance(Token.FUNCTION, null, null, null, null, null, null, null);
    Node annotatedFunctionNode = Node.newInstance(Token.FUNCTION, null, null, null, null, null, null, null);

    JSDocInfo constructorJsDoc = Node.newInstance(JSDocInfo.class);
    constructorJsDoc.setConstructor(true);

    JSDocInfo annotatedFunctionJsDoc = Node.newInstance(JSDocInfo.class);
    annotatedFunctionJsDoc.setThisType(true);

    when(compilerMock.getTraversal()).thenReturn(traversalMock);
    when(compilerMock.getSourceName()).thenReturn("test.js");
    when(constructorNode.getJSDocInfo()).thenReturn(constructorJsDoc);
    when(annotatedFunctionNode.getJSDocInfo()).thenReturn(annotatedFunctionJsDoc);

    Node constructorParentNode = Node.newInstance(Token.NAME, "constructorParentNode");
    Node annotatedFunctionParentNode = Node.newInstance(Token.NAME, "annotatedFunctionParentNode");

    boolean constructorResult = checker.shouldTraverse(traversalMock, constructorNode, constructorParentNode);
    assertEquals(false, constructorResult);

    boolean annotatedFunctionResult = checker.shouldTraverse(traversalMock, annotatedFunctionNode, annotatedFunctionParentNode);
    assertEquals(false, annotatedFunctionResult);
  }

  @org.junit.Test
  public void shouldTraverseFunctionLeftSideOfAssignment() {
    NodeTraversal traversalMock = mock(NodeTraversal.class);
    Node assignmentNode = Node.newInstance(Token.ASSIGN, null, null, null, null, null, null, null);
    Node functionNode = Node.newInstance(Token.FUNCTION, null, null, null, null, null, null, null);

    when(compilerMock.getTraversal()).thenReturn(traversalMock);
    when(compilerMock.getSourceName()).thenReturn("test.js");
    when(assignmentNode.getFirstChild()).thenReturn(functionNode);

    Node assignmentParentNode = Node.newInstance(Token.ASSIGN, null, null, null, null, null, null, null);

    boolean result = checker.shouldTraverse(traversalMock, functionNode, assignmentParentNode);
    assertEquals(true, result);
  }

  @org.junit.Test
  public void shouldReportGlobalThis() {
    NodeTraversal traversalMock = mock(NodeTraversal.class);
    Node globalThisNode = Node.newInstance(Token.THIS, null, null, null, null, null, null, null);
    Node globalThisParentNode = Node.newInstance(Token.NAME, "globalThisParentNode");

    when(compilerMock.getTraversal()).thenReturn(traversalMock);
    when(compilerMock.getSourceName()).thenReturn("test.js");

    checker.visit(traversalMock, globalThisNode, globalThisParentNode);

    DiagnosticType diagnosticType = checker.GLOBAL_THIS;
    assertEquals(diagnosticType.getName(), "JSC_USED_GLOBAL_THIS");
    assertEquals(1, compilerMock.getDiagnostics().size());
    assertEquals(diagnosticType, compilerMock.getDiagnostics().get(0));
  }

  @org.junit.Test
  public void shouldNotReportThisWithPropertyAccess() {
    NodeTraversal traversalMock = mock(NodeTraversal.class);
    Node thisNode = Node.newInstance(Token.THIS, null, null, null, null, null, null, null);
    Node propertyAccessNode = Node.newInstance(Token.GETPROP, null, null, null, null, null, null, null);
    Node propertyAccessParentNode = Node.newInstance(Token.NAME, "propertyAccessParentNode");

    when(compilerMock.getTraversal()).thenReturn(traversalMock);
    when(compilerMock.getSourceName()).thenReturn("test.js");

    checker.visit(traversalMock, thisNode, propertyAccessParentNode);

    assertEquals(0, compilerMock.getDiagnostics().size());
  }
}