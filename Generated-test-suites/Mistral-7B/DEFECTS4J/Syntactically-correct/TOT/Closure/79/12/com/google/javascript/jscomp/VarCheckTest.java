import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.Set;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private DiagnosticReporter diagnostics;

  @org.junit.Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    diagnostics = mock(DiagnosticReporter.class);
    when(compiler.newExternInput("")).thenReturn(new TestInput(""));
    when(compiler.newCompilerInputFromString("", null)).thenReturn(new TestInput(""));
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput rootInput = new TestInput("a = 1;");
    Node root = nodeFactory.parseTree(rootInput.getContent(), rootInput, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, diagnostics);
    varCheck.process(null, root);
    traversal.resume();

    Scope scope = traversal.getScope();
    assertEquals(1, scope.getVarCount());
    assertEquals("a", scope.getVar(0).getName());
    assertEquals(rootInput, scope.getVar(0).input);

    Node undeclaredVar = new Node(Token.NAME, "b");
    traversal.traverse(undeclaredVar, root);

    assertEquals(2, scope.getVarCount());
    assertEquals("b", scope.getVar(1).getName());
    assertEquals(null, scope.getVar(1).input);
  }

  @org.junit.Test
  public void testModuleDependency() {
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput leftInput = new TestInput("var x = 1;", "left.js");
    Node leftRoot = nodeFactory.parseTree(leftInput.getContent(), leftInput, nodeFactory);

    CompilerInput rightInput = new TestInput("var y = x;", "right.js");
    Node rightRoot = nodeFactory.parseTree(rightInput.getContent(), rightInput, nodeFactory);

    JSModule leftModule = new JSModule("left.js");
    JSModule rightModule = new JSModule("right.js");

    when(compiler.getModuleGraph().dependsOn(leftModule, rightModule)).thenReturn(false);

    NodeTraversal leftTraversal = new NodeTraversal(compiler, leftRoot, null, diagnostics);
    NodeTraversal rightTraversal = new NodeTraversal(compiler, rightRoot, null, diagnostics);

    varCheck.process(leftInput.getAstRoot(compiler), null);
    leftTraversal.resume();

    varCheck.process(rightInput.getAstRoot(compiler), null);
    rightTraversal.resume();

    Scope leftScope = leftTraversal.getScope();
    Scope rightScope = rightTraversal.getScope();

    assertEquals("x", rightScope.getVar(0).getName());
    assertEquals(leftInput, rightScope.getVar(0).input);

    when(compiler.getModuleGraph().dependsOn(leftModule, rightModule)).thenReturn(true);

    varCheck.process(leftInput.getAstRoot(compiler), null);
    leftTraversal.resume();

    varCheck.process(rightInput.getAstRoot(compiler), null);
    rightTraversal.resume();

    assertEquals(1, leftScope.getVarCount());
    assertEquals(1, rightScope.getVarCount());

    assertEquals("x", leftScope.getVar(0).getName());
    assertEquals(leftInput, leftScope.getVar(0).input);

    assertEquals("x", rightScope.getVar(0).getName());
    assertEquals(leftInput, rightScope.getVar(0).input);

    assertEquals("Module 'left.js' cannot reference 'x', defined in module 'right.js'",
        leftTraversal.getDiagnostics().getError(0).getMessage());
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput leftInput = new TestInput("var x = require('right.js').y;", "left.js");
    Node leftRoot = nodeFactory.parseTree(leftInput.getContent(), leftInput, nodeFactory);

    CompilerInput rightInput = new TestInput("var y = 1;", "right.js");
    Node rightRoot = nodeFactory.parseTree(rightInput.getContent(), rightInput, nodeFactory);

    when(compiler.getModuleGraph().dependsOn(leftInput.getModule(), rightInput.getModule())).thenReturn(false);

    NodeTraversal leftTraversal = new NodeTraversal(compiler, leftRoot, null, diagnostics);
    NodeTraversal rightTraversal = new NodeTraversal(compiler, rightRoot, null, diagnostics);

    varCheck.process(null, leftRoot);
    leftTraversal.resume();

    varCheck.process(rightRoot, null);
    rightTraversal.resume();

    assertEquals("missing module dependency; module 'left.js' should depend " +
        "on module 'right.js' because it references y",
        leftTraversal.getDiagnostics().getWarning(0).getMessage());
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput leftInput = new TestInput("var x = require('right.js').y;", "left.js");
    Node leftRoot = nodeFactory.parseTree(leftInput.getContent(), leftInput, nodeFactory);

    CompilerInput rightInput = new TestInput("var y = 1;", "right.js");
    Node rightRoot = nodeFactory.parseTree(rightInput.getContent(), rightInput, nodeFactory);

    JSModule leftModule = new JSModule("left.js");
    JSModule rightModule = new JSModule("right.js");

    when(compiler.getModuleGraph().dependsOn(leftModule, rightModule)).thenReturn(true);

    when(compiler.getErrorLevel(leftInput, 0, 0, STRICT_MODULE_DEP_ERROR)).thenReturn(CheckLevel.ERROR);

    NodeTraversal leftTraversal = new NodeTraversal(compiler, leftRoot, null, diagnostics);
    NodeTraversal rightTraversal = new NodeTraversal(compiler, rightRoot, null, diagnostics);

    varCheck.process(null, leftRoot);
    leftTraversal.resume();

    varCheck.process(rightRoot, null);
    rightTraversal.resume();

    assertEquals("module 'left.js' cannot reference 'y', defined in module 'right.js'",
        leftTraversal.getDiagnostics().getError(0).getMessage());
  }

  @org.junit.Test
  public void testVariableDeclarationInExterns() {
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput input = new TestInput("//@externs\nvar x = 1;", "");
    Node root = nodeFactory.parseTree(input.getContent(), input, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, diagnostics);
    varCheck.process(null, root);
    traversal.resume();

    assertEquals(1, traversal.getDiagnostics().getWarningCount());
    assertEquals("accessing name x in externs has no effect",
        traversal.getDiagnostics().getWarning(0).getMessage());
  }

  @org.junit.Test
  public void testFunctionDeclarationInExterns() {
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput input = new TestInput("//@externs\nfunction foo() {}", "");
    Node root = nodeFactory.parseTree(input.getContent(), input, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, diagnostics);
    varCheck.process(null, root);
    traversal.resume();

    assertEquals(1, traversal.getDiagnostics().getWarningCount());
    assertEquals("name foo is not undefined in the externs.",
        traversal.getDiagnostics().getWarning(0).getMessage());
  }

  private static class TestInput extends CompilerInput {

    private TestInput(String content, String fileName) {
      super(content, null, null, null, new TestSourceFile(fileName));
    }

    private TestInput(String content) {
      this(content, "");
    }

    @Override
    public boolean isExtern() {
      return true;
    }

    @Override
    public JSModule getModule() {
      return null;
    }

  }

}