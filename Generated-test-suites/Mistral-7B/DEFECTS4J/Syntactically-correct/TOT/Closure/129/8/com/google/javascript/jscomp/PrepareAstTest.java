import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.SourceFile;

public class PrepareAstTest {

  @Test
  public void testPrepareAstWithSimpleObjectLit() {
    String code = "{\n" +
        "  a: 1,\n" +
        "  b: function() {}\n" +
        "}";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = new PrepareAst(null, false).process(source, null);

    Node objlit = root.getFirstChild();
    assertEquals(2, objlit.getNumChildren());

    Node aNode = objlit.getChildAt(0);
    assertEquals("a", aNode.getString());
    assertEquals(Token.NUMBER, aNode.getType());
    assertNull(aNode.getJSDocInfo());

    Node funcNode = objlit.getChildAt(1);
    assertEquals("b", funcNode.getString());
    assertEquals(Node.FUNCTION, funcNode.getType());
    assertNull(funcNode.getJSDocInfo());
  }

  @Test
  public void testPrepareAstWithEmptyObjectLit() {
    String code = "{}";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = new PrepareAst(null, false).process(source, null);

    Node objlit = root.getFirstChild();
    assertEquals(0, objlit.getNumChildren());
  }

  @Test
  public void testPrepareAstWithControlStructureInObjectLit() {
    String code = "{\n" +
        "  if (true) {\n" +
        "    a: 1,\n" +
        "  }\n" +
        "}";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = new PrepareAst(null, false).process(source, null);

    Node objlit = root.getFirstChild();
    assertEquals(1, objlit.getNumChildren());

    Node ifNode = objlit.getFirstChild();
    assertEquals(Node.IF, ifNode.getType());
    assertFalse(ifNode.isBlock());
    assertTrue(ifNode.isControlStructure());
    assertNull(ifNode.getJSDocInfo());

    Node aNode = ifNode.getNextSibling();
    assertEquals("a", aNode.getString());
    assertEquals(Token.IDENTIFIER, aNode.getType());
    assertNull(aNode.getJSDocInfo());
  }

  @Test
  public void testPrepareAstWithControlStructureInFunction() {
    String code = "function test() {\n" +
        "  if (true) {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}";

    SourceFile source = new SourceFile("Test.js", code);
    Node root = new PrepareAst(null, false).process(source, null);

    Node funcNode = root.getFirstChild();
    assertEquals("test", funcNode.getString());
    assertEquals(Node.FUNCTION, funcNode.getType());
    assertFalse(funcNode.isBlock());
    assertTrue(funcNode.isControlStructure());
    assertNull(funcNode.getJSDocInfo());

    Node ifNode = funcNode.getChildAt(0);
    assertEquals(Node.IF, ifNode.getType());
    assertFalse(ifNode.isBlock());
    assertTrue(ifNode.isControlStructure());
    assertNull(ifNode.getJSDocInfo());

    Node returnNode = ifNode.getLastChild();
    assertEquals(Token.RETURN, returnNode.getType());
    assertEquals(1, returnNode.getFirstChild().getInt());
  }

  @Test(expected = AssertionError.class)
  public void testPrepareAstWithInvalidNodeType() {
    String code = "function test() {\n" +
        "  a: 1;\n" +
        "}";

    SourceFile source = new SourceFile("Test.js", code);
    new PrepareAst(null, false).process(source, null);
  }
}