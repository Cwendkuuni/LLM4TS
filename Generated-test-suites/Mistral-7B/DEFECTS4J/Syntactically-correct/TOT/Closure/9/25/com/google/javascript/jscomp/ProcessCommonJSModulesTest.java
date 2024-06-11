import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    CompilerOptions options = new CompilerOptions(true, null, null);
    Compiler compiler = new Compiler(options);

    // Test valid CommonJS module
    Node root = createTestNode("test1.js");
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(NodeFactory.getRootNode(), root);

    Node expected = createTestNode("test1_expected.js");
    assertEquals(expected.toSource(), root.toSource());

    // Test invalid CommonJS module
    root = createTestNode("test2.js");
    pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(NodeFactory.getRootNode(), root);

    Node expected2 = createTestNode("test2_expected.js");
    assertEquals(expected2.toSource(), root.toSource());

    // Test require call with relative path
    root = createTestNode("test3.js");
    pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(NodeFactory.getRootNode(), root);

    Node expected3 = createTestNode("test3_expected.js");
    assertEquals(expected3.toSource(), root.toSource());

    // Test require call with absolute path
    root = createTestNode("test4.js");
    pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(NodeFactory.getRootNode(), root);

    Node expected4 = createTestNode("test4_expected.js");
    assertEquals(expected4.toSource(), root.toSource());

    // Test require call with incorrect path format
    root = createTestNode("test5.js");
    pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(NodeFactory.getRootNode(), root);

    Node expected5 = createTestNode("test5_expected.js");
    assertEquals(expected5.toSource(), root.toSource());

    // Test require call with circular dependency
    root = createTestNode("test6.js");
    pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(NodeFactory.getRootNode(), root);

    Node expected6 = createTestNode("test6_expected.js");
    assertEquals(expected6.toSource(), root.toSource());
  }

  private Node createTestNode(String name) throws URISyntaxException {
    URI uri = new URI("file:/" + System.getProperty("user.dir") + "/src/" + name);
    return NodeFactory.getRootNode().getOrCreateChildNodes(
        NodeFactory.createText(uri.getPath()));
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testInvalidURI() throws IOException, URISyntaxException {
    Node root = NodeFactory.getRootNode();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(
        new Compiler(new CompilerOptions(true, null, null)), ".");
    pass.process(NodeFactory.getRootNode(), root);

    root.setString("require('invalid:uri:scheme')");
    pass.process(NodeFactory.getRootNode(), root);
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testMissingModuleName() throws IOException, URISyntaxException {
    Node root = NodeFactory.getRootNode();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(
        new Compiler(new CompilerOptions(true, null, null)), ".");
    pass.process(NodeFactory.getRootNode(), root);

    root.setString("require('')");
    pass.process(NodeFactory.getRootNode(), root);
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testMissingRequireCall()
      throws IOException, URISyntaxException {
    Node root = NodeFactory.getRootNode();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(
        new Compiler(new CompilerOptions(true, null, null)), ".");
    pass.process(NodeFactory.getRootNode(), root);

    root.setString("module.exports = 42;");
    pass.process(NodeFactory.getRootNode(), root);
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testMissingGoogRequire()
      throws IOException, URISyntaxException {
    Node root = NodeFactory.getRootNode();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(
        new Compiler(new CompilerOptions(true, null, null)), ".");
    pass.process(NodeFactory.getRootNode(), root);

    root.setString("require('module');");
    pass.process(NodeFactory.getRootNode(), root);
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testMissingGoogProvide()
      throws IOException, URISyntaxException {
    Node root = NodeFactory.getRootNode();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(
        new Compiler(new CompilerOptions(true, null, null)), ".");
    pass.process(NodeFactory.getRootNode(), root);

    root.setString("exports = module.exports;");
    pass.process(NodeFactory.getRootNode(), root);
  }
}