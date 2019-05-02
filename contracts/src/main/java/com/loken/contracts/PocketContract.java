package com.loken.contracts;


import com.loken.states.PocketState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

public class PocketContract implements Contract {
    public static String ID = "com.loken.contracts.PocketContract";

    // Rules for valid transactions
    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

        if (tx.getCommands().size() != 1)
            throw new IllegalArgumentException("Transaction must have one command");

        Command command = tx.getCommand(0);
        List<PublicKey> requiresSigners = command.getSigners();
        CommandData commandType = command.getValue();


        // Establish constraints per command
        if (commandType instanceof Issue){
            // Shape constraints
            if (tx.getInputStates().size() != 0)
                throw new IllegalArgumentException("No input expected");

            if (tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("One output required");

            // Content constraints
            ContractState output = tx.getOutput(0);
            PocketState pocketOutput = (PocketState) output;

/*            if (pocketOutput.getAmount() < 0)
                throw new IllegalArgumentException("Amount must be positive.");*/

            if (!(output instanceof PocketState))
                throw new IllegalArgumentException("Output must be a PocketState.");

            // Required signer constraints
            Party issuer = pocketOutput.getOwner();
            PublicKey issuerKey = issuer.getOwningKey();

            if (!(command.getSigners().contains(issuerKey)))
                throw new IllegalArgumentException("Issuer has to sign.");

        }

        else if (commandType instanceof transferBank){
            // Shape constraints
            if (tx.getInputStates().size() != 1)
                throw new IllegalArgumentException("Must have one input.");
            if (tx.getOutputStates().size() != 0)
                throw new IllegalArgumentException("No output expected.");

            // Content constraints
            ContractState input = tx.getInput(0);
            PocketState pocketInput = (PocketState) input;

            if (!(input instanceof PocketState))
                throw new IllegalArgumentException("Input must be a PocketState.");

/*            if (pocketInput.getAmount() <= 0)
                throw new IllegalArgumentException("Amount must be greater than zero.");*/

            // Required signer constraints
            Party issuer = pocketInput.getOwner();
            PublicKey issuerKey = issuer.getOwningKey();

            if (!(command.getSigners().contains(issuerKey)))
                throw new IllegalArgumentException("Issuer has to sign.");

        }

        else if (commandType instanceof transferLoken){

            // Shape constraints
            if (tx.getInputStates().size() != 1)
                throw new IllegalArgumentException("Must have one input.");
            if (tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("Must have one output.");

            // Content constraints
            ContractState output = tx.getOutput(0);
            ContractState input = tx.getInput(0);

            PocketState pocketInput = (PocketState) input;

            if (!(input instanceof PocketState))
                throw new IllegalArgumentException("Input must be a PocketState.");
            if (!(output instanceof PocketState))
                throw new IllegalArgumentException("Output must be a PocketState.");


            // Required signer constraints
            Party issuer = pocketInput.getOwner();
            PublicKey issuerKey = issuer.getOwningKey();

            if (!(command.getSigners().contains(issuerKey)))
                throw new IllegalArgumentException("Issuer has to sign.");
        }

        else {
            throw new IllegalArgumentException("Command type not recognized.");
        }


    }

    /*
    // Create commands (CommandData denotes a class is a command type)
    public interface Commands extends CommandData {
        class Issue implements Commands { } // Create a pocket
        class transferBank implements Commands{}
        class transferLoken implements Commands{}}
    }
    */


    public static class Issue implements CommandData {}
    public static class transferBank implements CommandData {}
    public static class transferLoken implements CommandData {}


}
