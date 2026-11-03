# LAB 10 - Remote DNS Cache Poisoning: Tấn công đầu độc cache DNS từ xa trên mô hình pfSense

## 1. Thông tin sinh viên

- **Họ và tên**: Ngô Quang Chương
- **Mã số SV**: 1150080087
- **Lớp**: 11THMT
- **Tên bài thực hành**: LAB10 - Remote DNS Cache Poisoning
- **Ngày thực hành**: 03/11/2026

## 2. Máy móc và phần mềm sử dụng

- **Hệ điều hành/máy ảo:**
  - pfSense - 6 interface: WAN (le0), MGMT (le1), LAN (le2), DMZ (le3), DNSLAB (le4) 10.9.0.1/24, ATTACKNET (le5) 10.10.0.1/24 (thêm mới)
  - DNS-SERVER - Ubuntu Server, BIND9 + `vuln_resolver_lab10.py` (resolver có chủ đích dễ tổn thương, cố định source port 33333) - IP 10.9.0.10
  - VICTIM - Ubuntu - IP 10.9.0.100
  - ATTACKER - Kali Linux (ATTACKER-KALI), nằm ở mạng ATTACKNET, gateway 10.10.0.1 - IP 10.10.0.200
  - APP-TEST/AUTH-DNS - authoritative DNS mô phỏng (`auth_dns_relay_lab10.py`, lắng nghe UDP/53 và cổng relay UDP/5300) - IP 172.16.6.20
  - DMZ-WEB - web hợp lệ - IP 172.16.6.10
  - Máy thật: Win11 quản trị pfSense qua WebGUI 192.168.56.254

- **Phần mềm chạy máy ảo:** VMWare Workstation.

- **Cấu hình mạng:** Giữ mạng DNSLAB 10.9.0.0/24 (DNS-SERVER, VICTIM) và dựng thêm mạng ATTACKNET 10.10.0.0/24 riêng phía sau pfSense dành cho Kali. Kali rời khỏi DNSLAB, đặt Promiscuous Mode = Deny nên không thể sniff lưu lượng DNS giữa DNS-SERVER và AUTH-DNS, mô phỏng đúng kịch bản tấn công từ xa. Ruleset Block+Log cô lập cả hai mạng khỏi MGMT (192.168.56.0/24) và 10.6.0.0/24; chỉ mở các luồng cần thiết cho bài lab.

- **Phần mềm sử dụng:** pfSense, BIND9, Python 3 (script resolver, AUTH-DNS relay, fake web), tcpdump, resolvectl, rndc, curl.

## 3. Các việc đã làm và kết quả

- **Dựng mạng ATTACKNET trên pfSense:** Thêm adapter mới cho pfSense, gán interface ATTACKNET (le5), đặt IPv4 tĩnh 10.10.0.1/24. Kali chuyển sang mạng ATTACKNET, IP 10.10.0.200/24, default route qua 10.10.0.1.
  → **Kết quả: PASS**

- **Tạo ruleset trên pfSense:**
  - DNSLAB: Pass VICTIM 10.9.0.100 → DNS-SERVER 10.9.0.10 (TCP/UDP 53). Pass DNS-SERVER → AUTH-DNS 172.16.6.20 (UDP 53). Pass DNS-SERVER → Kali 10.10.0.200 (UDP 53). Pass DNSLAB → Kali 10.10.0.200 (TCP 80). Block+Log DNSLAB → 10.6.0.0, 192.168.56.0 và Any.
  - ATTACKNET: Pass Kali → DNS-SERVER 10.9.0.10 (TCP/UDP 53). Pass Kali → AUTH-DNS 172.16.6.20 (UDP 5300). Block+Log ATTACKNET → 10.6.0.0, 192.168.56.0 và Any.
  → **Kết quả: PASS**

- **Cấu hình DNS-SERVER:** Kiểm tra cấu hình bằng `named-checkconf`, khởi động lại BIND9, xác nhận named lắng nghe trên 10.9.0.10:53 bằng `ss -lunp | grep :53`.
  → **Kết quả: PASS**

- **Baseline trước tấn công:** Dừng BIND9/systemd-resolved trên APP-TEST, chạy AUTH-DNS mô phỏng trên 172.16.6.20:53 (`www.example.lab10.test` → 172.16.6.10, tên ngẫu nhiên trả NXDOMAIN sau 3.0s). Xác nhận VICTIM `resolvectl query www.example.lab10.test` → 172.16.6.10.
  → **Kết quả: PASS**

