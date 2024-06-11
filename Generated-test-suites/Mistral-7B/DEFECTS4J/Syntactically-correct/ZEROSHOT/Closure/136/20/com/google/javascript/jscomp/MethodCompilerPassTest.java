import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass() {
    AbstractCompiler compilerMock = Mockito.mock(AbstractCompiler.class);

    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compilerMock) {
      @Override
      Callback getActingCallback() {
        return new StubMethodCompilerPassCallback();
      }

      @Override
      SignatureStore getSignatureStore() {
        return new StubSignatureStore();
      }
    };

    // Test empty case
    methodCompilerPass.process(null, null);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethods);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Collections.emptySet(), methodCompilerPass.nonMethodProperties);
    assertEquals(Collections.emptyMultimap(MethodCompilerPassTest.class), methodCompilerPass.methodDefinitions);

    // Test externs case with no methods
    Node externsNode = Mockito.mock(Node.class);
    methodCompilerPass.process(externsNode, null);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethods);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Collections.emptySet(), methodCompilerPass.nonMethodProperties);
    assertEquals(Collections.emptyMultimap(MethodCompilerPassTest.class), methodCompilerPass.methodDefinitions);

    // Test externs case with methods
    Node externsWithMethodsNode = Mockito.mock(Node.class);
    Set<String> externMethodsExpected = Sets.newHashSet("foo", "bar");
    Set<String> externMethodsWithoutSignaturesExpected = Sets.newHashSet("baz");
    Set<String> nonMethodPropertiesExpected = Sets.newHashSet("qux");
    Multimap<String, Node> methodDefinitionsExpected = HashMultimap.create();
    methodDefinitionsExpected.put("foo", Mockito.mock(Node.class));
    methodDefinitionsExpected.put("bar", Mockito.mock(Node.class));

    Mockito.when(externsWithMethodsNode.getFirstChild())
        .thenReturn(Mockito.mock(Node.class));
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext())
        .thenReturn(Mockito.mock(Node.class));
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getFirstChild())
        .thenReturn(Mockito.mock(Node.class));
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getFirstChild().getNext())
        .thenReturn(Mockito.mock(Node.class));
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getFirstChild().getNext().getFirstChild())
        .thenReturn(Mockito.mock(Node.class));
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getFirstChild().getNext().getFirstChild().getNext())
        .thenReturn(Mockito.mock(Node.class));
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getFirstChild().getNext().getFirstChild().getNext().getFirstChild())
        .thenReturn(Mockito.mock(Node.class));

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getString())
        .thenReturn("foo");
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getString())
        .thenReturn("bar");
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getString())
        .thenReturn("baz");
    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getString())
        .thenReturn("qux");

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getType())
        .thenReturn(Token.OBJECTLIT);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getFirstChild().getType())
        .thenReturn(Token.STRING);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getFirstChild().getNext().getType())
        .thenReturn(Token.ASSIGN);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getFirstChild().getNext().getNext().getType())
        .thenReturn(Token.FUNCTION);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getType())
        .thenReturn(Token.STRING);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getType())
        .thenReturn(Token.FUNCTION);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getType())
        .thenReturn(Token.STRING);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getNext().getType())
        .thenReturn(Token.FUNCTION);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getNext().getFirstChild().getType())
        .thenReturn(Token.STRING);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getNext().getFirstChild().getNext().getType())
        .thenReturn(Token.STRING);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getNext().getFirstChild().getNext().getNext().getType())
        .thenReturn(Token.FUNCTION);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getNext().getFirstChild().getNext().getNext().getFirstChild().getType())
        .thenReturn(Token.STRING);

    Mockito.when(externsWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getNext().getFirstChild().getNext().getNext().getNext().getType())
        .thenReturn(Token.FUNCTION);

    Mockito.when(compilerMock.isIdeMode())
        .thenReturn(false);

    methodCompilerPass.process(externsWithMethodsNode, null);
    assertEquals(externMethodsExpected, methodCompilerPass.externMethods);
    assertEquals(externMethodsWithoutSignaturesExpected, methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(nonMethodPropertiesExpected, methodCompilerPass.nonMethodProperties);
    assertEquals(methodDefinitionsExpected, methodCompilerPass.methodDefinitions);

    // Test root case with no methods
    Node rootNode = Mockito.mock(Node.class);
    methodCompilerPass.process(null, rootNode);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethods);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Collections.emptySet(), methodCompilerPass.nonMethodProperties);
    assertEquals(Collections.emptyMultimap(MethodCompilerPassTest.class), methodCompilerPass.methodDefinitions);

    // Test root case with methods
    Node rootWithMethodsNode = Mockito.mock(Node.class);

    Set<String> externMethodsExpectedRoot = Sets.newHashSet("foo", "bar");
    Set<String> externMethodsWithoutSignaturesExpectedRoot = Sets.newHashSet("baz");
    Set<String> nonMethodPropertiesExpectedRoot = Sets.newHashSet("qux");
    Multimap<String, Node> methodDefinitionsExpectedRoot = HashMultimap.create();
    methodDefinitionsExpectedRoot.put("foo", Mockito.mock(Node.class));
    methodDefinitionsExpectedRoot.put("bar", Mockito.mock(Node.class));

    Mockito.when(rootWithMethodsNode.getType())
        .thenReturn(Token.OBJECTLIT);

    Mockito.when(rootWithMethodsNode.getFirstChild())
        .thenReturn(Mockito.mock(Node.class));

    Mockito.when(rootWithMethodsNode.getFirstChild().getType())
        .thenReturn(Token.STRING);

    Mockito.when(rootWithMethodsNode.getFirstChild().getString())
        .thenReturn("foo");

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext())
        .thenReturn(Mockito.mock(Node.class));

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getType())
        .thenReturn(Token.FUNCTION);

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getFirstChild())
        .thenReturn(Mockito.mock(Node.class));

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getFirstChild().getType())
        .thenReturn(Token.STRING);

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getFirstChild().getString())
        .thenReturn("bar");

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext())
        .thenReturn(Mockito.mock(Node.class));

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getType())
        .thenReturn(Token.ASSIGN);

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getNext())
        .thenReturn(Mockito.mock(Node.class));

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getNext().getType())
        .thenReturn(Token.FUNCTION);

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getNext().getFirstChild())
        .thenReturn(Mockito.mock(Node.class));

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getNext().getFirstChild().getType())
        .thenReturn(Token.STRING);

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getNext().getFirstChild().getString())
        .thenReturn("baz");

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext())
        .thenReturn(Mockito.mock(Node.class));

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getType())
        .thenReturn(Token.STRING);

    Mockito.when(rootWithMethodsNode.getFirstChild().getNext().getNext().getNext().getNext().getString())
        .thenReturn("qux");

    Mockito.when(compilerMock.isIdeMode())
        .thenReturn(false);

    methodCompilerPass.process(null, rootWithMethodsNode);
    assertEquals(externMethodsExpectedRoot, methodCompilerPass.externMethods);
    assertEquals(externMethodsWithoutSignaturesExpectedRoot, methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(nonMethodPropertiesExpectedRoot, methodCompilerPass.nonMethodProperties);
    assertEquals(methodDefinitionsExpectedRoot, methodCompilerPass.methodDefinitions);
  }

  private static class StubMethodCompilerPassCallback implements Callback {
    @Override
    public void process(NodeTraversal t, Node n, Node parent) {}
  }

  private static class StubSignatureStore implements MethodCompilerPass.SignatureStore {
    private Multimap<String, Node> signatures = HashMultimap.create();

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }

    @Override
    public void removeSignature(String functionName) {}

    @Override
    public void reset() {
      signatures.clear();
    }

    public Multimap<String, Node> getSignatures() {
      return signatures;
    }
  }
}