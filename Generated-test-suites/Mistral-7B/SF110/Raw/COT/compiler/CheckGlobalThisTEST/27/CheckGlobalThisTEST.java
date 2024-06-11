import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.JSDocInfo.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private Compiler compiler;
  private NodeTraversal traverser;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.init();
    traverser = new NodeTraversal(compiler);
  }

  @Test
  public void testShouldTraverseFunctions() {
    Node node = createFunctionNode();
    assertTrue(new CheckGlobalThis(compiler).shouldTraverse(traverser, node, null));
  }

  @Test
  public void testShouldNotTraverseConstructors() {
    Node node = createConstructorNode();
    assertFalse(new CheckGlobalThis(compiler).shouldTraverse(traverser, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithThisAnnotation() {
    Node node = createFunctionWithThisAnnotationNode();
    assertFalse(new CheckGlobalThis(compiler).shouldTraverse(traverser, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithInterfaceAnnotation() {
    Node node = createFunctionWithInterfaceAnnotationNode();
    assertFalse(new CheckGlobalThis(compiler).shouldTraverse(traverser, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsInObjectLiterals() {
    Node node = createFunctionInObjectLiteralNode();
    assertFalse(new CheckGlobalThis(compiler).shouldTraverse(traverser, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionsLentToPrototype() {
    Node node = createFunctionLentToPrototypeNode();
    assertFalse(new CheckGlobalThis(compiler).shouldTraverse(traverser, node, null));
  }

  @Test
  public void testShouldNotTraverseAssignmentsToPrototypeProperties() {
    Node node = createAssignmentToPrototypePropertyNode();
    assertFalse(new CheckGlobalThis(compiler).shouldTraverse(traverser, node, null));
  }

  @Test
  public void testShouldNotTraverseAssignmentsToPrototypeSubproperties() {
    Node node = createAssignmentToPrototypeSubpropertyNode();
    assertFalse(new CheckGlobalThis(compiler).shouldTraverse(traverser, node, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node leftSideOfAssignment = createNode("a = this;");
    assertTrue(new CheckGlobalThis(compiler).shouldTraverse(traverser, leftSideOfAssignment, null));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node globalThisNode = createNode("function foo() { this.bar = 42; }");
    assertEquals(1, compiler.getDiagnostics().size());
    compiler.process(traverser, globalThisNode, null);
  }

  private Node createFunctionNode() {
    return createNode("function foo() { }");
  }

  private Node createConstructorNode() {
    return createNode("function Foo() { }");
  }

  private Node createFunctionWithThisAnnotationNode() {
    return createNode("@this function foo() { }");
  }

  private Node createFunctionWithInterfaceAnnotationNode() {
    return createNode("function foo() @interface Foo { }");
  }

  private Node createFunctionInObjectLiteralNode() {
    return createNode("{ a: function() { } }");
  }

  private Node createFunctionLentToPrototypeNode() {
    return createNode("{ Foo: { prototype: { bar: function() { } } } }");
  }

  private Node createAssignmentToPrototypePropertyNode() {
    return createNode("{ Foo: { prototype: { bar: null } }, foo: function() { this.bar = 42; } }");
  }

  private Node createAssignmentToPrototypeSubpropertyNode() {
    return createNode("{ Foo: { prototype: { bar: { qux: null } } }, foo: function() { this.bar.qux = 42; } }");
  }

  private Node createNode(String nodeString) {
    return compiler.parseTree(nodeString).getFirstChild();
  }
}