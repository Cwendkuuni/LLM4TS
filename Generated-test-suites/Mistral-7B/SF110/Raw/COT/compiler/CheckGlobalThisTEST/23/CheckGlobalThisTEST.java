package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Token;

/**
 * Test class for CheckGlobalThis class.
 *
 * @author your.name
 */
@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  /**
   * Test method for constructor.
   */
  @Test
  public void testConstructor() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker instanceof Callback);
  }

  /**
   * Test method for shouldTraverse.
   */
  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checker = new CheckGlobalThis(null);

    // Test a normal function declaration
    Node functionNode = Parser.parseFunctionDeclaration("function foo() {}", null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(null), functionNode, null));

    // Test a constructor
    Node constructorNode = Parser.parseFunctionDeclaration("function Foo() {}", null);
    assertTrue(constructorNode.isConstructor());
    assertFalse(checker.shouldTraverse(new NodeTraversal(null), constructorNode, null));

    // Test a function annotated with @constructor
    JSDocInfo jsDoc = Parser.parseFunctionJsDocInfo("function Foo() {}", null);
    jsDoc.setConstructor(true);
    Node constructorAnnotatedNode = Parser.parseFunctionDeclarationWithJsDoc(jsDoc, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(null), constructorAnnotatedNode, null));

    // Test a function annotated with @this
    jsDoc = Parser.parseFunctionJsDocInfo("function Foo() { @this }", null);
    Node thisAnnotatedNode = Parser.parseFunctionDeclarationWithJsDoc(jsDoc, null);
    assertFalse(checker.shouldTraverse(new NodeTraversal(null), thisAnnotatedNode, null));

    // Test a function with no logical place to put a @this annotation
    Node noLogicalPlaceNode = Parser.parseFunctionDeclaration("function foo() {}", null);
    assertTrue(checker.shouldTraverse(new NodeTraversal(null), noLogicalPlaceNode, null));

    // Test a prototype method
    Node prototypeNode = Parser.parseFunctionDeclarationWithJsDoc("function Foo.prototype.bar = function() {}", null);
    assertTrue(prototypeNode.isFunction() && prototypeNode.getParent().isObject());
    assertTrue(checker.shouldTraverse(new NodeTraversal(null), prototypeNode, prototypeNode.getParent()));

    // Test a function inside a script
    Node scriptNode = Parser.parseScript("(function foo() {})", null);
    assertTrue(checker.shouldTraverse(new NodeTraversal(null), scriptNode, null));

    // Test a function inside a block
    Node blockNode = Parser.parseBlock("{ function foo() {} }", null);
    assertTrue(checker.shouldTraverse(new NodeTraversal(null), blockNode, null));

    // Test a function inside an object literal
    Node objectNode = Parser.parseObjectLiteral("{ foo: function() {} }", null);
    assertTrue(checker.shouldTraverse(new NodeTraversal(null), objectNode, null));

    // Test a function inside an array literal
    Node arrayNode = Parser.parseArrayLiteral("[function foo() {}]", null);
    assertTrue(checker.shouldTraverse(new NodeTraversal(null), arrayNode, null));

    // Test a function that is getting lent to a prototype
    Node prototypeLentNode = Parser.parseFunctionDeclarationWithJsDoc("function Foo.prototype.prototypeBar = function() {}", null);
    JSDocInfo maybeLends = prototypeLentNode.getParent().getParent();
    assertTrue(maybeLends != null && maybeLends.getLendsName() != null && maybeLends.getLendsName().equals("Foo.prototype"));
    assertFalse(checker.shouldTraverse(new NodeTraversal(null), prototypeLentNode, prototypeLentNode.getParent().getParent()));
  }

  /**
   * Test method for visit.
   */
  @Test
  public void testVisit() {
    SourceFile sourceFile = new SourceFile("test.js", "test");
    CheckerFactory checkerFactory = new CheckerFactory(new Compiler(), new Parser(), null, null, null);
    Compiler compiler = checkerFactory.getChecker("checkGlobalThis");
    CheckGlobalThis checker = (CheckGlobalThis) compiler.getProcessor("CheckGlobalThis");
    Node rootNode = Parser.parseScript("this = 1;", null);

    NodeTraversal traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    // Test reporting a global THIS
    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() > 0);

    // Test not reporting a THIS inside a constructor
    Node constructorNode = Parser.parseFunctionDeclaration("function Foo() {}", null);
    Node constructorBodyNode = Parser.parseBlock("{ this.x = 1; }", null);
    rootNode.addChild(constructorNode);
    constructorNode.getFirstChild().addChild(constructorBodyNode);

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);

    // Test not reporting a THIS inside a function annotated with @this
    Node thisAnnotatedNode = Parser.parseFunctionDeclarationWithJsDoc("function Foo@this() {}", null);
    rootNode.addChild(thisAnnotatedNode);

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);

    // Test not reporting a THIS inside a function where there's no logical place to put a @this annotation
    Node noLogicalPlaceNode = Parser.parseFunctionDeclaration("function foo() {}", null);
    rootNode.addChild(noLogicalPlaceNode);

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);

    // Test not reporting a THIS inside a prototype method
    Node prototypeNode = Parser.parseFunctionDeclarationWithJsDoc("function Foo.prototype.bar = function() { this.x = 1; }", null);
    rootNode.addChild(Parser.parseObjectLiteral("{ x: 1 }", null));
    rootNode.addChild(Parser.parseBlock("", prototypeNode));

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);

    // Test not reporting a THIS on the left side of an assignment
    Node assignmentNode = Parser.parseAssignment("this = 1;", null);
    rootNode.addChild(assignmentNode);

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);

    // Test not reporting a THIS with a property access
    Node getPropertyNode = Parser.parseGetProperty("this.x", null);
    rootNode.addChild(getPropertyNode);

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);

    // Test not reporting a THIS inside a block
    Node blockNode = Parser.parseBlock("{ this.x = 1; }", null);
    rootNode.addChild(blockNode);

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);

    // Test not reporting a THIS inside an object literal
    Node objectNode = Parser.parseObjectLiteral("{ this.x: 1 }", null);
    rootNode.addChild(objectNode);

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);

    // Test not reporting a THIS inside an array literal
    Node arrayNode = Parser.parseArrayLiteral("{ this: 1 }", null);
    rootNode.addChild(arrayNode);

    traversal = new NodeTraversal(rootNode, new NodeTraversal.Callback() {
      @Override
      public void visit(Node n) {
        checker.visit(traversal, n, null);
      }
    });

    assertTrue(traversal.traverse());
    assertTrue(compiler.getDiagnostics().size() == 1);
  }
}