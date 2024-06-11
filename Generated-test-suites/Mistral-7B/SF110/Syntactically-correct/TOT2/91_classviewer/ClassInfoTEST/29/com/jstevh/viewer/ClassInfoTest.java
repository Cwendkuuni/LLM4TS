import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClassInfoTest {
    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        boolean actualResult = classInfo.isAbstract();
        boolean expectedResult = false;
        assertEquals(expectedResult, actualResult);
    }
}