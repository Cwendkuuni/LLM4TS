package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

/**
 * Test class for CheckGlobalThis.
 */
public class CheckGlobalThisTest {

  private static AbstractCompiler compiler;
  private static NodeFactory nodeFactory;
  private static Parser parser;
  private static NodeTraversal traversal;

  @org.junit.BeforeClass
  public static void setUpBeforeClass() throws Exception {
    compiler = new AbstractCompiler();
    nodeFactory = compiler.getFactory();
    parser = new Parser(new CheckGlobalThis(compiler));
    traversal = new NodeTraversal(parser, null);
  }

  /**
   * Test shouldTraverse method for functions that are constructors or have the
   * @constructor annotation.
   */
  @Test
  public void testShouldTraverseConstructorFunctions() {
    String code = "function Foo() {}";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertFalse(checker.shouldTraverse(NodeTraversal.traverse, root, null));
  }

  /**
   * Test shouldTraverse method for functions that are annotated with @this.
   */
  @Test
  public void testShouldTraverseThisAnnotatedFunctions() {
    String code = "function Foo() { @this }";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertFalse(checker.shouldTraverse(NodeTraversal.traverse, root, null));
  }

  /**
   * Test shouldTraverse method for functions that are annotated with @override.
   */
  @Test
  public void testShouldTraverseOverrideAnnotatedFunctions() {
    String code = "function Foo() { @override }";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertFalse(checker.shouldTraverse(NodeTraversal.traverse, root, null));
  }

  /**
   * Test shouldTraverse method for functions that are not constructors or not
   * annotated with @constructor, @this, or @override.
   */
  @Test
  public void testShouldTraverseOtherFunctions() {
    String code = "function Foo() {}";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldTraverse(NodeTraversal.traverse, root, null));
  }

  /**
   * Test shouldTraverse method for functions that are getting lent to a
   * prototype.
   */
  @Test
  public void testShouldTraversePrototypeFunctions() {
    String code = "var obj = {getX: function() {}};";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertFalse(checker.shouldTraverse(NodeTraversal.traverse, root, null));
  }

  /**
   * Test shouldTraverse method for functions that are getting assigned on the
   * left side of an assignment.
   */
  @Test
  public void testShouldTraverseAssignmentFunctions() {
    String code = "var a = function() {};";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldTraverse(NodeTraversal.traverse, root, null));
  }

  /**
   * Test shouldTraverse method for functions that are getting assigned as
   * properties or subproperties.
   */
  @Test
  public void testShouldTraversePropertyFunctions() {
    String code = "var obj = {get X() {}};";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldTraverse(NodeTraversal.traverse, root, null));
  }

  /**
   * Test visit method for THIS keywords that are on the left side of an
   * assignment.
   */
  @Test
  public void testVisitThisOnLeftSideOfAssignment() {
    String code = "var a = this;";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isThis()) {
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  /**
   * Test visit method for THIS keywords that are on the right side of an
   * assignment.
   */
  @Test
  public void testVisitThisOnRightSideOfAssignment() {
    String code = "var a = {}; a = this;";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isThis()) {
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  /**
   * Test visit method for THIS keywords that are on the left side of a
   * property access.
   */
  @Test
  public void testVisitThisOnLeftSideOfPropertyAccess() {
    String code = "var a = this.x;";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isThis()) {
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  /**
   * Test visit method for THIS keywords that are on the right side of a
   * property access.
   */
  @Test
  public void testVisitThisOnRightSideOfPropertyAccess() {
    String code = "var a = {}; a.x = this;";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isThis()) {
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  /**
   * Test visit method for functions that have a JSDoc @constructor annotation.
   */
  @Test
  public void testVisitFunctionWithJSDocConstructorAnnotation() {
    String code = "/** @constructor */ function Foo() {}";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          JSDocInfo jsDoc = checker.getFunctionJsDocInfo(node);
          assertTrue(jsDoc != null && jsDoc.isConstructor());
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  /**
   * Test visit method for functions that have a JSDoc @this annotation.
   */
  @Test
  public void testVisitFunctionWithJSDocThisAnnotation() {
    String code = "function Foo() { @this }";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          JSDocInfo jsDoc = checker.getFunctionJsDocInfo(node);
          assertTrue(jsDoc != null && jsDoc.hasThisType());
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  /**
   * Test visit method for functions that have no logical place to put a
   * @this annotation.
   */
  @Test
  public void testVisitFunctionWithNoLogicalPlaceForThisAnnotation() {
    String code = "function Foo() {}";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  /**
   * Test visit method for functions that are inside a prototype method.
   */
  @Test
  public void testVisitFunctionInsidePrototypeMethod() {
    String code = "var obj = function() {}; obj.getX = function() { function Foo() {} };";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  /**
   * Test visit method for functions that are inside a constructor function.
   */
  @Test
  public void testVisitFunctionInsideConstructorFunction() {
    String code = "function Foo() { function Bar() {} }";
    Node root = parser.parse(code, null, null).getTree();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal.traverse(root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          checker.visit(NodeTraversal.traverse, node, null);
        }
      }
    });
    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}

interface NodeVisitor {
  void visit(Node node);
}