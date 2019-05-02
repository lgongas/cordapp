package com.loken.states;

import com.google.common.collect.ImmutableList;
import com.loken.contracts.PocketContract;
import net.corda.core.contracts.Amount;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.Currency;
import java.util.List;

@BelongsToContract(PocketContract.class)
// Implement ContractState to mark instances of class as states on ledger
public class PocketState implements ContractState {

    // Attributes
/*    private String pocketName;
    private Party owner;
    private int amount;*/

    public Party owner;
    public Amount<Currency> amount;

    // Constructor
    public PocketState(Party owner, Amount<Currency> amount) {
        //this.pocketName = pocketName;
        this.owner = owner;
        this.amount = amount;
    }

    // Get methods
    //public String getPocketName() { return pocketName; }

    public Party getOwner() { return owner; }

    public Amount<Currency> getAmount() { return amount; }

    // Create state
    public static void main(Party owner, Amount<Currency> amount){
        PocketState pocketState = new PocketState(owner, amount);
    }

    // List of parties that should be notified when state changes
    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner); // Make list of participants
    }
}

