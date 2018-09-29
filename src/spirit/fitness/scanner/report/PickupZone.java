package spirit.fitness.scanner.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.common.HttpRequestCode;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.HttpRestApi;
import spirit.fitness.scanner.restful.ModelZoneMapRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;
import spirit.fitness.scanner.restful.listener.ModelZone2CallBackFunction;
import spirit.fitness.scanner.until.LoadingFrameHelper;

//import spirit.fitness.scanner.util.NetWorkHandler;

import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.ModelDailyReportbean;
import spirit.fitness.scanner.model.ModelZone2bean;
import spirit.fitness.scanner.model.PickUpZoneMap;
import spirit.fitness.scanner.printer.until.PrintJtableUtil;
import spirit.fitness.scanner.printer.until.PrintTableUntil;
import spirit.fitness.scanner.printer.until.PrinterHelper;



public class PickupZone {

	private static PickupZone modelZone2Report = null;

	public final static int REPORT = 0;
	public final static int MIN_QUANTITY = 1;
    private int startIdx;
	
	private List<String> resultModelItem;

	public JFrame frame1,frame2,frame3;
    public JPanel panel1,panel2,panel3;
	private LoadingFrameHelper loadingframe;
	private JProgressBar loading;
	private Timer timer;
	

	private JButton btnDone;

	private ModelZoneMapRepositoryImplRetrofit fgModelZone2;

	public static PickupZone getInstance() {
		if (modelZone2Report == null) {
			modelZone2Report = new PickupZone(2);
		}
		return modelZone2Report;
	}

	public static boolean isExit() {
		return modelZone2Report != null;
	}

	public PickupZone(int zoneCode) {
		resultModelItem = new ArrayList<String>();
		loadingframe = new LoadingFrameHelper("Loading Data from Server...");
		loading = loadingframe.loadingSample("Loading Data from Server...");
		intialCallback();
		loadModelZone2Map(zoneCode);
		setTimer();
		timer.start();

	}

	private void displayTable1(List<PickUpZoneMap> data) {

		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);

