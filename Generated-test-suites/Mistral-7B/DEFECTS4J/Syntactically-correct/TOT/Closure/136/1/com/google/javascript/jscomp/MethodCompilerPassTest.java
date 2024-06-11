import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
        return new DoNothingCallback();
      }
    };
  }

  @Test
  public void testExternMethods() {
    // Given
    String source = "var foo = foo.bar;";
    Node externs = makeNodeFromSource("externs.js", source);
    Node root = makeNodeFromSource("script.js", "function bar() {}");

    // When
    methodCompilerPass.process(externs, root);

    // Then
    verify(signatureStore).addSignature("bar", mock(Node.class), "script.js");
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("bar", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertSame(methodCompilerPass.methodDefinitions.get("bar"), methodCompilerPass.methodDefinitions.values().iterator().next());
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    // Given
    String source = "var foo = foo.bar;";
    Node externs = makeNodeFromSource("externs.js", source);
    Node root = makeNodeFromSource("script.js", "function bar() {}");

    // When
    methodCompilerPass.process(externs, root);

    // Then
    verify(signatureStore).addSignature("foo", mock(Node.class), "script.js");
    assertEquals(2, methodCompilerPass.methodDefinitions.size());
    assertEquals("bar", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertEquals("foo", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertSame(methodCompilerPass.methodDefinitions.get("foo"), methodCompilerPass.methodDefinitions.values().iterator().next());
    assertSame(methodCompilerPass.methodDefinitions.get("bar"), methodCompilerPass.methodDefinitions.values().iterator().next());
    assertTrue(methodCompilerPass.externMethods.contains("bar"));
    assertFalse(methodCompilerPass.externMethods.contains("baz"));
  }

  @Test
  public void testNonMethodProperties() {
    // Given
    String source = "var foo = 'bar';";
    Node externs = null;
    Node root = makeNodeFromSource("script.js", source);

    // When
    methodCompilerPass.process(externs, root);

    // Then
    assertFalse(methodCompilerPass.methodDefinitions.isEmpty());
    assertFalse(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.contains("foo"));
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunction() {
    // Given
    String source = "var foo = foo.bar;";
    Node externs = null;
    Node root = makeNodeFromSource("script.js", source);

    // When
    methodCompilerPass.process(externs, root);

    // Then
    // Should not reach this point
  }

  @Test
  public void testMethodInObjectLiteral() {
    // Given
    String source = "var foo = { getBar: function() {} };";
    Node externs = null;
    Node root = makeNodeFromSource("script.js", source);

    // When
    methodCompilerPass.process(externs, root);

    // Then
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("getBar", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertSame(methodCompilerPass.methodDefinitions.get("getBar"), methodCompilerPass.methodDefinitions.values().iterator().next());
  }

  @Test
  public void testPrototype() {
    // Given
    String source = "var Foo = function() {};\n" +
            "Foo.prototype.getBar = function() {};";
    Node externs = null;
    Node root = makeNodeFromSource("script.js", source);

    // When
    methodCompilerPass.process(externs, root);

    // Then
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("getBar", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertSame(methodCompilerPass.methodDefinitions.get("getBar"), methodCompilerPass.methodDefinitions.values().iterator().next());
  }

  private Node makeNodeFromSource(String fileName, String source) {
    return new Node(new SourceNode(fileName, source, null, null));
  }

  private static class DoNothingCallback implements Callback {
    @Override
    public void call(NodeTraversal nodeTraversal, Node node, Node parent) {}
  }
}