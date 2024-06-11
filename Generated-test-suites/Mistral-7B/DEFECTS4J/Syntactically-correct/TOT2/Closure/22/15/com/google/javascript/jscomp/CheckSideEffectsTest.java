import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createCallExpression;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createVariable;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler();
    compiler.setOptions(options);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    input = NodeFactory.createCompilationUnit();
    root = NodeFactory.createBlock(NodeFactory.createStatementList());
  }

  @Test
  public void testEmptyBlock() {
    Node block = NodeFactory.createBlock(NodeFactory.createStatementList());
    input.getAstRoot(compiler).addChildrenToBack(block);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testSimpleStatement() {
    Node statement = NodeFactory.createCallExpression(
        NodeFactory.createName("console.log"),
        NodeFactory.createNumberLiteral(42));
    input.getAstRoot(compiler).addChildrenToBack(statement);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testVoidStatement() {
    Node statement = NodeFactory.createCallExpression(
        NodeFactory.createName("console.log"),
        NodeFactory.createName("void 0"));
    input.getAstRoot(compiler).addChildrenToBack(statement);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testStatementWithSideEffect() {
    Node var = NodeFactory.createVariable("x", NodeFactory.createNumberLiteral(42), true);
    Node statement = NodeFactory.createAssignment(
        NodeFactory.createName("x"),
        NodeFactory.createCallExpression(
            NodeFactory.createName("foo"),
            NodeFactory.createNumberLiteral(5)));
    input.getAstRoot(compiler).addChildrenToBack(statement);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(!checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testStringConcatenation() {
    Node string1 = NodeFactory.createStringLiteral("This is ");
    Node string2 = NodeFactory.createStringLiteral("a test");
    Node statement = NodeFactory.createCallExpression(
        NodeFactory.createName("console.log"),
        NodeFactory.createCallExpression(
            NodeFactory.createName("concat"),
            NodeFactory.createName("this"),
            string1,
            string2));
    input.getAstRoot(compiler).addChildrenToBack(statement);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testFunctionCallWithSideEffect() {
    Node functionCall = NodeFactory.createCallExpression(
        NodeFactory.createName("console.log"),
        NodeFactory.createNumberLiteral(42));
    Node statement = NodeFactory.createStatement(functionCall);
    input.getAstRoot(compiler).addChildrenToBack(statement);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(!checkSideEffects.problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testProtectedCode() {
    Node statement = NodeFactory.createCallExpression(
        NodeFactory.createName(CheckSideEffects.PROTECTOR_FN),
        NodeFactory.createNumberLiteral(42));
    input.getAstRoot(compiler).addChildrenToBack(statement);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
  }

  @Test
  public void testProtectedCodeRemoved() {
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testDeadCodeWithSideEffect() {
    Node statement1 = NodeFactory.createCallExpression(
        NodeFactory.createName("console.log"),
        NodeFactory.createNumberLiteral(42));
    Node statement2 = NodeFactory.createStatement(
        NodeFactory.createExpressionStatement(
            NodeFactory.createCallExpression(
                NodeFactory.createName("console.log"),
                NodeFactory.createNumberLiteral(43))));
    input.getAstRoot(compiler).addChildrenToBack(statement1);
    input.getAstRoot(compiler).addChildrenToBack(statement2);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(!checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testDeadCodeWithSideEffectRemoved() {
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckLevel() {
    CheckLevel level = CheckLevel.ERROR;
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    Node statement = NodeFactory.createCallExpression(
        NodeFactory.createName("console.log"),
        NodeFactory.createNumberLiteral(42));
    input.getAstRoot(compiler).addChildrenToBack(statement);
    compiler.process(input, root);
    checkSideEffects.process(root, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
}