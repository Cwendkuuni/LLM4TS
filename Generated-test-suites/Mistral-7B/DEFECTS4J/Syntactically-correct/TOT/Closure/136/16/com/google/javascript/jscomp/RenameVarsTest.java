import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RenameVarsTest {

  @org.junit.Test
  public void testRenameVars() throws Exception {
    String input = "function foo(a, b) {" +
                   "  var x = 1;" +
                   "  var y = 2;" +
                   "  var z = 3;" +
                   "  function bar(x, y) {" +
                   "    return x + y;" +
                   "  }" +
                   "  var a = 4;" +
                   "  var b = 5;" +
                   "  return bar(a, b);" +
                   "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = NodeFactory.parseTree(new SourceFile("test.js", input));

    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null, null);
    renameVars.process(NodeTraversal.EMPTY_NODE_TRAVERSAL, root);

    StringWriter writer = new StringWriter();
    compiler.compile(root, writer, null, null);

    String output = writer.toString();

    String expectedOutput = "function foo(a, b) {" +
                            "  var fa = 1;" +
                            "  var fb = 2;" +
                            "  var fz = 3;" +
                            "  function bar(x, y) {" +
                            "    return x + y;" +
                            "  }" +
                            "  var a = 4;" +
                            "  var b = 5;" +
                            "  return bar(a, b);" +
                            "}";

    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testRenameVarsWithPreserveAnonymousFunctionNames() throws Exception {
    String input = "function foo(a, b) {" +
                   "  var x = function() {" +
                   "    return 1;" +
                   "  };" +
                   "  var y = 2;" +
                   "  return x();" +
                   "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = NodeFactory.parseTree(new SourceFile("test.js", input));

    RenameVars renameVars = new RenameVars(compiler, null, false, true, false, null, null, null);
    renameVars.process(NodeTraversal.EMPTY_NODE_TRAVERSAL, root);

    StringWriter writer = new StringWriter();
    compiler.compile(root, writer, null, null);

    String output = writer.toString();

    String expectedOutput = "function foo(a, b) {" +
                            "  var x = function() {" +
                            "    return 1;" +
                            "  };" +
                            "  var y = 2;" +
                            "  return x();" +
                            "}";

    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testRenameVarsWithGeneratePseudoNames() throws Exception {
    String input = "function foo(a, b) {" +
                   "  var x = 1;" +
                   "  var $y = 2;" +
                   "  return x + $y;" +
                   "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = NodeFactory.parseTree(new SourceFile("test.js", input));

    RenameVars renameVars = new RenameVars(compiler, null, false, false, true, null, null, null);
    renameVars.process(NodeTraversal.EMPTY_NODE_TRAVERSAL, root);

    StringWriter writer = new StringWriter();
    compiler.compile(root, writer, null, null);

    String output = writer.toString();

    String expectedOutput = "function foo(a, b) {" +
                            "  var $fa = 1;" +
                            "  var $fb = 2;" +
                            "  return $fa + $fb;" +
                            "}";

    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testRenameVarsWithExternVariables() throws Exception {
    String input = "var a = 1;" +
                   "function foo(b) {" +
                   "  var x = 2;" +
                   "  return b + x;" +
                   "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = NodeFactory.parseTree(new SourceFile("test.js", input));

    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null, null,
        Collections.singletonList("a"));
    renameVars.process(NodeTraversal.EMPTY_NODE_TRAVERSAL, root);

    StringWriter writer = new StringWriter();
    compiler.compile(root, writer, null, null);

    String output = writer.toString();

    String expectedOutput = "var a = 1;" +
                            "function foo(b) {" +
                            "  var $x = 2;" +
                            "  return b + $x;" +
                            "}";

    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testRenameVarsWithLocalRenamingOnly() throws Exception {
    String input = "function foo(a, b) {" +
                   "  var x = 1;" +
                   "  var y = 2;" +
                   "  function bar(x, y) {" +
                   "    return x + y;" +
                   "  }" +
                   "  var a = 4;" +
                   "  var b = 5;" +
                   "  return bar(a, b);" +
                   "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = NodeFactory.parseTree(new SourceFile("test.js", input));

    RenameVars renameVars = new RenameVars(compiler, null, true, false, false, null, null, null);
    renameVars.process(NodeTraversal.EMPTY_NODE_TRAVERSAL, root);

    StringWriter writer = new StringWriter();
    compiler.compile(root, writer, null, null);

    String output = writer.toString();

    String expectedOutput = "function foo(a, b) {" +
                            "  var x1 = 1;" +
                            "  var y1 = 2;" +
                            "  function bar(x, y) {" +
                            "    return x + y;" +
                            "  }" +
                            "  var a = 4;" +
                            "  var b = 5;" +
                            "  return bar(a, b);" +
                            "}";

    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testRenameVarsWithReservedNames() throws Exception {
    String input = "function foo() {" +
                   "  var if = 1;" +
                   "  var else = 2;" +
                   "  return if + else;" +
                   "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = NodeFactory.parseTree(new SourceFile("test.js", input));

    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null,
        Sets.newHashSet("if", "else"), null);
    renameVars.process(NodeTraversal.EMPTY_NODE_TRAVERSAL, root);

    StringWriter writer = new StringWriter();
    compiler.compile(root, writer, null, null);

    String output = writer.toString();

    String expectedOutput = "function foo() {" +
                            "  var i = 1;" +
                            "  var e = 2;" +
                            "  return i + e;" +
                            "}";

    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testRenameVarsWithReservedCharacters() throws Exception {
    String input = "function foo() {" +
                   "  var $ = 1;" +
                   "  return $$;" +
                   "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = NodeFactory.parseTree(new SourceFile("test.js", input));

    RenameVars renameVars = new RenameVars(compiler, null, false, false, false, null,
        null, Arrays.asList('$'));
    renameVars.process(NodeTraversal.EMPTY_NODE_TRAVERSAL, root);

    StringWriter writer = new StringWriter();
    compiler.compile(root, writer, null, null);

    String output = writer.toString();

    String expectedOutput = "function foo() {" +
                            "  var a = 1;" +
                            "  return aa;" +
                            "}";

    assertEquals(expectedOutput, output);
  }
}