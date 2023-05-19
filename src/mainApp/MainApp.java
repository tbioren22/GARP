package mainApp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

/**this should be the good version
 * Class: MainApp
 * @author S23_R301
 * <br>Purpose: Top level class for CSSE220 Project containing main method 
 * <br>Restrictions: None
 * 
 */
public class MainApp {
	private final String FRAME_TITLE = "Chromosome Thingiemabob";
	private final String GENERATION_FRAME_TITLE = "Evolution Viewer";
    private final int FRAME_WIDTH = 600;
    private final int FRAME_HEIGHT = 700;
    private final int PFRAME_WIDTH = 653;
    private final int PFRAME_HEIGHT = 675;
    private final int DELAY = 5;
    public static final int GENERATION_FRAME_WIDTH = 1200;
    public static final int GENERATION_FRAME_HEIGHT = 600;
    
    private double mutationRate;
    private int populationSize;
    private int generationNum;
    private int genomeLength;
    private double elitismPercent;
    private String selectionString;
    
    private SelectionMethod selectionMethod;
    private String[] selectionMethods = {"Truncation", "Roulette", "Rank"};
    private boolean crossover;
    private boolean isRunning = false;
    private GenerationComponent generation = new GenerationComponent();
    private JFrame generationFrame = new JFrame();
    private boolean restarting = false;;
	
