import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    String jsCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  } else {\n" +
        "    alert('unreachable');\n" +
        "  }\n" +
        "  var x;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node expectedRoot = new Node(Token.BLOCK, null);
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }

  @Test(expected = AssertionError.class)
  public void testExceptionThrown() {
    String jsCode = "function foo() {\n" +
        "  throw new Error('Exception thrown');\n" +
        "  return;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    // This test case should fail as the exception is not eliminated
  }

  @Test
  public void testRemoveDeadStatement() {
    String jsCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "  alert('message');\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node expectedRoot = new Node(Token.RETURN, null);
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }

  @Test
  public void testRemoveFunctionCallAsDeadStatement() {
    String jsCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "  var x = Math.sqrt(16);\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node expectedRoot = new Node(Token.RETURN, null);
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }

  @Test
  public void testRemoveEmptyBlock() {
    String jsCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "  {}\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node expectedRoot = new Node(Token.RETURN, null);
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }

  @Test
  public void testRemoveScript() {
    String jsCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "}\n" +
        "(function() {\n" +
        "  // This script should not be removed\n" +
        "})()";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node expectedRoot = new Node(Token.FUNCTION, null, null, null, null);
    expectedRoot.setName("foo");
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }

  @Test
  public void testRemoveBreak() {
    String jsCode = "function foo() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i === 5) {\n" +
        "      break;\n" +
        "    }\n" +
        "  }\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        if (n.isFunction()) {
          nt.traverse(compiler, n.getFirstChild(), this);
          return;
        }

        if (n.getType() == Token.BREAK) {
          n.setParent(null);
          NodeUtil.redeclareVarsInsideBranch(n);
          compiler.reportCodeChange();
          logger.fine("Removed break statement");
        }
      }
    });

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node expectedRoot = new Node(Token.FUNCTION, null, null, null, null);
    expectedRoot.setName("foo");
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }

  @Test
  public void testRemoveContinue() {
    String jsCode = "function foo() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i === 5) {\n" +
        "      continue;\n" +
        "    }\n" +
        "    console.log(i);\n" +
        "  }\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        if (n.isFunction()) {
          nt.traverse(compiler, n.getFirstChild(), this);
          return;
        }

        if (n.getType() == Token.CONTINUE) {
          n.setParent(null);
          NodeUtil.redeclareVarsInsideBranch(n);
          compiler.reportCodeChange();
          logger.fine("Removed continue statement");
        }
      }
    });

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    String expectedJsCode = "function foo() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    console.log(i);\n" +
        "  }\n" +
        "}";

    Node expectedRoot = compiler.parseTree(expectedJsCode, "test.js", null);
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }

  @Test
  public void testRemoveReturn() {
    String jsCode = "function foo() {\n" +
        "  return;\n" +
        "  console.log('This should not be executed');\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node expectedRoot = new Node(Token.NULL, null);
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }

  @Test
  public void testRemoveFunctionCallAsDeadStatementWithArguments() {
    String jsCode = "function foo(x, y) {\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "  console.log(Math.pow(x, y));\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = compiler.parseTree(jsCode, "test.js", null);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node expectedRoot = new Node(Token.RETURN, null);
    assertTrue("Unexpected code change", root.isEqual(expectedRoot));
  }
}