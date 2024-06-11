import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.Traverse;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {

  @Test
  public void testSimpleModule() {
    String input = "var foo = 'bar';";
    CompilerInput ci = new CompilerInput("test.js", input);
    ProcessCommonJSModules p = new ProcessCommonJSModules(
        new Processor(), ".");
    p.process(ci, null);
    Node root = ci.getRootNode();
    Node expected = IR.exprResult(IR.string("bar"));
    Traverse.traverse(p.compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName() && n.getString().equals("foo")) {
          assertEquals(expected, n.getParent().getChildAtIndex(1));
        }
      }
    });
  }

  @Test
  public void testModuleWithRequire() {
    String input = "var foo = require('other');";
    CompilerInput ci = new CompilerInput("test.js", input);
    ProcessCommonJSModules p = new ProcessCommonJSModules(
        new Processor(), ".", false);
    p.process(ci, null);

    String otherInput = "var bar = 'baz';";
    CompilerInput otherCi = new CompilerInput("other.js", otherInput);
    JSModule otherModule = new JSModule("other");
    otherModule.addAndOverrideModule(otherCi);
    otherCi.setModule(otherModule);

    Traverse.traverse(p.compiler, ci.getRootNode(), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 1 &&
            n.getFirstChild().getQualifiedName().equals("require")) {
          assertEquals("other", n.getChildAtIndex(0).getString());
          assertNull(n.getParent().getChildAtIndex(1).getParent());
          assertNull(n.getParent().getChildAtIndex(1).getQualifiedName());
        }
      }
    });
  }

  @Test
  public void testModuleWithModuleExports() {
    String input = "module.exports = 'foo';";
    CompilerInput ci = new CompilerInput("test.js", input);
    ProcessCommonJSModules p = new ProcessCommonJSModules(
        new Processor(), ".", false);
    p.process(ci, null);

    Traverse.traverse(p.compiler, ci.getRootNode(), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName() && n.getString().equals("module.exports")) {
          assertEquals("foo", n.getChildAtIndex(0).getString());
        }
      }
    });
  }

  @Test(expected = RuntimeException.class)
  public void testMalformedURI() {
    String input = "var foo = require('//google.com/module');";
    CompilerInput ci = new CompilerInput("test.js", input);
    ProcessCommonJSModules p = new ProcessCommonJSModules(
        new Processor(), ".", false);
    p.process(ci, null);
  }

  @Test
  public void testModuleWithRelativePath() {
    String input = "var foo = require('./other');";
    CompilerInput ci = new CompilerInput("test.js", input);
    ProcessCommonJSModules p = new ProcessCommonJSModules(
        new Processor(), ".", false);
    p.process(ci, null);

    String otherInput = "var bar = 'baz';";
    CompilerInput otherCi = new CompilerInput("./other.js", otherInput);
    JSModule otherModule = new JSModule("other");
    otherModule.addAndOverrideModule(otherCi);
    otherCi.setModule(otherModule);

    Traverse.traverse(p.compiler, ci.getRootNode(), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 1 &&
            n.getFirstChild().getQualifiedName().equals("require")) {
          String expected = "other";
          assertEquals(expected, n.getChildAtIndex(0).getString());
          assertNull(n.getParent().getChildAtIndex(1).getParent());
          assertNull(n.getParent().getChildAtIndex(1).getQualifiedName());
        }
      }
    });
  }

  @Test
  public void testModuleWithCyclicDependency() {
    String input1 = "var foo = require('bar');";
    CompilerInput ci1 = new CompilerInput("test1.js", input1);

    String input2 = "var bar = require('foo');";
    CompilerInput ci2 = new CompilerInput("test2.js", input2);

    ProcessCommonJSModules p = new ProcessCommonJSModules(
        new Processor(), ".", false);
    JSModule fooModule = new JSModule("foo");
    JSModule barModule = new JSModule("bar");
    fooModule.addAndOverrideModule(ci1);
    barModule.addAndOverrideModule(ci2);
    ci1.setModule(fooModule);
    ci2.setModule(barModule);

    p.process(ci1, null);
    p.process(ci2, null);

    assertThrows(RuntimeException.class, () -> {
      Traverse.traverse(p.compiler, ci1.getRootNode(), new AbstractPostOrderCallback() {
        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
          if (n.isCall() && n.getChildCount() == 1 &&
              n.getFirstChild().getQualifiedName().equals("require")) {
            JSModule module = n.getChildAtIndex(0).getModule();
            assertEquals("bar", module.getName());
          }
        }
      });
    });
  }
}