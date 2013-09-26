package com.gitahinganga.pesabu.business.loan;


import com.gitahinganga.pesabu.business.CalculationPiece;
import com.gitahinganga.pesabu.business.Calculator;

import java.util.HashMap;
import java.util.Map;

public class LoanCalculator implements Calculator {

	@Override
	public Map<CalculationPiece, Object> calculate(Map<CalculationPiece, Object> operands) {

		Map<CalculationPiece, Object> results = new HashMap<CalculationPiece, Object>();

		LoanOperand missing = (LoanOperand) operands.get(LoanOperand.MISSING);

		switch (missing) {
			case PRINCIPAL:
				calculatePrincipal(operands);
				break;
			case RATE:
				calculateRate(operands);
				break;
			case PERIOD:
				calculatePeriod(operands);
				break;
			case INSTALLMENT:
				calculateInstallment(operands);
				break;
		}
		results.put(LoanResult.TOTAL_REPAID, (Double) operands.get(LoanOperand.INSTALLMENT)
				* (Integer) operands.get(LoanOperand.PERIOD));
		results.put(LoanResult.TOTAL_INTEREST, (Double) results.get(LoanResult.TOTAL_REPAID)
				- (Double) operands.get(LoanOperand.PRINCIPAL));
		return results;
	}

	private void calculatePrincipal(Map<CalculationPiece, Object> operands) {
		Double rate = (Double) operands.get(LoanOperand.RATE);
		Integer period = (Integer) operands.get(LoanOperand.PERIOD);
		Double installment = (Double) operands.get(LoanOperand.INSTALLMENT);

		Double monthlyInterest = ((rate / 12) / 100);
		Double principle = installment / ((monthlyInterest / (Math.pow((1 + monthlyInterest), period) - 1))
				+ monthlyInterest);

		operands.put(LoanOperand.PRINCIPAL, principle);
	}

	private void calculateRate(Map<CalculationPiece, Object> operands) {
		Double principal = (Double) operands.get(LoanOperand.PRINCIPAL);
		Integer period = (Integer) operands.get(LoanOperand.PERIOD);
		Double installment = (Double) operands.get(LoanOperand.INSTALLMENT);

		double installmentOverPrinciple = installment / principal;
		Double rate = (Math.pow((installmentOverPrinciple / (installmentOverPrinciple - 1)), (1 / period)) - 1) * 12;

		operands.put(LoanOperand.RATE, rate);
	}

	private void calculatePeriod(Map<CalculationPiece, Object> operands) {
		Double principal = (Double) operands.get(LoanOperand.PRINCIPAL);
		Double rate = (Double) operands.get(LoanOperand.RATE);
		Double installment = (Double) operands.get(LoanOperand.INSTALLMENT);

		Double monthlyInterest = ((rate / 12) / 100);
		Integer period = (int) Math.round(Math.log(((monthlyInterest / ((installment / principal) - monthlyInterest))) + 1)
				/ Math.log((1 + monthlyInterest)));

		operands.put(LoanOperand.PERIOD, period);
	}

	private void calculateInstallment(Map<CalculationPiece, Object> operands) {
		Double principal = (Double) operands.get(LoanOperand.PRINCIPAL);
		Double rate = (Double) operands.get(LoanOperand.RATE);
		Integer period = (Integer) operands.get(LoanOperand.PERIOD);

		double monthlyInterest = ((rate / 12) / 100);
		double installment = principal * ((monthlyInterest / (Math.pow((1 + monthlyInterest), period) - 1)) + monthlyInterest);

 		operands.put(LoanOperand.INSTALLMENT, installment);
	}
}
