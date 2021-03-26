import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Map
{
	// create a file to hold my data for the graph is case the user wants to reuse it
	File data = new File("graph_data.txt");
	
	// radius for the vertices on the map
	final double RADIUS = 15.0 / 2;
	
	// path when running search
	ArrayList<Vertex<String>> path;
	
	// booleans to control button change
	boolean isgps = false;
	boolean isvertex = false;
	boolean isconnect = false;
	
	// background image
	BufferedImage image;
	
	// graph 
	LocationGraph<String> graph = new LocationGraph<String>();
	
	// previous vertex to remember when the user clicks on two vertices in a row
	Vertex<String> previous;
	
	
	// setting up frame and panels
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JPanel buttons = new JPanel();
	JPanel drawing = new JPanel() 
			{
				// repainting the main drawing panel
				public void paint(Graphics g)
				{
					// background image
					g.drawImage(image, 0, 0, null);
			
					// for all the vertices in the graph
					for (Vertex<String> v : graph.vertices.values())
					{
						// draw vertices and their labels
						g.setColor(Color.black);
						g.fillOval(v.xlocation, v.ylocation, 15, 15);
						g.drawString(v.info, v.xlocation - 15, v.ylocation + 25);

						// for all the edges for the given vertex
						for (LocationGraph.Edge e : v.edges)
						{
							// draw connections
							g.drawLine(v.xlocation + (int) RADIUS, v.ylocation + (int) RADIUS, e.getNeighbor(v).xlocation +  (int) RADIUS, e.getNeighbor(v).ylocation +  (int) RADIUS);
						}
						
						// if the user ran gps
						if (path != null)
						{
							// paint all the lines and all the vertices in the path in red
							for (int i = 0; i < path.size() - 1; i++)
							{
								g.setColor(Color.red);
								g.drawLine(path.get(i).xlocation + (int) RADIUS, path.get(i).ylocation + (int) RADIUS, path.get(i + 1).xlocation +  (int) RADIUS, path.get(i + 1).ylocation +  (int) RADIUS);
							}
							
							for (int i = 0; i < path.size(); i++)
							{
								g.setColor(Color.red);
								g.fillOval(path.get(i).xlocation, path.get(i).ylocation, 15, 15);
							}
						}
					}
				}
			};
	
	
	public Map() 
	{	
		try 
		{
			image = ImageIO.read(new File("map.jpg"));
		} 
		catch (IOException e) 
		{
			JOptionPane message = new JOptionPane();
			message.showMessageDialog(frame, "We were not able to load the background image. It is most likely not in this folder or is not named properly.", "Error", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		
		// button for the gps mode
		JButton gps = new JButton("Run GPS");
		buttons.add(gps);
		gps.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						isgps = true;
						isconnect = false;
						isvertex = false;
					}
				});
		
		// button for adding the vertex mode
		JButton vertex = new JButton("Add Vertex");
		buttons.add(vertex);
		vertex.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						isvertex = true;
						isgps = false;
						isconnect = false;
					}
				});
		
		// button for connecting two vertices mode
		JButton connect = new JButton("Add Connection");
		buttons.add(connect);
		vertex.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						isconnect = true;
						isvertex = false;
						isgps = false;
					}
				});
		
		
		// if the user clicks anywhere on the main panel, one should be able to make operations over the map
		drawing.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e) 
			{
				
			}

			public void mousePressed(MouseEvent e) 
			{
				// for all vertices
				for (Vertex<String> v : graph.vertices.values())
				{
					// if the user clicks on one vertex
					if (Math.sqrt(Math.pow(e.getX() - (v.xlocation + RADIUS), 2) + Math.pow(e.getY() - (v.ylocation + RADIUS), 2)) <= RADIUS && previous == null)
					{
						// set it to be the previous vertex and return
						previous = v;
						return;
					}
					
					// if the previous vertex is not null, then the user clicked on two vertices in a row
					else if (Math.sqrt(Math.pow(e.getX() - (v.xlocation + RADIUS), 2) + Math.pow(e.getY() - (v.ylocation + RADIUS), 2)) <= RADIUS && previous != null)
					{
						// if it's gps mode
						if (isgps == true)
						{
							// run search and repaint the frame; set previous to null for further operations
							for (LocationGraph.Edge edge : v.edges)
							{
								path = graph.search(v.info, previous.info);	
								frame.getContentPane().repaint();
								previous = null;
								return;
							}
						}
						
						// if it's connect two vertices mode -- connect, repaint and set previous to null for further operations
						if (isconnect = true)
						{
							graph.connect(v.info, previous.info);
							previous = null;
							frame.getContentPane().repaint();
							return;
						}
					}
				}	
				
				// if it's add vertex mode
				if (isvertex == true)
				{	
					// pop up message to input the place name
					String vertex = JOptionPane.showInputDialog("Label the place you are trying to add: ");
					
					// if there is no input from the user
					if (vertex == (null))
					{
						System.out.println("You tried to enter an empty vertex");
					}
					else 
					{
						// for all vertices in the graph, make sure that the vertex name is unique
						for (Vertex v : graph.vertices.values())
						{
							if (vertex.equals(v.info))
							{
								JOptionPane message = new JOptionPane();
								message.showMessageDialog(frame, "You cannot add two places with the same name. Make sure you distinguish them.", "Error", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
							
							// if all good -- add a vertex
							else 
							{
								graph.addVertex(vertex, e.getX(), e.getY());
							}
						}
					}
				}
				previous = null;
				path = null;
				frame.getContentPane().repaint();
			}

			public void mouseReleased(MouseEvent e) 
			{
				
			}

			public void mouseEntered(MouseEvent e) 
			{
				
			}

			public void mouseExited(MouseEvent e) 
			{
				
			}
		});
		
		// a button to save the map at any given time
		JButton save = new JButton("Click here to save your map");
		buttons.add(save);
		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					// create a file writer
					FileWriter writer = new FileWriter(data);
					
					// for all the vertices in the graph
					for (Vertex<String> v : graph.vertices.values())
					{
						// write their information into a file
						writer.write(v.info + "~" + v.xlocation + "~" + v.ylocation + "~");
						
						// for all the edges for all the vertices
						for (LocationGraph<String>.Edge<Double> ed : v.edges)
						{
							// write neighbor info into the file
							writer.write(ed.getNeighbor(v).info + "~");
						}
						writer.write("\n");
					}
					writer.close();
				} 
				catch (IOException e1) 
				{
					// handle exceptions
					System.out.println("Sorry, something went wrong :(");
					e1.printStackTrace();
				}
			}
		});

		// a button to load the latest saved map
		JButton latest = new JButton("Load latest saved map");
		buttons.add(latest);
		latest.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
						try 
						{
							// create a buffered reader to read the file
							BufferedReader reader = new BufferedReader(new FileReader(data));
							
							// reset the graph
							graph = new LocationGraph<String>();
							int k = 0;
							for (String line = reader.readLine(); line != null; line = reader.readLine())
							{
								// take each symbol for the vertex and add it as the vertex info for the graph
								String[] array = line.split("~");
								graph.addVertex(array[0], Integer.parseInt(array[1]), Integer.parseInt(array[2]));
								frame.getContentPane().repaint();
								k++;
							}
							reader.close();
							
							// create another reader to make connections
							BufferedReader reader2 = new BufferedReader(new FileReader(data));
							int f = 0;
							for (String line2 = reader2.readLine(); line2 != null; line2 = reader2.readLine())
							{
								String[] array2 = line2.split("~");
								f++;
								for (int i = 3; i < array2.length; i++)
								{
									if (!array2[i].equals(""))
									{
										graph.connect(array2[0], array2[i]);
									}
								}
							}
							reader2.close();
							frame.getContentPane().repaint();
							
						} 
						catch (FileNotFoundException e1) 
						{
							JOptionPane message = new JOptionPane();
							message.showMessageDialog(frame, "We were not able to find the file which stored your latest map. It is most likely not in this folder or is not named properly.", "Error", JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} 
						catch (IOException e1) 
						{
							System.out.println("Sorry, something went wrong :(");
							e1.printStackTrace();
						}
					}
				});
		
		
		// a clean button to clean everything on screen
		JButton clean = new JButton("Clean All");
		buttons.add(clean);
		clean.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					graph = new LocationGraph<String>();
					frame.getContentPane().repaint();
				}
			});
		
		
		
		// general setup for panels and frame
		frame.add(panel);
		panel.add(drawing);
		panel.add(buttons);
		drawing.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		
		frame.setSize(image.getWidth(), image.getHeight() + buttons.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setFocusable(true);
	}

	public static void main(String[] args)
	{
		new Map();
	}
}
