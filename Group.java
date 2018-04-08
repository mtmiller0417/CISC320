package pkgAlternative;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Logger;

public class Group {
	private static File file;
	private static Scanner scanner;
	private static PrintWriter writer; 
	
	private static String inFileName;//initial_in.txt for example
	private static String inFileLocation;//   Resources//intial_in.txt for example
	private static String outFileName;
	private static String outFileLocation;
	
	private static int numProblems;//Number of times you have to find amount of blood for our vampire friend(Found in openFile)
	private static int currentProblem;//The current problem you are on for our vampire friend
	
	private ArrayList<Node> printList;
	private Hashtable<String,Node> nodeTable;//Node name, Node
	private String start;
	private String end;
	
	

	public static void main(String [] wishMeLuck){
		String fileYouWishToRun = "input_in.txt";//Change this to change the input
		
		openFile(fileYouWishToRun);
		createOutFile();
		Group g = new Group();
		g.readAndWriteFile();
		System.out.println("\nWE ALL DONE EVERYTHNG");
		closeFile();
	}

	public Group(){
		nodeTable = new Hashtable<String, Node>();
		printList = new ArrayList<Node>();
	}
	
	public static void openFile(String fileName){
		currentProblem = 1;
		inFileName = fileName;
		inFileLocation = "Resources//"+ inFileName;
		outFileName = inFileName.substring(0, inFileName.length()-7) + "_out.txt";//grabs the part before _in.txt and appends to _out.txt to it
		outFileLocation = "Resources//" + outFileName;
		//System.out.println(inFileName + " --> " + outFileName);
		
		file = new File(inFileLocation);
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		numProblems = Integer.parseInt(scanner.nextLine());//gets the number of problems that need to be solved
		System.out.println(numProblems + " problem(s) to do\n");
	}
	
