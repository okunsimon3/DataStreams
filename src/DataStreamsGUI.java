import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {
    private final JTextArea originalTA;
    private final JTextArea filteredTA;
    private final JTextField searchTextField;

    public DataStreamsGUI() {
        setTitle("Data Streams");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        originalTA = new JTextArea();
        filteredTA = new JTextArea();
        searchTextField = new JTextField("",20);


        add(new JScrollPane(originalTA), BorderLayout.WEST);
        add(new JScrollPane(filteredTA), BorderLayout.EAST);

        JPanel controlPnl = new JPanel(new FlowLayout());
        controlPnl.add(new JLabel("Search String: "));
        controlPnl.add(searchTextField);

        JButton loadBtn = new JButton("Load File");
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });
        controlPnl.add(loadBtn);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFile();
            }
        });
        controlPnl.add(searchBtn);

        JButton quitBtn = new JButton("Quit");
        quitBtn.addActionListener((ActionEvent ae) ->  {
            int confirmed = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to quit?", "Quit Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        controlPnl.add(quitBtn);

        add(controlPnl, BorderLayout.SOUTH);



    }
    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            Path filePath = fileChooser.getSelectedFile().toPath();
            try {
                Stream<String> lines = Files.lines(filePath);
                lines.forEach(line -> originalTA.append(line + "\n"));
                lines.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,"Error loading file: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    private void searchFile() {
        String searchString = searchTextField.getText().trim();

        if(!searchString.isEmpty()) {
            filteredTA.setText("");

            originalTA.getText().lines()
                    .filter(line -> line.contains(searchString))
                    .forEach(line -> filteredTA.append(line + "\n"));
        }
    }

}
