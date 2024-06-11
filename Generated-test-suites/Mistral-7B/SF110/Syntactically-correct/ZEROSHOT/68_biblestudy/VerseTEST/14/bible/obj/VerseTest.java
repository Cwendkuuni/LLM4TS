package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.Database;
import bible.util.Util;

public class VerseTest {

	private static Connection connection;
	static {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Verse (Verse_ID INTEGER PRIMARY KEY, Text TEXT, Translation_ID INTEGER, Book_ID INTEGER, Chapter INTEGER, Verse_No INTEGER)");;;_;
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception {
		Database.Init();
		Database.ExecuteSQL("INSERT INTO Translation VALUES (1, 'English')");
		Database.ExecuteSQL("INSERT INTO Book VALUES (1, 'Genesis', 66)");
		Database.ExecuteSQL("INSERT INTO Verse VALUES (1, 'In the beginning God created the heaven and the earth.', 1, 1, 1, 1)");
		Database.ExecuteSQL("INSERT INTO Verse VALUES (2, 'Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.', 1, 1, 1, 2)");
		Database.ExecuteSQL("INSERT INTO Verse VALUES (3, 'And God said, “Let there be light,” and there was light.', 1, 1, 1, 3)");
	}

	@After
	public void tearDown() throws Exception {
		Database.Shutdown();
		connection.close();
	}

	@Test
	public void testNew() {
		Verse verse1 = Verse.New(1);
		assertEquals(1, verse1.getId());
		assertEquals("In the beginning God created the heaven and the earth.", verse1.getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verse1.getTranslationReference());
		assertEquals(new Reference(1, 1, 1), verse1.getReference());
		assertEquals("bible.obj.Verse:[1 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] In the beginning God created the heaven and the earth.]", verse1.toString());

		Verse verse2 = Verse.New(2);
		assertEquals(2, verse2.getId());
		assertEquals("Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.", verse2.getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verse2.getTranslationReference());
		assertEquals(new Reference(1, 1, 1), verse2.getReference());
		assertEquals("bible.obj.Verse:[2 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.]", verse2.toString());

		Verse verse3 = Verse.New(3);
		assertEquals(3, verse3.getId());
		assertEquals("And God said, “Let there be light,” and there was light.", verse3.getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verse3.getTranslationReference());
		assertEquals(new Reference(1, 1, 1), verse3.getReference());
		assertEquals("bible.obj.Verse:[3 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] And God said, “Let there be light,” and there was light.]", verse3.toString());
	}

	@Test
	public void testGetId() {
		assertEquals(1, Verse.GetId(new Translation(1, "English"), new Book(1, "Genesis", 66), 1, 1));
		assertEquals(2, Verse.GetId(new Translation(1, "English"), new Book(1, "Genesis", 66), 1, 2));
		assertEquals(3, Verse.GetId(new Translation(1, "English"), new Book(1, "Genesis", 66), 1, 3));
	}

	@Test
	public void testGet() {
		List<Verse> verses = Verse.Get(new Translation[0], new Book(1, "Genesis", 66), 1, 1);
		assertEquals(3, verses.size());
		assertEquals(1, verses.get(0).getId());
		assertEquals(2, verses.get(1).getId());
		assertEquals(3, verses.get(2).getId());

		verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 3));
		assertEquals(1, verses.size());
		assertEquals(1, verses.get(0).getId());
	}

	@Test
	public void testGetWithTranslations() {
		List<Verse> verses = Verse.Get(new Translation[] { new Translation(1, "English") }, new Book(1, "Genesis", 66), 1, 1);
		assertEquals(3, verses.size());
		assertEquals(1, verses.get(0).getId());
		assertEquals("In the beginning God created the heaven and the earth.", verses.get(0).getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verses.get(0).getTranslationReference());
		assertEquals("bible.obj.Verse:[1 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] In the beginning God created the heaven and the earth.]", verses.get(0).toString());

		assertEquals(2, verses.get(1).getId());
		assertEquals("Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.", verses.get(1).getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verses.get(1).getTranslationReference());
		assertEquals("bible.obj.Verse:[2 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.]", verses.get(1).toString());

		assertEquals(3, verses.get(2).getId());
		assertEquals("And God said, “Let there be light,” and there was light.", verses.get(2).getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verses.get(2).getTranslationReference());
		assertEquals("bible.obj.Verse:[3 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] And God said, “Let there be light,” and there was light.]", verses.get(2).toString());
	}

	@Test
	public void testGetWithBeginAndEnd() {
		List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 3));
		assertEquals(3, verses.size());
		assertEquals(1, verses.get(0).getId());
		assertEquals("In the beginning God created the heaven and the earth.", verses.get(0).getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verses.get(0).getTranslationReference());
		assertEquals("bible.obj.Verse:[1 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] In the beginning God created the heaven and the earth.]", verses.get(0).toString());

		assertEquals(2, verses.get(1).getId());
		assertEquals("Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.", verses.get(1).getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verses.get(1).getTranslationReference());
		assertEquals("bible.obj.Verse:[2 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.]", verses.get(1).toString());

		assertEquals(3, verses.get(2).getId());
		assertEquals("And God said, “Let there be light,” and there was light.", verses.get(2).getText());
		assertEquals(new TranslationReference(new Translation(1, "English"), new Reference(1, 1, 1)), verses.get(2).getTranslationReference());
		assertEquals("bible.obj.Verse:[3 [TranslationReference:[Translation:[1 English], Reference:[1 1 1]]] And God said, “Let there be light,” and there was light.]", verses.get(2).toString());
	}
}