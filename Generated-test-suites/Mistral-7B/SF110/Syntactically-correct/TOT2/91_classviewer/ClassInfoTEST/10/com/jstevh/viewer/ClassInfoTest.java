import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        boolean expected = false;
        boolean actual = classInfo.isAbstract();
        assertEquals(expected, actual);

        classInfo = new ClassInfo("java.lang.AbstractStringBuilder");
        expected = true;
        actual = classInfo.isAbstract();
        assertEquals(expected, actual);
    }
}