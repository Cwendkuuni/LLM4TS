package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
        assertTrue(beanUtilsBean instanceof BeanUtilsBean);
        assertTrue(beanUtilsBean.getBeanUtils() instanceof BeanUtilsBean);
        assertTrue(beanUtilsBean.getPropertyUtils() instanceof PropertyUtilsBean);
    }

    @Test
    public void testConfiguration() {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test
    public void testConfigure_AttributeExists() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("testKey", "testValue");
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final String actual = automaticBean.tryGetProperty("testKey");
        assertEquals("testValue", actual);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_AttributeDoesNotExist() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final String actual = automaticBean.tryGetProperty("nonExistentKey");
        fail("Property 'nonExistentKey' should not exist");
    }

    @Test
    public void testTryCopyProperty_Boolean() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("booleanKey", true);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final boolean actual = automaticBean.tryGetProperty("booleanKey");
        assertTrue(actual);
    }

    @Test
    public void testTryCopyProperty_Byte() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("byteKey", (byte) 5);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final byte actual = automaticBean.tryGetProperty("byteKey");
        assertEquals((byte) 5, actual);
    }

    @Test
    public void testTryCopyProperty_Short() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("shortKey", (short) 10);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final short actual = automaticBean.tryGetProperty("shortKey");
        assertEquals((short) 10, actual);
    }

    @Test
    public void testTryCopyProperty_Integer() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("integerKey", 15);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final int actual = automaticBean.tryGetProperty("integerKey");
        assertEquals(15, actual);
    }

    @Test
    public void testTryCopyProperty_Long() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("longKey", 20L);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final long actual = automaticBean.tryGetProperty("longKey");
        assertEquals(20L, actual);
    }

    @Test
    public void testTryCopyProperty_Float() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("floatKey", 15.5f);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final float actual = automaticBean.tryGetProperty("floatKey");
        assertEquals(15.5f, actual, 0.001f);
    }

    @Test
    public void testTryCopyProperty_Double() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("doubleKey", 15.5);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final double actual = automaticBean.tryGetProperty("doubleKey");
        assertEquals(15.5, actual, 0.001);
    }

    @Test
    public void testTryCopyProperty_String() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("stringKey", "testValue");
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final String actual = automaticBean.tryGetProperty("stringKey");
        assertEquals("testValue", actual);
    }

    @Test
    public void testTryCopyProperty_Array_Boolean() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("booleanArrayKey", new boolean[] { true, false });
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final boolean[] actual = automaticBean.tryGetProperty("booleanArrayKey");
        assertArrayEquals(new boolean[] { true, false }, actual);
    }

    @Test
    public void testTryCopyProperty_Array_Byte() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("byteArrayKey", new byte[] { 5, 10 });
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final byte[] actual = automaticBean.tryGetProperty("byteArrayKey");
        assertArrayEquals(new byte[] { 5, 10 }, actual);
    }

    @Test
    public void testTryCopyProperty_Array_Short() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("shortArrayKey", new short[] { 10, 20 });
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final short[] actual = automaticBean.tryGetProperty("shortArrayKey");
        assertArrayEquals(new short[] { 10, 20 }, actual);
    }

    @Test
    public void testTryCopyProperty_Array_Integer() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("integerArrayKey", new int[] { 15, 25 });
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final int[] actual = automaticBean.tryGetProperty("integerArrayKey");
        assertArrayEquals(new int[] { 15, 25 }, actual);
    }

    @Test
    public void testTryCopyProperty_Array_Long() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("longArrayKey", new long[] { 20L, 30L });
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final long[] actual = automaticBean.tryGetProperty("longArrayKey");
        assertArrayEquals(new long[] { 20L, 30L }, actual);
    }

    @Test
    public void testTryCopyProperty_Array_Float() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("floatArrayKey", new float[] { 15.5f, 25.5f });
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final float[] actual = automaticBean.tryGetProperty("floatArrayKey");
        assertArrayEquals(new float[] { 15.5f, 25.5f }, actual, 0.001f);
    }

    @Test
    public void testTryCopyProperty_Array_Double() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("doubleArrayKey", new double[] { 15.5, 25.5 });
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final double[] actual = automaticBean.tryGetProperty("doubleArrayKey");
        assertArrayEquals(new double[] { 15.5, 25.5 }, actual, 0.001);
    }

    @Test
    public void testContextualize() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("testKey", "testValue");

        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.contextualize(config);

        final String actual = automaticBean.tryGetProperty("testKey");
        assertEquals("testValue", actual);
    }

    @Test
    public void testGetConfiguration() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        final Configuration actual = automaticBean.getConfiguration();
        assertEquals(config, actual);
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_Null() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.setupChild(config);
        fail("Config should not be null");
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_NonEmpty() throws Exception {
        final Configuration childConfig = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.setupChild(childConfig);
        fail("Child configuration should not be allowed");
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final String testString = "test,string,array";
        final Converter converter = new AutomaticBean().new RelaxedStringArrayConverter();
        final Object convertedObject = converter.convert(String[].class, testString);
        final String[] result = (String[]) convertedObject;
        assertEquals(3, result.length);
        assertEquals("test", result[0]);
        assertEquals("string", result[1]);
        assertEquals("array", result[2]);
    }

    private Object tryGetProperty(String propertyName) throws Exception {
        final Method getPropertyMethod = this.getClass().getMethod("tryGetProperty", String.class);
        final Object result = getPropertyMethod.invoke(this, propertyName);
        return result;
    }
}