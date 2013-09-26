package com.gitahinganga.pesabu.business.loan;

import com.gitahinganga.pesabu.business.CalculationPiece;

public enum LoanOperand implements CalculationPiece {
	PRINCIPAL,
	RATE,
	PERIOD,
	INSTALLMENT,
	MISSING
}
