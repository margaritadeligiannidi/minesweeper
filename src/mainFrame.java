import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;


public class mainFrame extends javax.swing.JFrame {
    boolean gameOver=false;
    int tileSize=50; 
    int rows=12; 
    int columns=rows;
    int width=columns*tileSize;
    int height=tileSize*rows;
    mineTile [][] board=new mineTile[rows][columns];  //ta tiles gia na exw tis theseis tous
    int clicks=0; 
    int totalMines=10; 
    int totalFlags=totalMines;

    //ICONS
    ClassLoader classLoader = mainFrame.class.getClassLoader();
    ImageIcon bombIcon = new ImageIcon(classLoader.getResource("bomb-icon.png"));
    ImageIcon boomIcon=new ImageIcon(classLoader.getResource("boom-icon.png"));
    ImageIcon flagIcon=new ImageIcon(classLoader.getResource("flag2.png"));
    ImageIcon winnerIcon=new ImageIcon(classLoader.getResource("icons8-winner-48.png"));    
    ImageIcon gameOverIcon=new ImageIcon(classLoader.getResource("gameOver.png")); 
    ImageIcon number1=new ImageIcon(classLoader.getResource("number1.png")); 
    ImageIcon number2=new ImageIcon(classLoader.getResource("number2.png")); 
    ImageIcon number3=new ImageIcon(classLoader.getResource("number3.png")); 
    ImageIcon number4=new ImageIcon(classLoader.getResource("number4.png")); 
    ImageIcon number5=new ImageIcon(classLoader.getResource("number5.png")); 
    ImageIcon number6=new ImageIcon(classLoader.getResource("number6.png")); 
    ImageIcon number7=new ImageIcon(classLoader.getResource("number7.png")); 
    ImageIcon number8=new ImageIcon(classLoader.getResource("number8.png")); 

    ImageIcon []numberIcons={number1,number2,number3,number4,number5,number6,
         number7,number8};
    
