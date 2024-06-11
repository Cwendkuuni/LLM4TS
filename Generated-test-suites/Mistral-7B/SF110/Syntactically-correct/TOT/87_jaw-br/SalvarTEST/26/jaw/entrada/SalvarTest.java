package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Tipo;

public class SalvarTest {

	private ProcessarEntidades form;
	private Salvar salvar;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		form = mock(ProcessarEntidades.class);
		salvar = new Salvar(form, Salvar.SALVAR_XML);
	}

	@Test
	public void testSaveXMLWithValidData() {
		// Given
		List<Entidade> entidades = new ArrayList<>();
		Entidade entidade1 = new Entidade("Entidade1", "Tipo1", new Tipo("Tipo1"));
		Entidade entidade2 = new Entidade("Entidade2", "Tipo2", new Tipo("Tipo2"));
		entidades.add(entidade1);
		entidades.add(entidade2);
		when(form.getEntidades()).thenReturn(entidades);

		// When
		salvar.save();

		// Then
		String expectedXml = "<entidades>\n" +
							 "<entidade>\n" +
							 "<nome>Entidade1</nome>\n" +
							 "<tipo>\n" +
							 "<nome>Tipo1</nome>\n" +
							 "</tipo>\n" +
							 "</entidade>\n" +
							 "<entidade>\n" +
							 "<nome>Entidade2</nome>\n" +
							 "<tipo>\n" +
							 "<nome>Tipo2</nome>\n" +
							 "</tipo>\n" +
							 "</entidade>\n" +
							 "</entidades>";

		try {
			Document document = new Document(new XMLOutputter().output(salvar.getXml()));
			String actualXml = document.getRootElement().getXML();
			assertEquals(expectedXml, actualXml);
		} catch (JDOMException | IOException e) {
			fail("Failed to parse XML: " + e.getMessage());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveXMLWithNullEntidades() {
		// Given
		when(form.getEntidades()).thenReturn(null);

		// When & Then
		salvar.save();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveXMLWithEmptyFileName() {
		// Given
		String fileName = "";
		when(form.getNomeArquivoSalvo()).thenReturn(fileName);

		// When & Then
		salvar.save();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveXMLWithInvalidFileName() {
		// Given
		String fileName = "invalidFileName";
		when(form.getNomeArquivoSalvo()).thenReturn(fileName);

		// When & Then
		salvar.save();
	}

	@Test
	public void testSaveJawFileWithValidData() {
		// Given
		List<Entidade> entidades = Arrays.asList(new Entidade("Entidade1", "Tipo1", new Tipo("Tipo1")),
													 new Entidade("Entidade2", "Tipo2", new Tipo("Tipo2")));
		when(form.getEntidades()).thenReturn(entidades);
		String expectedFileName = "test.jaw";
		when(form.getNomeArquivoSalvo()).thenReturn(expectedFileName);

		// When
		salvar.save();

		// Then
		File file = new File(expectedFileName);
		assertTrue(file.exists());
		file.delete();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveJawFileWithNullEntidades() {
		// Given
		when(form.getEntidades()).thenReturn(null);

		// When & Then
		salvar.save();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveJawFileWithEmptyFileName() {
		// Given
		String fileName = "";
		when(form.getNomeArquivoSalvo()).thenReturn(fileName);

		// When & Then
		salvar.save();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveJawFileWithInvalidFileName() {
		// Given
		String fileName = "invalidFileName";
		when(form.getNomeArquivoSalvo()).thenReturn(fileName);

		// When & Then
		salvar.save();
	}
}