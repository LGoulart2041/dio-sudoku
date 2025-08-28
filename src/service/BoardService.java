package service;

import model.Board;
import model.Cell;
import model.GameStatusEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardService implements IBoardService{

    public final static int BOARD_LIMIT = 9;

    private final Board board;

    public BoardService(final Map<String, String> gameConfig) {
        this.board = new Board(initBoard(gameConfig));
    }

    public List<List<Cell>> getCells() {
        return board.getCells();
    }

    public boolean hasErrors() {
        return board.hasErrors();
    }

    private List<List<Cell>> initBoard(final Map<String, String> gameConfig) {
        List<List<Cell>> cells = new ArrayList<>();
        for(int i = 0; i < BOARD_LIMIT; i++) {
            cells.add(new ArrayList<>());
            for(int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = gameConfig.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentCell = new Cell(expected, fixed);
                cells.get(i).add(currentCell);
            }
        }

        return cells;
    }

    @Override
    public void resetGame() {
        board.resetGame();
    }

    @Override
    public GameStatusEnum checkGameStatus() {
        return board.checkGameStatus();
    }

    @Override
    public void finishGame() {
        board.finishGame();
    }
}
