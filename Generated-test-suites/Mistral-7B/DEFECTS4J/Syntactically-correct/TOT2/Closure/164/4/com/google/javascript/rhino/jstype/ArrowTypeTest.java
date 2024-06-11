import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter();
 }

 @Test
 public void testArrowTypeConstruction() {
 Node parameters = Node.newList();
 JSType returnType = JSTypeNative.INT;
 ArrowType arrowType = new ArrowType(registry, parameters, returnType);
 assertEquals(parameters, arrowType.parameters);
 assertEquals(returnType, arrowType.returnType);
 assertEquals(false, arrowType.returnTypeInferred);
 }

 @Test
 public void testArrowTypeConstructionWithInferredReturnType() {
 Node parameters = Node.newList();
 ArrowType arrowType = new ArrowType(registry, parameters, null, true);
 assertEquals(parameters, arrowType.parameters);
 assertEquals(JSTypeNative.UNKNOWN_TYPE, arrowType.returnType);
 assertEquals(true, arrowType.returnTypeInferred);
 }

 @Test
 public void testArrowTypeIsSubtypeWithSameTypes() {
 ArrowType arrowType1 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 assertTrue(arrowType1.isSubtype(arrowType2));
 assertTrue(arrowType2.isSubtype(arrowType1));
 }

 @Test
 public void testArrowTypeIsSubtypeWithDifferentTypes() {
 ArrowType arrowType1 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.NULL, JSTypeNative.STR);
 assertFalse(arrowType1.isSubtype(arrowType2));
 assertFalse(arrowType2.isSubtype(arrowType1));
 }

 @Test
 public void testArrowTypeIsSubtypeWithVarArgs() {
 ArrowType arrowType1 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.VAR_ARGS, JSTypeNative.INT);
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testArrowTypeGetLeastSupertype() {
 ArrowType arrowType1 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.NULL, JSTypeNative.STR);
 arrowType1.getLeastSupertype(arrowType2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testArrowTypeGetGreatestSubtype() {
 ArrowType arrowType1 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.NULL, JSTypeNative.STR);
 arrowType1.getGreatestSubtype(arrowType2);
 }

 @Test
 public void testArrowTypeHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.INT), JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.INT), JSTypeNative.INT);
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 assertTrue(arrowType2.hasEqualParameters(arrowType1));
 }

 @Test
 public void testArrowTypeHasUnequalParameters() {
 ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.INT), JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.STR), JSTypeNative.INT);
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 assertFalse(arrowType2.hasEqualParameters(arrowType1));
 }

 @Test
 public void testArrowTypeToString() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 assertEquals("ArrowType[parameters: [], returnType: Int]", arrowType.toString());
 }

 @Test
 public void testArrowTypeWithNullParameters() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 Node parameters = Node.NULL;
 ArrowType newArrowType = new ArrowType(registry, parameters, JSTypeNative.INT);
 assertEquals(arrowType, newArrowType);
 }

 @Test
 public void testArrowTypeWithEmptyParameters() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType newArrowType = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 newArrowType.parameters = Node.NULL;
 assertEquals(arrowType, newArrowType);
 }

 @Test
 public void testArrowTypeWithParameters() {
 ArrowType arrowType = new ArrowType(registry, Node.newList(Node.INT), JSTypeNative.INT);
 ArrowType newArrowType = new ArrowType(registry, Node.newList(Node.INT), JSTypeNative.INT);
 newArrowType.parameters = Node.newList(Node.INT);
 assertEquals(arrowType, newArrowType);
 }

 @Test
 public void testArrowTypeWithVarArgs() {
 ArrowType arrowType = new ArrowType(registry, Node.VAR_ARGS, JSTypeNative.INT);
 ArrowType newArrowType = new ArrowType(registry, Node.VAR_ARGS, JSTypeNative.INT);
 assertEquals(arrowType, newArrowType);
 }

 @Test
 public void testArrowTypeWithInferredReturnType() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, null, true);
 ArrowType newArrowType = new ArrowType(registry, Node.NULL, null, true);
 assertEquals(arrowType, newArrowType);
 }

 @Test
 public void testArrowTypeWithKnownReturnType() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType newArrowType = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 newArrowType.returnType = JSTypeNative.STR;
 assertFalse(arrowType.isEquivalentTo(newArrowType));
 }

 @Test
 public void testArrowTypeWithUnknownReturnType() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, null, true);
 ArrowType newArrowType = new ArrowType(registry, Node.NULL, null, true);
 newArrowType.returnType = JSTypeNative.UNKNOWN_TYPE;
 assertEquals(arrowType, newArrowType);
 }

 @Test
 public void testArrowTypeWithUnresolvedReturnType() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, null, true);
 ArrowType newArrowType = new ArrowType(registry, Node.NULL, null, true);
 newArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry));
 assertFalse(arrowType.isEquivalentTo(newArrowType));
 }

 @Test
 public void testArrowTypeWithResolvedReturnType() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, null, true);
 ArrowType newArrowType = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 newArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry));
 assertEquals(arrowType, newArrowType);
 }

 @Test
 public void testArrowTypeWithNullReturnType() {
 ArrowType arrowType = new ArrowType(registry, Node.NULL, null, true);
 ArrowType newArrowType = new ArrowType(registry, Node.NULL, null, true);
 newArrowType.returnType = null;
 assertEquals(arrowType, newArrowType);
 }

 @Test
 public void testArrowTypeEquals() {
 ArrowType arrowType1 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType3 = new ArrowType(registry, Node.NULL, JSTypeNative.STR);
 assertTrue(arrowType1.equals(arrowType1));
 assertTrue(arrowType1.equals(arrowType2));
 assertFalse(arrowType1.equals(arrowType3));
 }

 @Test
 public void testArrowTypeHashCode() {
 ArrowType arrowType1 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType2 = new ArrowType(registry, Node.NULL, JSTypeNative.INT);
 ArrowType arrowType3 = new ArrowType(registry, Node.NULL, JSTypeNative.STR);
 int hashCode1 = arrowType1.hashCode();
 int hashCode2 = arrowType1.hashCode();
 int hashCode3 = arrowType3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}