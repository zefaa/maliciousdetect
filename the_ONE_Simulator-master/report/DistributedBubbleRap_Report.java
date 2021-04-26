/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import core.DTNHost;
import core.SimScenario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import routing.DecisionEngineRouter;
import routing.MessageRouter;
import routing.RoutingDecisionEngine;
import routing.community.help;

/**
 *
 * @author M S I
 */
public class DistributedBubbleRap_Report extends Report {

    Map<DTNHost, Double> saveRerata = new HashMap<DTNHost, Double>();

    public void done() {
        List<DTNHost> nodes = SimScenario.getInstance().getHosts(); //
        for (DTNHost ho : nodes) { //
            MessageRouter r = ho.getRouter();
            if (!(r instanceof DecisionEngineRouter)) {
                continue;
            }
            RoutingDecisionEngine de = ((DecisionEngineRouter) r).getDecisionEngine();
            if (!(de instanceof help)) {
                continue;
            }
            help cd = (help) de;
            double hitungRerata = cd.Rerata();

            saveRerata.put(ho, hitungRerata);
        }
        for (Map.Entry<DTNHost, Double> entry : saveRerata.entrySet()) {
            String print = "";

            DTNHost key = entry.getKey();
            double value = entry.getValue();

            write("Node\n"+key + "\t" + "\nWaktu Kontak"+value);
        }

        super.done();
    }

}
