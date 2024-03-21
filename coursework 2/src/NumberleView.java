import javax.swing.*;

public class NumberleView extends JFrame{
    // 图片标签
    public JLabel imageLabel0, imageLabel1, imageLabel2, imageLabel3, imageLabel4, imageLabel5, imageLabel6, imageLabel7, imageLabel8, imageLabel9;
    public JLabel imageLabelPlus, imageLabelMinus, imageLabelMultiply, imageLabelDivision;
    public JLabel imageLabelDelete, imageLabelEqual, imageLabelEnter;

    // 图标对象
    public ImageIcon icon0, icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8, icon9;
    public ImageIcon iconPlus, iconMinus, iconMultiply, iconDivision;
    public ImageIcon iconDelete, iconEqual, iconEnter;
    public ImageIcon iconEmpty;
    public JButton startNewGameButton; // 开始新游戏的按钮对象
    public JLabel[][] emptyLabels;//这个二维数组用来存放输入的图标

    // 添加一个常量表示图标的颜色状态
    public final int ICON_COLOR_GREEN = 1;//常量1表示绿色
    public final int ICON_COLOR_ORANGE = 2;//常量2表示橙色
    public final int ICON_COLOR_GREY = 3;//常量3表示灰色

    private NumberleController controller; // 添加 NumberController 的实例变量

    /*// 在构造函数中初始化 NumberController 实例
    public NumberleView() {
        controller = new NumberleController();
    }*/
    //初始化JFrame界面
    public void initializeJFrame() {
        //设置界面的宽高
        this.setSize(1050, 950);
        //设置界面标题
        this.setTitle("Numberle");
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //取消默认的居中放置
        this.setLayout(null);
    }

    // 初始化图标对象
    public void initializeIcons() {
        icon0 = new ImageIcon("image/default/0.png");
        icon1 = new ImageIcon("image/default/1.png");
        icon2 = new ImageIcon("image/default/2.png");
        icon3 = new ImageIcon("image/default/3.png");
        icon4 = new ImageIcon("image/default/4.png");
        icon5 = new ImageIcon("image/default/5.png");
        icon6 = new ImageIcon("image/default/6.png");
        icon7 = new ImageIcon("image/default/7.png");
        icon8 = new ImageIcon("image/default/8.png");
        icon9 = new ImageIcon("image/default/9.png");
        iconPlus = new ImageIcon("image/default/plus.png");
        iconMinus = new ImageIcon("image/default/minus.png");
        iconMultiply = new ImageIcon("image/default/multiply.png");
        iconDivision = new ImageIcon("image/default/division.png");
        iconDelete = new ImageIcon("image/default/delete.png");
        iconEqual = new ImageIcon("image/default/equal.png");
        iconEnter = new ImageIcon("image/default/enter.png");
        iconEmpty = new ImageIcon("image/Empty.png");
    }

