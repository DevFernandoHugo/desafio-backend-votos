package com.desafio.sessao.utils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class ValidaCpfUtils {
	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	
	public static final int NUMERO_01 = 1;
	public static final int NUMERO_09 = 9;
	public static final int NUMERO_10 = 10;
	public static final int NUMERO_11 = 11;
	public static final int NUMERO_12 = 12;
	public static final int NUMERO_14 = 14;

	protected ValidaCpfUtils() {

	}

	public static boolean isValid(String cpf) {
		if (cpf == null || cpf.isEmpty()) {
			return false;
		} else {
			return (isValidCPF(cpf));
		}
	} 

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - NUMERO_01; indice >= 0; indice--) {
			int digito = Integer
					.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = NUMERO_11 - soma % NUMERO_11; 

		if (soma > NUMERO_09) {
			return 0;
		} else {
			return soma;
		}
	}

	private static String padLeft(String text, char character) {
		return String.format("%11s", text).replace(' ', character);
	}

	protected static boolean isValidCPF(String cpf) {
		cpf = cpf.trim().replace(".", "").replace("-", "");
		if ((cpf == null) || (cpf.length() != NUMERO_11)) {
			return false;
		}

		for (int j = 0; j < NUMERO_10; j++) {
			if (padLeft(Integer.toString(j), Character.forDigit(j, NUMERO_10)).equals(cpf)) {
				return false;
			}
		}

		Integer digito1 = calcularDigito(cpf.substring(0, NUMERO_09), pesoCPF);
		Integer digito2 = calcularDigito(cpf.substring(0, NUMERO_09) + digito1, pesoCPF);
		return cpf.equals(cpf.substring(0, NUMERO_09) + digito1.toString() + digito2.toString());
	}

}
