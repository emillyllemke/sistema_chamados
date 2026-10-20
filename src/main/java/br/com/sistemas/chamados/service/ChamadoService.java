package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.ChamadoRequest;
import br.com.sistemas.chamados.dto.ChamadoResponse;
import br.com.sistemas.chamados.entity.Chamado;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.repository.ChamadoRepository;
import br.com.sistemas.chamados.repository.ClienteRepository;
import br.com.sistemas.chamados.repository.CategoriaRepository;
import br.com.sistemas.chamados.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;
    private final TecnicoRepository tecnicoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository, ClienteRepository clienteRepository,
                          CategoriaRepository categoriaRepository, TecnicoRepository tecnicoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.clienteRepository = clienteRepository;
        this.categoriaRepository = categoriaRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public ChamadoResponse salvar(ChamadoRequest request) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(request.getTitulo());
        chamado.setDescricao(request.getDescricao());
        chamado.setPrioridade(request.getPrioridade());

        if (request.getClienteId() != null) {
            chamado.setCliente(clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado")));
        }

        if (request.getCategoriaId() != null) {
            chamado.setCategoria(categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada")));
        }

        if (request.getTecnicoId() != null) {
            chamado.setTecnico(tecnicoRepository.findById(request.getTecnicoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Técnico não encontrado")));
        }

        Chamado chamadoSalvo = chamadoRepository.save(chamado);
        return new ChamadoResponse(chamadoSalvo);
    }

    public List<ChamadoResponse> listarTodos() {
        return chamadoRepository.findAll().stream()
                .map(ChamadoResponse::new)
                .collect(Collectors.toList());
    }
}