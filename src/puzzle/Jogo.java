package puzzle;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Jogo extends JFrame {
    // Tamanho da instacia do jogo
    private int size;
    // Número de camadas
    private int nbTiles;
    // Grid UI
    private int dimension;
    // Cor do Foreground
    private static final Color FOREGROUND_COLOR = new Color(0, 100, 55);
    // Objetos aleatórios para embaralhar
    private static final Random RANDOM = new Random();
    // Armazenando as peças em Array 1D
    private int[] tiles;
    // Tamanho das peças
    private int tileSize;
    // Indica a posição do espaço vazio
    private int blankPos;
    // Margem do grid no quadro onde as peças ficarão
    private int margin;
    // Tamanho do Grid
    private int gridSize;
    // Verdadeiro para Fim de Jogo e Falso para outro resultado
    private boolean gameOver;

}
