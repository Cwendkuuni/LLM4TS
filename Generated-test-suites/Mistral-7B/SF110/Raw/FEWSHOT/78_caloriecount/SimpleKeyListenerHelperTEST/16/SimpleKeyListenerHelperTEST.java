package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 Component mockComponent = mock(Component.class);
 SimpleKeyListener mockListener = mock(SimpleKeyListener.class);

 @Test
 public void testConstructor() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testAttach() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 verify(mockComponent).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.detach();
 verify(mockComponent).removeKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testNotifyListener_enter() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(10);
 verify(mockListener).enter(mockComponent);
 }

 @Test
 public void testNotifyListener_insert() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(155);
 verify(mockListener).insert(mockComponent);
 }

 @Test
 public void testNotifyListener_delete() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(127);
 verify(mockListener).delete(mockComponent);
 }

 @Test
 public void testNotifyListener_tab() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(9);
 verify(mockListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressed_enter() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 10, 1, 0));
 simpleKeyListenerHelper.fire(10);
 }

 @Test
 public void testKeyPressed_insert() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 155, 1, 0));
 simpleKeyListenerHelper.fire(155);
 }

 @Test
 public void testKeyPressed_delete() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 127, 1, 0));
 simpleKeyListenerHelper.fire(127);
 }

 @Test
 public void testKeyPressed_tab() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 9, 1, 0));
 simpleKeyListenerHelper.fire(9);
 }

 @Test
 public void testKeyPressed_other() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, 1, 1, 0));
 simpleKeyListenerHelper.fire(1);
 assertNull(mockListener);
 }

 @Test
 public void testKeyReleased() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyReleased(new KeyEvent(mockComponent, 1, 1, 0));
 verify(mockListener, mockito.never()).keyReleased(mockito.any());
 }

 @Test
 public void testKeyTyped() {
 SimpleKeyListenerHelper simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyTyped(new KeyEvent(mockComponent, 1, 1, 0));
 verify(mockListener, mockito.never()).keyTyped(mockito.any());
 }
}