import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckSideEffectsTest {

  private Compiler compiler;
  private CheckSideEffects checker;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticType.NOT_USED, CheckLevel.VERBOSE);
    compiler = new Compiler(options);
    compiler.init();
    checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testNoSideEffects() {
    Node root = nodeFactory.createProgramNode();
    Node var1 = nodeFactory.createVar("x", nodeFactory.createNumberLiteral(1));
    Node var2 = nodeFactory.createVar("y", nodeFactory.createNumberLiteral(2));
    Node expr = nodeFactory.createBinaryOp("+", var1, var2);
    Node stmt = nodeFactory.createExpressionStatement(expr);
    root.appendChild(stmt);
    checker.process(null, root);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testSimpleOpNoSideEffects() {
    Node root = nodeFactory.createProgramNode();
    Node var1 = nodeFactory.createVar("x", nodeFactory.createNumberLiteral(1));
    Node var2 = nodeFactory.createVar("y", nodeFactory.createNumberLiteral(2));
    Node add = nodeFactory.createName("+", nodeFactory.createName("x"), nodeFactory.createName("y"));
    Node sub = nodeFactory.createName("-", add, nodeFactory.createNumberLiteral(1));
    Node stmt = nodeFactory.createExpressionStatement(sub);
    root.appendChild(stmt);
    checker.process(null, root);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testSimpleOpWithSideEffects() {
    Node root = nodeFactory.createProgramNode();
    Node var1 = nodeFactory.createVar("x", nodeFactory.createNumberLiteral(1));
    Node var2 = nodeFactory.createVar("y", nodeFactory.createNumberLiteral(2));
    Node add = nodeFactory.createName("+", nodeFactory.createName("x"), nodeFactory.createName("y"));
    Node inc = nodeFactory.createName("++", var1);
    Node stmt = nodeFactory.createExpressionStatement(add);
    root.appendChild(stmt);
    root.appendChild(inc);
    checker.process(null, root);
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.size() > 0);
    assertTrue(diagnostics.get(0).getSeverity().equals(DiagnosticType.Warning));
    assertTrue(diagnostics.get(0).getMessage().startsWith("Suspicious code."));
  }

  @Test
  public void testVoidStatement() {
    Node root = nodeFactory.createProgramNode();
    Node stmt = nodeFactory.createExpressionStatement(nodeFactory.createCommaOperator(nodeFactory.createVoidStatement(), nodeFactory.createNumberLiteral(42)));
    root.appendChild(stmt);
    checker.process(null, root);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCallStatement() {
    Node root = nodeFactory.createProgramNode();
    Node func = nodeFactory.createName("func");
    Node call = nodeFactory.createExpressionStatement(nodeFactory.createCall(func, nodeFactory.createNumberLiteral(1)));
    root.appendChild(call);
    checker.process(null, root);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCallStatementWithSideEffects() {
    Node root = nodeFactory.createProgramNode();
    Node func = nodeFactory.createName("func");
    Node call = nodeFactory.createExpressionStatement(nodeFactory.createCall(func, nodeFactory.createNumberLiteral(1)));
    Node assign = nodeFactory.createExpressionStatement(nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createNumberLiteral(42)));
    root.appendChild(call);
    root.appendChild(assign);
    checker.process(null, root);
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.size() > 0);
    assertTrue(diagnostics.get(0).getSeverity().equals(DiagnosticType.Warning));
    assertTrue(diagnostics.get(0).getMessage().startsWith("Suspicious code."));
  }

  @Test
  public void testForLoop() {
    Node root = nodeFactory.createProgramNode();
    Node var = nodeFactory.createVar("i", nodeFactory.createNumberLiteral(0));
    Node condition = nodeFactory.createBinaryOp("!=", var, nodeFactory.createNumberLiteral(10));
    Node increment = nodeFactory.createExpressionStatement(nodeFactory.createAssign(var, nodeFactory.createBinaryOp("+", var, nodeFactory.createNumberLiteral(1))));
    Node stmt = nodeFactory.createExpressionStatement(nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createNumberLiteral(42)));
    Node loop = nodeFactory.createForStatement(condition, null, increment, stmt);
    root.appendChild(loop);
    checker.process(null, root);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testForLoopWithSideEffects() {
    Node root = nodeFactory.createProgramNode();
    Node var = nodeFactory.createVar("i", nodeFactory.createNumberLiteral(0));
    Node condition = nodeFactory.createBinaryOp("!=", var, nodeFactory.createNumberLiteral(10));
    Node increment = nodeFactory.createExpressionStatement(nodeFactory.createAssign(var, nodeFactory.createBinaryOp("+", var, nodeFactory.createNumberLiteral(1))));
    Node call = nodeFactory.createExpressionStatement(nodeFactory.createCall(nodeFactory.createName("console.log"), nodeFactory.createStringLiteral("i: "), var));
    Node loop = nodeFactory.createForStatement(condition, null, increment, call);
    root.appendChild(loop);
    checker.process(null, root);
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.size() > 0);
    assertTrue(diagnostics.get(0).getSeverity().equals(DiagnosticType.Warning));
    assertTrue(diagnostics.get(0).getMessage().startsWith("Suspicious code."));
  }
}