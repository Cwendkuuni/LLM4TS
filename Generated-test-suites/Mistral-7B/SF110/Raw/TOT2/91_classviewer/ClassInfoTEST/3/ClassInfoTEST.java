import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertTrue(classInfo.isAbstract());

        ClassInfo classInfo2 = new ClassInfo("java.lang.StringBuilder");
        assertFalse(classInfo2.isAbstract());
    }
}