import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class NamedTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;
 private StaticScope<JSType> enclosing;
 private NamedType namedType;

 @Before
 public void setup() {
 registry = mock(JSTypeRegistry.class);
 errorReporter = mock(ErrorReporter.class);
 enclosing = mock(StaticScope.class);
 namedType = new NamedType(registry, "String", "test.java", 1, 1);
 }

 @Test
 public void testDefineProperty() {
 // Test defining a property when the type is not resolved
 namedType.defineProperty("testProperty", JSType.BOOLEAN, false, null);
 // Verify that the property is saved as a continuation
 assertEquals(1, namedType.propertyContinuations.size());
 PropertyContinuation continuation = namedType.propertyContinuations.get(0);
 assertEquals("testProperty", continuation.propertyName);
 assertEquals(JSType.BOOLEAN, continuation.type);
 assertFalse(continuation.inferred);
 assertNull(continuation.propertyNode);
 }

 @Test
 public void testFinishPropertyContinuations() {
 // Test finishing property continuations when the type is resolved
 ObjectType referencedObjType = mock(ObjectType.class);
 when(registry.getType("String")).thenReturn(referencedObjType);
 namedType.finishPropertyContinuations();
 // Verify that properties are defined on the referenced object type
 verify(referencedObjType).defineProperty("testProperty", eq(JSType.BOOLEAN), anyBoolean(), any(Node.class));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenTypeIsResolved() {
 namedType.resolveInternal(errorReporter, enclosing);
 namedType.finishPropertyContinuations();
 namedType.defineProperty("testProperty", JSType.BOOLEAN, false, null);
 fail("Should not be able to define property after finishPropertyContinuations is called");
 }

 @Test
 public void testGetReferencedType() {
 assertNull(namedType.getReferencedType());
 }

 @Test
 public void testGetReferencedTypeWhenResolved() {
 when(registry.getType("String")).thenReturn(JSType.STRING);
 namedType.resolveInternal(errorReporter, enclosing);
 assertEquals(JSType.STRING, namedType.getReferencedType());
 }

 @Test
 public void testResolveViaRegistry() {
 when(registry.getType("String")).thenReturn(JSType.STRING);
 namedType.resolveInternal(errorReporter, enclosing);
 verify(registry).getType("String");
 }

 @Test
 public void testResolveViaProperties() {
 JSType firstComponentType = JSType.STRING;
 StaticSlot<JSType> firstComponentSlot = mock(StaticSlot.class);
 when(enclosing.getSlot("String")).thenReturn(firstComponentSlot);
 when(firstComponentSlot.getType()).thenReturn(firstComponentType);
 ObjectType secondComponentType = JSType.NUMBER;
 when(firstComponentType.getPropertyType("Number")).thenReturn(secondComponentType);
 namedType.resolveInternal(errorReporter, enclosing);
 verify(enclosing).getSlot("String");
 verify(firstComponentSlot).getType();
 verify(firstComponentType).getPropertyType("Number");
 }

 @Test
 public void testSetReferencedAndResolvedType() {
 JSType type = JSType.STRING;
 namedType.setReferencedAndResolvedType(type, errorReporter, enclosing);
 assertEquals(type, namedType.getReferencedType());
 assertEquals(type, namedType.getResolvedType());
 }

 @Test
 public void testHandleTypeCycle() {
 namedType.handleTypeCycle(errorReporter);
 verify(errorReporter).warning("Cycle detected in inheritance chain of type test.java", eq("test.java"), anyInt(), anyInt());
 }

 @Test
 public void testCheckEnumElementCycle() {
 JSType enumElementType = mock(EnumElementType.class);
 when(enumElementType.getPrimitiveType()).thenReturn(namedType);
 namedType.checkEnumElementCycle(errorReporter);
 verify(errorReporter).warning("Cycle detected in inheritance chain of type test.java", eq("test.java"), anyInt(), anyInt());
 }

 @Test
 public void testHandleUnresolvedType() {
 namedType.handleUnresolvedType(errorReporter, false);
 verify(errorReporter, times(1)).warning("Bad type annotation. Unknown type test.java", eq("test.java"), anyInt(), anyInt());
 }

 @Test
 public void testHandleUnresolvedTypeWhenForwardDeclared() {
 registry.registerTypeName("test.java", "String");
 namedType.handleUnresolvedType(errorReporter, true);
 // No warning should be issued for a forward-declared type name
 verify(errorReporter, never()).warning("Bad type annotation. Unknown type test.java", eq("test.java"), anyInt(), anyInt());
 }
}