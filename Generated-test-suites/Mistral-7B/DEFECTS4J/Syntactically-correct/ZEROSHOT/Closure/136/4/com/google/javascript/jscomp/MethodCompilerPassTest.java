import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class MethodCompilerPassTest {

  private MethodCompilerPass pass;
  private SignatureStore signatureStoreMock;

  @Before
  public void setUp() {
    signatureStoreMock = Mockito.mock(SignatureStore.class);

    CompilerOptions options = new CompilerOptions();

    pass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStoreMock;
      }

      @Override
      Callback getActingCallback() {
        // no-op
        return new Callback() {};
      }
    };
  }

  @Test
  public void testClear() {
    pass.process(null, null);

    assertEquals(0, pass.externMethods.size());
    assertEquals(0, pass.externMethodsWithoutSignatures.size());
    assertEquals(0, pass.nonMethodProperties.size());
    assertEquals(0, pass.methodDefinitions.size());

    pass.process(null, null);

    assertEquals(0, pass.externMethods.size());
    assertEquals(0, pass.externMethodsWithoutSignatures.size());
    assertEquals(0, pass.nonMethodProperties.size());
    assertEquals(0, pass.methodDefinitions.size());
  }

  @Test
  public void testAddExternMethod() {
    Node externs = Node.newTextNode("function foo() {}");

    pass.process(externs, null);

    ArgumentCaptor<String> functionNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Node> functionNodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> sourceFileCaptor = ArgumentCaptor.forClass(String.class);

    Mockito.verify(signatureStoreMock).addSignature(functionNameCaptor.capture(), functionNodeCaptor.capture(), sourceFileCaptor.capture());

    String functionName = functionNameCaptor.getValue();
    Node functionNode = functionNodeCaptor.getValue();
    String sourceFile = sourceFileCaptor.getValue();

    assertEquals("foo", functionName);
    assertSame(Node.SCOPE_GLOBAL, functionNode.getParent().getNodeType());
    assertEquals("source.js", sourceFile);

    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
  }

  @Test
  public void testAddExternMethodWithoutSignature() {
    Node externs = Node.newTextNode("function foo() {}");

    pass.process(externs, null);

    ArgumentCaptor<String> functionNameCaptor = ArgumentCaptor.forClass(String.class);

    Mockito.verify(signatureStoreMock).removeSignature(functionNameCaptor.capture());

    String functionName = functionNameCaptor.getValue();

    assertEquals(1, pass.externMethodsWithoutSignatures.size());
    assertEquals("foo", pass.externMethodsWithoutSignatures.iterator().next());
  }

  @Test
  public void testAddNonMethodProperty() {
    Node root = Node.newTextNode("var x = 123;");

    pass.process(null, root);

    assertEquals(1, pass.nonMethodProperties.size());
    assertEquals("x", pass.nonMethodProperties.iterator().next());
  }

  @Test
  public void testCollectMethodDefinitions() {
    Node root = Node.newTextNode(
        "function foo() {}" +
        "function bar() {}" +
        "function baz(x) {}" +
        "function qux(x, y) {}");

    pass.process(null, root);

    ArgumentCaptor<String> functionNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Node> functionNodeCaptor = ArgumentCaptor.forClass(Node.class);

    Mockito.verify(signatureStoreMock, Mockito.times(4)).addSignature(functionNameCaptor.capture(), functionNodeCaptor.capture(), Mockito.anyString());

    List<String> functionNames = Lists.newArrayList(functionNameCaptor.getAllValues());
    List<Node> functionNodes = Lists.newArrayList(functionNodeCaptor.getAllValues());

    assertEquals(4, pass.methodDefinitions.size());

    for (int i = 0; i < 4; i++) {
      assertEquals(functionNames.get(i), pass.methodDefinitions.get(functionNames.get(i)).toString());
      assertSame(functionNodes.get(i), pass.methodDefinitions.get(functionNames.get(i)).getValue());
    }
  }

  @Test
  public void testFindMethodInMethodDefinitions() {
    Node root = Node.newTextNode(
        "function foo() {}" +
        "function bar() {}" +
        "function baz(x) {}" +
        "function qux(x, y) {}");

    pass.process(null, root);

    ArgumentCaptor<String> functionNameCaptor = ArgumentCaptor.forClass(String.class);

    Mockito.verify(signatureStoreMock, Mockito.times(4)).addSignature(functionNameCaptor.capture(), Mockito.any(Node.class), Mockito.anyString());

    String functionName = functionNameCaptor.getValue();

    assertSame(pass.methodDefinitions.get(functionName), pass.methodDefinitions.get("foo"));
  }

  @Test
  public void testFindNonExistentMethodReturnsNull() {
    Node root = Node.newTextNode(
        "function foo() {}" +
        "function bar() {}" +
        "function baz(x) {}" +
        "function qux(x, y) {}");

    pass.process(null, root);

    assertNull(pass.methodDefinitions.get("quux"));
  }

  @Test
  public void testResetSignatureStore() {
    Node root = Node.newTextNode(
        "function foo() {}" +
        "function bar() {}" +
        "function baz(x) {}" +
        "function qux(x, y) {}",
        "function quux() {}");

    pass.process(null, root);

    ArgumentCaptor<String> functionNameCaptor = ArgumentCaptor.forClass(String.class);

    Mockito.verify(signatureStoreMock, Mockito.times(5)).addSignature(functionNameCaptor.capture(), Mockito.any(Node.class), Mockito.anyString());

    List<String> functionNames = Lists.newArrayList(functionNameCaptor.getAllValues());

    pass.process(null, Node.newTextNode("function quux() {}"));

    Mockito.verify(signatureStoreMock, Mockito.times(1)).removeSignature(functionNames.get(4));
    Mockito.verifyNoMoreInteractions(signatureStoreMock);

    assertNull(pass.methodDefinitions.get("quux"));
  }

  @Test
  public void testFindMethodsInExterns() {
    Node externs = Node.newTextNode("function foo() {}" +
                                     "function bar() {}" +
                                     "function baz(x) {}" +
                                     "function qux(x, y) {}",
                                     "function quux() {}");

    pass.process(externs, null);

    ArgumentCaptor<String> functionNameCaptor = ArgumentCaptor.forClass(String.class);

    Mockito.verify(signatureStoreMock, Mockito.times(5)).addSignature(functionNameCaptor.capture(), Mockito.any(Node.class), Mockito.anyString());

    List<String> functionNames = Lists.newArrayList(functionNameCaptor.getAllValues());

    assertEquals(5, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
    assertEquals("bar", pass.externMethods.iterator().next());
    assertEquals("baz", pass.externMethods.iterator().next());
    assertEquals("qux", pass.externMethods.iterator().next());
    assertEquals("quux", pass.externMethods.iterator().next());

    assertFalse(pass.externMethodsWithoutSignatures.isEmpty());
  }

  @Test
  public void testFindMethodsInScript() {
    Node root = Node.newTextNode(
        "function foo() {}" +
        "function bar() {}" +
        "function baz(x) {}" +
        "function qux(x, y) {}",
        "function quux() {}");

    pass.process(null, root);

    ArgumentCaptor<String> functionNameCaptor = ArgumentCaptor.forClass(String.class);

    Mockito.verify(signatureStoreMock, Mockito.times(5)).addSignature(functionNameCaptor.capture(), Mockito.any(Node.class), Mockito.anyString());

    List<String> functionNames = Lists.newArrayList(functionNameCaptor.getAllValues());

    assertEquals(5, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
    assertEquals("bar", pass.externMethods.iterator().next());
    assertEquals("baz", pass.externMethods.iterator().next());
    assertEquals("qux", pass.externMethods.iterator().next());
    assertEquals("quux", pass.externMethods.iterator().next());

    assertFalse(pass.externMethodsWithoutSignatures.isEmpty());

    Mockito.verify(signatureStoreMock, Mockito.times(1)).removeSignature(functionNames.get(5));
    Mockito.verifyNoMoreInteractions(signatureStoreMock);

    assertNull(pass.methodDefinitions.get("quux"));
  }
}