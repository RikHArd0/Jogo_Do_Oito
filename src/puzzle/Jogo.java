package puzzle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Criação do jogo dos 8 com Java e Swing

public class Jogo extends JPanel {
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

    public Jogo(int size, int dim, int mar) {
        this.size = size;
        dimension = dim;
        margin = mar;


        nbTiles = size * size - 1; // -1 porque não se conta o espaço vazio
        tiles = new int[size * size];

        // Calcula o tamanho do Grid e das Peças
        gridSize = (dim - 2 * margin);
        tileSize = gridSize / size;

        setPreferredSize(new Dimension(dimension, dimension + margin));
        setBackground(Color.WHITE);
        setForeground(FOREGROUND_COLOR);
        setFont(new Font("SansSerif", Font.BOLD, 60));

        gameOver = true;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Usado para permitir interação dos usuários através de cliques

                if (gameOver) {
                    newGame();
                } else {

                    int ex = e.getX() - margin;
                    int ey = e.getY() - margin;


                    if (ex < 0 || ex > gridSize || ey < 0 || ey > gridSize)
                        return;

                    // Posicionar as peças no Grid
                    int c1 = ex / tileSize;
                    int r1 = ey / tileSize;

                    // Posicionar o espaço vazio
                    int c2 = blankPos % size;
                    int r2 = blankPos / size;


                    int clickPos = r1 * size + c1;

                    int dir = 0;

                    // Movimentar várias peças de uma vez
                    if (c1 == c2 && Math.abs(r1 - r2) > 0)
                        dir = (r1 - r2) > 0 ? size : -size;
                    else if (r1 == r2 && Math.abs(c1 - c2) > 0)
                        dir = (c1 - c2) > 0 ? 1 : -1;

                    if (dir != 0) {
                        // Mover peças
                        do {
                            int newBlankPos = blankPos + dir;
                            tiles[blankPos] = tiles[newBlankPos];
                            blankPos = newBlankPos;
                        } while (blankPos != clickPos);

                        tiles[blankPos] = 0;
                    }

                    // Checar se o jogo está resolvido
                    gameOver = isSolved();
                }

                //
                repaint();
            }
        });

        newGame();
    }

    private void newGame() {
        do {
            reset(); // Resetar para o estado inicial
            shuffle(); // Embaralha as peças
        } while (!isSolvable()); //

        gameOver = false;
    }
    private void reset() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
        }

        blankPos = tiles.length - 1;
    }

    private void shuffle() {
        int n = nbTiles;
        while (n > 1) {
            int r = RANDOM.nextInt(n--);
            int tmp = tiles[r];
            tiles[r] = tiles[n];
            tiles[n] = tmp;
        }
    }
    private boolean isSolvable() {
        int countInversions = 0;
        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i])
                    countInversions++;
            }
        }

        return countInversions % 2 == 0;
    }

    private boolean isSolved() {
        if (tiles[tiles.length - 1] != 0) // Se o espaço em branco não estiver na posição de resolvido ==> não resolvido
            return false;

        for (int i = nbTiles - 1; i >= 0; i--) {
            if (tiles[i] != i + 1)
                return false;
        }

        return true;
    }

    private void drawGrid(Graphics2D g) {
        for (int i = 0; i < tiles.length; i++) {
            //Converter coordenadas 1D em coordenadas 2D visto o tamanho do Array 2D
            int r = i / size;
            int c = i % size;
            int x = margin + c * tileSize;
            int y = margin + r * tileSize;

            if (tiles[i] == 0) {
                if (gameOver) {
                    g.setColor(FOREGROUND_COLOR);
                    drawCenteredString(g, "\u2713", x, y);
                }

                continue;
            }

            g.setColor(getForeground());
            g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.WHITE);

            drawCenteredString(g, String.valueOf(tiles[i]), x, y);
        }
    }
    private void drawStartMessage(Graphics2D g) {
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(FOREGROUND_COLOR);
            String s = "Clique para começar um Novo Jogo";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - margin);
        }
    }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        // Centraliza a string S para o bloco escolhido (x,y)
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s, x + (tileSize - fm.stringWidth(s)) / 2,
                y + (asc + (tileSize - (asc + desc)) / 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2D);
        drawStartMessage(g2D);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Jogo do 8");
            frame.setResizable(false);
            frame.add(new Jogo(3, 550, 30), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
