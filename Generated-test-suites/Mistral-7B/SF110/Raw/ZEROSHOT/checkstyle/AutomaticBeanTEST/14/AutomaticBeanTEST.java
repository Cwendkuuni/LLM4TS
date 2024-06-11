package com.example.automaticbean;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.BeanUtilsBean;
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
import org.apache.commons.collections4.iterators.ArrayIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@RunWith(org.junit.runner.JUnit4.class)
public class AutomaticBeanTest {

 private static final String MODULE_NAME = "AutomaticBean";

 @Test
 public void testCreateBeanUtilsBean() {
 BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
 assertTrue(beanUtilsBean instanceof BeanUtilsBean);
 assertTrue(beanUtilsBean.getConvertUtils().getConverters().contains(new BooleanConverter()));
 }

 @Test
 public void testCopyPropertyWithExistingProperty() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 automaticBean.setConfiguration(new Properties());
 automaticBean.getConfiguration().setProperty("testProperty", "testValue");

 AutomaticBean copy = new AutomaticBean();
 BeanUtils.copyProperties(automaticBean, copy);
 assertEquals("testValue", copy.getConfiguration().getProperty("testProperty"));
 }

 @Test(expected = CheckstyleException.class)
 public void testCopyPropertyWithMissingProperty() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 automaticBean.setConfiguration(new Properties());

 AutomaticBean copy = new AutomaticBean();
 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(copy, "nonExistentProperty");
 BeanUtils.copyProperties(automaticBean, copy, descriptor.getName());
 }

 @Test(expected = CheckstyleException.class)
 public void testCopyPropertyWithInvalidType() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 automaticBean.setConfiguration(new Properties());
 automaticBean.getConfiguration().setProperty("invalidProperty", new Object() {});

 AutomaticBean copy = new AutomaticBean();
 try {
 BeanUtils.copyProperties(automaticBean, copy);
 fail("Expected CheckstyleException");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testCopyPropertyWithInvalidValue() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 automaticBean.setConfiguration(new Properties());
 automaticBean.getConfiguration().setProperty("invalidProperty", "not a number");

 AutomaticBean copy = new AutomaticBean();
 try {
 BeanUtils.copyProperties(automaticBean, copy);
 fail("Expected CheckstyleException");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test
 public void testConfigure() throws Exception {
 AutomaticBean automaticBean = Mockito.spy(new AutomaticBean());
 Properties config = new Properties();
 config.setProperty("testProperty", "testValue");
 automaticBean.configure(config);
 assertEquals(config, automaticBean.getConfiguration());
 }

 @Test
 public void testContextualize() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 Properties context = new Properties();
 context.setProperty("testProperty", "testValue");
 automaticBean.contextualize(context);
 assertEquals("testValue", automaticBean.getConfiguration().getProperty("testProperty"));
 }

 @Test
 public void testGetConfiguration() {
 AutomaticBean automaticBean = new AutomaticBean();
 assertNull(automaticBean.getConfiguration());
 }

 @Test
 public void testFinishLocalSetup() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 assertDoesNotThrow(automaticBean::finishLocalSetup);
 }

 @Test(expected = CheckstyleException.class)
 public void testSetupChildWithNullChildConfiguration() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 Configuration childConfiguration = null;
 automaticBean.setupChild(childConfiguration);
 fail("Expected CheckstyleException");
 }

 @Test
 public void testTryCopyPropertyWithExistingProperty() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 Properties configuration = new Properties();
 configuration.setProperty("testProperty", "testValue");
 automaticBean.setConfiguration(configuration);

 AutomaticBean copy = new AutomaticBean();
 assertDoesNotThrow(() -> automaticBean.tryCopyProperty(MODULE_NAME, "testProperty", "testValue", true));
 assertEquals("testValue", copy.getConfiguration().getProperty("testProperty"));
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithMissingProperty() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 Properties configuration = new Properties();
 automaticBean.setConfiguration(configuration);

 try {
 automaticBean.tryCopyProperty(MODULE_NAME, "nonExistentProperty", "testValue", true);
 fail("Expected CheckstyleException");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithInvalidType() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 Properties configuration = new Properties();
 automaticBean.setConfiguration(configuration);

 try {
 automaticBean.tryCopyProperty(MODULE_NAME, "invalidProperty", new Object() {
 }, true);
 fail("Expected CheckstyleException");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithInvalidValue() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 Properties configuration = new Properties();
 automaticBean.setConfiguration(configuration);

 try {
 automaticBean.tryCopyProperty(MODULE_NAME, "invalidProperty", "not a number", true);
 fail("Expected CheckstyleException");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test
 public void testTryCopyPropertyWithNullValue() throws Exception {
 AutomaticBean automaticBean = new AutomaticBean();
 Properties configuration = new Properties();
 automaticBean.setConfiguration(configuration);

 try {
 automaticBean.tryCopyProperty(MODULE_NAME, "testProperty", null, true);
 fail("Expected CheckstyleException");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test
 public void testCreateConverter() {
 Converter converter = AutomaticBean.createConverter(String[].class);
 assertTrue(converter instanceof RelaxedStringArrayConverter);
 }
}