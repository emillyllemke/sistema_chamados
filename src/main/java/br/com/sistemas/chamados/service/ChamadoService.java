package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.ChamadoRequest;
import br.com.sistemas.chamados.dto.ChamadoResponse;
import br.com.sistemas.chamados.entity.Chamado;
import br.com.sistemas.chamados.entity.StatusChamado;
import br.com.sistemas.chamados.entity.Tecnico;
import br.com.sistemas.chamados.exception.ChamadoFechadoException;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.repository.*;
import br.com.sistemas.chamados.entity.Comentario;
import br.com.sistemas.chamados.dto.ComentarioRequest;
import br.com.sistemas.chamados.dto.ComentarioResponse;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemas.chamados.entity.Prioridade;
import br.com.sistemas.chamados.entity.HistoricoStatus;
import br.com.sistemas.chamados.repository.HistoricoStatusRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChamadoService {
    private final ComentarioRepository comentarioRepository;
    private final ChamadoRepository chamadoRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;
    private final TecnicoRepository tecnicoRepository;
    private final HistoricoStatusRepository historicoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          ClienteRepository clienteRepository,
                          CategoriaRepository categoriaRepository,
                          TecnicoRepository tecnicoRepository,
                          ComentarioRepository comentarioRepository,
                          HistoricoStatusRepository historicoRepository) {

        this.chamadoRepository = chamadoRepository;
        this.clienteRepository = clienteRepository;
        this.categoriaRepository = categoriaRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.comentarioRepository = comentarioRepository;
        this.historicoRepository = historicoRepository;
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

        if (chamado.getStatus() != novoStatus) {
            HistoricoStatus historico = new HistoricoStatus();
            historico.setStatusAnterior(chamado.getStatus());
            historico.setStatusNovo(novoStatus);
            historico.setChamado(chamado);
            historicoRepository.save(historico);
        }
        chamado.setStatus(novoStatus);

        if (novoStatus == StatusChamado.FECHADO) {
            chamado.setDataFechamento(LocalDateTime.now());
        }

        return chamadoRepository.save(chamado);
    }

    public Page<ChamadoResponse> listarComFiltros(StatusChamado status, Prioridade prioridade, Long clienteId, Pageable pageable) {
        return chamadoRepository.findByFiltros(status, prioridade, clienteId, pageable)
                .map(ChamadoResponse::new);
    }

    public Page<ChamadoResponse> listarPorCliente(Long clienteId, Pageable pageable) {
        return chamadoRepository.findByClienteId(clienteId, pageable)
                .map(ChamadoResponse::new);
    }

    public List<ChamadoResponse> listarTodos() {
        return chamadoRepository.findAll().stream()
                .map(ChamadoResponse::new)
                .collect(Collectors.toList());
    }

    public ComentarioResponse adicionarComentario(Long chamadoId, ComentarioRequest request) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado não encontrado"));

        Comentario comentario = new Comentario();
        comentario.setTexto(request.getTexto());
        comentario.setAutor(request.getAutor());
        comentario.setChamado(chamado);

        Comentario salvo = comentarioRepository.save(comentario);
        return new ComentarioResponse(salvo);
    }
}