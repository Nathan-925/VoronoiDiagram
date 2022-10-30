import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class VoronoiDiagram {

	private ArrayList<Point> points;
	private int[][] pixels;
	
	public VoronoiDiagram(int width, int height, Collection<Point> points) {
		points = new ArrayList<>(points);
		for(Point p: points)
			if(p.getX() < 0 || p.getX() >= width || p.getY() < 0 || p.getY() >= height)
				throw new IllegalArgumentException(String.format("Point (%d, %d) is not in rectangle of size %dx%d", p.getX(), p.getY(), width, height));
		generatePixels(width, height);
	}
	
	public VoronoiDiagram(int width, int height, Point ... points) {
		this(width, height, Arrays.asList(points));
	}
	
	public VoronoiDiagram(int width, int height, int numPoints, Random rand) {
		points = new ArrayList<>(numPoints);
		for(int i = 0; i < numPoints; i++)
			points.add(new Point(rand.nextInt(width), rand.nextInt(height)));
		generatePixels(width, height);
	}
	public VoronoiDiagram(int width, int height, int numPoints) {
		this(width, height, numPoints, new Random());
	}
	
	private void generatePixels(int width, int height) {
		pixels = new int[width][height];
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++) {
				double min = Double.MAX_VALUE;
				for(int l = 0; l < points.size(); l++) {
					double dist = Math.pow(points.get(l).getX()-i, 2)+Math.pow(points.get(l).getY()-j, 2);
					if(dist < min) {
						min = dist;
						pixels[i][j] = l;
					}
				}
			}
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_RGB);
		int colors[] = new int[points.size()];
		for(int i = 0; i < colors.length; i++)
			colors[i] = (int)(Math.random()*0x1000000);
		for(int i = 0; i < pixels.length; i++)
			for(int j = 0; j < pixels[i].length; j++)
				img.setRGB(i, j, colors[pixels[i][j]]);
		return img;
	}
	
	public Point getNearest(int x, int y) {
		return points.get(pixels[x][y]);
	}
	
	public ArrayList<Point> getPoints(){
		return points;
	}
	
	public static void main(String[] args) {
		int width = args.length >= 2 ? Integer.valueOf(args[0]) : 800;
		int height = args.length >= 2 ? Integer.valueOf(args[1]) : 600;
		int numPoints = args.length >= 3 ? Integer.valueOf(args[2]) : 10;
		Random rand = args.length >= 4 ? new Random(Long.valueOf(args[3])) : new Random();
		
		VoronoiDiagram vor = new VoronoiDiagram(width, height, numPoints, rand);
		
		JFrame frame = new JFrame("Voronoi Diagram");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().add(new JLabel(new ImageIcon(vor.getImage())));
		frame.pack();
		frame.setVisible(true);
	}
	
}