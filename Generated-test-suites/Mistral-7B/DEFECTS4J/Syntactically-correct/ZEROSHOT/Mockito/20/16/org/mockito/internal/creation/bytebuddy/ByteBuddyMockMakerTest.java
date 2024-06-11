package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMocker;
import org.mockito.quality.Strictness;
import org.powermock.modules.junit4.PowerMockRule;

import java.lang.reflect.Constructor;
import java.util.Arrays;

@RunWith(MockMakerMocker.class) @MockMaker(ByteBuddyMockMaker.class)
public class ByteBuddyMockMakerTest {

 @Rule public PowerMockRule powerMockRule = new PowerMockRule();
 private MockMaker mockMaker;
 private ByteBuddyMockMaker byteBuddyMockMaker;
 private ClassInstantiator classInstantiator;
 private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

 @Before public void setup() {
 mockMaker = MockMaker.createMultiMock(new Class[]{ByteBuddyMockMaker.class, ClassInstantiator.class, CachingMockBytecodeGenerator.class});
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 classInstantiator = mockMaker.getInstance(ClassInstantiator.class);
 cachingMockBytecodeGenerator = mockMaker.getInstance(CachingMockBytecodeGenerator.class);
 }

 @org.junit.Test public void testCreateMock() throws Exception {
 // Test creating a mock with no extra interfaces
 Class<TestClass> testClass = TestClass.class;
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 TestClass mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(TestClass.class, Arrays.asList()), mockHandler);
 Mockito.verify(mockHandler, Mockito.atLeastOnce()).handle(Mockito.any(), Mockito.eq(TestClass.class), Mockito.eq(null));

 // Test creating a mock with extra interfaces
 Class<ExtraInterface> extraInterface = ExtraInterface.class;
 MockHandler extraMockHandler = Mockito.mock(MockHandler.class);
 TestClass mockWithExtraInterfaces = byteBuddyMockMaker.createMock(new MockCreationSettings<>(TestClass.class, Arrays.asList(extraInterface)), mockHandler);
 Mockito.verify(mockHandler, Mockito.atLeastOnce()).handle(Mockito.any(), Mockito.eq(TestClass.class), Mockito.isInstanceOf(extraInterface));

 // Test serialization exception
 try {
 byteBuddyMockMaker.createMock(new MockCreationSettings<>(TestClass.class, Arrays.asList(TestClass.class), SerializableMode.ACROSS_CLASSLOADERS), mockHandler);
 } catch (MockitoException e) {
 Mockito.assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
 }
 }

 @org.junit.Test public void testGetHandler() throws Exception {
 TestClass mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(TestClass.class), Mockito.mock(MockHandler.class));
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);
 Mockito.assertEquals(mockHandler, handler);
 }

 @org.junit.Test public void testResetMock() throws Exception {
 TestClass mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(TestClass.class), Mockito.mock(MockHandler.class));
 MockHandler newMockHandler = Mockito.mock(MockHandler.class);
 byteBuddyMockMaker.resetMock(mock, newMockHandler, new MockCreationSettings<>(TestClass.class, Arrays.asList()));
 Mockito.assertEquals(newMockHandler, byteBuddyMockMaker.getHandler(mock));
 }

 @org.junit.Test public void testDescribeClass() throws Exception {
 Class<?> testClassType = TestClass.class;
 Mockito.assertEquals("'TestClass', loaded by classloader : 'sun.misc.Unsafe$1'", byteBuddyMockMaker.describeClass(testClassType));
 }

 @org.junit.Test public void testDescribeClassNull() throws Exception {
 Mockito.assertEquals("'null'", byteBuddyMockMaker.describeClass(null));
 }

 @org.junit.Test public void testDescribeClassObject() throws Exception {
 TestClass testClass = new TestClass();
 Mockito.assertEquals("'TestClass', loaded by classloader : 'sun.misc.Unsafe$1'", byteBuddyMockMaker.describeClass(testClass.getClass()));
 }

 @org.junit.Test public void testDescribeClassNullObject() throws Exception {
 Mockito.assertEquals("'null'", byteBuddyMockMaker.describeClass(null));
 }
}

 class TestClass {
 // Add any necessary methods or fields here
 }

 interface ExtraInterface {
 // Add any necessary methods or fields here
 }