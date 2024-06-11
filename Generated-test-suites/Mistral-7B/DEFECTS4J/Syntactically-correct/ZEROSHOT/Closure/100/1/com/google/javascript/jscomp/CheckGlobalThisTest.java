import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticMessageCategory;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal.Callback;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(new DiagnosticMessageCategory[] { DiagnosticMessageCategory.WARNING });
    Parser parser = new Parser(null, new AbstractCompiler(options));
    AbstractCompiler compiler = Mockito.spy(new AbstractCompiler(options));
    Checker checker = new Checker(options, compiler);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traverser = new NodeTraversal(parser.parse("function evil() { this.useful = undefined; }", null), checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checkGlobalThis.visit(this, n, parent);
      }
    };

    traverser.traverse();

    Node evilFunction = traverser.getRoot().getFirstChild();
    Node thisNode = evilFunction.getLastChild();
    Node thisPropertyAssign = thisNode.getNext();

    Mockito.verify(compiler, Mockito.times(1)).report(instanceOf(CheckGlobalThis.DiagnosticType.class));
  }

  @Test
  public void testNoCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(new DiagnosticMessageCategory[] { DiagnosticMessageCategory.WARNING });
    Parser parser = new Parser(null, new AbstractCompiler(options));
    AbstractCompiler compiler = Mockito.spy(new AbstractCompiler(options));
    Checker checker = new Checker(options, compiler);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traverser = new NodeTraversal(parser.parse("function evil() { this.x = 42; }", null), checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checkGlobalThis.visit(this, n, parent);
      }
    };

    traverser.traverse();

    Mockito.verify(compiler, Mockito.never()).report(instanceOf(CheckGlobalThis.DiagnosticType.class));
  }

  @Test
  public void testPrototypePropertyAccess() {
    CompilerOptions options = new CompilerOptions(new DiagnosticMessageCategory[] { DiagnosticMessageCategory.WARNING });
    Parser parser = new Parser(null, new AbstractCompiler(options));
    AbstractCompiler compiler = Mockito.spy(new AbstractCompiler(options));
    Checker checker = new Checker(options, compiler);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traverser = new NodeTraversal(parser.parse("function evil() { this.prototype.x = 42; }", null), checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checkGlobalThis.visit(this, n, parent);
      }
    };

    traverser.traverse();

    Mockito.verify(compiler, Mockito.never()).report(instanceOf(CheckGlobalThis.DiagnosticType.class));
  }

  @Test
  public void testPrototypeMethod() {
    CompilerOptions options = new CompilerOptions(new DiagnosticMessageCategory[] { DiagnosticMessageCategory.WARNING });
    Parser parser = new Parser(null, new AbstractCompiler(options));
    AbstractCompiler compiler = Mockito.spy(new AbstractCompiler(options));
    Checker checker = new Checker(options, compiler);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traverser = new NodeTraversal(parser.parse("function evil() { this.x = function() {}; }", null), checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checkGlobalThis.visit(this, n, parent);
      }
    };

    traverser.traverse();

    Mockito.verify(compiler, Mockito.never()).report(instanceOf(CheckGlobalThis.DiagnosticType.class));
  }

  @Test
  public void testConstructor() {
    CompilerOptions options = new CompilerOptions(new DiagnosticMessageCategory[] { DiagnosticMessageCategory.WARNING });
    Parser parser = new Parser(null, new AbstractCompiler(options));
    AbstractCompiler compiler = Mockito.spy(new AbstractCompiler(options));
    Checker checker = new Checker(options, compiler);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traverser = new NodeTraversal(parser.parse("function evil() { constructor: function() {} }", null), checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checkGlobalThis.visit(this, n, parent);
      }
    };

    traverser.traverse();

    Mockito.verify(compiler, Mockito.never()).report(instanceOf(CheckGlobalThis.DiagnosticType.class));
  }

  @Test
  public void testThisAnnotation() {
    CompilerOptions options = new CompilerOptions(new DiagnosticMessageCategory[] { DiagnosticMessageCategory.WARNING });
    Parser parser = new Parser(null, new AbstractCompiler(options));
    AbstractCompiler compiler = Mockito.spy(new AbstractCompiler(options));
    Checker checker = new Checker(options, compiler);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traverser = new NodeTraversal(parser.parse("/** @this {Object} */ function evil() { this.x = 42; }", null), checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checkGlobalThis.visit(this, n, parent);
      }
    };

    traverser.traverse();

    Mockito.verify(compiler, Mockito.never()).report(instanceOf(CheckGlobalThis.DiagnosticType.class));
  }

  @Test
  public void testAssignmentToThis() {
    CompilerOptions options = new CompilerOptions(new DiagnosticMessageCategory[] { DiagnosticMessageCategory.WARNING });
    Parser parser = new Parser(null, new AbstractCompiler(options));
    AbstractCompiler compiler = Mockito.spy(new AbstractCompiler(options));
    Checker checker = new Checker(options, compiler);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traverser = new NodeTraversal(parser.parse("function evil() { var a = this; }", null), checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checkGlobalThis.visit(this, n, parent);
      }
    };

    traverser.traverse();

    Mockito.verify(compiler, Mockito.never()).report(instanceOf(CheckGlobalThis.DiagnosticType.class));
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    CompilerOptions options = new CompilerOptions(new DiagnosticMessageCategory[] { DiagnosticMessageCategory.WARNING });
    Parser parser = new Parser(null, new AbstractCompiler(options));
    AbstractCompiler compiler = Mockito.spy(new AbstractCompiler(options));
    Checker checker = new Checker(options, compiler);
    Callback checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traverser = new NodeTraversal(parser.parse("function evil() { this.x = 42; }", null), checker) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node n, Node parent) {
        return checkGlobalThis.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        checkGlobalThis.visit(this, n, parent);
      }
    };

    traverser.traverse();

    Mockito.verify(compiler, Mockito.never()).report(instanceOf(CheckGlobalThis.DiagnosticType.class));
  }
}