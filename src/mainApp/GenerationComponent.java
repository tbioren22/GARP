package mainApp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;


public class GenerationComponent extends JComponent {
	
  	private final int SIDE_OFFSET = 30;
  	private final int TOP_OFFSET = 40;
  	private final int BOTTOM_OFFSET = 30;
  	
  	private final double LEGEND_X_OFFSET_RATIO = 0.2;
  	private final double LEGEND_Y_OFFSET_RATIO = 0.2;
  	private final int LEGEND_SQUARE_SIZE = 10;
  	
  	private final Color BEST_COLOR = Color.green;
  	private final Color WORST_COLOR = Color.red;
  	private final Color AVERAGE_COLOR = Color.yellow;
  	
  	
  	private Generation generation;
  	private int genNum;
  	
  	private ArrayList<Byte> bestLog;
	private ArrayList<Byte> worstLog;
	private ArrayList<Byte> avgLog;
	private double xInc;
	private double yInc;
	private int genCount;
  	
  	public GenerationComponent() {
  		this.setPreferredSize(new Dimension(MainApp.GENERATION_FRAME_WIDTH, MainApp.GENERATION_FRAME_HEIGHT) );
  		generation = new Generation(42, 100, 100);
  		genNum = 200;
  		bestLog = new ArrayList<Byte>();
  		worstLog = new ArrayList<Byte>();
  		avgLog = new ArrayList<Byte>();
  		bestLog.add(generation.getBestFitness(FitnessMethod.ONES));
  		worstLog.add(generation.getWorstFitness(FitnessMethod.ONES));
  		avgLog.add(generation.getAvgFitness(FitnessMethod.ONES));
  		xInc = 0;
  		yInc = 0;
  		genCount = 0;
  	}
  	
  	
  	@Override
  	protected void paintComponent(Graphics g) {
  		super.paintComponent(g);
  		Graphics2D g2 = (Graphics2D)g;
  		
  		
  		
  		for(int i = 0; i <= 10; i++) {
  			g2.drawString(i*10 + "", 
  					SIDE_OFFSET - 25, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10 + 5 - 1);
  			g2.drawLine(SIDE_OFFSET - 5, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10 - 1, 
  					SIDE_OFFSET, 
  					this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - BOTTOM_OFFSET - TOP_OFFSET) * i / 10 - 1);
  			g2.drawString(genNum / 10 * i + "", 
  					SIDE_OFFSET + (this.getWidth() - 2 * SIDE_OFFSET) * i / 10 - 5, 
  					this.getHeight() - BOTTOM_OFFSET + 20);
  			g2.drawLine(SIDE_OFFSET + (this.getWidth() - 2 * SIDE_OFFSET) * i / 10, 
  					this.getHeight() - BOTTOM_OFFSET, 
  					SIDE_OFFSET + (this.getWidth() - 2 * SIDE_OFFSET) * i / 10, 
  					this.getHeight() - BOTTOM_OFFSET + 5);
  		}
  		
  		
  		
  		g2.setColor(BEST_COLOR);
  		g2.fillRect((int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO),
  				LEGEND_SQUARE_SIZE, LEGEND_SQUARE_SIZE);
  		g2.setColor(Color.black);
  		g2.drawString("Best Fitness",
  				(int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO + 20), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 10));
  		
  		g2.setColor(AVERAGE_COLOR);
  		g2.fillRect((int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 20),
  				LEGEND_SQUARE_SIZE, LEGEND_SQUARE_SIZE);
  		g2.setColor(Color.black);
  		g2.drawString("Average Fitness",
  				(int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO + 20), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 10 + 20));
  		
  		g2.setColor(WORST_COLOR);
  		g2.fillRect((int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 20 * 2),
  				LEGEND_SQUARE_SIZE, LEGEND_SQUARE_SIZE);
  		g2.setColor(Color.black);
  		g2.drawString("Worst Fitness",
  				(int)(this.getWidth() - SIDE_OFFSET - (this.getWidth() - SIDE_OFFSET * 2) * LEGEND_X_OFFSET_RATIO + 20), 
  				(int)(this.getHeight() - BOTTOM_OFFSET - (this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) * LEGEND_Y_OFFSET_RATIO + 10 + 20 * 2));
  		
  		
  		
  		g2.translate(SIDE_OFFSET, this.getHeight()-BOTTOM_OFFSET);
  		xInc = ((double)this.getWidth() - SIDE_OFFSET * 2) / (genNum - 1);
  		yInc = ((double)this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET) / 100;
  		
  		for(int i = 1; i <= genCount; i++) {
  			g2.setColor(BEST_COLOR);
  	  		g2.drawLine((int)(Math.round(xInc * (i - 1))), (int)(Math.round(-bestLog.get(i - 1) * yInc)), 
  	  				(int)(Math.round(xInc * i)), (int)(Math.round(-bestLog.get(i) * yInc)));
  	  			
  	  		g2.setColor(AVERAGE_COLOR);
  	  		g2.drawLine((int)(Math.round(xInc * (i - 1))), (int)(Math.round(-avgLog.get(i - 1) * yInc)), 
  	  				(int)(Math.round(xInc * i)), (int)(Math.round(-avgLog.get(i) * yInc)));
  	  			
  	  		g2.setColor(WORST_COLOR);
  			g2.drawLine((int)(Math.round(xInc * (i - 1))), (int)(Math.round(-worstLog.get(i - 1) * yInc)), 
  					(int)(Math.round(xInc * i)), (int)(Math.round(-worstLog.get(i) * yInc)));	
  		}
  		g2.translate(-SIDE_OFFSET, -this.getHeight() + BOTTOM_OFFSET);
  		
  		
  		
  		g2.setColor(Color.black);
  		g2.drawRect(SIDE_OFFSET, TOP_OFFSET - 1, this.getWidth() - 2 * SIDE_OFFSET, this.getHeight() - TOP_OFFSET - BOTTOM_OFFSET);
  		
  	}
  	
  	public void update() {
  		if(genCount < genNum - 1) {
  			generation.evolve(FitnessMethod.ONES, SelectionMethod.TOP_HALF, 0.001, 0);
  			bestLog.add(generation.getBestFitness(FitnessMethod.ONES));
	  		worstLog.add(generation.getWorstFitness(FitnessMethod.ONES));
	  		avgLog.add(generation.getAvgFitness(FitnessMethod.ONES));
	  		genCount++;
  		}
  	}
}
