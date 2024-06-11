package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(Parameterized.class)
public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;
    private String propertyName;
    private Object propertyValue;

    public AutomaticBeanTest(String propertyName, Object propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    @Parameterized.Parameters(name = "{index}: testProperty({0} = {1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"booleanProperty", true},
                {"booleanArrayProperty", new boolean[] {true, false}},
                {"byteProperty", (byte) 1},
                {"byteArrayProperty", new byte[] {1, 2}},
                {"shortProperty", (short) 1},
                {"shortArrayProperty", new short[] {1, 2}},
                {"intProperty", 1},
                {"intArrayProperty", new int[] {1, 2}},
                {"longProperty", 1L},
                {"longArrayProperty", new long[] {1, 2}},
                {"floatProperty", 1.0f},
                {"floatArrayProperty", new float[] {1.0f, 2.0f}},
                {"doubleProperty", 1.0},
                {"doubleArrayProperty", new double[] {1.0, 2.0}},
                {"stringArrayProperty", new String[] {"one", "two"}},
                {"stringProperty", "one"}
        });
    }

    @Test
    public void testProperty() throws Exception {
        configuration = new Configuration();
        configuration.addAttribute(propertyName, propertyValue);
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, propertyName);
        Object expectedValue = PropertyUtils.getPropertyValue(automaticBean, propertyName);
        assertEquals(propertyValue, expectedValue);

        Object convertedValue = ConvertUtils.convert(propertyValue.getClass(), propertyValue);
        BeanUtils.copyProperty(automaticBean, propertyName, convertedValue);
        assertEquals(propertyValue, PropertyUtils.getPropertyValue(automaticBean, propertyName));
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidProperty() throws Exception {
        configuration = new Configuration();
        configuration.addAttribute("invalidProperty", "invalid value");
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        try {
            PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "invalidProperty");
            fail("property should not exist");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidValue_booleanProperty() throws Exception {
        configuration = new Configuration();
        configuration.addAttribute("booleanProperty", "invalid value");
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        try {
            PropertyUtils.getPropertyValue(automaticBean, "booleanProperty");
            fail("invalid value should throw CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidValue_stringArrayProperty() throws Exception {
        configuration = new Configuration();
        configuration.addAttribute("stringArrayProperty", "invalid,value");
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        try {
            Object value = PropertyUtils.getPropertyValue(automaticBean, "stringArrayProperty");
            fail("invalid value should throw CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }

        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "stringArrayProperty");
        String[] expectedValue = (String[]) descriptor.getReadMethod().invoke(automaticBean);
        assertArrayEquals(new String[0], expectedValue);
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidValue_nonConvertibleType() throws Exception {
        configuration = new Configuration();
        configuration.addAttribute("nonConvertibleTypeProperty", new Object());
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        try {
            PropertyUtils.getPropertyValue(automaticBean, "nonConvertibleTypeProperty");
            fail("non-convertible type should throw CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValue_invalidStringArrayValue() throws Exception {
        configuration = new Configuration();
        configuration.addAttribute("stringArrayProperty", "invalid,value,comma");
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "stringArrayProperty");
        String[] expectedValue = (String[]) descriptor.getReadMethod().invoke(automaticBean);
        assertArrayEquals(new String[0], expectedValue);

        try {
            String invalidValue = "invalid,value,comma";
            BeanUtils.copyProperty(automaticBean, "stringArrayProperty", invalidValue);
            fail("invalid string array value should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        Object[] actualValue = (Object[]) PropertyUtils.getPropertyValue(automaticBean, "stringArrayProperty");
        assertArrayEquals(new Object[0], actualValue);
    }

    @Test
    public void testCopyProperty_recheck() throws Exception {
        configuration = new Configuration();
        configuration.addAttribute("booleanProperty", true);
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "booleanProperty");
        boolean expectedValue = (boolean) descriptor.getReadMethod().invoke(automaticBean);
        assertTrue(expectedValue);

        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "booleanProperty", true, true);
        boolean actualValue = (boolean) PropertyUtils.getPropertyValue(automaticBean, "booleanProperty");
        assertTrue(actualValue);
    }

    @Test
    public void testSetupChild() throws Exception {
        Configuration childConfig = new Configuration();
        automaticBean = new AutomaticBean();
        automaticBean.configure(new Configuration());

        try {
            automaticBean.setupChild(childConfig);
            fail("child configuration should not be set");
        } catch (CheckstyleException e) {
            // expected
        }
    }

}