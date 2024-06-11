import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    // Create mock AbstractCompiler
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    // Create RenameVars instance
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(compiler, prefix, false, false, false,
        null, null, null);

    // Test empty input
    Node externs = Node.NULL();
    Node root = Node.NULL();
    renameVars.process(externs, root);
    assertEquals(Collections.emptyMap(), renameVars.getVariableMap());

    // Test simple local variables
    externs = createNode("var l0 = 1;", null);
    root = createNode("function f() { var l1 = 2; }", null);
    renameVars.process(externs, root);
    Map<String, String> variableMap = renameVars.getVariableMap();
    assertEquals("l0", variableMap.get("l1"));
    assertEquals("l1", variableMap.get("l0"));

    // Test local variables with the same name
    externs = Node.NULL();
    root = createNode("function f() { var l0 = 1; var l0 = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("l0_1", variableMap.get("l0_1"));
    assertEquals("l0_2", variableMap.get("l0_2"));

    // Test global variables
    externs = createNode("var g0 = 1;", null);
    root = createNode("function f() { g0 = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("g0", variableMap.get("g0"));

    // Test extern variables
    externs = createNode("var g0 = 1;", null);
    root = Node.NULL();
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("g0", variableMap.get("g0"));

    // Test global variables with the same name
    externs = createNode("var g0 = 1;", null);
    root = createNode("var g0 = 2;", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("g0_1", variableMap.get("g0_1"));
    assertEquals("g0_2", variableMap.get("g0_2"));

    // Test anonymous functions
    externs = Node.NULL();
    root = createNode("function f() { (function() { var l0 = 1; })(); }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("l0", variableMap.get("l0"));

    // Test extern and local variables with the same name
    externs = createNode("var g0 = 1;", null);
    root = createNode("function f() { var l0 = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("g0", variableMap.get("g0"));
    assertEquals("l0_1", variableMap.get("l0_1"));

    // Test variables with prefixes
    externs = Node.NULL();
    root = createNode("function f() { var l0_prefix_ = 1; }", null);
    renameVars = new RenameVars(compiler, prefix, false, false, false,
        null, null, Sets.newHashSet("prefix_"));
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("l0_prefix_", variableMap.get("l0_prefix_"));

    // Test local variables with the same prefix
    externs = Node.NULL();
    root = createNode("function f() { var l0_prefix_ = 1; var l1_prefix_ = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("l0_prefix_", variableMap.get("l0_prefix_"));
    assertEquals("l1_prefix_", variableMap.get("l1_prefix_"));

    // Test global variables with the same prefix
    externs = createNode("var g0_prefix_ = 1;", null);
    root = Node.NULL();
    renameVars = new RenameVars(compiler, prefix, false, false, false,
        null, Sets.newHashSet("prefix_"), null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("g0_prefix_", variableMap.get("g0_prefix_"));

    // Test global and local variables with the same prefix
    externs = createNode("var g0_prefix_ = 1;", null);
    root = createNode("function f() { var l0_prefix_ = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("g0_prefix_", variableMap.get("g0_prefix_"));
    assertEquals("l0_prefix_", variableMap.get("l0_prefix_"));

    // Test variables with the same name and prefix
    externs = createNode("var g0_prefix_ = 1;", null);
    root = createNode("function f() { var l0_prefix_ = 2; var l0_prefix_ = 3; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("g0_prefix_", variableMap.get("g0_prefix_"));
    assertEquals("l0_prefix__1", variableMap.get("l0_prefix_"));
    assertEquals("l0_prefix__2", variableMap.get("l0_prefix_"));

    // Test variables with the same name, same prefix, and same order of occurrence
    externs = Node.NULL();
    root = createNode("function f() { var l0_prefix_ = 2; var l0_prefix_ = 1; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("l0_prefix_", variableMap.get("l0_prefix_"));
    assertEquals("l0_prefix_", variableMap.get("l0_prefix_"));

    // Test variables with the same name, different prefixes
    externs = createNode("var g0_prefix1_ = 1;", null);
    root = createNode("function f() { var l0_prefix2_ = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("g0_prefix1_", variableMap.get("g0_prefix1_"));
    assertEquals("l0_prefix2_", variableMap.get("l0_prefix2_"));

    // Test variables with the same name, same prefix, and different order of occurrence
    externs = Node.NULL();
    root = createNode("function f() { var l0_prefix_ = 2; var l0_prefix_ = 1; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("l0_prefix_", variableMap.get("l0_prefix_"));
    assertEquals("l0_prefix_", variableMap.get("l0_prefix_"));

    // Test reserved names
    externs = Node.NULL();
    root = createNode("function f() { var for = 1; }", null);
    renameVars = new RenameVars(compiler, prefix, false, false, false,
        null, null, Sets.newHashSet("for"));
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("for", variableMap.get("for"));

    // Test variables with reserved characters
    externs = Node.NULL();
    root = createNode("function f() { var x$y = 1; }", null);
    renameVars = new RenameVars(compiler, prefix, false, false, false,
        null, new char[]{'$'}, null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("x$y", variableMap.get("x$y"));

    // Test variables with the same name and reserved characters
    externs = Node.NULL();
    root = createNode("function f() { var x$y = 1; var x$z = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("x$y", variableMap.get("x$y"));
    assertEquals("x$z", variableMap.get("x$z"));

    // Test variables with the same name, same prefix, and reserved characters
    externs = Node.NULL();
    root = createNode("function f() { var x$prefix_ = 1; var x$y = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("x$prefix_", variableMap.get("x$prefix_"));
    assertEquals("x$y", variableMap.get("x$y"));

    // Test variables with the same name, same prefix, and same order of occurrence,
    // and reserved characters
    externs = Node.NULL();
    root = createNode("function f() { var x$y = 2; var x$y = 1; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("x$y", variableMap.get("x$y"));
    assertEquals("x$y", variableMap.get("x$y"));

    // Test variables with the same name, different prefixes, and reserved characters
    externs = Node.NULL();
    root = createNode("function f() { var x$prefix1_ = 2; var x$prefix2_ = 1; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("x$prefix1_", variableMap.get("x$prefix1_"));
    assertEquals("x$prefix2_", variableMap.get("x$prefix2_"));

    // Test variables with the same name, same prefix, and same order of occurrence,
    // and reserved characters, different from the previous instance of the test
    externs = Node.NULL();
    root = createNode("function f() { var x$y = 1; var x$z = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("x$y", variableMap.get("x$y"));
    assertEquals("x$z", variableMap.get("x$z"));

    // Test variables with the same name and prefix, different order of occurrence,
    // and reserved characters
    externs = Node.NULL();
    root = createNode("function f() { var x$prefix_ = 2; var x$y = 1; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("x$prefix_", variableMap.get("x$prefix_"));
    assertEquals("x$y", variableMap.get("x$y"));

    // Test variables with the same name and prefix, same order of occurrence,
    // and reserved characters, different from the previous instance of the test
    externs = Node.NULL();
    root = createNode("function f() { var x$y = 1; var x$prefix_ = 2; }", null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("x$prefix_", variableMap.get("x$prefix_"));
    assertEquals("x$y", variableMap.get("x$y"));

    // Test reusing previously used variable map
    externs = Node.NULL();
    root = createNode("function f() { var l0 = 1; var l1 = 1; }", null);
    RenameVars prevRenameVars = new RenameVars(compiler, prefix, false, false, false,
        null, null, null);
    prevRenameVars.process(externs, root);
    Map<String, String> prevVariableMap = prevRenameVars.getVariableMap();
    renameVars.prevUsedRenameMap = prevVariableMap;
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("l1", variableMap.get("l0"));
    assertEquals("l0", variableMap.get("l1"));

    // Test reusing previously used variable map with different prefixes
    externs = Node.NULL();
    root = createNode("function f1() { var l0 = 1; }", null);
    prevRenameVars = new RenameVars(compiler, "prefix1_", false, false, false,
        null, null, null);
    prevRenameVars.process(externs, root);
    prevVariableMap = prevRenameVars.getVariableMap();
    renameVars.prevUsedRenameMap = prevVariableMap;
    renameVars = new RenameVars(compiler, "prefix2_", false, false, false,
        null, null, null);
    renameVars.process(externs, root);
    variableMap = renameVars.getVariableMap();
    assertEquals("l0", variableMap.get("l0"));
  }

  private Node createNode(String code, CompilerInput input) {
    Node node = Node.parse(code);
    if (input != null) {
      NodeTraversal traverser = NodeTraversal.createTraverser(compiler);
      traverser.traverse(node);
      traverser.setCurrentNode(node);
      traverser.getScope().setInput(input);
    }
    return node;
  }
}