import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private Node rootNode;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setUp() {
    compiler = Mockito.mock(Compiler.class);
    input = new CompilerInput("test.js", "test");
    rootNode = NodeUtil.parseJsFile("test.js");
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testProcess_noTypeChecksAddedForNonConstructorFunctions() {
    runtimeTypeCheck.process(null, rootNode);

    // No checks should have been added for non-constructor functions.
    Node functionNode = NodeUtil.findFunctionByName(rootNode, "noConstructor");
    assertTrue(functionNode.getLastChild().getChildren().isEmpty());
  }

  @Test
  public void testProcess_checksAddedForConstructorFunctions() {
    runtimeTypeCheck.process(null, rootNode);

    // Checks should have been added for constructor functions.
    Node constructorNode = NodeUtil.findFunctionByName(rootNode, "constructor");
    Node checkNode = findCheckNode(constructorNode);
    assertNotNull(checkNode);
  }

  @Test
  public void testProcess_checksAddedForParameters() {
    runtimeTypeCheck.process(null, rootNode);

    // Checks should have been added for constructor function parameters.
    Node constructorNode = NodeUtil.findFunctionByName(rootNode, "constructor");
    Node paramNode = constructorNode.getFirstChild().getFirstChild();
    Node checkNode = findCheckNode(paramNode);
    assertNotNull(checkNode);
  }

  @Test
  public void testProcess_checksAddedForReturnValues() {
    runtimeTypeCheck.process(null, rootNode);

    // Checks should have been added for constructor function return values.
    Node constructorNode = NodeUtil.findFunctionByName(rootNode, "constructor");
    Node returnNode = constructorNode.getLastChild();
    Node checkNode = findCheckNode(returnNode);
    assertNotNull(checkNode);
  }

  @Test
  public void testProcess_checksAddedForInterfaceImplementations() {
    input.setSource("Foo.js", "Foo implements Bar {}");
    compiler.setInput(input);
    rootNode = NodeUtil.parseJsFile("test.js");

    runtimeTypeCheck.process(null, rootNode);

    // Checks should have been added for interface implementations.
    Node constructorNode = NodeUtil.findFunctionByName(rootNode, "constructor");
    Node checkNode = findCheckNode(constructorNode);
    assertNotNull(checkNode);

    Node interfaceCheckerNode = findCheckerNode(checkNode, "Bar");
    assertNotNull(interfaceCheckerNode);
  }

  @Test
  public void testProcess_checksAddedForExternClasses() {
    input.setSource("Foo.js", "class Foo {}");
    input.setExtern(true);
    compiler.setInput(input);
    rootNode = NodeUtil.parseJsFile("test.js");

    runtimeTypeCheck.process(null, rootNode);

    // Checks should have been added for extern classes.
    Node constructorNode = NodeUtil.findFunctionByName(rootNode, "constructor");
    Node checkNode = findCheckNode(constructorNode);
    assertNotNull(checkNode);

    Node classCheckerNode = findCheckerNode(checkNode, "Foo");
    assertNotNull(classCheckerNode);
  }

  @Test
  public void testProcess_checksAddedForValueTypes() {
    Node parameterNode = new Node(Token.VAR, "param", new Node(Token.NUMBER, 0));
    Node checkNode = runtimeTypeCheck.createCheckTypeCallNode(new JSType(Token.NUMBER), parameterNode);
    assertNotNull(checkNode);
  }

  @Test
  public void testProcess_checksAddedForClassTypes() {
    Node classNameNode = new Node(Token.STRING, "Foo");
    Node checkNode = runtimeTypeCheck.createCheckTypeCallNode(new ObjectType("Foo"), classNameNode);
    assertNotNull(checkNode);
  }

  @Test
  public void testProcess_checksAddedForInterfaceTypes() {
    Node classNameNode = new Node(Token.STRING, "Bar");
    Node checkNode = runtimeTypeCheck.createCheckTypeCallNode(new ObjectType("Bar"), classNameNode);
    assertNotNull(checkNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_thowsExceptionForInvalidType() {
    Node invalidTypeNode = new Node(Token.NULL);
    runtimeTypeCheck.createCheckTypeCallNode(new JSType("invalidType"), invalidTypeNode);
  }

  @Test
  public void testProcess_boilerplateCodeAdded() {
    Node boilerplateCodeNode = runtimeTypeCheck.getBoilerplateCode(compiler, null).getFirstChild();
    assertTrue(boilerplateCodeNode.getChildren().size() > 0);
  }

  private Node findCheckNode(Node node) {
    return Iterables.find(node.getChildren(), checkNode -> checkNode instanceof Node && checkNode.getType() == Token.CALL);
  }

  private Node findCheckerNode(Node checkNode, String checkerName) {
    return Iterables.find(checkNode.getChildren(), arrayNode -> arrayNode instanceof Node && NodeUtil.getIdentifier(arrayNode).getText().equals(checkerName));
  }
}