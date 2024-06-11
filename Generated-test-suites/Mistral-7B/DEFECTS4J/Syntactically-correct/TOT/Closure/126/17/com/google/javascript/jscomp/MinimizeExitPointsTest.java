package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

/**
 * Test for MinimizeExitPoints class.
 */
@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  private static final Compiler compiler = new Compiler();
  private static final NodeFactory nodeFactory = new NodeFactory();

  @Test
  public void testMinimizeExitPoints() {
    String jsCode = "function foo() {"
        + "if (true) return;"
        + "if (false) return;"
        + "};";

    Node root = nodeFactory.parse(jsCode);
    Node externs = nodeFactory.parse("{}");

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    Node minimizedCode = root.getFirstChild();

    // Expect the number of exits to be minimized to one.
    int exitPointsBefore = countExitPoints(root);
    int exitPointsAfter = countExitPoints(minimizedCode);
    assertTrue("Expected to find one less exit point in the minimized code.",
        exitPointsAfter < exitPointsBefore);

    Node actualIf = minimizedCode.getFirstChild();
    Node expectedIf = nodeFactory.fromJsonString("{"
        + "  'type': 'IfStatement',"
        + "  'condition': {"
        + "    'type': 'BinaryOperator',"
        + "    'left': {"
        + "      'type': 'Identifier',"
        + "      'name': 'true'"
        + "    },"
        + "    'operator': 'LogicalOrOperator',"
        + "    'right': {"
        + "      'type': 'Identifier',"
        + "      'name': 'false'"
        + "    }"
        + "  },"
        + "  'consequent': {"
        + "    'type': 'BlockStatement',"
        + "    'statements': []"
        + "  },"
        + "  'alternate': null"
        + "}");

    // Expect the actual if statement to match the expected.
    Node actualIfStatement = NodeUtil.getStatement(minimizedCode, 0);
    assertTrue("Expected the first statement to be an if statement.",
        actualIfStatement != null);
    assertTrue("Expected the first statement to match the expected if statement.",
        NodeUtil.isDeeplyEqual(actualIfStatement, expectedIf));
  }

  @Test
  public void testMinimizeExitPointsWithLabel() {
    String jsCode = "function foo() {"
        + "label1: for (var i = 0; i < 5; i++) {"
        + "  if (i == 3) break label1;"
        + "  ;"
        + "}"
        + "};";

    Node root = nodeFactory.parse(jsCode);
    Node externs = nodeFactory.parse("{}");

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    Node minimizedCode = root.getFirstChild();

    // Expect the number of exits to be minimized to one.
    int exitPointsBefore = countExitPoints(root);
    int exitPointsAfter = countExitPoints(minimizedCode);
    assertTrue("Expected to find one less exit point in the minimized code.",
        exitPointsAfter < exitPointsBefore);

    // Expect the 'break label1' statement to be removed.
    Node actualBreak = findNodeByType(minimizedCode, Token.BREAK);
    assertFalse("Expected the 'break label1' statement to be removed.",
        actualBreak != null);
  }

  @Test
  public void testMinimizeExitPointsWithEmptyBlock() {
    String jsCode = "function foo() {"
        + "if (true) {"
        + "}"
        + "return;"
        + "};";

    Node root = nodeFactory.parse(jsCode);
    Node externs = nodeFactory.parse("{}");

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    Node minimizedCode = root.getFirstChild();

    // Expect the number of exits to be minimized to one.
    int exitPointsBefore = countExitPoints(root);
    int exitPointsAfter = countExitPoints(minimizedCode);
    assertTrue("Expected to find one less exit point in the minimized code.",
        exitPointsAfter < exitPointsBefore);

    // Expect the empty block to be removed.
    Node actualBlock = findNodeByType(minimizedCode, Token.BLOCK);
    assertFalse("Expected the empty block to be removed.",
        actualBlock != null);
  }

  @Test
  public void testMinimizeExitPointsWithComplexControlFlow() {
    String jsCode = "function foo() {"
        + "if (true) {"
        + "  if (false) {"
        + "    return;"
        + "  } else {"
        + "    if (true) {"
        + "      return;"
        + "    } else {"
        + "      return;"
        + "    }"
        + "  }"
        + "}"
        + "};";

    Node root = nodeFactory.parse(jsCode);
    Node externs = nodeFactory.parse("{}");

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(externs, root);

    Node minimizedCode = root.getFirstChild();

    // Expect the number of exits to be minimized to two.
    int exitPointsBefore = countExitPoints(root);
    int exitPointsAfter = countExitPoints(minimizedCode);
    assertTrue("Expected to find the same number of exit points in the minimized code.",
        exitPointsAfter == exitPointsBefore);

    // Expect the control flow to be preserved.
    Node actualIf1 = findNodeByType(minimizedCode, Token.IF);
    Node actualIf2 = findNodeByType(findNodeByType(actualIf1.getNext(),
        Token.ELSE), Token.IF);
    Node actualReturn1 = findNodeByType(findNodeByType(actualIf1.getNext(),
        Token.ELSE), Token.RETURN);
    Node actualReturn2 = findNodeByType(actualIf2.getNext(), Token.RETURN);

    Node expectedIf1 = nodeFactory.fromJsonString("{"
        + "  'type': 'IfStatement',"
        + "  'condition': {"
        + "    'type': 'BooleanLiteral',"
        + "    'value': true"
        + "  },"
        + "  'consequent': {"
        + "    'type': 'BlockStatement',"
        + "    'statements': ["
        + "      {"
        + "        'type': 'IfStatement',"
        + "        'condition': {"
        + "          'type': 'BooleanLiteral',"
        + "          'value': false"
        + "        },"
        + "        'consequent': {"
        + "          'type': 'BlockStatement',"
        + "          'statements': ["
        + "            {"
        + "              'type': 'ReturnStatement'"
        + "            }"
        + "          ]"
        + "        },"
        + "        'alternate': {"
        + "          'type': 'BlockStatement',"
        + "          'statements': ["
        + "            {"
        + "              'type': 'IfStatement',"
        + "              'condition': {"
        + "                'type': 'BooleanLiteral',"
        + "                'value': true"
        + "              },"
        + "              'consequent': {"
        + "                'type': 'BlockStatement',"
        + "                'statements': ["
        + "                  {"
        + "                    'type': 'ReturnStatement'"
        + "                  }"
        + "                ]"
        + "              },"
        + "              'alternate': {"
        + "                'type': 'BlockStatement',"
        + "                'statements': ["
        + "                  {"
        + "                    'type': 'ReturnStatement'"
        + "                  }"
        + "                ]"
        + "              }"
        + "            }"
        + "          ]"
        + "        }"
        + "      }"
        + "    ]"
        + "  }"
        + "}");

    Node expectedIf2 = nodeFactory.fromJsonString("{"
        + "  'type': 'IfStatement',"
        + "  'condition': {"
        + "    'type': 'BooleanLiteral',"
        + "    'value': true"
        + "  },"
        + "  'consequent': {"
        + "    'type': 'BlockStatement',"
        + "    'statements': ["
        + "      {"
        + "        'type': 'ReturnStatement'"
        + "      }"
        + "    ]"
        + "  }"
        + "}");

    // Expect the actual if statements to match the expected.
    Node actualIf1Statement = NodeUtil.getStatement(minimizedCode, 0);
    assertTrue("Expected the first statement to be an if statement.",
        actualIf1Statement != null);
    assertTrue("Expected the first if statement to match the expected if statement.",
        NodeUtil.isDeeplyEqual(actualIf1Statement, expectedIf1));

    Node actualIf2Statement = NodeUtil.getStatement(actualIf1Statement, 0);
    assertTrue("Expected the second statement to be an if statement.",
        actualIf2Statement != null);
    assertTrue("Expected the second if statement to match the expected if statement.",
        NodeUtil.isDeeplyEqual(actualIf2Statement, expectedIf2));
  }

  private static Node findNodeByType(Node node, int type) {
    for (Node child = node.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.getType() == type) {
        return child;
      }
      Node result = findNodeByType(child, type);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private static int countExitPoints(Node node) {
    int exitPoints = 0;
    for (Node child = node.getFirstChild(); child != null;
        child = child.getNext()) {
      if (child.getType() == Token.RETURN) {
        exitPoints++;
      } else if (child.getType() == Token.BREAK || child.getType() == Token.CONTINUE) {
        Node label = child.getPrevious();
        if (label != null && label.isLabel()) {
          exitPoints++;
        }
      }
      exitPoints += countExitPoints(child);
    }
    return exitPoints;
  }
}