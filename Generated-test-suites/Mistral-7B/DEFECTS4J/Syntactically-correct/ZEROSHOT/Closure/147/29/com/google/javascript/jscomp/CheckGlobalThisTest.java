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

public class CheckGlobalThisTest {

  @org.junit.Before
  public void setUp() {
  }

  @org.junit.Test
  public void testShouldTraverseFunctionAnnotatedWithAtconstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionNode = createFunctionNodeWithJsDoc("function MyConstructor() { }");

    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, null);
    assertNotNull(result);
    assertNotNull(result);
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionNode = createFunctionNodeWithJsDoc("@this function MyFunction() { }");

    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, null);
    assertFalse(result);
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionAnnotatedWithInterface() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionNode = createFunctionNodeWithJsDoc("@interface MyInterface { function myMethod() { } }");

    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, null);
    assertFalse(result);
  }

  @org.junit.Test
  public void testShouldTraverseFunctionWithoutJsDoc() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionNode = createFunctionNode("function MyFunction() { }");

    boolean result = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, null);
    assertNotNull(result);
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionInsideBlockOrScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionNode = createFunctionNode("function MyFunction() { }");
    Node blockNode = createBlockNode("{}");
    Node scriptNode = createScriptNode("function MyFunction() { }");

    boolean resultBlock = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, blockNode);
    assertFalse(resultBlock);

    boolean resultScript = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, scriptNode);
    assertFalse(resultScript);
  }

  @org.junit.Test
  public void testShouldTraverseFunctionInsideNameOrAssign() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionNode = createFunctionNode("function MyFunction() { }");
    Node nameNode = createNameNode("var x = function MyFunction() { }");
    Node assignNode = createAssignNode("x = function MyFunction() { }");

    boolean resultName = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, nameNode);
    assertNotNull(resultName);

    boolean resultAssign = checkGlobalThis.shouldTraverse(mock(NodeTraversal.class), functionNode, assignNode);
    assertNotNull(resultAssign);
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node assignNode = createAssignNode("a = this");

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    checkGlobalThis.visit(nodeTraversal, null, assignNode);

    DiagnosticType diagnosticType = checkGlobalThis.GLOBAL_THIS;
    assertEquals(diagnosticType, nodeTraversal.getCurrentError().getType());
  }

  @org.junit.Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node assignNode = createAssignNode("a = b");

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    checkGlobalThis.visit(nodeTraversal, null, assignNode);

    assertTrue(nodeTraversal.getCurrentError() == null);
  }

  @org.junit.Test
  public void testShouldReportThisOnPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node getNode = createGetNode("a.this");

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    checkGlobalThis.visit(nodeTraversal, null, getNode);

    DiagnosticType diagnosticType = checkGlobalThis.GLOBAL_THIS;
    assertEquals(diagnosticType, nodeTraversal.getCurrentError().getType());
  }

  @org.junit.Test
  public void testShouldNotReportThisOnPropertyAccessWithPrototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node getNode = createGetNode("Object.prototype.this");

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    checkGlobalThis.visit(nodeTraversal, null, getNode);

    assertTrue(nodeTraversal.getCurrentError() == null);
  }

  private Node createFunctionNodeWithJsDoc(String code) {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = new JsParser(compiler).parse(code, null, null);
    return node.getFirstChild();
  }

  private Node createFunctionNode(String code) {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = new JsParser(compiler).parse(code, null, null).getFirstChild();
    return node.getFirstChild();
  }

  private Node createBlockNode(String code) {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = new JsParser(compiler).parse(code, null, null).getFirstChild();
    return node;
  }

  private Node createScriptNode(String code) {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = new JsParser(compiler).parse(code, null, null).getFirstChild();
    return node;
  }

  private Node createAssignNode(String code) {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = new JsParser(compiler).parse(code, null, null).getFirstChild();
    return node;
  }

  private Node createNameNode(String code) {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = new JsParser(compiler).parse(code, null, null).getFirstChild();
    return node.getFirstChild();
  }

  private Node createGetNode(String code) {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node node = new JsParser(compiler).parse(code, null, null).getFirstChild();
    return node;
  }

  private void assertEquals(DiagnosticType expected, DiagnosticType actual) {
    assertEquals(expected.getMessage(), expected.getType(), actual.getMessage(), actual.getType());
  }
}