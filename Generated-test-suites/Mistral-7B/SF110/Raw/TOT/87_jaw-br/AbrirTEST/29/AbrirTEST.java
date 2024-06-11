package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

import jaw.entrada.Abrir;
import jaw.entrada.FormProcessadorEntidadesMock;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

	private FormProcessadorEntidadesMock form;
	private Abrir abrir;
	private JFileChooser fileChooser;
	private String arquivoXML = "teste.jaw.xml";
	private String arquivoSerializado = "teste.jaw";

	@Before
	public void setup() {
		form = new FormProcessadorEntidadesMock();
		fileChooser = mock(JFileChooser.class);
		when(fileChooser.showOpenDialog(anyObject())).thenReturn(JFileChooser.APPROVE_OPTION);
		when(fileChooser.getSelectedFile()).thenReturn(new File(arquivoXML));
		abrir = new Abrir(form, arquivoXML);
	}

	@After
	public void tearDown() {
		form = null;
		abrir = null;
		fileChooser = null;
	}

	@Test
	public void testAbrirXML() {
		when(new File(Main.configuracao.getCaminhoDosProjetos())).thenReturn(new File("testPath"));
		when(new FileReader(arquivoXML)).then(new FileReaderTestCase());

		abrir.abrirXML(arquivoXML);

		assertEquals("Expected XML file to be read correctly", form.getEntidades().size(), 5);
		verify(form, times(1)).setEntidades(any());
		verify(form, times(1)).setNomeArquivoSalvo(arquivoXML);
	}

	@Test(expected = IOException.class)
	public void testAbrirXMLInvalidFile() {
		when(new File(Main.configuracao.getCaminhoDosProjetos())).thenReturn(new File("testPath"));
		when(new FileReader(arquivoXML)).thenThrow(new IOException("Invalid File"));

		abrir.abrirXML(arquivoXML);
	}

	@Test
	public void testAbrirSerializado() {
		when(new File(Main.configuracao.getCaminhoDosProjetos())).thenReturn(new File("testPath"));
		when(fileChooser.getSelectedFile()).thenReturn(new File(arquivoSerializado));
		when(new Serializacao().abrir(arquivoSerializado)).thenReturn(form.getEntidades());

		abrir.abrirSerializado(arquivoSerializado, Abrir.ABRIR_XML);

		assertEquals("Expected Serializado file to be read correctly", form.getEntidades().size(), 5);
		verify(form, times(1)).setEntidades(any());
		verify(form, times(1)).setNomeArquivoSalvo(arquivoSerializado);
	}

	@Test(expected = IOException.class)
	public void testAbrirSerializadoInvalidFile() {
		when(new File(Main.configuracao.getCaminhoDosProjetos())).thenReturn(new File("testPath"));
		when(fileChooser.getSelectedFile()).thenReturn(new File(arquivoSerializado));
		when(new Serializacao().abrir(arquivoSerializado)).thenThrow(new IOException("Invalid File"));

		abrir.abrirSerializado(arquivoSerializado, Abrir.ABRIR_XML);
	}

	@Test
	public void testAbrirJFileChooserCancelButton() {
		when(fileChooser.showOpenDialog(anyObject())).thenReturn(JFileChooser.CANCEL_OPTION);

		abrir.abrirSerializado(null, Abrir.ABRIR_XML);

		assertNull("Expected no file to be opened when cancel button is clicked", abrir.getXml());
	}

	@Test
	public void testAbrirJFileChooserInvalidFileFilter() {
		when(new File(Main.configuracao.getCaminhoDosProjetos())).thenReturn(new File("testPath"));
		when(fileChooser.getSelectedFile()).thenReturn(new File("testFile"));
		when(fileChooser.getFileFilter()).thenReturn(new FiltroInvalido());

		abrir.abrirSerializado(null, Abrir.ABRIR);

		assertNull("Expected no file to be opened when invalid file filter is used", abrir.getXml());
	}

	private static class FileReaderTestCase implements FileReader {
		private int posicao = 0;
		private String conteudo = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>Teste1</nome>\n<tipo>Tipo1</tipo>\n<valor>Valor1</valor>\n</entidade>\n<entidade>\n<nome>Teste2</nome>\n<tipo>Tipo2</tipo>\n<valor>Valor2</valor>\n</entidade>\n<entidade>\n<nome>Teste3</nome>\n<tipo>Tipo3</tipo>\n<valor>Valor3</valor>\n</entidade>\n<entidade>\n<nome>Teste4</nome>\n<tipo>Tipo4</tipo>\n<valor>Valor4</valor>\n</entidade>\n<entidade>\n<nome>Teste5</nome>\n<tipo>Tipo5</tipo>\n<valor>Valor5</valor>\n</entidade>\n</entidades>";

		public int read() throws IOException {
			int leitura;
			if (posicao >= conteudo.length()) {
				leitura = -1;
			} else {
				leitura = (int) conteudo.charAt(posicao++);
			}
			return leitura;
		}
	}

	private static class FiltroInvalido extends FileFilter {
		@Override
		public boolean accept(File f) {
			return false;
		}

		@Override
		public String getDescription() {
			return "Invalid File Filter";
		}
	}
}