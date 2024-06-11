package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AutomaticBean.class)
public class AutomaticBeanTest {

 private static final String TEST_MODULE_NAME = "testModule";

 @Test
 public void testConfigureWithValidConfiguration() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute(AutomaticBean.PROPERTY_NAME, "propertyValue");
 AutomaticBean bean = new AutomaticBean();
 bean.configure(config);

 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, AutomaticBean.PROPERTY_NAME);
 assertEquals("propertyValue", descriptor.getReadMethod().invoke(bean));
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidConfiguration() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute(AutomaticBean.PROPERTY_NAME, "propertyValue");
 AutomaticBean bean = new AutomaticBean();
 bean.configure(config);

 PropertyUtils.destroyProperty(bean, AutomaticBean.PROPERTY_NAME);
 config.addAttribute(AutomaticBean.PROPERTY_NAME, null);
 bean.configure(config);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidPropertyName() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("wrongPropertyName", "propertyValue");
 AutomaticBean bean = new AutomaticBean();
 bean.configure(config);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithNullConfiguration() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 try {
 bean.configure(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testTryCopyPropertyWithValidValue() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 bean.tryCopyProperty(TEST_MODULE_NAME, AutomaticBean.PROPERTY_NAME, "propertyValue", true);
 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, AutomaticBean.PROPERTY_NAME);
 assertEquals("propertyValue", descriptor.getReadMethod().invoke(bean));
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithNullValue() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 try {
 bean.tryCopyProperty(TEST_MODULE_NAME, AutomaticBean.PROPERTY_NAME, null, true);
 fail();
 } catch (CheckstyleException e) {
 // expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithInvalidPropertyName() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 try {
 bean.tryCopyProperty(TEST_MODULE_NAME, "wrongPropertyName", "propertyValue", true);
 fail();
 } catch (CheckstyleException e) {
 // expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithInvalidValueType() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 try {
 bean.tryCopyProperty(TEST_MODULE_NAME, AutomaticBean.PROPERTY_NAME, 42, true);
 fail();
 } catch (CheckstyleException e) {
 // expected
 }
 }

 @Test
 public void testContextualizeWithValidContext() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 Configuration context = new Configuration();
 context.putAttribute("propertyName", "propertyValue");
 bean.contextualize(context);

 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, AutomaticBean.PROPERTY_NAME);
 assertEquals("propertyValue", descriptor.getReadMethod().invoke(bean));
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidContext() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 Configuration context = new Configuration();
 context.putAttribute("propertyName", null);
 bean.contextualize(context);
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithNullContext() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 try {
 bean.contextualize(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testGetConfiguration() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 Configuration config = new Configuration();
 bean.configure(config);
 Configurable configurable = (Configurable)bean;
 assertEquals(config, configurable.getConfiguration());
 }

 @Test
 public void testFinishLocalSetup() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 bean.finishLocalSetup();
 }

 @Test(expected = CheckstyleException.class)
 public void testSetupChildWithNullChildConfiguration() throws Exception {
 AutomaticBean bean = new AutomaticBean();
 Configuration childConf = null;
 bean.setupChild(childConf);
 }
}