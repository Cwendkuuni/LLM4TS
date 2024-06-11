package com.google.javascript.jscomp.tests;

import static com.google.javascript.jscomp.CheckSideEffects.*;
import static com.google.javascript.rhino.NodeProperty.JSDOC_INFO;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CheckSideEffectsTest {

  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() throws IOException {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);

    SourceFile sourceFile = new SourceFile("CheckSideEffectsTest.js", "source code");
    rootNode = sourceFile.getTree();
  }

  @Test
  public void testCheckSideEffects_Simple() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;

    AbstractCompiler compilerPass = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal traversal = new NodeTraversal(compilerPass);
    traversal.traverse(rootNode);
    List<JSError> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());
  }

  @Test
  public void testCheckSideEffects_UsefulCode() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;

    AbstractCompiler compilerPass = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal traversal = new NodeTraversal(compilerPass);

    // Add a simple assignment statement that does not have any side effect.
    Node assignmentNode =
        IR.assign(IR.name("x"), IR.intLit(42));
    rootNode.getFirstChild().addChildToBack(assignmentNode);

    traversal.traverse(rootNode);
    List<JSError> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Check if the generated warning message is correct.
    String expectedMessage = "Suspicious code. This code lacks side-effects.";
    Node warningNode = findWarningNode(assignmentNode);
    String actualMessage = warningNode.getString();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testCheckSideEffects_SideEffectFreeStatementWithJSDocInfo() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;

    AbstractCompiler compilerPass = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal traversal = new NodeTraversal(compilerPass);

    // Add a statement with JSDocInfo
    Node assignmentNode = IR.assign(IR.name("x"), IR.intLit(42));
    JSDocInfoBuilder builder = new JSDocInfoBuilder(false);
    builder.recordFunction();
    builder.recordParam("arg1", "description");
    assignmentNode.setJSDocInfo(builder.build(assignmentNode));
    rootNode.getFirstChild().addChildToBack(assignmentNode);

    traversal.traverse(rootNode);
    List<JSError> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Check if the generated warning message is not shown.
    Node warningNode = findWarningNode(assignmentNode);
    assertFalse(warningNode != null);
  }

  @Test
  public void testCheckSideEffects_NonSideEffectStatementInsideLoop() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;

    AbstractCompiler compilerPass = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal traversal = new NodeTraversal(compilerPass);
    // Add a for loop with a non-side effect statement
    Node forNode = IR.forInit(IR.name("i"), IR.intLit(0), IR.intLit(5), IR.emptyStatement());
    Node loopBody = IR.assign(IR.name("x"), IR.intLit(42));
    Node forStatement = IR.forStatement(forNode, loopBody, IR.emptyStatement());
    rootNode.getFirstChild().addChildToBack(forStatement);

    traversal.traverse(rootNode);
    List<JSError> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Check if the generated warning message is not shown.
    Node warningNode = findWarningNode(loopBody);
    assertFalse(warningNode != null);
  }

  @Test
  public void testCheckSideEffects_SideEffectFreeStatementInsideLoop() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;

    AbstractCompiler compilerPass = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal traversal = new NodeTraversal(compilerPass);
    // Add a for loop with a side effect statement
    Node forNode = IR.forInit(IR.name("i"), IR.intLit(0), IR.intLit(5), IR.emptyStatement());
    Node loopBody = IR.assign(IR.name("x"), IR.name("x").get(0));
    Node forStatement = IR.forStatement(forNode, loopBody, IR.emptyStatement());
    rootNode.getFirstChild().addChildToBack(forStatement);

    traversal.traverse(rootNode);
    List<JSError> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    // Check if the generated warning message is correct.
    String expectedMessage = "Suspicious code. This code lacks side-effects.";
    String actualMessage = errors.get(0).getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testCheckSideEffects_SideEffectStatementInsideFunctionCall() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;

    AbstractCompiler compilerPass = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal traversal = new NodeTraversal(compilerPass);

    // Add a function call with a side effect statement
    Node functionCallNode = IR.call(IR.name("myFunction"), IR.list(IR.name("arg1")));
    Node sideEffectStatementNode = IR.assign(IR.name("x"), IR.intLit(42));
    Node functionNode = IR.block(IR.list(sideEffectStatementNode), IR.emptyStatement());
    Node functionExpressionNode = IR.exprStatement(IR.assign(IR.name("myFunction"), functionNode));
    rootNode.getFirstChild().addChildToBack(functionExpressionNode);

    traversal.traverse(rootNode);
    List<JSError> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    // Check if the generated warning message is correct.
    String expectedMessage = "Suspicious code. This code lacks side-effects.";
    String actualMessage = errors.get(0).getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testCheckSideEffects_ProtectSideEffectFreeCode() {
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;

    AbstractCompiler compilerPass = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    NodeTraversal traversal = new NodeTraversal(compilerPass);

    // Add a statement that may have side effect
    Node statementNode = IR.assign(IR.name("x"), IR.name("document").get("body").get(0));
    rootNode.getFirstChild().addChildToBack(statementNode);

    traversal.traverse(rootNode);
    List<JSError> errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Check if the protected side-effect free nodes are not removed.
    Node protectedNode = findProtectedNode(statementNode);
    assertFalse(protectedNode == null);

    // Check if the protected node is still present after optimization passes.
    AbstractCompiler stripProtectionPass = new StripProtection(compiler);
    NodeTraversal stripTraversal = new NodeTraversal(stripProtectionPass);
    stripTraversal.traverse(rootNode);
    Node protectedNodeAfterOptimization = findProtectedNode(statementNode);
    assertTrue(protectedNodeAfterOptimization != null);
  }

  private Node findWarningNode(Node node) {
    for (Node childNode : node.getChildren()) {
      if (childNode.getType() == Token.ERROR) {
        DiagnosticType diagnosticType = childNode.getValue();
        if (diagnosticType.getCode().equals(JSC_USELESS_CODE)) {
          return childNode;
        }
      }
      Node warningNode = findWarningNode(childNode);
      if (warningNode != null) {
        return warningNode;
      }
    }
    return null;
  }

  private Node findProtectedNode(Node node) {
    for (Node childNode : node.getChildren()) {
      if (childNode.isCall() && childNode.getFirstChild().isName() &&
          childNode.getFirstChild().getString().equals(PROTECTOR_FN)) {
        return childNode;
      }
      Node protectedNode = findProtectedNode(childNode);
      if (protectedNode != null) {
        return protectedNode;
      }
    }
    return null;
  }

  private static final DiagnosticType JSC_USELESS_CODE = DiagnosticType.warning(
      "JSC_USELESS_CODE",
      "Suspicious code. {0}");

  private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";
}