package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.Lists;

public class AutomaticBeanTest {

    private static final String CONFIG_NAME = "testConfig";
    private static final String ATTRIBUTE_NAME_1 = "attribute1";
    private static final String ATTRIBUTE_NAME_2 = "attribute2";
    private static final String ATTRIBUTE_VALUE_1 = "true";
    private static final String ATTRIBUTE_VALUE_2 = "123";
    private static final String ATTRIBUTE_VALUE_3 = "123, 456, 789";
    private static final String ATTRIBUTE_VALUE_4 = new Boolean[]{true, false};
    private static final String ATTRIBUTE_VALUE_5 = new Byte[]{(byte)1, (byte)2};
    private static final String ATTRIBUTE_VALUE_6 = new Character[]{'a', 'b'};
    private static final String ATTRIBUTE_VALUE_7 = new Double[]{1.1, 2.2};
    private static final String ATTRIBUTE_VALUE_8 = new Float[]{1.1f, 2.2f};
    private static final String ATTRIBUTE_VALUE_9 = new Integer[]{1, 2};
    private static final String ATTRIBUTE_VALUE_10 = new Long[]{1L, 2L};
    private static final String ATTRIBUTE_VALUE_11 = new Short[]{1, 2};

    private AutomaticBean automaticBean;
    private Configuration testConfig;

