package weather_UDP;

import javax.swing.*;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientWeather extends JFrame {

    private static DatagramSocket clientSocket = null;
    private static byte[] bufferIn = new byte[1024];
    private static byte[] data = null;
    private static int serverPort = 2000;

    JLabel titleLabel;
    JLabel chooseCityLabel;
    JButton searchButton;
    JComboBox<String> cityComboBox;
    JLabel vNLabel;
    JLabel nhietDoLabel;
    JLabel doAmLabel;
    JLabel tocDoGioLabel;
    JLabel trangThaiLabel;
    JLabel tinhLable;
    JLabel khiApLabel;
    JLabel tamNhinLabel;
    JLabel matDoMayLabel;
    JLabel date;

    public ClientWeather() {
        setTitle("Thông tin thời tiết ở Việt Nam");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Thời tiết Việt Nam");
        titleLabel.setFont(new Font("Urdu Typesetting", Font.BOLD, 35));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        chooseCityLabel = new JLabel("Chọn Thành Phố:");
        chooseCityLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        cityComboBox = new JComboBox<>();
        chonCityTuFile("D:\\Weather\\weather_data.txt"); 
        cityComboBox.setFont(new Font("Times New Roman", Font.ITALIC, 15));

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				layThoiTiet();
			}
		});
        searchButton.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        vNLabel = new JLabel("Việt Nam");
        vNLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        nhietDoLabel = new JLabel();
        nhietDoLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        doAmLabel = new JLabel();
        doAmLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        tocDoGioLabel = new JLabel();
        tocDoGioLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        trangThaiLabel = new JLabel();
        trangThaiLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        tinhLable = new JLabel();
        tinhLable.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        khiApLabel = new JLabel();
        khiApLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        tamNhinLabel = new JLabel();
        tamNhinLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        matDoMayLabel = new JLabel();
        matDoMayLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        date = new JLabel();
        date.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(titleLabel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(chooseCityLabel);
        bottomPanel.add(cityComboBox);
        bottomPanel.add(searchButton);
        bottomPanel.add(vNLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        infoPanel.setPreferredSize(new Dimension(700, 300));

        JPanel firstRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        firstRowPanel.add(Box.createRigidArea(new Dimension(80, 0)));
        firstRowPanel.add(tinhLable);
        firstRowPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        firstRowPanel.add(date);
        firstRowPanel.add(Box.createRigidArea(new Dimension(130, 0)));
        firstRowPanel.add(nhietDoLabel);
        

        JPanel secondRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        secondRowPanel.add(Box.createRigidArea(new Dimension(80, 0)));
        secondRowPanel.add(doAmLabel);
        secondRowPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        secondRowPanel.add(tocDoGioLabel);
        secondRowPanel.add(Box.createRigidArea(new Dimension(150, 0)));
        secondRowPanel.add(khiApLabel);
        
        
        JPanel thirdRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        thirdRowPanel.add(Box.createRigidArea(new Dimension(80, 0)));
        thirdRowPanel.add(matDoMayLabel);
        thirdRowPanel.add(Box.createRigidArea(new Dimension(80, 0)));
        thirdRowPanel.add(tamNhinLabel);
        thirdRowPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        thirdRowPanel.add(trangThaiLabel);
        

        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(firstRowPanel);
        infoPanel.add(secondRowPanel);
        infoPanel.add(thirdRowPanel);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        setSize(750, 450);
        setLocationRelativeTo(null);
    }

    private void chonCityTuFile(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String city = data[0];
                cityComboBox.addItem(city);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void connectToServer() {
        try {
        	// Khởi tạo đối tượng DatagramSocket để gửi và nhận gói tin UDP
            clientSocket = new DatagramSocket(2001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    private void sendData(String selectedCity) {
        try {
            byte[] data = selectedCity.getBytes("UTF-8");
            // Khơit tạo 1 DatagramPacket để gửi yêu cầu 
            DatagramPacket gui = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), serverPort);
            clientSocket.send(gui);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String receiveData() {
        try {
        	// Khởi tạo DatagramPacket để nhận gói tin từ Server
            DatagramPacket nhan = new DatagramPacket(bufferIn, bufferIn.length);
            clientSocket.receive(nhan);
            return new String(nhan.getData(), 0, nhan.getLength(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void layThoiTiet() {
        String chonCity = (String) cityComboBox.getSelectedItem();
        try {
            connectToServer();
            sendData(chonCity);
            String weather = receiveData();
            
            
            String[] weatherInfo = weather.split(",");

            if (weatherInfo.length >= 9) {
            	tinhLable.setText("Tỉnh: " + weatherInfo[0]);
                date.setText("Ngày: " + weatherInfo[1]);
                
                nhietDoLabel.setText("Nhiệt độ: " + weatherInfo[2]);
                doAmLabel.setText("Độ ẩm: " + weatherInfo[3]);
                tocDoGioLabel.setText("Gió: " + weatherInfo[4]);
                tamNhinLabel.setText("Tầm nhìn: " + weatherInfo[5]);
                khiApLabel.setText("Khí áp: " + weatherInfo[6]);
                matDoMayLabel.setText("Mật độ mây: " + weatherInfo[7]);
                trangThaiLabel.setText("Trạng thái: " + weatherInfo[8]);
            } else {
            	tinhLable.setText("Tỉnh: N/A");
                date.setText("Ngày: N/A");
                
                nhietDoLabel.setText("Nhiệt độ: N/A");
                doAmLabel.setText("Độ ẩm: N/A");
                tocDoGioLabel.setText("Gió: N/A");
                tamNhinLabel.setText("Tầm nhìn: N/A");
                khiApLabel.setText("Khí áp: N/A");
                matDoMayLabel.setText("Mật độ mây: N/A");
                trangThaiLabel.setText("Trạng thái: N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWeather().setVisible(true);
            }
        });
    }
}