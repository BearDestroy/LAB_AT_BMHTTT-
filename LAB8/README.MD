# LAB 8 - Local DNS Attack: DNS Spoofing và Cache Poisoning trên mô hình pfSense

## 1. Thông tin sinh viên

- **Họ và tên**: Ngô Quang Chương
- **Mã số SV**: 1150080087
- **Tên bài thực hành**: LAB8 - Local DNS Attack

## 2. Máy móc và phần mềm sử dụng

- **Hệ điều hành/máy ảo:**
  - pfSense - thêm Adapter 5, OPT DNSLAB 10.9.0.1/24
  - DNS-SERVER - Ubuntu Server 24.04 LTS, BIND9 - IP 10.9.0.10
  - VICTIM - Ubuntu 24.04 Desktop/Server - IP 10.9.0.100
  - ATTACKER - Kali Linux - IP 10.9.0.200
  - APP-TEST/AUTH-DNS - authoritative DNS mô phỏng - IP 172.16.6.20
  - DMZ-WEB - web hợp lệ - IP 172.16.6.10
  - Máy thật: Win11 quản trị pfSense qua WebGUI 192.168.56.1

- **Phần mềm chạy máy ảo:** VMWare.

- **Cấu hình mạng:** Dựng thêm mạng DNS-LAB 10.9.0.0/24 riêng phía sau pfSense. Cô lập khỏi LAN-DOMAIN và MGMT bằng ruleset Block+Log. Chỉ cho phép DNS-SERVER truy vấn AUTH-DNS mô phỏng và Victim truy cập DMZ-WEB hợp lệ.

- **Phần mềm sử dụng:** pfSense, BIND9, Netplan, Scapy, Wireshark, tcpdump, Python.

## 3. Các việc đã làm và kết quả

- **Chuẩn bị snapshot và dựng mạng DNS-LAB trên pfSense:** Tạo snapshot. Thêm Adapter 5 cho pfSense. Gán interface OPT, đặt IPv4 tĩnh 10.9.0.1/24.
  → **Kết quả: PASS**

- **Tạo ruleset cô lập DNSLAB trên pfSense:** Pass DNS-SERVER → AUTH-DNS. Pass DNS-LAB → DMZ-WEB. Block+Log DNSLAB → LAN-DOMAIN. Block+Log DNSLAB → MGMT. Block+Log DNSLAB → Any.
  → **Kết quả: PASS**

- **Gắn card DNS-LAB cho các VM:** Dùng NAT tạm để cài gói trên DNS-SERVER/VICTIM/Kali, sau đó chuyển hẳn sang Internal Network dns-lab. Kali đặt Promiscuous Mode = Allow VMs.
  → **Kết quả: PASS**

- **Cấu hình DNS-SERVER (BIND9):** Đặt IP tĩnh 10.9.0.10/24. Cài BIND9 và công cụ kiểm thử. Chép cấu hình chuẩn. Kiểm tra bằng named-checkconf/named-checkzone và khởi động lại thành công.
  → **Kết quả: PASS**

- **Cấu hình VICTIM và ATTACKER:** VICTIM dùng Netplan trỏ DNS về 10.9.0.10. Kali cài Scapy/Wireshark/tcpdump, dựng trang fake web mô phỏng.
  → **Kết quả: PASS**

- **Baseline và bắt gói DNS:** Khởi động AUTH-DNS mô phỏng trên APP-TEST. Xác nhận baseline www.external.lab8.test → 172.16.6.10 từ cả DNS-SERVER và Victim trước khi tấn công. Bắt gói trên Kali bằng Wireshark.
  → **Kết quả: PASS**

- **Task 1 - Thay đổi hosts file:** Sửa /etc/hosts trên Victim để redirect www.lab8.test sang 10.9.0.200. curl/trình duyệt hiển thị trang mô phỏng của Kali. dig @10.9.0.10 vẫn trả 172.16.6.10. Khôi phục hosts file sau khi quan sát.
  → **Kết quả: PASS**

- **Task 2 - Direct DNS Spoofing:** Attacker quan sát truy vấn Victim → DNS-SERVER và gửi phản hồi DNS giả trực tiếp tới Victim. Victim tạm thời nhận 10.9.0.200. Minh chứng bằng Wireshark với query, legitimate response, forged response, transaction ID và UDP client port khớp.
  → **Kết quả: PASS**

- **Task 3 - DNS Cache Poisoning:** Xóa sạch cache DNS-SERVER trước thử nghiệm. Attacker giả mạo phản hồi từ AUTH-DNS gửi tới resolver trước phản hồi hợp lệ. Bản ghi giả được cache trong TTL. Xác nhận bằng rndc dumpdb và kiểm tra named_dump.db.
  → **Kết quả: PASS**

- **Khôi phục môi trường sau tấn công:** Dừng script spoof và fake web server trên Kali. Flush cache BIND9 và cache resolver trên Victim. Khôi phục /etc/hosts. Dừng AUTH-DNS mô phỏng. Trả Promiscuous Mode của Kali về Deny. Xác nhận lại www.external.lab8.test → 172.16.6.10.
  → **Kết quả: PASS**

- **Bài tập phòng thủ với DNS COOKIE:** Gỡ cấu hình send-cookie no khỏi server AUTH-DNS mô phỏng, khôi phục DNS COOKIE mặc định, restart BIND9 và lặp lại Task 3 để so sánh hiệu quả.
  → **Kết quả: PASS**

## 4. Trả lời câu hỏi phân tích

1. **Tại sao /etc/hosts có thể ảnh hưởng trình duyệt nhưng dig @DNS-SERVER vẫn trả kết quả DNS gốc?** → Kết quả: PASS
2. **Những trường nào của DNS/UDP phải khớp để một phản hồi giả có cơ hội được chấp nhận?** → Kết quả: PASS
3. **Tại sao Task 2 chỉ ảnh hưởng Victim hiện tại, trong khi Task 3 có thể ảnh hưởng nhiều client dùng cùng resolver?** → Kết quả: PASS
4. **Vai trò của TTL trong cache poisoning là gì?** → Kết quả: PASS
5. **Tại sao VMWare Promiscuous Mode = Allow VMs cần thiết trong bài này?** → Kết quả: PASS
6. **Vì sao LAB 8 dùng mạng DNSLAB riêng thay vì tấn công trực tiếp DC01 DNS của LAN-DOMAIN?** → Kết quả: PASS
7. **Khác nhau giữa authoritative DNS, recursive resolver và caching resolver?** → Kết quả: PASS
8. **DNSSEC giải quyết vấn đề gì và vì sao zone .test unsigned trong LAB 8 không tự được bảo vệ?** → Kết quả: PASS
9. **Tại sao chỉ chặn UDP/53 là chưa đủ nếu muốn kiểm soát DNS trong mạng hiện đại?** → Kết quả: PASS
10. **Nêu tối thiểu bốn biện pháp hardening sau khi hoàn thành bài.** → Kết quả: PASS
