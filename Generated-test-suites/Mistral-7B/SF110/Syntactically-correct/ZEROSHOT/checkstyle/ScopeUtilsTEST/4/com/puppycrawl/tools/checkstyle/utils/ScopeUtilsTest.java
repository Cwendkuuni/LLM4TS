import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createModsAST("public protected private");
        assertEquals(SCOPE_PUBLIC, ScopeUtils.getScopeFromMods(mods));

        mods = createModsAST("protected private");
        assertEquals(SCOPE_PROTECTED, ScopeUtils.getScopeFromMods(mods));

        mods = createModsAST("private");
        assertEquals(SCOPE_PRIVATE, ScopeUtils.getScopeFromMods(mods));

        mods = createModsAST("public");
        assertEquals(SCOPE_PUBLIC, ScopeUtils.getScopeFromMods(mods));

        mods = createModsAST("protected");
        assertEquals(SCOPE_PROTECTED, ScopeUtils.getScopeFromMods(mods));

        mods = createModsAST("private");
        assertEquals(SCOPE_PRIVATE, ScopeUtils.getScopeFromMods(mods));

        mods = createModsAST("");
        assertEquals(SCOPE_PACKAGE, ScopeUtils.getScopeFromMods(mods));

        mods = createModsAST("foo");
        assertEquals(SCOPE_PACKAGE, ScopeUtils.getScopeFromMods(mods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createClassDeclarationAST("public class Foo { }");
        assertEquals(SCOPE_PUBLIC, ScopeUtils.getSurroundingScope(node));

        node = createVariableDeclarationAST("int x;");
        assertEquals(SCOPE_ANONINNER, ScopeUtils.getSurroundingScope(node));

        node = createMethodDeclarationAST("public void foo() { }");
        assertEquals(SCOPE_PUBLIC, ScopeUtils.getSurroundingScope(node));

        node = createInterfaceDeclarationAST("public interface Bar { }");
        assertEquals(SCOPE_INTERFACE, ScopeUtils.getSurroundingScope(node));

        node = createAnnotationDeclarationAST("@annotation");
        assertEquals(SCOPE_ANNOTATION, ScopeUtils.getSurroundingScope(node));

        node = createEnumDeclarationAST("public enum Foo { VALUE }");
        assertEquals(SCOPE_ENUM, ScopeUtils.getSurroundingScope(node));

        node = createBlockAST("{ }");
        assertEquals(SCOPE_CODE_BLOCK, ScopeUtils.getSurroundingScope(node));

        node = createPackageDeclarationAST("package foo.bar");
        assertEquals(SCOPE_PACKAGE, ScopeUtils.getSurroundingScope(node));

        node = createEmptyAST();
        assertNull(ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createMethodDeclarationAST("public interface Foo { void foo(); }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createVariableDeclarationAST("interface Foo { int x; }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createMethodDeclarationAST("public class Foo { void foo(); }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createAnnotationDeclarationAST("@annotation");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = createClassDeclarationAST("public class Foo { }");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createMethodDeclarationAST("public interface Foo { void foo(); }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAnnotationDeclarationAST("@annotation");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createClassDeclarationAST("public class Foo { }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createEnumDeclarationAST("public enum Foo { VALUE }");
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = createClassDeclarationAST("public class Foo { }");
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createBlockAST("{ }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createMethodDeclarationAST("public void foo() { }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createClassDeclarationAST("public class Foo { }");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createClassDeclarationAST("public class Foo { }");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createMethodDeclarationAST("public class Foo { void foo() { } }");
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createVariableDeclarationAST("int x;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createClassFieldDeclarationAST("int x;");
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createClassFieldDeclarationAST("int x;");
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST node = createMethodDeclarationAST("public void foo() { }");
        assertTrue(ScopeUtils.isInScope(node, SCOPE_PUBLIC));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isInScope(node, SCOPE_PUBLIC));

        node = createMethodDeclarationAST("public void foo() { }");
        assertTrue(ScopeUtils.isInScope(node, SCOPE_CODE_BLOCK));

        node = createVariableDeclarationAST("int x;");
        assertFalse(ScopeUtils.isInScope(node, SCOPE_CODE_BLOCK));
    }

    private DetailAST createModsAST(String modifiers) {
        return new DetailAST() {
            @Override
            public int getType() {
                return 1;
            }

            @Override
            public String toString() {
                return modifiers;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }

    private DetailAST createClassDeclarationAST(String code) {
        return new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }

            @Override
            public String toString() {
                return code;
            }

            @Override
            public DetailAST getFirstChild() {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }

    private DetailAST createVariableDeclarationAST(String code) {
        return new DetailAST() {
            @Override
            public int getType() {
                return 7;
            }

            @Override
            public String toString() {
                return code;
            }

            @Override
            public DetailAST getFirstChild() {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }

    private DetailAST createMethodDeclarationAST(String code) {
        return new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }

            @Override
            public String toString() {
                return code;
            }

            @Override
            public DetailAST getFirstChild() {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }

    private DetailAST createInterfaceDeclarationAST(String code) {
        return new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }

            @Override
            public String toString() {
                return code;
            }

            @Override
            public DetailAST getFirstChild() {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }

    private DetailAST createAnnotationDeclarationAST(String code) {
        return new DetailAST() {
            @Override
            public int getType() {
                return 157;
            }

            @Override
            public String toString() {
                return code;
            }

            @Override
            public DetailAST getFirstChild() {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }

    private DetailAST createEnumDeclarationAST(String code) {
        return new DetailAST() {
            @Override
            public int getType() {
                return 154;
            }

            @Override
            public String toString() {
                return code;
            }

            @Override
            public DetailAST getFirstChild() {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }

    private DetailAST createBlockAST(String code) {
        return new DetailAST() {
            @Override
            public int getType() {
                return 9;
            }

            @Override
            public String toString() {
                return code;
            }

            @Override
            public DetailAST getFirstChild() {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }

    private DetailAST createEmptyAST() {
        return new DetailAST() {
            @Override
            public int getType() {
                return -1;
            }

            @Override
            public String toString() {
                return "";
            }

            @Override
            public DetailAST getFirstChild() {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST findFirstToken(int type) {
                return null;
            }
        };
    }
}