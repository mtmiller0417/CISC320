package pkgAlternative;

import java.util.ArrayList;

public class Node {
	private String name;
	private Integer blood;//Blood required from start (starts at infinity)
	private boolean visited; //Used to tell if the node has been visited during a traversal
	private ArrayList<Edge> edgeList;//Outgoing edges
	private ArrayList<Node> parentList;//incoming edges to this node
	
	
	private int earliestArrival;
	private boolean start;
	
	public Node(String name){
		this.edgeList = new ArrayList<Edge>();
		this.parentList = new ArrayList<Node>();
		this.blood = Integer.MAX_VALUE;//Set the blood to 'infinity' to start
		this.name = name;
		this.visited = false;
		this.earliestArrival = Integer.MAX_VALUE;
		this.start = false;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void addEdge(Edge route){
		edgeList.add(route);
	}
	
	public void setBlood(int blood){
		this.blood = blood;
	}
	
	public void addBlood(){
		this.blood += 1;
	}
	
	public int getBlood(){
		return this.blood;
	} 
	
	public ArrayList<Edge> getEdgeList(){
		return this.edgeList;
	}
	
	public void visited(){
		this.visited = true;
		//System.out.println("Visited " + this.toString());
	}
	
	public void setVisit(boolean tf){
		this.visited = tf;
	}
	
	public boolean isVisited(){
		return this.visited;
	}
	
	public void addParent(Node parent){
		parentList.add(parent);
	}
	
	public void checkAndSetEarliestArrival(int time){
		if(this.earliestArrival == 0){
			this.earliestArrival = time;
		}
		else if(time < this.earliestArrival){
			this.earliestArrival = time;
		}
		
	}
	
	public void overrideEarliestArrival(int time){
		this.earliestArrival = time;
	}
	
	public int getEarliestArrival(){
		return this.earliestArrival;
	}
	
	public void setStart(){
		this.start = true;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	

}
