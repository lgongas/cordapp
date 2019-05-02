package com.loken.flows;


import co.paralleluniverse.fibers.Suspendable;
import com.loken.contracts.PocketContract;
import com.loken.states.PocketState;
import net.corda.core.contracts.Amount;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.Collections;
import java.util.Currency;

@InitiatingFlow
@StartableByRPC
public class ExampleFlow extends FlowLogic<SignedTransaction>{
/*    private final String pocketName;
    private final int amount;*/

private final Amount<Currency> amount;

    // Initialize variables
    public ExampleFlow(Amount<Currency> amount) {
        /*this.pocketName = pocketName;*/
        this.amount = amount;
    }

    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    public SignedTransaction call() throws FlowException {
        // Choose transaction's notary.
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        // Get a reference to own identity.
        Party issuer = getOurIdentity();

        // Create PocketState (issue pocket).
        PocketState pocketState = new PocketState(issuer,amount);
        PocketContract.Issue command = new PocketContract.Issue();

        // Build transaction.
        TransactionBuilder transactionBuilder = new TransactionBuilder();
        transactionBuilder.setNotary(notary);
        transactionBuilder.addOutputState(pocketState, PocketContract.ID);
        transactionBuilder.addCommand(command, issuer.getOwningKey());

        // Check validity of transaction based on PocketContract
        transactionBuilder.verify(getServiceHub());

        // Sign transaction with private key for immutability
        SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(transactionBuilder);

        // Notarize transaction and record it in platform
        return subFlow(new FinalityFlow(signedTransaction, Collections.emptyList()));

    }
}

