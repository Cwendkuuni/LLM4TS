import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.abstractTraverse;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createFunctionCall;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createLiteral;
import static com.google.javascript.rhino.NodeFactory.createReturnStatement;
import static com.google.javascript.rhino.NodeFactory.createStatement;
import static com.google.javascript.rhino.NodeFactory.createSwitchStatement;
import static com.google.javascript.rhino.NodeFactory.createVariable;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {

  @Test
  public void testSimpleFunctionWithDeadCode() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("ECMAERR", Level.WARNING);
    options.setWarningLevel("UNDEFINED_EXPR", Level.ERROR);
    options.setWarningLevel("UNUSED_VAR", Level.WARNING);
    options.setWarningLevel("DEAD_CODE", Level.WARNING);

    Node source = createBlock(
        createStatement(createVariable("x", createLiteral(1))),
        createStatement(createReturnStatement(createLiteral(2))),
        createStatement(createLiteral(3))
    );

    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    CompilerResult result = compiler.compile(source, options, pass);

    assertTrue(result.getWarnings().isEmpty());
    assertFalse(NodeUtil.isNodeInTree(createLiteral(3), result.getTree()));
  }

  @Test
  public void testSimpleFunctionWithDeadCodeInsideIfStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("ECMAERR", Level.WARNING);
    options.setWarningLevel("UNDEFINED_EXPR", Level.ERROR);
    options.setWarningLevel("UNUSED_VAR", Level.WARNING);
    options.setWarningLevel("DEAD_CODE", Level.WARNING);

    Node source = createBlock(
        createStatement(createVariable("x", createLiteral(1))),
        createStatement(createIfStatement(createLiteral(0), createStatement(createLiteral(3)))),
        createStatement(createLiteral(2))
    );

    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    CompilerResult result = compiler.compile(source, options, pass);

    assertTrue(result.getWarnings().isEmpty());
    assertFalse(NodeUtil.isNodeInTree(createLiteral(3), result.getTree()));
  }

  @Test
  public void testSimpleFunctionWithDeadCodeInsideSwitchStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("ECMAERR", Level.WARNING);
    options.setWarningLevel("UNDEFINED_EXPR", Level.ERROR);
    options.setWarningLevel("UNUSED_VAR", Level.WARNING);
    options.setWarningLevel("DEAD_CODE", Level.WARNING);

    Node source = createBlock(
        createStatement(createVariable("x", createLiteral(1))),
        createStatement(createSwitchStatement(
            createExpression(createIdentifier("x")),
            ImmutableList.of(
                createCaseStatement(createLiteral(0), createStatement(createLiteral(3))),
                createDefaultStatement(createLiteral(4))
            )
        )),
        createStatement(createLiteral(2))
    );

    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    CompilerResult result = compiler.compile(source, options, pass);

    assertTrue(result.getWarnings().isEmpty());
    assertFalse(NodeUtil.isNodeInTree(createLiteral(3), result.getTree()));
  }

  @Test
  public void testFunctionWithDeadCodeAfterReturnStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("ECMAERR", Level.WARNING);
    options.setWarningLevel("UNDEFINED_EXPR", Level.ERROR);
    options.setWarningLevel("UNUSED_VAR", Level.WARNING);
    options.setWarningLevel("DEAD_CODE", Level.WARNING);

    Node source = createBlock(
        createStatement(createVariable("x", createLiteral(1))),
        createStatement(createReturnStatement(createLiteral(2))),
        createStatement(createLiteral(3))
    );

    CompilerPass pass = new UnreachableCodeElimination(compiler, false);
    CompilerResult result = compiler.compile(source, options, pass);

    assertTrue(result.getWarnings().isEmpty());
    assertFalse(NodeUtil.isNodeInTree(createLiteral(3), result.getTree()));
  }

  @Test
  public void testFunctionWithDeadCodeInsideLoop() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("ECMAERR", Level.WARNING);
    options.setWarningLevel("UNDEFINED_EXPR", Level.ERROR);
    options.setWarningLevel("UNUSED_VAR", Level.WARNING);
    options.setWarningLevel("DEAD_CODE", Level.WARNING);

    Node source = createBlock(
        createStatement(createVariable("x", createLiteral(1))),
        createStatement(createLabeledStatement(createIdentifier("loop"), createBlock(
            createStatement(createBreakStatement()),
            createStatement(createLiteral(3))
        ))),
        createStatement(createReturnStatement(createLiteral(2)))
    );

    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    CompilerResult result = compiler.compile(source, options, pass);

    assertTrue(result.getWarnings().isEmpty());
    assertFalse(NodeUtil.isNodeInTree(createLiteral(3), result.getTree()));
  }

  @Test
  public void testFunctionWithDeadCodeInsideConditionalBranch() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("ECMAERR", Level.WARNING);
    options.setWarningLevel("UNDEFINED_EXPR", Level.ERROR);
    options.setWarningLevel("UNUSED_VAR", Level.WARNING);
    options.setWarningLevel("DEAD_CODE", Level.WARNING);

    Node source = createBlock(
        createStatement(createVariable("x", createLiteral(1))),
        createStatement(createIfStatement(
            createExpression(createBinaryOperator(createIdentifier("x"), Token.GT, createLiteral(0))),
            createBlock(
                createStatement(createLiteral(3)),
                createStatement(createReturnStatement(createLiteral(2)))
            ),
            createBlock(createStatement(createLiteral(4)))
        )),
        createStatement(createLiteral(5))
    );

    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    CompilerResult result = compiler.compile(source, options, pass);

    assertTrue(result.getWarnings().isEmpty());
    assertFalse(NodeUtil.isNodeInTree(createLiteral(3), result.getTree()));
  }

  private Node createCaseStatement(Node value, Node statement) {
    return NodeUtil.createCaseStatement(createExpression(value), statement);
  }

  private Node createDefaultStatement(Node statement) {
    return NodeUtil.createDefaultStatement(statement);
  }

  private Node createLabeledStatement(String label, Node statement) {
    return NodeUtil.createLabeledStatement(createIdentifier(label), statement);
  }

  private Node createIfStatement(Node condition, Node thenBlock, Node elseBlock) {
    return NodeUtil.createIfStatement(condition, thenBlock, elseBlock);
  }

  private Node createBreakStatement() {
    return NodeUtil.createBreakStatement();
  }

  private Node createSwitchStatement(Node expression, List<Node> cases) {
    return NodeUtil.createSwitchStatement(expression, cases);
  }

  private Node createStatement(Node node) {
    return NodeUtil.createStatement(node);
  }

  private Node createReturnStatement(Node value) {
    return NodeUtil.createReturnStatement(value);
  }

  private Node createVariable(String name, Node initialValue) {
    return NodeUtil.createVariable(name, initialValue);
  }

  private Node createLiteral(int value) {
    return NodeFactory.createLiteral(value);
  }

  private Node createExpression(Node node) {
    return NodeUtil.createExpression(node);
  }

  private Node createBinaryOperator(Node left, int op, Node right) {
    return NodeUtil.createBinaryOperator(left, op, right);
  }

  private Node createBlock(Node... statements) {
    return NodeFactory.createBlock(Lists.newArrayList(statements));
  }

  private void abstractTraverse(Node node, CompilerPass pass) {
    abstractTraverse(new Compiler(), node, pass);
  }
}