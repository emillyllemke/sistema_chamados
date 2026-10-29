package br.com.sistemas.chamados.dto;
import br.com.sistemas.chamados.entity.HistoricoStatus;
import java.time.LocalDateTime;

public class HistoricoStatusResponse {
    private String statusAnterior;
    private String statusNovo;
    private LocalDateTime dataMudanca;

    public HistoricoStatusResponse(HistoricoStatus h) {
        this.statusAnterior = h.getStatusAnterior() != null ? h.getStatusAnterior().name() : null;
        this.statusNovo = h.getStatusNovo().name();
        this.dataMudanca = h.getDataMudanca();
    }
}