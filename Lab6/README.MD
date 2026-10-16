## 1. Thông tin sinh viên

- **Họ và tên**: Ngô Quang Chương
- **Mã số SV**: 1150080087
- **Tên bài thực hành**: LAB6 - pfSense: LDAP, Captive Portal, Alias, Schedule và Firewall Rules
- **Tên file Word gốc**: 11THMT-Lab6_1150080087-NgoQuangChuong.docx

## 2. Máy móc và phần mềm sử dụng

- **Hệ điều hành/máy ảo:**
  - pfSense-LAB6 (4 interface: WAN, LAN, DMZ, MGMT)
  - DC01 - Windows Server 2019 (Active Directory, DNS) - IP 10.6.0.10
  - Client-A, Client-B (subnet 10.6.0.0/24) Win11
  - Máy chủ web trong DMZ (JOB_SITE / SOCIAL_SITE / RESTRICTED_SITE, dải 172.16.6.x) - Kali Linux
  - Máy thật: Win11 để setting Pfsense

- **Phần mềm chạy máy ảo:** VMWare.

- **Cấu hình mạng:** pfSense-LAB6 dùng 4 adapter VMWare, topology riêng, cô lập với LAB 5.

- **Phần mềm sử dụng:** pfSense (Firewall, Alias, Schedule, Captive Portal, Authentication Server LDAP), Active Directory.

## 3. Các việc đã làm và kết quả

- **Tình huống 0 (cấu hình chung):** Cấu hình LDAP Authentication Server (bind bằng service account, Search Scope Entire Subtree), test đăng nhập sv_a và sv_b, bật Captive Portal trên LAN, tạo Alias và Schedule dùng cho các rule.
  - LDAP test sv_a:
  - LDAP test sv_b:
  - Captive Portal login: `
  - Alias:
  - Schedule:
    → **Kết quả: PASS**

- **Tình huống 1:** Tạo rule cho phép truy cập 172.16.6.10 và chặn truy cập 172.16.6.20, kiểm thử, kiểm tra Firewall Log và State Table.
  - Rule:
  - Kiểm thử vào được 172.16.6.10:
  - Kiểm thử không vào được 172.16.6.20:
  - Firewall System Log:
  - State Table:
    → **Kết quả: PASS**

- **Tình huống 2:** Tạo rule và kiểm thử trên cả Client A và Client B, kiểm tra Firewall Log và State Table.
  - Rules:
  - Kiểm thử Client A:
  - Kiểm thử Client B:
  - Firewall Log:
  - State Table:
    → **Kết quả: PASS**

- **Tình huống 3:** Tạo rule Pass có Schedule (`LUNCH_WINDOW`) cho Client-B tới SOCIAL_SITE, kèm rule Block SOCIAL_SITE bên dưới để chặn rõ ràng ngoài giờ nghỉ; kiểm thử, kiểm tra Firewall Log và State Table.
  - Rules:
  - Kiểm thử:
  - Firewall Log:
  - State Table:
    → **Kết quả: PASS**

- **Tình huống 4:** Tạo rule theo yêu cầu tình huống 4, kiểm thử, kiểm tra Firewall Log và State Table.
  - Rules:
  - Kiểm thử:
  - Firewall Log:
  - State Table:
    → **Kết quả: PASS**

- **Tình huống 5:** Tạo rule và kiểm thử cả hai chiều Client A → DMZ và DMZ → Client A, kiểm tra Firewall Log.
  - Rules: `images/th5-rules.png`
  - Kiểm thử Client A → DMZ: `images/th5-test-client-a-to-dmz.png`
  - Kiểm thử DMZ → Client A: `images/th5-test-dmz-to-client-a.png`
  - Firewall Log: `images/th5-firewall-log.png`
    → **Kết quả: PASS**

- **Cuối cùng:** Xuất cấu hình tại Diagnostics → Backup & Restore → Download configuration, nộp kèm `config.xml` cuối cùng.
  → **Kết quả: PASS**

## 4. Lỗi vặt lúc làm và cách sửa

- **Lỗi 1:** : Mặc dù trong Event Viewer Security tài khoản mật khẩu đăng nhập thành công trong khi đăng nhập authentication trên pfsense t hì báo thất bại sử dụng command hiển thị lỗi stronger authentication sửa 8 tiếng không xong nên em quyết định hạ phiên bản Windown Server 2026 xuống 2019
  - **Cách sửa:** Down version xuống Windown 2019

- **Lỗi 2:** Reset State loading gần 5p không xóa state
  - **Cách sửa:** Em không sửa được, em tiến hành check bằng status -> firewall -> thành công hoặc không.
