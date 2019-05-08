package com.loken.flows;

import net.corda.core.contracts.Amount;
import net.corda.core.contracts.Issued;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.utilities.OpaqueBytes;
import net.corda.finance.flows.AbstractCashFlow;
import net.corda.finance.flows.CashExitFlow;


import java.util.Currency;

@InitiatingFlow
@StartableByRPC
public class ExitFlow extends FlowLogic<AbstractCashFlow.Result> {

    private Amount<Currency> amount;

    public ExitFlow(Amount<Currency> amount) {
        this.amount = amount;
    }

    @Override
    public AbstractCashFlow.Result call() throws FlowException {

        OpaqueBytes issueRef = OpaqueBytes.of((byte) 0);


        AbstractCashFlow.Result result = subFlow(new CashExitFlow(amount,issueRef));

        return result;
    }
}
