import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CheckSideEffects.NodeUtil;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import java.util.List;

public class CheckSideEffectsTest {

  private static Node createNode(String code) {
    CompilerOptions options = new CompilerOptions(true, null, null);
    Compiler compiler = new Compiler();
    Node root = NodeTraversal.parseCompilationUnit(compiler, code, null, options);
    return root;
  }

  @org.junit.Test
  public void testCheckSideEffects() {
    // Test case 1: Simple variable assignment with no side effects
    String code1 = "var x = 10;";
    Node root1 = createNode(code1);
    CheckSideEffects checker1 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root1, checker1);
    assertEquals(0, checker1.problemNodes.size());

    // Test case 2: Variable assignment with side effects (reflow in browsers)
    String code2 = "var el = document.getElementById('id'); el.offsetWidth;";
    Node root2 = createNode(code2);
    CheckSideEffects checker2 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root2, checker2);
    assertEquals(1, checker2.problemNodes.size());

    // Test case 3: Return statement with no side effects
    String code3 = "function foo() { return 10; }";
    Node root3 = createNode(code3);
    CheckSideEffects checker3 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root3, checker3);
    assertEquals(0, checker3.problemNodes.size());

    // Test case 4: Compound assignment with no side effects
    String code4 = "var x = 10, y = 5; x += y;";
    Node root4 = createNode(code4);
    CheckSideEffects checker4 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root4, checker4);
    assertEquals(0, checker4.problemNodes.size());

    // Test case 5: Operator with no side effects
    String code5 = "var x = 10, y = 5; x *= y;";
    Node root5 = createNode(code5);
    CheckSideEffects checker5 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root5, checker5);
    assertEquals(0, checker5.problemNodes.size());

    // Test case 6: Return statement with side effects
    String code6 = "function foo() { document.write('Hello World!'); return 10; }";
    Node root6 = createNode(code6);
    CheckSideEffects checker6 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root6, checker6);
    assertEquals(1, checker6.problemNodes.size());

    // Test case 7: Multiple return statements with side effects
    String code7 = "function foo() { document.write('Hello World!'); return 10; } function bar() { document.write('Hello World!'); return 10; }";
    Node root7 = createNode(code7);
    CheckSideEffects checker7 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root7, checker7);
    assertEquals(2, checker7.problemNodes.size());

    // Test case 8: Variadic function call with side effects
    String code8 = "function foo(a, b, c) { document.write('a: ' + a + ', b: ' + b + ', c: ' + c); }";
    Node root8 = createNode(code8);
    CheckSideEffects checker8 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root8, checker8);
    assertEquals(1, checker8.problemNodes.size());

    // Test case 9: Function call with side effects inside an if statement
    String code9 = "function foo() { document.write('Inside foo'); }function bar() { if (true) foo(); }";
    Node root9 = createNode(code9);
    CheckSideEffects checker9 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root9, checker9);
    assertEquals(1, checker9.problemNodes.size());

    // Test case 10: Function with a side-effect sync function
    String code10 = "function foo() {" +
        "  function syncFunction() {" +
        "    document.write('Inside syncFunction');" +
        "  }" +
        "  syncFunction();" +
        "  return 10;" +
        "}";
    Node root10 = createNode(code10);
    CheckSideEffects checker10 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root10, checker10);
    assertEquals(1, checker10.problemNodes.size());

    // Test case 11: Function with a side-effect free function
    String code11 = "function foo() {" +
        "  function freeFunction() {" +
        "    // No side effects" +
        "  }" +
        "  freeFunction();" +
        "  return 10;" +
        "}";
    Node root11 = createNode(code11);
    CheckSideEffects checker11 = new CheckSideEffects(null, WARNING, false);
    NodeTraversal.traverse(new Compiler(), root11, checker11);
    assertEquals(0, checker11.problemNodes.size());
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    // Test case 1: Simple variable assignment with no side effects
    String code1 = "var x = 10;";
    Node root1 = createNode(code1);
    CheckSideEffects checker1 = new CheckSideEffects(null, WARNING, true);
    NodeTraversal.traverse(new Compiler(), root1, checker1);
    checker1.protectSideEffects();
    List<Node> protectedNodes = checker1.problemNodes;
    assertEquals(0, protectedNodes.size());

    Node protectedNode1 = protectedNodes.get(0);
    assertNull(protectedNode1);

    // Test case 2: Variable assignment with side effects (reflow in browsers)
    String code2 = "var el = document.getElementById('id'); el.offsetWidth;";
    Node root2 = createNode(code2);
    CheckSideEffects checker2 = new CheckSideEffects(null, WARNING, true);
    NodeTraversal.traverse(new Compiler(), root2, checker2);
    checker2.protectSideEffects();
    List<Node> protectedNodes2 = checker2.problemNodes;
    assertEquals(1, protectedNodes2.size());

    Node protectedNode2 = protectedNodes2.get(0);
    assertEquals("JSCOMPILER_PROTECTOR_FN", protectedNode2.getString());
  }
}