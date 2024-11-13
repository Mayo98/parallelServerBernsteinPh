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


public class parallelServerBernsteinPh {



    public static void setSteadyStateTransition(PetriNet net, Map<RewardRate, BigDecimal> steadyState, int numPlaces, int numPh){
        Place InitialPlace = net.addPlace("InitialPlace");

        for(RewardRate reward : steadyState.keySet()){

        }
        for(int i = 0; i< numPh; i++){
            net.addTransition("sph" + i);
            net.addPrecondition(net.getPlace("InitialPlace"), net.getTransition("sph"+i));
            net.addPostcondition(net.getTransition("sph"+i), net.getPlace("Ph"+i));
        }
        net.addTransition("sp");
        net.addPrecondition(net.getPlace("InitialPlace"), net.getTransition("sp"));
        net.addPostcondition(net.getTransition("sp"), net.getPlace("Pool"));


        //net.addTransition("InitialPlace");
        net.getTransition("Prova").addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal("1"), MarkingExpr.from("1*InitialPlace", net)));
    }



    public static String getPerformabilityRewards() {
        return "If(Pool==6,1,0);If(Pool==7,1,0)";
    }

    public static String getUnavailabilityRewards(){
        return "Pool;Ph1;Ph2;Ph3;Ph4";
    }

    public static List<Builder> getPerformabilityRewardMap() {
        String reward = getPerformabilityRewards();
        List<Builder> performabilityRewardMap = new ArrayList<>();

        String[] conditions = reward.split(";");

        // Elaborare ogni condizione
        for (String condition : conditions) {
            // Rimuovere "If(" e ");"
            condition = condition.replace("If(", "").replace(");", "");

            // Dividere la condizione dalla virgola
            String[] parts = condition.split(",");

            // Estrarre la condizione
            String[] conditionParts = parts[0].split("==");
            String varName = conditionParts[0].trim(); // Nome della variabile
            int varValue = Integer.parseInt(conditionParts[1].trim()); // Valore della variabile

            // Aggiungere alla mappa
            //performabilityRewardMap.add(new Builder(varName, varValue));
        }

        System.out.println("Mappa delle variabili: " + performabilityRewardMap);

        return performabilityRewardMap;
    }

    public static List<String> getAvailabilityRewardMap() {
        String reward = getUnavailabilityRewards();
        List<String> availabilityRewardMap = new ArrayList<>();

        String[] conditions = reward.split(";");

        // Elaborare ogni condizione
        for (String condition : conditions) {

            availabilityRewardMap.add(condition);
        }
        System.out.println("Mappa delle variabili: " + availabilityRewardMap);

        return availabilityRewardMap;
    }
   /* public static PetriNet build2(Marking marking) {
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
    }*/

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

    public static void saveResults(String filePath, Map<RewardRate, BigDecimal> steadyState, int value){
        //TODO: Sistema con nuovo metodo.

        String pRewards = getPerformabilityRewards();
        BigDecimal b = new BigDecimal(0);
        String[] conditions = pRewards.split(";");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
           for(RewardRate reward : steadyState.keySet()){
               writer.write(reward.toString() + " : " + steadyState.get(reward) + " " +value);
               writer.newLine();
               BigDecimal bigDecimal = steadyState.get(reward);
               b = b.add(bigDecimal);
           }
            writer.newLine();
            writer.write(b.toString());
           writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void saveTransientResults(String filePath, TransientSolution<Marking, RewardRate> rewards, int value) {
        List<RewardRate> rewardsList = rewards.getColumnStates();
        double step = 0.1;
        double [][][] solutions = rewards.getSolution();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.write("Time ");
            for(RewardRate reward : rewardsList){
                writer.write(reward.toString() + " ");
            }
            writer.newLine();
            for(int i= 0; i < solutions.length; i++){
                writer.write(step+ " ");
                step+=step;
                for(int j = 0; j < solutions[i][0].length; j++){
                    writer.write(solutions[i][0][j] + " ");

                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static List<RewardRate>getUnavailabilityRewardsList(){
        List<RewardRate> unavailabilityRewards = new ArrayList<>();
        String rewards = getUnavailabilityRewards();
        String[] conditions = rewards.split(";");
        for(int i = 0; i < conditions.length; i++){
            unavailabilityRewards.add(RewardRate.fromString(conditions[i]));
        }
        return unavailabilityRewards;
    }


    public static SteadyStateSolution<RewardRate> calculateGSPNSteadyState(PetriNet pn, Marking marking, int poolSize, String filePath){
        Map<Marking, Double> steadyStateMap = GSPNSteadyState.builder().build().compute(pn, marking);
        Map<Marking, BigDecimal> convertedSteadyStateMap = new HashMap<>();

        //lista per unavailability rewards
        List<RewardRate>unavailability = getUnavailabilityRewardsList();


        for (Map.Entry<Marking, Double> entry : steadyStateMap.entrySet()) {
            convertedSteadyStateMap.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
        }
        SteadyStateSolution<Marking> solution = new SteadyStateSolution<>(convertedSteadyStateMap);

        SteadyStateSolution<RewardRate> rewards = SteadyStateSolution.computeRewards(solution, unavailability.toArray(new RewardRate[0]));
        Map<RewardRate, BigDecimal> steadyState = rewards.getSteadyState();
        saveResults(filePath, steadyState, poolSize);
        return rewards;
    }


    public static void calculateGSPNTransientAnalysis(PetriNet pn, Marking marking, int poolSize, String filePath){//, SteadyStateSolution<Marking> solution) {
        double step = 0.1;
        List<RewardRate>unavailability = getUnavailabilityRewardsList();
        Pair<Map<Marking, Integer>, double[][]> result = GSPNTransient.builder()
                .timePoints(0.0, 10.0, step)
                .build().compute(pn, marking);
        Map<Marking, Integer> statePos = result.first();
        double[][] probs = result.second();
        //changeArrivals(pn, marking,1, 6);
        //changeArrivals(pn, marking,2, 7);
        TransientSolution<Marking, Marking> transientSolution = TransientSolution.fromArray(probs,step,statePos,marking);
        TransientSolution<Marking, RewardRate> rewards = TransientSolution.computeRewards(true, transientSolution, unavailability.toArray(new RewardRate[0]));
        rewards.getSolution();
        int value = 1;
        saveTransientResults( filePath, rewards, value);
    }
    public static void main(String[] args) {

        //PetriNet pn = new PetriNet();
        Marking marking = new Marking();
        int numPlaces = 3;
        int numPh = 4;
        int[] arrivalRates =  {2,1,3};
        int poolSize = 8;
        int numTest =  1;
        String filePathA = System.getProperty("user.dir") + "/SteadyStateResults.txt";
        String filePathP = System.getProperty("user.dir") + "/TransientResults2.txt";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Builder builder = new Builder(marking, numPlaces, numPh, arrivalRates, poolSize);
        PetriNet pn = builder.build();
        //PetriNet pn = build(marking, numPlaces, numPh, arrivalRates, poolSize);

        ///STEADY STATE
        SteadyStateSolution<RewardRate> rewards = new SteadyStateSolution<>();
        for(int i = 0; i < numTest; i++){
            changePoolsize(pn, marking, 8);
            rewards = calculateGSPNSteadyState(pn, marking, i, filePathA);
        }
        //Map<Marking, Double> steadyStateMap = GSPNSteadyState.builder().build().compute(pn, marking);
        //Map<Marking, BigDecimal> convertedSteadyStateMap = new HashMap<>();

        //lista per unavailability rewards
        List<RewardRate>unavailability = getUnavailabilityRewardsList();

        /*
        for (Map.Entry<Marking, Double> entry : steadyStateMap.entrySet()) {
            convertedSteadyStateMap.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
        }
        SteadyStateSolution<Marking> solution = new SteadyStateSolution<>(convertedSteadyStateMap);
        Map<Marking, BigDecimal> sol = solution.getSteadyState();
        BigDecimal max = new BigDecimal(0);
        Marking sMarking = new Marking();
        for(Marking entry : sol.keySet()){
            if(sol.get(entry).compareTo(max) == 1)
            {
                max = sol.get(entry);
                sMarking = entry;
            }
        }
        System.out.printf("max m: " + sMarking + " val: %n"+ max);
        double step = 0.1;
        changePoolsize(pn,marking,6);

         */



        //Calcolato Steady State re-buildo la net a steady state
        pn = builder.setSteadyStateTransition(pn, rewards.getSteadyState(), numPh, marking);


        System.out.printf(marking.toString() + "\n");

        //rewards = calculateGSPNSteadyState(pn, marking, 8, filePathA);
        calculateGSPNTransientAnalysis(pn,marking, poolSize,filePathP);
        /*Pair<Map<Marking, Integer>, double[][]> result = GSPNTransient.builder()
                .timePoints(0.0, 10.0, step)
                .build().compute(pn, sMarking);
        Map<Marking, Integer> statePos = result.first();
        double[][] probs = result.second();
        TransientSolution<Marking, Double> transientResults = new TransientSolution<>(10,step, probs, statePos, probs);


        int markingPos = statePos.get(marking);
        for (int t = 0; t < probs.length; t++) {
            double time = t * step;
            System.out.printf("%.1f %.6f %n", time, probs[t][markingPos]);
        }
        */

    }
}

        /*


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


        //SteadyStateSolution.computeRewards()
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


 */


