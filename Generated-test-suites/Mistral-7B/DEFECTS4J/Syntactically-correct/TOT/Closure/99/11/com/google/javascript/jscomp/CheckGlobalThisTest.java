import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new DiagnosticGroup(), new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          new CheckGlobalThis(compiler, WARNING).traverse(t, n, parent);
        }
      }
    });

    // Test safe cases
    String safeCode1 = "function safeFunction() {}";
    String safeCode2 = "var safeVar = function() {};";
    String safeCode3 = "function safeFunctionWithPrototype() { return this; }";
    String safeCode4 = "@constructor function SafeConstructor() {}";
    String safeCode5 = "function safeFunctionWithThis() { @this return this; }";

    Node safeNode1 = NodeFactory.parseTree(safeCode1);
    Node safeNode2 = NodeFactory.parseTree(safeCode2);
    Node safeNode3 = NodeFactory.parseTree(safeCode3);
    Node safeNode4 = NodeFactory.parseTree(safeCode4);
    Node safeNode5 = NodeFactory.parseTree(safeCode5);

    checker.checkSource(new String[] { safeCode1 }, new String[] { safeCode1 });
    checker.checkSource(new String[] { safeCode2 }, new String[] { safeCode2 });
    checker.checkSource(new String[] { safeCode3 }, new String[] { safeCode3 });
    checker.checkSource(new String[] { safeCode4 }, new String[] { safeCode4 });
    checker.checkSource(new String[] { safeCode5 }, new String[] { safeCode5 });

    DiagnosticGroup result = checker.getDiagnostics();
    assertThat(result.getNumberOfErrors(), is(0));

    // Test unsafe cases
    String unsafeCode1 = "this.variable = 1";
    String unsafeCode2 = "function unsafeFunction() { this.property = 1; }";
    String unsafeCode3 = "var unsafeVar = function() { this.property = 1; }";
    String unsafeCode4 = "function unsafeConstructor() {}";

    Node unsafeNode1 = NodeFactory.parseTree(unsafeCode1);
    Node unsafeNode2 = NodeFactory.parseTree(unsafeCode2);
    Node unsafeNode3 = NodeFactory.parseTree(unsafeCode3);
    Node unsafeNode4 = NodeFactory.parseTree(unsafeCode4);

    checker.checkSource(new String[] { unsafeCode1 }, new String[] { unsafeCode1 });
    checker.checkSource(new String[] { unsafeCode2 }, new String[] { unsafeCode2 });
    checker.checkSource(new String[] { unsafeCode3 }, new String[] { unsafeCode3 });
    checker.checkSource(new String[] { unsafeCode4 }, new String[] { unsafeCode4 });

    result = checker.getDiagnostics();
    for (DiagnosticType diagnostic : new DiagnosticType[] { CheckGlobalThis.GLOBAL_THIS }) {
      String message = diagnostic.getMessage();
      int lineNumber = 1;
      int columnNumber = 1;
      for (DiagnosticType.Entry entry : result.getEntries()) {
        if (entry.getDiagnosticType() == diagnostic &&
            entry.getSourceName().equals(unsafeCode1) &&
            entry.getLineNumber() == lineNumber &&
            entry.getColumnNumber() == columnNumber) {
          checker.report(entry.getSourcePosition(), WARNING, diagnostic);
          break;
        }
        lineNumber++;
      }
    }

    assertThat(result.getNumberOfWarnings(), is(4));
  }

  @Test
  public void testCheckGlobalThisWithAssignments() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new DiagnosticGroup(), new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          new CheckGlobalThis(compiler, WARNING).traverse(t, n, parent);
        }
      }
    });

    String code = "var a = function() { this.property = 1; }";
    Node node = NodeFactory.parseTree(code);

    checker.checkSource(new String[] { code }, new String[] { code });

    DiagnosticGroup result = checker.getDiagnostics();
    for (DiagnosticType diagnostic : new DiagnosticType[] { CheckGlobalThis.GLOBAL_THIS }) {
      String message = diagnostic.getMessage();
      int lineNumber = 1;
      int columnNumber = 1;
      for (DiagnosticType.Entry entry : result.getEntries()) {
        if (entry.getDiagnosticType() == diagnostic &&
            entry.getSourceName().equals(code) &&
            entry.getLineNumber() == lineNumber &&
            entry.getColumnNumber() >= columnNumber) {
          checker.report(entry.getSourcePosition(), WARNING, diagnostic);
          columnNumber = entry.getColumnNumber();
        }
      }
    }

    assertThat(result.getNumberOfWarnings(), is(1));
  }

  @Test
  public void testCheckGlobalThisWithFunctionExpression() {
    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new DiagnosticGroup(), new NodeTraversal.RootVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          new CheckGlobalThis(compiler, WARNING).traverse(t, n, parent);
        }
      }
    });

    String code = "function unsafeFunction() { this.property = 1; }";
    Node node = NodeFactory.parseTree(code);

    checker.checkSource(new String[] { code }, new String[] { code });

    DiagnosticGroup result = checker.getDiagnostics();
    for (DiagnosticType diagnostic : new DiagnosticType[] { CheckGlobalThis.GLOBAL_THIS }) {
      String message = diagnostic.getMessage();
      int lineNumber = 1;
      int columnNumber = 1;
      for (DiagnosticType.Entry entry : result.getEntries()) {
        if (entry.getDiagnosticType() == diagnostic &&
            entry.getSourceName().equals(code) &&
            entry.getLineNumber() == lineNumber &&
            entry.getColumnNumber() >= columnNumber) {
          checker.report(entry.getSourcePosition(), WARNING, diagnostic);
          columnNumber = entry.getColumnNumber();
        }
      }
    }

    assertThat(result.getNumberOfWarnings(), is(1));
  }
}