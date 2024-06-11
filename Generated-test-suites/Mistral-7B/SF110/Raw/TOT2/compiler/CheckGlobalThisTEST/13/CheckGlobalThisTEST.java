import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.node.NodeFactory.createName;
import static com.google.javascript.rhino.node.NodeFactory.createNumber;
import static com.google.javascript.rhino.node.NodeFactory.createObject;
import static com.google.javascript.rhino.node.NodeFactory.createProperty;
import static com.google.javascript.rhino.node.NodeFactory.createThis;
import static com.google.javascript.rhino.node.NodeFactory.createAssign;
import static com.google.javascript.rhino.node.NodeFactory.createGetProp;
import static com.google.javascript.rhino.node.NodeFactory.createScript;

import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;

public class CheckGlobalThisTest {

  private Node root;
  private Compiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    root = createScript("function test() { this.x = 1; }");
  }

  @Test
  public void testGlobalThis() {
    NodeTraversal traverser = new NodeTraversal(compiler, root, checker);
    traverser.traverse();
    assertTrue(compiler.getErrors().length > 0);
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node functionNode = createFunctionDeclaration("@this MyFunction() {}");
    NodeTraversal traverser = new NodeTraversal(compiler, functionNode, checker);
    traverser.traverse();
    assertFalse(compiler.getErrors().length > 0);
  }

  @Test
  public void testPrototypeMethod() {
    Node functionNode = createFunctionDeclaration("function MyObject() { this.x = 1; }");
    Node prototype = createObject();
    Node prototypeProperty = createProperty("x", createNumber(1));
    prototype.setProperty("prototype", prototypeProperty);
    Node myObject = createObject("MyObject");
    Node assign = createAssign();
    assign.setFirstChild(createThis());
    assign.setLastChild(myObject);
    NodeTraversal traverser = new NodeTraversal(compiler, assign, checker);
    traverser.traverse();
    assertFalse(compiler.getErrors().length > 0);
  }

  @Test
  public void testConstructor() {
    Node functionNode = createFunctionDeclaration("function MyConstructor() {}");
    Node constructor = createAssign();
    constructor.setFirstChild(createThis());
    constructor.setLastChild(functionNode);
    NodeTraversal traverser = new NodeTraversal(compiler, constructor, checker);
    traverser.traverse();
    assertFalse(compiler.getErrors().length > 0);
  }

  @Test
  public void testFunctionWithNoThisAnnotation() {
    Node functionNode = createFunctionDeclaration("function MyFunction() {}");
    NodeTraversal traverser = new NodeTraversal(compiler, functionNode, checker);
    traverser.traverse();
    assertFalse(compiler.getErrors().length > 0);
  }

  @Test
  public void testFunctionWithMultipleStatements() {
    Node functionNode = createFunctionDeclaration("function test() { this.x = 1; this.y = 2; }");
    NodeTraversal traverser = new NodeTraversal(compiler, functionNode, checker);
    traverser.traverse();
    assertTrue(compiler.getErrors().length > 0);
  }

  @Test
  public void testGetProperty() {
    Node functionNode = createFunctionDeclaration("function test() { this.x.y = 1; }");
    NodeTraversal traverser = new NodeTraversal(compiler, functionNode, checker);
    traverser.traverse();
    assertTrue(compiler.getErrors().length > 0);
  }

  @Test
  public void testGetPrototypeProperty() {
    Node functionNode = createFunctionDeclaration("function test() { Object.prototype.x.y = 1; }");
    NodeTraversal traverser = new NodeTraversal(compiler, functionNode, checker);
    traverser.traverse();
    assertFalse(compiler.getErrors().length > 0);
  }

  @Test
  public void testObjectLiteral() {
    Node root = createObject("{ x: function() { this.y = 1; } }");
    NodeTraversal traverser = new NodeTraversal(compiler, root, checker);
    traverser.traverse();
    assertFalse(compiler.getErrors().length > 0);
  }

  private Node createFunctionDeclaration(String jsDocInfo) {
    Node functionNode = createScript(String.format("function %s { %s }", "MyFunction", jsDocInfo));
    NodeFunction nodeFunction = (NodeFunction) functionNode;
    nodeFunction.setName(createName("MyFunction"));
    return functionNode;
  }

  private Node createScript(String code) {
    return createScript(code, null, null, null, null, null);
  }

  private Node createScript(String code, Node parent, int line, int lineTextStart, int lineTextEnd, int lineNumber) {
    return createScript(code, null, null, parent, line, lineTextStart, lineTextEnd, lineNumber);
  }

  private Node createScript(String code, Node parent, int line, int lineTextStart, int lineTextEnd, int lineNumber, Node lineno) {
    return createScript(code, null, null, parent, line, lineTextStart, lineTextEnd, lineNumber, lineno);
  }

  private Node createScript(String code, Node parent, int line, int lineTextStart, int lineTextEnd, int lineNumber, Node lineno, Node source) {
    return createScript(code, null, null, parent, line, lineTextStart, lineTextEnd, lineNumber, lineno, source);
  }

  private Node createScript(String code, Node parent, int line, int lineTextStart, int lineTextEnd, int lineNumber, Node lineno, Node source, Node sourceURL) {
    return createScript(code, null, null, parent, line, lineTextStart, lineTextEnd, lineNumber, lineno, source, sourceURL);
  }

  private Node createScript(String code, Node parent, int line, int lineTextStart, int lineTextEnd, int lineNumber, Node lineno, Node source, Node sourceURL, Node extra) {
    return createScript(code, null, null, parent, line, lineTextStart, lineTextEnd, lineNumber, lineno, source, sourceURL, extra);
  }

  private Node createScript(String code, Node parent, int line, int lineTextStart, int lineTextEnd, int lineNumber, Node lineno, Node source, Node sourceURL, Node extra, Node... children) {
    Node script = createScript(code, null, null, parent, 0, 0, 0, 0, null, null, null, null, null, children);
    script.setLine(lineNumber);
    script.setLineTextPosition(lineTextStart, lineTextEnd);
    script.setLineNumber(lineno);
    script.setSource(source);
    script.setSourceURL(sourceURL);
    if (extra != null) {
      script.setExtra(extra);
    }
    return script;
  }
}