import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String input = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'c': 2}" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b.c;" +
        "  return x + y;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "testVarPrefix");
    Node root = new Node(new Token(0, 0, Token.SCRIPT_START));
    Node externs = new Node(new Token(0, 0, Token.SCRIPT_START));
    compiler.compile(externs, input, root);
    inlineObjectLiterals.process(externs, root);

    // Check that the original object literal variables have been replaced
    // with individual variables as expected.
    Node foo = root.getFirstChild();
    Node objVariable = foo.getChildAtIndex(1);
    Node aVariable = findVariableByName("a", objVariable.getChildAtIndex(1).getChildAtIndex(1));
    Node bVariable = findVariableByName("b", objVariable.getChildAtIndex(1));
    Node cVariable = findVariableByName("c", bVariable.getChildAtIndex(1));
    assertEquals(Node.IS_VAR, aVariable.getNodeType());
    assertEquals(Node.IS_VAR, bVariable.getNodeType());
    assertEquals(Node.IS_VAR, cVariable.getNodeType());

    // Check that the 'x' and 'y' assignments still work as expected.
    Node xAssignment = findAssignmentToVariableByName("x", foo.getLastChild());
    Node yAssignment = findAssignmentToVariableByName("y", foo.getLastChild());
    Node expectedX = findVariableByName("a", aVariable);
    Node expectedY = findVariableByName("c", cVariable);
    assertEquals(expectedX, xAssignment.getFirstChild());
    assertEquals(expectedY, yAssignment.getFirstChild());

    // Check that the return statement still evaluates to the expected value.
    Node returnStatement = foo.getLastChild();
    Node expectedReturn = IR.add(
        IR.name("a"),
        IR.name("c"));
    assertEquals(expectedReturn, returnStatement);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedProperties() {
    String input = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: {" +
        "        c: 1," +
        "        d: 2" +
        "      }" +
        "    }," +
        "    e: 3" +
        "  };" +
        "  var x = obj.a.b.c;" +
        "  var y = obj.e;" +
        "  return x + y;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "testVarPrefix");
    Node root = new Node(new Token(0, 0, Token.SCRIPT_START));
    Node externs = new Node(new Token(0, 0, Token.SCRIPT_START));
    compiler.compile(externs, input, root);
    inlineObjectLiterals.process(externs, root);

    // Check that the original object literal variables have been replaced
    // with individual variables as expected.
    Node foo = root.getFirstChild();
    Node objVariable = foo.getChildAtIndex(1);
    Node aVariable = findVariableByName("a", objVariable.getChildAtIndex(1));
    Node bVariable = findVariableByName("b", aVariable.getChildAtIndex(1));
    Node cVariable = findVariableByName("c", bVariable.getChildAtIndex(1));
    Node eVariable = findVariableByName("e", objVariable.getChildAtIndex(1));
    assertEquals(Node.IS_VAR, aVariable.getNodeType());
    assertEquals(Node.IS_VAR, bVariable.getNodeType());
    assertEquals(Node.IS_VAR, cVariable.getNodeType());
    assertEquals(Node.IS_VAR, eVariable.getNodeType());

    // Check that the 'x' and 'y' assignments still work as expected.
    Node xAssignment = findAssignmentToVariableByName("x", foo.getLastChild());
    Node yAssignment = findAssignmentToVariableByName("y", foo.getLastChild());
    Node expectedX = findVariableByName("c", cVariable);
    Node expectedY = findVariableByName("e", eVariable);
    assertEquals(expectedX, xAssignment.getFirstChild());
    assertEquals(expectedY, yAssignment.getFirstChild());

    // Check that the return statement still evaluates to the expected value.
    Node returnStatement = foo.getLastChild();
    Node expectedReturn = IR.add(
        IR.name("c"),
        IR.name("e"));
    assertEquals(expectedReturn, returnStatement);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String input = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: obj" +
        "    }" +
        "  };" +
        "  return obj.a.b;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "testVarPrefix");
    Node root = new Node(new Token(0, 0, Token.SCRIPT_START));
    Node externs = new Node(new Token(0, 0, Token.SCRIPT_START));
    compiler.compile(externs, input, root);
    inlineObjectLiterals.process(externs, root);

    // Check that the original object literal variables have been replaced
    // with individual variables as expected.
    Node foo = root.getFirstChild();
    Node objVariable = foo.getChildAtIndex(1);
    Node aVariable = findVariableByName("a", objVariable.getChildAtIndex(1));
    Node bVariable = findVariableByName("b", aVariable.getChildAtIndex(1));
    assertEquals(Node.IS_VAR, aVariable.getNodeType());
    assertEquals(Node.IS_VAR, bVariable.getNodeType());

    // Check that the return statement still evaluates to the expected value.
    Node returnStatement = foo.getLastChild();
    Node expectedReturn = findVariableByName("b", bVariable);
    assertEquals(expectedReturn, returnStatement);
  }

  @Test
  public void testInlineObjectLiteralsWithCyclicReferences() {
    String input = "function foo() {" +
        "  var objA = {" +
        "    a: {'b': objB}," +
        "  };" +
        "  var objB = {" +
        "    b: {'a': objA}," +
        "  };" +
        "  return objA.a.b.b;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "testVarPrefix");
    Node root = new Node(new Token(0, 0, Token.SCRIPT_START));
    Node externs = new Node(new Token(0, 0, Token.SCRIPT_START));
    compiler.compile(externs, input, root);
    inlineObjectLiterals.process(externs, root);

    // Check that the original object literal variables have been replaced
    // with individual variables as expected.
    Node foo = root.getFirstChild();
    Node objAVariable = findVariableByName("a", foo.getChildAtIndex(1));
    Node objBVariable = findVariableByName("b", foo.getChildAtIndex(1));
    Node bVariable = findVariableByName("b", objBVariable.getChildAtIndex(1));
    Node aVariable = findVariableByName("a", objAVariable.getChildAtIndex(1));
    assertEquals(Node.IS_VAR, aVariable.getNodeType());
    assertEquals(Node.IS_VAR, bVariable.getNodeType());
    assertEquals(Node.IS_VAR, objAVariable.getNodeType());
    assertEquals(Node.IS_VAR, objBVariable.getNodeType());

    // Check that the return statement still evaluates to the expected value.
    Node returnStatement = foo.getLastChild();
    Node expectedReturn = findVariableByName("b", bVariable);
    assertEquals(expectedReturn, returnStatement);
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionDefinitions() {
    String input = "function foo() {" +
        "  function bar() {" +
        "  }" +
        "  var obj = {" +
        "    a: {b: 1}" +
        "  };" +
        "  bar.prototype.baz = function() {" +
        "    return this.b;" +
        "  }" +
        "  return obj.a.b;" +
        "}" +
        "function bar() {" +
        "}" +
        "bar.prototype.baz = function() {" +
        "  return 2;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "testVarPrefix");
    Node root = new Node(new Token(0, 0, Token.SCRIPT_START));
    Node externs = new Node(new Token(0, 0, Token.SCRIPT_START));
    compiler.compile(externs, input, root);
    inlineObjectLiterals.process(externs, root);

    // Check that the original object literal variables have been replaced
    // with individual variables as expected.
    Node foo = root.getFirstChild();
    Node objVariable = findVariableByName("obj", foo.getChildAtIndex(1));
    Node aVariable = findVariableByName("a", objVariable.getChildAtIndex(1));
    Node bVariable = findVariableByName("b", aVariable.getChildAtIndex(1));
    Node barFunction = findFunctionByName("bar", foo.getChildAtIndex(1));
    assertEquals(Node.IS_VAR, aVariable.getNodeType());
    assertEquals(Node.IS_VAR, bVariable.getNodeType());
    assertEquals(Node.IS_FUNCTION, barFunction.getNodeType());

    // Check that the 'return' statement still evaluates to the expected value.
    Node returnStatement = foo.getLastChild();
    Node expectedReturn = findVariableByName("b", bVariable);
    assertEquals(expectedReturn, returnStatement);
  }

  // Helper methods

  private Node findVariableByName(String name, Node node) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isName() && child.getString().equals(name)) {
        return child;
      }
      Node result = findVariableByName(name, child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findFunctionByName(String name, Node node) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isFunction()) {
        Node functionName = child.getFirstChild();
        if (functionName.getString().equals(name)) {
          return child;
        }
      }
      Node result = findFunctionByName(name, child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findAssignmentToVariableByName(String name, Node node) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.isAssign()) {
        Node leftHandSide = child.getFirstChild();
        if (leftHandSide.isName() && leftHandSide.getString().equals(name)) {
          return child;
        }
      }
      Node result = findAssignmentToVariableByName(name, child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}