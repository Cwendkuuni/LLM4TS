import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions.CompilationTarget;
import com.google.javascript.jscomp.JsCompiler;
import com.google.javascript.jscomp.NodeUtils;
import com.google.javascript.jscomp.testing.TopLevelTestCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() throws Exception {
    String sourceCode =
        "// File: foo.js\n" +
        "class Foo {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "  method1() {\n" +
        "    console.log('method1 in Foo');\n" +
        "  }\n" +
        "}\n" +
        "\n" +
        "// File: bar.js\n" +
        "class Bar extends Foo {\n" +
        "  method2() {\n" +
        "    console.log('method2 in Bar');\n" +
        "    this.method1();\n" +
        "  }\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag(--compilationTarget, CompilationTarget.ES5);

    Compiler compiler = new TopLevelTestCompiler(options);
    Node externs = Node.objectLiteral();

    Node root = Node.parse(sourceCode);
    compiler.compile(externs, root, null);

    // Check that methods are not moved by default
    Node fooNode = compiler.getTopLevelCode().lookupByName("Foo");
    Node barNode = compiler.getTopLevelCode().lookupByName("Bar");
    Node fooMethod1 = NodeUtils.getGetProperty(fooNode, "method1");
    Node barMethod2 = NodeUtils.getGetProperty(barNode, "method2");
    assertEquals(fooNode, NodeUtils.getFunctionDeclarationParent(fooMethod1));
    assertEquals(barNode, NodeUtils.getFunctionDeclarationParent(barMethod2));

    // Enable cross-module method motion
    CompilerPass crossModuleMethodMotion = new CrossModuleMethodMotion(
        compiler,
        new CrossModuleMethodMotion.IdGenerator(),
        true);

    compiler.addPass(compilationPhase.TOP_LEVEL, crossModuleMethodMotion);
    compiler.compile(externs, root, null);

    // Check that method is moved to the base module
    Node movedMethod1 = NodeUtils.getGetProperty(fooNode, "method1");
    Node fooMethod2 = NodeUtils.getGetProperty(fooNode, "method2");
    assertEquals(fooNode, NodeUtils.getFunctionDeclarationParent(movedMethod1));
    assertNull(NodeUtils.getFunctionDeclarationParent(fooMethod2));
    Node barMethod2 = NodeUtils.getGetProperty(barNode, "method2");
    assertNotNull(NodeUtils.getFunctionDeclarationParent(barMethod2));
    Node unstubbedMethod = NodeUtils.getGetProperty(
        NodeUtils.getFunctionDeclarationParent(barMethod2),
        "JSCompiler_unstubMethod");
    assertEquals(
        IR.string("method1 in Foo"),
        IR.getCall(unstubbedMethod, IR.number(0), movedMethod1));
  }
}