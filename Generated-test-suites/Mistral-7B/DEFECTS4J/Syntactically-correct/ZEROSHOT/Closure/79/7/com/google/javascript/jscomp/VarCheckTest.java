package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Pass;
import com.google.javascript.jscomp.SimpleSourceFile;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class VarCheckTest {

  @Test
  public void testVarCheck() {
    String sourceCode =
        "var x = 0;\n" +
        "function foo() {\n" +
        "  var x = 1;\n" +
        "}\n" +
        "function bar(x) {}\n" +
        "module foo {\n" +
        "  export var x = 0;\n" +
        "  export function baz() {}\n" +
        "}\n" +
        "module bar {\n" +
        "  import x from 'foo';\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new TestCompiler(options);

    CompilerInput input = new SimpleSourceFile("test.js", sourceCode, null);
    Node root = NodeTraversal.traverse(compiler, input.getAstRoot(compiler), null);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    // Check that x in the global scope is declared.
    Node globalX = root.getFirstChild();
    assertSame(NodeUtil.isVar(globalX), true);
    Scope globalScope = varCheck.compiler.getScope();
    assertEquals(globalScope.getVar("x"), globalX);

    // Check that x in function foo is a new variable.
    Node functionX = NodeUtil.getChildByProperty(root, "foo", "body")
        .getFirstChild();
    assertSame(NodeUtil.isVar(functionX), true);
    assertFalse(globalScope.getVar("x").equals(functionX));

    // Check that x in function bar does not need to be declared because
    // it is a formal parameter.
    Node functionBar = NodeUtil.getChildByProperty(root, "bar", "body");
    Node paramX = functionBar.getChild(0);
    assertSame(NodeUtil.isParam(paramX), true);
    assertSame(paramX.getName(), "x");
    assertNull(varCheck.compiler.getScope().getVar("x"));

    // Check that x in module foo is a variable and is exported.
    Node moduleFoo = NodeUtil.getChildByProperty(root, "foo", "body");
    Node varX = NodeUtil.getChildByProperty(moduleFoo, "exports", null);
    assertSame(NodeUtil.isVar(varX), true);
    assertEquals(varX.getName(), "x");
    assertTrue(varCheck.compiler.getScope().getVar("x").equals(varX));

    // Check that baz in module foo is a function and is exported.
    Node functionBaz = NodeUtil.getChildByProperty(moduleFoo, "baz", null);
    assertSame(NodeUtil.isFunction(functionBaz), true);
    assertEquals(functionBaz.getName(), "baz");
    assertSame(NodeUtil.isExported(functionBaz), true);

    // Check that x in module bar is imported from module foo.
    Node moduleBar = NodeUtil.getChildByProperty(root, "bar", "body");
    Node importX = NodeUtil.getChildByProperty(moduleBar, "imports", null);
    Node importedX = NodeUtil.getChildByProperty(importX, "local", null);
    assertSame(importedX.getName(), "x");
    assertSame(importedX, NodeUtil.getChildByProperty(moduleBar, "x", null));
  }

  @Test
  public void testVarCheckDuplicateDeclaration() {
    String sourceCode = "var x = 0;\nvar x = 1;\n";

    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new TestCompiler(options);

    CompilerInput input = new SimpleSourceFile("test.js", sourceCode, null);
    Node root = NodeTraversal.traverse(compiler, input.getAstRoot(compiler), null);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    // Check that the compiler reports an error for the duplicate declaration.
    assertEquals(varCheck.compiler.getNumDiagnostics(), 1);
    assertEquals(varCheck.compiler.getDiagnostics().getErrorCount(), 1);
  }

  @Test
  public void testVarCheckUndefinedVariable() {
    String sourceCode = "function foo() {\nvar x = 1;\nconsole.log(y);\n}";

    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new TestCompiler(options);

    CompilerInput input = new SimpleSourceFile("test.js", sourceCode, null);
    Node root = NodeTraversal.traverse(compiler, input.getAstRoot(compiler), null);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    // Check that the compiler reports an error for the undefined variable.
    assertEquals(varCheck.compiler.getNumDiagnostics(), 1);
    assertEquals(varCheck.compiler.getDiagnostics().getErrorCount(), 1);
  }

  @Test
  public void testVarCheckModuleDependency() {
    String sourceCodeFoo =
        "var x = 0;\n" +
        "export var y = x;\n";

    String sourceCodeBar =
        "import x from 'foo';\n" +
        "export var z = x;\n";

    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new TestCompiler(options);

    CompilerInput inputFoo = new SimpleSourceFile("foo.js", sourceCodeFoo, null);
    CompilerInput inputBar = new SimpleSourceFile("bar.js", sourceCodeBar, null);

    // Set up the module graph.
    JSModule moduleGraph = new JSModuleGraph();
    moduleGraph.addModule("foo", inputFoo.getModule());
    moduleGraph.addModule("bar", inputBar.getModule());
    moduleGraph.addDependency("bar", "foo");

    // Set up the compiler.
    CompilerInput rootInput = new SimpleSourceFile("test.js", "", null);
    CompilerInput[] inputs = {inputFoo, inputBar, rootInput};
    AbstractCompiler compiler = new TestCompiler(options, inputs, moduleGraph);

    Node root = NodeTraversal.traverseRoots(compiler, inputs, new VarCheck(compiler))
        .get(0);

    // Check that z in module bar is imported from module foo and has the
    // expected value.
    Node moduleBar = NodeUtil.getChildByProperty(root, "bar", "body");
    Node varZ = NodeUtil.getChildByProperty(moduleBar, "exports", null);
    assertSame(varZ.getType(), Token.VAR);
    Node importedX = NodeUtil.getChildByProperty(moduleBar, "imports", null)
        .getChild(0);
    Node importedY = NodeUtil.getChildByProperty(importedX, "local", null);
    Node exportX = NodeUtil.getChildByProperty(moduleBar, "foo", null);
    Node exportY = NodeUtil.getChildByProperty(exportX, "body", null)
        .getChild(0);
    assertEquals(importedY.getName(), "y");
    assertEquals(importedY, NodeUtil.getChildByProperty(varZ, "initial", null));
    assertEquals(exportY.getName(), "y");
    assertEquals(NodeUtil.getNumberValue(exportY), 0);
  }

  @Test
  public void testVarCheckModuleDependencyViolation() {
    String sourceCodeFoo = "export var x = 0;\n";
    String sourceCodeBar = "import x from 'foo';\n";

    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new TestCompiler(options);

    CompilerInput inputFoo = new SimpleSourceFile("foo.js", sourceCodeFoo, null);
    CompilerInput inputBar = new SimpleSourceFile("bar.js", sourceCodeBar, null);

    // Set up the module graph.
    JSModule moduleGraph = new JSModuleGraph();
    moduleGraph.addModule("foo", inputFoo.getModule());
    moduleGraph.addModule("bar", inputBar.getModule());
    moduleGraph.addDependency("bar", "foo");

    // Set up the compiler.
    CompilerInput rootInput = new SimpleSourceFile("test.js", "", null);
    AbstractCompiler compiler = new TestCompiler(options, inputs, moduleGraph);

    // Create a new NodeFactory to create nodes for the test.
    NodeFactory nodeFactory = new NodeFactory();

    // Create the root node.
    Node root = Node.newBlock(nodeFactory);
    Node moduleBar = Node.newModule(nodeFactory, "bar");
    Node imports = Node.newList(nodeFactory, Token.IMPORTS, null);
    Node importX = Node.newImportDeclaration(nodeFactory, null, "foo", "x", null);
    imports.addChildToBack(importX);
    moduleBar.addChildToBack(imports);
    root.addChildToBack(moduleBar);

    // Run the VarCheck pass.
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    // Check that the compiler reports an error for the module dependency
    // violation.
    assertEquals(varCheck.compiler.getNumDiagnostics(), 1);
    assertEquals(varCheck.compiler.getDiagnostics().getErrorCount(), 1);
  }

  @Test
  public void testVarCheckModuleDependencyMissing() {
    String sourceCodeFoo = "export var x = 0;\n";
    String sourceCodeBar = "import x from 'baz';\n";

    CompilerOptions options = new CompilerOptions(true);
    AbstractCompiler compiler = new TestCompiler(options);

    CompilerInput inputFoo = new SimpleSourceFile("foo.js", sourceCodeFoo, null);
    CompilerInput inputBar = new SimpleSourceFile("bar.js", sourceCodeBar, null);

    // Set up the module graph.
    JSModule moduleGraph = new JSModuleGraph();
    moduleGraph.addModule("foo", inputFoo.getModule());
    moduleGraph.addModule("bar", inputBar.getModule());

    // Set up the compiler.
    CompilerInput rootInput = new SimpleSourceFile("test.js", "", null);
    AbstractCompiler compiler = new TestCompiler(options, inputs, moduleGraph);

    // Create a new NodeFactory to create nodes for the test.
    NodeFactory nodeFactory = new NodeFactory();

    // Create the root node.
    Node root = Node.newBlock(nodeFactory);
    Node moduleBar = Node.newModule(nodeFactory, "bar");
    Node imports = Node.newList(nodeFactory, Token.IMPORTS, null);
    Node importX = Node.newImportDeclaration(nodeFactory, null, "baz", "x", null);
    imports.addChildToBack(importX);
    moduleBar.addChildToBack(imports);
    root.addChildToBack(moduleBar);

    // Run the VarCheck pass.
    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverse(compiler, root, varCheck);

    // Check that the compiler reports a warning for the missing module
    // dependency.
    assertEquals(varCheck.compiler.getNumDiagnostics(), 1);
    assertEquals(varCheck.compiler.getDiagnostics().getWarningCount(), 1);
  }
}