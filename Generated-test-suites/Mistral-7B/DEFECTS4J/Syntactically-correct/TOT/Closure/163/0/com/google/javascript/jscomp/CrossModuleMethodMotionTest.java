import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.jscomp.JSError.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CrossModuleMethodMotionTest {

  public static void main(String[] args) throws Exception {
    testCrossModuleMethodMotion();
  }

  private static void testCrossModuleMethodMotion() throws Exception {
    String sourceCode =
        "var Foo = function() {" +
        "  this.bar = function() {" +
        "    return 'foo.bar';" +
        "  }";

    String otherModuleSourceCode =
        "var Bar = function() {" +
        "  this.baz = function() {" +
        "    return 'bar.baz';" +
        "  }";
        // Depending on Foo.prototype.bar
        "Bar.prototype.qux = function() {" +
        "  return this.bar();" +
        "}";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULT);
    options.setWarnUndefinedGlobals(false);
    options.setShowWarnings(false);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    SourceFile sourceFile = new SourceFile("Foo.js", sourceCode);
    SourceFile otherModuleFile = new SourceFile("OtherModule.js", otherModuleSourceCode);
    List<SourceFile> files = ImmutableList.of(sourceFile, otherModuleFile);

    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    Node root = null;
    for (SourceFile file : files) {
      Node externs = null;
      Node node = Parser.parse(file.getText(), sourceFile.getCharPositionInLine(), sourceFile.getSourceName(), null, null, null, compiler.getOptions().getLanguageVersion(), null, externs);
      if (externs != null) {
        node = new NodeTraversal().traverse(node, new AbstractNodeVisitor() {
          @Override
          public void visit(NodeTraversal nt, Node parent, Node node) {
            if (node.isBlock()) {
              parent.replaceChildren(parent.getChildren(), Arrays.asList(node.getFirstChild(), node.getLastChild()));
              return;
            }
            if (node.isExpressionStatement()) {
              parent.addChildToFront(node.getFirstChild());
              parent.removeChild(node);
              return;
            }
            super.visit(nt, parent, node);
          }
        });
      }
      if (root == null) {
        root = node;
      } else {
        pass.process(externs, node);
      }
    }

    // Check that the method 'bar' was moved to OtherModule
    JSModule fooModule = moduleGraph(root).getModule("Foo");
    JSModule otherModule = moduleGraph(root).getModule("OtherModule");
    Node fooBarMethod = getMethod(fooModule, "bar");
    Node otherModuleBarMethod = getMethod(otherModule, "bar");
    assertNotNull(fooBarMethod);
    assertNull(fooBarMethod.getParent()); // Should have been replaced with a stub
    assertNotNull(otherModuleBarMethod);
    assertSame(fooBarMethod.getCode(), otherModuleBarMethod.getCode());

    // Check that the method 'qux' in OtherModule depends on 'bar'
    Node otherModuleQuxMethod = getMethod(otherModule, "qux");
    Node otherModuleQuxDependency = otherModuleQuxMethod.getFirstChild();
    Node expectedDependency = getMethodDependency(fooModule, "bar");
    assertEquals(expectedDependency, otherModuleQuxDependency);

    // Check that no warnings or errors were reported
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  private static Node getMethod(JSModule module, String name) {
    Iterator<Symbol> symbols = module.getSymbolTable().values().iterator();
    while (symbols.hasNext()) {
      Symbol symbol = symbols.next();
      if (symbol instanceof Property && ((Property) symbol).getName().equals(name)) {
        return ((Property) symbol).getValue();
      }
    }
    return null;
  }

  private static Node getMethodDependency(JSModule module, String name) {
    Iterator<Symbol> symbols = module.getSymbolTable().values().iterator();
    while (symbols.hasNext()) {
      Symbol symbol = symbols.next();
      if (symbol instanceof Property && ((Property) symbol).getName().equals(name)) {
        return ((Property) symbol).getPrototype();
      }
    }
    return null;
  }

  private static JSModuleGraph moduleGraph(Node root) {
    return compiler.getModuleGraph();
  }

  private static Compiler compiler() {
    return new Compiler();
  }
}