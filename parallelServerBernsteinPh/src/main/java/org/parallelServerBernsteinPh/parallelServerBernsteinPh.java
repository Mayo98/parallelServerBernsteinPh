package org.parallelServerBernsteinPh;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.oristool.models.gspn.GSPNSteadyState;
import org.oristool.models.gspn.GSPNTransient;
import org.oristool.models.stpn.*;
import org.oristool.models.stpn.steady.RegSteadyState;
import org.oristool.models.stpn.trans.RegTransient;
import org.oristool.models.stpn.trees.DeterministicEnablingState;
import org.oristool.models.stpn.trees.StochasticTransitionFeature;
import org.oristool.petrinet.Marking;
import org.oristool.petrinet.PetriNet;
import org.oristool.petrinet.Place;
import org.oristool.petrinet.Transition;
import org.oristool.util.Pair;

public class parallelServerBernsteinPh {
    public static PetriNet build(Marking marking, int numPlaces, int numPh, int[] arrivalRates, int poolSize) {
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
    public static String getPerformabilityReward() {
        return "If(Pool==6,1,0);If(Pool==7,1,0)";
    }
    public static PetriNet build2(Marking marking) {
        PetriNet net = new PetriNet();
        int[] arrivalRate = {2,1,3};
        int poolSize = 8;
        //Generating Nodes
        Place A1 = net.addPlace("A1");
        Place A2 = net.addPlace("A2");
        Place A3 = net.addPlace("A3");
        Place P1 = net.addPlace("P1");
        Place P2 = net.addPlace("P2");
        Place P3 = net.addPlace("P3");
        Place Ph1 = net.addPlace("Ph1");
        Place Ph2 = net.addPlace("Ph2");
        Place Ph3 = net.addPlace("Ph3");
        Place Ph4 = net.addPlace("Ph4");
        Place Pool = net.addPlace("Pool");
        Transition t1 = net.addTransition("t1");
        Transition t11 = net.addTransition("t11");
        Transition t12 = net.addTransition("t12");
        Transition t13 = net.addTransition("t13");
        Transition t14 = net.addTransition("t14");
        Transition t2 = net.addTransition("t2");
        Transition t21 = net.addTransition("t21");
        Transition t22 = net.addTransition("t22");
        Transition t23 = net.addTransition("t23");
        Transition t24 = net.addTransition("t24");
        Transition t3 = net.addTransition("t3");
        Transition t31 = net.addTransition("t31");
        Transition t32 = net.addTransition("t32");
        Transition t33 = net.addTransition("t33");
        Transition t34 = net.addTransition("t34");
        Transition t4 = net.addTransition("t4");

        //Generating Connectors
        net.addPostcondition(t31, A3);
        net.addPrecondition(A1, t12);
        net.addPostcondition(t2, Ph3);
        net.addPrecondition(A3, t33);
        net.addPostcondition(t3, Ph4);
        net.addPrecondition(Pool, t11);
        net.addPrecondition(Ph4, t4);
        net.addPostcondition(t31, Ph1);
        net.addPostcondition(t21, A2);
        net.addPostcondition(t14, Ph4);
        net.addPostcondition(t23, Ph3);
        net.addPostcondition(t11, Ph1);
        net.addPostcondition(t32, A3);
        net.addPostcondition(t22, Ph2);
        net.addPostcondition(t4, Pool);
        net.addPrecondition(Pool, t33);
        net.addPostcondition(t12, A1);
        net.addPrecondition(Pool, t31);
        net.addPrecondition(Pool, t22);
        net.addPrecondition(A1, t14);
        net.addPostcondition(t14, A1);
        net.addPostcondition(t24, A2);
        net.addPrecondition(A2, t24);
        net.addPrecondition(A1, t11);
        net.addPostcondition(t11, A1);
        net.addPostcondition(t12, Ph2);
        net.addPrecondition(Ph2, t2);
        net.addPostcondition(t21, Ph1);
        net.addPrecondition(A1, t13);
        net.addPrecondition(Pool, t24);
        net.addPrecondition(Pool, t13);
        net.addPrecondition(A2, t22);
        net.addPrecondition(Ph1, t1);
        net.addPostcondition(t34, A3);
        net.addPrecondition(A3, t34);
        net.addPostcondition(t34, Ph4);
        net.addPrecondition(Pool, t32);
        net.addPrecondition(Pool, t34);
        net.addPostcondition(t1, Ph2);
        net.addPrecondition(A2, t21);
        net.addPostcondition(t24, Ph4);
        net.addPrecondition(A2, t23);
        net.addPostcondition(t22, A2);
        net.addPostcondition(t32, Ph2);
        net.addPostcondition(t13, A1);
        net.addPrecondition(Pool, t21);
        net.addPostcondition(t13, Ph3);
        net.addPostcondition(t33, A3);
        net.addPrecondition(Pool, t14);
        net.addPostcondition(t23, A2);
        net.addPostcondition(t33, Ph3);
        net.addPrecondition(Pool, t12);
        net.addPrecondition(A3, t31);
        net.addPrecondition(Pool, t23);
        net.addPrecondition(A3, t32);
        net.addPrecondition(Ph3, t3);


        //Generating Properties
        marking.setTokens(A1, 1);
        marking.setTokens(A2, 1);
        marking.setTokens(A3, 1);
        marking.setTokens(P1, arrivalRate[0]);
        marking.setTokens(P2, arrivalRate[1]);
        marking.setTokens(P3, arrivalRate[2]);
        marking.setTokens(Ph1, 0);
        marking.setTokens(Ph2, 0);
        marking.setTokens(Ph3, 0);
        marking.setTokens(Ph4, 0);
        marking.setTokens(Pool, poolSize);
        t1.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("1*Ph1", net)));
        t11.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.25*P1", net)));
        t12.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.25*P1", net)));
        t13.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.25*P1", net)));
        t14.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.25*P1", net)));
        t2.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("2*Ph2", net)));
        t21.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.1*P2", net)));
        t22.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.2*P2", net)));
        t23.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.3*P2", net)));
        t24.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.4*P2", net)));
        t3.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("3*Ph3", net)));
        t31.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.4*P3", net)));
        t32.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.3*P3", net)));
        t33.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.2*P3", net)));
        t34.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("0.1*P3", net)));
        t4.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1"), MarkingExpr.from("4*Ph4", net)));
   return net;
    }

    //funzione per cambiare marcatura su Pool
    public static void changePoolsize(PetriNet net, Marking marking, int poolSize) {
        Place pool = net.getPlace("Pool");
        marking.setTokens(pool, poolSize);
    }
    //funzione per cambiare marcatura su Pn
    public static void changeArrivals(PetriNet net, Marking marking, int place, int value) {
        Place ok = net.getPlace("P" + place);
        marking.setTokens(ok, value);
    }

    public static void addRow(String filePath, BigDecimal reliability){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(reliability.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        //PetriNet pn = new PetriNet();
        Marking marking = new Marking();
        int numPlaces = 3;
        int numPh = 4;
        int[] arrivalRates =  {2,1,3};
        int poolSize = 8;
        String filePath = System.getProperty("user.dir") + "/results.txt";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        PetriNet pn = build(marking, numPlaces, numPh, arrivalRates, poolSize);

        ///STEADY STATE
        Map<Marking, Double> dist = GSPNSteadyState.builder().build().compute(pn, marking);


        System.out.printf("%s", marking);
        float totP = 0;
        for (Marking m : dist.keySet()) {
            if (m.getTokens("Pool") == 2)
                totP += dist.get(m);
            System.out.printf("%f %s%n", dist.get(m), m);
        }


        RegSteadyState analysis = RegSteadyState.builder().build();

        //GSPNSteadyState analysis = GSPNSteadyState.builder().build();
        SteadyStateSolution<Marking> result;

        SteadyStateSolution<RewardRate> resultReward;

        String rewards = getPerformabilityReward();
        Map<RewardRate, BigDecimal> steadyState;

        int p = 7;
        for(int i = 0; i<= 3; i++) {
            //dist = GSPNSteadyState.builder().build().compute(pn, marking).keySet().stream().filter();
            result = analysis.compute(pn, marking);
            resultReward = SteadyStateSolution.computeRewards(result, rewards);
            steadyState = resultReward.getSteadyState();
            BigDecimal reliability = steadyState.entrySet().stream().filter(t -> t.getKey().toString().equals(rewards)).findFirst().get().getValue();
            addRow(filePath, reliability);
            System.out.printf("%d", reliability);
            changePoolsize(pn, marking, p);
            p--;
        }

        /* Da guardare
        RegTransient analysis = RegTransient.builder()
                .greedyPolicy(new BigDecimal("12"), new BigDecimal("0.005"))
                .timeStep(new BigDecimal("0.02")).build();

        TransientSolution<DeterministicEnablingState, Marking> solution =
                analysis.compute(pn, marking);

        // display transient probabilities
        new TransientSolutionViewer(solution);

*/

        //System.out.printf("%s", resultReward.toString());
        //System.out.printf("%n%.6f", resultReward.getSteadyState());

        /*dist = resultReward.getSteadyState();
        for(int t = 0; t < resultReward.getSteadyState().size(); t++){
            for(int i = 0; i<  resultReward.getSteadyState().size(); i++){
                System.out.printf("%.1f %.6f %n", resultReward.getSteadyState()[t][markingPos]);
        }*/

        //SteadyStateSolution.computeRewards()
/*
        //Riduzione PoolSize 8-->4
        System.out.printf("Pool = 2: %f", totP);


        marking.setTokens(pn.getPlace("Pool"), 4);
        dist = GSPNSteadyState.builder().build().compute(pn, marking);
        System.out.printf("%nnuova Marcatura Pool: 4 --> %s%n", marking);
        totP = 0;
        for (Marking m : dist.keySet()) {
            if (m.getTokens("Pool") == 2)
                totP += dist.get(m);
        }
        System.out.printf("Pool = 2: %f", totP);

        //Esempio di Picco tasso di arrivo
        // P1: 2->4
        marking.setTokens(pn.getPlace("P1"), 4);
        //P2: 1 ->3
        marking.setTokens(pn.getPlace("P2"), 3);
        dist = GSPNSteadyState.builder().build().compute(pn, marking);
        System.out.printf("%nNuova Marcatura Pool: 4 --> %s%n", marking);
        totP = 0;
        for (Marking m : dist.keySet()) {
            if (m.getTokens("Pool") == 2)
                totP += dist.get(m);
        }
        System.out.printf("Pool = 2: %f", totP);

*/
        //GSPN Transient Analysis

/*
        double step = 0.1;
        marking.setTokens(pn.getPlace("Pool"), 2);
        Pair<Map<Marking, Integer>, double[][]> result = GSPNTransient.builder()
                .timePoints(0.0, 10.0, step)
                .build().compute(pn, marking);

        System.out.printf("%n%s", marking);
        Map<Marking, Integer> statePos = result.first();
        double[][] probs = result.second();
        int markingPos = statePos.get(marking);

        for (int t = 0; t < probs.length; t++) {
            double time = t * step;
            for (int i = 0; i < probs.length; i++) {
                System.out.printf("%.1f %.6f %n", time, probs[t][markingPos]);
                //System.out.println();
                //}


            }
        }*/
    }
    }


