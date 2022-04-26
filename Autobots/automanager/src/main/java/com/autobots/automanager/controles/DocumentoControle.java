package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
  @Autowired
  private DocumentoRepositorio RepositorioDocumento;
  
 
  @Autowired
  private DocumentoSelecionador selecionarDocumento;
  
  @Autowired
  private ClienteRepositorio clienteRepositorio;
  
  
  @Autowired
  private ClienteSelecionador clienteSelecionador;
  
  
  @GetMapping("/documentos")
  public List<Documento> obterDocumento() {
	  List<Documento> documento =RepositorioDocumento.findAll();
    return documento;
  }

  @GetMapping("documento/{id}")
  public Documento obterDocumento(@PathVariable long id) {
    List<Documento> documentos = RepositorioDocumento.findAll();
    return selecionarDocumento.selecionar(documentos, id);
  }

  @PutMapping("/cadastro")
  public void cadastrarDocumento(@RequestBody Cliente cliente) {
	 List<Cliente> clientes = clienteRepositorio.findAll();
	 Cliente clienteAlvo = clienteSelecionador.selecionar(clientes, cliente.getId());
	 clienteAlvo.getDocumentos().addAll(cliente.getDocumentos());
	 clienteRepositorio.save(clienteAlvo);
  }

  @DeleteMapping("/excluir/{clienteId}/{documentoId}")
  public void deletarDocumento(@PathVariable long clienteId, @PathVariable long documentoId) {
	  Cliente cliente = clienteRepositorio.getById(clienteId);
	  List<Documento> listaDocumentos = cliente.getDocumentos();
	  Documento findDocumento = null;
	  for (Documento documento : listaDocumentos) {
		  if(documento.getId()== documentoId) {
			  findDocumento = documento;
		  }	  
	}
	listaDocumentos.remove(findDocumento);	  
	clienteRepositorio.save(cliente);	  
  }

}