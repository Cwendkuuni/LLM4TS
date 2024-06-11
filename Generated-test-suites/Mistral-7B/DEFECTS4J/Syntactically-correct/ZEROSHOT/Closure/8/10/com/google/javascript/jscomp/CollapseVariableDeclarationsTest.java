import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.AbstractPostOrderCallbackFlag.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;

import java.util.List;
import java.util.Set;

/**
 * Test class for CollapseVariableDeclarations.
 */
public class CollapseVariableDeclarationsTest {

  /**
   * Test method for {@link CollapseVariableDeclarations#CollapseVariableDeclarations(AbstractCompiler)}.
   */
  @org.junit.Test
  public void testConstructor() {
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    assertNotNull(pass);
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @org.junit.Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    String sourceCode =
        "function foo() {" +
            "var a;" +
            "var b = 1;" +
            "var c = 2;" +
            "var d = 3;" +
            "var e = 4;" +
            "var f = 5;" +
            "var g = 6;" +
            "var h = 7;" +
            "var i = 8;" +
            "var j = 9;" +
            "var k = 10;" +
            "var a, b = 1, c = 2;" +
            "var j, k = 11, 12;" +
            "var l = 13;" +
            "var m = 14;" +
            "var n = 15;" +
            "var o = 16;" +
            "var p = 17;" +
            "var q = 18;" +
            "var r = 19;" +
            "var s = 20;" +
            "var t = 21;" +
            "var u = 22;" +
            "var v = 23;" +
            "var w = 24;" +
            "var x = 25;" +
            "var y = 26;" +
            "var z = 27;" +
            "var a, b, c = 28, 29, 30;" +
            "var i, j = 31, 32;" +
            "var o = new Object();" +
            "o.a = 33;" +
            "o.b = 34;" +
            "var a = o.a;" +
            "}" +
            "function bar() {" +
            "  var a = true;" +
            "  var b = true;" +
            "  var c = true;" +
            "}";

    Node externs = new Node(Token.PROGRAM);
    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    pass.process(externs, root);

    List<Node> collapses = Lists.newArrayList();

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node) {
        if (node.isVar()) {
          Collapse collapse = findCollapse(node);
          if (collapse != null) {
            collapses.add(collapse);
          }
        }
      }

      private Collapse findCollapse(Node node) {
        for (Collapse collapse : pass.collapses) {
          if (collapse.startNode == node) {
            return collapse;
          }
        }
        return null;
      }
    }, EXPLORING, null, null);

    List<Node> expectedCollapses = Lists.newArrayList(
        new Collapse(new Node("var", null, null), new Node("var", null, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 1, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 2, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 3, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 4, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 5, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 6, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 7, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 8, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 9, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 10, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 11, null), new Node("var", 12, null)),
        new Collapse(new Node("var", null, null), new Node("var", 13, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 14, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 15, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 16, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 17, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 18, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 19, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 20, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 21, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 22, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 23, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 24, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 25, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 26, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 27, null), null),
        new Collapse(new Node("var", 28, null), new Node("var", 29, 30), null),
        new Collapse(new Node("var", 31, null), new Node("var", 32, null), null),
        new Collapse(new Node("var", null, null), new Node("var", 33, null), new Node("Object", null, null)),
        new Collapse(new Node("var", null, null), new Node("var", 34, null), new Node("Object", null, null)),
        new Collapse(new Node("var", "a", null), new Node("var", "a", null), null),
        new Collapse(new Node("var", null, null), new Node("var", 33, new Node("Object", null, null)), null),
        new Collapse(new Node("var", "b", null), new Node("var", "b", null), null)
    );

    assertEquals(expectedCollapses.size(), collapses.size());
    for (int i = 0; i < expectedCollapses.size(); i++) {
      Collapse expectedCollapse = expectedCollapses.get(i);
      Collapse actualCollapse = collapses.get(i);
      assertEquals(expectedCollapse.startNode, actualCollapse.startNode);
      assertEquals(expectedCollapse.endNode, actualCollapse.endNode);
      assertEquals(expectedCollapse.parent, actualCollapse.parent);
    }
  }

  /**
   * Test class for {@link CollapseVariableDeclarations#applyCollapses()}.
   */
  @org.junit.Test
  public void testApplyCollapses() {
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    String sourceCode =
        "function foo() {" +
            "var a;" +
            "var b = 1;" +
            "var c = 2;" +
            "var d = 3;" +
            "var e = 4;" +
            "var f = 5;" +
            "var g = 6;" +
            "var h = 7;" +
            "var i = 8;" +
            "var j = 9;" +
            "var k = 10;" +
            "var a, b = 1, c = 2;" +
            "var j, k = 11, 12;" +
            "var l = 13;" +
            "var m = 14;" +
            "var n = 15;" +
            "var o = 16;" +
            "var p = 17;" +
            "var q = 18;" +
            "var r = 19;" +
            "var s = 20;" +
            "var t = 21;" +
            "var u = 22;" +
            "var v = 23;" +
            "var w = 24;" +
            "var x = 25;" +
            "var y = 26;" +
            "var z = 27;" +
            "var a, b, c = 28, 29, 30;" +
            "var i, j = 31, 32;" +
            "var o = new Object();" +
            "o.a = 33;" +
            "o.b = 34;" +
            "var a = o.a;" +
            "}" +
            "function bar() {" +
            "  var a = true;" +
            "  var b = true;" +
            "  var c = true;" +
            "}";

    Node externs = new Node(Token.PROGRAM);
    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    pass.process(externs, root);
    pass.applyCollapses();

    String expectedSourceCode =
        "function foo() {" +
            "var a, b = 1, c = 2;" +
            "var j, k = 11, 12;" +
            "var o = new Object();" +
            "o.a = 33;" +
            "o.b = 34;" +
            "var a = o.a;" +
            "}" +
            "function bar() {" +
            "  var c = true;" +
            "  var c = true;" +
            "  var c = true;" +
            "}";

    String actualSourceCode = compiler.getSource();
    assertEquals(expectedSourceCode, actualSourceCode);
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#GatherCollapses#blacklistStubVars(NodeTraversal, Node)}.
   */
  @org.junit.Test
  public void testBlacklistStubVars() {
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() { var a; }";

    Node externs = new Node(Token.PROGRAM);
    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    pass.process(externs, root);

    Set<Var> blacklistedVars = pass.collapses.get(0).blacklistedVars;

    assertEquals(1, blacklistedVars.size());
    assertEquals("a", blacklistedVars.iterator().next().getName());
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#GatherCollapses#canBeRedeclared(Node, Scope)}.
   */
  @org.junit.Test
  public void testCanBeRedeclared() {
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() { var a = 1; var a = 2; }";

    Node externs = new Node(Token.PROGRAM);
    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    Scope scope = new Scope(root.getFirstChild());
    pass.process(externs, root);

    boolean actualResult = pass.collapses.get(0).getGatherCollapses().canBeRedeclared(new Node("="), scope);

    assertTrue(actualResult);
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#GatherCollapses#canBeRedeclared(Node, Scope)}.
   */
  @org.junit.Test
  public void testCannotBeRedeclared() {
    AbstractCompiler compiler = new Compiler();
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() { var a = 1; var a = new Object(); }";

    Node externs = new Node(Token.PROGRAM);
    Node root = Parser.parse(sourceCode, null, null).getRootNode();
    Scope scope = new Scope(root.getFirstChild());
    pass.process(externs, root);

    boolean actualResult = pass.collapses.get(0).getGatherCollapses().canBeRedeclared(new Node("new"), scope);

    assertFalse(actualResult);
  }
}