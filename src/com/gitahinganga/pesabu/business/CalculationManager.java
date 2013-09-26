package com.gitahinganga.pesabu.business;

import com.gitahinganga.pesabu.business.loan.LoanCalculator;

import java.util.Map;

public class CalculationManager {

	public static Map<CalculationPiece, Object> calculate(Map<CalculationPiece, Object> operands, Calculator.Type type) {
		switch (type) {
			case LOAN:
				return new LoanCalculator().calculate(operands);
		}
		return null;
	}
}
