package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.ChamadoRequest;
import br.com.sistemas.chamados.dto.ChamadoResponse;
import br.com.sistemas.chamados.entity.Chamado;
import br.com.sistemas.chamados.entity.StatusChamado;
import br.com.sistemas.chamados.entity.Tecnico;
import br.com.sistemas.chamados.exception.ChamadoFechadoException;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.repository.ChamadoRepository;
import br.com.sistemas.chamados.repository.ClienteRepository;
import br.com.sistemas.chamados.repository.CategoriaRepository;
import br.com.sistemas.chamados.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Chamado atribuirTecnico(Long id, Long tecnicoId) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        if (chamado.getStatus() == StatusChamado.FECHADO) {
            throw new ChamadoFechadoException("Impedido: O chamado já está fechado.");
        }

        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));

        chamado.setTecnico(tecnico);
        return chamadoRepository.save(chamado);
    }

    public Chamado atualizarStatus(Long id, StatusChamado novoStatus) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        if (chamado.getStatus() == StatusChamado.FECHADO) {
            throw new ChamadoFechadoException("Impedido: Não é possível alterar um chamado fechado.");
        }

        chamado.setStatus(novoStatus);

        if (novoStatus == StatusChamado.FECHADO) {
            chamado.setDataFechamento(LocalDateTime.now());
        }

        return chamadoRepository.save(chamado);
    }

    public List<ChamadoResponse> listarTodos() {
        return chamadoRepository.findAll().stream()
                .map(ChamadoResponse::new)
                .collect(Collectors.toList());
    }
}