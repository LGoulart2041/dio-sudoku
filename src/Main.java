import model.Board;
import model.Cell;
import util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static util.BoardTemplate.BOARD_TEMPLATE;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    public final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {

        final var positions = Stream.of(args)
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
        var option = -1;
        while (true) {
            System.out.println("Selecione uma das opções a seguir:");
            System.out.println("1 - Iniciar novo jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar o jogo");
            System.out.println("7 - Finalizar o jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das posições do menu");
            }
        }

    }

    private static void startGame(final Map<String, String> positions) {
        if(nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }

        List<List<Cell>> cells = new ArrayList<>();
        for(int i = 0; i < BOARD_LIMIT; i++) {
            cells.add(new ArrayList<>());
            for(int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = positions.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentCell = new Cell(expected, fixed);
                cells.get(i).add(currentCell);
            }
        }

        board = new Board(cells);
        System.out.println("O jogo está pronto para começar");
    }

    private static void inputNumber() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna que o número será inserido");
        var col =  runUnitlGetValidNumber(0, 8);

        System.out.println("Informe a linha que o número será inserido");
        var row = runUnitlGetValidNumber(0, 8);

        System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);
        var value = runUnitlGetValidNumber(1, 9);
        if(!board.changeValue(col, row, value)){
            System.out.printf("A posição[%s, %s] tem um valor fixo\n", col, row);
        }
    }

    private static void removeNumber() {
        if(isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna que o número será inserido");
        var col =  runUnitlGetValidNumber(0, 8);

        System.out.println("Informe a linha que o número será inserido");
        var row = runUnitlGetValidNumber(0, 8);

        if(!board.clearValue(col, row)){
            System.out.printf("A posição[%s, %s] tem um valor fixo\n", col, row);
        }
    }

    private static void showCurrentGame() {
        if(isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for(int i = 0; i < BOARD_LIMIT; i++) {
            for(var col : board.getCells()) {
                args[argPos++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }

        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BOARD_TEMPLATE) + "%n", args);
    }

    private static void showGameStatus() {
        if(isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus().getLabel());

        if(board.hasErrors()) {
            System.out.println("O jogo contém erros");
        } else {
            System.out.println("O jogo não contém erros.");
        }
    }

    private static void clearGame() {
        if(isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Tem certeza que deseja limpar seu jogo e perder todo seu progresso?");
        var confirm = scanner.next();
        while(!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")){
            System.out.println("Informe 'sim' ou 'não'");
            confirm = scanner.next();
        }

        if(confirm.equalsIgnoreCase("sim")) board.reset();
    }

    private static void finishGame() {
        if(isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        if(board.gameIsFinished()) {
            System.out.println("Parabéns você concluiu o jogo");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros, verifique o board e ajuste-o");
        } else {
            System.out.println("Você ainda precisa preencher algum espaço.");
        }
    }

    private static int runUnitlGetValidNumber(final int min, final int max) {
        var current = scanner.nextInt();
        while(current < min || current > max) {
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }

        return  current;
    }
}