import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstractTrue() {
        ClassInfo classInfo = new ClassInfo("java.lang.AbstractClass");
        assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsAbstractFalse() {
        ClassInfo classInfo = new ClassInfo("java.lang.ConcreteClass");
        assertFalse(classInfo.isAbstract());
    }
}