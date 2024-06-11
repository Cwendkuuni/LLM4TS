import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.HashSet;
import java.util.Set;

public class MethodCompilerPassTest {

  @SuppressWarnings("unchecked")
  private static abstract class TestMethodCompilerPass extends MethodCompilerPass {

    Set<String> actualExternMethods = new HashSet<>();
    Set<String> actualExternMethodsWithoutSignatures = new HashSet<>();
    Set<String> actualNonMethodProperties = new HashSet<>();
    Multimap<String, Node> actualMethodDefinitions = HashMultimap.create();

    void assertExternMethodsEquals(Set<String> expectedExternMethods) {
      assertEquals(expectedExternMethods, actualExternMethods);
    }

    void assertExternMethodsWithoutSignaturesEquals(
        Set<String> expectedExternMethodsWithoutSignatures) {
      assertEquals(expectedExternMethodsWithoutSignatures,
          actualExternMethodsWithoutSignatures);
    }

    void assertNonMethodPropertiesEquals(Set<String> expectedNonMethodProperties) {
      assertEquals(expectedNonMethodProperties, actualNonMethodProperties);
    }

    void assertMethodDefinitionsEquals(Multimap<String, Node> expectedMethodDefinitions) {
      assertEquals(expectedMethodDefinitions, actualMethodDefinitions);
    }

    abstract Callback getActingCallback();

    abstract SignatureStore getSignatureStore();

    void testMethodCompilerPass(String input) {
      CompilerOptions options = new CompilerOptions();
      AbstractCompiler compiler = new Compiler(options);
      Node root = Parser.parse(input, null, null);

      MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass(compiler);
      methodCompilerPass.process(null, root);

      assertExternMethodsEquals(Sets.newHashSet("foo", "bar"));
      assertExternMethodsWithoutSignaturesEquals(Sets.newHashSet("externBaz"));
      assertNonMethodPropertiesEquals(Sets.newHashSet("prototype"));
      assertMethodDefinitionsEquals(HashMultimap.createPutAll(
          actualMethodDefinitions,
          Sets.newImmutableEnumSet(
              Map.entry("foo", new NodeFake(Token.FUNCTION, "function foo() { }")),
              Map.entry("bar", new NodeFake(Token.FUNCTION, "function bar() { }")),
              Map.entry("externBaz", new NodeFake(Token.NAME, "externBaz")))
      ));
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  static class NodeFake implements Node {

    private final int type;
    private final String stringValue;

    NodeFake(int type, String stringValue) {
      this.type = type;
      this.stringValue = stringValue;
    }

    @Override
    public int getType() {
      return type;
    }

    @Override
    public String getString() {
      return stringValue;
    }
  }

  @org.junit.Test
  public void testMethodCompilerPass() {
    TestMethodCompilerPass test = new TestMethodCompilerPass() {
      @Override
      Callback getActingCallback() {
        return new NodeTraversal.AbstractCallback() {};
      }

      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Set<String> signatures = new HashSet<>();

          @Override
          public void reset() {
            signatures.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode,
              String sourceFile) {
            signatures.add(functionName);
          }

          @Override
          public void removeSignature(String functionName) {
            signatures.remove(functionName);
          }
        };
      }
    };

    test.testMethodCompilerPass(
        "var foo = function() {};\n" +
        "var bar = function() {};\n" +
        "extern function foo();\n" +
        "extern function externBaz();\n" +
        "var prototype = Foo.prototype;\n" +
        "Foo.prototype.foo = function() { };\n" +
        "Foo.prototype.bar = function() { };\n" +
        "Foo.prototype.externBaz = function() { };");
  }
}