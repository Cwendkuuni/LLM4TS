import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNull;
import static com.google.javascript.rhino.NodeFactory.createNumber;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createAssign;
import static com.google.javascript.rhino.NodeFactory.createAdd;
import static com.google.javascript.rhino.NodeFactory.createIfStatement;
import static com.google.javascript.rhino.NodeFactory.createWhileStatement;
import static com.google.javascript.rhino.NodeFactory.createForStatement;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createReturnStatement;
import static com.google.javascript.rhino.NodeFactory.createCallExpression;
import static com.google.javascript.rhino.NodeFactory.createThisValue;
import static com.google.javascript.rhino.NodeFactory.createFunctionExpression;
import static com.google.javascript.rhino.NodeFactory.createFunctionCall;
import static com.google.javascript.rhino.NodeFactory.createVariable;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createVariableDeclarationList;
import static com.google.javascript.rhino.NodeFactory.createVariableDeclaration;
import static com.google.javascript.rhino.NodeFactory.createScope;
import static com.google.javascript.rhino.NodeFactory.createBlockStatement;
import static com.google.javascript.rhino.NodeFactory.createStatementList;
import static com.google.javascript.rhino.NodeFactory.createEmptyStatement;
import static com.google.javascript.rhino.NodeFactory.createIfStatement;
import static com.google.javascript.rhino.NodeFactory.createExpressionStatement;
import static com.google.javascript.rhino.NodeFactory.createAssignment;
import static com.google.javascript.rhino.NodeFactory.createBinaryOperator;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class DeadAssignmentsEliminationTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
  }

  @Test
  public void testRemoveDeadAssignmentsSimple() {
    String sourceCode =
        "function f() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return x;" +
        "}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNode = NodeUtil.findFunctionInScope(root, "f");
    Node functionBody = functionNode.getFirstChild();
    Node functionReturn = NodeUtil.getFunctionReturn(functionBody);

    assertEquals(1, functionReturn.getFirstChild().getNumberValue().intValue());
  }

  @Test
  public void testRemoveDeadAssignmentsIf() {
    String sourceCode =
        "function f() {" +
        "  var x = 1;" +
        "  if(x) { x = 2; }" +
        "  return x;" +
        "}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNode = NodeUtil.findFunctionInScope(root, "f");
    Node functionBody = functionNode.getFirstChild();
    Node functionReturn = NodeUtil.getFunctionReturn(functionBody);

    assertEquals(1, functionReturn.getFirstChild().getNumberValue().intValue());
  }

  @Test
  public void testRemoveDeadAssignmentsWhile() {
    String sourceCode =
        "function f() {" +
        "  var x = 1;" +
        "  while(x < 5) {" +
        "    x = x + 1;" +
        "  }" +
        "  return x;" +
        "}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNode = NodeUtil.findFunctionInScope(root, "f");
    Node functionBody = functionNode.getFirstChild();
    Node functionReturn = NodeUtil.getFunctionReturn(functionBody);

    assertEquals(5, functionReturn.getFirstChild().getNumberValue().intValue());
  }

  @Test
  public void testRemoveDeadAssignmentsFor() {
    String sourceCode =
        "function f() {" +
        "  var x = 0;" +
        "  for (var i = 0; i < 5; i++) {" +
        "    x = x + 1;" +
        "  }" +
        "  return x;" +
        "}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNode = NodeUtil.findFunctionInScope(root, "f");
    Node functionBody = functionNode.getFirstChild();
    Node functionReturn = NodeUtil.getFunctionReturn(functionBody);

    assertEquals(5, functionReturn.getFirstChild().getNumberValue().intValue());
  }

  @Test
  public void testRemoveDeadAssignmentsFunctionCall() {
    String sourceCode =
        "function f() {" +
        "  var x = 1;" +
        "  g();" +
        "  x = 2;" +
        "  return x;" +
        "}" +
        "function g() {}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNode = NodeUtil.findFunctionInScope(root, "f");
    Node functionBody = functionNode.getFirstChild();
    Node functionReturn = NodeUtil.getFunctionReturn(functionBody);

    assertEquals(1, functionReturn.getFirstChild().getNumberValue().intValue());
  }

  @Test
  public void testRemoveDeadAssignmentsFunctionCallWithAssignment() {
    String sourceCode =
        "function f() {" +
        "  var x = 1;" +
        "  g();" +
        "  x = 2;" +
        "  x = x + 1;" +
        "  return x;" +
        "}" +
        "function g() {}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNode = NodeUtil.findFunctionInScope(root, "f");
    Node functionBody = functionNode.getFirstChild();
    Node functionReturn = NodeUtil.getFunctionReturn(functionBody);

    assertEquals(3, functionReturn.getFirstChild().getNumberValue().intValue());
  }

  @Test
  public void testRemoveDeadAssignmentsComplex() {
    String sourceCode =
        "function f() {" +
        "  var x = 1;" +
        "  if(x > 0) {" +
        "    x = 2;" +
        "    while(x < 5) {" +
        "      x = x + 1;" +
        "    }" +
        "  }" +
        "  return x;" +
        "}" +
        "function g() {" +
        "  var y = 1;" +
        "  for(var i = 0; i < 5; i++) {" +
        "    y = y + 1;" +
        "  }" +
        "  return y;" +
        "}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNodeX = NodeUtil.findFunctionInScope(root, "f");
    Node functionBodyX = functionNodeX.getFirstChild();
    Node functionReturnX = NodeUtil.getFunctionReturn(functionBodyX);

    Node functionNodeY = NodeUtil.findFunctionInScope(root, "g");
    Node functionBodyY = functionNodeY.getFirstChild();
    Node functionReturnY = NodeUtil.getFunctionReturn(functionBodyY);

    assertEquals(6, functionReturnX.getFirstChild().getNumberValue().intValue());
    assertEquals(6, functionReturnY.getFirstChild().getNumberValue().intValue());
  }

  @Test(expected = AssertionError.class)
  public void testRemoveDeadAssignmentsException() {
    String sourceCode =
        "function f() {" +
        "  try {" +
        "    var x = 1;" +
        "    throw new Error('Test Error')" +
        "  } catch (e) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = null;

    try {
      result = compiler.compileDeep(Lists.newArrayList(sourceFile), options);
    } catch (IOException e) {
      // Ignore, this is expected
    }

    Node functionNode = NodeUtil.findFunctionInScope(root, "f");
    Node functionBody = functionNode.getFirstChild();
    Node functionReturn = NodeUtil.getFunctionReturn(functionBody);

    fail("Dead assignment elimination should have thrown an exception");
  }

  @Test
  public void testRemoveDeadAssignmentsReturnStatement() {
    String sourceCode =
        "function f() {" +
        "  var x = 1;" +
        "  return x;" +
        "}" +
        "function g() {" +
        "  return 1;" +
        "}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNodeX = NodeUtil.findFunctionInScope(root, "f");
    Node functionBodyX = functionNodeX.getFirstChild();
    Node functionReturnX = NodeUtil.getFunctionReturn(functionBodyX);

    Node functionNodeY = NodeUtil.findFunctionInScope(root, "g");
    Node functionBodyY = functionNodeY.getFirstChild();
    Node functionReturnY = NodeUtil.getFunctionReturn(functionBodyY);

    assertEquals(1, functionReturnX.getFirstChild().getNumberValue().intValue());
    assertEquals(1, functionReturnY.getFirstChild().getIntValue());
  }

  @Test
  public void testRemoveDeadAssignmentsMultipleAssignments() {
    String sourceCode =
        "function f() {" +
        "  var x = 1;" +
        "  x = x + 1;" +
        "  x = 2;" +
        "  return x;" +
        "}";

    SourceFile sourceFile = new SourceFile("TestFile.js", sourceCode);
    Node root = sourceFile.getAst();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    NodeTraversal nodeTraversal = new NodeTraversal(root, null, null, null, null);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);
    nodeTraversal.traverse();

    CompilerResult result = compiler.compileDeep(Lists.newArrayList(sourceFile),
        options);

    Node functionNode = NodeUtil.findFunctionInScope(root, "f");
    Node functionBody = functionNode.getFirstChild();
    Node functionReturn = NodeUtil.getFunctionReturn(functionBody);

    assertEquals(3, functionReturn.getFirstChild().getNumberValue().intValue());
  }
}