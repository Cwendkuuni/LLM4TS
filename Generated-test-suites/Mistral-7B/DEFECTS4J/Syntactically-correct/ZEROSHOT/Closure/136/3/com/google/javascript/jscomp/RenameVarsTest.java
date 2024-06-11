import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserUtil;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for RenameVars
 */
public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    String input = "function f(x, y) {" +
                   "  var a = 1, b = 2, c = 3, d = 4;" +
                   "  var e = function() { return a; }," +
                   "      g = function() { return b; }," +
                   "      h = function() { return c; }," +
                   "      i = function() { return d; }";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameVars renamer = new RenameVars(compiler, "", false, false, false, null, null, null);

    Node externs = ParserUtil.parse(input, options, null);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(compiler, root, externs);
    renamer.process(externs, root);

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("a", "j");
    expectedMap.put("b", "k");
    expectedMap.put("c", "l");
    expectedMap.put("d", "m");
    expectedMap.put("x", "n");
    expectedMap.put("y", "o");
    VariableMap expectedVariableMap = new VariableMap(expectedMap);

    Map<String, String> actualMap = renamer.getVariableMap();
    VariableMap actualVariableMap = (VariableMap) actualMap;

    assertEquals(expectedVariableMap, actualVariableMap);
  }

  @Test
  public void testRenameVarsWithPrefix() {
    String input = "function f(x, y) {" +
                   "  var a = 1, b = 2, c = 3, d = 4;" +
                   "  var e = function() { return a; }," +
                   "      g = function() { return b; }," +
                   "      h = function() { return c; }," +
                   "      i = function() { return d; }";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameVars renamer = new RenameVars(compiler, "prefix_", false, false, false, null, null, null);

    Node externs = ParserUtil.parse(input, options, null);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(compiler, root, externs);
    renamer.process(externs, root);

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("a", "prefix__j");
    expectedMap.put("b", "prefix__k");
    expectedMap.put("c", "prefix__l");
    expectedMap.put("d", "prefix__m");
    expectedMap.put("x", "prefix__n");
    expectedMap.put("y", "prefix__o");
    VariableMap expectedVariableMap = new VariableMap(expectedMap);

    Map<String, String> actualMap = renamer.getVariableMap();
    VariableMap actualVariableMap = (VariableMap) actualMap;

    assertEquals(expectedVariableMap, actualVariableMap);
  }

  @Test
  public void testRenameVarsWithLocalRenamingOnly() {
    String input = "function f(x, y) {" +
                   "  var a = 1," +
                   "      b = function() { return x; }," +
                   "      c = 1;" +
                   "  var d = 2," +
                   "      e = function() { return y; }";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameVars renamer = new RenameVars(compiler, "", true, false, false, null, null, null);

    Node externs = ParserUtil.parse(input, options, null);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(compiler, root, externs);
    renamer.process(externs, root);

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("a", "a0");
    expectedMap.put("b", "b0");
    expectedMap.put("c", "c0");
    expectedMap.put("d", "d0");
    expectedMap.put("e", "e0");
    expectedMap.put("x", "x");
    expectedMap.put("y", "y");
    VariableMap expectedVariableMap = new VariableMap(expectedMap);

    Map<String, String> actualMap = renamer.getVariableMap();
    VariableMap actualVariableMap = (VariableMap) actualMap;

    assertEquals(expectedVariableMap, actualVariableMap);
  }

  @Test
  public void testRenameVarsWithPreserveAnonymousFunctionNames() {
    String input = "function f() {" +
                   "  var a = 1;" +
                   "  var g = function() { return a; }";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameVars renamer = new RenameVars(compiler, "", false, true, false, null, null, null);

    Node externs = ParserUtil.parse(input, options, null);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(compiler, root, externs);
    renamer.process(externs, root);

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("a", "a");
    expectedMap.put("g", "g");
    VariableMap expectedVariableMap = new VariableMap(expectedMap);

    Map<String, String> actualMap = renamer.getVariableMap();
    VariableMap actualVariableMap = (VariableMap) actualMap;

    assertEquals(expectedVariableMap, actualVariableMap);
  }

  @Test
  public void testRenameVarsWithGeneratePseudoNames() {
    String input = "function f() {" +
                   "  var a = 1;" +
                   "  var g = function() { return a; }";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameVars renamer = new RenameVars(compiler, "", false, false, true, null, null, null);

    Node externs = ParserUtil.parse(input, options, null);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(compiler, root, externs);
    renamer.process(externs, root);

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("a", "$a$$");
    expectedMap.put("g", "$g$$");
    VariableMap expectedVariableMap = new VariableMap(expectedMap);

    Map<String, String> actualMap = renamer.getVariableMap();
    VariableMap actualVariableMap = (VariableMap) actualMap;

    assertEquals(expectedVariableMap, actualVariableMap);
  }

  @Test
  public void testRenameVarsWithPreserveAnonymousFunctionNamesAndGeneratePseudoNames() {
    String input = "function f() {" +
                   "  var a = 1;" +
                   "  var g = function() { return a; }";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameVars renamer = new RenameVars(compiler, "", false, true, true, null, null, null);

    Node externs = ParserUtil.parse(input, options, null);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(compiler, root, externs);
    renamer.process(externs, root);

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("a", "$a$$");
    expectedMap.put("g", "$g$$");
    VariableMap expectedVariableMap = new VariableMap(expectedMap);

    Map<String, String> actualMap = renamer.getVariableMap();
    VariableMap actualVariableMap = (VariableMap) actualMap;

    assertEquals(expectedVariableMap, actualVariableMap);
  }

  @Test
  public void testRenameVarsWithReservedNames() {
    String input = "function f() {" +
                   "  var a = 1;" +
                   "  var for = 2;" +
                   "  var var = 3";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    RenameVars renamer = new RenameVars(compiler, "", false, false, false, null, null, Collections.singletonList("for", "var"));

    Node externs = ParserUtil.parse(input, options, null);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(compiler, root, externs);
    renamer.process(externs, root);

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("a", "a");
    expectedMap.put("for", "for_0");
    expectedMap.put("var", "var_0");
    VariableMap expectedVariableMap = new VariableMap(expectedMap);

    Map<String, String> actualMap = renamer.getVariableMap();
    VariableMap actualVariableMap = (VariableMap) actualMap;

    assertEquals(expectedVariableMap, actualVariableMap);
  }

  @Test
  public void testRenameVarsWithReservedCharacters() {
    String input = "function f() {" +
                   "  var a = 1,$b = 2,c = 3,d = 4" +
                   "  var e = function() { return a; }";

    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    char[] reservedCharacters = { '$' };
    RenameVars renamer = new RenameVars(compiler, "", false, false, false, null, reservedCharacters, null);

    Node externs = ParserUtil.parse(input, options, null);
    Node root = Node.root();
    NodeTraversal traversal = new NodeTraversal(compiler, root, externs);
    renamer.process(externs, root);

    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("a", "a");
    expectedMap.put("b", "b0");
    expectedMap.put("c", "c");
    expectedMap.put("d", "d");
    expectedMap.put("e", "e");
    VariableMap expectedVariableMap = new VariableMap(expectedMap);

    Map<String, String> actualMap = renamer.getVariableMap();
    VariableMap actualVariableMap = (VariableMap) actualMap;

    assertEquals(expectedVariableMap, actualVariableMap);
  }
}