package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

private SimpleKeyListenerHelper simpleKeyListenerHelper;
private SimpleKeyListener mockSimpleKeyListener;

@Before
public void setUp() {
mockSimpleKeyListener = mock(SimpleKeyListener.class);
simpleKeyListenerHelper = new SimpleKeyListenerHelper(new JFrame()) {
{
this.addComponentListener(new ComponentAdapter() {
@Override
public void componentResized(ComponentEvent e) {
simpleKeyListenerHelper.detach();
}
});
}
};
}

@Test
public void testSimpleKeyListenerHelperConstruction() {
assertNotNull(simpleKeyListenerHelper);
}

@Test
public void testSimpleKeyListenerHelperAddKeyListener() {
assertNotNull(simpleKeyListenerHelper.getComponent());
}

@Test
public void testSimpleKeyListenerHelperDetach() {
simpleKeyListenerHelper.detach();
assertNull(simpleKeyListenerHelper.getComponent());
}

@Test
public void testSimpleKeyListenerHelperNotifyListenerEnter() throws KeyEvent {
when(mockSimpleKeyListener.enter(any(Component.class))).thenReturn(true);
simpleKeyListenerHelper.addListener(mockSimpleKeyListener);
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
verify(mockSimpleKeyListener).enter(simpleKeyListenerHelper.getComponent());
}

@Test
public void testSimpleKeyListenerHelperNotifyListenerInsert() throws KeyEvent {
when(mockSimpleKeyListener.insert(any(Component.class))).thenReturn(true);
simpleKeyListenerHelper.addListener(mockSimpleKeyListener);
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, 0));
verify(mockSimpleKeyListener).insert(simpleKeyListenerHelper.getComponent());
}

@Test
public void testSimpleKeyListenerHelperNotifyListenerDelete() throws KeyEvent {
when(mockSimpleKeyListener.delete(any(Component.class))).thenReturn(true);
simpleKeyListenerHelper.addListener(mockSimpleKeyListener);
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, 0));
verify(mockSimpleKeyListener).delete(simpleKeyListenerHelper.getComponent());
}

@Test
public void testSimpleKeyListenerHelperNotifyListenerTab() throws KeyEvent {
when(mockSimpleKeyListener.tab(any(Component.class))).thenReturn(true);
simpleKeyListenerHelper.addListener(mockSimpleKeyListener);
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, 0));
verify(mockSimpleKeyListener).tab(simpleKeyListenerHelper.getComponent());
}

@Test
public void testSimpleKeyListenerHelperKeyPressedEnter() {
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 0));
assertTrue(simpleKeyListenerHelper.getComponent() instanceof JFrame);
}

@Test
public void testSimpleKeyListenerHelperKeyPressedInsert() {
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, 0));
assertTrue(simpleKeyListenerHelper.getComponent() instanceof JFrame);
}

@Test
public void testSimpleKeyListenerHelperKeyPressedDelete() {
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, 0));
assertTrue(simpleKeyListenerHelper.getComponent() instanceof JFrame);
}

@Test
public void testSimpleKeyListenerHelperKeyPressedTab() {
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, 0));
assertTrue(simpleKeyListenerHelper.getComponent() instanceof JFrame);
}

@Test
public void testSimpleKeyListenerHelperKeyPressedOther() {
simpleKeyListenerHelper.keyPressed(new KeyEvent(simpleKeyListenerHelper.getComponent(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 0));
assertTrue(simpleKeyListenerHelper.getComponent() instanceof JFrame);
}

private Component getComponent() {
return simpleKeyListenerHelper.getComponent();
}
}