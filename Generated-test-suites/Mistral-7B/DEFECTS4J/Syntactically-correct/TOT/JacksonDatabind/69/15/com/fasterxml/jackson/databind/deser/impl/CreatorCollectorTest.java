package com.fasterxml.jackson.databind.deser.impl;

 import static org.junit.Assert.*;

 import java.io.IOException;
 import java.lang.reflect.InvocationTargetException;
 import java.util.*;

 import com.fasterxml.jackson.annotation.*;
 import com.fasterxml.jackson.databind.*;
 import com.fasterxml.jackson.databind.cfg.MapperFactory;
 import com.fasterxml.jackson.databind.deser.*;
 import com.fasterxml.jackson.databind.deser.impl.*;
 import com.fasterxml.jackson.databind.deser.std.*;
 import com.fasterxml.jackson.databind.introspect.*;
 import com.fasterxml.jackson.databind.util.*;
 import com.fasterxml.jackson.core.type.*;

 import org.junit.*;
 import org.junit.experimental.categories.*;

 @Category(IntegrationTests.class)
 public class CreatorCollectorTest {

 private static final BeanDescription BEAN_DESC = new BeanDescription(StandardMapper.getDefaultInstance().introspect(TestBean.class));

 @Test
 public void testDefaultCreator() throws Exception {
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance());
 assertEquals(0, bean.getIntField());
 assertEquals("", bean.getStringField());
 }

 @Test
 public void testStringCreator() throws Exception {
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), "test", "testStr");
 assertEquals("test", bean.getStringField());
 }

 @Test
 public void testIntCreator() throws Exception {
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), 10);
 assertEquals(10, bean.getIntField());
 }

 @Test
 public void testLongCreator() throws Exception {
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), 10L);
 assertEquals(10L, bean.getLongField());
 }

 @Test
 public void testDoubleCreator() throws Exception {
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), 10.0);
 assertEquals(10.0, bean.getDoubleField(), 0.001);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), true);
 assertTrue(bean.isBooleanField());
 }

 @Test
 public void testDelegateCreator() throws Exception {
 TestDelegate delegate = new TestDelegate();
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), delegate);
 assertEquals(delegate, bean.getDelegateField());
 }

 @Test
 public void testArrayDelegateCreator() throws Exception {
 TestDelegate[] delegates = new TestDelegate[2];
 delegates[0] = new TestDelegate();
 delegates[1] = new TestDelegate();
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), delegates);
 assertEquals(delegates, bean.getDelegateArrayField());
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 TestPropertyBasedBean propertyBasedBean = new TestPropertyBasedBean();
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), propertyBasedBean);
 assertEquals(propertyBasedBean.getIntField(), bean.getIntField());
 assertEquals(propertyBasedBean.getStringField(), bean.getStringField());
 }

 @Test
 public void testIncompleteParameter() throws Exception {
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), null, 10);
 assertEquals(10, bean.getIntField());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicatePropertyCreator() throws Exception {
 TestBean bean = new TestBean();
 TestPropertyBasedBean propertyBasedBean1 = new TestPropertyBasedBean();
 TestPropertyBasedBean propertyBasedBean2 = new TestPropertyBasedBean();
 propertyBasedBean1.setIntField(1);
 propertyBasedBean2.setIntField(2);
 CreatorCollector collector = new CreatorCollector(BEAN_DESC, new MapperFactory(StandardMapper.getDefaultInstance()));
 collector.addPropertyCreator(TestPropertyBasedBean.class.getDeclaredMethod("getIntField", null), null, new SettableBeanProperty[]{propertyBasedBean1, propertyBasedBean2});
 try {
 TestBean createdBean = collector.constructValueInstantiator(StandardMapper.getDefaultInstance()).getValueInstance(null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testInvalidCreator() throws Exception {
 TestBean bean = CreatorCollector.createValueInstance(BEAN_DESC, StandardMapper.getDefaultInstance(), new Object());
 assertEquals(0, bean.getIntField());
 }

 private static TestBean createValueInstance(BeanDescription beanDesc, ObjectCreator creator, Object... args)
 throws IOException {
 ObjectInitContext context = ObjectInitContext.emptyBase();
 DeserializationContext ctxt = new TestDeserializationContext();
 context.setActiveInitializer(creator);
 context.setCurrentValue(BeanProperty.Value.nullValue());
 context.setBeanDescription(beanDesc);
 context.setContextualValueFactory(beanDesc.getValueFactory());
 context.setContextualBeanPropertyFactory(beanDesc.getBeanPropertyFactory());
 context.setDeserializationConfig(ctxt.getConfig());
 context.setContextualTypeResolver(beanDesc.getTypeResolver());
 context.setBeanContext(ctxt.getActiveContext());
 context.setBeanContextualizer(beanDesc.getBeanContextualizer());
 context.setActiveConstructor(beanDesc.getUndefOrDefaultConstructor());
 try {
 return (TestBean) creator.valueInstance(context, args);
 } catch (InvocationTargetException e) {
 throw e.getTargetException();
 }
 }
 }

 class TestBean {
 private int intField;
 private String stringField;
 private boolean booleanField;
 private TestDelegate delegateField;
 private TestDelegate[] delegateArrayField;
 private TestPropertyBasedBean propertyBasedBean;

 public int getIntField() { return intField; }
 public String getStringField() { return stringField; }
 public boolean isBooleanField() { return booleanField; }
 public TestDelegate getDelegateField() { return delegateField; }
 public TestDelegate[] getDelegateArrayField() { return delegateArrayField; }
 public void setDelegateField(TestDelegate delegateField) { this.delegateField = delegateField; }
 public void setDelegateArrayField(TestDelegate[] delegateArrayField) { this.delegateArrayField = delegateArrayField; }

 @JsonProperty("intField")
 public int intField;
 @JsonProperty("stringField")
 public String stringField;
 @JsonProperty("booleanField")
 public boolean booleanField;
 @JsonProperty("delegateField")
 @JsonIgnore
 public TestDelegate delegateField;
 @JsonProperty("delegateArrayField")
 @JsonIgnore
 public TestDelegate[] delegateArrayField;
 }

 class TestDelegate {
 }

 class TestPropertyBasedBean {
 private int intField;
 private String stringField;

 public int getIntField() { return intField; }
 public String getStringField() { return stringField; }
 public void setIntField(int intField) { this.intField = intField; }
 public void setStringField(String stringField) { this.stringField = stringField; }

 @JsonProperty("intField")
 public int intField;
 @JsonProperty("stringField")
 public String stringField;
 }

 class TestDeserializationContext extends DeserializationContext {
 @Override
 public ObjectMapper getConfig() { return StandardMapper.getDefaultInstance(); }
 @Override
 public ObjectReader getRootValueFactory() { return null; }
 @Override
 public ObjectFactory getFactory() { return null; }
 @Override
 public BeanDescription getActiveValueHandlerDescription() { return null; }
 @Override
 public JavaType getTypeFactory() { return null; }
 @Override
 public JavaType getRootValueType() { return null; }
 @Override
 public ObjectReader getValueHandler() { return null; }
 @Override
 public void configure(DeserializationConfig config, MapperConfig<?> mapperConfig) {}
 }