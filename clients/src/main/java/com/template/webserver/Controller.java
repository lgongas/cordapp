package com.template.webserver;

import net.corda.core.contracts.Amount;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.finance.contracts.asset.Cash;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.loken.flows.*;
import java.util.Currency;
import java.util.Map;
import java.util.Set;


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


    @GetMapping(value = "/states", produces = "text/plain")
    private String states() {
         return proxy.vaultQuery(Cash.State.class).toString();
    }

    @RequestMapping(value = "/issue", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String issue(@RequestBody Map<String, Object> json_issue) throws Exception {
        JSONObject jsonObject = new JSONObject(json_issue);
        String value = jsonObject.get("amount") + " COP";
        Amount<Currency> amount = Amount.parseCurrency(value);
        proxy.startFlowDynamic(IssueFlow.class,amount);
        return "200";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String transfer(@RequestBody Map<String, Object> json_issue) throws Exception {
        JSONObject jsonObject = new JSONObject(json_issue);

        String value = jsonObject.get("amount") + " COP";
        Amount<Currency> amount = Amount.parseCurrency(value);
        String user = "CO" + jsonObject.get("user");
        Party recipient = proxy.partiesFromName(user, true).toArray(new Party[0])[0];

        proxy.startFlowDynamic(TransferFlow.class,recipient,amount);
        return "200";
    }
}