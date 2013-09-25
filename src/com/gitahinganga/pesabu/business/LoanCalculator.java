package com.gitahinganga.pesabu.business;


import java.util.HashMap;
import java.util.Map;

public class LoanCalculator {

	public enum Operand {
		PRINCIPAL,
		RATE,
		PERIOD,
		INSTALLMENT,
		MISSING
	}

	public enum Result {
		TOTAL_REPAID,
		TOTAL_INTEREST
	}

	public Map<Result, Object> calculate(Map<Operand, Object> operands) {

		Map<Result, Object> results = new HashMap<Result, Object>();

		Operand missing = (Operand) operands.get(Operand.MISSING);

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
		results.put(Result.TOTAL_REPAID, (Double) operands.get(Operand.INSTALLMENT)
				* (Integer) operands.get(Operand.PERIOD));
		results.put(Result.TOTAL_INTEREST, (Double) results.get(Result.TOTAL_REPAID)
				- (Double) operands.get(Operand.PRINCIPAL));
		return results;
	}

	public void calculatePrincipal(Map<Operand, Object> operands) {
		Double rate = (Double) operands.get(Operand.RATE);
		Integer period = (Integer) operands.get(Operand.PERIOD);
		Double installment = (Double) operands.get(Operand.INSTALLMENT);

		Double monthlyInterest = ((rate / 12) / 100);
		Double principle = installment / ((monthlyInterest / (Math.pow((1 + monthlyInterest), period) - 1))
				+ monthlyInterest);

		operands.put(Operand.PRINCIPAL, principle);
	}

	public void calculateRate(Map<Operand, Object> operands) {
		Double principal = (Double) operands.get(Operand.PRINCIPAL);
		Integer period = (Integer) operands.get(Operand.PERIOD);
		Double installment = (Double) operands.get(Operand.INSTALLMENT);

		double installmentOverPrinciple = installment / principal;
		Double rate = (Math.pow((installmentOverPrinciple / (installmentOverPrinciple - 1)), (1 / period)) - 1) * 12;

		operands.put(Operand.RATE, rate);
	}

	public void calculatePeriod(Map<Operand, Object> operands) {
		Double principal = (Double) operands.get(Operand.PRINCIPAL);
		Double rate = (Double) operands.get(Operand.RATE);
		Double installment = (Double) operands.get(Operand.INSTALLMENT);

		Double monthlyInterest = ((rate / 12) / 100);
		Integer period = (int) Math.round(Math.log(((monthlyInterest / ((installment / principal) - monthlyInterest))) + 1)
				/ Math.log((1 + monthlyInterest)));

		operands.put(Operand.PERIOD, period);
	}

	public void calculateInstallment(Map<Operand, Object> operands) {
		Double principal = (Double) operands.get(Operand.PRINCIPAL);
		Double rate = (Double) operands.get(Operand.RATE);
		Integer period = (Integer) operands.get(Operand.PERIOD);

		double monthlyInterest = ((rate / 12) / 100);
		double installment = principal * ((monthlyInterest / (Math.pow((1 + monthlyInterest), period) - 1)) + monthlyInterest);

 		operands.put(Operand.INSTALLMENT, installment);
	}
}
