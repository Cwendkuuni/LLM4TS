package com.lts.event.test;

import static org.junit.Assert.*;
import com.lts.event.SimpleKeyListenerHelper;
import com.lts.event.SimpleKeyListener;
import java.awt.*;
import java.awt.event.*;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

 @Testpublic void testConstructor() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame()); assertNotNull(helper);
 }

 @Testpublic void testDetach() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame()); helper.detach(); assertNull(helper.component);
 }

 @Test(expected = IllegalStateException.class)public void testDetachAlreadyDetached() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame()); helper.detach(); helper.detach();
 }

 @Testpublic void testNotifyListenerEnter() {
 SimpleKeyListener listener = mock(SimpleKeyListener.class); when(listener.enter(any(Component.class))).thenReturn(true);
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame()); helper.component = new JLabel(); helper.notifyListener(listener, 10, null); verify(listener).enter(helper.component);
 }

 @Testpublic void testNotifyListenerInsert() {
 SimpleKeyListener listener = mock(SimpleKeyListener.class); when(listener.insert(any(Component.class))).thenReturn(true);
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new JFrame()); helper.component = new JLabel(); helper.notifyListener(listener, 155, null); verify(listener).insert(helper.component);
 }

 // Test other methods and edge cases
 // ...

 @AfterClasspublic static void cleanup() {
 // Cleanup resources
 }

}