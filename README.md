# 🎬 FlickFind 

## Cách sử dụng

Đây là các file source code (.kt) cho dự án FlickFind.
Vì Android Studio không thể import một folder code lẻ như một project hoàn chỉnh,
bạn cần tạo project mới rồi copy code vào theo hướng dẫn dưới đây.

---

## Bước 1 — Tạo project mới trong Android Studio

1. Mở Android Studio → **New Project** → **Empty Activity**
2. Điền thông tin:
   - Name: `FlickFind`
   - Package name: `com.example.flickfind`
   - Language: `Kotlin`
   - Min SDK: `API 26`
3. Nhấn **Finish** và chờ Gradle sync

---

## Bước 2 — Copy các file code

Sau khi project tạo xong, copy các file sau vào đúng vị trí:

```
app/src/main/java/com/example/flickfind/
├── MainActivity.kt                        ← Copy đè file có sẵn
├── ui/
│   ├── auth/
│   │   ├── AuthUiState.kt                 ← Tạo mới
│   │   ├── AuthViewModel.kt               ← Tạo mới
│   │   └── AuthScreen.kt                  ← Tạo mới
│   ├── home/
│   │   └── HomeScreen.kt                  ← Tạo mới
│   └── theme/
│       └── Theme.kt                       ← Copy đè file có sẵn
```

---

## Bước 3 — Thêm dependency vào build.gradle.kts

Mở file `app/build.gradle.kts`, tìm khối `dependencies` và thêm 2 dòng này:

```kotlin
// Icons extended
implementation("androidx.compose.material:material-icons-extended:1.7.0")

// ViewModel cho Compose
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
```

Sau đó nhấn **Sync Now**.

---

## Bước 4 — Chạy ứng dụng

Nhấn **Run** (▶) hoặc Shift+F10

### Tài khoản test mặc định:
| Trường     | Giá trị              |
|------------|----------------------|
| Email      |abc@flickfind.com   |
| Mật khẩu  | vinhhoanggay              |

---

## Cấu trúc project

```
AuthUiState.kt   → Data class mô tả trạng thái UI (bất biến)
AuthViewModel.kt → Xử lý logic, validate, giữ dữ liệu qua xoay màn hình
AuthScreen.kt    → Giao diện Compose (chỉ hiển thị, không có logic)
HomeScreen.kt    → Màn hình sau khi đăng nhập thành công
MainActivity.kt  → Điểm vào ứng dụng, điều hướng đơn giản
```

## Luồng dữ liệu (UDF)

```
Người dùng nhấn nút
        ↓  (Event)
  AuthViewModel  ←── xử lý, validate, cập nhật _uiState
        ↓  (State - AuthUiState)
   AuthScreen    ←── collectAsState(), recompose, hiển thị kết quả
```
