package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isVarDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.testType.MakeDeclaredNamesUniqueTestNode;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  private static class MakeDeclaredNamesUniqueTester implements NodeTraversal.ScopedCallback {

    private final MakeDeclaredNamesUnique makeDeclaredNamesUnique;
    private final Scope scope;
    private final Map<String, String> originalNames;
    private final Map<String, String> renamedNames;
    private final Node rootNode;

    public MakeDeclaredNamesUniqueTester(Node rootNode) {
      this.makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
      this.scope = new Scope();
      this.originalNames = ImmutableSet.of("a", "b", "c", "d").asMap();
      this.renamedNames = ImmutableSet.of().asMap();
      this.rootNode = rootNode;
    }

    public void test() {
      makeDeclaredNamesUnique.traverse(NodeTraversal.create(scope, rootNode, this));

      for (Map.Entry<String, String> entry : originalNames.entrySet()) {
        String originalName = entry.getKey();
        String newName = makeDeclaredNamesUnique.getReplacementName(originalName);
        String renamedName = renamedNames.get(originalName);

        assertEquals(
            "Unexpected renaming of " + originalName,
            newName, renamedName);
      }

      for (Node node : scope.getAllNodes()) {
        if (node.getType() == Token.NAME) {
          String name = node.getString();
          String renamedName = makeDeclaredNamesUnique.getReplacementName(name);
          if (renamedName != null) {
            assertEquals(
                "Unexpected renaming of local variable " + name,
                renamedName, name);
          }
        }
      }
    }

    @Override
    public void enterScope(NodeTraversal t) {
      makeDeclaredNamesUnique.enterScope(t);
    }

    @Override
    public void exitScope(NodeTraversal t) {
      makeDeclaredNamesUnique.exitScope(t);

      if (!t.inGlobalScope()) {
        Node node = t.getScopeRoot();
        String name = node.getFirstChild().getString();
        renamedNames.put(name, node.getString());
      }
    }

    @Override
    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
      return true;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.getType() == Token.NAME) {
        String originalName = originalNames.get(n.getString());
        String newName = makeDeclaredNamesUnique.getReplacementName(n.getString());

        if (originalName != null) {
          Preconditions.checkState(
              renamedNames.containsKey(originalName),
              "Unexpected missing renaming of " + originalName);
          renamedNames.put(n.getString(), originalName);
        } else if (newName != null) {
          assertEquals(
              "Unexpected renaming of local variable " + n.getString(),
              newName, n.getString());
        }
      }
    }
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique() {
    // Testing with a simple Node to ensure proper renaming of local variables.
    MakeDeclaredNamesUniqueTestNode rootNode = new MakeDeclaredNamesUniqueTestNode();

    MakeDeclaredNamesUniqueTester tester = new MakeDeclaredNamesUniqueTester(rootNode);
    tester.test();

    // Testing with a more complex Node to ensure proper renaming of function
    // and variable names.
    MakeDeclaredNamesUniqueTestNode complexRootNode = new MakeDeclaredNamesUniqueTestNodeComplex();

    MakeDeclaredNamesUniqueTester complexTester = new MakeDeclaredNamesUniqueTester(complexRootNode);
    complexTester.test();

    // Testing that the renaming does not happen for global variables.
    MakeDeclaredNamesUniqueTester globalTester = new MakeDeclaredNamesUniqueTester(null);
    globalTester.test();
  }
}