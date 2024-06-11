import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect. Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.TestSourceFile;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique_contextualRenamer() {
    String sourceCode = ""
        + "function foo(x) {\n"
        + "  var x = 1;\n"
        + "  var y = 2;\n"
        + "  var z = x;\n"
        + "  function bar(z) {\n"
        + "    x = 3;\n"
        + "  }\n"
        + "}\n";

    TestSourceFile sourceFile = new TestSourceFile(sourceCode);
    Node ast = sourceFile.getAst();

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(ast);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    Scope globalScope = nodeTraversal.getRootScope();
    Map<String, Node> originalVars = getOriginalVars(globalScope);
    Map<String, String> renamedVars = getRenamedVars(globalScope);

    Map<String, String> expectedRenamedVars = ImmutableMap.<String, String>builder()
        .put("x", "x_1")
        .put("y", "y")
        .put("z", "z_1")
        .build();
    assertEquals(expectedRenamedVars, renamedVars);
    assertEquals(3, originalVars.size());
    assertEquals(3, renamedVars.size());

    List<String> originalNames = getOriginalNames(originalVars.keySet());
    List<String> renamedNames = getRenamedNames(renamedVars.keySet());
    assertEquals(originalNames, renamedNames);
  }

  @Test
  public void testMakeDeclaredNamesUnique_inlineRenamer() {
    String sourceCode = ""
        + "function foo(x) {\n"
        + "  var x = 1;\n"
        + "  var y = 2;\n"
        + "  var z = x;\n"
        + "  function bar(z) {\n"
        + "    x = 3;\n"
        + "  }\n"
        + "}\n";

    TestSourceFile sourceFile = new TestSourceFile(sourceCode);
    Node ast = sourceFile.getAst();

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
        new InlineRenamer(() -> "i_", "", false));
    NodeTraversal nodeTraversal = new NodeTraversal(ast);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    Scope globalScope = nodeTraversal.getRootScope();
    Map<String, Node> originalVars = getOriginalVars(globalScope);
    Map<String, String> renamedVars = getRenamedVars(globalScope);

    Map<String, String> expectedRenamedVars = ImmutableMap.<String, String>builder()
        .put("x", "x_i_0")
        .put("y", "y")
        .put("z", "z_i_0")
        .build();
    assertEquals(expectedRenamedVars, renamedVars);
    assertEquals(3, originalVars.size());
    assertEquals(3, renamedVars.size());

    List<String> originalNames = getOriginalNames(originalVars.keySet());
    List<String> renamedNames = getRenamedNames(renamedVars.keySet());
    assertEquals(originalNames, renamedNames);
  }

  @Test
  public void testMakeDeclaredNamesUnique_contextualRenamer_global() {
    String sourceCode = ""
        + "var x = 1;\n"
        + "var y = 2;\n"
        + "function foo() {\n"
        + "  var x = 3;\n"
        + "  var z = x;\n"
        + "}\n";

    TestSourceFile sourceFile = new TestSourceFile(sourceCode);
    Node ast = sourceFile.getAst();

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(ast);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    Scope globalScope = nodeTraversal.getRootScope();
    Map<String, Node> originalVars = getOriginalVars(globalScope);
    Map<String, String> renamedVars = getRenamedVars(globalScope);

    Map<String, String> expectedRenamedVars = ImmutableMap.<String, String>builder()
        .put("x", "x_1")
        .put("y", "y")
        .build();
    assertEquals(expectedRenamedVars, renamedVars);
    assertEquals(2, originalVars.size());
    assertEquals(2, renamedVars.size());

    List<String> originalNames = getOriginalNames(originalVars.keySet());
    List<String> renamedNames = getRenamedNames(renamedVars.keySet());
    assertEquals(originalNames, renamedNames);
  }

  @Test
  public void testMakeDeclaredNamesUnique_inlineRenamer_global() {
    String sourceCode = ""
        + "var x = 1;\n"
        + "var y = 2;\n"
        + "function foo() {\n"
        + "  var x = 3;\n"
        + "  var z = x;\n"
        + "}\n";

    TestSourceFile sourceFile = new TestSourceFile(sourceCode);
    Node ast = sourceFile.getAst();

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
        new InlineRenamer(() -> "", "", false));
    NodeTraversal nodeTraversal = new NodeTraversal(ast);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    Scope globalScope = nodeTraversal.getRootScope();
    Map<String, Node> originalVars = getOriginalVars(globalScope);
    Map<String, String> renamedVars = getRenamedVars(globalScope);

    Map<String, String> expectedRenamedVars = ImmutableMap.<String, String>builder()
        .put("x", "x_0")
        .put("y", "y")
        .build();
    assertEquals(expectedRenamedVars, renamedVars);
    assertEquals(2, originalVars.size());
    assertEquals(2, renamedVars.size());

    List<String> originalNames = getOriginalNames(originalVars.keySet());
    List<String> renamedNames = getRenamedNames(renamedVars.keySet());
    assertEquals(originalNames, renamedNames);
  }

  @Test
  public void testMakeDeclaredNamesUnique_contextualRenamer_function() {
    String sourceCode = ""
        + "function foo() {\n"
        + "  var x = 1;\n"
        + "  function bar() {\n"
        + "    var x = 2;\n"
        + "  }\n"
        + "}\n";

    TestSourceFile sourceFile = new TestSourceFile(sourceCode);
    Node ast = sourceFile.getAst();

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(ast);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    Scope functionScope = nodeTraversal.getCurrentScope();
    Map<String, Node> originalVars = getOriginalVars(functionScope);
    Map<String, String> renamedVars = getRenamedVars(functionScope);

    Map<String, String> expectedRenamedVars = ImmutableMap.<String, String>builder()
        .put("x", "x_1")
        .build();
    assertEquals(expectedRenamedVars, renamedVars);
    assertEquals(1, originalVars.size());
    assertEquals(1, renamedVars.size());

    List<String> originalNames = getOriginalNames(originalVars.keySet());
    List<String> renamedNames = getRenamedNames(renamedVars.keySet());
    assertEquals(originalNames, renamedNames);
  }

  @Test
  public void testMakeDeclaredNamesUnique_inlineRenamer_function() {
    String sourceCode = ""
        + "function foo() {\n"
        + "  var x = 1;\n"
        + "  function bar() {\n"
        + "    var x = 2;\n"
        + "  }\n"
        + "}\n";

    TestSourceFile sourceFile = new TestSourceFile(sourceCode);
    Node ast = sourceFile.getAst();

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
        new InlineRenamer(() -> "", "", false));
    NodeTraversal nodeTraversal = new NodeTraversal(ast);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    Scope functionScope = nodeTraversal.getCurrentScope();
    Map<String, Node> originalVars = getOriginalVars(functionScope);
    Map<String, String> renamedVars = getRenamedVars(functionScope);

    Map<String, String> expectedRenamedVars = ImmutableMap.<String, String>builder()
        .put("x", "x_0")
        .build();
    assertEquals(expectedRenamedVars, renamedVars);
    assertEquals(1, originalVars.size());
    assertEquals(1, renamedVars.size());

    List<String> originalNames = getOriginalNames(originalVars.keySet());
    List<String> renamedNames = getRenamedNames(renamedVars.keySet());
    assertEquals(originalNames, renamedNames);
  }

  @Test
  public void testMakeDeclaredNamesUnique_noRenaming() {
    String sourceCode = ""
        + "function foo() {\n"
        + "  var x = 1;\n"
        + "  var y = 2;\n"
        + "  function bar() {\n"
        + "    var x = 3;\n"
        + "  }\n"
        + "}\n";

    TestSourceFile sourceFile = new TestSourceFile(sourceCode);
    Node ast = sourceFile.getAst();

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(ast);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    Scope functionScope = nodeTraversal.getCurrentScope();
    Map<String, Node> originalVars = getOriginalVars(functionScope);
    Map<String, String> renamedVars = getRenamedVars(functionScope);

    List<String> originalNames = getOriginalNames(originalVars.keySet());
    List<String> renamedNames = getRenamedNames(renamedVars.keySet());
    assertTrue(originalNames.equals(renamedNames));
  }

  private Map<String, Node> getOriginalVars(Scope scope) {
    Map<String, Node> result = Maps.newHashMap();
    for (Var var : scope.getVars()) {
      result.put(var.getName(), var.getNameNode());
    }
    return result;
  }

  private Map<String, String> getRenamedVars(Scope scope) {
    Map<String, String> result = Maps.newHashMap();
    for (Var var : scope.getVars()) {
      String name = var.getName();
      String newName = var.getNameNode().getString();
      result.put(name, newName);
    }
    return result;
  }

  private List<String> getOriginalNames(Iterable<String> names) {
    return Lists.newArrayList(names);
  }

  private List<String> getRenamedNames(Iterable<String> names) {
    return Collections.list(names);
  }

}