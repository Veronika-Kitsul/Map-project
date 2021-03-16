import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class LocationGraph<E> 
{
	HashMap<E, Vertex<E>> vertices;
	
	
	// add vertices to a hash map that matches their input to the actual vertex
	public LocationGraph()
	{
		vertices = new HashMap<E, Vertex<E>>();
	}
	
	// constructor for adding vertices
	public void addVertex(E info, Integer xlocation, Integer ylocation)
	{
		vertices.put(info, new Vertex<E>(info, xlocation, ylocation));
	}
	
	// connecting vertices with edges
	public void connect(E info1, E info2)
	{
		Vertex<E> v1 = vertices.get(info1);
		Vertex<E> v2 = vertices.get(info2);
		
		Edge e = new Edge(v1, v2);
		
		v1.edges.add(e);
		v2.edges.add(e);
	}
	
	// constructor of labeled edges
	public class Edge
	{
		Double label;
		Vertex<E> v1, v2;
		
		public Edge(Vertex<E> v1, Vertex<E> v2)
		{
			this.label = Math.sqrt(Math.pow((v1.xlocation - v2.xlocation), 2) + Math.pow((v1.ylocation - v2.ylocation), 2));
			this.v1 = v1;
			this.v2 = v2;
		}
		
		public Vertex<E> getNeighbor(Vertex<E> v)
		{
			if (v.info.equals(v1.info))
			{
				return v2;
			}
			return v1;
		}
	}
	
	/* constructor of vertices
	public class Vertex 
	{
		E info;
		Integer xlocation;
		Integer ylocation;
		HashSet<Edge> edges;
		
		public Vertex(E info, Integer xlocation, Integer ylocation)
		{
			this.info = info;
			this.xlocation = xlocation;
			this.ylocation = ylocation;
			edges = new HashSet<Edge>();
		}
		
		public boolean equals(Vertex other)
		{
			return info.equals(other.info);
		}
	}*/
	
	// Dijkstra's Algorithm
	public ArrayList<Object> search(E place, E destiny)
	
	{
		// handle null pointers and give details as to what went wrong
		if (vertices.get(destiny) == null)
		{
			System.out.println("There is no destination with this name. ");
			return null;
		}
		else if (vertices.get(place) == null)
		{
			System.out.println("There is no starting point with this name. ");
			return null;
		}
		
		// create a priority queue of places to visit
		PriorityQueue<Vertex<E>> toVisit = new PriorityQueue<Vertex<E>>();
		toVisit.put(0.0, vertices.get(place));
		
		// create a list of visited places so we don't waste time visiting them twice
		HashSet<Vertex<E>> visited = new HashSet<Vertex<E>>();
		visited.add(vertices.get(place));
		
		// create a map to remember which vertex leads to which vertex
		HashMap<Vertex<E>, Edge> leadsTo = new HashMap<Vertex<E>, Edge>();
		
		// hash map of distances
		HashMap<Vertex, Double> distances = new HashMap<Vertex, Double>();
		
		for (Vertex<E> v: vertices.values())
		{
			distances.put(v, (double) Integer.MAX_VALUE);
		}
		distances.put(vertices.get(place), 0.0);
		
		Double distanceToStart = 0.0 ;
		
		// while we have nothing to visit == we searched the whole graph
		while (toVisit.size() != 0)
		{
			// we don't need to visit current vertex anymore because we are here, so we pop it 
			Vertex<E> curr = toVisit.pop();
			
			if (curr.info == destiny)
			{
				backTrace(curr, leadsTo);
			}
			
			// for all of the edges to the current vertex
			for (Edge e: curr.edges)
			{
				Vertex<E> neighbor = e.getNeighbor(curr);
				
				// if you have already visited then just continue
				if (visited.contains(neighbor)) continue;
				
				distanceToStart = distanceToStart + e.label;
				
				// put the path to the map
				if (distanceToStart < distances.get(neighbor))
				{
					leadsTo.put(neighbor, e);
					distances.put(neighbor, distanceToStart);
					toVisit.put(distanceToStart, neighbor);
				}
				visited.add(curr);
			}
			distanceToStart = 0.0;
		}
		return null;
	}
	
	// back trace method
	private ArrayList<Object> backTrace(Vertex<E> destiny, HashMap<Vertex<E>, Edge> leadsTo)
	{
		// start at the final position
		Vertex<E> curr = destiny;
		// array list to hold the path
		ArrayList<Object> path = new ArrayList<Object>();
		
		// while there is a vertex to lead us to another vertex
		while (leadsTo.get(curr) != null) 
		{
			// add it to the path
			path.add(0, curr.info);
			path.add(0, leadsTo.get(curr).label);
			curr = leadsTo.get(curr).getNeighbor(curr);
		}
		path.add(0, curr.info);
		System.out.println(path);
		return path;
	}
	

	public static void main(String[] args)
	{
		LocationGraph<String> g = new LocationGraph<String>();
		g.addVertex("A", 1, 1);
		g.addVertex("B", 3, 8);
		g.addVertex("C", 5, 1);
		g.addVertex("D", 2, -2);
		g.addVertex("E", 3, 0);
		
		g.connect("A", "B");
		g.connect("C", "B");
		g.connect("A", "D");
		g.connect("D", "E");
		g.connect("C", "E");
		
		g.search("A", "C");
	}
}
