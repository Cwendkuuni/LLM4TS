import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("var bar = function foo() {}");

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseConstructorFunction() {
    Node functionNode = createNode("function Foo() {}");
    Node parentNode = createNode("function Foo() {}");

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    Node functionNode = createNode("@this function foo() {}");
    Node parentNode = createNode("function foo() {}");

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node functionNode = createNode("function foo() {}");

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithBlockParent() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("{}");

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithScriptParent() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("(function() {})()");

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNameParent() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("var foo = function() {}");

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithAssignParent() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("var x = function foo() {}");

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithGetPropertyParent() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("{get prop() {}}");

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithGetPropertyLendsParent() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("{get prop() {}}", "Object.prototype");

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypePropertyParent() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("{get prop() {prototype: {}}}", "Object.prototype");

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeSubPropertyParent() {
    Node functionNode = createNode("function foo() {}");
    Node parentNode = createNode("{get prop() {prop2: {}}}", "Object.prototype");

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, functionNode, parentNode));
  }

  @Test
  public void testShouldReportThis() {
    Node thisNode = createNode("this");

    assertTrue(checker.shouldReportThis(thisNode));
  }

  @Test
  public void testShouldNotReportThisInsidePrototypeMethod() {
    Node thisNode = createNode("this");
    Node parentNode = createNode("{foo: function foo() {this}}");

    assertFalse(checker.shouldReportThis(thisNode));
  }

  @Test
  public void testShouldNotReportThisInsideFunctionAnnotatedWithConstructor() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("@constructor function foo() {}", "constructor");

    assertFalse(checker.shouldReportThis(thisNode));
  }

  @Test
  public void testShouldNotReportThisInsideFunctionAnnotatedWithThis() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("@this function foo() {}", "this");

    assertFalse(checker.shouldReportThis(thisNode));
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithNoParent() {
    Node thisNode = createNode("this");

    checker.shouldReportThis(thisNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithBlockParent() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("function foo() {}", "{}");

    checker.shouldReportThis(thisNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithScriptParent() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("function foo() {}", "(function() {})()");

    checker.shouldReportThis(thisNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithNameParent() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("function foo() {}", "var foo = function() {}");

    checker.shouldReportThis(thisNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithAssignParent() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("function foo() {}", "var x = function foo() {}");

    checker.shouldReportThis(thisNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithGetPropertyParent() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("function foo() {}", "{get prop() {}}");

    checker.shouldReportThis(thisNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithGetPropertyLendsParent() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("function foo() {}", "{get prop() {}}", "Object.prototype");

    checker.shouldReportThis(thisNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithPrototypePropertyParent() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("function foo() {}", "{get prop() {prototype: {}}}");

    checker.shouldReportThis(thisNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThisInsideFunctionWithPrototypeSubPropertyParent() {
    Node thisNode = createNode("this");
    Node functionNode = createNode("function foo() {}", "{get prop() {prop2: {}}}");

    checker.shouldReportThis(thisNode);
  }

  private Node createNode(String code, String... jsDocInfos) {
    NodeTraversal traversal = NodeTraversal.createEmpty();
    JSDocInfo jsDocInfo = null;
    if (jsDocInfos != null) {
      jsDocInfo = new JSDocInfo(code, jsDocInfos[0]);
    }
    Node node = compiler.parseCompilationUnit(code, null, null, null, null, jsDocInfo, null).getRootNode();
    when(compiler.report(NodeTraversal.EMPTY, NodeUtil.findFirstErrorNode(node))).thenReturn(new Object());
    return node;
  }

  private Node createNode(String code) {
    return createNode(code, null);
  }

  private void assertTrue(boolean condition) {
    assertNotNull(condition);
  }

  private void assertFalse(boolean condition) {
    assertNotNull(condition);
    assertFalse(condition);
  }
}