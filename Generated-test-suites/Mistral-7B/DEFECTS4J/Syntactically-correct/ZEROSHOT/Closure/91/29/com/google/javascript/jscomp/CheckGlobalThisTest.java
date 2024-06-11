import static org.junit.Assert.assertThat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.NodeToken.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(true, null);
    compiler = new AbstractCompiler() {
      @Override
      public CompilerOptions getOptions() {
        return options;
      }
    };
    checker = new Checker(compiler);
  }

  @Test
  public void testCheckGlobalThis() {
    String code = "function evil() { this.useful = undefined; }";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(1));
    DiagnosticType diagnosticType =
        diagnostics.getDiagnostics().get(0).getDiagnosticType();
    assertThat(diagnosticType.getKey(), equalTo(CheckGlobalThis.GLOBAL_THIS.getKey()));
    Node node = diagnostics.getDiagnostics().get(0).getNodes()[0];
    assertThat(node.getTokenPosition().getLineNumber(), equalTo(1));
    assertThat(node.getTokenPosition().getColumnNumber(), equalTo(17));
    assertThat(node.getType(), equalTo(THIS));
  }

  @Test
  public void testCheckGlobalThisWithPrototypePropertyAccess() {
    String code = "function evil() { Object.prototype.useful = undefined; }";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisInsidePrototypeMethod() {
    String code = "function MyObject() {} MyObject.prototype.evil = function() { this.useful = undefined; }";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisInsideFunctionWithThisAnnotation() {
    String code = "function MyFunction(@this) {}";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisInsideFunctionWithNoThisAnnotation() {
    String code = "function MyFunction() {}";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisWithLhsChild() {
    String code = "function evil(a) { this.a = a; }";
    Node ast = Parser.parseJs(code, null, null).getTree();

    NodeTraversal nodeTraversal = new NodeTraversal(ast, checker, null);
    new CheckGlobalThis(compiler, WARNING).traverse(nodeTraversal, ast, null);

    Diagnostics diagnostics = nodeTraversal.getDiagnostics();

    assertThat(diagnostics.getDiagnostics().size(), equalTo(1));
    DiagnosticType diagnosticType =
        diagnostics.getDiagnostics().get(0).getDiagnosticType();
    assertThat(diagnosticType.getKey(), equalTo(CheckGlobalThis.GLOBAL_THIS.getKey()));
    Node node = diagnostics.getDiagnostics().get(0).getNodes()[0];
    assertThat(node.getTokenPosition().getLineNumber(), equalTo(1));
    assertThat(node.getTokenPosition().getColumnNumber(), equalTo(14));
    assertThat(node.getType(), equalTo(THIS));
  }

  @Test
  public void testCheckGlobalThisAssignmentToVariable() {
    String code = "function evil() { var a = this; }";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisAssignmentToObject() {
    String code = "function evil() { var obj = {}; obj.this = this; }";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisFunctionWithPrototypeMethod() {
    String code = "function MyObject() { this.method = function() {}; }";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisFunctionWithConstructorAnnotation() {
    String code = "function MyClass() {}";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisFunctionWithThisAnnotation() {
    String code = "function MyFunction(@this) {}";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisFunctionWithNoAnnotation() {
    String code = "function MyFunction() {}";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    NodeTraversal nodeTraversal = new NodeTraversal(ast, checker, null);
    new CheckGlobalThis(compiler, WARNING).traverse(nodeTraversal, ast, null);

    assertThat(diagnostics.getDiagnostics().size(), equalTo(1));
    DiagnosticType diagnosticType =
        diagnostics.getDiagnostics().get(0).getDiagnosticType();
    assertThat(diagnosticType.getKey(), equalTo(CheckGlobalThis.GLOBAL_THIS.getKey()));
    Node node = diagnostics.getDiagnostics().get(0).getNodes()[0];
    assertThat(node.getTokenPosition().getLineNumber(), equalTo(1));
    assertThat(node.getTokenPosition().getColumnNumber(), equalTo(1));
    assertThat(node.getType(), equalTo(FUNCTION));
  }

  @Test
  public void testCheckGlobalThisFunctionWithPrototype() {
    String code = "function MyObject() {} MyObject.prototype.method = function() {};";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    NodeTraversal nodeTraversal = new NodeTraversal(ast, checker, null);
    new CheckGlobalThis(compiler, WARNING).traverse(nodeTraversal, ast, null);

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisFunctionWithPrototypeAndThisAnnotation() {
    String code = "function MyObject() {} MyObject.prototype.method = function(@this) {};";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    NodeTraversal nodeTraversal = new NodeTraversal(ast, checker, null);
    new CheckGlobalThis(compiler, WARNING).traverse(nodeTraversal, ast, null);

    assertThat(diagnostics.getDiagnostics().size(), equalTo(0));
  }

  @Test
  public void testCheckGlobalThisFunctionWithPrototypeAndNoAnnotation() {
    String code = "function MyObject() {} MyObject.prototype.method = function() {};";
    Node ast = Parser.parseJs(code, null, null).getTree();

    Diagnostics diagnostics = checker.check(ast, new CompilerOptions() {
      @Override
      public CheckLevel getOption(String key) {
        if (CheckGlobalThis.GLOBAL_THIS.getKey().equals(key)) {
          return WARNING;
        }
        return null;
      }
    });

    NodeTraversal nodeTraversal = new NodeTraversal(ast, checker, null);
    new CheckGlobalThis(compiler, WARNING).traverse(nodeTraversal, ast, null);

    Node methodNode = findMethodNode(ast, "method");
    assertThat(methodNode.getJSDocInfo().isConstructor(), is(false));
    assertThat(methodNode.getJSDocInfo().isInterface(), is(false));
    assertThat(methodNode.getJSDocInfo().hasThisType(), is(false));
    assertThat(methodNode.getJSDocInfo().isOverride(), is(false));
    assertThat(diagnostics.getDiagnostics().size(), equalTo(1));
    DiagnosticType diagnosticType =
        diagnostics.getDiagnostics().get(0).getDiagnosticType();
    assertThat(diagnosticType.getKey(), equalTo(CheckGlobalThis.GLOBAL_THIS.getKey()));
    Node node = diagnostics.getDiagnostics().get(0).getNodes()[0];
    assertThat(node.getTokenPosition().getLineNumber(), equalTo(2));
    assertThat(node.getTokenPosition().getColumnNumber(), equalTo(12));
    assertThat(node.getType(), equalTo(FUNCTION));
  }

  private Node findMethodNode(Node node, String name) {
    if (node.getType() == FUNCTION && name.equals(node.getFirstChild().getString())) {
      return node;
    }
    for (Node child : node.getChildren()) {
      Node result = findMethodNode(child, name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}