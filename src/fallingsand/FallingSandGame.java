package fallingsand;

/**
 *
 * @author DinhAn
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FallingSandGame extends JPanel implements MouseMotionListener {
    private static final int WIDTH = 800;  // Chiều rộng của cửa sổ
    private static final int HEIGHT = 600; // Chiều cao của cửa sổ
    private static final int CELL_SIZE = 5; // Kích thước của mỗi ô (pixel)
    private boolean[][] sandGrid; // Lưới boolean lưu trạng thái hạt cát (true = có cát, false = trống)
    private float hue = 360;
    private Color[][] sandColors; // Mảng để lưu trữ màu sắc của từng hạt cát
    private JButton button;

    public FallingSandGame() {
        // Khởi tạo lưới cát với kích thước dựa trên chiều rộng và chiều cao
        this.sandGrid = new boolean[WIDTH / CELL_SIZE][HEIGHT / CELL_SIZE];
        this.sandColors = new Color[WIDTH / CELL_SIZE][HEIGHT / CELL_SIZE]; // Khởi tạo mảng màu sắc
        
        // Thêm trình nghe sự kiện để nhận tương tác chuột
        this.addMouseMotionListener(this);

        // Thiết lập kích thước cửa sổ
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //tạo nút
        button = new JButton("Click me!");
        button.setVisible(false);
        button.setBounds((WIDTH - 100)/2, (HEIGHT - 50)/2, 100, 50);
        this.setLayout(null);

        //thêm chưsc năng mở cho nút
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChucmungFrame().setVisible(true);
            }
        });

        this.add(button);

        // Tạo bộ đếm thời gian để cập nhật trạng thái trò chơi sau mỗi 30ms
        Timer timer = new Timer(10, e -> update());
        timer.start();
    }

    // Hàm cập nhật trạng thái hạt cát
    private void update() {
        // Cập nhật giá trị hue
        hue += 0.5; // Tăng hue sau mỗi lần update
        if (hue >= 360) {
            hue = 0; // Đặt lại hue về 0 nếu đạt 360
        }

        // Duyệt qua lưới từ dưới lên để xử lý hạt cát
        for (int x = 0; x < sandGrid.length; x++) {
            for (int y = sandGrid[0].length - 2; y >= 0; y--) {
                // Nếu có cát ở vị trí hiện tại và ô bên dưới trống, thì di chuyển cát xuống dưới
                if (sandGrid[x][y] && !sandGrid[x][y + 1]) {
                    sandGrid[x][y] = false; // Xóa cát ở vị trí hiện tại
                    sandGrid[x][y + 1] = true; // Thêm cát vào ô bên dưới
                    sandColors[x][y + 1] = Color.getHSBColor(hue / 360, 1.0f, 1.0f); // Cập nhật màu cho hạt cát mới rơi
                }
                // Nếu ô bên dưới là 1, kiểm tra ô bên trái và bên phải
                else if (sandGrid[x][y] && sandGrid[x][y + 1]) {
                    if (x > 0 && !sandGrid[x - 1][y + 1]) { // Kiểm tra ô bên trái
                        sandGrid[x][y] = false; // Xóa cát ở vị trí hiện tại
                        sandGrid[x - 1][y + 1] = true; // Di chuyển cát sang trái
                        sandColors[x - 1][y + 1] = Color.getHSBColor(hue / 360, 1.0f, 1.0f); // Cập nhật màu cho hạt cát mới rơi
                    } else if (x < sandGrid.length - 1 && !sandGrid[x + 1][y + 1]) { // Kiểm tra ô bên phải
                        sandGrid[x][y] = false; // Xóa cát ở vị trí hiện tại
                        sandGrid[x + 1][y + 1] = true; // Di chuyển cát sang phải
                        sandColors[x + 1][y + 1] = Color.getHSBColor(hue / 360, 1.0f, 1.0f); // Cập nhật màu cho hạt cát mới rơi
                    }
                }
            }
        }
        // Yêu cầu vẽ lại giao diện
        repaint();
    }

    // Phương thức vẽ lưới và các hạt cát
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ nền đen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Vẽ chữ ở giữa màn hình
        String text = "Cô Vân ơi! thả cát che dòng chữ này đii";
        g.setColor(Color.WHITE); // Đặt màu chữ là trắng
        FontMetrics metrics = g.getFontMetrics(); // Lấy thông tin về font
        int a = (WIDTH - metrics.stringWidth(text)) / 2; // Tính toán tọa độ x để căn giữa
        int b = (HEIGHT - metrics.getHeight()) / 2 + metrics.getAscent(); // Tính toán tọa độ y để căn giữa
        g.drawString(text, a, b); // Vẽ chữ tại tọa độ (a, b)

        // Vẽ hạt cát với màu sắc đã lưu
        for (int x = 0; x < sandGrid.length; x++) {
            for (int y = 0; y < sandGrid[0].length; y++) {
                if (sandGrid[x][y]) {
                    g.setColor(sandColors[x][y]); // Sử dụng màu sắc đã lưu
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Vẽ hạt cát
                }
            }
        }

        // Kiểm tra chiều cao của cát đã rơi và hiển thị nút nếu cần
        boolean isSandAboveText = false;
        for (int xPos = 0; xPos < sandGrid.length; xPos++) {
            // Kiểm tra tất cả các ô bên dưới dòng chữ
            for (int checkY = b / CELL_SIZE; checkY < sandGrid[0].length; checkY++) {
                if (sandGrid[xPos][checkY]) {
                    isSandAboveText = true; // Có khối cát liên tục
                } else {
                    // Nếu gặp ô trống, dừng kiểm tra
                    break;
                }
            }
            if (isSandAboveText) break; // Thoát khỏi vòng lặp nếu đã tìm thấy cát
        }

        // Hiển thị nút nếu có cát đã rơi ở trên dòng chữ
        button.setVisible(isSandAboveText);
    }

    // Xử lý sự kiện kéo chuột để thêm hạt cát vào lưới
    @Override
    public void mouseDragged(MouseEvent e) {
        // Tính tọa độ của ô dựa trên vị trí chuột
        int x = e.getX() / CELL_SIZE;
        int y = e.getY() / CELL_SIZE;

        // Kiểm tra tọa độ nằm trong lưới và thêm hạt cát
        if (x >= 0 && x < sandGrid.length && y >= 0 && y < sandGrid[0].length) {
            for (int dx = 0; dx < 3; dx++) {
                for (int dy = 0; dy < 3; dy++) {
                    int newX = x + dx;
                    int newY = y + dy;
                    // Kiểm tra xem newX và newY có nằm trong giới hạn không
                    if (newX >= 0 && newX < sandGrid.length && newY >= 0 && newY < sandGrid[0].length) {
                        if (Math.random() < 0.7) {
                            sandGrid[newX][newY] = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Không làm gì khi chuột di chuyển mà không kéo
    }

    // Điểm khởi chạy chương trình
    public static void main(String[] args) {
        JFrame frame = new JFrame("Falling Sand Game"); // Tạo cửa sổ
        FallingSandGame game = new FallingSandGame();   // Tạo đối tượng trò chơi
        frame.add(game); // Thêm trò chơi vào cửa sổ
        frame.pack(); // Tự động điều chỉnh kích thước cửa sổ
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng cửa sổ khi bấm nút đóng
        frame.setVisible(true); // Hiển thị cửa sổ
    }
}
