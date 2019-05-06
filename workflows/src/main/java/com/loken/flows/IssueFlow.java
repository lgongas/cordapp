package com.loken.flows;


import net.corda.core.contracts.Amount;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.OpaqueBytes;
import net.corda.finance.flows.AbstractCashFlow;
import net.corda.finance.flows.CashIssueAndPaymentFlow;
import net.corda.finance.flows.CashIssueFlow;
import net.corda.finance.flows.CashPaymentFlow;

import java.util.Collections;
import java.util.Currency;

@InitiatingFlow
@StartableByRPC
public class IssueFlow extends FlowLogic<AbstractCashFlow.Result> {

    private Amount<Currency> amount;
    private Party recipient;


    public IssueFlow(Amount<Currency> amount, Party recipient) {
        this.amount = amount;
        this.recipient = recipient;
    }

    @Override
    public AbstractCashFlow.Result call() throws FlowException {
        // Choose transaction's notary.
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        OpaqueBytes issueRef = OpaqueBytes.of((byte) 0);
        Boolean anonymous = false;


        AbstractCashFlow.Result result = subFlow(new CashIssueFlow(amount, issueRef, notary));

        AbstractCashFlow.Result result2 = subFlow(new CashPaymentFlow(amount, recipient, false, notary));

        //AbstractCashFlow.Result result = subFlow(new CashIssueAndPaymentFlow(amount, issueRef, recipient, anonymous, notary));

        return result2;

        //SignedTransaction stx = result.getStx();

        // Notarize transaction and record it in platform
        // return subFlow(new FinalityFlow(stx, Collections.emptyList()));


        // run vaultQuery contractStateType: net.corda.finance.contracts.asset.Cash$State
    }
}
