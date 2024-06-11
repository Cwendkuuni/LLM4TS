import static org.junit.Assert.*;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {
    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.AbstractClass"); // AbstractClass is an abstract class
        assertTrue(classInfo.isAbstract());

        ClassInfo classInfo2 = new ClassInfo("java.lang.String"); // String is not an abstract class
        assertFalse(classInfo2.isAbstract());
    }
}