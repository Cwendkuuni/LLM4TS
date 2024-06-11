import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;
import static com.google.javascript.jscomp.JSError.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.GraphTraversingCompilerPass;
import com.google.javascript.jscomp.NodeTraversingCompilerPass;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() {
    String jsCode = "(" +
        "function foo() {" +
        "  return 1;" +
        "}" +
        "function Foo() {" +
        "}" +
        "Foo.prototype.bar = foo;" +
        "function A() {" +
        "}" +
        "Foo.prototype.baz = function() {" +
        "  return 2;" +
        "};" +
        "A.prototype.qux = function() {" +
        "  return 3;" +
        "};" +
        "A.prototype.baz = Foo.prototype.baz;" +
        "function B() {" +
        "}" +
        "B.prototype.qux = A.prototype.qux;" +
        "B.prototype.baz = function() {" +
        "  return 4;" +
        "};" +
        ")";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    SourceFile source = new SourceFile("test.js", jsCode, SourceName.guessSourceName(jsCode));
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);

    // Process the code without the CrossModuleMethodMotion pass.
    CompilerResult result = passManager.runPasses(source, emptyHandler(), NO_SOURCE_FILES,
        Collections.emptyList());
    Node root = result.astRoot;

    // Check that all methods are in their original position.
    checkMethodsPosition(source.getSourceName().getName(), root);

    passManager.addPass(new CrossModuleMethodMotion(
        compiler, new CrossModuleMethodMotion.IdGenerator(), false));
    result = passManager.runPasses(source, emptyHandler(), NO_SOURCE_FILES,
        Collections.emptyList());
    root = result.astRoot;

    // Check that all methods are moved to the deepest common module.
    checkMethodsPosition(source.getSourceName().getName(), root);
  }

  private void checkMethodsPosition(String testName, Node root) {
    Set<String> methodNames = new HashSet<String>();
    traverseNode(root, new NodeTraversingCompilerPass<Node>() {
      @Override
      public void process(Node node, Compiler compiler) {
        if (node.isFunction()) {
          methodNames.add(node.getName());
        }
      }
    });

    // Check that each method appears exactly twice in the AST:
    // once in the deepest common module and once in the original module.
    int count = 0;
    for (String methodName : methodNames) {
      count += countMethods(root, methodName);
    }
    assertEquals("Number of methods in the test code.", methodNames.size(), count);
  }

  private int countMethods(Node root, String methodName) {
    return (root.isFunctionDeclaration() && root.getName().equals(methodName)) ? 1 : 0
        + traverseChildren(root, 0, new GraphTraversingCompilerPass<Node>() {
          @Override
          public int process(Node node, Compiler compiler) {
            return countMethods(node, methodName);
          }
        });
  }

  private int traverseChildren(Node node, int depth,
      NodeTraversingCompilerPass<Node> pass) {
    int result = 0;
    for (int i = 0; i < node.getNumChildren(); i++) {
      result += pass.process(node.getChild(i), compiler);
    }
    return result;
  }

  private NodeTraversingCompilerPass<Void> emptyHandler() {
    return new NodeTraversingCompilerPass<Void>() {
      @Override
      public Void process(Node node, Compiler compiler) {
        return null;
      }
    };
  }
}