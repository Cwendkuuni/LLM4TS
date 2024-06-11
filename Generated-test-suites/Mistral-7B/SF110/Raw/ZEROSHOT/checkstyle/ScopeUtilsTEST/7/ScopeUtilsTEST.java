package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = createAst("public class Test { protected int a; private void b(); }");
        Scope expected = Scope.PROTECTED;
        Scope actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public interface Test { protected int a; }");
        expected = Scope.PROTECTED;
        actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { private int a; }");
        expected = Scope.PRIVATE;
        actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { }");
        expected = Scope.PACKAGE;
        actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { public int a; }");
        expected = Scope.PUBLIC;
        actual = ScopeUtils.getScopeFromMods(ast.findFirstToken(0));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = createAst("public class Test { public void test() { int a; } }");
        Scope expected = Scope.LOCAL;
        Scope actual = ScopeUtils.getSurroundingScope(ast.findFirstToken(1));
        assertEquals(expected, actual);

        ast = createAst("public interface Test { int a = 1; }");
        expected = Scope.INTERFACE;
        actual = ScopeUtils.getSurroundingScope(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { public static int a = 1; }");
        expected = Scope.CLASS;
        actual = ScopeUtils.getSurroundingScope(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { public static void main(String[] args) { int a = 1; } }");
        expected = Scope.METHOD;
        actual = ScopeUtils.getSurroundingScope(ast.findFirstToken(2));
        assertEquals(expected, actual);

        ast = createAst("public class Test { public static void main(String[] args) { class Inner { int a = 1; } } }");
        expected = Scope.ANONINNER;
        actual = ScopeUtils.getSurroundingScope(ast.findFirstToken(3));
        assertEquals(expected, actual);

        ast = createAst("public interface Test { public int a(); }");
        expected = Scope.INTERFACE;
        actual = ScopeUtils.getSurroundingScope(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("@interface Test { int a(); }");
        expected = Scope.ANNOTATION;
        actual = ScopeUtils.getSurroundingScope(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public enum Test { Test, VALUE }");
        expected = Scope.ENUM;
        actual = ScopeUtils.getSurroundingScope(ast.findFirstToken(0));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = createAst("public class Test { int a; interface Interface { int b; } }");
        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceBlock(ast.findFirstToken(1));
        assertEquals(expected, actual);

        ast = createAst("public interface Test { int a; }");
        expected = true;
        actual = ScopeUtils.isInInterfaceBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = createAst("public @interface Test { int a(); }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInAnnotationBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { @Test int a; }");
        expected = false;
        actual = ScopeUtils.isInAnnotationBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = createAst("public @interface Test { int a(); }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { int a; interface Interface { int b; } }");
        expected = false;
        actual = ScopeUtils.isInInterfaceOrAnnotationBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = createAst("public enum Test { Test, VALUE }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInEnumBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { int a; enum Inner { int b; } }");
        expected = false;
        actual = ScopeUtils.isInEnumBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = createAst("public class Test { int a; }");
        boolean expected = true;
        boolean actual = ScopeUtils.isInCodeBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public interface Test { int a; }");
        expected = false;
        actual = ScopeUtils.isInCodeBlock(ast.findFirstToken(0));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = createAst("public class Test { int a; }");
        boolean expected = true;
        boolean actual = ScopeUtils.isOuterMostType(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public class Test { public class Inner { int a; } }");
        expected = false;
        actual = ScopeUtils.isOuterMostType(ast.findFirstToken(1));
        assertEquals(expected, actual);

        ast = createAst("public interface Test { int a; interface Inner { int b; } }");
        expected = false;
        actual = ScopeUtils.isOuterMostType(ast.findFirstToken(1));
        assertEquals(expected, actual);

        ast = createAst("@interface Test { int a(); }");
        expected = false;
        actual = ScopeUtils.isOuterMostType(ast.findFirstToken(0));
        assertEquals(expected, actual);

        ast = createAst("public enum Test { Test, VALUE }");
        expected = false;
        actual = ScopeUtils.isOuterMostType(ast.findFirstToken(0));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = createAst("public class Test { int a; void test() { int b; } }");
        boolean expected = true;
        boolean actual = ScopeUtils.isLocalVariableDef(ast.findFirstToken(3));
        assertEquals(expected, actual);

        ast = createAst("public class Test { int a; void test() { int a; } }");
        expected = false;
        actual = ScopeUtils.isLocalVariableDef(ast.findFirstToken(3));
        assertEquals(expected, actual);

        ast = createAst("public class Test { int a; void test() { class Inner { int a; } } }");
        expected = false;
        actual = ScopeUtils.isLocalVariableDef(ast.findFirstToken(4));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = createAst("public class Test { int a; }");
        boolean expected = true;
        boolean actual = ScopeUtils.isClassFieldDef(ast.findFirstToken(1));
        assertEquals(expected, actual);

        ast = createAst("public class Test { int a; void test() { int a; } }");
        expected = false;
        actual = ScopeUtils.isClassFieldDef(ast.findFirstToken(1));
        assertEquals(expected, actual);

        ast = createAst("public class Test { class Inner { int a; } }");
        expected = false;
        actual = ScopeUtils.isClassFieldDef(ast.findFirstToken(2));
        assertEquals(expected, actual);
    }

    private DetailAST createAst(String code) {
        JavaRecognizer recognizer = new JavaRecognizer();
        Javalexer lexer = new Javalexer();
        CharStream charStream = new ANTLRInputStream(System.in);
        TokenStream tokens = new CommonTokenStream(lexer, charStream);
        recognizer.setTokenStream(tokens);
        Ast ast = recognizer.parse(JavaParser.class);
        CharStream astStream = new ANTLRFileStream(code);
        CommonTokenStream tokenStream = new CommonTokenStream(new Javalexer(), astStream);
        recognizer.setTokenStream(tokenStream);
        JavaParser parser = new JavaParser(tokenStream, recognizer);
        DetailAST parsedAst = parser.compilationUnit();
        return parsedAst;
    }

}