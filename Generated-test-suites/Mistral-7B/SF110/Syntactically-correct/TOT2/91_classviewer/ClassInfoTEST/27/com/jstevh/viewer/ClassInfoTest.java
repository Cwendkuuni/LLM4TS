
package com.example.junit4test;

import org.junit.*;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        // Test case 1: Basic functionality
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertFalse(classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(abstractClassInfo.isAbstract());

        // Test case 2: Null ClassInfo instance
        ClassInfo nullClassInfo = null;
        assertFalse(nullClassInfo.isAbstract());

        // Test case 3: Null class name
        try {
            new ClassInfo(null);
        } catch (Exception e) {
            assertTrue(true);
        }

        // Test case 4: Class that is not abstract
        ClassInfo stringClassInfo = new ClassInfo("java.lang.String");
        assertFalse(stringClassInfo.isAbstract());

        // Test case 5: Abstract class
        ClassInfo abstractListClassInfo = new ClassInfo("java.util.AbstractList");
        assertTrue(abstractListClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        // Test case 1: Basic functionality
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertFalse(classInfo.isInterface());

        ClassInfo listClassInfo = new ClassInfo("java.util.List");
        assertTrue(listClassInfo.isInterface());

        // Test case 2: Null ClassInfo instance
        ClassInfo nullClassInfo = null;
        assertFalse(nullClassInfo.isInterface());

        // Test case 3: Null class name
        try {
            new ClassInfo(null);
        } catch (Exception e) {
            assertTrue(true);
        }

        // Test case 4: Class that is an interface
        ClassInfo listInterfaceClassInfo = new ClassInfo("java.util.List");
        assertTrue(listInterfaceClassInfo.isInterface());

        // Test case 5: Class that is not an interface
        ClassInfo objectClassInfo = new ClassInfo("java.lang.Object");
        assertFalse(objectClassInfo.isInterface());

        // Test case 6: Abstract class
        ClassInfo abstractListClassInfo = new ClassInfo("java.util.AbstractList");
        assertTrue(abstractListClassInfo.isAbstract());
        assertFalse(abstractListClassInfo.isInterface());
    }

    @Test
    public void testGetClassName() {
        // Test case 1: Basic functionality
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        assertEquals("java.lang.Object", classInfo.getClassName());

        ClassInfo customClassInfo = new ClassInfo("com.example.MyCustomClass");
        assertEquals("com.example.MyCustomClass", classInfo.getClassName());

        // Test case 2: Null ClassInfo instance
        ClassInfo nullClassInfo = null;
        assertNull(nullClassInfo.getClassName());

        // Test case 3: Empty string as class name
        ClassInfo emptyClassNameInfo = new ClassInfo("");
        assertNull(emptyClassNameInfo.getClassName());

        // Test case 4: Invalid class name
        ClassInfo invalidClassNameInfo = new ClassInfo("java.lang.Object$");
        assertNull(invalidClassNameInfo.getClassName());

        // Test case 5: Class name with special characters
        ClassInfo specialClassNameInfo = new ClassInfo("java.util.ArrayList@123");
        assertEquals("java.util.ArrayList", classInfo.getClassName());

        // Test case 6: Class name with package name
        ClassInfo packageClassNameInfo = new ClassInfo("com.example.MyPackage.MyClass");
        assertEquals("com.example.MyPackage.MyClass", classInfo.getClassName());

        // Test case 7: Class name with long name
        ClassInfo longClassNameInfo = new ClassInfo("com.example.MyPackage.MyClassWithAVeryLongNameThatIsLongerThanTheMaximumLengthAllowedByJVM");
        assertEquals("com.example.MyPackage.MyClassWithAVeryLongNameThatIsLongerThanTheMaximumLengthAllowedByJVM", classInfo.getClassName());
    }
}
