import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    signatureStore = mock(SignatureStore.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        // Placeholder for the actual callback
        return new Callback() {
          @Override
          public void call(Node node) {
            // Empty implementation
          }
        };
      }
    };
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    Node externsNode = createExternsNode("externs.js");
    Node rootNode = createRootNode("main.js");

    methodCompilerPass.process(externsNode, rootNode);

    // Check that externMethodsWithoutSignatures contains the expected entries
    assertFalse(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("foo"));
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("bar"));
    assertFalse(methodCompilerPass.externMethodsWithoutSignatures.contains("baz"));

    // Check that externMethods contains the expected entries
    assertFalse(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethods.contains("foo"));
    assertTrue(methodCompilerPass.externMethods.contains("bar"));
    assertFalse(methodCompilerPass.externMethods.contains("baz"));
  }

  @Test
  public void testExternMethodsWithSignatures() {
    Node externsNode = createExternsNode("externs.js");
    Node rootNode = createRootNode("main.js");

    methodCompilerPass.process(externsNode, rootNode);

    // Check that getSignatureStore contains the expected signatures
    verify(signatureStore, times(2)).addSignature("foo", anyNode(), "externs.js");
    verify(signatureStore, times(1)).addSignature("bar", anyNode(), "externs.js");
    verifyNoMoreInteractions(signatureStore);
  }

  @Test
  public void testMethodsDefinedInCode() {
    Node externsNode = null;
    Node rootNode = createRootNode("main.js");

    methodCompilerPass.process(externsNode, rootNode);

    // Check that methodDefinitions contains the expected entries
    List<Node> methodDefinitions = methodCompilerPass.methodDefinitions.asList();
    assertEquals(3, methodDefinitions.size());

    Node fooMethodDefinition = methodDefinitions.get(0);
    assertEquals("foo", methodCompilerPass.getFunctionName(fooMethodDefinition));

    Node barMethodDefinition = methodDefinitions.get(1);
    assertEquals("bar", methodCompilerPass.getFunctionName(barMethodDefinition));

    Node bazMethodDefinition = methodDefinitions.get(2);
    assertEquals("baz", methodCompilerPass.getFunctionName(bazMethodDefinition));
  }

  private Node createExternsNode(String fileName) {
    // Create a mock Node for the externs file
    Node externsNode = mock(Node.class);
    when(externsNode.getToken()).thenReturn(new Token(Token.OBJECTLIT, 0, 0));

    // Add a mock Node for the "externs" property
    Node externsPropertyNode = createPropertyNode("externs", externsNode);

    // Add a mock Node for the array of extern methods
    Node externMethodsNode = createArrayNode();

    // Add two mock Nodes for the extern methods without signatures
    Node fooNodeWithoutSignature = createFunctionNode("foo");
    Node barNodeWithoutSignature = createFunctionNode("bar");
    externMethodsNode.setFirstChild(fooNodeWithoutSignature);
    externMethodsNode.setNext(barNodeWithoutSignature);

    // Add the externMethods Node as the value of the "externs" property
    externsPropertyNode.setNext(externMethodsNode);

    // Set the externsNode as the child of the rootNode
    externsNode.setParent(rootNode);

    return externsNode;
  }

  private Node createRootNode(String fileName) {
    // Create a mock Node for the rootNode
    Node rootNode = mock(Node.class);
    when(rootNode.getToken()).thenReturn(new Token(Token.PROGRAM, 0, 0));

    return rootNode;
  }

  private Node createPropertyNode(String name, Node value) {
    Node propertyNode = new Node(Token.GETPROP, 0, 0);
    propertyNode.setFirstChild(new Node(Token.NAME, 0, 0, name));
    propertyNode.setNext(value);

    return propertyNode;
  }

  private Node createArrayNode() {
    Node arrayNode = new Node(Token.ARRAYLIT, 0, 0);

    return arrayNode;
  }

  private Node createFunctionNode(String name) {
    Node functionNode = new Node(Token.FUNCTION, 0, 0);
    functionNode.setName(name);

    return functionNode;
  }

  private String getFunctionName(Node node) {
    if (node.getType() == Token.FUNCTION) {
      return node.getName();
    }

    throw new AssertionError("Node is not a function node: " + node);
  }
}