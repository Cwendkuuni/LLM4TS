import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() throws Exception {
    List<HotSwapCompilerPass> passes = Lists.newArrayList();
    passes.add(new ScopedAliases(new TestCompiler(), null, null));

    SourceFile source = new SourceFile("Test.js",
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});");

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.NEVER);

    Node root = TestCompiler.compile(source, passes, options);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        root.toString());

    // Test with alias transformation enabled
    options.setAliasTransformation(AliasTransformation.ALWAYS);
    passes.clear();
    passes.add(new ScopedAliases(new TestCompiler(), null, null));

    root = TestCompiler.compile(source, passes, options);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        root.toString());

    // Test with invalid goog.scope usage
    source = new SourceFile("TestInvalid.js",
        "goog.scope(function() {" +
        "  function foo() {" +
        "    return this.bar;" +
        "  }" +
        "});");

    passes.clear();
    passes.add(new ScopedAliases(new TestCompiler(), null, null));

    try {
      TestCompiler.compile(source, passes, options);
      fail("Expected GOOG_SCOPE_REFERENCES_THIS error.");
    } catch (AssertionError e) {
      // Expected
    }

    // Test with invalid goog.scope parameters
    source = new SourceFile("TestInvalid.js",
        "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "  });" +
        "});");

    passes.clear();
    passes.add(new ScopedAliases(new TestCompiler(), null, null));

    try {
      TestCompiler.compile(source, passes, options);
      fail("Expected GOOG_SCOPE_USED_IMPROPERLY error.");
    } catch (AssertionError e) {
      // Expected
    }

    // Test with alias redefinition
    source = new SourceFile("TestInvalid.js",
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var DIV = 'div';" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});");

    passes.clear();
    passes.add(new ScopedAliases(new TestCompiler(), null, null));

    try {
      TestCompiler.compile(source, passes, options);
      fail("Expected GOOG_SCOPE_ALIAS_REDEFINED error.");
    } catch (AssertionError e) {
      // Expected
    }

    // Test with non-alias local variable
    source = new SourceFile("TestInvalid.js",
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var div = goog.dom.createElement(DIV);" +
        "});");

    passes.clear();
    passes.add(new ScopedAliases(new TestCompiler(), null, null));

    try {
      TestCompiler.compile(source, passes, options);
      fail("Expected GOOG_SCOPE_NON_ALIAS_LOCAL error.");
    } catch (AssertionError e) {
      // Expected
    }
  }
}