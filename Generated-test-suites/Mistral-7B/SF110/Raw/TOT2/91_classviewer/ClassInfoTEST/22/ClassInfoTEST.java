import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractClass");
        assertTrue(abstractClassInfo.isAbstract());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        String className = classInfo.getClassName();
        assertNotNull(className);
        assertEquals("java.lang.Class", className);
    }

    // Add test cases for all other methods

    @Test
    public void testPrintMethodsWithEmptyString() {
        ClassInfo classInfo = new ClassInfo();
        String[] result = classInfo.printMethods("");
        assertNull(result);
    }

    // Add test cases for all other edge cases and exception handling scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testGetFoundMethodWithInvalidIndex() {
        String[] methods = new String[]{"method1", "method2", "method3"};
        ClassInfo classInfo = new ClassInfo();
        classInfo.cMethods = methods;
        classInfo.fndMethods = new String[3];

        classInfo.getFoundMethod(3);
    }

    // Add test cases for all other methods with edge cases and exception handling scenarios

    @Test
    public void testGetSuperClassNameForObjectClass() {
        ClassInfo classInfo = new ClassInfo();
        String superClassName = classInfo.getSuperClassName();
        assertNull(superClassName);
    }

    @Test
    public void testGetSuperClassNameForInterface() {
        ClassInfo classInfo = new ClassInfo("java.lang.Interface");
        String superClassName = classInfo.getSuperClassName();
        assertNull(superClassName);
    }

    @Test
    public void testGetSuperClassNameForClassWithSuperclass() {
        ClassInfo classInfo = new ClassInfo("java.lang.SuperClass");
        String superClassName = classInfo.getSuperClassName();
        assertEquals("java.lang.SuperSuperClass", superClassName);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSuperClassNameWithNullClassInfo() {
        String superClassName = new ClassInfo().getSuperClassName();
    }

    // Add test cases for all other methods
}