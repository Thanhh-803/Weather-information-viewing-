package weather_UDP;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServerWeather {
    
    static byte[] bufferIn = new byte[1024];

    public static void main(String[] args) {
        try {
        	//Bước 1. Khởi tao đối tượng DatagramSocket để gửi và nhận gói tin UDP
            DatagramSocket serverSocket = new DatagramSocket(2000);
            System.out.println("Đã kết nối!!!");
            while (true) {
            	// Khởi tạo đối tượng DatagramPacket để lưu trữ yêu cầu được nhận từ Client
                DatagramPacket receivePacket = new DatagramPacket(bufferIn, bufferIn.length);
                serverSocket.receive(receivePacket);
                // Lấy địa chỉ của máy nhận
                InetAddress clientAddress = InetAddress.getByName("localhost");
                

                String request = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                System.out.println("Nhận yêu cầu từ Client: " + request);

                String message = xuLyYeuCau(request);
                byte[] data = message.getBytes("UTF-8");
                // Khởi tạo đối tương DatagramPacket để gửi
                DatagramPacket Out = new DatagramPacket(data, data.length, clientAddress, 2001);
                serverSocket.send(Out);

                System.out.println("Gửi phản hồi cho Client: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String xuLyYeuCau(String request) {
    	
        String city = request.trim();

        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\Weather\\weather_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 8 && city.equalsIgnoreCase(parts[0].trim())) {
                	String ngay = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); // Ngày hiện tại
                    String nhietDo = parts[1].trim();
                    String doAm = parts[2].trim();
                    String gio = parts[3].trim();
                    String tamNhin = parts[4].trim();
                    String khiAp = parts[5].trim();
                    String may = parts[6].trim();
                    String trangThai = parts[7].trim();
                    
                    return city + "," 
                    	+ ngay + "," 
                    	+ nhietDo + "," 
                    	+ doAm + "," 
                    	+ gio + "," 
                    	+ tamNhin + "," 
                    	+ khiAp + "," 
                    	+ may + "," 
                    	+ trangThai;
                }
            }
        } catch (IOException e) {
            return "Đã xảy ra lỗi khi xử lý dữ liệu thời tiết.";
        }

        return "Không tìm thấy dữ liệu thời tiết cho thành phố " + city + ".";
    }
}