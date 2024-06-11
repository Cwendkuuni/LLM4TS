package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.beans.PropertyDescriptor;
import java.util.List;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        final Configuration config = new BasicConfiguration();
        config.addAttribute("test.boolean", true);
        config.addAttribute("test.booleanArray", new boolean[]{true, false});
        config.addAttribute("test.byte", (byte) 1);
        config.addAttribute("test.byteArray", new byte[]{1, 2});
        config.addAttribute("test.char", 'a');
        config.addAttribute("test.charArray", new char[]{'a', 'b'});
        config.addAttribute("test.double", 1.0);
        config.addAttribute("test.doubleArray", new double[]{1.0, 2.0});
        config.addAttribute("test.float", 1.0f);
        config.addAttribute("test.floatArray", new float[]{1.0f, 2.0f});
        config.addAttribute("test.integer", 1);
        config.addAttribute("test.integerArray", new int[]{1, 2});
        config.addAttribute("test.long", 1L);
        config.addAttribute("test.longArray", new long[]{1L, 2L});
        config.addAttribute("test.short", (short) 1);
        config.addAttribute("test.shortArray", new short[]{1, 2});
        config.addAttribute("test.string", "foo");
        config.addAttribute("test.stringArray", new String[]{"foo", "bar"});
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);

        final Object booleanValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.boolean");
        assertTrue(booleanValue instanceof Boolean);
        assertEquals(booleanValue, true);

        final Object booleanArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.booleanArray");
        assertTrue(booleanArrayValue instanceof boolean[]);
        assertArrayEquals((boolean[])booleanArrayValue, new boolean[]{true, false});

        final Object byteValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.byte");
        assertTrue(byteValue instanceof Byte);
        assertEquals(byteValue, Byte.valueOf((byte) 1));

        final Object byteArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.byteArray");
        assertTrue(byteArrayValue instanceof byte[]);
        assertArrayEquals((byte[])byteArrayValue, new byte[]{1, 2});

        final Object charValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.char");
        assertTrue(charValue instanceof Character);
        assertEquals(charValue, Character.valueOf('a'));

        final Object charArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.charArray");
        assertTrue(charArrayValue instanceof char[]);
        assertArrayEquals((char[])charArrayValue, new char[]{'a', 'b'});

        final Object doubleValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.double");
        assertTrue(doubleValue instanceof Double);
        assertEquals(doubleValue, Double.valueOf(1.0));

        final Object doubleArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.doubleArray");
        assertTrue(doubleArrayValue instanceof double[]);
        assertArrayEquals((double[])doubleArrayValue, new double[]{1.0, 2.0});

        final Object floatValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.float");
        assertTrue(floatValue instanceof Float);
        assertEquals(floatValue, Float.valueOf(1.0f));

        final Object floatArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.floatArray");
        assertTrue(floatArrayValue instanceof float[]);
        assertArrayEquals((float[])floatArrayValue, new float[]{1.0f, 2.0f});

        final Object integerValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.integer");
        assertTrue(integerValue instanceof Integer);
        assertEquals(integerValue, Integer.valueOf(1));

        final Object integerArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.integerArray");
        assertTrue(integerArrayValue instanceof int[]);
        assertArrayEquals((int[])integerArrayValue, new int[]{1, 2});

        final Object longValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.long");
        assertTrue(longValue instanceof Long);
        assertEquals(longValue, Long.valueOf(1L));

        final Object longArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.longArray");
        assertTrue(longArrayValue instanceof long[]);
        assertArrayEquals((long[])longArrayValue, new long[]{1L, 2L});

        final Object shortValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.short");
        assertTrue(shortValue instanceof Short);
        assertEquals(shortValue, Short.valueOf((short) 1));

        final Object shortArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.shortArray");
        assertTrue(shortArrayValue instanceof short[]);
        assertArrayEquals((short[])shortArrayValue, new short[]{1, 2});

        final Object stringValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.string");
        assertTrue(stringValue instanceof String);
        assertEquals(stringValue, "foo");

        final Object stringArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.stringArray");
        assertTrue(stringArrayValue instanceof String[]);
        assertArrayEquals((String[])stringArrayValue, new String[]{"foo", "bar"});
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidProperty() throws Exception {
        final Configuration config = new BasicConfiguration();
        config.addAttribute("test.invalid", 1);
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
    }

    @Test
    public void testContextualize() throws Exception {
        final Context context = new BasicCheckstyleContext();
        context.putAttribute("test.boolean", true);
        context.putAttribute("test.booleanArray", new boolean[]{true, false});
        context.putAttribute("test.byte", (byte) 1);
        context.putAttribute("test.byteArray", new byte[]{1, 2});
        context.putAttribute("test.char", 'a');
        context.putAttribute("test.charArray", new char[]{'a', 'b'});
        context.putAttribute("test.double", 1.0);
        context.putAttribute("test.doubleArray", new double[]{1.0, 2.0});
        context.putAttribute("test.float", 1.0f);
        context.putAttribute("test.floatArray", new float[]{1.0f, 2.0f});
        context.putAttribute("test.integer", 1);
        context.putAttribute("test.integerArray", new int[]{1, 2});
        context.putAttribute("test.long", 1L);
        context.putAttribute("test.longArray", new long[]{1L, 2L});
        context.putAttribute("test.short", (short) 1);
        context.putAttribute("test.shortArray", new short[]{1, 2});
        final AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);

        final Object booleanValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.boolean");
        assertTrue(booleanValue instanceof Boolean);
        assertEquals(booleanValue, true);

        final Object booleanArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.booleanArray");
        assertTrue(booleanArrayValue instanceof boolean[]);
        assertArrayEquals((boolean[])booleanArrayValue, new boolean[]{true, false});

        final Object byteValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.byte");
        assertTrue(byteValue instanceof Byte);
        assertEquals(byteValue, Byte.valueOf((byte) 1));

        final Object byteArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.byteArray");
        assertTrue(byteArrayValue instanceof byte[]);
        assertArrayEquals((byte[])byteArrayValue, new byte[]{1, 2});

        final Object charValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.char");
        assertTrue(charValue instanceof Character);
        assertEquals(charValue, Character.valueOf('a'));

        final Object charArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.charArray");
        assertTrue(charArrayValue instanceof char[]);
        assertArrayEquals((char[])charArrayValue, new char[]{'a', 'b'});

        final Object doubleValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.double");
        assertTrue(doubleValue instanceof Double);
        assertEquals(doubleValue, Double.valueOf(1.0));

        final Object doubleArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.doubleArray");
        assertTrue(doubleArrayValue instanceof double[]);
        assertArrayEquals((double[])doubleArrayValue, new double[]{1.0, 2.0});

        final Object floatValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.float");
        assertTrue(floatValue instanceof Float);
        assertEquals(floatValue, Float.valueOf(1.0f));

        final Object floatArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.floatArray");
        assertTrue(floatArrayValue instanceof float[]);
        assertArrayEquals((float[])floatArrayValue, new float[]{1.0f, 2.0f});

        final Object integerValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.integer");
        assertTrue(integerValue instanceof Integer);
        assertEquals(integerValue, Integer.valueOf(1));

        final Object integerArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.integerArray");
        assertTrue(integerArrayValue instanceof int[]);
        assertArrayEquals((int[])integerArrayValue, new int[]{1, 2});

        final Object longValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.long");
        assertTrue(longValue instanceof Long);
        assertEquals(longValue, Long.valueOf(1L));

        final Object longArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.longArray");
        assertTrue(longArrayValue instanceof long[]);
        assertArrayEquals((long[])longArrayValue, new long[]{1L, 2L});

        final Object shortValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.short");
        assertTrue(shortValue instanceof Short);
        assertEquals(shortValue, Short.valueOf((short) 1));

        final Object shortArrayValue = FieldUtils.readField(bean, "configuration", true).getAttribute("test.shortArray");
        assertTrue(shortArrayValue instanceof short[]);
        assertArrayEquals((short[])shortArrayValue, new short[]{1, 2});
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidProperty() throws Exception {
        final Context context = new BasicCheckstyleContext();
        context.putAttribute("test.invalid", 1);
        final AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (expected instanceof byte[] && actual instanceof byte[]) {
            assertArrayEquals((byte[])expected, (byte[])actual);
        } else if (expected instanceof char[] && actual instanceof char[]) {
            assertArrayEquals((char[])expected, (char[])actual);
        } else if (expected instanceof int[] && actual instanceof int[]) {
            assertArrayEquals((int[])expected, (int[])actual);
        } else if (expected instanceof long[] && actual instanceof long[]) {
            assertArrayEquals((long[])expected, (long[])actual);
        } else if (expected instanceof boolean[] && actual instanceof boolean[]) {
            assertArrayEquals((boolean[])expected, (boolean[])actual);
        } else if (expected != null && actual != null) {
            assertEquals(expected.toString(), actual.toString());
        } else {
            assertEquals(expected, actual);
        }
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected instanceof List && actual instanceof List) {
            List expectedList = (List)expected;
            List actualList = (List)actual;
            assertEquals(expectedList.size(), actualList.size());
            for (int i = 0; i < expectedList.size(); i++) {
                assertEquals(expectedList.get(i), actualList.get(i));
            }
        } else {
            assertEquals(expected, actual);
        }
    }
}