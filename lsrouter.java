import java.util.*;
import java.io.*;


public class lsrouter {
  static String topFile = "topofile";
  static String mesFile = "messagefile";
  static String changesfile = "changesfile";
  static int[][] topology;
  static String[][] messages;
  static ArrayList<Integer> path = new ArrayList<Integer>();
  static String DELIM = "\\s";
  static int topCount;
  static int MAX = 999;
  static int vertices;
  static int edges;

  public static void main(String[] args) {
    readFile(topFile);
    readMessage(mesFile);
    System.out.println("MATRIX: ");
    printM();
    System.out.println("\nDIJKSTRA: " + 0);
    Djikstra(0);
    System.out.println("\nDIJKSTRA: " + 1);
    Djikstra(1);
    System.out.println("\nDIJKSTRA: " + 2);
    Djikstra(2);
    System.out.println("\nDIJKSTRA: " + 3);
    Djikstra(3);
    System.out.println("\nDIJKSTRA: " + 4);
    Djikstra(4);
    System.out.println("\nPATH: ");
    printPath();
    System.out.println("\nCHANGE 0: ");
    readChanges(changesfile, 0);        
    System.out.println("\nDIJKSTRA: " + 0);
    Djikstra(0);
    System.out.println("\nDIJKSTRA: " + 1);
    Djikstra(1);
    System.out.println("\nDIJKSTRA: " + 2);
    Djikstra(2);
    System.out.println("\nDIJKSTRA: " + 3);
    Djikstra(3);
    System.out.println("\nDIJKSTRA: " + 4);
    Djikstra(4);
    System.out.println("\nCHANGE 1: ");
    readChanges(changesfile, 1);
    System.out.println("\nDIJKSTRA: " + 0);
    Djikstra(0);
    System.out.println("\nDIJKSTRA: " + 1);
    Djikstra(1);
    System.out.println("\nDIJKSTRA: " + 2);
    Djikstra(2);
    System.out.println("\nDIJKSTRA: " + 3);
    Djikstra(3);
    System.out.println("\nDIJKSTRA: " + 4);
    Djikstra(4);
    System.out.println("\nCHANGE 2: ");
    readChanges(changesfile, 2);
    System.out.println("\nDIJKSTRA: " + 0);
    Djikstra(0);
    System.out.println("\nDIJKSTRA: " + 1);
    Djikstra(1);
    System.out.println("\nDIJKSTRA: " + 2);
    Djikstra(2);
    System.out.println("\nDIJKSTRA: " + 3);
    Djikstra(3);
    System.out.println("\nDIJKSTRA: " + 4);
    Djikstra(4);
    printM();
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
      int source = 0, destination = 0, distance = 0, vertices = 0;
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
      int source = 0, destination = 0, distance = 0;
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

  public static void Djikstra(int source) {
    int[] distances = new int[topCount];
    Boolean[] shortPath = new Boolean[topCount];

    for(int i = 0; i < topCount; i++) {
      distances[i] = MAX;
      shortPath[i] = false;
    }
    distances[source] = 0;
    int min = 0;
    for(int i = 0; i < topCount - 1; i++) {
      min = minDistance(distances, shortPath);
      shortPath[min] = true;
      for(int j = 0; j < topCount; j++) {
        if(!shortPath[j] && topology[min][j] != 0 && distances[min] != MAX && distances[min] + topology[min][j] < distances[j]) {
          distances[j] = distances[min] + topology[min][j];
        }
      }
      int leastIndex = 9999;
      int least = 9999;
      for (int j = 0; j < distances.length; j++) {
        if (distances[j] < least && distances[j] != 0) {
          least = distances[j];
          leastIndex = j;
        }
      }
      path.add(leastIndex);
    }
    printP(distances);
  }

  public static int minDistance(int[] distances, Boolean[] shortPath) {
    int min = MAX, minIndex = -1;
    for(int i = 0; i < topCount; i++) {
      if(shortPath[i] != true && distances[i] <= min) {
        min = distances[i];
        minIndex = i;
      }
    }
    return minIndex;
  }
  
  public static void printP(int[] distances) {
    for(int i = 0; i < topCount; i++) {
      System.out.println(i + "\t" + distances[i]);
    }
  }

  public static void printPath() {
    for (int i = 0; i < path.size(); i++) {
      System.out.println(path.get(i));
    }
  }

  public static void printM() {
    for(int i = 0; i < topCount; i++) {
      for(int j = 0; j < topCount; j++) {
        System.out.print(topology[i][j] + " "); 
      }
      System.out.println();
    }
  }
}