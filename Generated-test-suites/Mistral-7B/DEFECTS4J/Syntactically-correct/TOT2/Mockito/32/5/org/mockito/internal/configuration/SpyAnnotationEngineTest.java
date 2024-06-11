import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.MockitoAnnotations;
import org.reflections.Reflections;
import org.mockito.exceptions.Reporter;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine engine;
    private Object testClass;

    @Before
    public void setUp() {
        testClass = new Object() {};
        engine = new SpyAnnotationEngine();
        MockitoAnnotations.initMocks(testClass);
    }

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        assertNull(engine.createMockFor(null, null));
        assertNull(engine.createMockFor(new Annotation(){}, null));
    }

    @Test
    public void testProcess() {
        Field field = Reflections.getFields(testClass.getClass()).findField("fieldName");
        assertNull(field);

        engine.process(testClass.getClass(), testClass);

        Field spiedField = Reflections.getFields(testClass.getClass()).findField("fieldName");
        assertNotNull(spiedField);
        assertNotNull(spiedField.get(testClass));
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithAnnotationPresentButInstanceMissing() {
        Field field = Reflections.getFields(testClass.getClass()).findField("fieldName");
        field.setAccessible(true);
        field.set(testClass, new Object());
        engine.process(testClass.getClass(), testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithMockInstance() {
        Field field = Reflections.getFields(testClass.getClass()).findField("fieldName");
        field.setAccessible(true);
        field.set(testClass, Mockito.mock(String.class));
        engine.process(testClass.getClass(), testClass);
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcessWithIllegalAccessException() {
        Field field = Reflections.getFields(testClass.getClass()).findField("fieldName2");
        field.setAccessible(true);
        engine.process(testClass.getClass(), testClass);
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        new Annotation[]{Mockito.Mock.class, Captor.class}.forEach(annotation -> {
            Field field = new Object() {}.getClass().getDeclaredField("field");
            engine.assertNoAnnotations(Spy.class, field, annotation.getClass());
        });
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotationsWithAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Object() {
            @Spy
            public void test() {
            }
        }.getClass().getDeclaredField("field");
        engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class);
    }

    @Test
    public void testAssertNoAnnotationsNoExceptionThrown() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Object() {}.getClass().getDeclaredField("field");
        engine.assertNoAnnotations(Spy.class, field, Annotation[].class);
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotationsWithAnnotationsPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Object() {
            @Spy
            @Captor
            public void test() {
            }
        }.getClass().getDeclaredField("field");
        engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class);
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotationsWithUnknownAnnotation() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Object() {}.getClass().getDeclaredField("field");
        engine.assertNoAnnotations(Spy.class, field, new Annotation(){}.getClass());
    }
}