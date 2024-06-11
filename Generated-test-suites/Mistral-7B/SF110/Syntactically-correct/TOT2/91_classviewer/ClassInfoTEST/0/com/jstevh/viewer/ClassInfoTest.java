import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.lang.reflect.*;
import java.io.File;
import static com.jstevh.viewer.ClassInfo.*;

public class ClassInfoTest {
    

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(abstractClassInfo.isAbstract());
    }

    @Test
    public void testIsInterface() {
        ClassInfo classInfo = new ClassInfo();
        assertFalse(classInfo.isInterface());

        ClassInfo stringInterface = new ClassInfo("java.lang.String");
        assertTrue(stringInterface.isInterface());
    }

    @Test
    public void testGetClassName() {
        ClassInfo classInfo = new ClassInfo();
        assertEquals(classInfo.getClassName(), "com.jstevh.viewer.ClassInfo");

        ClassInfo stringClass = new ClassInfo("java.lang.String");
        assertEquals(stringClass.getClassName(), "java.lang.String");
    }

    @Test
    public void testGetClassPackage() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getClassPackage());

        ClassInfo stringClass = new ClassInfo("java.lang.String");
        assertEquals(stringClass.getClassPackage(), "java.lang");
    }

    @Test
    public void testGetSuperClassName() {
        ClassInfo classInfo = new ClassInfo();
        assertNull(classInfo.getSuperClassName());

        ClassInfo stringClass = new ClassInfo("java.lang.String");
        assertEquals(stringClass.getSuperClassName(), "object");
    }

    @Test(expected = ClassNotFoundException.class)
    public void testInvalidClassName() {
        new ClassInfo("java.not.a.class");
    }

    @Test
    public void testPrintFields() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        String[] fields = classInfo.printFields();
        assertNotNull(fields);
    }

    // Add test cases for other methods here

    
}