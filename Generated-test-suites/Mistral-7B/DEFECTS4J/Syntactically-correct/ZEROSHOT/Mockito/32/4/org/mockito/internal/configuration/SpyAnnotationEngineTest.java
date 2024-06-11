package org.mockito.internal.configuration;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 @Mock private Object testClass;
 @Mock private Annotation mockAnnotation;
 @Spy private Object spyInstance;
 @Captor private ArgumentCaptor<Annotation> annotationCaptor;

 @Before
 public void setUp() {
 annotationEngine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockFor() throws Exception {
 assertNull(annotationEngine.createMockFor(mockAnnotation, null));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_NullContext() {
 annotationEngine.process(null, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_NullTestClass() {
 annotationEngine.process(TestContext.class, null);
 }

 @Test
 public void testProcess_NoSpyAnnotation() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithoutAnnotation");
 annotationEngine.process(TestContext.class, testClass);
 assertFalse(field.isAccessible());
 assertNull(field.get(testClass));
 }

 @Test
 public void testProcess_SpyAnnotationPresent() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithSpyAnnotation");
 annotationEngine.process(TestContext.class, testClass);
 assertTrue(field.isAccessible());
 assertEquals(spyInstance, field.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_NullInstance() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithSpyAnnotation");
 field.setAccessible(true);
 field.set(testClass, null);
 annotationEngine.process(TestContext.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_MockInstance() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithSpyAnnotation");
 field.setAccessible(true);
 @Mock Object mockInstance = mock(Object.class);
 field.set(testClass, mockInstance);
 annotationEngine.process(TestContext.class, testClass);
 }

 @Test
 public void testProcess_SpyAnnotationPresent_NonMockInstance() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithSpyAnnotation");
 field.setAccessible(true);
 field.set(testClass, spyInstance);
 annotationEngine.process(TestContext.class, testClass);
 assertSame(spyInstance, field.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_IllegalAccess() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithSpyAnnotation");
 annotationEngine.process(TestContext.class, testClass);
 field.setAccessible(false);
 annotationEngine.process(TestContext.class, testClass);
 }

 @Test
 public void testAssertNoAnnotations() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithSpyAnnotation");
 annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_SpyAnnotationPresent_MockAnnotationPresent() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithSpyAnnotation");
 field.setAccessible(true);
 @Mock Object mockAnnotationInstance = mock(Annotation.class);
 field.set(testClass, mockAnnotationInstance);
 annotationEngine.process(TestContext.class, testClass);
 annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test
 public void testAssertNoAnnotations_SpyAnnotationPresent_CaptorAnnotationPresent() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithSpyAnnotation");
 @Captor ArgumentCaptor<Annotation> captor = ArgumentCaptor.forClass(Spy.class);
 field.setAccessible(true);
 field.set(testClass, spyInstance);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 doReturn(captor.getValue()).when(invocation).getValue(eq(Spy.class));
 annotationEngine.process(TestContext.class, testClass);
 annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test
 public void testProcess_MultipleSpyAnnotations() throws Exception {
 Field field1 = TestContext.class.getDeclaredField("fieldWithSpyAnnotation1");
 Field field2 = TestContext.class.getDeclaredField("fieldWithSpyAnnotation2");
 annotationEngine.process(TestContext.class, testClass);
 assertSame(spyInstance, field1.get(testClass));
 assertSame(spyInstance, field2.get(testClass));
 }

 @Test
 public void testProcess_SpyAnnotationPresentWithMocks() throws Exception {
 Field field = TestContext.class.getDeclaredField("fieldWithMocksAndSpyAnnotation");
 annotationEngine.process(TestContext.class, testClass);
 assertSame(mockInstance1, field.get(testClass));
 assertSame(mockInstance2, field.get(field.get(testClass)));
 }

 class TestContext {
 @Spy Object fieldWithSpyAnnotation;
 Object fieldWithoutAnnotation;
 Object fieldWithMocksAndSpyAnnotation;
 @Mock Object mockInstance1;
 @Mock Object mockInstance2;
 }

 class MyAnswer implements Answer<Object> {
 Object value;
 MyAnswer(Object value) {
 this.value = value;
 }

 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 this.value = invocation.callRealMethod();
 return null;
 }
 }
}