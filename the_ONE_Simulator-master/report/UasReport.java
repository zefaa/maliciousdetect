/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import core.DTNHost;
import core.Settings;
import core.SimScenario;
import java.util.List;
import routing.DecisionEngineRouter;
import routing.MessageRouter;
import routing.RoutingDecisionEngine;
import routing.community.InterfaceUas;

/**
 *
 * @author Rosemary
 */
public class UasReport extends Report {

    public UasReport() {
        Settings settings = getSettings();
        init();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void done() {
        List<DTNHost> nodes = SimScenario.getInstance().getHosts();

       
        for(DTNHost host : nodes){
            MessageRouter r = host.getRouter();
            if(!(r instanceof DecisionEngineRouter)){
                continue;
            }
            RoutingDecisionEngine de = ((DecisionEngineRouter) r).getDecisionEngine();
            if(!(de instanceof InterfaceUas)){
                continue;
            }
            
            InterfaceUas fe = (InterfaceUas) de;
            int[] a = fe.Sentraliti();
            
            String cetak = "";
            for (int i=0;i<a.length;i++){
                cetak += a[i] + ", ";
            }
              write(host+", "+cetak);  
        }
        
        super.done();
    }

}
