
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder.IDKey;
import org.apache.commons.lang3.reflect.ArrayUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class HashCodeBuilderTest {

    @Test
    public void testAppendEmptyBuilderAndEmptyObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object emptyObject = new Object();
        int hashCode = hcb.toHashCode();
        hcb.append(emptyObject);
        int hashCode2 = hcb.toHashCode();
        assertSame(hashCode, hashCode2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(null);
    }

    @Test
    public void testAppendArrayOfDifferentTypes() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(new Object[]{new Object(), new Byte((byte)1), new Short((short)3), new Integer(5), new Long(6), new Float(3.14f), new Double(3.14), new Boolean(true)});
        int hashCode = hcb.toHashCode();
        hcb.reset();
        hcb.append(new Object[]{new Object(), new Byte((byte)1), new Short((short)3), new Integer(5), new Long(6), new Float(3.14f), new Double(3.14), new Boolean(false)});
        int hashCode2 = hcb.toHashCode();
        assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendArrayWithSameTypeButDifferentValues() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(new Integer[]{1, 2, 3, 4, 5});
        int hashCode = hcb.toHashCode();
        hcb.reset();
        hcb.append(new Integer[]{2, 3, 4, 5, 6});
        int hashCode2 = hcb.toHashCode();
        assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendObjectWithSameHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object obj = new Object() {
            @Override
            public int hashCode() {
                return 1;
            }
        };
        hcb.append(obj);
        int hashCode = hcb.toHashCode();
        hcb.reset();
        hcb.append(obj);
        int hashCode2 = hcb.toHashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testBehaviorWithEmptyRegistry() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertNull(registry);
        hcb.append(new Object());
        int hashCode = hcb.toHashCode();
        hcb.reset();
        registry = HashCodeBuilder.getRegistry();
        assertNull(registry);
        assertEquals(hashCode, hcb.toHashCode());
    }

    @Test
    public void testBehaviorWithNonEmptyRegistry() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object obj1 = new Object();
        Object obj2 = new Object();
        HashCodeBuilder.register(obj1);
        HashCodeBuilder.register(obj2);
        hcb.append(new Object());
        int hashCode = hcb.toHashCode();
        hcb.reset();
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertSame(registry, HashCodeBuilder.getRegistry());
        assertSame(registry.size(), 2);
        assertEquals(hashCode, hcb.toHashCode());
    }

    @Test(expected = NullPointerException.class)
    public void testBehaviorWithNullRegistry() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        HashCodeBuilder.REGISTRY.set(null);
        hcb.append(new Object());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testBehaviorWithUnsynchronizedRegistry() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object obj = new Object();
        HashCodeBuilder.REGISTRY = new ThreadLocal<Set<IDKey>>() {};
        HashCodeBuilder.register(obj);
        hcb.append(new Object());
    }

    @Test
    public void testBehaviorWithInfiniteRecursion() {
        HashCodeBuilder hcb = new HashCodeBuilder() {
            @Override
            public void append(Object object) {
                super.append(object);
                this.append(this);
            }
        };
        hcb.append(new Object());
        try {
            hcb.toHashCode();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // Expected exception
        }
    }
}