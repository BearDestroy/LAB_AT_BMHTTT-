# LAB 7 - Xây dựng chính sách Web Protection trên mô hình pfSense + Web Proxy

## 1. Thông tin sinh viên

- **Họ và tên**: Ngô Quang Chương
- **Mã số SV**: 1150080087
- **Tên bài thực hành**: LAB7 - pfSense + Web Proxy (Squid, E2Guardian, ClamAV): Web Protection

## 2. Máy móc và phần mềm sử dụng

- **Hệ điều hành/máy ảo:**
  - pfSense-LAB6 LAN 10.6.0.1, DMZ 172.16.6.1, MGMT 192.168.56.254
  - DC01 - Windows Server 2019 (Active Directory, DNS) - IP 10.6.0.10
  - Client-A (10.6.0.21), Client-B (10.6.0.22) - Win11
  - WEB-PROXY - Ubuntu Server 24.04 LTS - IP 10.6.0.30
  - DMZ-WEB - Ubuntu - IP 172.16.6.10
  - Máy thật: Win11 để cấu hình pfSense qua WebGUI (192.168.56.1)

- **Phần mềm chạy máy ảo:** VMware Workstation.

- **Cấu hình mạng:** Dùng lại topology LAB 6. Client-A/B không được ra Web trực tiếp, bắt buộc đi qua proxy `10.6.0.30:8080`.

- **Phần mềm sử dụng:** pfSense (Alias, Firewall Rules, Captive Portal), Squid, E2Guardian, ClamAV, Nginx, DNS Server trên DC01.

## 3. Các việc đã làm và kết quả

- **Chuẩn bị WEB-PROXY và website mô phỏng:** Cài Squid, E2Guardian, ClamAV trên WEB-PROXY; dựng 13 virtual host Nginx trên DMZ-WEB; tạo DNS `lab7.test` trên DC01 trỏ về 172.16.6.10; cấu hình Manual Proxy trên Client-A/B.
  → **Kết quả: PASS**

- **Cấu hình pfSense ép lưu lượng Web qua proxy:** Tạo Alias (`LAB7_CLIENTS`, `LAB7_PROXY`, `WEB_PORTS`, `MAIL_PORTS`) và ruleset LAN theo thứ tự: Pass DNS → Pass client tới proxy TCP 8080 → Pass mail → Block client ra thẳng 80/443 → Pass proxy ra 80/443 → Pass proxy tới DMZ-WEB → Block LAN còn lại.
  → **Kết quả: PASS**

- **Kịch bản 1 - Baseline (normal.lab7.test) và chống bypass:** Truy cập `normal.lab7.test` qua proxy thành công; tắt proxy trên trình duyệt thì pfSense chặn direct 80/443; từ WEB-PROXY dùng `curl` vẫn truy cập được.
  → **Kết quả: PASS**

- **Kịch bản 2 - Geo-Sim (china / mideast):** Đưa `china.lab7.test`, `mideast.lab7.test` vào bannedsitelist và non-bypass list (hard block).
  → **Kết quả: PASS**

- **Kịch bản 3 - Job Search:** Hard block `jobs.lab7.test`.
  → **Kết quả: PASS**

- **Kịch bản 4 - Warning content (weapons / politics / religion):** Đưa vào bannedsitelist nhưng không nằm trong non-bypass list, đặt `bypass = 300`; người dùng xác nhận ở trang cảnh báo để mở tạm 5 phút.
  → **Kết quả: PASS**

- **Kịch bản 5 - Education:** Đưa `education.lab7.test`, `normal.lab7.test`, `download.lab7.test` vào exceptionsitelist; education luôn được phép, không cảnh báo.
  → **Kết quả: PASS**

- **Kịch bản 6 - Entertainment/Game theo thời gian:** Dùng Captive Portal (Hard Timeout) để giới hạn thời gian phiên, sau đó truy cập `games.lab7.test` trong phiên đăng nhập.
  → **Kết quả: PASS**

- **Kịch bản 7 - Streaming / Kịch bản 8 - Specific game:** `streaming.lab7.test` đặt cùng nhóm warning (bypass tạm); `blockedgame.lab7.test` đặt trong hard block (xem cấu hình ở kịch bản 2 và 4).
  → **Kết quả: PASS**

- **Kịch bản 9 - Antivirus (EICAR qua ClamAV):** Kiểm tra `clamav-daemon`, restart `e2guardian`, tải `eicar.com.txt` từ `download.lab7.test`, theo dõi log.
  → **Kết quả: CHƯA ĐẠT** (file EICAR vẫn hiển thị trên trình duyệt, chưa bị AV chặn - xem Lỗi 2)

- **Kịch bản 10 - Giới hạn file > 100 MB:** Cấu hình Squid `reply_body_max_size 101 MB`, kiểm thử tải `100MB.bin` và `110MB.bin`.`
  → **Kết quả: PASS**

- **Tùy biến trang block:** Dùng `reportinglevel = 3` với template HTML riêng, hiển thị "HE THONG AN NINH MANG LAB 7 - WEBSITE BI CHAN".
  → **Kết quả: PASS**

- **Log E2Guardian và Firewall:**
  - E2Guardian access log: `images/log-e2guardian.png`
  - pfSense Firewall Log (Status → System Logs → Firewall): `images/log-firewall-pfsense.png`
    → **Kết quả: PASS**

- **HTTPS inspection (mở rộng):** Không thực hiện.

## 4. Trả lời câu hỏi phân tích

1. **Vì sao LAB 7 không cài Squid/SquidGuard trực tiếp trên pfSense?** → Kết quả: PASS
2. **Firewall pfSense và Web Proxy khác nhau ở lớp nào?** → Kết quả: PASS
3. **Vì sao phải block direct TCP/80,443 từ Client-A/B?** → Kết quả: PASS
4. **Vì sao lọc theo IP không ổn định với website thật?** → Kết quả: PASS
5. **Temporary bypass khác hard block ở điểm nào?** → Kết quả: PASS
6. **Vì sao EICAR qua HTTPS không quét được nếu không có SSL MITM?** → Kết quả: PASS
7. **CA dùng cho HTTPS inspection tạo ra rủi ro gì nếu private key bị lộ?** → Kết quả: FAILED
8. **`maxcontentfilecachescansize` có phải luôn là hard download-size limit không?** → Kết quả: PASS
9. **Vì sao Schedule 60 phút không đồng nghĩa với daily category quota của Sophos?** → Kết quả: PASS
10. **Khi browser lỗi nhưng E2Guardian không có log, kiểm tra gì đầu tiên?** C→ Kết quả: PASS
