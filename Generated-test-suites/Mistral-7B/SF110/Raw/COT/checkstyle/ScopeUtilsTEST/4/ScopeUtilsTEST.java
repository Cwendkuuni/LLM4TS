package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createModsAST("public protected private");
        Scope expected = Scope.PROTECTED;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_emptyMods() {
        DetailAST mods = createModsAST("");
        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_nullMods() {
        DetailAST mods = null;
        ScopeUtils.getScopeFromMods(mods);
    }

    @Test
    public void testGetScopeFromMods_singleScope() {
        DetailAST mods = createModsAST("public");
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_multipleScopesInOrder() {
        DetailAST mods = createModsAST("public protected private");
        Scope expected = Scope.PROTECTED;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_multipleScopesMixedOrder() {
        DetailAST mods = createModsAST("protected public private");
        Scope expected = Scope.PROTECTED;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_emptyToken() {
        DetailAST mods = createModsAST(" ");
        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createNode("public class Test { int a; }");
        Scope expected = Scope.CLASS;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_nullNode() {
        DetailAST node = null;
        Scope expected = null;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_nodeNotInScope() {
        DetailAST node = createNode("public class Test { int a; }");
        DetailAST wrongScopeNode = createNode("public class Test { int a; int b; }");
        Scope expected = null;
        Scope actual = ScopeUtils.getSurroundingScope(node, wrongScopeNode);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_nodeInScope() {
        DetailAST node = createNode("public class Test { int a; int b; }");
        DetailAST scopeNode = createNode("public class Test {}");
        Scope expected = Scope.CLASS;
        Scope actual = ScopeUtils.getSurroundingScope(node, scopeNode);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createNode("@interface Test { int a; }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock_nodeNotInInterfaceBlock() {
        DetailAST node = createNode("public class Test { int a; }");
        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createNode("@Test public void test() { int a; }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock_nodeNotInAnnotationBlock() {
        DetailAST node = createNode("public class Test { int a; }");
        boolean expected = false;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createNode("@interface Test { int a; }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_nodeNotInInterfaceOrAnnotationBlock() {
        DetailAST node = createNode("public class Test { int a; }");
        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createNode("enum Test { VALUE1, VALUE2 }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock_nodeNotInEnumBlock() {
        DetailAST node = createNode("public class Test { int a; }");
        boolean expected = false;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createNode("public class Test { int a; }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock_nodeNotInCodeBlock() {
        DetailAST node = createNode("@interface Test { int a; }");
        boolean expected = false;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createNode("public class Test { int a; }");
        boolean expected = true;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType_nodeNotOuterMostType() {
        DetailAST node = createNode("public class Test { int a; } class Inner { int b; }");
        boolean expected = false;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createNode("int a;");
        boolean expected = true;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef_nodeNotLocalVariableDef() {
        DetailAST node = createNode("public class Test { int a; }");
        boolean expected = false;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createNode("int a;");
        boolean expected = true;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef_nodeNotClassFieldDef() {
        DetailAST node = createNode("int a; class Test { }");
        boolean expected = false;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createNode("public class Test { int a; }");
        Scope scope = Scope.CLASS;
        boolean expected = true;
        boolean actual = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope_astNotInScope() {
        DetailAST ast = createNode("public class Test { int a; }");
        Scope scope = Scope.PACKAGE;
        boolean expected = false;
        boolean actual = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, actual);
    }

    private DetailAST createModsAST(String modifiers) {
        AST list = new ArrayList();
        for (String modifier : modifiers.split(" ")) {
            AST modifierAST = new CommonToken(new Token(Modifiers.MODIFIER, modifier, 0));
            list.add(modifierAST);
        }
        DetailAST mods = new SimpleNode(Nodes.MODIFIERS, list);
        return mods;
    }

    private DetailAST createNode(String code) {
        ANTLRInputStream input = new ANTLRInputStream(new StringReader(code));
        CheckstyleLexer lexer = new CheckstyleLexer(input);
        DefaultTokenStream tokens = new CommonTokenStream(lexer);
        ParseTree tree = new CheckstyleBaseListener().parse(tokens);
        DetailAST ast = tree.getRootNode().firstChild;
        return ast;
    }
}