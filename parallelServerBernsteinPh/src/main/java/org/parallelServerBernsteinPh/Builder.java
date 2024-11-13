package org.parallelServerBernsteinPh;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oristool.models.gspn.GSPNSteadyState;
import org.oristool.models.gspn.GSPNTransient;
import org.oristool.models.stpn.*;
import org.oristool.models.stpn.trees.StochasticTransitionFeature;
import org.oristool.petrinet.Marking;
import org.oristool.petrinet.PetriNet;
import org.oristool.petrinet.Place;
import org.oristool.util.Pair;


public class Builder {
    private Marking marking;
    private int numPlaces;
    private int numPh;
    private int[] arrivalRates;
    private int poolSize;

    // Costruttore della classe
    public Builder(Marking marking, int numPlaces, int numPh, int[] arrivalRates, int poolSize) {
        this.marking = marking;
        this.numPlaces = numPlaces;
        this.numPh = numPh;
        this.arrivalRates = arrivalRates;
        this.poolSize = poolSize;
    }

    public PetriNet build() {
        PetriNet net = new PetriNet();
        // Generare Pn e An
        for (int i = 1; i <= numPlaces; i++) {
            net.addPlace("P" + i);
            net.addPlace("A" + i);
        }
        Place Pool = net.addPlace("Pool");

        //Generare Phn
        for (int i = 1; i <= numPh; i++) {
            net.addPlace("Ph" + i);
        }

        // Generare Transizioni
        for (int i = 1; i <= numPh; i++) {
            net.addTransition("t" + i);
        }


        for (int i = 1; i <= numPlaces; i++) {
            for (int j = 1; j <= numPh; j++) {
                net.addTransition("t" + i + j);
            }
        }

        //Generare Precondizioni e PostCondizioni  ->Pool
        for(int i = 1; i<= numPlaces;i++){
            for(int j = 1; j<= numPh;j++){
                //An -> tnk
                net.addPrecondition(net.getPlace("A"+i), net.getTransition("t"+i+j));
                //An <- tnk
                net.addPostcondition(net.getTransition("t"+i+j), net.getPlace("A"+i));
                //Pool ->Tnk
                net.addPrecondition(Pool, net.getTransition("t"+i+j));
                //Tnk -> Phn
                net.addPostcondition(net.getTransition("t"+i+j), net.getPlace("Ph" + j));
            }
        }
        //PHn -> tn
        for(int i = 1; i <= numPh;i++){
            net.addPrecondition(net.getPlace("Ph"+i), net.getTransition("t"+i));
        }
        //Tn -> Ph n+1
        for(int i = 1; i<numPh; i++)
        {
            net.addPostcondition(net.getTransition("t"+ i), net.getPlace("Ph"+ (i+1)));
        }
        //Phn -> Pool
        net.addPostcondition(net.getTransition("t"+ numPh), net.getPlace("Pool"));

        //Generating Properties
        for(int i = 1; i <= numPlaces; i++)
        {
            marking.setTokens(net.getPlace("A"+ i), 1);
            marking.setTokens(net.getPlace("P"+ i), arrivalRates[i-1]);
            marking.setTokens(net.getPlace("Ph"+ i), 0);
        }
        marking.setTokens(Pool, poolSize);
        net.getTransition("t1").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("1*Ph1", net)));
        net.getTransition("t11").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.25*P1", net)));
        net.getTransition("t12").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.25*P1", net)));
        net.getTransition("t13").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.25*P1", net)));
        net.getTransition("t14").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.25*P1", net)));
        net.getTransition("t2").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("2*Ph2", net)));
        net.getTransition("t21").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.1*P2", net)));
        net.getTransition("t22").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.2*P2", net)));
        net.getTransition("t23").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.3*P2", net)));
        net.getTransition("t24").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.4*P2", net)));
        net.getTransition("t3").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("3*Ph3", net)));
        net.getTransition("t31").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.4*P3", net)));
        net.getTransition("t32").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.3*P3", net)));
        net.getTransition("t33").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.2*P3", net)));
        net.getTransition("t34").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.1*P3", net)));
        net.getTransition("t4").addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("4*Ph4", net)));

        //
            /*
            for(int i = 1; i<= numPh; i++){
                net.getTransition("t"+i).addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from(i+"*Ph"+i, net)));
                for(int j = 1; j<=numPlaces; ) {
                    if(i == 1)
                        net.getTransition("t" + i + j).addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from(fireRates[i - 1] + "*P"+i, net)));
                    if(i ==2)
                        net.getTransition("t" + i + j).addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from(fireRates[i - 1] + "*P"+i, net)));
                }
            }
          */
        return net;
    }

    public PetriNet setSteadyStateTransition(PetriNet net, Map<RewardRate, BigDecimal> steadyState, int numPh, Marking marking) {
        Place InitialPlace = net.addPlace("InitialPlace");
        for (int i = 1; i <= numPh; i++) {
            marking.setTokens(net.getPlace("Ph" + i), 0);
        }
        marking.setTokens(net.getPlace("Pool"), 0);
        marking.setTokens(net.getPlace("InitialPlace"), poolSize);
        for (RewardRate reward : steadyState.keySet()) {
            System.out.printf(reward.toString() + "\n");
        }
        for (RewardRate rewards : steadyState.keySet()) {
            net.addTransition(rewards.toString());
            net.addPrecondition(net.getPlace("InitialPlace"), net.addTransition(rewards.toString()));
            net.addPostcondition(net.getTransition(rewards.toString()), net.getPlace(rewards.toString()));
            BigDecimal pool = new BigDecimal(poolSize);
            net.getTransition(rewards.toString()).addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal("1"),
                    MarkingExpr.from("1*"+steadyState.get(rewards), net)));
        }
        return net;
    }

}

        /*
        for(int i = 1; i<= numPh; i++){
            net.addTransition("sph" + i);
            net.addPrecondition(net.getPlace("InitialPlace"), net.getTransition("sph"+i));
            net.addPostcondition(net.getTransition("sph"+i), net.getPlace("Ph"+i));
        }
        net.addTransition("sp");
        net.addPrecondition(net.getPlace("InitialPlace"), net.getTransition("sp"));
        net.addPostcondition(net.getTransition("sp"), net.getPlace("Pool"));


        //net.addTransition("InitialPlace");
        net.getTransition("sph1").addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal("1"), MarkingExpr.from("1*" , net)));
    }
}
*/