package ui.custom.screen;

import com.sun.tools.javac.Main;
import model.Cell;
import model.ICell;
import service.BoardService;
import ui.custom.button.CheckGameStatusButton;
import ui.custom.button.FinishGameButton;
import ui.custom.button.ResetButton;
import ui.custom.frame.MainFrame;
import ui.custom.input.InputValidator;
import ui.custom.input.NumberText;
import ui.custom.input.SudokuInputValidator;
import ui.custom.panel.MainPanel;
import ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;

    private JButton addShowGameStatusButton;
    private JButton resetGameButton;
    private JButton finishGameButton;
    private JButton checkGameStatusButton;
    private SudokuInputValidator validator;


    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.validator = new SudokuInputValidator();
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r = 0; r < 9; r+=3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3) {
                var endCol = c + 2;
                var cells = getCellsFromSector(boardService.getCells(), c, endCol, r, endRow);
                JPanel sector = generateSection(cells, validator);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addShowGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private List<ICell> getCellsFromSector(List<List<ICell>> cells,
                                           final int initCol, final int endCol,
                                           final int initRow, final int endRow){
        List<ICell> cellSector = new ArrayList<>();
        for(int r = initRow; r <= endRow; r++){
            for(int c = initCol; c <= endCol; c++) {
                cellSector.add(cells.get(c).get(r));
            }
        }

        return cellSector;

    }

    private JPanel generateSection(final List<ICell> cells, SudokuInputValidator validator) {
        List<NumberText> fields = cells.stream()
                .map(cell -> new NumberText(cell, validator))
                .toList();
        return new SudokuSector(fields);
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if(boardService.gameIsFinish()) {
                JOptionPane.showMessageDialog(null, "Parabéns, você concluiu o jogo");
                resetGameButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                var message = "Seu jogo tem alguma inconsistência. Ajuste e tente novamente";
                JOptionPane.showMessageDialog(null, message);
            }
        });
        mainPanel.add(finishGameButton);
    }

    private void addShowGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.checkGameStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetGameButton = new ResetButton(e ->{
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja realmente reiniciar o jogo?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if(dialogResult == 0) {
                boardService.resetGame();
            }
        });
        mainPanel.add(resetGameButton);
    }
}
