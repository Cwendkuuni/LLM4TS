package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

    @Mock
    private InspirentoMediator mediator;

    @InjectMocks
    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(mediator);
    }

    @After
    public void tearDown() {
        mainMenu = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(mainMenu);
    }

    @Test
    public void testCreateMenu() {
        String menu = "File";
        JMenu expectedMenu = new JMenu(menu);
        String[] items = {"Open", "Save", "--", "Export", "#Print", "Separator", "Exit"};
        when(UIResources.getString(menu)).thenReturn(menu + "*");
        when(UIResources.getString(menu + "Label")).thenAnswer(i -> i.getParameterNames()[0]);
        when(UIResources.getString("Open" + "Label")).thenReturn("Open*");
        when(UIResources.getString("Open" + "Shortcut")).thenReturn("Ctrl O");
        when(UIResources.getString("Open" + "Icon")).thenReturn("/icons/open.png");
        when(UIResources.getString("Save" + "Label")).thenReturn("Save*");
        when(UIResources.getString("Save" + "Shortcut")).thenReturn("Ctrl S");
        when(UIResources.getString("Save" + "Icon")).thenReturn("/icons/save.png");
        when(UIResources.getString("Export" + "Label")).thenReturn("Export*");
        when(UIResources.getString("Export" + "Shortcut")).thenReturn("Ctrl X");
        when(UIResources.getString("Export" + "Icon")).thenReturn("/icons/export.png");
        when(UIResources.getString("Print" + "Label")).thenReturn("Print*");
        when(UIResources.getString("Print" + "Shortcut")).thenReturn("Ctrl P");
        when(UIResources.getString("Print" + "Icon")).thenReturn("/icons/print.png");

        JMenu actualMenu = mainMenu.createMenu(menu);

        assertEquals(expectedMenu, actualMenu);
        assertEquals(6, actualMenu.getMenuComponentCount());

        for (int i = 0; i < 6; i++) {
            JMenuItem item = (JMenuItem) actualMenu.getMenuComponent(i);
            String key = "";
            if (i == 1 || i == 3 || i == 5) {
                key = Arrays.toString(ItemEvent.SELECTED).substring(1, Arrays.toString(ItemEvent.SELECTED).length() - 1).replace(" ", "").replace("]", "").replace("_", "");
            }
            if (i == 4) {
                key = "Print";
            }

            assertNotNull(item);
            assertEquals(menu + ":" + key, item.getActionCommand());
            assertEquals(menu, item.getParent());

            if (i != 4) {
                String expectedShortcut = "";
                String expectedIconPath = "";

                if (i == 1) {
                    expectedShortcut = "Ctrl O";
                } else if (i == 3) {
                    expectedShortcut = "Ctrl X";
                }

                if (i == 1 || i == 3) {
                    expectedIconPath = "/icons/open.png";
                }

                assertNull(item.getAccelerator());
                assertNull(item.getIcon());

                if (expectedShortcut.length() > 0) {
                    doReturn(new KeyStroke(KeyEvent.VK_CONTROL, KeyEvent.VK_O)).when(item).getAccelerator();
                }

                if (expectedIconPath.length() > 0) {
                    when(UIResources.getString(key + "Icon")).thenReturn(expectedIconPath);
                    URL iconUrl = new File("/icons").toURI().toURL();
                    when(mainMenu.getClass().getResource(expectedIconPath)).thenReturn(iconUrl);
                    assertEquals(expectedIconPath, item.getIcon().getDescription(null));
                }
            }
        }

        assertEquals(1, actualMenu.getComponentCount());
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingLabel() {
        String menu = "MissingLabel";
        mainMenu.createMenu(menu);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingItemLabel() {
        String menu = "File";
        String itemKey = "MissingItemLabel";
        mainMenu.createMenu(menu);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingShortcut() {
        String menu = "File";
        String itemKey = "Open";
        when(UIResources.getString(menu)).thenReturn(menu + "*");
        when(UIResources.getString(menu + "Label")).thenAnswer(i -> i.getParameterNames()[0]);
        when(UIResources.getString(menu + "Label")).thenReturn(menu + "*");
        when(UIResources.getString(itemKey + "Label")).thenReturn(itemKey + "*");
        when(UIResources.getString(itemKey)).thenReturn(itemKey);

        mainMenu.createMenu(menu);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingIcon() {
        String menu = "File";
        String itemKey = "Print";
        when(UIResources.getString(menu)).thenReturn(menu + "*");
        when(UIResources.getString(menu + "Label")).thenAnswer(i -> i.getParameterNames()[0]);
        when(UIResources.getString(menu + "Label")).thenReturn(menu + "*");
        when(UIResources.getString(itemKey + "Label")).thenReturn(itemKey + "*");
        when(UIResources.getString(itemKey)).thenReturn(itemKey);
        when(UIResources.getString(itemKey + "Icon")).thenReturn(itemKey + "Icon");

        mainMenu.createMenu(menu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        JCheckBoxMenuItem checkboxMenuItem = mock(JCheckBoxMenuItem.class);
        String key = "Print";

        mainMenu.addShortcutAndIcon(checkboxMenuItem, key);

        assertEquals(key, checkboxMenuItem.getActionCommand());

        verify(checkboxMenuItem, never()).setAccelerator(any(KeyStroke.class));
        verify(checkboxMenuItem, never()).setIcon(any(Icon.class));

        String shortcut = "Ctrl P";
        doReturn(new KeyStroke(KeyEvent.VK_CONTROL, KeyEvent.VK_P)).when(checkboxMenuItem).getAccelerator();

        mainMenu.addShortcutAndIcon(checkboxMenuItem, key);

        assertEquals(shortcut, checkboxMenuItem.getAccelerator().toString());

        String iconPath = "/icons/print.png";
        when(UIResources.getString(key + "Icon")).thenReturn(iconPath);
        when(mainMenu.getClass().getResource(iconPath)).thenReturn(new File("/icons").toURI().toURL());

        mainMenu.addShortcutAndIcon(checkboxMenuItem, key);

        assertEquals(iconPath, checkboxMenuItem.getIcon().getDescription(null));
    }

    @Test
    public void testAddShortcutAndIconToMenuItem() {
        JMenuItem menuItem = mock(JMenuItem.class);
        String key = "Open";

        mainMenu.addShortcutAndIcon(menuItem, key);

        assertEquals(key, menuItem.getActionCommand());

        verify(menuItem, never()).setAccelerator(any(KeyStroke.class));
        verify(menuItem, never()).setIcon(any(Icon.class));

        String shortcut = "Ctrl O";
        doReturn(new KeyStroke(KeyEvent.VK_CONTROL, KeyEvent.VK_O)).when(menuItem).getAccelerator();

        mainMenu.addShortcutAndIcon(menuItem, key);

        assertEquals(shortcut, menuItem.getAccelerator().toString());

        String iconPath = "/icons/open.png";
        when(UIResources.getString(key + "Icon")).thenReturn(iconPath);
        when(mainMenu.getClass().getResource(iconPath)).thenReturn(new File("/icons").toURI().toURL());

        mainMenu.addShortcutAndIcon(menuItem, key);

        assertEquals(iconPath, menuItem.getIcon().getDescription(null));
    }
}