	/**
	 * Manages the general structure of the app. Serves as the graphics viewer class that manages the 
	 * JFrame and component and includes the buttons and their listeners. All possible exceptions are
	 * all so tried and caught in this class.
	 * 
	 * @throws IOException
	 * @throws IncorrectFileSizeException
	 */
	private void runApp() throws IOException, IncorrectFileSizeException {
		// Set up JFrame and PopulationComponent
		JFrame pFrame = new JFrame();
		pFrame.setTitle("Population Viewer");
		pFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PopulationComponent population = new PopulationComponent(generation.getGeneration());
		pFrame.add(population, BorderLayout.CENTER);
		pFrame.setVisible(true);
		
		
		//Set up JFrame and ChromosomeComponent
		JFrame frame = new JFrame();
		frame.setTitle(FRAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChromosomeComponent chromosome = new ChromosomeComponent();
		frame.add(chromosome, BorderLayout.CENTER);
		frame.setTitle(chromosome.getFileName());
		frame.setVisible(true);
		
		
		generationFrame.setTitle(GENERATION_FRAME_TITLE);
		generationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		generationFrame.add(generation, BorderLayout.CENTER);
		
		//Generation Frame buttons & misc
		JPanel labelPanel = new JPanel();
		generationFrame.add(labelPanel, BorderLayout.NORTH);
		
		JLabel generationViewerLabel = new JLabel("Generation Viewer");
		labelPanel.add(generationViewerLabel);
		
		JPanel interactionPanel = new JPanel();
		generationFrame.add(interactionPanel, BorderLayout.SOUTH);
		
		JLabel mutationTextLabel = new JLabel("Mutation Rate (decimal) ");
		JTextField mutationTextField = new JTextField(3);
		interactionPanel.add(mutationTextLabel);
		interactionPanel.add(mutationTextField);
		
		JLabel selectionTextLabel = new JLabel("Selection ");
		JComboBox selectionBox = new JComboBox(selectionMethods);
		interactionPanel.add(selectionTextLabel);
		interactionPanel.add(selectionBox);
		selectionBox.setEnabled(true);
		
		JLabel crossoverYN = new JLabel("Crossover Y/N?");
		JCheckBox crossoverCheck = new JCheckBox();
		interactionPanel.add(crossoverYN);
		interactionPanel.add(crossoverCheck);
		
		JLabel populationSizeLabel = new JLabel("Population Size ");
		JTextField populationTextField = new JTextField(3);
		interactionPanel.add(populationSizeLabel);
		interactionPanel.add(populationTextField);
		
		JLabel generationLabel = new JLabel("Generations ");
		JTextField generationTextField = new JTextField(5);
		interactionPanel.add(generationLabel);
		interactionPanel.add(generationTextField);
		
		JLabel genomeLengthLabel = new JLabel("Genome Length ");
		JTextField genomeTextField = new JTextField(3);
		interactionPanel.add(genomeLengthLabel);
		interactionPanel.add(genomeTextField);
		
		JLabel elitismLabel = new JLabel("Elitism (decimal) ");
		JTextField elitismTextField = new JTextField(3);
		interactionPanel.add(elitismLabel);
		interactionPanel.add(elitismTextField);
		
		JButton evolutionButton = new JButton("Start Evolution");
		interactionPanel.add(evolutionButton);
		evolutionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (evolutionButton.getText() == "Start Evolution" || evolutionButton.getText() == "Restart Evolution") {
					if(evolutionButton.getText() == "Restart Evolution"){
						generation.reset();
						restarting = false;
						isRunning = true;
					}
					evolutionButton.setText("Pause Evolution");
					isRunning = true;
					try {
						mutationRate = Double.parseDouble(mutationTextField.getText());
					} catch (NumberFormatException e1) {
						mutationRate = 0.01;
					}

					generation.setMutationRate(mutationRate);

					selectionString = selectionBox.getSelectedItem().toString();

					if (selectionString == "Roulette") {
						selectionMethod = SelectionMethod.ROULETTE;
					} else if (selectionString == "Rank") {
						selectionMethod = SelectionMethod.RANK;
					} else {
						selectionMethod = SelectionMethod.TOP_HALF;
					}

					generation.setSelectionMethod(selectionMethod);
					// Crossover

					try {
						populationSize = Integer.parseInt(populationTextField.getText());
					} catch (NumberFormatException e1) {
						populationSize = 100;
					}

					
					generation.setPopulationSize(populationSize);
					try {
						generationNum = Integer.parseInt(generationTextField.getText());
					} catch (NumberFormatException e1) {
						generationNum = 100;
					}

					generation.setNumOfGen(generationNum);

					try {
						genomeLength = Integer.parseInt(genomeTextField.getText());
					} catch (NumberFormatException e1) {
						genomeLength = 100;
					}
					if(genomeLength > 100 || genomeLength < 0)
						genomeLength = 100;

					generation.setGenomeLength(genomeLength);

					try {
						elitismPercent = Double.parseDouble(elitismTextField.getText());
					} catch (NumberFormatException e1) {
						elitismPercent = 0;
					}

					generation.setEliteNum(elitismPercent * populationSize);


					generation.setCrossover(crossover);
					
					
				} else {
					evolutionButton.setText("Start Evolution");
					isRunning = false;
				}


			}

		});
		
		crossoverCheck.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					System.out.println("Checkbox Selected");
					crossover = true;
				} else {
					System.out.println("Checkbox Deselected");
					crossover = false;
				}
				
			}
				
		}); 
		
		
		
		
		
		//Manage buttons and text field
		JPanel mutatePanel = new JPanel();
		frame.add(mutatePanel, BorderLayout.SOUTH);
		
		JButton mutateButton = new JButton("Mutate");
		mutatePanel.add(mutateButton);
		
		JTextField textField = new JTextField(3);
		JLabel textFieldLabel = new JLabel("Mutation Rate (decimal): ");
		mutatePanel.add(textFieldLabel);
		mutatePanel.add(textField);

		class MutateListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					chromosome.mutateChromosome(Double.parseDouble(textField.getText())); 
				} catch(NumberFormatException e1){ 
					chromosome.mutateChromosome();
				}
				System.out.println("Mutate chromosome.");
				chromosome.repaint();
			}
		}
		
		mutateButton.addActionListener(new MutateListener());
		
		JButton loadButton = new JButton("Load");
		mutatePanel.add(loadButton);
		
		class LoadListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Load chromosome.");
				try {
					JFileChooser jf = new JFileChooser();
					jf.showOpenDialog(null);
					File f = jf.getSelectedFile();
					chromosome.loadNewChromosome(f);
					chromosome.repaint();
					frame.setTitle(chromosome.getFileName());
				} catch (IncorrectFileSizeException e2) {
					e2.printErrorMessage();
				} catch (Exception e1) {
					System.err.println("Unknown Exception. Please try again.");
				} 
			}
			
		}
		
		loadButton.addActionListener(new LoadListener());
		
		JButton saveButton = new JButton("Save");
		mutatePanel.add(saveButton);
		
		class SaveListener implements ActionListener {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					chromosome.saveChromosome();
				} catch (IOException e1) {
					System.out.println("IO Exception.");
				} 
				
			}
			
		}
		
		saveButton.addActionListener(new SaveListener());
		
		frame.pack();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		pFrame.pack();
		pFrame.setSize(PFRAME_WIDTH, PFRAME_HEIGHT);
		
		Timer t = new Timer(DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(isRunning && !generation.isTerminated()) {
					generation.update();
					chromosome.setChromo(generation.getBestChromo());
					generation.repaint();
					generationFrame.repaint();
					chromosome.repaint();
					frame.repaint();
					population.setGeneration(generation.getGeneration());
					population.repaint();
					pFrame.repaint();
					
				}
				if(generation.isTerminated()) {
					evolutionButton.setText("Restart Evolution");
					restarting = true;
					isRunning = false;
				}
				
			}
		});
		
		//Starts the simulator
		t.start();
		generationFrame.setVisible(true);
		generationFrame.pack();
		generationFrame.setSize(GENERATION_FRAME_WIDTH, GENERATION_FRAME_HEIGHT);
	} // runApp
	
	
	/**
	 * ensures: runs the application
	 * @param args unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		MainApp mainApp = new MainApp();
		try {
			mainApp.runApp();
		} catch (IncorrectFileSizeException e) {

			e.printStackTrace();
		}		
	} // main

}