import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class VarCheckTest {

  private Compiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setWarningLevel(CheckLevel.QUIRKS);
    compiler = new Compiler(options);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testUndefinedVariable() {
    String code = "function foo() { var x; x = 1; }";
    Node root = Parser.parse(nodeFactory, code, null, null);
    CompilerPass pass = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(root, pass);
    traversal.traverseAll();

    assertTrue(pass.getDiagnostics().isEmpty());

    code = "function foo() { var x; }";
    root = Parser.parse(nodeFactory, code, null, null);
    pass = new VarCheck(compiler);
    traversal = new NodeTraversal(root, pass);
    traversal.traverseAll();

    assertEquals(1, pass.getDiagnostics().size());
    assertEquals("JSC_UNDEFINED_VARIABLE", pass.getDiagnostics().get(0).getType());
    assertEquals("x", pass.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testUndefinedVariableWithDeclaration() {
    String code = "function foo() { var x; x = 1; }";
    Node root = Parser.parse(nodeFactory, code, null, null);
    CompilerPass pass = new VarCheck(compiler, true);
    NodeTraversal traversal = new NodeTraversal(root, pass);
    traversal.traverseAll();

    assertTrue(pass.getDiagnostics().isEmpty());

    code = "function foo() { var x; }";
    root = Parser.parse(nodeFactory, code, null, null);
    pass = new VarCheck(compiler, true);
    traversal = new NodeTraversal(root, pass);
    traversal.traverseAll();

    assertTrue(pass.getDiagnostics().isEmpty());
  }

  @Test
  public void testMissingModuleDependency() {
    String moduleA = "function foo() { var x; }";
    String moduleB = "function bar(x) { }";

    Node rootA = Parser.parse(nodeFactory, moduleA, null, null);
    Node rootB = Parser.parse(nodeFactory, moduleB, null, null);

    CompilerInput inputA = compiler.newExternInput("A");
    inputA.addDep("B");
    CompilerInput inputB = compiler.newExternInput("B");

    CompilerPass pass = new VarCheck(compiler);

    NodeTraversal traversalA = new NodeTraversal(rootA, pass);
    traversalA.setInput(inputA);
    traversalA.traverseAll();

    assertEquals(1, pass.getDiagnostics().size());
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY", pass.getDiagnostics().get(0).getType());
    assertEquals("B", pass.getDiagnostics().get(0).getMessage(null));

    CompilerPass sanityCheckPass = new VarCheck(compiler, true);

    NodeTraversal traversalB = new NodeTraversal(rootB, sanityCheckPass);
    traversalB.setInput(inputB);
    traversalB.traverseAll();

    assertTrue(sanityCheckPass.getDiagnostics().isEmpty());
  }

  @Test
  public void testStrictModuleDependency() {
    String moduleA = "function foo() { var x; }";
    String moduleB = "function bar(x) { }";

    Node rootA = Parser.parse(nodeFactory, moduleA, null, null);
    Node rootB = Parser.parse(nodeFactory, moduleB, null, null);

    CompilerInput inputA = compiler.newExternInput("A");
    inputA.addDep("B");
    CompilerInput inputB = compiler.newExternInput("B");

    CompilerPass pass = new VarCheck(compiler);

    NodeTraversal traversalA = new NodeTraversal(rootA, pass);
    traversalA.setInput(inputA);
    traversalA.traverseAll();

    assertEquals(0, pass.getDiagnostics().size());

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CheckLevel.ERROR);

    compiler = new Compiler(options);

    pass = new VarCheck(compiler);

    NodeTraversal traversalB = new NodeTraversal(rootB, pass);
    traversalB.setInput(inputB);
    traversalB.traverseAll();

    assertTrue(pass.getDiagnostics().isEmpty());

    // Strict check should report an error.
    options.setErrorLevel(JSError.make("", 0, 0, UNDEFINED_EXTERN_VAR_ERROR));

    compiler = new Compiler(options);

    pass = new VarCheck(compiler);

    traversalB = new NodeTraversal(rootB, pass);
    traversalB.setInput(inputB);
    traversalB.traverseAll();

    assertEquals(1, pass.getDiagnostics().size());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", pass.getDiagnostics().get(0).getType());
    assertEquals("B", pass.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testDuplicateDeclaration() {
    String code = "function foo() { var x; var x; }";
    Node root = Parser.parse(nodeFactory, code, null, null);
    CompilerPass pass = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(root, pass);
    traversal.traverseAll();

    assertEquals(1, pass.getDiagnostics().size());
    assertEquals("JSC_DUplicate_DECLARATION", pass.getDiagnostics().get(0).getType());
    assertEquals("x", pass.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testFunctionDeclarationWithEmptyName() {
    String code = "function() { var x; }";
    Node root = Parser.parse(nodeFactory, code, null, null);
    CompilerPass pass = new VarCheck(compiler);
    NodeTraversal traversal = new NodeTraversal(root, pass);
    traversal.traverseAll();

    assertTrue(pass.getDiagnostics().isEmpty());
  }
}