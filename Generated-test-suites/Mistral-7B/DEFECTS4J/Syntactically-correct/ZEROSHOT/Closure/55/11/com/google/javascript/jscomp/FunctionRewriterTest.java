import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

public class FunctionRewriterTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testEmptyFunctionReducer() {
    String testSource = "function Test() {} Test.prototype.getA = function() {}; Test.prototype.setA = function(newValue) {}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node getA = testPrototype.getLastChild().getLastChild();
    Node setA = testPrototype.getLastChild().getLastChild().getNext();

    assertEquals(nodeFactory.getCallNode(getHelperMethodName(EmptyFunctionReducer.class), null), getA);
    assertEquals(nodeFactory.getCallNode(getHelperMethodName(EmptyFunctionReducer.class), null), setA);
  }

  @Test
  public void testIdentityReducer() {
    String testSource = "function Test() {return this;} Test.prototype.getA = function() {return this}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node getA = testPrototype.getLastChild().getLastChild();

    assertEquals(nodeFactory.getCallNode(getHelperMethodName(IdentityReducer.class), null), getA);
  }

  @Test
  public void testReturnConstantReducer() {
    String testSource = "function Test() {return 10} Test.prototype.getA = function() {return 10}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node getA = testPrototype.getLastChild().getLastChild();

    assertEquals(nodeFactory.getCallNode(getHelperMethodName(ReturnConstantReducer.class), 10), getA);
  }

  @Test
  public void testGetterReducer() {
    String testSource = "function Test() {} Test.prototype.getA = function() {return this.a_}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node getA = testPrototype.getLastChild().getLastChild();

    assertEquals(nodeFactory.getCallNode(getHelperMethodName(GetterReducer.class), "a_"), getA);
  }

  @Test
  public void testSetterReducer() {
    String testSource = "function Test() {} Test.prototype.setA = function(newValue) {this.a_ = newValue}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node setA = testPrototype.getLastChild().getLastChild();

    assertEquals(nodeFactory.getCallNode(getHelperMethodName(SetterReducer.class), "a_"), setA);
  }

  @Test
  public void testReducerDoesNotApplyWhenNoSavings() {
    String testSource = "function Test() {} Test.prototype.getA = function() {return 1 + 1}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node getA = testPrototype.getLastChild().getLastChild();

    // Since JSCompiler_add is not defined, the cost of the helper
    // method is greater than the savings, so the reduction is not applied.
    assertNotEquals(nodeFactory.getCallNode(getHelperMethodName(GetterReducer.class), "a_"), getA);
  }

  @Test
  public void testReducerDoesNotApplyWhenEmptyFunction() {
    String testSource = "function Test() {} Test.prototype.getA = function() {}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node getA = testPrototype.getLastChild().getLastChild();

    // An empty function has no cost, but the helper method has a
    // non-zero cost, so the reduction is not applied.
    assertNotEquals(nodeFactory.getCallNode(getHelperMethodName(GetterReducer.class), "a_"), getA);
  }

  @Test
  public void testReducerDoesNotApplyWhenHelperMethodCostGreaterThanSavings() {
    String testSource = "function Test() {} Test.prototype.getA = function() {return 1 + 1 + 1 + 1 + 1}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node getA = testPrototype.getLastChild().getLastChild();

    // The helper method has a higher cost than the savings, so the reduction is not applied.
    assertNotEquals(nodeFactory.getCallNode(getHelperMethodName(GetterReducer.class), "a_"), getA);
  }

  @Test
  public void testReducerAppliesWhenSavingsGreaterThanHelperMethodCost() {
    String testSource = "function Test() {} Test.prototype.getA = function() {return 1 + 1 + 1}";
    Node rootNode = compiler.parseTree(testSource, null);

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(nodeFactory.getRootNode(), rootNode);

    Node testPrototype = rootNode.getFirstChild().getFirstChild();
    Node getA = testPrototype.getLastChild().getLastChild();

    // The savings are greater than the helper method cost, so the reduction is applied.
    assertEquals(nodeFactory.getCallNode(getHelperMethodName(GetterReducer.class), "a_"), getA);
  }

  private String getHelperMethodName(Class<? extends Reducer> reducerClass) {
    return reducerClass.getSimpleName() + "_helper";
  }
}