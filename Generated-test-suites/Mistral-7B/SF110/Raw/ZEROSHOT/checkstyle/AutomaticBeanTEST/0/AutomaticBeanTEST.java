package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class AutomaticBeanTest {

 private AutomaticBean automaticBean;
 private Configuration configuration;
 private ConvertUtilsBean convertUtilsBean;
 private BeanUtilsBean beanUtilsBean;

 @Before
 public void setUp() {
 Configuration config = new Configuration();
 configuration.addAttribute("moduleName", "testModule");
 configuration.addAttribute("key1", "value1");
 configuration.addAttribute("key2", "value2");
 configuration.addChild(new Configuration());
 automaticBean = new AutomaticBean();
 convertUtilsBean = new ConvertUtilsBean();
 convertUtilsBean.register(new BooleanConverter(), Boolean.TYPE);
 convertUtilsBean.register(new BooleanConverter(), Boolean.class);
 beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
 automaticBean.configure(configuration);
 }

 @Test
 public void testConfigure() {
 Configuration config = new Configuration();
 config.addAttribute("moduleName", "testModule");
 config.addAttribute("key3", "value3");

 AutomaticBean testAutomaticBean = new AutomaticBean();
 BeanUtilsBean testBeanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());

 try {
 testAutomaticBean.configure(config);
 } catch (CheckstyleException e) {
 e.printStackTrace();
 fail("Unexpected CheckstyleException");
 }

 assertEquals("testModule", automaticBean.getConfiguration().getName());
 assertEquals("value1", automaticBean.getConfiguration().getAttribute("key1"));
 assertEquals("value2", automaticBean.getConfiguration().getAttribute("key2"));
 assertArrayEquals(new String[]{"value1", "value2"}, automaticBean.getConfiguration().getChildren()[0].getAttributeNames());

 String[] attributeNames = config.getAttributeNames();
 String[] attributes = attributeNames = config.getAttributeNames();
 for (String key : attributeNames) {
 assertEquals(key, automaticBean.getConfiguration().getAttribute(key));
 }

 assertNull(automaticBean.getConfiguration().getChildren()[1]);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidAttribute() {
 Configuration config = new Configuration();
 config.addAttribute("moduleName", "testModule");
 config.addAttribute("key4", new Object());

 AutomaticBean testAutomaticBean = new AutomaticBean();
 BeanUtilsBean testBeanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());

 try {
 testAutomaticBean.configure(config);
 fail("Expected CheckstyleException");
 } catch (CheckstyleException e) {
 // Expected exception
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidChild() {
 Configuration config = new Configuration();
 config.addAttribute("moduleName", "testModule");
 config.addChild(new Object());

 AutomaticBean testAutomaticBean = new AutomaticBean();
 BeanUtilsBean testBeanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());

 try {
 testAutomaticBean.configure(config);
 fail("Expected CheckstyleException");
 } catch (CheckstyleException e) {
 // Expected exception
 }
 }

 @Test
 public void testTryCopyProperty() throws Exception {
 final String key = "key4";
 final Object value = "value4";

 assertEquals(null, automaticBean.getConfiguration().getAttribute(key));
 automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, value, true);
 assertEquals(value, automaticBean.getConfiguration().getAttribute(key));

 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
 assertNotNull(descriptor);

 try {
 automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, null, true);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }

 try {
 automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, "invalid_value", true);
 fail("Expected ConversionException");
 } catch (ConversionException e) {
 // Expected exception
 }

 try {
 automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, 1, true);
 fail("Expected ConversionException");
 } catch (ConversionException e) {
 // Expected exception
 }

 try {
 automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, new Object(), true);
 fail("Expected ConversionException");
 } catch (ConversionException e) {
 // Expected exception
 }
}

 @Test
 public void testContextualize() {
 Configuration contextConfig = new Configuration();
 contextConfig.addAttribute("key5", "value5");
 contextConfig.addAttribute("key6", new Configuration());
 contextConfig.addAttribute("key7", new Integer(1));
 contextConfig.addAttribute("key8", Collections.singletonList("value8"));
 contextConfig.addAttribute("key9", Arrays.asList(1, 2, 3));

 automaticBean.contextualize(contextConfig);
 assertEquals(1, automaticBean.getConfiguration().getChildren()[0].getAttributeAsInt("key5"));
 assertEquals("value5", automaticBean.getConfiguration().getChildren()[0].getAttribute("key5"));
 assertArrayEquals(new Configuration[0], automaticBean.getConfiguration().getChildren()[1].getChildren());
 assertNull(automaticBean.getConfiguration().getChildren()[1].getAttributeNames());
 assertEquals(1, ((List)automaticBean.getConfiguration().getChildren()[2].getAttribute("key8")).size());
 assertEquals(1, ((List)automaticBean.getConfiguration().getChildren()[3].getAttribute("key9")).size());
 assertEquals(1, ((List)automaticBean.getConfiguration().getChildren()[3].getAttribute("key9")).get(0).intValue());
 assertEquals(2, ((List)automaticBean.getConfiguration().getChildren()[3].getAttribute("key9")).get(1).intValue());
 assertEquals(3, ((List)automaticBean.getConfiguration().getChildren()[3].getAttribute("key9")).get(2).intValue());
}

 @Test
 public void testGetConfiguration() {
 assertEquals(configuration, automaticBean.getConfiguration());
 }
}