    @Before
    public void setup() throws Exception {
        testConfig = new Properties();
        testConfig.setProperty(ATTRIBUTE_NAME_1, ATTRIBUTE_VALUE_1);
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(new String[]{ATTRIBUTE_VALUE_2, ATTRIBUTE_VALUE_3}, ","));
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_4).toArray(new Object[0]), ","));
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_5).toArray(new Object[0]), ","));
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_6).toArray(new Object[0]), ","));
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_7).toArray(new Object[0]), ","));
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_8).toArray(new Object[0]), ","));
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_9).toArray(new Object[0]), ","));
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_10).toArray(new Object[0]), ","));
        testConfig.setProperty(ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_11).toArray(new Object[0]), ","));

        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() throws Exception {
        automaticBean.configure(testConfig);

        Object attribute1 = automaticBean.getConfiguration().getAttribute(ATTRIBUTE_NAME_1);
        assertTrue(attribute1 instanceof Boolean);
        assertEquals(Boolean.parseBoolean(ATTRIBUTE_VALUE_1), (Boolean) attribute1);

        Object attribute2 = automaticBean.getConfiguration().getAttribute(ATTRIBUTE_NAME_2);
        assertTrue(attribute2 instanceof Object[]);
        Object[] attribute2Array = (Object[]) attribute2;
        assertEquals(attribute2Array.length, 3);

        assertEquals(attribute2Array[0], Boolean.parseBoolean(ATTRIBUTE_VALUE_1));
        assertEquals(attribute2Array[1], Integer.parseInt(ATTRIBUTE_VALUE_2));
        List<Object> attribute2List = ListUtils.toList((Object[]) attribute2Array[2]);
        assertEquals(attribute2List.size(), 3);
        assertEquals(attribute2List.get(0), Integer.parseInt(ATTRIBUTE_VALUE_2));
        assertEquals(attribute2List.get(1), Integer.parseInt("456"));
        assertEquals(attribute2List.get(2), Integer.parseInt("789"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidProperty() throws Exception {
        testConfig.setProperty("invalidProperty", "value");
        automaticBean.configure(testConfig);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidDataType() throws Exception {
        testConfig.setProperty(ATTRIBUTE_NAME_2, "invalidType");
        automaticBean.configure(testConfig);
    }

    @Test
    public void testContextualize() throws Exception {
        Properties context = new Properties();
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_1, ATTRIBUTE_VALUE_1);
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(new String[]{ATTRIBUTE_VALUE_2, ATTRIBUTE_VALUE_3}, ","));
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_4).toArray(new Object[0]), ","));
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_5).toArray(new Object[0]), ","));
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_6).toArray(new Object[0]), ","));
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_7).toArray(new Object[0]), ","));
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_8).toArray(new Object[0]), ","));
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_9).toArray(new Object[0]), ","));
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_10).toArray(new Object[0]), ","));
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_2, StringUtils.join(Arrays.asList(ATTRIBUTE_VALUE_11).toArray(new Object[0]), ","));

        automaticBean.contextualize(context);

        Object attribute1 = automaticBean.getConfiguration().getAttribute(ATTRIBUTE_NAME_1);
        assertTrue(attribute1 instanceof Boolean);
        assertEquals(Boolean.parseBoolean(ATTRIBUTE_VALUE_1), (Boolean) attribute1);

        Object attribute2 = automaticBean.getConfiguration().getAttribute(ATTRIBUTE_NAME_2);
        assertTrue(attribute2 instanceof Object[]);
        Object[] attribute2Array = (Object[]) attribute2;
        assertEquals(attribute2Array.length, 3);

        assertEquals(attribute2Array[0], Boolean.parseBoolean(ATTRIBUTE_VALUE_1));
        assertEquals(attribute2Array[1], Integer.parseInt(ATTRIBUTE_VALUE_2));
        List<Object> attribute2List = ListUtils.toList((Object[]) attribute2Array[2]);
        assertEquals(attribute2List.size(), 3);
        assertEquals(attribute2List.get(0), Integer.parseInt(ATTRIBUTE_VALUE_2));
        assertEquals(attribute2List.get(1), Integer.parseInt("456"));
        assertEquals(attribute2List.get(2), Integer.parseInt("789"));
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidKey() throws Exception {
        Properties context = new Properties();
        context.setProperty("invalidKey", "value");
        automaticBean.contextualize(context);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidValue() throws Exception {
        Properties context = new Properties();
        context.setProperty(AutomaticBean.class.getName() + "." + ATTRIBUTE_NAME_1, "invalidValue");
        automaticBean.contextualize(context);
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        Object attribute1 = new Boolean(Boolean.parseBoolean(ATTRIBUTE_VALUE_1));
        Method setAttributeMethod = AutomaticBean.class.getDeclaredMethod("tryCopyProperty", String.class, String.class, Object.class, boolean.class);
        setAttributeMethod.setAccessible(true);

        try {
            setAttributeMethod.invoke(automaticBean, "moduleName", ATTRIBUTE_NAME_1, attribute1, true);
        } catch (InvocationTargetException e) {
            fail("Unexpected exception: " + e.getCause().getMessage());
        }

        Object configurationAttribute1 = automaticBean.getConfiguration().getAttribute(ATTRIBUTE_NAME_1);
        assertEquals(configurationAttribute1, attribute1);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyInvalidProperty() throws Exception {
        Method setAttributeMethod = AutomaticBean.class.getDeclaredMethod("tryCopyProperty", String.class, String.class, Object.class, boolean.class);
        setAttributeMethod.setAccessible(true);

        try {
            setAttributeMethod.invoke(automaticBean, "moduleName", "invalidProperty", new Boolean(true), true);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof CheckstyleException);
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyNullValue() throws Exception {
        Method setAttributeMethod = AutomaticBean.class.getDeclaredMethod("tryCopyProperty", String.class, String.class, Object.class, boolean.class);
        setAttributeMethod.setAccessible(true);

        try {
            setAttributeMethod.invoke(automaticBean, "moduleName", ATTRIBUTE_NAME_1, null, true);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof CheckstyleException);
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyInvalidType() throws Exception {
        Object attribute1 = new Double(1.1);
        Method setAttributeMethod = AutomaticBean.class.getDeclaredMethod("tryCopyProperty", String.class, String.class, Object.class, boolean.class);
        setAttributeMethod.setAccessible(true);

        try {
            setAttributeMethod.invoke(automaticBean, "moduleName", ATTRIBUTE_NAME_1, attribute1, true);
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof CheckstyleException);
        }
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        // Empty implementation
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildInvalidConfig() throws Exception {
        Configuration childConfig = new Properties();
        automaticBean.setupChild(childConfig);
    }
}