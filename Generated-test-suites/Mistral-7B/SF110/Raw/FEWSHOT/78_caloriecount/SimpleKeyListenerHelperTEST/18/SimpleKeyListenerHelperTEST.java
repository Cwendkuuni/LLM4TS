package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener simpleKeyListener;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 component = mock(Component.class);
 simpleKeyListener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testConstructor() {
 assertNotNull(simpleKeyListenerHelper);
 assertSame(component, simpleKeyListenerHelper.component);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 verify(component, times(1)).removeKeyListener(simpleKeyListenerHelper);
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNotifyListenerEnter() {
 simpleKeyListenerHelper.fire(10);
 verify(simpleKeyListener, times(1)).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper.fire(155);
 verify(simpleKeyListener, times(1)).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper.fire(127);
 verify(simpleKeyListener, times(1)).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper.fire(9);
 verify(simpleKeyListener, times(1)).tab(component);
 }

 @Test
 public void testKeyPressedEnterOrTabOrDeleteOrInsert() {
 KeyEvent keyEvent = mock(KeyEvent.class);
 when(keyEvent.getKeyCode()).thenReturn(10);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 verify(simpleKeyListener, times(1)).enter(component);

 when(keyEvent.getKeyCode()).thenReturn(9);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 verify(simpleKeyListener, times(1)).tab(component);

 when(keyEvent.getKeyCode()).thenReturn(127);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 verify(simpleKeyListener, times(1)).delete(component);

 when(keyEvent.getKeyCode()).thenReturn(155);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 verify(simpleKeyListener, times(1)).insert(component);
 }

 @Test
 public void testKeyPressedOtherKeycode() {
 KeyEvent keyEvent = mock(KeyEvent.class);
 when(keyEvent.getKeyCode()).thenReturn(11);
 simpleKeyListenerHelper.keyPressed(keyEvent);
 verify(simpleKeyListener, times(0)).enter(component);
 verify(simpleKeyListener, times(0)).insert(component);
 verify(simpleKeyListener, times(0)).delete(component);
 verify(simpleKeyListener, times(0)).tab(component);
 }
}