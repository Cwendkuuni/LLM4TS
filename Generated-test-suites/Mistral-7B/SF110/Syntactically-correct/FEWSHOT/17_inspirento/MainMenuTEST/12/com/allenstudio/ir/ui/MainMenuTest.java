package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

public class MainMenuTest {

    @Test
    public void testMainMenuCreation() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediatorMock);
        assertNotNull(menu);
    }

    @Test(expected = NullPointerException.class)
    public void testMenuCreationWithNullMediator() {
        new MainMenu(null);
    }

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        String menuName = "TestMenu";
        String menuLabel = "Test Menu";
        String subMenuName = "SubMenu";
        String subMenuLabel = "Sub Menu";

        when(UIResources.getString(menuName + "Label")).thenReturn(menuLabel);
        when(UIResources.getString(subMenuName + "Label")).thenReturn(subMenuLabel);

        MainMenu menu = new MainMenu(mediatorMock);
        JMenu menuComponent = menu.createMenu(menuName);
        assertEquals(menuLabel, menuComponent.getText());

        JMenu subMenu = menuComponent.add(menu.createMenu(subMenuName));
        assertEquals(subMenuLabel, subMenu.getText());
    }

    @Test
    public void testCreateJMenuItem() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        String itemName = "TestItem";
        String itemLabel = "Test Item";
        String shortcut = "ctrl T";
        String iconPath = "TestIcon.png";

        when(UIResources.getString(itemName + "Label")).thenReturn(itemLabel);

        MainMenu menu = new MainMenu(mediatorMock);
        JMenuItem jMenuItem = menu.createMenuItem(itemName);
        assertEquals(itemLabel, jMenuItem.getText());
        assertNull(jMenuItem.getAccelerator());
        assertNull(jMenuItem.getIcon());

        menu.addShortcutAndIcon(jMenuItem, itemName);
        assertEquals(shortcut, jMenuItem.getAccelerator().toString());
        assertNotNull(jMenuItem.getIcon());
    }

    @Test
    public void testCreateJCheckBoxMenuItem() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        String itemName = "TestCheckboxItem";
        String itemLabel = "Test Checkbox Item";
        String shortcut = "ctrl T";
        String iconPath = "TestIcon.png";

        when(UIResources.getString(itemName + "Label")).thenReturn(itemLabel);

        MainMenu menu = new MainMenu(mediatorMock);
        JCheckBoxMenuItem jMenuItem = menu.createCheckboxMenuItem(itemName);
        assertEquals(itemLabel, jMenuItem.getText());
        assertNull(jMenuItem.getAccelerator());
        assertNull(jMenuItem.getIcon());

        menu.addShortcutAndIcon(jMenuItem, itemName);
        assertEquals(shortcut, jMenuItem.getAccelerator().toString());
        assertNotNull(jMenuItem.getIcon());
    }

    @Test
    public void testAddSeparator() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        String itemName = "TestSeparator";

        MainMenu menu = new MainMenu(mediatorMock);
        JMenu menuComponent = menu.createMenu("TestMenu");
        menuComponent.add(menu.createSeparator());
        assertTrue(menuComponent.getComponentCount() > 1);
    }

    @Test
    public void testMissingResourceException() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        String menuName = "MissingMenu";
        String menuLabel = "Missing Menu";

        when(UIResources.getString(menuName + "Label")).thenThrow(new MissingResourceException("", "", menuName));

        MainMenu menu = new MainMenu(mediatorMock);
        try {
            menu.createMenu(menuName);
            fail("Exception expected");
        } catch (MissingResourceException e) {
            // Expected exception
        }
    }

    @Test
    public void testMissingResourceExceptionForMenuItem() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        String itemName = "MissingItem";
        String itemLabel = "Missing Item";

        when(UIResources.getString(itemName + "Label")).thenThrow(new MissingResourceException("", "", itemName));

        MainMenu menu = new MainMenu(mediatorMock);
        try {
            menu.createMenuItem(itemName);
            fail("Exception expected");
        } catch (MissingResourceException e) {
            // Expected exception
        }
    }

    @Test
    public void testMissingResourceExceptionForShortcut() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        String key = "MissingShortcut";
        String shortcut = "ctrl T";

        when(UIResources.getString(key)).thenThrow(new MissingResourceException("", "", key));

        MainMenu menu = new MainMenu(mediatorMock);
        JMenuItem jMenuItem = menu.createMenuItem("TestItem");
        menu.addShortcutAndIcon(jMenuItem, key);
        assertNull(jMenuItem.getAccelerator());
    }

    @Test
    public void testMissingResourceExceptionForIcon() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        String key = "MissingIcon";
        String iconPath = "TestIcon.png";

        when(UIResources.getString(key)).thenThrow(new MissingResourceException("", "", key));

        MainMenu menu = new MainMenu(mediatorMock);
        JMenuItem jMenuItem = menu.createMenuItem("TestItem");
        menu.addShortcutAndIcon(jMenuItem, key);
        assertNull(jMenuItem.getIcon());
    }
}