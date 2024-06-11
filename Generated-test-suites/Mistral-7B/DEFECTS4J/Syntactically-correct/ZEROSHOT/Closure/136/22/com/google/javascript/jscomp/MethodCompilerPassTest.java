import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureMock();
      }

      @Override
      Callback getActingCallback() {
        return Mockito.mock(Callback.class);
      }
    };

    // Expected method definitions
    String[] expectedMethods = {"method1", "method2", "method3"};

    // Setup source code
    Node root = Mockito.mock(Node.class);
    Node externs = Mockito.mock(Node.class);

    // Set up method definitions in root
    for (String methodName : expectedMethods) {
      Node methodNode = Mockito.mock(Node.class);
      Mockito.when(root.getChild(0)).thenReturn(methodNode);
      Mockito.when(methodNode.getType()).thenReturn(Token.FUNCTION);

      // Set up method definition in externs
      Node externMethodNode = Mockito.mock(Node.class);
      Mockito.when(externs.getChild(0)).thenReturn(externMethodNode);
      Mockito.when(externMethodNode.getType()).thenReturn(Token.OBJECTLIT);
      Node externsMethodNode = Mockito.mock(Node.class);
      Mockito.when(externMethodNode.getFirstChild()).thenReturn(externsMethodNode);
      Mockito.when(externsMethodNode.getType()).thenReturn(Token.STRING);
      Mockito.when(externsMethodNode.getString()).thenReturn(methodName);
    }

    // Call process method
    methodCompilerPass.process(externs, root);

    // Assert expected method definitions are present
    Multimap<String, Node> actualMethods = methodCompilerPass.methodDefinitions;
    for (String methodName : expectedMethods) {
      Node actualMethodNode = actualMethods.get(methodName).iterator().next();
      assertEquals("Unexpected method node type", Token.FUNCTION, actualMethodNode.getType());
      assertEquals("Unexpected method name", methodName, actualMethodNode.getFirstChild().getString());
    }
  }

  private static class SignatureMock implements SignatureStore {

    private Multimap<String, Node> signatures = HashMultimap.create();

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }

    @Override
    public void removeSignature(String functionName) {
      signatures.remove(functionName);
    }

    @Override
    public void reset() {
      signatures.clear();
    }

    public Multimap<String, Node> getSignatures() {
      return signatures;
    }
  }
}