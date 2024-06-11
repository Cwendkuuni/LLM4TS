import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiteralWithSingleProperty() {
    Node rootNode = new Node(Token.BLOCK, Lists.newArrayList());
    Node externsNode = new Node(Token.BLOCK, Lists.newArrayList());

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new StaticCompilerPass(), () -> "test_var_");

    inlineObjectLiterals.process(externsNode, rootNode);

    // Given: An object literal with a single property
    Node objectLiteralNode = new Node(Token.OBJECTLIT, Lists.newArrayList());
    Node propertyNameNode = new Node(Token.STRING_LITERAL, "name");
    Node propertyValueNode = new Node(Token.NUMBER_LITERAL, "John Doe");
    objectLiteralNode.addChildToBack(new Node(Token.ADD_PROPERTY, propertyNameNode, propertyValueNode));

    Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.NAME, "obj"), objectLiteralNode);
    rootNode.getLastChild().addChildToFront(assignmentNode);

    // When: The InlineObjectLiterals pass is applied
    inlineObjectLiterals.process(externsNode, rootNode);

    // Then: The object literal is replaced with individual variable assignments
    Node expectedVar1 = new Node(Token.VAR, new Node(Token.NAME, "name"), null);
    Node expectedVar2 = new Node(Token.ASSIGN, expectedVar1, propertyValueNode);
    Node expectedStatement = new Node(Token.STATEMENT, expectedVar2);

    Node actualVar1 = null;
    Node actualVar2 = null;
    Node actualStatement = null;

    for (Node child = rootNode.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.VAR && child.getFirstChild().getString().equals("name")) {
        actualVar1 = child;
      }
      if (child.getType() == Token.STATEMENT && child.getFirstChild().getType() == Token.ASSIGN) {
        actualVar2 = child.getFirstChild().getFirstChild();
        actualStatement = child;
      }
    }

    // Assert: The expected and actual statements match
    assertEquals("Unexpected variable declaration for 'name'", expectedVar1, actualVar1);
    assertEquals("Unexpected variable assignment for 'name'", expectedVar2, actualVar2);
    assertEquals("Unexpected statement for 'name'", expectedStatement.toString(), actualStatement.toString());
  }

  @Test
  public void testInlineObjectLiteralWithMultipleProperties() {
    Node rootNode = new Node(Token.BLOCK, Lists.newArrayList());
    Node externsNode = new Node(Token.BLOCK, Lists.newArrayList());

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new StaticCompilerPass(), () -> "test_var_");

    inlineObjectLiterals.process(externsNode, rootNode);

    // Given: An object literal with multiple properties
    Node objectLiteralNode = new Node(Token.OBJECTLIT, Lists.newArrayList());
    Node propertyName1Node = new Node(Token.STRING_LITERAL, "name1");
    Node propertyValue1Node = new Node(Token.NUMBER_LITERAL, "John Doe");
    Node propertyName2Node = new Node(Token.STRING_LITERAL, "age");
    Node propertyValue2Node = new Node(Token.NUMBER_LITERAL, "30");
    objectLiteralNode.addChildToBack(
        new Node(Token.ADD_PROPERTY, propertyName1Node, propertyValue1Node));
    objectLiteralNode.addChildToBack(
        new Node(Token.ADD_PROPERTY, propertyName2Node, propertyValue2Node));

    Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.NAME, "obj"), objectLiteralNode);
    rootNode.getLastChild().addChildToFront(assignmentNode);

    // When: The InlineObjectLiterals pass is applied
    inlineObjectLiterals.process(externsNode, rootNode);

    // Then: The object literal is replaced with individual variable assignments
    List<Node> expectedVars = Lists.newArrayList();
    List<Node> expectedAssignments = Lists.newArrayList();

    for (String propertyName : Lists.newArrayList("name1", "age")) {
      String variableName = "test_var_" + propertyName;
      Node expectedVar = new Node(Token.VAR, new Node(Token.NAME, variableName), null);
      Node expectedAssignment = new Node(Token.ASSIGN, expectedVar, null);

      expectedVars.add(expectedVar);
      expectedAssignments.add(expectedAssignment);
    }

    Node actualVars = null;
    Node actualAssignments = null;

    for (Node child = rootNode.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.VAR) {
        actualVars = child;
      }
      if (child.getType() == Token.BLOCK && child.getFirstChild().getType() == Token.STATEMENT) {
        Node statement = child.getFirstChild();
        if (statement.getType() == Token.ASSIGN) {
          actualAssignments = statement;
          break;
        }
      }
    }

    // Assert: The expected and actual variables and assignments match
    assertEquals("Unexpected number of variables", expectedVars.size(), actualVars.getChildCount());
    for (int i = 0; i < expectedVars.size(); i++) {
      Node expectedVar = expectedVars.get(i);
      Node actualVar = actualVars.getChildAtIndex(i);

      assertEquals("Unexpected variable declaration for '" + expectedVar.getFirstChild().getString() + "'",
          expectedVar, actualVar);
    }

    assertEquals("Unexpected number of assignments", expectedAssignments.size(), actualAssignments.getChildCount());
    for (int i = 0; i < expectedAssignments.size(); i++) {
      Node expectedAssignment = expectedAssignments.get(i);
      Node actualAssignment = actualAssignments.getChildAtIndex(i);

      assertEquals("Unexpected assignment for '" + expectedAssignment.getFirstChild().getString() + "'",
          expectedAssignment, actualAssignment);
    }
  }

  @Test
  public void testInlineObjectLiteralWithCyclicReference() {
    Node rootNode = new Node(Token.BLOCK, Lists.newArrayList());
    Node externsNode = new Node(Token.BLOCK, Lists.newArrayList());

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new StaticCompilerPass(), () -> "test_var_");

    inlineObjectLiterals.process(externsNode, rootNode);

    // Given: An object literal with a cyclic reference
    Node objectLiteralNode = new Node(Token.OBJECTLIT, Lists.newArrayList());
    Node propertyName1Node = new Node(Token.STRING_LITERAL, "name1");
    Node propertyValue1Node = new Node(Token.NAME, "name2");
    Node propertyName2Node = new Node(Token.STRING_LITERAL, "name2");
    Node propertyValue2Node = new Node(Token.NAME, "name1");
    objectLiteralNode.addChildToBack(
        new Node(Token.ADD_PROPERTY, propertyName1Node, propertyValue1Node));
    objectLiteralNode.addChildToBack(
        new Node(Token.ADD_PROPERTY, propertyName2Node, propertyValue2Node));

    Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.NAME, "obj"), objectLiteralNode);
    rootNode.getLastChild().addChildToFront(assignmentNode);

    // When: The InlineObjectLiterals pass is applied
    try {
      inlineObjectLiterals.process(externsNode, rootNode);
      fail("Expected an error for the given object literal");
    } catch (RuntimeException e) {
      // Then: An error is thrown due to the cyclic reference
    }
  }

  @Test
  public void testInlineObjectLiteralWithFunctionProperty() {
    Node rootNode = new Node(Token.BLOCK, Lists.newArrayList());
    Node externsNode = new Node(Token.BLOCK, Lists.newArrayList());

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        new StaticCompilerPass(), () -> "test_var_");

    inlineObjectLiterals.process(externsNode, rootNode);

    // Given: An object literal with a function property
    Node objectLiteralNode = new Node(Token.OBJECTLIT, Lists.newArrayList());
    Node propertyNameNode = new Node(Token.STRING_LITERAL, "func");
    Node propertyValueNode = new Node(Token.FUNCTION, Lists.newArrayList(), Lists.newArrayList());
    Node functionBodyNode = new Node(Token.BLOCK, Lists.newArrayList());
    Node functionReturnNode = new Node(Token.RETURN, new Node(Token.NUMBER_LITERAL, "42"));
    functionBodyNode.addChildToBack(functionReturnNode);
    propertyValueNode.addChildToBack(functionBodyNode);
    objectLiteralNode.addChildToBack(
        new Node(Token.ADD_PROPERTY, propertyNameNode, propertyValueNode));

    Node assignmentNode = new Node(Token.ASSIGN, new Node(Token.NAME, "obj"), objectLiteralNode);
    rootNode.getLastChild().addChildToFront(assignmentNode);

    // When: The InlineObjectLiterals pass is applied
    inlineObjectLiterals.process(externsNode, rootNode);

    // Then: The object literal is replaced with individual variable assignments
    List<Node> expectedVars = Lists.newArrayList();
    List<Node> expectedAssignments = Lists.newArrayList();

    for (String propertyName : Lists.newArrayList("func")) {
      String variableName = "test_var_" + propertyName;
      Node expectedVar = new Node(Token.VAR, new Node(Token.NAME, variableName), null);
      Node expectedAssignment = new Node(Token.ASSIGN, expectedVar, null);

      expectedVars.add(expectedVar);
      expectedAssignments.add(expectedAssignment);
    }

    Node actualVars = null;
    Node actualAssignments = null;

    for (Node child = rootNode.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.VAR) {
        actualVars = child;
      }
      if (child.getType() == Token.BLOCK && child.getFirstChild().getType() == Token.STATEMENT) {
        Node statement = child.getFirstChild();
        if (statement.getType() == Token.ASSIGN) {
          actualAssignments = statement;
          break;
        }
      }
    }

    // Assert: The expected and actual variables and assignments match
    assertEquals("Unexpected number of variables", expectedVars.size(), actualVars.getChildCount());
    for (int i = 0; i < expectedVars.size(); i++) {
      Node expectedVar = expectedVars.get(i);
      Node actualVar = actualVars.getChildAtIndex(i);

      assertEquals("Unexpected variable declaration for '" + expectedVar.getFirstChild().getString() + "'",
          expectedVar, actualVar);
    }

    assertEquals("Unexpected number of assignments", expectedAssignments.size(), actualAssignments.getChildCount());
    for (int i = 0; i < expectedAssignments.size(); i++) {
      Node expectedAssignment = expectedAssignments.get(i);
      Node actualAssignment = actualAssignments.getChildAtIndex(i);

      // Assert: The function property is not expanded, as it is not assignable
      if (expectedAssignment.getFirstChild().getString().equals("func")) {
        assertEquals("Unexpected assignment for 'func'", expectedAssignment, actualAssignment);
        continue;
      }

      assertEquals("Unexpected assignment for '" + expectedAssignment.getFirstChild().getString() + "'",
          expectedAssignment, actualAssignment);
    }
  }
}