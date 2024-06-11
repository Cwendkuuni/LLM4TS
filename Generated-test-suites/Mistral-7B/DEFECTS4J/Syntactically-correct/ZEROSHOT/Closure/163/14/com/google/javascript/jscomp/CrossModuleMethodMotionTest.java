package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions.WarningLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.parser.util.SourceFile;
import com.google.javascript.rhino.JSON;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private JSModuleGraph moduleGraph;
  private IdGenerator idGenerator;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    options.setCheckLevel(CheckLevel.ERROR);
    options.setWarningLevel(WarningLevel.QUIRKS);
    compiler = new AbstractCompiler() {
      @Override
      public CompilerOptions getOptions() {
        return options;
      }
    };

    passManager = new CompilerPassManager(compiler);
    moduleGraph = new JSModuleGraph();
    idGenerator = new IdGenerator();
  }

  @Test
  public void testCrossModuleMethodMotion() {
    // Create a simple test case with two modules: A and B.
    // A has a prototype property b, which is a method.
    // B does not depend on A, but A depends on B.

    // Create source files for A and B
    SourceFile sourceFileA = new SourceFile("A.js",
        "function A() {}\n" +
        "A.prototype.b = function() {\n" +
        "  return 'A.b';\n" +
        "};");

    SourceFile sourceFileB = new SourceFile("B.js",
        "function B() {}\n" +
        "B.prototype.c = function() {\n" +
        "  return 'B.c';\n" +
        "};");

    // Compile A and B
    Node astA = compiler.parseAndCompileSingleFile(sourceFileA);
    Node astB = compiler.parseAndCompileSingleFile(sourceFileB);

    // Set up the CrossModuleMethodMotion pass
    CrossModuleMethodMotion crossModuleMethodMotion =
        new CrossModuleMethodMotion(compiler, idGenerator, true);
    passManager.addPass(crossModuleMethodMotion);

    // Run the compiler passes
    Diagnostics diagnostics = new Diagnostics();
    Node root = compiler.process(astA, astB, null, diagnostics);

    // Check that no errors were reported
    assertTrue(diagnostics.isErrorFree());

    // Check that the method b was moved from A to B
    NameInfo nameInfoB = findNameInfoB(root);
    assertSame(astB, nameInfoB.getModule());
    Node bMethod = nameInfoB.getSymbols().get(0).getValue();
    assertFalse(bMethod.isFunction());
    assertNull(bMethod.getParent());

    // Check that the stub method was added to A
    Node stubMethod = findStubMethod(root);
    assertSame(astA, stubMethod.getParent());
    assertFalse(stubMethod.isFunction());
    assertTrue(stubMethod.isCallExpr());
    assertEquals(1, stubMethod.getNumChildren());
    Node stubFunction = stubMethod.getChild(0);
    assertTrue(stubFunction.isFunction());
  }

  private NameInfo findNameInfoB(Node root) {
    for (NameInfo nameInfo : AnalyzePrototypeProperties.getAllNameInfo(root)) {
      if ("B.prototype.c".equals(nameInfo.name)) {
        return nameInfo;
      }
    }
    throw new AssertionError();
  }

  private Node findStubMethod(Node root) {
    for (Node node = root; node != null; node = node.getPreviousSibling()) {
      if (node.isCallExpr() && IR.isName(node.getChild(0), STUB_METHOD_NAME)) {
        return node;
      }
    }
    throw new AssertionError();
  }
}