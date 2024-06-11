package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = 1;" +
        "  function bar() {" +
        "    var x = 2;" +
        "  }" +
        "  var x = 3;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode);
    Node rootNode = compiler.getRootNode();

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("1", rootNode.getFirstChild().getLastChild().getString());
    assertEquals("1_0", rootNode.getLastChild().getFirstChild().getLastChild().getString());
    assertEquals("1_1", rootNode.getLastChild().getLastChild().getNext().getString());
    assertEquals("2", rootNode.getLastChild().getLastChild().getNext().getLastChild().getString());
    assertEquals("3", rootNode.getLastChild().getLastChild().getNext().getNext().getString());

    Node barFunction = rootNode.getLastChild().getFirstChild();
    assertEquals("bar_0", barFunction.getNameNode().getString());

    // Test that unused names are not renamed.
    String jsCode2 = "function foo() {" +
        "  var x = 1;" +
        "  var y = 1;" +
        "  function bar() {}" + // unused
        "  var x = 3;" +
        "}";

    compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode2);
    rootNode = compiler.getRootNode();

    renamer = new MakeDeclaredNamesUnique();
    traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("1", rootNode.getFirstChild().getLastChild().getString());
    assertEquals("1_0", rootNode.getLastChild().getFirstChild().getLastChild().getString());
    assertEquals("1_1", rootNode.getLastChild().getFirstChild().getNext().getString());
    assertEquals("3", rootNode.getLastChild().getLastChild().getNext().getNext().getString());
    assertNull(NodeUtil.findNode(barFunction, rootNode));
  }

  @Test
  public void testMakeDeclaredNamesUniqueInGlobalScope() {
    String jsCode = "var x = 1;" +
        "var y = 1;" +
        "function foo() {" +
        "  var x = 2;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode);
    Node rootNode = compiler.getRootNode();

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    // Global variables should not be renamed.
    assertEquals("x", rootNode.getFirstChild().getNameNode().getString());
    assertEquals("x", rootNode.getLastChild().getNameNode().getString());

    Node fooFunction = rootNode.getLastChild();
    assertEquals("foo_0", fooFunction.getNameNode().getString());

    // Test that local variable with the same name as a global variable is renamed.
    String jsCode2 = "var x = 1;" +
        "function foo() {" +
        "  var x = 2;" +
        "}";

    compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode2);
    rootNode = compiler.getRootNode();

    renamer = new MakeDeclaredNamesUnique();
    traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("x_1", rootNode.getFirstChild().getNameNode().getString());
    assertEquals("x_2", fooFunction.getNameNode().getString());
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithContextualRenamer() {
    String jsCode = "function foo() {" +
        "  function bar() {" +
        "    var x = 1;" +
        "  }" +
        "  function baz() {" +
        "    var x = 1;" +
        "  }" +
        "  function qux() {" +
        "    function quux() {" +
        "      var x = 1;" +
        "    }" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode);
    Node rootNode = compiler.getRootNode();

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique.ContextualRenamer contextualRenamer = renamer.new ContextualRenamer();
    NodeTraversal traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);

    // Traverse the entire tree with the ContextualRenamer.
    traverser.addCallback(contextualRenamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("bar_0", rootNode.getLastChild().getFirstChild().getNameNode().getString());
    assertEquals("baz_0", rootNode.getLastChild().getLastChild().getFirstChild().getNameNode().getString());
    assertEquals("quux_0", NodeUtil.findNode(rootNode, "qux").getLastChild().getLastChild().getNameNode().getString());

    // Traverse the entire tree again with a new instance of MakeDeclaredNamesUnique
    // to make sure that the names are not modified by the previous traversal.
    renamer = new MakeDeclaredNamesUnique();
    traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("bar_1", rootNode.getLastChild().getFirstChild().getNameNode().getString());
    assertEquals("baz_1", rootNode.getLastChild().getLastChild().getFirstChild().getNameNode().getString());
    assertEquals("quux_1", NodeUtil.findNode(rootNode, "qux").getLastChild().getLastChild().getNameNode().getString());
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithInlineRenamer() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  function bar() {" +
        "    var x = 2;" +
        "  }" +
        "  var x = 3;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode);
    Node rootNode = compiler.getRootNode();

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique.InlineRenamer inlineRenamer = renamer.new InlineRenamer(() -> "_", "_foo", true);
    NodeTraversal traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);

    // Traverse the entire tree with the InlineRenamer.
    traverser.addCallback(inlineRenamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("1_foo_0", rootNode.getFirstChild().getLastChild().getString());
    assertEquals("_foo_1", rootNode.getLastChild().getFirstChild().getNameNode().getString());
    assertEquals("_foo_2", rootNode.getLastChild().getLastChild().getNameNode().getString());
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithContextualAndInlineRenamers() {
    String jsCode = "function foo() {" +
        "  function bar() {" +
        "    var x = 1;" +
        "  }" +
        "  var x = 1;" +
        "  function baz() {" +
        "    var x = 1;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode);
    Node rootNode = compiler.getRootNode();

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique.ContextualRenamer contextualRenamer = renamer.new ContextualRenamer();
    MakeDeclaredNamesUnique.InlineRenamer inlineRenamer = renamer.new InlineRenamer(() -> "_", "_foo", true);
    NodeTraversal traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);

    // Traverse the entire tree with the ContextualRenamer first.
    traverser.addCallback(contextualRenamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("bar_0", rootNode.getLastChild().getFirstChild().getNameNode().getString());

    // Traverse the entire tree again with the InlineRenamer to make sure that the names are not modified by the previous traversal.
    inlineRenamer = renamer.new InlineRenamer(() -> "_", "_foo", true);
    traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);
    traverser.addCallback(inlineRenamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("1_foo_0", rootNode.getFirstChild().getLastChild().getString());
    assertEquals("_foo_1", rootNode.getLastChild().getFirstChild().getNameNode().getString());
    assertEquals("_foo_2", rootNode.getLastChild().getLastChild().getNameNode().getString());
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithGloballyDeclaredFunction() {
    String jsCode = "var foo = function() {" +
        "  var x = 1;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode);
    Node rootNode = compiler.getRootNode();

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("foo_0", rootNode.getNameNode().getString());
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithGloballyDeclaredVariable() {
    String jsCode = "var x = 1";

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.compile(ImmutableSet.of(), jsCode);
    Node rootNode = compiler.getRootNode();

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(rootNode, null, null);
    traverser.addCallback(renamer);
    NodeTraversal.traverse(compiler, rootNode, traverser);

    assertEquals("x", rootNode.getNameNode().getString());
  }
}