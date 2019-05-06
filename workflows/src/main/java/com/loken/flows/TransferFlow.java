package com.loken.flows;


import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.Amount;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;
import net.corda.finance.flows.AbstractCashFlow;
import net.corda.finance.flows.CashPaymentFlow;

import java.util.Currency;


@InitiatingFlow
@StartableByRPC
public class TransferFlow extends FlowLogic<AbstractCashFlow.Result> {

    public Party recipient;
    public Amount<Currency> amount;

    public TransferFlow(Party recipient, Amount<Currency> amount) {
        this.recipient = recipient;
        this.amount = amount;
    }

    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    public AbstractCashFlow.Result call() throws FlowException {
        // Choose transaction's notary.
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        // Cash.generateSpend(serviceHub, txBuilder, amount, otherParty)

        AbstractCashFlow.Result result = subFlow(new CashPaymentFlow(amount, recipient, false, notary));

        //SignedTransaction stx = result.getStx();

        return result;
    }
}
