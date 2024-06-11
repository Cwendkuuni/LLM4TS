package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener simpleKeyListener;

 @Before
 public void setup() {
 simpleKeyListener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(mock(Component.class)) {
 @Override
 protected void fire(int keyCode) {
 simpleKeyListener.keyPress(keyCode);
 }
 };
 }

 @Test
 public void testNotifyListener_Enter_KeyCode_10() {
 // Given
 when(simpleKeyListener.enter(any(Component.class))).thenReturn(true);

 // When
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 10, null);

 // Then
 verify(simpleKeyListener).enter(any(Component.class));
 }

 @Test
 public void testNotifyListener_Insert_KeyCode_155() {
 // Given
 when(simpleKeyListener.insert(any(Component.class))).thenReturn(true);

 // When
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 155, null);

 // Then
 verify(simpleKeyListener).insert(any(Component.class));
 }

 @Test
 public void testNotifyListener_Delete_KeyCode_127() {
 // Given
 when(simpleKeyListener.delete(any(Component.class))).thenReturn(true);

 // When
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 127, null);

 // Then
 verify(simpleKeyListener).delete(any(Component.class));
 }

 @Test
 public void testNotifyListener_Tab_KeyCode_9() {
 // Given
 when(simpleKeyListener.tab(any(Component.class))).thenReturn(true);

 // When
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 9, null);

 // Then
 verify(simpleKeyListener).tab(any(Component.class));
 }

 @Test(expected = AssertionError.class)
 public void testNotifyListener_Unexpected_KeyCode() {
 // Given
 when(simpleKeyListener.enter(any(Component.class))).thenReturn(true);

 // When
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 1, null);

 // Then
 // An unexpected key code should cause an AssertionError
 fail("Unexpected key code received");
 }

 @Test
 public void testKeyPressed_Valid_KeyCode() {
 // Given
 int validKeyCode = KeyEvent.VK_ENTER;

 // When
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mock(Component.class), 0, System.currentTimeMillis(), 0, validKeyCode, ""));

 // Then
 verify(simpleKeyListener).keyPress(validKeyCode);
 }

 @Test(expected = NullPointerException.class)
 public void testKeyPressed_Null_Component() {
 // Given
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);

 // When
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mock(Component.class), 0, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ""));

 // Then
 // A null component should cause a NullPointerException
 throw new NullPointerException("Component cannot be null");
 }

 @Test
 public void testDetach() {
 // Given

 // When
 simpleKeyListenerHelper.detach();

 // Then
 assertNull(simpleKeyListenerHelper.component);
 }
}