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


    public static void setSteadyStateTransition(PetriNet net, Map<RewardRate, BigDecimal> steadyState, int numPlaces, int numPh) {
        Place InitialPlace = net.addPlace("InitialPlace");

        for (RewardRate reward : steadyState.keySet()) {

        }
        for (int i = 0; i < numPh; i++) {
            net.addTransition("sph" + i);
            net.addPrecondition(net.getPlace("InitialPlace"), net.getTransition("sph" + i));
            net.addPostcondition(net.getTransition("sph" + i), net.getPlace("Ph" + i));
        }
        net.addTransition("sp");
        net.addPrecondition(net.getPlace("InitialPlace"), net.getTransition("sp"));
        net.addPostcondition(net.getTransition("sp"), net.getPlace("Pool"));


        //net.addTransition("InitialPlace");
        net.getTransition("Prova").addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal("1"), MarkingExpr.from("1*InitialPlace", net)));
    }


    public static String getPerformabilityRewards() {
        return "If(Pool==0,1,0);If(Pool==1,1,0);If(Pool==2,1,0);If(Pool==3,1,0);If(Pool==4,1,0);If(Pool==5,1,0);If(Pool==6,1,0);If(Pool==7,1,0);If(Pool==8,1,0)";
    }

    public static String getAvailabilityRewards() {
        return "Pool;Ph1;Ph2;Ph3;Ph4";
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
    public static void changeInitialPlace(PetriNet net, Marking marking, int poolSize) {
        Place pool = net.getPlace("InitialPlace");
        marking.setTokens(pool, poolSize);
    }


    public static void saveResults(String filePath, Map<RewardRate, BigDecimal> steadyState, int value, PetriNet pn, Marking marking, int mode) {
        if (mode == 1) {
            String fileNames = filePath + "SteadyStateFileNames.txt";
            BigDecimal b = new BigDecimal(0);
            String result = filePath + marking.getTokens("P1") + "P1 " +
                    marking.getTokens("P2") + "P2 " +
                    marking.getTokens("P3") + "P3 " +
                    marking.getTokens("Pool") + "Pool.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNames, true))) {
                writer.write(result);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(result, false))) {

                for (RewardRate reward : steadyState.keySet()) {
                    writer.write(reward.toString() + " : " + steadyState.get(reward) + " " + value);
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
        } else {
            String result = filePath + "SteadyStateTests.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(result, true))) {

                for (RewardRate reward : steadyState.keySet()) {
                    writer.write(reward.toString() + " : " + steadyState.get(reward) + " " + value);
                    writer.newLine();
                }
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveTransientResults(String filePath, TransientSolution<Marking, RewardRate> rewards, Marking marking, int val, String markingChanged) {
        String fileNames = filePath + "TransientFileNames.txt";
        String result = filePath + marking.getTokens("P1") + "P1 " +
                marking.getTokens("P2") + "P2 " +
                marking.getTokens("P3") + "P3 " +
                marking.getTokens("InitialPlace") + "Pool" + markingChanged + ".txt";
        List<RewardRate> rewardsList = rewards.getColumnStates();
        double step = 0.1;
        double time = step;
        double[][][] solutions = rewards.getSolution();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNames, true))) {
            writer.write(result);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(result, false))) {
            writer.write("Time ");
            for (RewardRate reward : rewardsList) {
                writer.write(reward.toString() + " ");
            }
            writer.newLine();
            for (int i = 0; i < solutions.length; i++) {
                writer.write(time + " ");
                time += step;
                for (int j = 0; j < solutions[i][0].length; j++) {
                    writer.write(solutions[i][0][j] + " ");

                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static List<RewardRate> getAvailabilityRewardsList() {
        List<RewardRate> availabilityRewards = new ArrayList<>();
        String rewards = getAvailabilityRewards();
        String[] conditions = rewards.split(";");
        for (int i = 0; i < conditions.length; i++) {
            availabilityRewards.add(RewardRate.fromString(conditions[i]));
        }
        return availabilityRewards;
    }

    public static List<RewardRate> getPerformabilityRewardsList() {
        List<RewardRate> performabilityRewards = new ArrayList<>();
        String rewards = getPerformabilityRewards();//getUnavailabilityRewards();
        String[] conditions = rewards.split(";");
        for (int i = 0; i < conditions.length; i++) {
            performabilityRewards.add(RewardRate.fromString(conditions[i]));
        }
        return performabilityRewards;
    }


    public static SteadyStateSolution<RewardRate> calculateGSPNSteadyState(PetriNet pn, Marking marking, int poolSize, String filePath, List<RewardRate> rewardList, int mode) {
        Map<Marking, Double> steadyStateMap = GSPNSteadyState.builder().build().compute(pn, marking);
        Map<Marking, BigDecimal> convertedSteadyStateMap = new HashMap<>();

        //lista per unavailability rewards

        for (Map.Entry<Marking, Double> entry : steadyStateMap.entrySet()) {
            convertedSteadyStateMap.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
        }
        SteadyStateSolution<Marking> solution = new SteadyStateSolution<>(convertedSteadyStateMap);

        SteadyStateSolution<RewardRate> rewards = SteadyStateSolution.computeRewards(solution, rewardList.toArray(new RewardRate[0]));
        Map<RewardRate, BigDecimal> steadyState = rewards.getSteadyState();
        saveResults(filePath, steadyState, poolSize, pn, marking, mode);
        return rewards;
    }


    public static void calculateGSPNTransientAnalysis(PetriNet pn, Marking marking, int poolSize, String filePath, List<RewardRate> rewardList, String markingChanged) {//, SteadyStateSolution<Marking> solution) {
        double step = 0.1;
        Pair<Map<Marking, Integer>, double[][]> result = GSPNTransient.builder()
                .timePoints(0.0, 10.0, step)
                .build().compute(pn, marking);
        Map<Marking, Integer> statePos = result.first();
        double[][] probs = result.second();
        //changeArrivals(pn, marking,1, 6);
        //changeArrivals(pn, marking,2, 7);

        TransientSolution<Marking, Marking> transientSolution = TransientSolution.fromArray(probs, step, statePos, marking);
        TransientSolution<Marking, RewardRate> rewards = TransientSolution.computeRewards(false, transientSolution, rewardList.toArray(new RewardRate[0]));
        //TransientSolution<RewardRate, BigDecimal> rewardsA = new TransientSolution<>(new BigDecimal(10), new BigDecimal(step),)       TransientSolution.computeRewards(true,)
        //rewards.getSolution();
        int value = 1;
        saveTransientResults(filePath, rewards, marking, value, markingChanged);
    }

    // ## TEST TYPE: INCREASING A SINGOL MARKING ON A SPECIFIC PLACE Pn
    public static void runSteadyStatesTests(PetriNet pn, Marking marking, String filePathP_ss, String filePathA_ss, int numPlaces) {
        List<RewardRate> availabilityRewardsList = getAvailabilityRewardsList();
        List<RewardRate> performabilityRewardsList = getPerformabilityRewardsList();
        SteadyStateSolution<RewardRate> performabilityRewards = new SteadyStateSolution<>();
        SteadyStateSolution<RewardRate> availabilityRewards = new SteadyStateSolution<>();
        for (int n = 1; n <= numPlaces; n++) {
            String filePathP_ss_arr = filePathP_ss + "/P" + n + " Tests/";
            String filePathA_ss_arr = filePathA_ss + "/P" + n + " Tests/";


            int tmpVal = marking.getTokens("P" + n);
            for (int i = 1; i < 15; i++) {
                changeArrivals(pn, marking, n, i);
                performabilityRewards = calculateGSPNSteadyState(pn, marking, i, filePathP_ss_arr, performabilityRewardsList, 0);
                availabilityRewards = calculateGSPNSteadyState(pn, marking, i, filePathA_ss_arr, availabilityRewardsList, 0);
            }
            changeArrivals(pn, marking, n, tmpVal);
        }
        String filePathP_ss_pool = filePathP_ss + "/Pool Tests/";
        String filePathA_ss_pool = filePathA_ss + "/Pool Tests/";
        int tmpPool = marking.getTokens("Pool");
        for (int i = 1; i < 15; i++) {
            changePoolsize(pn, marking, i);
            performabilityRewards = calculateGSPNSteadyState(pn, marking, i, filePathP_ss_pool, performabilityRewardsList, 0);
            availabilityRewards = calculateGSPNSteadyState(pn, marking, i, filePathA_ss_pool, availabilityRewardsList, 0);
        }
        changePoolsize(pn, marking, tmpPool);
    }


    public static void runAllTests(PetriNet pn, Marking marking, int numPlaces, int numPh, int[] arrivalValues, int poolSize, int[] poolSizeValues, int[] arrivalRates, int[] increaseRates) {
        int numTest = poolSizeValues.length;
        String filePathP_ss = System.getProperty("user.dir") + "/SteadyStateResults/Performability/";
        String filePathA_ss = System.getProperty("user.dir") + "/SteadyStateResults/Availability/";
        String filePathP_ta = System.getProperty("user.dir") + "/TransientResults/Performability/";
        String filePathA_ta = System.getProperty("user.dir") + "/TransientResults/Availability/";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Builder builder = new Builder(marking, numPlaces, numPh, arrivalRates, poolSize);

        SteadyStateSolution<RewardRate> performabilityRewards = new SteadyStateSolution<>();
        SteadyStateSolution<RewardRate> availabilityRewards = new SteadyStateSolution<>();
        List<RewardRate> availabilityRewardsList = getAvailabilityRewardsList();
        List<RewardRate> performabilityRewardsList = getPerformabilityRewardsList();
        for (int i = 0; i < numTest; i++) {
            int P1 = arrivalValues[i];
            changeArrivals(pn, marking, 1, P1);
            poolSize = poolSizeValues[i];
            changePoolsize(pn, marking, poolSize);
            for (int j = 0; j < arrivalValues.length; j++) {
                int P2 = arrivalValues[j];
                changeArrivals(pn, marking, 2, P2);
                for (int k = 0; k < arrivalValues.length; k++) {
                    int P3 = arrivalValues[k];

                    changeArrivals(pn, marking, 3, P3);
                    System.out.println("Marcatura attuale: " + marking);
                    //CALCOLO STEADY STATE
                    performabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathP_ss, performabilityRewardsList, 1);
                    availabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathA_ss, availabilityRewardsList, 1);

                    //Calcolato Steady State re-buildo la net a steady state
                    PetriNet steadyNet = builder.buildSteadyStateNet(availabilityRewards.getSteadyState(), numPh, marking);
                    //base Transient
                    calculateGSPNTransientAnalysis(pn, marking, poolSize, filePathA_ta, availabilityRewardsList, "");
                    calculateGSPNTransientAnalysis(pn, marking, poolSize, filePathP_ta, performabilityRewardsList, "");
                    for (int pt = 1; pt <= numPlaces; pt++) {

                        int tmp = marking.getTokens("P" + pt);
                        for (int h = 0; h < increaseRates.length; h++) {
                            //increasing arrivals for Pn
                            String markingChanged = " -- " + tmp + "P" + pt;
                            int increasedValue = tmp + increaseRates[h];
                            System.out.println("Calcolo transiente per : " + marking + " incrementando: " + tmp + "P" + pt + " portandolo a: " + increasedValue);
                            changeArrivals(steadyNet, marking, pt, increasedValue);
                            String tmpfilePathA_ta = filePathA_ta.concat("IncreasedRate/");
                            String tmpfilePathP_ta = filePathP_ta.concat("IncreasedRate/");
                            calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, tmpfilePathA_ta, availabilityRewardsList, markingChanged);
                            calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, tmpfilePathP_ta, performabilityRewardsList, markingChanged);
                        }
                        //Ri sistemo la marcatura incrementata
                        changeArrivals(steadyNet, marking, pt, tmp);
                    }
                    //pn = builder.setSteadyStateTransition(pn, performabilityRewards.getSteadyState(), numPh, marking);

                    //rewards = calculateGSPNSteadyState(pn, marking, 8, filePathA);
                }
            }
        }
    }


    public static void main(String[] args) {

        //PetriNet pn = new PetriNet();
        Marking marking = new Marking();
        int numPlaces = 3;
        int numPh = 4;
        int[] arrivalRates = {1, 2, 5};//{2, 1, 3};
        int poolSize = 10;


        int[] poolSizeValues = {3, 8, 10, 12};
        int[] arrivalValues = {1, 2, 3, 5};
        int[] increaseRates = {1, 2, 3};
        //int numTest = poolSizeValues.length;
        String filePathP_ss = System.getProperty("user.dir") + "/SteadyStateResults/Performability/";
        String filePathA_ss = System.getProperty("user.dir") + "/SteadyStateResults/Availability/";
        String filePathP_ta = System.getProperty("user.dir") + "/TransientResults/Performability/";
        String filePathA_ta = System.getProperty("user.dir") + "/TransientResults/Availability/";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Builder builder = new Builder(marking, numPlaces, numPh, arrivalRates, poolSize);
        PetriNet pn = builder.build();

        //TUTTI I TEST
        //runAllTests(pn, marking, numPlaces, numPh, arrivalValues, poolSize, poolSizeValues, arrivalRates, increaseRates);

        ///STEADY STATE
        SteadyStateSolution<RewardRate> performabilityRewards = new SteadyStateSolution<>();
        SteadyStateSolution<RewardRate> availabilityRewards = new SteadyStateSolution<>();
        List<RewardRate> availabilityRewardsList = getAvailabilityRewardsList();
        List<RewardRate> performabilityRewardsList = getPerformabilityRewardsList();
        performabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathP_ss, performabilityRewardsList, 1);
        availabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathA_ss, availabilityRewardsList, 1);
        //Calcolato Steady State re-buildo la net a steady state
        PetriNet steadyNet = builder.buildSteadyStateNet(availabilityRewards.getSteadyState(), numPh, marking);
        //base Transient
        //base Transient
        calculateGSPNTransientAnalysis(pn, marking, poolSize, filePathA_ta, availabilityRewardsList, "");
        calculateGSPNTransientAnalysis(pn, marking, poolSize, filePathP_ta, performabilityRewardsList, "");
        int pt = 3;
        //int tmp = marking.getTokens("P" + pt);
        int tmp = marking.getTokens("InitialPlace");
        String markingChanged = " -- " + tmp + "Init";
        int increasedValue = tmp + increaseRates[1];
        System.out.println("Calcolo transiente per : " + marking + " incrementando: " + tmp + "P" + pt + " portandolo a: " + increasedValue);
        //changeArrivals(steadyNet, marking, pt, increasedValue);
        changeInitialPlace(steadyNet, marking, 6);
        String tmpfilePathA_ta = filePathA_ta.concat("IncreasedRate/");
        String tmpfilePathP_ta = filePathP_ta.concat("IncreasedRate/");
        calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, tmpfilePathA_ta, availabilityRewardsList, markingChanged);
        calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, tmpfilePathP_ta, performabilityRewardsList, markingChanged);
        /*
        for (int pt = 1; pt <= numPlaces; pt++) {

            int tmp = marking.getTokens("P" + pt);
            for (int h = 0; h < increaseRates.length; h++) {
                //increasing arrivals for Pn
                String markingChanged = " -- " + tmp + "P" + pt;
                int increasedValue = tmp + increaseRates[h];
                System.out.println("Calcolo transiente per : " + marking + " incrementando: " + tmp + "P" + pt + " portandolo a: " + increasedValue);
                changeArrivals(steadyNet, marking, pt, increasedValue);
                String tmpfilePathA_ta = filePathA_ta.concat("IncreasedRate/");
                String tmpfilePathP_ta = filePathP_ta.concat("IncreasedRate/");
                calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, tmpfilePathA_ta, availabilityRewardsList, markingChanged);
                calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, tmpfilePathP_ta, performabilityRewardsList, markingChanged);
            }
            */
