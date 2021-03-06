package com.ukpatel.layouts;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    private JTextField playerName;
    private JTextField serverAddress;
    private JTextField serverPortNo;
    private JButton connect;

    public InfoPanel() {
        try {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JLabel gameTitle = new JLabel("Tic Tac Tae", SwingConstants.CENTER);
            gameTitle.setFont(new Font("Lemon", Font.BOLD, 40));
            gameTitle.setAlignmentX(CENTER_ALIGNMENT);
            gameTitle.setAlignmentY(CENTER_ALIGNMENT);
            this.add(gameTitle, BorderLayout.NORTH);

            // Getting information from user panel.
            JPanel getInfo = new JPanel();
            getInfo.setLayout(new BoxLayout(getInfo, BoxLayout.Y_AXIS));
            getInfo.setAlignmentX(CENTER_ALIGNMENT);
            getInfo.setAlignmentY(CENTER_ALIGNMENT);
            getInfo.setPreferredSize(new Dimension(500, 350));

            // Player 1 information Panel.
            JPanel playerNameInfo = new JPanel();

            JLabel playerNameLabel = new JLabel("Name :  ");
            playerNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
            playerNameInfo.add(playerNameLabel);

            playerName = new JTextField("");
            playerName.setFont(new Font("Arial", Font.PLAIN, 18));
            playerName.setPreferredSize(new Dimension(170, 30));
            playerNameInfo.add(playerName);

            // Server Host Information.
            JPanel serverHostInfo = new JPanel();

            JLabel labelServerHostInfo = new JLabel("Host Name/IPV4 Address : ");
            labelServerHostInfo.setFont(new Font("Arial", Font.BOLD, 18));
            serverHostInfo.add(labelServerHostInfo);

            serverAddress = new JTextField("");
            serverAddress.setPreferredSize(new Dimension(170, 30));
            serverAddress.setFont(new Font("Arial", Font.PLAIN, 18));
            serverHostInfo.add(serverAddress);

            // Server Port Information.
            JPanel serverPortInfo = new JPanel();

            JLabel labelPortInfo = new JLabel("Port No. : ");
            labelPortInfo.setFont(new Font("Arial", Font.BOLD, 18));
            serverPortInfo.add(labelPortInfo);

            serverPortNo = new JTextField("");
            serverPortNo.setPreferredSize(new Dimension(170, 30));
            serverPortNo.setFont(new Font("Arial", Font.PLAIN, 18));
            serverPortInfo.add(serverPortNo);

            // Add all information panel into main panel(getInfo)
            getInfo.add(playerNameInfo);
            getInfo.add(serverHostInfo);
            getInfo.add(serverPortInfo);

            // Button for connecting the server.
            JPanel connectPanel = new JPanel();

            connect = new JButton("Connect");
            connect.setFont(new Font("Arial", Font.BOLD, 20));
            connect.setAlignmentX(CENTER_ALIGNMENT);
            connectPanel.add(connect);

            this.add(Box.createRigidArea(new Dimension(10, 90)));
            this.add(getInfo);
            this.add(Box.createRigidArea(new Dimension(10, 100)));
            this.add(connectPanel);
            this.add(Box.createRigidArea(new Dimension(10, 30)));
        } catch (Exception e) {
            System.out.println(e);
        }

        setInfo();
    }

    private void setInfo() {
        serverAddress.setText("ukpatel");
        serverPortNo.setText("3334");
    }

    public String getPlayerName() {
        return this.playerName.getText();
    }

    public String getServerAddress() {
        return this.serverAddress.getText();
    }

    public String getServerPortNo() {
        return this.serverPortNo.getText();
    }

    public JButton getConnectButton() {
        return this.connect;
    }
}
