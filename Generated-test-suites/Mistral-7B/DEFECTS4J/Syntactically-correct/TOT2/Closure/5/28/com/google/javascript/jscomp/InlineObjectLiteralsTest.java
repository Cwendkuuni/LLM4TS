import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @org.junit.Test
  public void testInlineObjectLiterals() throws Exception {
    String sourceCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {c: 2},\n" +
        "    d: function() {\n" +
        "      return 3;\n" +
        "    }\n" +
        "  };\n" +
        "  var result = obj.a + obj.b.c;\n" +
        "  return result + obj.d();\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    Node root = new IR.Program();
    Node externs = new Node(Token.NULL);

    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
            compiler, new StaticNameIdSupplier());
        inlineObjectLiterals.process(externs, root);
      }
    });

    compiler.compile(sourceCode, root, externs, options);

    Node resultNode = compiler.getResults()[0];

    Node expectedResultNode = new Node(Token.CALL,
        new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNode.toString(), resultNode.toString());

    // Test for object with no properties
    String sourceCodeEmpty = "function foo() {\n" +
        "  var obj = {};\n" +
        "  return obj;\n" +
        "}\n";

    compiler.compile(sourceCodeEmpty, root, externs, options);

    Node resultNodeEmpty = compiler.getResults()[0];

    Node expectedResultNodeEmpty = new Node(Token.NAME, "foo");

    assertEquals(expectedResultNodeEmpty.toString(), resultNodeEmpty.toString());

    // Test for object with self-referential properties
    String sourceCodeSelfRef = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: {\n" +
        "      b: obj\n" +
        "    },\n" +
        "    c: 1\n" +
        "  };\n" +
        "  return obj.a.b.c;\n" +
        "}\n";

    compiler.compile(sourceCodeSelfRef, root, externs, options);

    Node resultNodeSelfRef = compiler.getResults()[0];

    // Self referential property should be converted to a circular reference
    // which is not valid in JavaScript, hence the test should fail
    // TODO: Add a test failure assertion

    // Test for object with getter property
    String sourceCodeGetter = "function foo() {\n" +
        "  var obj = {\n" +
        "    get a() {\n" +
        "      return 1;\n" +
        "    }\n" +
        "  };\n" +
        "  return obj.a;\n" +
        "}\n";

    compiler.compile(sourceCodeGetter, root, externs, options);

    Node resultNodeGetter = compiler.getResults()[0];

    Node expectedResultNodeGetter = new Node(Token.NAME, "foo");

    assertEquals(expectedResultNodeGetter.toString(), resultNodeGetter.toString());

    // Test for object with setter property
    String sourceCodeSetter = "function foo() {\n" +
        "  var obj = {\n" +
        "    set a(value) {\n" +
        "      this.c = value;\n" +
        "    }\n" +
        "  };\n" +
        "  obj.a = 1;\n" +
        "  return obj.c;\n" +
        "}\n";

    compiler.compile(sourceCodeSetter, root, externs, options);

    Node resultNodeSetter = compiler.getResults()[0];

    Node expectedResultNodeSetter = new Node(
        Token.CALL, new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNodeSetter.toString(), resultNodeSetter.toString());

    // Test for object with function property
    String sourceCodeFunction = "function foo() {\n" +
        "  var obj = {\n" +
        "    foo: function() {\n" +
        "      return 1;\n" +
        "    },\n" +
        "    a: 1\n" +
        "  };\n" +
        "  return obj.foo() + obj.a;\n" +
        "}\n";

    compiler.compile(sourceCodeFunction, root, externs, options);

    Node resultNodeFunction = compiler.getResults()[0];

    Node expectedResultNodeFunction = new Node(
        Token.CALL, new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNodeFunction.toString(), resultNodeFunction.toString());

    // Test for object with property shadows
    String sourceCodeShadow = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      a: 2,\n" +
        "      c: 3\n" +
        "    }\n" +
        "  };\n" +
        "  obj.b.a = 3;\n" +
        "  return obj.a + obj.b.c;\n" +
        "}\n";

    compiler.compile(sourceCodeShadow, root, externs, options);

    Node resultNodeShadow = compiler.getResults()[0];

    Node expectedResultNodeShadow = new Node(
        Token.CALL, new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNodeShadow.toString(), resultNodeShadow.toString());

    // Test for object with empty property
    String sourceCodeEmptyProperty = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: {}\n" +
        "  };\n" +
        "  return obj.a;\n" +
        "}\n";

    compiler.compile(sourceCodeEmptyProperty, root, externs, options);

    Node resultNodeEmptyProperty = compiler.getResults()[0];

    Node expectedResultNodeEmptyProperty = new Node(Token.NAME, "foo");

    assertEquals(expectedResultNodeEmptyProperty.toString(), resultNodeEmptyProperty.toString());

    // Test for object with undefined property
    String sourceCodeUndefinedProperty = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: undefined\n" +
        "  };\n" +
        "  return obj.a + 1;\n" +
        "}\n";

    compiler.compile(sourceCodeUndefinedProperty, root, externs, options);

    Node resultNodeUndefinedProperty = compiler.getResults()[0];

    Node expectedResultNodeUndefinedProperty = new Node(
        Token.CALL, new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNodeUndefinedProperty.toString(), resultNodeUndefinedProperty.toString());

    // Test for object with null property
    String sourceCodeNullProperty = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: null\n" +
        "  };\n" +
        "  return obj.a + 1;\n" +
        "}\n";

    compiler.compile(sourceCodeNullProperty, root, externs, options);

    Node resultNodeNullProperty = compiler.getResults()[0];

    Node expectedResultNodeNullProperty = new Node(
        Token.CALL, new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNodeNullProperty.toString(), resultNodeNullProperty.toString());

    // Test for object with boolean property
    String sourceCodeBooleanProperty = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: true\n" +
        "  };\n" +
        "  return obj.a + 1;\n" +
        "}\n";

    compiler.compile(sourceCodeBooleanProperty, root, externs, options);

    Node resultNodeBooleanProperty = compiler.getResults()[0];

    Node expectedResultNodeBooleanProperty = new Node(
        Token.CALL, new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNodeBooleanProperty.toString(), resultNodeBooleanProperty.toString());

    // Test for object with number property
    String sourceCodeNumberProperty = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1\n" +
        "  };\n" +
        "  return obj.a + 1;\n" +
        "}\n";

    compiler.compile(sourceCodeNumberProperty, root, externs, options);

    Node resultNodeNumberProperty = compiler.getResults()[0];

    Node expectedResultNodeNumberProperty = new Node(
        Token.CALL, new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNodeNumberProperty.toString(), resultNodeNumberProperty.toString());

    // Test for object with string property
    String sourceCodeStringProperty = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: \"abc\"\n" +
        "  };\n" +
        "  return obj.a.charAt(0) + obj.a.charAt(obj.a.length() - 1);\n" +
        "}\n";

    compiler.compile(sourceCodeStringProperty, root, externs, options);

    Node resultNodeStringProperty = compiler.getResults()[0];

    Node expectedResultNodeStringProperty = new Node(
        Token.CALL, new Node(Token.IDENT, "foo"),
        new Node[0]);

    assertEquals(expectedResultNodeStringProperty.toString(), resultNodeStringProperty.toString());
  }

  // Static class used to generate unique names for variables
  private static class StaticNameIdSupplier implements Supplier<String> {
    private int nextId = 0;

    @Override
    public String get() {
      return "JSCompiler_object_inline_" + nextId++;
    }
  }
}