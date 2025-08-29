package model;

public interface ICell {
    boolean isFixed();
    void clearCell();
    void setActual(final Integer value);
    Integer getActual();
    int getExpected();
}
