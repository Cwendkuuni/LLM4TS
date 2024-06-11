import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;
import static com.google.javascript.rhino.NodeType.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private Parser parser;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public CompilerOptions getOptions() {
        return new CompilerOptions();
      }
    };

    nodeFactory = compiler.getFactory();
    parser = new Parser(new SourceFile("test.js", "<xml></xml>", nodeFactory));
    parser.parse();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }

  @Test
  public void testSafeUsesOfThis() {
    Node functionNode = parser.getNodeFactory().newFunction("safeFunction", new Node[0], new Node[0]);
    Node prototypeNode = nodeFactory.newObject("Object");
    prototypeNode.setProperty("prototype", nodeFactory.newObject("Object").setProperty("constructor", functionNode));
    Node objectNode = nodeFactory.newObject("Object", prototypeNode);

    Node contextNode = parser.parseExpression("function safeFunction() {}");
    Node prototypeAssignmentNode = parser.parseExpression("Object.prototype = Object.prototype;");
    Node objectAssignmentNode = parser.parseExpression("var obj = new Object();");

    Node unscopedFunctionNode = parser.parseExpression("function unscopedFunction() {}");

    Node anonymousFunctionNode = nodeFactory.newExpressionStatement(nodeFactory.newFunctionExpression(null, new Node[0], new Node[0], null, "anonymousFunction"));
    Node prototypeMethodNode = nodeFactory.newMethodExpression(prototypeNode, "safeMethod", new Node[0], null);
    Node constructorMethodNode = nodeFactory.newMethodExpression(prototypeNode, "constructor", new Node[0], null);

    Node safeFunctionCallNode = nodeFactory.newCallExpression(functionNode, new Node[0]);
    Node prototypeMethodCallNode = nodeFactory.newCallExpression(prototypeMethodNode, new Node[0]);
    Node constructorMethodCallNode = nodeFactory.newCallExpression(constructorMethodNode, new Node[0]);
    Node anonymousFunctionCallNode = nodeFactory.newCallExpression(anonymousFunctionNode, new Node[0]);
    Node unscopedFunctionCallNode = nodeFactory.newCallExpression(unscopedFunctionNode, new Node[0]);
    Node objectMethodCallNode = nodeFactory.newCallExpression(nodeFactory.newGetExpression(objectNode, "safeMethod"), new Node[0]);

    Node safeAssignmentNode = nodeFactory.newAssignment(nodeFactory.newThisKeyword(), nodeFactory.newIdentifier("safeVariable"));
    Node safePropertyAccessNode = nodeFactory.newGetExpression(nodeFactory.newThisKeyword(), "safeProperty");

    Node objectAssignmentNodeWithThis = nodeFactory.newAssignment(nodeFactory.newThisKeyword(), objectNode);

    Node safePrototypeMethodAssignmentNode = nodeFactory.newAssignment(prototypeMethodNode, nodeFactory.newIdentifier("safePrototypeMethod"));
    Node safePrototypePropertyAccessNode = nodeFactory.newGetExpression(prototypeNode, "safePrototypeProperty");

    Node safeAnonymousFunctionAssignmentNode = nodeFactory.newAssignment(anonymousFunctionNode, nodeFactory.newIdentifier("safeAnonymousFunction"));
    Node safeAnonymousFunctionPropertyAccessNode = nodeFactory.newGetExpression(anonymousFunctionNode, "safeAnonymousFunctionProperty");

    Node safeFunctionWithNoAnnotationNode = nodeFactory.newFunction(nodeFactory.newIdentifier("safeFunctionWithNoAnnotation"), new Node[0], new Node[0]);
    Node safeFunctionWithNoAnnotationCallNode = nodeFactory.newCallExpression(safeFunctionWithNoAnnotationNode, new Node[0]);

    Node safeThisKeywordAssignmentNode = nodeFactory.newAssignment(nodeFactory.newThisKeyword(), nodeFactory.newIdentifier("safeThisKeyword"));

    Node safeBlockNode = nodeFactory.newBlock(new Node[0]);
    Node safeVarNode = nodeFactory.newVar("safeVar", null, null, new Node[0]);
    Node safeFunctionDeclarationNode = nodeFactory.newFunctionDeclaration(nodeFactory.newIdentifier("safeFunctionDeclaration"), new Node[0], null, null, null, safeBlockNode);

    Node safeScriptNode = nodeFactory.newScript(new Node[0]);

    Node safeFunctionWithThisAnnotationNode = nodeFactory.newFunction(nodeFactory.newIdentifier("safeFunctionWithThisAnnotation"), new Node[0], new Node[0], null, "@this", null);
    Node safeFunctionWithThisAnnotationCallNode = nodeFactory.newCallExpression(safeFunctionWithThisAnnotationNode, new Node[0]);

    Node safeConstructorFunctionNode = nodeFactory.newFunction(nodeFactory.newIdentifier("safeConstructorFunction"), new Node[0], new Node[0], null, "@constructor", null);
    Node safeConstructorFunctionCallNode = nodeFactory.newCallExpression(safeConstructorFunctionNode, new Node[0]);

    Node safeFunctionWithNoLogicalPlaceToPutThisAnnotationNode = nodeFactory.newFunction(nodeFactory.newIdentifier("safeFunctionWithNoLogicalPlaceToPutThisAnnotation"), new Node[0], new Node[0]);

    Node[] nodes = {
        safeFunctionNode,
        prototypeNode,
        objectNode,
        unscopedFunctionNode,
        prototypeMethodNode,
        constructorMethodNode,
        anonymousFunctionNode,
        safeFunctionCallNode,
        prototypeMethodCallNode,
        constructorMethodCallNode,
        anonymousFunctionCallNode,
        unscopedFunctionCallNode,
        safeAssignmentNode,
        safePropertyAccessNode,
        objectAssignmentNode,
        safePrototypeMethodAssignmentNode,
        safePrototypePropertyAccessNode,
        safeAnonymousFunctionAssignmentNode,
        safeAnonymousFunctionPropertyAccessNode,
        safeFunctionWithNoAnnotationNode,
        safeFunctionWithNoAnnotationCallNode,
        safeThisKeywordAssignmentNode,
        safeBlockNode,
        safeVarNode,
        safeFunctionDeclarationNode,
        safeScriptNode,
        safeFunctionWithThisAnnotationNode,
        safeFunctionWithThisAnnotationCallNode,
        safeConstructorFunctionNode,
        safeConstructorFunctionCallNode,
        safeFunctionWithNoLogicalPlaceToPutThisAnnotationNode,

        // This should be safe as per the documentation.
        nodeFactory.newFunction(nodeFactory.newIdentifier("safeFunctionWithPrototypeProperty"), new Node[0], new Node[0]),
        nodeFactory.newGetExpression(nodeFactory.newGetExpression(prototypeNode, "safeFunctionWithPrototypeProperty"), "prototype"),
        nodeFactory.newCallExpression(nodeFactory.newGetExpression(nodeFactory.newGetExpression(prototypeNode, "safeFunctionWithPrototypeProperty"), "prototype"), new Node[0])
    };

    Node rootNode = nodeFactory.newBlock(nodes);

    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    checkGlobalThis.traverse(traversal, rootNode, null);

    assertTrue("Expected 0 diagnostic messages", checkGlobalThis.getDiagnostics().isEmpty());
  }

  @Test
  public void testUnsafeUsesOfThis() {
    Node functionNode = parser.parseExpression("function unsafeFunction() { this.property = 'unsafe'; }");

    Node unsafeAssignmentNode = nodeFactory.newAssignment(nodeFactory.newThisKeyword(), nodeFactory.newIdentifier("unsafeVariable"));
    Node unsafePropertyAccessNode = nodeFactory.newGetExpression(nodeFactory.newThisKeyword(), "unsafeProperty");

    Node unsafeFunctionCallNode = nodeFactory.newCallExpression(functionNode, new Node[0]);

    Node[] nodes = {functionNode, unsafeAssignmentNode, unsafePropertyAccessNode, unsafeFunctionCallNode};

    Node rootNode = nodeFactory.newBlock(nodes);

    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    checkGlobalThis.traverse(traversal, rootNode, null);

    assertEquals("Expected 1 diagnostic message", 1, checkGlobalThis.getDiagnostics().size());
    assertEquals("Unexpected diagnostic message", GLOBAL_THIS.getMessage(), checkGlobalThis.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeUseOfThisInPrototypeProperty() {
    Node functionNode = parser.parseExpression("function unsafeFunction() { Object.prototype.property = 'unsafe'; }");

    Node unsafeFunctionCallNode = nodeFactory.newCallExpression(functionNode, new Node[0]);

    Node[] nodes = {functionNode, unsafeFunctionCallNode};

    Node rootNode = nodeFactory.newBlock(nodes);

    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    checkGlobalThis.traverse(traversal, rootNode, null);

    assertEquals("Expected 1 diagnostic message", 1, checkGlobalThis.getDiagnostics().size());
    assertEquals("Unexpected diagnostic message", GLOBAL_THIS.getMessage(), checkGlobalThis.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeUseOfThisInPrototypeMethod() {
    Node functionNode = parser.parseExpression("function unsafeFunction() { this.safeMethod = function() {}; }");
    Node methodNode = nodeFactory.newMethodExpression(nodeFactory.newThisKeyword(), "safeMethod", new Node[0], null);

    Node unsafeFunctionCallNode = nodeFactory.newCallExpression(functionNode, new Node[0]);
    Node unsafeMethodCallNode = nodeFactory.newCallExpression(methodNode, new Node[0]);

    Node[] nodes = {functionNode, methodNode, unsafeFunctionCallNode, unsafeMethodCallNode};

    Node rootNode = nodeFactory.newBlock(nodes);

    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    checkGlobalThis.traverse(traversal, rootNode, null);

    assertEquals("Expected 1 diagnostic message", 1, checkGlobalThis.getDiagnostics().size());
    assertEquals("Unexpected diagnostic message", GLOBAL_THIS.getMessage(), checkGlobalThis.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeUseOfThisInNestedFunction() {
    Node functionNode = parser.parseExpression("function unsafeFunction() { function innerFunction() { this.property = 'unsafe'; } }");
    Node innerFunctionNode = nodeFactory.newFunctionExpression(nodeFactory.newThisKeyword(), new Node[0], new Node[0], null, "innerFunction");

    Node unsafeFunctionCallNode = nodeFactory.newCallExpression(functionNode, new Node[0]);
    Node innerFunctionCallNode = nodeFactory.newCallExpression(innerFunctionNode, new Node[0]);

    Node[] nodes = {functionNode, innerFunctionNode, unsafeFunctionCallNode, innerFunctionCallNode};

    Node rootNode = nodeFactory.newBlock(nodes);

    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    checkGlobalThis.traverse(traversal, rootNode, null);

    assertEquals("Expected 1 diagnostic message", 1, checkGlobalThis.getDiagnostics().size());
    assertEquals("Unexpected diagnostic message", GLOBAL_THIS.getMessage(), checkGlobalThis.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeUseOfThisInArrowFunction() {
    Node functionNode = parser.parseExpression("function unsafeFunction() { const arrowFunction = () => { this.property = 'unsafe'; } }");
    Node arrowFunctionNode = nodeFactory.newArrowFunctionExpression(null, new Node[0], () -> nodeFactory.newAssignment(nodeFactory.newThisKeyword(), nodeFactory.newIdentifier("unsafeVariable")));

    Node unsafeFunctionCallNode = nodeFactory.newCallExpression(functionNode, new Node[0]);
    Node arrowFunctionCallNode = nodeFactory.newCallExpression(arrowFunctionNode, new Node[0]);

    Node[] nodes = {functionNode, arrowFunctionNode, unsafeFunctionCallNode, arrowFunctionCallNode};

    Node rootNode = nodeFactory.newBlock(nodes);

    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    checkGlobalThis.traverse(traversal, rootNode, null);

    assertEquals("Expected 1 diagnostic message", 1, checkGlobalThis.getDiagnostics().size());
    assertEquals("Unexpected diagnostic message", GLOBAL_THIS.getMessage(), checkGlobalThis.getDiagnostics().get(0).getMessage());
  }
}