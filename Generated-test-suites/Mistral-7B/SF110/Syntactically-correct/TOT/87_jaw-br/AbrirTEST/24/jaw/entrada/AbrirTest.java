package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.entrada.Abrir;
import jaw.entrada.FormProcessadorEntidadesMock;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

	private Abrir objetoTestado;
	private FormProcessadorEntidadesMock form;
	private String caminhoArquivoValidoXML;
	private String caminhoArquivoValidoSerializado;
	private String caminhoArquivoInvalidoXML;
	private String caminhoArquivoInvalidoSerializado;
	private String caminhoArquivoNaoExistente;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		form = new FormProcessadorEntidadesMock();
		objetoTestado = new Abrir(form, "teste.jaw.xml");
		caminhoArquivoValidoXML = "teste.jaw.xml";
		caminhoArquivoValidoSerializado = "teste.jaw";
		caminhoArquivoInvalidoXML = "teste.txt";
		caminhoArquivoInvalidoSerializado = "teste.txt";
		caminhoArquivoNaoExistente = "teste.jaw.xml";
	}

	@Test
	public void testAbrirXMLArquivoValido() {
		// Cenário
		when(new File(caminhoArquivoValidoXML).exists()).thenReturn(true);
		when(new FileReader(caminhoArquivoValidoXML)).then(reader -> new FileReader(caminhoArquivoValidoXML));

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR_XML);
		objetoTestado.escolherArquivo(new JFileChooser(), new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro2());

		// Verificação
		verify(form).setNomeArquivoSalvo(caminhoArquivoValidoXML);
		assertTrue(form.getEntidades().isEmpty() == false);
	}

	@Test
	public void testAbrirXMLArquivoInvalido() {
		// Cenário
		when(new File(caminhoArquivoInvalidoXML).exists()).thenReturn(true);

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR_XML);
		objetoTestado.escolherArquivo(new JFileChooser(), new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro2());

		// Verificação
		verify(form, never()).setEntidades(any());
		assertEquals("Arquivo xml corrompido ou incompatível com aplicação!", form.getMensagemDeErro());
	}

	@Test
	public void testAbrirSerializadoArquivoValido() {
		// Cenário
		when(new File(caminhoArquivoValidoSerializado).exists()).thenReturn(true);

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR);
		objetoTestado.escolherArquivo(new JFileChooser(), new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro());

		// Verificação
		verify(form).setNomeArquivoSalvo(caminhoArquivoValidoSerializado);
		assertTrue(form.getEntidades().isEmpty() == false);
	}

	@Test
	public void testAbrirSerializadoArquivoInvalido() {
		// Cenário
		when(new File(caminhoArquivoInvalidoSerializado).exists()).thenReturn(true);

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR);
		objetoTestado.escolherArquivo(new JFileChooser(), new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro());

		// Verificação
		verify(form, never()).setEntidades(any());
		assertEquals("Arquivo jaw corrompido ou incompatível com aplicação!", form.getMensagemDeErro());
	}

	@Test
	public void testAbrirArquivoNaoExistente() {
		// Cenário
		when(new File(caminhoArquivoNaoExistente).exists()).thenReturn(false);

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR);
		objetoTestado.escolherArquivo(new JFileChooser(), new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro());

		// Verificação
		assertEquals("", form.getNomeArquivoSalvo());
		assertEquals("", form.getMensagemDeErro());
	}

	@Test
	public void testAbrirSemArquivoSelecionado() {
		// Cenário
		JFileChooser escolhedor = new JFileChooser();
		escolhedor.setSelectedFile(null);

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR);
		objetoTestado.escolherArquivo(escolhedor, new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro());

		// Verificação
		assertEquals("", form.getNomeArquivoSalvo());
		assertEquals("", form.getMensagemDeErro());
	}

	@Test
	public void testAbrirXMLArquivoNaoTerminaEmXML() {
		// Cenário
		String arquivo = "teste.txt.xml";
		when(new File(arquivo).exists()).thenReturn(true);

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR_XML);
		objetoTestado.escolherArquivo(new JFileChooser(), new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro2());

		// Verificação
		verify(form, never()).setEntidades(any());
		assertEquals("Arquivo xml corrompido ou incompatível com aplicação!", form.getMensagemDeErro());
	}

	@Test
	public void testAbrirSerializadoArquivoNaoTerminaEmJaw() {
		// Cenário
		String arquivo = "teste.txt.jaw";
		when(new File(arquivo).exists()).thenReturn(true);

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR);
		objetoTestado.escolherArquivo(new JFileChooser(), new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro());

		// Verificação
		verify(form, never()).setEntidades(any());
		assertEquals("Arquivo jaw corrompido ou incompatível com aplicação!", form.getMensagemDeErro());
	}

	@Test
	public void testAbrirSemExtensao() {
		// Cenário
		String arquivo = "teste";
		when(new File(arquivo).exists()).thenReturn(true);

		// Ação
		objetoTestado = new Abrir(form, Abrir.ABRIR);
		objetoTestado.escolherArquivo(new JFileChooser(), new File(Main.configuracao.getCaminhoDosProjetos()), new Filtro());

		// Verificação
		assertEquals("", form.getNomeArquivoSalvo());
		assertEquals("", form.getMensagemDeErro());
	}
}