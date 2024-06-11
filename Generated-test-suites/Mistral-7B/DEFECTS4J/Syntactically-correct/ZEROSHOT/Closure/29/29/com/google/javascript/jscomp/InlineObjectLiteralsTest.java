import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.IR;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private static class MockCompiler extends AbstractCompiler {
    @Override
    public CompilerPass getPass(String passName) {
      if ("InlineObjectLiterals".equals(passName)) {
        return new InlineObjectLiterals(this, mock(Supplier.class));
      }
      return null;
    }
  }

  @org.junit.Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList("x = {a: 1, b: 1}"), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was replaced with individual variables
    Map<String, Node> expectedVariables = ImmutableMap.of(
        "a", root.findFirstChildByType(Node.VAR),
        "b", root.findFirstChildByType(Node.VAR)
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      Node variable = entry.getValue();
      String variableName = variable.getString();
      Node objectLiteral = root.findFirstChildByType(Node.VAR, variableName);
      Node objectLiteralValue = objectLiteral.getFirstChild();
      assertEquals(variable, objectLiteralValue);
    }

    // Check that the variables were assigned the correct values
    Map<String, Object> expectedValues = ImmutableMap.of(
        "a", 1,
        "b", 1
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      String variableName = entry.getKey();
      Node variable = entry.getValue();
      Node initializer = variable.getFirstChild();
      Object value = initializer.isNumber() ? initializer.getNumberValue() : initializer.getString();
      assertEquals(expectedValues.get(variableName), value);
    }
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithFunctionCalls() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList("x = function() { return { a: 1, b: 1 } }; x();"), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was replaced with individual variables
    Map<String, Node> expectedVariables = ImmutableMap.of(
        "a", root.findFirstChildByType(Node.VAR),
        "b", root.findFirstChildByType(Node.VAR)
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      Node variable = entry.getValue();
      String variableName = variable.getString();
      Node objectLiteral = null;
      if (variable.getParent().isFunctionCall()) {
        objectLiteral = variable.getParent().getFirstChild();
      } else {
        objectLiteral = root.findFirstChildByType(Node.VAR, variableName);
      }
      Node objectLiteralValue = objectLiteral.getFirstChild();
      assertEquals(variable, objectLiteralValue);
    }

    // Check that the variables were assigned the correct values
    Map<String, Object> expectedValues = ImmutableMap.of(
        "a", 1,
        "b", 1
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      String variableName = entry.getKey();
      Node variable = entry.getValue();
      Node initializer = variable.getFirstChild();
      Object value = initializer.isNumber() ? initializer.getNumberValue() : initializer.getString();
      assertEquals(expectedValues.get(variableName), value);
    }
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithGetterSetter() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        "x = { get a() { return 1; }, set a(value) { this.b = value; } }",
        "x.a = 1;"
    ), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was replaced with individual variables
    Map<String, Node> expectedVariables = ImmutableMap.of(
        "b", root.findFirstChildByType(Node.VAR)
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      Node variable = entry.getValue();
      String variableName = variable.getString();
      Node objectLiteral = root.findFirstChildByType(Node.VAR, variableName);
      Node objectLiteralValue = objectLiteral.getFirstChild();
      assertEquals(variable, objectLiteralValue);
    }

    // Check that the variables were assigned the correct values
    Map<String, Object> expectedValues = ImmutableMap.of(
        "b", 1
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      String variableName = entry.getKey();
      Node variable = entry.getValue();
      Node initializer = variable.getFirstChild();
      Object value = initializer.isNumber() ? initializer.getNumberValue() : initializer.getString();
      assertEquals(expectedValues.get(variableName), value);
    }
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithSelfReferentialAssignment() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        "x = { a: { b: x } }"
    ), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was not replaced
    Node objectLiteral = root.findFirstChildByType(Node.VAR, "x");
    Node objectLiteralValue = objectLiteral.getFirstChild();
    assertEquals(objectLiteral, objectLiteralValue);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithNonInlineableObject() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        "x = { a: function() { return 1; } }"
    ), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was not replaced
    Node objectLiteral = root.findFirstChildByType(Node.VAR, "x");
    Node objectLiteralValue = objectLiteral.getFirstChild();
    assertEquals(objectLiteral, objectLiteralValue);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList("x = {}"), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was not replaced
    Node objectLiteral = root.findFirstChildByType(Node.VAR, "x");
    Node objectLiteralValue = objectLiteral.getFirstChild();
    assertEquals(objectLiteral, objectLiteralValue);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithIdentifier() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList("x = { a: 1, b: 1 }; y = x"), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was replaced with individual variables
    Map<String, Node> expectedVariables = ImmutableMap.of(
        "a", root.findFirstChildByType(Node.VAR),
        "b", root.findFirstChildByType(Node.VAR)
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      Node variable = entry.getValue();
      String variableName = variable.getString();
      Node objectLiteral = root.findFirstChildByType(Node.VAR, variableName);
      Node objectLiteralValue = objectLiteral.getFirstChild();
      assertEquals(variable, objectLiteralValue);
    }

    // Check that the variables were assigned the correct values
    Map<String, Object> expectedValues = ImmutableMap.of(
        "a", 1,
        "b", 1
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      String variableName = entry.getKey();
      Node variable = entry.getValue();
      Node initializer = variable.getFirstChild();
      Object value = initializer.isNumber() ? initializer.getNumberValue() : initializer.getString();
      assertEquals(expectedValues.get(variableName), value);
    }

    // Check that the identifier 'x' still refers to the object literal
    Node x = root.findFirstChildByType(Node.VAR, "x");
    Node xValue = x.getFirstChild();
    Node expectedXValue = Node.objectLiteral(
        Node.property(
            Node.name("a"),
            Node.number(1)
        ),
        Node.property(
            Node.name("b"),
            Node.number(1)
        )
    );
    assertEquals(expectedXValue, xValue);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithIdentifiersAndFunctionCall() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        "x = function() { return { a: 1, b: 1 }; }(); y = x.a"), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was replaced with individual variables
    Map<String, Node> expectedVariables = ImmutableMap.of(
        "a", root.findFirstChildByType(Node.VAR),
        "b", root.findFirstChildByType(Node.VAR)
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      Node variable = entry.getValue();
      String variableName = variable.getString();
      Node objectLiteral = root.findFirstChildByType(Node.VAR, variableName);
      Node objectLiteralValue = objectLiteral.getFirstChild();
      assertEquals(variable, objectLiteralValue);
    }

    // Check that the variables were assigned the correct values
    Map<String, Object> expectedValues = ImmutableMap.of(
        "a", 1,
        "b", 1
    );
    for (Map.Entry<String, Node> entry : expectedVariables.entrySet()) {
      String variableName = entry.getKey();
      Node variable = entry.getValue();
      Node initializer = variable.getFirstChild();
      Object value = initializer.isNumber() ? initializer.getNumberValue() : initializer.getString();
      assertEquals(expectedValues.get(variableName), value);
    }

    // Check that the identifier 'y' still refers to the value of 'a'
    Node y = root.findFirstChildByType(Node.VAR, "y");
    Node yValue = y.getFirstChild();
    Node expectedYValue = root.findFirstChildByType(Node.VAR, "a");
    assertEquals(expectedYValue, yValue);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsWithSelfAssignedObject() {
    AbstractCompiler compiler = new MockCompiler();
    Scope scope = new Scope(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.script(Lists.newArrayList(
        "x = { a: x };"), scope);

    InlineObjectLiterals inlineObjectLiterals =
        (InlineObjectLiterals) compiler.getPass("InlineObjectLiterals");
    inlineObjectLiterals.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse();

    // Check that the object literal was not replaced
    Node objectLiteral = root.findFirstChildByType(Node.VAR, "x");
    Node objectLiteralValue = objectLiteral.getFirstChild();
    assertEquals(objectLiteral, objectLiteralValue);
  }
}