package com.example.automaticbean;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AutomaticBeanTest {

 private AutomaticBean automaticBean;
 private Configuration configuration;

 @Before
 public void setUp() {
 automaticBean = new AutomaticBean();
 configuration = new Configuration();
 configuration.addAttribute("testKey1", "testValue1");
 configuration.addAttribute("testKey2", "testValue2");
 }

 @Test
 public void testConfigure() throws Exception {
 Configuration expectedConfig = new Configuration();
 expectedConfig.addAttribute("testKey1", "testValue1");
 expectedConfig.addAttribute("testKey2", "testValue2");
 expectedConfig.addAttribute("testKey3", "testValue3"); // new attribute to test

 automaticBean.configure(configuration);

 Configuration actualConfig = automaticBean.getConfiguration();
 assertEquals(expectedConfig.getName(), actualConfig.getName());
 assertEquals(expectedConfig.size(), actualConfig.size());

 List<String> actualAttributeNames = Arrays.asList(actualConfig.getAttributeNames());
 List<String> expectedAttributeNames = Arrays.asList(expectedConfig.getAttributeNames());
 assertEquals(expectedAttributeNames, actualAttributeNames);

 Map<String, Object> actualAttributes = new LinkedHashMap<>();
 Map<String, Object> expectedAttributes = new LinkedHashMap<>();
 for (String key : actualConfig.getAttributeNames()) {
 actualAttributes.put(key, actualConfig.getAttribute(key));
 expectedAttributes.put(key, expectedConfig.getAttribute(key));
 }
 assertEquals(expectedAttributes, actualAttributes);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureNullConfig() {
 automaticBean.configure(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureDuplicateKey() {
 Configuration config = new Configuration();
 config.addAttribute("testKey1", "testValue1");
 config.addAttribute("testKey1", "testValue2");
 automaticBean.configure(config);
 }

 @Test
 public void testTryCopyProperty() throws Exception {
 Object testValue = new Object();
 automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "testProperty1", testValue, true);

 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testProperty1");
 assertNotNull(descriptor);

 Object actualValue = BeanUtils.getPropertyValue(automaticBean, "testProperty1");
 assertEquals(testValue, actualValue);
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyNullValue() throws Exception {
 automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "testProperty2", null, true);
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyInvalidType() throws Exception {
 automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "testProperty3", new Object(), true);
 }

 @Test
 public void testContextualize() {
 Configuration contextConfig = new Configuration();
 contextConfig.addAttribute("testContextKey1", "testContextValue1");
 contextConfig.addAttribute("testContextKey2", "testContextValue2");

 automaticBean.contextualize(contextConfig);

 Map<String, Object> actualAttributes = new LinkedHashMap<>();
 for (String key : automaticBean.getConfiguration().getAttributeNames()) {
 actualAttributes.put(key, automaticBean.getConfiguration().getAttribute(key));
 }

 Map<String, Object> expectedAttributes = new LinkedHashMap<>();
 expectedAttributes.put("testKey1", "testValue1");
 expectedAttributes.put("testKey2", "testValue2");
 expectedAttributes.put("testContextKey1", "testContextValue1");
 expectedAttributes.put("testContextKey2", "testContextValue2");

 assertEquals(expectedAttributes, actualAttributes);
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeNullContext() {
 automaticBean.contextualize(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeDuplicateKey() {
 Configuration contextConfig = new Configuration();
 contextConfig.addAttribute("testContextKey1", "testContextValue1");
 contextConfig.addAttribute("testContextKey1", "testContextValue2");
 automaticBean.contextualize(contextConfig);
 }

}