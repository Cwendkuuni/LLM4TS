package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
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
import org.apache.commons.beanutils.propertyeditors.RelaxedStringArrayEditor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;
    private BeanUtilsBean beanUtilsBean;
    private ConvertUtilsBean convertUtilsBean;

    @Before
    public void setUp() throws Exception {
        automaticBean = new AutomaticBean();
        configuration = new Configuration();
        beanUtilsBean = new BeanUtilsBean();
        convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new BooleanConverter(), Boolean.TYPE);
        convertUtilsBean.register(new BooleanConverter(), Boolean.class);
        convertUtilsBean.register(new BooleanConverter(), boolean[].class);
        convertUtilsBean.register(new ByteConverter(), Byte.TYPE);
        convertUtilsBean.register(new ByteConverter(), Byte.class);
        convertUtilsBean.register(new ByteConverter(), byte[].class);
        convertUtilsBean.register(new CharacterConverter(), Character.TYPE);
        convertUtilsBean.register(new CharacterConverter(), Character.class);
        convertUtilsBean.register(new CharacterConverter(), char[].class);
        convertUtilsBean.register(new DoubleConverter(), Double.TYPE);
        convertUtilsBean.register(new DoubleConverter(), Double.class);
        convertUtilsBean.register(new DoubleConverter(), double[].class);
        convertUtilsBean.register(new FloatConverter(), Float.TYPE);
        convertUtilsBean.register(new FloatConverter(), Float.class);
        convertUtilsBean.register(new FloatConverter(), float[].class);
        convertUtilsBean.register(new IntegerConverter(), Integer.TYPE);
        convertUtilsBean.register(new IntegerConverter(), Integer.class);
        convertUtilsBean.register(new IntegerConverter(), int[].class);
        convertUtilsBean.register(new LongConverter(), Long.TYPE);
        convertUtilsBean.register(new LongConverter(), Long.class);
        convertUtilsBean.register(new LongConverter(), long[].class);
        convertUtilsBean.register(new ShortConverter(), Short.TYPE);
        convertUtilsBean.register(new ShortConverter(), Short.class);
        convertUtilsBean.register(new ShortConverter(), short[].class);
        convertUtilsBean.register(new RelaxedStringArrayConverter(), String[].class);

        configuration.addAttribute("booleanAttribute", true);
        configuration.addAttribute("byteAttribute", (byte) 5);
        configuration.addAttribute("shortAttribute", (short) 10);
        configuration.addAttribute("intAttribute", 15);
        configuration.addAttribute("longAttribute", 20L);
        configuration.addAttribute("floatAttribute", 25.0f);
        configuration.addAttribute("doubleAttribute", 30.0);
        configuration.addAttribute("booleanArrayAttribute", new boolean[] { true, false });
        configuration.addAttribute("byteArrayAttribute", new byte[] { 1, 2 });
        configuration.addAttribute("shortArrayAttribute", new short[] { 3, 4 });
        configuration.addAttribute("intArrayAttribute", new int[] { 5, 6 });
        configuration.addAttribute("longArrayAttribute", new long[] { 7, 8 });
        configuration.addAttribute("floatArrayAttribute", new float[] { 9, 10 });
        configuration.addAttribute("doubleArrayAttribute", new double[] { 11, 12 });
        configuration.addAttribute("stringArrayAttribute", new String[] { "A", "B" });
        configuration.addChild(new Configuration()); // valid configuration
        configuration.addChild(new Object()); // invalid configuration
    }

    @Test
    public void testAutomaticBeanConfiguration() throws Exception {
        automaticBean.configure(configuration);

        Configuration configuration = automaticBean.getConfiguration();
        assertEquals("expected configuration", configuration, automaticBean.getConfiguration());

        // test boolean property
        assertTrue("expected boolean property to be true", automaticBean.isBooleanProperty());
        assertFalse("expected boolean property to be false", automaticBean.isBooleanProperty());
        assertEquals("expected boolean property value", true, automaticBean.getBooleanProperty());
        assertEquals("expected boolean property setter", "booleanProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "booleanProperty"));
        BeanUtilsBean.copyProperty(automaticBean, "booleanProperty", false);
        assertFalse("expected boolean property to be false", automaticBean.isBooleanProperty());
        assertEquals("expected boolean property value", false, automaticBean.getBooleanProperty());

        // test byte property
        assertEquals("expected byte property value", (byte) 5, automaticBean.getByteProperty());
        assertEquals("expected byte property setter", "byteProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "byteProperty"));
        BeanUtilsBean.copyProperty(automaticBean, "byteProperty", (byte) 10);
        assertEquals("expected byte property value", (byte) 10, automaticBean.getByteProperty());

        // test short property
        assertEquals("expected short property value", (short) 10, automaticBean.getShortProperty());
        assertEquals("expected short property setter", "shortProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "shortProperty"));
        BeanUtilsBean.copyProperty(automaticBean, "shortProperty", (short) 20);
        assertEquals("expected short property value", (short) 20, automaticBean.getShortProperty());

        // test int property
        assertEquals("expected int property value", 15, automaticBean.getIntProperty());
        assertEquals("expected int property setter", "intProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "intProperty"));
        BeanUtilsBean.copyProperty(automaticBean, "intProperty", 25);
        assertEquals("expected int property value", 25, automaticBean.getIntProperty());

        // test long property
        assertEquals("expected long property value", 20L, automaticBean.getLongProperty());
        assertEquals("expected long property setter", "longProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "longProperty"));
        BeanUtilsBean.copyProperty(automaticBean, "longProperty", 30L);
        assertEquals("expected long property value", 30L, automaticBean.getLongProperty());

        // test float property
        assertEquals("expected float property value", 25.0f, automaticBean.getFloatProperty(), 0.01);
        assertEquals("expected float property setter", "floatProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "floatProperty"));
        BeanUtilsBean.copyProperty(automaticBean, "floatProperty", 30.0f);
        assertEquals("expected float property value", 30.0f, automaticBean.getFloatProperty(), 0.01);

        // test double property
        assertEquals("expected double property value", 30.0, automaticBean.getDoubleProperty(), 0.01);
        assertEquals("expected double property setter", "doubleProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "doubleProperty"));
        BeanUtilsBean.copyProperty(automaticBean, "doubleProperty", 40.0);
        assertEquals("expected double property value", 40.0, automaticBean.getDoubleProperty(), 0.01);

        // test boolean array property
        Object booleanArrayProperty = automaticBean.getBooleanArrayProperty();
        assertArrayEquals("expected boolean array property value", new boolean[] { true, false }, booleanArrayProperty);
        assertEquals("expected boolean array property setter", "booleanArrayProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "booleanArrayProperty"));
        Object newBooleanArrayProperty = new boolean[] { false, true };
        BeanUtilsBean.copyProperty(automaticBean, "booleanArrayProperty", newBooleanArrayProperty);
        assertArrayEquals("expected boolean array property value", new boolean[] { false, true }, automaticBean.getBooleanArrayProperty());

        // test byte array property
        Object byteArrayProperty = automaticBean.getByteArrayProperty();
        assertArrayEquals("expected byte array property value", new byte[] { 1, 2 }, byteArrayProperty);
        assertEquals("expected byte array property setter", "byteArrayProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "byteArrayProperty"));
        Object newByteArrayProperty = new byte[] { 3, 4 };
        BeanUtilsBean.copyProperty(automaticBean, "byteArrayProperty", newByteArrayProperty);
        assertArrayEquals("expected byte array property value", new byte[] { 3, 4 }, automaticBean.getByteArrayProperty());

        // test short array property
        Object shortArrayProperty = automaticBean.getShortArrayProperty();
        assertArrayEquals("expected short array property value", new short[] { 3, 4 }, shortArrayProperty);
        assertEquals("expected short array property setter", "shortArrayProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "shortArrayProperty"));
        Object newShortArrayProperty = new short[] { 5, 6 };
        BeanUtilsBean.copyProperty(automaticBean, "shortArrayProperty", newShortArrayProperty);
        assertArrayEquals("expected short array property value", new short[] { 5, 6 }, automaticBean.getShortArrayProperty());

        // test int array property
        Object intArrayProperty = automaticBean.getIntArrayProperty();
        assertArrayEquals("expected int array property value", new int[] { 5, 6 }, intArrayProperty);
        assertEquals("expected int array property setter", "intArrayProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "intArrayProperty"));
        Object newIntArrayProperty = new int[] { 7, 8 };
        BeanUtilsBean.copyProperty(automaticBean, "intArrayProperty", newIntArrayProperty);
        assertArrayEquals("expected int array property value", new int[] { 7, 8 }, automaticBean.getIntArrayProperty());

        // test long array property
        Object longArrayProperty = automaticBean.getLongArrayProperty();
        assertArrayEquals("expected long array property value", new long[] { 7, 8 }, longArrayProperty);
        assertEquals("expected long array property setter", "longArrayProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "longArrayProperty"));
        Object newLongArrayProperty = new long[] { 9, 10 };
        BeanUtilsBean.copyProperty(automaticBean, "longArrayProperty", newLongArrayProperty);
        assertArrayEquals("expected long array property value", new long[] { 9, 10 }, automaticBean.getLongArrayProperty());

        // test float array property
        Object floatArrayProperty = automaticBean.getFloatArrayProperty();
        assertArrayEquals("expected float array property value", new float[] { 9, 10 }, floatArrayProperty);
        assertEquals("expected float array property setter", "floatArrayProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "floatArrayProperty"));
        Object newFloatArrayProperty = new float[] { 11, 12 };
        BeanUtilsBean.copyProperty(automaticBean, "floatArrayProperty", newFloatArrayProperty);
        assertArrayEquals("expected float array property value", new float[] { 11, 12 }, automaticBean.getFloatArrayProperty());

        // test double array property
        Object doubleArrayProperty = automaticBean.getDoubleArrayProperty();
        assertArrayEquals("expected double array property value", new double[] { 11, 12 }, doubleArrayProperty);
        assertEquals("expected double array property setter", "doubleArrayProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "doubleArrayProperty"));
        Object newDoubleArrayProperty = new double[] { 13, 14 };
        BeanUtilsBean.copyProperty(automaticBean, "doubleArrayProperty", newDoubleArrayProperty);
        assertArrayEquals("expected double array property value", new double[] { 13, 14 }, automaticBean.getDoubleArrayProperty());

        // test string array property
        Object stringArrayProperty = automaticBean.getStringArrayProperty();
        assertArrayEquals("expected string array property value", new String[] { "A", "B" }, stringArrayProperty);
        assertEquals("expected string array property setter", "stringArrayProperty", PropertyUtils.getPropertyName(AutomaticBean.class, "stringArrayProperty"));
        Object newStringArrayProperty = new String[] { "C", "D" };
        BeanUtilsBean.copyProperty(automaticBean, "stringArrayProperty", newStringArrayProperty);
        assertArrayEquals("expected string array property value", new String[] { "C", "D" }, automaticBean.getStringArrayProperty());

        // test child configurations
        assertEquals("expected number of child configurations", 1, automaticBean.getConfiguration().size());
        Configuration childConfiguration = automaticBean.getConfiguration().get(0);
        Configuration childConfigurationCopy = BeanUtils.cloneBean(childConfiguration);
        assertNotSame("expected different child configurations", childConfiguration, childConfigurationCopy);
        assertEquals("expected child configuration name", "childConfiguration", childConfiguration.getName());

        // test invalid child configuration
        try {
            automaticBean.setupChild(new Object());
            fail("expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertEquals("expected null", actual);
        } else if (expected.getClass().isArray()) {
            assertArrayEquals("expected", (Object[]) expected, (Object[]) actual);
        } else {
            assertEquals("expected", expected, actual);
        }
    }

    private void assertArrayEquals(String message, Object expected, Object actual) {
        if (expected == null) {
            assertEquals(message, null, actual);
        } else if (expected.getClass().isArray()) {
            assertArrayEquals(message, (Object[]) expected, (Object[]) actual);
        } else {
            assertEquals(message, expected, actual);
        }
    }

    private void assertArrayEquals(String message, Object[] expected, Object[] actual) {
        if (expected == null) {
            assertArrayEquals(message, null, actual);
        } else {
            int length = expected.length;
            assertArrayEquals(message, length, actual.length);
            for (int i = 0; i < length; i++) {
                assertEquals(message, expected[i], actual[i]);
            }
        }
    }
}