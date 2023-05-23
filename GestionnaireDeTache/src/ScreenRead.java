import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;



/**
 * @author nocta
 *
 */
/**
 * Cette classe représente une interface utilisateur qui affiche les données
 * d'une liste de tâches, avec la possibilité de les trier par date.
 */
public class ScreenRead extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTable tableau;
	private DefaultTableModel modele;
	String[] priorite = {"Defcon 1", "Defcon 2", "Defcon 3", "Defcon 4", "Defcon 5"};
	String[] etat = {"A faire", "En cours", "Terminer"};
	private JFormattedTextField dateFin;
	Connection conn = null;
	
    /**
     * Affiche l'écran de la liste de tâches.
     */
	public void afficheEcran() {

		String url = "jdbc:mysql://localhost:3306/gestion"; // replace "localhost" with your MySQL server's IP address if it's not running locally
		String user = "mysql";
		String password = "mysql";
		
		//Connection a la base de données
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to database established.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
	    JPanel panel = new JPanel(new BorderLayout());
	    getContentPane().add(panel);

	    String[] entetes = {"ID", "Description", "Priorité", "État", "Date de Fin"};
	    
	    //Récupération des données de la bdd
	    try {
	    	String sql = "SELECT * FROM tasks;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			String sql2 = "SELECT count(*) FROM tasks;";
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
			ResultSet sizeResult = stmt2.executeQuery();
			sizeResult.next();
			int size = sizeResult.getInt(1); // Get the value of the first column
			System.out.println(size);
		    Object[][] donnees = new Object[size][5];
		    int i = 0;
			while(rs.next()) {
				
				String id = rs.getString("Id");
				String description = rs.getString("description");
		        String priorite = rs.getString("priority");
		        String etat = rs.getString("status");
		        String dateFin = rs.getString("due_date");
				donnees[i][0] = id;
				donnees[i][1] = description;
				donnees[i][2] = priorite;
				donnees[i][3] = etat;
				donnees[i][4] = dateFin;
				i++;
			}
			
		    modele = new DefaultTableModel(donnees, entetes);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    



	    tableau = new JTable(modele);

	    triage(modele); 

	    JScrollPane scrollPane = new JScrollPane(tableau);
	    panel.add(scrollPane, BorderLayout.CENTER);
	    
	    JPanel field = new JPanel();
	    
	    JLabel descriptionAdd = new JLabel("Description");
	    
	    JTextField description = new JTextField();
	    description.setPreferredSize(new Dimension(200,26));
	    
	    JComboBox<String> prioriteAdd = new JComboBox<>(priorite);
	    
	    JComboBox<String> etatAdd = new JComboBox<>(etat);
	    
	    JLabel dateFinLabel = new JLabel("Date normale de fin : ");
	    
        dateFin = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        dateFin.setPreferredSize(new Dimension(200,26));
        dateFin.setValue(new Date());

	    field.add(descriptionAdd);
	    field.add(description);
	    field.add(prioriteAdd);
	    field.add(etatAdd);
	    field.add(dateFinLabel);
	    field.add(dateFin);
	    panel.add(field, BorderLayout.NORTH);
	    
	    
	    JPanel allButton = new JPanel();
	    
	    JButton ajouter = new JButton("Ajouter tache");
	    ajouter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        String descriptionValue = description.getText();
		        String prioriteValue = (String) prioriteAdd.getSelectedItem();
		        String etatValue = (String) etatAdd.getSelectedItem();
		        Date dateFinValue = (Date) dateFin.getValue();
		        
		        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		        String formattedDate = dateFormat.format(dateFinValue); // Formatage de la date
		        Object[] rowData = {modele.getRowCount() + 1, descriptionValue, prioriteValue, etatValue, formattedDate};
		        modele.addRow(rowData);
		        notification(descriptionValue);

		        
		        try {
		            String sql = "INSERT INTO tasks (description, priority, status, due_date) VALUES (?, ?, ?, ?)";
		            PreparedStatement stmt = conn.prepareStatement(sql);
		            stmt.setString(1, descriptionValue);
		            stmt.setString(2, prioriteValue);
		            stmt.setString(3, etatValue);
		            stmt.setString(4, formattedDate);
		            
		            int rowsInserted = stmt.executeUpdate();
		            if (rowsInserted > 0) {
		                System.out.println("A new task was added to the database.");
		            }
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }

			}


		});
	    
	    JButton modifier = new JButton("Modifier tache");
	    modifier.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            int row = tableau.getSelectedRow(); // Récupération de la ligne sélectionnée
	            if (row >= 0) { // Vérification que l'utilisateur a bien sélectionné une ligne
	                String descriptionValue = description.getText();
	                String prioriteValue = (String) prioriteAdd.getSelectedItem();
	                String etatValue = (String) etatAdd.getSelectedItem();
	                String dateFinValue = dateFin.getText(); // Récupération de la date de fin modifiée

	                try {
	                    String sql = "UPDATE tasks SET description = ?, priority = ?, status = ?, due_date = ? WHERE id = ?";
	                    PreparedStatement stmt = conn.prepareStatement(sql);
	                    int trueLine = row+1;
	                    stmt.setString(1, descriptionValue);
	                    stmt.setString(2, prioriteValue);
	                    stmt.setString(3, etatValue);
	                    stmt.setString(4, dateFinValue);
	                    stmt.setString(5, ""+trueLine);
	                    stmt.executeUpdate();
	                    System.out.println("Task updated in the database.");
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                }

	                // Mise à jour des données de la ligne sélectionnée
	                modele.setValueAt(descriptionValue, row, 1);
	                modele.setValueAt(prioriteValue, row, 2);
	                modele.setValueAt(etatValue, row, 3);
	                modele.setValueAt(dateFinValue, row, 4);
	            }
	        }
	    });

	    
	    JButton supprimer = new JButton("Supprimer tache");
	    supprimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				supprimerTache();
			}
		});
	    
	    allButton.add(ajouter);
	    allButton.add(modifier);
	    allButton.add(supprimer);

	    panel.add(allButton, BorderLayout.SOUTH);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pack(); //dimensionne automatiquement la Frame
	    setLocationRelativeTo(null); //lance l'affichage au milieu de l'écran
	    setVisible(true);
	    
	   
	}
	
	private void notification(String descriptionValue) {
		String msg = "Tache a venir :" + descriptionValue;
		JOptionPane.showMessageDialog(this, msg, "Notification", JOptionPane.INFORMATION_MESSAGE);
	}

	
    protected void supprimerTache() {
    	int row = tableau.getSelectedRow(); // Récupération de la ligne sélectionnée
        if (row >= 0) { // Vérification que l'utilisateur a bien sélectionné une ligne
        	int trueLine = row +1;
        	
        	try {
        		String sql = "DELETE FROM tasks WHERE id = ?;";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, ""+trueLine);
				stmt.execute();
				System.out.println("Task deleted from the database");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            modele.removeRow(row); // Suppression de la ligne du modèle de données
        }
	}


	/**
     * Trie les données par date.
     * 
     * @param modele2 le modèle de données à trier
     */
	private void triage(DefaultTableModel modele2) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modele);
        tableau.setRowSorter(sorter);
        Comparator<String> dateComparator = Comparator.comparing(o -> o.toString());
        sorter.setComparator(4, dateComparator);

		
	}
}