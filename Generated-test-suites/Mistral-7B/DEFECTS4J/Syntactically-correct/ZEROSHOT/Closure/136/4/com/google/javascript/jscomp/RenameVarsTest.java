import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class RenameVarsTest {
  private final String inputCode;
  private final String expectedOutput;

  public RenameVarsTest(String inputCode, String expectedOutput) {
    this.inputCode = inputCode;
    this.expectedOutput = expectedOutput;
  }

  @Parameterized.Parameters(name = "{index}: input={0} -> output={1}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {"function foo() { var a = 1; }", "function foo() { var a = a$0; }"},
        {"function foo() { var a = 1; var b = 2; }", "function foo() { var a = a$0; var b = b$1; }"},
        {"function foo() { var a = 1; var b = 2; var c = 3; }", "function foo() { var a = a$0; var b = b$1; var c = c$2; }"},
        {"function foo() { var a = 1; var b = 2; function bar() {} }", "function foo() { var a = a$0; var b = b$1; function bar() {} }"},
        {"function foo() { var a = 1; function bar() {} var b = 2; }", "function foo() { var a = a$0; function bar() {} var b = b$1; }"},
        {"function foo() { var a = 1, b = 2; }", "function foo() { var a = a$0, b = b$1; }"},
        {"function foo() { var a = 1, b = 2, c = 3; }", "function foo() { var a = a$0, b = b$1, c = c$2; }"},
        {"function foo() { var a = 1, b = 2, function bar() {}; }", "function foo() { var a = a$0, b = b$1, function bar() {}; }"},
        {"function foo() { function bar() {} var a = 1; }", "function foo() { function bar$0() {} var a = a$1; }"},
        {"function foo() { var a = 1, function bar() {}; }", "function foo() { var a = a$0, function bar$1() {}; }"},
        {"function foo() { var a = 1, var b = 2, function bar() {}; }", "function foo() { var a = a$0, var b = b$1, function bar$2() {}; }"},
        {"function foo() { function bar(x) { } var a = 1; }", "function foo() { function bar$0(x) { } var a = a$1; }"},
        {"function foo() { function bar(x, y) { } var a = 1; }", "function foo() { function bar$0(x, y) { } var a = a$1; }"},
        {"function foo() { function bar(x, y) { } var a = 1, b = 2; }", "function foo() { function bar$0(x, y) { } var a = a$1, b = b$2; }"},
        {"function foo() { function bar(x, y) { } var a = 1, b = 2, c = 3; }", "function foo() { function bar$0(x, y) { } var a = a$1, b = b$2, c = c$3; }"},
        {"function foo() { function bar() { } var a = 1; }", "function foo() { function bar$0() { } var a = a$1; }"},
        {"function foo() { var a = 1; function bar() { var b = 2; } }", "function foo() { var a = a$0; function bar$1() { var b = b$2; } }"},
        {"function foo() { var a = 1; function bar() { var b = 2, c = 3; } }", "function foo() { var a = a$0; function bar$1() { var b = b$2, c = c$3; } }"},
        {"function foo() { var a = 1; function bar() { var b = 2, function baz() {}; } }", "function foo() { var a = a$0; function bar$1() { var b = b$2, function baz$3() {}; } }"},
        {"function foo() { var a = 1; function bar() { var b = 2, function baz() { } } }", "function foo() { var a = a$0; function bar$1() { var b = b$2, function baz$4() { } } }"},
        {"function foo() { var a = 1; function bar() { function baz() { } } }", "function foo() { var a = a$0; function bar$1() { function baz$5() { } } }"},
        {
            "function foo() {" +
            "  var a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9;" +
            "  function bar() {};" +
            "}"
            ,
            "function foo() {" +
            "  var a = a$0, b = b$1, c = c$2, d = d$3, e = e$4, f = f$5, g = g$6, h = h$7, i = i$8;" +
            "  function bar$9() {};" +
            "}"
        },
        {
            "function foo() {" +
            "  var a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9, j = 10;" +
            "  function bar() {};" +
            "}"
            ,
            "function foo() {" +
            "  var a = a$0, b = b$1, c = c$2, d = d$3, e = e$4, f = f$5, g = g$6, h = h$7, i = i$8, j = j$9;" +
            "  function bar$10() {};" +
            "}"
        },
        {
            "function foo() {" +
            "  var a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9, j = 10, k = 11;" +
            "  function bar() {};" +
            "}"
            ,
            "function foo() {" +
            "  var a = a$0, b = b$1, c = c$2, d = d$3, e = e$4, f = f$5, g = g$6, h = h$7, i = i$8, j = j$9, k = k$10;" +
            "  function bar$11() {};" +
            "}"
        },
        {
            "function foo() {" +
            "  var a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9, j = 10, k = 11, l = 12;" +
            "  function bar() {};" +
            "}"
            ,
            "function foo() {" +
            "  var a = a$0, b = b$1, c = c$2, d = d$3, e = e$4, f = f$5, g = g$6, h = h$7, i = i$8, j = j$9, k = k$10, l = l$11;" +
            "  function bar$12() {};" +
            "}"
        },
        {
            "function foo() {" +
            "  var a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9, j = 10, k = 11, l = 12, m = 13;" +
            "  function bar() {};" +
            "}"
            ,
            "function foo() {" +
            "  var a = a$0, b = b$1, c = c$2, d = d$3, e = e$4, f = f$5, g = g$6, h = h$7, i = i$8, j = j$9, k = k$10," +
            "      l = l$11, m = m$12;" +
            "  function bar$13() {};" +
            "}"
        },
        {
            "function foo() {" +
            "  var a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9, j = 10, k = 11, l = 12, m = 13, n = 14;" +
            "  function bar() {};" +
            "}"
            ,
            "function foo() {" +
            "  var a = a$0, b = b$1, c = c$2, d = d$3, e = e$4, f = f$5, g = g$6, h = h$7, i = i$8, j = j$9, k = k$10," +
            "      l = l$11, m = m$12, n = n$13;" +
            "  function bar$14() {};" +
            "}"
        },
        {
            "function foo() {" +
            "  var a = 1, b = 2, c = 3, d = 4, e = 5, f = 6, g = 7, h = 8, i = 9, j = 10, k = 11, l = 12, m = 13, n = 14, o = 15;" +
            "  function bar() {};" +
            "}"
            ,
            "function foo() {" +
            "  var a = a$0, b = b$1, c = c$2, d = d$3, e = e$4, f = f$5, g = g$6, h = h$7, i = i$8, j = j$9, k = k$10," +
            "      l = l$11, m = m$12, n = n$13, o = o$14;" +
            "  function bar$15() {};" +
            "}"
        }
    });
  }

  @Test
  public void testRenameVars() {
    AbstractCompiler compiler = new AbstractCompiler();
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    NodeAST ast = Parser.parseCompilationUnit(inputCode, null, null);
    renameVars.process(ast.getExterns(), ast.getRootNode());
    String output = Printer.printCompilationUnit(compiler, ast);
    assertEquals(expectedOutput, output);
  }
}