import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node parseSyntheticCode(String name, String code) {
        return null;
      }
    };

    functionRewriter = new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testEmptyFunctionReduction() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node emptyFunction = new Node(Token.FUNCTION_DECL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "emptyFunction"),
        new Node(Token.BLOCK, 1, 1),
        null);
    root.addChildToBack(emptyFunction);

    Node expectedResult = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, 1, 1, "JSCompiler_emptyFn"));

    functionRewriter.process(null, root);

    Node actualResult = NodeUtil.getFirstChild(emptyFunction);
    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testIdentityFunctionReduction() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node identityFunction = new Node(Token.FUNCTION_DECL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "identityFunction"),
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.NAME, 1, 1, "a")));
    root.addChildToBack(identityFunction);

    Node expectedResult = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, 1, 1, "JSCompiler_identityFn"));

    functionRewriter.process(null, root);

    Node actualResult = NodeUtil.getFirstChild(identityFunction);
    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testReturnConstantReduction() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node constantFunction = new Node(Token.FUNCTION_DECL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "constantFunction"),
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.RETURN, 1, 1, new Node(Token.NUMBER, 1, 1, 10)));
    root.addChildToBack(constantFunction);

    Node expectedResult = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, 1, 1, "JSCompiler_returnArg"),
        new Node(Token.NUMBER, 1, 1, 10));

    functionRewriter.process(null, root);

    Node actualResult = NodeUtil.getFirstChild(constantFunction);
    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testGetterReduction() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node getterFunction = new Node(Token.FUNCTION_DECL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "getterFunction"),
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.RETURN, 1, 1,
            new Node(Token.GETPROP, 1, 1,
                new Node(Token.THIS, 1, 1),
                new Node(Token.NAME, 1, 1, "a"))));
    root.addChildToBack(getterFunction);

    Node expectedResult = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, 1, 1, "JSCompiler_get"),
        new Node(Token.STRING, 1, 1, "a"));

    functionRewriter.process(null, root);

    Node actualResult = NodeUtil.getFirstChild(getterFunction);
    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testSetterReduction() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node setterFunction = new Node(Token.FUNCTION_DECL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "setterFunction"),
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.RETURN, 1, 1, null),
        new Node(Token.VAR, 1, 1, null, null, new Node(Token.NAME, 1, 1, "a")));
    setterFunction = setterFunction.getFirstChild(); // remove return statement

    Node assignment = new Node(Token.ASSIGN, 1, 1,
        new Node(Token.GETPROP, 1, 1, new Node(Token.THIS, 1, 1), new Node(Token.NAME, 1, 1, "a")),
        new Node(Token.NAME, 1, 1, "value"));
    Node argumentList = new Node(Token.LIST, 1, 1);
    argumentList.addChildToBack(new Node(Token.NAME, 1, 1, "value"));
    Node callExpression = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, 1, 1, "JSCompiler_set"),
        new Node(Token.STRING, 1, 1, "a"));
    Node statement = new Node(Token.EXPRESSION_STATEMENT, 1, 1, callExpression);
    Node body = new Node(Token.BLOCK, 1, 1, null, statement);

    setterFunction.setLastChild(body);
    root.addChildToBack(setterFunction);

    Node expectedResult = new Node(Token.CALL, 1, 1,
        new Node(Token.NAME, 1, 1, "JSCompiler_set"),
        new Node(Token.STRING, 1, 1, "a"));

    functionRewriter.process(null, root);

    Node actualResult = NodeUtil.getFirstChild(setterFunction);
    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testNoReduction() {
    Node root = new Node(Token.BLOCK, 1, 1);
    Node functionDeclaration = new Node(Token.FUNCTION_DECL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "functionDeclaration"),
        new Node(Token.BLOCK, 1, 1),
        null);
    root.addChildToBack(functionDeclaration);

    functionRewriter.process(null, root);

    Node actualResult = NodeUtil.getFirstChild(functionDeclaration);
    assertNotNull(actualResult);
  }
}