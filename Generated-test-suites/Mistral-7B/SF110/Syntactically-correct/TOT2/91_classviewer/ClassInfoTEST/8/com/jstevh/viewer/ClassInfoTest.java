import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        assertEquals(false, classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        assertEquals(true, abstractClassInfo.isAbstract());
    }
}