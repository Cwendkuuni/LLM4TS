import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckGlobalThisTest.createNode;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.Token.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParserUtil;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node node = ParserUtil.parseJsString("function foo() {}");
    assertTrue(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node node = ParserUtil.parseJsString("function Foo() {}");
    assertFalse(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node node = ParserUtil.parseJsString("function Foo@override() {}");
    assertFalse(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    Node node = ParserUtil.parseJsString("function Foo@this() {}");
    assertTrue(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    Node node = ParserUtil.parseJsString("function foo() {}");
    assertFalse(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithScriptParent() {
    Node node = ParserUtil.parseJsString("script\nfunction foo() {}");
    assertTrue(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithBlockParent() {
    Node node = ParserUtil.parseJsString("{\nfunction foo() {}\n}");
    assertTrue(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNameParent() {
    Node node = ParserUtil.parseJsString("var a = function foo() {};");
    assertTrue(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithAssignParent() {
    Node node = ParserUtil.parseJsString("var a = function foo() {};\na = function bar() {};");
    assertTrue(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    Node node = ParserUtil.parseJsString("var a = function foo() {};\nObject.prototype.a = function bar() {};");
    assertFalse(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccess() {
    Node node = ParserUtil.parseJsString("var a = function foo() {};\nvar b = a.prototype;\nb.bar = function() {};");
    assertFalse(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccess2() {
    Node node = ParserUtil.parseJsString("var a = function foo() {};\nvar b = new a();\nb.__proto__.bar = function() {};");
    assertFalse(checker.shouldTraverse(new NodeTraversal(node), node, null));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node node = ParserUtil.parseJsString("var a = function() { this.x = 1; }");
    traverse(new NodeTraversal(node), checker, node, null);
    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 1, 0));
  }

  @Test
  public void testShouldNotReportThisInAssignment() {
    Node node = ParserUtil.parseJsString("var a = this;");
    traverse(new NodeTraversal(node), checker, node, null);
    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 1, 0));
  }

  @Test
  public void testShouldNotReportThisInAssignment2() {
    Node node = ParserUtil.parseJsString("var a = this.x = 1;");
    traverse(new NodeTraversal(node), checker, node, null);
    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 1, 0));
  }

  @Test
  public void testShouldNotReportThisInPropertyAccess() {
    Node node = ParserUtil.parseJsString("var a = { x: this };");
    traverse(new NodeTraversal(node), checker, node, null);
    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 1, 0));
  }

  @Test
  public void testShouldReportThisInPrototypeMethod() {
    Node node = ParserUtil.parseJsString("function Foo() {}\nFoo.prototype.x = function() { this.x = 1; }");
    Node constructorNode = NodeUtil.findSingleNodeOfType(node, IS_FUNCTION);
    traverse(new NodeTraversal(node), checker, constructorNode, node);
    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 1, 0));

    traverse(new NodeTraversal(node), checker, node, constructorNode);
    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 3, 1));
  }

  @Test
  public void testShouldReportThisInPrototypeMethod2() {
    Node node = ParserUtil.parseJsString("function Foo() {}\nFoo.prototype.x = function() { this.x = this.x + 1; }");
    Node constructorNode = NodeUtil.findSingleNodeOfType(node, IS_FUNCTION);
    traverse(new NodeTraversal(node), checker, constructorNode, node);
    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 1, 0));

    traverse(new NodeTraversal(node), checker, node, constructorNode);
    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 3, 1));
  }

  @Test
  public void testShouldReportThisInFunctionWithNoLogicalPlace() {
    Node node = ParserUtil.parseJsString("function foo() { this.x = 1; }");
    traverse(new NodeTraversal(node), checker, node, null);
    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS", 1, 0));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node node = ParserUtil.parseJsString("function foo@this() {}", "");
    JSDocInfo jsDoc = checker.getFunctionJsDocInfo(node);
    assertTrue(jsDoc != null);
    assertTrue(jsDoc.isThisType());
  }

  @Test
  public void testGetFunctionJsDocInfo2() {
    Node node = ParserUtil.parseJsString("function foo() {}", "");
    JSDocInfo jsDoc = checker.getFunctionJsDocInfo(node);
    assertFalse(jsDoc != null);
  }

  private static Node createNode(String code) {
    Node node = ParserUtil.parseJsString(code);
    if (code.endsWith(";")) {
      Node lastNode = NodeUtil.findSingleNodeOfType(node, IS_SCRIPT);
      return lastNode != null ? lastNode : node;
    }
    return node;
  }
}