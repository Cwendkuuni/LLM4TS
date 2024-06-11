
package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testEmptyConstructor() {
        JavaType referenceType = ReferenceType.construct(ReferenceType.class, TypeBindings.emptyBindings());
        assertNotNull(referenceType);
        assertNull(referenceType.getContentType());
    }

    @Test
    public void testConstructorWithArguments() {
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = JavaType.construct(Integer.class);
        JavaType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType, anchorType, null, null, false);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(Integer.class);
        JavaType refType = JavaType.construct(String.class);
        JavaType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(JavaType.construct(ReferenceType.class), referenceType.getRawType());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = JavaType.construct(Integer.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testUpgradeFromNullBaseType() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType.upgradeFrom(null, refType);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = JavaType.construct(Double.class);
        JavaType refType = JavaType.construct(String.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }
}