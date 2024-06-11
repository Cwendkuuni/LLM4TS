import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

  private Compiler compiler;
  private AbstractCompiler abstractCompiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    abstractCompiler = new AbstractCompiler(compiler);
  }

  @Test
  public void testDevirtualizePrototypeMethods() {
    String input =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) {this.total += value; return this.total}";

    Node root = AbstractCompilerTest.parseAndTraverse(input, abstractCompiler);
    DevirtualizePrototypeMethods devirtualizer =
        new DevirtualizePrototypeMethods(abstractCompiler);
    devirtualizer.process(null, root);

    Node expectedOutput =
        "var accumulate = function(self, value) {self.total += value; return self.total}";

    String outputCode = AbstractCompilerTest.printTree(root);
    assertEquals(expectedOutput, outputCode);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMethodsInObjectLiteral() {
    String input =
        "function A() {}\n" +
        "var a = new A();\n" +
        "a.prototype.accumulate = function(value) {this.total += value; return this.total}";

    Node root = AbstractCompilerTest.parseAndTraverse(input, abstractCompiler);
    DevirtualizePrototypeMethods devirtualizer =
        new DevirtualizePrototypeMethods(abstractCompiler);
    devirtualizer.process(null, root);

    String outputCode = AbstractCompilerTest.printTree(root);
    assertFalse("Devirtualization should not happen for methods in object literals",
        outputCode.contains("JSCompiler_StaticMethods_accumulate"));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleDefinitions() {
    String input =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) {this.total += value; return this.total}\n" +
        "A.prototype.accumulate = function(value) {this.total += value; return this.total}";

    Node root = AbstractCompilerTest.parseAndTraverse(input, abstractCompiler);
    DevirtualizePrototypeMethods devirtualizer =
        new DevirtualizePrototypeMethods(abstractCompiler);
    devirtualizer.process(null, root);

    String outputCode = AbstractCompilerTest.printTree(root);
    assertTrue(outputCode.contains("accumulate"));
    assertFalse(outputCode.contains("JSCompiler_StaticMethods_accumulate"));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithExportedMethod() {
    String input =
        "function A() {}\n" +
        "exports.accumulate = A.prototype.accumulate = function(value) {this.total += value; return this.total}";

    Node root = AbstractCompilerTest.parseAndTraverse(input, abstractCompiler);
    DevirtualizePrototypeMethods devirtualizer =
        new DevirtualizePrototypeMethods(abstractCompiler);
    devirtualizer.process(null, root);

    String outputCode = AbstractCompilerTest.printTree(root);
    assertTrue(outputCode.contains("exports.accumulate"));
    assertFalse(outputCode.contains("JSCompiler_StaticMethods_accumulate"));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithDuplicateCallSites() {
    String input =
        "function A() {}\n" +
        "function B() {}\n" +
        "B.prototype.accumulate = function(value) {A.prototype.accumulate.call(this, value)}";

    Node root = AbstractCompilerTest.parseAndTraverse(input, abstractCompiler);
    DevirtualizePrototypeMethods devirtualizer =
        new DevirtualizePrototypeMethods(abstractCompiler);
    devirtualizer.process(null, root);

    String outputCode = AbstractCompilerTest.printTree(root);
    assertTrue(outputCode.contains("JSCompiler_StaticMethods_accumulate"));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithCallSiteInModuleLoadedBeforeDefinition() {
    String input =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) {this.total += value; return this.total}\n" +
        "function B() {}\n" +
        "B.prototype.double = function(value) {A.prototype.accumulate.call(this, value)}";

    Node root = AbstractCompilerTest.parseAndTraverse(input, abstractCompiler);
    DevirtualizePrototypeMethods devirtualizer =
        new DevirtualizePrototypeMethods(abstractCompiler);
    devirtualizer.process(null, root);

    String outputCode = AbstractCompilerTest.printTree(root);
    assertTrue(outputCode.contains("JSCompiler_StaticMethods_accumulate"));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithEmptyObject() {
    String input = "function A() { return {}; }";

    Node root = AbstractCompilerTest.parseAndTraverse(input, abstractCompiler);
    DevirtualizePrototypeMethods devirtualizer =
        new DevirtualizePrototypeMethods(abstractCompiler);
    devirtualizer.process(null, root);

    String outputCode = AbstractCompilerTest.printTree(root);
    assertTrue(outputCode.contains("Object.create"));
    assertFalse(outputCode.contains("JSCompiler_StaticMethods_"));
  }
}