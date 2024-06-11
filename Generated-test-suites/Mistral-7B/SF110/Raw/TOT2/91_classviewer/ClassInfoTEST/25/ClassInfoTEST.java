import static org.junit.Assert.assertEquals;
import com.jstevh.viewer.ClassInfo;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        boolean expected = false;
        boolean actual = classInfo.isAbstract();
        assertEquals(expected, actual);

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        expected = true;
        actual = classInfo.isAbstract();
        assertEquals(expected, actual);
    }
}