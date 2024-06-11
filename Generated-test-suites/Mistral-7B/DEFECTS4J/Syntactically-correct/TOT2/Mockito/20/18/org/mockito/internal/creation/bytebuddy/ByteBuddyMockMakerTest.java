import org.junit.jupiter.api.Assertions;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Class;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMakerTest {

   @Test
   public void testConstructor() {
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      assertNotNull(byteBuddyMockMaker.classInstantiator);
      assertNotNull(byteBuddyMockMaker.cachingMockBytecodeGenerator);
   }

   @SuppressWarnings("unchecked")
   @Test
   public <T> void testCreateMockSuccess() throws Exception {
      Class<MockitoSpy<String>> typeToMock = MockitoSpy.class;
      MockHandler handler = mock(MockHandler.class);
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      T mock = byteBuddyMockMaker.createMock(new MockCreationSettings<T>() {
         @Override
         public Class<T> getTypeToMock() {
            return typeToMock;
         }
      }, handler);
      assertInstanceOf(typeToMock, mock);
      verify(handler).handle(eq(any()));
   }

   @Test
   public void testCreateMockSerializationException() {
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {
         @Override
         public Class<Object> getTypeToMock() {
            return Object.class;
         }

         @Override
         public SerializableMode getSerializableMode() {
            return SerializableMode.ACROSS_CLASSLOADERS;
         }
      };
      assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, mock(MockHandler.class)));
   }

   @Test
   public void testCreateMockInstantiationException() {
      Class<String> typeToMock = String.class;
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(new MockCreationSettings<String>() {
         @Override
         public Class<String> getTypeToMock() {
            return typeToMock;
         }
      }, mock(MockHandler.class)));
   }

   @Test
   public <T> void testEnsureMockIsAssignableToMockedType() {
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      Class<MockitoSpy<String>> typeToMock = MockitoSpy.class;
      MockHandler handler = mock(MockHandler.class);
      T mock = byteBuddyMockMaker.createMock(new MockCreationSettings<T>() {
         @Override
         public Class<T> getTypeToMock() {
            return typeToMock;
         }
      }, handler);
      Class<T> castType = typeToMock.cast(mock);
      assertEquals(typeToMock, castType);
   }

   @Test
   public void testGetHandler() {
      Object mock = mock(MockHandler.class);
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      MockHandler result = byteBuddyMockMaker.getHandler(mock);
      assertEquals(mock, result);
   }

   @Test
   public void testResetMock() {
      Object mock = mock(Object.class);
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      MockHandler newHandler = mock(MockHandler.class);
      ByteBuddyMockMaker.ResetMockHandler resetMockHandler = byteBuddyMockMaker.resetMock(mock, newHandler, null);
      assertSame(newHandler, resetMockHandler);
   }

   @Test
   public void testInitializeClassInstantiator() {
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
      Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
      ClassInstantiator result = (ClassInstantiator) usingClassCacheConstructor.newInstance(true);
      ClassInstantiator expected = byteBuddyMockMaker.initializeClassInstantiator();
      assertEquals(expected, result);
   }

   @Test
   public void testDescribeClass() {
      byte[] byteCode = new byte[0];
      Class<?> mockClass = defineClass("MockClass", byteCode);
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      String result = byteBuddyMockMaker.describeClass(mockClass);
      String expected = "class MockClass, loaded by classloader : " + mockClass.getClassLoader();
      assertEquals(expected, result);
   }

   private Class<?> defineClass(String name, byte[] bytes) throws Exception {
      ClassLoader classLoader = new URLClassLoader(new URL[]{}, this.getClass().getClassLoader());
      return classLoader.defineClass(name, bytes, 0, bytes.length);
   }

   @Test
   public void testResetMock_NullMockHandler() {
      Object mock = mock(Object.class);
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      assertNull(byteBuddyMockMaker.resetMock(mock, null, null));
   }

   @Test
   public void testResetMock_NullNewHandler() {
      Object mock = mock(Object.class);
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      assertThrows(NullPointerException.class, () -> byteBuddyMockMaker.resetMock(mock, null, null));
   }

   @Test
   public void testResetMock_NonMockAccessMock() {
      Object mock = new Object() {};
      ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
      assertThrows(MockitoException.class, () -> byteBuddyMockMaker.resetMock(mock, mock(MockHandler.class), null));
   }
}