import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType selfReferenceType;
 private JavaType recursiveTypeA;
 private JavaType recursiveTypeB;

 @Before
 public void setup() {
 Class<?> erasedTypeA = RecursiveTypeA.class;
 Class<?> erasedTypeB = RecursiveTypeB.class;
 TypeBindings bindings = new TypeBindings();
 this.recursiveTypeA = new ResolvedRecursiveType(erasedTypeA, bindings);
 this.recursiveTypeB = new ResolvedRecursiveType(erasedTypeB, bindings);
 this.selfReferenceType = new ResolvedRecursiveType(RecursiveTypeA.class, bindings);
 selfReferenceType.setReference(recursiveTypeA);
 }

 @Test
 public void testSetAndGetSelfReferencedType() {
 JavaType expectedSelfReferencedType = recursiveTypeA;
 assertEquals(expectedSelfReferencedType, selfReferenceType.getSelfReferencedType());
 selfReferenceType.setReference(recursiveTypeB);
 assertEquals(recursiveTypeB, selfReferenceType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSettingSelfReferencedTypeTwice() {
 selfReferenceType.setReference(recursiveTypeA);
 selfReferenceType.setReference(recursiveTypeB);
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder expectedResult = new StringBuilder();
 selfReferenceType.getGenericSignature(expectedResult);
 String result = expectedResult.toString();
 String expected = "[recursive type; " + RecursiveTypeA.class.getName() + "]";
 assertEquals(expected, result);
 }

 @Test
 public void testToString() {
 String expected = "[recursive type; " + RecursiveTypeA.class.getName() + "]";
 assertEquals(expected, selfReferenceType.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType selfReferenceTypeCopy = new ResolvedRecursiveType(RecursiveTypeA.class, bindings);
 selfReferenceTypeCopy.setReference(recursiveTypeA);
 assertTrue(selfReferenceType.equals(selfReferenceTypeCopy));
 }

 @Test
 public void testNotEqualsDifferentTypes() {
 ResolvedRecursiveType selfReferenceTypeCopy = new ResolvedRecursiveType(RecursiveTypeB.class, bindings);
 assertFalse(selfReferenceType.equals(selfReferenceTypeCopy));
 }

 @Test
 public void testNotEqualsNull() {
 assertFalse(selfReferenceType.equals(null));
 }

 @Test
 public void testNotEqualsUnresolvedType() {
 ResolvedRecursiveType unresolvedType = new ResolvedRecursiveType(RecursiveTypeA.class, bindings);
 assertFalse(selfReferenceType.equals(unresolvedType));
 }
}

class RecursiveTypeA {}

class RecursiveTypeB {}