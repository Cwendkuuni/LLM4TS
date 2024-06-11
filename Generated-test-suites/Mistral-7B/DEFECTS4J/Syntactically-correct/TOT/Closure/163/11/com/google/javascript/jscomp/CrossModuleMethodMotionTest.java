import static com.google.javascript.jscomp.CompilationTest.compile;
import static com.google.javascript.jscomp.CompilerOptions.WarningLevel.QUIET;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilationTest;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {

  @org.junit.Test
  public void testMoveMethod() {
    String sourceCode =
        "class Foo {\n" +
        "  static function bar() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}\n" +
        "class Bar extends Foo {}";

    CompilerOptions options = new CompilerOptions(CompilerOptions.ShowTypes.ALL);
    options.setWarningLevel(QUIET, DiagnosticType.ALL);

    Node root = compile(sourceCode, options);
    Node barMethod = root.lookupClassMemberByName("Foo", "bar");

    String expectedSourceCode =
        "class Foo {\n" +
        "  static function JSCompiler_stubMethod_0(JSCompiler_stubMethod_id) {\n" +
        "    return function() {\n" +
        "      return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(\n" +
        "          this, arguments);\n" +
        "    };\n" +
        "  } \n" +
        "  static JSCompiler_unstubMethod_1 = function() {\n" +
        "    return 1;\n" +
        "  };\n" +
        "}\n" +
        "class Bar extends Foo {}";

    Node expectedRoot = compile(expectedSourceCode, options).getTree();

    Node actualBarMethod = expectedRoot.lookupClassMemberByName("Foo", "bar");
    Node actualStubMethod = actualBarMethod.getParent();

    assertEquals(expectedSourceCode, actualStubMethod.getSource());
    assertEquals(1, actualStubMethod.getChildCount());
    assertEquals("JSCompiler_unstubMethod_1", actualStubMethod.getChild(0).getSource());
  }

  @org.junit.Test
  public void testNullDeepestCommonModuleRef() {
    String sourceCode = "class Foo {}";

    CompilerOptions options = new CompilerOptions(CompilerOptions.ShowTypes.ALL);
    options.setWarningLevel(QUIET, DiagnosticType.ALL);

    Node root = compile(sourceCode, options);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        null, // The compiler is not set
        new CrossModuleMethodMotion.IdGenerator(),
        false);

    pass.process(null, root); // The externRoot is null

    assertEquals(sourceCode, root.getSource());
  }

  @org.junit.Test
  public void testMoveMethodAcrossModules() {
    String sourceCode =
        "class A {\n" +
        "  static function bar() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}\n" +
        "class B extends A {}";

    CompilerOptions options = new CompilerOptions(CompilerOptions.ShowTypes.ALL);
    options.setWarningLevel(QUIET, DiagnosticType.ALL);

    Node root = compile(sourceCode, options);
    Node barMethod = root.lookupClassMemberByName("A", "bar");

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        null, // The compiler is not set
        new CrossModuleMethodMotion.IdGenerator(),
        false);

    pass.process(root, null);

    String expectedSourceCode =
        "class A {\n" +
        "  static JSCompiler_stubMethod_0(JSCompiler_stubMethod_id) {\n" +
        "    return function() {\n" +
        "      return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(\n" +
        "          this, arguments);\n" +
        "    };\n" +
        "  } \n" +
        "  static JSCompiler_unstubMethod_1 = function() {\n" +
        "    return 1;\n" +
        "  };\n" +
        "}\n" +
        "class B extends A {}";

    Node expectedRoot = compile(expectedSourceCode, options).getTree();

    Node actualBarMethod = expectedRoot.lookupClassMemberByName("A", "bar");
    Node actualStubMethod = actualBarMethod.getParent();

    assertEquals(expectedSourceCode, actualStubMethod.getSource());
    assertEquals(1, actualStubMethod.getChildCount());
    assertEquals("JSCompiler_unstubMethod_1", actualStubMethod.getChild(0).getSource());
  }

  @org.junit.Test
  public void testMoveMethodToModuleWithSameName() {
    String sourceCode =
        "class A {\n" +
        "  static function bar() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}\n" +
        "class B {\n" +
        "  static function bar() {\n" +
        "    return 2;\n" +
        "  }\n" +
        "}";

    CompilerOptions options = new CompilerOptions(CompilerOptions.ShowTypes.ALL);
    options.setWarningLevel(QUIET, DiagnosticType.ALL);

    Node root = compile(sourceCode, options);
    Node barMethodA = root.lookupClassMemberByName("A", "bar");
    Node barMethodB = root.lookupClassMemberByName("B", "bar");

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        null, // The compiler is not set
        new CrossModuleMethodMotion.IdGenerator(),
        false);

    pass.process(root, null);

    String expectedSourceCode =
        "class A {\n" +
        "  static JSCompiler_stubMethod_0(JSCompiler_stubMethod_id) {\n" +
        "    return function() {\n" +
        "      return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(\n" +
        "          this, arguments);\n" +
        "    };\n" +
        "  } \n" +
        "  static JSCompiler_unstubMethod_1 = function() {\n" +
        "    return 1;\n" +
        "  };\n" +
        "}\n" +
        "class B {\n" +
        "  static function bar() {\n" +
        "    return JSCompiler_unstubMethod_1();\n" +
        "  }\n" +
        "}";

    Node expectedRoot = compile(expectedSourceCode, options).getTree();

    Node actualBarMethodA = expectedRoot.lookupClassMemberByName("A", "bar");
    Node actualStubMethodA = actualBarMethodA.getParent();
    Node actualBarMethodB = expectedRoot.lookupClassMemberByName("B", "bar");

    assertEquals(expectedSourceCode, actualStubMethodA.getSource());
    assertEquals(1, actualStubMethodA.getChildCount());
    assertEquals("JSCompiler_unstubMethod_1", actualStubMethodA.getChild(0).getSource());

    assertEquals(1, actualBarMethodB.getChildCount());
    assertEquals("JSCompiler_unstubMethod_1", actualBarMethodB.getChild(0).getSource());
  }
}