/*
String unavailabilityReward = builder.getUnavailabilityReward();
String unreliabilityReward = builder.getUnreliabilityReward();
String performabilityReward = builder.getPerformabilityReward(performabilityBoundMap.get(poolSize));
String rewards = unavailabilityReward + ";" + unreliabilityReward + ";" + performabilityReward;

RegSteadyState analysis = RegSteadyState.builder().build();

SteadyStateSolution<Marking> result;
SteadyStateSolution<RewardRate> resultReward;

Map<RewardRate, BigDecimal> steadyState;

for(int waitTrigger = leftBound; waitTrigger<= rightBound; waitTrigger+=granularity){

            builder.changePoolsize(net,marking, poolSize);
            builder.changeTrigger(net,Integer.toString(waitTrigger));

          result = analysis.compute(net, marking);
            resultReward = SteadyStateSolution\.computeRewards(result, rewards);



BigDecimal reliability = steadyState.entrySet().stream().filter(t -> t.getKey().toString().equals(unreliabilityReward)).findFirst().get().getValue();
//BigDecimal availability = steadyState.entrySet().stream().filter(t -> t.getKey().toString().equals("Ko+Rej")).findFirst().get().getValue();
BigDecimal availability = steadyState.entrySet().stream().filter(t -> t.getKey().toString
addRow(filePath, waitTrigger, reliability, availability, performability);
@Override
    public String getPerformabilityReward() {
        return "Ko+Rej";
    }

    @Override
    public String getNickName() {
        return "coordinated-sequential";
    }



//modifica transizione
@Override
    public void changeTrigger(PetriNet net, String triggerValue) {
        Transition trigger = net.getTransition("trigger");
        trigger.removeFeature(StochasticTransitionFeature.class);
        trigger.addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal(triggerValue), MarkingExpr.from("1", net)));
    }

    @Override
    public void changePoolsize(PetriNet net, Marking marking, int poolSize) {
        Place ok = net.getPlace("Ok");
        marking.setTokern



 */


