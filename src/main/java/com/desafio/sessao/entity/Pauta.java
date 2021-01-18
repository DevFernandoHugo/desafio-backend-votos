package com.desafio.sessao.entity;

import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VOTACAO_PAUTA")
public class Pauta implements Serializable {
	
	private static final long serialVersionUID = 2904093042702722750L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pauta_sequence")
	@SequenceGenerator(name="pauta_sequence", sequenceName="pauta_seq")
    private long codigo;
    
    @Column(name = "DESCRICAO")
    private String descricao;
}