/*
            //runSteadyStatesTests(pn, marking, filePathP_ss, filePathA_ss, numPlaces);
            //runAllTests(pn, marking, numPlaces, numPh, arrivalValues, poolSize, poolSizeValues, arrivalRates, increaseRates);

            ///
            //TODO: CODICE TEST CORRETTO
/*
        for (int i = 0; i < numTest; i++) {
            int P1 = arrivalValues[i];
            changeArrivals(pn, marking, 1, P1);
            for (int j = 0; j < arrivalValues.length; j++) {
                int P2 = arrivalValues[j];
                changeArrivals(pn, marking, 2, P2);
                for (int k = 0; k < arrivalValues.length; k++) {
                    int P3 = arrivalValues[k];
                    poolSize = poolSizeValues[i];
                    changePoolsize(pn, marking, poolSize);
                    changeArrivals(pn, marking, 3, P3);
                    System.out.println("Marcatura attuale: "+ marking);
                    performabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathP_ss, performabilityRewardsList, 1 );
                    availabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathA_ss, availabilityRewardsList, 1);
                    //Calcolato Steady State re-buildo la net a steady state
                    PetriNet steadyNet = builder.buildSteadyStateNet(availabilityRewards.getSteadyState(), numPh, marking);
                    //base Transient
                    calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, filePathA_ta, availabilityRewardsList, "");
                    calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, filePathP_ta, performabilityRewardsList, "");
                    for(int pt = 1; pt <= numPlaces; pt++) {

                        int tmp = marking.getTokens("P" + pt);
                        for (int h = 0; h < increaseRates.length; h++) {
                            //increasing arrivals for Pn
                            String markingChanged = " -- " + tmp +"P" + pt;
                            int increasedValue = tmp + increaseRates[h];
                            System.out.println("Calcolo transiente per : "+ marking + " incrementando: " + tmp + "P" +pt +" portandolo a: "+ increasedValue);
                            changeArrivals(steadyNet, marking, pt, increasedValue);
                            String tmpfilePathA_ta = filePathA_ta.concat("IncreasedRate/");
                            String tmpfilePathP_ta = filePathP_ta.concat("IncreasedRate/");
                            calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, tmpfilePathA_ta, availabilityRewardsList, markingChanged);
                            calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, tmpfilePathP_ta, performabilityRewardsList, markingChanged);
                        }
                        //Ri sistemo la marcatura incrementata
                        changeArrivals(steadyNet, marking, pt, tmp);
                    }
                    //pn = builder.setSteadyStateTransition(pn, performabilityRewards.getSteadyState(), numPh, marking);

                    //rewards = calculateGSPNSteadyState(pn, marking, 8, filePathA);
                }*/
        }
    }



/*
        performabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathP_ss, performabilityRewardsList);
        availabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathA_ss, availabilityRewardsList);
        //Calcolato Steady State re-buildo la net a steady state
        PetriNet steadyNet = builder.buildSteadyStateNet(availabilityRewards.getSteadyState(), numPh, marking);
        calculateGSPNTransientAnalysis(steadyNet, marking, poolSize, filePathP_ta, performabilityRewardsList, "");
        */

        //Calcolato Steady State re-buildo la net a steady state

    /*    availabilityRewards = calculateGSPNSteadyState(pn, marking, poolSize, filePathA_ss, availabilityRewardsList);
        //String startMarking = marking.toString();
        int a = 3;
        pn = builder.setSteadyStateTransition(pn, availabilityRewards.getSteadyState(), numPh, marking);
        for (int i = 0; i < 3; i++) {
            calculateGSPNTransientAnalysis(pn, marking, poolSize, filePathP_ta, availabilityRewardsList);
            changeArrivals(pn,marking, 2, a);
            a++;
        }*/



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


