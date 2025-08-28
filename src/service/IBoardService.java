package service;

import model.GameStatusEnum;

public interface IBoardService {
    void resetGame();
    GameStatusEnum checkGameStatus();
    void finishGame();
}
