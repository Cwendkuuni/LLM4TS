import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compilerMock;

  @Before
  public void setup() {
    compilerMock = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
  }

  @Test
  public void testSafeUsesOfThis() {
    Node functionNode = createFunctionNode("function Foo() {}");
    Node traversal = new NodeTraversal(functionNode);
    checker.traverse(traversal, functionNode, null);
    // No error should be reported since this is a constructor
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testUnsafeUseOfThisInConstructor() {
    Node functionNode = createFunctionNode("function Foo() { this.x = 1; }");
    Node traversal = new NodeTraversal(functionNode);
    checker.traverse(traversal, functionNode, null);
    // Error should be reported since this is an assignment to a property
    // of the constructor's this
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getId());
  }

  @Test
  public void testUnsafeUseOfThisInPrototypeMethod() {
    Node functionNode = createFunctionNode("function Foo() {} Foo.prototype.bar = function() { this.x = 1; }");
    Node traversal = new NodeTraversal(functionNode);
    checker.traverse(traversal, functionNode, null);
    // No error should be reported since this is a prototype method
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithThisAnnotation() {
    Node functionNode = createFunctionNode(
        "function Foo@this() { this.x = 1; }",
        new JSDocInfo("@this", null)
    );
    Node traversal = new NodeTraversal(functionNode);
    checker.traverse(traversal, functionNode, null);
    // No error should be reported since this is a function annotated with @this
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithoutThisAnnotation() {
    Node functionNode = createFunctionNode("function Foo() { this.x = 1; }");
    Node traversal = new NodeTraversal(functionNode);
    checker.traverse(traversal, functionNode, null);
    // Error should be reported since this is an assignment to a property
    // of the function's this without a @this annotation
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getId());
  }

  @Test
  public void testUnsafeUseOfThisInAssignment() {
    Node assignNode = createAssignmentNode("a = this;");
    Node traversal = new NodeTraversal(assignNode);
    checker.traverse(traversal, assignNode, null);
    // No error should be reported since this is an assignment to a variable
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testUnsafeUseOfThisInAssignmentWithPropertyAccess() {
    Node assignNode = createAssignmentNode("a.x = this;");
    Node traversal = new NodeTraversal(assignNode);
    checker.traverse(traversal, assignNode, null);
    // Error should be reported since this is an assignment to a property
    // of the assignment's this
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals(DiagnosticType.WARNING, diagnosticType);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getId());
  }

  private Node createFunctionNode(String code) {
    return createFunctionNode(code, null);
  }

  private Node createFunctionNode(String code, JSDocInfo jsDocInfo) {
    Node node = new Node();
    when(compilerMock.parse(code, null)).thenReturn(node);
    node.setJSDocInfo(jsDocInfo);
    return node;
  }

  private Node createAssignmentNode(String code) {
    Node functionNode = createFunctionNode(
        "function Foo() {}",
        new JSDocInfo("Foo", new JSDocInfo.Type("function"))
    );
    Node assignNode = functionNode.getLastChild();
    Node leftSide = assignNode.getPrevious();
    Node rightSide = new Node();
    when(compilerMock.parse(code.substring(code.indexOf("=") + 1), null)).thenReturn(rightSide);
    assignNode.setFirstChild(leftSide);
    assignNode.setLastChild(rightSide);
    return assignNode;
  }
}