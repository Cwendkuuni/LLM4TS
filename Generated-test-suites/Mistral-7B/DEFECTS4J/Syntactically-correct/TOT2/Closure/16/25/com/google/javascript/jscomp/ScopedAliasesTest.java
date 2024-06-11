import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;
  private SourceFile testSourceFile;

  @Before
  public void setUp() throws IOException {
    compiler = new AbstractCompiler();
    transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String fileName, SourcePosition position) {
        return new AliasTransformation(fileName, position, Arrays.asList("goog", "g"), Arrays.asList("dom", "d"));
      }
    };
  }

  @Test
  public void testBasicScoping() {
    String testCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
    Node result = root.getLastChild();
    assertEquals(NodeUtil.makeString("goog.dom.createElement(goog.dom.TagName.DIV)"), result.toString());
  }

  @Test(expected = AssertionError.class)
  public void testImproperUsage() {
    String testCode = "goog.scope(function() {" +
        "goog.scope(function() {" +
        "});" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testBadParameters() {
    String testCode = "goog.scope(function(x) {" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testRefersThis() {
    String testCode = "goog.scope(function() {" +
        "function f() {" +
        "this.x = 1;" +
        "}" +
        "f();" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testUsesReturn() {
    String testCode = "goog.scope(function() {" +
        "return 1;" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testUsesThrow() {
    String testCode = "goog.scope(function() {" +
        "throw new Error();" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
  }

  @Test
  public void testMultipleAliases() {
    String testCode = "goog.scope(function() {" +
        "var g = goog;" +
        "var d = goog.dom;" +
        "var DIV = d.TagName.DIV;" +
        "d.createElement(DIV);" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
    Node result = root.getLastChild();
    assertEquals(NodeUtil.makeString("goog.dom.createElement(goog.dom.TagName.DIV)"), result.toString());
  }

  @Test(expected = AssertionError.class)
  public void testAliasRedefined() {
    String testCode = "goog.scope(function() {" +
        "var g = goog;" +
        "var g = 'test';" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
  }

  @Test(expected = AssertionError.class)
  public void testNonAliasLocal() {
    String testCode = "goog.scope(function() {" +
        "var x = 1;" +
        "});";

    testSourceFile = new SourceFile("test.js", testCode);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, testSourceFile.getAST());
    Node root = testSourceFile.getAST();
    scopedAliases.hotSwapScript(root, null);
  }
}