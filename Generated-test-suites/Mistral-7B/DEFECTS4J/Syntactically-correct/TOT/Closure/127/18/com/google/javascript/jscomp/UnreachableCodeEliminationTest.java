import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.TRAVERSE;
import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilerFlag;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  @Test
  public void testSimpleUnreachableCode() {
    String input = "function f() { if (true) { return; } alert('unreachable'); }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKS, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertFalse(NodeUtil.findChildByType(result, ALERT));
  }

  @Test
  public void testDeadCodeAfterReturn() {
    String input = "function f() { if (true) { return; alert('unreachable'); } }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKS, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.isEmpty(result.getLastChild()));
  }

  @Test
  public void testDeadCodeSameBranch() {
    String input = "function f() { if (true) { return; } if (true) { alert('unreachable'); } }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKS, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.isEmpty(result.getLastChild()));
  }

  @Test
  public void testDeadCodeDifferentBranch() {
    String input = "function f() { if (true) { alert('branch1'); return; } if (true) { alert('branch2'); } }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKs, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.findChildByType(result, ALERT, 0).getValue().getString().equals("branch1"));
  }

  @Test
  public void testNoOpStatement() {
    String input = "function f() { var x = 1, y = 1; }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKs, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.isEmpty(result));
  }

  @Test
  public void testNoOpStatementWithSideEffect() {
    String input = "function f() { var x = 1, y = 1; x = 2; }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKs, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.hasChildWithType(result, VAR, 0));
    assertEquals(2, ((Node) NodeUtil.findChildByType(result, VAR, 0)).getFirstChild().getIntValueExact());
  }

  @Test
  public void testFunctionWithNoOpStatement() {
    String input = "function f() { function g() {} }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKs, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.isEmpty(result.getLastChild()));
  }

  @Test
  public void testFunctionWithNoOpStatementInsideLoop() {
    String input = "function f() { for (var i = 0; i < 10; i++) { var x = 1; } }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKs, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.isEmpty(result));
  }

  @Test
  public void testUnreachableCodeWithCondition() {
    String input = "function f() { if (false) { return; } alert('unreachable'); }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKs, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.isEmpty(result));
  }

  @Test
  public void testUnreachableCodeInsideFunction() {
    String input = "function f() { function g() { return; alert('unreachable'); } }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKs, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.isEmpty(result.getLastChild()));
  }

  @Test
  public void testUnreachableCodeWithConditionInsideFunction() {
    String input = "function f() { function g() { if (false) { return; } alert('unreachable'); } }";
    Node root = NodeFactory.compile(input);

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(CompilerFlag.STRICT_MODE, false);
    options.setCompilerFlag(CompilerFlag.ENABLE_STRICT_MODE_CHECKs, false);
    options.setCompilerFlag(CompilerFlag.WARNINGS, true);

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    passManager.addPass(elimination);

    Node toplevel = NodeFactory.createScopeNode();
    toplevel.appendChild(NodeFactory.createNameNode("f"));
    toplevel.appendChild(root);

    passManager.runPasses(toplevel);

    Node result = elimination.getResultNode(toplevel);
    assertTrue(result.isFunction());
    assertTrue(NodeUtil.isEmpty(result.getLastChild()));
  }
}