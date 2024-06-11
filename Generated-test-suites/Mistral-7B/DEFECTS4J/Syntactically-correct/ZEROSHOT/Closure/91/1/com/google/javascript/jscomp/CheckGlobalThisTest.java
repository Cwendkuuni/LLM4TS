import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Resolver;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory nodeFactory;
  private Resolver resolver;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    checker = new Checker(compiler, new CompilerOptions());
    nodeFactory = Mockito.mock(NodeFactory.class);
    resolver = Mockito.mock(Resolver.class);
  }

  @Test
  public void testSafeUsesOfThis() {
    Node node = Node.newFunction((FunctionDeclaration) null);
    Node functionNode = Node.newFunction((FunctionDeclaration) null);

    Node prototypeNode = Node.newObjectLiteral();
    prototypeNode.setProperty("prototype", Node.newObjectLiteral());

    Node constructorNode = Node.newFunction((ConstructorDeclaration) null);
    constructorNode.setProperty("prototype", prototypeNode);

    Node constructorPropertyNode = Node.newGetProperty(null, "prototype");
    Node prototypeFunctionNode = Node.newFunction((FunctionDeclaration) null);
    prototypeFunctionNode.setProperty("prototype", Node.newObjectLiteral());
    prototypeFunctionNode.getLastChild().setType(Token.FUNCTION);

    Node thisKeywordNode = Node.newThisKeyword();

    // Test function with no parent
    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, null);
    assertTrue(checker.getErrors().isEmpty());

    // Test function as a child of a block
    Node blockNode = Node.newBlockStatement();
    blockNode.addChild(node);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, blockNode);
    assertTrue(checker.getErrors().isEmpty());

    // Test function as a child of a script
    Node scriptNode = Node.newScript();
    scriptNode.addChild(blockNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, scriptNode);
    assertTrue(checker.getErrors().isEmpty());

    // Test function as a child of a name node
    Node nameNode = Node.newName("a");
    nameNode.addChild(node);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, nameNode);
    assertTrue(checker.getErrors().isEmpty());

    // Test function as a child of an assign node
    Node assignNode = Node.newAssign(Token.ASSIGN, nameNode, node);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, assignNode);
    assertTrue(checker.getErrors().isEmpty());

    // Test function as a child of a getprop node accessing prototype
    Node prototypeAccessNode = Node.newGetProperty(null, "prototype");
    prototypeAccessNode.getLastChild().setType(Token.GETPROP);
    prototypeAccessNode.getLastChild().getLastChild().setType(Token.NAME);
    prototypeAccessNode.getLastChild().getLastChild().setString("constructor");

    prototypeAccessNode.addChild(constructorNode);
    constructorNode.addChild(constructorPropertyNode);
    constructorPropertyNode.addChild(thisKeywordNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, prototypeAccessNode);
    assertTrue(checker.getErrors().isEmpty());

    // Test prototype method
    prototypeFunctionNode.getLastChild().setType(Token.FUNCTION);
    prototypeFunctionNode.getLastChild().addChild(thisKeywordNode);

    prototypeNode.addChild(prototypeFunctionNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, prototypeAccessNode);
    assertTrue(checker.getErrors().isEmpty());

    // Test constructor function
    constructorNode.getLastChild().setType(Token.FUNCTION);
    constructorNode.getLastChild().addChild(thisKeywordNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, constructorNode);
    assertTrue(checker.getErrors().isEmpty());

    // Test this annotation function
    Node thisAnnotationNode = Node.newAnnotation(
        "this", Node.newName("@this"));
    constructorNode.addChild(thisAnnotationNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, constructorNode);
    assertTrue(checker.getErrors().isEmpty());

    // Test function without a logical place to put a this annotation
    Node functionWithoutThisAnnotationNode = Node.newFunction((FunctionDeclaration) null);
    functionWithoutThisAnnotationNode.addChild(thisKeywordNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, functionWithoutThisAnnotationNode);
    assertTrue(checker.getErrors().isEmpty());
  }

  @Test
  public void testUnsafeUsesOfThis() {
    Node node = Node.newFunction((FunctionDeclaration) null);
    Node functionNode = Node.newFunction((FunctionDeclaration) null);

    Node unsafeNode = Node.newThisKeyword();

    // Test unsafe uses of this on the left side of an assign
    Node assignNode = Node.newAssign(Token.ASSIGN, Node.newName("a"), unsafeNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, assignNode);
    assertEquals(1, checker.getErrors().size());
    assertEquals(
        "dangerous use of the global 'this' object",
        checker.getErrors().get(0).getMessage(null));

    // Test unsafe uses of this as a child of a getprop node
    Node getPropNode = Node.newGetProperty(null, "prototype");
    getPropNode.getLastChild().setType(Token.GETPROP);
    getPropNode.getLastChild().getLastChild().setType(Token.NAME);
    getPropNode.getLastChild().getLastChild().setString("constructor");

    getPropNode.addChild(constructorNode);
    constructorNode.addChild(unsafeNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, getPropNode);
    assertEquals(1, checker.getErrors().size());
    assertEquals(
        "dangerous use of the global 'this' object",
        checker.getErrors().get(0).getMessage(null));
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node node = Node.newFunction((FunctionDeclaration) null);

    Node nonGlobalContextNode = Node.newObjectLiteral();

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, nonGlobalContextNode);
    assertTrue(checker.getErrors().isEmpty());
  }

  @Test
  public void testShouldNotTraverseFunctionsWithConstructorOrThisOrOverrideAnnotations() {
    Node node = Node.newFunction((FunctionDeclaration) null);
    Node constructorAnnotationNode = Node.newAnnotation("constructor", Node.newName("@constructor"));
    Node thisAnnotationNode = Node.newAnnotation("this", Node.newName("@this"));
    Node overrideAnnotationNode = Node.newAnnotation("override", Node.newName("@override"));

    Node constructorFunctionNode = Node.newFunction((ConstructorDeclaration) null);
    constructorFunctionNode.addChild(constructorAnnotationNode);

    Node thisFunctionNode = Node.newFunction((FunctionDeclaration) null);
    thisFunctionNode.addChild(thisAnnotationNode);

    Node overrideFunctionNode = Node.newFunction((FunctionDeclaration) null);
    overrideFunctionNode.addChild(overrideAnnotationNode);

    Node functionNodeWithConstructorAnnotation = Node.newFunction(
        (FunctionDeclaration) null);
    functionNodeWithConstructorAnnotation.addChild(constructorAnnotationNode);

    Node functionNodeWithThisAnnotation = Node.newFunction(
        (FunctionDeclaration) null);
    functionNodeWithThisAnnotation.addChild(thisAnnotationNode);

    Node functionNodeWithOverrideAnnotation = Node.newFunction(
        (FunctionDeclaration) null);
    functionNodeWithOverrideAnnotation.addChild(overrideAnnotationNode);

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, constructorFunctionNode);
    assertTrue(checker.getErrors().isEmpty());

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, thisFunctionNode);
    assertTrue(checker.getErrors().isEmpty());

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, overrideFunctionNode);
    assertTrue(checker.getErrors().isEmpty());

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, functionNodeWithConstructorAnnotation);
    assertTrue(checker.getErrors().isEmpty());

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, functionNodeWithThisAnnotation);
    assertTrue(checker.getErrors().isEmpty());

    checker.getChecks().get(CheckGlobalThis.class).traverse(
        checker.getRootNode(), node, functionNodeWithOverrideAnnotation);
    assertTrue(checker.getErrors().isEmpty());
  }
}