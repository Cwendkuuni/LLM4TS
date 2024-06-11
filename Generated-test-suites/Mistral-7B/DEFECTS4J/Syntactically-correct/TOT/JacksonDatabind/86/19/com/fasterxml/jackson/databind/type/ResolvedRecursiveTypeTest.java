import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ResolvedRecursiveTypeTest {

 private JavaType refTypeA;
 private JavaType refTypeB;
 private JavaType recursiveType;

 @Before
 public void setUp() {
 TypeFactory typeFactory = new TypeFactory();
 refTypeA = typeFactory.constructType(String[].class);
 refTypeB = typeFactory.constructType(Integer[].class);
 }

 @Test
 public void testSetReference() {
 // Test setting reference to a valid JavaType
 recursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 recursiveType.setReference(refTypeA);
 assertNotNull(recursiveType.getSelfReferencedType());
 assertSame(refTypeA, recursiveType.getSelfReferencedType());

 // Test setting reference multiple times
 recursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 recursiveType.setReference(refTypeA);
 recursiveType.setReference(refTypeB);
 assertIllegalStateException(recursiveType, "Trying to re-set self reference");
 }

 @Test
 public void testGetSelfReferencedType() {
 // Test getting self-referenced type after setting it
 recursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 recursiveType.setReference(refTypeA);
 assertSame(refTypeA, recursiveType.getSelfReferencedType());

 // Test getting self-referenced type before setting it
 assertNull(new ResolvedRecursiveType(Object.class, new TypeBindings()).getSelfReferencedType());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String[].class, new TypeBindings());
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String[].class, new TypeBindings());
 assertFalse(type1.equals(type2));

 ResolvedRecursiveType type3 = new ResolvedRecursiveType(type1.getRawClass(), new TypeBindings());
 assertTrue(type1.equals(type3));

 ResolvedRecursiveType type4 = new ResolvedRecursiveType(Integer[].class, new TypeBindings());
 assertFalse(type1.equals(type4));

 ResolvedRecursiveType type5 = new ResolvedRecursiveType(type1.getRawClass(), new TypeBindings());
 type5.setReference(refTypeA);
 assertTrue(type1.equals(type5));
 }

 private void assertIllegalStateException(JavaType type, String message) {
 try {
 type.setReference(refTypeB);
 } catch (IllegalStateException e) {
 assertEquals(message, e.getMessage());
 }
 }
}