import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler.SourceFile;
import com.google.javascript.rhino.IR;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    Compiler compiler = new Compiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "test/");

    SourceFile input = new SourceFile("test/input.js",
        "function foo() { require('bar'); }", "test/input.js");
    Node root = input.getRootNode();

    pass.process(input.getExterns(), root);

    Node result = root.getChildAtIndex(0);
    assertEquals("function foo() { goog.require('bar'); }", result.toSource());

    // Test for require call with relative path
    input = new SourceFile("test/input2.js",
        "function foo() { require('./bar'); }", "test/input2.js");
    root = input.getRootNode();
    pass.process(input.getExterns(), root);
    result = root.getChildAtIndex(0);
    assertEquals("function foo() { goog.require('test:bar'); }", result.toSource());

    // Test for require call with absolute path
    input = new SourceFile("test/input3.js",
        "function foo() { require('/test/bar.js'); }", "test/input3.js");
    root = input.getRootNode();
    pass.process(input.getExterns(), root);
    result = root.getChildAtIndex(0);
    assertEquals("function foo() { goog.require('test:bar.js'); }", result.toSource());

    // Test for require call with non-existing module
    input = new SourceFile("test/input4.js",
        "function foo() { require('non-existing'); }", "test/input4.js");
    root = input.getRootNode();
    pass.process(input.getExterns(), root);
    result = root.getChildAtIndex(0);
    assertEquals("function foo() { goog.require('non-existing'); }", result.toSource());

    // Test for module with exports
    input = new SourceFile("test/input5.js",
        "var foo = require('bar').module$exports.baz;", "test/input5.js");
    root = input.getRootNode();
    pass.process(input.getExterns(), root);
    result = root.getChildAtIndex(0);
    assertEquals("var foo = bar.module$exports.baz;", result.toSource());

    // Test for module without exports
    input = new SourceFile("test/input6.js",
        "require('bar');", "test/input6.js");
    root = input.getRootNode();
    pass.process(input.getExterns(), root);
    result = root.getChildAtIndex(0);
    assertEquals("goog.require('bar');", result.toSource());
  }
}