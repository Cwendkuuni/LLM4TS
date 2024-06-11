import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        Token publicToken = new SimpleToken(TokenTypes.KEYWORD, "public", "public", null, 1, 1);
        Token protectedToken = new SimpleToken(TokenTypes.KEYWORD, "protected", "protected", null, 1, 1);
        Token privateToken = new SimpleToken(TokenTypes.KEYWORD, "private", "private", null, 1, 1);

        mods.addChild(publicToken);
        mods.addChild(protectedToken);
        mods.addChild(privateToken);

        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        // test when no matching keyword is found
        mods = new DetailAST();
        mods.addChild(new SimpleToken(TokenTypes.IDENT, "MyClass", null, null, 1, 1));
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, actual);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        DetailAST parent1 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        DetailAST parent3 = new DetailAST();

        parent2.addChild(new SimpleToken(TokenTypes.MODIFIER, "public", "public", null, 1, 1));
        parent2.addChild(node);
        parent1.addChild(new SimpleToken(TokenTypes.MODIFIER, "protected", "protected", null, 1, 1));
        parent1.addChild(parent2);

        Scope expected1 = Scope.PROTECTED;
        Scope actual1 = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected1, actual1);

        expected1 = null;
        parent3.addChild(new SimpleToken(TokenTypes.MODIFIER, "private", "private", null, 1, 1));
        parent3.addChild(node);
        parent2.addChild(parent3);
        parent1.addChild(parent2);

        Scope actual2 = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected1, actual2);

        // test for anon inner
        DetailAST anonInner = new DetailAST();
        parent3.addChild(new SimpleToken(TokenTypes.KEYWORD, "class", "class", null, 1, 1));
        parent3.addChild(new SimpleToken(TokenTypes.IDENT, "MyAnonInner", "MyAnonInner", null, 1, 1));
        parent3.addChild(new SimpleToken(TokenTypes.LEFTBRACE, "{", "{", null, 1, 1));
        parent3.addChild(node);
        parent3.addChild(new SimpleToken(TokenTypes.RIGHTBRACE, "}", "}", null, 1, 1));
        parent2.addChild(parent3);
        parent1.addChild(parent2);

        Scope expected3 = Scope.ANONINNER;
        Scope actual3 = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected3, actual3);
    }

    @Test(expected = AssertionError.class)
    public void testGetSurroundingScope_ThrowsException_WhenNodeIsNull() {
        ScopeUtils.getSurroundingScope(null);
    }

    @Test(expected = AssertionError.class)
    public void testGetSurroundingScope_ThrowsException_WhenNodeHasNoParent() {
        DetailAST node = new DetailAST();
        ScopeUtils.getSurroundingScope(node);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent1 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        DetailAST parent3 = new DetailAST();

        parent3.addChild(new SimpleToken(TokenTypes.MODIFIER, "interface", "interface", null, 1, 1));
        parent3.addChild(parent2);
        parent2.addChild(new SimpleToken(TokenTypes.IDENT, "MyInterface", "MyInterface", null, 1, 1));
        parent2.addChild(node);

        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);

        parent3.addChild(new SimpleToken(TokenTypes.IDENT, "MyClass", "MyClass", null, 1, 1));
        parent2.addChild(parent3);

        actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);

        parent3.setType(TokenTypes.CLASS);
        actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(false, actual);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent1 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        DetailAST parent3 = new DetailAST();

        parent3.addChild(new SimpleToken(TokenTypes.ANNOTATION, "@MyAnnotation", "@MyAnnotation", null, 1, 1));
        parent3.addChild(parent2);
        parent2.addChild(node);

        boolean expected = true;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);

        parent3.setType(TokenTypes.CLASS);
        actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(false, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent1 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        DetailAST parent3 = new DetailAST();

        parent3.addChild(new SimpleToken(TokenTypes.MODIFIER, "interface", "interface", null, 1, 1));
        parent3.addChild(parent2);
        parent2.addChild(node);

        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);

        parent3.setType(TokenTypes.CLASS);
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(false, actual);

        parent3.addChild(new SimpleToken(TokenTypes.ANNOTATION, "@MyAnnotation", "@MyAnnotation", null, 1, 1));
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(true, actual);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent1 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        DetailAST parent3 = new DetailAST();

        parent3.addChild(new SimpleToken(TokenTypes.MODIFIER, "enum", "enum", null, 1, 1));
        parent3.addChild(parent2);
        parent2.addChild(node);

        boolean expected = true;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);

        parent3.setType(TokenTypes.CLASS);
        actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(false, actual);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent1 = new DetailAST();
        DetailAST parent2 = new DetailAST();

        parent2.addChild(new SimpleToken(TokenTypes.LEFTBRACE, "{", "{", null, 1, 1));
        parent2.addChild(node);
        parent2.addChild(new SimpleToken(TokenTypes.RIGHTBRACE, "}", "}", null, 1, 1));
        parent1.addChild(parent2);

        boolean expected = true;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);

        parent2.setType(TokenTypes.ANNOTATION);
        actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(false, actual);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        DetailAST parent1 = new DetailAST();

        parent1.addChild(new SimpleToken(TokenTypes.MODIFIER, "public", "public", null, 1, 1));
        parent1.addChild(new SimpleToken(TokenTypes.IDENT, "MyClass", "MyClass", null, 1, 1));
        parent1.addChild(node);

        boolean expected = true;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);

        DetailAST grandParent = new DetailAST();
        parent1.addChild(grandParent);
        grandParent.addChild(new SimpleToken(TokenTypes.MODIFIER, "package", "package", null, 1, 1));
        grandParent.addChild(parent1);

        actual = ScopeUtils.isOuterMostType(node);
        assertEquals(false, actual);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();

        Token idToken = new SimpleToken(TokenTypes.IDENT, "myVariable", "myVariable", null, 1, 1);
        Token varToken = new SimpleToken(TokenTypes.MODIFIER, "final", "final", null, 1, 1);
        Token assignmentToken = new SimpleToken(TokenTypes.ASSIGN, ":", ":", null, 1, 1);

        node.addChild(varToken);
        node.addChild(idToken);
        node.addChild(assignmentToken);

        boolean expected = true;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);

        node.setType(TokenTypes.VARIABLE); // note: this is not a valid DetailAST for Checkstyle but the test still need to be done
        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);

        DetailAST parent = new DetailAST();
        parent.addChild(new SimpleToken(TokenTypes.VARIABLE, "int", "int", null, 1, 1));
        parent.addChild(node);

        actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(false, actual);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();

        parent.addChild(new SimpleToken(TokenTypes.MODIFIER, "private", "private", null, 1, 1));
        parent.addChild(new SimpleToken(TokenTypes.IDENT, "myField", "myField", null, 1, 1));
        parent.addChild(node);

        boolean expected = true;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);

        parent.setType(TokenTypes.VARIABLE);
        actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(false, actual);
    }

    @Test
    public void testIsInScope() {
        DetailAST node = new DetailAST();
        DetailAST ast = new DetailAST();

        DetailAST parent1 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.addChild(new SimpleToken(TokenTypes.MODIFIER, "public", "public", null, 1, 1));
        parent2.addChild(parent1);
        parent1.addChild(node);
        parent1.addChild(ast);
        parent1.addChild(new SimpleToken(TokenTypes.MODIFIER, "protected", "protected", null, 1, 1));

        Scope expected = Scope.PUBLIC;
        boolean actual = ScopeUtils.isInScope(node, expected);
        assertTrue(actual);

        expected = Scope.PROTECTED;
        actual = ScopeUtils.isInScope(ast, expected);
        assertTrue(actual);

        expected = Scope.PRIVATE;
        actual = ScopeUtils.isInScope(node, expected);
        assertFalse(actual);

        expected = Scope.ANONINNER;
        actual = ScopeUtils.isInScope(node, expected);
        assertFalse(actual);
    }
}