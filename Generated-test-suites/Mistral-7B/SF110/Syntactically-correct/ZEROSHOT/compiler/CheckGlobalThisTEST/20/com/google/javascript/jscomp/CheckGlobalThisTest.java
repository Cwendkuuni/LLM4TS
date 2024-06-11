package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

/**
 * Tests for {@code CheckGlobalThis}.
 */
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getOptions().getOptionsFor(AbstractCompiler.class);
    abstractCompiler.setWarningsAsErrors(true);
    abstractCompiler.setUseErrorConsolidation(true);

    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler);
    Parser parser = new Parser(new SourceFile("Test.js", ""));
    Node program = parser.parseProgram();

    NodeTraversal traversal = new NodeTraversal(program, checker);
    traversal.traverse();

    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test safe usage of 'this'.
    String safeCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "function FooConstructor() {}\n" +
        "FooConstructor.prototype = Foo.prototype;\n" +
        "FooConstructor.prototype.constructor = FooConstructor;\n" +
        "function FooFunction() {}\n" +
        "FooFunction.prototype = { x: function() {}}\n" +
        "var FooVar = function() {}\n";

    parser = new Parser(new SourceFile("Test.js", safeCode));
    program = parser.parseProgram();
    traversal = new NodeTraversal(program, checker);
    traversal.traverse();

    assertTrue(checker.compiler.getDiagnostics().isEmpty());

    // Test unsafe usage of 'this'.
    String unsafeCode = "function evil() { this.useful = undefined; }\n";

    parser = new Parser(new SourceFile("Test.js", unsafeCode));
    program = parser.parseProgram();
    traversal = new NodeTraversal(program, checker);
    traversal.traverse();

    assertFalse(checker.compiler.getDiagnostics().isEmpty());
    assertTrue(checker.compiler.getDiagnostics().size() == 1);
  }
}