import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RenameVarsTest {

  @ org.junit.Test
  public void testRenameVars() {
    // Test case 1: Rename local variables
    CompilerInput input = new CompilerInput("Test.java", 1);
    AbstractCompiler compiler = new Compiler();
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    Node root = NodeOperations.parseString("function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = function() {}" +
        "  var d = 3;" +
        "}");
    renameVars.process(null, root);
    Node renamedRoot = renameVars.getRenamedNodes(root);

    List<String> expectedRenamedLocals = Lists.newArrayList("a0", "b1", "c2", "d3");
    List<String> actualRenamedLocals = Lists.newArrayList();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    assertEquals(expectedRenamedLocals, actualRenamedLocals);

    // Test case 2: Rename global variables
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("var a = 1;var b = 2;");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(nodeToExternal(root), nodeToExternal(root));

    List<String> expectedRenamedGlobals = Lists.newArrayList("a0", "b1");
    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    assertEquals(expectedRenamedGlobals, actualRenamedLocals);

    // Test case 3: Rename global variables with prefix
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("var prefix_a = 1;var prefix_b = 2;");
    renameVars = new RenameVars(compiler, "prefix_", false, false, false, null, null, null);
    renameVars.process(nodeToExternal(root), nodeToExternal(root));

    expectedRenamedGlobals.clear();
    expectedRenamedGlobals.add("prefix_a0");
    expectedRenamedGlobals.add("prefix_b1");

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    assertEquals(expectedRenamedGlobals, actualRenamedLocals);

    // Test case 4: Rename local variables with conflict
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("function foo() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(null, root);

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    // The test for the local variable 'a' should have been renamed
    // to a different name as it was declared twice.
    String actualFirstA = actualRenamedLocals.get(0);
    assertNotEquals("a", actualFirstA);

    String actualSecondA = actualRenamedLocals.get(1);    // The second 'a' should still be 'a'
    assertEquals("a", actualSecondA);

    // Test case 5: Rename local variables with same length
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("function foo() {" +
        "  var a = 1;" +
        "  var b = 1;" +
        "  var c = 1;" +
        "  var d = 1;" +
        "}");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(null, root);

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    // The variables 'a', 'b', 'c', and 'd' should be renamed based on their order of occurrence.
    List<String> expectedRenamedLocals = Lists.newArrayList("a0", "b1", "c2", "d3");

    assertEquals(expectedRenamedLocals, actualRenamedLocals);

    // Test case 6: Rename global variables with same length
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("var a = 1;var b = 1;");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(nodeToExternal(root), nodeToExternal(root));

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    // The variables 'a' and 'b' should be renamed based on their order of declaration.
    expectedRenamedGlobals.clear();
    expectedRenamedGlobals.add("a0");
    expectedRenamedGlobals.add("b1");

    assertEquals(expectedRenamedGlobals, actualRenamedLocals);

    // Test case 7: Rename global and local variables
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("var a = 1;function foo() {" +
        "  var a = 2;" +
        "}");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(nodeToExternal(root), nodeToExternal(root));

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    // The global variable 'a' should have a different name than the local one.
    expectedRenamedLocals.clear();
    expectedRenamedLocals.add("a0"); // Global 'a'
    expectedRenamedLocals.add("a1"); // Local 'a'

    assertEquals(expectedRenamedLocals, actualRenamedLocals);

    // Test case 8: Rename local variables with same name as global variables
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("var a = 1;function foo() {" +
        "  var a = 2;" +
        "}");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(null, root);

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    // The local variable 'a' should have a different name than the global one.
    expectedRenamedLocals.clear();
    expectedRenamedLocals.add("a0"); // Global 'a'
    expectedRenamedLocals.add("a1"); // Local 'a'

    assertEquals(expectedRenamedLocals, actualRenamedLocals);

    // Test case 9: Rename global and local variables with same name as each other
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("var a = 1;function foo() {" +
        "  var a = 2;" +
        "}");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(nodeToExternal(root), nodeToExternal(root));

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    // The local and global variables 'a' should have different names.
    expectedRenamedLocals.clear();
    expectedRenamedLocals.add("a0"); // Global 'a'
    expectedRenamedLocals.add("a1"); // Local 'a'

    assertEquals(expectedRenamedLocals, actualRenamedLocals);

    // Test case 10: Preserve anonymous functions names
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("function foo() {" +
        "  var a = function() {}" +
        "}");
    renameVars = new RenameVars(compiler, "", false, true, false, null, null, null);
    renameVars.process(null, root);

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    List<String> anonymousFunctionNames = Lists.newArrayList();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.FUNCTION)) {
      if (!NodeUtil.isAnonymousFunction(n)) {
        continue;
      }

      anonymousFunctionNames.add(n.getString());
    }

    // The anonymous function should have the same name as in the original code.
    assertEquals(1, anonymousFunctionNames.size());
    assertEquals("function () {}", anonymousFunctionNames.get(0));

    // Test case 11: Rename variables with reserved words
    compiler.reset();
    input = new CompilerInput("Test.java", 1);
    root = NodeOperations.parseString("var break = 1;var continue = 2;");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(nodeToExternal(root), nodeToExternal(root));

    actualRenamedLocals.clear();

    for (Node n : NodeTraversal.collectDescendantsOfTypes(renamedRoot, NodeType.VAR)) {
      assertNotNull(n.getName());
      actualRenamedLocals.add(n.getString());
    }

    List<String> reservedVariables = Lists.newArrayList();

    for (String reservedWord : Arrays.asList("break", "continue")) {
      // The reserved variables should be renamed.
      reservedVariables.add(renameVars.renameMap.get(reservedWord));
    }

    assertEquals(2, reservedVariables.size());
    assertTrue(Sets.difference(Sets.newHashSet(reservedVariables),
        Sets.newHashSet("break", "continue")).isEmpty());
  }

  // Helper methods

  /**
   * Converts a given node to an external node.
   */
  private static Node nodeToExternal(Node node) {
    if (node == null) {
      return null;
    }

    if (node.isThis()) {
      return NodeOperations.parseString("this");
    } else if (node.isString()) {
      return NodeOperations.parseString(node.getString());
    } else if (node.isNumber()) {
      return NodeOperations.parseNumber(node.getNumber().toString());
    } else if (node.isIdentifier()) {
      return NodeOperations.parseString(node.getString());
    } else if (node.isObjectLiteral()) {
      Map<String, Node> properties = Maps.newHashMap();
      for (Node property : node.getChildren()) {
        properties.put(property.getString(), property);
      }
      return new ObjectLiteral(NodeOperations.parseString("{}"), properties);
    } else if (node.isArrayLiteral()) {
      List<Node> elements = Lists.newArrayList();
      for (Node element : node.getChildren()) {
        elements.add(element);
      }
      return new ArrayLiteral(NodeOperations.parseString("[]"), elements);
    } else if (node.isRegExpLiteral()) {
      String pattern = node.getString();
      String flags = node.getChildren().get(1).getString();
      return NodeOperations.parseString("/" + pattern + "/" + flags);
    } else if (node.isConditionalExpression()) {
      Node condition = node.getChildren().get(0);
      Node consequent = node.getChildren().get(1);
      Node alternative = node.getChildren().get(2);
      return new ConditionalExpression(condition, consequent, alternative);
    } else if (node.isExpressionStatement()) {
      Node expression = node.getChildren().get(0);
      return NodeOperations.parseString(";")
          .addChild(expression);
    } else if (node.isStatement()) {
      return NodeOperations.parseString(";");
    } else if (node.isExpression()) {
      return NodeOperations.parseString("null");
    } else {
      throw new RuntimeException("Unsupported node type: " + node.getClass());
    }
  }
}