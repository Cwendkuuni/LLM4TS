import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversingVisitor;
import com.google.javascript.jscomp.Pass;
import com.google.javascript.jscomp.test_util.AbstractCompilerTestCase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class CrossModuleMethodMotionTest extends AbstractCompilerTestCase {

  public void testMoveMethodAcrossModules() {
    String source1 = "class A {\n" +
        "  function foo() {\n" +
        "    return 'A.foo';\n" +
        "  }\n" +
        "  class B {\n" +
        "    function bar(a) {\n" +
        "      return a.foo();\n" +
        "    }\n" +
        "  }\n" +
        "}";

    String source2 = "class C {\n" +
        "  function foo() {\n" +
        "    return 'C.foo';\n" +
        "  }\n" +
        "}";

    CompilerOptions options = new CompilerOptions(CompilerOptions.defaultOptions());
    options.setAllowAllTypesIf(ConditionalCompilation.ALLOW_ALL_TYPES_IF);

    SourceFile input1 = createSourceFile("A.js", source1);
    SourceFile input2 = createSourceFile("C.js", source2);

    Compiler compiler = new Compiler();
    compiler.addPasses(Pass.getDefaults(new CompilerPass[0]));
    compiler.addPass(new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true));

    compiler.compile(ImmutableList.of(input1, input2), options, null);

    Node rootNode = compiler.getRootNode();

    Node expectedRootNode = newNode("(function() {\n" +
        "  var JSCompiler_stubMap = [];\n" +
        "  function JSCompiler_stubMethod(JSCompiler_stubMethod_id) {\n" +
        "    return function() {\n" +
        "      return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(\n" +
        "          this,\n" +
        "          arguments);\n" +
        "    };\n" +
        "  }\n" +
        "  function JSCompiler_unstubMethod(\n" +
        "      JSCompiler_unstubMethod_id,\n" +
        "      JSCompiler_unstubMethod_body) {\n" +
        "    return JSCompiler_stubMap[JSCompiler_unstubMethod_id] = \n" +
        "        JSCompiler_unstubMethod_body;\n" +
        "  }\n" +
        "  \n" +
        "  var A = function() {\n" +
        "    this.foo = JSCompiler_stubMethod(0);\n" +
        "  };\n" +
        "  A.prototype.foo = JSCompiler_unstubMethod(0,\n" +
        "      function() {\n" +
        "        return 'A.foo';\n" +
        "      });\n" +
        "  \n" +
        "  var C = function() {\n" +
        "    this.foo = JSCompiler_stubMethod(1);\n" +
        "  };\n" +
        "  C.prototype.foo = JSCompiler_unstubMethod(1,\n" +
        "      function() {\n" +
        "        return 'C.foo';\n" +
        "      });\n" +
        "  \n" +
        "  var B = function() {\n" +
        "    this.bar = JSCompiler_stubMethod(2);\n" +
        "  };\n" +
        "  B.prototype.bar = JSCompiler_unstubMethod(2,\n" +
        "      function(a) {\n" +
        "        return a.foo();\n" +
        "      });\n" +
        "  \n" +
        "})();");

    assertEquals(toString(expectedRootNode), toString(rootNode));

    Node aFoo = findNode("A.foo", rootNode);
    Node cFoo = findNode("C.foo", rootNode);
    Node bBar = findNode("B.bar", rootNode);

    // Test function calls
    assertEquals("A.foo", toString(aFoo.call(null).getFirstChild()));
    assertEquals("C.foo", toString(cFoo.call(null).getFirstChild()));
    assertEquals("A.foo", toString(bBar.call(new A(), new C()).getFirstChild()));

    // Test property access
    assertEquals("A.foo", toString(aFoo.getFirstChild()));
    assertEquals("C.foo", toString(cFoo.getFirstChild()));
    assertEquals("A.foo", toString(bBar.get(0).getFirstChild()));

    // Test error cases
    Node nullNode = nullNode();
    try {
      findNode("null.foo", rootNode);
      fail();
    } catch (AssertionError e) {
      // expected
    }

    try {
      findNode("C.bar", rootNode);
      fail();
    } catch (AssertionError e) {
      // expected
    }

    Node aFooSetter = newNode("function(v) {\n" +
        "  this.foo = v;\n" +
        "}", null);
    Node bBarSetter = newNode("function(v) {\n" +
        "  this.bar = v;\n" +
        "}", null);
    Node a = newNode("A()", null);
    Node b = newNode("B()", null);

    try {
      aFooSetter.call(a, newNode("function() {\n" +
          "  return 'Error';\n" +
          "}", null));
      bBarSetter.call(b, aFoo);
      fail();
    } catch (AssertionError e) {
      // expected
    }
  }

  Node findNode(String name, Node node) {
    NodeTraversingVisitor visitor = new NodeTraversingVisitor() {
      @Override
      public void visitCallExpression(Node call) {
        if (call.getFirstChild().getFirstChild().getString().equals(name)) {
          return;
        }
        super.visitCallExpression(call);
      }

      @Override
      public void visitGetprop(Node getprop) {
        if (getprop.getString().equals(name)) {
          return;
        }
        super.visitGetprop(getprop);
      }

      @Override
      public void visitIdentifier(Node identifier) {
        if (identifier.getString().equals(name)) {
          return;
        }
        super.visitIdentifier(identifier);
      }
    };
    visitor.visit(node);
    return visitor.getNode();
  }

  @Override
  protected SourceFile createSourceFile(String fileName, String sourceCode) {
    return new SourceFile(fileName, sourceCode);
  }
}