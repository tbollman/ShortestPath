import java.util.*;
import java.io.*;

public class dvrouter {
  static String topFile = "topofile";
  static String mesFile = "messagefile";
  static String changesFile = "changesfile";
  static String outputFile = "output.txt";
  static int[][] topology;
  static int[] distances;
  static String[] nextHops;
  static String hops;
  static int count = 0;
  static String[][] messages;
  static String two2one;
  static String three2five;
  static String DELIM = "\\s";
  static int topCount;
  static int MAX = 999;
  static int vertices;
  static int edges;

  public static void main(String[] args) {
    readFile(topFile);
    readMessage(mesFile);

    BellmanFord(0);
    BellmanFord(1);
    BellmanFord(2);
    BellmanFord(3);
    BellmanFord(4);
	writeMessages(two2one, three2five);

    readChanges(changesFile, 0);
    BellmanFord(0);
    BellmanFord(1);
    BellmanFord(2);
    BellmanFord(3);
    BellmanFord(4);
	writeMessages(two2one, three2five);

    readChanges(changesFile, 1);
    BellmanFord(0);
    BellmanFord(1);
    BellmanFord(2);
    BellmanFord(3);
    BellmanFord(4);
	writeMessages(two2one, three2five);

    readChanges(changesFile, 2);
    BellmanFord(0);
    BellmanFord(1);
    BellmanFord(2);
    BellmanFord(3);
    BellmanFord(4);
	writeMessages(two2one, three2five);
  } 
  
    public static void readFile(String fileName) {
    try {
      Scanner fileScanner = new Scanner(new File(fileName));
      int count = 0;
      while(fileScanner.hasNextLine()) {
        count++;
        fileScanner.nextLine();
      }
      edges = count;
      topology = new int[count][count];
      count = 0;
      fileScanner = new Scanner(new File(fileName));
      int source = 0, destination = 0, distance = 0;
      while(fileScanner.hasNextLine()) {
        String fileLine = fileScanner.nextLine();
        String[] splitLine = fileLine.split(DELIM);
        source = Integer.parseInt(splitLine[0]);
        destination = Integer.parseInt(splitLine[1]);
        distance = Integer.parseInt(splitLine[2]);
        topology[source - 1][destination - 1] = distance;
        topology[destination - 1][source - 1] = distance;
        if(source > vertices) {
          vertices = source;
        }
        else if(destination > vertices) {
          vertices = destination;
        }
      }
      for(int i = 0; i < topology.length; i++) {
        for(int j = 0; j < topology[i].length; j++) {
          if(i != j && topology[i][j] == 0) {
            topology[i][j] = MAX;
          }
        }
      }
      topCount = vertices;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void readMessage(String fileName) {
    try {
      Scanner fileScanner = new Scanner(new File(fileName));
      messages = new String[topCount][topCount];
      int mesSource = 0, mesDes = 0;
      while(fileScanner.hasNextLine()) {
        String temp = "";
        String fileLine = fileScanner.nextLine();
        String[] splitLine = fileLine.split(DELIM);
        mesSource = Integer.parseInt(splitLine[0]);
        mesDes = Integer.parseInt(splitLine[1]);
        for(int i = 2; i < splitLine.length; i++) {
          temp += splitLine[i] + " ";
        }
        messages[mesSource - 1][mesDes - 1] = temp;
        
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void readChanges(String fileName, int line) {
    try {
      Scanner fileScanner = new Scanner(new File(fileName));
      int current = 0;
      int source = 0, destination = 0, distance = 0, vertices = 0;
      while (true) {
        if (current == line) {
          String fileLine = fileScanner.nextLine();
          String[] splitLine = fileLine.split(DELIM);
          source = Integer.parseInt(splitLine[0]);
          destination = Integer.parseInt(splitLine[1]);
          distance = Math.abs(Integer.parseInt(splitLine[2]));
          topology[source - 1][destination - 1] = distance;
          topology[destination - 1][source - 1] = distance;
          break;
        }
        current++;
        fileScanner.nextLine();
      }
    }
    catch (Exception e) {}
  }
  
  public static void BellmanFord(int source) {
    int[] distances = new int[topCount];
    for(int i = 0; i < topCount; i++) {
      distances[i] = MAX;
    }
    distances[source] = 0;
    for(int i = 0; i < topCount - 1; i++) {
      for(int j = 0; j < topCount; j++) {
        for (int k = 0; k < topCount; k++)
        if (topology[j][k] != MAX) {
          if(distances[k] > distances[j] + topology[j][k]) {
            distances[k] = distances[j] + topology[j][k];
          }
        }
      }
    }
    // Negative weight cycles
    for (int i = 0; i < vertices; i++) {
      for (int j = 0; j < vertices; j++) {
        if (topology[i][j] != MAX) {
          if (distances[j] > distances[i] + topology[i][j]) {
            System.out.println("NEGATIVE WEIGHT CYCLE FOUND");
          }
        }
      }
    }
  }
  
  public static void printSolution(int startVertex, int[] distances, int[] parents, String[] nextHops) {
    for (int vertexIndex = 0; vertexIndex < topCount; vertexIndex++) {
      hops = printPath(vertexIndex, parents);
      if (startVertex == 1 && vertexIndex == 0) {
        two2one = hops;
      } else if (startVertex == 2 && vertexIndex == 4) {
        three2five = hops;
      }
      if (hops.length() == 2) {
        nextHops[startVertex] = String.valueOf(hops.charAt(0));
        count++;
      } else {
        nextHops[count] = String.valueOf(hops.charAt(2));
        count++;
      }
    }
    writeTables();
  }

  public static void writeMessages(String two2one, String three2five) {
    try {
      PrintWriter fileWriter = new PrintWriter(new FileWriter(outputFile, true));
      fileWriter.println("from 2 to 1: hops" + two2one + "; message: " + messages[1][0]);
      fileWriter.println("from 3 to 5: hops" + three2five + "; message: " + messages[2][4]);
      fileWriter.println("");
      fileWriter.println("");
      fileWriter.close();
      two2one = "";
      three2five = "";
    } catch (Exception e) {}
  }

  public static String printPath(int currentVertex, int[] parents) {
    if (currentVertex == -1) {
      return "";
    }
    return printPath(parents[currentVertex], parents) + " " + (currentVertex + 1);
  }

  public static void writeTables() {
    try {
      PrintWriter fileWriter = new PrintWriter(new FileWriter(outputFile, true));
      for (int i = 0; i < topCount; i++) {
        fileWriter.println((i + 1) + " " + nextHops[i] + " " + distances[i]);
      }
      fileWriter.println("");
      fileWriter.close();
    } catch (Exception e) {}
  }
}
