import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.SimpleSourceFile;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RenameVarsTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerOptions options;
  private RenameVars renameVars;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    options = new CompilerOptions();
    compiler = new Compiler(options);
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
  }

  @Test
  public void testRenameGlobalVariables() {
    // Test with no variables to rename
    String inputCode = "function test() {}";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    checkNoNameChanges(root);

    // Test with global variables to rename
    inputCode = "var a = 1, b = 2, c = 3; function test() {}";
    sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    List<String> oldNames = Arrays.asList("a", "b", "c");
    List<String> newNames = Arrays.asList("x", "y", "z");
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("a", "x");
    expectedRenameMap.put("b", "y");
    expectedRenameMap.put("c", "z");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameLocalVariables() {
    // Test with no local variables to rename
    String inputCode = "function test(a, b) { var c = a + b; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    checkNoNameChanges(root);

    // Test with local variables to rename
    inputCode = "function test(a, b) { var a = 1, b = 2, c = a + b; }";
    sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("a", "b");
    newNames = Arrays.asList("x", "y");
    expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("a", "x");
    expectedRenameMap.put("b", "y");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameExternVariables() {
    // Test with extern variables to rename
    String inputCode = "var a = 1; function test() { var b = a; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("a");
    newNames = Arrays.asList("x");
    expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("a", "x");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameAnonymousFunctionNames() {
    // Test with anonymous function names to preserve
    String inputCode = "function test() { var a = function() {}; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    checkNoNameChanges(root);
  }

  @Test
  public void testRenameReservedVariables() {
    // Test with reserved variables to rename
    String inputCode = "function test() { var throw = 1; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    Set<String> reservedNames = new HashSet<>(renameVars.reservedNames);
    reservedNames.remove("throw");
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, reservedNames);
    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("throw");
    newNames = Arrays.asList("x");
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("throw", "x");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameVariablesWithSameName() {
    // Test with variables having the same name
    String inputCode = "function test() { var a = 1; var a = 2; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("a");
    newNames = Arrays.asList("x", "y");
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("a", "x");
    expectedRenameMap.put("a", "y");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameVariablesWithLongNames() {
    // Test with variables having long names
    String inputCode = "function test() { var this_is_a_very_long_variable_name = 1; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("this_is_a_very_long_variable_name");
    newNames = Arrays.asList("z");
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("this_is_a_very_long_variable_name", "z");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameVariablesWithSameLengthButDifferentNames() {
    // Test with variables having the same length but different names
    String inputCode = "function test() { var a = 1, b = 2; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("a", "b");
    newNames = Arrays.asList("x", "y");
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("a", "x");
    expectedRenameMap.put("b", "y");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test(expected = IllegalStateException.class)
  public void testRenameVariablesWithExistingRenamedVariable() {
    // Test with variables having the same name as a previously renamed variable
    String inputCode = "function test() { var a = 1, a = 2; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("a");
    newNames = Arrays.asList("x", "x");
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("a", "x");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameVariablesWithStringLiterals() {
    // Test with string literals
    String inputCode = "function test() { var a = 'foo'; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("a");
    newNames = Arrays.asList("x");
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("a", "x");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameVariablesWithNestedFunctions() {
    // Test with nested functions
    String inputCode = "function test() { function inner() {} }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    checkNoNameChanges(root);
  }

  @Test
  public void testRenameVariablesWithLoopVariable() {
    // Test with loop variable
    String inputCode = "function test() { for (var i = 0; i < 10; i++) {} }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    oldNames = Arrays.asList("i");
    newNames = Arrays.asList("j");
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("i", "j");
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  @Test
  public void testRenameVariablesWithSwitchStatement() {
    // Test with switch statement
    String inputCode = "function test() { switch (1) { case 0: break; case 1: break; } }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("TestFile.js", inputCode);
    Node root = nodeFactory.parse(sourceFile, null, null);

    renameVars.process(compiler.getExterns(), root);
    checkNoNameChanges(root);
  }

  private void checkNoNameChanges(Node root) {
    Map<String, String> renameMap = renameVars.getVariableMap();
    List<String> oldNames = Arrays.asList();
    List<String> newNames = Arrays.asList();
    Map<String, String> expectedRenameMap = new HashMap<>();
    checkNameChanges(root, oldNames, newNames, expectedRenameMap);
  }

  private void checkNameChanges(Node root, List<String> oldNames, List<String> newNames, Map<String, String> expectedRenameMap) {
    Map<String, String> actualRenameMap = renameVars.getVariableMap();

    for (String oldName : oldNames) {
      String newName = actualRenameMap.get(oldName);
      assertEquals(newNames.get(oldNames.indexOf(oldName)), newName);
      assertEquals(expectedRenameMap.get(oldName), newName);
    }

    for (Map.Entry<String, String> entry : actualRenameMap.entrySet()) {
      String oldName = entry.getKey();
      if (!oldNames.contains(oldName)) {
        assertNull("Unexpectedly renamed variable: " + oldName, newName);
      }
    }
  }
}