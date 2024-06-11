import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler.CodingConvention;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  @org.junit.Test
  public void testRenameVars() {
    String code = "function foo(a, b) {\n" +
                  "  var c = a + b;\n" +
                  "  var d = 5;\n" +
                  "  return c * d;\n" +
                  "}";

    Compiler compiler = new Compiler();
    CompilerInput input = new CompilerInput("test.js", code, null);
    CodingConvention codingConvention = new CodingConvention();

    // Create RenameVars instance
    String prefix = "";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    char[] reservedCharacters = null;
    Set<String> reservedNames = new HashSet<>();

    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    Node externs = null;
    Node root = NodeUtil.parseTree(code, null);

    renameVars.process(externs, root);

    // Check the renamed variables
    List<Node> localNameNodes = renameVars.localNameNodes;
    List<String> expectedLocalNames = Arrays.asList("L0", "L1", "L2");

    for (int i = 0; i < localNameNodes.size(); i++) {
      String actualLocalName = localNameNodes.get(i).getString();
      assertEquals(expectedLocalNames.get(i), actualLocalName);
    }

    // Check the renamed variables without prefix
    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<String> expectedGlobalNames = Arrays.asList("c", "d");

    for (Node node : globalNameNodes) {
      String actualGlobalName = node.getString();
      assertEquals(expectedGlobalNames.get(globalNameNodes.indexOf(node)),
          actualGlobalName);
    }

    // Test with prefix
    String prefixValue = "TEST_";
    renameVars = new RenameVars(compiler, prefixValue, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    renameVars.process(externs, root);

    // Check the renamed variables with prefix
    for (Node node : globalNameNodes) {
      String actualGlobalName = node.getString();
      assertEquals(prefixValue + expectedGlobalNames.get(globalNameNodes.indexOf(node)),
          actualGlobalName);
    }

    // Test with reserved characters
    Set<String> reservedNamesWithReservedChars = new HashSet<>();
    reservedNamesWithReservedChars.addAll(reservedNames);
    reservedNamesWithReservedChars.add("$a");

    renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        new char[]{'$'}, reservedNamesWithReservedChars);

    renameVars.process(externs, root);

    Node dollarASignNode = findNodeWithName("$a", root);
    assertNull(dollarASignNode);

    // Test with custom coding convention
    codingConvention = new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return name.equals("exportedName");
      }
    };

    renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);
    renameVars.compiler.setCodingConvention(codingConvention);

    root.setString("function exportedName(a, b) { return a + b; }");
    renameVars.process(externs, root);

    List<Node> exportedNameNodes = findNodesWithName("exportedName", root);
    assertNotNull(exportedNameNodes);
    assertEquals(1, exportedNameNodes.size());
    String actualExportedName = exportedNameNodes.get(0).getString();
    assertNotEquals("exportedName", actualExportedName);

    // Test with error
    String errorCode = "function foo() {\n" +
                      "  var x = y;\n" +
                      "}";

    input = new CompilerInput("test.js", errorCode, null);
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    try {
      renameVars.process(externs, root);
      fail("Expected CompilerException");
    } catch (CompilerException e) {
      // Expected
    }
  }

  private Node findNodeWithName(String name, Node root) {
    NodeTraversal nodeTraversal = new NodeTraversal(root);

    for (Node node : nodeTraversal) {
      if (node.getType() == Token.NAME && node.getString().equals(name)) {
        return node;
      }
    }

    return null;
  }

  private List<Node> findNodesWithName(String name, Node root) {
    NodeTraversal nodeTraversal = new NodeTraversal(root);

    List<Node> result = new ArrayList<>();

    for (Node node : nodeTraversal) {
      if (node.getType() == Token.NAME && node.getString().equals(name)) {
        result.add(node);
      }
    }

    return result;
  }
}