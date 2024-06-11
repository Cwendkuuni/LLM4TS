import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

    private UnsafeAllocator allocator = UnsafeAllocator.create();

    @Test
    public void testNewInstanceObject() throws Exception {
        Object o = allocator.newInstance(Object.class);
        assertNotNull(o);
        assertEquals(o.getClass(), Object.class);
    }

    @Test
    public void testNewInstanceClassWithInterface() throws Exception {
        try {
            allocator.newInstance(Runnable.class);
            assert false; // should not reach this point
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceClassWithAbstractMethod() throws Exception {
        try {
            allocator.newInstance(AbstractClassWithMethod.class);
            assert false; // should not reach this point
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceClassWithConstructor() throws Exception {
        ConstructorClassWithPublicConstructor c = new ConstructorClassWithPublicConstructor();
        ConstructorClassWithPublicConstructor instance = (ConstructorClassWithPublicConstructor) allocator.newInstance(c.getClass());
        assertEquals(instance.value, "created by unsafe allocator");
    }

    @Test
    public void testNewInstanceClassWithPrivateConstructor() throws Exception {
        ConstructorClassWithPrivateConstructor c = new ConstructorClassWithPrivateConstructor();
        ConstructorClassWithPrivateConstructor instance = (ConstructorClassWithPrivateConstructor) allocator.newInstance(c.getClass());
        assertEquals(instance.value, "created by unsafe allocator");
        assertFalse(Modifier.isPublic(instance.getClass().getModifiers()));
    }

    @Test
    public void testNewInstanceClassWithProtectedConstructor() throws Exception {
        ConstructorClassWithProtectedConstructor c = new ConstructorClassWithProtectedConstructor();
        ConstructorClassWithProtectedConstructor instance;
        try {
            instance = (ConstructorClassWithProtectedConstructor) allocator.newInstance(c.getClass());
            assert false; // should not reach this point
        } catch (InvocationTargetException e) {
            instance = (ConstructorClassWithProtectedConstructor) e.getCausedByException();
        }
        assertEquals(instance.value, "created by unsafe allocator");
        assertFalse(Modifier.isPublic(instance.getClass().getModifiers()));
        assertInstanceof(ConstructorClassWithProtectedConstructor.class, instance);
    }

    private static class AbstractClassWithMethod {
        public abstract void doSomething();
    }

    private static class ConstructorClassWithPublicConstructor {
        public String value;
        public ConstructorClassWithPublicConstructor() {
            this.value = "created by constructor";
        }
    }

    private static class ConstructorClassWithPrivateConstructor {
        private String value;
        private ConstructorClassWithPrivateConstructor() {
            this.value = "created by constructor";
        }
        public String getValue() {
            return value;
        }
    }

    private static class ConstructorClassWithProtectedConstructor {
        protected String value;
        private ConstructorClassWithProtectedConstructor() {
            this.value = "created by constructor";
        }
    }
}