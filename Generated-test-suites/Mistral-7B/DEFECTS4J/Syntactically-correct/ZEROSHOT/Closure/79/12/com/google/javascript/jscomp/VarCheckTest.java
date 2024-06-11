import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticListener;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class VarCheckTest {

  private Compiler compiler;
  private DiagnosticListener diagnostics;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    diagnostics = new DiagnosticListener() {
      @Override
      public void report(int line, int character, int code, String message,
          Object[] data, Object location) {
        fail("Unexpected diagnostic: " + message);
      }
    };
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testSimpleVariableDeclaration() {
    String input = "function foo() {" + "  var x; " + "  x = 5; " + "}";

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    assertNotNull(globalScope.getVar("x"));
  }

  @Test
  public void testUndefinedVariable() {
    String input = "function foo() {" + "  var x = y; " + "}";

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    assertNull(globalScope.getVar("y"));
    assertEquals(1, diagnostics.getDiagnostics().size());
    Diagnostics diag = diagnostics.getDiagnostics().get(0);
    assertEquals(DiagnosticType.ERROR, diag.getType());
    assertEquals("variable y is undefined", diag.getMessage());
  }

  @Test
  public void testUndefinedVariableInFunction() {
    String input = "function foo() {" + "  function bar() {" + "    var x = y; " + "  } " + "}";

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    assertNull(globalScope.getVar("y"));
    assertEquals(1, diagnostics.getDiagnostics().size());
    Diagnostics diag = diagnostics.getDiagnostics().get(0);
    assertEquals(DiagnosticType.ERROR, diag.getType());
    assertEquals("variable y is undefined", diag.getMessage());
  }

  @Test
  public void testUndefinedVariableInFunctionWithExterns() {
    String input = "//@extern function foo() {" +
        "//@extern var y;" +
        "function bar() {" +
        "  var x = y; " +
        "}" +
        "}";

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    assertNotNull(globalScope.getVar("y"));
    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testUndefinedVariableInGlobalScope() {
    String input = "var y; " + "function foo() {" + "  var x = y; " + "}";

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    assertNotNull(globalScope.getVar("y"));
    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testVariableDeclarationInExterns() {
    String input = "//@extern function foo() {" +
        "//@extern var x;" +
        "function bar() {" +
        "  var x = 5; " +
        "}" +
        "}";

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    Scope barScope = globalScope.getChildScope("bar");
    assertNotNull(barScope.getVar("x"));
    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    String input = "function foo() {" +
        "  var x;" +
        "  var x;" +
        "}"
        ;

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    assertNotNull(globalScope.getVar("x"));
    assertEquals(1, diagnostics.getDiagnostics().size());
    Diagnostics diag = diagnostics.getDiagnostics().get(0);
    assertEquals(DiagnosticType.WARNING, diag.getType());
    assertEquals("duplicate declaration of variable x", diag.getMessage());
  }

  @Test
  public void testDuplicateVariableDeclarationInExterns() {
    String input = "//@extern function foo() {" +
        "//@extern var x;" +
        "//@extern var x;" +
        "}"
        ;

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    ImmutableSet<String> vars = ImmutableSet.of("x");
    assertEquals(1, globalScope.getAllVars().size());
    assertEquals(vars, globalScope.getAllVars().keySet());
    assertEquals(1, diagnostics.getDiagnostics().size());
    Diagnostics diag = diagnostics.getDiagnostics().get(0);
    assertEquals(DiagnosticType.WARNING, diag.getType());
    assertEquals("duplicate declaration of variable x", diag.getMessage());
  }

  @Test
  public void testVariableDeclarationWithNameReferenceInExterns() {
    String input = "//@extern function foo() {" +
        "//@extern var x;" +
        "function bar() {" +
        "  var x = 5;" +
        "}" +
        "}"
        ;

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    Scope globalScope = compiler.getGlobalScope();
    Scope barScope = globalScope.getChildScope("bar");
    assertNotNull(barScope.getVar("x"));
    assertEquals(1, diagnostics.getDiagnostics().size());
    Diagnostics diag = diagnostics.getDiagnostics().get(0);
    assertEquals(DiagnosticType.WARNING, diag.getType());
    assertEquals("accessing name x in externs has no effect", diag.getMessage());
  }

  @Test
  public void testMissingModuleDependency() {
    String inputA = "//@extern library foo {}";
    String inputB = "//@extern library bar {}";
    String inputC = "//@extern library baz {}";

    String input = "//@extern library a {" +
        "//@extern function foo.bar() {}" +
        "}" +
        "//@extern library b {" +
        "//@extern function baz.qux() {" +
        "  var x = foo.bar.quux; // missing dependency on a" +
        "}" +
        "}"
        ;

    CompilerInput inputCompiledA = compiler.compile(inputA, null, diagnostics);
    CompilerInput inputCompiledB = compiler.compile(inputB, null, diagnostics);
    CompilerInput inputCompiledC = compiler.compile(inputC, null, diagnostics);
    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);

    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    assertEquals(1, diagnostics.getDiagnostics().size());
    Diagnostics diag = diagnostics.getDiagnostics().get(0);
    assertEquals(DiagnosticType.WARNING, diag.getType());
    assertEquals("missing module dependency; module b should depend on library a because it references foo.bar.quux", diag.getMessage());
  }

  @Test
  public void testStrictModuleDependency() {
    String inputA = "//@extern library foo {}";
    String inputB = "//@extern library bar {}";

    String input = "//@extern library a {" +
        "//@extern function foo.bar() {}" +
        "}" +
        "//@extern library b {" +
        "//@extern function baz.qux() {" +
        "  var x = foo.bar.quux; // should not be an error since a loads before b" +
        "}" +
        "}"
        ;

    CompilerInput inputCompiledA = compiler.compile(inputA, null, diagnostics);
    CompilerInput inputCompiledB = compiler.compile(inputB, null, diagnostics);

    NodeTraversal.traverse(compiler, inputCompiledB.getAstRoot(compiler), new VarCheck(compiler, true));
    assertEquals(0, diagnostics.getDiagnostics().size());

    CompilerOptions options = new CompilerOptions();
    options.setErrorLevel(JSError.make("", 0, 0, UNDEFINED_EXTERN_VAR_ERROR), CheckLevel.ERROR);

    Compiler compilerStrict = new Compiler(options);
    CompilerInput inputCompiledStrictA = compilerStrict.compile(inputA, null, diagnostics);
    CompilerInput inputCompiledStrictB = compilerStrict.compile(inputB, null, diagnostics);

    NodeTraversal.traverse(compilerStrict, inputCompiledStrictB.getAstRoot(compilerStrict), new VarCheck(compilerStrict, true));
    assertEquals(1, diagnostics.getDiagnostics().size());
    Diagnostics diag = diagnostics.getDiagnostics().get(0);
    assertEquals(DiagnosticType.ERROR, diag.getType());
    assertEquals("module b cannot reference foo, defined in library a, since library a loads after library b", diag.getMessage());
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    String input = "function foo() {}";

    CompilerInput inputCompiled = compiler.compile(input, null, diagnostics);
    Node root = inputCompiled.getAstRoot(compiler);

    NodeTraversal.traverse(compiler, root, new VarCheck(compiler));
    assertEquals(1, diagnostics.getDiagnostics().size());
    Diagnostics diag = diagnostics.getDiagnostics().get(0);
    assertEquals(DiagnosticType.ERROR, diag.getType());
    assertEquals("Syntax error: function declaration must have a name", diag.getMessage());
  }
}