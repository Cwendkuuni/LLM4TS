package com.example.automaticbean; // Replace with your package name

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.beanutils.properties.PropertyDescriptor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;

    @Parameters
    public static Collection<Object[]> data() throws InstantiationException, IllegalAccessException {
        return Arrays.asList(new Object[][]{
                {new Configuration(), new AutomaticBean()}
        });
    }

    public AutomaticBeanTest(Configuration config, AutomaticBean bean) {
        this.configuration = config;
        this.automaticBean = bean;
    }

    @Test
    public void testConfigure() throws IllegalAccessException, InvocationTargetException, IntrospectionException, CheckstyleException {
        // Basic test
        Configuration config = new Configuration();
        config.setProperty("testProperty", "testValue");
        automaticBean.configure(config);
        Object testValue = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals("testValue", testValue);

        // Test with null configuration
        Configuration nullConfig = null;
        try {
            automaticBean.configure(nullConfig);
            fail();
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with null property
        Configuration nullPropertyConfig = new Configuration();
        nullPropertyConfig.setProperty(null, "testValue");
        try {
            automaticBean.configure(nullPropertyConfig);
            fail();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | CheckstyleException e) {
            // Expected
        }

        // Test with non-existent property
        Configuration nonExistentPropertyConfig = new Configuration();
        try {
            automaticBean.configure(nonExistentPropertyConfig);
            fail();
        } catch (CheckstyleException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Property 'nonExistentProperty' in module " + automaticBean.getClass().getName() + " does not exist, please check the documentation"));
        }
    }

    @Test
    public void testTryCopyProperty_Recheck() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        // Basic test
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testProperty");
        automaticBean.tryCopyProperty("testModule", "testProperty", "testValue", true);
        Object testValue = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals("testValue", testValue);

        // Test with non-existent property
        try {
            automaticBean.tryCopyProperty("testModule", "nonExistentProperty", "testValue", true);
            fail();
        } catch (CheckstyleException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Property 'nonExistentProperty' in module testModule does not exist, please check the documentation"));
        }
    }

    @Test
    public void testTryCopyProperty_String() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", "testStringValue", false);
        Object testStringValue = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals("testStringValue", testStringValue);
    }

    @Test
    public void testTryCopyProperty_Boolean() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", true, false);
        boolean testBooleanValue = (boolean) PropertyUtils.getProperty(automaticBean, "testProperty");
        assertTrue(testBooleanValue);
    }

    @Test
    public void testTryCopyProperty_Integer() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", 1, false);
        int testIntegerValue = (int) PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals(1, testIntegerValue);
    }

    @Test
    public void testTryCopyProperty_Long() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", 1L, false);
        long testLongValue = (long) PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals(1L, testLongValue);
    }

    @Test
    public void testTryCopyProperty_Double() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", 1.0, false);
        double testDoubleValue = (double) PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals(1.0, testDoubleValue, 0.001);
    }

    @Test
    public void testTryCopyProperty_Float() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", 1.0f, false);
        float testFloatValue = (float) PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals(1.0f, testFloatValue, 0.001f);
    }

    @Test
    public void testTryCopyProperty_Byte() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", (byte) 1, false);
        byte testByteValue = (byte) PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals((byte) 1, testByteValue);
    }

    @Test
    public void testTryCopyProperty_Short() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", (short) 1, false);
        short testShortValue = (short) PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals((short) 1, testShortValue);
    }

    @Test
    public void testTryCopyProperty_Character() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        automaticBean.tryCopyProperty("testModule", "testProperty", 'A', false);
        char testCharValue = (char) PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals('A', testCharValue);
    }

    @Test
    public void testTryCopyProperty_ArrayOfBoolean() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        boolean[] testBooleanArrayValue = {true, false, true};
        automaticBean.tryCopyProperty("testModule", "testProperty", testBooleanArrayValue, true);
        Object testBooleanArray = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertArrayEquals(testBooleanArrayValue, (boolean[]) testBooleanArray);
    }

    @Test
    public void testTryCopyProperty_ArrayOfByte() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        byte[] testByteArrayValue = {1, 2, 3};
        automaticBean.tryCopyProperty("testModule", "testProperty", testByteArrayValue, true);
        Object testByteArray = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertArrayEquals(testByteArrayValue, (byte[]) testByteArray);
    }

    @Test
    public void testTryCopyProperty_ArrayOfCharacter() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        char[] testCharArrayValue = {'A', 'B', 'C'};
        automaticBean.tryCopyProperty("testModule", "testProperty", testCharArrayValue, true);
        Object testCharArray = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertArrayEquals(testCharArrayValue, (char[]) testCharArray);
    }

    @Test
    public void testTryCopyProperty_ArrayOfInt() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        int[] testIntArrayValue = {1, 2, 3};
        automaticBean.tryCopyProperty("testModule", "testProperty", testIntArrayValue, true);
        Object testIntArray = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertArrayEquals(testIntArrayValue, (int[]) testIntArray);
    }

    @Test
    public void testTryCopyProperty_ArrayOfLong() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        long[] testLongArrayValue = {1L, 2L, 3L};
        automaticBean.tryCopyProperty("testModule", "testProperty", testLongArrayValue, true);
        Object testLongArray = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertArrayEquals(testLongArrayValue, (long[]) testLongArray);
    }

    @Test
    public void testTryCopyProperty_ArrayOfDouble() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        double[] testDoubleArrayValue = {1.0, 2.0, 3.0};
        automaticBean.tryCopyProperty("testModule", "testProperty", testDoubleArrayValue, true);
        Object testDoubleArray = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertArrayEquals(testDoubleArrayValue, (double[]) testDoubleArray);
    }

    @Test
    public void testTryCopyProperty_ArrayOfFloat() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        float[] testFloatArrayValue = {1.0f, 2.0f, 3.0f};
        automaticBean.tryCopyProperty("testModule", "testProperty", testFloatArrayValue, true);
        Object testFloatArray = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertArrayEquals(testFloatArrayValue, (float[]) testFloatArray);
    }

    @Test
    public void testTryCopyProperty_ArrayOfString() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        String[] testStringArrayValue = {"test1", "test2", "test3"};
        automaticBean.tryCopyProperty("testModule", "testProperty", testStringArrayValue, true);
        Object testStringArray = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertArrayEquals(testStringArrayValue, (String[]) testStringArray);
    }

    @Test
    public void testContextualize() throws IllegalAccessException, InvocationTargetException, CheckstyleException {
        // Basic test
        Configuration context = new Configuration();
        context.setProperty("testProperty", "testValue");
        automaticBean.contextualize(context);
        Object testValue = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertEquals("testValue", testValue);

        // Test with null context
        Configuration nullContext = null;
        try {
            automaticBean.contextualize(nullContext);
            fail();
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with non-existent property
        Configuration nonExistentPropertyContext = new Configuration();
        try {
            automaticBean.contextualize(nonExistentPropertyContext);
            fail();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | CheckstyleException e) {
            // Expected
        }

        // Test with empty context
        Configuration emptyContext = new Configuration();
        automaticBean.contextualize(emptyContext);
        Object emptyValue = PropertyUtils.getProperty(automaticBean, "testProperty");
        assertNull(emptyValue);
    }

    @Test
    public void testGetConfiguration() throws IllegalAccessException {
        assertEquals(configuration, automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws CheckstyleException {
        Configuration childConfig = new Configuration();
        automaticBean.setupChild(childConfig);
    }
}