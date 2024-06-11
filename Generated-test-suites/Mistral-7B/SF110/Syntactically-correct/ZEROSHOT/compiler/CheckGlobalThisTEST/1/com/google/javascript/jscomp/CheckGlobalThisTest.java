import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private static final DiagnosticType GLOBAL_THIS = DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object");

  @org.junit.Test
  public void testShouldTraverseFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("myFunction", null, null, false, null);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    assertEquals(true, checker.shouldTraverse(traversal, functionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructorFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node constructorFunctionNode = Node.newFunction("MyConstructor", null, null, true, null);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    assertEquals(false, checker.shouldTraverse(traversal, constructorFunctionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionAnnotatedWithThisNode = Node.newFunction("myFunction", null, null, false, "@this");

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    assertEquals(false, checker.shouldTraverse(traversal, functionAnnotatedWithThisNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWhereNoLogicalPlaceToPutThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("myFunction", null, null, false, null);
    Node objLiteral = Node.newObjectLiteral();
    Node propertyAssignment = Node.newAssignment(objLiteral, Node.newName(" property "), functionNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    assertEquals(true, checker.shouldTraverse(traversal, functionNode, null));
    assertEquals(false, checker.shouldTraverse(traversal, propertyAssignment, functionNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionGettingLentToPrototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("myFunction", null, null, false, null);
    Node objectLiteral = Node.newObjectLiteral();
    Node prototypePropertyAssignment = Node.newAssignment(objectLiteral, Node.newName("prototype.property"), functionNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    assertEquals(false, checker.shouldTraverse(traversal, functionNode, objectLiteral));
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newThis());

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.makeError(Node.newName("a"), GLOBAL_THIS)).thenReturn(null);

    checker.visit(traversal, traversal.getCurrentNode(), null);
    checker.visit(traversal, assignmentNode, null);

    assertEquals(1, traversal.getDiagnostics().size());
  }

  @org.junit.Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node assignmentNode = Node.newAssignment(Node.newName("a"), Node.newThis());

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.makeError(Node.newName("a"), GLOBAL_THIS)).thenReturn(null);

    checker.visit(traversal, traversal.getCurrentNode(), null);
    checker.visit(traversal, Node.newName("a"), null);
    checker.visit(traversal, assignmentNode, null);

    assertEquals(0, traversal.getDiagnostics().size());
  }

  @org.junit.Test
  public void testShouldNotReportThisInsideObjectLiteral() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node objectLiteral = Node.newObjectLiteral();
    Node thisInsideObjectLiteral = Node.newThis();

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.makeError(Node.newName("a"), GLOBAL_THIS)).thenReturn(null);

    checker.visit(traversal, traversal.getCurrentNode(), null);
    checker.visit(traversal, objectLiteral, null);
    checker.visit(traversal, thisInsideObjectLiteral, null);

    assertEquals(0, traversal.getDiagnostics().size());
  }

  @org.junit.Test
  public void testShouldReportThisOnPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node propertyAccessNode = Node.newGetProp(Node.newName("a"), Node.newName("prototype"));

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.makeError(Node.newName("a"), GLOBAL_THIS)).thenReturn(null);

    checker.visit(traversal, traversal.getCurrentNode(), null);
    checker.visit(traversal, Node.newName("a"), null);
    checker.visit(traversal, propertyAccessNode, null);

    assertEquals(1, traversal.getDiagnostics().size());
  }

  @org.junit.Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("myFunction", null, null, false, null);
    Node constructorFunctionNode = Node.newFunction("MyConstructor", null, null, true, null);
    Node varFunctionNode = Node.newFunction("myFunction", null, null, false, null, Node.newName("x"));
    Node varVarFunctionNode = Node.newFunction("myFunction", null, null, false, null, Node.newVar("x"));

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    JSDocInfo functionJsDocInfo = checker.getFunctionJsDocInfo(functionNode);
    JSDocInfo constructorJsDocInfo = checker.getFunctionJsDocInfo(constructorFunctionNode);
    JSDocInfo varFunctionJsDocInfo = checker.getFunctionJsDocInfo(varFunctionNode);
    JSDocInfo varVarFunctionJsDocInfo = checker.getFunctionJsDocInfo(varVarFunctionNode);

    assertEquals(null, functionJsDocInfo);
    assertEquals(null, constructorJsDocInfo);
    assertEquals(null, varFunctionJsDocInfo);
    assertEquals(null, varVarFunctionJsDocInfo);

    JSDocInfo functionJsDocInfoWithDocs = Node.newFunction("myFunction", null, null, false, "@this @constructor");
    JSDocInfo functionJsDocInfoWithDocsAndInterface = Node.newFunction("myFunction", null, null, false, "@this @constructor @interface MyInterface");

    when(functionNode.getJSDocInfo()).thenReturn(functionJsDocInfoWithDocs);
    when(constructorFunctionNode.getJSDocInfo()).thenReturn(constructorJsDocInfoWithDocs);

    JSDocInfo functionJsDocInfoWithDocsResult = checker.getFunctionJsDocInfo(functionNode);
    JSDocInfo constructorJsDocInfoWithDocsResult = checker.getFunctionJsDocInfo(constructorFunctionNode);

    assertEquals(functionJsDocInfoWithDocs, functionJsDocInfoWithDocsResult);
    assertEquals(constructorJsDocInfo, constructorJsDocInfoWithDocsResult);
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWhereParentIsNotFunctionOrObjectLiteral() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("myFunction", null, null, false, null);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    Node parentNode = Node.newName("parentNode");
    when(functionNode.getParent()).thenReturn(parentNode);

    assertEquals(false, checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWhereParentIsBlock() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("myFunction", null, null, false, null);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    Node parentNode = Node.newBlock();
    when(functionNode.getParent()).thenReturn(parentNode);

    assertEquals(false, checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWhereParentIsScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunction("myFunction", null, null, false, null);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(traversal.getCurrentScope()).thenReturn(null);

    Node parentNode = Node.newScript();
    when(functionNode.getParent()).thenReturn(parentNode);

    assertEquals(false, checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  private static class CompilerOptions {}
}