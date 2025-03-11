package com.BreakTimeReminder;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainBTR {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ConstructBTRMainJFrame mainObj = new ConstructBTRMainJFrame();  // Construct BTR main JFrame object
		
	}

}

class ConstructBTRMainJFrame extends JFrame{
	public ConstructBTRMainJFrame() {
		JLabel titleLable= new JLabel("Break Time Reminder App" , SwingConstants.CENTER);
		titleLable.setFont(new Font("Serif",Font.PLAIN,25));
		
		// Timer Label
        JLabel timerLabel = new JLabel("Set the timer: ");
        timerLabel.setFont(new Font("Serif", Font.PLAIN, 20));


        // Dropdown Menu (ComboBox) for minutes
        String[] minutes = new String[11]; // (10, 15, 20, ..., 60)
        for (int i = 0, value = 10; value <= 60; i++, value += 5) {
            minutes[i] = value + " min";
        }
        JComboBox<String> minuteDropdown = new JComboBox<>(minutes);
        
        // Radio Button for starting timer
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//Start a the timer in new JFrame by taking the value from dropdown menu
        		String selectedTime = (String) minuteDropdown.getSelectedItem();
                int minutes = Integer.parseInt(selectedTime.split(" ")[0]); // Extract numeric value
                new ConstructTimerFrame(minutes); // Start timer window
                dispose();
        	}
        }
        		);
        
        
        JButton exitButton1 = new JButton ("Exit");
        exitButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);// End the program i.e main class so the program also end cpu and ram
			}
        	
        });
		
		setLayout(null);  // null is used so the contents you want in the frame can place anywhere in the frame
		titleLable.setBounds(40, 10, 300, 40); // Centering the label
		add(titleLable);
		
		timerLabel.setBounds(50, 90, 120, 30);
        add(timerLabel);
        
        minuteDropdown.setBounds(180, 90, 100, 30);
        add(minuteDropdown);
		
        startButton.setBounds(50, 150, 100, 40);
		add(startButton);
		
		exitButton1.setBounds(200,150,100,40);
		add(exitButton1);
		
		
		setVisible(true);		// Setting the JFrame can visible
		setSize(400,300);		// Setting the JFrame size height and width
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// This ensure when you close the JFrame then the program also end
	}

}

// ConstructTimerFrame: Starts a countdown timer
class ConstructTimerFrame extends JFrame {
	private JLabel timerLabel;
	private Timer timer;
	private int secondsRemaining;
	private JButton back;
	private Clip clip;
	
	public ConstructTimerFrame(int minutes) {
		setTitle("Timer Window");
		setSize(350, 200);
		setLayout(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Display Timer
		timerLabel = new JLabel(minutes + ":00", SwingConstants.CENTER);
		timerLabel.setFont(new Font("Serif", Font.BOLD, 18));
		timerLabel.setBounds(30, 30, 280, 50);
		add(timerLabel);
		
		// Convert minutes to seconds
		secondsRemaining = minutes * 60;
		
		// Start countdown using Swing Timer (updates every 1 sec)
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				secondsRemaining--;
				int min = secondsRemaining / 60;
				int sec = secondsRemaining % 60;
				timerLabel.setText(String.format("%02d:%02d", min, sec));
				
				
				if(min > 0) {
					timerLabel.setText(String.format("Next break in %02d:%02d minutes", min, sec));
				}
				else {
					timerLabel.setText("Next break in " + sec + " seconds");
				}
				if (secondsRemaining <= 0) {
					timer.stop();
					playSound();
					JOptionPane.showMessageDialog(null, "Break Time!");
					stopSound();
					ConstructBTRMainJFrame mainframe=new ConstructBTRMainJFrame();
					mainframe.setVisible(true);
					dispose(); // Close timer window
					
				}
			}
		});
		

		// Stop Button
		JButton stopButton = new JButton("Stop");
		stopButton.setBounds(40, 90, 80, 30);
		stopButton.setFocusable(false);
		add(stopButton);

		stopButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (timer.isRunning()) {
		            timer.stop();
		            stopButton.setText("Start"); // Change text to "Start"
		        } else {
		            timer.start();
		            stopButton.setText("Stop"); // Change text back to "Stop"
		        }
		    }
		});

		// Reset Button
		JButton resetButton = new JButton("Reset");
		resetButton.setBounds(140, 90, 80, 30);
		resetButton.setFocusable(false);
		add(resetButton);

		resetButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        timer.stop(); // Stop current timer
		        secondsRemaining = minutes * 60; // Reset time
		        int min = secondsRemaining / 60;
		        timerLabel.setText("Next break in " + min + " minutes");
		        stopButton.setText("Stop"); // Reset Stop button text
		        timer.start(); // Start timer again
		    }
		});

		back = new JButton("← Back");
		back.setBounds(240, 90, 80, 30);
		back.setFocusable(false);
		add(back);

		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ConstructBTRMainJFrame mainframe=new ConstructBTRMainJFrame();
				mainframe.setVisible(true);
				dispose(); // Close timer window
			}
			
		});
		
		timer.start();
		setVisible(true);
	}
	
	// Function to play sound
	public void playSound() {
	    try {
	        URL soundURL = getClass().getResource("timer_ringtone.wav"); // Load from package
	        if (soundURL == null) {
	            throw new RuntimeException("Sound file not found!");
	        }

	        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
	        clip = AudioSystem.getClip();
	        clip.open(audioStream);
	        clip.start();
	    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
	        e.printStackTrace();
	    }
	}
	
	// Method to stop the sound
	public void stopSound() {
	    if (clip != null && clip.isRunning()) {
	        clip.stop();
	        clip.close();
	    }
	}
	
}








