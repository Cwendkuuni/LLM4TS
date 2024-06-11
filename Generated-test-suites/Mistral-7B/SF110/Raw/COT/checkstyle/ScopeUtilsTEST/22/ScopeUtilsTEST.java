import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.*;

public class ScopeUtilsTest {

 private DetailAST createTestNode(int type, DetailAST parent) {
 return new DetailAST() {
 @Override public int getType() { return type; }
 @Override public DetailAST getParent() { return parent; }
 };
 }

 @Test
 public void testGetScopeFromMods_public() {
 DetailAST mods = new BasicASTList();
 DetailAST publicKeyword = createTestNode(TokenTypes.KEYWORD_PUBLIC, mods);
 DetailAST testNode = new DetailAST() {
 @Override public int getType() { return 0; }
 };
 Scope expected = Scope.PUBLIC;
 Scope result = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, result);
 result = ScopeUtils.getScopeFromMods(new BasicASTList(publicKeyword, testNode));
 assertEquals(expected, result);
 }

 @Test
 public void testGetScopeFromMods_protected() {
 DetailAST mods = new BasicASTList();
 DetailAST protectedKeyword = createTestNode(TokenTypes.KEYWORD_PROTECTED, mods);
 DetailAST testNode = new DetailAST() {
 @Override public int getType() { return 0; }
 };
 Scope expected = Scope.PROTECTED;
 Scope result = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, result);
 result = ScopeUtils.getScopeFromMods(new BasicASTList(protectedKeyword, testNode));
 assertEquals(expected, result);
 }

 @Test
 public void testGetScopeFromMods_private() {
 DetailAST mods = new BasicASTList();
 DetailAST privateKeyword = createTestNode(TokenTypes.KEYWORD_PRIVATE, mods);
 DetailAST testNode = new DetailAST() {
 @Override public int getType() { return 0; }
 };
 Scope expected = Scope.PRIVATE;
 Scope result = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, result);
 result = ScopeUtils.getScopeFromMods(new BasicASTList(privateKeyword, testNode));
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetScopeFromMods_nullMods() {
 ScopeUtils.getScopeFromMods(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetScopeFromMods_emptyMods() {
 ScopeUtils.getScopeFromMods(new BasicASTList());
 }

 @Test
 public void testGetScopeFromMods_multipleKeywords() {
 DetailAST mods = new BasicASTList();
 DetailAST publicKeyword = createTestNode(TokenTypes.KEYWORD_PUBLIC, mods);
 DetailAST protectedKeyword = createTestNode(TokenTypes.KEYWORD_PROTECTED, mods);
 DetailAST testNode = new DetailAST() {
 @Override public int getType() { return 0; }
 };
 Scope expected = Scope.PROTECTED;
 Scope result = ScopeUtils.getScopeFromMods(new BasicASTList(protectedKeyword, publicKeyword, testNode));
 assertEquals(expected, result);
 }

 @Test
 public void testGetScopeFromMods_noKeyword() {
 DetailAST mods = new BasicASTList();
 DetailAST testNode = new DetailAST() {
 @Override public int getType() { return 0; }
 };
 Scope result = ScopeUtils.getScopeFromMods(mods);
 assertEquals(Scope.PACKAGE, result);
 result = ScopeUtils.getScopeFromMods(new BasicASTList(testNode));
 assertEquals(Scope.PACKAGE, result);
 }

 @Test
 public void testGetSurroundingScope_classLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 Scope expected = Scope.CLASS;
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, result);
 }

 @Test
 public void testGetSurroundingScope_packageLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 14; }
 };
 Scope expected = Scope.PACKAGE;
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, result);
 }

 @Test
 public void testGetSurroundingScope_interfaceLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 157; }
 };
 Scope expected = Scope.INTERFACE;
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, result);
 }

 @Test
 public void testGetSurroundingScope_annotationLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 157; }
 };
 Scope expected = Scope.ANNOTATION;
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, result);
 }

 @Test
 public void testGetSurroundingScope_anonymousClassLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 136; }
 };
 Scope expected = Scope.ANONINNER;
 Scope result = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInInterfaceBlock_interfaceLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 boolean expected = true;
 boolean result = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInInterfaceBlock_classLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 boolean expected = false;
 boolean result = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInAnnotationBlock_annotationLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 157; }
 };
 boolean expected = true;
 boolean result = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInAnnotationBlock_classLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 boolean expected = false;
 boolean result = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_interfaceLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 boolean expected = true;
 boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_annotationLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 157; }
 };
 boolean expected = true;
 boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_classLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 boolean expected = false;
 boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInEnumBlock_enumLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 154; }
 };
 boolean expected = true;
 boolean result = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInEnumBlock_classLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 boolean expected = false;
 boolean result = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInCodeBlock_codeBlockLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 9; }
 };
 boolean expected = true;
 boolean result = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsInCodeBlock_commentLevel() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 2; }
 };
 boolean expected = false;
 boolean result = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsOuterMostType_outerMostType() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 boolean expected = true;
 boolean result = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, result);
 }

 @Test
 public void testIsOuterMostType_notOuterMostType() {
 DetailAST node = new DetailAST() {
 @Override public int getType() { return 15; }
 };
 boolean expected = false;
 DetailAST parent = new DetailAST() {
 @Override public int getType() { return 14; }
 };
 node.setParent(parent);
 boolean result = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, result);
 }
}