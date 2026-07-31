package Puzzle_Game;


import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.net.URL;

public class GameJFrame extends JFrame {
    int IconWidth;
    int IconHeight;
    int scaledWidth;
    int scaledHeight;
    int blank_Row = 4;
    int blank_Col = 4;
    int count = 0;
    JLabel countLabel2;
    HashMap<Integer, JLabel>map = new HashMap<>();
    Point[][] Point_copy_of_labelarray = new Point[5][5];
    JLabel completeImageLabel;
    JLabel vicLabel;


    JLabel[][] labelarray = new JLabel[5][5];
    int[][] numarr = {
            {0, 0, 0, 0, 0},
            {0, 1, 2, 3, 4},
            {0, 5, 6, 7, 8},
            {0, 9, 10, 11, 12},
            {0, 13, 14, 15, 16}
    };

    final int[][] tararr = {
            {0, 0, 0, 0, 0},
            {0, 1, 2, 3, 4},
            {0, 5, 6, 7, 8},
            {0, 9, 10, 11, 12},
            {0, 13, 14, 15, 16}
    };




    public GameJFrame(){
        getImageSize(0.8);

        initJFrame();

        initJMenuBar();

        this.setVisible(true);

        initImage(0.8);

        initButton();
    }

    private void initButton(){
        JButton bt1 = new JButton("蠢人按钮");
        ButtonModel bm1 = bt1.getModel();
        bt1.setUI(new BasicButtonUI());
        bt1.setOpaque(true);
        bt1.setContentAreaFilled(true);
        bt1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                    bt1.setBounds(20, 20, 80, 80);
                    bt1.setBackground(Color.RED);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                bt1.setBounds(0, 0, 120, 120);
                bt1.setBackground(Color.LIGHT_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e){
                bt1.setBounds(0, 0, 120, 120);
                bt1.setBackground(Color.LIGHT_GRAY);
            }
        });
        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameJFrame.this.completeImageLabel.setVisible(true);
                winFunc2();
                System.out.println("作弊按钮被点击");
            }
        });
        bt1.setBounds(0, 0, 120, 120);
        this.add(bt1);
        this.getContentPane().setComponentZOrder(bt1, 0);
    }

    private void initImage(double factor){
        ImageIcon BGImage = loadImage("BGPhoto(2).jpg");

        Image originalImage = BGImage.getImage();

        Image scaledImage = originalImage.getScaledInstance(
                4*scaledWidth+112,
                4*scaledHeight+112,
                Image.SCALE_SMOOTH
        );

        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel BGLabel = new JLabel(scaledIcon);
        int startHeight = (this.getContentPane().getHeight()-4*scaledHeight-12)/2;
        int startWidth = (this.getContentPane().getWidth()-4*scaledWidth-12)/2;
        BGLabel.setBounds(startWidth-50, startHeight-50, scaledIcon.getIconWidth(), scaledIcon.getIconHeight());

        initCompleteImage(startWidth, startHeight);//初始化完整图片
        completeImageLabel.setVisible(false);//默认隐藏图片

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel targetLabel = (JLabel) e.getComponent();
                for(int i = 1; i<=4; i++){
                    for(int j = 1; j<=4; j++){
                        if((Math.abs(i-blank_Row)+Math.abs(j-blank_Col)) == 1){
                            if(targetLabel == labelarray[i][j]){
                                count++;
                                countLabel2.setText(String.valueOf(count));
                                int tem = numarr[i][j];
                                numarr[i][j] = 16;
                                numarr[blank_Row][blank_Col] = tem;
                                JLabel temp = labelarray[i][j];
                                labelarray[i][j] = labelarray[blank_Row][blank_Col];
                                labelarray[blank_Row][blank_Col] = temp;
                                Point old_Blank_pos = labelarray[i][j].getLocation();
                                labelarray[i][j].setLocation(labelarray[blank_Row][blank_Col].getLocation());
                                labelarray[blank_Row][blank_Col].setLocation(old_Blank_pos);
                                blank_Row = i;
                                blank_Col = j;
                                for(int m = 1; m<=4; m++){
                                    for(int n = 1; n<=4; n++){
                                        if(numarr[m][n] != tararr[m][n]){
                                            return;
                                        }
                                    }
                                }
                                winFunc3();//胜利结算
                                return;
                            }//更新成员变量中的label数组
                        }

                    }
                }
            }
        };

        for(int i = 1; i<=4; i++){
            for(int j = 1; j<=4; j++){
                ImageIcon scaledicon = scaleImages("crop_"+((4*(i-1))+j)+".png", factor);
                JLabel label = new JLabel(scaledicon);
                label.setBounds((j-1)*(scaledWidth+4)+startWidth, (i-1)*(scaledHeight+4)+startHeight, scaledWidth, scaledHeight);
                label.setBorder(new BevelBorder(BevelBorder.RAISED));
                labelarray[i][j] = label;
                label.addMouseListener(mouseAdapter);
                map.put((4*(i-1))+j, label);
                this.getContentPane().add(label);
            }
        }
        JLabel blanklabel = new JLabel();
        blanklabel.setBounds(3*(scaledWidth+4)+startWidth, 3*(scaledHeight+4)+startHeight, scaledWidth, scaledHeight);
        this.getContentPane().add(blanklabel);
        this.getContentPane().remove(labelarray[4][4]);
        map.put(16, blanklabel);
        labelarray[4][4] = blanklabel;
        int [] standardarr = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
        };
        int [][] randomizedarr;
        do {
            randomizedarr = randomize(standardarr);
        } while (!isSolvable(randomizedarr));

        for(int i = 1; i<=4; i++){
            for(int j = 1; j<=4; j++){
                Point_copy_of_labelarray[i][j] = labelarray[i][j].getLocation();
            }
        }
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4;j++){
                Point pt = Point_copy_of_labelarray[i+1][j+1];
                labelarray[i+1][j+1] = map.get(randomizedarr[i][j]);
                labelarray[i+1][j+1].setLocation(pt);
                if(randomizedarr[i][j] == 16){
                    blank_Row = i+1;
                    blank_Col = j+1;
                }
            }
        }



        for(int i = 1; i<=4; i++){
            for(int j = 1; j<=4; j++){
                numarr[i][j] = randomizedarr[i-1][j-1];
            }
        }//更新胜利判断数组numarr

        this.getContentPane().add(BGLabel);


        // 明确将背景放到最底层
        this.getContentPane().setComponentZOrder(
                BGLabel,
                this.getContentPane().getComponentCount() - 1
        );
        this.revalidate();
        this.repaint();
    }

    private void initCompleteImage(int startWidth, int startHeight){
        ImageIcon CompleteIcon = scaleImages("CompletePhoto.jpg", 0.8);
        completeImageLabel = new JLabel(CompleteIcon);
        completeImageLabel.setBounds(startWidth, startHeight, 4*scaledWidth+12, 4*scaledHeight+12);
        this.getContentPane().add(completeImageLabel);
        this.getContentPane().setComponentZOrder(completeImageLabel, 0);
    }

    private void getImageSize(double factor){
        ImageIcon SampleImage = loadImage("crop_1.png");
        IconWidth = SampleImage.getIconWidth();
        IconHeight = SampleImage.getIconHeight();
        scaledWidth = (int)(IconWidth*factor);
        scaledHeight = (int)(IconHeight*factor);
    }

    private void initJMenuBar() {
        //初始化菜单
        JMenuBar menuBar = new JMenuBar();

        //次一级条目对象
        JMenu funcMenu = new JMenu("功能");
        JMenu aboutMenu = new JMenu("关于");

        //单独的功能
        JMenuItem replayItem = new JMenuItem("重新游戏");
        JMenuItem reloginItem = new JMenuItem("重新登录");
        JMenuItem closeItem = new JMenuItem("关闭游戏");

        JMenuItem accountItem = new JMenuItem("公众号");

        //添加到相应的条目对象
        funcMenu.add(replayItem);
        funcMenu.add(reloginItem);
        funcMenu.add(closeItem);

        aboutMenu.add(accountItem);

        //添加到总菜单栏
        menuBar.add(funcMenu);
        menuBar.add(aboutMenu);

        //将总菜单栏放置于窗口中
        this.setJMenuBar(menuBar);
    }

    private void initJFrame() {
        //外框架属性设置
        this.setSize(4*scaledWidth+200, 4*scaledHeight+400);
        this.setTitle("拼图单机版");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        JLabel countLabel1 = new JLabel("步数: ");
        countLabel2 = new JLabel(String.valueOf(count));
        int countAreaX = (getWidth() - 120) / 2;
        int countAreaY = 40;

        countLabel1.setBounds(
                countAreaX,
                countAreaY,
                70,
                35
        );

        countLabel2.setBounds(
                countAreaX + 70,
                countAreaY,
                50,
                35
        );

        countLabel1.setFont(new Font("微软雅黑", Font.BOLD, 22));
        countLabel2.setFont(new Font("微软雅黑", Font.BOLD, 22));

        this.add(countLabel1);
        this.add(countLabel2);

        // 获取根面板的输入映射
        InputMap inputMap = this.getRootPane().getInputMap(
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // 将 Esc 键和名为 "close" 的动作绑定
        inputMap.put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "close"
        );

        // 获取根面板的动作映射
        ActionMap actionMap = this.getRootPane().getActionMap();

        // 设置 "close" 动作具体需要执行的代码
        actionMap.put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameJFrame.this.dispose();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false),
                "Show_Complete_Image"
        );

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true),
                "Hide_Complete_Image"
        );

        actionMap.put("Show_Complete_Image", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameJFrame.this.completeImageLabel.setVisible(true);
            }
        });

        actionMap.put("Hide_Complete_Image", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameJFrame.this.completeImageLabel.setVisible(false);
            }
        });
    }
    private void winFunc2(){
        vicLabel = new JLabel("懦夫，你\"赢\"了");
        winFunc();
    }

    private void winFunc3(){
        vicLabel = new JLabel("游戏胜利");
        winFunc();
    }

    private void winFunc(){

        // 拼图区域总尺寸
        int puzzleWidth = 4 * scaledWidth + 12;
        int puzzleHeight = 4 * scaledHeight + 12;

        // 重新计算拼图左上角坐标
        int startWidth =
                (getContentPane().getWidth() - puzzleWidth) / 2;

        int startHeight =
                (getContentPane().getHeight() - puzzleHeight) / 2;

        // 胜利提示框尺寸
        int vicWidth = (int) (puzzleWidth * 0.6);
        int vicHeight = (int) (puzzleHeight * 0.25);

        // 让提示框位于拼图正中间
        int vicX =
                startWidth + (puzzleWidth - vicWidth) / 2;

        int vicY =
                startHeight + (puzzleHeight - vicHeight) / 2;

        vicLabel.setBounds(
                vicX,
                vicY,
                vicWidth,
                vicHeight
        );

        vicLabel.setHorizontalAlignment(SwingConstants.CENTER);
        vicLabel.setVerticalAlignment(SwingConstants.CENTER);

        vicLabel.setOpaque(true);
        vicLabel.setBackground(new Color(255, 255, 255, 230));
        vicLabel.setForeground(Color.RED);
        vicLabel.setFont(
                new Font("微软雅黑", Font.BOLD, 28)
        );

        vicLabel.setBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 3)
        );

        getContentPane().add(vicLabel);

        // 保证胜利提示显示在最上面
        getContentPane().setComponentZOrder(vicLabel, 0);

        revalidate();
        repaint();
    }

    public int[][] randomize(int arr[]){
        int [][] returnarr = new int[4][4];
        boolean [][] occupationarr = new boolean[4][4];
        double pos;
        int realpos;
        for(int i = 0; i<16; i++){
            do{pos = Math.random();
                realpos = (int)(16*pos);
            } while(occupationarr[realpos/4][realpos%4] == true);
            returnarr[realpos/4][realpos%4] = arr[i];
            occupationarr[realpos/4][realpos%4] = true;
        }
        return returnarr;
    }

    public ImageIcon scaleImages(String filepath, double multiplying_factor){
        ImageIcon originalIcon = loadImage(filepath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                (int)(multiplying_factor*originalIcon.getIconWidth()),
                (int)(multiplying_factor*originalIcon.getIconHeight()),
                Image.SCALE_SMOOTH
        );
        return new ImageIcon(scaledImage);
    }

    private boolean isSolvable(int[][] arr) {
        int[] numbers = new int[15];
        int index = 0;
        int blankRowFromBottom = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (arr[i][j] == 16) {
                    blankRowFromBottom = 4 - i;
                } else {
                    numbers[index] = arr[i][j];
                    index++;
                }
            }
        }

        int inversionCount = 0;

        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] > numbers[j]) {
                    inversionCount++;
                }
            }
        }

        return (inversionCount + blankRowFromBottom) % 2 == 1;
    }

    private ImageIcon loadImage(String fileName) {
        URL imageUrl = GameJFrame.class.getResource(
                "/Puzzle_Game/Photos/" + fileName
        );

        if (imageUrl == null) {
            throw new IllegalArgumentException(
                    "找不到图片：" + fileName
            );
        }

        return new ImageIcon(imageUrl);
    }
}
