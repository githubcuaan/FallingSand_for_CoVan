package fallingsand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChucmungFrame extends JFrame {
    private String message = "Em An mafia internet nhân ngày 20-11 chúc cô Vân sức khỏe, bình an.";
    private String message2 = "Chúc cô 8386 thành công trong công việc nhé! Cô Vân mãi Đỉnh!";
    private StringBuilder displayedMessage = new StringBuilder();
    private int index = 0;
    private StringBuilder displayedMessage2 = new StringBuilder();
    private int index2 = 0;

    public ChucmungFrame() {
        setTitle("ChucmungFrame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Timer timer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < message.length()) {
                    displayedMessage.append(message.charAt(index));
                    index++;
                } else if (index2 < message2.length()) {
                    displayedMessage2.append(message2.charAt(index2));
                    index2++;
                } else {
                    ((Timer)e.getSource()).stop();
                }
                repaint();
            }
        });
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(new Font("Arial", Font.PLAIN, 48));
        FontMetrics fm = g.getFontMetrics();
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent() - 125;
        
        String[] words = displayedMessage.toString().split(" ");
        StringBuilder line = new StringBuilder();
        int lineHeight = fm.getHeight();
        int currentY = y;

        for (String word : words) {
            if (fm.stringWidth(line + word) < getWidth()) {
                line.append(word).append(" ");
            } else {
                int x = (getWidth() - fm.stringWidth(line.toString())) / 2;
                g.drawString(line.toString(), x, currentY);
                line = new StringBuilder(word + " ");
                currentY += lineHeight;
            }
        }
        int x = (getWidth() - fm.stringWidth(line.toString())) / 2;
        g.drawString(line.toString(), x, currentY);
        
        if (index >= message.length()) {
            currentY += lineHeight + 20;
            words = displayedMessage2.toString().split(" ");
            line = new StringBuilder();

            for (String word : words) {
                if (fm.stringWidth(line + word) < getWidth()) {
                    line.append(word).append(" ");
                } else {
                    int x2 = (getWidth() - fm.stringWidth(line.toString())) / 2;
                    g.drawString(line.toString(), x2, currentY);
                    line = new StringBuilder(word + " ");
                    currentY += lineHeight;
                }
            }
            int x2 = (getWidth() - fm.stringWidth(line.toString())) / 2;
            g.drawString(line.toString(), x2, currentY);
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            ChucmungFrame frame = new ChucmungFrame();
//            frame.setVisible(true);
//        });
//    }
}
