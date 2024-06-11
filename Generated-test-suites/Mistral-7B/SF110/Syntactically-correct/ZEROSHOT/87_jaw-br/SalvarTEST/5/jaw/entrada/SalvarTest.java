package jaw.entrada.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;

import jaw.metamodelo.Entidade;
import jaw.metamodelo.Metamodelo;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private Serializacao serializacao;
 private SerializacaoXML serializacaoXML;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = mock(ProcessarEntidades.class);
 entidades = new ArrayList<Entidade>();
 entidades.add(new Entidade());
 entidades.add(new Entidade());
 salvar = new Salvar(form, 20);
 serializacao = mock(Serializacao.class);
 serializacaoXML = mock(SerializacaoXML.class);
 when(serializacao.salvar(entidades, "/temp/teste.jaw")).thenReturn(true);
 when(serializacaoXML.entidades2XML(entidades)).thenReturn(createXMLString());
 }

 @Test
 public void testSalvarJaw() {
 salvar.salvar(entidades, "/temp/teste.jaw");
 assertFalse(salvar.vela);
 assertSame(entidades, salvar.entidade);
 assertTrue(new File("/temp/teste.jaw").exists());
 try {
 FileWriter writer = new FileWriter("/temp/teste.jaw");
 writer.write("teste");
 writer.close();
 } catch (IOException e) {
 // ignore
 }
 salvar.salvar(entidades, "/temp/teste.jaw");
 assertFalse(salvar.vela);
 assertSame(entidades, salvar.entidade);
 assertFalse(new File("/temp/teste.jaw").exists());
 }

 @Test
 public void testSalvarJawComNomeArquivo() {
 salvar = new Salvar(form, 20);
 when(form.getNomeArquivoSalvo()).thenReturn("novoProjeto.jaw");
 salvar.salvar(entidades, null);
 assertFalse(salvar.vela);
 assertSame(entidades, salvar.entidade);
 assertFalse(new File("novoProjeto.jaw").exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarJawSemEntidades() {
 salvar = new Salvar(form, 20);
 salvar.salvar(new ArrayList<Entidade>(), "/temp/teste.jaw");
 }

 @Test
 public void testSalvarXML() {
 salvar = new Salvar(form, 10);
 salvar.salvar(entidades, "/temp/teste.jaw.xml");
 assertFalse(salvar.vela);
 assertSame(entidades, salvar.entidade);
 assertNull(salvar.xml);
 try {
 FileWriter writer = new FileWriter("/temp/teste.jaw.xml");
 writer.write("teste");
 writer.close();
 } catch (IOException e) {
 // ignore
 }
 salvar.salvar(entidades, "/temp/teste.jaw.xml");
 assertFalse(salvar.vela);
 assertSame(entidades, salvar.entidade);
 assertNull(salvar.xml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarXMLSemEntidades() {
 salvar = new Salvar(form, 10);
 salvar.salvar(new ArrayList<Entidade>(), "/temp/teste.jaw.xml");
 }

 @Test
 public void testExportarXML() {
 salvar.exportarXML(entidades, "/temp/teste.jaw.xml");
 assertFalse(salvar.vela);
 assertSame(entidades, salvar.entidade);
 assertNotNull(salvar.xml);
 try {
 Document doc = Document.parse(new File("/temp/teste.jaw.xml"));
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>entidade1</nome>\n<id>1</id>\n<atributo1>valor1</atributo1>\n<atributo2>valor2</atributo2>\n</entidade>\n<entidade>\n<nome>entidade2</nome>\n<id>2</id>\n<atributo1>valor3</atributo1>\n<atributo2>valor4</atributo2>\n</entidade>\n</entidades>", salvar.xml);
 } catch (JDOMException e) {
 // ignore
 }
 try {
 FileWriter writer = new FileWriter("/temp/teste.jaw.xml");
 writer.write("teste");
 writer.close();
 } catch (IOException e) {
 // ignore
 }
 salvar.exportarXML(entidades, "/temp/teste.jaw.xml");
 assertFalse(salvar.vela);
 assertSame(entidades, salvar.entidade);
 assertNull(salvar.xml);
 }

 private String createXMLString() {
 XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
 String xml = null;
 try {
 xml = outputter.outputString(new Document(serializacaoXML.entidades2XML(entidades)));
 } catch (JDOMException e) {
 // ignore
 }
 return xml;
 }

}