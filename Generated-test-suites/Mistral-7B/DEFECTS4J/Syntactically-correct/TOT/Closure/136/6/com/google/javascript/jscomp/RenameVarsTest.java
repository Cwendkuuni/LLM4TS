import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeFactory;

import java.util.Arrays;
import java.util.List;

public class RenameVarsTest {

  private static Node createVariableNode(String name) {
    return NodeFactory.createName(NodeFactory.createIdentifier(name));
  }

  @SuppressWarnings("unchecked")
  private static <T> List<T> asList(T... values) {
    return Arrays.asList(values);
  }

  /**
   * Tests that the RenameVars compiler pass does not rename variables that
   * should not be renamed according to the coding convention.
   */
  @org.junit.Test
  public void testDoesNotRenameExportedVariables() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    RenameVars renameVars = new RenameVars(compiler, "test", false, false, false,
        null, null, null);

    Node root = NodeFactory.createProgram(NodeFactory.emptyList());
    Node variable = createVariableNode("test");
    root.setFirstChild(variable);

    renameVars.process(compiler.getExterns(), root);

    assertEquals("test", variable.getString());
  }

  /**
   * Tests that the RenameVars compiler pass renames local variables.
   */
  @org.junit.Test
  public void testRenamesLocalVariables() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    RenameVars renameVars = new RenameVars(compiler, "test", false, false, false,
        null, null, null);

    Node root = NodeFactory.createProgram(
        NodeFactory.createList(NodeFactory.createStatementList(
            NodeFactory.createVariableStatement("int a = 1;", null),
            NodeFactory.createStatementList(NodeFactory.createExpressionStatement(
                NodeFactory.createAssignment(
                    NodeFactory.createName(NodeFactory.createIdentifier("a")),
                    NodeFactory.createNumberLiteral(2)
                )
            ))), null));

    renameVars.process(compiler.getExterns(), root);

    String oldLocalName = "a";
    String expectedLocalName = "L0";
    assertEquals(expectedLocalName, root.getFirstChild().getFirstChild()
        .getFirstChild().getString());

    String newLocalName = renameVars.getVariableMap().lookupName(oldLocalName);
    assertEquals(expectedLocalName, newLocalName);
  }

  /**
   * Tests that the RenameVars compiler pass renames global variables.
   */
  @org.junit.Test
  public void testRenamesGlobalVariables() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    RenameVars renameVars = new RenameVars(compiler, "test", false, false, false,
        null, null, null);

    Node root = NodeFactory.createProgram(
        NodeFactory.createList(NodeFactory.createStatementList(
            NodeFactory.createVariableStatement("int a = 1;", NodeFactory.createScopeNode()),
            NodeFactory.createStatementList(NodeFactory.createExpressionStatement(
                NodeFactory.createAssignment(
                    NodeFactory.createName(NodeFactory.createIdentifier("a")),
                    NodeFactory.createNumberLiteral(2)
                )
            ))), NodeFactory.createScopeNode()));

    renameVars.process(compiler.getExterns(), root);

    String oldGlobalName = "a";
    String expectedGlobalName = "test_a";
    assertEquals(expectedGlobalName, root.getFirstChild().getString());

    String newGlobalName = renameVars.getVariableMap().lookupName(oldGlobalName);
    assertEquals(expectedGlobalName, newGlobalName);
  }

  /**
   * Tests that the RenameVars compiler pass renames variables with
   * non-alphanumeric characters.
   */
  @org.junit.Test
  public void testRenamesVariablesWithNonAlphanumericCharacters() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    RenameVars renameVars = new RenameVars(compiler, "test", false, false, false,
        null, null, null);

    Node root = NodeFactory.createProgram(
        NodeFactory.createList(NodeFactory.createStatementList(
            NodeFactory.createVariableStatement("int α = 1;", null),
            NodeFactory.createStatementList(NodeFactory.createExpressionStatement(
                NodeFactory.createAssignment(
                    NodeFactory.createName(NodeFactory.createIdentifier("α")),
                    NodeFactory.createNumberLiteral(2)
                )
            ))), null));

    renameVars.process(compiler.getExterns(), root);

    String oldNameWithNonAlphanumericCharacter = "α";
    String expectedNameWithNonAlphanumericCharacter = "__α__";
    assertEquals(expectedNameWithNonAlphanumericCharacter, root.getFirstChild()
        .getFirstChild().getString());

    String newNameWithNonAlphanumericCharacter = renameVars.getVariableMap()
        .lookupName(oldNameWithNonAlphanumericCharacter);
    assertEquals(expectedNameWithNonAlphanumericCharacter, newNameWithNonAlphanumericCharacter);
  }

  /**
   * Tests that the RenameVars compiler pass does not rename variables with
   * names that are already in the map.
   */
  @org.junit.Test
  public void testDoesNotRenameAlreadyRenamedVariables() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    RenameVars renameVars = new RenameVars(compiler, "test", false, false, false,
        null, null, null);

    Node root = NodeFactory.createProgram(NodeFactory.createList(
        NodeFactory.createStatementList(
            NodeFactory.createVariableStatement("int a = 1;", null),
            NodeFactory.createStatementList(NodeFactory.createExpressionStatement(
                NodeFactory.createAssignment(
                    NodeFactory.createName(NodeFactory.createIdentifier("a")),
                    NodeFactory.createNumberLiteral(2)
                )
            ))), NodeFactory.createScopeNode()));

    renameVars.process(compiler.getExterns(), root);

    String oldRenamedName = "L0";
    String expectedOldRenamedName = "a";
    assertEquals(expectedOldRenamedName, root.getFirstChild().getFirstChild()
        .getFirstChild().getString());

    String newRenamedName = renameVars.getVariableMap().lookupName(oldRenamedName);
    assertEquals(expectedOldRenamedName, newRenamedName);
  }

  /**
   * Tests that the RenameVars compiler pass does not rename variables that
   * have already been renamed in the previous compilation.
   */
  @ org.junit.Test
  public void testDoesNotRenameAlreadyRenamedVariablesFromPreviousCompilation() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    RenameVars renameVars = new RenameVars(compiler, "test", false, false, false,
        new VariableMap(Arrays.asList("a", "L0")), null, null);

    Node root = NodeFactory.createProgram(NodeFactory.createList(
        NodeFactory.createStatementList(
            NodeFactory.createVariableStatement("int a = 1;", null),
            NodeFactory.createStatementList(NodeFactory.createExpressionStatement(
                NodeFactory.createAssignment(
                    NodeFactory.createName(NodeFactory.createIdentifier("a")),
                    NodeFactory.createNumberLiteral(2)
                )
            ))), NodeFactory.createScopeNode()));

    renameVars.process(compiler.getExterns(), root);

    String oldName = "a";
    String expectedName = "L0";
    assertEquals(expectedName, root.getFirstChild().getFirstChild()
        .getFirstChild().getString());

    String newName = renameVars.getVariableMap().lookupName(oldName);
    assertEquals(expectedName, newName);
  }

  /**
   * Tests that the RenameVars compiler pass does not rename variables with
   * names that are reserved.
   */
  @org.junit.Test
  public void testDoesNotRenameReservedVariables() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Set<String> reservedNames = Sets.newHashSet("parseInt", "console");
    RenameVars renameVars = new RenameVars(compiler, "test", false, false, false,
        null, new char[]{}, reservedNames);

    Node root = NodeFactory.createProgram(NodeFactory.createList(
        NodeFactory.createStatementList(
            NodeFactory.createVariableStatement("function parseInt() {}", null)
        ), NodeFactory.createScopeNode()));

    renameVars.process(compiler.getExterns(), root);

    String oldReservedName = "parseInt";
    String expectedReservedName = "parseInt";
    assertEquals(expectedReservedName, root.getFirstChild().getString());

    String newReservedName = renameVars.getVariableMap().lookupName(oldReservedName);
    assertEquals(expectedReservedName, newReservedName);
  }

  /**
   * Tests that the RenameVars compiler pass renames anonymous functions.
   */
  @org.junit.Test
  public void testRenamesAnonymousFunctions() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    RenameVars renameVars = new RenameVars(compiler, "test", false, true, false,
        null, null, null);

    Node root = NodeFactory.createProgram(NodeFactory.createList(
        NodeFactory.createStatementList(
            NodeFactory.createExpressionStatement(NodeFactory.createCall(
                NodeFactory.createName(NodeFactory.createIdentifier("function")),
                NodeFactory.createList(
                    NodeFactory.createStatementList(
                        NodeFactory.createVariableStatement("function f() { return 1; }",
                            null)
                    )
                )
            ))), NodeFactory.createScopeNode()));

    renameVars.process(compiler.getExterns(), root);

    String oldAnonymousFunctionName = "f";
    String expectedAnonymousFunctionName = "anonymous_function_" +
        Integer.toHexString(System.identityHashCode(root.getFirstChild()));
    assertEquals(expectedAnonymousFunctionName, root.getFirstChild().getString());

    String newAnonymousFunctionName = renameVars.getVariableMap().lookupName(oldAnonymousFunctionName);
    assertEquals(expectedAnonymousFunctionName, newAnonymousFunctionName);
  }

  /**
   * Tests that the RenameVars compiler pass does not rename variables with
   * names that are too long.
   */
  @org.junit.Test
  public void testDoesNotRenameVariablesWithNamesThatAreTooLong() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    RenameVars renameVars = new RenameVars(compiler, "test", false, false, false,
        null, null, null);

    Node root = NodeFactory.createProgram(NodeFactory.createList(
        NodeFactory.createStatementList(
            NodeFactory.createVariableStatement("int a0b = 1;", null),
            NodeFactory.createStatementList(NodeFactory.createExpressionStatement(
                NodeFactory.createAssignment(
                    NodeFactory.createName(NodeFactory.createIdentifier("a0b")),
                    NodeFactory.createNumberLiteral(2)
                )
            ))), NodeFactory.createScopeNode()));

    renameVars.process(compiler.getExterns(), root);

    String oldVariableName = "a0b";
    String expectedVariableName = "a0b";
    assertEquals(expectedVariableName, root.getFirstChild().getFirstChild()
        .getFirstChild().getString());

    String newVariableName = renameVars.getVariableMap().lookupName(oldVariableName);
    assertEquals(expectedVariableName, newVariableName);
  }
}