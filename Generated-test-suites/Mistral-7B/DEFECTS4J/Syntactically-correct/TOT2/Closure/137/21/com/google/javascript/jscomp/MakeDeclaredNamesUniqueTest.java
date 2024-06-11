import static com.google.javascript.jscomp.MakeDeclaredNamesUnique.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    String sourceCode = "function foo(a, b) {\n" +
        "  var x = a;\n" +
        "  var y = b;\n" +
        "  var x = 10;\n" +
        "  var y = function bar() {}\n" +
        "  var x = [1, 2, 3];\n" +
        "}\n";

    Node rootNode = new TokenStream(sourceCode).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.visit(null, rootNode, null);

    List<String> expectedRenamedVariables = Lists.newArrayList(
        "a_1", "b_1", "x_2", "y_3", "x_4", "y_5");

    verifyRenamedVariables(rootNode, expectedRenamedVariables);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionInjection() {
    String sourceCode = "function foo() {\n" +
        "  function bar() {}\n" +
        "  function baz() {}\n" +
        "}\n";

    Node rootNode = new TokenStream(sourceCode).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
        new InlineRenamer(
            () -> "_injected_" + new Integer(1).toString(),
            "injected",
            true));
    makeDeclaredNamesUnique.visit(null, rootNode, null);

    List<String> expectedRenamedFunctions = Lists.newArrayList(
        "bar_injected_1", "baz_injected_2");

    verifyRenamedFunctions(rootNode, expectedRenamedFunctions);
  }

  @Test
  public void testMakeDeclaredNamesUnique_globalScope() {
    String sourceCode = "var a = 1;\n" +
        "function foo() {\n" +
        "  var a = 2;\n" +
        "}\n";

    Node rootNode = new TokenStream(sourceCode).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.visit(null, rootNode, null);

    List<String> expectedRenamedVariables = Lists.newArrayList(
        "a_1", "a_2");

    verifyRenamedVariables(rootNode, expectedRenamedVariables);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUnique_nullNode() {
    Node rootNode = null;
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.visit(null, rootNode, null);
  }

  @Test
  public void testMakeDeclaredNamesUnique_cycle() {
    String sourceCode = "function foo() {\n" +
        "  function bar() {\n" +
        "    var x = 1;\n" +
        "    x = foo();\n" +
        "  }\n" +
        "  bar();\n" +
        "}\n";

    Node rootNode = new TokenStream(sourceCode).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    try {
      makeDeclaredNamesUnique.visit(null, rootNode, null);
      fail("Expected an IllegalArgumentException due to a cycle in the code.");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  private void verifyRenamedVariables(Node rootNode, List<String> expectedRenamedVariables) {
    Iterator<Node> variableIt =
        new NodeTraversal(new ScopeCreator(new BaseScope()))
            .traverse(rootNode)
            .findAll(NodeUtil::isVarDeclaration)
            .iterator();

    int index = 0;
    while (variableIt.hasNext()) {
      Node variableNode = variableIt.next();
      String actualRenamedVariable = variableNode.getFirstChild().getString();
      assertEquals("Expected variable '" + variableNode.getFirstChild().getString() +
          "' to be renamed to '" + expectedRenamedVariables.get(index) + "'.",
          expectedRenamedVariables.get(index), actualRenamedVariable);

      index++;
    }

    assertEquals("Unexpected number of variables.", expectedRenamedVariables.size(),
        index);
  }

  private void verifyRenamedFunctions(Node rootNode, List<String> expectedRenamedFunctions) {
    Iterator<Node> functionIt =
        new NodeTraversal(new ScopeCreator(new BaseScope()))
            .traverse(rootNode)
            .findAll(NodeUtil::isFunctionDeclaration)
            .iterator();

    int index = 0;
    while (functionIt.hasNext()) {
      Node functionNode = functionIt.next();
      String actualRenamedFunction = functionNode.getFirstChild().getString();
      assertEquals("Expected function '" + functionNode.getFirstChild().getString() +
          "' to be renamed to '" + expectedRenamedFunctions.get(index) + "'.",
          expectedRenamedFunctions.get(index), actualRenamedFunction);

      index++;
    }

    assertEquals("Unexpected number of functions.", expectedRenamedFunctions.size(),
        index);
  }

}