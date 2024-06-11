import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createAssign;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createGetProp;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createFunctionExpression;
import static com.google.javascript.rhino.NodeFactory.createVar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private Compiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = createFunction("testFunction", new int[] { Token.FUNCTION }, null, null, null, null, null);
    Node traversalParent = createScript("var x = testFunction();");
    assertTrue(checker.shouldTraverse(NodeTraversal.create(compiler, traversalParent), functionNode, traversalParent));
  }

  @Test
  public void testShouldNotTraverseNonFunction() {
    Node nonFunctionNode = createIdentifier("testIdentifier");
    Node traversalParent = createScript("var x = testIdentifier;");
    assertFalse(checker.shouldTraverse(NodeTraversal.create(compiler, traversalParent), nonFunctionNode, traversalParent));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node globalThisNode = createThis();
    Node traversalParent = createScript("var x = this;");
    assertTrue(checker.shouldReportThis(globalThisNode));
  }

  @Test
  public void testShouldNotReportThisInsidePrototypeProperty() {
    Node prototypePropertyNode = createGetProp(createIdentifier("prototype"), createIdentifier("Object"));
    Node traversalParent = createObjectLiteral();
    assertFalse(checker.shouldReportThis(prototypePropertyNode));
  }

  @Test
  public void testShouldNotReportThisInsidePrototypeSubproperty() {
    Node prototypeSubpropertyNode = createGetProp(createIdentifier("prototype"), createIdentifier("prototype"), createString("subproperty"));
    Node traversalParent = createObjectLiteral();
    assertFalse(checker.shouldReportThis(prototypeSubpropertyNode));
  }

  @Test
  public void testShouldReportThisInsideAssignment() {
    Node leftSideNode = createIdentifier("a");
    Node rightSideNode = createThis();
    Node assignmentNode = createAssign(leftSideNode, rightSideNode);
    Node traversalParent = createScript("var a = this;");
    assertTrue(checker.shouldReportThis(rightSideNode));
  }

  @Test
  public void testShouldNotReportThisInsidePrototypeAssignment() {
    Node prototypeNode = createIdentifier("prototype");
    Node leftSideNode = createGetProp(createIdentifier("Object"), prototypeNode);
    Node rightSideNode = createThis();
    Node assignmentNode = createAssign(leftSideNode, rightSideNode);
    Node traversalParent = createObjectLiteral();
    assertFalse(checker.shouldReportThis(rightSideNode));
  }

  @Test
  public void testShouldReportThisInsideFunctionAnnotatedWithConstructor() {
    Node constructorNode =
        createFunctionExpression(
            createIdentifier("TestConstructor"),
            new int[] { Token.FUNCTION, Token.CONSTRUCTOR },
            null,
            null,
            null,
            null,
            null,
            null);
    Node traversalParent = createScript("new TestConstructor();");
    assertTrue(checker.shouldTraverse(NodeTraversal.create(compiler, traversalParent), constructorNode, traversalParent));
  }

  @Test
  public void testShouldReportThisInsideFunctionAnnotatedWithThisType() {
    Node thisTypeNode = createIdentifier("this");
    Node thisAnnotationNode = createIdentifier("@this");
    Node functionNode = createFunction("testFunction", new int[] { Token.FUNCTION }, thisAnnotationNode, null, null, null, null, null, null, null, null);
    Node traversalParent = createScript("testFunction();");
    assertTrue(checker.shouldTraverse(NodeTraversal.create(compiler, traversalParent), functionNode, traversalParent));
  }

  @Test
  public void testShouldReportThisInsideFunctionWithNoPlausibleThisType() {
    Node functionNode = createFunction("testFunction", new int[] { Token.FUNCTION }, null, null, null, null, null, null, null, null, null);
    Node traversalParent = createScript("testFunction();");
    assertTrue(checker.shouldTraverse(NodeTraversal.create(compiler, traversalParent), functionNode, traversalParent));
  }
}