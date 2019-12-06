import java.util.*;
import java.io.*;


public class lsrouter {
  static String topFile = "topofile";
  static String mesFile = "messagefile";
  static String changesFile = "changesfile";
  static String outputFile = "output.txt";
  static int[][] topology;
  static int[] distances;
  static int[] nHops;
  static String nextHops = "";
  static String[][] messages;
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

    System.out.println("\nDIJKSTRA: " + 1);
    Djikstra(0);
    System.out.println("\n\nDIJKSTRA: " + 2);
    Djikstra(1);
    System.out.println("\n\nDIJKSTRA: " + 3);
    Djikstra(2);
    System.out.println("\n\nDIJKSTRA: " + 4);
    Djikstra(3);
    System.out.println("\n\nDIJKSTRA: " + 5);
    Djikstra(4);

    System.out.println("\n\n\nCHANGE 0: ");
    readChanges(changesFile, 0);        
    System.out.println("\n\nDIJKSTRA: " + 1);
    Djikstra(0);
    System.out.println("\n\nDIJKSTRA: " + 2);
    Djikstra(1);
    System.out.println("\n\nDIJKSTRA: " + 3);
    Djikstra(2);
    System.out.println("\n\nDIJKSTRA: " + 4);
    Djikstra(3);
    System.out.println("\n\nDIJKSTRA: " + 5);
    Djikstra(4);

    System.out.println("\n\n\nCHANGE 1: ");
    readChanges(changesFile, 1);
    System.out.println("\n\nDIJKSTRA: " + 1);
    Djikstra(0);
    System.out.println("\n\nDIJKSTRA: " + 2);
    Djikstra(1);
    System.out.println("\n\nDIJKSTRA: " + 3);
    Djikstra(2);
    System.out.println("\n\nDIJKSTRA: " + 4);
    Djikstra(3);
    System.out.println("\n\nDIJKSTRA: " + 5);
    Djikstra(4);

    System.out.println("\n\n\nCHANGE 2: ");
    readChanges(changesFile, 2);
    System.out.println("\n\nDIJKSTRA: " + 1);
    Djikstra(0);
    System.out.println("\n\nDIJKSTRA: " + 2);
    Djikstra(1);
    System.out.println("\n\nDIJKSTRA: " + 3);
    Djikstra(2);
    System.out.println("\n\nDIJKSTRA: " + 4);
    Djikstra(3);
    System.out.println("\n\nDIJKSTRA: " + 5);
    Djikstra(4);
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
    distances = new int[topCount];
    Boolean[] added = new Boolean[topCount];
    for(int i = 0; i < topCount; i++) {
      distances[i] = MAX;
      added[i] = false;
    }
    distances[source] = 0;
    int[] parents = new int[topCount];
    parents[source] = -1;
    for(int i = 1; i < topCount; i++) {
      int nearestVertex = -1;
      int shortestDistance = MAX;
      for (int vertexIndex = 0; vertexIndex < topCount; vertexIndex++) {
        if (!added[vertexIndex] && distances[vertexIndex] < shortestDistance) {
          nearestVertex = vertexIndex;
          shortestDistance = distances[vertexIndex];
        }
      }
      added[nearestVertex] = true;
      for (int vertexIndex = 0; vertexIndex < topCount; vertexIndex++) {
        int edgeDistance = topology[nearestVertex][vertexIndex];
        if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < distances[vertexIndex])) {
          parents[vertexIndex] = nearestVertex;
          distances[vertexIndex] = shortestDistance + edgeDistance;
        }
      }
    }
    printSolution(source, distances, parents);
    writeTables();
  }

  public static void printSolution(int startVertex, int[] distances, int[] parents) {
    System.out.print("Vertex\t Distance\tPath");
    for (int vertexIndex = 0; vertexIndex < topCount; vertexIndex++) {
      System.out.print("\n" + (startVertex + 1) + " -> ");
      System.out.print((vertexIndex + 1) + " \t\t ");
      System.out.print(distances[vertexIndex] + "\t\t");
      printPath(vertexIndex, parents);
    }
  }

  public static void printPath(int currentVertex, int[] parents) {
    if (currentVertex == -1) {
      return;
    }
    printPath(parents[currentVertex], parents);
    System.out.print((currentVertex + 1) + " ");
  }

  public static void printM() {
    for(int i = 0; i < topCount; i++) {
      for(int j = 0; j < topCount; j++) {
        System.out.print(topology[i][j] + " "); 
      }
      System.out.println();
    }
  }

  public static void writeTables() {
    try {
      PrintWriter fileWriter = new PrintWriter(new FileWriter(outputFile, true));
      for (int i = 0; i < topCount; i++) {
        fileWriter.println((i + 1) + " " + "0" + " " + distances[i]);
      }
      fileWriter.println("");
      fileWriter.close();
    } catch (Exception e) {}
  }
}