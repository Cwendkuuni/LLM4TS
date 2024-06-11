import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RenameVarsTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private RenameVars renamer;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    options = new CompilerOptions();
    renamer = new RenameVars(compiler, "", false, false, false, null, null, null);
  }

  @Test
  public void testProcess_emptySource() {
    Node root = Node.createTree();
    renamer.process(Node.rootNode, root);
    assertEquals(0, renamer.getVariableMap().size());
  }

  @Test
  public void testProcess_noRename() {
    String source = "function foo() {}";
    Node root = Node.createTreeFromString(source);

    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();
    assertEquals("foo", map.get("foo"));
  }

  @Test
  public void testProcess_renameLocalVariables() {
    String source = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var f = 1;\n" +
        "}";
    Node root = Node.createTreeFromString(source);

    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    // Check local variable names were renamed
    assertEquals("L2", map.get("c"));
    assertEquals("f", map.get("f"));

    // Check global variable names were not renamed
    assertEquals("a", map.get("a"));
    assertEquals("b", map.get("b"));
  }

  @Test
  public void testProcess_renameExternals() {
    String source = "var a = 1;\n" +
        "function foo(b) {}\n";
    Node root = Node.createTreeFromString(source);

    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    // Check local variable names were not renamed
    assertEquals("b", map.get("b"));

    // Check global variable names were renamed
    assertEquals("a", map.get("a"));
  }

  @Test
  public void testProcess_renameAnonymousFunction() {
    String source = "function foo() {\n" +
        "  var a = function() {};\n" +
        "}";
    Node root = Node.createTreeFromString(source);

    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    // Check anonymous function name was not renamed
    assertEquals("a", map.get("a"));
  }

  @Test
  public void testProcess_renameWithExternals() {
    String source = "var a = 1;\n" +
        "function foo(b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var f = 1;\n" +
        "}";
    Node root = Node.createTreeFromString(source);

    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    // Check local variable names were renamed
    assertEquals("L2", map.get("c"));
    assertEquals("f", map.get("f"));

    // Check global variable names were not renamed
    assertEquals("a", map.get("a"));
    assertEquals("b", map.get("b"));
  }

  @Test
  public void testProcess_renameWithPrefix() {
    String source = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var f = 1;\n" +
        "}";
    Node root = Node.createTreeFromString(source);

    renamer = new RenameVars(compiler, "prefix_", false, false, false, null, null, null);
    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    // Check local variable names were renamed
    assertEquals("prefix_L2", map.get("c"));
    assertEquals("f", map.get("f"));

    // Check global variable names were not renamed
    assertEquals("a", map.get("a"));
    assertEquals("b", map.get("b"));
  }

  @Test
  public void testProcess_renameWithMultipleGlobalVariables() {
    String source = "var a = 1;\n" +
        "var b = 2;\n" +
        "function foo(c) {}\n";
    Node root = Node.createTreeFromString(source);

    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    List<String> expectedNames = Arrays.asList("a", "b");
    assertEquals(expectedNames.size(), map.size());

    List<String> actualNames = new ArrayList<>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      actualNames.add(entry.getKey());
    }

    Collections.sort(expectedNames);
    Collections.sort(actualNames);

    assertEquals(expectedNames, actualNames);
  }

  @Test
  public void testProcess_renameWithMultipleLocalVariables() {
    String source = "function foo(a) {\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}";
    Node root = Node.createTreeFromString(source);

    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    List<String> expectedNames = Arrays.asList("L0", "L1");
    assertEquals(expectedNames.size(), map.size());

    List<String> actualNames = new ArrayList<>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      actualNames.add(entry.getKey());
    }

    Collections.sort(expectedNames);
    Collections.sort(actualNames);

    assertEquals(expectedNames, actualNames);
  }

  @Test
  public void testProcess_renameWithSameLengthNames() {
    String source = "function foo(a) {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "}";
    Node root = Node.createTreeFromString(source);

    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    List<String> expectedNames = Arrays.asList("L0", "L1");
    assertEquals(expectedNames.size(), map.size());

    List<String> actualNames = new ArrayList<>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      actualNames.add(entry.getKey());
    }

    Collections.sort(expectedNames);
    Collections.sort(actualNames);

    assertEquals(expectedNames, actualNames);
  }

  @Test
  public void testProcess_renameWithReservedNames() {
    String source = "function foo() {\n" +
        "  var if = function() {};\n" +
        "}";
    Node root = Node.createTreeFromString(source);

    Set<String> reservedNames = new HashSet<>();
    renamer = new RenameVars(compiler, "", false, false, false, null, null, reservedNames);
    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    assertEquals("if", map.get("if"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_nullCompiler() {
    renamer = new RenameVars(null, "", false, false, false, null, null, null);
  }

  @Test
  public void testProcess_withPrevUsedRenameMap() {
    String source = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var f = 1;\n" +
        "}";
    Node root = Node.createTreeFromString(source);

    CompilerInput input = new CompilerInput(null, "test.js", 0, 0, null, null);
    Scope.Context context = new Scope.Context(input, null, null, null);
    Scope scope = new Scope(context);
    scope.setVar("c", input, null, null, null, null, null, null, 0, Node.NAME, null, null, null, null, null, null, null, null, null, null);

    VariableMap prevUsedRenameMap = new VariableMap(Collections.singletonMap("c", "CC"));

    renamer = new RenameVars(compiler, "", false, false, false, prevUsedRenameMap, null, null);
    renamer.process(Node.rootNode, root);
    Map<String, String> map = renamer.getVariableMap();

    assertEquals("CC", map.get("c"));
    assertEquals("a", map.get("a"));
    assertEquals("b", map.get("b"));
    assertEquals("f", map.get("f"));
  }
}