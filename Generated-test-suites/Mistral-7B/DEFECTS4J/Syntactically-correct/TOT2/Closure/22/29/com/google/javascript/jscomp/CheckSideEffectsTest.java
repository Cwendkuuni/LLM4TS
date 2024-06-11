import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String source = "function foo() { return 1; } var s = 'this string is ';\n" +
        "// continued on the next line but you forgot the +\n" +
        "x = foo();\n" +
        "foo();;\n";

    Compiler compiler = new Compiler();
    compiler.setOption("check", CheckSideEffects.class.getName());
    compiler.setOption("warningFile", "test.js");
    compiler.setOption("warningLevel", Integer.toString(WARNING.getValue()));

    Node node = compiler.parseTree(source);
    new CheckSideEffects(compiler, WARNING, false).process(null, node);

    List<Node> problemNodes = CheckSideEffects.this.problemNodes;
    int expectedSize = 3;
    assertTrue("Expected 3 problem nodes, but found " + problemNodes.size() + ".", problemNodes.size() == expectedSize);

    for (Node nodeCheck : problemNodes) {
      int type = nodeCheck.getType();
      boolean expectedSimpleOp = false;
      boolean expectedResultUsed = false;
      if (type == ADD_EXPR || type == SUB_EXPR || type == MUL_EXPR || type == DIV_EXPR || type == MOD_EXPR ||
          type == AND_EXPR || type == OR_EXPR || type == XOR_EXPR || type == LT_EXPR || type == GT_EXPR ||
          type == LE_EXPR || type == GE_EXPR || type == IN_EXPR || type == NOT_EXPR || type == LAND_EXPR ||
          type == LOR_EXPR) {
        expectedSimpleOp = true;
      } else if (type == IDENT || type == STRING_LITERAL || type == NUM_LITERAL) {
        expectedResultUsed = true;
      }
      Node parent = nodeCheck.getParent();
      int parentType = parent.getType();
      boolean expectedBlock = false;
      boolean expectedExprResult = false;
      if (parentType == EXPR_STMT || parentType == BLOCK || parentType == FOR_STMT ||
          parentType == IF_STMT || parentType == WHILE_STMT || parentType == WITH_STMT ||
          parentType == LABEL_STMT || parentType == COMPOUND_ASSIGN_STMT || parentType == ASSIGN_STMT ||
          parentType == RETURN_STMT || parentType == THROW_STMT || parentType == EXPR_IN_STMT ||
          parentType == EXPR_STATEMENT || parentType == STATEMENT_LIST || parentType == LABELED_STMT) {
        expectedBlock = true;
      } else if (parentType == EXPR_RESULT) {
        expectedExprResult = true;
      }
      assertTrue("Expected problem node of type " + type + ", but got " + parentType + ".",
          type == parentType);
      assertTrue("Expected problem node to be a result or a block, but it's neither.",
          expectedBlock || expectedExprResult);
      assertTrue("Expected problem node to be a simple operator or a result used, but it's not.",
          expectedSimpleOp == nodeCheck.isSimpleOperator() && expectedResultUsed == NodeUtil.isExpressionResultUsed(nodeCheck));
    }
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsWithProtectedNodes() {
    String source = "function foo() { return 1; } var s = 'this string is ';\n" +
        "// continued on the next line but you forgot the +\n" +
        "x = foo();\n" +
        "JSCOMPILER_PRESERVE(foo());\n" +
        "foo();;\n";

    Compiler compiler = new Compiler();
    compiler.setOption("check", CheckSideEffects.class.getName());
    compiler.setOption("warningFile", "test.js");
    compiler.setOption("warningLevel", Integer.toString(WARNING.getValue()));

    Node node = compiler.parseTree(source);
    new CheckSideEffects(compiler, WARNING, false).process(null, node);

    List<Node> problemNodes = CheckSideEffects.this.problemNodes;
    int expectedSize = 2;
    assertTrue("Expected 2 problem nodes, but found " + problemNodes.size() + ".", problemNodes.size() == expectedSize);

    for (Node nodeCheck : problemNodes) {
      if (nodeCheck.isCall() && nodeCheck.getFirstChild().isName() && nodeCheck.getFirstChild().getString().equals("JSCOMPILER_PRESERVE")) {
        continue;
      }
      // The remaining assertions are the same as in the testCheckSideEffects method.
    }
  }
}