import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;


public class BiClusterTool
{
    private Flag[] listOfFlags;
    private int numberOfClusters;
    private float percentageToKeep;

    public BiClusterTool(Flag[] listOfFlags, int numberOfClusters, float percentageToKeep) {
        this.listOfFlags = listOfFlags;
        this.numberOfClusters = numberOfClusters;
        this.percentageToKeep = percentageToKeep;
    }

    // main method to pilot all the biclustering process
    public List<List<Flag>> biCluster(List<List<Flag>> clusteredFlags)
    {

        long millisStart = Calendar.getInstance().getTimeInMillis();

        // analyze pairs in all the clusters, create and add sets where 2 flags have at least 3 criteria in common
        List<List<Flag>> sets = new ArrayList<List<Flag>>();
        List<List<Integer>> footPrints = new ArrayList<List<Integer>>();
        for(int a=0; a<clusteredFlags.size(); a++) {
            for(int i=0; i<clusteredFlags.get(a).size();i++) {
                Flag flagOne = clusteredFlags.get(a).get(i);
                for (int j = i + 1; j < clusteredFlags.size(); j++) {
                    Flag flagTwo = clusteredFlags.get(a).get(j);
                    ArrayList<Integer> footPrint = comparePairFlags(flagOne, flagTwo);
                    if(footPrint.size() >=3 && checkIfFootPrintGenerated(footPrint, footPrints)==0) {
                        footPrints.add(footPrint);
                        List<Flag> newSet = new ArrayList<Flag>();
                        newSet.add(flagOne);
                        newSet.add(flagTwo);
                        sets.add(newSet);
                    } else if(footPrint.size() >= 3 && checkIfFootPrintGenerated(footPrint, footPrints)>0){
                        sets.get(checkIfFootPrintGenerated(footPrint, footPrints)).add(flagOne);
                        sets.get(checkIfFootPrintGenerated(footPrint, footPrints)).add(flagTwo)
                    }
                }
            }
        }
        
        return sets;
    }

    private boolean checkIfFootPrintGenerated(ArrayList<Integer> footPrint, List<List<Integer>> footPrints) {
    }


    private ArrayList<Integer> comparePairFlags(Flag flagOne, Flag flagTwo)
    {
        ArrayList<Integer> footPrint = new ArrayList<Integer>();
        if(stringCompare(flagOne.name,flagTwo.name)>0) footPrint.add(1);
        if(nonSubtractionCompare(flagOne.landmass,flagTwo.landmass) >0) footPrint.add(2);
        if(nonSubtractionCompare(flagOne.zone,flagTwo.zone)>0) footPrint.add(3);
        if(nonSubtractionCompare(flagOne.area, flagTwo.area)>0) footPrint.add(4);
        if(nonSubtractionCompare(flagOne.population, flagTwo.population)>0) footPrint.add(5);
        if(nonSubtractionCompare(flagOne.language,flagTwo.language)>0) footPrint.add(6);
        if(nonSubtractionCompare(flagOne.religion,flagTwo.religion)>0) footPrint.add(7);
        if(nonSubtractionCompare(flagOne.bars,flagTwo.bars)>0) footPrint.add(8);
        if(nonSubtractionCompare(flagOne.stripes, flagTwo.stripes)>0) footPrint.add(9);
        if(nonSubtractionCompare(flagOne.colours, flagTwo.colours)>0) footPrint.add(10);
        if(nonSubtractionCompare(flagOne.red, flagTwo.red)>0) footPrint.add(11);
        if(nonSubtractionCompare(flagOne.green, flagTwo.green)>0) footPrint.add(12);
        if(nonSubtractionCompare(flagOne.blue, flagTwo.blue)>0) footPrint.add(13);
        if(nonSubtractionCompare(flagOne.gold, flagTwo.gold)>0) footPrint.add(14);
        if(nonSubtractionCompare(flagOne.white, flagTwo.white)>0) footPrint.add(15);
        if(nonSubtractionCompare(flagOne.black, flagTwo.black)>0) footPrint.add(16);
        if(nonSubtractionCompare(flagOne.orange, flagTwo.orange)>0) footPrint.add(17);
        if(stringCompare(flagOne.mainhue,flagTwo.mainhue)>0) footPrint.add(18);
        if(nonSubtractionCompare(flagOne.circles, flagTwo.circles)>0) footPrint.add(19);
        if(nonSubtractionCompare(flagOne.crosses, flagTwo.crosses)>0) footPrint.add(20);
        if(nonSubtractionCompare(flagOne.saltires, flagTwo.saltires)>0) footPrint.add(21);
        if(nonSubtractionCompare(flagOne.quarters, flagTwo.quarters)>0) footPrint.add(22);
        if(nonSubtractionCompare(flagOne.sunstars, flagTwo.sunstars)>0) footPrint.add(23);
        if(nonSubtractionCompare(flagOne.crescent, flagTwo.crescent)>0) footPrint.add(24);
        if(nonSubtractionCompare(flagOne.triangle, flagTwo.triangle)>0) footPrint.add(25);
        if(nonSubtractionCompare(flagOne.icon, flagTwo.icon)>0) footPrint.add(26);
        if(nonSubtractionCompare(flagOne.animate, flagTwo.animate)>0) footPrint.add(27);
        if(nonSubtractionCompare(flagOne.text, flagTwo.text)>0) footPrint.add(28);
        if(stringCompare(flagOne.topleft,flagTwo.topleft)>0) footPrint.add(29);
        if(stringCompare(flagOne.botright,flagTwo.botright)>0) footPrint.add(30);
        return footPrint;
    }

    // method to compare two String, return 0 if identical, 1 otherwise
    private int stringCompare(String a, String b)
    {
        return a.matches(b) ? 1 : 0;
    }

    //method to compare 2 ints, when subtraction doesn't represent closeness. Same output as above
    private int nonSubtractionCompare(int a, int b){
        return a == b ? 1 : 0;
    }

    //prints the result of the biclustering
    public void printBicluster(List<List<Flag>> biClusteredFlags){
        int num = 0;
        for(int i=0; i<biClusteredFlags.size();i++){
            num+=biClusteredFlags.get(i).size();
            System.out.println(biClusteredFlags.get(i));
        }
        System.out.println("Number of Countries : "+num);
    }


    //##########################

    // method to display the flags of the result of the bicluster
    public void displayClustersFlags(List<List<Flag>> clusters)
    {
        int counter = 1;
        for(List<Flag> cluster : clusters)
        {
            String title = "Cluster "+counter;
            JPanel pane = new JPanel();
            for(Flag flag : cluster)
            {
                String flagName = flag.name;
                flagName = flagName.replaceAll("-"," ");
                String src = "flags/Flag of "+flagName+".gif";
                ImageIcon icon = new ImageIcon(src);
                icon = resizeIcon(icon, 150, 118);
                JLabel label = new JLabel(icon);
                label.setText(flagName);
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setVerticalTextPosition(JLabel.BOTTOM);
                pane.add(label);
            }
            showFrame(pane,title);
            counter++;
        }
    }
    // resizes an ImageIcon
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height)
    {
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
    // shows the frame with the flags
    private void showFrame(JPanel pane, String title)
    {
        JFrame mainframe = new JFrame(title);
        mainframe.getContentPane().add(pane);
        mainframe.setBounds(0, 0, 1000, 1000);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setVisible(true);
    }


}
