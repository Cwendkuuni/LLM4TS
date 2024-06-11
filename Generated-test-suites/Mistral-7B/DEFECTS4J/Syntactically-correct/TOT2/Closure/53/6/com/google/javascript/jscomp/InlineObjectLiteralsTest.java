import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given: A simple object literal assignment
    Node externs = Node.objectLiteral();
    Node root = Node.script(
        "var obj = {" +
        "  a: 1," +
        "  b: {'c': 2}" +
        "};");

    InlineObjectLiterals pass = new InlineObjectLiterals(
        new AbstractCompiler(externs), () -> "test_var_");
    pass.process(externs, root);

    // When: The InlineObjectLiterals pass is applied
    Node result = pass.compiler.getCompiledResult();

    // Then: The object literal should be inlined and variables assigned
    Node obj = result.getFirstChild();
    Node a = obj.getChildAtIndex(0);
    Node b = obj.getChildAtIndex(1);
    Node c = b.getChildAtIndex(0);
    assertEquals(Token.VAR, a.getType());
    assertEquals(Token.INTEGER, a.getFirstChild().getType());
    assertEquals(1, a.getFirstChild().getInt());

    assertEquals(Token.VAR, b.getType());
    assertEquals(Token.OBJECTLIT, b.getFirstChild().getType());
    Node cVar = b.getChildAtIndex(1);
    Node cVal = c.getFirstChild();
    assertEquals(Token.VAR, cVar.getType());
    assertEquals(Token.INTEGER, cVal.getType());
    assertEquals(2, cVal.getInt());

    // Given: An object literal with a non-simple value
    root = Node.script(
        "var obj = {" +
        "  a: {'b': 1}" +
        "};");

    // When: The InlineObjectLiterals pass is applied
    pass.process(externs, root);

    // Then: The object literal should be inlined and variables assigned
    result = pass.compiler.getCompiledResult();
    obj = result.getFirstChild();
    a = obj.getChildAtIndex(0);
    b = a.getChildAtIndex(0);
    assertEquals(Token.VAR, a.getType());
    assertEquals(Token.OBJECTLIT, a.getFirstChild().getType());
    assertEquals(Token.VAR, b.getType());
    assertEquals(Token.OBJECTLIT, b.getFirstChild().getType());
    Node bVal = b.getChildAtIndex(0);
    assertEquals(Token.INTEGER, bVal.getType());
    assertEquals(1, bVal.getInt());

    // Given: An object literal with a self-referential property
    root = Node.script(
        "var obj = {" +
        "  a: {" +
        "    b: obj" +
        "  }" +
        "};");

    // When: The InlineObjectLiterals pass is applied
    pass.process(externs, root);

    // Then: The object literal should not be inlined due to self-reference
    result = pass.compiler.getCompiledResult();
    obj = result.getFirstChild();
    a = obj.getChildAtIndex(0);
    b = a.getChildAtIndex(0);
    assertEquals(Token.VAR, a.getType());
    assertEquals(Token.OBJECTLIT, a.getFirstChild().getType());
    assertEquals(Token.GETPROP, b.getType());
    assertEquals("obj", b.getLastChild().getString());

    // Given: An object literal with a cyclic reference
    root = Node.script(
        "var obj1 = {" +
        "  a: {" +
        "    b: obj2" +
        "  }" +
        "};" +
        "var obj2 = {" +
        "  a: obj1" +
        "};");

    // When: The InlineObjectLiterals pass is applied
    pass.process(externs, root);

    // Then: The object literals should not be inlined due to cyclic reference
    result = pass.compiler.getCompiledResult();
    obj1 = result.getFirstChild();
    a1 = obj1.getChildAtIndex(0);
    b1 = a1.getChildAtIndex(0);
    obj2 = obj1.getLastChild();
    a2 = obj2.getChildAtIndex(0);
    assertEquals(Token.VAR, a1.getType());
    assertEquals(Token.OBJECTLIT, a1.getFirstChild().getType());
    assertEquals(Token.VAR, b1.getType());
    assertEquals(Token.GETPROP, b1.getParent().getType());
    assertEquals("obj2", b1.getLastChild().getString());
    assertEquals(Token.VAR, a2.getType());
    assertEquals(Token.OBJECTLIT, a2.getFirstChild().getType());
    assertEquals(Token.VAR, obj2.getChildAtIndex(0).getType());
  }

  @Test
  public void testInlineObjectLiteralsWithExports() {
    // Given: An object literal with exports
    Node externs = Node.objectLiteral();
    Node root = Node.script(
        "/** @export */ var obj = {" +
        "  a: 1," +
        "  b: {'c': 2}" +
        "};");

    InlineObjectLiterals pass = new InlineObjectLiterals(
        new AbstractCompiler(externs), () -> "test_var_");
    pass.process(externs, root);

    // Then: The object literal should not be inlined as it is exported
    Node result = pass.compiler.getCompiledResult();
    Node obj = result.getFirstChild();
    assertEquals(Token.VAR, obj.getType());
    assertEquals(Token.OBJECTLIT, obj.getFirstChild().getType());
    Node a = obj.getChildAtIndex(0);
    Node b = obj.getChildAtIndex(1);
    assertEquals(Token.VAR, a.getType());
    assertEquals(Token.INTEGER, a.getFirstChild().getType());
    assertEquals(1, a.getFirstChild().getInt());

    assertEquals(Token.VAR, b.getType());
    assertEquals(Token.OBJECTLIT, b.getFirstChild().getType());
    Node c = b.getChildAtIndex(0);
    assertEquals(Token.VAR, c.getType());
    assertEquals(Token.INTEGER, c.getFirstChild().getType());
    assertEquals(2, c.getFirstChild().getInt());

    // Given: An object literal with a property marked as exported
    root = Node.script(
        "/** @export */ var obj = {" +
        "  a: 1," +
        "  b: {'c': 2}" +
        "};" +
        "/** @export */ var c = obj.b.c;");

    // When: The InlineObjectLiterals pass is applied
    pass.process(externs, root);

    // Then: The object literal should be inlined but the exported property should not
    result = pass.compiler.getCompiledResult();
    obj = result.getFirstChild();
    a = obj.getChildAtIndex(0);
    b = obj.getChildAtIndex(1);
    c = NodeUtil.newGetProp(Node.newString(Token.NAME, "b"), b);
    Node cVar = NodeUtil.newVarNode("c", c.getAssignedValue());
    Node exportAssign = NodeUtil.newAssign(cVar, c.getLastChild());
    assertEquals(Token.VAR, a.getType());
    assertEquals(Token.INTEGER, a.getFirstChild().getType());
    assertEquals(1, a.getFirstChild().getInt());

    assertEquals(Token.VAR, b.getType());
    assertEquals(Token.OBJECTLIT, b.getFirstChild().getType());
    Node bObj = b.getChildAtIndex(0);
    Node bC = bObj.getChildAtIndex(0);
    assertEquals(Token.VAR, bC.getType());
    assertEquals(Token.INTEGER, bC.getFirstChild().getType());
    assertEquals(2, bC.getFirstChild().getInt());

    assertEquals(Token.VAR, cVar.getType());
    assertEquals(Token.VAR, exportAssign.getParent().getType());
    assertEquals(Token.ASSIGN, exportAssign.getType());
  }
}