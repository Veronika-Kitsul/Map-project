import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Map
{
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JPanel buttons = new JPanel();
	JPanel drawing = new JPanel() 
			{
				public void paint(Graphics g)
				{
					g.drawImage(image, 0, 0, null);
			
					g.setColor(Color.black);
					for (LocationGraph.Vertex v : graph.vertices.values())
					{
						g.fillOval(v.xlocation, v.ylocation, 10, 10);
						g.drawString((String) v.info, v.xlocation - 10, v.ylocation + 25);
					}
				}
			};
	
	BufferedImage image;
	LocationGraph<String> graph = new LocationGraph<String>();
	
	public Map() 
	{	
		try 
		{
			image = ImageIO.read(new File("map.jpg"));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		drawing.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				
			}

			@Override
			public void mousePressed(MouseEvent e) 
			{
				String vertex = JOptionPane.showInputDialog("Label the place you are trying to add: ");
				graph.addVertex(vertex, e.getX(), e.getY());
				frame.getContentPane().repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) 
			{
				
			}

			@Override
			public void mouseEntered(MouseEvent e) 
			{
				
			}

			@Override
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
				File data = new File("graph_data.txt");
				try 
				{
					FileWriter writer = new FileWriter(data);
					for (LocationGraph.Vertex v : graph.vertices.values())
					{
						writer.write(v.info + "~" + v.xlocation + "~" + v.ylocation + "\n");
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
