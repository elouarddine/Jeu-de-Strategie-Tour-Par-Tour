package vue;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import controle.Controleur;

public class Panels extends JPanel implements PropertyChangeListener {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel tableModel;
    private Controleur controleur;
    @SuppressWarnings("unused")
	private Frame frame;
    private JPanel gridPanel; 
    private JPanel tablePanel; 
    private JPanel buttonsPanel; 
    private JSplitPane splitPane; 

    public Panels(Controleur controleur, Frame frame) {
        this.controleur = controleur;
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        this.controleur.addPropertyChangeListener(this);

        initializeTablePanel();   
        initializeGridPanel();    
        initializeButtonsPanel(); 

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, gridPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.0); 
        
        add(splitPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    // Méthode pour initialiser le panneau de la grille
    private void initializeGridPanel() {
        gridPanel = creerGrillePanel(controleur);
    }

    // Méthode pour initialiser le panneau de la table
    private void initializeTablePanel() {
        String[] columnNames = {"Nom", "Énergie", "Armes", "Explosifs", "État"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new EnergyRowRenderer());

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(Color.GRAY);
        header.setForeground(Color.WHITE);

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);

        table.setRowHeight(30);
        table.setBackground(Color.DARK_GRAY);
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        scrollPane.getViewport().setBackground(Color.DARK_GRAY);

        chargerInformationsTable();

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(300, getHeight()));

        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Informations des Joueurs",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.BLACK));
    }

    // Méthode pour initialiser le panneau des boutons (inchangée)
    private void initializeButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setBackground(Color.DARK_GRAY);

        JButton suivantButton = createButton("Suivant", Color.decode("#4CAF50"), Color.decode("#388E3C"));
        suivantButton.addActionListener(e -> {
            controleur.jouerTourJoueurCourant();
            // Pas besoin d'appeler mettreAJourAffichage(), cela sera fait via l'observateur
        });

        JButton quitterButton = createButton("Quitter", Color.decode("#D32F2F"), Color.decode("#B71C1C"));
        quitterButton.addActionListener(e -> controleur.quitterApplication());

        JButton rejouerButton = createButton("Rejouer", Color.decode("#FFA726"), Color.decode("#FB8C00"));
        rejouerButton.addActionListener(e -> {
            controleur.reinitialiserJeu();
        });

        buttonsPanel.add(suivantButton);
        buttonsPanel.add(quitterButton);
        buttonsPanel.add(rejouerButton);
    }
    
    
    public JPanel getGridPanel(){
    	
    	return this.gridPanel;
    }

    public static JPanel creerGrillePanel(Controleur controleur) {
        int largeur = controleur.getLargeurGrille();
        int longueur = controleur.getLongueurGrille();

        int cellWidth = 60;
        int cellHeight = 60;

        JPanel gridPanel = new JPanel(new GridLayout(longueur, largeur));
        gridPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Vue Grille",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.BLACK));
        gridPanel.setBackground(Color.LIGHT_GRAY);

        for (int y = 0; y < longueur; y++) {
            for (int x = 0; x < largeur; x++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(cellWidth, cellHeight));

                String imagePath = controleur.getImagePourPosition(x, y); // Appel à la méthode pour récupérer l'image
                Color borderColor = Color.BLACK; 
                cell.setBorder(BorderFactory.createLineBorder(borderColor));

                if (imagePath != null) {
                    ImageIcon imageIcon = redimensionnerImage(imagePath, cellWidth, cellHeight + 10);
                    if (imageIcon != null) {
                        JLabel label = new JLabel(imageIcon);
                        cell.setLayout(new BorderLayout());
                        cell.add(label, BorderLayout.CENTER);
                    } else {
                        cell.setBackground(borderColor);
                    }
                } else {
                    cell.setBackground(borderColor);
                }
                gridPanel.add(cell);
            }
        }

        return gridPanel;
    }


    private static ImageIcon redimensionnerImage(String imagePath, int largeur, int hauteur) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Erreur lors du redimensionnement de l'image : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour mettre à jour l'affichage
    public void mettreAJourAffichage() {
        // Mettre à jour la table des joueurs
        chargerInformationsTable();

        // Mettre à jour la grille
        gridPanel = creerGrillePanel(controleur);
        splitPane.setRightComponent(gridPanel);

        revalidate();
        repaint();
    }

    // Méthode de l'observateur
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("modeleMisAJour".equals(evt.getPropertyName())) {
            mettreAJourAffichage();
        }
    }

    private JButton createButton(String text, Color bgColor, Color borderColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(borderColor, 3));
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }

    private void chargerInformationsTable() {
        tableModel.setRowCount(0);

        List<Object[]> informations = controleur.getInformationsPourJTable();
        for (Object[] row : informations) {
            tableModel.addRow(row);
        }
    }

    private static class EnergyRowRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            String etat = (String) table.getModel().getValueAt(row, 4);

            Color rowColor;
            switch (etat) {
                case "total":
                    rowColor = Color.GREEN;
                    break;
                case "moitie":
                    rowColor = Color.YELLOW;
                    break;
                case "eliminer":
                    rowColor = Color.RED;
                    break;
                default:
                    rowColor = Color.GRAY;
                    break;
            }

            if (!isSelected) {
                cell.setBackground(rowColor);
                table.setSelectionBackground(rowColor.darker());
            }

            cell.setForeground(Color.BLACK);
            return cell;
        }
    }
}