    //初始化游戏界面
    public void initializeGameInterface() {
        imageLabel1 = createLabelWithIconAndBounds(icon1, 50, 700, 100, 100);
        imageLabel2 = createLabelWithIconAndBounds(icon2, 140, 700, 100, 100);
        imageLabel3 = createLabelWithIconAndBounds(icon3, 230, 700, 100, 100);
        imageLabel4 = createLabelWithIconAndBounds(icon4, 320, 700, 100, 100);
        imageLabel5 = createLabelWithIconAndBounds(icon5, 410, 700, 100, 100);
        imageLabel6 = createLabelWithIconAndBounds(icon6, 500, 700, 100, 100);
        imageLabel7 = createLabelWithIconAndBounds(icon7, 590, 700, 100, 100);
        imageLabel8 = createLabelWithIconAndBounds(icon8, 680, 700, 100, 100);
        imageLabel9 = createLabelWithIconAndBounds(icon9, 770, 700, 100, 100);
        imageLabel0 = createLabelWithIconAndBounds(icon0, 860, 700, 100, 100);

        imageLabelPlus = createLabelWithIconAndBounds(iconPlus, 250, 800, 100, 100);
        imageLabelMinus = createLabelWithIconAndBounds(iconMinus, 360, 800, 100, 100);
        imageLabelMultiply = createLabelWithIconAndBounds(iconMultiply, 470, 800, 100, 100);
        imageLabelDivision = createLabelWithIconAndBounds(iconDivision, 580, 800, 100, 100);
        imageLabelDelete = createLabelWithIconAndBounds(iconDelete, 50, 800, 200, 100);
        imageLabelEqual = createLabelWithIconAndBounds(iconEqual, 690, 800, 100, 100);
        imageLabelEnter = createLabelWithIconAndBounds(iconEnter, 790, 800, 200, 100);

        emptyLabels = new JLabel[6][7];  // 6行7列的二维数组，表示这个游戏可以存放的图标最多是6行7列
        //创建一个Empty按钮图片对象
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 7; i++) {
                // 创建一个JLable对象
                emptyLabels[j][i] = new JLabel(iconEmpty);
                emptyLabels[j][i].setBounds(80 + i * 120, j * 110, 100, 100);
                // 把管理容器添加到界面
                this.getContentPane().add(emptyLabels[j][i]);
            }
        }
    }

    // 初始化开始新游戏按钮
    public void initializeStartNewGameButton() {
        startNewGameButton = new JButton("New Game");
        startNewGameButton.setBounds(900, 50, 120, 40);
        startNewGameButton.setEnabled(false);
        startNewGameButton.addActionListener(controller.new StartNewGameListener());
        this.getContentPane().add(startNewGameButton);
    }

    //创建带有图标和指定边界的 JLabel 对象。
    private JLabel createLabelWithIconAndBounds(ImageIcon icon, int x, int y, int width, int height) {
        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        label.addMouseListener(controller.new ImageClickListener());
        this.getContentPane().add(label);
        return label;
    }



    // 检查用户输入是否匹配目标方程，返回一个匹配状态数组
    private int[] checkMatchStatusForEquation(String targetEquation, JLabel[][] inputLabels) {
        // 将用户输入的图标转换为对应的字符数组
        char[] userInput = new char[inputLabels[controller.numberOfRowDuringGame].length];
        for (int i = 0; i < inputLabels[controller.numberOfRowDuringGame].length; i++) {
            JLabel label = inputLabels[controller.numberOfRowDuringGame][i];
            userInput[i] = getCharacterFromImageLabel(label);
        }

        // 将目标方程转换为字符数组
        char[] targetArray = targetEquation.toCharArray();

        // 初始化匹配状态数组
        int[] matchArray = new int[userInput.length];

        // 检查每个图标是否与目标方程的对应字符相匹配
        for (int i = 0; i < userInput.length; i++) {
            if (i < targetArray.length && userInput[i] == targetArray[i]) {
                matchArray[i] = ICON_COLOR_GREEN;
            } else if (i < targetArray.length && targetEquation.contains(String.valueOf(userInput[i]))) {
                matchArray[i] = ICON_COLOR_ORANGE;
            } else {
                matchArray[i] = ICON_COLOR_GREY;
            }
        }
        return matchArray;
    }

    // 设置相对应的imageLabel颜色
    public void setColorForImageLabel(char correspondingCharacter, String color) {
        switch (correspondingCharacter) {
            case '1':
                imageLabel1.setIcon(getColoredIconForCharacter('1', color));
                break;
            case '2':
                imageLabel2.setIcon(getColoredIconForCharacter('2', color));
                break;
            case '3':
                imageLabel3.setIcon(getColoredIconForCharacter('3', color));
                break;
            case '4':
                imageLabel4.setIcon(getColoredIconForCharacter('4', color));
                break;
            case '5':
                imageLabel5.setIcon(getColoredIconForCharacter('5', color));
                break;
            case '6':
                imageLabel6.setIcon(getColoredIconForCharacter('6', color));
                break;
            case '7':
                imageLabel7.setIcon(getColoredIconForCharacter('7', color));
                break;
            case '8':
                imageLabel8.setIcon(getColoredIconForCharacter('8', color));
                break;
            case '9':
                imageLabel9.setIcon(getColoredIconForCharacter('9', color));
                break;
            case '0':
                imageLabel0.setIcon(getColoredIconForCharacter('0', color));
                break;
            case '+':
                imageLabelPlus.setIcon(getColoredIconForCharacter('+', color));
                break;
            case '-':
                imageLabelMinus.setIcon(getColoredIconForCharacter('-', color));
                break;
            case '*':
                imageLabelMultiply.setIcon(getColoredIconForCharacter('*', color));
                break;
            case '/':
                imageLabelDivision.setIcon(getColoredIconForCharacter('/', color));
                break;
            case '=':
                imageLabelEqual.setIcon(getColoredIconForCharacter('=', color));
                break;
        }
    }

    // 获取JLabel对应的数字或符号字符
    public char getCharacterFromImageLabel(JLabel label) {
        if (label.getIcon() == icon0) {
            return '0';
        } else if (label.getIcon() == icon1) {
            return '1';
        } else if (label.getIcon() == icon2) {
            return '2';
        } else if (label.getIcon() == icon3) {
            return '3';
        } else if (label.getIcon() == icon4) {
            return '4';
        } else if (label.getIcon() == icon5) {
            return '5';
        } else if (label.getIcon() == icon6) {
            return '6';
        } else if (label.getIcon() == icon7) {
            return '7';
        } else if (label.getIcon() == icon8) {
            return '8';
        } else if (label.getIcon() == icon9) {
            return '9';
        } else if (label.getIcon() == iconPlus) {
            return '+';
        } else if (label.getIcon() == iconMinus) {
            return '-';
        } else if (label.getIcon() == iconMultiply) {
            return '*';
        } else if (label.getIcon() == iconDivision) {
            return '/';
        } else if (label.getIcon() == iconEqual) {
            return '=';
        }
        return '\0';  // 如果没有匹配的图标，返回空字符
    }

    // 根据点击的按钮返回对应的图标
    public ImageIcon getIconFromImageLabel(JLabel sourceLabel) {
        if (sourceLabel == imageLabel1) {
            return icon1;
        } else if (sourceLabel == imageLabel2) {
            return icon2;
        } else if (sourceLabel == imageLabel3) {
            return icon3;
        } else if (sourceLabel == imageLabel4) {
            return icon4;
        } else if (sourceLabel == imageLabel5) {
            return icon5;
        } else if (sourceLabel == imageLabel6) {
            return icon6;
        } else if (sourceLabel == imageLabel7) {
            return icon7;
        } else if (sourceLabel == imageLabel8) {
            return icon8;
        } else if (sourceLabel == imageLabel9) {
            return icon9;
        } else if (sourceLabel == imageLabel0) {
            return icon0;
        } else if (sourceLabel == imageLabelPlus) {
            return iconPlus;
        } else if (sourceLabel == imageLabelMinus) {
            return iconMinus;
        } else if (sourceLabel == imageLabelMultiply) {
            return iconMultiply;
        } else if (sourceLabel == imageLabelDivision) {
            return iconDivision;
        } else if (sourceLabel == imageLabelEqual) {
            return iconEqual;
        } else if (sourceLabel == imageLabelDelete) {
            return iconDelete;
        } else if (sourceLabel == imageLabelEnter) {
            return iconEnter;
        }

        return null;  // 如果没有匹配的按钮，返回null
    }


    // 获取指定颜色的图标
    private ImageIcon getColoredIconForCharacter(char correspondingCharacter, String color) {
        String filePath = "image/" + color + "/";
        switch (correspondingCharacter) {
            case '0':
                return new ImageIcon(filePath + "0.png");
            case '1':
                return new ImageIcon(filePath + "1.png");
            case '2':
                return new ImageIcon(filePath + "2.png");
            case '3':
                return new ImageIcon(filePath + "3.png");
            case '4':
                return new ImageIcon(filePath + "4.png");
            case '5':
                return new ImageIcon(filePath + "5.png");
            case '6':
                return new ImageIcon(filePath + "6.png");
            case '7':
                return new ImageIcon(filePath + "7.png");
            case '8':
                return new ImageIcon(filePath + "8.png");
            case '9':
                return new ImageIcon(filePath + "9.png");
            case '+':
                return new ImageIcon(filePath + "plus.png");
            case '-':
                return new ImageIcon(filePath + "minus.png");
            case '*':
                return new ImageIcon(filePath + "multiply.png");
            case '/':
                return new ImageIcon(filePath + "division.png");
            case '=':
                return new ImageIcon(filePath + "equal.png");
            default:
                return null;
        }
    }

    // 获取绿色图标
    public ImageIcon getGreenIcon(JLabel sourceLabel) {
        // 根据源标签获取对应的数字或符号字符
        char correspondingCharacter = getCharacterFromImageLabel(sourceLabel);

        // 根据字符获取对应的绿色图标
        return getColoredIconForCharacter(correspondingCharacter, "Green");
    }

    // 获取灰色图标
    public ImageIcon getGreyIcon(JLabel sourceLabel) {
        // 根据源标签获取对应的数字或符号字符
        char correspondingCharacter = getCharacterFromImageLabel(sourceLabel);

        // 根据字符获取对应的灰色图标
        return getColoredIconForCharacter(correspondingCharacter, "Grey");
    }

    // 获取橙色图标
    public ImageIcon getOrangeIcon(JLabel sourceLabel) {
        // 根据源标签获取对应的数字或符号字符
        char correspondingCharacter = getCharacterFromImageLabel(sourceLabel);

        // 根据字符获取对应的橙色图标
        return getColoredIconForCharacter(correspondingCharacter, "Orange");
    }


}
