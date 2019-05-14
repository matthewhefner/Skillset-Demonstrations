package util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

public class Tools
{   
    private static final int NEW_LINE_REP = 319;
    private static JFrame f;
    private static JProgressBar progressBar;
    
    public static int[] fileToAsciiArray(File file) throws FileNotFoundException
    {
        Scanner fileScanner = new Scanner(file);
        ArrayList<Integer> asciiList = new ArrayList<Integer>();
        while (fileScanner.hasNextLine())
        {
            String line = fileScanner.nextLine();
            for (Character c : line.toCharArray())
            {
                if ((int) c < 319)
                    asciiList.add((int) c);
                else
                    //The Space character (32) replaces any unicode character whose
                    //value is greater than 318
                    asciiList.add(32);
            }
            //Add new line character as 319
            asciiList.add(NEW_LINE_REP);
        }
        int[] asciiArray = new int[asciiList.size()];
        for (int i = 0; i < asciiList.size(); i ++)
        {
            asciiArray[i] = asciiList.get(i);
        }
        fileScanner.close();
        return asciiArray;
    }
    
    public static BigInteger[] concatenateAscii(int[] asciiArray, BigInteger modulus)
    {
        ArrayList<BigInteger> bigInts = new ArrayList<BigInteger>();
        String currentIntString = "";
        for (int i = 0; i < asciiArray.length; i++)
        {
            if ((new BigInteger(currentIntString + asciiArray[i])).compareTo(modulus) == -1)
            {
                currentIntString += String.valueOf(asciiArray[i]);
            }
            else
            {
                bigInts.add(new BigInteger(currentIntString));
                currentIntString = "" + asciiArray[i];
            }
            if (i == asciiArray.length - 1)
            {
                bigInts.add(new BigInteger(currentIntString));
            }
        }
        BigInteger[] myBigIntegers = new BigInteger[bigInts.size()];
        for (int i = 0; i < bigInts.size(); i ++)
        {
            myBigIntegers[i] = bigInts.get(i);
        }
        return myBigIntegers;
    }
    
    public static int[] partitionAscii(BigInteger[] concatenatedAsciis)
    {
        ArrayList<Integer> reverseAsciiList = new ArrayList<Integer>(); 
        StringBuffer total = new StringBuffer("");
        initiateProgress("Partitioning ASCII Values");
        for (BigInteger i : concatenatedAsciis)
        {
            //Here's why I used StringBuffer and not String!
            total.append(i.toString());
            //The complexity would have been horrible! D:
        }
        String temp = "";
        for (int j = 0; j < total.length(); j++)
        {
            if ((new BigInteger(temp + total.charAt(j))).compareTo(new BigInteger("319")) != 1)
            {
                temp += total.charAt(j);
            }
            else
            {
                if ((new BigInteger(temp).intValue()) == NEW_LINE_REP)
                {
                    reverseAsciiList.add((new BigInteger("10").intValue()));
                }
                else
                {
                    reverseAsciiList.add((new BigInteger(temp).intValue()));
                }
                temp = "" + total.charAt(j);
            }
            if (j == total.length() - 1 && temp.length() > 0)
            {
                if ((new BigInteger(temp).intValue()) == NEW_LINE_REP)
                {
                    reverseAsciiList.add((new BigInteger("10").intValue()));
                }
                else
                {
                    reverseAsciiList.add((new BigInteger(temp).intValue()));
                }  
            }
            reportProgress(j + 1, total.length());
        }
        
        int[] asciiValues = new int[reverseAsciiList.size()];
        for (int i = 0; i < reverseAsciiList.size(); i++)
        {
            asciiValues[i] = reverseAsciiList.get(i);
        }
        return asciiValues;
    }
    
    public static BigInteger[] fileToBigIntegers(File file) throws FileNotFoundException
    {
        Scanner fileScanner = new Scanner(file);
        ArrayList<BigInteger> bigInts = new ArrayList<BigInteger>();
        while (fileScanner.hasNext())
        {
            bigInts.add(new BigInteger(fileScanner.next()));
        }
        BigInteger[] bigIntsArray = new BigInteger[bigInts.size()];
        for (int i = 0; i < bigInts.size(); i ++)
        {
            bigIntsArray[i] = bigInts.get(i);
        }
        fileScanner.close();
        return bigIntsArray;
    }
    
    public static void initiateProgress(String process)
    {
        f = new JFrame(process + "...");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = f.getContentPane();
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder(process + "...");
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);
        f.setSize(300, 100);
        f.setLocation(300, 200);
        f.setVisible(true);
    }
    
    public static void reportProgress(int count, int totalCount)
    {
        progressBar.setValue(100 * count / totalCount);
        if (count >= totalCount)
        {
            f.setVisible(false);
            f.dispose();
        }
    }
}
