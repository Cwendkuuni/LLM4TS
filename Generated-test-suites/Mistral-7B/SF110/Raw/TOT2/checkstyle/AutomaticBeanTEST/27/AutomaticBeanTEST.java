package com.example;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class AutomaticBeanTest {

 @Test
 public void testConfigure() {
 AutomaticBean automaticBean = new AutomaticBean();
 Configuration config = new Configuration();
 automaticBean.configure(config);
 assertNotNull(automaticBean.getConfiguration());
 }

 @Test
 public void testContextualize() {
 AutomaticBean automaticBean = new AutomaticBean();
 Context context = new Context();
 context.put("key1", "value1");
 context.put("key2", "value2");
 automaticBean.contextualize(context);
 assertEquals("value1", automaticBean.tryGetProperty("key1"));
 assertEquals("value2", automaticBean.tryGetProperty("key2"));
 }

 @Test
 public void testTryCopyProperty() {
 AutomaticBean automaticBean = new AutomaticBean();
 Configuration config = new Configuration();
 config.setAttribute("key1", "value1");
 automaticBean.configure(config);
 assertEquals("value1", automaticBean.tryGetProperty("key1"));

 Context context = new Context();
 context.put("key2", "value2");
 automaticBean.contextualize(context);
 assertEquals("value2", automaticBean.tryGetProperty("key2"));
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithNullConfig() {
 AutomaticBean automaticBean = new AutomaticBean();
 automaticBean.configure(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidAttribute() {
 Configuration config = new Configuration();
 config.setAttribute("key1", null);
 AutomaticBean automaticBean = new AutomaticBean();
 automaticBean.configure(config);
 }

 @Test(expected = CheckstyleException.class)
 public void testCopyPropertyFailure() {
 AutomaticBean automaticBean = new AutomaticBean();
 Configuration config = new Configuration();
 config.setAttribute("key1", "value1");
 automaticBean.configure(config);
 try {
 automaticBean.tryCopyProperty(null, "key1", null, true);
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithNullContext() {
 AutomaticBean automaticBean = new AutomaticBean();
 automaticBean.contextualize(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidKey() {
 Context context = new Context();
 context.put("invalidKey", "value");
 AutomaticBean automaticBean = new AutomaticBean();
 automaticBean.contextualize(context);
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithNullName() {
 AutomaticBean automaticBean = new AutomaticBean();
 Configuration config = new Configuration();
 config.setAttribute("key1", "value1");
 automaticBean.configure(config);
 try {
 automaticBean.tryCopyProperty(null, null, null, true);
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithNullValue() {
 AutomaticBean automaticBean = new AutomaticBean();
 Configuration config = new Configuration();
 config.setAttribute("key1", "value1");
 automaticBean.configure(config);
 try {
 automaticBean.tryCopyProperty("propertyName", null, null, true);
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithUnsupportedType() {
 AutomaticBean automaticBean = new AutomaticBean();
 Configuration config = new Configuration();
 config.setAttribute("key1", new Object());
 automaticBean.configure(config);
 try {
 automaticBean.tryCopyProperty("propertyName", "value", new Object(), true);
 } catch (CheckstyleException e) {
 // Expected
 }
 }
}