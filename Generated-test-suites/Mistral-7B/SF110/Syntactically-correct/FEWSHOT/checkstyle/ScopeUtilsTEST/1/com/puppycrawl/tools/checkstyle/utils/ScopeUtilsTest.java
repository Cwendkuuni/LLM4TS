package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_Package() {
        DetailAST ast = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        Scope scope = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetScopeFromMods_Public() {
        DetailAST ast = new DetailAST() {
            @Override
            public int getType() {
                return 16;
            }

            @Override
            public String getText() {
                return "public";
            }
        };
        DetailAST mods = new DetailAST() {
            @Override
            public int getType() {
                return 5;
            }
        };
        DetailAST modToken = new DetailAST() {
            @Override
            public int getType() {
                return ast.getType();
            }

            @Override
            public String getText() {
                return ast.getText();
            }
        };
        mods.addChild(modToken);
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetScopeFromMods_Protected() {
        DetailAST ast = new DetailAST() {
            @Override
            public int getType() {
                return 17;
            }

            @Override
            public String getText() {
                return "protected";
            }
        };
        DetailAST mods = new DetailAST() {
            @Override
            public int getType() {
                return 5;
            }
        };
        DetailAST modToken = new DetailAST() {
            @Override
            public int getType() {
                return ast.getType();
            }

            @Override
            public String getText() {
                return ast.getText();
            }
        };
        mods.addChild(modToken);
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PROTECTED, scope);
    }

    @Test
    public void testGetScopeFromMods_Private() {
        DetailAST ast = new DetailAST() {
            @Override
            public int getType() {
                return 18;
            }

            @Override
            public String getText() {
                return "private";
            }
        };
        DetailAST mods = new DetailAST() {
            @Override
            public int getType() {
                return 5;
            }
        };
        DetailAST modToken = new DetailAST() {
            @Override
            public int getType() {
                return ast.getType();
            }

            @Override
            public String getText() {
                return ast.getText();
            }
        };
        mods.addChild(modToken);
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PRIVATE, scope);
    }

    @Test
    public void testGetScopeFromMods_NoModifier() {
        DetailAST ast = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        Scope scope = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope_ClassScope() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.CLASS, scope);
    }

    @Test
    public void testGetSurroundingScope_PackageScope() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }
        };
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope_ModuleScope() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 157;
            }
        };
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.MODULE, scope);
    }

    @Test
    public void testGetSurroundingScope_AnonymousInnerScope() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 136;
            }
        };
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, scope);
    }

    @Test
    public void testGetSurroundingScope_Null() {
        DetailAST node = null;
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);
    }

    @Test
    public void testIsInInterfaceBlock_InterfaceBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }
        };
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock_NotInterfaceBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_AnnotationBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 157;
            }
        };
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_NotAnnotationBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_InterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }
        };
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NotInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock_EnumBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 154;
            }
        };
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInEnumBlock_NotEnumBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock_CodeBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 9;
            }
        };
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsInCodeBlock_NotCodeBlock() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }
        };
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType_OuterMostType() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsOuterMostType_NotOuterMostType() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }
        };
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef_LocalVariableDef() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 10;
            }

            @Override
            public String getText() {
                return "localVariable";
            }
        };
        DetailAST parent = new DetailAST() {
            @Override
            public int getType() {
                return 7;
            }
        };
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsLocalVariableDef_NotLocalVariableDef() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 10;
            }

            @Override
            public String getText() {
                return "classField";
            }
        };
        DetailAST parent = new DetailAST() {
            @Override
            public int getType() {
                return 96;
            }
        };
        node.setParent(parent);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef_ClassFieldDef() {
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 10;
            }

            @Override
            public String getText() {
                return "classField";
            }
        };
        assertFalse(ScopeUtils.isLocalVariableDef(node));
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope_InScope() {
        DetailAST ast = new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }
        };
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }
        };
        node.setParent(ast);
        Scope scope = Scope.MODULE;
        assertTrue(ScopeUtils.isInScope(node, scope));
    }

    @Test
    public void testIsInScope_NotInScope() {
        DetailAST ast = new DetailAST() {
            @Override
            public int getType() {
                return 15;
            }
        };
        DetailAST node = new DetailAST() {
            @Override
            public int getType() {
                return 14;
            }
        };
        node.setParent(ast);
        Scope scope = Scope.PACKAGE;
        assertFalse(ScopeUtils.isInScope(node, scope));
    }
}