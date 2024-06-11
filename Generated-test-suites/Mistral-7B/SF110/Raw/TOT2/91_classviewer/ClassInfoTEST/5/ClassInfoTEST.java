import org.junit.Assert;
import org.junit.Test;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.AbstractClass");
        Assert.assertTrue(classInfo.isAbstract());
    }

    @Test
    public void testIsNotAbstract() {
        ClassInfo classInfo = new ClassInfo("java.lang.String");
        Assert.assertFalse(classInfo.isAbstract());
    }
}