	public static void createOutFile(){//Include file ending in part of string
		
		//Try creating the file if it doesn't exist; if it does exist, tell user that it does
		try{
			file = new File(outFileLocation);
			boolean hasOpened = file.createNewFile();
			if(hasOpened){
				//System.out.println("File has been succesfully created!");
			}
			else{
				//System.out.println("File is already present at specific location...");
			}
		}
		catch(IOException e){
			System.out.println("IOException occurred!");
			e.printStackTrace();
		}
		
		try {
			writer = new PrintWriter(outFileLocation, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void readAndWriteFile(){
		if(currentProblem <= numProblems){
			processLine(scanner.nextLine());//Send the next line to processLine() method to be handled
			currentProblem++;
			nodeTable.clear();
			printList.clear();
			readAndWriteFile();//RUUUUN it back!
		}		
	}
	
	public void processLine(String line){
		String[] words = getWordArray(line);
		int validRoutes = 0;
		
		if(words.length == 1){
			int numRoutes = Integer.parseInt(words[0]);
			//System.out.println("\nNumber of Total Routes: " + words[0]);
			//System.out.println("Only legal routes shown below: ");
			
			for(int x = 0; x < numRoutes; x++){
				String nextLine = scanner.nextLine();
				words = getWordArray(nextLine);
				
				if(checkValidTime(Integer.parseInt(words[2]), Integer.parseInt(words[3]))){//words[2] is the departTime, and words[3] is the travelTime
					validRoutes++;
					nodeManipulation(words);
					//System.out.println(nextLine);
				}
			}
			//System.out.println("Total number of valid routes is " + validRoutes);
			String travel = scanner.nextLine();//Needed
			String[] startEnd = getWordArray(travel);
			this.start = startEnd[0];
			this.end = startEnd[1];
			//System.out.println("TRIP: " + start +" --> " + end);
			if(!nodeTable.isEmpty()){//If the table isn't empty
				modifiedBFS(start);
				printAndWrite();
			}
			else//If the table is empty
			{
				String message = "There is no route Vladimir can take...";
				System.out.println("Test Case " + currentProblem + ".");
				System.out.println(message);
				writer.println("Test Case " + currentProblem + ".");
				writer.println(message);
			}
			
			
		}
	}
	
	public void nodeManipulation(String[] info){//VERY IMPORTANT METHOD
		/*
		 * info[0]: Name of departureNode	(Node)
		 * info[1]: Name of arrivalNode		(Node)
		 * info[2]: Departure time			(String)
		 * info[3]: Travel time				(String)
		 * 
		 * DESCRIPTION: 
		 * 		Checks if arrivalNode is in the Hashtable, if not it makes it
		 * 		Then it checks if departureNode is in the Hashtable, if not it makes it 
		 * 		Then adds an edge to the departureNode's edgeList
		 */
		
		Node departureNode;//Initiate for wide scope
		Node arrivalNode;//Initiate for wide scope
		
		//Check if the arrivalNode is in the table first!
		if(!nodeTable.containsKey(info[1])){//If you DO NOT have the arrivalNode already in the table
			//Make it and add it to hashtable
			arrivalNode = new Node(info[1]);
			nodeTable.put(info[1], arrivalNode);
			printList.add(arrivalNode);
			/*System.out.println("Node " + arrivalNode.toString() + " was created and added to the table!");*/
		}
		else{ //Otherwise it already exists in Hashtable...
			arrivalNode = nodeTable.get(info[1]);
		}//arrivalNode exists at this point here no matter what
		

		//Check if departureNode is in the table second!
		if(nodeTable.containsKey(info[0])){//if the node is already in the table
			//FIND it
			departureNode = nodeTable.get(info[0]);//Retrieve that node
		}
		else{//If the node is not already in the table
			//MAKE it and add to hashtable
			departureNode = new Node(info[0]);//Make the new node
			printList.add(departureNode);
			nodeTable.put(info[0], departureNode);
			/*System.out.println("Node " + departureNode.toString() + " was created and added to the table!");*/
		}
		
		//Finally, add the edge to departureNode's edgeList
		Edge edge = new Edge(departureNode, arrivalNode, info[2], info[3]);//Make a new Edge
		departureNode.addEdge(edge);//Make and add a new Edge to the departureNode edgeList
		arrivalNode.addParent(departureNode);
		//System.out.println("Edge: " + edge + " was successfully added to " + info[0] + "'s edgeList");
	}

	public String[] getWordArray(String line){
		return line.split("\\s+");//Splits the string into a a words array (removes all whitespace(\\s+)) 
	}
	
	/*
	 * 
	 */
	
	public boolean checkValidTime(int departureTime, int travelTime){
		int arrivalTime = calcArrival(departureTime, travelTime);//Calculate the time the train would arrive
		if(arrivalTime > 6 && arrivalTime < 18){
			//Oh no, this time won't work! He'd need to be inside while hes on the train!
			return false;
		}
		else if(departureTime < 18 && departureTime >= 6){//If departure Time is between 7 and 17 hours(7am and 5pm)
			//Our vampire friend should be inside!
			return false;
		}
		else if(departureTime <= 6 && arrivalTime>= 18 ){
			return false;
		}
		else{
			return true;
		}
	}
	
	public int calcArrival(int departureTime, int travelTime){
		return ((departureTime + travelTime) % 24);
	}
	
	public boolean checkNeedBlood(int timeOne, int timeTwo){
		boolean bloodNeeded = false;
		while(timeOne != timeTwo){
			timeOne = incrementTime(timeOne);
			if(timeOne == 12){
				//If you ever pass hour 12 on your way to departureTime
				bloodNeeded = true;
			}
				
		}
		return bloodNeeded;
	}
	
	public int incrementTime(int time){
		return ((time + 1) % 24);
	}
	
	public boolean checkTimes(int arrival0, int departure1){
		/*
		 * If return is true then, 
		 * If return is false then, 
		 */
		if(departure1 < arrival0){
			return true;//You do need an additional bag of blood to wait it out
		}
		else{
			return false;//You don't need an additional bag of blood
			//return checkNeedBlood(arrival0, departure1);
		}
	}
	
	public boolean noonBetween(int timeOne, int timeTwo){
		if(timeOne == 0){
			return false;
		}
		
		if(12 > timeOne && 12 < timeTwo){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public void modifiedBFS(String start){
		//System.out.println("\n" + "basicBFS: ");
		Node currentNode;//The node you will be working with 
		Node startNode = nodeTable.get(start);//I dare you to throw an error...
		Queue<Node> queue = new LinkedList<Node>();//Make the queue
		
		startNode.setBlood(0);//The initialNode starts out at 0 (b/c its takes 0 to get to 'n' from 'n')
		startNode.checkAndSetEarliestArrival(0);//So no additional blood is used b/c Vladimir can take first train whenever w/o blood
		startNode.visited();//Mark node as visited
		queue.add(startNode);//Queue the initial Node
		
		while(!queue.isEmpty()){
			currentNode = queue.poll();//Retrieves and removes head of queue
			
			Iterator<Edge> edgeIterator = currentNode.getEdgeList().iterator();
			while(edgeIterator.hasNext()){
				Edge edge = edgeIterator.next();
				/*System.out.println("BEFORE: Parent(" + edge.getParent().toString() + "|" + edge.getParent().getBlood() + 
						") Destination(" + edge.getDestination().toString() + "|" + edge.getDestination().getBlood() + ")");*/
				queue.add(edge.getDestination());
				
				if(edge.getParent().getEarliestArrival() == 0){
					//The parent is the start
					edge.getDestination().overrideEarliestArrival(edge.getArrivalTime());
				}
				else{
					edge.getDestination().checkAndSetEarliestArrival(edge.getArrivalTime());//Sets arrival time of destination node
				}
				
				
				int parentsBlood = edge.getParent().getBlood();
				int destinationsBlood = parentsBlood;
				
				/*System.out.println("Arr0: " + edge.getParent().getEarliestArrival() + " | Dep1: " + edge.getDepartureTime()
						+ "  IF (dep < arr){addBlood} ");//add if dep < arr*/				
				if(checkTimes(edge.getParent().getEarliestArrival(), edge.getDepartureTime())){
					//Need to add one bag of blood to 
					//System.out.println("(1)Added blood to " + edge.getDestination().toString());
					destinationsBlood++;
				}
				else if(checkNeedBlood(edge.getDepartureTime(), edge.getArrivalTime())){//If extra blood is needed to make the ROUTE
					//System.out.println("(2)Added Bag to " + edge.getDestination().toString());
					destinationsBlood++;
					//Otherwise, no extra blood is needed to make the trip
				}
				else if(noonBetween(edge.getParent().getEarliestArrival(), edge.getDepartureTime())){
					//System.out.println("(3)Added Bag to " + edge.getDestination().toString());
					destinationsBlood++;
				}
				
				if(destinationsBlood < edge.getDestination().getBlood()){
					//Found a way that uses less blood!
					edge.getDestination().setBlood(destinationsBlood);
				}
				if(edge.getParent().getEarliestArrival() == 0){
					
				}
				/*System.out.println("AFTER: Parent(" + edge.getParent().toString() + "|" + edge.getParent().getBlood() + 
										") Destination(" + edge.getDestination().toString() + "|" + edge.getDestination().getBlood() + ")" + "\n");*/

			}
			//Once all its edges have been checked...
			currentNode.visited();//Mark this node as visited
		}
		//printNodes();
		//printAndWrite();
	}
	
	public void printNodes(){
		for(Node n : printList){
			//System.out.println(n.toString() + ": " + n.getBlood() + " bag(s) of blood");
		}
	}
	
	//Probably isn't necessary
	public void printAndWrite(){
		//System.out.println("Finalizing");
		Node n = nodeTable.get(end);
		String message = null;
		if(n.getBlood() == Integer.MAX_VALUE){
			message = "There is no route Vladimir can take...";
		}
		else{
			message = "Vladimir needs to bring " + n.getBlood() + " bag(s) of blood";
		}
		writer.println("Test Case " + currentProblem + ".");
		writer.println(message);
		
		System.out.println("Test Case " + currentProblem + ".");
		System.out.println(message);
	}
	
	public static void closeFile(){
		writer.close();
	}
}
