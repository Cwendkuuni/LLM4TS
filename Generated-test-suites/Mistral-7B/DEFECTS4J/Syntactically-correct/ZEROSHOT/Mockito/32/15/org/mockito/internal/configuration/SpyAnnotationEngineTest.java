package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 @Mock
 private AnnotationEngine mockAnnotationEngine;
 @Spy
 private Field testField;
 @Spy
 private Object testInstance;

 @Before
 public void before() {
 annotationEngine = new SpyAnnotationEngine() {
 @Override
 public AnnotationEngine getAnnotationEngine() {
 return mockAnnotationEngine;
 }
 };
 }

 @Test
 public void testCreateMockFor() {
 // The createMockFor method is empty, so we don't need to test it
 }

 @Test
 public void testProcess_NoSpyAnnotation() {
 // Arrange
 Class context = this.getClass();
 Object testClass = this;

 // Act
 annotationEngine.process(context, testClass);

 // Assert
 // Since no spy annotation is present, nothing should have happened
 }

 @Test
 public void testProcess_SpyAnnotationPresent_InstanceNotFound() {
 // Arrange
 testField = FieldUtils.getField(SpyAnnotationEngineTest.class, "testField");
 testField.setAccessible(true);
 Object testClass = this;

 // Act & Assert
 annotationEngine.process(context, testClass);

 // Verify no changes were made to the test instance
 assertSame(testInstance, testField.get(testClass));
 }

 @Test
 public void testProcess_SpyAnnotationPresent_InstanceAlreadyMocked() {
 // Arrange
 testField = FieldUtils.getField(SpyAnnotationEngineTest.class, "testField");
 testField.setAccessible(true);
 testInstance = mock(String.class);
 testField.set(this, testInstance);
 MockitoAnnotations.initMocks(this);
 Object testClass = this;

 // Act & Assert
 annotationEngine.process(context, testClass);

 // Verify the instance was not modified since it's already mocked
 assertEquals(testInstance, testField.get(testClass));
 }

 @Test
 public void testProcess_SpyAnnotationPresent_InstanceNotMocked() {
 // Arrange
 testField = FieldUtils.getField(SpyAnnotationEngineTest.class, "testField");
 testField.setAccessible(true);
 testInstance = new String("test");
 testField.set(this, testInstance);
 Object testClass = this;

 // Act
 annotationEngine.process(context, testClass);

 // Assert
 // The instance should now be spied
 verify(mockAnnotationEngine).spy(testInstance);
 assertEquals(testInstance, testField.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_InstanceMissing() {
 // Arrange
 testField = FieldUtils.getField(SpyAnnotationEngineTest.class, "testField");
 testField.setAccessible(true);
 Object testClass = this;

 // Act & Assert
 annotationEngine.process(context, testClass);
 }

 @Test
 public void testProcess_SpyAnnotationPresent_MultipleInstances() {
 // Arrange
 testField1 = FieldUtils.getField(SpyAnnotationEngineTest.class, "testField1");
 testField1.setAccessible(true);
 testField2 = FieldUtils.getField(SpyAnnotationEngineTest.class, "testField2");
 testField2.setAccessible(true);
 testInstance1 = new String("test1");
 testInstance2 = new String("test2");
 testField1.set(this, testInstance1);
 testField2.set(this, testInstance2);
 Object testClass = this;

 // Act
 annotationEngine.process(context, testClass);

 // Assert
 // Both instances should now be spied
 verify(mockAnnotationEngine).spy(testInstance1);
 verify(mockAnnotationEngine).spy(testInstance2);
 assertEquals(testInstance1, testField1.get(testClass));
 assertEquals(testInstance2, testField2.get(testClass));
 }

 @Test
 public void testAssertNoAnnotations() {
 // Arrange
 testField = FieldUtils.getField(SpyAnnotationEngineTest.class, "testField");
 testField.setAccessible(true);
 testInstance = new String("test");
 testField.set(this, testInstance);
 Object testClass = this;

 // Act & Assert
 annotationEngine.assertNoAnnotations(Spy.class, testField, Mock.class, Mockito.Mock.class, ArgumentCaptor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_SpyAnnotationPresent() {
 // Arrange
 testField = FieldUtils.getField(SpyAnnotationEngineTest.class, "testField");
 testField.setAccessible(true);
 testInstance = new String("test");;
 testField.set(this, testInstance);
 Object testClass = this;

 // Arrange a spy annotation
 testField.setAnnotation(new Spy());

 // Act & Assert
 annotationEngine.assertNoAnnotations(Spy.class, testField, Mock.class, Mockito.Mock.class, ArgumentCaptor.class);
 }
}