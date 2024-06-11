import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.SourceName;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MethodCompilerPassTest {
  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = new SignatureStore() {
            private final Multimap<String, Node> signatures = HashMultimap.create();

            @Override
            public void reset() {
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
        // No actual action needed for testing
        return null;
      }
    };
  }

  @Test
  public void testExtractMethods() {
    String sourceCode = "function Foo() {}" +
        "Foo.prototype.bar = function() {}" +
        "Foo.prototype.baz = function(a) {}";

    Node root = new Parser(new BaseVisitor()).parse(sourceCode, null, null);
    methodCompilerPass.process(null, root);

    assertEquals(2, methodCompilerPass.externMethods.size());
    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals("baz", methodCompilerPass.externMethods.iterator().next());

    assertEquals(2, methodCompilerPass.methodDefinitions.size());
    assertEquals("bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodCompilerPass.methodDefinitions.get("bar"),
        signatures.get("bar"));

    assertEquals("baz", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodCompilerPass.methodDefinitions.get("baz"),
        signatures.get("baz"));
  }

  @Test
  public void testExtractMethodsWithExterns() {
    String sourceCode = "function Foo() {}" +
        "Foo.prototype.bar = function() {}" +
        "Foo.prototype.baz = function(a) {}" +
        "extern Foo.prototype.qux";

    Node root = new Parser(new BaseVisitor()).parse(sourceCode, null, null);
    methodCompilerPass.process(new SourceName("externs.js"), root);

    assertEquals(3, methodCompilerPass.externMethods.size());
    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals("baz", methodCompilerPass.externMethods.iterator().next());
    assertEquals("qux", methodCompilerPass.externMethods.iterator().next());

    assertEquals(2, methodCompilerPass.methodDefinitions.size());
    assertEquals("bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodCompilerPass.methodDefinitions.get("bar"),
        signatures.get("bar"));

    assertEquals("baz", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodCompilerPass.methodDefinitions.get("baz"),
        signatures.get("baz"));
  }

  @Test
  public void testIgnoreMethodsWithNoSignatures() {
    String sourceCode = "function Foo() {}" +
        "Foo.prototype.qux = function() {}";

    Node root = new Parser(new BaseVisitor()).parse(sourceCode, null, null);
    methodCompilerPass.process(null, root);

    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingFunction() {
    String sourceCode = "function Foo() {}" +
        "Foo.prototype.qux = undefined";

    Node root = new Parser(new BaseVisitor()).parse(sourceCode, null, null);
    methodCompilerPass.process(null, root);
  }
}