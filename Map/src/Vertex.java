import java.util.HashSet;


//constructor of vertices
	public class Vertex<E> 
	{
		E info;
		Integer xlocation;
		Integer ylocation;
		HashSet<LocationGraph<E>.Edge<Double>> edges;
		
		public Vertex(E info, Integer xlocation, Integer ylocation)
		{
			this.info = info;
			this.xlocation = xlocation;
			this.ylocation = ylocation;
			edges = new HashSet<LocationGraph<E>.Edge<Double>>();
		}
		
		public boolean equals(Vertex<E> other)
		{
			return info.equals(other.info);
		}
	}
