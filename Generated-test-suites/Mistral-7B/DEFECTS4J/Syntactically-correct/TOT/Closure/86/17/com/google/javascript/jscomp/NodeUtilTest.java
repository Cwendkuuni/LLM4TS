import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    String jsCode = "for (var i in [1, 2, 3]) {}";
    SourceRoot srcRoot = new SourceRoot(new TestSourceFile(jsCode, "Test.js"));
    Node rootNode = srcRoot.getSource().getParser().parse(srcRoot.getSource().getText());
    assertTrue(isForIn(rootNode.getChild(0)));
  }

  @Test
  public void testIsForIn_NotForIn() {
    String jsCode = "for (var i = 0; i < 10; i++) {}";
    SourceRoot srcRoot = new SourceRoot(new TestSourceFile(jsCode, "Test.js"));
    Node rootNode = srcRoot.getSource().getParser().parse(srcRoot.getSource().getText());
    assertFalse(isForIn(rootNode.getChild(0)));
  }

  @Test
  public void testIsStatement() {
    String jsCode = "var x = 1;";
    SourceRoot srcRoot = new SourceRoot(new TestSourceFile(jsCode, "Test.js"));
    Node rootNode = srcRoot.getSource().getParser().parse(srcRoot.getSource().getText());
    Node statementNode = rootNode.getChild(0);
    assertTrue(isStatement(statementNode));
  }

  @Test
  public void testIsStatement_NotStatement() {
    String jsCode = "1 + 1";
    SourceRoot srcRoot = new SourceRoot(new TestSourceFile(jsCode, "Test.js"));
    Node rootNode = srcRoot.getSource().getParser().parse(srcRoot.getSource().getText());
    Node expressionNode = rootNode.getChild(0);
    assertFalse(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    String jsCode = "function foo() {}";
    SourceRoot srcRoot = new SourceRoot(new TestSourceFile(jsCode, "Test.js"));
    Node rootNode = srcRoot.getSource().getParser().parse(srcRoot.getSource().getText());
    Node functionDeclarationNode = rootNode.getChild(0);
    assertTrue(isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    String jsCode = "var x = function() {};";
    SourceRoot srcRoot = new SourceRoot(new TestSourceFile(jsCode, "Test.js"));
    Node rootNode = srcRoot.getSource().getParser().parse(srcRoot.getSource().getText());
    Node functionExpressionNode = rootNode.getChild(0);
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    String jsCode = "function foo() {}";
    SourceRoot srcRoot = new SourceRoot(new TestSourceFile(jsCode, "Test.js"));
    Node rootNode = srcRoot.getSource().getParser().parse(srcRoot.getSource().getText());
    Node functionDeclarationNode = rootNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    String jsCode = "function foo() {}";
    String jsCode2 = "function bar() {}";
    SourceRoot srcRoot = new SourceRoot(
        new TestSourceFile(jsCode, "Test1.js"),
        new TestSourceFile(jsCode2, "Test2.js")
    );
    Node rootNode = srcRoot.getSource().getParser().parse(srcRoot.getSource().getText());
    Node functionDeclarationNode = rootNode.getChild(0);
    Node functionDeclarationNode2 = rootNode.getChild(1);
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode2));
  }
}