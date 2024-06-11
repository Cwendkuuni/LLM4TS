package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.parser.Parser;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Test cases for shouldTraverse method

    // Traverse function with @constructor annotation
    Node functionConstructor = Parser.parseCompilationUnit(
        "function MyConstructor() {}").getTree().getChildAtIndex(0);
    CheckGlobalThis checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    Checker c = checker.compiler.getChecker();
    assertFalse(checker.shouldTraverse(new NodeTraversal(c, null, functionConstructor), functionConstructor, null));

    // Traverse function with @this annotation
    Node functionThis = Parser.parseCompilationUnit(
        "function MyFunction() { @this }").getTree().getChildAtIndex(0);
    checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    assertFalse(checker.shouldTraverse(new NodeTraversal(c, null, functionThis), functionThis, null));

    // Traverse function with @override annotation
    Node functionOverride = Parser.parseCompilationUnit(
        "function MyFunction() { @override }").getTree().getChildAtIndex(0);
    checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    assertFalse(checker.shouldTraverse(new NodeTraversal(c, null, functionOverride), functionOverride, null));

    // Traverse function inside a block or script
    Node functionBlock = Parser.parseCompilationUnit(
        "function MyFunction() {}") //
            .getTree() //
            .getChildAtIndex(0) //
            .getLastChild();
    checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    assertTrue(checker.shouldTraverse(new NodeTraversal(c, null, functionBlock), functionBlock, null));

    // Traverse function inside an object literal
    Node functionObject = Parser.parseCompilationUnit(
        "var myObj = { myFunction: function() {} }") //
            .getTree() //
            .getChildAtIndex(0) //
            .getLastChild();
    checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    assertTrue(checker.shouldTraverse(new NodeTraversal(c, null, functionObject), functionObject, null));

    // Traverse function that is lent to a prototype
    Node functionPrototype = Parser.parseCompilationUnit(
        "var MyPrototype = function() {};\n" //
            + "MyPrototype.prototype = {\n" //
            + "  myFunction: function() {}\n" //
            + "};") //
            .getTree() //
            .getChildAtIndex(0) //
            .getLastChild();
    checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    assertFalse(checker.shouldTraverse(new NodeTraversal(c, null, functionPrototype), functionPrototype, null));
  }

  @Test
  public void testShouldReportThis() {
    // Test cases for shouldReportThis method

    // Report THIS on the left side of an assignment
    Node assignmentLeftThis = Parser.parseCompilationUnit(
        "var a = this;") //
            .getTree() //
            .getChildAtIndex(0) //
            .getLastChild();
    CheckGlobalThis checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    assertTrue(checker.shouldReportThis(assignmentLeftThis));

    // Report THIS with a property access
    Node propertyAccessThis = Parser.parseCompilationUnit(
        "var a = { myProperty: this };") //
            .getTree() //
            .getChildAtIndex(0) //
            .getLastChild() //
            .getLastChild();
    checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    assertTrue(checker.shouldReportThis(propertyAccessThis));
  }

  @Test
  public void testShouldTraverseFunctionsWithAssignLhsChild() {
    // Test cases for shouldTraverse method when assignLhsChild is set

    // Traverse function inside a block or script with assignLhsChild set
    Node functionBlockWithAssignLhsChild = Parser.parseCompilationUnit(
        "function MyFunction() {\n" //
            + "  var a = this;\n" //
            + "  a.myProperty = 'value';\n" //
            + "}") //
            .getTree() //
            .getChildAtIndex(0) //
            .getLastChild();
    CheckGlobalThis checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    checker.assignLhsChild = functionBlockWithAssignLhsChild.getFirstChild();
    assertTrue(checker.shouldTraverse(new NodeTraversal(checker.compiler.getChecker(), null, functionBlockWithAssignLhsChild), functionBlockWithAssignLhsChild, null));

    // Traverse function inside an object literal with assignLhsChild set
    Node functionObjectWithAssignLhsChild = Parser.parseCompilationUnit(
        "var myObj = {\n" //
            + "  myFunction: function() {\n" //
            + "    var a = this;\n" //
            + "    a.myProperty = 'value';\n" //
            + "  }\n" //
            + "};") //
            .getTree() //
            .getChildAtIndex(0) //
            .getLastChild();
    checker = new CheckGlobalThis(Compiler.createCheckerToCompiler(new Compiler()));
    checker.assignLhsChild = functionObjectWithAssignLhsChild.getFirstChild();
    assertTrue(checker.shouldTraverse(new NodeTraversal(checker.compiler.getChecker(), null, functionObjectWithAssignLhsChild), functionObjectWithAssignLhsChild, null));
  }

  @Test
  public void testVisit() {
    // Test cases for visit method

    // Report error when THIS is encountered
    Node thisNode = Parser.parseCompilationUnit(
        "function MyFunction() {\n" //
            + "  this;\n" //
            + "}") //
            .getTree() //
            .getChildAtIndex(0) //
            .getLastChild();
    Compiler compiler = new Compiler();
    parser.parseAndCompile(new SourceFile("MyFunction.js"), compiler);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(checker.compiler.getChecker(), null, thisNode);
    checker.visit(traversal, thisNode, null);
    assertTrue(compiler.getDiagnostics().hasError(GLOBAL_THIS));
  }
}