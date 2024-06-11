package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private CompilerResult result;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setCompilerPasses(Sets.newHashSet(new CompilerPass[] {
        new ProcessCommonJSModules(null, ".")
    }));
    compiler = new Compiler(options);
  }

  @After
  public void tearDown() throws IOException {
    if (result != null) {
      result.getTempFiles().forEach(File::delete);
    }
  }

  @Test
  public void testSimpleCommonJSModule() throws URISyntaxException {
    String input = "// file: a.js\n" +
        "var exports = module.exports = {\n" +
        "  foo: 'bar'\n" +
        "};\n" +
        "exports.hello = function() {\n" +
        "  return 'hello';\n" +
        "};\n";

    String expectedOutput = "// file: a.js\n" +
        "goog.provide('a');\n" +
        "\n" +
        "a.module$exports = {\n" +
        "  foo: 'bar'\n" +
        "};\n" +
        "\n" +
        "goog.provide('a.hello');\n" +
        "\n" +
        "a.hello = function() {\n" +
        "  return 'hello';\n" +
        "};\n";

    File inputFile = new File("a.js");
    result = compiler.compile(List.of(inputFile), null, null);
    assertEquals(expectedOutput, result.toSourceString(new File("a.js")));
    assertNull(result.getDiagnostics());
  }

  @Test
  public void testRelativeCommonJSModule() throws URISyntaxException {
    String aInput = "// file: a.js\n" +
        "var exports = module.exports = {\n" +
        "  foo: 'bar'\n" +
        "};\n";

    String bInput = "// file: b.js\n" +
        "var require = require('../a');\n" +
        "exports.hello = function() {\n" +
        "  return require.foo; // should be replaced with 'a.foo'\n" +
        "};\n";

    String expectedOutputA = "// file: a.js\n" +
        "goog.provide('a');\n" +
        "\n" +
        "a.module$exports = {\n" +
        "  foo: 'bar'\n" +
        "};\n" +
        "\n" +
        "goog.provide('a.hello');\n" +
        "\n" +
        "a.hello = function() {\n" +
        "  return 'hello';\n" +
        "};\n";

    String expectedOutputB = "// file: b.js\n" +
        "goog.provide('b');\n" +
        "\n" +
        "b.module$exports = {\n" +
        "  hello: function() {\n" +
        "    return 'hello';\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "goog.provide('b.hello');\n" +
        "\n" +
        "b.hello = function() {\n" +
        "  return a.foo;\n" +
        "};\n" +
        "\n" +
        "goog.require('a');\n";

    File aFile = new File("a.js");
    File bFile = new File("b.js");

    result = compiler.compile(List.of(aFile, bFile), null, null);
    String aOutput = result.toSourceString(aFile);
    String bOutput = result.toSourceString(bFile);

    assertEquals(expectedOutputA, aOutput);
    assertEquals(expectedOutputB, bOutput);
  }

  @Test
  public void testNoModule() throws URISyntaxException {
    String input = "// file: a.js\n" +
        "var foo = 'bar';\n";

    String expectedOutput = "// file: a.js\n" +
        "goog.provide('a');\n" +
        "\n" +
        "a.foo = 'bar';\n";

    File inputFile = new File("a.js");
    result = compiler.compile(List.of(inputFile), null, null);
    assertEquals(expectedOutput, result.toSourceString(new File("a.js")));
    assertNull(result.getDiagnostics());
  }

  @Test
  public void testRequireCallWithInvalidModuleName() throws URISyntaxException {
    String input = "// file: a.js\n" +
        "var require = require('invalid-module');\n";

    File inputFile = new File("a.js");
    result = compiler.compile(List.of(inputFile), null, null);
    assertNull(result.getDiagnostics());
  }

  @Test
  public void testRequireCallWithMissingModule() throws URISyntaxException {
    String aInput = "// file: a.js\n" +
        "var exports = module.exports = {\n" +
        "  foo: 'bar'\n" +
        "};\n";

    String bInput = "// file: b.js\n" +
        "var require = require('a');\n";

    File aFile = new File("a.js");
    File bFile = new File("b.js");

    result = compiler.compile(List.of(aFile, bFile), null, null);

    assertEquals(1, result.getDiagnostics().size());
    assertEquals("ReferenceError: a is not defined", result.getDiagnostics()
        .get(0).getMessage());
  }

  @Test
  public void testNoRequireCall() throws URISyntaxException {
    String input = "// file: a.js\n" +
        "var foo = 'bar';\n";

    File inputFile = new File("a.js");
    result = compiler.compile(List.of(inputFile), null, null);
    assertNull(result.getDiagnostics());
  }

  @Test
  public void testNoModuleExports() throws URISyntaxException {
    String input = "// file: a.js\n" +
        "var foo = 'bar';\n";

    File inputFile = new File("a.js");
    result = compiler.compile(List.of(inputFile), null, null);
    assertEquals("", result.toSourceString(new File("a.js")));
  }
}