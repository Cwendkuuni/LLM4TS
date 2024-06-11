package net.sourceforge.beanbin.reflect;

import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import static net.sourceforge.beanbin.reflect.ReflectionSearch.Conditional.*;

public class ReflectionSearchTest {

    @BeforeClass
    public static void setup() {
        // Initialize ReflectionShelf or any other static setup if necessary
    }

    @AfterClass
    public static void teardown() {
        // Perform any necessary cleanup or tear down
    }

    @Before
    public void beforeEach() {
        // Perform any necessary setup for each test
    }

    @After
    public void afterEach() {
        // Perform any necessary cleanup for each test
    }

    @Test
    public void testHasAnnotation() {
        Class<MyAnnotation> clazz = MyAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasAnnotationWithWildcard() {
        Class<MyAnnotation> clazz = MyAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
    }

    @Test
    public void testHasAnnotationFalse() {
        Class<Object> clazz = Object.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasAnnotationEmptyString() {
        Class<MyAnnotation> clazz = MyAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertFalse(reflectionSearch.hasAnnotation(""));
    }

    @Test(expected = BeanBinException.class)
    public void testHasAnnotationNull() {
        Class<Object> clazz = Object.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.hasAnnotation(null);
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        Class<MyClassWithMethod> clazz = MyClassWithMethod.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        List<Method> methods = reflectionSearch.methodsThatHave("myMethod").getMethods();
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        assertEquals("myMethod", method.getName());
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHaveEmptyQuery() throws BeanBinException {
        Class<MyClassWithMethod> clazz = MyClassWithMethod.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatDontHave("");
    }
}

class MyAnnotation {
}

class MyClassWithMethod {
    public void myMethod() {
    }
}