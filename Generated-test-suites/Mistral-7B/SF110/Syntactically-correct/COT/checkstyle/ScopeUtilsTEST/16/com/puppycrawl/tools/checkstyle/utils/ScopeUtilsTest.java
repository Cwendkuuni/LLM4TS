import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST mods = ast.findFirstToken(5);

        Scope expected = PUBLIC;
        String modifier = mods.getText();
        if (modifier.equals("protected")) {
            expected = PROTECTED;
        } else if (modifier.equals("private")) {
            expected = PRIVATE;
        }

        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_noModifierFound() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST mods = ast.getFirstChild();

        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(PACKAGE, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_nullInput() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        Scope expected = PACKAGE;
        Scope actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = PUBLIC;
        actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = PROTECTED;
        actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = PRIVATE;
        actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = ANONINNER;
        actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        boolean expected = false;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = true;
        actual = ScopeUtils.isInInterfaceBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        boolean expected = false;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = true;
        actual = ScopeUtils.isInAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        boolean expected = false;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = true;
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        boolean actual = ScopeUtils.isInEnumBlock(node);
        boolean expected = false;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = true;
        actual = ScopeUtils.isInEnumBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        boolean actual = ScopeUtils.isInCodeBlock(node);
        boolean expected = false;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = true;
        actual = ScopeUtils.isInCodeBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        boolean actual = ScopeUtils.isOuterMostType(node);
        boolean expected = true;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = false;
        actual = ScopeUtils.isOuterMostType(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        boolean actual = ScopeUtils.isLocalVariableDef(node);
        boolean expected = false;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = true;
        actual = ScopeUtils.isLocalVariableDef(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();

        boolean actual = ScopeUtils.isClassFieldDef(node);
        boolean expected = false;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = true;
        actual = ScopeUtils.isClassFieldDef(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();
        Scope scope = PUBLIC;

        boolean actual = ScopeUtils.isInScope(node, scope);
        boolean expected = false;

        assertEquals(expected, actual);

        node = node.getNextSibling();
        actual = ScopeUtils.isInScope(node, scope);

        assertEquals(expected, actual);

        node = node.getNextSibling();
        expected = true;
        actual = ScopeUtils.isInScope(node, scope);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope_differentScopes() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();
        Scope scope = PROTECTED;

        boolean actual = ScopeUtils.isInScope(node, scope);
        boolean expected = false;

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInScope_nullInput() {
        DetailAST ast = getTestAst("/testCases/ScopeUtilsTestCases.java");
        DetailAST node = ast.findFirstChild();
        Scope scope = null;

        ScopeUtils.isInScope(node, scope);
    }

    private DetailAST getTestAst(String fileName) {
        // Initialize your ANTLR lexer and parser here
        // ...

        // Parse the file
        // ...

        return ast;
    }
}