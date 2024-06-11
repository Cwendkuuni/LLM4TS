import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;
import static com.google.javascript.jscomp.NodeType.*;
import static com.google.javascript.rhino.Nodeops.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.NodeTraversingCompilerPass;
import com.google.javascript.jscomp.NodeUtils;
import com.google.javascript.rhino.*;
import org.junit.Test;

import java.util.*;

/**
 * Test class for CrossModuleMethodMotion.
 */
public class CrossModuleMethodMotionTest {

  private static final String TEST_SOURCE =
      "var a = {\n" +
      "  foo: function() {}\n" +
      "};\n" +
      "\n" +
      "var b = {\n" +
      "  bar: function() {}\n" +
      "};\n" +
      "\n" +
      "var c = {\n" +
      "  b:\n" +
      "    prototype:\n" +
      "    {\n" +
      "      qux: function() {}\n" +
      "    }\n" +
      "};\n" +
      "\n" +
      "// file: main.js\n" +
      "a.prototype.foo.call(b);\n" +
      "c.b.qux.call(a);\n";

  private abstract static class TestCompiler extends Compiler {

    protected TestCompiler() {
      super(getCompilerOptions());
    }

    protected abstract CompilerOptions getCompilerOptions();

    protected abstract void addPasses();

    @Override
    protected void processRoot(Node externs, Node root) {
      super.processRoot(externs, root);
      assertCompilationSucceeded();
    }

    protected void assertCompilationSucceeded() {
      // no-op
    }

    protected void assertModuleGraphHasExpectedNodes(JSModule expectedModule,
        Node expectedNode) {
      JSModule actualModule = getModuleGraph().getModule(expectedModule.getName());
      Node actualNode = findNode(actualModule, expectedNode.toString());
      assertEquals("Expected module to contain node:\n" + expectedNode,
          expectedNode, actualNode);
    }

    protected Node findNode(JSModule module, String nodeName) {
      Iterator<Node> iterator = module.getBody().getChildrenIterator();
      Node node = null;
      while (iterator.hasNext()) {
        Node currentNode = iterator.next();
        if (NodeOps.isIdent(currentNode) && currentNode.toString().equals(nodeName)) {
          node = currentNode;
          break;
        }
      }
      return node;
    }

    protected List<Property> getDeclarations(Node propertyNode) {
      Iterator<Symbol> iterator =
          analyzePrototypeProperties(propertyNode).getDeclarations().iterator();
      List<Property> declarations = Lists.newArrayList();
      while (iterator.hasNext()) {
        declarations.add((Property) iterator.next());
      }
      return declarations;
    }

    protected AnalyzePrototypeProperties analyzePrototypeProperties(Node node) {
      AbstractCompiler compiler = new TestCompiler() {
        @Override
        protected CompilerOptions getCompilerOptions() {
          return new CompilerOptions();
        }

        @Override
        protected void addPasses() {
          addPass(new ProcessExternalDeclarationsPass());
          addPass(new CheckTypesPass());
          addPass(new CheckDeclaredNamesPass());
          addPass(new CrossModuleMethodMotion(compiler, null, true));
        }
      };
      compiler.compile(null, createNode(TEST_SOURCE));
      return compiler.getAnalysisResult().getAnalyzePrototypeProperties();
    }
  }

  @Test
  public void testCrossModuleMethodMotion() {
    TestCompiler compiler = new TestCaseCompiler();
    Node rootNode = compiler.compile(null, createNode(TEST_SOURCE)).getRoot();

    JSModule aModule = compiler.getModuleGraph().getModule("a");
    JSModule bModule = compiler.getModuleGraph().getModule("b");
    JSModule cModule = compiler.getModuleGraph().getModule("c");

    // verify that a.prototype.foo and c.b.qux are in the correct modules
    assertModuleGraphHasExpectedNodes(aModule, "prototype");
    assertModuleGraphHasExpectedNodes(aModule, "foo");
    assertModuleGraphHasExpectedNodes(bModule, "prototype");
    assertModuleGraphHasExpectedNodes(cModule, "b");
    assertModuleGraphHasExpectedNodes(cModule, "prototype");
    assertModuleGraphHasExpectedNodes(cModule, "qux");

    Node aPrototype = findNode(aModule, "prototype");
    Node aFoo = findNode(aPrototype, "foo");

    Node cBPrototype = findNode(cModule, "prototype");
    Node cQux = findNode(cBPrototype, "qux");

    // verify that a.prototype.foo is stubbed in b and unstubbed in c
    Node aFooStubCall = findNode(bModule, "JSCompiler_stubMethod");
    assertTrue("Expected stub function to have been generated",
        aFooStubCall != null);
    Node aFooStubId = findNode(aFooStubCall, "[1]");
    assertEquals("Unexpected stub function id", 1, Integer.parseInt(
        aFooStubId.toString()));

    Node cBQuxUnstubCall = findNode(cModule, "JSCompiler_unstubMethod");
    assertTrue("Expected unstub function to have been generated",
        cBQuxUnstubCall != null);
    Node cBQuxUnstubId = findNode(cBQuxUnstubCall, "[1]");
    Node cBQuxUnstubBody = findNode(cBQuxUnstubCall, "[2]");
    Node cBQux = findNode(cBPrototype, "qux");
    assertEquals("Unexpected unstub function id", 1, Integer.parseInt(
        cBQuxUnstubId.toString()));
    Node cBQuxOriginalBody = findNode(cBQux, "body");
    assertEquals("Unexpected original function body", cBQuxOriginalBody,
        cBQuxUnstubBody);

    // verify that a.prototype.foo is called with the correct id in b
    Node aFooCall = findNode(bModule, "a.prototype.foo");
    Node aFooCallStubId = findNode(aFooCall, "[1]");
    assertEquals("Unexpected stub function id", 1, Integer.parseInt(
        aFooCallStubId.toString()));

    // verify that c.b.qux is called with the correct id in a
    Node cBQuxCall = findNode(aModule, "c.b.qux");
    Node cBQuxCallStubId = findNode(cBQuxCall, "[1]");
    assertEquals("Unexpected stub function id", 1, Integer.parseInt(
        cBQuxCallStubId.toString()));
  }

  private static class TestCaseCompiler extends TestCompiler {

    @Override
    protected CompilerOptions getCompilerOptions() {
      return new CompilerOptions()
          .setWarningLevel(Diagnostics.Type.ECMA_ERROR,
              Diagnostics.WarningCode.CIRCULAR_DEPENDENCY)
          .setWarningLevel(Diagnostics.Type.JAVASCRIPT, DiagnosticType.ERROR)
          .setGenerateClock(false)
          .setGenerateErrors(false)
          .setGenerateSourceMaps(false);
    }

    @Override
    protected void addPasses() {
      super.addPasses();
      addPass(new CrossModuleMethodMotion(this, null, true));
    }

    @Override
    protected void assertCompilationSucceeded() {
      fail("Expected compilation to fail");
    }
  }
}