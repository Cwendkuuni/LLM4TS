import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Token;
import com.google.common.collect.ImmutableList;

public class FunctionRewriterTest {

  private Compiler compiler;
  private FunctionRewriter functionRewriter;

  @Before
  public void setup() {
    compiler = new Compiler();
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testEmptyFunctionReducer() {
    String testCode = "function Test() {}";
    Node root = compiler.parse(testCode);
    functionRewriter.process(compiler.getExterns(), root);
    Node testPrototype = compiler.getExterns().getFirstChild();
    Node testMethod = testPrototype.getLastChild();

    Node expectedHelperNode = compiler.parseSyntheticCode(
        "FunctionRewriterTest:helper", FunctionRewriter.EMPTY_FUNCTION_REDUCER + ":helper");
    Node expectedHelperRoot = expectedHelperNode.removeFirstChild();

    assertEquals(expectedHelperRoot, testMethod);
  }

  @Test
  public void testIdentityReducer() {
    String testCode = "function Test() {return this}";
    Node root = compiler.parse(testCode);
    functionRewriter.process(compiler.getExterns(), root);
    Node testPrototype = compiler.getExterns().getFirstChild();
    Node testMethod = testPrototype.getLastChild();

    Node expectedHelperNode = compiler.parseSyntheticCode(
        "FunctionRewriterTest:helper", FunctionRewriter.IDENTITY_REDUCER + ":helper");
    Node expectedHelperRoot = expectedHelperNode.removeFirstChild();

    assertEquals(expectedHelperRoot, testMethod);
  }

  @Test
  public void testIdentityReducerWithArgument() {
    String testCode = "function Test(x) {return x}";
    Node root = compiler.parse(testCode);
    functionRewriter.process(compiler.getExterns(), root);
    Node testPrototype = compiler.getExterns().getFirstChild();
    Node testMethod = testPrototype.getLastChild();

    Node expectedHelperNode = compiler.parseSyntheticCode(
        "FunctionRewriterTest:helper", FunctionRewriter.IDENTITY_REDUCER + ":helper");
    Node expectedHelperRoot = expectedHelperNode.removeFirstChild();

    Node argumentNode = testMethod.getFirstChild().getNext();

    assertEquals(expectedHelperRoot, testMethod.getLastChild());
    assertEquals(argumentNode, testMethod.getLastChild().getFirstChild());
  }

  @Test
  public void testReturnConstantReducer() {
    String testCode = "function Test() {return 10}";
    Node root = compiler.parse(testCode);
    functionRewriter.process(compiler.getExterns(), root);
    Node testPrototype = compiler.getExterns().getFirstChild();
    Node testMethod = testPrototype.getLastChild();

    Node expectedHelperNode = compiler.parseSyntheticCode(
        "FunctionRewriterTest:helper", FunctionRewriter.RETURN_CONSTANT_REDUCER + ":helper");
    Node expectedHelperRoot = expectedHelperNode.removeFirstChild();

    assertEquals(expectedHelperRoot, testMethod);
  }

  @Test
  public void testReturnConstantReducerWithArgument() {
    String testCode = "function Test() {return 10 + x}";
    Node root = compiler.parse(testCode);
    Node argumentNode = root.getFirstChild().getNext();

    functionRewriter.process(compiler.getExterns(), root);
    Node testPrototype = compiler.getExterns().getFirstChild();
    Node testMethod = testPrototype.getLastChild();

    Node expectedHelperNode = compiler.parseSyntheticCode(
        "FunctionRewriterTest:helper", FunctionRewriter.RETURN_CONSTANT_REDUCER + ":helper");
    Node expectedHelperRoot = expectedHelperNode.removeFirstChild();

    assertEquals(expectedHelperRoot, testMethod);
    assertEquals(argumentNode, testMethod.getFirstChild());
  }

  @Test
  public void testGetterReducer() {
    String testCode = "function Test() {" +
        "get A() {" +
        "  return this.a_;" +
        "}";
    Node root = compiler.parse(testCode);
    functionRewriter.process(compiler.getExterns(), root);
    Node testPrototype = compiler.getExterns().getFirstChild();
    Node getterNode = testPrototype.getLastChild();

    Node expectedHelperNode = compiler.parseSyntheticCode(
        "FunctionRewriterTest:helper", FunctionRewriter.GETTER_REDUCER + ":helper");
    Node expectedHelperRoot = expectedHelperNode.removeFirstChild();

    assertEquals(expectedHelperRoot, getterNode);
  }

  @Test
  public void testSetterReducer() {
    String testCode = "function Test() {" +
        "set A(value) {" +
        "  this.a_ = value;" +
        "}";
    Node root = compiler.parse(testCode);
    functionRewriter.process(compiler.getExterns(), root);
    Node testPrototype = compiler.getExterns().getFirstChild();
    Node setterNode = testPrototype.getLastChild();

    Node expectedHelperNode = compiler.parseSyntheticCode(
        "FunctionRewriterTest:helper", FunctionRewriter.SETTER_REDUCER + ":helper");
    Node expectedHelperRoot = expectedHelperNode.removeFirstChild();

    assertEquals(expectedHelperRoot, setterNode);
  }

  @Test
  public void testMultipleReducers() {
    String testCode = "function Test() {" +
        "get A() {" +
        "  return this.a_;" +
        "}" +
        "set A(value) {" +
        "  this.a_ = value;" +
        "}" +
        "get B() {" +
        "  return this.b_;" +
        "}" +
        "set B(value) {" +
        "  this.b_ = value;" +
        "}" +
        "get C() {" +
        "  return this.c_" +
        "}";
    Node root = compiler.parse(testCode);
    functionRewriter.process(compiler.getExterns(), root);
    Node testPrototype = compiler.getExterns().getFirstChild();

    Node getterA = testPrototype.getLastChild();
    Node setterA = testPrototype.getLastChild().getNext();
    Node getterB = testPrototype.getLastChild().getNext().getNext();
    Node setterB = testPrototype.getLastChild().getNext().getNext().getNext();
    Node getterC = testPrototype.getLastChild().getNext().getNext().getNext().getNext();

    List<Node> getters = ImmutableList.of(getterA, getterB, getterC);
    List<Node> setters = ImmutableList.of(setterA, setterB);

    for (int i = 0; i < getters.size(); i++) {
      Node getter = getters.get(i);
      Node setter = (i < setters.size()) ? setters.get(i) : null;

      Node expectedHelperNode = null;

      if (getter instanceof GetterReducer) {
        expectedHelperNode = compiler.parseSyntheticCode(
            "FunctionRewriterTest:helper", ((GetterReducer) getter).getName() + ":helper");
      } else if (getter instanceof SetterReducer) {
        expectedHelperNode = compiler.parseSyntheticCode(
            "FunctionRewriterTest:helper", ((SetterReducer) getter).getName() + ":helper");
      }

      Node expectedHelperRoot = (expectedHelperNode != null) ?
          expectedHelperNode.removeFirstChild() : null;

      assertEquals(expectedHelperRoot, getter);

      if (setter != null) {
        assertEquals(getter, setter.getLastChild());
      }
    }
  }
}