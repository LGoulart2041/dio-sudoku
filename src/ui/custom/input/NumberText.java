package ui.custom.input;

import model.Cell;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static java.awt.Font.PLAIN;

public class NumberText extends JTextField {

    private final Cell cell;

    public NumberText(final Cell cell) {
        this.cell = cell;
        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", PLAIN, 20));
        this.setHorizontalAlignment(CENTER);
        this.setDocument(new NumberTextLimit());
        this.setEnabled(!cell.isFixed());
        if(cell.isFixed()) {
            this.setText(cell.getActual().toString());
        }
        this.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeCell();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeCell();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeCell();
            }

            private void changeCell() {
                if(getText().isEmpty()) {
                    cell.clearCell();
                    return;
                }
                cell.setActual(Integer.parseInt(getText()));
            }
        });
    }

}
