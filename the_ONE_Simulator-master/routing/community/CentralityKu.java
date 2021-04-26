/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routing.community;

import core.DTNHost;
import core.SimClock;
import java.util.List;
import java.util.Map;

/**
 *
 * @author M S I
 */
public interface CentralityKu extends Centrality {

    public int[] getArrayGlobalCentrality(Map<DTNHost, List<Duration>> connHistory);

}
