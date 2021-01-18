package com.desafio.sessao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VOTACAO_VOTO")
public class Voto implements Serializable {
	
	private static final long serialVersionUID = 3648907217450912987L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="voto_sequence")
	@SequenceGenerator(name="voto_sequence", sequenceName="voto_seq")
    private Long codigo;

    @NotBlank
    @Column(name = "CPF")
    private String cpf;

    @NotNull
    @Column(name = "ESCOLHA")
    private Boolean escolha;

    @NotNull
    @Column(name = "CODIGOPAUTA")
    private Long codigoPauta;


}
