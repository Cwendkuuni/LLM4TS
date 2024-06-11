package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldAccessException;
import org.mockito.mock.MockComponent;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Set<Field> injectMocksFields;
 private Object testClassInstance;

 @Before
 public void setUp() {
 mocks = new HashSet<>();
 injectMocksFields = new HashSet<>();
 testClassInstance = mock(TestClass.class);
 injectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 // given
 injectMocksFields.add(new FieldTest1());
 injectMocksFields.add(new FieldTest2());
 injectMocksFields.add(new FieldTest3());

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // then
 verify(testClassInstance, VerificationMode.INVERSE).setFieldTest1(Mockito.any());
 verify(testClassInstance, VerificationMode.INVERSE).setFieldTest2(Mockito.any());
 verify(testClassInstance, VerificationMode.INVERSE).setFieldTest3(Mockito.any());

 // test injection order
 verify(testClassInstance).getFieldTest3_1().getName().equals("test3_1");
 verify(testClassInstance).getFieldTest3_2().getName().equals("test3_2");
 }

 @Test(expected = BaseMockitoException.class)
 public void testInjectMocksOnFieldsWithMultipleCandidates() {
 // given
 injectMocksFields.add(new FieldTest4());
 injectMocksFields.add(new FieldTest5());

 // when and then
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testInjectMockCandidate() {
 // given
 Class<?> awaitingInjectionClazz = FieldTest6.class;
 MockComponent mockComponent = mock(MockComponent.class);
 mocks.add(mockComponent);

 // when
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

 // then
 verify(mockComponent).getName().equals("mockComponent");
 }

 @Test
 public void testInjectMockCandidateWithNoCandidate() {
 // given
 injectMocksFields.add(new FieldTest7());

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // then
 verifyZeroInteractions(testClassInstance);
 }

 @Test
 public void testInjectMockCandidateWithFieldAccessException() {
 // given
 injectMocksFields.add(new FieldTest8());

 // when and then
 try {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 } catch (FieldAccessException e) {
 // then
 verifyNoInteractions(testClassInstance);
 }
 }

 @Test
 public void testInjectMockCandidateWithInvalidFieldType() {
 // given
 injectMocksFields.add(new FieldTest9());

 // when and then
 try {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 } catch (ClassCastException e) {
 // then
 verifyNoInteractions(testClassInstance);
 }
 }

 @Test
 public void testInjectMockCandidateWithMockCandidateFilter() {
 // given
 injectMocksFields.add(new FieldTest10());

 // when
 injectionEngine.injectMockCandidate(FieldTest10.class, mocks, testClassInstance);

 // then
 verify(testClassInstance).setFieldTest10(Mockito.any());
 }

 static class FieldTest1 {
 private TestComponent testComponent;

 public void setFieldTest1(TestComponent testComponent) {
 this.testComponent = testComponent;
 }

 public TestComponent getTestComponent() {
 return testComponent;
 }
 }

 static class FieldTest2 {
 private TestComponent testComponent;

 @Mock
 public TestComponent getTestComponent() {
 return testComponent;
 }

 public void setFieldTest2(TestComponent testComponent) {
 this.testComponent = testComponent;
 }
 }

 static class FieldTest3 extends FieldTest2 {
 private TestComponent3 testComponent3;

 public TestComponent3 getFieldTest3_1() {
 return testComponent3;
 }

 public void setFieldTest3(TestComponent3 testComponent3) {
 this.testComponent3 = testComponent3;
 }

 public static class TestComponent3 {}
 }

 static class FieldTest4 {
 @Mock
 private TestComponent testComponent;
 }

 static class FieldTest5 {
 @Mock
 private TestOtherComponent testOtherComponent;
 }

 static class FieldTest6 {
 private TestComponent fieldTest6;

 public void setFieldTest6(TestComponent testComponent) {
 this.fieldTest6 = testComponent;
 }
 }

 static class FieldTest7 {
 private TestComponent fieldTest7;
 }

 static class FieldTest8 {
 @Mock
 private FieldTest8Mock fieldTest8Mock;

 private String name;

 public void setName(String name) {
 this.name = name;
 }

 public String getName() {
 return name;
 }}

 static class FieldTest8Mock {
 public String getMockName() {
 return "mockComponent";
 }
 }

 static class FieldTest9 {
 private TestComponent fieldTest9;

 public void setFieldTest9(int i) {
 this.fieldTest9 = new TestComponent();
 }
 }

 static class FieldTest10 {
 @Mock
 private TestComponent10 testComponent10;

 public void setFieldTest10(TestComponent10 testComponent10) {
 this.testComponent10 = testComponent10;
 }
 }

 static class TestComponent {}
 static class TestComponent10 {
 private TestComponent11 testComponent11;

 public TestComponent11 getTestComponent11() {
 return testComponent11;
 }

 public void setTestComponent11(TestComponent11 testComponent11) {
 this.testComponent11 = testComponent11;
 }
 }

 static class TestComponent11 {
 private String name;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
 }

 static class TestOtherComponent {}
}