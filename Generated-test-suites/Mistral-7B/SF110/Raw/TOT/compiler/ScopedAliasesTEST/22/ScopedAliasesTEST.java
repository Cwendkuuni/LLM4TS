package com.google.javascript.jscomp.test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.TestSourceFile;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Tests for the {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformations = Sets.newEnumSet(CompilerOptions.AliasTransformation.GOOG);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new IdentityAliasTransformationHandler());

    String source = "({\n" +
        "  goog.scope(function() {\n" +
        "    var dom = goog.dom;\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "    var x = goog.x;\n" +
        "    var y = goog.y;\n" +
        "    dom.createElement(DIV);\n" +
        "  })\n" +
        "})";

    SourceFile sf = new TestSourceFile("Test.java", source);

    HotSwapCompilerPass.HotSwapResult result = scopedAliases.process(null, sf.getRootNode());
    Assert.assertTrue(result.getErrorCount() == 0);

    String expectedOutput = "com.google.javascript.jscomp.ScopedAliases.goog_x";
    String actualOutput = result.getOutputFile().getContent();
    Assert.assertEquals(expectedOutput, actualOutput);

    source = "({\n" +
        "  goog.scope(function() {\n" +
        "    var dom = goog.dom;\n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "    var x = goog.x;\n" +
        "    var y = goog.y;\n" +
        "    var z = goog.z;\n" +
        "    dom.createElement(DIV);\n" +
        "  })\n" +
        "})";

    sf = new TestSourceFile("Test2.java", source);

    result = scopedAliases.process(null, sf.getRootNode());
    Assert.assertTrue(result.getErrorCount() > 0);

    Map<String, String> errors = Maps.newHashMap();
    errors.put(3, "JSC_GOOG_SCOPE_ALIAS_REDEFINED: The alias 'z' is assigned a value more than once");
    Assert.assertEquals(errors, scopedAliases.getErrors());
  }
}