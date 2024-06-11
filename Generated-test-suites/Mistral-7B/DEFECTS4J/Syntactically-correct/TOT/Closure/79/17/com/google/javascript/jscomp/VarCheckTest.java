import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VarCheck;
import com.google.javascript.rhino.Token;

import java.util.Set;

/**
 * Tests for {@link VarCheck} class.
 */
public class VarCheckTest {

  private CompilerInput input;
  private CompilerInput externsInput;
  private Node root;
  private VarCheck checker;

  @org.junit.Before
  public void setUp() {
    input = new CompilerInput("test.js");
    externsInput = new CompilerInput("externs.js");

    root = NodeOperations.parse("function foo() {" +
                               "  var x, y;" +
                               "}", null);

    checker = new VarCheck(new Compiler());
  }

  @org.junit.Test
  public void testNormalCase() {
    NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(input.getAstRoot(checker.getCompiler()), root), checker);

    Scope globalScope = checker.compiler.getTopLevelScope();

    assertEquals("x should be declared", 1, globalScope.getVarCount());
    assertEquals("y should be declared", 1, globalScope.getVarCount());

    assertFalse("x should not be declared in externs", checker.varsToDeclareInExterns.contains("x"));
    assertFalse("y should not be declared in externs", checker.varsToDeclareInExterns.contains("y"));
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    input = new CompilerInput("test.js", "",
        "function foo() {" +
        "  var x;" +
        "  y = 5; // undefined variable y",
        null);
    root = input.getAstRoot(checker.getCompiler());

    NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(input.getAstRoot(checker.getCompiler()), root), checker);

    Scope globalScope = checker.compiler.getTopLevelScope();

    assertFalse("x should be declared", globalScope.getVar("x") instanceof Scope.Undeclared);
    assertTrue("y should be undeclared", globalScope.getVar("y") instanceof Scope.Undeclared);

    assertEquals("One undefined variable error reported", 1, checker.compiler.getDiagnostics().size());
    assertEquals("Expected error message",
        "variable y is undefined",
        checker.compiler.getDiagnostics().get(0).getMessageText());
  }

  @org.junit.Test
  public void testModuleDependency() {
    String moduleA = "module A {" +
                     "  export function foo() {" +
                     "    var a;" +
                     "  }" +
                     "}";
    String moduleB = "module B {" +
                     "  import foo from 'A';" +
                     "  function bar() {" +
                     "    var a;" +
                     "  }" +
                     "}";

    input = new CompilerInput("test.js", "",
        moduleA,
        null);
    externsInput = new CompilerInput("externs.js", "",
        moduleB,
        null);

    root = NodeOperations.parse(moduleB, null);

    checker = new VarCheck(new Compiler());

    NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(input.getAstRoot(checker.getCompiler()), root), checker);

    Scope globalScope = checker.compiler.getTopLevelScope();

    assertTrue("a should be declared in module A", globalScope.getVar("a") != null);
    assertTrue("a should not be declared in module B", globalScope.getVar("a") == null);

    assertFalse("No error should be reported", checker.compiler.getDiagnostics().isEmpty());
    assertEquals("Expected warning message",
        "missing module dependency; module B should depend on module A because it references a",
        checker.compiler.getDiagnostics().get(0).getMessageText());
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    String moduleA = "module A {}";
    String moduleB = "module B {" +
                    "  import foo from 'A';" +
                    "  function bar() {}" +
                    "}";

    input = new CompilerInput("test.js", "",
        moduleA,
        null);
    externsInput = new CompilerInput("externs.js", "",
        moduleB,
        null);

    root = NodeOperations.parse(moduleB, null);

    checker = new VarCheck(new Compiler(), true);

    NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(input.getAstRoot(checker.getCompiler()), root), checker);

    assertFalse("No error should be reported", checker.compiler.getDiagnostics().isEmpty());
    assertEquals("Expected disabled error message",
        "module B cannot reference foo, defined in module A",
        checker.compiler.getDiagnostics().get(0).getMessageText());
  }

  @org.junit.Test
  public void testDuplicateVariableDeclaration() {
    String module = "module M {" +
                    "  export function foo() {" +
                    "    var x = 5;" +
                    "    var x;" +
                    "  }" +
                    "}";

    input = new CompilerInput("test.js", "",
        module,
        null);

    root = NodeOperations.parse(module, null);

    checker = new VarCheck(new Compiler());

    NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(input.getAstRoot(checker.getCompiler()), root), checker);

    Scope globalScope = checker.compiler.getTopLevelScope();

    assertEquals("x should be declared only once", 1, globalScope.getVarCount());

    assertTrue("x should be declared in externs", checker.varsToDeclareInExterns.isEmpty());
  }

  @org.junit.Test
  public void testEmptyFunctionNameDeclaration() {
    String module = "module M {" +
                    "  export function foo() {}" +
                    "}";

    input = new CompilerInput("test.js", "",
        module,
        null);

    root = NodeOperations.parse(module, null);

    checker = new VarCheck(new Compiler());

    NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(input.getAstRoot(checker.getCompiler()), root), checker);

    Scope globalScope = checker.compiler.getTopLevelScope();

    assertTrue("foo should be declared", globalScope.getVar("foo") != null);
  }

  @org.junit.Test
  public void testExternVariableReference() {
    String module = "module M {" +
                    "  export function foo() {" +
                    "    var x = 5;" +
                    "    var y = x;" +
                    "  }" +
                    "}";

    input = new CompilerInput("test.js", "",
        module,
        null);

    externalVariable("x", "number");

    root = NodeOperations.parse(module, null);

    checker = new VarCheck(new Compiler());

    NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(input.getAstRoot(checker.getCompiler()), root), checker);

    Scope globalScope = checker.compiler.getTopLevelScope();

    assertTrue("x should be declared in externs", checker.varsToDeclareInExterns.isEmpty());

    assertTrue("y should be declared", globalScope.getVar("y") != null);
  }

  @org.junit.Test
  public void testFunctionDeclarationWithoutName() {
    String module = "module M {" +
                    "  export function foo() {" +
                    "    var x = 5;" +
                    "  }" +
                    "  function bar() {}" +
                    "}";

    input = new CompilerInput("test.js", "",
        module,
        null);

    root = NodeOperations.parse(module, null);

    checker = new VarCheck(new Compiler());

    NodeTraversal.traverseRoots(
        checker, Lists.newArrayList(input.getAstRoot(checker.getCompiler()), root), checker);

    assertFalse("No error should be reported", checker.compiler.getDiagnostics().isEmpty());
    assertEquals("Expected error message",
        "function bar declaration must have a name",
        checker.compiler.getDiagnostics().get(0).getMessageText());
  }

  private void externalVariable(String variableName, String type) {
    externsInput = new CompilerInput("externs.js", "",
        String.format("/** @type {!%s} **/\nvar %s;", type, variableName),
        null);
  }
}