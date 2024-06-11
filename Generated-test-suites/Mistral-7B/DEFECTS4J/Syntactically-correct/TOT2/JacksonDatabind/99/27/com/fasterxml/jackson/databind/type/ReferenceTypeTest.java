package com.example;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ReferenceTypeTest {

    @Test
    public void testWithContentType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = Mockito.mock(JavaType.class);
        JavaType anchorType = Mockito.mock(JavaType.class);
        Object valueHandler = Mockito.mock(Object.class);
        Object typeHandler = Mockito.mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, false);
        JavaType newContentType = Mockito.mock(JavaType.class);

        ReferenceType newReferenceType = referenceType.withContentType(newContentType);

        Assert.assertNotSame(referenceType, newReferenceType);
        Assert.assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testWithContentTypeNull() {
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], null, null, null, null, false);
        Assert.assertThrows(IllegalArgumentException.class, () -> referenceType.withContentType(null));
    }

    @Test
    public void testWithContentTypeEmptyType() {
        JavaType emptyType = Mockito.mock(JavaType.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], null, null, null, null, false);
        Assert.assertThrows(IllegalArgumentException.class, () -> referenceType.withContentType(emptyType));
    }

    @Test
    public void testWithContentTypeDifferentType() {
        JavaType differentType = Mockito.mock(JavaType.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], null, null, null, null, false);
        Assert.assertNotSame(referenceType, referenceType.withContentType(differentType));
        Assert.assertFalse(referenceType.isReferenceType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<List<String>> listClass = ArrayList.class;
        JavaType baseType = Mockito.mock(JavaType.class);
        JavaType refdType = Mockito.mock(JavaType.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        Assert.assertEquals(listClass, referenceType.getRawClass());
        Assert.assertEquals(refdType, referenceType.getReferencedType());
    }

    @Test
    public void testUpgradeFromNull() {
        JavaType baseType = Mockito.mock(JavaType.class);
        Assert.assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(baseType, null));
    }

    @Test
    public void testUpgradeFromEmptyType() {
        JavaType baseType = Mockito.mock(JavaType.class);
        Assert.assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(baseType, Mockito.mock(JavaType.class)));
    }

    @Test
    public void testUpgradeFromDifferentTypes() {
        JavaType baseType = Mockito.mock(JavaType.class);
        JavaType refdType = Mockito.mock(JavaType.class);
        Assert.assertNotEquals(baseType, refdType);
        Assert.assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(baseType, refdType));
    }
}