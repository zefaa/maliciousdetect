/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import core.DTNHost;
import core.Settings;
import core.SimScenario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import routing.DecisionEngineRouter;
import routing.MessageRouter;
import routing.RoutingDecisionEngine;
import routing.community.bantuReport_DistBubb;
import routing.community.help;


/**
 *
 * @author M S I
 */
public class DistributedBubbleRap_CentralityReport extends Report{

    public DistributedBubbleRap_CentralityReport() {
        init();
    }

    public void done() {
        List<DTNHost> nodes = SimScenario.getInstance().getHosts(); //
        Map<DTNHost, List<Integer>> arraycentralities = new HashMap<DTNHost, List<Integer>>();

        for (DTNHost ho : nodes) {
            MessageRouter r = ho.getRouter();
            if (!(r instanceof DecisionEngineRouter)) {
                continue;
            }
            RoutingDecisionEngine de = ((DecisionEngineRouter) r).getDecisionEngine();
            if (!(de instanceof bantuReport_DistBubb)) {
                continue;
            }
            bantuReport_DistBubb cd = (bantuReport_DistBubb) de;

            int[] myArray = cd.Centrality();
            List<Integer> array = new ArrayList<Integer>();
            for (int cent : myArray) {
                array.add(cent);
            }
            arraycentralities.put(ho, array);
        }
        for (Map.Entry<DTNHost, List<Integer>> entry : arraycentralities.entrySet()) {
            DTNHost a = entry.getKey();
            Integer b = a.getAddress();
            
            write(" " + b + ' ' + "global" + ' ' + entry.getValue());
    
                }
    }
}
