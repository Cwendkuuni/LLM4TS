import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    nodeFactory = options.getInitialSourceFile().getNodeFactory();
  }

  @Test
  public void testProcessCommonJSModules() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    SourceFile input = new TestSourceFile("input.js", "var x = require('./output.js');");
    Node root = traverse(compiler, input.getTree());
    processor.process(input.getExterns(), root);
    Node expectedOutput = nodeFactory.getProgram();
    expectedOutput.addChildToFront(expectedModuleOutput("input.js", "x"));
    expectedOutput.addChildToFront(expectedModuleOutput("output.js", "module.exports"));
    assertEquals(expectedOutput.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithDependencies() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", true);
    SourceFile input = new TestSourceFile("input.js", "var x = require('./output.js'); var y = require('./y.js');");
    Node root = traverse(compiler, input.getTree());
    processor.process(input.getExterns(), root);
    Node expectedOutput = nodeFactory.getProgram();
    expectedOutput.addChildToFront(expectedModuleOutput("input.js", "x"));
    expectedOutput.addChildToFront(expectedModuleOutput("output.js", "module.exports"));
    expectedOutput.addChildToFront(expectedModuleOutput("y.js", "module.exports"));
    expectedOutput.addChildToFront(expectedRequireStatements("input.js", Sets.newHashSet("output.js", "y.js")));
    assertEquals(expectedOutput.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithSameFilename() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    SourceFile input = new TestSourceFile("input.js", "var x = require('./input.js');");
    Node root = traverse(compiler, input.getTree());
    processor.process(input.getExterns(), root);
    Node expectedOutput = nodeFactory.getProgram();
    expectedOutput.addChildToFront(expectedModuleOutput("input.js", "module.exports"));
    expectedOutput.addChildToFront(expectedModuleOutput("input.js", "x"));
    assertEquals(expectedOutput.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativPath() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    SourceFile input = new TestSourceFile("input.js", "var x = require('./output/submodule.js');");
    Node root = traverse(compiler, input.getTree());
    processor.process(input.getExterns(), root);
    Node expectedOutput = nodeFactory.getProgram();
    expectedOutput.addChildToFront(expectedModuleOutput("input.js", "x"));
    expectedOutput.addChildToFront(expectedModuleOutput("output/submodule.js", "module.exports"));
    assertEquals(expectedOutput.toString(), root.toString());
  }

  @Test
  public void testProcessCommonJSModulesWithParentPath() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    SourceFile input = new TestSourceFile("input.js", "var x = require('../output.js');");
    Node root = traverse(compiler, input.getTree());
    processor.process(input.getExterns(), root);
    Node expectedOutput = nodeFactory.getProgram();
    expectedOutput.addChildToFront(expectedModuleOutput("input.js", "x"));
    expectedOutput.addChildToFront(expectedModuleOutput("output.js", "module.exports"));
    assertEquals(expectedOutput.toString(), root.toString());
  }

  private Node expectedModuleOutput(String filename, String variableName) {
    Node module = nodeFactory.getObjectLiteral();
    Node exports = nodeFactory.getObjectLiteral();
    exports.addChild(nodeFactory.getMember(nodeFactory.getIdentifier("exports"), null));
    exports.addChild(nodeFactory.getObjectLiteral());
    module.addChild(nodeFactory.getMember(nodeFactory.getIdentifier("module"), null));
    module.addChild(nodeFactory.getObjectLiteral());
    module.addChild(nodeFactory.getMember(nodeFactory.getIdentifier(filename), null));
    module.addChild(nodeFactory.getMember(nodeFactory.getIdentifier(variableName), exports));
    return module;
  }

  private Node expectedRequireStatements(String filename, Set<String> dependencies) {
    Node statement = nodeFactory.getCall(
        nodeFactory.getIdentifier("goog.require"), nodeFactory.getArray(dependencies.toArray(new String[0]))).cloneTree();
    statement.putProp(Node.ORIGINALNAME_PROP, "require");
    Node block = nodeFactory.getBlock();
    block.addChildToBack(statement);
    return block;
  }
}