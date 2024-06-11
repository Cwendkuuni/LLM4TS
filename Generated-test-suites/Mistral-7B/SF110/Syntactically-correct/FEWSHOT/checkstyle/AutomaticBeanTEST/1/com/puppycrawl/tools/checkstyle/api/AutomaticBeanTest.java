package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class AutomaticBeanTest {

    private static final AutomaticBean AUTOMATIC_BEAN = new AutomaticBean();
    private static final BeanUtilsBean BEAN_UTILS_BEAN = new BeanUtilsBean(new ConvertUtilsBean(), new PropertyUtilsBean());

    @Parameters(name = "{index}: test propertyName={0} value={1}")
    public static List<Object[]> data() {
        return ListUtils.unionWith(Arrays.asList(new Object[]{"String", "testString"}), Arrays.asList(propertiesData()));
    }

    private static Object[][] propertiesData() {
        return new Object[][]{
                {"boolean", true},
                {"byte", (byte) 1},
                {"short", (short) 1},
                {"int", 1},
                {"long", 1L},
                {"float", 1.0f},
                {"double", 1.0},
                {"boolean[]", new boolean[]{true, false}},
                {"byte[]", new byte[]{1, 2}},
                {"short[]", new short[]{1, 2}},
                {"int[]", new int[]{1, 2}},
                {"long[]", new long[]{1, 2}},
                {"float[]", new float[]{1.0f, 2.0f}},
                {"double[]", new double[]{1.0, 2.0}},
                {"String[]", new String[]{"test1", "test2"}},
        };
    }

    private final String propertyName;
    private final Object propertyValue;

    public AutomaticBeanTest(final String propertyName, final Object propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    @Test
    public void testConfigure() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("testProperty", propertyValue);
        AUTOMATIC_BEAN.configure(config);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(AUTOMATIC_BEAN, propertyName);
        if (descriptor != null) {
            BEAN_UTILS_BEAN.copyProperty(AUTOMATIC_BEAN, propertyName, propertyValue);
            assertEquals(propertyValue, descriptor.getReadMethod().invoke(AUTOMATIC_BEAN));
        } else {
            assertNull(AUTOMATIC_BEAN.getConfiguration().getAttribute(propertyName));
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidPropertyType() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("testProperty", new Object());
        AUTOMATIC_BEAN.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidPropertyName() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("testProperty", propertyValue);
        AUTOMATIC_BEAN.configure(config);
        final String invalidPropertyName = StringUtils.randomAlphaNumeric(10);
        try {
            BEAN_UTILS_BEAN.copyProperty(AUTOMATIC_BEAN, invalidPropertyName, propertyValue);
        } catch (IllegalArgumentException ex) {
            throw new CheckstyleException("", ex);
        }
    }

    @Test
    public void testContextualize() throws Exception {
        final Context context = new Context();
        context.put("testProperty", propertyValue);
        AUTOMATIC_BEAN.contextualize(context);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(AUTOMATIC_BEAN, propertyName);
        if (descriptor != null) {
            BEAN_UTILS_BEAN.copyProperty(AUTOMATIC_BEAN, propertyName, propertyValue);
            assertEquals(propertyValue, descriptor.getReadMethod().invoke(AUTOMATIC_BEAN));
        } else {
            assertNull(AUTOMATIC_BEAN.getConfiguration().getAttribute(propertyName));
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidPropertyType() throws Exception {
        final Context context = new Context();
        context.put("testProperty", new Object());
        AUTOMATIC_BEAN.contextualize(context);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidPropertyName() throws Exception {
        final Context context = new Context();
        context.put("testProperty", propertyValue);
        AUTOMATIC_BEAN.contextualize(context);
        final String invalidPropertyName = StringUtils.randomAlphaNumeric(10);
        try {
            BEAN_UTILS_BEAN.copyProperty(AUTOMATIC_BEAN, invalidPropertyName, propertyValue);
        } catch (IllegalArgumentException ex) {
            throw new CheckstyleException("", ex);
        }
    }
}