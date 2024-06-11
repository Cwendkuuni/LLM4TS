package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.*;
import java.net.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenuNullMenu() {
        String menu = null;
        MainMenu menuInstance = new MainMenu(null);
        assertNull(menuInstance.createMenu(menu));
    }

    @Test
    public void testCreateMenuEmptyMenu() {
        String menu = "";
        MainMenu menuInstance = new MainMenu(null);
        JMenu result = menuInstance.createMenu(menu);
        assertNull(result);
    }

    @Test
    public void testCreateMenuValidMenu() {
        String menu = "File";
        MainMenu menuInstance = new MainMenu(null);
        JMenu result = menuInstance.createMenu(menu);
        assertNotNull(result);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuMissingLabel() {
        String menu = "InvalidMenu";
        MainMenu menuInstance = new MainMenu(null);
        menuInstance.createMenu(menu);
    }

    @Test
    public void testCreateMenuItemNullItem() {
        String menu = "File";
        String item = null;
        MainMenu menuInstance = new MainMenu(null);
        JMenuItem result = menuInstance.createMenu(menu).getComponent(0);
        assertNull(result);
    }

    @Test
    public void testCreateMenuItemValidItemLabel() {
        String menu = "File";
        String item = "Open";
        MainMenu menuInstance = new MainMenu(null);
        JMenuItem result = (JMenuItem) menuInstance.createMenu(menu).getComponent(0).getComponent(0);
        assertEquals("Open", result.getText());
    }

    @Test
    public void testCreateMenuItemInvalidItemLabel() {
        String menu = "File";
        String item = "InvalidItem";
        MainMenu menuInstance = new MainMenu(null);
        JMenuItem result = (JMenuItem) menuInstance.createMenu(menu).getComponent(0).getComponent(1);
        assertNull(result);
    }

    @Test
    public void testCreateMenuWithSeparator() {
        String menu = "File";
        MainMenu menuInstance = new MainMenu(null);
        JMenu result = menuInstance.createMenu(menu);
        JMenuItem separator = (JMenuItem) result.getComponent(result.getComponentCount() - 1);
        assertTrue(separator.isSeparator());
    }

    @Test
    public void testAddShortcutAndIconNullItem() {
        String key = "InvalidKey";
        JMenuItem item = null;
        MainMenu menuInstance = new MainMenu(null);
        menuInstance.addShortcutAndIcon(item, key);
    }

    @Test
    public void testAddShortcutAndIconMissingShortcut() {
        String key = "InvalidKey";
        JMenuItem item = new JMenuItem("InvalidItem");
        MainMenu menuInstance = new MainMenu(null);
        menuInstance.addShortcutAndIcon(item, key);
        assertEquals(0, item.getAccelerator().getKeyCode());
    }

    @Test
    public void testAddShortcutAndIconMissingIcon() {
        String key = "ValidKey";
        JMenuItem item = new JMenuItem("ValidItem");
        MainMenu menuInstance = new MainMenu(null);
        menuInstance.addShortcutAndIcon(item, key);
        assertNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIconValidShortcutAndIcon() {
        String key = "ValidKey";
        JMenuItem item = new JMenuItem("ValidItem");
        MainMenu menuInstance = new MainMenu(null);
        menuInstance.addShortcutAndIcon(item, key);
        KeyStroke accelerator = item.getAccelerator();
        InputMap im = item.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        int keyCode = accelerator.getKeyCode();
        int modifiers = accelerator.getModifiers();
        assertEquals("Ctrl+S", "Ctrl+S", KeyEvent.getKeyText(keyCode) + "+" + Integer.toBinaryString(modifiers).substring(1));
        assertNotNull(item.getIcon());
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconMissingShortcutLabel() {
        String key = "ValidKey";
        JMenuItem item = new JMenuItem("ValidItem");
        MainMenu menuInstance = new MainMenu(null);
        menuInstance.addShortcutAndIcon(item, key);
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconMissingIconPath() {
        String key = "ValidKey";
        JMenuItem item = new JMenuItem("ValidItem");
        MainMenu menuInstance = new MainMenu(null);
        menuInstance.addShortcutAndIcon(item, key);
    }

    @Test
    public void testChanged() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        menu.changed();
        verify(mediator).changed();
    }
}