		if(frame1 == null) {
		frame1 = new JFrame();
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setTitle("Zone2fifo");
		frame1.setLocationRelativeTo(null);
		frame1.setBounds(70, 100, 1050, 1875);
		//frame.setUndecorated(true);
		frame1.setResizable(false);
		}
		panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));
		panel1.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		frame1.add(panel1);

		placeComponents(panel1, data);
		loadingframe.setVisible(false);
		loadingframe.dispose();
		// frame.setLocationRelativeTo(null);
		// frame.setSize(1000, 500);
		frame1.setVisible(true);

		frame1.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame1.dispose();
				frame1.setVisible(false);
			}
		});

	}
	
	private void displayTable2(List<PickUpZoneMap> data) {

		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);
		if(frame2 == null) {
		frame2 = new JFrame();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setTitle("Zone2fifo");
		frame2.setLocationRelativeTo(null);
		frame2.setBounds(70, 100, 1050, 1875);
		//frame.setUndecorated(true);
		frame2.setResizable(false);
		}
		panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));
		panel2.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		frame2.add(panel2);

		placeComponents(panel2, data);
		loadingframe.setVisible(false);
		loadingframe.dispose();
		// frame.setLocationRelativeTo(null);
		// frame.setSize(1000, 500);
		frame2.setVisible(true);

		frame2.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame2.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame2.dispose();
				frame2.setVisible(false);
			}
		});

	}
	
	private void displayTable3(List<PickUpZoneMap> data) {

		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);
		if(frame3 == null) {
		frame3 = new JFrame();
		frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame3.setTitle("Zone2fifo");
		frame3.setLocationRelativeTo(null);
		frame3.setBounds(70, 100, 1050, 1875);
		//frame.setUndecorated(true);
		frame3.setResizable(false);

		}
	
		panel3 = new JPanel();
		panel3.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));
		panel3.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		frame3.add(panel3);

		placeComponents(panel3, data);
		loadingframe.setVisible(false);
		loadingframe.dispose();
		// frame.setLocationRelativeTo(null);
		// frame.setSize(1000, 500);
		frame3.setVisible(true);

		frame3.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame3.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame3.dispose();
				frame3.setVisible(false);
			}
		});

	}

	private void placeComponents(JPanel panel, List<PickUpZoneMap> data) {

		/*
		 * We will discuss about layouts in the later sections of this tutorial. For now
		 * we are setting the layout to null
		 */
		panel.setLayout(null);

		// ScrollPane for Result
		JScrollPane scrollZonePane = new JScrollPane();

		scrollZonePane.setBackground(Constrant.TABLE_COLOR);
		panel.add(scrollZonePane);

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

		double dataSize = data.size();
		double divide = 3;
		double size = dataSize/divide;
		
		
		int limit = (int) Math.ceil(size);
		
		if(startIdx + limit > data.size())
			limit =(int) dataSize - startIdx;
		Object rowDataReport[][] = new Object[limit][3];
		//System.out.println("data : "+limit);

		int s = 0;
		for (int i = startIdx; i < startIdx + limit; i++) {
			String printItem = "";
			for (int j = 0; j < 3; j++) {
				rowDataReport[s][0] = " " + data.get(i).ModelNo;
				rowDataReport[s][1] = " " + data.get(i).Location;
				rowDataReport[s][2] = data.get(i).Qty;
				
				
			}
			s++;
			
			resultModelItem.add(printItem);
		}
		startIdx = startIdx + limit;
		String zone = "";

		Object columnNames[] = { "Model#", "Location", "Quantity" };
		Font font = new Font("Verdana", Font.BOLD, 38);
		final Class[] columnClass = new Class[] { String.class, String.class, Integer.class };

		DefaultTableModel model = new DefaultTableModel(rowDataReport, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {

				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setFont(font);
		table.getTableHeader().setBackground(Constrant.DISPALY_ITEMS_TABLE_COLOR);
		table.setBackground(Constrant.DISPALY_ITEMS_TABLE_COLOR);
		table.setFont(font);
		table.setRowHeight(45);
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		TableColumn modelNo = table.getColumnModel().getColumn(0);
		modelNo.setPreferredWidth(10);
		modelNo.setCellRenderer(leftRenderer);
		TableColumn modelTitle = table.getColumnModel().getColumn(1);
		modelTitle.setPreferredWidth(240);
		modelTitle.setCellRenderer(leftRenderer);
		TableColumn qtycolumn = table.getColumnModel().getColumn(2);
		qtycolumn.setPreferredWidth(3);
		qtycolumn.setCellRenderer(leftRenderer);
		
		
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.setEnabled(false);

		int heigh = 0;
		//System.out.println("" + 50 * rowDataReport.length + 20);
		//if (40 * rowDataReport.length + 20 > 1800)
		//	heigh = 1800;
		//else
			heigh = 60 * rowDataReport.length;

		scrollZonePane.setBounds(10, 10, 1020, heigh);
		scrollZonePane.setViewportView(table);

		panel.add(scrollZonePane);

		btnDone = new JButton("Print");
		btnDone.setFont(font);
		btnDone.setBounds(5, 540, 200, 50);

		
		//panel.add(btnDone);

		/*
		 * refreshDone = new JButton("Refresh"); refreshDone.setFont(font);
		 * refreshDone.setBounds(220, 540, 200, 50);
		 * 
		 * refreshDone.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { refreshDone.setEnabled(false); loadingframe
		 * = new LoadingFrameHelper(); loading =
		 * loadingframe.loadingSample("Loading Data from Server...");
		 * loading.setValue(50); frame.dispose(); frame.setVisible(false); frame = null;
		 * loadModelZone2Map(); } }); panel.add(refreshDone);
		 */

		JButton exitDone = new JButton("Exit");
		exitDone.setFont(font);
		exitDone.setBounds(220, 540, 200, 50);

	
		//panel.add(exitDone);

	}

	private void intialCallback() {
		fgModelZone2 = new ModelZoneMapRepositoryImplRetrofit();
		fgModelZone2.setinventoryServiceCallBackFunction(new ModelZone2CallBackFunction() {

			@Override
			public void resultCode(int code) {
				// TODO Auto-generated method stub
				if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {

				}
			}

			@Override
			public void getReportItems(List<ModelZone2bean> items) {

				
			}

			@Override
			public void getModelDailyReportItems(List<ModelDailyReportbean> items) {
				// TODO Auto-generated method stub

			}

			@Override
			public void pickUpZone(List<PickUpZoneMap> items) {
				//System.out.println("size"+items.size()+","+items.size()/3);
				displayTable1(items);
				//System.out.println("startIdx:"+startIdx);
				displayTable2(items);
				//startIdx = startIdx + Math.round(items.size()/3) +items.size()%3;
				//System.out.println("startIdx:"+startIdx);
				displayTable3(items);
			}

		});
	}

	// Loading Models data from Server
	private void loadModelZone2Map(int zoneCode) {

		// loading model and location information from Server
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					fgModelZone2.pickupZoneQty(zoneCode);

				} catch (Exception e) {
					e.printStackTrace();
					//NetWorkHandler.displayError(loadingframe);
				}
			}
		});

	}

	private void printer() {

		List<String> headersList = Arrays.asList("Model#", "FG", "Quantity", "From(Zone 1)", "To(Zone 2)");

		List<List<String>> rowsList = new ArrayList<List<String>>();
		for (String s : resultModelItem) {
			String[] rowdata = s.split("\n");
			rowsList.add(Arrays.asList(rowdata));
		}

		// String result = PrintTableUtil.printReport(headersList, rowsList);
		String result = PrintTableUntil.printModelQuantityReport(headersList, rowsList);
		// content += result + itemsInfo;
		System.out.println(result);

		PrinterHelper print = new PrinterHelper();
		print.printTable(result);

	}

	private void printer(JTable table, JFrame frame) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		PrintJtableUtil.printTableResult("Replenishment Report - " + timeStamp, "", true, true, true, table, frame);
	}


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PickupZone window = new PickupZone(2);
					// QueryResult window = new QueryResult();
					// window.setContent(0, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
		   @Override
		   protected Boolean doInBackground() throws Exception {
		    // Simulate doing something useful.
			   loadModelZone2Map(2);
		    // Here we can return some object of whatever type
		    // we specified for the first template parameter.
		    // (in this case we're auto-boxing 'true').
		    return true;
		   }
		  };

	private void setTimer() {
		timer = new javax.swing.Timer(300000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("updated");
				startIdx = 0;
				frame1.dispose();
				frame1.setVisible(false);
				frame1.remove(panel1);
				frame2.dispose();
				frame2.setVisible(false);
				frame2.remove(panel2);
				frame3.dispose();
				frame3.setVisible(false);
				frame3.remove(panel3);
				loadModelZone2Map(2);

			}
		});
	}
	
		  
}
