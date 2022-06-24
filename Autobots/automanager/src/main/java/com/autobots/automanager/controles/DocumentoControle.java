package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentoControle {
  @Autowired
  private DocumentoRepositorio RepositorioDocumento;
  
  @Autowired
  private DocumentoSelecionador selecionarDocumento;
  
  @Autowired
  private AdicionadorLinkDocumento adicionadorLinkDocumento;
  
  
	@GetMapping("/documento/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		List<Documento> documentos = RepositorioDocumento.findAll();
		Documento documento = selecionarDocumento.selecionar(documentos, id);
		if (documento == null) {
			ResponseEntity<Documento> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLinkDocumento.adicionarLink(documento);
			ResponseEntity<Documento> resposta = new ResponseEntity<Documento>(documento, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = RepositorioDocumento.findAll();
		if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {	
			adicionadorLinkDocumento.adicionarLink(documentos);
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(documentos, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	
	@PutMapping("/documento/atualizar")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Documento documento = RepositorioDocumento.getById(atualizacao.getId());
		if (documento != null) {
			DocumentoAtualizador atualizador = new DocumentoAtualizador();
			atualizador.atualizar(documento, atualizacao);
			RepositorioDocumento.save(documento);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}



	@PostMapping("/documento/cadastro")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (documento.getId() == null) {
			RepositorioDocumento.save(documento);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);

	}


	@DeleteMapping("/documento/excluir")
	public ResponseEntity<?> excluirDocumento(@RequestBody Documento exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Documento documento = RepositorioDocumento.getById(exclusao.getId());
		if (documento != null) {
			RepositorioDocumento.delete(documento);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}

