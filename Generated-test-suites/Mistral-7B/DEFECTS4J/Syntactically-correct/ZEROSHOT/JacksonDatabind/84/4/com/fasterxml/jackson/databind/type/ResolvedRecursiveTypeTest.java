package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveType() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 JavaType type2 = new ResolvedRecursiveType(Integer.class, null);
 JavaType type3 = new ResolvedRecursiveType(Double.class, null);

 // test constructor
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(Boolean.class, null);

 // test setReference method
 type1.setReference(type2);
 assertNull(type1.getSelfReferencedType());
 assertSame(type2, type1.getSelfReferencedType());

 // test getSelfReferencedType method
 type1.setReference(type3);
 assertSame(type3, type1.getSelfReferencedType());

 // test equals method
 ResolvedRecursiveType type5 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type6 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type7 = new ResolvedRecursiveType(Integer.class, null);
 ResolvedRecursiveType type8 = new ResolvedRecursiveType(Double.class, null);
 ResolvedRecursiveType type9 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type10 = new ResolvedRecursiveType(String.class, null);

 assertTrue(type1.equals(type1));
 assertFalse(type1.equals(type2));
 assertFalse(type1.equals(type3));
 assertFalse(type1.equals(type4));
 assertFalse(type1.equals(type5));
 assertFalse(type1.equals(type6));
 assertFalse(type1.equals(type7));
 assertFalse(type1.equals(type8));
 assertFalse(type1.equals(type9));
 assertFalse(type1.equals(type10));

 assertTrue(type1.equals(type9)); // test equality of equal objects
 assertFalse(type1.equals(new Object()));

 // test isContainerType method
 assertFalse(type1.isContainerType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType ref = new ResolvedRecursiveType(Integer.class, null);
 type.setReference(ref);
 type.setReference(new ResolvedRecursiveType(Double.class, null));
 }
}