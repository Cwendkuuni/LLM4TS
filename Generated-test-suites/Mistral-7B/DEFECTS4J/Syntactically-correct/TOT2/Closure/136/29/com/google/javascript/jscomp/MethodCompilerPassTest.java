import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class MethodCompilerPassTest {

  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setup() {
    methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = new SignatureStore() {
            private Multimap<String, Node> signatures = HashMultimap.create();

            @Override
            public void reset() {
              signatures.clear();
            }

            @Override
            public void addSignature(String functionName, Node functionNode, String sourceFile) {
              signatures.put(functionName, functionNode);
            }

            @Override
            public void removeSignature(String functionName) {
              signatures.remove(functionName);
            }
          };
        }
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        // no action required for testing
        return null;
      }
    };
  }

  /**
   * Test for a simple method declaration with a signature in the source code.
   */
  @Test
  public void testSimpleMethodWithSignature() {
    Node rootNode = new Node(Token.PROGRAM);
    Node externsNode = null;

    methodCompilerPass.process(externsNode, rootNode);

    String methodName = "simpleMethod";
    Node functionNode = new Node(Token.FUNCTION);
    Node functionBody = new Node(Token.BLOCK);
    functionNode.setLineNumber(1);
    functionNode.setLinePositionInLine(1);
    functionBody.setLineNumber(1);
    functionBody.setLinePositionInLine(1);
    functionNode.setFirstChild(functionBody);

    Node methodDeclarationNode = new Node(Token.ASSIGN);
    methodDeclarationNode.setLineNumber(2);
    methodDeclarationNode.setLinePositionInLine(1);
    methodDeclarationNode.setFirstChild(new Node(Token.GETPROP, null, "prototype"));
    methodDeclarationNode.getFirstChild().setLineNumber(2);
    methodDeclarationNode.getFirstChild().setLinePositionInLine(2);
    methodDeclarationNode.getFirstChild().setNext(new Node(Token.STRING, methodName));
    methodDeclarationNode.getFirstChild().getNext().setLineNumber(2);
    methodDeclarationNode.getFirstChild().getNext().setLinePositionInLine(3);
    methodDeclarationNode.setNext(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(methodCompilerPass.compiler);
    nodeTraversal.traverse(nodeTraversal.getRootNode(), rootNode);

    methodCompilerPass.addPossibleSignature(methodName, methodDeclarationNode.getFirstChild().getNext(), nodeTraversal);

    Node expectedFunctionNode = signatures.get(methodName).getFirstChild();
    assertEquals("Expected function node to match", functionNode, expectedFunctionNode);
  }

  /**
   * Test for a simple method declaration without a signature in the source code.
   */
  @Test
  public void testSimpleMethodWithoutSignature() {
    Node rootNode = new Node(Token.PROGRAM);

    methodCompilerPass.process(null, rootNode);

    String methodName = "simpleMethod";
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setLineNumber(1);
    functionNode.setLinePositionInLine(1);
    functionBody.setLineNumber(1);
    functionBody.setLinePositionInLine(1);
    functionNode.setFirstChild(functionBody);

    Node methodDeclarationNode = new Node(Token.ASSIGN);
    methodDeclarationNode.setLineNumber(2);
    methodDeclarationNode.setLinePositionInLine(1);
    methodDeclarationNode.setFirstChild(new Node(Token.GETPROP, null, "prototype"));
    methodDeclarationNode.getFirstChild().setLineNumber(2);
    methodDeclarationNode.getFirstChild().setLinePositionInLine(2);
    methodDeclarationNode.getFirstChild().setNext(new Node(Token.STRING, methodName));
    methodDeclarationNode.getFirstChild().getNext().setLineNumber(2);
    methodDeclarationNode.getFirstChild().getNext().setLinePositionInLine(3);
    methodDeclarationNode.setNext(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(methodCompilerPass.compiler);
    nodeTraversal.traverse(nodeTraversal.getRootNode(), rootNode);

    methodCompilerPass.addPossibleSignature(methodName, methodDeclarationNode.getFirstChild().getNext(), nodeTraversal);

    assertFalse("Expected method to not be present in signatures", signatures.containsKey(methodName));
  }

  /**
   * Test for a method declaration with a name that is not a method.
   */
  @Test
  public void testNonMethodProperty() {
    Node rootNode = new Node(Token.PROGRAM);

    methodCompilerPass.process(null, rootNode);

    String nonMethodPropertyName = "simpleProperty";

    Node nonMethodPropertyNode = new Node(Token.ASSIGN);
    nonMethodPropertyNode.setLineNumber(1);
    nonMethodPropertyNode.setLinePositionInLine(1);
    nonMethodPropertyNode.setFirstChild(new Node(Token.GETPROP, null, "prototype"));
    nonMethodPropertyNode.getFirstChild().setLineNumber(1);
    nonMethodPropertyNode.getFirstChild().setLinePositionInLine(1);
    nonMethodPropertyNode.getFirstChild().setNext(new Node(Token.STRING, nonMethodPropertyName));
    nonMethodPropertyNode.getFirstChild().getNext().setLineNumber(1);
    nonMethodPropertyNode.getFirstChild().getNext().setLinePositionInLine(2);
    nonMethodPropertyNode.setNext(new Node(Token.NUMBER, 42));

    NodeTraversal nodeTraversal = new NodeTraversal(methodCompilerPass.compiler);
    nodeTraversal.traverse(nodeTraversal.getRootNode(), rootNode);

    methodCompilerPass.addPossibleSignature(nonMethodPropertyName, null, nodeTraversal);

    assertFalse("Expected property to not be present in signatures", signatures.containsKey(nonMethodPropertyName));
  }

  /**
   * Test for an error scenario where a method declaration does not have a name.
   */
  @Test
  public void testMethodWithoutName() {
    Node rootNode = new Node(Token.PROGRAM);

    methodCompilerPass.process(null, rootNode);

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setLineNumber(1);
    functionNode.setLinePositionInLine(1);
    functionBody.setLineNumber(1);
    functionBody.setLinePositionInLine(1);
    functionNode.setFirstChild(functionBody);

    Node methodDeclarationNode = new Node(Token.ASSIGN);
    methodDeclarationNode.setLineNumber(2);
    methodDeclarationNode.setLinePositionInLine(1);
    methodDeclarationNode.setNext(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(methodCompilerPass.compiler);
    nodeTraversal.traverse(nodeTraversal.getRootNode(), rootNode);

    methodCompilerPass.addPossibleSignature(null, methodDeclarationNode.getNext(), nodeTraversal);

    assertThrows("Expected an exception to be thrown", IllegalArgumentException.class, () -> {
      methodCompilerPass.addSignature("", functionNode, "");
    });
  }

  /**
   * Test for an error scenario where a method signature is added twice.
   */
  @Test
  public void testDuplicateMethodSignature() {
    Node rootNode = new Node(Token.PROGRAM);

    methodCompilerPass.process(null, rootNode);

    String methodName = "duplicateMethod";

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setLineNumber(1);
    functionNode.setLinePositionInLine(1);
    functionBody.setLineNumber(1);
    functionBody.setLinePositionInLine(1);
    functionNode.setFirstChild(functionBody);

    Node methodDeclarationNode1 = new Node(Token.ASSIGN);
    methodDeclarationNode1.setLineNumber(2);
    methodDeclarationNode1.setLinePositionInLine(1);
    methodDeclarationNode1.setFirstChild(new Node(Token.GETPROP, null, "prototype"));
    methodDeclarationNode1.getFirstChild().setLineNumber(2);
    methodDeclarationNode1.getFirstChild().setLinePositionInLine(1);
    methodDeclarationNode1.getFirstChild().setNext(new Node(Token.STRING, methodName));
    methodDeclarationNode1.getFirstChild().getNext().setLineNumber(2);
    methodDeclarationNode1.getFirstChild().getNext().setLinePositionInLine(2);
    methodDeclarationNode1.setNext(functionNode);

    Node methodDeclarationNode2 = new Node(Token.ASSIGN);
    methodDeclarationNode2.setLineNumber(3);
    methodDeclarationNode2.setLinePositionInLine(1);
    methodDeclarationNode2.setFirstChild(new Node(Token.GETPROP, null, "prototype"));
    methodDeclarationNode2.getFirstChild().setLineNumber(3);
    methodDeclarationNode2.getFirstChild().setLinePositionInLine(1);
    methodDeclarationNode2.getFirstChild().setNext(new Node(Token.STRING, methodName));
    methodDeclarationNode2.getFirstChild().getNext().setLineNumber(3);
    methodDeclarationNode2.getFirstChild().getNext().setLinePositionInLine(2);
    methodDeclarationNode2.setNext(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(methodCompilerPass.compiler);
    nodeTraversal.traverse(nodeTraversal.getRootNode(), rootNode);

    methodCompilerPass.addPossibleSignature(methodName, methodDeclarationNode1.getFirstChild().getNext(), nodeTraversal);
    methodCompilerPass.addPossibleSignature(methodName, methodDeclarationNode2.getFirstChild().getNext(), nodeTraversal);

    assertThrows("Expected an exception to be thrown", IllegalStateException.class, () -> {
      methodCompilerPass.addSignature(methodName, functionNode, "");
    });
  }

  /**
   * Test for a missing getSignatures callback.
   */
  @Test
  public void testMissingGetSignaturesCallback() {
    Node rootNode = new Node(Token.PROGRAM);

    MethodCompilerPass testClass = new MethodCompilerPass(null) {
      @Override
      Callback getActingCallback() {
        // no action required for testing
        return null;
      }

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }
    };

    assertThrows("Expected an exception to be thrown", UnsupportedOperationException.class, () -> {
      testClass.process(null, rootNode);
    });
  }
}