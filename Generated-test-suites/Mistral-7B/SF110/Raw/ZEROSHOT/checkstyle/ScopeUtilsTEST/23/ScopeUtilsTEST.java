import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;
import org.junit.*;

public class ScopeUtilsTest
{
    @Test
    public void testGetScopeFromMods()
    {
        DetailAST mods = new DefaultASTFactory().createList(
            new DefaultASTFactory().createToken(TokenTypes.MODIFIERS, "public protected private"),
            null);
        Scope expected = Scope.PROTECTED; // Assuming the first non-private modifier is returned
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = new DefaultASTFactory().createList(
                new DefaultASTFactory().createToken(TokenTypes.MODIFIERS, "private"),
                null);
        expected = Scope.PRIVATE;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = new DefaultASTFactory().createList(
                new DefaultASTFactory().createToken(TokenTypes.MODIFIERS, "public"),
                null);
        expected = Scope.PUBLIC;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = new DefaultASTFactory().createList(
                new DefaultASTFactory().createToken(TokenTypes.MODIFIERS, ""),
                null);
        expected = Scope.PACKAGE;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = new DefaultASTFactory().createList(
                new DefaultASTFactory().createToken(TokenTypes.MODIFIERS, "protected public"),
                null);
        expected = Scope.PROTECTED; // Assuming protected takes precedence over public
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.CLASS_DEF, "class X { }");
        Scope expected = Scope.CLASS;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = new DefaultASTFactory().createToken(TokenTypes.INTERFACE_DEF, "interface X { }");
        expected = Scope.INTERFACE;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = new DefaultASTFactory().createToken(TokenTypes.ANNOTATION_DEF, "@interface X { }");
        expected = Scope.ANNOTATION;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = new DefaultASTFactory().createToken(TokenTypes.ENUM_DEF, "enum X { X }");
        expected = Scope.ENUM;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = new DefaultASTFactory().createToken(TokenTypes.ANON_CLASS_DEF, "class X { }");
        expected = Scope.ANONINNER;
        actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);

        node = new DefaultASTFactory().createToken(TokenTypes.METHOD_DEF, "void foo() { }");
        DetailAST parent = node.getParent();
        expected = Scope.METHOD;
        while (parent != null)
        {
            if (parent.getType() == TokenTypes.CLASS_DEF || parent.getType() == TokenTypes.INTERFACE_DEF)
            {
                actual = ScopeUtils.getScopeFromMods(parent.findFirstToken(5));
                break;
            }
            parent = parent.getParent();
        }
        actual = actual == null ? Scope.PACKAGE : actual;
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.METHOD_DEF, "void foo() { }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = new DefaultASTFactory().createToken(TokenTypes.INTERFACE_DEF, "interface X { }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.METHOD_DEF, "void foo() { }");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = new DefaultASTFactory().createToken(TokenTypes.ANNOTATION_DEF, "@interface X { }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.METHOD_DEF, "void foo() { }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = new DefaultASTFactory().createToken(TokenTypes.INTERFACE_DEF, "interface X { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = new DefaultASTFactory().createToken(TokenTypes.ANNOTATION_DEF, "@interface X { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.METHOD_DEF, "void foo() { }");
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = new DefaultASTFactory().createToken(TokenTypes.ENUM_DEF, "enum X { X }");
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.ANNOTATION_DEF, "@interface X { }");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = new DefaultASTFactory().createToken(TokenTypes.METHOD_DEF, "void foo() { }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = new DefaultASTFactory().createToken(TokenTypes.CLASS_DEF, "class X { }");
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.METHOD_DEF, "void foo() { }");
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = new DefaultASTFactory().createToken(TokenTypes.CLASS_DEF, "class X { }");
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.VAR_DEF, "int x;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = new DefaultASTFactory().createToken(TokenTypes.FIELD_DEF, "int x;");
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.FIELD_DEF, "int x;");
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = new DefaultASTFactory().createToken(TokenTypes.VAR_DEF, "int x;");
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = new DefaultASTFactory().createToken(TokenTypes.FIELD_DEF, "int x;");
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope()
    {
        DetailAST node = new DefaultASTFactory().createToken(TokenTypes.METHOD_DEF, "void foo() { }");
        DetailAST enclosingClass = node.getParent();
        Scope enclosingScope = null;
        while (enclosingClass != null)
        {
            if (enclosingClass.getType() == TokenTypes.CLASS_DEF || enclosingClass.getType() == TokenTypes.INTERFACE_DEF)
            {
                enclosingScope = ScopeUtils.getScopeFromMods(enclosingClass.findFirstToken(5));
                break;
            }
            enclosingClass = enclosingClass.getParent();
        }
        Scope expected = enclosingScope;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertTrue(expected.isIn(actual));

        node = new DefaultASTFactory().createToken(TokenTypes.FIELD_DEF, "int x;");
        DetailAST enclosingClass2 = node.getParent();
        expected = Class.CLASS;
        while (enclosingClass2 != null)
        {
            if (enclosingClass2.getType() == TokenTypes.CLASS_DEF)
            {
                actual = ScopeUtils.getScopeFromMods(enclosingClass2.findFirstToken(5));
                break;
            }
            enclosingClass2 = enclosingClass2.getParent();
        }
        assertFalse(expected.isIn(actual));
    }
}