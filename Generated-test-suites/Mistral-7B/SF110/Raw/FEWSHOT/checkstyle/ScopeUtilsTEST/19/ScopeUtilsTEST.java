package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        String code = "public class Foo {\n" +
                "    public int bar;\n" +
                "    private int baz;\n" +
                "    protected int qux;\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST mods = astRoot.findFirstToken(ASTNodeType.MODIFIERS);

        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(mods));
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(astRoot.findChild(2).findFirstToken(ASTNodeType.MODIFIERS)));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(astRoot.findChild(1).findFirstToken(ASTNodeType.MODIFIERS)));
    }

    @Test
    public void testGetSurroundingScope_Package() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(astRoot));
    }

    @Test
    public void testGetSurroundingScope_Class() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "}\n" +
                "public class Bar {\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST clsFoo = astRoot.findChild(0);
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(clsFoo));
    }

    @Test
    public void testGetSurroundingScope_Method() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "    public void bar() {\n" +
                "    }\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST mthBar = astRoot.findChild(0).findChild(1);
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(mthBar));
    }

    @Test
    public void testGetSurroundingScope_Variable() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "    int bar;\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST varBar = astRoot.findChild(0).findChild(1).findChild(0);
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(varBar));
    }

    @Test
    public void testGetSurroundingScope_AnonymousClass() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "    void doSomething() {\n" +
                "        new Runnable() {\n" +
                "            @Override\n" +
                "            public void run() {\n" +
                "            }\n" +
                "        };\n" +
                "    }\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST anonClass = astRoot.findChild(2).findChild(0);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(anonClass));
    }

    @Test
    public void testIsInInterfaceBlock() {
        String code = "package com.example;\n" +
                "interface Foo {\n" +
                "    void bar();\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST intfFoo = astRoot.findChild(0);
        assertTrue(ScopeUtils.isInInterfaceBlock(intfFoo));
    }

    @Test
    public void testIsInInterfaceBlock_NotAnInterface() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "}\n" +
                "public interface Bar {\n" +
                "}\n";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST clsFoo = astRoot.findChild(0);
        assertFalse(ScopeUtils.isInInterfaceBlock(clsFoo));
    }

    @Test
    public void testIsInAnnotationBlock() {
        String code = "package com.example;\n" +
                "@Retention(RetentionPolicy.RUNTIME)\n" +
                "annotation MyAnnotation {\n" +
                "}\n" +
                "public class Foo {\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST annotation = astRoot.findFirstToken(ASTNodeType.ANNOTATION);
        assertTrue(ScopeUtils.isInAnnotationBlock(annotation));
    }

    @Test
    public void testIsInAnnotationBlock_NotAnAnnotation() {
        String code = "package com.example;\n" +
                "interface Foo {\n" +
                "}\n" +
                "@Retention(RetentionPolicy.RUNTIME)\n" +
                "annotation MyAnnotation {\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST intfFoo = astRoot.findChild(0);
        assertFalse(ScopeUtils.isInAnnotationBlock(intfFoo));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        String code1 = "package com.example;\n" +
                "interface Foo {\n" +
                "}\n" +
                "@Retention(RetentionPolicy.RUNTIME)\n" +
                "annotation MyAnnotation {\n" +
                "}";

        String code2 = "package com.example;\n" +
                "public class Foo {\n" +
                "}";

        DetailAST astRoot1 = ParseUtil.parse(code1);
        DetailAST intfFoo = astRoot1.findChild(0);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(intfFoo));

        DetailAST annMyAnnotation = astRoot1.findFirstToken(ASTNodeType.ANNOTATION);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(annMyAnnotation));

        DetailAST clsFoo = ParseUtil.parse(code2).findChild(0);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(clsFoo));
    }

    @Test
    public void testIsInEnumBlock() {
        String code = "package com.example;\n" +
                "enum Foo {\n" +
                "    VALUE_1,\n" +
                "    VALUE_2,\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST enumFoo = astRoot.findChild(0);
        assertTrue(ScopeUtils.isInEnumBlock(enumFoo));
    }

    @Test
    public void testIsInEnumBlock_NotAnEnum() {
        String code = "package com.example;\n" +
                "public interface Foo {\n" +
                "}\n" +
                "enum Bar {\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST enumBar = astRoot.findChild(1);
        assertFalse(ScopeUtils.isInEnumBlock(astRoot.findChild(0)));
        assertTrue(ScopeUtils.isInEnumBlock(enumBar));
    }

    @Test
    public void testIsInCodeBlock() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "    public void bar() {\n" +
                "        int x = 0;\n" +
                "        if (x > 0) {\n" +
                "            System.out.println(\"x is greater than 0\");\n" +
                "        }\n" +
                "    }\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST codeBlock = astRoot.findChild(0).findChild(1);
        assertTrue(ScopeUtils.isInCodeBlock(codeBlock));
    }

    @Test
    public void testIsOuterMostType() {
        String code = "package com.example;\n" +
                "public interface Foo {\n" +
                "    void bar();\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST intfFoo = astRoot.findChild(0);
        assertFalse(ScopeUtils.isOuterMostType(intfFoo));

        DetailAST astRoot2 = ParseUtil.parse(code);
        DetailAST root = astRoot2.getRootNode();
        assertTrue(ScopeUtils.isOuterMostType(root));
    }

    @Test
    public void testIsLocalVariableDef() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "    void doSomething() {\n" +
                "        int x = 0;\n" +
                "    }\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST varX = astRoot.findChild(0).findChild(1).findChild(0);
        assertTrue(ScopeUtils.isLocalVariableDef(varX));
    }

    @Test
    public void testIsLocalVariableDef_NotAVariable() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "    void doSomething() {\n" +
                "        System.out.println(\"Hello World\");\n" +
                "    }\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST mthDoSomething = astRoot.findChild(0);
        assertFalse(ScopeUtils.isLocalVariableDef(mthDoSomething));
    }

    @Test
    public void testIsClassFieldDef() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "    int x;\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST fieldX = astRoot.findChild(0).findChild(1).findChild(0);
        assertTrue(ScopeUtils.isClassFieldDef(fieldX));
    }

    @Test
    public void testIsClassFieldDef_NotAField() {
        String code = "package com.example;\n" +
                "public class Foo {\n" +
                "    void doSomething() {\n" +
                "    }\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST mthDoSomething = astRoot.findChild(0);
        assertFalse(ScopeUtils.isClassFieldDef(mthDoSomething));
    }

    @Test
    public void testIsInScope() {
        String code = "package com.example;\n" +
                "public interface Foo {\n" +
                "    void bar();\n" +
                "}\n" +
                "public class Bar implements Foo {\n" +
                "    void bar() {\n" +
                "        int x = 0;\n" +
                "    }\n" +
                "}";

        DetailAST astRoot = ParseUtil.parse(code);
        DetailAST intfFoo = astRoot.findChild(0);
        DetailAST clsBar = astRoot.findChild(1);

        assertTrue(ScopeUtils.isInScope(clsBar, Scope.INTERFACE));
        assertTrue(ScopeUtils.isInScope(clsBar, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(clsBar, Scope.PACKAGE));

        assertTrue(ScopeUtils.isInScope(intfFoo, Scope.INTERFACE));
        assertFalse(ScopeUtils.isInScope(intfFoo, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(intfFoo, Scope.PACKAGE));

        DetailAST mthBar = clsBar.findChild(0);
        assertTrue(ScopeUtils.isInScope(mthBar, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(mthBar, Scope.INTERFACE));
        assertFalse(ScopeUtils.isInScope(mthBar, Scope.PACKAGE));

        DetailAST varX = mthBar.findChild(0);
        assertTrue(ScopeUtils.isInScope(varX, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(varX, Scope.INTERFACE));
        assertFalse(ScopeUtils.isInScope(varX, Scope.PACKAGE));
    }

    @Test
    public void testGetSurroundingScope_NullNode() {
        DetailAST node = null;
        assertNull(ScopeUtils.getSurroundingScope(node));
    }
}