import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.SourceFile;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testSafeUsesOfThis() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker() {
        return new Checker() {
          @Override
          public void check(Node root, Node current) {}
        };
      }
    };

    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, check);

    SourceFile source = new SourceFile("Test.js");
    String code = "function MyFunction() {" +
                  "  this.myProperty = 'safe';" +
                  "  this.myMethod = function() {" +
                  "    this.myProperty = 'changed';" +
                  "  }" +
                  "}" +
                  "MyFunction.prototype.myPrototypeMethod = function() {" +
                  "  this.myProperty = 'changed via prototype';" +
                  "}";
    Node root = Parser.parse(code, source);

    traversal.traverse(root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testUnsafeUsesOfThis() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker() {
        return new Checker() {
          @Override
          public void check(Node root, Node current) {}
        };
      }
    };

    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, check);

    SourceFile source = new SourceFile("Test.js");
    String code = "function MyFunction() {" +
                  "  this.myProperty = 'unsafe';" +
                  "}" +
                  "MyFunction();";
    Node root = Parser.parse(code, source);

    traversal.traverse(root);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testFunctionWithNoThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker() {
        return new Checker() {
          @Override
          public void check(Node root, Node current) {}
        };
      }
    };

    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, check);

    SourceFile source = new SourceFile("Test.js");
    String code = "function MyFunction() {" +
                  "  this.myProperty = 'unsafe';" +
                  "}" +
                  "MyFunction.prototype.myPrototypeMethod = function() {" +
                  "  this.myProperty = 'changed via prototype';" +
                  "}";
    Node root = Parser.parse(code, source);

    traversal.traverse(root);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testFunctionWithThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker() {
        return new Checker() {
          @Override
          public void check(Node root, Node current) {}
        };
      }
    };

    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, check);

    SourceFile source = new SourceFile("Test.js");
    String code = "/** @this {MyClass} */\n" +
                  "function MyFunction(myClass) {" +
                  "  this.myProperty = 'safe';" +
                  "}" +
                  "MyFunction.prototype.myPrototypeMethod = function() {" +
                  "  this.myProperty = 'changed via prototype';" +
                  "}" +
                  "function MyClass() {}";
    Node root = Parser.parse(code, source);

    traversal.traverse(root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testConstructorFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Checker getChecker() {
        return new Checker() {
          @Override
          public void check(Node root, Node current) {}
        };
      }
    };

    CompilerOptions options = new CompilerOptions();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, check);

    SourceFile source = new SourceFile("Test.js");
    String code = "function MyClass(myProperty) {" +
                  "  this.myProperty = myProperty;" +
                  "}" +
                  "MyClass.prototype.myPrototypeMethod = function() {" +
                  "  this.myProperty = 'changed via prototype';" +
                  "}" +
                  "MyClass.prototype.constructor = MyClass;";
    Node root = Parser.parse(code, source);

    traversal.traverse(root);
    assertEquals(0, compiler.getDiagnostics().size());
  }
}