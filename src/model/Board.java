package model;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Cell>> cells;

    public Board(List<List<Cell>> cells) {
        this.cells = cells;
    }

    public List<List<Cell>> getSpaces() {
        return cells;
    }

    public GameStatusEnum getStatus() {
        if(cells.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return GameStatusEnum.NON_STARTED;
        }

        return cells.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? GameStatusEnum.INCOMPLETE : GameStatusEnum.COMPLETE;
    }

    public boolean hasErrors() {
        if(getStatus() == GameStatusEnum.NON_STARTED){
            return false;
        }

        return cells.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final Integer value) {
        var space = cells.get(col).get(row);
        if(space.isFixed()){
            return false;
        }

        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row) {
        var cell = cells.get(col).get(row);
        if(cell.isFixed()){
            return false;
        }

        cell.clearCell();
        return true;
    }

    public void reset() {
        cells.forEach(c -> c.forEach(Cell::clearCell));
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus() == GameStatusEnum.COMPLETE;
    }
}
