import static org.junit.Assert.*;
import static com.google.javascript.jscomp.checker.CheckerFactory.checkType;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.rhino.token.TokenType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private CheckGlobalThis checkerUnderTest;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    checker = CheckerFactory.getChecker("checkGlobalThis", compiler);
    checkerUnderTest = new CheckGlobalThis(compiler);
  }

  // Test for method: public CheckGlobalThis(AbstractCompiler compiler)
  @Test
  public void testConstructor() {
    CheckGlobalThis instance = new CheckGlobalThis(compiler);
    assertNotNull(instance);
  }

  // Test for method: boolean shouldTraverse(NodeTraversal, Node, Node)
  @Test
  public void testShouldTraverse_Function() {
    Node functionNode = newFunctionExpression("function a() {}");
    Node parentNode = newIdentifier("root");
    boolean expected = true;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, functionNode, parentNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldTraverse_Constructor() {
    Node constructorNode = newConstructorExpression("function A() {}");
    Node parentNode = newIdentifier("root");
    boolean expected = false;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, constructorNode, parentNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation() {
    Node functionNode = newFunctionExpression("@this function a() {}");
    Node parentNode = newIdentifier("root");
    boolean expected = true;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, functionNode, parentNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation() {
    Node functionNode = newFunctionExpression("@override function a() {}");
    Node parentNode = newIdentifier("root");
    boolean expected = true;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, functionNode, parentNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldTraverse_FunctionInObjectLiteral() {
    Node functionNode = newFunctionExpression("var obj = {a: function a() {}}");
    Node parentNode = newObjectLiteral();
    boolean expected = true;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, functionNode, parentNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldTraverse_FunctionLentToPrototype() {
    Node functionNode = newFunctionExpression("function obj() {}");
    Node prototypeNode = newObjectLiteral();
    prototypeNode.addChildToEnd(newProperty("prototype", functionNode));
    Node parentNode = newIdentifier("root");
    parentNode.setParent(prototypeNode);
    boolean expected = false;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, functionNode, parentNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldTraverse_FunctionWithAssignOnLeftSide() {
    Node assignNode = newAssignmentExpression(ASSIGN, newIdentifier("a"), newThisExpression());
    Node parentNode = newIdentifier("root");
    boolean expected = true;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, assignNode, parentNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldTraverse_PropertyAccess() {
    Node functionNode = newFunctionExpression("function a() {}");
    Node propertyAccessNode = newGetExpression(newIdentifier("a"), newIdentifier("prototype"));
    Node parentNode = newIdentifier("root");
    boolean expected = false;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, propertyAccessNode, parentNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testShouldTraverse_PropertyAccessWithNestedProperty() {
    Node functionNode = newFunctionExpression("function a() {}");
    Node propertyAccessNode = newGetExpression(newGetExpression(newIdentifier("a"), newIdentifier("prototype")), newIdentifier("x"));
    Node parentNode = newIdentifier("root");
    boolean expected = false;
    boolean actual = checkerUnderTest.shouldTraverse(new NodeTraversal(compiler) {
    }, propertyAccessNode, parentNode);
    assertEquals(expected, actual);
  }

  // Test for method: void visit(NodeTraversal, Node, Node)
  @Test
  public void testVisit_ThisKeyword() {
    Node thisKeywordNode = newThisExpression();
    Node parentNode = newIdentifier("root");
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = ImmutableList.of(
        newErrorDiagnosticNode(GLOBAL_THIS, thisKeywordNode)
    );

    checker.check(new NodeList(new Node[] { thisKeywordNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest);
    List<Node> actualDiagnostics = checker.getDiagnostics();

    assertEquals(expectedDiagnostics, actualDiagnostics);
  }

  @Test
  public void testVisit_Function() {
    Node functionNode = newFunctionExpression("function a() {}");
    Node parentNode = newIdentifier("root");
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { functionNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }

  @Test
  public void testVisit_Constructor() {
    Node constructorNode = newConstructorExpression("function A() {}");
    Node parentNode = newIdentifier("root");
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { constructorNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }

  @Test
  public void testVisit_FunctionWithThisAnnotation() {
    Node functionNode = newFunctionExpression("@this function a() {}");
    Node parentNode = newIdentifier("root");
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { functionNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }

  @Test
  public void testVisit_FunctionWithOverrideAnnotation() {
    Node functionNode = newFunctionExpression("@override function a() {}");
    Node parentNode = newIdentifier("root");
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { functionNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }

  @Test
  public void testVisit_FunctionInObjectLiteral() {
    Node functionNode = newFunctionExpression("var obj = {a: function a() {}}");
    Node parentNode = newObjectLiteral();
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { functionNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }

  @Test
  public void testVisit_FunctionLentToPrototype() {
    Node functionNode = newFunctionExpression("function obj() {}");
    Node prototypeNode = newObjectLiteral();
    prototypeNode.addChildToEnd(newProperty("prototype", functionNode));
    Node parentNode = prototypeNode;
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { functionNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }

  @Test
  public void testVisit_FunctionWithAssignOnLeftSide() {
    Node assignNode = newAssignmentExpression(ASSIGN, newIdentifier("a"), newThisExpression());
    Node parentNode = newIdentifier("root");
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { assignNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }

  @Test
  public void testVisit_PropertyAccess() {
    Node functionNode = newFunctionExpression("function a() {}");
    Node propertyAccessNode = newGetExpression(newIdentifier("a"), newIdentifier("prototype"));
    Node parentNode = newIdentifier("root");
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { propertyAccessNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }

  @Test
  public void testVisit_PropertyAccessWithNestedProperty() {
    Node functionNode = newFunctionExpression("function a() {}");
    Node propertyAccessNode = newGetExpression(newGetExpression(newIdentifier("a"), newIdentifier("prototype")), newIdentifier("x"));
    Node parentNode = newIdentifier("root");
    Node traversal = new NodeTraversal(compiler);

    List<Node> expectedDiagnostics = new ArrayList<>();
    List<Node> actualDiagnostics = checker.check(new NodeList(new Node[] { propertyAccessNode }), new NodeList(new Node[] { parentNode }), checkerUnderTest).getDiagnostics();

    assertEmpty(actualDiagnostics);
  }
}