- **Chứng minh attacker không sniff được DNSLAB:** Chạy `tcpdump -ni any udp port 53` trên Kali trong lúc VICTIM truy vấn DNS. Kali không bắt được gói nào của DNS-SERVER ↔ AUTH-DNS, nên không biết Transaction ID và source port của truy vấn.
  → **Kết quả: PASS**

- **Remote poisoning (log PENDING/HIT/CACHE):** Kali kích hoạt DNS-SERVER truy vấn tên ngẫu nhiên dưới `example.lab10.test`. AUTH-DNS ghi nhận `[PENDING]` (giữ truy vấn 3.0s) rồi nhận danh sách đoán TXID qua cổng relay 5300 (`[HIT] batch 53248-57343 contains txid=55315`). Phản hồi giả kèm delegation `ns.evil.example.lab10.test` + glue A 10.10.0.200 được chấp nhận. DNS-SERVER ghi `[CACHE] delegation poisoned: example.lab10.test. NS ns.evil.example.lab10.test. -> 10.10.0.200`, các truy vấn sau của `www.example.lab10.test` bị chuyển về Kali. Lặp lại thành công ở vòng khác (txid=52784, batch 49152-53247).
  → **Kết quả: PASS**

- **Bắt gói tin xác nhận:** Chạy `tcpdump -ni any udp port 33333` trên DNS-SERVER, thấy truy vấn `A? ricuy8s4pif.example.lab10.test` tới 172.16.6.20, sau đó DNS-SERVER chuyển truy vấn `www.example.lab10.test` sang 10.10.0.200:53 và nhận về `A 10.10.0.200`.
  → **Kết quả: PASS**

- **Kết quả phía VICTIM sau poison:** Flush cache bằng `resolvectl flush-caches`. `resolvectl query www.example.lab10.test` trả 10.10.0.200. `curl http://www.example.lab10.test` hiển thị trang "LAB 10 - Fake Site" mô phỏng vô hại trên Kali (không có form đăng nhập, không thu thập dữ liệu).
  → **Kết quả: PASS**

- **Khôi phục môi trường và hardening:** Dừng `vuln_resolver_lab10.py`, khởi động lại BIND9 (`active (running)`), dừng AUTH-DNS relay mô phỏng. `rndc flush` xóa cache. Kiểm tra lại bằng `tcpdump host 172.16.6.20 and udp port 53`: tên ngẫu nhiên (test1, test2...) nhận NXDOMAIN bình thường, không còn bản ghi bị đầu độc. Kali không còn quyền recursion tới DNS-SERVER.
  → **Kết quả: PASS**

## 4. Trả lời câu hỏi phân tích

1. **Vì sao LAB 10 buộc Kali rời DNSLAB và đặt Promiscuous Mode = Deny?** → Kết quả: PASS
2. **Trong remote poisoning, attacker thiếu thông tin nào so với LAB trước?** → Kết quả: PASS
3. **Nếu chỉ có transaction ID 16-bit, xác suất một batch K giá trị chứa TXID đúng là bao nhiêu?** → Kết quả: PASS
4. **Source-port randomization làm tăng không gian đoán như thế nào?** → Kết quả: PASS
5. **Vì sao dùng tên ngẫu nhiên ở mỗi vòng giúp vượt qua cache effect?** → Kết quả: PASS
6. **Bản ghi NS và glue A có vai trò gì trong việc đổi hướng resolver?** → Kết quả: PASS
7. **Vì sao Additional record ngoài bailiwick có thể bị resolver từ chối?** → Kết quả: PASS
8. **Tại sao LAB 10 dùng .test thay cho website thật?** → Kết quả: PASS
9. **Phân biệt tác dụng của DNSSEC validation, recursion ACL và firewall segmentation.** → Kết quả: PASS
10. **Vì sao race relay không làm cho LAB 10 trở thành mô hình tấn công Internet thực tế?** → Kết quả: PASS
11. **Khi khôi phục BIND, vì sao Kali không nên còn quyền recursion?** → Kết quả: PASS
12. **Nêu tối thiểu bốn biện pháp hardening resolver production.** → Kết quả: PASS