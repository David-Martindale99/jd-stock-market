package jdstockmarket;

import javax.swing.*;
import java.awt.*;

public class InstructionDialog extends JDialog {

    private static final long serialVersionUID = 1L;

	public InstructionDialog(JFrame parentFrame) {
        super(parentFrame, "Instructions", true); // 'true' for modal dialog
        initComponents();
    }

    private void initComponents() {
        // Set the size and layout of the dialog
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Create a text area for instructions
        JTextArea instructionText = new JTextArea();
        instructionText.setText("Instructions and tips:\n\n" +
                                "1. Enter a known stock ticker to fetch information.\n" +
                                "2. Add stocks you like to your portfolio with 'Add Stock'.\n" +
                                "3. As of currently, this applicaiton has a limit of 25 API stock calls\n    per 24 hour period. This limitation does not extend to\n    congressinal stock data in the bottom display\n" +
                                "4. The 'Live Portfolio' button updates current stock data for all\n    stocks in your portolio, if turned on when the 'Display Portfolio'\n    button is clicked\n" +
                                "5. Nancy Pelosi ? Try AAPL" +
                                "\n" + 
                                "\n\nOne of the API's cost me $10...pls give me an A :(");
        instructionText.setEditable(false);
        instructionText.setWrapStyleWord(true);
        instructionText.setLineWrap(true);

        // Add a scroll pane to the text area
        JScrollPane scrollPane = new JScrollPane(instructionText);
        add(scrollPane, BorderLayout.CENTER);

        // Prevent window from being resized
        setResizable(false);

        // Set the dialog location relative to the parent frame
        setLocationRelativeTo(getParent());
    }
}

