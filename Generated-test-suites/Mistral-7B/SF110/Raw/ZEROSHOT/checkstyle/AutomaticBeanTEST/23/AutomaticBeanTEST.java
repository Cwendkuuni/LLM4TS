package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

 private AutomaticBean automaticBean;
 private Configuration configuration;
 private BeanUtilsBean beanUtilsBean;

 @Before
 public void setup() {
 automaticBean = new AutomaticBean();
 configuration = new Configuration();
 beanUtilsBean = new BeanUtilsBean(new ConvertUtils(), new PropertyUtilsBean());
 }

 @Test
 public void testConfigure() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testBoolean", true);
 config.addAttribute("testString", "test");
 config.addAttribute("testInteger", 1);
 config.addAttribute("testList", Arrays.asList("element1", "element2"));
 config.addChild(new Configuration());

 automaticBean.configure(config);

 assertEquals(config, automaticBean.getConfiguration());

 assertEquals(true, automaticBean.getConfiguration().getAttribute("testBoolean"));
 assertEquals("test", automaticBean.getConfiguration().getAttribute("testString"));
 assertEquals(1, automaticBean.getConfiguration().getAttribute("testInteger").intValue());

 List<String> testList = Arrays.asList("element1", "element2");
 List<String> expectedList = ListUtils.union(Arrays.asList(automaticBean.getConfiguration().getAttributeNames()), testList);
 assertEquals(expectedList, Arrays.asList(automaticBean.getConfiguration().getAttributeNames()));

 Configuration childConfig = automaticBean.getConfiguration().getChildren()[0];
 assertNotNull(childConfig);
 assertThrows(CheckstyleException.class, () -> automaticBean.setupChild(childConfig));
 }

 @Test
 public void testContextualize() throws Exception {
 configuration.putAttribute("testBoolean", true);
 configuration.putAttribute("testString", "test");
 configuration.putAttribute("testInteger", 1);
 configuration.putAttribute("testList", Arrays.asList("element1", "element2"));

 automaticBean.contextualize(configuration);

 assertEquals(true, automaticBean.getConfiguration().getAttribute("testBoolean"));
 assertEquals("test", automaticBean.getConfiguration().getAttribute("testString"));
 assertEquals(1, automaticBean.getConfiguration().getAttribute("testInteger").intValue());

 List<String> testList = Arrays.asList("element1", "element2");
 List<String> expectedList = ListUtils.union(Arrays.asList(automaticBean.getConfiguration().getAttributeNames()), testList);
 assertEquals(expectedList, Arrays.asList(automaticBean.getConfiguration().getAttributeNames()));
 }

 @Test
 public void testTryCopyPropertyWithRecheck() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testBoolean", true);
 config.addAttribute("testInvalidBoolean", "true");

 automaticBean.configure(config);
 assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty("AutomaticBean", "testInvalidBoolean", "invalidValue", true));
 }

 @Test
 public void testTryCopyPropertyWithInvalidValue() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testBoolean", true);
 config.addAttribute("testString", "invalidValue");

 assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty("AutomaticBean", "testString", "invalidValue", false));
 }

 @Test
 public void testTryCopyPropertyWithIllegalAccess() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testPrivateField", new PrivateFieldValue(42));

 Class<AutomaticBean> clazz = AutomaticBean.class;
 Field privateField = FieldUtils.getField(clazz, "privateField", Accessible.PRIVATE);
 privateField.setAccessible(true);
 Object value = new PrivateFieldValue(43);

 try {
 automaticBean.tryCopyProperty("AutomaticBean", "testPrivateField", value, false);
 fail("Should throw exception");
 } catch (IllegalAccessException e) {
 // Expected
 }
 }

 @Test
 public void testTryCopyPropertyWithNoSuchMethod() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testMethod", new PrivateMethodValue());

 Class<AutomaticBean> clazz = AutomaticBean.class;
 Method method = MethodUtils.getAccessibleMethod(clazz, "setPrivateMethod", Accessible.PRIVATE);
 method.setAccessible(true);
 Object value = new PrivateMethodValue();

 try {
 automaticBean.tryCopyProperty("AutomaticBean", "testMethod", value, false);
 fail("Should throw exception");
 } catch (NoSuchMethodException e) {
 // Expected
 }
 }

 @Test
 public void testRelaxedStringArrayConverter() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testStringArray", "element1, element2, element3");

 automaticBean.configure(config);

 String[] expectedArray = new String[]{"element1", "element2", "element3"};
 assertArrayEquals(expectedArray, (String[])automaticBean.getConfiguration().getAttribute("testStringArray"));
 }

 @Test
 public void testRelaxedStringArrayConverterWithEmptyString() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testStringArray", "element1,,element3");

 automaticBean.configure(config);

 String[] expectedArray = new String[]{"element1", "", "element3"};
 assertArrayEquals(expectedArray, (String[])automaticBean.getConfiguration().getAttribute("testStringArray"));
 }

 @Test
 public void testRelaxedStringArrayConverterWithNull() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testStringArray", "element1, ,element3, null");

 automaticBean.configure(config);

 String[] expectedArray = new String[]{"element1", "", "element3", null};
 assertArrayEquals(expectedArray, (String[])automaticBean.getConfiguration().getAttribute("testStringArray"));
 }

 @Test
 public void testRelaxedStringArrayConverterWithEmptyList() throws Exception {
 Configuration config = new Configuration();
 config.addAttribute("testStringArray", Arrays.asList("element1", Collections.emptyList()));

 automaticBean.configure(config);

 String[] expectedArray = new String[]{"element1", ""};
 assertArrayEquals(expectedArray, (String[])automaticBean.getConfiguration().getAttribute("testStringArray"));
 }

 private static class PrivateFieldValue {
 private int value;

 PrivateFieldValue(int value) {
 this.value = value;
 }
 }

 private static class PrivateMethodValue {
 public void setPrivateMethod(int value) {
 // do nothing
 }
 }

 private static void assertArrayEquals(Object expected, Object actual) {
 if (!expected.getClass().isArray() || !actual.getClass().isArray() || expected.getClass() != actual.getClass()) {
 throw new AssertionError("Expected and actual not arrays of the same type");
 }

 Object[] expectedArray = (Object[])expected;
 Object[] actualArray = (Object[])actual;
 assertArrayEquals(expectedArray, actualArray, null);
 }

 private static void assertArrayEquals(Object[] expected, Object[] actual, String message) {
 if (expected.length != actual.length) {
 fail("Array lengths differ: expected " + expected.length + " actual " + actual.length);
 }

 for (int i = 0; i < expected.length; i++) {
 if (!expected[i].equals(actual[i])) {
 fail(message + ": expected " + expected[i] + " actual " + actual[i]);
 }
 }
 }
}