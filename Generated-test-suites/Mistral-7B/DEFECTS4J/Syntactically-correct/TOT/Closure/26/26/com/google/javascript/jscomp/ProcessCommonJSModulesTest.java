import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessingEnvironment.NodeType.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() throws IOException {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    nodeFactory = compiler.getFactory();
    processor = new ProcessCommonJSModules(compiler, ".");
  }

  @Test
  public void testSimpleModule() throws IOException, URISyntaxException {
    String input = "exports.a = 1;";
    Node node = parseNode(input);
    processor.process(nodeFactory.getEmptyRootNode(), node);

    Node result = getModuleExports(node);
    Node a = result.getFirstChild();
    assertEquals(1, a.getIntegerLiteral().getValue());
    assertEquals("a", a.getString());
  }

  @Test
  public void testNestedModules() throws IOException, URISyntaxException {
    String moduleA = "exports.a = 1;";
    String moduleB = "exports.b = require('./moduleA');";

    Node moduleANode = parseNode(moduleA);
    Node moduleBNode = parseNode(moduleB);

    Node moduleAModule = getModule(moduleANode);
    Node moduleBModule = getModule(moduleBNode);

    moduleAModule.addChild(moduleANode);
    moduleBModule.addChild(moduleBNode);

    Node root = nodeFactory.getEmptyRootNode();
    processor.process(nodeFactory.getEmptyRootNode(), root);
    root.addChild(moduleAModule);

    Node result = getModuleExports(root);
    Node a = result.getFirstChild();
    Node b = a.getNextSibling();
    assertEquals(1, a.getIntegerLiteral().getValue());
    assertEquals("a", a.getString());
    assertEquals(1, b.getIntegerLiteral().getValue());
    assertEquals("b", b.getString());
  }

  @Test
  public void testCircularDependency() throws IOException, URISyntaxException {
    String moduleA = "exports.a = require('./moduleB');";
    String moduleB = "exports.b = require('./moduleA');";

    Node moduleANode = parseNode(moduleA);
    Node moduleBNode = parseNode(moduleB);

    Node moduleAModule = getModule(moduleANode);
    Node moduleBModule = getModule(moduleBNode);

    moduleAModule.addChild(moduleANode);
    moduleBModule.addChild(moduleBNode);

    Node root = nodeFactory.getEmptyRootNode();
    processor.process(nodeFactory.getEmptyRootNode(), root);
    root.addChild(moduleAModule);
    root.addChild(moduleBModule);

    // Should not crash
    getModuleExports(root);
  }

  @Test
  public void testFunctionCall() throws IOException, URISyntaxException {
    String module = "exports.a = function() { return 1; };";
    String requireCall = "exports.b = require('./module');";

    Node moduleNode = parseNode(module);
    Node requireCallNode = parseNode(requireCall);

    Node moduleModule = getModule(moduleNode);
    Node requireCallModule = getModule(requireCallNode);

    moduleModule.addChild(moduleNode);
    requireCallModule.addChild(requireCallNode);

    Node root = nodeFactory.getEmptyRootNode();
    processor.process(nodeFactory.getEmptyRootNode(), root);
    root.addChild(moduleModule);

    Node result = getModuleExports(root);
    Node a = result.getFirstChild();
    Node func = a.getNextSibling();
    Node b = func.getNextSibling();

    assertEquals(1, func.callFunction(null, new Object[0]).getIntegerLiteral()
        .getValue());
    assertEquals("a", a.getString());
    assertEquals("b", b.getString());
  }

  @Test
  public void testMultipleModules() throws IOException, URISyntaxException {
    String moduleA = "exports.a = 1;";
    String moduleB = "exports.b = 1;";

    Node moduleANode = parseNode(moduleA);
    Node moduleBNode = parseNode(moduleB);

    Node moduleAModule = getModule(moduleANode);
    Node moduleBModule = getModule(moduleBNode);

    moduleAModule.addChild(moduleANode);
    moduleBModule.addChild(moduleBNode);

    Node root = nodeFactory.getEmptyRootNode();
    processor.process(nodeFactory.getEmptyRootNode(), root);
    root.addChild(moduleAModule);
    root.addChild(moduleBModule);

    Node result = getModuleExports(root);
    Set<String> expected = new HashSet<>(Arrays.asList("a", "b"));
    Set<String> actual = new HashSet<>();
    for (Node child = result.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      actual.add(child.getString());
    }
    assertEquals(expected, actual);
  }

  private Node parseNode(String input) throws IOException {
    Node node = parser(input);
    processor.process(nodeFactory.getEmptyRootNode(), node);
    return node;
  }

  private Node getModule(Node node) {
    for (Node child = node; child != null; child = child.getParent()) {
      if (child instanceof JSModule) {
        return (JSModule) child;
      }
    }
    throw new RuntimeException("Expected JSModule");
  }

  private Node getModuleExports(Node node) {
    for (Node child = node; child != null; child = child.getNextSibling()) {
      if ("module.exports".equals(child.getQualifiedName())) {
        return child;
      }
    }
    throw new RuntimeException("Expected 'module.exports'");
  }

  private Node parser(String code) throws IOException {
    String[] files = {new File("").getAbsolutePath() + File.separator + "input.js"};
    return compiler.parseTree(Arrays.asList(code), files, null, null, null);
  }
}