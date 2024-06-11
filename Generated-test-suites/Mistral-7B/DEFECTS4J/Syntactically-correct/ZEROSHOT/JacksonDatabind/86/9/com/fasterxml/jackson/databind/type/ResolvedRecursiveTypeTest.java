package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class ResolvedRecursiveTypeTest {

private JavaType createMockedType() {
 return mock(JavaType.class);
}

@Test
public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType ref = createMockedType();
 type.setReference(ref);
 JavaType expectedRef = ref;
 assertSame("setReference should set referenced type", expectedRef, type.getSelfReferencedType());
}

@Test(expected = IllegalStateException.class)
public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType ref1 = createMockedType();
 type.setReference(ref1);
 JavaType ref2 = createMockedType();
 type.setReference(ref2);
}

@Test
public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 assertNull("getSelfReferencedType should return null for uninitialized type", type.getSelfReferencedType());
 JavaType ref = createMockedType();
 type.setReference(ref);
 assertSame("getSelfReferencedType should return set reference", ref, type.getSelfReferencedType());
}

@Test
public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType ref = createMockedType();
 type1.setReference(ref);
 JavaType ref2 = createMockedType();
 type2.setReference(ref2);
 assertFalse("unequal types should not be equal", type1.equals(type2));
 TypeFactory typeFactory = mock(TypeFactory.class);
 when(typeFactory.constructType(TypeFactory.DefaultBinding.ClassMap.instance(), Object.class, "String"))
 .thenReturn(createMockedType());
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 type3.setReference(typeFactory.constructType(TypeFactory.DefaultBinding.ClassMap.instance(), Object.class, "String"));
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 type4.setReference(type3);
 assertSame("equal types should be equal", type1, type4);
}
}