/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import core.DTNHost;
import core.Message;
import core.SimScenario;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import routing.DecisionEngineRouter;
import routing.DecisionEngineRouterMalicious;
import routing.EpidemicDecisionEngine;
import routing.MessageRouter;
import routing.RoutingDecisionEngine;
import routing.RoutingDecisionEngineMalicious;

/**
 *
 * @author M S I
 */
public class NodeMaliciousReports extends Report {

    private String nodeList = "";
    private LinkedList<Message> drop = new LinkedList<Message>();

    public NodeMaliciousReports() {
        super();
    }

    @Override
    public void done() {
        List<DTNHost> nodes = SimScenario.getInstance().getHosts();
        for (DTNHost ho : nodes) {
            MessageRouter r = ho.getRouter();
            if (!(r instanceof DecisionEngineRouterMalicious)) {
                continue;
            }
            RoutingDecisionEngineMalicious de = ((DecisionEngineRouterMalicious) r).getDecisionEngine();
            if (!(de instanceof NodeMaliciousHelper)) {
                continue;
            }
            NodeMaliciousHelper nm = (NodeMaliciousHelper) de;

            drop = nm.getNodeMalicious();

            if (ho.toString().startsWith("mal")) { //cetak yang malicious aja. kalo mau liat semua dicomment aja.
              String cetak = " ";
            Iterator<Message> iter = drop.iterator(); // baca linkedlist
            while (iter.hasNext()) {
                Message a = iter.next();
                cetak+= a.toString() + "  " ;
            }
            write(ho + cetak);  
            }
            
        }
        super.done();
    }
}
