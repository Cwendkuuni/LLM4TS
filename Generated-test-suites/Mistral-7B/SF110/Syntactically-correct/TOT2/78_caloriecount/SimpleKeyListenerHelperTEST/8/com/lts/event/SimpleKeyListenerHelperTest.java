package com.lts.event;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
@Test
public void testSimpleKeyListenerHelperConstructor() {
Component component = new JFrame(); // a valid component
SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
assertNotNull(simpleKeyListenerHelper);
}

// Test case for the constructor method with null component
@Test(expected = NullPointerException.class)
public void testSimpleKeyListenerHelperConstructorNullComponent() {
SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
}

// Test case for the detach method
@Test
public void testDetachMethod() {
SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
simpleKeyListenerHelper.detach();
assertNull(simpleKeyListenerHelper.component);
}

// Test case for the detach method with null component
@Test
public void testDetachMethodNullComponent() {
SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
simpleKeyListenerHelper.detach();
assertNull(simpleKeyListenerHelper.component);
}

// Test case for the notifyListener method with valid argument
@Test
public void testNotifyListenerValidArguments() {
SimpleKeyListener listener = mock(SimpleKeyListener.class);
SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
simpleKeyListenerHelper.notifyListener(listener, 10, this);
verify(listener).enter(argThat(instanceOf(Component.class)));
}

// Test case for the notifyListener method with invalid argument type
@Test(expected = IllegalArgumentException.class)
public void testNotifyListenerInvalidArgumentType() {
SimpleKeyListener listener = new SimpleKeyListener();
SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
simpleKeyListenerHelper.notifyListener(listener, 3, this);
}

// Test case for the keyPressed method with valid KeyEvent
@Test
public void testKeyPressedValidKeyEvent() {
KeyEvent keyEvent = new KeyEvent(new JFrame(), 1, KeyEvent.KEY_PRESSED, 0);
SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
simpleKeyListenerHelper.keyPressed(keyEvent);
simpleKeyListenerHelper.fire(keyEvent.getKeyCode());
verify(listener).enter(argThat(instanceOf(Component.class)));
}

// Test case for the keyPressed method with an unhandled KeyEvent
@Test(expected = UnsupportedOperationException.class)
public void testKeyPressedUnhandledKeyEvent() {
SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame());
KeyEvent keyEvent = new KeyEvent(new JFrame(), 1, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_F1, "F1");
simpleKeyListenerHelper.keyPressed(keyEvent);
}
}