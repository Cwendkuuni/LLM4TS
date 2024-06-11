import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.test.CompilerTestCase;

@RunWith(JUnit4.class)
public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass() throws Exception {
    String input = "function Foo() {} \n"
        + "Foo.prototype.bar = function() {}; \n"
        + "Foo.prototype.baz = function() {}; \n"
        + "Foo.prototype.qux = function() {}; \n"
        + "Foo.prototype.quux = function() {}; \n"
        + "Foo.prototype = {getQuux: function() {}}; \n"
        + "Foo.prototype.prototype = {}; \n"
        + "Foo.prototype.prototype.quuz = function() {}; \n"
        + "function Bar() {} \n"
        + "Bar.quux = function() {}; \n"
        + "Bar.prototype = {getQuux: function() {}}; \n"
        + "function Quux() {} \n"
        + "Quux.prototype = Object.create(Foo.prototype); \n"
        + "Quux.prototype.quuz = function() {}; \n"
        + "function QuuxQuux() {} \n"
        + "QuuxQuux.prototype = Object.create(Quux.prototype); \n"
        + "QuuxQuux.prototype.quuz = function() {}; \n"
        + "function FooBar() {} \n"
        + "FooBar.prototype = Object.create(Foo.prototype); \n"
        + "FooBar.prototype.quuz = function() {}; \n"
        + "function FooQuux() {} \n"
        + "FooQuux.prototype = Object.create(Foo.prototype); \n"
        + "FooQuux.prototype.quuz = function() {}; \n"
        + "function FooQuuxQuux() {} \n"
        + "FooQuuxQuux.prototype = Object.create(FooQuux.prototype); \n"
        + "FooQuuxQuux.prototype.quuz = function() {}; \n"
        + "FooBar.prototype.qux = function() {}; \n"
        + "FooBar.prototype.quuzz = function() {}; \n"
        + "FooBar.prototype.prototype = {getQuux: function() {}}; \n"
        + "FooBar.prototype.prototype.quuz = function() {}; \n"
        + "FooQuux.prototype.qux = function() {}; \n"
        + "FooQuux.prototype.quuzz = function() {}; \n"
        + "FooQuux.prototype.prototype = {getQuux: function() {}}; \n"
        + "FooQuux.prototype.prototype.quuz = function() {}; \n"
        + "FooQuuxQuux.prototype.qux = function() {}; \n"
        + "FooQuuxQuux.prototype.quuzz = function() {}; \n"
        + "FooQuuxQuuz.prototype = Object.create(FooQuuxQuux.prototype); \n"
        + "FooQuuzQuuz.prototype.qux = function() {}; \n"
        + "FooQuuzQuuz.prototype.quuzz = function() {}; \n"
        + "function FooQuuzQuux() {} \n"
        + "FooQuuzQuux.prototype = Object.create(FooQuuzQuuz.prototype); \n"
        + "FooQuuzQuux.prototype.qux = function() {}; \n"
        + "FooQuuzQuux.prototype.quuzz = function() {}; \n"
        + "function FooQuuxQuuxQuux() {} \n"
        + "FooQuuxQuuxQuux.prototype = Object.create(FooQuuxQuuxQuux.prototype); \n"
        + "FooQuuxQuuxQuux.prototype.qux = function() {}; \n"
        + "FooQuuzQuuzQuuz.prototype = Object.create(FooQuuzQuuzQuuz.prototype); \n"
        + "FooQuuzQuuzQuuz.prototype.qux = function() {}; \n"
        + "FooQuuzQuuzQuuz.prototype.quuzz = function() {}; \n"
        + "function FooQuuxQuuzQuux() {} \n"
        + "FooQuuxQuuzQuux.prototype = Object.create(FooQuuxQuuzQuux.prototype); \n"
        + "FooQuuxQuuzQuux.prototype.qux = function() {}; \n"
        + "FooQuuxQuuzQuuz.prototype.quuzz = function() {}; \n"
        + "function FooQuuxQuuzQuuzQuux() {} \n"
        + "FooQuuzQuuzQuuzQuuz.prototype = Object.create(FooQuuzQuuzQuuzQuuz.prototype); \n"
        + "FooQuuzQuuzQuuzQuuz.prototype.qux = function() {}; \n"
        + "FooQuuzQuuzQuuzQuuz.prototype.quuzz = function() {}; \n"
        + "function FooQuuxQuuzQuuzQuuxQuux() {} \n"
        + "FooQuuxQuuzQuuzQuuxQuux.prototype = Object.create(FooQuuxQuuzQuuzQuuxQuux.prototype); \n"
        + "FooQuuxQuuzQuuzQuuxQuux.prototype.qux = function() {}; \n"
        + "FooQuuxQuuzQuuzQuuxQuux.prototype.quuzz = function() {}; \n"
        + "FooQuuxQuuzQuuzQuuxQuux.prototype.prototype = {getQuux: function() {}}; \n"
        + "FooQuuxQuuzQuuzQuuxQuux.prototype.prototype.qux = function() {}; \n"
        + "FooQuuxQuuzQuuzQuuxQuux.prototype.prototype.quuzz = function() {}; ";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(input, null, null);

    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Map<String, Node> signatures = Maps.newHashMap();
          Set<String> externMethods = Sets.newHashSet();

          @Override
          public void reset() {
            signatures.clear();
            externMethods.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode,
              String sourceFile) {
            signatures.put(functionName, functionNode);
          }

          @Override
          public void removeSignature(String functionName) {
            signatures.remove(functionName);
          }

          @Override
          public Set<String> getExternMethods() {
            return ImmutableSet.copyOf(externMethods);
          }
        };
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(NodeTraversal nodeTraversal, Node node,
              Node parent) {
            if (node.getType() == Token.FUNCTION) {
              String functionName = node.getFirstChild().getString();
              methodCompilerPass.addSignature(functionName, node, nodeTraversal);
            }
          }
        };
      }
    };

    methodCompilerPass.process(null, ast);

    // Check signatures
    Map<String, Node> expectedSignatures = Maps.newHashMap();
    expectedSignatures.put("Bar.quux", ast.getChild(12));
    expectedSignatures.put("Foo.prototype.bar", ast.getChild(1));
    expectedSignatures.put("Foo.prototype.baz", ast.getChild(3));
    expectedSignatures.put("Foo.prototype.qux", ast.getChild(18));
    expectedSignatures.put("Foo.prototype.quuzz", ast.getChild(22));
    expectedSignatures.put("Foo.prototype.prototype.quuz", ast.getChild(27));
    expectedSignatures.put("FooBar.prototype.qux", ast.getChild(30));
    expectedSignatures.put("FooBar.prototype.quuzz", ast.getChild(34));
    expectedSignatures.put("FooBar.prototype.prototype.qux", ast.getChild(39));
    expectedSignatures.put("FooBar.prototype.prototype.quuzz", ast.getChild(43));
    expectedSignatures.put("FooQuux.prototype.qux", ast.getChild(46));
    expectedSignatures.put("FooQuux.prototype.quuzz", ast.getChild(50));
    expectedSignatures.put("FooQuux.prototype.prototype.qux", ast.getChild(55));
    expectedSignatures.put("FooQuux.prototype.prototype.quuzz", ast.getChild(59));
    expectedSignatures.put("FooQuuxQuux.prototype.qux", ast.getChild(62));
    expectedSignatures.put("FooQuuzQuuz.prototype.qux", ast.getChild(66));
    expectedSignatures.put("FooQuuzQuuz.prototype.quuzz", ast.getChild(70));
    expectedSignatures.put("FooQuuxQuuzQuux.prototype.qux", ast.getChild(73));
    expectedSignatures.put("FooQuuxQuuzQuux.prototype.quuzz", ast.getChild(77));
    expectedSignatures.put("FooQuuxQuuzQuuzQuux.prototype.qux", ast.getChild(81));
    expectedSignatures.put("FooQuuxQuuzQuuzQuux.prototype.quuzz", ast.getChild(85));

    Collection<Node> actualSignatures = methodCompilerPass.getSignatureStore().getSignatures().values();
    List<Node> expectedSignatureList = Lists.newArrayList(expectedSignatures.values());
    List<Node> actualSignatureList = Lists.newArrayList(actualSignatures);

    assertEquals(expectedSignatureList, actualSignatureList);

    // Check externMethods
    Set<String> expectedExternMethods = methodCompilerPass.getSignatureStore().getExternMethods();
    Set<String> actualExternMethods = Sets.newHashSet();
    for (Map.Entry<String, Node> entry : methodCompilerPass.methodDefinitions.entries()) {
      if (!expectedSignatures.containsKey(entry.getKey())) {
        actualExternMethods.add(entry.getKey());
      }
    }

    assertEquals(expectedExternMethods, actualExternMethods);

    // Check nonMethodProperties
    Set<String> expectedNonMethodProperties = Sets.newHashSet("prototype");
    Set<String> actualNonMethodProperties = methodCompilerPass.nonMethodProperties;

    assertEquals(expectedNonMethodProperties, actualNonMethodProperties);
  }

  interface SignatureStore {
    void reset();
    void addSignature(String functionName, Node functionNode, String sourceFile);
    void removeSignature(String functionName);
    Map<String, Node> getSignatures();
    Set<String> getExternMethods();
  }
}