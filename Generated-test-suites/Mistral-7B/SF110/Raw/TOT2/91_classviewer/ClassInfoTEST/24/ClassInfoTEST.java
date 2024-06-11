import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        // Test for abstract class
        assertTrue(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.StringBuilder");
        // Test for non-abstract class
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.util.ArrayList");
        // Test for interface
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo("java.lang.Object");
        // Test for Object class
        assertFalse(classInfo.isAbstract());

        classInfo = new ClassInfo(null);
        // Test for null argument
        assertNull(classInfo);

        classInfo = new ClassInfo("com.non.existent.Class");
        // Test for non-existent class
        assertNull(classInfo);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundException() {
        new ClassInfo("com.jstevh.viewer.NonExistentClass");
    }
}