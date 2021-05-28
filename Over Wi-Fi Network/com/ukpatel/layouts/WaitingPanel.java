package com.ukpatel.layouts;

import javax.swing.*;
import java.awt.*;

public class WaitingPanel extends JPanel implements Runnable {

    private boolean isRunning = true;
    private JLabel waitingLabel;
    private Thread t;

    public static final String WAITING_TEXT = "Waiting for another player.";

    public WaitingPanel() {
        this.setLayout(new GridBagLayout());

        waitingLabel = new JLabel(WAITING_TEXT);
        waitingLabel.setFont(new Font("Arial", Font.BOLD, 25));
        this.add(waitingLabel);

        t = new Thread(this, "Waiting");
        t.start();
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                waitingLabel.setText(WAITING_TEXT + ".");
                Thread.sleep(1000);
                waitingLabel.setText(WAITING_TEXT + "..");
                Thread.sleep(1000);
                waitingLabel.setText(WAITING_TEXT + "...");
                Thread.sleep(1000);
                waitingLabel.setText(WAITING_TEXT);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        }

    }

    public void setRunningThreadFalse() {
        isRunning = false;
    }
}