    /**
     * Creates new form mainFrame
     */
    public mainFrame() {        
        initComponents();
        setUp();
        
        //set the text of the control labels
        paragraphLabel.setText("<html>A number of mines is randomly placed based on the difficulty mode. Numbered<br>"
                                   + "tiles show how many mines touch them. Clicking an empty tile reveals all<br>"
                                   + "connected empty tiles at once. If you hit a mine you lose! If you reveal all<br>"
                                   + "tiles that are not mines you win!<html>");
     
        creditLabel.setText("CREDITS: All icons are from https://icons8.com"); 
        
        //sets visible the correct panel
        startPanel.setVisible(true);
        gamePanel.setVisible(false);
        controlsPanel.setVisible(false);
    }
    
    
    public void setUp(){    
        matrixPanel.setLayout(new GridLayout(rows,columns,0,0));
         //gemisma tou matrix
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
              mineTile t=new mineTile(i,j);
              board[i][j]=t;
              t.setText(" ");
              t.setIcon(null);
              t.setBackground(Color.BLUE);
              t.setIsMine(false);
              t.setIsFlag(false);
              //t.setEnabled(true);
              t.setIsClickable(true);
              t.setFocusPainted(false);
              t.setPreferredSize(new Dimension(50,50));
              t.setHorizontalTextPosition(SwingConstants.CENTER);
              t.setVerticalTextPosition(SwingConstants.CENTER);

               //-----------TILE EVENTS-----------//
              t.addMouseListener(new MouseAdapter(){
                 
                   public void mousePressed(MouseEvent e){
                      mineTile triggered=(mineTile)e.getSource();  //poio ekane trigger
                      if(gameOver==false){
                      //left click
                      if(e.getButton()==MouseEvent.BUTTON1){
                          leftClick(triggered);
                          
                      } //right click
                      else if (e.getButton()==MouseEvent.BUTTON3){
                          rightClick(triggered);
                      }
                    }
                  }
                   
                   //----MATRIX MOUSE HOVER EVENTS---
            public void mouseEntered(MouseEvent e) {
               mineTile triggered = (mineTile)e.getSource();
               if(triggered.getIsClickable() && gameOver==false)
               triggered.setBackground(new Color(0,153,255));
            }

           public void mouseExited(MouseEvent e) {
              mineTile triggered= (mineTile)e.getSource();
              if(triggered.getIsClickable() && gameOver==false)
              triggered.setBackground(Color.BLUE);
           }
                   
            });
              matrixPanel.add(t);
                  }
            } 
        setMines(board);
    }  
    
    
    
     //method that sets mines random on matrix
    public void setMines(mineTile board[][] ){
       Random r=new Random();
       int count=0;
       while(count<totalMines){
           int randomR=r.nextInt(rows);
           int randomC=r.nextInt(columns);
           if(board[randomR][randomC].getIsMine()==false){
               board[randomR][randomC].setIsMine(true);
               count++;
           }
       }
    }//end of setmines
    
    
     //otan paththei to left click
    public void leftClick(mineTile triggered){
        
         if(triggered.getIsClickable()==true && triggered.getIsFlag()==false){
            if(triggered.getIsMine()==true){
                triggered.setIsClickable(false);
                //triggered.setEnabled(false);
                triggered.setBackground(Color.RED);
                gameOver=true;
                revealMines(triggered.getR(),triggered.getC());
                gameOver();
            }
            else {
                checkMines(triggered.getR(),triggered.getC());
            }
        }
    }//end of left click
    
    
     //reveals all the mines
    public void revealMines(int r,int c){
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(board[i][j].getIsMine()==true){ 
                    if(i==r && j==c){
                     board[i][j].setIcon(boomIcon);
                     //board[i][j].setEnabled(false);
                     board[i][j].setIsClickable(false);
                    }
                    else{
                    board[i][j].setIcon(bombIcon);
                    //board[i][j].setEnabled(false);
                    board[i][j].setIsClickable(false);
                    board[i][j].setBackground(Color.LIGHT_GRAY);
                    }
                }
            }
        } //end of revealmine
    }
    
    
    
     public void checkMines(int r,int c){
        
          if(r<0 || r>=rows || c<0 || c>=columns){  //an den exei geitona
            return ;
        }
        
        mineTile t=board[r][c];
        int mineCount=0;
        
        if(t.getIsClickable()==false){  //exw ksana episkeftei to button auto
            return;
        }
        
        //t.setEnabled(false);
        t.setIsClickable(false);
        t.setBackground(Color.LIGHT_GRAY);
        clicks++;
        
        //elegxos gia tous 8 geitones
        mineCount+=countOfMines(r-1,c-1);
        mineCount+=countOfMines(r-1,c);
        mineCount+=countOfMines(r-1,c+1);
        mineCount+=countOfMines(r,c+1);
        mineCount+=countOfMines(r,c-1);
        mineCount+=countOfMines(r+1,c-1);
        mineCount+=countOfMines(r+1,c);
        mineCount+=countOfMines(r+1,c+1);
     

        if(mineCount<=0){
            if(t.getIsFlag()){
                totalFlags++;
                t.setIsFlag(false);
                flagLabel.setText(Integer.toString(totalFlags));
            }
            
        t.setText(" ");
        t.setIcon(null);
        checkMines(r-1,c-1);
        checkMines(r-1,c);
        checkMines(r-1,c+1);
         checkMines(r,c+1);
        checkMines(r,c-1);
        checkMines(r+1,c-1);
        checkMines(r+1,c);
        checkMines(r+1,c+1);
      
        }
        else{
               if(t.getIsFlag()){
                totalFlags++;
                t.setIsFlag(false);
                flagLabel.setText(Integer.toString(totalFlags));
               }
               
            t.setIcon(numberIcons[mineCount-1]);
            //t.setText(Integer.toString(mineCount));  
        }

          if (clicks == rows *columns - totalMines) {
            gameOver = true;
            revealMines(r,c);
            winner();
        }
  
    }//end of checkmines
     
     
     
      public int countOfMines(int r,int c){
        if(r<0 || r>=rows || c<0 || c>=columns){  //an den exei geitona
            return 0;
        }
        
        if(board[r][c].getIsMine()){ //an o geitonas einai mine
            return 1;
        }
        
        return 0;
    }
      
      
      
    //PLACE THE FLAGS
   public void rightClick(mineTile triggered){

       if(gameOver==false){
       if(triggered.getIsFlag()==false && triggered.getIsClickable() && totalFlags>0 ){
           triggered.setIcon(flagIcon);
           triggered.setIsFlag(true);
           totalFlags--;
           flagLabel.setText(Integer.toString(totalFlags));
       }
       else if(triggered.getIsFlag()==true  ){
           if(easy.isSelected() && totalFlags<10){
           triggered.setIcon(null);
           triggered.setIsFlag(false);
           totalFlags++;
           flagLabel.setText(Integer.toString(totalFlags));
          }
           else if(medium.isSelected() && totalFlags<20){
           triggered.setIcon(null);
           triggered.setIsFlag(false);
           totalFlags++;
           flagLabel.setText(Integer.toString(totalFlags));   
           }
           else if(hard.isSelected() && totalFlags<30){
           triggered.setIcon(null);
           triggered.setIsFlag(false);
           totalFlags++;
           flagLabel.setText(Integer.toString(totalFlags)); 
          }
       }
   }
   }

   //GAME OVER
   public void gameOver(){       
       int minesFound=0;
       for(int i=0;i<rows;i++){
           for(int j=0;j<columns;j++){
               if(board[i][j].getIsMine() && board[i][j].getIsFlag()){
                   minesFound++;
               }
           }
       }  
        JOptionPane loser=new JOptionPane();
        loser.showMessageDialog(this,"Better luck next time!\nMines found: "+minesFound,"GAME OVER",
        JOptionPane.PLAIN_MESSAGE,gameOverIcon); 
   }
   
   
   public void winner(){
       JOptionPane winner=new JOptionPane();
       winner.showMessageDialog(this,"You win!\nAll mines cleared!","WINNER",
       JOptionPane.PLAIN_MESSAGE,winnerIcon);
   }
   
   //RESTART
   public void restart(){
         if(easy.isSelected()){
            totalMines=10;
       }
       else if(medium.isSelected()){
           totalMines=20;
       }
       else{
           totalMines=30;
       }
 
     gameOver=false;
     clicks=0;
     totalFlags=totalMines;
     flagLabel.setText(" "+totalFlags+" ");
     mineLabel.setText(" "+totalMines+" ");
 
     for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
              board[i][j].setText(" ");
              board[i][j].setBackground(Color.BLUE);
              board[i][j].setIcon(null);
              board[i][j].setIsMine(false);
              board[i][j].setIsFlag(false);
              //board[i][j].setEnabled(true);
              board[i][j].setIsClickable(true);
            }  
        }
       setMines(board);
   }
        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mode = new javax.swing.ButtonGroup();
        gamePanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        easy = new javax.swing.JRadioButton();
        medium = new javax.swing.JRadioButton();
        hard = new javax.swing.JRadioButton();
        restartButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        controlsButton = new javax.swing.JButton();
        matrixPanel = new javax.swing.JPanel();
        textPanel = new javax.swing.JPanel();
        mineLabel = new javax.swing.JLabel();
        flagLabel = new javax.swing.JLabel();
        controlsPanel = new javax.swing.JPanel();
        controls = new javax.swing.JPanel();
        rightClickP = new javax.swing.JPanel();
        rightClickL = new javax.swing.JLabel();
        leftClickP = new javax.swing.JPanel();
        leftClickLabel = new javax.swing.JLabel();
        rules = new javax.swing.JPanel();
        paragraph = new javax.swing.JPanel();
        paragraphLabel = new javax.swing.JLabel();
        ruleTitle = new javax.swing.JPanel();
        ruleTitleLabel = new javax.swing.JLabel();
        credits = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        creditP = new javax.swing.JPanel();
        creditLabel = new javax.swing.JLabel();
        startPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MINESWEEPER");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        gamePanel.setLayout(new java.awt.BorderLayout());

        menuPanel.setBackground(new java.awt.Color(255, 255, 255));

        easy.setBackground(new java.awt.Color(255, 255, 255));
        mode.add(easy);
        easy.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        easy.setSelected(true);
        easy.setText("Easy");
        easy.setToolTipText("Easy");
        easy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        easy.setFocusPainted(false);
        easy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                easyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                easyMouseExited(evt);
            }
        });
        easy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                easyActionPerformed(evt);
            }
        });
        menuPanel.add(easy);

        medium.setBackground(new java.awt.Color(255, 255, 255));
        mode.add(medium);
        medium.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        medium.setText("Medium");
        medium.setToolTipText("Medium");
        medium.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        medium.setFocusPainted(false);
        medium.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mediumMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mediumMouseExited(evt);
            }
        });
        medium.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mediumActionPerformed(evt);
            }
        });
        menuPanel.add(medium);

        hard.setBackground(new java.awt.Color(255, 255, 255));
        mode.add(hard);
        hard.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        hard.setText("Hard");
        hard.setToolTipText("Hard");
        hard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hard.setFocusPainted(false);
        hard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hardMouseExited(evt);
            }
        });
        hard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hardActionPerformed(evt);
            }
        });
        menuPanel.add(hard);

        restartButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        restartButton.setText("RESTART");
        restartButton.setToolTipText("Restart");
        restartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        restartButton.setFocusPainted(false);
        restartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                restartButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                restartButtonMouseExited(evt);
            }
        });
        restartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButtonActionPerformed(evt);
            }
        });
        menuPanel.add(restartButton);

        aboutButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        aboutButton.setText("ABOUT");
        aboutButton.setToolTipText("About");
        aboutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        aboutButton.setFocusPainted(false);
        aboutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                aboutButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                aboutButtonMouseExited(evt);
            }
        });
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });
        menuPanel.add(aboutButton);

        controlsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons8-mouse-16.png"))); // NOI18N
        controlsButton.setToolTipText("Controls");
        controlsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        controlsButton.setFocusPainted(false);
        controlsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                controlsButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                controlsButtonMouseExited(evt);
            }
        });
        controlsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                controlsButtonActionPerformed(evt);
            }
        });
        menuPanel.add(controlsButton);

        gamePanel.add(menuPanel, java.awt.BorderLayout.NORTH);

        matrixPanel.setBackground(new java.awt.Color(255, 255, 255));
        matrixPanel.setLayout(new java.awt.GridLayout(1, 0));
        gamePanel.add(matrixPanel, java.awt.BorderLayout.CENTER);

        textPanel.setBackground(new java.awt.Color(255, 255, 255));

        mineLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        mineLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons8-bomb-48.png"))); // NOI18N
        mineLabel.setText(" 10  ");
        mineLabel.setToolTipText("bomb icon");
        textPanel.add(mineLabel);

        flagLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        flagLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons8-destination.gif"))); // NOI18N
        flagLabel.setText(" 10  ");
        flagLabel.setToolTipText("flag icon");
        textPanel.add(flagLabel);

        gamePanel.add(textPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(gamePanel, "card5");

        controlsPanel.setBackground(new java.awt.Color(204, 255, 255));
        controlsPanel.setPreferredSize(new java.awt.Dimension(650, 650));
        controlsPanel.setLayout(new java.awt.BorderLayout());

        controls.setBackground(new java.awt.Color(255, 255, 255));
        controls.setLayout(new javax.swing.BoxLayout(controls, javax.swing.BoxLayout.LINE_AXIS));

        rightClickP.setBackground(new java.awt.Color(255, 255, 255));

        rightClickL.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        rightClickL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons8-right-click-80.png"))); // NOI18N
        rightClickL.setText("Place flag");
        rightClickL.setToolTipText("right click");

        javax.swing.GroupLayout rightClickPLayout = new javax.swing.GroupLayout(rightClickP);
        rightClickP.setLayout(rightClickPLayout);
        rightClickPLayout.setHorizontalGroup(
            rightClickPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
            .addGroup(rightClickPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(rightClickPLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(rightClickL)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        rightClickPLayout.setVerticalGroup(
            rightClickPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 406, Short.MAX_VALUE)
            .addGroup(rightClickPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(rightClickPLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(rightClickL)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        controls.add(rightClickP);

        leftClickP.setBackground(new java.awt.Color(255, 255, 255));

        leftClickLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        leftClickLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons8-left-click-80.png"))); // NOI18N
        leftClickLabel.setText("Uncover tile");
        leftClickLabel.setToolTipText("left click");

        javax.swing.GroupLayout leftClickPLayout = new javax.swing.GroupLayout(leftClickP);
        leftClickP.setLayout(leftClickPLayout);
        leftClickPLayout.setHorizontalGroup(
            leftClickPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
            .addGroup(leftClickPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(leftClickPLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(leftClickLabel)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        leftClickPLayout.setVerticalGroup(
            leftClickPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 406, Short.MAX_VALUE)
            .addGroup(leftClickPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(leftClickPLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(leftClickLabel)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        controls.add(leftClickP);

        controlsPanel.add(controls, java.awt.BorderLayout.CENTER);

        rules.setBackground(new java.awt.Color(255, 255, 255));
        rules.setLayout(new java.awt.BorderLayout());

        paragraph.setBackground(new java.awt.Color(255, 255, 255));

        paragraphLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        paragraphLabel.setText("rule paragraph");

        javax.swing.GroupLayout paragraphLayout = new javax.swing.GroupLayout(paragraph);
        paragraph.setLayout(paragraphLayout);
        paragraphLayout.setHorizontalGroup(
            paragraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
            .addGroup(paragraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(paragraphLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(paragraphLabel)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        paragraphLayout.setVerticalGroup(
            paragraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
            .addGroup(paragraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(paragraphLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(paragraphLabel)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        rules.add(paragraph, java.awt.BorderLayout.SOUTH);

        ruleTitle.setBackground(new java.awt.Color(255, 255, 255));

        ruleTitleLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ruleTitleLabel.setText("RULES");

        javax.swing.GroupLayout ruleTitleLayout = new javax.swing.GroupLayout(ruleTitle);
        ruleTitle.setLayout(ruleTitleLayout);
        ruleTitleLayout.setHorizontalGroup(
            ruleTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
            .addGroup(ruleTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ruleTitleLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(ruleTitleLabel)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        ruleTitleLayout.setVerticalGroup(
            ruleTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(ruleTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ruleTitleLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(ruleTitleLabel)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        rules.add(ruleTitle, java.awt.BorderLayout.NORTH);

        controlsPanel.add(rules, java.awt.BorderLayout.NORTH);

        credits.setBackground(new java.awt.Color(255, 255, 255));
        credits.setLayout(new java.awt.BorderLayout());

        buttonPanel.setBackground(new java.awt.Color(255, 255, 255));

        backButton.setBackground(new java.awt.Color(0, 153, 255));
        backButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText("BACK");
        backButton.setToolTipText("Back");
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backButton.setFocusPainted(false);
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButtonMouseExited(evt);
            }
        });
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
            .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(buttonPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(backButton)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(buttonPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(backButton)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        credits.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        buttonPanel.getAccessibleContext().setAccessibleDescription("");

        creditP.setBackground(new java.awt.Color(255, 255, 255));

        creditLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        creditLabel.setText("CREDITS");
        creditP.add(creditLabel);

        credits.add(creditP, java.awt.BorderLayout.NORTH);

        controlsPanel.add(credits, java.awt.BorderLayout.SOUTH);

        getContentPane().add(controlsPanel, "card4");

        startPanel.setBackground(new java.awt.Color(255, 255, 255));
        startPanel.setPreferredSize(new java.awt.Dimension(650, 650));
        startPanel.setLayout(null);

        titleLabel.setBackground(new java.awt.Color(255, 255, 255));
        titleLabel.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(0, 0, 255));
        titleLabel.setText("MINESWEEPER");
        titleLabel.setToolTipText("minesweeper");
        startPanel.add(titleLabel);
        titleLabel.setBounds(180, 140, 280, 70);

        startButton.setBackground(new java.awt.Color(0, 0, 255));
        startButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        startButton.setForeground(new java.awt.Color(255, 255, 255));
        startButton.setText("START");
        startButton.setToolTipText("Start");
        startButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        startButton.setFocusPainted(false);
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButtonMouseExited(evt);
            }
        });
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        startPanel.add(startButton);
        startButton.setBounds(280, 370, 80, 50);

        quitButton.setBackground(new java.awt.Color(0, 0, 255));
        quitButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        quitButton.setForeground(new java.awt.Color(255, 255, 255));
        quitButton.setText("QUIT");
        quitButton.setToolTipText("Quit");
        quitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        quitButton.setFocusPainted(false);
        quitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quitButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quitButtonMouseExited(evt);
            }
        });
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });
        startPanel.add(quitButton);
        quitButton.setBounds(280, 440, 80, 50);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flag2.png"))); // NOI18N
        startPanel.add(jLabel1);
        jLabel1.setBounds(460, 160, 30, 30);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/flagMirror.png"))); // NOI18N
        startPanel.add(jLabel3);
        jLabel3.setBounds(140, 160, 30, 30);

        getContentPane().add(startPanel, "card4");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    //-----------------BUTTON EVENTS------------------
    private void easyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_easyActionPerformed
        restart();
    }//GEN-LAST:event_easyActionPerformed

    private void mediumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mediumActionPerformed
       restart();
    }//GEN-LAST:event_mediumActionPerformed

    private void hardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hardActionPerformed
      restart();
    }//GEN-LAST:event_hardActionPerformed

    private void restartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButtonActionPerformed
       restart();
    }//GEN-LAST:event_restartButtonActionPerformed

    //------ABOUT------
    private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed
        JOptionPane about=new JOptionPane();
        about.setBackground(Color.green);
        about.showMessageDialog(this,"Όνομα: Μαργαρίτα\nΕπώνυμο: Δεληγιαννίδη\nΗμερομηνία: 2023","ABOUT",
        JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_aboutButtonActionPerformed

    private void controlsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_controlsButtonActionPerformed
        controlsPanel.setVisible(true); 
        gamePanel.setVisible(false);
        startPanel.setVisible(false);
    }//GEN-LAST:event_controlsButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        gamePanel.setVisible(true);
        controlsPanel.setVisible(false);
        startPanel.setVisible(false);
    }//GEN-LAST:event_backButtonActionPerformed

    
    
    //---------------MOUSE HOVER EVENTS----------------------------------
    private void restartButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_restartButtonMouseEntered
        restartButton.setBackground(new Color(0,153,255));
        restartButton.setForeground(Color.white);

    }//GEN-LAST:event_restartButtonMouseEntered

    private void restartButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_restartButtonMouseExited
        restartButton.setBackground(new JButton().getBackground());
        restartButton.setForeground(Color.black);

    }//GEN-LAST:event_restartButtonMouseExited

    private void aboutButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutButtonMouseEntered
        aboutButton.setBackground(new Color(0,153,255));
        aboutButton.setForeground(Color.white);

    }//GEN-LAST:event_aboutButtonMouseEntered

    private void aboutButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutButtonMouseExited
        aboutButton.setBackground(new JButton().getBackground());
        aboutButton.setForeground(Color.black);

    }//GEN-LAST:event_aboutButtonMouseExited

    private void controlsButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controlsButtonMouseEntered
       controlsButton.setBackground(new Color(0,153,255));
       controlsButton.setForeground(Color.white);

    }//GEN-LAST:event_controlsButtonMouseEntered

    private void controlsButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controlsButtonMouseExited
       controlsButton.setBackground(new JButton().getBackground());
       controlsButton.setForeground(Color.black);

    }//GEN-LAST:event_controlsButtonMouseExited

    private void easyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_easyMouseEntered
        easy.setForeground(Color.BLUE);
    }//GEN-LAST:event_easyMouseEntered

    private void mediumMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mediumMouseEntered
        medium.setForeground(Color.BLUE);
    }//GEN-LAST:event_mediumMouseEntered

    private void hardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hardMouseEntered
        hard.setForeground(Color.BLUE);
    }//GEN-LAST:event_hardMouseEntered

    private void easyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_easyMouseExited
        easy.setForeground(Color.BLACK);
    }//GEN-LAST:event_easyMouseExited

    private void mediumMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mediumMouseExited
        medium.setForeground(Color.BLACK);
    }//GEN-LAST:event_mediumMouseExited

    private void hardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hardMouseExited
        hard.setForeground(Color.BLACK);
    }//GEN-LAST:event_hardMouseExited

    private void backButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backButtonMouseEntered
        backButton.setBackground(Color.BLUE);
    }//GEN-LAST:event_backButtonMouseEntered

    private void backButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backButtonMouseExited
       backButton.setBackground(new Color(0,153,255));
    }//GEN-LAST:event_backButtonMouseExited

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
       gamePanel.setVisible(true);
       startPanel.setVisible(false);
       controlsPanel.setVisible(false);
    }//GEN-LAST:event_startButtonActionPerformed

    
    private void startButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseEntered
       startButton.setBackground(new Color(0,153,255));
    }//GEN-LAST:event_startButtonMouseEntered

    private void startButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseExited
        startButton.setBackground(Color.BLUE);
    }//GEN-LAST:event_startButtonMouseExited

    private void quitButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quitButtonMouseEntered
        quitButton.setBackground(new Color(0,153,255));
    }//GEN-LAST:event_quitButtonMouseEntered

    private void quitButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quitButtonMouseExited
        quitButton.setBackground(Color.BLUE);   
    }//GEN-LAST:event_quitButtonMouseExited

    
    //CLOSING THE APP
    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitButtonActionPerformed

   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
      
       
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {                
               mainFrame main=new mainFrame();
               main.setVisible(true);
               
               main.setSize(650,650);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutButton;
    private javax.swing.JButton backButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel controls;
    private javax.swing.JButton controlsButton;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JLabel creditLabel;
    private javax.swing.JPanel creditP;
    private javax.swing.JPanel credits;
    private javax.swing.JRadioButton easy;
    private javax.swing.JLabel flagLabel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JRadioButton hard;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel leftClickLabel;
    private javax.swing.JPanel leftClickP;
    private javax.swing.JPanel matrixPanel;
    private javax.swing.JRadioButton medium;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JLabel mineLabel;
    private javax.swing.ButtonGroup mode;
    private javax.swing.JPanel paragraph;
    private javax.swing.JLabel paragraphLabel;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton restartButton;
    private javax.swing.JLabel rightClickL;
    private javax.swing.JPanel rightClickP;
    private javax.swing.JPanel ruleTitle;
    private javax.swing.JLabel ruleTitleLabel;
    private javax.swing.JPanel rules;
    private javax.swing.JButton startButton;
    private javax.swing.JPanel startPanel;
    private javax.swing.JPanel textPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
