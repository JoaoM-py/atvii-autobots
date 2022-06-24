package com.autobots.automanager.controles;

import java.util.List;

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


import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.AdicionadorLinkTelefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
public class TelefoneControle {
  @Autowired
  private TelefoneRepositorio RepositorioTelefone;
  
  
  @Autowired
  private TelefoneSelecionador selecionarTelefone;
  
  @Autowired
  private AdicionadorLinkTelefone adicionadorLinkTelefone;

  
  
  
	@GetMapping("/telefone/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		List<Telefone> telefones = RepositorioTelefone.findAll();
		Telefone telefone = selecionarTelefone.selecionar(telefones, id);
		if (telefone == null) {
			ResponseEntity<Telefone> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLinkTelefone.adicionarLink(telefone);
			ResponseEntity<Telefone> resposta = new ResponseEntity<Telefone>(telefone, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = RepositorioTelefone.findAll();
		if (telefones.isEmpty()) {
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {	
			adicionadorLinkTelefone.adicionarLink(telefones);
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(telefones, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/telefone/cadastro")
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (telefone.getId() == null) {
			RepositorioTelefone.save(telefone);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);

	}
	
	
	
	@PutMapping("/telefone/atualizar")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Telefone telefone = RepositorioTelefone.getById(atualizacao.getId());
		if (telefone != null) {
			TelefoneAtualizador atualizador = new TelefoneAtualizador();
			atualizador.atualizar(telefone, atualizacao);
			RepositorioTelefone.save(telefone);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	
	

	@DeleteMapping("/telefone/excluir")
	public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Telefone telefone = RepositorioTelefone.getById(exclusao.getId());
		if (telefone != null) {
			RepositorioTelefone.delete(telefone);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

}
