package com.gitahinganga.pesabu.business;


import java.util.Map;

public interface Calculator {

	public enum Type {
		LOAN,
		TBILL,
		TBOND,
		PAYE
	}

	public Map<CalculationPiece, Object> calculate(Map<CalculationPiece, Object> operands);
}
