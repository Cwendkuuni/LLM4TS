package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Salvar;
import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloFactory;
import jaw.metamodelo.Tipo;

public class SalvarTest {

	private ProcessarEntidades form;
	private Salvar salvar;
	private List<Entidade> entidades;

	@Before
	public void setUp() {
		form = new ProcessarEntidades();
		entidades = new ArrayList<>();
		entidades.add(MetamodeloFactory.eInstancia("Tipo", "Tipo1", new Tipo("Tipo1", "Tipo1Description")));
		form.setEntidades(entidades);
		salvar = new Salvar(form, Salvar.SALVAR_XML);
	}

	@After
	public void tearDown() {
		File file = new File("SalvarTest.jaw.xml");
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void testSalvarComArquivoValido() {
		salvar.salvar(entidades, "SalvarTest.jaw.xml");
		assertTrue(new File("SalvarTest.jaw.xml").exists());
	}

	@Test(expected = NullPointerException.class)
	public void testSalvarSemArquivo() {
		salvar.salvar(entidades, null);
	}

	@Test(expected = NullPointerException.class)
	public void testSalvarSemEntidades() {
		salvar = new Salvar(form, Salvar.SALVAR_XML);
		salvar.salvar(null, "SalvarTest.jaw.xml");
	}

	@Test
	public void testSalvarComNomeInvalido() {
		salvar = new Salvar(form, Salvar.SALVAR);
		salvar.salvar(entidades, "");
		assertEquals("", salvar.arquivo);
	}

	@Test
	public void testSalvarComArquivoExistente() {
		salvar = new Salvar(form, Salvar.SALVAR);
		File file = new File("SalvarTest.jaw");
		file.createNewFile();
		salvar.salvar(entidades, file.getAbsolutePath());
		assertTrue(file.exists());
		salvar.salvar(entidades, file.getAbsolutePath());
		assertTrue(file.length() > 0);
	}

	@Test(expected = IOException.class)
	public void testSerializarException() {
		salvar = new Salvar(form, Salvar.SALVAR_XML);
		FileWriter writer = new FileWriter("SalvarTest.jaw.xml");
		writer.write("invalid xml");
		writer.close();
		salvar.salvar(entidades, "SalvarTest.jaw.xml");
	}

	@Test
	public void testSerializarSucesso() {
		salvar = new Salvar(form, Salvar.SALVAR_XML);
		String xml = "<entidades>" +
				"<entidade>" +
				"<nome>Tipo1</nome>" +
				"<descricao>Tipo1Description</descricao>" +
				"</entidade>" +
				"</entidades>";
		FileWriter writer = new FileWriter("SalvarTest.jaw.xml");
		writer.write(xml);
		writer.close();
		salvar.salvar(entidades, "SalvarTest.jaw.xml");
		assertEquals(xml, salvar.xml);
	}

	@Test
	public void testSalvarComNovoProjeto() {
		salvar = new Salvar(form, Salvar.SALVAR);
		File file = new File("SalvarTest.jaw");
		file.createNewFile();
		salvar.salvar(entidades, file.getAbsolutePath());
		assertEquals("SalvarTest.jaw", salvar.arquivo);
		salvar.salvar(entidades, "Novo Projeto");
		assertEquals("Novo Projeto.jaw", salvar.arquivo);
	}

	@Test(expected = IOException.class)
	public void testExportarException() {
		salvar = new Salvar(form, Salvar.SALVAR_XML);
		FileWriter writer = new FileWriter("SalvarTest.jaw.xml");
		writer.write("invalid xml");
		writer.close();
		salvar.salvar(entidades, "SalvarTest.jaw.xml");
		salvar.exportar("SalvarTest.jaw.xml");
	}

	@Test
	public void testExportarSucesso() {
		salvar = new Salvar(form, Salvar.SALVAR_XML);
		String xml = "<entidades>" +
				"<entidade>" +
				"<nome>Tipo1</nome>" +
				"<descricao>Tipo1Description</descricao>" +
				"</entidade>" +
				"</entidades>";
		FileWriter writer = new FileWriter("SalvarTest.jaw.xml");
		writer.write(xml);
		writer.close();
		salvar.salvar(entidades, "SalvarTest.jaw.xml");
		salvar.exportar("SalvarTest.jaw.xml");
		assertEquals(xml, salvar.xml);
	}

	@Test
	public void testExportarComArquivoExistente() {
		salvar = new Salvar(form, Salvar.SALVAR_XML);
		String xml = "<entidades>" +
				"<entidade>" +
				"<nome>Tipo1</nome>" +
				"<descricao>Tipo1Description</descricao>" +
				"</entidade>" +
				"</entidades>";
		FileWriter writer = new FileWriter("SalvarTest.jaw.xml");
		writer.write(xml);
		writer.close();
		salvar.salvar(entidades, "SalvarTest.jaw.xml");
		salvar.exportar("SalvarTest.jaw.xml");
		File file = new File("SalvarTest.jaw.xml");
		assertTrue(file.length() > 0);
	}
}