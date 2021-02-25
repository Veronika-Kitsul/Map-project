
public class Node<E> {

	public Double priority;
	public E info;
	
	public Node(Double p, E i) 
	{
		this.priority = p;
		this.info = i;
	}
		
	public String toString()
	{
		return (priority + " - " + info);
	}
}
