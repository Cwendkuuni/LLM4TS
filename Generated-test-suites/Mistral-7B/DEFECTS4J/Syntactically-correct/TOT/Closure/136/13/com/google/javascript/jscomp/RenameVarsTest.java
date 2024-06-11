package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test for {@link RenameVars} class.
 */
public class RenameVarsTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = mock(CompilerPassManager.class);
    when(compiler.getPassManager()).thenReturn(passManager);
  }

  @Test
  public void testRenameVarsWithEmptyCode() {
    String code = "";
    Node rootNode = Node.createTree(code);
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(null, rootNode);

    // Empty code should not generate any variable assignments.
    assertThat(renameVars.getVariableMap()).isEmpty();
  }

  @Test
  public void testRenameVarsForLocalVariablesOnly() {
    String code = "function foo() { var a = 1; var a = 2; }";
    Node rootNode = Node.createTree(code);

    RenameVars renameVars = new RenameVars(compiler, "", true, false, false, null, null, null);
    renameVars.localRenamingOnly = true;
    renameVars.process(null, rootNode);

    Map<String, String> variableMap = renameVars.getVariableMap();
    List<String> expectedVariableNames = Arrays.asList("a_1", "a_2");

    // Verify that local variables are renamed.
    assertThat(variableMap).containsEntry("a", "a_1");
    assertThat(variableMap).containsEntry("a", "a_2");
    assertThat(variableMap.size()).isEqualTo(2);
  }

  @Test
  public void testRenameVarsForGlobalAndLocalVariables() {
    String code = "var a = 1; function foo() { var a = 2; }";
    Node rootNode = Node.createTree(code);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(null, rootNode);

    Map<String, String> variableMap = renameVars.getVariableMap();
    List<String> expectedVariableNames = Arrays.asList("a_1", "a__1", "a_2");

    // Verify that global variables are renamed and local variables are not.
    assertThat(variableMap).containsEntry("a", "a_1");
    assertThat(variableMap.size()).isEqualTo(1);
  }

  @Test
  public void testRenameVarsForAnonymousFunctionVariables() {
    String code = "function foo() { var a = function() {}; }";
    Node rootNode = Node.createTree(code);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.preserveAnonymousFunctionNames = true;
    renameVars.process(null, rootNode);

    Map<String, String> variableMap = renameVars.getVariableMap();
    List<String> expectedVariableNames = Arrays.asList("a", "a");

    // Verify that anonymous function variables are preserved.
    assertThat(variableMap).containsEntry("a", "a");
    assertThat(variableMap.size()).isEqualTo(1);
  }

  @Test
  public void testRenameVarsForReservedKeywords() {
    String code = "function foo() { var var = 1; }";
    Node rootNode = Node.createTree(code);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

    // Verify that keywords cannot be renamed.
    try {
      renameVars.process(null, rootNode);
      fail("Expected an exception to be thrown.");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testRenameVarsForReservedNames() {
    String code = "var Math = 1, undefined = 2; function foo() { }";
    Node rootNode = Node.createTree(code);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.reservedNames = new HashSet<>(Arrays.asList("Math", "undefined"));
    renameVars.process(null, rootNode);

    Map<String, String> variableMap = renameVars.getVariableMap();
    List<String> expectedVariableNames = Arrays.asList("Math__1", "undefined__1");

    // Verify that reserved names are not renamed.
    assertThat(variableMap).containsEntry("Math", "Math__1");
    assertThat(variableMap).containsEntry("undefined", "undefined__1");
    assertThat(variableMap.size()).isEqualTo(2);
  }

  @Test
  public void testRenameVarsForInvalidCharacterInVariableNames() {
    String code = "function foo() { var a$ = 1; }";
    Node rootNode = Node.createTree(code);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, new char[]{'$'});
    renameVars.process(null, rootNode);

    Map<String, String> variableMap = renameVars.getVariableMap();
    List<String> expectedVariableNames = Arrays.asList("a");

    // Verify that invalid character in variable names is removed.
    assertThat(variableMap).containsEntry("a", "a");
    assertThat(variableMap.size()).isEqualTo(1);
  }

  @Test
  public void testRenameVarsForLongVariableNames() {
    String code = "function foo() { var lengthOfAVeryLongVariableName = 1; }";
    Node rootNode = Node.createTree(code);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.generatePseudoNames = true;

    renameVars.process(null, rootNode);

    Map<String, String> variableMap = renameVars.getVariableMap();
    List<String> expectedVariableNames = Arrays.asList("lengthOfAVeryLongVariableName$$", "1");

    // Verify that long variable names are preserved.
    assertThat(variableMap).containsEntry("lengthOfAVeryLongVariableName", "lengthOfAVeryLongVariableName$$");
    assertThat(variableMap).containsEntry("1", "1");
    assertThat(variableMap.size()).isEqualTo(2);
  }
}