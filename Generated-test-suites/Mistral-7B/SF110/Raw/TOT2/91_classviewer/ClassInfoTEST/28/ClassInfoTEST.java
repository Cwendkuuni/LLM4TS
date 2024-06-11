import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {
    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.AbstractClass"); // an abstract class
        assertTrue(classInfo.isAbstract());

        ClassInfo classInfo2 = new ClassInfo("java.lang.String"); // a non-abstract class
        assertFalse(classInfo2.isAbstract());
    }
}