package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createAST("modifiers public protected private");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(mods));

        DetailAST modsPublic = createAST("modifiers public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(modsPublic));

        DetailAST modsPrivate = createAST("modifiers private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(modsPrivate));

        DetailAST modsPackage = createAST("modifiers");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(modsPackage));

        DetailAST modsDefault = createAST("modifiers");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(modsDefault));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST nodeInterface = createAST("interface Foo {}");
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(nodeInterface));

        DetailAST nodeClass = createAST("class Foo {}", createAST("interface Bar {}"));
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(nodeClass));

        DetailAST nodeMethod = createAST("class Foo { void foo() {} }");
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(nodeMethod));

        DetailAST nodeVariable = createAST("class Foo { int x; }");
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(nodeVariable));

        DetailAST nodeAnnotation = createAST("@annotation Foo {}");
        assertEquals(Scope.ANNOTATION, ScopeUtils.getSurroundingScope(nodeAnnotation));

        DetailAST nodeEnum = createAST("enum Foo {}");
        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(nodeEnum));

        DetailAST nodeAnonymous = createAST("class Foo { void foo() { new Object() {}; } }");
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(nodeAnonymous));

        DetailAST nodeBlockComment = createAST("/* comment */ class Foo {}", createAST("public class Foo {}", createAST("void foo()")));
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(nodeBlockComment));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST nodeInterface = createAST("interface Foo {}");
        assertTrue(ScopeUtils.isInInterfaceBlock(nodeInterface));

        DetailAST nodeClass = createAST("class Foo {}", createAST("interface Bar {}"));
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeClass));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST nodeAnnotation = createAST("@annotation Foo {}");
        assertTrue(ScopeUtils.isInAnnotationBlock(nodeAnnotation));

        DetailAST nodeClass = createAST("class Foo {}", createAST("@annotation Bar {}"));
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeClass));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST nodeInterface = createAST("interface Foo {}");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeInterface));

        DetailAST nodeAnnotation = createAST("@annotation Foo {}");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAnnotation));

        DetailAST nodeClass = createAST("class Foo {}", createAST("interface Bar {}"));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeClass));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST nodeEnum = createAST("enum Foo {}");
        assertTrue(ScopeUtils.isInEnumBlock(nodeEnum));

        DetailAST nodeClass = createAST("class Foo {}", createAST("enum Bar {}"));
        assertFalse(ScopeUtils.isInEnumBlock(nodeClass));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST nodeCodeBlock = createAST("class Foo { void foo() {}}");
        assertTrue(ScopeUtils.isInCodeBlock(nodeCodeBlock));

        DetailAST nodeInterface = createAST("interface Foo {}");
        assertFalse(ScopeUtils.isInCodeBlock(nodeInterface));

        DetailAST nodeAnnotation = createAST("@annotation Foo {}");
        assertFalse(ScopeUtils.isInCodeBlock(nodeAnnotation));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST nodeClass = createAST("class Foo {}", createAST("class Bar {}", createAST("class Baz")));
        assertTrue(ScopeUtils.isOuterMostType(nodeClass));

        DetailAST nodeInterface = createAST("interface Foo {}", createAST("interface Bar {}", createAST("interface Baz")));
        assertTrue(ScopeUtils.isOuterMostType(nodeInterface));

        DetailAST nodeEnum = createAST("enum Foo {}", createAST("enum Bar ", createAST("enum Baz")));
        assertTrue(ScopeUtils.isOuterMostType(nodeEnum));

        DetailAST nodeAnonymous = createAST("class Foo {}", createAST("class Bar {}", createAST("void foo() { new Object() {}; } }")));
        assertFalse(ScopeUtils.isOuterMostType(nodeAnonymous));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST nodeVariable = createAST("class Foo { int x; }");
        DetailAST nodeLocalVariable = createAST("class Foo { void foo() { int x; } }");
        DetailAST nodeLocalVariableAnonymous = createAST("class Foo { void foo() { new Object() { int x; }; } }");
        DetailAST nodeClass = createAST("class Foo { int x; }");

        assertTrue(ScopeUtils.isLocalVariableDef(nodeLocalVariable));
        assertTrue(ScopeUtils.isLocalVariableDef(nodeLocalVariableAnonymous));
        assertFalse(ScopeUtils.isLocalVariableDef(nodeVariable));
        assertFalse(ScopeUtils.isLocalVariableDef(nodeClass));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST nodeVariable = createAST("class Foo { int x; }");
        DetailAST nodeLocalVariable = createAST("class Foo { void foo() { int x; } }");

        assertFalse(ScopeUtils.isClassFieldDef(nodeVariable));
        assertFalse(ScopeUtils.isClassFieldDef(nodeLocalVariable));
    }

    @Test
    public void testIsInScope() {
        DetailAST nodeCodeBlock = createAST("class Foo { int x; }");
        DetailAST nodeVariable = createAST("int x;");

        assertTrue(ScopeUtils.isInScope(nodeCodeBlock, Scope.CLASS));
        assertTrue(ScopeUtils.isInScope(nodeVariable, Scope.CLASS));

        DetailAST nodeClass = createAST("class Foo {}", createAST("int x;"));
        assertTrue(ScopeUtils.isInScope(nodeClass, Scope.CLASS));

        DetailAST nodeInterface = createAST("interface Foo {}", createAST("int x;"));
        assertFalse(ScopeUtils.isInScope(nodeInterface, Scope.CLASS));

        DetailAST nodeAnnotation = createAST("@annotation Foo {}", createAST("int x;"));
        assertFalse(ScopeUtils.isInScope(nodeAnnotation, Scope.CLASS));
    }

    private DetailAST createAST(String code) {
        return new DetailAST() {
            @Override
            public String toString() {
                return code;
            }

            @Override
            public int getType() {
                return DetailAST.Type.PROGRAM_UNIT;
            }
        };
    }

    private DetailAST createAST(String... codes) {
        DetailAST root = new DetailAST() {
            @Override
            public String toString() {
                StringBuilder builder = new StringBuilder();
                for (String code : codes) {
                    builder.append(code);
                    builder.append("\n");
                }
                return builder.toString();
            }

            @Override
            public int getType() {
                return DetailAST.Type.PROGRAM_UNIT;
            }
        };
        for (String code : codes) {
            DetailAST ast = createAST(code);
            root.setChild(root.getLastChild(), ast);
        }
        return root;
    }
}