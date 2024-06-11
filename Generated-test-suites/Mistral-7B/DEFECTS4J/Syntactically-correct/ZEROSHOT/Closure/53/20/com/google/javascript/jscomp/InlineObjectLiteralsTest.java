import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Token;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler(null, options);

    CompilerPassManager manager = new CompilerPassManager(compiler);
    manager.addPass(new InlineObjectLiterals(compiler, () -> "tempVar"));

    String input = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() {" +
        "      return 3;" +
        "    }" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c();" +
        "  obj = null;" +
        "  return x + y + z;" +
        "}";

    Node root = NodeUtil.parseJs(input);
    manager.process(root, null);

    Node expectedOutput = new Node(Token.PROGRAM, null,
        new Node(Token.BLOCK, null,
          new Node(Token.VAR, null, new Node(Token.NAME, "x"), null),
          new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"),
              new Node(Token.NAME, "a")),
          new Node(Token.VAR, null, new Node(Token.NAME, "y"), null),
          new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"),
              new Node(Token.NAME, "b")),
          new Node(Token.VAR, null, new Node(Token.NAME, "z"), null),
          new Node(Token.ASSIGN, null, new Node(Token.NAME, "z"),
              new Node(Token.NAME, "cFunc")),
          new Node(Token.NAME, "cFunc"),
          new Node(Token.RETURN, null, new Node(Token.PLUS,
              new Node(Token.PLUS, new Node(Token.NAME, "x"),
                  new Node(Token.NAME, "y")),
              new Node(Token.NAME, "z"))),
          new Node(Token.BLOCK, null, null)
        )
    );

    Node output = compiler.getRootNode();
    assertEquals(expectedOutput, output);
  }
}