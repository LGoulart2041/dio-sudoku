package service;

import model.GameStatusEnum;

public interface IBoardService {
    void resetGame();
    GameStatusEnum checkGameStatus();
    boolean gameIsFinish();
}
