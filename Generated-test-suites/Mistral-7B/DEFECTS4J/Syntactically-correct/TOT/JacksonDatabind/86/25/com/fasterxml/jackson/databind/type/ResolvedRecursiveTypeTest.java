package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType originalType = new ResolvedRecursiveType(Object.class, null);
 JavaType referencedType = new ResolvedRecursiveType(String.class, null);

 originalType.setReference(referencedType);
 ResolvedRecursiveType testedType = (ResolvedRecursiveType) originalType;
 JavaType selfReferencedType = testedType.getSelfReferencedType();

 assertEquals(String.class, selfReferencedType.getRawClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType originalType = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = new ResolvedRecursiveType(String.class, null);
 JavaType ref2 = new ResolvedRecursiveType(Integer.class, null);

 originalType.setReference(ref1);
 originalType.setReference(ref2);
 }
}