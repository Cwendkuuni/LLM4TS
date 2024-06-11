import org.junit.Test;
import static org.junit.Assert.*;

public class ClassInfoTest {

    @Test
    public void testIsAbstract() {
        ClassInfo classInfo = new ClassInfo("com.jstevh.viewer.ClassInfo");
        assertFalse(classInfo.isAbstract());

        ClassInfo abstractClassInfo = new ClassInfo("java.lang.AbstractClass");
        assertTrue(abstractClassInfo.isAbstract());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testIsAbstractThrowsClassNotFoundException() {
        new ClassInfo("InvalidClassName");
    }

    @Test
    public void testIsInterface() {
        ClassInfo interfaceClassInfo = new ClassInfo("java.util.ArrayList");
        assertFalse(interfaceClassInfo.isInterface());

        ClassInfo interfaceClassInfo2 = new ClassInfo("java.util.List");
        assertTrue(interfaceClassInfo2.isInterface());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testIsInterfaceThrowsClassNotFoundException() {
        new ClassInfo("InvalidInterfaceName");
    }

    // Add other test methods for other methods in the ClassInfo class

    @Test
    public void testPrintMethods() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] methods = classInfo.printMethods();
        assertNotNull(methods);
    }

    @Test(expected = NullPointerException.class)
    public void testPrintMethodsNullPointerException() {
        new ClassInfo();
        String[] methods = null;
        classInfo.printMethods(methods);
    }

    @Test
    public void testPrintMethodsEmptyArray() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String[] methods = classInfo.printMethods();
        assertEquals(0, methods.length);
    }

    // Add other test methods for other methods in the ClassInfo class

    @Test
    public void testPrintInterfaces() {
        ClassInfo classInfo = new ClassInfo("java.util.ArrayList");
        String[] interfaces = classInfo.printInterfaces();
        assertArrayEquals(new String[]{"java.lang.Iterator", "java.util.RandomAccess", "java.io.Serializable"}, interfaces);
    }

    @Test(expected = NullPointerException.class)
    public void testPrintInterfacesNullPointerException() {
        new ClassInfo();
        String[] interfaces = null;
        classInfo.printInterfaces();
    }

    @Test
    public void testPrintInterfacesEmptyArray() {
        ClassInfo classInfo = new ClassInfo("java.lang.Object");
        String[] interfaces = classInfo.printInterfaces();
        assertEquals(0, interfaces.length);
    }

    // Add other test methods for other methods in the ClassInfo class

}