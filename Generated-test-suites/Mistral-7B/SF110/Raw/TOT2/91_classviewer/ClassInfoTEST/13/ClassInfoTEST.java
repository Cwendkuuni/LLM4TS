import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.statusAbstract = true;
        assertTrue(classInfo.isAbstract());

        classInfo.statusAbstract = false;
        assertFalse(classInfo.isAbstract());
    }
}