import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.jscomp.compiler.CompilerInput;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.diagnostic.DiagnosticType;
import com.google.javascript.rhino.NodeFactory;

public class VarCheckTest {

  @Test
  public void testValidVariableDeclarations() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is a valid AbstractCompiler implementation
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput input = NodeFactory.createRoot(Token.PROGRAM, null);
    NodeTraversal traversal = new NodeTraversal(compiler, null, input, null);

    varCheck.process(null, input);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @Test
  public void testUndefinedVariableError() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is a valid AbstractCompiler implementation
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput input = NodeFactory.createRoot(Token.PROGRAM, null);
    input.setFilePosition("test.js", 1, 1);
    NodeTraversal traversal = new NodeTraversal(compiler, null, input, null);

    Node n = NodeFactory.createVariableDeclaration("x");
    input.addRoot(n);

    varCheck.process(null, input);

    varCheck.visit(NodeTraversal.INSTANCE, n, null);

    DiagnosticType error = varCheck.getLastError();
    assertTrue(error != null);
    assertTrue(error.getCode().equals("JSC_UNDEFINED_VARIABLE"));
  }

  @Test
  public void testMissingModuleDependencyWarning() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is a valid AbstractCompiler implementation
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput input1 = NodeFactory.createRoot(Token.PROGRAM, null);
    input1.setModuleName("module1.js");
    NodeTraversal traversal1 = new NodeTraversal(compiler, null, input1, null);
    traversal1.addRoot(NodeFactory.createVariableDeclaration("x"));

    CompilerInput input2 = NodeFactory.createRoot(Token.PROGRAM, null);
    input2.setModuleName("module2.js");
    NodeTraversal traversal2 = new NodeTraversal(compiler, null, input2, null);
    traversal2.addRoot(NodeFactory.createVariableDeclaration("y"));
    traversal2.addRoot(NodeFactory.createGetProperty("x"));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input1, input2), varCheck);

    DiagnosticType warning = varCheck.getLastWarning();
    assertTrue(warning != null);
    assertTrue(warning.getCode().equals("JSC_MISSING_MODULE_DEPENDENCY"));
  }

  @Test
  public void testStrictModuleDependencyError() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is a valid AbstractCompiler implementation
    VarCheck varCheck = new VarCheck(compiler, true);

    CompilerInput input1 = NodeFactory.createRoot(Token.PROGRAM, null);
    input1.setModuleName("module1.js");
    NodeTraversal traversal1 = new NodeTraversal(compiler, null, input1, null);
    traversal1.addRoot(NodeFactory.createVariableDeclaration("x"));

    CompilerInput input2 = NodeFactory.createRoot(Token.PROGRAM, null);
    input2.setModuleName("module2.js");
    NodeTraversal traversal2 = new NodeTraversal(compiler, null, input2, null);
    traversal2.addRoot(NodeFactory.createVariableDeclaration("y"));
    traversal2.addRoot(NodeFactory.createGetProperty("x"));

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(input1, input2), varCheck);

    DiagnosticType error = varCheck.getLastError();
    assertTrue(error != null);
    assertTrue(error.isDisabled());
  }

  @Test
  public void testValidFunctionDeclaration() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is a valid AbstractCompiler implementation
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput input = NodeFactory.createRoot(Token.PROGRAM, null);
    NodeTraversal traversal = new NodeTraversal(compiler, null, input, null);

    Node functionDeclaration = NodeFactory.createFunctionDeclaration("myFunction");
    input.addRoot(functionDeclaration);

    varCheck.process(null, input);

    assertFalse(varCheck.varsToDeclareInExterns.contains("myFunction"));
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is a valid AbstractCompiler implementation
    VarCheck varCheck = new VarCheck(compiler);

    CompilerInput input = NodeFactory.createRoot(Token.PROGRAM, null);
    NodeTraversal traversal = new NodeTraversal(compiler, null, input, null);

    Node functionDeclaration = NodeFactory.createFunctionDeclaration("myFunction");
    Node variableDeclaration1 = NodeFactory.createVariableDeclaration("x");
    Node variableDeclaration2 = NodeFactory.createVariableDeclaration("x");
    input.addRoot(functionDeclaration);
    input.addRoot(variableDeclaration1);
    input.addRoot(variableDeclaration2);

    varCheck.process(null, input);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }
}