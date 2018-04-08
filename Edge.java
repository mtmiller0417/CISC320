package pkgAlternative;

public class Edge {
	private Node parent;
	private Node destination;
	private int departureTime;
	private int travelTime;
	private int arrivalTime;
	
	public Edge(Node parent, Node destination, int departureTime, int travelTime){//[0][1][2][3] 
		this.parent = parent;
		this.destination = destination;
		this.departureTime = departureTime;
		this.travelTime = travelTime;
		this.arrivalTime = ((departureTime + travelTime) % 24);
	}
	public Edge(Node parent, Node destination, String departureTime, String travelTime){//[0][1][2][3] 
		int departTime = Integer.parseInt(departureTime);
		int travTime = Integer.parseInt(travelTime);
		this.parent = parent;
		this.destination = destination;
		this.departureTime = departTime;
		this.travelTime = travTime;
		this.arrivalTime = ((departTime + travTime) % 24);
	}
	
	public Node getDestination(){
		return destination;
	}
	
	public int getDepartureTime(){
		return departureTime;
	}
	
	public Node getParent(){
		return this.parent;
	}
	
	public int getArrivalTime(){
		return arrivalTime;
	}
	 
	@Override
	public String toString(){
		return "[" + parent + " " + destination + " " + this.departureTime + " " + this.travelTime + "]";
	}
}
