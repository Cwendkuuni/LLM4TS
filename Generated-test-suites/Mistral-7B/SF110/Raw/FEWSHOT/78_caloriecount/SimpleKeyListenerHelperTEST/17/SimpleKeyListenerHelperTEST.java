package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener mockSimpleKeyListener;

 @Before
 public void setUp() {
 component = mock(Component.class);
 mockSimpleKeyListener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testConstructor() {
 assertNotNull(simpleKeyListenerHelper);
 assertNotNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testDetach() {
 assertNotNull(simpleKeyListenerHelper.getComponent());
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testNotifyListener() {
 when(component.isDisplayable()).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
 verify(mockSimpleKeyListener).enter(component);

 when(component.isDisplayable()).thenReturn(false);
 simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
 // nothing happens as component is not displayable
 }

 @Test
 public void testKeyPressed_EnterKey() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 verify(mockSimpleKeyListener).enter(component);
 }

 @Test
 public void testKeyPressed_TabKey() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 verify(mockSimpleKeyListener, never()).tab(component);
 // tab event is not handled by SimpleKeyListenerHelper and is left to be handled by the component or its parent
 }

 @Test
 public void testKeyPressed_OtherKeys() {
 KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, KeyEvent.CHAR_A);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 verify(mockSimpleKeyListener, never()).enter(component);
 verify(mockSimpleKeyListener, never()).insert(component);
 verify(mockSimpleKeyListener, never()).delete(component);
 verify(mockSimpleKeyListener, never()).tab(component);
 }
}