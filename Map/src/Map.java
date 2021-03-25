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
	///////////////////////////////////////
	////// wrong
	///// algorithm
	////// for
	///// the 
	////// graph
	
	
	
	
	
	File data = new File("graph_data.txt");
	final double RADIUS = 15.0 / 2;
	ArrayList<Vertex<String>> path;
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JPanel buttons = new JPanel();
	JPanel drawing = new JPanel() 
			{
				public void paint(Graphics g)
				{
					g.drawImage(image, 0, 0, null);
			
					for (Vertex<String> v : graph.vertices.values())
					{
						g.setColor(Color.black);
						g.fillOval(v.xlocation, v.ylocation, 15, 15);
						g.drawString(v.info, v.xlocation - 15, v.ylocation + 25);

						for (LocationGraph.Edge e : v.edges)
						{
							g.drawLine(v.xlocation + (int) RADIUS, v.ylocation + (int) RADIUS, e.getNeighbor(v).xlocation +  (int) RADIUS, e.getNeighbor(v).ylocation +  (int) RADIUS);
						}
						
						if (path != null)
						{
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
	
	BufferedImage image;
	LocationGraph<String> graph = new LocationGraph<String>();
	Vertex<String> previous;
	
	public Map() 
	{	
		try 
		{
			image = ImageIO.read(new File("map.jpg"));
		} 
		catch (IOException e) 
		{
			System.out.println("Sorry, the background is not in this folder or is not named correclty");
			e.printStackTrace();
		}
		
		
		drawing.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e) 
			{
				
			}

			public void mousePressed(MouseEvent e) 
			{
				for (Vertex<String> v : graph.vertices.values())
				{
					// if the user clicks on two vertices in a row
					if (Math.sqrt(Math.pow(e.getX() - (v.xlocation + RADIUS), 2) + Math.pow(e.getY() - (v.ylocation + RADIUS), 2)) <= RADIUS && previous == null)
					{
						previous = v;
						return;
					}
					else if (Math.sqrt(Math.pow(e.getX() - (v.xlocation + RADIUS), 2) + Math.pow(e.getY() - (v.ylocation + RADIUS), 2)) <= RADIUS && previous != null)
					{
						for (LocationGraph.Edge edge : v.edges)
						{
							if (edge.v2.info == previous.info || edge.v1.info == previous.info)
							{
								path = graph.search(v.info, previous.info);	
								frame.getContentPane().repaint();
								previous = null;
								return;
							}
						}
						graph.connect(v.info, previous.info);
						previous = null;
						frame.getContentPane().repaint();
						return;
					}
				}	
				
				String vertex = JOptionPane.showInputDialog("Label the place you are trying to add: ");
				
				if (vertex == (null))
				{
					System.out.println("You tried to enter an empty vertex");
				}
				else 
				{
					graph.addVertex(vertex, e.getX(), e.getY());
				}
				
				previous = null;
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
				// TODO Auto-generated method stub
				
			}
		});
		
		JButton save = new JButton("Click here to save your map");
		buttons.add(save);
		
		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					FileWriter writer = new FileWriter(data);
					for (Vertex<String> v : graph.vertices.values())
					{
						writer.write(v.info + "~" + v.xlocation + "~" + v.ylocation + "~");
						
						for (LocationGraph<String>.Edge<Double> ed : v.edges)
						{
							writer.write(ed.getNeighbor(v).info + "~");
						}
						writer.write("\n");
					}
					writer.close();
				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		
		JButton latest = new JButton("Load latest saved map");
		buttons.add(latest);
		latest.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
						try 
						{
							BufferedReader reader = new BufferedReader(new FileReader(data));
							graph = new LocationGraph<String>();
							int k = 0;
							for (String line = reader.readLine(); line != null; line = reader.readLine())
							{
								String[] array = line.split("~");
								graph.addVertex(array[0], Integer.parseInt(array[1]), Integer.parseInt(array[2]));
								frame.getContentPane().repaint();
								k++;
							}
							reader.close();
							
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
							e1.printStackTrace();
						} 
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}
					}
				});
		
		
		
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
