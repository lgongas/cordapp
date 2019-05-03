package com.template.webserver;

import net.corda.core.contracts.Amount;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.finance.contracts.asset.Cash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.loken.flows.*;
import java.util.Currency;
import net.corda.core.



/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
public class Controller {
    private final CordaRPCOps proxy;
    private final static Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(NodeRPCConnection rpc) {
        this.proxy = rpc.proxy;
    }

    @GetMapping(value = "/templateendpoint", produces = "text/plain")
    private String templateendpoint() {
        return "Define an endpoint here.";
    }

    @GetMapping(value = "/addresses", produces = "text/plain")
    private String addresses() {
        return proxy.nodeInfo().getAddresses().toString();
    }

    @GetMapping(value = "/issue", produces = "text/plain")
    private String issue() {
        //Amount amount = new Amount(10,"COP");
        Amount<Currency> amount = Amount.parseCurrency("12 COP");
        proxy.startFlowDynamic(IssueFlow.class,amount);
        return "200";
    }

    @GetMapping(value = "/states", produces = "text/plain")
    private String states() {
         return proxy.vaultQuery(Cash.State.class).toString();
    }
}