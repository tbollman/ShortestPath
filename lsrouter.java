import java.util.*;
import java.io.*;


public class lsrouter {
static String topFile = "topofile";
static String mesFile = "messagefile";
static int[][] topology;
static String[][] messages;
static int mesSource, mesDes;
static String DELIM = "\\s";
static int topCount;
static int MAX = Integer.MAX_VALUE;

  public static void main(String[] args) {
    readFile(topFile);
    readMessage(mesFile);
    Djikstra(4);
    //printM();
  } 

  public static void readFile(String fileName) {
    try {
      Scanner fileScanner = new Scanner(new File(fileName));
      int count = 0;
      while(fileScanner.hasNextLine()) {
        count++;
        fileScanner.nextLine();
      }
      topCount = count;
      topology = new int[count+1][count+1];
      count = 0;
      fileScanner = new Scanner(new File(fileName));
      int source = 0, destination = 0, distance = 0;
      while(fileScanner.hasNextLine()) {
        String fileLine = fileScanner.nextLine();
        if(
        source = Integer.parseInt(fileLine);
        destination = Integer.parseInt(splitLine[1]);
        distance = Integer.parseInt(splitLine[2]);
        topology[source][destination] = distance;
        topology[destination][source] = distance;
        count++;
      }
      for(int i = 0; i < topology.length; i++) {
        for(int j = 0; j < topology[i].length; j++) {
          if(i != j && topology[i][j] == 0) {
            topology[i][j] = MAX;
          }
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static void readMessage(String fileName) {
    try {
      Scanner fileScanner = new Scanner(new File(fileName));
      messages = new String[topCount+1][topCount+1];
      while(fileScanner.hasNextLine()) {
        String temp = "";
        String fileLine = fileScanner.nextLine();
        String[] splitLine = fileLine.split(DELIM);
        mesSource = Integer.parseInt(splitLine[0]);
        mesDes = Integer.parseInt(splitLine[1]);
        for(int i = 2; i < splitLine.length; i++) {
          temp += splitLine[i]+ " ";
        }
        messages[mesSource][mesDes] = temp;
        
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void Djikstra(int source) {
    int[] distances = new int[topCount+1];
    Boolean[] shortPath = new Boolean[topCount+1];

    for(int i = 1; i < topCount + 1; i++) {
      distances[i] = MAX;
      shortPath[i] = false;
    }
    distances[source] = 0;
    for(int i = 1; i < topCount; i++) {
      int min = minDistance(distances,shortPath);
      System.out.println("TEST: "+ min);
      shortPath[min] = true;
      
      for(int j = 1; j < topCount+1; j++) {
        if(!shortPath[j] && topology[min][j] != 0 && distances[min] !=
            MAX && distances[min] + topology[min][j] <
            distances[j]) {
              distances[j] = distances[min] + topology[min][j];
        }
      }

    }
    /*for(int i = 1; i < topCount+1; i++) {
      System.out.println(shortPath[i]);
    }*/
    printP(distances);
  }

  public static int minDistance(int[] distances, Boolean[] shortPath) {
    int min = MAX, minIndex = -1;
    for(int i = 1; i < topCount+1; i++) {
      if(shortPath[i] != true && distances[i] <= min) {
        min = distances[i];
        minIndex = i;
      }
    }
    return minIndex;
  }

  public static void printP(int[] distances) {
    for(int i = 1; i < topCount +1; i++) {
      System.out.println(i + "\t" + distances[i]);
    }
  }

  public static void printM() {
    for(int i = 1; i < topCount + 1; i++) {
      for(int j = 1; j < topCount + 1; j++) {
        System.out.print(topology[i][j] + " "); 
      }
      System.out.println();
    }
  }
}

