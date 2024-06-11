import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.bare.ast.FunctionDeclaration;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new CompilerOptions().setWarningLevel(WARNING).getCompiler();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testGlobalThis() {
    Node ast = createGlobalThisNode();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(ast);

    int numErrors = compiler.getDiagnostics().size();
    assertEquals("One global 'this' warning expected", 1, numErrors);
    DiagnosticType errorType = compiler.getDiagnostics().get(0).getType();
    assertEquals("Unexpected diagnostic type", GLOBAL_THIS, errorType);
  }

  @Test
  public void testPrototypeMethod() {
    Node ast = createPrototypeMethodNode();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(ast);

    int numErrors = compiler.getDiagnostics().size();
    assertEquals("No warnings expected", 0, numErrors);
  }

  @Test
  public void testConstructor() {
    Node ast = createConstructorNode();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(ast);

    int numErrors = compiler.getDiagnostics().size();
    assertEquals("No warnings expected", 0, numErrors);
  }

  @Test
  public void testThisAnnotation() {
    Node ast = createThisAnnotationNode();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(ast);

    int numErrors = compiler.getDiagnostics().size();
    assertEquals("No warnings expected", 0, numErrors);
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    Node ast = createFunctionWithNoAnnotationNode();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(ast);

    int numErrors = compiler.getDiagnostics().size();
    assertEquals("One global 'this' warning expected", 1, numErrors);
  }

  @Test
  public void testFunctionWithAssignment() {
    Node ast = createFunctionWithAssignmentNode();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(ast);

    int numErrors = compiler.getDiagnostics().size();
    assertEquals("One global 'this' warning expected", 1, numErrors);
  }

  @Test
  public void testFunctionWithPrototypeAccess() {
    Node ast = createFunctionWithPrototypeAccessNode();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(ast);

    int numErrors = compiler.getDiagnostics().size();
    assertEquals("One global 'this' warning expected", 1, numErrors);
  }

  @Test
  public void testFunctionWithNestedPrototypeAccess() {
    Node ast = createFunctionWithNestedPrototypeAccessNode();
    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(ast);

    int numErrors = compiler.getDiagnostics().size();
    assertEquals("One global 'this' warning expected", 1, numErrors);
  }

  private Node createGlobalThisNode() {
    FunctionDeclaration function = nodeFactory.createFunctionDeclaration("evil", null, null, null);
    Node thisNode = nodeFactory.createThis();
    function.setBody(nodeFactory.createStatement(nodeFactory.createAssignment(function, thisNode)));
    return function;
  }

  private Node createPrototypeMethodNode() {
    FunctionDeclaration constructor = nodeFactory.createFunctionDeclaration("Constructor", null, null, null);
    constructor.setPrototype(nodeFactory.createObjectLiteral());
    FunctionDeclaration method = nodeFactory.createFunctionDeclaration("method", constructor, null, null);
    Node thisNode = nodeFactory.createThis();
    method.setBody(nodeFactory.createStatement(nodeFactory.createAssignment(thisNode, nodeFactory.createString("useful"))));
    constructor.getBody().addChildren(method);
    return constructor;
  }

  private Node createConstructorNode() {
    FunctionDeclaration constructor = nodeFactory.createFunctionDeclaration("Constructor", null, null, null);
    constructor.setPrototype(nodeFactory.createObjectLiteral());
    return constructor;
  }

  private Node createThisAnnotationNode() {
    FunctionDeclaration function = nodeFactory.createFunctionDeclaration("function", null, null, null);
    function.setBody(nodeFactory.createStatement(nodeFactory.createThisAnnotation()));
    return function;
  }

  private Node createFunctionWithNoAnnotationNode() {
    FunctionDeclaration function = nodeFactory.createFunctionDeclaration("function", null, null, null);
    return function;
  }

  private Node createFunctionWithAssignmentNode() {
    FunctionDeclaration function = nodeFactory.createFunctionDeclaration("function", null, null, null);
    Node thisNode = nodeFactory.createThis();
    Node assignNode = nodeFactory.createStatement(nodeFactory.createAssignment(function, thisNode));
    function.setBody(assignNode);
    return function;
  }

  private Node createFunctionWithPrototypeAccessNode() {
    FunctionDeclaration function = nodeFactory.createFunctionDeclaration("function", null, null, null);
    Node prototypeAccessNode = nodeFactory.createGetProperty(nodeFactory.createThis(), "prototype");
    function.setBody(nodeFactory.createStatement(nodeFactory.createAssignment(prototypeAccessNode, nodeFactory.createNull())));
    return function;
  }

  private Node createFunctionWithNestedPrototypeAccessNode() {
    FunctionDeclaration constructor = nodeFactory.createFunctionDeclaration("Constructor", null, null, null);
    constructor.setPrototype(nodeFactory.createObjectLiteral());
    FunctionDeclaration function = nodeFactory.createFunctionDeclaration("function", constructor, null, null);
    Node prototypeAccessNode = nodeFactory.createGetProperty(nodeFactory.createThis(), "prototype");
    Node nestedPrototypeAccessNode = nodeFactory.createGetProperty(prototypeAccessNode, "prototype");
    function.setBody(nodeFactory.createStatement(nodeFactory.createAssignment(nestedPrototypeAccessNode, nodeFactory.createNull())));
    constructor.getBody().addChildren(function);
    return constructor;
  }
}