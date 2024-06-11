import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.FileSource;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerPassManager passManager;
  private File inputFile;
  private File outputFile;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    passManager = compiler.getPassManager();
    inputFile = File.createTempFile("test", ".js");
    outputFile = File.createTempFile("test", ".js");
  }

  @After
  public void tearDown() throws Exception {
    inputFile.delete();
    outputFile.delete();
  }

  @Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String input =
        "var foo = require('bar');\n" +
        "exports.baz = function() {\n" +
        "  return foo.quux();\n" +
        "};";

    String filename = "test.js";
    FileSource inputSource = new FileSource(new URI(filename), input);
    passManager.addPass(new ProcessCommonJSModules(compiler, "" + File.separator, true));

    compiler.compile(inputSource, outputFile, null, null, null);

    Node root = nodeFactory.parseTree(outputFile);
    NodeTraversal.traverse(compiler, root, new TestTraversalCallback());

    Set<String> expected = Sets.newHashSet("bar", "test$exports");
    Set<String> actual = Sets.newHashSet();
    NodeTraversal.traverse(compiler, root, new CollectRequiresCallback(actual));

    assertEquals(expected, actual);
  }

  private static class TestTraversalCallback extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() >= 2 &&
          "goog.provide".equals(n.getFirstChild().getQualifiedName())) {
        assertEquals("test$" + ProcessCommonJSModules.MODULE_NAME_PREFIX,
            n.getChildAtIndex(1).getString());
      }
    }
  }

  private static class CollectRequiresCallback extends AbstractPostOrderCallback {
    private final Set<String> requires;

    CollectRequiresCallback(Set<String> requires) {
      this.requires = requires;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() >= 2 &&
          "require".equals(n.getFirstChild().getQualifiedName())) {
        requires.add(n.getChildAtIndex(1).getString());
      }
    }
  }
}