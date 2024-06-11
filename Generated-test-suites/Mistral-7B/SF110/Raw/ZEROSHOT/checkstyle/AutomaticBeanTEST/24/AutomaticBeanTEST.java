package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

 private AutomaticBean automaticBean;
 private Configuration configuration;
 private ConvertUtilsBean convertUtilsBean;
 private BeanUtilsBean beanUtilsBean;

 @Before
 public void setUp() throws Exception {
 automaticBean = new AutomaticBean();
 configuration = new Configuration();
 convertUtilsBean = new ConvertUtilsBean();
 convertUtilsBean.register(new BooleanConverter(), Boolean.TYPE);
 convertUtilsBean.register(new BooleanConverter(), Boolean.class);
 beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
 }

 @Test
 public void testConfigure() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testBoolean", true);
 config.addAttribute("testBooleanArray", new boolean[] { true, false, true });
 config.addAttribute("testByte", (byte) 1);
 config.addAttribute("testByteArray", new byte[] { 1, 2, 3 });
 config.addAttribute("testCharacter", 'A');
 config.addAttribute("testCharacterArray", new char[] { 'A', 'B', 'C' });
 config.addAttribute("testDouble", 1.1);
 config.addAttribute("testDoubleArray", new double[] { 1.1, 2.2, 3.3 });
 config.addAttribute("testFloat", 1.0f);
 config.addAttribute("testFloatArray", new float[] { 1.0f, 2.0f, 3.0f });
 config.addAttribute("testInteger", 1);
 config.addAttribute("testIntegerArray", new int[] { 1, 2, 3 });
 config.addAttribute("testLong", 1L);
 config.addAttribute("testLongArray", new long[] { 1L, 2L, 3L });
 config.addAttribute("testShort", (short) 1);
 config.addAttribute("testShortArray", new short[] { 1, 2, 3 });
 config.addAttribute("testStringArray", new String[] { "test1", "test2", "test3" });

 automaticBean.configure(config);

 assertEquals(config, automaticBean.getConfiguration());

 PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testBoolean");
 assertTrue((Boolean)propertyDescriptor.getReadMethod().invoke(automaticBean));

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testBooleanArray");
 Object value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new boolean[] { true, false, true }, value);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testByte");
 assertEquals((byte)1, (Byte)propertyDescriptor.getReadMethod().invoke(automaticBean));

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testByteArray");
 value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new byte[] { 1, 2, 3 }, value);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testCharacter");
 assertEquals('A', (Character)propertyDescriptor.getReadMethod().invoke(automaticBean));

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testCharacterArray");
 value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new char[] { 'A', 'B', 'C' }, value);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testDouble");
 assertEquals(1.1, (Double)propertyDescriptor.getReadMethod().invoke(automaticBean), 0.001);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testDoubleArray");
 value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new double[] { 1.1, 2.2, 3.3 }, value, 0.001);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testFloat");
 assertEquals(1.0f, (Float)propertyDescriptor.getReadMethod().invoke(automaticBean), 0.001);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testFloatArray");
 value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new float[] { 1.0f, 2.0f, 3.0f }, value, 0.001);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testInteger");
 assertEquals(1, (Integer)propertyDescriptor.getReadMethod().invoke(automaticBean));

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testIntegerArray");
 value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new int[] { 1, 2, 3 }, value);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testLong");
 assertEquals(1L, (Long)propertyDescriptor.getReadMethod().invoke(automaticBean));

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testLongArray");
 value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new long[] { 1L, 2L, 3L }, value);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testShort");
 assertEquals((short)1, (Short)propertyDescriptor.getReadMethod().invoke(automaticBean));

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testShortArray");
 value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new short[] { 1, 2, 3 }, value);

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testStringArray");
 value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new String[] { "test1", "test2", "test3" }, value);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidProperty() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testInvalid", new Object());
 automaticBean.configure(config);
 fail("Exception expected: Property 'testInvalid' of type 'java.lang.Object' is not supported.");
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidType() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testInvalidType", new Object() {});
 automaticBean.configure(config);
 fail("Exception expected: Cannot set property 'testInvalidType' to 'com.puppycrawl.tools.checkstyle.api.AutomaticBean$1@6d2d4a6d' in module AutomaticBean");
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithNullConfiguration() throws Exception {
 automaticBean.configure(null);
 fail("Exception expected: Null configuration argument.");
 }

 @Test
 public void testContextualize() throws Exception {
 configuration.addAttribute("testBoolean", true);
 configuration.addAttribute("testStringArray", new String[] { "test1", "test2", "test3" });

 automaticBean.contextualize(configuration);

 PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testBoolean");
 assertTrue((Boolean)propertyDescriptor.getReadMethod().invoke(automaticBean));

 propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testStringArray");
 Object value = propertyDescriptor.getReadMethod().invoke(automaticBean);
 assertArrayEquals(new String[] { "test1", "test2", "test3" }, value);
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithNullContext() throws Exception {
 automaticBean.contextualize(null);
 fail("Exception expected: Null context argument.");
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidProperty() throws Exception {
 configuration.addAttribute("testInvalidProperty", new Object());
 automaticBean.contextualize(configuration);
 fail("Exception expected: Property 'testInvalidProperty' in module AutomaticBean does not exist, please check the documentation");
 }

 @Test
 public void testGetConfiguration() throws Exception {
 assertEquals(configuration, automaticBean.getConfiguration());
 }

}