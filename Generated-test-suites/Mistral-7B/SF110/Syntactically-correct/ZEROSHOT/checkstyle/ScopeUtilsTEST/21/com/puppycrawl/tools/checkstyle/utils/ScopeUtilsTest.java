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
 DetailAST astMods = createMockModsAST();
 Scope expectedScope = Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
 assertEquals(expectedScope, actualScope);

 astMods = createMockModsAST("public");
 expectedScope = Scope.PUBLIC;
 actualScope = ScopeUtils.getScopeFromMods(astMods);
 assertEquals(expectedScope, actualScope);

 astMods = createMockModsAST("protected");
 expectedScope = Scope.PROTECTED;
 actualScope = ScopeUtils.getScopeFromMods(astMods);
 assertEquals(expectedScope, actualScope);

 astMods = createMockModsAST("private");
 expectedScope = Scope.PRIVATE;
 actualScope = ScopeUtils.getScopeFromMods(astMods);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_noModifiers() {
 DetailAST astMods = createMockModsAST();
 Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
 assertEquals(Scope.PACKAGE, actualScope);
 }

 @Test
 public void testGetScopeFromMods_multipleModifiers() {
 DetailAST astMods = createMockModsAST("public protected private");
 Scope expectedScope = Scope.PUBLIC;
 Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_invalidModifier() {
 DetailAST astMods = createMockModsAST("invalid");
 Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
 assertEquals(Scope.PACKAGE, actualScope);
 }

 @Test
 public void testGetSurroundingScope() {
 DetailAST astNode = createMockClassDeclarationAST();
 Scope expectedScope = Scope.CLASS;
 Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
 assertEquals(expectedScope, actualScope);

 DetailAST astNodeInInterface = createMockInterfaceDeclarationAST();
 expectedScope = Scope.INTERFACE;
 actualScope = ScopeUtils.getSurroundingScope(astNodeInInterface);
 assertEquals(expectedScope, actualScope);

 DetailAST astNodeInAnnotation = createMockAnnotationAST();
 expectedScope = Scope.ANNOTATION;
 actualScope = ScopeUtils.getSurroundingScope(astNodeInAnnotation);
 assertEquals(expectedScope, actualScope);

 DetailAST astNodeInEnum = createMockEnumDeclarationAST();
 expectedScope = Scope.ENUM;
 actualScope = ScopeUtils.getSurroundingScope(astNodeInEnum);
 assertEquals(expectedScope, actualScope);

 DetailAST astNodeInCodeBlock = createMockCodeBlockAST();
 expectedScope = Scope.CODE_BLOCK;
 actualScope = ScopeUtils.getSurroundingScope(astNodeInCodeBlock);
 assertEquals(expectedScope, actualScope);

 DetailAST astNodeInInterfaceBlock = createMockInterfaceBlockAST();
 expectedScope = Scope.INTERFACE_BLOCK;
 actualScope = ScopeUtils.getSurroundingScope(astNodeInInterfaceBlock);
 assertEquals(expectedScope, actualScope);

 DetailAST astNodeInAnnotationBlock = createMockAnnotationBlockAST();
 expectedScope = Scope.ANNOTATION_BLOCK;
 actualScope = ScopeUtils.getSurroundingScope(astNodeInAnnotationBlock);
 assertEquals(expectedScope, actualScope);

 DetailAST astNodeInEnumBlock = createMockEnumBlockAST();
 expectedScope = Scope.ENUM_BLOCK;
 actualScope = ScopeUtils.getSurroundingScope(astNodeInEnumBlock);
 assertEquals(expectedScope, actualScope);

 DetailAST astNodeInAnonymousClass = createMockAnonymousClassAST();
 expectedScope = Scope.ANONINNER;
 actualScope = ScopeUtils.getSurroundingScope(astNodeInAnonymousClass);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST astNode = createMockClassDeclarationAST();
 assertFalse(ScopeUtils.isInInterfaceBlock(astNode));

 DetailAST astNodeInInterface = createMockInterfaceDeclarationAST();
 assertTrue(ScopeUtils.isInInterfaceBlock(astNodeInInterface));
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST astNode = createMockClassDeclarationAST();
 assertFalse(ScopeUtils.isInAnnotationBlock(astNode));

 DetailAST astNodeInAnnotation = createMockAnnotationAST();
 assertTrue(ScopeUtils.isInAnnotationBlock(astNodeInAnnotation));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST astNode = createMockClassDeclarationAST();
 assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode));

 DetailAST astNodeInInterface = createMockInterfaceDeclarationAST();
 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNodeInInterface));

 DetailAST astNodeInAnnotation = createMockAnnotationAST();
 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNodeInAnnotation));
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST astNode = createMockClassDeclarationAST();
 assertFalse(ScopeUtils.isInEnumBlock(astNode));

 DetailAST astNodeInEnum = createMockEnumDeclarationAST();
 assertTrue(ScopeUtils.isInEnumBlock(astNodeInEnum));
 }

 @Test
 public void testIsInInterfaceOrEnumBlock() {
 DetailAST astNodeInInterface = createMockInterfaceDeclarationAST();
 assertFalse(ScopeUtils.isInInterfaceOrEnumBlock(astNodeInInterface));

 DetailAST astNodeInEnum = createMockEnumDeclarationAST();
 assertTrue(ScopeUtils.isInInterfaceOrEnumBlock(astNodeInEnum));
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST astNodeInCodeBlock = createMockCodeBlockAST();
 assertTrue(ScopeUtils.isInCodeBlock(astNodeInCodeBlock));
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST astNode = createMockClassDeclarationAST();
 assertTrue(ScopeUtils.isOuterMostType(astNode));

 DetailAST astNodeInClass = createMockClassDeclarationAST("class InnerClass { }");
 assertFalse(ScopeUtils.isOuterMostType(astNodeInClass));
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST astNodeLocalVariable = createMockVariableDeclarationAST("int x;");
 assertTrue(ScopeUtils.isLocalVariableDef(astNodeLocalVariable));

 DetailAST astNodeClassField = createMockVariableDeclarationAST("int x;", "class Test { int x; }");
 assertFalse(ScopeUtils.isLocalVariableDef(astNodeClassField));
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST astNodeClassField = createMockVariableDeclarationAST("int x;", "class Test { int x; }");
 assertTrue(ScopeUtils.isClassFieldDef(astNodeClassField));
 }

 @Test
 public void testIsInScope() {
 DetailAST astNode = createMockClassDeclarationAST();
 DetailAST astNodeInScope = createMockVariableDeclarationAST("int x;", "class Test { int x; }");
 assertTrue(ScopeUtils.isInScope(astNode, Scope.CLASS));

 DetailAST astNodeOutOfScope = createMockVariableDeclarationAST("int x;", "class Test { int y; }");
 assertFalse(ScopeUtils.isInScope(astNode, Scope.CLASS));

 DetailAST astNodeInEnum = createMockEnumDeclarationAST();
 assertTrue(ScopeUtils.isInScope(astNode, Scope.ENUM));

 DetailAST astNodeOutOfEnum = createMockCodeBlockAST();
 assertFalse(ScopeUtils.isInScope(astNode, Scope.ENUM));
 }

 private DetailAST createMockModsAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 0;
 }
 };
 }

 private DetailAST createMockModsAST(String... modifiers) {
 DetailAST astMods = new DetailAST() {
 @Override
 public int getType() {
 return 1;
 }

 @Override
 public int numChildren() {
 return modifiers.length;
 }

 @Override
 public DetailAST img(int i) {
 return new DetailAST() {
 @Override
 public String getText() {
 return modifiers[i];
 }

 @Override
 public int getType() {
 return 19;
 }
 };
 }
 };
 return astMods;
 }

 private DetailAST createMockClassDeclarationAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 14;
 }
 };
 }

 private DetailAST createMockInterfaceDeclarationAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 15;
 }
 };
 }

 private DetailAST createMockAnnotationAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 157;
 }
 };
 }

 private DetailAST createMockEnumDeclarationAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 154;
 }
 };
 }

 private DetailAST createMockCodeBlockAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 9;
 }
 };
 }

 private DetailAST createMockInterfaceBlockAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 15;
 }
 };
 }

 private DetailAST createMockAnnotationBlockAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 157;
 }
 };
 }

 private DetailAST createMockEnumBlockAST() {
 return new DetailAST() {
 @Override
 public int getType() {
 return 154;
 }
 };
 }

 private DetailAST createMockVariableDeclarationAST(String varDecl, String context) {
 DetailAST astVariableDeclaration = new DetailAST() {
 @Override
 public int getType() {
 return 10;
 }

 @Override
 public int numChildren() {
 return 2;
 }

 @Override
 public DetailAST img(int i) {
 if (i == 0) {
 return new DetailAST() {
 @Override
 public String getText() {
 return varDecl;
 }
 };
 } else {
 return new DetailAST() {
 @Override
 public String getText() {
 return context;
 }

 @Override
 public int getType() {
 return 8;
 }
 };
 }
 }
 };
 return astVariableDeclaration;
 }

 private DetailAST createMockAnonymousClassAST() {
 DetailAST astAnonymousClass = new DetailAST() {
 @Override
 public int getType() {
 return 136;
 }
 };
 return astAnonymousClass;
 }
}