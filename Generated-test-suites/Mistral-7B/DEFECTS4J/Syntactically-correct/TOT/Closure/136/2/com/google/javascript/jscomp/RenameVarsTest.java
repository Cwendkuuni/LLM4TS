import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    VariableMap prevUsedRenameMap = new VariableMap();

    String prefix = "prefix_";
    RenameVars renameVars = new RenameVars(compiler, prefix, false, false, false,
        prevUsedRenameMap, null, null);

    CompilerInput input = mock(CompilerInput.class);
    Node root = createNode("var a = 5;");
    Node externs = createNode("var b = 10;");

    renameVars.process(externs, root);

    // Test that global variable renaming works
    assertEquals("prefix_b", externs.getFirstChild().getString());
    assertNull(root.getFirstChild());

    // Test that local variable renaming works
    Node localVarRoot = createNode("function foo(a) { var a = 1; }");
    renameVars.process(externs, localVarRoot);
    assertEquals("prefix_a", localVarRoot.getFirstChild().getFirstChild().getString());

    // Test that anonymous function names are preserved
    Node anonymousFunction = createNode(
        "function foo() { return function a() {}; }");
    renameVars.process(externs, anonymousFunction);
    assertEquals("a", anonymousFunction.getFirstChild().getFirstChild()
        .getFirstChild().getFirstChild().getString());

    // Test that external variables are not renamed
    renameVars.localRenamingOnly = true;
    renameVars.process(externs, root);
    assertEquals("b", externs.getFirstChild().getString());

    // Test that variables with reserved names are not renamed
    char[] reservedChars = {'$', '.', '!'};
    Set<String> reservedNames = Sets.newHashSet("name1", "name2");
    RenameVars reservedRenameVars = new RenameVars(compiler, prefix, false, false, false,
        prevUsedRenameMap, reservedChars, reservedNames);
    Node reservedVariable = createNode("var name1 = 5;");
    reservedRenameVars.process(externs, root);
    assertEquals("name1", reservedVariable.getString());

    // Test that variables with the same name as a local variable are not renamed
    Node sameNameLocalVariable = createNode(
        "function foo() { var a = 5; var a = 10; }");
    renameVars.process(externs, sameNameLocalVariable);
    assertEquals("a", sameNameLocalVariable.getLastChild().getString());

    // Test that variables with the same name as a reserved word are not renamed
    CharSequence keywords = NodeUtil.getJavaScriptKeywords();
    for (String keyword : keywords.split("\\s+")) {
      Node reservedKeyword = createNode("var " + keyword + " = 5;");
      renameVars.process(externs, root);
      assertEquals(keyword, reservedKeyword.getString());
    }

    // Test that variables in externs are renamed when localRenamingOnly is false
    renameVars.localRenamingOnly = false;
    renameVars.process(externs, root);
    assertEquals("prefix_a", root.getFirstChild().getFirstChild().getString());

    // Test that variables in globals are renamed when localRenamingOnly is true
    renameVars.localRenamingOnly = true;
    renameVars.process(createNode("var a = 5;"), root);
    assertEquals("a", root.getFirstChild().getFirstChild().getString());

    // Test that variables with long names are renamed
    String longVariableName = "veryLongVariableName";
    Node longVariableNode = createNode("var " + longVariableName + " = 5;");
    renameVars.process(externs, root);
    String expectedLongVariableName = prefix + longVariableName.substring(0, 1)
        + String.valueOf(longVariableName.length() - 1);
    assertEquals(expectedLongVariableName, longVariableNode.getString());

    // Test that variables with short names are not renamed
    String shortVariableName = "a";
    Node shortVariableNode = createNode("var " + shortVariableName + " = 5;");
    renameVars.process(externs, root);
    assertEquals(shortVariableName, shortVariableNode.getString());

    // Test that variables are renamed in the order they are defined
    Node multipleVariables =
        createNode("var a = 1, b = 2, c = 3;");
    renameVars.process(externs, root);
    String prefixA = prefix + "a";
    String prefixB = prefix + "b";
    String prefixC = prefix + "c";
    assertEquals(prefixA, multipleVariables.getFirstChild().getString());
    assertEquals(prefixB, multipleVariables.getLastChild(1).getString());
    assertEquals(prefixC, multipleVariables.getLastChild(2).getString());

    // Test that variables with the same name but in different scopes are renamed differently
    Node sameNameVariables = createNode(
        "function foo() {" +
          "  var a = 1;" +
          "  function bar() {" +
          "    var a = 2;" +
          "  }" +
          "  bar();" +
          "}"
        );
    renameVars.process(externs, root);
    assertEquals("foo_a", sameNameVariables.getFirstChild().getFirstChild()
        .getString());
    assertEquals("bar_a", sameNameVariables.getLastChild().getFirstChild()
        .getFirstChild().getString());

    // Test that variables with the same name but different types are renamed differently
    Node sameNameVariablesWithDifferentTypes =
        createNode("function foo() {" +
                  "  var a = 1;" +
                  "  function bar() {" +
                  "    var a = true;" +
                  "  }" +
                  "  bar();" +
                  "}"
                 );
    renameVars.process(externs, root);
    assertEquals("foo_a", sameNameVariablesWithDifferentTypes.getFirstChild()
        .getFirstChild().getString());
    assertEquals("bar_a", sameNameVariablesWithDifferentTypes.getLastChild()
        .getFirstChild().getString());

    // Test that variables with the same name but different initializations are renamed differently
    Node sameNameVariablesWithDifferentInitializations =
        createNode("function foo() {" +
                  "  var a = 1;" +
                  "  var a = 'hello';" +
                  "}"
                 );
    renameVars.process(externs, root);
    assertEquals("foo_a_1", sameNameVariablesWithDifferentInitializations
        .getFirstChild().getFirstChild().getString());
    assertEquals("foo_a_2", sameNameVariablesWithDifferentInitializations
        .getLastChild().getString());

    // Test that variables with the same name but different inputs are renamed differently
    CompilerInput input1 = mock(CompilerInput.class);
    CompilerInput input2 = mock(CompilerInput.class);
    when(input1.getSourceName()).thenReturn("path1.js");
    when(input2.getSourceName()).thenReturn("path2.js");
    Node sameNameVariablesWithDifferentInputs =
        createNode("var a = 1;", input1) + createNode("var a = 2;", input2);
    renameVars.process(externs, root);
    assertEquals("path1_js_a_1", sameNameVariablesWithDifferentInputs.getFirstChild()
        .getString());
    assertEquals("path2_js_a_2", sameNameVariablesWithDifferentInputs.getLastChild()
        .getString());

    // Test that variables with the same name but different parent nodes are renamed differently
    Node parentNode1 = createNode("var a = 1;");
    Node parentNode2 = createNode("function foo() { var a = 2; }");
    Node sameNameVariablesWithDifferentParentNodes =
        parentNode1 + parentNode2;
    renameVars.process(externs, root);
    assertEquals("parentNode1_a_1", sameNameVariablesWithDifferentParentNodes
        .getFirstChild().getString());
    assertEquals("foo_a_2", sameNameVariablesWithDifferentParentNodes.getLastChild()
        .getString());

    // Test that variables with the same name but different types and parents are renamed differently
    Node parentNode3 = createNode("function foo() { var a = 1; }");
    Node parentNode4 = createNode("function bar() { var a = true; }");
    Node sameNameVariablesWithDifferentTypesAndParents =
        parentNode3 + parentNode4;
    renameVars.process(externs, root);
    assertEquals("foo_a_1", sameNameVariablesWithDifferentTypesAndParents
        .getFirstChild().getFirstChild().getString());
    assertEquals("bar_a_2", sameNameVariablesWithDifferentTypesAndParents
        .getLastChild().getString());

    // Test that variables with the same name but different scopes and parents are renamed differently
    Node parentNode5 = createNode("function foo() { var a = 1; }");
    Node parentNode6 = createNode("function bar() { function baz() { var a = 2; } }");
    Node sameNameVariablesWithDifferentScopesAndParents =
        parentNode5 + parentNode6;
    renameVars.process(externs, root);
    assertEquals("foo_a_1", sameNameVariablesWithDifferentScopesAndParents
        .getFirstChild().getFirstChild().getString());
    assertEquals("baz_a_2", sameNameVariablesWithDifferentScopesAndParents
        .getLastChild().getString());
  }

  private Node createNode(String code, CompilerInput... inputs) {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    Node root = nodeTraversal.parse(code, inputs);
    return root;
  }

  private Node createNode(String code) {
    return createNode(code, (CompilerInput) null);
  }
}