import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract_WhenObjectIsAbstract_ThenReturnTrue() {
        ClassInfo classInfo = new ClassInfo("java.lang.AbstractClass");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsAbstract_WhenObjectIsNotAbstract_ThenReturnFalse() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertFalse(classInfo.isAbstract());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testIsAbstract_WhenInvalidClassName_ThenThrowException() {
        new ClassInfo("java.NonexistentClass");
    }
}