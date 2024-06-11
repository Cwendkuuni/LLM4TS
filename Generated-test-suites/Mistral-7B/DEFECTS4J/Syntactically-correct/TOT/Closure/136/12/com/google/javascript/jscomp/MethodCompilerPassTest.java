import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

  private static final String SOURCE_NAME = "TestSource.js";

  @org.junit.Test
  public void testMethodCompilerPass() {
    AbstractCompiler compiler = new Compiler();
    MethodCompilerPass pass = new MyMethodCompilerPass(compiler);

    Node root = createTestSource();
    Node externs = createExterns();

    pass.process(externs, root);

    // Assert that the expected methods were found and stored
    assertEquals(6, pass.getMethodDefinitions().size());
    Map<String, Node> methodDefinitions = pass.getMethodDefinitions();
    assertEquals("myMethod1", methodDefinitions.keySet().toArray()[0].toString());
    assertEquals("myMethod2", methodDefinitions.keySet().toArray()[1].toString());
    assertEquals("myObjectMethod1", methodDefinitions.keySet().toArray()[2].toString());
    assertEquals("myObjectMethod2", methodDefinitions.keySet().toArray()[3].toString());
    assertEquals("myClassMethod1", methodDefinitions.keySet().toArray()[4].toString());
    assertEquals("myClassMethod2", methodDefinitions.keySet().toArray()[5].toString());

    // Assert that the expected signatures were stored
    SignatureStore signatureStore = pass.getSignatureStore();
    List<Map.Entry<String, Node>> signatures = signatureStore.getSignatures();
    assertEquals(6, signatures.size());
    assertEquals("myMethod1", signatures.get(0).getKey());
    assertEquals("function(a) { ... }", signatures.get(0).getValue().toString());
    assertEquals("myMethod2", signatures.get(1).getKey());
    assertEquals("function(a, b) { ... }", signatures.get(1).getValue().toString());
    assertEquals("myObjectMethod1", signatures.get(2).getKey());
    assertEquals("function() { ... }", signatures.get(2).getValue().toString());
    assertEquals("myObjectMethod2", signatures.get(3).getKey());
    assertEquals("function(a) { ... }", signatures.get(3).getValue().toString());
    assertEquals("myClassMethod1", signatures.get(4).getKey());
    assertEquals("function() { ... }", signatures.get(4).getValue().toString());
    assertEquals("myClassMethod2", signatures.get(5).getKey());
    assertEquals("function(b) { ... }", signatures.get(5).getValue().toString());

    // Assert that expected extern methods were identified
    Set<String> externMethods = pass.getExternMethods();
    assertEquals(2, externMethods.size());
    assertTrue(externMethods.contains("myExternMethod1"));
    assertTrue(externMethods.contains("myExternMethod2"));

    // Assert that expected non-method properties were identified
    Set<String> nonMethodProperties = pass.getNonMethodProperties();
    assertEquals(1, nonMethodProperties.size());
    assertTrue(nonMethodProperties.contains("myNonMethodProperty"));

    // Assert that expected unused methods were not stored
    Set<String> unusedMethods = pass.getUnusedMethods();
    assertEquals(0, unusedMethods.size());
  }

  private Node createTestSource() {
    // Test source code
    // ...
    return null;
  }

  private Node createExterns() {
    // Test externs file
    // ...
    return null;
  }
}

class MyMethodCompilerPass extends MethodCompilerPass {
  MyMethodCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  SignatureStore getSignatureStore() {
    // Return a signature store implementation for testing
    return new MySignatureStore();
  }

  @Override
  Callback getActingCallback() {
    // Return a callback implementation for testing
    return new MyActingCallback();
  }

  static class MySignatureStore implements SignatureStore {
    private List<Map.Entry<String, Node>> signatures = Lists.newArrayList();

    @Override
    public void reset() {
      signatures.clear();
    }

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.add(Map.entry(functionName, functionNode));
    }

    @Override
    public void removeSignature(String functionName) {
      signatures.removeIf(entry -> entry.getKey().equals(functionName));
    }

    @Override
    public List<Map.Entry<String, Node>> getSignatures() {
      return signatures;
    }
  }

  static class MyActingCallback extends AbstractPostOrderCallback {
    // Implement the method that performs the actual work
    // when given the list of method signatures
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // ...
    }
  }
}