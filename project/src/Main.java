import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

public class Main {

    private final int MAX_WIDTH = 750;
    private final int MAX_HEIGHT = 500;

    private JFileChooser fc;

    private JPanel titlePanel;
    private JPanel bigPanel;
    private JSlider slider;
    private JTextField text;
    private JLabel dropLabel;
    private String path;

    public static void main(String[] args) {
        new Main().program();
    }

    void program() {
        JFrame f = new JFrame("Subtitle Adjuster");
        f.setLocation(300,200);
        f.setSize(MAX_WIDTH, MAX_HEIGHT);

        initPanels();

        f.add(BorderLayout.NORTH, titlePanel);
        f.add(BorderLayout.CENTER, bigPanel);

        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        fc = new JFileChooser("B:");
    }

    public void initPanels() {
        titlePanel = new JPanel();
        bigPanel = new JPanel(new BorderLayout());
        JPanel panel1 = new JPanel(new GridBagLayout());
        JPanel panel2 = new JPanel(new GridBagLayout());

        JLabel title = new JLabel("Time to adjust (seconds)", SwingConstants.CENTER);
        title.setForeground(Color.LIGHT_GRAY);
        titlePanel.add(title);
        titlePanel.setBackground(Color.DARK_GRAY);

        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(0,10,0,10);

        initSlider();
        initText();
        panel1.setSize(new Dimension(MAX_WIDTH, 50));
        panel1.add(slider, c1);
        panel1.add(text);
        panel1.setBackground(Color.DARK_GRAY);

        MyDragDropListener myDragDropListener = new MyDragDropListener();
        dropLabel = new JLabel("Drag something here!", SwingConstants.CENTER);
        JPanel subPanel1 = new JPanel(new BorderLayout());
        subPanel1.setBackground(Color.GRAY);
        subPanel1.add(BorderLayout.CENTER, dropLabel);
        subPanel1.setPreferredSize(new Dimension(500,320));
        new DropTarget(dropLabel, myDragDropListener);
        JPanel subPanel2 = new JPanel(new BorderLayout());
        JButton b = new JButton("Browse");
        b.addActionListener(e -> {
            fc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .srt files", "srt");
            fc.addChoosableFileFilter(restrict);
            Action details = fc.getActionMap().get("viewTypeDetails");
            details.actionPerformed(null);
            int r = fc.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                path = fc.getSelectedFile().getPath();
                dropLabel.setText(path);
            }
        });
        b.setPreferredSize(new Dimension(100,30));
        subPanel2.add(BorderLayout.NORTH, b);
        subPanel2.setBackground(Color.DARK_GRAY);
        subPanel2.setPreferredSize(new Dimension(100,320));
        panel2.setBackground(Color.DARK_GRAY);
        panel2.add(subPanel1, c1);
        c1.gridx = 1;
        c1.gridy = 0;
        panel2.add(subPanel2, c1);

        JButton button = new JButton("Go!");
        button.addActionListener(e -> {
            long milliSeconds = slider.getValue();
            new Thread(new Adjust(path, milliSeconds)).start();
        });

        bigPanel.add(BorderLayout.NORTH, panel1);
        bigPanel.add(BorderLayout.CENTER, panel2);
        bigPanel.add(BorderLayout.SOUTH, button);
    }

    public void initSlider() {
        slider = new JSlider(JSlider.HORIZONTAL,-50000,50000, 0);

        Hashtable<Integer,JLabel> labelTable = new Hashtable<>();
        JLabel l1 = new JLabel("50.0");
        l1.setForeground(Color.LIGHT_GRAY);
        labelTable.put(50000, l1);
        l1 = new JLabel("25.0");
        l1.setForeground(Color.LIGHT_GRAY);
        labelTable.put(25000, l1);
        l1 = new JLabel("0");
        l1.setForeground(Color.LIGHT_GRAY);
        labelTable.put(0, l1);
        l1 = new JLabel("-25.0");
        l1.setForeground(Color.LIGHT_GRAY);
        labelTable.put(-25000, l1);
        l1 = new JLabel("-50.0");
        l1.setForeground(Color.LIGHT_GRAY);
        labelTable.put(-50000, l1);
        slider.setLabelTable( labelTable );

        slider.setPaintTicks(false);
        slider.setPaintTrack(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

        slider.setMajorTickSpacing(1000);
        slider.setMinorTickSpacing(50);
        slider.setPreferredSize(new Dimension(600,50));

        slider.setBackground(Color.DARK_GRAY);
        slider.setForeground(Color.WHITE);

        slider.addChangeListener(e -> {
            double value = slider.getValue();
            value /= 1000;
            text.setText(String.valueOf(value));
        });
    }

    public void initText() {
        text = new JTextField(5) {
            @Override
            public void setBorder(Border border) {
                // NO!
            }
        };
        text.setForeground(Color.WHITE);
        text.setBackground(Color.GRAY);

        text.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String typed = text.getText();
                slider.setValue(0);
                if(!typed.matches("\\d+(\\.\\d+)?") || typed.length() > 5) {
                    return;
                }
                double value = Double.parseDouble(typed);
                value *= 1000;
                int val = (int) value;
                slider.setValue(val);
            }
        });
    }

    public String timeToString(int value) {
        return value + "ms";
    }

    class MyDragDropListener implements DropTargetListener {

        @Override
        public void drop(DropTargetDropEvent event) {

            // Accept copy drops
            event.acceptDrop(DnDConstants.ACTION_COPY);

            // Get the transfer which can provide the dropped item data
            Transferable transferable = event.getTransferable();

            // Get the data formats of the dropped item
            DataFlavor[] flavors = transferable.getTransferDataFlavors();

            // Loop through the flavors
            for (DataFlavor flavor : flavors) {

                try {

                    // If the drop items are files
                    if (flavor.isFlavorJavaFileListType()) {

                        // Get all of the dropped files
                        List files = (List) transferable.getTransferData(flavor);

                        Object file = files.get(files.size()-1);
                        path = ((File) file).getPath();
                        dropLabel.setText(path);
                    }

                } catch (Exception e) {

                    // Print out the error stack
                    e.printStackTrace();

                }
            }

            // Inform that the drop is complete
            event.dropComplete(true);

        }

        @Override
        public void dragEnter(DropTargetDragEvent event) {
        }

        @Override
        public void dragExit(DropTargetEvent event) {
        }

        @Override
        public void dragOver(DropTargetDragEvent event) {
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent event) {
        }

    }
}
