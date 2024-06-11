import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    signatureStore = Mockito.spy(new SignatureStore() {
      private Multimap<String, Node> methodDefinitions = HashMultimap.create();

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        methodDefinitions.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {}

      @Override
      public void reset() {
        methodDefinitions.clear();
      }

      public Multimap<String, Node> getMethodDefinitions() {
        return methodDefinitions;
      }
    });

    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return Mockito.mock(Callback.class);
      }
    };
  }

  @Test
  public void testEmpty() {
    methodCompilerPass.process(null, null);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethods);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Collections.emptySet(), methodCompilerPass.nonMethodProperties);
    assertNull(methodCompilerPass.methodDefinitions.get("method1"));
  }

  @Test
  public void testExterns() {
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    String functionName = "method1";
    Node functionNode = Mockito.mock(Node.class);

    methodCompilerPass.process(externs, root);

    Mockito.verify(compiler, Mockito.times(1)).isIdeMode();

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Mockito.when(compiler.getTraverser()).thenReturn(traversal);

    GetExternMethods getExternMethods = new GetExternMethods();
    Mockito.when(traversal.getSourceName()).thenReturn("externs.js");

    Mockito.when(externs.getType()).thenReturn(Token.OBJECTLIT);

    Mockito.when(externs.getFirstChild()).thenReturn(getExternMethods);

    getExternMethods.visit(Mockito.eq(traversal), Mockito.eq(externs), Mockito.eq(null));

    Mockito.verify(signatureStore, Mockito.times(1)).addSignature(functionName, functionNode, "externs.js");
    Mockito.verify(signatureStore, Mockito.times(1)).removeSignature("method2");
    Mockito.verify(methodCompilerPass.methodDefinitions, Mockito.times(1)).put(functionName, functionNode);

    assertEquals(Sets.newHashSet("method1"), methodCompilerPass.externMethods);
    assertEquals(Sets.newHashSet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Sets.newHashSet(), methodCompilerPass.nonMethodProperties);
    assertSame(functionNode, methodCompilerPass.methodDefinitions.get("method1"));
  }

  @Test
  public void testMethods() {
    String functionName = "method1";
    Node functionNode = Mockito.mock(Node.class);
    String sourceFile = "source.js";

    methodCompilerPass.process(null, null);

    Node root = Mockito.mock(Node.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Mockito.when(compiler.getTraverser()).thenReturn(traversal);

    GatherSignatures gatherSignatures = new GatherSignatures();

    Mockito.when(root.getType()).thenReturn(Token.PROGRAM);

    Mockito.when(root.getFirstChild()).thenReturn(gatherSignatures);

    gatherSignatures.visit(Mockito.eq(traversal), Mockito.eq(root), Mockito.eq(null));

    Mockito.when(root.getFirstChild().getNext()).thenReturn(functionNode);

    gatherSignatures.visit(Mockito.eq(traversal), Mockito.eq(root.getFirstChild()), Mockito.eq(root));

    Mockito.verify(signatureStore, Mockito.times(1)).addSignature(functionName, functionNode, sourceFile);
    Mockito.verify(methodCompilerPass.methodDefinitions, Mockito.times(1)).put(functionName, functionNode);

    assertEquals(Sets.newHashSet("method1"), methodCompilerPass.externMethods);
    assertEquals(Sets.newHashSet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Sets.newHashSet(), methodCompilerPass.nonMethodProperties);
    assertSame(functionNode, methodCompilerPass.methodDefinitions.get("method1"));
  }

  @Test
  public void testNonMethodProperties() {
    String propertyName = "property1";

    methodCompilerPass.process(null, null);

    Node root = Mockito.mock(Node.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Mockito.when(compiler.getTraverser()).thenReturn(traversal);

    GetExternMethods getExternMethods = new GetExternMethods();

    Mockito.when(root.getType()).thenReturn(Token.PROGRAM);

    Mockito.when(root.getFirstChild()).thenReturn(getExternMethods);

    getExternMethods.visit(Mockito.eq(traversal), Mockito.eq(root), Mockito.eq(null));

    Mockito.when(root.getFirstChild().getNext()).thenReturn(getExternMethods);

    getExternMethods.visit(Mockito.eq(traversal), Mockito.eq(root.getFirstChild()), Mockito.eq(root));

    Mockito.verify(signatureStore, Mockito.never()).addSignature(propertyName, Mockito.any(Node.class), Mockito.anyString());
    Mockito.verify(methodCompilerPass.methodDefinitions, Mockito.never()).put(propertyName, Mockito.any(Node.class));
    assertFalse(methodCompilerPass.externMethods.contains(propertyName));
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains(propertyName));
    assertTrue(methodCompilerPass.nonMethodProperties.contains(propertyName));
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    String functionName = "method1";
    Node functionNode = Mockito.mock(Node.class);

    methodCompilerPass.process(null, null);

    Node root = Mockito.mock(Node.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Mockito.when(compiler.getTraverser()).thenReturn(traversal);

    GetExternMethods getExternMethods = new GetExternMethods();

    Mockito.when(root.getType()).thenReturn(Token.PROGRAM);

    Mockito.when(root.getFirstChild()).thenReturn(getExternMethods);

    getExternMethods.visit(Mockito.eq(traversal), Mockito.eq(root), Mockito.eq(null));

    Mockito.when(root.getFirstChild().getNext()).thenReturn(functionNode);

    getExternMethods.visit(Mockito.eq(traversal), Mockito.eq(root.getFirstChild()), Mockito.eq(root));

    Mockito.verify(signatureStore, Mockito.never()).addSignature(functionName, Mockito.any(Node.class), Mockito.anyString());
    Mockito.verify(methodCompilerPass.methodDefinitions, Mockito.never()).put(functionName, Mockito.any(Node.class));
    assertFalse(methodCompilerPass.externMethods.contains(functionName));
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains(functionName));
  }

  @Test
  public void testActingCallback() {
    String functionName = "method1";
    Node functionNode = Mockito.mock(Node.class);
    String sourceFile = "source.js";

    methodCompilerPass.process(null, null);

    Callback actingCallback = Mockito.mock(Callback.class);

    Mockito.when(actingCallback.process(Mockito.eq(methodCompilerPass), Mockito.eq(functionName), Mockito.eq(functionNode), Mockito.eq(sourceFile)))
        .thenReturn(true);

    methodCompilerPass.setActingCallback(actingCallback);

    Node root = Mockito.mock(Node.class);
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Mockito.when(compiler.getTraverser()).thenReturn(traversal);

    GatherSignatures gatherSignatures = new GatherSignatures();

    Mockito.when(root.getType()).thenReturn(Token.PROGRAM);

    Mockito.when(root.getFirstChild()).thenReturn(gatherSignatures);

    gatherSignatures.visit(Mockito.eq(traversal), Mockito.eq(root), Mockito.eq(null));

    Mockito.when(root.getFirstChild().getNext()).thenReturn(functionNode);

    gatherSignatures.visit(Mockito.eq(traversal), Mockito.eq(root.getFirstChild()), Mockito.eq(root));

    Mockito.verify(signatureStore, Mockito.times(1)).addSignature(functionName, functionNode, sourceFile);
    Mockito.verify(methodCompilerPass.methodDefinitions, Mockito.times(1)).put(functionName, functionNode);
    Mockito.verify(actingCallback, Mockito.times(1)).process(Mockito.eq(methodCompilerPass), Mockito.eq(functionName), Mockito.eq(functionNode), Mockito.eq(sourceFile));

    assertEquals(Sets.newHashSet("method1"), methodCompilerPass.externMethods);
    assertEquals(Sets.newHashSet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Sets.newHashSet(), methodCompilerPass.nonMethodProperties);
    assertSame(functionNode, methodCompilerPass.methodDefinitions.get("method1"));
  }

  @Test
  public void testReset() {
    methodCompilerPass.process(null, null);

    assertEquals(Sets.newHashSet(), methodCompilerPass.externMethods);
    assertEquals(Sets.newHashSet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Sets.newHashSet(), methodCompilerPass.nonMethodProperties);
    assertNull(methodCompilerPass.methodDefinitions.get("method1"));

    methodCompilerPass.reset();

    assertEquals(Collections.emptySet(), methodCompilerPass.externMethods);
    assertEquals(Collections.emptySet(), methodCompilerPass.externMethodsWithoutSignatures);
    assertEquals(Collections.emptySet(), methodCompilerPass.nonMethodProperties);
    assertNull(methodCompilerPass.methodDefinitions.get("method1"));
  }
}

class GetExternMethods extends AbstractPostOrderCallback {
  // ...
}

class GatherSignatures extends AbstractPostOrderCallback {
  // ...
}